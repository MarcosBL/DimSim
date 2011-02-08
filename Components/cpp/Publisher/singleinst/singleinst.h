#pragma once

#include "resource.h"
#include <windows.h>

#define WM_TRAYNOTIFY	WM_USER+8150
extern HINSTANCE		hAppInstance;

// operations possible
//
// 1. run			
// 2. upload
// 3. stop conversion
// 4. start sharing
// 5. stop sharing
// 6. kill
//
//	-[run|conversion|screencast|kill] [start|stop] [<filename> <filetype>|<handle>] [<uploadurl> <dcsurl>|<screencasturl>]
//


const TCHAR dimdimRun[]					= _T("-run");
const TCHAR dimdimExit[]				= _T("-kill");

const TCHAR dimdimConversion[]			= _T("-conversion");
const TCHAR dimdimScreencast[]			= _T("-screencast");

const TCHAR dimdimMenuHandler[]			= _T("-menu");
const TCHAR dimdimScreencasterMenu[]	= _T("screencaster");
const TCHAR dimdimConverterMenu[]		= _T("converter");
const TCHAR dimdimMenuEnable[]			= _T("enable");
const TCHAR dimdimMenuDisable[]			= _T("disable");
const TCHAR dimdimMenuWarning[]			= _T("warning");

const TCHAR dimdimScreencasterShare[]	= _T("share");
const TCHAR dimdimScreencasterStop[]	= _T("stop");
const TCHAR dimdimScreencasterTrack[]	= _T("track");

const TCHAR dimdimConverterStart[]		= _T("start");
const TCHAR dimdimConverterCancel[]		= _T("cancel");

const TCHAR dimdimScreencasterRun[]		= _T("run");
const TCHAR dimdimScreencasterConnect[]	= _T("connect");
const TCHAR dimdimScreencasterCheckDriver[] = _T("checkdriver");