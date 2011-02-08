// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

#if (!defined(_DIMDIM_POLLTHREAD_DEFINED))
#define _DIMDIM_POLLTHREAD_DEFINED

#include <omnithread.h>
#include <curl/curl.h>

#include "Helpers.h"

////////////////////////////

class CPollThread
{
private:

	CPollThread();
	static CPollThread* instance;

private:

	HANDLE m_ePoll;
	HANDLE m_eThreadWait;
	HANDLE m_eThreadStop;
	HANDLE m_hThread;
	HANDLE m_eSleepTimer;

	omni_mutex poll_lock;
	CURL* m_handler;

	// Input queue
	VAIOBlock *in_queue;
	omni_mutex in_queue_lock;

public:
	virtual ~CPollThread();

	static CPollThread* getInstance();
	static void removeInstance();

	static DWORD WINAPI run(LPVOID lParam);

	void setPollEvent(bool bSet);

	DWORD poller();
	void start();

	VBool ReadQueued(const char *buff, const VCard bufflen);
	VCard ReadFromQueue(char* buff, VCard bufflen);

	VInt Read();

	bool IsDataAvailable();
};	
#endif