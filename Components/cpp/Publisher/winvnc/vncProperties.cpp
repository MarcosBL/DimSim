//  Copyright (C) 2002-2003 Constantin Kaplinsky. All Rights Reserved.
//  Copyright (C) 2002 RealVNC Ltd. All Rights Reserved.
//  Copyright (C) 2000 Tridia Corporation. All Rights Reserved.
//  Copyright (C) 1999 AT&T Laboratories Cambridge. All Rights Reserved.
//
//  This file is part of the VNC system.
//
//  The VNC system is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
//  USA.
//
// TightVNC distribution homepage on the Web: http://www.tightvnc.com/
//
// If the source code for the VNC system is not available from the place 
// whence you received this file, check http://www.uk.research.att.com/vnc or contact
// the authors on vnc@uk.research.att.com for information on obtaining it.


// vncProperties.cpp

// Implementation of the Properties dialog!

#include "stdhdrs.h"
#include "lmcons.h"
#include "vncService.h"

#include "WinVNC.h"
#include "vncProperties.h"
#include "vncServer.h"
#include "vncPasswd.h"
#include "commctrl.h"
#include <string>
#include <fstream>

const char NO_PASSWORD_WARN [] = "WARNING : Running WinVNC without setting a password is "
								"a dangerous security risk!\n"
								"Until you set a password, WinVNC will not accept incoming connections.";
const char NO_OVERRIDE_ERR [] = "This machine has been preconfigured with WinVNC settings, "
								"which cannot be overridden by individual users.  "
								"The preconfigured settings may be modified only by a System Administrator.";
const char NO_PASSWD_NO_OVERRIDE_ERR [] =
								"No password has been set & this machine has been "
								"preconfigured to prevent users from setting their own.\n"
								"You must contact a System Administrator to configure WinVNC properly.";
const char NO_PASSWD_NO_LOGON_WARN [] =
								"WARNING : This machine has no default password set.  WinVNC will present the "
								"Default Properties dialog now to allow one to be entered.";
const char NO_CURRENT_USER_ERR [] = "The WinVNC settings for the current user are unavailable at present.\n"
								"If you have started the service manually, please run the Service Helper as well.";
const char CANNOT_EDIT_DEFAULT_PREFS [] = "You do not have sufficient priviliges to edit the default local WinVNC settings.";

const char CONFIG_FILE_NAME[] = "DesktopShare.cfg";
const std::string sSelfExe("dsc.exe");

// Constructor & Destructor
vncProperties::vncProperties()
{
	m_alloweditclients = TRUE;
	m_allowproperties = TRUE;
	m_allowshutdown = TRUE;
	m_dlgvisible = FALSE;
	m_usersettings = TRUE;

	m_tab_id = 0;
	m_tab_id_restore = false;
}

vncProperties::~vncProperties()
{
}

// Initialisation
BOOL
vncProperties::Init(vncServer *server)
{
	// Save the server pointer
	m_server = server;

	// Load the settings from the registry
//	LoadSettings(TRUE);	-- We do this when adding a new client anyways - Bharat Varma

	return TRUE;
}

void vncProperties::LoadConfigFile(std::map<std::string,int>& paramMap)
{
	HMODULE moduleHandle = ::GetModuleHandle(sSelfExe.c_str());
	char exeLoc[1024];
	::GetModuleFileName(moduleHandle, exeLoc, 1024);
	std::string fileName(exeLoc);
	fileName = fileName.substr(0, fileName.length() - strlen(sSelfExe.c_str()));
	fileName.append(CONFIG_FILE_NAME);

	std::ifstream fis(fileName.c_str());
	if (false == fis.is_open())
		return;

	std::string line("");
	while (std::getline(fis,line)) 
	{
		//TODO: add the trim line
		if(line.length() > 0)
		{
			if(line[0] != '#')
			{
				size_t index = line.find_first_of('=');
				if(index < line.size())
				{
					std::string name = line.substr(0,index);
					std::string value = line.substr(index+1);
					int iVal = atoi(value.c_str());
					paramMap.insert(make_pair(name, iVal));
				}
			}
		}
	}

	fis.close();
}

