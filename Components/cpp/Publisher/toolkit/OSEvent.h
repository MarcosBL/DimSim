#ifndef _DIMDIM_OS_EVENT_H_
#define _DIMDIM_OS_EVENT_H_

#include <windows.h>

/// a mutually exclusive lock class
class OSEvent
{
public:
	OSEvent();
	virtual ~OSEvent();
	bool wait();
	bool wait(unsigned int ms);
	void setState(bool bSignalled);
	void set(){ setState(true); }
	void reset(){ setState(false); }
	bool isValid() const;
private:
	handle_t eventObject;

	OSEvent(const OSEvent&); 
	const OSEvent& operator = (const OSEvent&);
};

#endif
