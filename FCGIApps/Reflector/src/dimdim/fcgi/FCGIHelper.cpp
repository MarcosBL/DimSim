#include "FCGIHelper.h"

namespace dm
{
	const size_t FCGIHelper::STDIN_MAX = 1000000;
	void FCGIHelper::printEnv(const char * const * envp, std::ostream& os)
	{
		for ( ; *envp; ++envp)
	    {
	        os << *envp << "\n";
	    }
		
	}
	size_t FCGIHelper::readRequestBuffer(FCGX_Request * request, char ** content, std::istream& in)
	{
		char * clenstr = FCGX_GetParam("CONTENT_LENGTH", request->envp);
	    size_t clen = STDIN_MAX;

	    if (clenstr)
	    {
	        clen = (size_t)strtol(clenstr, &clenstr, 10);
	        if (*clenstr)
	        {
	            Logger::error() << "can't parse \"CONTENT_LENGTH="
	                 << FCGX_GetParam("CONTENT_LENGTH", request->envp)
	                 << "\"\n";
	            clen = STDIN_MAX;
	        }

	        // *always* put a cap on the amount of data that will be read
	        if (clen > STDIN_MAX) clen = STDIN_MAX;
	        
	        Logger::verbose()<<"CLEN : "<<clen<<std::endl;
	        *content = new char[clen];
	        in.read(*content, clen);
	        clen = in.gcount();
	    }
	    else
	    {
	        // *never* read stdin when CONTENT_LENGTH is missing or unparsable
	        *content = 0;
	        clen = 0;
	        Logger::verbose()<<"Content-Length is ZERO!"<<std::endl;
	    }

	    // Chew up any remaining stdin - this shouldn't be necessary
	    // but is because mod_fastcgi doesn't handle it correctly.

	    // ignore() doesn't set the eof bit in some versions of glibc++
	    // so use gcount() instead of eof()...
	    do in.ignore(1024); while (in.gcount() == 1024);

	    return clen;
	}
};

