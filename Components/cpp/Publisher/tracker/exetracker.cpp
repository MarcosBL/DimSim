#include "stdafx.h"
#include "toolkit\Win32Helper.h"
#include "exetracker.h"

CTracker* CTracker::instance_ = NULL;

const LPTSTR lpszConduit = _T("\\\\.\\pipe\\10df8770-cbeb-11dc-95ff-0800200c9a66");

CTracker::CTracker()
{
	m_eTracker = CreateEvent(NULL,true,false,_T("Tracker"));
	m_eThreadWait = CreateEvent(NULL,true,false,_T("TrackThreadWait"));
	m_eThreadStop = CreateEvent(NULL,true,false,_T("TrackThreadStop"));
	m_eTimer = CreateEvent(NULL,true,false,_T("TrackTimer"));

	m_sExeName.clear();
}

CTracker::~CTracker()
{
	SetEvent(m_eThreadWait);
	::WaitForSingleObject(m_eThreadStop, INFINITE);
	CloseHandle(m_hThread);
	CloseHandle(m_eTracker);
	CloseHandle(m_eThreadStop);
	CloseHandle(m_eThreadWait);
	CloseHandle(m_eTimer);

	m_sExeName.clear();
}

CTracker* CTracker::getInstance()
{
	if (NULL == instance_)
		instance_ = new CTracker();
	return instance_;
}

void CTracker::removeInstance()
{
	if (NULL != instance_)
		delete instance_;
	instance_ = 0;
}

void CTracker::setTrackEvent()
{
	SetEvent(m_eTracker);
}

void CTracker::resetTrackEvent()
{
	ResetEvent(m_eTracker);
}

DWORD CTracker::track()
{
	while (true)
	{
		// Don't bug the publisher component, just because the event is not set. Give it time.
		if (::WaitForSingleObject(m_eTracker, 500) == WAIT_OBJECT_0)
		{
			// Run this atleast 500 ms apart.
			::WaitForSingleObject(m_eTimer, 500);

			HANDLE hProcess = ProcessExaminer::isProgramRunning(_T("dsc.exe"));
			if (NULL == hProcess)
			{
				notifyBrowserInterface(_T("{screencastResult:\"5\"}"));
			}			
		}
		if (::WaitForSingleObject(m_eThreadWait, 0) == WAIT_OBJECT_0)
		{
			::SetEvent(m_eThreadStop);
			break;
		}
	}
	return TRUE;
}

DWORD CTracker::run(LPVOID lParam)
{
	return CTracker::getInstance()->track();
}

void CTracker::start()
{
	m_hThread = CreateThread(NULL, 0, run, &m_eTracker, 0, NULL);
}

bool CTracker::notifyBrowserInterface(std::wstring msg)
{
	HANDLE hPipe; 
	BOOL fSuccess; 
	DWORD cbWritten, dwMode; 

	// Try to open a named pipe; wait for it, if necessary. 

	while (1) 
	{ 
		hPipe = CreateFile(
			lpszConduit,   // pipe name 
			GENERIC_WRITE, 
			0,              // no sharing 
			NULL,           // default security attributes
			OPEN_EXISTING,  // opens existing pipe 
			0,              // default attributes 
			NULL);          // no template file 

		// Break if the pipe handle is valid. 

		if (hPipe != INVALID_HANDLE_VALUE) 
			break; 

		// Exit if an error other than ERROR_PIPE_BUSY occurs. 

		if (GetLastError() != ERROR_PIPE_BUSY) 
		{
			// Error: could not open pipe
			try
			{
				CloseHandle(hPipe); 
			}
			catch(...)
			{
			}
			return 0;
		}

		// All pipe instances are busy, so wait for 20 seconds. 

		if (!WaitNamedPipe(lpszConduit, 20000)) 
		{ 
			// Error: could not open pipe
			try
			{
				CloseHandle(hPipe); 
			}
			catch(...)
			{
			}
			return 0;
		} 
	} 

	// The pipe connected; change to message-read mode. 

	dwMode = PIPE_READMODE_MESSAGE; 
	fSuccess = SetNamedPipeHandleState( 
		hPipe,    // pipe handle 
		&dwMode,  // new pipe mode 
		NULL,     // don't set maximum bytes 
		NULL);    // don't set maximum time 
	if (!fSuccess) 
	{
		// Error: SetNamedPipeHandleState failed
		try
		{
			CloseHandle(hPipe); 
		}
		catch(...)
		{
		}
		return 0;
	}

	// Send a message to the pipe server. 

	fSuccess = WriteFile( 
		hPipe,                  // pipe handle 
		msg.c_str(),             // message 
		(DWORD)(msg.size() * sizeof(wchar_t)), // message length 
		&cbWritten,             // bytes written 
		NULL);                  // not overlapped 
	if (!fSuccess) 
	{
		// Error: WriteFile failed
		try
		{
			CloseHandle(hPipe); 
		}
		catch(...)
		{
		}
		return 0;
	}

	try
	{
		CloseHandle(hPipe); 
	}
	catch(...)
	{
	}

	return 0; 
}
