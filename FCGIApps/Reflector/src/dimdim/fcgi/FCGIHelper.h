#ifndef _DDSS_FCGI_HELPER_H_
#define _DDSS_FCGI_HELPER_H_

#include "../common/api.h"
namespace dm
{
	class FCGIHelper
	{
	public:
		static const size_t STDIN_MAX;
		static void printEnv(const char * const * envp, std::ostream& os);
		static size_t readRequestBuffer(FCGX_Request * request, char ** content, std::istream& in);
	};
};
#endif

