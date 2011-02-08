#ifndef _DDSS_TIMER_H_
#define _DDSS_TIMER_H_

#include "Includes.h"
namespace dm
{
	class Timer
	{
	public:
		static u32 currentTimeMillis();
		static String timeStamp();
	};
	class TimeStampGenerator
	{
	public:
		TimeStampGenerator(){ reset(); }
		virtual ~TimeStampGenerator(){ reset(); }
		static u32 getCurrentTimeMillis(){ return Timer::currentTimeMillis(); }
		u32 getTimeStamp(){ u32 t = getCurrentTimeMillis(); return t - startTime; }
		void reset(){ startTime = getCurrentTimeMillis(); }
	private:
		u32 startTime;
	};
};

#endif /*TIMER_H_*/


