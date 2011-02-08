#ifndef _dss_input_stream_h_
#define _dss_input_stream_h_
#include "Includes.h"

namespace dm
{
	
	///
	///	A Base class for a binary input stream which reads in big endian format
	///
	class DSSFRAMEWORKAPI InputStream
	{
	public:
		InputStream(){}
		virtual ~InputStream(){}

		virtual bool eof() = 0;
		virtual bool isValid()= 0;
		
		virtual size_t read(void* buffer, size_t len) = 0;
		virtual size_t peek(void* buffer, size_t len) = 0;
		virtual size_t skip(size_t len)=0;
		virtual size_t getMarkerPosition()= 0;
		virtual size_t getRemainingByteCount() = 0;
		virtual size_t getLength()=0;
		virtual void   rewind()=0;


		virtual size_t readByte(u8* val);
		virtual size_t readShort(u16* val);
		virtual size_t readMedium(u32* val);
		virtual size_t readLong(u32* val);
		virtual size_t readFloat(f32* val);
		virtual size_t readDouble(f64* val);
		

	
		
		/// read len bytes into a string which does not include the null terminator
		virtual size_t readStringExact(String& str, size_t len);

		virtual size_t peekByte(u8* val);
		virtual size_t peekShort(u16* val);
		virtual size_t peekMedium(u32* val);
		virtual size_t peekLong(u32* val);
		virtual size_t peekFloat(f32* val);
		virtual size_t peekDouble(f64* val);
		

		
		/// peek len bytes into a string which does not include the null terminator
		virtual size_t peekStringExact(String& str, size_t len);
		
		
		// LITTLE ENDIAN METHODS
		
		virtual size_t readShortLE(u16* val);
		virtual size_t readLongLE(u32* val);
		virtual size_t readFloatLE(f32* val);
		virtual size_t readDoubleLE(f64* val);

		virtual size_t peekShortLE(u16* val);
		virtual size_t peekLongLE(u32* val);
		virtual size_t peekFloatLE(f32* val);
		virtual size_t peekDoubleLE(f64* val);	

	private:
		DDSS_FORCE_BY_REF_ONLY(InputStream);
	};
};
#endif



