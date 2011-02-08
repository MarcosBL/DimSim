#include "Timer.h"
#include <ctime>
namespace dm
{
#ifdef _WIN32
	int DDSS_gettimeofday (struct timeval *tv, void* tz)
	{
	  union {
		__int64 ns100; /*time since 1 Jan 1601 in 100ns units */
		FILETIME ft;
	  } now;

	  GetSystemTimeAsFileTime (&now.ft);
	  tv->tv_usec = (long) ((now.ns100 / 10LL) % 1000000LL);
	  tv->tv_sec = (long) ((now.ns100 - 116444736000000000LL) / 10000000LL);
	  return (0);
	} 
#endif
	
	u32 Timer::currentTimeMillis()
	{
		timeval tv;
		memset(&tv,0,sizeof(tv));
#ifndef _WIN32
		if(gettimeofday(&tv,0) == 0)
#else
		if(DDSS_gettimeofday(&tv,0))
#endif
		{
			return (u32)(tv.tv_sec * 1000 + 0.001 * (f64)tv.tv_usec);
		}
		else
		{
			return (u32)time(0);
		}

	}
	String Timer::timeStamp()
	{
		time_t t = time(0);
		char *buf = ctime(&t);
		if(buf && strlen(buf))
		{
			buf[strlen(buf)-1] = 0;
		}
		return buf?buf:"";
	}

};