void vncProperties::LoadSettings(BOOL usersettings)
{
	// Load the settings from the registry
	vnclog.Print(LL_INTINFO, VNCLOG("Loading Configuration Settings\n"));

	m_alloweditclients = TRUE;
	m_allowshutdown = TRUE;
	m_allowproperties = TRUE;

	std::map<std::string, int> paramMap;
	paramMap.clear();
	std::map<std::string, int>::iterator it;
	LoadConfigFile(paramMap);


	m_pref_RemoveWallpaper = TRUE;		// Default
	m_pref_BlankScreen = TRUE;		// Default
	m_pref_EnableFileTransfers = TRUE;		// Default
	m_pref_EnableRemoteInputs = TRUE;		// Default
	m_pref_PollingCycle = 300;		// Default
	m_pref_DontUseDriver = FALSE;		// Default
	m_server->SetLoopbackOk(TRUE);		// Default
	m_server->EnableCopyRect(TRUE);		// Default

	it = paramMap.find("Logging");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val >= 2)
			val = 2;
		else
			val = 0;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tLogging=%d\r\n\r\n"), val);
		vnclog.SetMode(val);
	}
	else
	{
		vnclog.SetMode(2);		// Default
	}

	it = paramMap.find("LogLevel");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val > 10)
			val = 10;
		if (val < 0)
			val = 0;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tLogLevel=%d\r\n\r\n"), val);
		vnclog.SetLevel(val);
	}
	else
	{
		vnclog.SetLevel(0);		// Default
	}
	
	it = paramMap.find("OperationType");
	if (it != paramMap.end())
	{
		UINT val = it->second;
		if (val < 1)
			val = 1;
		if (val > 3)
			val = 3;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tOperationType=%d\r\n\r\n"), val);
		m_server->SetOperationType(val);
	}
	else
	{
		m_server->SetOperationType(2);
	}

	it = paramMap.find("EnablePointerAlgorithm");
	if (it != paramMap.end())
	{
		UINT val = it->second;
		if (val < 0)
			val = 0;
		if (val > 1)
			val = 1;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tEnablePointerAlgorithm=%d\r\n\r\n"), val);
		m_server->EnablePointerAlgorithm(val == 1);
	}
	else
	{
		m_server->EnablePointerAlgorithm(FALSE);
	}

	it = paramMap.find("EnableRestrictedColors");
	if (it != paramMap.end())
	{
		UINT val = it->second;
		if (val < 0)
			val = 0;
		if (val > 1)
			val = 1;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tEnableRestrictedColors=%d\r\n\r\n"), val);
		m_server->EnableRestrictedColors(val == 1);
	}
	else
	{
		m_server->EnableRestrictedColors(FALSE);
	}

	it = paramMap.find("MaxRetries");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val < 0)
			val = 0;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tMaxRetries=%d\r\n\r\n"), val);
		m_server->SetMaxRetries(val);
	}
	else
	{
		m_server->SetMaxRetries(2);
	}

	it = paramMap.find("MaxWaitTime");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val < 1)
			val = 1;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tMaxWaitTime=%d\r\n\r\n"), val);
		m_server->SetMaxWaitTime(val);
	}
	else
	{
		m_server->SetMaxWaitTime(7);
	}

	it = paramMap.find("StressCount");
	if (it != paramMap.end())
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("\tStressCount=%d\r\n\r\n"), it->second);
		m_server->SetStressCount(it->second);
	}
	else
	{
		m_server->SetStressCount(20);
	}

	it = paramMap.find("CompressLevel");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val < 0)
			val = 0;
		if (val > 9)
			val = 9;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tCompressLevel=%d\r\n\r\n"), val);
		m_server->SetCompressLevel(val);
	}
	else
	{
		m_server->SetCompressLevel(6);
	}

	it = paramMap.find("JPEGEncodingLevel");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val < -1)
			val = -1;
		if (val > 9)
			val = 9;
		vnclog.Print(LL_DIMDIM, VNCLOG("\tJPEGEncodingLevel=%d\r\n\r\n"), val);
		m_server->SetJPEGLevel(val);
	}
	else
	{
		m_server->SetJPEGLevel(-1);		// disable by default till there is support
	}

	it = paramMap.find("MinTransactionWait");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val < 20)
			val = 20;

		vnclog.Print(LL_DIMDIM, VNCLOG("\tMinTransactionWait=%d\r\n\r\n"), val);
		m_server->SetTransactionWaitTime(val);
	}
	else
	{
		m_server->SetTransactionWaitTime(100);
	}

	it = paramMap.find("BlockSize");
	if (it != paramMap.end())
	{
		int val = it->second;
		if (val < 16)
			val = 16;
		
		val = (val / 16) * 16;

		vnclog.Print(LL_DIMDIM, VNCLOG("\tBlockSize=%d\r\n\r\n"), val);
		m_server->SetBlockSize(val * 1024);
	}
	else
	{
		m_server->SetBlockSize(32 * 1024);
	}

	// Make the loaded settings active..
	ApplyUserPrefs();

	// Note whether we loaded the user settings or just the default system settings
	m_usersettings = usersettings;
}

