#ifndef _dss_byte_order_h_
#define _dss_byte_order_h_
#include "Includes.h"

#ifndef LOWORD
typedef unsigned char BYTE;
typedef unsigned short WORD;
typedef unsigned long  LONG;
typedef unsigned long DWORD_PTR;
typedef unsigned long DWORD;

#define MAKEWORD(a, b) ((WORD)(((BYTE)((DWORD_PTR)(a) & 0xff)) | ((WORD)((BYTE)((DWORD_PTR)(b) & 0xff))) << 8))
#define MAKELONG(a, b) ((LONG)(((WORD)((DWORD_PTR)(a) & 0xffff)) | ((DWORD)((WORD)((DWORD_PTR)(b) & 0xffff))) << 16))
#define LOWORD(l) ((WORD)((DWORD_PTR)(l) & 0xffff)) 
#define HIWORD(l) ((WORD)((DWORD_PTR)(l) >> 16))
#define LOBYTE(w) ((BYTE)((DWORD_PTR)(w) & 0xff))
#define HIBYTE(w) ((BYTE)((DWORD_PTR)(w) >> 8))
#endif

#define SWAP_PIXEL8(pixel)  (pixel)

#define SWAP_PIXEL16(pixel)                     \
  (((pixel) << 8 & 0xFF00) |                    \
   ((pixel) >> 8 & 0x00FF))

#define SWAP_PIXEL32(pixel)                     \
  (((pixel) << 24 & 0xFF000000) |               \
   ((pixel) << 8  & 0x00FF0000) |               \
   ((pixel) >> 8  & 0x0000FF00) |               \
   ((pixel) >> 24 & 0x000000FF))

namespace dm
{
	class DSSFRAMEWORKAPI ByteOrder
	{
	public:
		enum Endian
		{
			B_ENDIAN = 0,
			L_ENDIAN = 1
		};
		static ByteOrder& getInstance();

		bool isBigEndian() const;
		bool isLittleEndian() const;

		static u16 makeWord(u8 hi, u8 lo);
		static u32 makeLong(u16 hi, u16 lo);
		
		static void splitWord(u16 val, u8& hi, u8& lo);
		static void splitLong(u32 val, u16& hi, u16& lo);

		static u16 swapBytesShort(u16 val);
		static u32 swapBytesLong(u32 val);
		static f32 swapBytesFloat(f32 val);
		static f64 swapBytesDouble(f64 val);

		static const u8* swapBytes(const u8* src, u8* dst, size_t len);
		static const u8* swapBytes(u8* src, size_t len);
	private:
		Endian endian;
		ByteOrder();
		virtual ~ByteOrder();
		static ByteOrder *sInstance;
		DDSS_FORCE_BY_REF_ONLY(ByteOrder);
	};
};
#endif


