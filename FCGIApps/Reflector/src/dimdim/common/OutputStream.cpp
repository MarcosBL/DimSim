#include "OutputStream.h"
#include "Helper.h"
#include "ByteOrder.h"
namespace dm
{
	
	size_t OutputStream::writeByte(u8 val)
	{
		return write(&val,1);
	}
	size_t OutputStream::writeShort(u16 val)
	{
		val = ByteOrder::swapBytesShort(val);
		return write(&val,2);
	}
	size_t OutputStream::writeMedium(u32 val)
	{
		val = ByteOrder::swapBytesLong(val);
		u8 *octets = (u8 *)&val;
		return write(octets + 1, 3);
	}
	size_t OutputStream::writeLong(u32 val)
	{
		val = ByteOrder::swapBytesLong(val);
		return write(&val,4);
	}
	size_t OutputStream::writeFloat(f32 val)
	{
		val = ByteOrder::swapBytesFloat(val);
		return write(&val,4);
	}
	size_t OutputStream::writeDouble(f64 val)
	{
		val = ByteOrder::swapBytesDouble(val);
		return write(&val,8);
	}
	size_t OutputStream::writeStringExact(const String& str)
	{
		return write(str.c_str(),str.size());
	}
	size_t OutputStream::writeStringWithNull(const String& str)
	{
		return write(str.c_str(),str.size()+1);
	}
	size_t OutputStream::writeShortLE(u16 val)
	{
		return write(&val,2);
	}
	size_t OutputStream::writeLongLE(u32 val)
	{
		return write(&val,4);
	}
	size_t OutputStream::writeFloatLE(f32 val)
	{
		return write(&val,4);
	}
	size_t OutputStream::writeDoubleLE(f64 val)
	{
		return write(&val,8);
	}
};


