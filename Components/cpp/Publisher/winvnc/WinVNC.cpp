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


// WinVNC.cpp

// 24/11/97		WEZ

// WinMain and main WndProc for the new version of WinVNC

////////////////////////////
// System headers
#include "stdhdrs.h"

////////////////////////////
// Custom headers
#include "VSocket.h"
#include "WinVNC.h"

#include "vncServer.h"
#include "vncMenu.h"
#include "vncInstHandler.h"
#include "vncService.h"

extern "C" {
#include "ParseHost.h"
}

#include <iostream>

// Application instance and name
HINSTANCE	hAppInstance;
const char	*szAppName = "DimdimScreenCaster";

DWORD		mainthreadId;

std::string parseArgsForCmd(std::string args, std::string cmd)
{
	size_t cmdLoc = args.find(cmd);
	if (cmdLoc < 0 || cmdLoc > args.length())
		return "";
	size_t valLoc = args.find_first_of(" ", (cmdLoc + cmd.length() + 1));
	if (valLoc < 0 || valLoc > args.length())
		valLoc = args.length();
	return args.substr((cmdLoc + cmd.length() + 1), (valLoc - ((cmdLoc + cmd.length() + 1))));
}

// WinMain parses the command line and either calls the main App
// routine or, under NT, the main service routine.
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, PSTR szCmdLine, int iCmdShow)
{
#ifdef _DEBUG
	{
		// Get current flag
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );

		// Turn on leak-checking bit
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;

		// Set flag to the new value
		_CrtSetDbgFlag( tmpFlag );
	}
