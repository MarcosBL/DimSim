#include "stdafx.h"
#include <windows.h>
#include "InstanceHandler.h"

const TCHAR mutexName[] = _T("Dimdim_Publisher_Mutex");

CInstanceHandler::CInstanceHandler() : m_instanceMutex(NULL)
{
}

CInstanceHandler::~CInstanceHandler()
{
	release();
}

BOOL CInstanceHandler::init()
{
	m_instanceMutex = CreateMutex(NULL, FALSE, mutexName);
	if (NULL == m_instanceMutex)
	{
		return FALSE;
	}
	
	return (GetLastError() == ERROR_ALREADY_EXISTS)? FALSE : TRUE;
}

DWORD CInstanceHandler::release()
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