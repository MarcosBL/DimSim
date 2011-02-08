#include "ScreenDecoder.h"

namespace dm
{

	ScreenDecoder::ScreenDecoder(ScreenBuffer& buffer) : screenBuffer(buffer), tightDecoder(0)
	{
		resetAll();
		tightDecoder = new TightDecoder(*this);
		numRectsTotal = 0;
		resetAll();
	}
	ScreenDecoder::~ScreenDecoder()
	{
		if(tightDecoder)
		{
			delete tightDecoder;
		}
	}
	bool ScreenDecoder::decode(InputStream& in, ScreenDecoderCallback* cb)
	{
		DDSS_VERBOSE("ScreenDecoder")<<"!---------------------- DECODING : "<<in.getRemainingByteCount()<<" BYTES!!!!!!!!!!!"<<std::endl;
		callBack = cb;
		
		/*if(pendingData.getLength() > 0)
		{
			ByteBuffer bigOne(pendingData.getLength() + in.getRemainingByteCount());
			bigOne.copyFrom(pendingData.getData(),pendingData.getLength());
			in.read(bigOne.getData(pendingData.getLength()), bigOne.getLength() - pendingData.getLength());
			ByteBufferInputStream bigStream(&bigOne,false);
			pendingData.destroy();
			decode(bigStream,cb);
			return;
			
		}*/
		if(in.getRemainingByteCount() == 0)
		{
			//cb->errorOccured(-1,"end of input stream reached");
			return true;
		}
		else
		{
			switch(getState())
			{
			case SDS_EXPECTING_SERVER_CMD:
				decodeServerCmd(in);
				break;
			case SDS_EXPECTING_RECT_HEADER:
				decodeRectHeader(in);
				break;
			case SDS_EXPECTING_RECT_DATA:
				decodeRectData(in);
				break;
			case SDS_ERROR:
			case SDS_INVALID:
			default:
				DDSS_INPUT_ERROR("ScreenDec",in)<<"Invalid State : "<<getState()<<std::endl;
				return false;
			}
			
			if(in.getRemainingByteCount() == 0)
			{
				DDSS_INPUT_VERBOSE("ScreenDecoder",in)<<"Packet has been parsed completely!"<<std::endl;
				return getState() != SDS_ERROR;
			}
			else if(getState() == SDS_ERROR)
			{
				DDSS_WARNING("ScreenDecoder")<<"Error encountered in decode!"<<std::endl;
				//DDSS_DEBUG_EXIT();
				return false;
			}
			else
			{
				DDSS_INPUT_VERBOSE("ScreenDecoder",in)<<"Recursively calling decode!!!"<<std::endl;
				decode(in,cb);
			}
		}
		return true;
	}
	void ScreenDecoder::resetAll()
	{
		resetRect();
		serverCommand = 0xFF;
		numRectsMax = 0;
		numRectsCurrent = 0;
		markerExpected = false;
		setState(SDS_EXPECTING_SERVER_CMD);
	}
	void ScreenDecoder::resetRect()
	{
		RectHelper::resetRect(currentRect);
		setState(SDS_EXPECTING_RECT_HEADER);
	}
	void  ScreenDecoder::rectComplete(ScreenRectangle& r)
	{
		numRectsCurrent++;
		numRectsTotal++;
		DDSS_VERBOSE("ScreenDecoder")<<"[RECT-FOUND] Count ("<<numRectsCurrent<<"/"<<numRectsMax<<") "<<r<<std::endl;
		if(callBack)
		{
			callBack->rectComplete(r);
		}
		
		if(r.enc == RFB_ENCODING_LASTRECT)
		{
			resetRect();
			updateComplete(true);
		}
		else if(r.enc == RFB_ENCODING_COPYRECT)
		{
			screenBuffer.copy_rect(r);
			resetRect();
		}
		else if(r.enc == RFB_ENCODING_NEWFBSIZE)
		{
			screenBuffer.resize(r.w,r.h);
			if(callBack)
			{
				callBack->resizeReceived(r.w,r.h);
			}
			resetRect();
			updateComplete(true);
		}
		else if(numRectsCurrent == numRectsMax)
		{
			resetRect();
			updateComplete(false);
		}
		r.x = r.y = r.w = r.h = 0;
		r.enc = 0;
	}
	void  ScreenDecoder::updateComplete(bool markerFound)
	{
		DDSS_VERBOSE("Decoder")<<"[UPDATE-COMPLETE] rects="<<numRectsCurrent<<"/maxRects="<<numRectsMax<<"/marker found="<<(markerFound?"YES":"NO")<<std::endl;
		resetAll();
	}
	void  ScreenDecoder::errorOccured(int errorCode, const String errorMsg)
	{
		DDSS_ERROR("Decoder")<<"[RECT-ERROR] (Code="<<errorCode<<") "<<errorMsg<<std::endl;
		setState(SDS_ERROR);
	}
	
