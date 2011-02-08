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
    File Name  : dssAMFStringLongUTF8.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFStringLongUTF8.h"

namespace dm
{
	namespace amf
	{

		AMFStringLongUTF8::AMFStringLongUTF8(const String val) : AMFStringUTF8(val)
		{
		}
		AMFStringLongUTF8::~AMFStringLongUTF8()
		{
		}

		u8 AMFStringLongUTF8::getType() const
		{
			return AMFConstants::AMF_TYPE_UTF8;
		}
		void AMFStringLongUTF8::write(OutputStream* output)
		{
			output->writeLong((u32)value.size());
			output->writeStringExact(value);
		}
		void AMFStringLongUTF8::read(InputStream* input)
		{
			u32 len = 0;
			input->readLong(&len);
			input->readStringExact(value,(size_t)len);
		}
	};
}
