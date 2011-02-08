#include "stdafx.h"
#include "Emissary.h"
#include "Publisher.h"
#include "..\toolkit\Registry.h"
#include <fstream>

CEmissary* CEmissary::_emiInstance = NULL;

CEmissary::CEmissary() : m_pubTray (NULL)
{
	CRegistry reg;
	bool retval = reg.Open(_T("Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings"), HKEY_CURRENT_USER);
	if (retval == false)
		return;
	std::wstring acu = (std::wstring)reg[_T("AutoConfigURL")];
	reg.Close();

	if (acu.length() > 0)
	{
		std::string sFileName(getenv("APPDATA"));
		sFileName.append("\\Dimdim\\proxyloc");
		std::wofstream fos(sFileName.c_str());
		fos << acu.c_str();
		fos.close();
	}
}

CEmissary::~CEmissary()
{
	cleanup();
}

CEmissary* CEmissary::getInstance()
{
	if (NULL ==_emiInstance)
		_emiInstance = new CEmissary();
	return _emiInstance;
}

void CEmissary::removeInstance()
{
	if (NULL != _emiInstance)
		delete _emiInstance;
	_emiInstance = 0;
}

bool CEmissary::initTray()
{
	CPublisherTray* tray = NULL;
	tray = new CPublisherTray();
	if (!tray)
		return false;

	return true;
}

void CEmissary::converterStart(std::wstring args)
{
	CPublisher::getInstance()->executeConverter(_T("convert"), args);
}

void CEmissary::converterKill(std::wstring args)
{
	CPublisher::getInstance()->executeConverter(_T("cancel"), args);
}

void CEmissary::screencastRun()
{
	CPublisher::getInstance()->executeScreencaster(_T("run"));
}

void CEmissary::screencastConnect(std::wstring screencastURL)
{
	CPublisher::getInstance()->executeScreencaster(_T("url"), screencastURL);
}

void CEmissary::manageDriver()
{
	if (1 != CPublisher::getInstance()->isDriverInstalled())
	{
		CPublisher::getInstance()->invokeDriverInstall();
	}
	CPublisher::getInstance()->notifyBrowserInterface(_T("driverready"));
}

void CEmissary::screencastShare(std::wstring handle)
{
	if (0 == handle.compare(_T("0")))
		CPublisher::getInstance()->executeScreencaster(_T("shareprimary"));
	else
		CPublisher::getInstance()->executeScreencaster(_T("sharehandle"), handle);

//	if (0 == handle.compare(_T("0")))
//		CPublisher::getInstance()->executeScreencaster(_T("shareall"));
//	else
//		CPublisher::getInstance()->executeScreencaster(_T("sharehandle"), handle);		
}

void CEmissary::screencastTrack()
{
	CPublisher::getInstance()->screencastTrack();
}

void CEmissary::screencastKill()
{
	CPublisher::getInstance()->executeScreencaster(_T("kill"));
}

void CEmissary::exitTray()
{
	HWND hwnd;

	while ((hwnd = FindWindow(DIMDIM_PUBLISHER_TRAY_CLASS_NAME, NULL)) != NULL)
	{
		// Post the message
		PostMessage(hwnd, WM_CLOSE, 0, 0);
		Sleep(1000);
	}
}

void CEmissary::setMenuItemState(std::wstring itemString, std::wstring state)
{
	HWND hwnd;
	
	if ((hwnd = FindWindow(DIMDIM_PUBLISHER_TRAY_CLASS_NAME, NULL)) == NULL)
		return;

	if (itemString.compare(_T("screencaster")) == 0)
	{
		if (state.compare(_T("enable")) == 0)
			PostMessage(hwnd, WM_DIMDIM_SCREENCASTER_PROGRESS, 0, 0);
		else if (state.compare(_T("warning")) == 0)
			PostMessage(hwnd, WM_DIMDIM_SCREENCASER_FORCELOW, 0, 0);
		else
			PostMessage(hwnd, WM_DIMDIM_IDLE, 0, 0);
	}
	else
	{
		if (state.compare(_T("enable")) == 0)
			PostMessage(hwnd, WM_DIMDIM_CONVERTER_PROGRESS, 0, 0);
		else
			PostMessage(hwnd, WM_DIMDIM_IDLE, 0, 0);
	}
}

void CEmissary::cleanup()
{
	if (m_pubTray)
		delete m_pubTray;
	m_pubTray = NULL;
}

void CEmissary::notifyBrowserInterface(std::wstring msg)
{
	CPublisher::getInstance()->notifyBrowserInterface(msg);
}