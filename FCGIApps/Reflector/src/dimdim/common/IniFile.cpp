#include "IniFile.h"
#include "Timer.h"
#include "Logger.h"
#include "Helper.h"
namespace dm
{
	
	IniFile::IniFile()
	{
	}
	IniFile::~IniFile()
	{
	}
	bool IniFile::hasSection(const String sectionName) const
	{
		std::map<String,StringTable>::const_iterator iter = values.find(sectionName);
		return (iter != values.end());
	}
	bool IniFile::hasKey(const String sectionName, const String keyName) const
	{
		std::map<String,StringTable>::const_iterator iter = values.find(sectionName);
		if(iter != values.end())
		{
			const StringTable& table = iter->second;
			StringTable::const_iterator iter2 = table.find(keyName);
			return (iter2 != table.end());
		}
		return false;
	}
	const String IniFile::get(const String sectionName, const String keyName, const String defaultValue) const
	{
		std::map<String,StringTable>::const_iterator iter = values.find(sectionName);
		if(iter != values.end())
		{
			const StringTable& table = iter->second;
			StringTable::const_iterator iter2 = table.find(keyName);
			if (iter2 != table.end())
			{
				return iter2->second;
			}
		}
		return defaultValue;
	}
	s32 IniFile::getInt(const String sectionName, const String keyName, int defaultValue )
	{
		std::map<String,StringTable>::const_iterator iter = values.find(sectionName);
		if(iter != values.end())
		{
			const StringTable& table = iter->second;
			StringTable::const_iterator iter2 = table.find(keyName);
			if (iter2 != table.end())
			{
				return atoi(iter2->second.c_str());
			}
		}
		return defaultValue;
	}
	bool IniFile::getBool(const String sectionName, const String keyName, bool defaultValue)
	{

		std::map<String,StringTable>::const_iterator iter = values.find(sectionName);
		if(iter != values.end())
		{
			const StringTable& table = iter->second;
			StringTable::const_iterator iter2 = table.find(keyName);
			if (iter2 != table.end())
			{
				return (iter2->second == "true" || iter2->second == "TRUE" || iter2->second == "on" || iter2->second == "ON" || iter2->second == "yes" || iter2->second == "YES");
			}
		}
		return defaultValue;
		
	}
	
	
	void IniFile::setInt(const String sectionName, const String keyName, int value)
	{

		std::map<String,StringTable>::iterator iter = values.find(sectionName);
		char buf[128];
		sprintf(buf,"%d",value);
		if(iter != values.end())
		{
			iter->second[keyName] = value;
		}
		else
		{
			StringTable t;
			t[keyName] = (value?"yes":"no");
			values[sectionName] = t;
		}
	}
	void IniFile::setBool(const String sectionName, const String keyName, bool value)
	{

		std::map<String,StringTable>::iterator iter = values.find(sectionName);
		if(iter != values.end())
		{
			iter->second[keyName] = value;
		}
		else
		{
			StringTable t;
			t[keyName] = (value?"yes":"no");
			values[sectionName] = t;
		}
	}
			
	void IniFile::set(const String sectionName, const String keyName, const String value)
	{
		std::map<String,StringTable>::iterator iter = values.find(sectionName);
		if(iter != values.end())
		{
			iter->second[keyName] = value;
		}
		else
		{
			StringTable t;
			t[keyName] = value;
			values[sectionName] = t;
		}
	}
	void IniFile::write(std::ostream& out) const
	{
		out<<std::endl;
		out<<"# --------------------------------------------------- "<<std::endl;
		out<<"#	 IniFile saved at : "<<Timer::timeStamp()<<std::endl;
		out<<"# --------------------------------------------------- "<<std::endl;
		std::map<String,StringTable>::const_iterator iter = values.begin();
		while(iter != values.end())
		{
			out<<"["<<iter->first<<"]"<<std::endl;

			const StringTable& table = iter->second;
			StringTable::const_iterator iter2 = table.begin();
			while(iter2 != table.end())
			{

				out<<iter2->first<<"="<<iter2->second<<std::endl;
				iter2++;
			}
			iter++;
		}
		out<<std::endl;
		out.flush();
	}
	void IniFile::read(std::istream& in)
	{
		char linebuf[DDSS_INIFILE_MAX_LINE_LEN+1];
		memset(linebuf,0,DDSS_INIFILE_MAX_LINE_LEN+1);

		bool firstSectionFound = false;
		String sectionName = "";
		while(in.good() && !in.eof())
		{
			in.getline(linebuf,DDSS_INIFILE_MAX_LINE_LEN);
			String line = Helper::trimString(linebuf);
			if(line.size() > 0 && line[0] != '#')
			{
				if(line[0] == '[')
				{
					
					if(line[line.size()-1] == ']')
					{
						firstSectionFound = true;
						sectionName = line.substr(1,line.size() - 2);
						
					}
					else
					{
						DDSS_ERR("IniFile")<<"Malformed Section Header. closing bracket missing. {"<<line<<"}"<<std::endl;
						return;
					}
				}
				else
				{
					size_t index = line.find_first_of('=');
					if(index < line.size())
					{
						if(firstSectionFound)
						{
							String name = line.substr(0,index);
							String val = line.substr(index+1);
							set(sectionName,name,val);
						}
						else
						{
							DDSS_WARN("Inifile")<<"Failed to parse IniFile. Name-Value Pair {"<<line<<"} specified without a valid section header!!"<<std::endl;
							return;
						}
						
					}
				}
			
			}

		}
	}
	void IniFile::clear()
	{
		values.clear();
	}
	size_t IniFile::getSectionNames(std::vector<String>& sectionNames) const
	{
		size_t count = 0;
		std::map<String,StringTable>::const_iterator iter = values.begin();
		while(iter != values.end())
		{
			sectionNames.push_back(iter->first);
			iter++;
			count++;
		}
		return count;
	}
	size_t IniFile::getKeyNames(const String sectionName, std::vector<String>& keyNames) const
	{
		size_t count = 0;
		std::map<String,StringTable>::const_iterator iter = values.find(sectionName);
		if(iter != values.end())
		{
			StringTable::const_iterator iter2= iter->second.begin();
			while(iter2 != iter->second.end())
			{
				
				keyNames.push_back(iter2->first);
				iter2++;
				count++;
				
			}
		}
		return count;
	}
};


