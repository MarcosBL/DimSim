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
    File Name  : dssAMFStringUTF8.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_STRING_UTF8_H_
#define _DIMDIM_AMF_STRING_UTF8_H_
#include "AMFVariant.h"

namespace dm
{
	namespace amf
	{
		///
		///	a string and a 16 bit length value 2^15 max size
		///
		class DSSAMFAPI AMFStringUTF8 : public AMFVariant
		{
		public:
			AMFStringUTF8(const String val="");
			virtual ~AMFStringUTF8();

			virtual u8 getType() const;
			virtual void write(OutputStream* output);
			virtual void read(InputStream* input);
			void setValue(const String val){ value = val; }
			const String& getValue() const{ return value; }
			std::ostream& dump(std::ostream& os) const{ return os<<value; }
		protected:
			String value;
		private:
			DSS_FORCE_BY_REF_ONLY(AMFStringUTF8);
		};
	};
};
#endif
