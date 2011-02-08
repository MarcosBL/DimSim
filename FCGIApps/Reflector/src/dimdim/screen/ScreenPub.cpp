#include "ScreenPub.h"
#define DDSS_IDENTITY_SEP "____"

namespace dm
{
	
	ScreenPub::ScreenPub(Screen& s) : ScreenClient(ScreenClient::SCR_PUB,s), dumpOn(true)
	{
	}
	ScreenPub::~ScreenPub()
	{
		cleanup();
	}
	bool ScreenPub::init()
	{
		return true;
	}
	void ScreenPub::cleanup()
	{
		if(dumpFile.is_open())
		{
			dumpFile.close();
		}
	}
	bool ScreenPub::onHandleOpen(const void* buf, size_t len, OutputStream& out)
	{
		
		u32 timeStamp = Timer::currentTimeMillis();
		ScreenInfo sinfo;
		ByteBufferInputStream bis((const u8*)buf,len,false);
		sinfo.load(bis);
		
		String dimdimId = "defaultUser";
		String roomId = getScreen().getConfKey();
		String sessionId = "defaultSession";
		String installationId = "";
		if(bis.getRemainingByteCount() > 0)
		{
			//u32 lenRemains = bis.getRemainingByteCount();
			//char *tmp = new char[lenRemains];
			//bis.peek(tmp,lenRemains);
			//std::cout<<"Remaining buffer : "<<lenRemains<<std::endl;
			//Dump::hexDump(std::cout,tmp,lenRemains);
			//std::cout<<"Reading RAD strings\n"<<std::endl;
			size_t len = 0;
			{
				
				bis.readLong((u32*)&len);
				std::cout<<"Length Read : "<<len<<std::endl;
				char *buf = new char[len+1];
				ScopedArray<char> bufPtr(buf);
				memset(buf,0,len+1);
				bis.read(buf,len);
				std::cout<<"dimdim id : ["<<buf<<"]"<<std::endl;
				String identity = buf;
				StringTokenizer st(identity,DDSS_IDENTITY_SEP);
				if(st.countTokens() == 1)
				{
					dimdimId = buf;
					installationId = "";
				}
				else
				{
					installationId = st.nextToken();
					dimdimId = st.nextToken();
				}

                DDSS_INFO("ScreenPub")<<"Install ID ["<<installationId<<"] Dimdim ID : ["<<dimdimId<<"]"<<std::endl;
			}
			len = 0;
			{
			
				bis.readLong((u32*)&len);
                                std::cout<<"Length Read : "<<len<<std::endl;
				char *buf = new char[len+1];
				ScopedArray<char> bufPtr(buf);
				memset(buf,0,len+1);
				bis.read(buf,len);
				std::cout<<"room name : ["<<buf<<"]"<<std::endl;
				roomId = buf;
			}
			len = 0;
			{
				
				bis.readLong((u32*)&len);
				
				std::cout<<"Length Read : "<<len<<std::endl;
				char *buf = new char[len+1];
				ScopedArray<char> bufPtr(buf);
				memset(buf,0,len+1);
				bis.read(buf,len);
				std::cout<<"session id : ["<<buf<<"]"<<std::endl;
				sessionId = buf;
			}
			
		}
		DDSS_VERBOSE("ScreenPub")<<"LOADED SCREEN INFO : "<<sinfo<<std::endl;
		getScreen().init(sinfo);
		getScreen().setRADParams(dimdimId, roomId, sessionId, installationId);
		DDSS_VERBOSE("ScreenPub")<<"SCREEN INIT DONE : "<<getScreen().getInfo()<<std::endl;
		out.write(getId().c_str(),getId().size());
		out.write("\n",1);
		
		if(dumpOn)
		{
				std::cout<<"RAD Dir : "<<getScreen().getRADDir()<<std::endl;
				String dumpFileName = getScreen().getRADDir();
				dumpFileName += "/Inbox/DTP/";
				dumpFileName += getScreen().getStreamName();
				dumpFileName += ".bin";
				std::cout<<"Dumping incoming traffic to : "<<dumpFileName<<"..."<<std::endl;
				dumpFile.open(dumpFileName.c_str(), std::ios::out | std::ios::binary);
				if(!dumpFile.is_open())
				{
					DDSS_WARN("ScreenPub")<<"[DUMP-FAIL] Failed to open : ("<<dumpFileName<<")"<<std::endl;
					dumpOn = false;
				}
		}
		
		if(dumpOn && dumpFile.is_open() && buf)
		{
			ScopedLock sl(dumpLock);
			//std::cout<<"Writing open buffer of length : "<<len<<"bytes!!! "<<std::endl;
			dumpFile.write((const char*)&len,4);
			dumpFile.write((const char*)&timeStamp,4);
			dumpFile.write((const char*)buf,(int)len);
			dumpFile.flush();
		}
		return true;
	}
		
	bool ScreenPub::onHandlePost(const void* buf, size_t len, OutputStream& out)
	{
		u32 timeStamp = Timer::currentTimeMillis();
		if(dumpOn && dumpFile.is_open() && buf && len > 0)
		{

			ScopedLock sl(dumpLock);
			//std::cout<<getId()<<" [DUMP] Post Call "<<len<<" bytes!"<<std::endl;
			//Dump::hexDump(os,buf,len);
			dumpFile.write((const char*)&len,4);
			dumpFile.write((const char*)&timeStamp,4);
			dumpFile.write((const char*)buf,(int)len);
			dumpFile.flush();
		}

		//DDSS_AUDIT("CLIENT")<<"[PUB-RECV],"<<Timer::currentTimeMillis()<<","<<getId()<<","<<(len)<<std::endl;
		
		/*std::cout<<"Incoming "<<len<<" byte rect data"<<std::endl;
		Dump::hexDump(std::cout,buf,len);
		std::cout<<"---- end "<<len<<" byte rect data"<<std::endl;
		*/
		ScopedWriteLock swl(getScreen().getRWLock());
		return getScreen().postData(buf,len, timeStamp);
	}
	bool ScreenPub::onHandlePoll(OutputStream& out)
	{
		//TODO: add logic to send the client mouse/key events etc. when control is added
		return true;
	}
};

