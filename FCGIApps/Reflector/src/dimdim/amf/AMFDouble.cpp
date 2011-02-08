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
    File Name  : dssAMFDouble.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFDouble.h"

namespace dm
{

	namespace amf
	{
		AMFDouble::AMFDouble(f64 val) : value(val)
		{
		}
		AMFDouble::~AMFDouble()
		{
		}

		u8 AMFDouble::getType() const
		{
			return AMFConstants::AMF_TYPE_DOUBLE;
		}
		void AMFDouble::write(OutputStream* output)
		{
			output->writeDouble(value);
		}
		void AMFDouble::read(InputStream* input)
		{
			value = 0;
			input->readDouble(&value);
		}
		std::ostream& AMFDouble::dump(std::ostream& os) const
		{
			char buf[256];
			sprintf(buf,"%f",value);
			return os<<buf;
		}
	};
};
