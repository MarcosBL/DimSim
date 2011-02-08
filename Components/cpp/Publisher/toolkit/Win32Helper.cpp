#include "Win32Helper.h"
#include "psapi.h"
#pragma comment(lib, "psapi.lib")

#include <tchar.h>
ApplicationWindow::ApplicationWindow()
{
}
ApplicationWindow::~ApplicationWindow()
{
	reset();
}
void ApplicationWindow::reset()
{
	windowCaption = _T("");
	windowHandle = (HWND)INVALID_HANDLE_VALUE;
}
void ApplicationWindow::reloadData()
{
	if(isValid())
	{
		if(isDesktopData())
		{
			windowCaption = _T("Desktop");
			WindowExaminer::getDesktopDimensions(width,height);
			xPosition = yPosition = 0;
		}
		else
		{
			TCHAR tmpName[1024];
			memset(tmpName,0,1024);
			::GetWindowText(windowHandle,tmpName,1023);
			windowCaption = tmpName;

			for (size_t i = 0; i < windowCaption.length(); i++)
			{
				if (windowCaption.at(i) < 32 || windowCaption.at(i) > 127)
				{
					if (windowCaption.at(i) == '\0')
						continue;
					windowCaption.at(i) = ' ';
				}
			}

			RECT rect;
			memset(&rect,0,sizeof(rect));
			::GetWindowRect(windowHandle,&rect);
			//std::cout<<" Window ["<<tmpName<<"] - ("<<rect.left<<","<<rect.top<<") ("<<rect.right<<","<<rect.top<<")"<<std::endl;
			width = rect.right - rect.left;
			height = rect.bottom - rect.top;
			xPosition = rect.left;
			yPosition = rect.top;
		}
	}
	else
	{
		reset();
	}
}
bool ApplicationWindow::isVisible() const
{
	if(!isValid())
	{
		return false;
	}
	if(isDesktopData())
	{
		return true;
	}
	BOOL icon = IsIconic(windowHandle);
	if(width <=0 || height <= 0)
	{
		if(!icon)
		{
			return false;
		}
	}
	if(xPosition < 0 || yPosition < 0)
	{
		if(!icon)
		{
			(xPosition + width) > 0 && (yPosition + height) > 0;
		}

	}
	if(IsWindow(windowHandle) && IsWindowVisible(windowHandle))
	{
		return true;
	}
	else
	{
		return false;
	}
	return true;
}

size_t DesktopEnumerator::getApplicationMap(std::map<LONG, std::wstring>& appMap)
{
	appMap.clear();
	// ------------------------
	//mohaps: for blocking display of "Entire Desktop" in App List
	//uncomment following line to add desktop back again
	//
	//windowHandles.push_back(HWND_DESKTOP);
	//
	// end block
	// ------------------------

	std::vector<ApplicationWindow> appWindows;

	for (size_t s = 0; s < appWindows.size(); s++)
	{
		appWindows[s].reset();
	}
	appWindows.clear();

	WindowExaminer::enumWindows(appWindows);

	for (size_t s = 0; s < appWindows.size(); s++)
	{
		if(appWindows[s].isValid())
			appMap[(LONG)appWindows[s].getWindowHandle()] = appWindows[s].getWindowCaption();
	}
	return appMap.size();
}

size_t DesktopEnumerator::getApplicationList(std::vector<HWND>& windowHandles)
{
	windowHandles.clear();
	// ------------------------
	//mohaps: for blocking display of "Entire Desktop" in App List
	//uncomment following line to add desktop back again
	//
	//windowHandles.push_back(HWND_DESKTOP);
	//
	// end block
	// ------------------------
	std::vector<ApplicationWindow> appWindows;

	for (size_t s = 0; s < appWindows.size(); s++)
	{
		appWindows[s].reset();
	}
	appWindows.clear();

	WindowExaminer::enumWindows(appWindows);

	for(size_t s = 0; s < appWindows.size(); s++)
	{
		if(appWindows[s].isValid())
		{
			windowHandles.push_back(appWindows[s].getWindowHandle());
		}
	}

	return windowHandles.size();
}

std::wstring DesktopEnumerator::getProgramFromHandle(HWND hWnd)
{
	std::wstring programName(_T(""));
	ProcessExaminer::getProgramFromHandle(hWnd, &programName);
	return programName;
}

void WindowExaminer::getDesktopDimensions(int& width, int& height)
{
	width = 0;
	height = 0;
	//create the display dc
	HDC hDisplayDCLocal = CreateDC(_T("DISPLAY"),NULL,NULL,NULL);
	// Get display information
	width = GetDeviceCaps(hDisplayDCLocal,HORZRES);
	height = GetDeviceCaps(hDisplayDCLocal,VERTRES);
	DeleteDC(hDisplayDCLocal);
}
void WindowExaminer::getDisplayContextDimensions(HDC hdc, int& width, int& height)
{
	width = 0;
	height = 0;
	// Get display information
	width = GetDeviceCaps(hdc,HORZRES);
	height = GetDeviceCaps(hdc,VERTRES);
}
size_t WindowExaminer::enumWindows(std::vector<ApplicationWindow>& windows)
{

	for(size_t s = 0; s < windows.size(); s++)
	{
		windows[s].reset();
	}
	windows.clear();

	EnumWindows(WindowExaminer::EnumWindowsProc,(LPARAM)&windows);
	return windows.size();
}

