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


// vncProperties

// Object implementing the Properties dialog for WinVNC.
// The Properties dialog is displayed whenever the user selects the
// Properties option from the system tray menu.
// The Properties dialog also takes care of loading the program
// settings and saving them on exit.

#ifdef HORIZONLIVE
#include "horizon/horizonProperties.h"
#else

class vncProperties;

#if (!defined(_WINVNC_VNCPROPERTIES))
#define _WINVNC_VNCPROPERTIES

// Includes
#include "stdhdrs.h"
#include "vncServer.h"
#include "commctrl.h"
#include <map>
// The vncProperties class itself
class vncProperties
{
public:
	// Constructor/destructor
	vncProperties();
	~vncProperties();

	// Initialisation
	BOOL Init(vncServer *server);

	// Loading & saving of preferences from file
	void LoadSettings(BOOL usersettings);
	void LoadConfigFile(std::map<std::string,int>& paramMap);

	// TRAY ICON MENU SETTINGS
	BOOL AllowProperties() {return m_allowproperties;};
	BOOL AllowShutdown() {return m_allowshutdown;};
	BOOL AllowEditClients() {return m_alloweditclients;};

	void ResetTabId() { m_tab_id = 0; }

	// Implementation
protected:

	// The server object to which this properties object is attached.
	vncServer *			m_server;

	// Flag to indicate whether the currently loaded settings are for
	// the current user, or are default system settings
	BOOL				m_usersettings;

	// Tray icon menu settings
	BOOL				m_allowproperties;
	BOOL				m_allowshutdown;
	BOOL				m_alloweditclients;

	// Making the loaded user prefs active
	void ApplyUserPrefs();

	BOOL m_returncode_valid;
	BOOL m_dlgvisible;

	// STORAGE FOR THE PROPERTIES PRIOR TO APPLICATION
	BOOL m_pref_SockConnect;
	BOOL m_pref_AutoPortSelect;
	LONG m_pref_PortNumber;
	LONG m_pref_HttpPortNumber;
	BOOL m_pref_BeepConnect;
	BOOL m_pref_BeepDisconnect;
	char m_pref_passwd[MAXPWLEN];
	BOOL m_pref_passwd_set;
	char m_pref_passwd_viewonly[MAXPWLEN];
	BOOL m_pref_passwd_viewonly_set;
	BOOL m_pref_CORBAConn;
	UINT m_pref_QuerySetting;
	UINT m_pref_QueryTimeout;
	BOOL m_pref_QueryAccept;
	BOOL m_pref_QueryAllowNoPass;
	UINT m_pref_IdleTimeout;
	BOOL m_pref_RemoveWallpaper;
	BOOL m_pref_BlankScreen;
	BOOL m_pref_EnableFileTransfers;
	BOOL m_pref_EnableRemoteInputs;
	int  m_pref_LockSettings;
	BOOL m_pref_DisableLocalInputs;
	BOOL m_pref_PollUnderCursor;
	BOOL m_pref_PollForeground;
	BOOL m_pref_PollFullScreen;
	BOOL m_pref_PollConsoleOnly;
	BOOL m_pref_PollOnEventOnly;
	BOOL m_pref_PollingCycle;
	BOOL m_pref_DontSetHooks;
	BOOL m_pref_DontUseDriver;
	BOOL m_pref_DriverDirectAccess;
	UINT m_pref_PriorityTime;
	BOOL m_pref_LocalInputPriority;
	BOOL m_pref_AllowLoopback;
	BOOL m_pref_OnlyLoopback;
	BOOL m_pref_RequireAuth;
	BOOL m_pref_Log;
	BOOL m_pref_LogLots;
	int m_pref_Priority;

private:
	// Remember previously selected tab.
	int m_tab_id;
	bool m_tab_id_restore;

	HWND m_hShared;
};

#endif // _WINVNC_VNCPROPERTIES
#endif // HORIZONLIVE
