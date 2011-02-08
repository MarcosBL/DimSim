#ifndef _DDSS_FCGI_INPUT_STREAM_H_
#define _DDSS_FCGI_INPUT_STREAM_H_

#include "../common/api.h"

namespace dm
{
		class FCGIInputStream : public std::istream
		{
		public:
			FCGIInputStream(fcgi_streambuf* buf);
			virtual ~FCGIInputStream();
		private:
			DDSS_FORCE_BY_REF_ONLY(FCGIInputStream);
		};
};
#endif

