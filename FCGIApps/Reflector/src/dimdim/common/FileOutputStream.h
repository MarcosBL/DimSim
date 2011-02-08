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
    File Name  : dFileOutputStream.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FILE_OUTPUT_STREAM_H_
#define _DIMDIM_FILE_OUTPUT_STREAM_H_
#include "Includes.h"
#include "OutputStream.h"

namespace dm
{
	class FileOutputStream : public OutputStream
	{
	public:
		FileOutputStream();
		virtual ~FileOutputStream();

		void open(const String fileName);
		void close();

		bool isValid(); 
		size_t write(const void* buf, size_t len);

	private:
		std::ofstream outputFile;
		DSS_FORCE_BY_REF_ONLY(FileOutputStream);
	};
};
#endif
