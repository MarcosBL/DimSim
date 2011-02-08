#include "ByteOrder.h"
#include "Helper.h"
namespace dm
{
	
	ByteOrder* ByteOrder::sInstance = 0;
	ByteOrder& ByteOrder::getInstance()
	{
		if(sInstance == 0)
		{
			sInstance = new ByteOrder();
		}
		return *sInstance;
	}

	bool ByteOrder::isBigEndian() const
	{
		return endian == ByteOrder::B_ENDIAN;
	}
	bool ByteOrder::isLittleEndian() const
	{
		return endian == ByteOrder::L_ENDIAN;
	}
	ByteOrder::ByteOrder() : endian(ByteOrder::L_ENDIAN)
	{
		u16 val = 0x01;
		char octets[2];
		memcpy(octets,&val,2);
		if(octets[0] == 0x01)
		{
			endian = ByteOrder::L_ENDIAN;
		}
		else
		{
			endian = ByteOrder::B_ENDIAN;
		}
	}
	ByteOrder::~ByteOrder()
	{
	}
	u16 ByteOrder::makeWord(u8 hi, u8 lo)
	{
		return MAKEWORD(lo,hi);
	}
	u32 ByteOrder::makeLong(u16 hi, u16 lo)
	{
		return MAKELONG(lo,hi);
	}
	void ByteOrder::splitWord(u16 val, u8& hi, u8& lo)
	{
		hi = HIBYTE(val);
		lo = LOBYTE(val);
	}
	void ByteOrder::splitLong(u32 val, u16& hi, u16& lo)
	{
		hi = HIWORD(val);
		lo = LOWORD(val);
	}u16 ByteOrder::swapBytesShort(u16 val)
	{
		u8 octets[2];
		memcpy(octets,&val,2);
		Helper::swapValues(octets[0],octets[1]);
		return *(u16 *)octets;

	}
	u32 ByteOrder::swapBytesLong(u32 val)
	{
		u8 octets[4];
		memcpy(octets,&val,4);
		Helper::swapValues(octets[0],octets[3]);
		Helper::swapValues(octets[1],octets[2]);
		return *(u32 *)octets;

	}
	f32 ByteOrder::swapBytesFloat(f32 val)
	{
		u8 octets[4];
		memcpy(octets,&val,4);
		Helper::swapValues(octets[0],octets[3]);
		Helper::swapValues(octets[1],octets[2]);
		return *(f32 *)octets;
	}
	f64 ByteOrder::swapBytesDouble(f64 val)		
	{
		u8 octets[8];
		memcpy(octets,&val,8);
		Helper::swapValues(octets[0],octets[7]);
		Helper::swapValues(octets[1],octets[6]);
		Helper::swapValues(octets[2],octets[5]);
		Helper::swapValues(octets[3],octets[4]);
		return *(f64 *)octets;

	}

	const u8* ByteOrder::swapBytes(const u8* src, u8* dst, size_t len)
	{
		for(size_t s = len; s >0; s--)
		{
			dst[s] = src[len - s - 1];
		}
		return dst;
	}
	const u8* ByteOrder::swapBytes(u8* src, size_t len)
	{
		size_t pivot = len / 2;

		for(size_t s = 0; s < pivot; s++)
		{
			u8 tmp = src[s];
			src[s] = src[len - s - 1];
			src[len - s - 1] = tmp;
		}
		return src;
	}
};


