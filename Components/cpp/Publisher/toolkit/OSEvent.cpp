#include <stdafx.h>
#include "OSEvent.h"


OSEvent::OSEvent() : eventObject(INVALID_HANDLE_VALUE)
{
	eventObject = ::CreateEvent(0,1,0,0);
}
OSEvent::~OSEvent()
{
	::CloseHandle(eventObject);
}
bool OSEvent::wait()
{
	return wait(INFINITE);
}
bool OSEvent::wait(unsigned int ms)
{
	if(WaitForSingleObject(eventObject,ms) == WAIT_OBJECT_0)
	{
		//reset();
		return true;
	}
	return false;
}
void OSEvent::setState(bool bSignalled)
{
	if(bSignalled)
	{
		SetEvent(eventObject);
	}
	else
	{
		ResetEvent(eventObject);
	}
}
bool OSEvent::isValid() const
{
	return eventObject && eventObject != INVALID_HANDLE_VALUE;
}
