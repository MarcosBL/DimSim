// Header

#include "stdafx.h"
#include "MirageManager.h"
#include "Win32Helper.h"
#include <fstream>
#include <sstream>

const TCHAR MIRAGE_DRIVER_LOCATIONSTUB[] = _T("\\system32\\drivers\\dfmirage.sys");
const std::wstring sMirageSetup(_T("Install-DFMirage.bat"));
const std::wstring sMirageDirectory(_T("DFMirage"));
const std::wstring sMirageInst(_T("MirrInst.exe"));

#define	DRIVER_NAME			_T("Mirage Driver")
#define	MINIPORT_NAME		_T("dfmirage")
#define MINIPORT_REGISTRY_PATH	_T("SYSTEM\\CurrentControlSet\\Hardware Profiles\\Current\\System\\CurrentControlSet\\Services")

#define ESC_QVI		1026
#define	DMF_VERSION_DEFINE(_ver_0,_ver_1,_ver_2,_ver_3)	((_ver_0<<24) | (_ver_1<<16) | (_ver_2<<8) | _ver_3)
#define	BYTE0(x)	((x) & 0xFF)
#define	BYTE1(x)	(((x) >> 8) & 0xFF)
#define	BYTE2(x)	(((x) >> 16) & 0xFF)
#define	BYTE3(x)	(((x) >> 24) & 0xFF)
#define	DMF_PROTO_VER_CURRENT	DMF_VERSION_DEFINE(1,2,0,0)
#define	DMF_PROTO_VER_MINCOMPAT	DMF_VERSION_DEFINE(0,9,0,1)

// Implementation

LONG CMirageManager::CommitDisplayChanges(DEVMODE* pdm, DISPLAY_DEVICE& deviceInfo)
{
	if (pdm)
	{
		LONG code = ChangeDisplaySettingsEx(deviceInfo.DeviceName, pdm, NULL, CDS_UPDATEREGISTRY, NULL);
		if (code < 0){return code;}
		code = ChangeDisplaySettingsEx(deviceInfo.DeviceName, pdm, NULL, 0, NULL);
		if (code < 0){return code;}
	}
	else
	{
		LONG code = ChangeDisplaySettingsEx(deviceInfo.DeviceName, NULL, NULL, 0, NULL);
		if (code < 0){return code;}
	}
	return ERROR_SUCCESS;
}

LONG CMirageManager::SetupDriver(CRegKey& regKeyDevice, DISPLAY_DEVICE& deviceInfo, Esc_dmf_Qvi_OUT& qvi_out)
{
	DFEXT_DEVMODE deviceMode;
	memset(&deviceMode, 0, sizeof(deviceMode));
	deviceMode.dmSize = sizeof(DEVMODE);

	WORD drvExtraSaved = deviceMode.dmDriverExtra;
	memset(&deviceMode, 0, sizeof(DEVMODE));
	deviceMode.dmSize = sizeof(DEVMODE);
	deviceMode.dmDriverExtra = drvExtraSaved;

	POINTL pos;
	pos.x = pos.y = 0;
	deviceMode.dmPelsWidth = 100;
	deviceMode.dmPelsHeight = 100;
	deviceMode.dmBitsPerPel = 32;
	deviceMode.dmPosition = pos;

	deviceMode.dmFields = DM_BITSPERPEL | DM_PELSWIDTH | DM_PELSHEIGHT | DM_POSITION;
	deviceMode.dmDeviceName[0] = '\0';

	CMirageManager::WriteDiagnostics(_T("Attaching to desktop"));
	LONG retval = regKeyDevice.SetDWORDValue(_T("Attach.ToDesktop"), true);
	if (ERROR_SUCCESS != retval)
	{
		CMirageManager::WriteDiagnostics(_T("Failed to attach to desktop"));
		return retval;
	}

	CMirageManager::WriteDiagnostics(_T("Committing display changes"));
	retval = CMirageManager::CommitDisplayChanges(&deviceMode, deviceInfo);
	if (ERROR_SUCCESS != retval)
	{
		CMirageManager::WriteDiagnostics(_T("Failed to commit display changes"));
		return retval;
	}

	CMirageManager::WriteDiagnostics(_T("Creating device context"));
	HDC driverDC;
	driverDC = CreateDC(deviceInfo.DeviceName, NULL, NULL, NULL);
	if (!driverDC)
	{
		CMirageManager::WriteDiagnostics(_T("Failed to create device context"));
		return ERROR_DC_NOT_FOUND;
	}

	CMirageManager::WriteDiagnostics(_T("Querying Driver Version"));
	Esc_dmf_Qvi_IN qvi_in;
	qvi_in.cbSize = sizeof(qvi_in);
	qvi_in.app_actual_version = DMF_PROTO_VER_CURRENT;
	qvi_in.display_minreq_version = DMF_PROTO_VER_MINCOMPAT;
	qvi_in.connect_options = 0;

	qvi_out.cbSize = sizeof(qvi_out);
	ExtEscape(driverDC, ESC_QVI, sizeof(qvi_in), (LPSTR) &qvi_in, sizeof(qvi_out), (LPSTR) &qvi_out);
	
	std::wstringstream driverVersion(_T(""));
	driverVersion << _T("Driver Version : ") << BYTE3(qvi_out.display_actual_version) << _T(".") << BYTE2(qvi_out.display_actual_version)
		<< _T(".") << BYTE1(qvi_out.display_actual_version) << _T(".") << BYTE0(qvi_out.display_actual_version)
		<< _T(" build(") << qvi_out.display_buildno << _T(")");
	CMirageManager::WriteDiagnostics(driverVersion.str());

	if (driverDC)
	{
		CMirageManager::WriteDiagnostics(_T("Deleting device context"));
		::DeleteDC(driverDC);
		driverDC = NULL;
	}

	CMirageManager::WriteDiagnostics(_T("Detaching from desktop"));
	retval = regKeyDevice.SetDWORDValue(_T("Attach.ToDesktop"), false);
	if (ERROR_SUCCESS != retval)
		return retval;

	deviceMode.dmPelsWidth = 0;
	deviceMode.dmPelsHeight = 0;
	OSVERSIONINFO osvi;
	osvi.dwOSVersionInfoSize = sizeof(osvi);
	GetVersionEx(&osvi);
	
	DEVMODE* pdm = NULL;
	if (osvi.dwMajorVersion > 5 || (osvi.dwMajorVersion == 5 && osvi.dwMinorVersion > 0))
		pdm = &deviceMode;

	CMirageManager::WriteDiagnostics(_T("Committing display changes"));
	retval = CommitDisplayChanges(pdm, deviceInfo);
	if (ERROR_SUCCESS != retval)
		return retval;

	return ERROR_SUCCESS;
}

