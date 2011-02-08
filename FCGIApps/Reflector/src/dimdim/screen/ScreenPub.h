#ifndef _DDSS_SCREEN_PUB_H_
#define _DDSS_SCREEN_PUB_H_

#include "ScreenClient.h"
#include "Screen.h"

namespace dm
{
	class Screen;
	class ScreenClient;
	class ScreenPub : public ScreenClient
	{
	public:
		ScreenPub(Screen& s);
		virtual ~ScreenPub();
		bool init();
		void cleanup();
		
		bool isDumpOn() const{ return dumpOn; }
		void setDumpOn(bool b){ dumpOn = b; }
	protected:
		bool onHandleOpen(const void* buf, size_t len, OutputStream& out);
		bool onHandlePost(const void* buf, size_t len, OutputStream& out);
		bool onHandlePoll(OutputStream& out);
	private:
		std::ofstream dumpFile;
		Lock dumpLock;
		bool dumpOn;
		DDSS_FORCE_BY_REF_ONLY(ScreenPub);
	};
};

#endif