BOOL WindowExaminer::EnumChildProc(HWND hwndChild, LPARAM lParam)
{
	std::vector<ApplicationWindow>* childList = (std::vector<ApplicationWindow>*)lParam;
	if (!childList)
	{
		return FALSE;
	}
	else
	{
		ApplicationWindow childEntry;
		childEntry.loadApplicationData(hwndChild);
		std::wstring windowCaption = childEntry.getWindowCaption();


		for (size_t i = 0; i < windowCaption.length(); i++)
		{
			if (windowCaption.at(i) < 32 || windowCaption.at(i) > 127)
			{
				if (windowCaption.at(i) == '\0')
					continue;
				windowCaption.at(i) = ' ';
			}
		}


		if (windowCaption.size() == 0)
			return TRUE;

		childList->push_back(childEntry);
	}

	return TRUE;
}

BOOL WindowExaminer::EnumWindowsProc(HWND hwnd, LPARAM lParam)
{
	std::vector<ApplicationWindow>* windowList = (std::vector<ApplicationWindow>*)lParam;
	if(!windowList)
	{
		return FALSE;
	}
	else
	{
		if (FALSE == ::IsWindow(hwnd))
			return TRUE;
		ApplicationWindow windowEntry;
		windowEntry.loadApplicationData(hwnd);
		std::wstring windowCaption = windowEntry.getWindowCaption();

		if (0 == windowCaption.compare(_T("Program Manager")))
			return TRUE;

		for (size_t i = 0; i < windowCaption.length(); i++)
		{
			if (windowCaption.at(i) < 32 || windowCaption.at(i) > 127)
			{
				if (windowCaption.at(i) == '\0')
					continue;
				windowCaption.at(i) = ' ';
			}
		}

		if(windowCaption.size() == 0)
		{
			return TRUE;
		}
		if(windowEntry.isVisible())
		{
			windowList->push_back(windowEntry);
		}
	}
	return TRUE;
}

HANDLE ProcessExaminer::isProgramRunning(std::wstring sProgramName)
{
	DWORD aProcesses[1024], cbNeeded, cProcesses;
	
	// Enumerate all processes
	if (!EnumProcesses(aProcesses, sizeof(aProcesses), &cbNeeded))
		return NULL;

	// Calculate how many process identifiers were returned.
	cProcesses = cbNeeded / sizeof(DWORD);

	// Loop through all processes and look for the 
	// program that we want
	
	for (int i = 0; i < cProcesses; i++)
	{
		if (!aProcesses[i])
			continue;

		TCHAR szProcessName[MAX_PATH];

		HANDLE hProcess = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, aProcesses[i]);

		// Get the process name
		if (NULL != hProcess)
		{
			HMODULE hMod;
		//	DWORD cbNeededP;

			if (EnumProcessModules(hProcess, &hMod, sizeof(hMod), &cbNeeded))
			{
				// Get the name of the exe, now.
				GetModuleBaseName(hProcess, hMod, szProcessName, sizeof(szProcessName)/sizeof(TCHAR));
				if (sProgramName.compare(std::wstring(szProcessName)) == 0)
					return hProcess;
			}
		}
	}
	return NULL;
}

void ProcessExaminer::getProgramFromHandle(HWND hWnd, std::wstring *sProgramName)
{
	std::map<HWND, std::wstring> programVector;
	programVector.clear();
	EnumWindows(ProcessExaminer::EnumWindowsProc, (LPARAM) &programVector);
	sProgramName->clear();
	std::map<HWND, std::wstring>::iterator it = programVector.find(hWnd);
	if (it != programVector.end())
		sProgramName->append(it->second);
}

std::wstring ProcessExaminer::getEXEName(DWORD dwProcessID)
{
	DWORD aProcesses [1024], cbNeeded, cProcesses;
	unsigned int i;

	//Enumerate all processes
	if (!EnumProcesses(aProcesses, sizeof(aProcesses), &cbNeeded))
		return NULL;

	// Calculate how many process identifiers were returned.
	cProcesses = cbNeeded / sizeof(DWORD);

	TCHAR szEXEName[MAX_PATH];
	//Loop through all process to find the one that matches
	//the one we are looking for
	for (i = 0; i < cProcesses; i++)
	{
		if (aProcesses [i] == dwProcessID)
		{
			// Get a handle to the process
			HANDLE hProcess = OpenProcess(PROCESS_QUERY_INFORMATION |
				PROCESS_VM_READ, FALSE, dwProcessID);

			// Get the process name
			if (NULL != hProcess)
			{
				HMODULE hMod;
				DWORD cbNeeded;

				if(EnumProcessModules(hProcess, &hMod, 
					sizeof(hMod), &cbNeeded))
				{
					//Get the name of the exe file
					GetModuleBaseName(hProcess, hMod, szEXEName, 
						sizeof(szEXEName)/sizeof(TCHAR));

					return std::wstring(szEXEName);
				}
			}
		}    
	}

	return NULL;
}

BOOL ProcessExaminer::EnumWindowsProc(HWND hWnd, LPARAM lParam)
{
	std::map<HWND, std::wstring>* programList = (std::map<HWND, std::wstring>*)lParam;

	//Make sure that the window is visible
	TCHAR szWindowText [MAX_PATH];
	if (!::IsWindowVisible (hWnd))
		return TRUE;

	//Get the text on the title bar
	::GetWindowText (hWnd, szWindowText, MAX_PATH);

	//If the window is Process Manager than don't display it
	if (_tcsstr (_T("Program Manager"), szWindowText))
		return TRUE;		

	//Get process ID
	DWORD dwProcessID;
	GetWindowThreadProcessId (hWnd, &dwProcessID);

	//Get the name of the executable file
	(*programList)[hWnd] = getEXEName(dwProcessID).c_str();

	return TRUE;
}