//$$<!--TOFUTAG-->$$//
 /************************************************************************** 	 
  *                                                                        *
  *               DDDDD   iii             DDDDD   iii                      * 
  *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           * 
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
  *                                                                        *
  **************************************************************************
/* *************************************************************************	
	THIS FILE IS PART OF THE DIMDIM CODEBASE. TO VIEW LICENSE AND EULA
	FOR THIS CODE VISIT http://www.dimdim.com
   ************************************************************************ */

/* ------------------------------------------------------ 
    File Name  : dFileOutputStream.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#include "FileOutputStream.h"

namespace dm
{
	
	FileOutputStream::FileOutputStream()
	{
	}
	FileOutputStream::~FileOutputStream()
	{
		try{ close(); } catch(...){}
	}

	void FileOutputStream::open(const String fileName)
	{
		close();
		outputFile.open(fileName.c_str(),std::ios::binary | std::ios::out);
	}

	void FileOutputStream::close()
	{
		if(outputFile.is_open())
		{
			outputFile.close();
		}
	}

	bool FileOutputStream::isValid()
	{
		return outputFile.is_open();
	}
	size_t FileOutputStream::write(const void* buf, size_t len)
	{
		if(isValid() && buf && len > 0)
		{
			outputFile.write((const char*)buf,(std::streamsize)len);
		}
		return 0;
	}
};
