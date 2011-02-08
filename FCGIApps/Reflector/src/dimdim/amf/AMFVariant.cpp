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
    File Name  : dssAMFVariant.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFVariant.h"
#include "AMFCodec.h"

namespace dm
{
	namespace amf
	{
		const char* AMFVariant::getTypeName() const
		{
			return AMFCodec::getTypeName(getType());
		}
		/*const String AMFVariant::toString() const
		{
			std::ostrstream os;
			dump(os);
			os<<std::ends;
			return os.str();
		}*/

		const String AMFVariant::toString() const
		{
			std::ostrstream os;
			dump(os);
			os<<std::ends;
			String toStr;
			toStr.append(os.str());
			os.rdbuf()->freeze(0);
			return toStr;
		}

	};
};
