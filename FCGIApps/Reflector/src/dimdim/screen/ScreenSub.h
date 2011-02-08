#ifndef _DDSS_SCREEN_SUB_H_
#define _DDSS_SCREEN_SUB_H_

#include "ScreenClient.h"
#include "Screen.h"
namespace dm
{
	class Screen;
	class ScreenClient;
	
	class ScreenSub : public ScreenClient
	{
	public:
		friend class Screen;
		enum State
		{
			SUB_INACTIVE = 0,
			SUB_CONNECTED = 1,
			SUB_READY = 2
		};
		ScreenSub(Screen& s);
		virtual ~ScreenSub();
		bool init();
		void cleanup();
		State getState(){ return state; }
		void setState(State s){ state = s; }
		void setJpegLevelOut(int level){ jpegLevelOut = level; }
		
	protected:
		bool onHandleOpen(const void* buf, size_t len, OutputStream& out);
		bool onHandlePost(const void* buf, size_t len, OutputStream& out);
		bool onHandlePoll(OutputStream& out);

		void addRect(ScreenRectangle& r);
		void writeRects(OutputStream& out);

	private:
		ScreenEncoder* encoder;
		Lock rectLock;
		u16 rectCount;
		ByteBuffer rectData;
		State state;
		std::ofstream dumpFile;
		bool flashClient;
		int jpegLevelOut;
		DDSS_FORCE_BY_REF_ONLY(ScreenSub);
	};
};

#endif


