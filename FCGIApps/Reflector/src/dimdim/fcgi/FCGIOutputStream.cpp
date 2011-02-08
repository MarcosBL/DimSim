#include "FCGIOutputStream.h"

namespace dm
{
	FCGIOutputStream::FCGIOutputStream(fcgi_streambuf* buf) : std::ostream(0)
	{ 
		rdbuf(buf);
	}
	FCGIOutputStream::~FCGIOutputStream()
	{
	}
};

