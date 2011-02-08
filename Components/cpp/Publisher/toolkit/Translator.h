#ifndef _DIMDIM_TOOLKIT_TRANSLATOR_H_
#define _DIMDIM_TOOLKIT_TRANSLATOR_H_

#include <map>
#include <vector>
#include <string>
#include <tchar.h>
#include <sstream>
#include <windows.h>

class CTranslator
{
public:
	
	static bool jsonArgsToMapW(BSTR args, std::map<std::wstring, std::wstring>& argMap);

	static bool browserArgsToMapW(BSTR args, std::map<std::wstring, std::wstring>& argMap);
	static bool browserArgsToMapA(std::string args, std::map<std::string, std::string>& argMap);

	static BSTR applicationMapToSimpleJSONW(std::map<LONG, std::wstring>appMap, std::wstring name, std::wstring value);
	static std::string applicationMapToSimpleJSONA(std::map<LONG, std::string>appMap, std::string name, std::string value);

	static void stringToVectorA(std::string str, std::string delimiter, std::vector<std::string>& vStrList);
	static void stringToVectorW(std::wstring str, std::wstring delimiter, std::vector<std::wstring>& vStrList);	
	
	static void vectorToMapA(std::vector<std::string> vStrList, std::string delimiter, std::map<std::string, std::string>& vStrMap);
	static void vectorToMapW(std::vector<std::wstring> vStrList, std::wstring delimiter, std::map<std::wstring, std::wstring>& vStrMap);

	static std::string sanitizeStringA(std::string str);
	static std::wstring sanitizeStringW(std::wstring str);

	static void setWorkingDirectoryW(std::wstring moduleName, std::wstring additionalPath = L"");

	template <class T>
	static std::wstring toStringW(T tVal)
	{
		std::wstringstream out;
		out << tVal;
		return out.str();
	}
	template <class T>
	static std::string toStringA(T tVal)
	{
		std::stringstream out;
		out << tVal;
		return out.str();
	}

	static BSTR getScreencastProfile();
	static bool setScreencastProfile(int iVal, std::string fileName, std::string affliction = "");


	//////////////////////////////////////
	//		ShellExecute helpers		//
	//////////////////////////////////////

	static bool executePublisherRunW();
	static bool executePublisherCheckDriverW();
	static bool executePublisherKillW();

	static bool executeScreencasterShareAndConnectW(std::wstring handle, std::wstring connectURL);
	static bool executeScreencasterRunW();
	static bool executeScreencasterConnectW(std::wstring connectURL);
	static bool executeScreencasterShareW(std::wstring handle);
	static bool executeScreencasterStopW();
	static void executeScreencasterRunTrackW();
	static void executeScreencasterKillW();
	static void executeScreencasterKillTrackW();
	static void executeScreencasterStartTrackW();
	static void executeScreencasterStopTrackW();
	static bool executeScreencasterMenuW(std::wstring menuAction);

	static void invokeDriverInstall();

private:
	static std::wstring m_workingDirectoryW;
};

#endif