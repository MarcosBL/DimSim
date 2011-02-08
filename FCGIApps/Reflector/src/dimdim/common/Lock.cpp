#include "Lock.h"

namespace dm
{


	Lock::Lock()
	{
#ifndef _WIN32
		pthread_mutex_init(&lockObject,0);
#else
		lockObject = ::CreateMutex(0,0,0);
#endif
	}
	
	Lock::~Lock()
	{
#ifndef _WIN32
		pthread_mutex_destroy(&lockObject);
#else
		CloseHandle(lockObject);
#endif
	}
	void Lock::lock()
	{
#ifndef _WIN32
		pthread_mutex_lock(&lockObject);
#else
		WaitForSingleObject(lockObject,INFINITE);
#endif
	}
	void Lock::unlock()
	{
#ifndef _WIN32
		pthread_mutex_unlock(&lockObject);
#else
		ReleaseMutex(lockObject);
#endif
	}

	RWLock::RWLock()
	{
		pthread_rwlock_init(&lockObject,0);
	}
	RWLock::~RWLock()
	{
		pthread_rwlock_destroy(&lockObject);
	}
	void RWLock::lockRead()
	{
		pthread_rwlock_rdlock(&lockObject);
	}
	void RWLock::unlockRead()
	{
		pthread_rwlock_unlock(&lockObject);
	}
	void RWLock::lockWrite()

	{
		pthread_rwlock_wrlock(&lockObject);
	}
	void RWLock::unlockWrite()
	{
		pthread_rwlock_unlock(&lockObject);
	}

};


