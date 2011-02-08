#include <stdafx.h>
#include <sstream>
#include "ProfileManager.h"
#include "Translator.h"
#include "MirageManager.h"
#include "Win32Helper.h"

const std::wstring sPublisherExe(_T("dpc.exe"));
const std::wstring sTrackerExe(_T("dtc.exe"));
const std::wstring sScreencasterExe(_T("dsc.exe"));
std::wstring CTranslator::m_workingDirectoryW = _T("");

void CTranslator::stringToVectorA(std::string str, std::string delimiter, std::vector<std::string>& vStrList)
{
	size_t offset = 0;
	size_t iPos = -1;

	while (offset < str.length())
	{
		iPos = str.find(delimiter, offset);
		if (iPos == -1)
		{
			vStrList.push_back(str.substr(offset, str.length() - offset));
			break;
		}
		vStrList.push_back(str.substr(offset, iPos - offset));
		offset = iPos+1;
	}

	if (vStrList.size() == 0)
	{
		vStrList.push_back(str);
	}
}

void CTranslator::stringToVectorW(std::wstring str, std::wstring delimiter, std::vector<std::wstring>& vStrList)
{
	size_t offset = 0;
	size_t iPos = -1;

	while (offset < str.length())
	{
		iPos = str.find(delimiter, offset);
		if (iPos == -1)
		{
			vStrList.push_back(str.substr(offset, str.length() - offset));
			break;
		}
		vStrList.push_back(str.substr(offset, iPos - offset));
		offset = iPos+1;
	}

	if (vStrList.size() == 0)
	{
		vStrList.push_back(str);
	}
}


void CTranslator::vectorToMapA(std::vector<std::string> vStrList, std::string delimiter, 
							   std::map<std::string, std::string>& vStrMap)
{
	for (size_t s = 0; s < vStrList.size(); s++)
	{
		size_t iPos = vStrList[s].find(delimiter);
		if (iPos < 0)
			continue;

		std::string name = vStrList[s].substr(0, iPos);
		std::string value = vStrList[s].substr(iPos+1, vStrList[s].length() - iPos);

		if (name[0] == '\"')
			name = name.substr(1, name.length() - 2);
		if (value[0] == '\"')
			value = value.substr(1, value.length() - 2);

		vStrMap[name] = value;
	}
}
void CTranslator::vectorToMapW(std::vector<std::wstring> vStrList, std::wstring delimiter, 
							   std::map<std::wstring, std::wstring>& vStrMap)
{
	for (size_t s = 0; s < vStrList.size(); s++)
	{
		size_t iPos = vStrList[s].find(delimiter);
		if (iPos < 0)
			continue;

		std::wstring name = vStrList[s].substr(0, iPos);
		std::wstring value = vStrList[s].substr(iPos+1, vStrList[s].length() - iPos);

		if (name[0] == '\"')
			name = name.substr(1, name.length() - 2);
		if (value[0] == '\"')
			value = value.substr(1, value.length() - 2);

		vStrMap[name] = value;
	}
}

bool CTranslator::jsonArgsToMapW(BSTR args, std::map<std::wstring, std::wstring>& argMap)
{
	// {name:\"value\",name:\"value\"}

	std::wstring argStr(args);
	argStr.assign(argStr.substr(1, argStr.length() - 2));

	std::vector<std::wstring> vArgList;
	vArgList.clear();

	CTranslator::stringToVectorW(argStr.c_str(), _T(","), vArgList);
	CTranslator::vectorToMapW(vArgList, _T(":"), argMap);

	return true;
}

bool CTranslator::browserArgsToMapA(std::string args, std::map<std::string, std::string>& argMap)
{
	std::vector<std::string> vArgList;
	vArgList.clear();

	CTranslator::stringToVectorA(args.c_str(), "&", vArgList);
	CTranslator::vectorToMapA(vArgList, "=", argMap);

	return true;
}
bool CTranslator::browserArgsToMapW(BSTR args, std::map<std::wstring, std::wstring>& argMap)
{
	std::vector<std::wstring> vArgList;
	vArgList.clear();

	CTranslator::stringToVectorW(args, _T("&"), vArgList);
	CTranslator::vectorToMapW(vArgList, _T("="), argMap);

	return true;
}

