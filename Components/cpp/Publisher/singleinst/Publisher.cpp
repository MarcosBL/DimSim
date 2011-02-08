#include "stdafx.h"
#include "toolkit\MirageManager.h"
#include "toolkit\Win32Helper.h"
#include "Publisher.h"
#include <windows.h>
#include <string>

CPublisher* CPublisher::_pubInstance = NULL;

const LPTSTR lpszConduit = _T("\\\\.\\pipe\\10df8770-cbeb-11dc-95ff-0800200c9a66");
const std::wstring sScreencasterExe(_T("dsc.exe"));
const std::wstring sConverterExe(_T("dcc.exe"));
const std::wstring sSelfExe(_T("dpc.exe"));

CPublisher* CPublisher::getInstance()
{
	if (NULL ==_pubInstance)
		_pubInstance = new CPublisher();
	return _pubInstance;
}

void CPublisher::removeInstance()
{
	if (NULL != _pubInstance)
		delete _pubInstance;
	_pubInstance = 0;
}

void CPublisher::screencastTrack()
{
	HANDLE hProcess = ProcessExaminer::isProgramRunning(sScreencasterExe);
	if (NULL == hProcess)
	{
		notifyBrowserInterface(_T("{screencastResult:\"5\"}"));
	}			
}

void CPublisher::disconnectComponents()
{
	TCHAR envPath[128];
	GetEnvironmentVariable(_T("SYSTEMROOT"), envPath, 128);

	std::wstring appPath(envPath);
	appPath.append(_T("\\system32\\tskill"));
	::ShellExecute(NULL, _T("open"), appPath.c_str(), _T("dtc"), 0, 0);

	HANDLE hProcess = ProcessExaminer::isProgramRunning(sScreencasterExe);
	std::wstring programName(sScreencasterExe);
	if (NULL == hProcess)
	{
		hProcess = ProcessExaminer::isProgramRunning(sConverterExe);
		if (NULL == hProcess)
			return;

		programName.assign(sConverterExe);
		CPublisher::getInstance()->executeConverter(_T("cancel"));
	}
	else
	{
		CPublisher::getInstance()->executeScreencaster(_T("kill"));
	}

	while (ProcessExaminer::isProgramRunning(programName) != 0)
	{
		Sleep(100);
	}
}

bool CPublisher::notifyBrowserInterface(std::wstring msg)
{
	HANDLE hPipe; 
	BOOL fSuccess; 
	DWORD cbWritten, dwMode; 

	// Try to open a named pipe; wait for it, if necessary. 

	while (1) 
	{ 
		hPipe = CreateFile(
			lpszConduit,   // pipe name 
			GENERIC_WRITE, 
			0,              // no sharing 
			NULL,           // default security attributes
			OPEN_EXISTING,  // opens existing pipe 
			0,              // default attributes 
			NULL);          // no template file 

		// Break if the pipe handle is valid. 

		if (hPipe != INVALID_HANDLE_VALUE) 
			break; 

		// Exit if an error other than ERROR_PIPE_BUSY occurs. 

		if (GetLastError() != ERROR_PIPE_BUSY) 
		{
			// Error: could not open pipe
			try
			{
				CloseHandle(hPipe); 
			}
			catch(...)
			{
			}
			return 0;
		}

		// All pipe instances are busy, so wait for 20 seconds. 

		if (!WaitNamedPipe(lpszConduit, 20000)) 
		{ 
			// Error: could not open pipe
			try
			{
				CloseHandle(hPipe); 
			}
			catch(...)
			{
			}
			return 0;
		} 
	} 

	// The pipe connected; change to message-read mode. 

	dwMode = PIPE_READMODE_MESSAGE; 
	fSuccess = SetNamedPipeHandleState( 
		hPipe,    // pipe handle 
		&dwMode,  // new pipe mode 
		NULL,     // don't set maximum bytes 
		NULL);    // don't set maximum time 
	if (!fSuccess) 
	{
		// Error: SetNamedPipeHandleState failed
		try
		{
			CloseHandle(hPipe); 
		}
		catch(...)
		{
		}
		return 0;
	}

	// Send a message to the pipe server. 

	fSuccess = WriteFile( 
		hPipe,                  // pipe handle 
		msg.c_str(),             // message 
		(DWORD)(msg.size() * sizeof(wchar_t)), // message length 
		&cbWritten,             // bytes written 
		NULL);                  // not overlapped 
	if (!fSuccess) 
	{
		// Error: WriteFile failed
		try
		{
			CloseHandle(hPipe); 
		}
		catch(...)
		{
		}
		return 0;
	}

	try
	{
		CloseHandle(hPipe); 
	}
	catch(...)
	{
	}

	return 0; 
}