void CMirageManager::CleanupDiagnostics()
{
	std::wstring sFileName(_wgetenv(_T("APPDATA")));
	sFileName.append(_T("\\Dimdim\\DriverDiagnostics.info"));
	DeleteFile(sFileName.c_str());
}

void CMirageManager::WriteDiagnostics(const std::wstring& data)
{
	std::wstring sFileName(_wgetenv(_T("APPDATA")));
	sFileName.append(_T("\\Dimdim\\DriverDiagnostics.info"));
	FILE* fout = _wfopen(sFileName.c_str(), _T("a+"));
	fwprintf(fout, _T("%s\n"), data.c_str());
	fclose(fout);
}

BOOL CMirageManager::LookupDisplayDevices(DISPLAY_DEVICE& deviceInfo, DWORD& deviceNumber, bool bShouldLog)
{
	memset(&deviceInfo, 0, sizeof(deviceInfo));
	deviceInfo.cb = sizeof(deviceInfo);

	deviceNumber = 0;
	BOOL result = FALSE;

	std::wstring dataStream(_T(""));
	while (result = EnumDisplayDevices(NULL, deviceNumber, &deviceInfo, 0))
	{
		if (bShouldLog)
		{
			dataStream.append(_T("Detected Device :: "));
			dataStream.append(deviceInfo.DeviceName);
			dataStream.append(_T(" - "));
			dataStream.append(deviceInfo.DeviceString);
			CMirageManager::WriteDiagnostics(dataStream);
			dataStream.clear();
		}
		if (wcscmp(deviceInfo.DeviceString, DRIVER_NAME) == 0)
		{
			result = TRUE;
			break;
		}
		++deviceNumber;
	}
	return result;
}

LONG CMirageManager::OpenDeviceRegistryKey(CRegKey& regkeyDevice, DISPLAY_DEVICE& deviceInfo)
{
	TCHAR deviceSubkey[8];
	wcscpy(deviceSubkey, _T("DEVICE0"));

	TCHAR deviceKey[MAX_PATH];
	wcsncpy(deviceKey, deviceInfo.DeviceKey, MAX_PATH - 1);
	deviceKey[MAX_PATH - 1] = 0;
	wcsupr(deviceKey);
	if (LPCTSTR devNum = wcsstr(deviceKey, _T("\\DEVICE")))
		deviceSubkey[6] = devNum[7];

	CRegKey regkeyServices;
	CRegKey regkeyDriver;

	// trying to open key with the minimal privileges
	if (ERROR_SUCCESS != regkeyServices.Open(HKEY_LOCAL_MACHINE, MINIPORT_REGISTRY_PATH, KEY_ENUMERATE_SUB_KEYS) ||
		ERROR_SUCCESS != regkeyDriver.Open(regkeyServices, MINIPORT_NAME, KEY_ENUMERATE_SUB_KEYS) ||
		ERROR_SUCCESS != regkeyDevice.Open(regkeyDriver, deviceSubkey, KEY_SET_VALUE))
	{
		regkeyServices.Close();
		regkeyDriver.Close();
		LONG retval = regkeyServices.Create(HKEY_LOCAL_MACHINE, MINIPORT_REGISTRY_PATH, REG_NONE, 0, KEY_ENUMERATE_SUB_KEYS | KEY_CREATE_SUB_KEY);
		if (ERROR_SUCCESS != retval)
			return retval;
		retval = regkeyDriver.Create(regkeyServices, MINIPORT_NAME, REG_NONE, 0, KEY_ENUMERATE_SUB_KEYS | KEY_CREATE_SUB_KEY);
		if (ERROR_SUCCESS != retval)
			return retval;
		retval = regkeyDevice.Create(regkeyDriver, deviceSubkey, REG_NONE, 0, KEY_SET_VALUE);
		if (ERROR_SUCCESS != retval)
			return retval;
	}
	return ERROR_SUCCESS;
}

