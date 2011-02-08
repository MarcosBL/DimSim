#ifndef _DDSS_SCREEN_CLIENT_H_
#define _DDSS_SCREEN_CLIENT_H_

#include "../common/api.h"

namespace dm
{
	class Screen;
	class ScreenClient
	{
	public:
		enum Role
		{
			SCR_NONE = 0,
			SCR_PUB,
			SCR_SUB,
		};
		virtual ~ScreenClient();
		virtual bool init() = 0;
		virtual void cleanup() = 0;
		bool handleOpen(const void* buf, size_t len, OutputStream& out);
		bool handlePost(const void* buf, size_t len, OutputStream& out);
		bool handlePoll(OutputStream& out);
		Screen& getScreen();
		const Screen& getScreen() const;
		bool isPub() const{ return (role == SCR_PUB); }
		bool isSub() const{ return (role == SCR_SUB); }
		bool hasRole() const{ return (isPub() || isSub()); }
		const String& getId() const{ return id; }
		u32 getIdleAge() const;
		u32 getStartTime() const{ return startTime; }
		u32 getTimeSinceLastOp() const
		{   
			u32 curTime = Helper::currentTimeMillis();
			if(lastTime > 0)
			{
				return (curTime - lastTime); 
			}
			return lastTime;
		}
		u32 getOpCount() const{ return opCount; }
	protected:
		u32 lastOpTime;
		ScreenClient(Role r, Screen& s);
		void updateStartTime(){ startTime = Helper::currentTimeMillis(); lastTime = 0; }
		void updateLastTime(){ lastTime = Helper::currentTimeMillis(); }
		virtual bool onHandleOpen(const void* buf, size_t len, OutputStream& out) = 0;
		virtual bool onHandlePost(const void* buf, size_t len, OutputStream& out) = 0;
		virtual bool onHandlePoll(OutputStream& out) = 0;
	private:
		u32 opCount;
		u32 startTime;
		u32 lastTime;
		Screen& screen;
		Role role;
		String id;
		DDSS_FORCE_BY_REF_ONLY(ScreenClient);
	};
};

#endif

