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
    File Name  : dssAMFBool.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFBool.h"

namespace dm
{
	namespace amf
	{
		AMFBool::AMFBool() : value(false)
		{
		}
		AMFBool::~AMFBool()
		{
		}
		u8 AMFBool::getType() const
		{
			return AMFConstants::AMF_TYPE_BOOL;
		}
		void AMFBool::write(OutputStream* output)
		{
			output->writeByte(value?1:0);
		}
		void AMFBool::read(InputStream* input)
		{
			u8 bv = 0;
			input->readByte(&bv);
			setValue(bv > 0);
		}
	};
};
