/* -*- Mode: C++; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*- */
/* ***** BEGIN LICENSE BLOCK *****
* Version: NPL 1.1/GPL 2.0/LGPL 2.1
*
* The contents of this file are subject to the Netscape Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/NPL/
*
* Software distributed under the License is distributed on an "AS IS" basis,
* WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
* for the specific language governing rights and limitations under the
* License.
*
* The Original Code is mozilla.org code.
*
* The Initial Developer of the Original Code is 
* Netscape Communications Corporation.
* Portions created by the Initial Developer are Copyright (C) 1998
* the Initial Developer. All Rights Reserved.
*
* Contributor(s):
*
* Alternatively, the contents of this file may be used under the terms of
* either the GNU General Public License Version 2 or later (the "GPL"), or 
* the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
* in which case the provisions of the GPL or the LGPL are applicable instead
* of those above. If you wish to allow use of your version of this file only
* under the terms of either the GPL or the LGPL, and not to allow others to
* use your version of this file under the terms of the NPL, indicate your
* decision by deleting the provisions above and replace them with the notice
* and other provisions required by the GPL or the LGPL. If you do not delete
* the provisions above, a recipient may use your version of this file under
* the terms of any one of the NPL, the GPL or the LGPL.
*
* ***** END LICENSE BLOCK ***** */

#include "plugin.h"
#include "nsIServiceManager.h"
#include "nsIMemory.h"
#include "nsISupportsUtils.h" // this is where some useful macros defined

#include <map>
#include <string>
#include <tchar.h>
#include <sstream>
#include "xproperty.h"
#include "Translator.h"
#include "publisherio.h"

#include "nsStringAPI.h"

#include <shlwapi.h>

// service manager which will give the access to all public browser services
// we will use memory service as an illustration
nsIServiceManager * gServiceManager = NULL;

// Unix needs this
#ifdef XP_UNIX
#define MIME_TYPES_HANDLED  "application/npdimdim4"
#define PLUGIN_NAME         "Dimdim Publisher for Mozilla"
#define MIME_TYPES_DESCRIPTION  MIME_TYPES_HANDLED"::"PLUGIN_NAME
#define PLUGIN_DESCRIPTION  PLUGIN_NAME "Dimdim Publisher" 

char* NPP_GetMIMEDescription(void)
{
	return(MIME_TYPES_DESCRIPTION);
}

// get values per plugin
NPError NS_PluginGetValue(NPPVariable aVariable, void *aValue)
{
	NPError err = NPERR_NO_ERROR;
	switch (aVariable) {
	case NPPVpluginNameString:
		*((char **)aValue) = PLUGIN_NAME;
		break;
	case NPPVpluginDescriptionString:
		*((char **)aValue) = PLUGIN_DESCRIPTION;
		break;
	default:
		err = NPERR_INVALID_PARAM;
		break;
	}
	return err;
}
#endif //XP_UNIX

//////////////////////////////////////
//
// general initialization and shutdown
//
NPError NS_PluginInitialize()
{
	// this is probably a good place to get the service manager
	// note that Mozilla will add reference, so do not forget to release
	nsISupports * sm = NULL;

	NPN_GetValue(NULL, NPNVserviceManager, &sm);

	// Mozilla returns nsIServiceManager so we can use it directly; doing QI on
	// nsISupports here can still be more appropriate in case something is changed 
	// in the future so we don't need to do casting of any sort.
	if(sm) {
		sm->QueryInterface(NS_GET_IID(nsIServiceManager), (void**)&gServiceManager);
		NS_RELEASE(sm);
	}

	return NPERR_NO_ERROR;
}

void NS_PluginShutdown()
{
	// we should release the service manager
	NS_IF_RELEASE(gServiceManager);
	gServiceManager = NULL;
}

/////////////////////////////////////////////////////////////
//
// construction and destruction of our plugin instance object
//
nsPluginInstanceBase * NS_NewPluginInstance(nsPluginCreateData * aCreateDataStruct)
{
	if(!aCreateDataStruct)
		return NULL;

	nsPluginInstance * plugin = new nsPluginInstance(aCreateDataStruct->instance);
	return plugin;
}

void NS_DestroyPluginInstance(nsPluginInstanceBase * aPlugin)
{
	if(aPlugin)
		delete (nsPluginInstance *)aPlugin;
}

////////////////////////////////////////
//
// nsPluginInstance class implementation
//
nsPluginInstance::nsPluginInstance(NPP aInstance) : nsPluginInstanceBase(),
mInstance(aInstance),
mInitialized(FALSE),
mScriptablePeer(NULL)
{
	mString[0] = '\0';

	m_property = new CXProperty();
	m_bServerOnline = false;

	CTranslator::setWorkingDirectoryW(L"", L"PROGRAMFILES");
}

