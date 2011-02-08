// tracker.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "exetracker.h"
#include "TrackerHandler.h"

#include <string>

int TrackerMain();
int _tmain(int argc, _TCHAR* argv[])
{
	// -run
	// -start
	// -stop
	// -kill

	if (argc < 2)
		return 0;

	std::wstring cmd(argv[1]);
	if (cmd.compare(_T("-run")) == 0)
	{
		return TrackerMain();
	}

	CTrackerHandler aTrackerHandler;
	if (TRUE == aTrackerHandler.init())
	{
		aTrackerHandler.release();
		return 0;
	}

	if (cmd.compare(_T("-start")) == 0)
	{
		CTracker::getInstance()->setTrackEvent();
		return 1;
	}
	else if (cmd.compare(_T("-stop")) == 0)
	{
		CTracker::getInstance()->resetTrackEvent();
		return 1;
	}
	if (cmd.compare(_T("-kill")) == 0)
	{
		CTracker::removeInstance();
		CTrackerHandler aTrackerHandler;
		aTrackerHandler.release();
		return 1;
	}
	return 0;
}

int TrackerMain()
{

	CTrackerHandler aTrackerHandler;
	if (FALSE == aTrackerHandler.init())
	{
		return 0;
	}

	CTracker::getInstance()->start();
	MSG msg;

	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	return (int) msg.wParam;
}