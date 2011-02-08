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
    File Name  : dssAMFNull.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_NULL_H_
#define _DIMDIM_AMF_NULL_H_
#include "AMFVariant.h"

namespace dm
{
	namespace amf
	{
		class DSSAMFAPI AMFNull : public AMFVariant
		{
		public:
			AMFNull();
			virtual ~AMFNull();

			u8 getType() const;
			void write(OutputStream* output);
			void read(InputStream* input);
			std::ostream& dump(std::ostream& os) const{ return os; };
		private:
			DSS_FORCE_BY_REF_ONLY(AMFNull);
		};
	};
};
#endif