bool CPublisher::executeConverter(std::wstring operation, std::wstring argument)
{
	if (0 == operation.find(_T("cancel")))
	{
		std::wstring argList(_T("/im "));
		argList.append(sConverterExe);
		argList.append(_T(" /f /t"));

		ShellExecute(NULL, _T("open"), _T("taskkill"), argList.c_str(), NULL, 0);
		// Wait for a bit
		Sleep(500);

		std::wstring msg(_T("{conversionResult:\"2\",result:\"true\",method:\"cancelDocumentConversion\",error:\"7200\",docID:\""));
		msg.append(argument);
		msg.append(_T("\"}"));
		notifyBrowserInterface(msg);
		return true;
	}
	std::wstring shellArgs(_T("-"));
	shellArgs.append(operation);
	if(argument.length() > 0)
	{
		shellArgs.append(_T(" "));
		shellArgs.append(argument);
	}

	HMODULE moduleHandle = ::GetModuleHandle(sSelfExe.c_str());
	TCHAR exeLoc[1024];
	::GetModuleFileName(moduleHandle, exeLoc, 1024);
	std::wstring currentDir(exeLoc);
	currentDir = currentDir.substr(0, currentDir.length() - wcslen(sSelfExe.c_str()));

	::ShellExecute(NULL, _T("open"), sConverterExe.c_str(), shellArgs.c_str(), currentDir.c_str(), 0);
	return true;
}

bool CPublisher::executeScreencaster(std::wstring operation, std::wstring argument)
{

	HANDLE hProcess = NULL;	

	if (argument != _T("run"))
	{
		unsigned int iCount = 0;
		HANDLE dscTimer = CreateEvent(NULL, true, false, _T("dscTimer"));
		while (true)
		{
			hProcess = ProcessExaminer::isProgramRunning(sScreencasterExe.c_str());
			if (NULL != hProcess || iCount >= 10)
				break;
			::WaitForSingleObject(dscTimer, 500);
			iCount += 1;
		}
		CloseHandle(dscTimer);
	}

	std::wstring shellArgs(_T("-"));
	shellArgs.append(operation);
	if (argument.length() > 0)
	{
		shellArgs.append(_T(" "));
		shellArgs.append(argument);
	}

	HMODULE moduleHandle = ::GetModuleHandle(sSelfExe.c_str());
	TCHAR exeLoc[1024];
	::GetModuleFileName(moduleHandle, exeLoc, 1024);
	std::wstring currentDir(exeLoc);
	currentDir = currentDir.substr(0, currentDir.length() - wcslen(sSelfExe.c_str()));

	::ShellExecute(NULL, _T("open"), sScreencasterExe.c_str(), shellArgs.c_str(), currentDir.c_str(), 0);

	return true;
}

UINT CPublisher::isDriverInstalled()
{
	return CMirageManager::isDriverInstalled();
}

void CPublisher::invokeDriverInstall()
{
	HMODULE moduleHandle = ::GetModuleHandle(sSelfExe.c_str());
	TCHAR exeLoc[1024];
	::GetModuleFileName(moduleHandle, exeLoc, 1024);
	std::wstring currentDir(exeLoc);
	currentDir = currentDir.substr(0, currentDir.length() - wcslen(sSelfExe.c_str()));
	CMirageManager::invokeMirageInstaller(currentDir);
}
