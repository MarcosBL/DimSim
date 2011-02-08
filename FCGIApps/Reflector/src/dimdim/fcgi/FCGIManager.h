#ifndef _DDSS_FCGI_MANAGER_H_
#define _DDSS_FCGI_MANAGER_H_
#include "../common/api.h"
#include "FCGIHelper.h"
#include "FCGIApp.h"

#define DDSS_FCGI_URL_PATTERN_DEFAULT	"/dimdimScreenShare/"
namespace dm
{
	class FCGIManager
	{
	public:
		static FCGIManager* getInstance();
		void init(IniFile& iniFile);
		void cleanup();
		void run();
	private:
		FCGIManager();
		virtual ~FCGIManager();
	};
};
#endif

