// singleinst.cpp : Defines the entry point for the application.
//
#include "stdafx.h"
#include "Emissary.h"
#include "singleinst.h"
#include "InstanceHandler.h"
#include "toolkit/ProfileManager.h"

#include <string>

HINSTANCE hAppInstance;

int PublisherMain();
int APIENTRY _tWinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPTSTR    lpCmdLine,
                     int       nCmdShow)
{
	hAppInstance = hInstance;
	UNREFERENCED_PARAMETER(hPrevInstance);

	std::wstring unmodifiedArgs(lpCmdLine);

	// Make the command-line lowercase and parse it
	size_t i;
	for (i = 0; i < wcslen(lpCmdLine); i++)
	{
		lpCmdLine[i] = tolower(lpCmdLine[i]);
	}

	std::wstring args(lpCmdLine);
	if (0 == args.compare(dimdimRun))
	{
		return PublisherMain();
	}

	// Check if a publisher instance exists
	// Otherwise, don't process any command
	// other than the run command

	CInstanceHandler aInstanceHandler;
	if (TRUE == aInstanceHandler.init())
	{
		aInstanceHandler.release();
		return 0;
	}

	if (0 == args.compare(dimdimExit))
	{
		CEmissary::getInstance()->exitTray();
		CEmissary::removeInstance();
		CInstanceHandler aInstanceHandler;
		aInstanceHandler.release();
		return 1;
	}

	if (0 == args.find(dimdimScreencast))
	{
		//-screencast run
		//-screencast connect <screencastURL>
		//-screencast start <handle>
		//-screencast stop
		//-screencast track

		args = args.substr(wcslen(dimdimScreencast) + 1, args.length() - (wcslen(dimdimScreencast) + 1));

		if (0 == args.find(dimdimScreencasterRun))
		{
			CEmissary::getInstance()->screencastRun();
		}
		else if (0 == args.find(dimdimScreencasterConnect))
		{
			args = args.substr(wcslen(dimdimScreencasterConnect) + 1, args.length() - (wcslen(dimdimScreencasterConnect) + 1));
			CEmissary::getInstance()->screencastConnect(args);
		}
		else if (0 == args.find(dimdimScreencasterShare))
		{
			args = args.substr(wcslen(dimdimScreencasterShare) + 1, args.length() - (wcslen(dimdimScreencasterShare) + 1));
			CEmissary::getInstance()->screencastShare(args);
		}
		else if (0 == args.find(dimdimScreencasterTrack))
		{
			CEmissary::getInstance()->screencastTrack();
		}
		else
		{
			CEmissary::getInstance()->screencastKill();
		}

		return 1;
	}

	if (0 == args.find(dimdimConversion))
	{
		//-conversion start dmsURL confURL filePath
		//-conversion cancel

		unmodifiedArgs = unmodifiedArgs.substr(wcslen(dimdimConversion) + 1, unmodifiedArgs.length() - (wcslen(dimdimConversion) + 1));
		if (0 == unmodifiedArgs.find(dimdimConverterStart))
		{
			unmodifiedArgs = unmodifiedArgs.substr(wcslen(dimdimConverterStart) + 1, unmodifiedArgs.length() - (wcslen(dimdimConverterStart) + 1));
			CEmissary::getInstance()->converterStart(unmodifiedArgs);
		}
		else
		{
			unmodifiedArgs = unmodifiedArgs.substr(wcslen(dimdimConverterCancel) + 1, unmodifiedArgs.length() - (wcslen(dimdimConverterCancel) + 1));
			CEmissary::getInstance()->converterKill(unmodifiedArgs);
		}

		return 1;
	}

	if (0 == args.find(dimdimMenuHandler))
	{
		// -menu <screencaster/converter> <enable/disable>

		args = args.substr(wcslen(dimdimMenuHandler) + 1, args.length() - (wcslen(dimdimMenuHandler) + 1));

		if (0 == args.find(dimdimScreencasterMenu))
		{
			args = args.substr(wcslen(dimdimScreencasterMenu) + 1, args.length() - (wcslen(dimdimScreencasterMenu) + 1));
			if (0 == args.find(dimdimMenuEnable))
				CEmissary::getInstance()->setMenuItemState(_T("screencaster"), _T("enable"));
			else if (0 == args.find(dimdimMenuWarning))
				CEmissary::getInstance()->setMenuItemState(_T("screencaster"), _T("warning"));
			else
				CEmissary::getInstance()->setMenuItemState(_T("screencaster"), _T("disable"));
		}
		else
		{
			args = args.substr(wcslen(dimdimConverterMenu) + 1, args.length() - (wcslen(dimdimConverterMenu) + 1));
			if (0 == args.find(dimdimMenuEnable))
				CEmissary::getInstance()->setMenuItemState(_T("converter"), _T("enable"));
			else
				CEmissary::getInstance()->setMenuItemState(_T("converter"), _T("disable"));
		}

		return 1;
	}

	if (0 <= args.find(dimdimScreencasterCheckDriver))
	{
		CEmissary::getInstance()->manageDriver();
		return 1;
	}

	return 0;
}

int PublisherMain()
{
	// Set this process to be the last application to be shut down.
	SetProcessShutdownParameters(0x100, 0);

	CInstanceHandler aInstanceHandler;
	if (FALSE == aInstanceHandler.init())
	{
		return 0;
	}
 	
	if (!CEmissary::getInstance()->initTray())
		return 0;

	CProfileManager::getInstance()->LoadConfig();

	MSG msg;

	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	return (int) msg.wParam;
}