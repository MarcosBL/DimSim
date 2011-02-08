#ifndef _DDSS_SCREEN_SHARE_APP_H_
#define _DDSS_SCREEN_SHARE_APP_H_
#include "../fcgi/api.h"
#include "ScreenCmd.h"
#include "Screen.h"
#include "ScreenClient.h"
#include "ScreenSub.h"
#include "ScreenPub.h"
namespace dm
{
	class ScreenShareApp : public FCGIApp
	{
	public:
		typedef FCGIApp super;
		ScreenShareApp();
		virtual ~ScreenShareApp();
	protected:
		bool onInit(IniFile& iniFile);
		bool processRequest(FCGIRequest& request, FCGIResponse& response);
		bool handleScreenCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response);
		bool handleBWTestCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response);
		bool handleOpenCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response);
		bool handleCloseCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response);
		bool handleSendCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response);
		bool handlePollCmd(ScreenCmd& cmd, FCGIRequest& request, FCGIResponse& response);
		void runGC();
	public:
		Screen* newScreen(const String confKey, const String streamName);
		Screen* findScreen(const String confKey, const String streamName);
		void deleteScreen(const String confKey, const String streamName);
		
		
		static const String createScreenKey(const String confKey, const String streamName);
		static void* GCThreadProc(void* param);
		void prune();

		void dumpScreenInfo(std::ostream& os);

		bool generateSWFViewerHTML(FCGIRequest& request, FCGIResponse& response);
		bool generateSWFViewerHTML2(FCGIRequest& request, FCGIResponse& response);
		bool handleScreenShareQuery(FCGIRequest& request, FCGIResponse& response);
		bool handleScreenShareAdminCmd(FCGIRequest& request, FCGIResponse& response);
		bool handleScreenShareStatus(FCGIRequest& request, FCGIResponse& response);
		const String& getMailBoxRoot() const{ return mailboxRoot; }		
	private:
		Lock screenLock, cmdLock;
		std::map<String,Screen*> screens;
		u32 maxIdleAgeMs;
		u32 pruneIntervalMs;
		s32 jpegLevelOut;
		u32 startTime;
		String mailboxRoot;
		DDSS_FORCE_BY_REF_ONLY(ScreenShareApp);
	};
};
#endif

