#include "ScreenSub.h"

namespace dm
{
	
	ScreenSub::ScreenSub(Screen& s) : ScreenClient(ScreenClient::SCR_SUB,s), encoder(0), flashClient(false)
	{
		setState(SUB_INACTIVE);
		rectCount = 0;
		this->jpegLevelOut = -1;
	}
	ScreenSub::~ScreenSub()
	{
		cleanup();
	}
	bool ScreenSub::init()
	{
		setState(SUB_CONNECTED);
		rectCount = 0;
		
		encoder = new ScreenEncoder(getScreen().screenBuffer);
		this->encoder->setFlashClient(flashClient);
		std::cout<<"sub init : jpeg level : "<<this->jpegLevelOut<<std::endl;
		encoder->setJpegLevelOut(this->jpegLevelOut);
		return true;
	}
	void ScreenSub::cleanup()
	{
		dumpFile.close();
		if(encoder)
		{
			delete encoder;
		}
	}
	bool ScreenSub::onHandleOpen(const void* buf, size_t len, OutputStream& out)
	{
		Screen& s = getScreen();
		if(s.getInfo().isValid())
		{
			if(encoder)
			{
				this->encoder->setFlashClient(false);
			}
			if(len > 0)
			{
				String openStr = (const char*)buf;
				if(openStr.find_first_of("flash") < (openStr.size() - 1))
				{
					std::cout<<"FLASH CLIENT!!!"<<std::endl;
					DDSS_INFO("ScreenSub")<<getId()<<" - is a flash client!"<<std::endl;
					flashClient = true;
					if(encoder)
					{
						this->encoder->setFlashClient(true);
					}
				}
				else
				{
					std::cout<<"JAVA CLIENT : "<<openStr<<std::endl;
				}
			}
			ScreenInfo si = s.getInfo();
			si.save(out);
			out.writeLong((u32)(getId().size()));
			out.write(getId().c_str(),getId().size());
			s.writeKeyFrame(out);
			setState(SUB_READY);
			return true;
		}
		setState(SUB_INACTIVE);
		return false;
	}
	bool ScreenSub::onHandlePost(const void* buf, size_t len, OutputStream& out)
	{
		//TODO: when control is added handle incoming mouse / cursor update / key events etc.
		return true;
	}
	bool ScreenSub::onHandlePoll(OutputStream& out)
	{
		//getScreen().lockCheck();
		ScopedReadLock swl(getScreen().getRWLock());
		ByteBufferOutputStream& bos = (ByteBufferOutputStream &)out;
		
		size_t l1 = bos.get()->getLength();
		writeRects(out);
		size_t l2 = bos.get()->getLength();
		//size_t rectSize = (l2 - l1);
		//DDSS_AUDIT("CLIENT")<<"[SUB-SEND],"<<Timer::currentTimeMillis()<<","<<getId()<<","<<(l2-l1)<<std::endl;
		return true;
	}
	
	void ScreenSub::addRect(ScreenRectangle& r)
	{
		ScopedLock sl(rectLock);
		bool rectOk = false;
		if(r.enc == RFB_ENCODING_TIGHT || r.enc == RFB_ENCODING_COPYRECT)
		{
			rectOk = (r.w !=0 && r.h != 0);
		}
		else if(r.enc == RFB_ENCODING_CURSOR_POSITION)
		{
			rectOk = true;
		}
		else if(r.enc == RFB_ENCODING_NEWFBSIZE)
		{
			rectOk = true;
		}
		if(getState() == SUB_READY &&  rectOk)
		{
			rectCount++;
			if(encoder)
			{
				encoder->addRect(r);
			}
		}
	}
	void ScreenSub::writeRects(OutputStream& out)
	{
		ScopedLock sl(rectLock);

		if(getState() == SUB_READY)
		{
			if(encoder)
			{
				encoder->createUpdate(out);
				
			}
		}
		//clear rect data
	}
};

