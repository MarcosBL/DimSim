#include "Screen.h"



namespace dm
{
	Screen::Screen(const String confKeyStr, const String streamNameStr) : confKey(confKeyStr), 
	streamName(streamNameStr), 
	decoder(0)
	{
		publisher = 0;
		jpegLevelOut = -1;
#ifdef DDSS_USE_NEW_DECODER
		decoder = new ScreenDecoder2(screenBuffer);
#else
		decoder = new ScreenDecoder(screenBuffer);
#endif
		this->generateFLV = false;
		tagsEncoded = 0;
		mailboxRoot = "etc/dat/flv";
		//DDSS_VERBOSE("SCREEN")<<"SCREEN DECODER GOT BUFFER : 0x"<<std::hex<<(u32)(&screenBuffer)<<std::dec<<std::endl;
		cursorImg = 0;
		mouseX = 10;
		mouseY = 10;
	}
	
	Screen::~Screen()
	{
		cleanup();
	}

	bool Screen::initGenerateFLV(int w, int h)
	{
		flvWidth = w;
		flvHeight = h;
		return initFLV(w,h);
	}
	bool Screen::initFLV(int w, int h)
	{
		std::cout<<"init generated flv : "<<w<<"X"<<h<<std::endl;
		String dirName = this->radDir;
		dirName += "/Output/";
		dirName += this->streamName;
		dirName += ".flv";
		const char* flvName = dirName.c_str();
		std::cout<<"Creating FLV File : "<<flvName<<"..."<<std::endl;
		try
		{
			this->flvFile.init(flvName);
		}
		catch(std::exception& ex)
		{
			std::cerr<<"Failed to open flv file ("<<flvName<<"). Error = "<<ex.what()<<std::endl;
			this->generateFLV = false;
			return false;
		}
		svFrame.init(true,w,h);
		this->generateFLV = true;
		return true;
	}
	RGBImage* Screen::getScreenImage()
	{
		RGBImage *img = new RGBImage(this->screenBuffer.getWidth(),this->screenBuffer.getHeight());
		ScopedPointer<RGBImage> imgPtr(img);
		img->setRGB32((const u32*)screenBuffer.getBuffer(0));
		if(cursorImg)
		{
			//std::cerr<<"Drawing mouse image at : "<<mouseX<<","<<mouseY<<std::endl;
			RGBPixel colorKey;
			colorKey.r = 0xFF;
			colorKey.g = 0x00;
			colorKey.b = 0x00;
			int cursorWidth = cursorImg->getWidth();
			int cursorHeight = cursorImg->getHeight();

			int blitX = mouseX < 0? 0:mouseX;
			int blitY = mouseY < 0? 0:mouseY;

			int maxX = img->getWidth() - cursorWidth;
			int maxY = img->getHeight() - cursorHeight;
			
			if(blitX > maxX){ blitX = maxX; }
			if(blitY > maxY){ blitY = maxY; }
			
			//blitX = img->getWidth() - blitX;
			//std::cout<<"Blit Y : "<<blitY<<" / img height : "<<img->getHeight()<<" / Cursor Y : "<<cursorHeight<<std::endl;
			
			if(blitY < cursorHeight){ blitY = cursorHeight; }
			blitY = img->getHeight() - blitY - cursorHeight;	
			//std::cerr<<"Adjusted Blit Y : "<<blitY<<std::endl;
			img->set(blitX, blitY, cursorImg, colorKey);
		}
		RGBImage* img2 = new RGBImage(flvWidth,flvHeight);
		ScopedPointer<RGBImage> imgPtr2(img2);
		img->scale(img2);
		return imgPtr2.release();
		
	}
	void Screen::addLastFLVTag(u32 duration, RGBImage* defaultImage)
	{
		
		FLVTag tag;
		bool emptyTag = false;
		RGBImage *screenImage = defaultImage;
		//ScopedPointer<RGBImage> imgPtr(screenImage);
		bool keyFrame = true;
		u32 timeStampTmp = duration;
		this->lastTagTimestamp = this->timeStamp;
		bool kf = ScreenVideoEncoder::encodeFrame(&svFrame, screenImage, keyFrame, emptyTag);
		if(kf)
		{
			svFrame.makeKeyFrame();
		}
		ScreenVideoEncoder::writeTag(&tag,&svFrame,timeStampTmp);
		this->flvFile.writeTag(tag);
		this->flvFile.close();
		//std::cout<<"Added "<<tagsEncoded<<" Tag!!!"<<std::endl;
		//std::cout<<std::endl;
		
	}
	void Screen::addTag(size_t rectCount, size_t maxRects, bool markerFound, bool keyFrame)
	{
		
		//std::cout<<"Adding tag : "<<rectCount<<"/"<<maxRects<<"/"<<markerFound<<std::endl;
		FLVTag tag;
		bool emptyTag = false;
		RGBImage *screenImage = getScreenImage();
		ScopedPointer<RGBImage> imgPtr(screenImage);
		if(tagsEncoded == 0)
		{
			this->timeStampFirst = this->timeStamp;
		}
		else
		{
			if(this->timeStamp - this->lastTagTimestamp >= 500){ keyFrame = true; }
		}

		this->lastTagTimestamp = this->timeStamp;
		u32 timeStampTmp = this->timeStamp - this->timeStampFirst;
		
	
		tagsEncoded++;
		bool kf = ScreenVideoEncoder::encodeFrame(&svFrame, screenImage, keyFrame, emptyTag);
		if(kf)
		{
			svFrame.makeKeyFrame();
		}
		ScreenVideoEncoder::writeTag(&tag,&svFrame,timeStampTmp);
		this->flvFile.writeTag(tag);
		
		//std::cout<<"Added "<<tagsEncoded<<" Tag!!!"<<std::endl;
		//std::cout<<std::endl;
	}
	void Screen::closeFLV()
	{
	//	this->flvFile.close();
	}
	ScreenPub* Screen::getPublisher()
	{
		ScopedLock sl(screenLock);
		return publisher;
	}
	ScreenSub* Screen::getSubscriber(const String id)
	{
		ScopedLock sl(screenLock);
		std::map<String,ScreenSub*>::iterator iter = this->subs.find(id);
		return (iter == subs.end()?(ScreenSub*)0:iter->second);
	}

