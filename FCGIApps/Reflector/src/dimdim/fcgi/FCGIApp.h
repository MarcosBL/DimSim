#ifndef _DDSS_FCGI_APP_H_
#define _DDSS_FCGI_APP_H_
#include "../common/api.h"
#include "FCGIRequest.h"
#include "FCGIResponse.h"
namespace dm
{
	class FCGIApp
	{
	public:
		const String& getName() const{ return name; }
		bool init(IniFile& iniFile);
		void cleanup();
		void run();
		static void* RunThreadProc(void* p);
		void setToolMode(bool b){ toolMode = b; }
	protected:
		FCGIApp(const String appName);
		virtual ~FCGIApp();
		virtual bool processRequest(FCGIRequest& request, FCGIResponse& respone);
		virtual bool onInit(IniFile& iniFile){ return true; }
		bool isRunning() const{ return running; }
		void setRunning(bool b){ running = b; }
		void run_private();
	private:
		bool toolMode;
		bool threadPool;
		int threadCount;
		Lock acceptLock;
		bool running;
		String name;
		int listenSocket;
		Lock logLock;
		DDSS_FORCE_BY_REF_ONLY(FCGIApp);
	};
};
#endif

