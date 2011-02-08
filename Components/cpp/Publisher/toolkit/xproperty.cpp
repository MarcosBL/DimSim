#include "stdafx.h"
#include "xproperty.h"
#include "MirageManager.h"

#include "Win32Helper.h"
#include "Translator.h"

#include "ScopedLock.h"

const LONG DIMDIM_VERSION_MAJOR		= 4;
const LONG DIMDIM_VERSION_MINOR		= 0;
const LONG DIMDIM_VERSION_MONTH		= 11;
const LONG DIMDIM_VERSION_DATE		= 26;
const LONG DIMDIM_VERSION_YEAR		= 8;

const std::wstring Screencaster(_T("dsc.exe"));

CXProperty::CXProperty()
{
	m_registrationID.clear();

	m_dcsURL.clear();
	m_uploadURL.clear();
	m_screencastURL.clear();
	m_uDriverReady = 0;

	m_screencastResult = _T("{screencastResult:\"0\"}");
}

bool CXProperty::isValidHandle(LONG hWnd)
{
	if (0 == hWnd)
		return true;
	std::map<LONG, std::wstring> appMap;
	appMap.clear();
	DesktopEnumerator::getApplicationMap(appMap);
	
	std::map<LONG, std::wstring>::iterator it = appMap.find(hWnd);
	if (it == appMap.end())
		return false;
	return true;
}

BSTR CXProperty::getWindowData()
{
	std::map<LONG, std::wstring> appMap;
	appMap.clear();
	DesktopEnumerator::getApplicationMap(appMap);
	return CTranslator::applicationMapToSimpleJSONW(appMap, _T("handle"), _T("caption"));
}

BSTR CXProperty::getScreencastResult(bool bExecute)
{
	ScopedLock sLock(propertyLock);
	remoteDesktop();

	if (m_screencastResult.find(_T("{screencastResult:\"1\"}")) == 0)
		CTranslator::executeScreencasterStartTrackW();
	else
		CTranslator::executeScreencasterStopTrackW();

	if ( (0 != m_screencastResult.compare(_T("{screencastResult:\"1\"}"))) &&
		(0 != m_screencastResult.compare(_T("{screencastResult:\"0\"}"))) )
	{
		// Put the console window in the foreground.

		std::vector<HWND> applist;
		DesktopEnumerator::getApplicationList(applist);
		for (size_t i = 0; i < applist.size(); i++)
		{
			ApplicationWindow appwin;
			appwin.loadApplicationData(applist[i]);
			if (appwin.getWindowCaption().find(_T("Welcome to Web Meeting")) >= 0 && 
				appwin.getWindowCaption().find(_T("Welcome to Web Meeting")) <= appwin.getWindowCaption().length())
			{
				if (IsIconic(applist[i]))
					ShowWindow(applist[i], SW_RESTORE);
				::SetForegroundWindow(applist[i]);
			}
		}

		if (false == bExecute)
			return ::SysAllocString(m_screencastResult.c_str());

		if ( (0 == m_screencastResult.compare(_T("{screencastResult:\"3\"}"))) ||
			(0 == m_screencastResult.compare(_T("{screencastResult:\"4\"}"))) || 
			(0 == m_screencastResult.compare(_T("{screencastResult:\"8\"}"))) )
		{
			// No need to handle because dsc isn't there.

			// 2 - screencaster closed correctly.
			// 5 - screencaster not found error.
			// 6 - screencaster already closed because of a locked desktop.

			// Need to close dsc

			// 3 - connection error
			// 4 - window not found error (in case of application sharing)
			// 8  - remote desktop detected. screencaster may or may not be there.

			CTranslator::executeScreencasterStopW();
		}
		else if ((0 == m_screencastResult.compare(_T("{screencastResult:\"9\"}"))) ||
			(0 == m_screencastResult.compare(_T("{screencastResult:\"10\"}"))) )
		{
			// 9  - unsupported dual monitor setup. We need to kill the screencaster.
			// 10 - 16 bit display currently unsupported. we need to kill the screencaster.

			CTranslator::executeScreencasterKillTrackW();
			CTranslator::executeScreencasterStopTrackW();
			CTranslator::executeScreencasterKillW();
		}
	}

	return ::SysAllocString(m_screencastResult.c_str());
}

void CXProperty::setScreencastResult(BSTR result)
{
	ScopedLock sLock(propertyLock);
	m_screencastResult.clear();
	m_screencastResult.assign(result);
}

std::wstring CXProperty::getDCSURL()
{
	return m_dcsURL;
}

std::wstring CXProperty::getUploadURL()
{
	return m_uploadURL;
}

std::wstring CXProperty::getScreencastURL()
{
	return m_screencastURL;
}

void CXProperty::setDCSURL(std::wstring url)
{
	m_dcsURL.clear();
	m_dcsURL.assign(url);
}

void CXProperty::setUploadURL(std::wstring url)
{
	m_uploadURL.clear();
	m_uploadURL.assign(url);
}

void CXProperty::setScreencastURL(std::wstring url)
{
	m_screencastURL.clear();
	m_screencastURL.assign(url);
}

LONG CXProperty::getVersion()
{
	return DIMDIM_VERSION_YEAR + (DIMDIM_VERSION_DATE * 100) + (DIMDIM_VERSION_MONTH * 10000) + 
		(DIMDIM_VERSION_MINOR * 1000000) + (DIMDIM_VERSION_MAJOR * 10000000);
}

UINT CXProperty::driverInstalled()
{
	return m_uDriverReady;
}

UINT CXProperty::desktopAvailable()
{
	return 1;
}

void CXProperty::setDriverStatus(UINT bValue)
{
	m_uDriverReady = bValue;
}

UINT CXProperty::remoteDesktop()
{
	if (1 == ::GetSystemMetrics(SM_REMOTESESSION))
	{
		setScreencastResult(_T("{screencastResult:\"8\"}"));
		return 1;
	}

	return 0;
}