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
    File Name  : dssAMFStringLongUTF8.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_STRING_LONG_UTF8_H_
#define _DIMDIM_AMF_STRING_LONG_UTF8_H_
#include "AMFStringUTF8.h"
namespace dm
{
	namespace amf
	{
		///
		///	A String with a 32 bit length
		///
		class DSSAMFAPI AMFStringLongUTF8 : public AMFStringUTF8
		{
		public:
			AMFStringLongUTF8(const String val="");
			virtual ~AMFStringLongUTF8();

			u8 getType() const;
			void write(OutputStream* output);
			void read(InputStream* input);
		private:
			DSS_FORCE_BY_REF_ONLY(AMFStringLongUTF8);
		};
	};
};
#endif
