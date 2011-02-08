#ifndef _DIMDIM_XPROPERTY_H_
#define _DIMDIM_XPROPERTY_H_

#include <string>
#include "Lock.h"

class CXProperty
{
private:
	
	std::wstring	m_dcsURL;
	std::wstring	m_uploadURL;
	std::wstring	m_screencastURL;

	std::wstring	m_screencastResult;
	
	UINT			m_uDriverReady;
	std::wstring	m_registrationID;

	Lock propertyLock;
	bool m_bServerStatus;

public:

	CXProperty();
	virtual ~CXProperty() {}

	bool isValidHandle(LONG hWnd);
	BSTR getWindowData();
	BSTR getScreencastResult(bool bExecute = true);

	void setScreencastResult(BSTR result);

	std::wstring getDCSURL();
	std::wstring getUploadURL();
	std::wstring getScreencastURL();

	void setDCSURL(std::wstring url);
	void setUploadURL(std::wstring url);
	void setScreencastURL(std::wstring url);

	LONG getVersion();

	void setDriverStatus(UINT bValue);
	UINT driverInstalled();
	UINT remoteDesktop();
	UINT desktopAvailable();

	void setRegistration(std::wstring reg) {m_registrationID.assign(reg);}
	std::wstring retrieveRegistration() {return m_registrationID;}
};

#endif