	void Screen::pruneSubs(u32 maxIdleAgeMs)
	{

	
		
		
	}
	bool Screen::isPubIdleExceeded(u32 maxIdleAgeMs)
	{
		ScopedLock sl(screenLock);
		if(publisher)
		{
			if(publisher->getTimeSinceLastOp() > maxIdleAgeMs)
			{
				return true;
			}
			else if(publisher->getTimeSinceLastOp() > maxIdleAgeMs && publisher->getOpCount() == 0)
			{
				return true;
			}
			else if(info.width * info.height == 0)
			{
				return true;
			}
			return false;
		}
		else
		{
			return true;
		}
	}
	ScreenPub* Screen::createPublisher()
	{
		ScopedLock sl(screenLock);
		publisher = new ScreenPub(*this);
		return publisher;
	}
	ScreenSub* Screen::createSubscriber()
	{
		ScopedLock sl(screenLock);
		
		ScreenSub* sub = new ScreenSub(*this);
		sub->setJpegLevelOut(this->jpegLevelOut);
		subs[sub->getId()] = sub;
		return sub;
	}

	void Screen::deletePublisher()
	{
		ScopedLock sl(screenLock);
		if(publisher)
		{
			delete publisher;
			publisher = 0;
		}

	}
	void Screen::deleteSubscriber(const String id)
	{
		ScopedLock sl(screenLock);
		std::map<String,ScreenSub*>::iterator iter = this->subs.find(id);
		std::vector<String> idList;
		if (iter != subs.end())
		{
			delete iter->second;
			idList.push_back(iter->first);
			iter++;
		}
		for(size_t s = 0; s < idList.size(); s++)
		{
			subs.erase(idList[s]);
		}
	}
	void Screen::deleteAllSubscribers()
	{
		ScopedLock sl(screenLock);
		Helper::clearPtrMap(subs);
	}
	void Screen::init(ScreenInfo& screenInfo)
	{
		ScopedLock sl(screenLock);
		this->info = screenInfo;
		//DDSS_VERBOSE("SCREEN")<<"SCREEN BUFFER BEING INITIALIZED : 0x"<<std::hex<<(u32)(&screenBuffer)<<std::dec<<std::endl;
		screenBuffer.init(info);
	
		std::string id;
		IDGenerator::generateClientId(id);

#ifdef DDSS_RECT_DUMP_ENABLED
		std::string fileName = "etc/dat/screen-rect-";
		fileName += id;
		fileName += ".log";	
		rectDump.open(fileName.c_str());
#endif
	}

