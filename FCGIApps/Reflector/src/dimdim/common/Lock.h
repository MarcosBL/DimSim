#ifndef _DDSS_LOCK_H_
#define _DDSS_LOCK_H_
#include "Includes.h"
namespace dm
{

	class Lock
	{
	public:
		Lock();
		virtual ~Lock();
		void lock();
		void unlock();
	private:
#ifndef _WIN32
		pthread_mutex_t lockObject;
#else
		HANDLE lockObject;
#endif
		DDSS_FORCE_BY_REF_ONLY(Lock);
	};
	class ScopedLock
	{
	public:
		ScopedLock(Lock& l) : lck(l){ lck.lock(); }
		virtual ~ScopedLock(){ lck.unlock(); }
	private:
		Lock& lck;
		DDSS_FORCE_BY_REF_ONLY(ScopedLock);
	};
	class RWLock
	{
		public:
			RWLock();
			virtual ~RWLock();
			void lockWrite();
			void lockRead();
			void unlockRead();
			void unlockWrite();
		private:
			pthread_rwlock_t lockObject;
	};
	class ScopedWriteLock
	{
	public:
		ScopedWriteLock(RWLock& l) : lock(l){ lock.lockWrite(); }
		virtual ~ScopedWriteLock(){ lock.unlockWrite(); }
	private:
		RWLock& lock;
	};

	class ScopedReadLock
	{
	public:
		ScopedReadLock(RWLock& l) : lock(l){ lock.lockRead(); }
		virtual ~ScopedReadLock(){ lock.unlockRead(); }
	private:
		RWLock& lock;
	};
}

#endif /*LOCK_H_*/


