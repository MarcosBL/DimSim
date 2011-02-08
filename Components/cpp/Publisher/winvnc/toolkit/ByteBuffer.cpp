#include "ByteBuffer.h"
#include <cassert>

ByteBuffer::ByteBuffer(size_t len) : data(0), dataLen(0), doNotDelete(false)
{
	reallocate(len,false);
}

ByteBuffer::ByteBuffer(const void* buffer, size_t buflen, bool createCopy) : data(0),dataLen(0), doNotDelete(false)
{
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
	destroy();
}

void ByteBuffer::reallocate(size_t newLen, bool keepContents)
{
	assert(!doNotDelete && "Buffer needs to be owned for reallocation");
	if(newLen == dataLen)
	{
		if(!keepContents)
		{
			zeroMemory();
		}
		return;
	}
	bool keep = keepContents && (newLen > dataLen) && !isNull();

	u8* tmp = 0;
	size_t tmpLen = 0;
	ScopedArray<u8> tmpPtr(tmp);

	if(keep)
	{
		tmp = new u8[dataLen + 1];
		tmp[dataLen] = '\0';
		tmpLen = dataLen;
		copyTo(tmp,dataLen);
	}

	destroy();
	data = new u8[newLen + 1];
	data[newLen] = '\0';
	dataLen = newLen;
	zeroMemory();


	if(keep)
	{
		copyFrom(tmp,tmpLen);
		delete[] tmp;
	}
}

void ByteBuffer::zeroMemory()
{
	if(data && dataLen > 0)
	{
		memset(data,0,dataLen);
	}
}

void ByteBuffer::destroy()
{
	if(data)
	{
		if(!this->doNotDelete)
		{
			SAFE_DELETE_ARRAY(data);
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
