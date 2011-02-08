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
    File Name  : dssAMFObject.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_OBJECT_H_
#define _DIMDIM_AMF_OBJECT_H_
#include "AMFTable.h"

namespace dm
{
	namespace amf
	{
		///
		///	An AMF Object is just a pair of utf8 names with amf variant values
		///
		class DSSAMFAPI AMFObject : public AMFTable
		{
		public:
			AMFObject();
			virtual ~AMFObject();

			virtual u8 getType() const;
			virtual void write(OutputStream* output);
			virtual void read(InputStream* input);
			std::ostream& dump(std::ostream& os) const{ return AMFTable::dump(os); }
		private:
			DSS_FORCE_BY_REF_ONLY(AMFObject);
		};
	};
};
#endif