std::string CTranslator::applicationMapToSimpleJSONA(std::map<LONG,std::string> appMap, std::string name, std::string value)
{
	std::map<LONG, std::string>::iterator it = appMap.begin();

	std::string buf("{windowList:[");
	while (it != appMap.end())
	{
		buf.append("{");
		buf.append(name);
		buf.append(":");
		buf.append(CTranslator::toStringA(it->first));
		buf.append(",");
		buf.append(value);
		buf.append(":\"");
		buf.append(CTranslator::sanitizeStringA(it->second));
		buf.append("\"}");
		it++;
		if (it == appMap.end())
			break;
		buf.append(",");
	}
	buf.append("]}");
	return buf;
}
BSTR CTranslator::applicationMapToSimpleJSONW(std::map<LONG,std::wstring> appMap, std::wstring name, std::wstring value)
{
	std::map<LONG, std::wstring>::iterator it = appMap.begin();

	std::wstring buf(_T("{windowList:["));
	while (it != appMap.end())
	{
		buf.append(_T("{"));
		buf.append(name);
		buf.append(_T(":"));
		buf.append(CTranslator::toStringW(it->first));
		buf.append(_T(","));
		buf.append(value);
		buf.append(_T(":\""));
		buf.append(CTranslator::sanitizeStringW(it->second));
		buf.append(_T("\"}"));
		it++;
		if (it == appMap.end())
			break;
		buf.append(_T(","));
	}
	buf.append(_T("]}"));
	return ::SysAllocString(buf.c_str());
}

std::string CTranslator::sanitizeStringA(std::string str)
{
	std::string retval("");

	for (size_t s = 0; s < str.length(); s++)
	{
		retval.push_back(str.at(s));
		if ('\\' == str.at(s))
			retval.push_back('\\');
	}

	return retval;
}
std::wstring CTranslator::sanitizeStringW(std::wstring str)
{
	std::wstring retval(_T(""));

	for (size_t s = 0; s < str.length(); s++)
	{
		retval.push_back(str.at(s));
		if (_T('\\') == str.at(s))
			retval.push_back(_T('\\'));
	}

	return retval;
}

void CTranslator::setWorkingDirectoryW(std::wstring moduleName, std::wstring additionalPath)
{
	if (moduleName.length() > 0)
	{
		HMODULE moduleHandle = ::GetModuleHandle(moduleName.c_str());
		TCHAR moduleLoc[1024];
		::GetModuleFileName(moduleHandle, moduleLoc, 1024);

		m_workingDirectoryW.clear();
		m_workingDirectoryW.assign(moduleLoc);
		m_workingDirectoryW = m_workingDirectoryW.substr(0, m_workingDirectoryW.length() - moduleName.length());

		if (additionalPath.length() > 0)
		{
			m_workingDirectoryW.append(additionalPath);
		}
	}
	else
	{
		// Here the additional path is actually an environment variable
		TCHAR envPath[128];
		GetEnvironmentVariable(additionalPath.c_str(), envPath, 128);

		std::wstring sProgramFilesPath(envPath);
		m_workingDirectoryW.assign(sProgramFilesPath);
		m_workingDirectoryW.append(_T("\\Dimdim\\Publisher\\FF\\"));
	}
}

bool CTranslator::executePublisherRunW()
{
	::ShellExecute(NULL, _T("open"), sPublisherExe.c_str(), _T("-run"), m_workingDirectoryW.c_str(), 0);
	return true;
}

bool CTranslator::executePublisherCheckDriverW()
{
	::ShellExecute(NULL, _T("open"), sPublisherExe.c_str(), _T("-checkdriver"), m_workingDirectoryW.c_str(), 0);
	return true;
}

bool CTranslator::executePublisherKillW()
{
	::ShellExecute(NULL, _T("open"), sPublisherExe.c_str(), _T("-kill"), m_workingDirectoryW.c_str(), 0);
	executeScreencasterStopTrackW();
	::ShellExecute(NULL, _T("open"), sScreencasterExe.c_str(), _T("-kill"), m_workingDirectoryW.c_str(), 0);
	executeScreencasterKillTrackW();
	return true;
}

void CTranslator::executeScreencasterKillW()
{
	TCHAR envPath[128];
	GetEnvironmentVariable(_T("SYSTEMROOT"), envPath, 128);

	std::wstring appPath(envPath);
	appPath.append(_T("\\system32\\tskill"));
	::ShellExecute(NULL, _T("open"), appPath.c_str(), _T("dsc"), 0, 0);
}