nsPluginInstance::~nsPluginInstance()
{
	// mScriptablePeer may be also held by the browser 
	// so releasing it here does not guarantee that it is over
	// we should take precaution in case it will be called later
	// and zero its mPlugin member

	if (TRUE == CPublisherIO::getInstance()->validateRegistration(m_property, true))
		CPublisherIO::removeInstance();

	delete m_property;
	m_property = 0;

	if (NULL != mScriptablePeer)				// Loading a plugin in GWT seems to destroy the scriptable
		mScriptablePeer->SetInstance(NULL);		// peer before the instance is set to NULL - Bharat Varma

	NS_IF_RELEASE(mScriptablePeer);
}

NPBool nsPluginInstance::init(NPWindow* aWindow)
{
	if(aWindow == NULL)
		return FALSE;

	mInitialized = TRUE;
	return TRUE;
}

void nsPluginInstance::shut()
{
	mInitialized = FALSE;
}

NPBool nsPluginInstance::isInitialized()
{
	return mInitialized;
}

void nsPluginInstance::performAction(const nsAString &args, PRInt32 *_retval)
{
	std::map<std::wstring, std::wstring> argMap;
	argMap.clear();

	const PRUnichar* data = new PRUnichar[args.Length() * sizeof(PRUnichar)];
	NS_StringGetData(args, &data); 
	std::wstring localArgs(data);

	BSTR bArgs = ::SysAllocString(localArgs.c_str());

	CTranslator::browserArgsToMapW(bArgs, argMap);
	std::map<std::wstring, std::wstring>::iterator it = argMap.find(L"operation");
	std::map<std::wstring, std::wstring>::iterator regit = argMap.find(L"reg");

	if (it != argMap.end())
	{
		if (0 == it->second.compare(L"screencast"))
		{
			if (1 == m_property->remoteDesktop())
			{
				*_retval = 1;
				return;
			}
			CTranslator::executePublisherRunW();

			if (regit != argMap.end())
			{
				m_property->setRegistration(regit->second);
				if (false == CPublisherIO::getInstance()->validateRegistration(m_property))
				{
					*_retval = -1;
					return;
				}
			}
			else
			{
				*_retval = -1;
				return;
			}

			it = argMap.find(L"action");
			if (it != argMap.end())
			{
				if (0 == it->second.compare(L"share"))
				{
					CTranslator::executeScreencasterRunW();
					CTranslator::executeScreencasterRunTrackW();
					setupDriver();
					//if (1 != m_property->driverInstalled())
					//	CTranslator::invokeDriverInstall();

					it = argMap.find(L"handle");
					if (it != argMap.end())
					{
						LONG handle = StrToIntW(it->second.c_str());
						if (false == m_property->isValidHandle(handle))
						{
							m_property->setScreencastResult(L"{screencastResult:\"4\"}");
							*_retval = 1;
							return;
						}

						if (0 != wcscmp(m_property->getScreencastResult(false), L"{screencastResult:\"1\"}"))
						{
							m_property->setScreencastResult(L"{screencastResult:\"0\"}");
							CTranslator::executeScreencasterShareAndConnectW(it->second, m_property->getScreencastURL());
						}
						else
						{
							CTranslator::executeScreencasterShareW(it->second);
						}

						*_retval = 1;
						return;
					}
				}
				else
				{
					CTranslator::executeScreencasterStopW();
					*_retval = 1;
					return;
				}
			}
		}
		else if (0 == it->second.compare(L"mint"))
		{
			it = argMap.find(L"action");
			if (it != argMap.end())
			{
				if (0 == it->second.compare(L"run"))
				{
					CTranslator::executePublisherRunW();
				}
				else if (0 == it->second.compare(L"installDriver"))
				{
					CTranslator::invokeDriverInstall();
				}
				else if (0 == it->second.compare(L"checkDriver"))
				{
					//*_retval = m_property->driverInstalled();
					*_retval = 1;
					return;
				}
				else
				{
					CTranslator::executePublisherKillW();
				}
			}
			*_retval = 1;
		}
	}

	*_retval = 0;
}

