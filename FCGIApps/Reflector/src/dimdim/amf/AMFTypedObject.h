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
    File Name  : dssAMFTypedObject.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_TYPED_OBJECT_H_
#define _DIMDIM_AMF_TYPED_OBJECT_H_
#include "AMFObject.h"

namespace dm
{
	namespace amf
	{
		class DSSAMFAPI AMFTypedObject : public AMFObject
		{
		public:
			AMFTypedObject();
			virtual ~AMFTypedObject();

			u8 getType() const;
			void write(OutputStream* output);
			void read(InputStream* input);

			const String& getObjectType() const{ return objectType; }
			void setObjectType(const String type){ objectType = type; }
			std::ostream& dump(std::ostream& os) const;
		private:
			String objectType;
			DSS_FORCE_BY_REF_ONLY(AMFTypedObject);
		};
	};
};
#endif