void
vncProperties::ApplyUserPrefs()
{
	// APPLY DEFAULT SETTINGS TO THE SERVER
	// Default Settings not allowed to be changed.
	// These settings are either fixed or are not used by the stripped VNC code (e.g. m_pref_HttpPortNumber)

	m_pref_SockConnect = FALSE;
	m_pref_AutoPortSelect = FALSE;
	m_pref_PortNumber = 0;
	m_pref_HttpPortNumber = 0;
	m_pref_BeepConnect = FALSE;
	m_pref_BeepDisconnect = FALSE;
	m_pref_passwd_set = FALSE;
	m_pref_passwd_viewonly_set = FALSE;
	m_pref_CORBAConn = FALSE;
	m_pref_QuerySetting=2;
	m_pref_QueryTimeout=30;
	m_pref_QueryAccept=FALSE;
	m_pref_QueryAllowNoPass=FALSE;
	m_pref_IdleTimeout = 0;
	m_pref_LockSettings = 0;
	m_pref_PollUnderCursor = FALSE;
	m_pref_PollForeground = TRUE;
	m_pref_PollFullScreen = FALSE;
	m_pref_PollConsoleOnly = TRUE;
	m_pref_PollOnEventOnly = FALSE;
	m_pref_DontSetHooks = FALSE;
	m_pref_RequireAuth = FALSE;
	m_pref_OnlyLoopback = FALSE;
	m_pref_DisableLocalInputs = FALSE;
	m_pref_LocalInputPriority = FALSE;
	m_pref_DriverDirectAccess = TRUE;
	m_pref_PriorityTime = 3;


	m_server->SetAuthHosts(0);
	m_server->SetAuthRequired(FALSE);
	m_server->SetLoopbackOnly(FALSE);
	m_server->SetDisableTrayIcon(TRUE);
	m_server->SetHttpdEnabled(FALSE, FALSE);
	m_server->SetConnectPriority(1);

	// APPLY THE CACHED PREFERENCES TO THE SERVER
	// Update the connection querying settings
	m_server->SetQuerySetting(m_pref_QuerySetting);
	m_server->SetQueryTimeout(m_pref_QueryTimeout);
	m_server->SetQueryAccept(m_pref_QueryAccept);
	m_server->SetQueryAllowNoPass(m_pref_QueryAllowNoPass);
	m_server->SetAutoIdleDisconnectTimeout(m_pref_IdleTimeout);
	m_server->EnableRemoveWallpaper(m_pref_RemoveWallpaper);
	m_server->SetBlankScreen(m_pref_BlankScreen);
	m_server->EnableFileTransfers(m_pref_EnableFileTransfers);

	// Update the password
	m_server->SetPassword(m_pref_passwd_set, m_pref_passwd);
	m_server->SetPasswordViewOnly(m_pref_passwd_viewonly_set, m_pref_passwd_viewonly);

	// Now change the listening port settings
	m_server->SetAutoPortSelect(m_pref_AutoPortSelect);
	if (!m_pref_AutoPortSelect)
		m_server->SetPorts(m_pref_PortNumber, m_pref_HttpPortNumber);
	
	// Set the beep options
	m_server->SetBeepConnect(m_pref_BeepConnect);
	m_server->SetBeepDisconnect(m_pref_BeepDisconnect);
	
	// Remote access prefs
	m_server->EnableRemoteInputs(m_pref_EnableRemoteInputs);
	m_server->SetLockSettings(m_pref_LockSettings);
	m_server->DisableLocalInputs(m_pref_DisableLocalInputs);
	
	m_server->SetDisableTime(m_pref_PriorityTime);
	m_server->SetPollingCycle(m_pref_PollingCycle);

	m_server->SockConnect(m_pref_SockConnect);
	// Polling prefs
	m_server->PollUnderCursor(m_pref_PollUnderCursor);
	m_server->PollForeground(m_pref_PollForeground);
	m_server->PollFullScreen(m_pref_PollFullScreen);
	m_server->PollConsoleOnly(m_pref_PollConsoleOnly);
	m_server->PollOnEventOnly(m_pref_PollOnEventOnly);
	m_server->DontSetHooks(m_pref_DontSetHooks);
	m_server->DontUseDriver(m_pref_DontUseDriver);
	m_server->DriverDirectAccess(m_pref_DriverDirectAccess);

	m_server->LocalInputPriority(m_pref_LocalInputPriority);
}
