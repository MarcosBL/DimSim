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
    File Name  : dByteBufferOutputStream.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_TOOLKIT_BYTE_BUFFER_OUTPUT_STREAM_H_
#define _DIMDIM_TOOLKIT_BYTE_BUFFER_OUTPUT_STREAM_H_
#include "OutputStream.h"
#include "ByteBuffer.h"
#include "Helper.h"

namespace dm
{
	///
	///	a binary big endian output stream which writes to a byte buffer in memory
	///
	class DSSFRAMEWORKAPI ByteBufferOutputStream : public OutputStream
	{
	public:
		ByteBufferOutputStream();
		ByteBufferOutputStream(ByteBuffer* buffer, bool deleteOnExit);
		virtual ~ByteBufferOutputStream();
		bool isValid() const;
		bool isValid();
		size_t write(const void* buf, size_t len);
		ByteBuffer* get();
		const ByteBuffer* get() const;
		ByteBuffer* release();
	private:
		ScopedPointer<ByteBuffer> bufferPtr;
		bool deleteBufferOnExit;
		DDSS_FORCE_BY_REF_ONLY(ByteBufferOutputStream);
	};
};
#endif


