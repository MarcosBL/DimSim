#ifndef _DDSS_INIFILE_H_
#define _DDSS_INIFILE_H_
#include "Includes.h"

#define DDSS_INIFILE_MAX_LINE_LEN	10239
namespace dm
{
	class IniFile
	{
	public:
		IniFile();
		virtual ~IniFile();
		bool hasSection(const String sectionName) const;
		bool hasKey(const String sectionName, const String keyName) const;
		const String get(const String sectionName, const String keyName, const String defaultValue = "") const;
		s32 getInt(const String sectionName, const String keyName, int defaultValue = 0);
		bool getBool(const String sectionName, const String keyName, bool defaultValue = false);
		
		void set(const String sectionName, const String keyName, const String value);
		void setInt(const String sectionName, const String keyName, int value);
		void setBool(const String sectionName, const String keyName, bool value);
				
		void write(std::ostream& out) const;
		void read(std::istream& in);
		size_t getSectionNames(std::vector<String>& sectionNames) const;
		size_t getKeyNames(const String sectionName, std::vector<String>& keyNames) const;
		void clear();
	private:
		std::map<String,StringTable> values;
		DDSS_FORCE_BY_REF_ONLY(IniFile);
	};
};
#endif