#endif

	// Save the application instance and main thread id
	hAppInstance = hInstance;
	mainthreadId = GetCurrentThreadId();

	// Initialise the CurlSocket system
	CurlSocketSystem socksys;
	if (!socksys.Initialised())
	{
		MessageBox(NULL, "Failed to initialise the socket system", szAppName, MB_OK);
		return 0;
	}
	vnclog.Print(LL_STATE, VNCLOG("sockets initialised\n"));

	// Make the command-line lowercase and parse it
	size_t i;
	for (i = 0; i < strlen(szCmdLine); i++)
	{
		szCmdLine[i] = tolower(szCmdLine[i]);
	}

	BOOL argfound = FALSE;
	std::string connectURL("");
	bool cancelConnect = false;

	for (i = 0; i < strlen(szCmdLine); i++)
	{
		if (szCmdLine[i] <= ' ')
			continue;
		argfound = TRUE;

		// Determine the length of current argument in the command line
		size_t arglen = strcspn(&szCmdLine[i], " \t\r\n\v\f");

		if (strncmp(&szCmdLine[i], winvncRunAsUserApp, arglen) == 0 &&
			arglen == strlen(winvncRunAsUserApp))
		{
			// WinVNC is being run as a user-level program
			return WinVNCAppMain();
		}

		if (strncmp(&szCmdLine[i], winvncKillAllClients, arglen) == 0 &&
			arglen == strlen(winvncKillAllClients))
		{
			// NB : This flag MUST be parsed BEFORE "-kill", otherwise it will match
			// the wrong option!

			// Kill all connected clients
			vncService::KillAllClients();
			i += arglen;
			continue;
		}

		if (strncmp(&szCmdLine[i], winvncKillRunningCopy, arglen) == 0 &&
			arglen == strlen(winvncKillRunningCopy))
		{
			vnclog.Print(LL_STATE, "Kill running copy\r\n");
			// Kill any already running copy of WinVNC
			vncService::KillRunningCopy();
			i += arglen;
			continue;
		}
		if (strncmp(&szCmdLine[i], winvncSharePrimary, arglen) == 0 &&
			arglen == strlen(winvncSharePrimary))
		{
			// Show only the primary display to VNC clients
			vnclog.Print(LL_STATE, "Sharing primary display\r\n");
			vncService::PostSharePrimary();
			i += arglen;
			continue;
		}
		if (strncmp(&szCmdLine[i], winvncShareAll, arglen) == 0 &&
			arglen == strlen(winvncShareAll))
		{
			// Show full desktop to VNC clients
			vnclog.Print(LL_STATE, "Sharing full desktop\r\n");
			vncService::PostShareAll();
			i += arglen;
			continue;
		}
		if (strncmp(&szCmdLine[i], winvncShareHandle, arglen) == 0 &&
			arglen == strlen(winvncShareHandle))
		{
			vnclog.Print(LL_STATE, "Sharing a specific handle\r\n");
			// Find a window to share, by its handle
			i += arglen;

			cancelConnect = true;

			int start = i, end;
			while (szCmdLine[start] && szCmdLine[start] <= ' ') start++;
			if (szCmdLine[start] == '"') {
				start++;
				char *ptr = strchr(&szCmdLine[start], '"');
				if (ptr == NULL) {
					end = strlen(szCmdLine);
					i = end;
				} else {
					end = ptr - szCmdLine;
					i = end + 1;
				}
			} else {
				end = start;
				while (szCmdLine[end] > ' ') end++;
				i = end;
			}
			if (end - start > 0) {
				char *handle = new char[end - start + 1];
				if (handle != NULL) {
					strncpy(handle, &szCmdLine[start], end - start);
					handle[end - start] = 0;
					HWND hwndFound = (HWND)atol(handle);
					if (hwndFound != NULL)
						cancelConnect = false;

					if (TRUE == IsIconic(hwndFound))
						ShowWindow(hwndFound, SW_RESTORE);

					::SetForegroundWindow(hwndFound);
					vncService::PostShareWindow(hwndFound);
					delete [] handle;
				}
			}
			continue;
		}
		if (strncmp(&szCmdLine[i], winvncShareWindow, arglen) == 0 &&
			arglen == strlen(winvncShareWindow))
		{
			vnclog.Print(LL_STATE, "Sharing a window by title\r\n");
			// Find a window to share, by its title
			i += arglen;

			cancelConnect = true;	// Ignore the -connect option unless
			// there will be valid window to share

			int start = i, end;
			while (szCmdLine[start] && szCmdLine[start] <= ' ') start++;
			if (szCmdLine[start] == '"') {
				start++;
				char *ptr = strchr(&szCmdLine[start], '"');
				if (ptr == NULL) {
					end = strlen(szCmdLine);
					i = end;
				} else {
					end = ptr - szCmdLine;
					i = end + 1;
				}
			} else {
				end = start;
				while (szCmdLine[end] > ' ') end++;
				i = end;
			}
			if (end - start > 0) {
				char *title = new char[end - start + 1];
				if (title != NULL) {
					strncpy(title, &szCmdLine[start], end - start);
					title[end - start] = 0;
					HWND hwndFound = vncService::FindWindowByTitle(title);
					if (hwndFound != NULL)
						cancelConnect = false;
					vncService::PostShareWindow(hwndFound);
					delete [] title;
				}
			}
			continue;
		}
		if (strncmp(&szCmdLine[i], winvncStress, arglen) == 0 &&
			arglen == strlen(winvncStress) && connectURL.length() == 0)
		{
			vnclog.Print(LL_STATE, "Starting a stress test\r\n");
			// Add a new client to an existing copy of winvnc
			i += arglen;

			// First, we have to parse the command line to get the hostname to use
			int start, end;
			start=i;
			while (szCmdLine[start] && szCmdLine[start] <= ' ') start++;
			end = start;
			while (szCmdLine[end] > ' ') end++;

			// Was there a hostname (and optionally a port number) given?
			if (end-start > 0) 
			{
				connectURL.assign(szCmdLine);
				connectURL.assign(connectURL.substr(strlen(winvncStress) + 1, connectURL.length() - strlen(winvncStress)));
				if (connectURL.substr(connectURL.length() - 1, 1) != std::string("/") && 
					connectURL.substr(connectURL.length() - 2, 1) != std::string("/"))
					connectURL.append("/");
			}
			i = end;
			break;
		}
		if (strncmp(&szCmdLine[i], winvncAddNewClientURL, arglen) == 0 &&
			arglen == strlen(winvncAddNewClientURL) && connectURL.length() == 0)
		{
			// Expecting a URL of type
			// http://ip:port/screenshare<id>/meetingkey/streamID~dimdimID/clientID/sessionID

			connectURL.assign(&szCmdLine[i]);
			
			// Add a new client to an existing copy of winvnc
			i += arglen;

			connectURL.assign(connectURL.substr(strlen(winvncAddNewClientURL) + 1, connectURL.length() - strlen(winvncAddNewClientURL)));

			if (connectURL.find("~") > 0 && connectURL.find("~") < connectURL.length())
			{
				std::string metaData(connectURL);
				metaData.assign(metaData.substr(metaData.find_first_of("~") + 1, metaData.length() - (metaData.find_first_of("~"))));
				connectURL.assign(connectURL.substr(0, connectURL.length() - metaData.length() - 1));

				if (connectURL.substr(connectURL.length() - 1, 1) != std::string("/") && 
					connectURL.substr(connectURL.length() - 2, 1) != std::string("/"))
					connectURL.append("/");

				connectURL.append("~");
				connectURL.append(metaData);
			}
			else
			{
				if (connectURL.substr(connectURL.length() - 1, 1) != std::string("/") && 
					connectURL.substr(connectURL.length() - 2, 1) != std::string("/"))
					connectURL.append("/");
			}
			i = connectURL.length();
			break;
		}
		if (strncmp(&szCmdLine[i], winvncConnect, arglen) == 0 &&
			arglen == strlen(winvncConnect) && connectURL.length() == 0)
		{
			vnclog.Print(LL_STATE, "Connecting a client\r\n");
			// Add a new client to an existing copy of winvnc
			i += arglen;

			// First, we have to parse the command line to get the complete url
			int start, end;
			start=i;
			while (szCmdLine[start] && szCmdLine[start] <= ' ') start++;
			end = start;
			while (szCmdLine[end] > ' ') end++;

			if (end-start > 0) 
			{
				// -connect -host -port -app -confKey -streamID
				// Basic format : -<cmd> <value>
				std::string args(szCmdLine);
				// remove the primary command first
				args.assign(args.substr(strlen(winvncConnect), args.length() - strlen(winvncConnect)));
			
				std::string host = parseArgsForCmd(args, winvncHost);
				std::string port = parseArgsForCmd(args, winvncPort);
				std::string app = parseArgsForCmd(args, winvncApp);
				std::string confKey = parseArgsForCmd(args, winvncConfKey);
				std::string streamID = parseArgsForCmd(args, winvncStreamID);
				if (host.length() == 0 || port.length() == 0 || app.length() == 0 || confKey.length() == 0 || streamID.length() == 0)
				{
				//	MessageBox(NULL, winvncUsageText, "Screencaster Usage", MB_OK | MB_ICONINFORMATION);
					return 0;
				}

				connectURL.assign("http://");
				connectURL.append(host);
				connectURL.append(":");
				connectURL.append(port);
				connectURL.append("/");
				connectURL.append(app);
				connectURL.append("/");
				connectURL.append(confKey);
				connectURL.append("/");
				connectURL.append(streamID);
				connectURL.append("/");
			} 
			i = end;
			break;
		}

		// Either the user gave the -help option or there is something odd on the cmd-line!

		// Show the usage dialog
//		MessageBox(NULL, winvncUsageText, "Screencaster Usage", MB_OK | MB_ICONINFORMATION);
		break;
	}

	// If no arguments were given then just run
	if (!argfound)
		return WinVNCAppMain();

	if (connectURL.length() > 0 && !cancelConnect)
	{
		vncService::PostAddNewClientURL(connectURL);
	}
	return 0;
}

