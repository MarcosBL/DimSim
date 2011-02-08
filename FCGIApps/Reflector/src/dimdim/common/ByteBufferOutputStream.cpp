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
    File Name  : dByteBufferOutputStream.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#include "ByteBufferOutputStream.h"
#include "Helper.h"
namespace dm
{
	ByteBufferOutputStream::ByteBufferOutputStream() : bufferPtr(new ByteBuffer()), deleteBufferOnExit(true)
	{
	}
	ByteBufferOutputStream::ByteBufferOutputStream(ByteBuffer* buffer, bool deleteOnExit) : bufferPtr(buffer), deleteBufferOnExit(deleteOnExit)
	{
	}
	ByteBufferOutputStream::~ByteBufferOutputStream()
	{
		if(!deleteBufferOnExit)
		{
			release();
		}
	}
	bool ByteBufferOutputStream::isValid()
	{
		return !bufferPtr.isNull();
	}
	bool ByteBufferOutputStream::isValid() const
	{
		return !bufferPtr.isNull();
	}
	size_t ByteBufferOutputStream::write(const void* buf, size_t len)
	{

		
		if(isValid() && buf && len > 0)
		{
			bufferPtr->append(buf,len);
			//HexDump::dumpHex(std::cout,bufferPtr->getData(),bufferPtr->getLength());
			return len;
		}
		return 0;
	}
	ByteBuffer* ByteBufferOutputStream::get()
	{
		return bufferPtr.get();
	}
	const ByteBuffer* ByteBufferOutputStream::get() const
	{
		return bufferPtr.get();
	}
	ByteBuffer* ByteBufferOutputStream::release()
	{
		return bufferPtr.release();
	}



};


