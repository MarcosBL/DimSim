#include "ScreenShareApp.h"

#define DDSS_SWF_TPL_1	"etc/tpl/swf.tpl.1"
#define DDSS_SWF_TPL_2	"etc/tpl/swf.tpl.2"

namespace dm
{
	void* ScreenShareApp::GCThreadProc(void* param)
	{
		ScreenShareApp* app = (ScreenShareApp*)param;
		app->runGC();
		return 0;
	}
	void ScreenShareApp::runGC()
	{
		while(isRunning())
		{
			usleep(pruneIntervalMs * 1000);
			prune();
		}
		DDSS_WARNING("ScreenShareApp")<<"PRUNE/GC THREAD ENDED!!!!"<<std::endl;
		std::cout<<"GC THREAD ENDED!!!!"<<std::endl;
	}
	void ScreenShareApp::prune()
	{
		DDSS_VERBOSE("ScreenShareApp")<<"PRUNE BEGINS!!!"<<std::endl;
		ScopedLock sl(screenLock);

		std::map<String,Screen*>::iterator iter = screens.begin();
		std::vector<std::string> screenIds;
		while(iter != screens.end())
		{
			Screen* screen = iter->second;
			if(screen)
			{
				if(screen->isPubIdleExceeded(maxIdleAgeMs))
				{
					screenIds.push_back(iter->first);
				}
			}
			iter++;
		}
		if(screenIds.size() > 0)
		{
			DDSS_WARNING("ScreenShareApp")<<"PRUNING : "<<screenIds.size()<<" SCREEN!"<<std::endl;
		}
		for(size_t s = 0; s < screenIds.size(); s++)
		{
			
			std::string screenId = screenIds[s];
			DDSS_WARNING("ScreenShareApp")<<"  Screen : "<<screenId<<" pruned!"<<std::endl;
			delete screens[screenId];
			screens.erase(screenId);
		}
		DDSS_VERBOSE("ScreenShareApp")<<"PRUNE BEGINS!!!"<<std::endl;
	}
	bool ScreenShareApp::onInit(IniFile& iniFile)
	{
		startTime = Timer::currentTimeMillis();
		jpegLevelOut = iniFile.getInt("SCREENSHARE","jpegLevelOut",-1);
		
		bool pruneEnable = iniFile.getBool("SCREENSHARE","enablePrune");
		if(pruneEnable)
		{
			pruneIntervalMs=iniFile.getInt("SCREENSHARE","pruneIntervalMs",1500);
		
			maxIdleAgeMs=iniFile.getInt("SCREENSHARE","maxIdleAgeMs",2000);
			DDSS_VERBOSE("ScreenShareApp")<<"GC Params (Prune Interval :  "<<pruneIntervalMs<<") (Max Idle Age : "<<maxIdleAgeMs<<")"<<std::endl;
			pthread_t gcThread;
			pthread_create(&gcThread,0,GCThreadProc,this);
			DDSS_VERBOSE("ScreenShareApp")<<"PRUNE THREAD LAUNCHED!!!"<<std::endl;
		}
		screens.clear();
		
		this->mailboxRoot = iniFile.get("RAD","mailboxRoot","etc/dat/flv");
		std::cout<<"Mailbox Root : "<<this->mailboxRoot<<std::endl;
		return FCGIApp::onInit(iniFile);
	}
	ScreenShareApp::ScreenShareApp() : FCGIApp("dimdim.screenShare")
	{
	}
	ScreenShareApp::~ScreenShareApp()
	{
	}
	bool ScreenShareApp::generateSWFViewerHTML2(FCGIRequest& request, FCGIResponse& response)
	{
		std::ifstream tpl1(DDSS_SWF_TPL_1);
		std::ifstream tpl2(DDSS_SWF_TPL_2);
		if(tpl1.is_open() && tpl2.is_open())
		{
			String uri = request.getRequestURI();
					
			StringTokenizer st(uri,"/");
			
			/*
			 * URL pattern = /screenshareviewer/http/host/port/app/conf/stream/cacheBuster(optional)
			 */
			if(st.countTokens() < 7)
			{
				std::cout<<"Insufficient tokens : "<<st.countTokens()<<std::endl;
				DDSS_ERR("ScreenShare")<<uri<<" has invalid or missing parameters!"<<std::endl;
				return false;
			}
			String appUrl = st.nextToken();
			String protocol = st.nextToken();
			String host = st.nextToken();
			String port = st.nextToken();
			String app = st.nextToken();
			String conf = st.nextToken();
			String stream = st.nextToken();
			
			if(st.hasMoreTokens())
			{
				String cacheBuster = st.nextToken();
			}
			OutputStream& out = response.getOutputStream();
			response.setContentType("text/html");
			response.setCode(200);
			
			std::ostrstream os;
			char buf[10*1024];
			while(!tpl1.eof())
			{
				memset(buf,0,10*1024);
				tpl1.getline(buf,10*1024);
				os<<buf<<std::endl;
			}
			/*
			 <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head>
			<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" /> 
			<title>Dimdim Desktop / Application Sharing</title> 
			<script type="text/javascript" src="/screensharestatic/swfobject.js"></script>
			<style> body { margin: 0px; padding: 0px; border: 0px; }  div { margin: 0px; padding: 0px; } </style>
			</head> 
			<body bgcolor="#ffffff"> 
			<div id="flashcontent">Loading...</div> 
			<script type="text/javascript">
			var so = new SWFObject("/screensharestatic/fvnc.swf","mymovie", "100%", "100%", "9", "#336699");
			so.addParam("FlashVars","url=http://jp4.dimdim.com:8088/screenshare/&confKey=j1&streamName=858b355d96081f397f8b43a8");
			so.write("flashcontent")
			</script></body></html>
			 */
			os<<"so.addParam(\"FlashVars\",\"url="<<protocol<<"://"<<host<<":"<<port<<"/"<<app<<"/&confKey="<<conf<<"&streamName="<<stream<<"\")"<<std::endl;
			while(!tpl2.eof())
			{
				memset(buf,0,10*1024);
				tpl2.getline(buf,10*1024);
				os<<buf<<std::endl;
			}
			
			os<<std::ends;
					
			const char* html = os.str();
			os.rdbuf()->freeze(false);
			
			out.write(html,strlen(html));
			// ----
			return true; 
			
		}
		else
		{
			DDSS_ERROR("generateSWFHTML")<<"Failed to load template files!"<<std::endl;
			return false;
		}
	}
	bool ScreenShareApp::generateSWFViewerHTML(FCGIRequest& request, FCGIResponse& response)
	{
		
		String uri = request.getRequestURI();
		
		StringTokenizer st(uri,"/");
		
		/*
		 * URL pattern = /screenshareviewer/http/host/port/app/conf/stream/cacheBuster(optional)
		 */
		if(st.countTokens() < 7)
		{
			std::cout<<"Insufficient tokens : "<<st.countTokens()<<std::endl;
			DDSS_ERR("ScreenShare")<<uri<<" has invalid or missing parameters!"<<std::endl;
			return false;
		}
		String appUrl = st.nextToken();
		String protocol = st.nextToken();
		String host = st.nextToken();
		String port = st.nextToken();
		String app = st.nextToken();
		String conf = st.nextToken();
		String stream = st.nextToken();
		
		if(st.hasMoreTokens())
		{
			String cacheBuster = st.nextToken();
		}
		
		
		OutputStream& out = response.getOutputStream();
		response.setContentType("text/html");
		response.setCode(200);
		
		std::ostrstream os;
		
		// ---
		os<<"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head>"<<std::endl;
		os<<"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /> "<<std::endl;
		os<<"<title>Dimdim Desktop / Application Sharing</title> "<<std::endl;
		os<<"<script type=\"text/javascript\" src=\"/screensharestatic/swfobject.js\"></script> "<<std::endl;
		os<<"</head> "<<std::endl;
		os<<"<body bgcolor=\"#ffffff\"> "<<std::endl;
		os<<"<div id=\"flashcontent\">Loading...</div> "<<std::endl;
		
		os<<"<script type=\"text/javascript\">"<<std::endl;
		os<<"var so = new SWFObject(\"/screensharestatic/fvnc.swf?url="<<protocol<<"://"<<host<<":"<<port<<"/"<<app<<"/&confKey="<<conf<<"&streamName="<<stream<<"\",\"mymovie\", \"100%\", \"100%\", \"9\", \"#336699\");"<<std::endl;
		os<<"so.write(\"flashcontent\")"<<std::endl;
		os<<"</script></body></html>"<<std::endl;
		os<<std::ends;
		
		const char* html = os.str();
		os.rdbuf()->freeze(false);
		
		out.write(html,strlen(html));
		// ----
		return true; 
	}
	bool ScreenShareApp::handleScreenShareQuery(FCGIRequest& request, FCGIResponse& response)
	{ 
		String uri = request.getRequestURI();
				
		StringTokenizer st(uri,"/");
		
		/*
		 * URL pattern = /screensharequery/conf/stream/cacheBuster(optional)
		 */
		if(st.countTokens() < 3)
		{
			std::cout<<"Insufficient tokens : "<<st.countTokens()<<std::endl;
			DDSS_ERR("ScreenShare")<<uri<<" has invalid or missing parameters!"<<std::endl;
			return false;
		}
		String appUrl = st.nextToken();
		String conf = st.nextToken();
		String stream = st.nextToken();		
		if(st.hasMoreTokens())
		{
			String cacheBuster = st.nextToken();
		}
		
		Screen* screen = this->findScreen(conf,stream);
		OutputStream& out = response.getOutputStream();
		response.setContentType("text/html");
		response.setCode(200);
		
		std::ostrstream os;
		
		if(screen)
		{ 
			os<<"$$SUCCESS$$Found screen for conf = "<<conf<<" and stream = "<<stream<<std::endl; 
		}
		else
		{ 
			os<<"$$ERROR$$ No screen for conf = "<<conf<<" and stream = "<<stream<<std::endl; 
		}
		os<<std::ends;
		
		const char* html = os.str();
		os.rdbuf()->freeze(false);
		
		out.write(html,strlen(html));
		return true; 
	}
	bool ScreenShareApp::handleScreenShareStatus(FCGIRequest& request, FCGIResponse& response)
	{ 
		String uri = request.getRequestURI();
						
		StringTokenizer st(uri,"/");
		
		/*
		 * URL pattern = /screensharestatus/cacheBuster(optional)
		 */
		
		String appUrl = st.nextToken();	
		if(st.hasMoreTokens())
		{
			String cacheBuster = st.nextToken();
		}
		OutputStream& out = response.getOutputStream();
		response.setContentType("text/html");
		response.setCode(200);
		
		std::ostrstream os;
		os<<"<html><head><title>Screen Share status</title></head><body><pre>"<<std::endl;
		os<<"FCGI PID : "<<getpid()<<std::endl;
		os<<"Uptime (ms) : "<<(Timer::currentTimeMillis() - startTime)<<" ms!"<<std::endl;
		
		os<<"Screen Info : "<<std::endl;
		dumpScreenInfo(os);
		os<<std::endl;
		
		os<<"----------------- request environment ---------------------------"<<std::endl;
		FCGIHelper::printEnv(request.getRequestImpl().envp,os);
		os<<"-----------------------------------------------------------------"<<std::endl;
		os<<"</pre></body></html>"<<std::ends;
		
		const char* html = os.str();
		os.rdbuf()->freeze(false);
		out.write(html,strlen(html));
		return true; 
	}
	bool ScreenShareApp::handleScreenShareAdminCmd(FCGIRequest& request, FCGIResponse& response)
	{
		String uri = request.getRequestURI();
	
		StringTokenizer st(uri,"/");
		
		/*
		 * URL pattern = /screenshareadmin/cacheBuster(optional)
		 */
		
		String appUrl = st.nextToken();	
		if(st.hasMoreTokens())
		{
			String cacheBuster = st.nextToken();
		}
		OutputStream& out = response.getOutputStream();
		response.setContentType("text/html");
		response.setCode(200);
		
		std::ostrstream os;
		os<<"<html><head><title>Screen Share status</title></head><body><pre>"<<std::endl;
		os<<"Admin api not implemented yet! When done this will support start, stop, log view etc."<<std::endl;
		os<<"Till then here is some static data about the FCGI process!"<<std::endl;
		os<<"FCGI PID : "<<getpid()<<std::endl;
		os<<"Uptime (ms) : "<<(Timer::currentTimeMillis() - startTime)<<" ms!"<<std::endl;
		os<<"----------------- request environment ---------------------------"<<std::endl;
		FCGIHelper::printEnv(request.getRequestImpl().envp,os);
		os<<"-----------------------------------------------------------------"<<std::endl;
		os<<"</pre></body></html>"<<std::ends;
		os<<std::ends;
		
		const char* html = os.str();
		os.rdbuf()->freeze(false);
		
		out.write(html,strlen(html));
		return true; 
	}
	bool ScreenShareApp::processRequest(FCGIRequest& request, FCGIResponse& response)
	{
		dm::ScreenCmd cmd;
		bool paramFound = false;
		String requestMethod = request.getParam("REQUEST_METHOD",paramFound);
		
		//parse for the various new urls
		
		String uri = request.getRequestURI();
		//DDSS_VERBOSE("ScreenShare")<<"URI : ["<<uri<<"]"<<std::endl;
		if(strncmp(uri.c_str(), "/screenshareviewer", strlen("/screenshareviewer")) == 0)
		{
			//screen share viewer command
			if(!generateSWFViewerHTML2(request,response))
			{
				response.setContentType("text/plain");
				response.setCode(500);
				return false;
			}
			else
			{
				return true;
			}
		}
		else if(strncmp(uri.c_str(), "/screensharequery", strlen("/screensharequery")) == 0)
		{
			//screen share viewer command
			if(!handleScreenShareQuery(request,response))
			{
				response.setContentType("text/plain");
				response.setCode(500);
				return false;
			}
			else
			{
				return true;
			}
		}
		else if(strncmp(uri.c_str(), "/screenshareadmin", strlen("/screenshareadmin")) == 0)
		{
			//screen share viewer command
			if(!handleScreenShareAdminCmd(request,response))
			{
				response.setContentType("text/plain");
				response.setCode(500);
				return false;
			}
			else
			{
				return true;
			}
		}
		else if(strncmp(uri.c_str(),"/screensharestatus", strlen("/screensharestatus")) == 0)
		{
			//screen share viewer command
			if(!handleScreenShareStatus(request,response))
			{
				response.setContentType("text/plain");
				response.setCode(500);
				return false;
			}
			else
			{
				return true;
			}
		}
		
		
		if(cmd.parse(request.getRequestURI()))
		{
			if(cmd.getAppName() == "dimdimBWTest")
			{
				return handleBWTestCmd(cmd,request,response);
			}
			else
			{
				return handleScreenCmd(cmd, request,response);
			}
		}
		else
		{
			DDSS_ERR("ScreenShareApp")<<"URI is not of valid format!"<<std::endl;
			return false;
		}
	}
	bool ScreenShareApp::handleBWTestCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response)
	{
		//DDSS_VERBOSE("ScreenShareApp")"Processing : "<<cmd<<std::endl;
		if(cmd.getCmdName() == "send")
		{
			response.getOutputStream().write("success",strlen("success")+1);
			return true;
		}
		else if(cmd.getCmdName() == "recv")
		{
			size_t size = (size_t)atoi(cmd.getClientId().c_str());
			ByteBuffer bb(size);
			memset(bb.getData(),'m',bb.getLength());
			response.setCode(200);
			response.setContentType("text/html");
			response.getOutputStream().write(bb.getData(),bb.getLength());
			return true;
		}
		else
		{
			return false;
		}
	}
	bool ScreenShareApp::handleScreenCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response)
	{
		DDSS_VERBOSE("ScreenShareApp")<<"Processing : "<<cmd<<std::endl;
		String cmdName = cmd.getCmdName();
		if(cmdName == "open")
		{
			return handleOpenCmd(cmd,request,response);
		}
		else if(cmdName == "close")
		{
			return handleCloseCmd(cmd,request,response);
		}
		else if(cmdName == "send")
		{
			return handleSendCmd(cmd, request, response);
		}
		else if(cmdName == "poll")
		{
			return handlePollCmd(cmd, request, response);
		}
		else
		{
			DDSS_ERR("ScreenShareApp")<<"handleScreenCmd("<<cmd<<") failed. Invalid command name."<<std::endl;
			return false;
		}
	}
	bool ScreenShareApp::handleOpenCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response)
	{
		ScopedLock sl(cmdLock);
		if(cmd.getRole() == "pub")
		{
			//deleteScreen(cmd.getConfKey(), cmd.getStreamName());
			Screen* screen = newScreen(cmd.getConfKey(), cmd.getStreamName());
		
			if(screen)
			{
				if(request.getContent() && request.getContentLength() > 0)
				{
					ScreenPub* pub = screen->createPublisher();
					if(!pub)
					{
						DDSS_ERR("ScreenShareApp")<<"handlClientseOpenCmd("<<cmd<<") failed. pub could not be created!"<<std::endl;
						return false;
					}
					else
					{
						//pub->setMailBoxRoot(mailboxRoot);
						if(!pub->init())
						{
							DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. pub init failed."<<std::endl;
							screen->deletePublisher();
							deleteScreen(cmd.getConfKey(), cmd.getStreamName());
							return false;
						}
						else if(pub->handleOpen(request.getContent(),request.getContentLength(), response.getOutputStream()))
						{
							response.setContentType("application/x-dimdim-screenshare");
							response.setCode(200);
							return true;
						}
						else
						{
							DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. publisher handle open returned false!"<<std::endl;
							screen->deletePublisher();
							deleteScreen(cmd.getConfKey(),cmd.getStreamName());
							return false;
						}
					}
				}
				else
				{
					DDSS_WARN("ScreenShareApp")<<DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. publisher open data missing."<<std::endl;
				
			
				}
			}
			else
			{
				DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. could not create screen! screen already exists!"<<std::endl;

				std::ostrstream errOut;
				errOut<<"Screen "<<cmd.getConfKey()<<"/"<<cmd.getStreamName()<<" already exists!"<<std::ends;
				const char* err = errOut.str();
				errOut.rdbuf()->freeze(false);
				response.getOutputStream().write(err,strlen(err)+1);
				return false;
			}
		}
		else if(cmd.getRole() == "sub")
		{
			DDSS_VERBOSE("ScreenShareApp")<<"SUB OPEN >>>>"<<std::endl;
			Screen* screen = findScreen(cmd.getConfKey(), cmd.getStreamName());
		
			if(screen)
			{
				if(request.getContentLength() > 0)
				{
					String str = request.getContent();
					DDSS_VERBOSE("ScreenShareApp")<<"Received from client sub::open : "<<str<<std::endl;
				}
					
				ScreenSub* sub = screen->createSubscriber();
				if(!sub)
				{
					DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. sub could not be created!"<<std::endl;
					return false;
				}
				else
				{
					DDSS_VERBOSE("ScreenShareApp")<<"SUB::INIT >>>>"<<std::endl;
					if(!sub->init())
					{
						DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. pub init failed."<<std::endl;
						screen->deleteSubscriber(sub->getId());
						return false;
					}
					else if(sub->handleOpen(request.getContent(),request.getContentLength(), response.getOutputStream()))
					{
						DDSS_VERBOSE("ScreenShareApp")<<"SUB::OPEN done!"<<std::endl;
						response.setContentType("application/x-dimdim-screenshare");
						response.setCode(200);
						return true;
					}
					else
					{
						DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. subscriber handle open returned false!"<<std::endl;
						screen->deleteSubscriber(sub->getId());
						return false;
					}
				}
				
			}
			else
			{
				DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. could not find screen!"<<std::endl;
				std::ostrstream errOut;
				errOut<<"Screen "<<cmd.getConfKey()<<"/"<<cmd.getStreamName()<<" does not exist!"<<std::ends;
				const char* err = errOut.str();
				errOut.rdbuf()->freeze(false);
				response.getOutputStream().write(err,strlen(err)+1);
				return false;
			}
		}
		else
		{
			DDSS_ERR("ScreenShareApp")<<"handleOpenCmd("<<cmd<<") failed. invalid role!"<<std::endl;
			return false;
		}
		return false;
	}
	bool ScreenShareApp::handleCloseCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response)
	{
		
		ScopedLock sl(cmdLock);
		
		Screen* screen = findScreen(cmd.getConfKey(),cmd.getStreamName());
		if(screen)
		{
			if(cmd.getRole() == "sub")
			{
				screen->deleteSubscriber(cmd.getClientId());
				return true;
			}
			else if(cmd.getRole() == "pub")
			{
				
				ScreenPub* pub = screen->getPublisher();
				if(pub)
				{
					if(pub->getId() != cmd.getClientId())
					{
						DDSS_WARN("ScreenShareApp")<<"handleCloseCmd("<<cmd<<") failed. Pub has different id ("<<pub->getId()<<")"<<std::endl;
						return false;
					}
					else
					{
						deleteScreen(cmd.getConfKey(),cmd.getStreamName());
						return true;
					}
				}
				else
				{
					DDSS_WARN("ScreenShareApp")<<"handleCloseCmd("<<cmd<<") failed. no pub found!"<<std::endl;
					return false;
				}
			}
			else
			{
				DDSS_WARN("ScreenShareApp")<<"handleCloseCmd("<<cmd<<") failed. Invalid role !"<<std::endl;
				return false;
			}
		}
		else
		{
			DDSS_WARN("ScreenShareApp")<<"handleCloseCmd("<<cmd<<") failed. Screen not found!"<<std::endl;
			return false;
		}
		return false;
	
	}
	bool ScreenShareApp::handleSendCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response)
	{
		{
			ScopedLock sl(cmdLock);
		}
		Screen* screen = findScreen(cmd.getConfKey(),cmd.getStreamName());
		if(screen)
		{
			if(cmd.getRole() == "sub")
			{
				ScreenSub* sub = screen->getSubscriber(cmd.getClientId());
				if(sub)
				{
					return sub->handlePost(request.getContent(), request.getContentLength(), response.getOutputStream());
				}
				else
				{
					DDSS_WARN("ScreenShareApp")<<"handleSendCmd("<<cmd<<") failed. Invalid sub id!"<<std::endl;
					return false;
				}

			}
			else if(cmd.getRole() == "pub")
			{
				
				ScreenPub* pub = screen->getPublisher();
				if(pub)
				{
					if(pub->getId() != cmd.getClientId())
					{
						DDSS_WARN("ScreenShareApp")<<"handleSendCmd("<<cmd<<") failed. Pub has different id ("<<pub->getId()<<")"<<std::endl;
						return false;
					}
					else
					{
						return pub->handlePost(request.getContent(), request.getContentLength(), response.getOutputStream());
					}
				}
				else
				{
					DDSS_WARN("ScreenShareApp")<<"handleSendCmd("<<cmd<<") failed. no pub found!"<<std::endl;
				}
			}
			else
			{
				DDSS_WARN("ScreenShareApp")<<"handleSendCmd("<<cmd<<") failed. Invalid role !"<<std::endl;
				return false;
			}
		}
		else
		{
			DDSS_WARN("ScreenShareApp")<<"handleSendCmd("<<cmd<<") failed. Screen not found!"<<std::endl;
			return false;
		}
		return false;

	}
	void ScreenShareApp::dumpScreenInfo(std::ostream& os)
	{
		ScopedLock sl(cmdLock);
		ScopedLock sl2(screenLock);
		size_t i = 0;
		std::map<String,Screen*>::iterator iter = screens.begin();

		os<<"Total screens : "<<screens.size()<<std::endl;
		if(i < screens.size())
		{
			
			Screen* screen = iter->second;
			if(screen)
			{

				ScreenPub* pub = screen->getPublisher();
				u32 lastT = (pub==0?0:pub->getTimeSinceLastOp());
				u32 age = (pub==0?0:(Helper::currentTimeMillis() - pub->getStartTime()));
				u32 count = (pub == 0? 0: pub->getOpCount());
				os<<"   screen # "<<i<<": "<<iter->first
				  <<"   name : "<<screen->getInfo().getScreenName()
				  <<" / Dimension : "<<screen->getInfo().getWidth()<<"X"<<screen->getInfo().getHeight()
				  <<"/ Age (ms) : "<<age
				  <<" / Sub count : "<<screen->getSubCount()
				  <<" / Time Since Last Op (ms) : "<<lastT
				  <<" / Op count : "<<count
				  <<std::endl;
				os.flush();
				
			}
			else
				
			iter++;
			i++;
		}
		
		
	}
	bool ScreenShareApp::handlePollCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response)
	{
		{
			ScopedLock sl(cmdLock);
		}
		Screen* screen = findScreen(cmd.getConfKey(),cmd.getStreamName());
		if(screen)
		{
			if(cmd.getRole() == "sub")
			{
				DDSS_VERBOSE("ScreenShareApp")<<"i>>>> Sub::POLL begins..."<<std::endl;
				ScreenSub* sub = screen->getSubscriber(cmd.getClientId());
				if(sub)
				{
					ByteBufferOutputStream& bos = (ByteBufferOutputStream&)response.getOutputStream();
					size_t old = bos.get()->getLength();
					bool v =  sub->handlePoll(response.getOutputStream());
					size_t len = bos.get()->getLength();
					DDSS_VERBOSE("ScreenShareApp")<<"i<<<< Sub::POLL ends!!!"<<(len - old)<<" bytes!"<<std::endl;
					return v;
				}
				else
				{
					DDSS_WARN("ScreenShareApp")<<"handlePollCmd("<<cmd<<") failed. Invalid sub id!"<<std::endl;
				}

			}
			else if(cmd.getRole() == "pub")
			{
				
				ScreenPub* pub = screen->getPublisher();
				if(pub)
				{
					if(pub->getId() != cmd.getClientId())
					{
						DDSS_WARN("ScreenShareApp")<<"handlePollCmd("<<cmd<<") failed. Pub has different id ("<<pub->getId()<<")"<<std::endl;
					}
					else
					{
						return pub->handlePoll(response.getOutputStream());
					}
				}
				else
				{
					DDSS_WARN("ScreenShareApp")<<"handlePollCmd("<<cmd<<") failed. no pub found!"<<std::endl;
				}
			}
			else
			{
				DDSS_WARN("ScreenShareApp")<<"handlePollCmd("<<cmd<<") failed. Invalid role !"<<std::endl;
				return false;
			}
		}
		else
		{
			DDSS_WARN("ScreenShareApp")<<"handlePollCmd("<<cmd<<") failed. Screen not found!"<<std::endl;
			return false;
		}
		return false;
	}

	Screen* ScreenShareApp::newScreen(const String confKey, const String streamName)
	{
		ScopedLock sl(screenLock);
		String key = createScreenKey(confKey,streamName);
		std::map<String,Screen*>::iterator iter = screens.find(key);
		if(iter == screens.end())
		{
			DDSS_VERBOSE("ScreenShareApp")<<"Creating new screen : "<<key<<std::endl;
			Screen* s = new Screen(confKey,streamName);
			screens[key] = s;
			DDSS_VERBOSE("ScreenShareApp")<<"Setting JPEG LEVEL OUT for new screen : "<<key<<" to "<<this->jpegLevelOut<<std::endl;
			s->setJpegLevelOut(this->jpegLevelOut);
			s->setMailboxRoot(this->mailboxRoot);
			std::cout<<"Setting mbox root : "<<this->mailboxRoot<<std::endl;
			return s;
		}
		else
		{
			DDSS_WARNING("SD2")<<"Screen key already exitst!"<<std::endl;
			return (Screen*)0;
		}

	}
	Screen* ScreenShareApp::findScreen(const String confKey, const String streamName)
	{
		ScopedLock sl(screenLock);
		String key = createScreenKey(confKey,streamName);
		std::map<String,Screen*>::iterator iter = screens.find(key);
		if(iter != screens.end())
		{
			return iter->second;
		}
		return (Screen*)0;
	}
	void ScreenShareApp::deleteScreen(const String confKey, const String streamName)
	{
		ScopedLock sl(screenLock);
		String key = createScreenKey(confKey,streamName);
		std::map<String,Screen*>::iterator iter = screens.find(key);
		if(screens.size() >  0 &&  iter != screens.end())
		{

			screens.erase(key);
			DDSS_VERBOSE("ScreenShareApp")<<"Deleting screen : "<<key<<"..."<<std::endl;
			Screen* s = iter->second;
			delete s;
			DDSS_VERBOSE("ScreenShareApp")<<"Screen Deleted : "<<key<<std::endl;
			s = 0;
			
		}
	}
	const String ScreenShareApp::createScreenKey(const String confKey, const String streamName)
	{
		String key ="screen=";
		key+=streamName;
		key+="@conf=";
		key+=confKey;
		return key;
	}
};