	bool ScreenDecoder::decodeServerCmd(InputStream& in)
	{
		resetAll();
		if(in.readByte(&serverCommand) != 1)
		{
			DDSS_INPUT_ERROR("Decoder",in)<<"Failed to read the server command byte. "<<std::endl;
			setState(SDS_ERROR);
			callBack->errorOccured(-1,"Failed to read server command byte.");
			return false;
		}
		else if(serverCommand == 0)
		{
			DDSS_VERBOSE("Decoder")<<"[UPDATE-CMD-RECEIVED]"<<std::endl;
			if(in.skip(1) != 1)
			{
				DDSS_INPUT_ERROR("Decoder",in)<<"Failed to read server command padding byte. "<<std::endl;
				setState(SDS_ERROR);
				callBack->errorOccured(-1,"Failed to read server command padding byte.");
				return false;
			}
			else if(in.readShort(&numRectsMax) != 2)
			{
				DDSS_INPUT_ERROR("Decoder",in)<<"Failed to read the rect count for this update"<<std::endl;
				setState(SDS_ERROR);
				callBack->errorOccured(-1,"Failed to read update rect count.");
				return false;
			}
			else
			{
				if(numRectsMax == 0xFFFF)
				{
					this->markerExpected = true;
				}
				DDSS_VERBOSE("Decoder")<<"[RECT-COUNT] Rect Count : "<<numRectsMax<<" / Marker Rect Expected : "<<(markerExpected?"TRUE":"FALSE")<<std::endl;
				setState(SDS_EXPECTING_RECT_HEADER);
			}
		}
		else if(serverCommand == 2)
		{
			DDSS_VERBOSE("Decoder")<<"BELL Event!!!"<<std::endl;
			if(in.skip(1) == 1)
			{
				setState(SDS_EXPECTING_SERVER_CMD);
				return true;
			}
			else
			{
				DDSS_INPUT_ERROR("Decoder",in)<<"could not get bell byte!"<<std::endl;
				return false;
			}
		}
		else if(serverCommand == 3)
		{
			DDSS_VERBOSE("Decoder")<<"CUTTEXT Event!!!"<<std::endl;
			if(in.skip(3) == 3)
			{
				u32 len = 0;
				if(in.readLong(&len) == 4)
				{
					DDSS_VERBOSE("Decoder")<<"Cut text length : "<<len<<std::endl;
					ByteBuffer buf(len);
					if(in.read(buf.getData(),len) == len)
					{
						DDSS_VERBOSE("Decoder")<<"Read cut text of "<<len<<" bytes!"<<std::endl;
						return true;
					}
					else
					{
						DDSS_INPUT_ERROR("Decoder",in)<<"Could not get "<<len<<" bytes cut text!!!"<<std::endl;
						return false;
					}
				}
				
			}
			
			DDSS_INPUT_ERROR("Decoder",in)<<"could not cut text data!"<<std::endl;
			return false;
		
		}
		else
		{
			DDSS_INPUT_ERROR("Decoder",in)<<"Unrecognized Server Command : 0x"<<std::hex<<(u16)serverCommand<<std::dec<<std::endl;
			return false;
		}
		return true;
	}
	bool ScreenDecoder::decodeRectHeader(InputStream& in)
	{
		RectHelper::resetRect(currentRect);
		if(in.readShort(&currentRect.x) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect X"<<std::endl;
			errorOccured(-1,"Failed to read rect x");
			return false;
		}
		else if(in.readShort(&currentRect.y) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect y"<<std::endl;
			errorOccured(-1,"Failed to read rect y");
			return false;
		}
		else if(in.readShort(&currentRect.w) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect W"<<std::endl;
			errorOccured(-1,"Failed to read rect w");
			return false;
		}
		else if(in.readShort(&currentRect.h) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect H"<<std::endl;
			errorOccured(-1,"Failed to read rect h");
			return false;
		}
		else if(in.readLong(&currentRect.enc) != 4)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect enc"<<std::endl;
			errorOccured(-1,"Failed to read rect encoding");
			return false;
		}
		else if(RectHelper::isRectValid(currentRect,screenBuffer.getWidth(),screenBuffer.getHeight()))
		{
			DDSS_VERBOSE("ScreenDecoder")<<"RECT #"<<numRectsCurrent<<"/"<<numRectsMax<<". CurrentHeader "<<currentRect<<std::endl;
			if(currentRect.enc == RFB_ENCODING_TIGHT)
			{
				DDSS_VERBOSE("ScreenDecoder")<<" TIGHT ENCODING!"<<std::endl;
				if(in.getRemainingByteCount() > 0)
				{
					return tightDecoder->decode(in,currentRect);
				}
				else
				{
					DDSS_INPUT_ERROR("SDecoder",in)<<"TIGHT RECTANGLE DATA PENDING!"<<std::endl;
					return false;
				}
			}
			else if(currentRect.enc == RFB_ENCODING_RAW)
			{
				if(in.getRemainingByteCount() > 0)
				{
					return decodeRawRectangle(in,currentRect);
				}
				else
				{
					DDSS_INPUT_ERROR("SDecoder",in)<<"RAW RECTANGLE DATA PENDING!"<<std::endl;
					return true;
				}
			}
			else if(currentRect.enc == RFB_ENCODING_LASTRECT)
			{
				DDSS_VERBOSE("ScreenDecoder")<<" LAST MARKER RECT!"<<std::endl;
				rectComplete(currentRect);
				return true;
			}
			else if(currentRect.enc == RFB_ENCODING_NEWFBSIZE)
			{
				DDSS_VERBOSE("ScreenDecoder")<<" NEW FBSIZE RECT!"<<std::endl;
				rectComplete(currentRect);
				return true;
			}
			else if(currentRect.enc == RFB_ENCODING_CURSOR_POSITION)
			{
				DDSS_VERBOSE("Enc")<<" CURSOR POSITION : "<<currentRect.x<<","<<currentRect.y<<std::endl;
				rectComplete(currentRect);
				return true;
			}
			else if(currentRect.enc == RFB_ENCODING_RICH_CURSOR)
			{
				DDSS_VERBOSE("ScreenDecoder")<<"RICH CURSOR : "<<currentRect<<std::endl;
				//read cursor psuedo encoding
				size_t cpixels = currentRect.w * currentRect.h * (this->screenBuffer.screenInfo.getBitsPerPixel()/8);
				size_t maskSize = (size_t)(floor((f64)(currentRect.w + 7)/8.0)) * currentRect.h;
				DDSS_VERBOSE("ScreenDecoder")<<"Skipping Pixel Size : "<<cpixels<<" / Mask Size : "<<maskSize<<std::endl;
				if(in.skip(cpixels) != cpixels)
				{
					DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Could not read "<<cpixels<<"pixel data for rich cursor!!!"<<std::endl;
					return false;
				}
				DDSS_VERBOSE("ScreenDecoder")<<"Going to parse "<<maskSize<<" bytes mask data from "<<in.getRemainingByteCount()<<" bytes remaining!!!"<<std::endl;
				if(in.skip(maskSize) != maskSize)
				{
					DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Could not read "<<maskSize<<"mask data for rich cursor!!!"<<std::endl;
					return false;
				}
				rectComplete(currentRect);
				return true;
			}
			else if(currentRect.enc == RFB_ENCODING_COPYRECT)
			{
				DDSS_VERBOSE("ScreenDecoder")<<" COPY RECT"<<std::endl;
				in.readShort(&currentRect.src_x);
				in.readShort(&currentRect.src_y);
				DDSS_VERBOSE("ScreenDecoder")<<" SRC X,Y = "<<currentRect.src_x<<" "<<currentRect.src_y<<std::endl;
				rectComplete(currentRect);
				return true;
			}
			else
			{
				DDSS_INPUT_ERROR("ScreenDecoder",in)<<"UNKNOWN ENCODING!!!"<<currentRect<<std::endl;
				setState(SDS_ERROR);
				std::cout<<"UNKNOWN ENCODING : "<<currentRect<<std::endl;
				//DDSS_DEBUG_EXIT();
				return false;
			}
		}
		else
		{
			DDSS_WARNING("ScreenDecoder")<<"[OVERALL RECT = "<<this->numRectsTotal<<"]The rectangle #"<<numRectsCurrent<<" "<<currentRect<<" is not valid!!!"<<std::endl;
			std::cerr<<"[WARN] The rectangle #"<<numRectsCurrent<<" "<<currentRect<<" is not valid!!!"<<std::endl;
			setState(SDS_ERROR);
			return false;
		}
	}
	bool ScreenDecoder::decodeRectData(InputStream& in)
	{
		return false;
	}
	bool ScreenDecoder::decodeRawRectangle(InputStream& in,ScreenRectangle& cur_rect)
	{
		int idx;
	
		int rect_cur_row = 0;
	
		size_t rowSize = cur_rect.w * sizeof(CARD32);
		DDSS_VERBOSE("ScreenDecoder")<<"ROW SIZE : "<<rowSize<<std::endl;
		while (++rect_cur_row < cur_rect.h)
		{
			/* Read next row */
			idx = (cur_rect.y + rect_cur_row) * (int)screenBuffer.getWidth() + cur_rect.x;
			if(in.read(screenBuffer.getBuffer(idx), rowSize) != rowSize)
			{
				DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read raw rectangle : "<<cur_rect<<std::endl;
				return false;
			}
		}
		
		/* Done with this rectangle */
		rectComplete(cur_rect);
		return true;
	
	}
}