bool CTranslator::executeScreencasterShareAndConnectW(std::wstring handle, std::wstring connectURL)
{
	int i = 0;
	Sleep(1000);
	while (ProcessExaminer::isProgramRunning(sScreencasterExe) == 0)
	{
		if (i == 10)
			break;

		Sleep(500);

		i += 1;
	}

	std::wstring args(_T(""));
	if ( (0 == handle.compare(_T(""))) || (0 == handle.compare(_T("0"))))
	{
		args.append(_T("-shareprimary "));
	}
	else
	{
		args.append(_T("-sharehandle "));
		args.append(handle);
		args.append(_T(" "));
	}

	args.append(_T("-url "));
	args.append(connectURL);
	::ShellExecute(NULL, _T("open"), sScreencasterExe.c_str(), args.c_str(), m_workingDirectoryW.c_str(), 0);
	return true;
}

bool CTranslator::executeScreencasterRunW()
{
	::ShellExecute(NULL, _T("open"), sScreencasterExe.c_str(), _T("-run"), m_workingDirectoryW.c_str(), 0);
	return true;
}

void CTranslator::executeScreencasterRunTrackW()
{
	::ShellExecute(NULL, _T("open"), sTrackerExe.c_str(), _T("-run"), m_workingDirectoryW.c_str(), 0);
}


void CTranslator::executeScreencasterKillTrackW()
{
	TCHAR envPath[128];
	GetEnvironmentVariable(_T("SYSTEMROOT"), envPath, 128);

	std::wstring appPath(envPath);
	appPath.append(_T("\\system32\\tskill"));
	::ShellExecute(NULL, _T("open"), appPath.c_str(), _T("dtc"), 0, 0);
}


void CTranslator::executeScreencasterStartTrackW()
{
	::ShellExecute(NULL, _T("open"), sTrackerExe.c_str(), _T("-start"), m_workingDirectoryW.c_str(), 0);
}

void CTranslator::executeScreencasterStopTrackW()
{
	::ShellExecute(NULL, _T("open"), sTrackerExe.c_str(), _T("-stop"), m_workingDirectoryW.c_str(), 0);
}

bool CTranslator::executeScreencasterStopW()
{
	::ShellExecute(NULL, _T("open"), sScreencasterExe.c_str(), _T("-killallclients"), m_workingDirectoryW.c_str(), 0);
	return true;
}

bool CTranslator::executeScreencasterMenuW(std::wstring menuAction)
{
	if (menuAction == _T("enable"))
		::ShellExecute(NULL, _T("open"), sPublisherExe.c_str(), _T("-menu screencaster enable"), m_workingDirectoryW.c_str(), 0);
	else if (menuAction == _T("disable"))
		::ShellExecute(NULL, _T("open"), sPublisherExe.c_str(), _T("-menu screencaster disable"), m_workingDirectoryW.c_str(), 0);
	else if (menuAction == _T("warning"))
		::ShellExecute(NULL, _T("open"), sPublisherExe.c_str(), _T("-menu screencaster warning"), m_workingDirectoryW.c_str(), 0);

	return true;
}

bool CTranslator::executeScreencasterShareW(std::wstring handle)
{
	std::wstring args(_T("-screencast share "));
	args.append(handle);
	::ShellExecute(NULL, _T("open"), sPublisherExe.c_str(), args.c_str(), m_workingDirectoryW.c_str(), 0);
	Sleep(200);
	return true;
}

void CTranslator::invokeDriverInstall()
{
	CMirageManager::invokeMirageInstaller(m_workingDirectoryW.c_str());
}

BSTR CTranslator::getScreencastProfile()
{
	int iVal = CProfileManager::getInstance()->RetrieveConfig("BlockSize");
	std::wstring profile(_T("{BWProfile:\""));
	if (16 == iVal)
		profile.append(_T("2\""));
	else if (32 == iVal)
		profile.append(_T("1\""));
	else
		profile.append(_T("0\""));

	return ::SysAllocString(profile.c_str());
}

bool CTranslator::setScreencastProfile(int iVal, std::string fileName, std::string affliction)
{
	char location[1024];
	GetModuleFileNameA(GetModuleHandleA(fileName.c_str()), location, 1024);
	std::string currentDirectory(location);
	currentDirectory.assign(currentDirectory.substr(0, currentDirectory.length() - strlen(fileName.c_str())));
	currentDirectory.append(affliction);

	CProfileManager::getInstance()->LoadConfig(currentDirectory);

	if (iVal == 0)
		CProfileManager::getInstance()->enforceHighBWProfile();
	else if (iVal == 2)
		CProfileManager::getInstance()->enforceLowBWProfile();
	else
		CProfileManager::getInstance()->enforceMediumBWProfile();

	CProfileManager::getInstance()->CommitConfig(currentDirectory);
	return true;
}