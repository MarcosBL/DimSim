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
    File Name  : dByteBufferInputStream.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_TOOLKIT_BYTE_BUFFER_INPUT_STREAM_H_
#define _DIMDIM_TOOLKIT_BYTE_BUFFER_INPUT_STREAM_H_
#include "InputStream.h"
#include "ByteBuffer.h"
#include "Helper.h"

namespace dm
{
	///
	///	An input stream that writes to an in memory byte buffer
	///
	class DSSFRAMEWORKAPI ByteBufferInputStream : public InputStream
	{
	public:
		ByteBufferInputStream(ByteBuffer* buffer, bool deleteOnExit);
		ByteBufferInputStream(const u8* buffer, size_t buflen, bool deleteOnExit=false);
		virtual ~ByteBufferInputStream();	
	    bool eof();
		bool isValid();	
		size_t read(void* buffer, size_t len);
		size_t peek(void* buffer, size_t len);
		size_t skip(size_t len);
		size_t getMarkerPosition();
		size_t getRemainingByteCount();
		size_t getLength();
		void   rewind();
		ByteBuffer* get();
		const ByteBuffer* get() const;
		ByteBuffer* release();
	private:
		
		ScopedPointer<ByteBuffer> bufferPtr;
		bool deleteBufferOnExit;
		size_t marker;
		DDSS_FORCE_BY_REF_ONLY(ByteBufferInputStream);
	};
};
#endif


