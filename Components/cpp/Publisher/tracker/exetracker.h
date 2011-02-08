/**	\file	CTracker.h
 *	\brief	Header for class CTracker
 *	\author	Bharat Varma Nadimpalli
 *	\date	12-04-2007
 */

#if (!defined(_DIMDIM_TRACKER))
#define _DIMDIM_TRACKER

#include <string>
#include <windows.h>

class CTracker
{
public:

	virtual ~CTracker();

	static CTracker* getInstance();
	static void removeInstance();
	bool notifyBrowserInterface(std::wstring msg);
	void setExeName(std::wstring exeName) { m_sExeName.assign(exeName); }

	static DWORD WINAPI run(LPVOID lParam);
	DWORD track();
	void start();


	void setTrackEvent();
	void resetTrackEvent();


private:
	CTracker();
	std::wstring m_sExeName;
	static CTracker* instance_;

	HANDLE m_eTracker;
	HANDLE m_eThreadWait;
	HANDLE m_eThreadStop;
	HANDLE m_eTimer;
	HANDLE m_hThread;
};

#endif