// This is the main routine for WinVNC when running as an application
// (under Windows 95 or Windows NT)
// Under NT, WinVNC can also run as a service.  The WinVNCServerMain routine,
// defined in the vncService header, is used instead when running as a service.

int WinVNCAppMain()
{
	// Set this process to be the last application to be shut down.
	SetProcessShutdownParameters(0x100, 0);

	// Check for previous instances of WinVNC!
	vncInstHandler instancehan;
	if (!instancehan.Init())
	{
		// We don't allow multiple instances!
	//	MessageBox(NULL, "Another instance of Screencaster is already running", szAppName, MB_OK);
		return 0;
	}

	// CREATE SERVER
	vncServer server;

	// Set the name and port number
	server.SetName(szAppName);
	vnclog.Print(LL_STATE, VNCLOG("server created ok\n"));

	// Create tray icon & menu if we're running as an app
	vncMenu *menu = new vncMenu(&server);
	if (menu == NULL)
	{
		vnclog.Print(LL_INTERR, VNCLOG("failed to create tray menu\n"));
		PostQuitMessage(0);
	}

	// Now enter the message handling loop until told to quit!
	MSG msg;
	while (GetMessage(&msg, NULL, 0,0) ) {
	//	vnclog.Print(LL_INTINFO, VNCLOG("message %d received\n"), msg.message);
		TranslateMessage(&msg);	// convert key ups and downs to chars
		DispatchMessage(&msg);
	}

	vnclog.Print(LL_STATE, VNCLOG("shutting down server\n"));

	if (menu != NULL)
		delete menu;

	return msg.wParam;
}
