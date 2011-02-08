#ifndef _DIMDIM_TOOLKIT_LOCK_H_
#define _DIMDIM_TOOLKIT_LOCK_H_

#include <string>
#include <windows.h>

/// a mutually exclusive lock class
class Lock
{
public:
	Lock();
	virtual ~Lock();
	bool lock();
	bool lock(const unsigned int ms);
	void unlock();
	bool isValid() const;
protected:
	Lock(const std::wstring name);
private:
	handle_t lockObject;

	// Force by reference

	Lock(const Lock&); 
	const Lock& operator = (const Lock&);
};

#endif
