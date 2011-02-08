#include "ScreenDecoder2.h"
#include "TightDecoder2.h"
namespace dm
{
	ScreenDecoder2::ScreenDecoder2(ScreenBuffer& sbuf) : screenBuffer(sbuf), tightDecoder(0)
	{
		resetAll();
		tightDecoder = new TightDecoder2(*this);
		
	}
	ScreenDecoder2::~ScreenDecoder2()
	{
		resetAll();
	}
	bool ScreenDecoder2::decode(InputStream& in, ScreenDecoderCallback* sdCallback, bool recursiveCall)
	{
		if(in.getRemainingByteCount() == 0)
		{
			return true;
		}
		else if(hasPendingData())
		{
			ByteBuffer* tmpBuf = pendingData.clone();
			ScopedPointer<ByteBuffer> bbPtr(tmpBuf);
			ByteBufferInputStream& bin = (ByteBufferInputStream&)in;
			tmpBuf->append(bin.get());
			clearPendingData();
			ByteBufferInputStream bin2(tmpBuf,true);
			bbPtr.release();
			return decode(bin2,sdCallback,recursiveCall);
		}
		else
		{
			switch(state)
			{
			case STATE_SERVER_CMD_EXPECTED:
				if(!decodeServerCommand(in, sdCallback))
				{
					this->decoderError(sdCallback,"Failed to decode server command");
					DDSS_INPUT_ERROR("SD2",in)<<"Failed to decode server command!"<<std::endl;
					return false;
				}
				break;
			case STATE_RECT_LIST_EXPECTED:
				if(!decodeRectList(in, sdCallback))
				{
					this->decoderError(sdCallback,"Failed to decode rect list");
					DDSS_INPUT_ERROR("SD2",in)<<"Failed to decode rect list!"<<std::endl;
					return false;
				}
				break;
			case STATE_RECT_HEADER_EXPECTED:
				if(!decodeRectHeader(in, sdCallback))
				{
					this->decoderError(sdCallback,"Failed to decode rect header");
					DDSS_INPUT_ERROR("SD2",in)<<"Failed to decode rect header!"<<std::endl;
					return false;
				}
				break;
			case STATE_RECT_DATA_EXPECTED:
				if(!decodeRectData(in, sdCallback))
				{

					this->decoderError(sdCallback,"Failed to decode rect data");
					DDSS_INPUT_ERROR("SD2",in)<<"Failed to decode rect data!"<<std::endl;
					return false;
				}
				break;
			default:
				this->decoderError(sdCallback,"decoder in invalid state");
				DDSS_INPUT_ERROR("SD2",in)<<"Decoder is in invalid state!"<<std::endl;
				DDSS_VERBOSE("SD2")<<"exiting..."<<std::endl;
				//DDSS_DEBUG_EXIT();
				return false;
			}
		}
		//continue decoding till we have data
		if(in.getRemainingByteCount() > 0)
		{
			return decode(in,sdCallback,true);
		}
		else
		{
			return true;
		}
		
	}
	void ScreenDecoder2::setPendingData(const void* pendingDataBuf, size_t pendingDataLen, State s)
	{
		setState(s);
		pendingData.append(pendingDataBuf, pendingDataLen);
	}
	bool ScreenDecoder2::hasPendingData() const
	{ 
		return pendingData.getLength() > 0; 
	
	}
	void ScreenDecoder2::clearPendingData()
	{ 
		pendingData.destroy(); 
	}
	
