#ifndef _dss_output_stream_h_
#define _dss_output_stream_h_
#include "Includes.h"
namespace dm
{
	///
	///	 A Binary Big Endian output stream
	///
	class DSSFRAMEWORKAPI OutputStream
	{
	public:
		OutputStream(){}
		virtual ~OutputStream(){}

		virtual bool isValid() = 0;
		virtual size_t write(const void* buf, size_t len)=0;


		virtual size_t writeByte(u8 val);
		virtual size_t writeShort(u16 val);
		virtual size_t writeMedium(u32 val);
		virtual size_t writeLong(u32 val);
		virtual size_t writeFloat(f32 val);
		virtual size_t writeDouble(f64 val);
		virtual size_t writeStringExact(const String& str);
		virtual size_t writeStringWithNull(const String& str);
		

		virtual size_t writeShortLE(u16 val);
		virtual size_t writeLongLE(u32 val);
		virtual size_t writeFloatLE(f32 val);
		virtual size_t writeDoubleLE(f64 val);

	};
};
#endif


