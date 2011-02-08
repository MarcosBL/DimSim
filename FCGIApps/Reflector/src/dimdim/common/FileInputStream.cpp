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
    File Name  : dFileInputStream.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#include "FileInputStream.h"
#include "Logger.h"
namespace dm
{

	FileInputStream::FileInputStream() : length(0)
	{
	}
	FileInputStream::~FileInputStream()
	{
		close();
	}
	void FileInputStream::open(const String fileName)
	{
		close();
		inputFile.open(fileName.c_str(), std::ios::in | std::ios::binary);
		if(!inputFile.is_open())
		{
			
			DDSS_WARN("FileInputStream")<<"Failed to open input file "<<fileName<<"."<<std::ends;
		}
		else
		{
			inputFile.seekg(0,std::ios::end);
			length = (size_t)inputFile.tellg();
			rewind();
		}
	}
	void FileInputStream::close()
	{
		if(inputFile.is_open())
		{
			inputFile.close();
		}
		length = 0;
	}
	bool FileInputStream::eof()
	{
		if(isValid())
		{
			return  inputFile.eof();
		}
		return true;
	}
	bool FileInputStream::isValid()
	{
		return inputFile.is_open();
	}
	size_t FileInputStream::read(void* buffer, size_t len)
	{
		if(isValid())
		{
			std::streamsize pos = inputFile.tellg();
			inputFile.read((char *)buffer,(std::streamsize)len);
			std::streamsize pos2 = inputFile.tellg();
			if(pos2 > pos)
			{
				return pos2 - pos;
			}
			
		}
		return 0;
	}
	size_t FileInputStream::peek(void* buffer, size_t len)
	{
		if(isValid())
		{
			std::streamsize pos = inputFile.tellg();
			inputFile.read((char *)buffer,(std::streamsize)len);
			std::streamsize pos2 = inputFile.tellg();
			inputFile.seekg(pos,std::ios::beg);
			if(pos2 > pos)
			{
				return pos2 - pos;
			}
			
		}
		return 0;
	}
	size_t FileInputStream::skip(size_t len)
	{
		std::streamsize pos = inputFile.tellg();
		inputFile.seekg((std::streamsize)len, std::ios::cur);
		std::streamsize pos2 = inputFile.tellg();
		if(pos2 > pos)
		{
			return pos2 - pos;
		}
		return 0;
	}
	size_t FileInputStream::getMarkerPosition()
	{
		if (isValid())
		{
			return inputFile.tellg();
		}
		else
		{
			return 0;
		}
//		return isValid()?inputFile.tellg():0;
	}
	size_t FileInputStream::getRemainingByteCount()
	{
		return length - getMarkerPosition();
	}
	size_t FileInputStream::getLength()
	{
		return length;
	}
	void   FileInputStream::rewind()
	{
		if(isValid())
		{
			inputFile.seekg(0,std::ios::beg);
		}
	}
};
