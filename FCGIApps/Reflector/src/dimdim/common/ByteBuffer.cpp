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
    File Name  : dByteBuffer.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#include "ByteBuffer.h"
#include "Helper.h"
#include "ByteOrder.h"
#include "Helper.h"
#include <cassert>
namespace dm
{
		
	ByteBuffer::ByteBuffer(size_t len) : data(0), dataLen(0), doNotDelete(false)
	{
		////DSS_DEBUG("Entering:	ByteBuffer::ByteBuffer");
		reallocate(len,false);
	}

	ByteBuffer::ByteBuffer(const void* buffer, size_t buflen, bool createCopy) : data(0),dataLen(0), doNotDelete(false)
	{
		////DSS_DEBUG("Entering:	ByteBuffer::ByteBuffer");

		if(createCopy)
		{
			reallocate(buflen,false);
			copyFrom(buffer,buflen);
		}
		else
		{
			doNotDelete = true;
			data = (u8*)buffer;
			dataLen = buflen;
		}
	}
	ByteBuffer::~ByteBuffer(void)
	{
		////DSS_DEBUG("Entering:	ByteBuffer::~ByteBuffer");
		destroy();
	}
	void ByteBuffer::reallocate(size_t newLen, bool keepContents)
	{
		////DSS_DEBUG("Entering:	ByteBuffer::reallocate");
		assert(!doNotDelete && "Buffer needs to be owned for reallocation");
		if(newLen == dataLen)
		{
			if(!keepContents)
			{
				zeroMemory();
			}
			////DSS_DEBUG("Leaving:		ByteBuffer::reallocate");
			return;
		}
		bool keep = keepContents && (newLen > dataLen) && !isNull();
		
		u8* tmp = 0;
		size_t tmpLen = 0;
		ScopedArray<u8> tmpPtr(tmp);

		if(keep)
		{
			tmp = new u8[dataLen];
			tmpLen = dataLen;
			copyTo(tmp,dataLen);
		}

		destroy();
		data = new u8[newLen];
		dataLen = newLen;
		zeroMemory();


		if(keep)
		{
			copyFrom(tmp,tmpLen);
			delete[] tmp;
		}

		////DSS_DEBUG("Leaving:		ByteBuffer::reallocate");
	}
	void ByteBuffer::zeroMemory()
	{
		////DSS_DEBUG("Entering:		ByteBuffer::zeroMemory");

		if(data && dataLen > 0)
		{
			memset(data,0,dataLen);
		}
	}
	void ByteBuffer::destroy()
	{
		////DSS_DEBUG("Entering:		ByteBuffer::destroy");

		if(data)
		{
			if(!this->doNotDelete)
			{
				if(data)
				{
					delete[] data;
				}
			}
		}
		data = 0;
		dataLen = 0;
	}
	bool ByteBuffer::isNull() const
	{
		return data == 0;
	}
	u8* ByteBuffer::getData(size_t offset)
	{
		return isNull()?0:(data + offset);
	}
	const u8* ByteBuffer::getData(size_t offset) const
	{
		return isNull()?0:(data + offset);
	}
	size_t ByteBuffer::getRemainingLength(size_t offset) const
	{
		if(!isNull())
		{
			if(offset < dataLen)
			{
				return (dataLen - offset);
			}
		}
		return 0;
	}
	void ByteBuffer::append(const void* buffer, size_t bufLen)
	{
		try
		{
				if(buffer && bufLen > 0)
		{
			size_t oldLen = dataLen;
			size_t newLen = oldLen + bufLen;
			
				reallocate(newLen,true);
				copyFrom(buffer,bufLen,oldLen);
			
		}}
			catch(...){}
	}
	void ByteBuffer::append(const ByteBuffer* buffer)
	{
		if(buffer)
		{
			append(buffer->getData(),buffer->getLength());
		}
	}
	void ByteBuffer::copyTo(void* buffer, size_t bufLen, size_t dataOffset)
	{
		////DSS_DEBUG("Entering:		ByteBuffer::copyTo");

		size_t len = getRemainingLength(dataOffset);
		if(len >= bufLen)
		{
			memcpy(buffer,getData(dataOffset),bufLen);
		}
		else
		{
			memcpy(buffer,getData(dataOffset),len);
		}
	}
	ByteBuffer* ByteBuffer::clone()
	{
		////DSS_DEBUG("Entering:		ByteBuffer::clone");
		if(!isNull())
		{
			return new ByteBuffer(data,dataLen,true);
		}
		else
		{
			return new ByteBuffer();
		}
	}
	void ByteBuffer::copyFrom(const void* buffer, size_t bufLen, size_t dataOffset)
	{
		////DSS_DEBUG("Entering:		ByteBuffer::copyFrom");

		size_t len = getRemainingLength(dataOffset);
		if(len > bufLen)
		{
			memcpy(getData(dataOffset),buffer,bufLen);
		}
		else
		{
			memcpy(getData(dataOffset),buffer,len);
		}
	}

};


