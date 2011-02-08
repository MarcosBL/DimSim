#include <stdafx.h>
#include "Lock.h"

Lock::Lock() : lockObject(0)
{
	lockObject = ::CreateMutex(0,0,0);
}
Lock::~Lock()
{
	if(isValid())
	{
		if (lockObject)
		{
			CloseHandle(lockObject);
			lockObject = INVALID_HANDLE_VALUE;
		}
	}
}
bool Lock::isValid() const
{
	return lockObject != 0  && lockObject != INVALID_HANDLE_VALUE;
}
bool Lock::lock()
{
	return lock(INFINITE);
}
bool Lock::lock(const unsigned int ms)
{
	if(isValid())
	{
		return ::WaitForSingleObject(lockObject,ms) == WAIT_OBJECT_0;
	}
	return false;
}
void Lock::unlock()
{
	if(isValid())
	{
		::ReleaseMutex(lockObject);
	}
}