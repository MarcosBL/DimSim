#ifndef _DIMDIM_TOOLKIT_SCOPED_LOCK_H_
#define _DIMDIM_TOOLKIT_SCOPED_LOCK_H_
#include "Lock.h"


///
///	 A Scoped Lock class which acquires the lock on creation
///  and releases it on destruction.
///  useful for implementing synchronized blocks of code
///  
class ScopedLock
{
public:
	ScopedLock(Lock& lock);
	virtual ~ScopedLock();
private:
	Lock& theLock;

	// Force by reference

	ScopedLock(const ScopedLock&); 
	const ScopedLock& operator = (const ScopedLock&);
};

#endif
