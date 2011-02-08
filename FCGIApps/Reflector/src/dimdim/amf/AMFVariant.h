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
    File Name  : dssAMFVariant.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_VARIANT_H_
#define _DIMDIM_AMF_VARIANT_H_
#include "AMFConstants.h"

namespace dm
{
	namespace amf
	{
		///
		///	the base amf encoded variant class
		///
		class DSSAMFAPI AMFVariant
		{
		public:
			virtual ~AMFVariant(){}
			virtual u8		getType() const= 0;
			virtual void	write(OutputStream* output)=0;
			virtual void    read(InputStream* input)=0;

			bool isNull() const{ return getType() == AMFConstants::AMF_TYPE_NULL; }
			const c8*  getTypeName() const;

			virtual std::ostream& dump(std::ostream& os) const=0;
			std::ostream& dumpAll(std::ostream& os) const
			{
				os<<"("<<getTypeName()<<") ";
				dump(os);
				return os;
			}
			const String toString() const;
		protected:
			AMFVariant(){}
		private:
			DSS_FORCE_BY_REF_ONLY(AMFVariant);
		};
	};
};

template<class T>
inline DSSAMFAPI std::ostream& operator << (std::ostream& os, const dm::amf::AMFVariant& var)
{
	return var.dumpAll(os);
}
#endif
