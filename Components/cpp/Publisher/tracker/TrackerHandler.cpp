#include "stdafx.h"
#include <windows.h>
#include "TrackerHandler.h"

const TCHAR mutexName[] = _T("Dimdim_Tracker_Mutex");

CTrackerHandler::CTrackerHandler() : m_instanceMutex(NULL)
{
}

CTrackerHandler::~CTrackerHandler()
{
	release();
}

BOOL CTrackerHandler::init()
{
	m_instanceMutex = CreateMutex(NULL, FALSE, mutexName);
	if (NULL == m_instanceMutex)
	{
		return FALSE;
	}
	
	return (GetLastError() == ERROR_ALREADY_EXISTS)? FALSE : TRUE;
}

DWORD CTrackerHandler::release()
{
	try
	{
		if (m_instanceMutex)
		{
			CloseHandle(m_instanceMutex);
		}
	}
	catch(...)
	{
	}

	return GetLastError();
}