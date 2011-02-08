#ifndef _DDSS_FCGI_OUTPUT_STREAM_H_
#define _DDSS_FCGI_OUTPUT_STREAM_H_
#include "../common/api.h"

namespace dm
{
	
	class FCGIOutputStream : public std::ostream
	{
	public:
		FCGIOutputStream(fcgi_streambuf* buf);
		virtual ~FCGIOutputStream();
	private:
		DDSS_FORCE_BY_REF_ONLY(FCGIOutputStream);
	};
};
#endif