	void Screen::cleanup()
	{
		
		if(rectDump.is_open()) 
		{
			rectDump.close();
		}
		
		{
			ScopedLock sl(screenLock);
			if(decoder)
		
			{
				delete decoder;
			}
			decoder = 0;
		}
		deletePublisher();
		deleteAllSubscribers();
		screenBuffer.destroy();
		this->flvFile.close();
	}
	void Screen::captureScreenToJPEG()
	{
	//	std::ostrstream os;
	//	os<<"etc/dat/screen-capture-capture-count-"<<captureCount++<<".jpg"<<std::ends;
		
	}
	void Screen::lockCheck(){ ScopedLock sl(screenLock); }
	bool Screen::postData(const void* buf, size_t len, u32 timeStamp)
	{
		ScopedLock sl(screenLock);
		this->timeStamp = timeStamp;
		if(info.isValid() && publisher)
		{
			ByteBufferInputStream bis((const u8*)buf,len,false);
			return parseUpdate(bis);
		}
		else
		{
			DDSS_ERR("Screen")<<"postData("<<confKey<<"/"<<streamName<<") for "<<len<<" bytes not executed as stream is in disabled state!"<<std::endl;
			return false;
		}
	}
	void Screen::writeKeyFrame(OutputStream& out)
	{
		// TODO: Generate the tight encoded update for the current 
		// screen buffer key frame here
	}
	bool Screen::parseUpdate(InputStream& in)
	{
		if(decoder)
		{	
#ifdef DDSS_RECT_DUMP_ENABLED
			rectDump<<"[DATA=RECEIVED] = "<<in.getRemainingByteCount()<<std::endl;
#endif
			return decoder->decode(in,this);
		}
		else
		{
			DDSS_WARNING("Screen")<<"Failed to parse update"<<std::endl;
			return false;
		}
	}

	void Screen::rectComplete(ScreenRectangle& r)
	{
		//if(r.enc == RFB_ENCODING_CURSOR_POSITION) std::cout<<" Cursor Pos : "<<r<<std::endl;
#ifdef DDSS_RECT_DUMP_ENABLED
		rectDump<<"[RECT-RECEIVED],"<<Timer::currentTimeMillis()<<","<<r<<std::endl;
#endif

		if(r.enc == RFB_ENCODING_CURSOR_POSITION)
		{
			mouseX = r.x;
			mouseY = r.y;
			if(this->getGenerateFLVFlag())
			{
				std::cerr<<"Mouse position : "<<mouseX<<","<<mouseY<<std::endl;
			}
		}
		int updateCount = 0;
		std::map<String,ScreenSub*>::iterator iter = subs.begin();
		while(iter != subs.end())
		{
			ScreenSub* sub = iter->second;
			if(sub && sub->getState() == ScreenSub::SUB_READY)
			{
				sub->addRect(r);
				updateCount++;
			}
			iter++;
		}

		if(this->getGenerateFLVFlag())
		{
			this->addTag(0,0,false,false);	
		}
		DDSS_VERBOSE("Screen")<<"[SUBS-UPDATED] SUB UPDATED COUNT : "<<updateCount<<" of "<<subs.size()<<std::endl;
		
	}
	void Screen::updateComplete(size_t rectCount, size_t maxRects, bool markerFound)
	{
#ifdef DDSS_RECT_DUMP_ENABLED
		rectDump<<"[UPDATE-COMPLETE],"<<Timer::currentTimeMillis()<<","<<rectCount<<","<<maxRects<<","<<markerFound<<std::endl;
#endif
		
		if(this->getGenerateFLVFlag())
		{
			this->addTag(rectCount,maxRects,markerFound,true);	
		}
		DDSS_VERBOSE("SCREEN")<<" UPDATE COMPLETE. RECTS : "<<rectCount<<"/"<<maxRects<<". MARKER FOUND : "<<(markerFound?"YES":"NO")<<std::endl;
	}
	void Screen::errorOccured(int errorCode, const String errorMsg)
	{
		DDSS_ERROR("Screen")<<"ERROR : "<<errorCode<<" "<<errorMsg<<std::endl;
	}
	void Screen::resizeReceived(size_t w, size_t h)
	{
		this->info.width = (u16)w;
		this->info.height = (u16)h;
		
	}
};

