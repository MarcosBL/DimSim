#ifndef _DDSS_COMMON_STATEFUL_H_
#define _DDSS_COMMON_STATEFUL_H_

#include "Includes.h"
#include "Lock.h"

template<class STATE>
class Stateful
{
private:
	STATE state;
	DDSS_FORCE_BY_REF_ONLY(Stateful);
public:
	Stateful(){}
	virtual ~Stateful(){}
	STATE getState() const{ return state; }
	void setState(STATE s){ state = s; }
};

#endif


