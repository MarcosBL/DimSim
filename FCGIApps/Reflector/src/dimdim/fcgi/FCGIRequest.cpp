#include "FCGIRequest.h"
#include "FCGIInputStream.h"
namespace dm
{
	
	FCGIRequest::FCGIRequest(FCGX_Request& r, std::istream& in) : request(r), fcgiIn(in)
	{

		char* contentTmp = 0;
		size_t len = FCGIHelper::readRequestBuffer(&request,&contentTmp,fcgiIn);
		ScopedArray<char> ctPtr(contentTmp);
		if(len > 0 && contentTmp)
		{
			DDSS_VERBOSE("FCGIRequest")<<"Read in coming content of length "<<len<<" bytes!"<<std::endl;
			this->content.append(contentTmp,len);
		}

	}
	FCGIRequest::~FCGIRequest()
	{
	}
	FCGX_Request& FCGIRequest::getRequestImpl()
	{
		return request;
	}
	size_t FCGIRequest::getContentLength() const
	{
		return content.getLength();
	}
	const c8* FCGIRequest::getContent() const
	{
		return (const c8*)content.getData();
	}
	const String FCGIRequest::getParam(const String paramKey, bool& paramFound, const String defaultValue) const
	{
		const char* val = ::FCGX_GetParam(paramKey.c_str(),request.envp);
		if(val)
		{
			paramFound = true;
			return val;
		}
		else
		{
			paramFound = false;
			return defaultValue;
		}
	}
	const String FCGIRequest::getRequestURI() const
	{
		bool pf = false;
		return getParam("REQUEST_URI",pf);
	}
};

