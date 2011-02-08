#include "ScreenEncoder.h"
#include "../common/IDGenerator.h"

#define DDSS_MAX_SUB_PKT_SIZE (64*1024)

#define DDSS_MAX_RECT_W 400
#define DDSS_MAX_RECT_H 200
#define DDSS_RECT_CHUNK_W 100
#define DDSS_RECT_CHUNK_H 150

namespace dm
{

	ScreenEncoder::ScreenEncoder(ScreenBuffer& buffer) :
		screenBuffer(buffer), tightEncoder(0), flashClient(false),sendCount(0), maxSize(DDSS_MAX_SUB_PKT_SIZE),overflowCount(0), underflowCount(0)
	{
	
		tightEncoder = new TightEncoder(*this);
		CL_SLOT *cl = (CL_SLOT *)&slot;
		memset(cl, 0, sizeof(CL_SLOT));
		cl->fb_width = screenBuffer.getWidth();
		cl->fb_height = screenBuffer.getHeight();
		screenBuffer.screenInfo.populate(cl->format);
	
		cl->trans_table = 0;
		cl->enc_enable[RFB_ENCODING_RAW] = 1;
		cl->enc_enable[RFB_ENCODING_TIGHT] = 1;
		//cl->enc_enable[RFB_ENCODING_LASTRECT] = 1;
		cl->enc_prefer = RFB_ENCODING_TIGHT;
		cl->bgr233_f = (screenBuffer.screenInfo.getBitsPerPixel() == 8)?1:0;
		cl->compress_level = 9; /* default compression level */
		cl->jpeg_quality = -1; /* disable JPEG by default */
	
		/* The client did not requested framebuffer updates yet */
		cl->update_requested = 0;
		cl->update_in_progress = 0;
		REGION_INIT(&cl->pending_region, NullBox, 16);
		REGION_INIT(&cl->copy_region, NullBox, 8);
		cl->newfbsize_pending = 0;
		/* We are connected. */
		cl->connected = 1;
		cl->enable_lastrect = 1;
		
		u32 wFull = screenBuffer.getWidth();
		u32 hFull = screenBuffer.getHeight();
		u32 wChunk = wFull / 16;
		u32 hChunk = hFull / 16;
		u32 wLast = wFull - wChunk * 15;
		u32 hLast = hFull - hChunk * 15;
		for(int y = 0; y < 16; y++)
		{

			for(int x = 0; x < 16; x++)
			{
				ScreenRectangle r;
				r.w = (u16)(x == 15? wLast:wChunk);
				r.h = (u16)(y == 15? hLast:hChunk);
				r.x = (u16)(x * wChunk);
				r.y = (u16)(y * hChunk);
				r.enc = RFB_ENCODING_TIGHT;
				this->pendingRectList.push_back(r);
			}
		}
		DDSS_VERBOSE("ScreenEncoder")<<"Pixel format translation tables not needed"<<std::endl;
		//String id = "ddd";
		//IDGenerator::generateClientId(id);
		//id+="-rect.log";
		//dumpFile.open(id.c_str());
	
		screenBuffer.screenInfo.fill(&cl->format);
}
	ScreenEncoder::~ScreenEncoder()
	{
		dumpFile.close();
		delete tightEncoder;
	}
	void ScreenEncoder::setJpegLevelOut(int level)
	{ 
		this->jpegLevelOut = level; 
		this->slot.jpeg_quality = level;
		DDSS_VERBOSE("ScreenShareApp")<<"Encoder JPEG LEVEL : "<<level<<std::endl;
	}
	void ScreenEncoder::setFlashClient(bool b){ flashClient = b; tightEncoder->setFlashClient(b);}
	void ScreenEncoder::addRectPrivate(ScreenRectangle& inRect)
	{
		pendingRectList.push_back(inRect);
	}
	void ScreenEncoder::addRect(ScreenRectangle& inRect)
	{
		ScopedLock s(rectLock);
		size_t prevSize = pendingRectList.size();
		if(inRect.enc == RFB_ENCODING_NEWFBSIZE)
		{
			std::cout<<"-- SCREEN ENC : NEW FB SIZE PACKET RECEIVED : "<<inRect<<std::endl;
			clearRects();
			DDSS_VERBOSE("ScreenShareApp")<<"Adding the fb size rect : "<<inRect<<std::endl;
			pendingRectList.push_back(inRect);
			CL_SLOT *cl = (CL_SLOT *)&slot;
			cl->fb_width = inRect.w;
			cl->fb_height = inRect.h;
			DDSS_VERBOSE("ScreenShareApp")<<"Forcing a full screen update!!!"<<std::endl;
			u32 wFull = inRect.w;
			u32 hFull = inRect.h;
			
			u32 wChunk = DDSS_RECT_CHUNK_W;
			u32 hChunk = DDSS_RECT_CHUNK_H;
			
			u32 countX = wFull / wChunk;
			if(wFull % wChunk > 0) countX++;
			

			u32 countY = hFull / hChunk;
			if(hFull % hChunk > 0) countY++;
			
			u32 wLast = wFull - wChunk * (countX-1);
			u32 hLast = hFull - hChunk * (countY-1);
			
			for(u32 y = 0; y < countY; y++)
			{

				for(u32 x = 0; x < countX; x++)
				{
					ScreenRectangle r;
					r.w = (u16)(x == (countX-1)? wLast:wChunk);
					r.h = (u16)(y == (countY-1)? hLast:hChunk);
					r.x = inRect.x + (u16)(x * wChunk);
					r.y = inRect.y + (u16)(y * hChunk);
					r.enc = RFB_ENCODING_TIGHT;
					this->pendingRectList.push_back(r);
				}
			}
			DDSS_VERBOSE("ScreenShareApp")<<"Full screen update sent / fb size sent out!!!"<<std::endl;
			
		}

		else
		{
			addRectPrivate(inRect);
		}
		size_t newSize = pendingRectList.size();
		DDSS_VERBOSE("ScreenShareApp")<<"addRect() done (PREV: "<<prevSize<<"/NEW:"<<newSize<<std::endl;;
	}
	void ScreenEncoder::clearRects()
	{
		pendingRectList.clear();
	}
	void ScreenEncoder::createUpdate(OutputStream& out)
	{
		DDSS_VERBOSE("SEnc")<<"Create update called!!!!"<<std::endl;
		sendUpdate(out);
		
		
	}
	void ScreenEncoder::sendUpdate(OutputStream& out)
	{

		ScopedLock s(rectLock);
		sendCount++;
		CL_SLOT *cl = &slot;
//		BoxRec fb_rect;
//		RegionRec fb_region, clip_region, outer_region;
		CARD8 msg_hdr[4] =
		{
			0, 0, 0, 1
		};
		CARD8 rect_hdr[12];
		ScreenRectangle rect;
	
	
		int num_penging_rects = pendingRectList.size();
		
		//if(num_penging_rects == 0){ return; }
		buf_put_CARD16(&msg_hdr[2], 0xFFFF);
		if(num_penging_rects > 0) 
		{
			out.write(msg_hdr, 4);
		}
		else 
		{
			return;
		}
	
		//DDSS_VERBOSE("ScreenShareApp")<<"(sub) Pending Rects : "<<pendingRects.size()<<std::endl;
		/* For each of the usual pending rectangles: */
		while(pendingRectList.size() > 0)
		{

			
			ScreenRectangle rect = pendingRectList[0]; 
			pendingRectList.erase(pendingRectList.begin());
			if(rect.enc == RFB_ENCODING_COPYRECT)
			{
				DDSS_VERBOSE("ScreenShareApp")<<"CopyRect : "<<rect<<std::endl;
				CARD8 tmp[16];
				RFBHelper::put_rect_copy(tmp,&rect);
				out.write(tmp,16);
			}
			else if(rect.enc == RFB_ENCODING_CURSOR_POSITION)
			{
				CARD8 tmp[12];
				RFBHelper::put_rect_header(tmp,&rect);
				out.write(tmp,12);
				DDSS_VERBOSE("Enc")<<"Sending Cursor update "<<rect<<" to sub!!!"<<std::endl;
			}
			else if(rect.enc == RFB_ENCODING_TIGHT)// || rect.enc == RFB_ENCODING_COPYRECT)
			{
				rect.enc = RFB_ENCODING_TIGHT;
				if(tightEncoder)
				{
					ByteBufferOutputStream& bos = (ByteBufferOutputStream&)out;
					size_t l1 = bos.get()->getLength();
					tightEncoder->encode(out, &rect);
					size_t l2 = bos.get()->getLength();

					
					DDSS_VERBOSE("[SUB-ENC]")<<"("<< (l2-l1)<<" bytes  / "<<(rect.w * rect.h * 3)<<" bytes)"<<rect<<std::endl;
				}
			}
			else if(rect.enc == RFB_ENCODING_NEWFBSIZE)
			{
				CARD8 tmp[12];
				RFBHelper::put_rect_header(tmp,&rect);
				out.write(tmp,12);
			}
			else
			{
				//DDSS_VERBOSE("ScreenShareApp")<<"[SUB-ENC] UNKNOWN ENCODED RECT : "<<rect<<std::endl;
			}
			
			
			size_t len = ((ByteBufferOutputStream&)out).get()->getLength();
			if(len >= maxSize)
			{
				break;
			}
		}
		size_t len = ((ByteBufferOutputStream&)out).get()->getLength();
		if(len > maxSize)
		{
			overflowCount++;
		}
		else
		{
			underflowCount++;
		}

		/* Send LastRect marker. */
		if(num_penging_rects > 0)
		{
			rect.x = rect.y = rect.w = rect.h = 0;
			rect.enc = RFB_ENCODING_LASTRECT;
			RFBHelper::put_rect_header(rect_hdr, &rect);
			out.write(rect_hdr, 12);
		}
	
	}
	void ScreenEncoder::bumpMaxSizeUp()
	{	
		if(maxSize == 128 * 1024){ return; }
		else if(maxSize == 32 * 1024){ maxSize = 48 * 1024; }
		else if(maxSize == 48 * 1024){ maxSize = 64 * 1024; }
		else if(maxSize == 64 * 1024){ maxSize = 80 * 1024; }
		else if(maxSize == 80 * 1024){ maxSize = 96 * 1024; }
		else if(maxSize == 96 * 1024){ maxSize = 128 * 1024; }
	}
	void ScreenEncoder::bumpMaxSizeDown()
	{
		if(maxSize == 32 * 1024){ return; }
		else if(maxSize == 128 * 1024){ maxSize = 96 * 1024; }
		else if(maxSize == 96 * 1024){ maxSize = 80 * 1024; }
		else if(maxSize == 80 * 1024){ maxSize = 60 * 1024; }
		else if(maxSize == 60 * 1024){ maxSize = 48 * 1024; }
		else if(maxSize == 48 * 1024){ maxSize = 32 * 1024; }
	}	
}
