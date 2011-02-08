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
    File Name  : dssAMFTypedObject.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFTypedObject.h"

namespace dm
{
	namespace amf
	{
		AMFTypedObject::AMFTypedObject()
		{
		}
		AMFTypedObject::~AMFTypedObject()
		{
		}
		u8 AMFTypedObject::getType() const
		{
			return AMFConstants::AMF_TYPE_OBJECT;
		}
		void AMFTypedObject::write(OutputStream* output)
		{
			AMFStringUTF8 type;
			type.setValue(objectType);
			type.write(output);

			AMFObject::write(output);
		}
		void AMFTypedObject::read(InputStream* input)
		{
			AMFStringUTF8 type;
			type.read(input);
			setObjectType(type.getValue());
			AMFObject::read(input);
		}

		std::ostream& AMFTypedObject::dump(std::ostream& os) const
		{
			return AMFObject::dump(os);
		}
	};
};
