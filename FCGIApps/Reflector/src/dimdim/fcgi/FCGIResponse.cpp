#include "FCGIResponse.h"

namespace dm
{
	FCGIResponse::FCGIResponse(FCGX_Request& req, std::ostream& out) : request(req),
																	   fcgiOut(out),
																	   code(200),
																	   contentType("text/html")
	{
		setContentType("text/html");
		setCode(200);
	}
	FCGIResponse::~FCGIResponse()
	{
	}
	void FCGIResponse::sendResponse()
	{
		ByteBuffer* buf = bufferOut.get();
		if(code != 200)
		{
			fcgiOut<<"Status: "<<code<<"\r\n";
			fcgiOut<<"Connection: close\r\n";
		}
		if(buf->getLength() > 0)
		{
			fcgiOut<<"Content-type: "<<contentType<<"\r\n";
			fcgiOut<<"Content-length: "<<buf->getLength()<<"\r\n\r\n";
			fcgiOut.write((const char*)(buf->getData()),(int)(buf->getLength()));
			
			/*std::ostream& os =*/ DDSS_VERBOSE("FCGI")<<"Sending : "<<buf->getLength()<<" bytes!!!"<<std::endl;
			//Dump::hexDump(os, buf->getData(),buf->getLength());
			//std::cout<<"SENT "<<buf->getLength()<<" bytes!!!!"<<std::endl;
			//size_t l = 36;
			//if(l < buf->getLength())
			//{
			//	l = buf->getLength();
			//}
			//Dump::hexDump(std::cout, buf->getData(),l);
			
		}
		else
		{
			fcgiOut<<"Content-length: 0\r\n\r\n";
			
		}

	}
};

