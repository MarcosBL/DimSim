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
    File Name  : dFileInputStream.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_TOOLKIT_FILE_INPUT_STREAM_H_
#define _DIMDIM_TOOLKIT_FILE_INPUT_STREAM_H_
#include "InputStream.h"

namespace dm
{
	///
	///	A Big Endian File Input Stream
	///
	class  FileInputStream : public InputStream
	{
	public:
		FileInputStream();
		virtual ~FileInputStream();
		void open(const String fileName);
		void close();

		bool eof();
		bool isValid();	
		size_t read(void* buffer, size_t len);
		size_t peek(void* buffer, size_t len);
		size_t skip(size_t len);
		size_t getMarkerPosition();
		size_t getRemainingByteCount();
		size_t getLength();
		void   rewind();
	private:
		std::ifstream inputFile;
		size_t length;
		DSS_FORCE_BY_REF_ONLY(FileInputStream);

	};
};
#endif
