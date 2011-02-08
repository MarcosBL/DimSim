#include "FCGIApp.h"

namespace dm
{
	
	FCGIApp::FCGIApp(const String appName) : running(false), name(appName)
	{
		toolMode = false;
	}
	FCGIApp::~FCGIApp()
	{
	}
	bool FCGIApp::init(IniFile& iniFile)
	{
		DDSS_INFO("FCGIApp")<<"FCGI App("<<name<<") init begins ..."<<std::endl;
		if(FCGX_Init() != 0)
		{
			DDSS_ERR("FCGIApp")<<"FCGX_Init() failed!"<<std::endl;
			return false;
		}
		
		threadPool = iniFile.getBool("FCGI","threadPoolEnabled");
		if(!threadPool)
		{
			threadCount = 0;
		}
		threadCount = iniFile.getInt("FCGI","threadCount",0);
		if(!threadCount)
		{
			threadPool = false;
		}
	
		String socketPath = ":6678";
		int backlog = 15;
		this->listenSocket = 0;
		String listenMode = iniFile.get("FCGI","listenMode");
		if(listenMode == "socket" && !toolMode)
		{
			int listenPort = iniFile.getInt("FCGI","listenPort");
			String listenHost = iniFile.get("FCGI","listenHost");
			char buf[128];
			sprintf(buf,"%s:%d",listenHost.c_str(),listenPort);
			socketPath = buf;
			listenSocket = ::FCGX_OpenSocket(socketPath.c_str(),backlog);
			
			if(!listenSocket)
			{
				DDSS_ERR("FCGIApp")<<"FCGX_OpenSocket("<<socketPath<<","<<backlog<<") failed with return value : "<<listenSocket<<std::endl;
				return false;
			}
			else
			{
				DDSS_INFO("FCGIApp")<<"FCGX_OpenSocket("<<socketPath<<") successful!!"<<std::endl;
			}
		}
		DDSS_INFO("FCGIApp")<<"FCGI App ("<<name<<") initialized!"<<std::endl;
		
		return onInit(iniFile);
	}
	void FCGIApp::cleanup()
	{
	}
	void* FCGIApp::RunThreadProc(void* p)
	{
		FCGIApp* app = (FCGIApp*)p;

		app->run_private();
		return 0;
	}
	void FCGIApp::run()
	{

		setRunning(true);
		if(threadPool)
		{
			for(int i = 0; i < threadCount; i++)
			{
				pthread_t pid;
				pthread_create(&pid, 0, RunThreadProc,this);
			}
		}
		run_private();
	}
	void FCGIApp::run_private()
	{
		FCGX_Request request;
		::FCGX_InitRequest(&request,listenSocket,0);
		int ret = 0;
		int svcCount = 0;
		u32 tid = (u32)pthread_self();
		String uri = "";
		u32 t1 = 0;
		u32 t2 = 0;
		u32 t3 = 0;
		u32 t4 = 0;
		u32 t5 = 0;
		size_t dataLenIn = 0;
		size_t dataLenOut = 0;
		while(isRunning() && ret == 0)
		{
			t1 = Timer::currentTimeMillis();
			{
				ScopedLock sl(acceptLock);
				ret = ::FCGX_Accept_r(&request);
			}
			svcCount++;
			t2 = Timer::currentTimeMillis();
			if(ret == 0)
			{
				fcgi_streambuf cin_fcgi_streambuf(request.in);
		        fcgi_streambuf cout_fcgi_streambuf(request.out);
		        fcgi_streambuf cerr_fcgi_streambuf(request.err);
	
		        FCGIOutputStream out(&cout_fcgi_streambuf);
	        	FCGIOutputStream err(&cerr_fcgi_streambuf);
	        	FCGIInputStream in(&cin_fcgi_streambuf);
				//create the request and response classes
				FCGIRequest req(request,in);
				u32 t3 = Timer::currentTimeMillis();
				dataLenIn = req.getContentLength();
				FCGIResponse resp(request,out);
				uri = req.getRequestURI();	
			
				//call the process request
				if(!this->processRequest(req,resp))
				{
					ScopedLock sl2(logLock);
					DDSS_ERROR("FCGIApp")<<"FCGI-TX-ERROR,"<<t1<<","<<tid<<","<<svcCount<<","<<uri<<","<<dataLenIn<<std::endl;
					DDSS_ERR("FCGIApp")<<"Process Request returned false"<<std::endl;
					out<<"Status: 500\r\nConnection: close\r\nContent-type: text/plain\r\n\r\nFailed to process FCGI request with env values : "<<std::endl;
					FCGIHelper::printEnv(request.envp,out);
					
				}
				else
				{
					dataLenOut = ((ByteBufferOutputStream&)resp.getOutputStream()).get()->getLength();
					t4 = Timer::currentTimeMillis();
					DDSS_VERBOSE("FCGIApp")<<"Sending response!"<<std::endl;
					//send whatever the processed response was to the client
					resp.sendResponse();
					t5 = Timer::currentTimeMillis();
				}
				//finish processing the request
				::FCGX_Finish_r(&request);
				
				u32 acceptTime = t2 > t1?t2 - t1:0;
				u32 readTime = t3 > t2?t3 - t2:0;
				u32 processTime = t4 > t3?t4 - t3:0;
				u32 writeTime = t5 > t4?t5 - t4:0;
				u32 totalTime = t5 > t1?t5-t1:0;
				{
					ScopedLock sl2(logLock);
					DDSS_AUDIT("FCGIApp")<<"FCGI-TX,"<<t1<<","<<tid<<","<<svcCount<<","<<uri<<","<<dataLenIn<<","<<dataLenOut<<","<<acceptTime<<","<<readTime<<","<<processTime<<","<<writeTime<<","<<totalTime<<std::endl;
				}
				
			}
		}
		setRunning(false);
	}
	bool FCGIApp::processRequest(FCGIRequest& request, FCGIResponse& response)
	{
		DDSS_VERBOSE("FCGIApp")<<"Processing request..."<<std::endl;
		std::ostrstream os;
		response.setContentType("text/plain");
		response.setCode(200);
		os<<"REQUEST ENVIRONMENT"<<std::endl;
		os<<"--------------------------"<<std::endl;
		FCGIHelper::printEnv(request.getRequestImpl().envp,os);
		os<<std::endl;
		if(request.getContentLength() > 0)
		{
			os<<"REQUEST BUFFER ("<<request.getContentLength()<<")"<<std::endl;
			os<<"---------------------------------------"<<std::endl;
			////Dump::hexDump(os,request.getContent(),request.getContentLength());
			os<<std::endl;
		}
		os<<std::ends;

		const char* respbuf = os.str();
		os.rdbuf()->freeze(false);
		response.getOutputStream().write(respbuf,strlen(respbuf));
		return true;
	}
};

