// Header

#include "stdafx.h"
#include "ProfileManager.h"
#include <fstream>
#include <windows.h>
#include <sstream>

// Implementation

const char CONFIG_FILE_NAME[] = "DesktopShare.cfg";
const std::string sSelfExe("dpc.exe");

CProfileManager* CProfileManager::instance_ = NULL;

CProfileManager::CProfileManager()
{
	paramMap.clear();
	descMap.clear();

	descMap["Logging"] = "# Accepted Values : 0, 2 to disable and enable logging.";
	paramMap["Logging"] = 2;

	descMap["LogLevel"] = "# Accepted Values: 0...10. Determines level of logging in terms of verbosity. LogLevel 10 is the most verbose.";
	paramMap["LogLevel"] = 10;

	descMap["OperationType"] = "# Accepted Values: 1...3. Type 1 dumps all captured data to a local file. Type 2 sends data over the network. Type 3 does both.";
	paramMap["OperationType"] = 2;
	
	descMap["EnablePointerAlgorithm"] = "# Accepted Values: 0, 1 to disable and enable pointer algorithm.";
	paramMap["EnablePointerAlgorithm"] = 1;
	
	descMap["EnableRestrictedColors"] = "# Accepted Values: 0, 1 to disable and enable 8-bit screencasting.";
	paramMap["EnableRestrictedColors"] = 1;
	
	descMap["CompressLevel"] = "# Accepted Values: 0...9 for ZLIB compression level.";
	paramMap["CompressLevel"] = 6;
	
	descMap["JPEGEncodingLevel"] = "# Accepted Values: 0...0 for JPEG Encoding levels.";
	paramMap["JPEGEncodingLevel"] = 6;
	
	descMap["MaxRetries"] = "#Accepted Values: 0 and above. Determines the number of retries after a failed HTTP operation.";
	paramMap["MaxRetries"] = 2;
	
	descMap["MaxWaitTime"] = "#Acccepted Values: 1 and above (in seconds). Determines the timeout value used for each HTTP transaction.";
	paramMap["MaxWaitTime"] = 30;
	
	descMap["StressCount"]= "# FOR INTERNAL PURPOSES ONLY. INTERFACE TO USE THIS PARAMETER IS NOT EXPOSED.";
	paramMap["StressCount"] = 10;
	
	descMap["BlockSize"] = "# Accepted Values: Multiples of 16 (in kilobytes). Determines the maximum size of a packet going from the screencaster.";
	paramMap["BlockSize"] = 32;	

	descMap["MinTransactionWait"] = "#Accepted Values: Minimum of 20 (in milliseconds). The minimum wait time between each send+capture loop.";
	paramMap["MinTransactionWait"] = 100;
}

CProfileManager::~CProfileManager()
{
}

CProfileManager* CProfileManager::getInstance()
{
	if (NULL == instance_)
		instance_ = new CProfileManager();
	return instance_;
}

void CProfileManager::removeInstance()
{
	if (NULL != instance_)
		delete instance_;
	instance_ = NULL;
}

void CProfileManager::enforceHighBWProfile()
{
	UpdateConfig("EnableRestrictedColors", 0);
	UpdateConfig("JPEGEncodingLevel", 6);
	UpdateConfig("BlockSize", 64);
	UpdateConfig("CaptureWaitTime", 100);
	UpdateConfig("EnableAdaptiveCapture", 1);
	UpdateConfig("EnablePointerAlgorithm", 0);
}

void CProfileManager::enforceMediumBWProfile()
{
	UpdateConfig("EnableRestrictedColors", 0);
	UpdateConfig("JPEGEncodingLevel", 6);
	UpdateConfig("BlockSize", 32);
	UpdateConfig("CaptureWaitTime", 250);
	UpdateConfig("EnableAdaptiveCapture", 1);
	UpdateConfig("EnablePointerAlgorithm", 1);
}

void CProfileManager::enforceLowBWProfile()
{
	UpdateConfig("EnableRestrictedColors", 1);
	UpdateConfig("JPEGEncodingLevel", -1);
	UpdateConfig("BlockSize", 16);
	UpdateConfig("CaptureWaitTime", 500);
	UpdateConfig("EnableAdaptiveCapture", 1);
	UpdateConfig("EnablePointerAlgorithm", 1);
}

void CProfileManager::CommitConfig(std::string currentDir)
{
	std::string fileName("");
	if (currentDir.length() > 0)
	{
		fileName.assign(currentDir);
	}
	else
	{
		HMODULE moduleHandle = ::GetModuleHandleA(sSelfExe.c_str());
		char exeLoc[1024];
		::GetModuleFileNameA(moduleHandle, exeLoc, 1024);
		fileName.assign(exeLoc);
		fileName = fileName.substr(0, fileName.length() - strlen(sSelfExe.c_str()));
	}
	fileName.append(CONFIG_FILE_NAME);
	std::ofstream fos(fileName.c_str());
	if (false == fos.is_open())
		return;

	std::map<std::string, int>::iterator paramit;

	for (paramit = paramMap.begin(); paramit != paramMap.end(); paramit++)
	{
		std::map<std::string, std::string>::iterator descit;
		descit = descMap.find(paramit->first);
		if (descit != descMap.end())
			fos << descit->second;
		fos << std::endl;
		fos << (paramit->first).c_str();
		fos << "=";
		fos << paramit->second;
		fos << std::endl;
	}

	fos.close();
}

int CProfileManager::RetrieveConfig(std::string key)
{
	LoadConfig();
	std::map<std::string, int>::iterator it = paramMap.find(key);

	if (it != paramMap.end())
	{
		return it->second;
	}

	return -999;
}

void CProfileManager::LoadConfig(std::string currentDir)
{
	std::string fileName("");
	if (currentDir.length() > 0)
	{
		fileName.assign(currentDir);
	}
	else
	{
		HMODULE moduleHandle = ::GetModuleHandleA(sSelfExe.c_str());
		char exeLoc[1024];
		::GetModuleFileNameA(moduleHandle, exeLoc, 1024);
		fileName.assign(exeLoc);
		fileName = fileName.substr(0, fileName.length() - strlen(sSelfExe.c_str()));
	}
	fileName.append(CONFIG_FILE_NAME);
	std::ifstream fis(fileName.c_str());
	if (false == fis.is_open())
	{
		// Commit a Medium bandwidth profile by default
		enforceMediumBWProfile();
		CommitConfig();
		return;
	}

	std::string line("");
	fis >> line;
	std::string cache("");
	do
	{
		cache.assign(line);
		if(line.length() > 0)
		{
			if(line[0] != '#')
			{
				size_t index = line.find_first_of('=');
				if(index < line.size())
				{
					std::string name = line.substr(0,index);
					std::string value = line.substr(index+1);
					int iVal = atoi(value.c_str());
					paramMap[name] = iVal;
				}
			}
		}
		fis >> line;
	}
	while(cache.compare(line) != 0 && line.length() > 0);

	fis.close();
}

void CProfileManager::UpdateConfig(std::string key, int val)
{
	std::map<std::string, int>::iterator it = paramMap.find(key);

	if (it != paramMap.end())
	{
		paramMap.erase(it);
		paramMap[key] = val;
	}
}