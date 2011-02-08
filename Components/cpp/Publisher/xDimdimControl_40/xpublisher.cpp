// xpublisher.cpp : Implementation of Cxpublisher
#include "stdafx.h"
#include "xpublisher.h"

#include <map>
#include <string>

// Cxpublisher

STDMETHODIMP Cxpublisher::getProperty(BSTR args, BSTR* buf)
{

	// Three property values may be queried
	//
	// 1. windowList - Return JSON buffer of HANDLE-Window Caption pairs
	// 2. screencastResult

	std::map<std::wstring, std::wstring> argMap;
	argMap.clear();

	CTranslator::browserArgsToMapW(args, argMap);
	std::map<std::wstring, std::wstring>::iterator it = argMap.find(_T("name"));

	if (it != argMap.end())
	{
		if (0 == it->second.compare(_T("windowList")))
		{
			*buf = m_property->getWindowData();
		}
		else if (0 == it->second.compare(_T("screencastResult")))
		{
			*buf = m_property->getScreencastResult();
		}
		else if (0 == it->second.compare(_T("BWProfile")))
		{
			*buf = CTranslator::getScreencastProfile();
		}
		else
		{
			*buf = ::SysAllocString(_T(""));
		}
	}
	else
	{
		*buf = ::SysAllocString(_T(""));
	}
	return S_OK;
}

STDMETHODIMP Cxpublisher::getVersion(LONG* retval)
{
	*retval = m_property->getVersion();
	return S_OK;
}

STDMETHODIMP Cxpublisher::performAction(BSTR args, LONG* retval)
{
	std::map<std::wstring, std::wstring> argMap;
	argMap.clear();

	CTranslator::browserArgsToMapW(args, argMap);
	std::map<std::wstring, std::wstring>::iterator it = argMap.find(_T("operation"));
	std::map<std::wstring, std::wstring>::iterator regit = argMap.find(_T("reg"));

	if (it != argMap.end())
	{
		if (0 == it->second.compare(_T("screencast")))
		{
			if (1 == m_property->remoteDesktop())
			{
				*retval = 1;
				return S_OK;
			}

			CTranslator::executePublisherRunW();
			
			if (regit != argMap.end())
			{
				m_property->setRegistration(regit->second);
				if (false == CPublisherIO::getInstance()->validateRegistration(m_property))
				{
					*retval = -1;
					return S_OK;
				}
			}
			else
			{
				*retval = -1;
				return S_OK;
			}

			it = argMap.find(_T("action"));
			if (it != argMap.end())
			{
				if (0 == it->second.compare(_T("share")))
				{
					CTranslator::executeScreencasterRunW();
					CTranslator::executeScreencasterRunTrackW();
					setupDriver();
					/*if (1 != m_property->driverInstalled())
					CTranslator::invokeDriverInstall();*/

					it = argMap.find(_T("handle"));
					if (it != argMap.end())
					{
						LONG handle = StrToLong(it->second.c_str());
						if (false == m_property->isValidHandle(handle))
						{
							m_property->setScreencastResult(_T("{screencastResult:\"4\"}"));
							*retval = 1;
							return S_OK;
						}

						if (0 != wcscmp(m_property->getScreencastResult(false), _T("{screencastResult:\"1\"}")))
						{
							m_property->setScreencastResult(_T("{screencastResult:\"0\"}"));
							CTranslator::executeScreencasterShareAndConnectW(it->second, m_property->getScreencastURL());
						}
						else
						{
							CTranslator::executeScreencasterShareW(it->second);
						}

						*retval = 1;
						return S_OK;
					}
				}
				else
				{
					CTranslator::executeScreencasterStopW();
					*retval = 1;
					return S_OK;
				}
			}
		}
		else if (0 == it->second.compare(_T("mint")))
		{

			it = argMap.find(_T("action"));
			if (it != argMap.end())
			{
				if (0 == it->second.compare(_T("run")))
				{
					CTranslator::executePublisherRunW();
				}
				else if (0 == it->second.compare(_T("installDriver")))
				{
					CTranslator::invokeDriverInstall();
				}
				else if (0 == it->second.compare(_T("checkDriver")))
				{
					//*retval = m_property->driverInstalled();
					*retval = 1;
					return S_OK;
				}
				else
				{
					CTranslator::executePublisherKillW();
				}
			}
			*retval = 1;
			return S_OK;
		}
	}

	*retval = 0;
	return S_OK;
}

STDMETHODIMP Cxpublisher::setProperty(BSTR args, LONG* retval)
{
	// TODO: Add your implementation code here

	// 3 properties can be set
	// {dcsURL:<value>,dmsURL:<value>,screenURL:<value>,screenProfile:<value>}

	std::map<std::wstring, std::wstring> argMap;
	argMap.clear();

	CTranslator::jsonArgsToMapW(args, argMap);
	std::map<std::wstring, std::wstring>::iterator it;

	for (it = argMap.begin(); it != argMap.end(); it++)
	{
		std::wstring value(it->second);
		if (0 == it->first.compare(_T("dcsURL")))
		{
			m_property->setDCSURL(value);
		}
		else if (0 == it->first.compare(_T("dmsURL")))
		{
			m_property->setUploadURL(value);
		}
		else if (0 == it->first.compare(_T("screenURL")))
		{
			m_property->setScreencastURL(value);
		}
		else
		{
			if (value.compare(_T("0")) == 0)
				CTranslator::setScreencastProfile(0, "xDimdimControl_40.dll");
			else if (value.compare(_T("2")) == 0)
				CTranslator::setScreencastProfile(2, "xDimdimControl_40.dll");
			else
				CTranslator::setScreencastProfile(1, "xDimdimControl_40.dll");
		}
	}

	*retval = 1;

	return S_OK;
}
