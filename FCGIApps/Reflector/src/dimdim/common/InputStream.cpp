#include "InputStream.h"
#include "Helper.h"
#include "ByteOrder.h"

namespace dm
{
	
	size_t InputStream::readByte(u8* val)
	{
		return read(val,sizeof(u8));
	}
	size_t InputStream::readShort(u16* val)
	{
		size_t bread = read(val,sizeof(u16));
		*val = ByteOrder::swapBytesShort(*val);
		return bread;
	}
	size_t InputStream::readMedium(u32* val)
	{
		*val = 0;
		u8 octets[3];
		memset(octets,0,3);
		size_t bread = read(octets,3);

		u32 ret = octets[0] * 256 * 256;
		ret += octets[1] * 256;
		ret += octets[2];
		*val = ret;
		return bread;
	}
	size_t InputStream::readLong(u32* val)
	{
		size_t bread =  read(val,sizeof(u32));
		*val = ByteOrder::swapBytesLong(*val);
		return bread;
	}
	size_t InputStream::readFloat(f32* val)
	{
		size_t bread =  read(val,sizeof(f32));
		*val = ByteOrder::swapBytesFloat(*val);
		return bread;
	}
	size_t InputStream::readDouble(f64* val)
	{
		size_t bread =  read(val,sizeof(f64));
		*val = ByteOrder::swapBytesDouble(*val);
		return bread;
	}
	/// read len bytes into a string which does not include the null terminator
	size_t InputStream::readStringExact(String& str, size_t len)
	{
		str = "";
		u8 *buf = new u8[len+1];
		memset(buf,0,len+1);
		ScopedArray<u8> bufPtr(buf);
		size_t bread = read(buf,len);
		str = (c8*)buf;
		return bread;
	}

	size_t InputStream::peekByte(u8* val)
	{
		return peek(val,sizeof(u8));
	}
	size_t InputStream::peekShort(u16* val)
	{
		size_t bread = peek(val,sizeof(u16));
		*val = ByteOrder::swapBytesShort(*val);
		return bread;
	}
	size_t InputStream::peekMedium(u32* val)
	{
		*val = 0;
		u8 octets[3];
		memset(octets,0,3);
		size_t bread = peek(octets,3);
		u32 ret = octets[0] * 256 * 256;
		ret += octets[1] * 256;
		ret += octets[2];
		//std::cout<<"Medium Value Should Be : "<<ret<<std::endl;
		*val = ret;
		return bread;
	}
	size_t InputStream::peekLong(u32* val)
	{
		size_t bread =  peek(val,sizeof(u32));
		*val = ByteOrder::swapBytesLong(*val);
		return bread;
	}
	size_t InputStream::peekFloat(f32* val)
	{
		size_t bread =  peek(val,sizeof(f32));
		*val = ByteOrder::swapBytesFloat(*val);
		return bread;
	}
	size_t InputStream::peekDouble(f64* val)
	{
		size_t bread =  peek(val,sizeof(f64));
		*val = ByteOrder::swapBytesDouble(*val);
		return bread;
	}
	/// peek len bytes into a string which does not include the null terminator
	size_t InputStream::peekStringExact(String& str, size_t len)
	{
		str = "";
		u8 *buf = new u8[len+1];
		memset(buf,0,len+1);
		ScopedArray<u8> bufPtr(buf);
		size_t bread = peek(buf,len);
		str = (c8*)buf;
		return bread;
	}
	size_t InputStream::readShortLE(u16* val)
	{
		return read(val,sizeof(u16));
	}
	size_t InputStream::readLongLE(u32* val)
	{
		return read(val,sizeof(u32));
	}
	size_t InputStream::readFloatLE(f32* val)
	{
		return read(val,sizeof(f32));
	}
	size_t InputStream::readDoubleLE(f64* val)
	{
		return read(val,sizeof(f64));
	}

	size_t InputStream::peekShortLE(u16* val)
	{
		return peek(val,sizeof(u16));
	}
	size_t InputStream::peekLongLE(u32* val)
	{
		return peek(val,sizeof(u32));
	}
	size_t InputStream::peekFloatLE(f32* val)
	{
		return peek(val,sizeof(f32));
	}
	size_t InputStream::peekDoubleLE(f64* val)
	{
		return peek(val,sizeof(f64));
	}
};



