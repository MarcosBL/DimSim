#ifndef _DIMDIM_TOOLKIT_WIN32HELPER_H_
#define _DIMDIM_TOOLKIT_WIN32HELPER_H_

#include <map>
#include <string>
#include <vector>
#include <windows.h>

class ApplicationWindow
{
public:
	ApplicationWindow();
	virtual ~ApplicationWindow();
	void reset();
	void reloadData();


	void loadDesktopData(){ windowHandle = HWND_DESKTOP; reloadData(); }
	void loadApplicationData(HWND handle){ windowHandle = handle; reloadData(); }

	bool isValid() const{ return windowHandle != INVALID_HANDLE_VALUE; }
	bool isDesktopData() const{ return windowHandle == HWND_DESKTOP; }


	const std::wstring& getWindowCaption() const{ return windowCaption; }
	HWND getWindowHandle(){ return windowHandle; }
	const HWND getWindowHandle() const{ return windowHandle; }
	int  getXPosition() const{ return xPosition; }
	int  getYPosition() const{ return yPosition; }
	int  getWidth() const{ return width; }
	int  getHeight() const{ return height; }

	bool isVisible() const;

private:
	std::wstring windowCaption;
	HWND windowHandle;
	int xPosition;
	int yPosition;
	int width;
	int height;
};

class DesktopEnumerator
{
public:
	static std::wstring getProgramFromHandle(HWND hWnd);
	static size_t getApplicationList(std::vector<HWND>& windowHandles);
	static size_t getApplicationMap(std::map<LONG, std::wstring>& appMap);
};

class WindowExaminer
{
public:
	static void getDesktopDimensions(int& width, int& height);
	static size_t enumWindows(std::vector<ApplicationWindow>& windows);
	static BOOL CALLBACK EnumWindowsProc(HWND hwnd, LPARAM lParam);
	static BOOL CALLBACK EnumChildProc(HWND hwndChild, LPARAM lParam);
	static void getDisplayContextDimensions(HDC hdc, int& width, int& height);		
};

class ProcessExaminer
{
public:
	static std::wstring getEXEName(DWORD dwProcessID);
	static void getProgramFromHandle(HWND hWnd, std::wstring *sProgramName);
	static BOOL CALLBACK EnumWindowsProc(HWND hwnd, LPARAM lParam);
	static HANDLE isProgramRunning(std::wstring sProgramName);
};

#endif