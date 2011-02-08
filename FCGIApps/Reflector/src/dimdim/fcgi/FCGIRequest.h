#ifndef _DDSS_FCGI_REQUEST_H_
#define _DDSS_FCGI_REQUEST_H_
#include "../common/api.h"
#include "FCGIInputStream.h"
#include "FCGIOutputStream.h"
#include "FCGIHelper.h"
namespace dm
{
	class FCGIRequest
	{
	public:
		FCGIRequest(FCGX_Request& r, std::istream& in);
		virtual ~FCGIRequest();

		FCGX_Request& getRequestImpl();
		size_t getContentLength() const;
		const c8* getContent() const;
		const String getParam(const String paramKey, bool& paramFound, const String defaultValue="") const;
		const String getRequestURI() const;

	private:

		ByteBuffer content;
		FCGX_Request& request;
		std::istream& fcgiIn;
		DDSS_FORCE_BY_REF_ONLY(FCGIRequest);
	};
};
#endif

