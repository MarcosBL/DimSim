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
    File Name  : dssAMFDouble.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_DOUBLE_H_
#define _DIMDIM_AMF_DOUBLE_H_
#include "AMFVariant.h"

namespace dm
{
	namespace amf
	{
		class DSSAMFAPI AMFDouble : public AMFVariant
		{
		public:
			AMFDouble(f64 val=0.0);
			virtual ~AMFDouble();

			u8 getType() const;
			void write(OutputStream* output);
			void read(InputStream* input);
			void setValue(f64 val){ value = val; }
			f64 getValue() const{ return value; }
			void setValue(const u8* octets){ memcpy(&value,octets,8); }
			std::ostream& dump(std::ostream& os) const;
		private:
			f64 value;
			DSS_FORCE_BY_REF_ONLY(AMFDouble);
		};
	};
};
#endif
