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
    File Name  : dssAMFObject.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFObject.h"

namespace dm
{
	namespace amf
	{
		AMFObject::AMFObject()
		{
		}
		AMFObject::~AMFObject()
		{
		}
		u8 AMFObject::getType() const
		{
			return AMFConstants::AMF_TYPE_OBJECT;
		}
		void AMFObject::write(OutputStream* output)
		{
			AMFTable::write(output);
		}
		void AMFObject::read(InputStream* input)
		{
			AMFTable::read(input);
		}
	};
};
