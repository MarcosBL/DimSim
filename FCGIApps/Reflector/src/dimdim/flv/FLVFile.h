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
    File Name  : dFLVFile.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FLV_FILE_H_
#define _DIMDIM_FLV_FILE_H_
#include "FLVConstants.h"
#include "FLVHeader.h"
#include "FLVTag.h"

namespace dm
{
	/// an exception indicating a bad magic in an flv file
	class DSSFLVAPI FLVErrorBadMagic : public std::exception
	{
	public:
#ifdef WIN32
		const char* what() const{ return "flv file has invalid magic"; }
#else
		const char* what() const throw() {return "flv file has invalid magic";}
#endif
	};
	/// flv error indicating invalid format
	class DSSFLVAPI FLVErrorInvalidFormat : public std::exception
	{
	public:
#ifdef WIN32
		const char* what() const{ return "flv file has invalid format"; }
#else
		const char* what() const throw() {return "flv file has invalid format";}
#endif
	};
	/// flv error indicating invalid file
	class DSSFLVAPI FLVErrorInvalidFile : public std::exception
	{
	public:
#ifdef WIN32
		const char* what() const{ return "flv file could not be opened for I/O"; }
#else
		const char* what() const throw() {return "flv file could not be opened for I/O";}
#endif
	};
	///
	///	A Handler / Visitor class for an FLV File
	///
	class DSSFLVAPI FLVReadHandler
	{
	public:
		FLVReadHandler(){}
		virtual ~FLVReadHandler(){}
		virtual void handleReadHeader(FLVHeader* header) = 0;
		virtual void handleReadTag(FLVTag* tag) = 0;
		virtual void handleReadFinish() = 0;
	private:
		DSS_FORCE_BY_REF_ONLY(FLVReadHandler);
	};
	/// an FLV File class
	class DSSFLVAPI FLVFile : public FLVReadHandler
	{
	public:
		FLVFile();
		virtual ~FLVFile();
		void clear();
		void load(const String fileName,FLVReadHandler* handler = 0);
		void load(InputStream* input, FLVReadHandler* handler=0);
		
		virtual void handleReadHeader(FLVHeader* header);
		virtual void handleReadTag(FLVTag* tag);
		virtual void handleReadFinish();
		
		void open(const String fileName);
		void open(InputStream* input);
		FLVTag* getNextTag(FLVTag& tag);
		
	private:
		FileInputStream flvFileInput;
		FLVHeader header;
		DSS_FORCE_BY_REF_ONLY(FLVFile);
	};

	class DSSFLVAPI FLVWriter
	{
	public:
		FLVWriter();
		virtual ~FLVWriter();
		void init(const char* fileName,bool video=true, bool audio=false);
		void close();
		void writeTag(FLVTag& tag);
	private:
		u32 timeStamp;
		TimeStampGenerator tsGen;
		FileOutputStream outFile;
		FLVHeader header;
	};
};

#endif
