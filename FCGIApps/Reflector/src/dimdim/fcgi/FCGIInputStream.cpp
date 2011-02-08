#include "FCGIInputStream.h"

namespace dm
{
	
	FCGIInputStream::FCGIInputStream(fcgi_streambuf* buf) : std::istream(0)
	{ 
		rdbuf(buf);
	}
	FCGIInputStream::~FCGIInputStream()
	{
	}
};

