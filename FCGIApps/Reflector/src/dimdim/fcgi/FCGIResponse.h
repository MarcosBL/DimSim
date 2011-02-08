#ifndef _FCGI_RESPONSE_H_
#define _FCGI_RESPONSE_H_
#include "FCGIHelper.h"
#include "FCGIOutputStream.h"

namespace dm
{
	class FCGIResponse
	{
	public:
		FCGIResponse(FCGX_Request& req, std::ostream& out);
		virtual ~FCGIResponse();
		void setCode(int c){ code = c; }
		void setContentType(String ctype){ contentType = ctype; }
		OutputStream& getOutputStream(){ return bufferOut; }
		void sendResponse();
	private:

		FCGX_Request& request;
		std::ostream& fcgiOut;
		int code;
		String contentType;
		ByteBufferOutputStream bufferOut;
		DDSS_FORCE_BY_REF_ONLY(FCGIResponse);
	};
};

#endif