	bool ScreenDecoder2::decodeServerCommand(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		//ByteBufferInputStream& bin = (ByteBufferInputStream&)in;
		
		DDSS_INFO("SD2")<<"DECODING SERVER COMMAND..."<<std::endl;
		u8 serverCommand = 0xFF;
		if(in.readByte(&serverCommand) != 1)
		{
			DDSS_INPUT_ERROR("SD2",in)<<"Failed to decode server command type!"<<std::endl;
			return false;
		}
		else
		{
			switch(serverCommand)
			{
			case 0:
				return decodeServerCommandScreenBuffer(in,sdCallback);
			case 3:
				return decodeServerCommandCutText(in,sdCallback);
			case 2:
				return decodeServerCommandBell(in,sdCallback);
			default:
				DDSS_INPUT_ERROR("SD2",in)<<"Invalid server command received! Command = 0x"<<std::hex<<(u32)serverCommand<<(std::dec)<<std::endl;
				return false;
			}
		}
		DDSS_INFO("SD2")<<"SERVER COMMAND DECODED!!!"<<std::endl;
	}
	bool ScreenDecoder2::decodeServerCommandScreenBuffer(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		setState(ScreenDecoder2::STATE_RECT_LIST_EXPECTED);
		return true;
	}
	bool ScreenDecoder2::decodeServerCommandCutText(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		size_t retlen = in.skip(3);
		if(retlen == 3)
		{
			size_t len = 0;
			if(in.readLong((u32*)&len) != 4)
			{
				DDSS_INPUT_ERROR("SD2",in)<<"Could not read cut text length!"<<std::endl;
				return false;
			}
			else if(in.skip(len) != len)
			{	
				DDSS_INPUT_ERROR("SD2",in)<<"Could not read cut text of length : "<<len<<std::endl;
				return false;
			}
			else
			{
				DDSS_VERBOSE("SD2")<<"CUT TEXT LENGTH : "<<len<<" bytes processed!"<<std::endl;
				return true;
			}
		}
		else
		{
			DDSS_INPUT_ERROR("SD2",in)<<"cut text buffer does not have padding!"<<std::endl;
			return false;
		}
		
		return false;
	}
	bool ScreenDecoder2::decodeServerCommandBell(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		if(in.skip(1) == 1)
		{
			return true;
		}
		else
		{
			DDSS_INPUT_ERROR("SD2",in)<<"Server command bell parse failed!"<<std::endl;
			return false;
		}
	}
	bool ScreenDecoder2::decodeRectList(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		if(in.skip(1) != 1)
		{
			DDSS_INPUT_ERROR("SD2",in)<<"decode rect list could not skip padding byte!"<<std::endl;
			return false;
		}
		u16 numRects = 0;
		if(in.readShort(&numRects) != 2)
		{
			DDSS_INPUT_ERROR("SD2",in)<<"decode rect list could not skip padding byte!"<<std::endl;
			return false;
			
		}
		
		rectsTotal = numRects;
		if(rectsTotal == 0xFFFF)
		{
			this->markerExpected = true;
		}
		this->rectsRemaining = rectsTotal;
		this->rectCount = 0;
		RectHelper::resetRect(currentRect);
		setState(ScreenDecoder2::STATE_RECT_HEADER_EXPECTED);
		DDSS_VERBOSE("SD2")<<"[UPDATE-BEGIN] Max Rects "<<rectsTotal<<" / Marker Expected = "<<(markerExpected?"Y":"N")<<std::endl; 
		return true;
	}
	bool ScreenDecoder2::decodeRectHeader(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		if(in.getRemainingByteCount() == 0)
		{
			return true;
		}
		RectHelper::resetRect(currentRect);
		if(in.readShort(&currentRect.x) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect X"<<std::endl;
			return false;
		}
		else if(in.readShort(&currentRect.y) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect y"<<std::endl;
			return false;
		}
		else if(in.readShort(&currentRect.w) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect W"<<std::endl;
			return false;
		}
		else if(in.readShort(&currentRect.h) != 2)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect H"<<std::endl;
			return false;
		}
		else if(in.readLong(&currentRect.enc) != 4)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read rect enc"<<std::endl;
			return false;
		}
		else if(RectHelper::isRectValid(currentRect,screenBuffer.getWidth(),screenBuffer.getHeight()))
		{
			this->setState(ScreenDecoder2::STATE_RECT_DATA_EXPECTED);
			return true;
		}
		else
		{
			DDSS_ERROR("ScreenDecoder")<<"Invalid Rectangle encountered!"<<currentRect<<std::endl;
			return false;
		}
		return false;
	}
	bool ScreenDecoder2::decodeRectData(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		DDSS_VERBOSE("SD2")<<"Reading rect data for : "<<currentRect<<std::endl;
		switch(currentRect.enc)
		{
		case RFB_ENCODING_RAW:
			return decodeRawRectangle(in,sdCallback);
		case RFB_ENCODING_TIGHT:
			return decodeTightRectangle(in,sdCallback);
		case RFB_ENCODING_NEWFBSIZE:
			return decodeNewFBSize(in,sdCallback);
		case RFB_ENCODING_LASTRECT:
			DDSS_VERBOSE("SD2")<<"Last Rect!!!!"<<std::endl;
			return decodeLastRectangle(in,sdCallback);
		case RFB_ENCODING_CURSOR_POSITION:
			return decodeCursorPosition(in,sdCallback);
		case RFB_ENCODING_RICH_CURSOR:
			return decodeRichCursor(in,sdCallback);
		case RFB_ENCODING_COPYRECT:
			return decodeCopyRectangle(in,sdCallback);
			
		}
		return false;
	}
	bool ScreenDecoder2::decodeRawRectangle(InputStream& in, ScreenDecoderCallback* sdCallback)
	{

		DDSS_INPUT_ERROR("SD2",in)<<"RECEIVED RAW RECTANGLE SHOULD NOT HAPPEN WITH DIMDIM SCREEN SHARE!!!!"<<std::endl;
		return false;
/*
		DDSS_VERBOSE("SD2")<<"raw rect"<<std::endl;

		size_t markerPos = in.getMarkerPosition();
		size_t len = in.getRemainingByteCount();
		int rect_cur_row = 0;
		int idx = 0;
	
		size_t rowSize = currentRect.w * (screenBuffer.screenInfo.getBitsPerPixel()/8);
		DDSS_VERBOSE("ScreenDecoder")<<"ROW SIZE : "<<rowSize<<std::endl;
		DDSS_VERBOSE("SD2")<<"Total rect size : "<<(currentRect.w * currentRect.h * sizeof(CARD32))<<std::endl;
		while (++rect_cur_row < currentRect.h)
		{
			// Read next row 
			idx = (currentRect.y + rect_cur_row) * (int)screenBuffer.getWidth() + currentRect.x;
			if(in.read(screenBuffer.getBuffer(idx), rowSize) != rowSize)
			{
				DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Failed to read raw rectangle : "<<currentRect<<std::endl;
				ByteBufferInputStream& bin = (ByteBufferInputStream&)in;
				const ByteBuffer* buf = bin.get();
				this->setPendingData(buf->getData(markerPos),len,ScreenDecoder2::STATE_RECT_DATA_EXPECTED);
				DDSS_VERBOSE("SD2")<<"[DECODE-RAW-PARTIAL] Marked "<<len<<" bytes of pending data!!!"<<std::endl;
				return true;
			}
		}
		
		return true;*/
	}
	bool ScreenDecoder2::decodeTightRectangle(InputStream& in, ScreenDecoderCallback* sdCallback)
	{

		DDSS_VERBOSE("SD2")<<"tight rect"<<std::endl;
		if(tightDecoder)
		{
			size_t markerPos = in.getMarkerPosition();
			size_t len = in.getRemainingByteCount();
			DDSS_VERBOSE("SD2")<<"************ TightDecode - marker : "<<markerPos<<" / remaining : "<<len<<std::endl;
			TightDecoder2::Result result = this->tightDecoder->decode(in,currentRect);
			if(result == TightDecoder2::TDR_FAILED)
			{
				DDSS_ERROR("SD2")<<"Failed to decode tight rectangle : "<<currentRect<<std::endl;
				return false;
			}
			else if(result == TightDecoder2::TDR_OK)
			{
				rectComplete(sdCallback);
				return true;
			}
			else if(result == TightDecoder2::TDR_INCOMPLETE)
			{
				
				ByteBufferInputStream& bin = (ByteBufferInputStream&)in;
				const ByteBuffer* buf = bin.get();
				this->setPendingData(buf->getData(markerPos),len,ScreenDecoder2::STATE_RECT_DATA_EXPECTED);
				
				DDSS_VERBOSE("SD2")<<"[INCOMPLETE] Marked "<<len<<" bytes of pending data!!!"<<std::endl;
				return true;
			}
		}
		return false;
	}
	bool ScreenDecoder2::decodeCopyRectangle(InputStream& in, ScreenDecoderCallback* sdCallback)
	{

		DDSS_VERBOSE("SD2")<<"copy rect"<<std::endl;
		if(in.readShort(&currentRect.src_x) != 2)
		{
			DDSS_INPUT_ERROR("SD2",in)<<"decode copy rect failed to read src_x."<<currentRect<<std::endl;
			return false;
		}
		if(in.readShort(&currentRect.src_y) != 2)
		{
			DDSS_INPUT_ERROR("SD2",in)<<"decode copy rect failed to read src_y."<<currentRect<<std::endl;
			return false;
		}
		
		//apply copy rect to screen buffer
		this->screenBuffer.copy_rect(currentRect);
		this->rectComplete(sdCallback);
		return true;
	}
	bool ScreenDecoder2::decodeLastRectangle(InputStream& in, ScreenDecoderCallback* sdCallback)
	{

		DDSS_VERBOSE("SD2")<<"last rect"<<std::endl;
		rectComplete(sdCallback);
		return true;
	}
	bool ScreenDecoder2::decodeCursorPosition(InputStream& in, ScreenDecoderCallback* sdCallback)
	{

		DDSS_VERBOSE("SD2")<<"cursor position"<<std::endl;
		rectComplete(sdCallback);
		return true;
	}
	bool ScreenDecoder2::decodeRichCursor(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		DDSS_VERBOSE("SD2")<<"rich cursor"<<std::endl;
		//read cursor psuedo encoding
		size_t cpixels = currentRect.w * currentRect.h * (this->screenBuffer.screenInfo.getBitsPerPixel()/8);
		size_t maskSize = (size_t)(floor((f64)(currentRect.w + 7)/8.0)) * currentRect.h;
		DDSS_VERBOSE("SD2")<<"Skipping Pixel Size : "<<cpixels<<" / Mask Size : "<<maskSize<<std::endl;
		if(in.skip(cpixels) != cpixels)
		{
			DDSS_INPUT_ERROR("ScreenDecoder",in)<<"Could not read "<<cpixels<<"pixel data for rich cursor!!!"<<std::endl;
			return false;
		}
		DDSS_VERBOSE("SD2")<<"Going to parse "<<maskSize<<" bytes mask data from "<<in.getRemainingByteCount()<<" bytes remaining!!!"<<std::endl;
		if(in.skip(maskSize) != maskSize)
		{
			DDSS_INPUT_ERROR("SD2",in)<<"Could not read "<<maskSize<<"mask data for rich cursor!!!"<<std::endl;
			return false;
		}
		this->rectComplete(sdCallback);
		return true;
	}
	bool ScreenDecoder2::decodeNewFBSize(InputStream& in, ScreenDecoderCallback* sdCallback)
	{
		DDSS_VERBOSE("SD2")<<"new fb size"<<std::endl;
		screenBuffer.resize(currentRect.w,currentRect.h);
		return true;
	}
	
	void ScreenDecoder2::resetRects()
	{
		RectHelper::resetRect(currentRect);
		this->setState(STATE_RECT_HEADER_EXPECTED);
		
	}
	void ScreenDecoder2::resetAll()
	{
		resetRects();
		this->rectCount = this->rectsRemaining = this->rectsTotal = 0;
		this->markerExpected = false;
		this->setState(STATE_SERVER_CMD_EXPECTED);
	}

	void ScreenDecoder2::rectComplete(ScreenDecoderCallback* sdCallback)
	{
		if(sdCallback)
		{
			sdCallback->rectComplete(currentRect);
		}
		this->rectCount++;
		this->overallRectCount++;
		this->rectsRemaining = this->rectsTotal - this->rectCount;
		DDSS_VERBOSE("SD2")<<" [RECT RECEIVED] : "<<rectCount<<" of "<<rectsTotal<<" /  remaining : "<<rectsRemaining<<std::endl;
		if(this->rectsRemaining == 0)
		{
			updateComplete(sdCallback, false);
		}
		else if(this->markerExpected && this->currentRect.enc == RFB_ENCODING_LASTRECT)
		{
			updateComplete(sdCallback, true);
		}
		else
		{
			resetRects();
		}
	}
	void ScreenDecoder2::updateComplete(ScreenDecoderCallback* sdCallback, bool markerMet)
	{
		if(sdCallback)
		{
			sdCallback->updateComplete(rectCount, rectsTotal, markerMet);
		}

		this->overallUpdateCount++;
		DDSS_VERBOSE("SD2")<<"UPDATE COMPLETE [rect count = "<<rectCount<<"] [totalRects = "<<rectsTotal<<"] [MARKER = "<<(markerMet?"YES":"NO")<<"]"<<std::endl;
		DDSS_INFO("SD2")<<"RECT STATS (Total Updates : "<<overallUpdateCount<<") (Total Rects : "<<overallRectCount<<")"<<std::endl;
		resetAll();
	}
	void ScreenDecoder2::decoderError(ScreenDecoderCallback* sdCallback, const String errorMsg, int errorCode)
	{
		resetAll();
		setState(STATE_ERROR);
		if(sdCallback)
		{
			sdCallback->errorOccured(errorCode, errorMsg);
		}
	}
};
