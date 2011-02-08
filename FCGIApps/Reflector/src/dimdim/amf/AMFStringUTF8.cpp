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
    File Name  : dssAMFStringUTF8.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFStringUTF8.h"

namespace dm
{

	namespace amf
	{
		AMFStringUTF8::AMFStringUTF8(const String val) : value(val)
		{
		}
		AMFStringUTF8::~AMFStringUTF8()
		{
		}

		u8 AMFStringUTF8::getType() const
		{
			return AMFConstants::AMF_TYPE_UTF8;
		}
		void AMFStringUTF8::write(OutputStream* output)
		{
			output->writeShort((u16)value.size());
			output->writeStringExact(value);
		}
		void AMFStringUTF8::read(InputStream* input)
		{
			u16 len = 0;
			input->readShort(&len);
			input->readStringExact(value,(size_t)len);
		}
	};
}