UINT CMirageManager::isDriverInstalled()
{
	CMirageManager::CleanupDiagnostics();

	DISPLAY_DEVICE	deviceInfo;
	DWORD			deviceNumber;

	if (FALSE == CMirageManager::LookupDisplayDevices(deviceInfo, deviceNumber))
	{
		// driver not installed
		CMirageManager::WriteDiagnostics(_T("Driver not installed. Dimdim will attempt to install the driver."));
		return 0;
	}

	CRegKey regKeyDevice;
	LONG retval = CMirageManager::OpenDeviceRegistryKey(regKeyDevice, deviceInfo);
	if (ERROR_SUCCESS != retval)
	{
		switch(retval)
		{
		case ERROR_FILE_NOT_FOUND:
			CMirageManager::WriteDiagnostics(_T("Driver not installed. Couldn't find or create appropriate registry keys"));
			return 0;		// driver not installed
		case ERROR_ACCESS_DENIED:
			CMirageManager::WriteDiagnostics(_T("Access denied to registry."));
			CMirageManager::WriteDiagnostics(_T("Please uninstall the driver and restart your machine. \
												Then, install the driver or restart desktop sharing."));
			return 3;		// access denied.. decide what to do

		}
	}

	Esc_dmf_Qvi_OUT qvi_out;
	retval = CMirageManager::SetupDriver(regKeyDevice, deviceInfo, qvi_out);
	if (ERROR_SUCCESS == retval)
	{
		CMirageManager::WriteDiagnostics(_T("Driver has been installed correctly. Desktop sharing should work."));
		return 1;
	}
	
	CMirageManager::WriteDiagnostics(_T("Driver isn't properly installed yet. Please restart your machine and try again."));
	return 2; // decide what to do.
}

std::wstring CMirageManager::createInstallerScript(std::wstring currentDir)
{
	std::wstring fileName(currentDir);
	fileName.append(_T("\\installScript.cmd"));
	FILE* fout = _wfopen(fileName.c_str(), _T("w"));
	if (!fout)
	{
		return _T("");
	}
	fwprintf(fout, _T("@echo off\n"));
	fwprintf(fout, _T("\""));
	fwprintf(fout, currentDir.c_str());
	fwprintf(fout, _T("\\MirrInst.exe\" -i \"dfmirage\" \"Mirage Driver\" \""));
	fwprintf(fout, currentDir.c_str());
	fwprintf(fout, _T("\" \""));
	fwprintf(fout, currentDir.c_str());
	fwprintf(fout, _T("\\dfmirage.inf\""));
	fclose(fout);
	return fileName;
}

void CMirageManager::invokeMirageInstaller(std::wstring currentDir)
{
	HANDLE hProcess = ProcessExaminer::isProgramRunning(sMirageInst);
	if (NULL != hProcess)
		return;

	// Append the Mirage Driver directory to currentDir;
	currentDir.append(sMirageDirectory);

	std::wstring command(currentDir);
	command.append(_T("\\mirage105.exe"));
	std::wstring data(_T("Launching Mirage Driver installation using "));
	data.append(command);
	CMirageManager::WriteDiagnostics(data);
	::ShellExecute(NULL, _T("open"), command.c_str(), NULL, currentDir.c_str(), SW_HIDE);

	unsigned int iCount = 0;
	DISPLAY_DEVICE	deviceInfo;
	DWORD			deviceNumber;

	HANDLE mirageTimer = CreateEvent(NULL, true, false, _T("mirageTimer"));
	while (true)
	{
		if (TRUE == CMirageManager::LookupDisplayDevices(deviceInfo, deviceNumber, false) || iCount >= 15)
		{
			CMirageManager::WriteDiagnostics(_T("Detected Mirage Driver"));
			break;
		}
		::WaitForSingleObject(mirageTimer, 500);
		iCount += 1;
	}
	CloseHandle(mirageTimer);
}