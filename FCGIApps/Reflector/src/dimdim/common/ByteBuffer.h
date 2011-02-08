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
    File Name  : dByteBuffer.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_TOOLKIT_BYTE_BUFFER_H_
#define _DIMDIM_TOOLKIT_BYTE_BUFFER_H_
#include "Includes.h"

namespace dm
{
	
	///
	///	A simple dynamic byte buffer class
	///
	class DSSFRAMEWORKAPI ByteBuffer
	{
	public:
		ByteBuffer(size_t len=0);
		ByteBuffer(const void* buffer, size_t buflen, bool createCopy);
		virtual ~ByteBuffer(void);
		ByteBuffer* clone();
		ByteBuffer* cloneAndRelease(){ ByteBuffer* cb = clone(); destroy(); return cb; }
		void reallocate(size_t newLen, bool keepContents=false);
		void zeroMemory();
		void destroy();
		bool isNull() const;
		u8* getData(size_t offset=0);
		const u8* getData(size_t offset=0) const;
		size_t getRemainingLength(size_t offset=0) const;
		size_t getLength() const{ return getRemainingLength(0); }
		void append(const void* buffer, size_t bufLen);
		void append(const ByteBuffer* buffer);
		void copyTo(void* buffer, size_t bufLen, size_t dataOffset = 0);
		void copyFrom(const void* buffer, size_t bufLen, size_t dataOffset = 0);

		u8 getByte(size_t index) const{ return data[index]; }
		void setByte(size_t index, u8 val){ data[index] = val; }
	private:
		u8 *data;
		size_t dataLen;
		bool doNotDelete;
		DDSS_FORCE_BY_REF_ONLY(ByteBuffer);

	};
};
#endif


