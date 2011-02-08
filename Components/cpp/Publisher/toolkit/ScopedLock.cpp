#include <stdafx.h>
#include "ScopedLock.h"

ScopedLock::ScopedLock(Lock& lock) : theLock(lock)
{
	theLock.lock();
}
ScopedLock::~ScopedLock()
{
	theLock.unlock();
}