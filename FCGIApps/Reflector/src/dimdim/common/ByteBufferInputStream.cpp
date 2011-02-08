//$$<!--TOFUTAG-->$$//
 /************************************************************************** 	 
  *                                                                        *
  *               DDDDD   iii             DDDDD   iii                      * 
  *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           * 
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
  *                                                                        *
  **************************************************************************/
/* *************************************************************************	
	THIS FILE IS PART OF THE DIMDIM CODEBASE. TO VIEW LICENSE AND EULA
	FOR THIS CODE VISIT http://www.dimdim.com
   ************************************************************************ */

/* ------------------------------------------------------ 
    File Name  : dByteBufferInputStream.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#include "ByteBufferInputStream.h"

namespace dm
{
	
	ByteBufferInputStream::ByteBufferInputStream(ByteBuffer* buffer, bool deleteOnExit) : bufferPtr(buffer),
																						  deleteBufferOnExit(deleteOnExit),
																						  marker(0)
	{
	}
	ByteBufferInputStream::ByteBufferInputStream(const u8* buffer, size_t buflen, bool deleteOnExit) : bufferPtr(new ByteBuffer(buffer,buflen,deleteOnExit)),
															 deleteBufferOnExit(true),
															 marker(0)
	{
		
	}
	ByteBufferInputStream::~ByteBufferInputStream()
	{
		if(!deleteBufferOnExit)
		{
			release();
		}
	}	
	bool ByteBufferInputStream::eof()
	{
		if(isValid())
		{
			return getRemainingByteCount() == 0;
		}
		return true;
	}
	bool ByteBufferInputStream::isValid() 
	{
		return !bufferPtr.isNull();
	}
	size_t ByteBufferInputStream::read(void* buffer, size_t len)
	{
		size_t bread = peek(buffer,len);
		skip(bread);
		return bread;
	}
	size_t ByteBufferInputStream::peek(void* buffer, size_t len)
	{
		//std::cout<<" peek("<<len<<") => marker = "<<marker<<std::endl;
		size_t bcount = getRemainingByteCount();
		if(bcount > len)
		{
			bcount = len;
		}
		if(bcount > 0)
		{
			memset(buffer,0,len);
			bufferPtr->copyTo(buffer,bcount,marker);
		}
		//std::cout<<" peek() :: Returning : "<<bcount<<std::endl;
		return bcount;

	}
	size_t ByteBufferInputStream::skip(size_t len)
	{
		//std::cout<<" skip("<<len<<")"<<std::endl;
		//std::cout<<"     MARKER (OLD) : "<<marker<<std::endl;
		size_t bcount = getRemainingByteCount();
		//std::cout<<"     REMAINING BYTES :: "<<bcount<<std::endl;
		//std::cout<<"     REQUESTED BYTES :: "<<len<<std::endl;
		if(bcount > len)
		{
			bcount = len;
		}
		marker += bcount;
		//std::cout<<"     MARKER (NEW) : "<<marker<<std::endl;
		return bcount;
	}
	size_t ByteBufferInputStream::getMarkerPosition()
	{
		return marker;
	}
	size_t ByteBufferInputStream::getRemainingByteCount()
	{
		if(!bufferPtr.isNull())
		{
			return bufferPtr->getRemainingLength(marker);
		}
		return 0;
	}
	size_t ByteBufferInputStream::getLength()
	{
		if(!bufferPtr.isNull())
		{
			return bufferPtr->getLength();
		}
		return 0;
	}
	void   ByteBufferInputStream::rewind()
	{
		marker = 0;
	}
	ByteBuffer* ByteBufferInputStream::get()
	{
		return bufferPtr.get();
	}
	const ByteBuffer* ByteBufferInputStream::get() const
	{
		return bufferPtr.get();
	}
	ByteBuffer* ByteBufferInputStream::release()
	{
		return bufferPtr.release();
	}
};