void nsPluginInstance::getProperty(const nsAString &args, nsAString &_retval)
{
	// Three property values may be queried
	//
	// 1. windowList - Return JSON buffer of HANDLE-Window Caption pairs
	// 2. screencastResult


	std::map<std::wstring, std::wstring> argMap;
	argMap.clear();

	const PRUnichar* data = new PRUnichar[args.Length() * sizeof(PRUnichar)];
	NS_StringGetData(args, &data);
	std::wstring localArgs(data);

	BSTR bArgs = ::SysAllocString(localArgs.c_str());

	CTranslator::browserArgsToMapW(bArgs, argMap);
	std::map<std::wstring, std::wstring>::iterator it = argMap.find(L"name");

	if (it != argMap.end())
	{
		if (0 == it->second.compare(L"windowList"))
		{
			_retval = m_property->getWindowData();
		}
		else if (0 == it->second.compare(L"screencastResult"))
		{
			_retval = m_property->getScreencastResult();
		}
		else if (0 == it->second.compare(L"BWProfile"))
		{
			_retval = CTranslator::getScreencastProfile();
		}
		else
		{
			_retval = ::SysAllocString(L"");
		}
	}
	else
	{
		_retval = ::SysAllocString(L"");
	}
}

void nsPluginInstance::setProperty(const nsAString &args, PRInt32 *_retval)
{
	// 3 properties can be set
	// {dcsURL:<value>,dmsURL:<value>,screenURL:<value>}


	std::map<std::wstring, std::wstring> argMap;
	argMap.clear();

	const PRUnichar* data = new PRUnichar[args.Length() * sizeof(PRUnichar)];
	NS_StringGetData(args, &data);
	std::wstring localArgs(data);

	BSTR bArgs = ::SysAllocString(localArgs.c_str());

	CTranslator::jsonArgsToMapW(bArgs, argMap);
	std::map<std::wstring, std::wstring>::iterator it;

	for (it = argMap.begin(); it != argMap.end(); it++)
	{
		std::wstring value(it->second);
		if (0 == it->first.compare(L"dcsURL"))
		{
			m_property->setDCSURL(value);
		}
		else if (0 == it->first.compare(L"dmsURL"))
		{
			m_property->setUploadURL(value);
		}
		else if (0 == it->first.compare(L"screenURL"))
		{
			m_property->setScreencastURL(value);
		}
		else
		{
			if (value.compare(L"0") == 0)
				CTranslator::setScreencastProfile(0, "npDimdimControl.dll", "..\\publisher\\");
			else if (value.compare(L"2") == 0)
				CTranslator::setScreencastProfile(2, "npDimdimControl.dll", "..\\publisher\\");
			else
				CTranslator::setScreencastProfile(1, "npDimdimControl.dll", "..\\publisher\\");
		}
	}

	*_retval = 1;
}

void nsPluginInstance::setupDriver()
{
	m_property->setDriverStatus(0);
	Sleep(100);
	CTranslator::executePublisherCheckDriverW();
	HANDLE driverTimer = CreateEvent(NULL, true, false, _T("driverTimer"));
	int iCount = 0;
	while (true)
	{
		if (0 != m_property->driverInstalled() || iCount >= 15)
		{
			break;
		}
		::WaitForSingleObject(driverTimer, 500);
		iCount += 1;
	}
	CloseHandle(driverTimer);
}

void nsPluginInstance::getVersion(PRInt32* _retval)
{
	*_retval = m_property->getVersion();
}

// ==============================
// ! Scriptability related code !
// ==============================
//
// here the plugin is asked by Mozilla to tell if it is scriptable
// we should return a valid interface id and a pointer to 
// nsScriptablePeer interface which we should have implemented
// and which should be defined in the corressponding *.xpt file
// in the bin/components folder
NPError	nsPluginInstance::GetValue(NPPVariable aVariable, void *aValue)
{
	NPError rv = NPERR_NO_ERROR;

	switch (aVariable) {
	case NPPVpluginScriptableInstance: {
		// addref happens in getter, so we don't addref here
		nsIDimdimControl * scriptablePeer = getScriptablePeer();
		if (scriptablePeer) {
			*(nsISupports **)aValue = scriptablePeer;
		} else
			rv = NPERR_OUT_OF_MEMORY_ERROR;
									   }
									   break;

	case NPPVpluginScriptableIID: {
		static nsIID scriptableIID = NS_IDIMDIMCONTROL_IID;
		nsIID* ptr = (nsIID *)NPN_MemAlloc(sizeof(nsIID));
		if (ptr) {
			*ptr = scriptableIID;
			*(nsIID **)aValue = ptr;
		} else
			rv = NPERR_OUT_OF_MEMORY_ERROR;
								  }
								  break;

	default:
		break;
	}

	return rv;
}

// ==============================
// ! Scriptability related code !
// ==============================
//
// this method will return the scriptable object (and create it if necessary)
nsScriptablePeer* nsPluginInstance::getScriptablePeer()
{
	if (!mScriptablePeer) {
		mScriptablePeer = new nsScriptablePeer(this);
		if(!mScriptablePeer)
			return NULL;

		NS_ADDREF(mScriptablePeer);
	}

	// add reference for the caller requesting the object
	NS_ADDREF(mScriptablePeer);
	return mScriptablePeer;
}
