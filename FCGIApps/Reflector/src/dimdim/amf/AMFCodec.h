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
    File Name  : dssAMFCodec.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_CODEC_H_
#define _DIMDIM_AMF_CODEC_H_
#include "AMFConstants.h"
#include "AMFVariant.h"

namespace dm
{
	namespace amf
	{
		class AMFVariant;
		///
		///	A Codec to encode and decode amf types into raw data
		///
		class DSSAMFAPI AMFCodec
		{
		public:
			static AMFVariant* decode(InputStream* input);
			static void encode(OutputStream* output, AMFVariant* var);
			static AMFVariant* newVariant(u8 amfType);
			static const  char* getTypeName(u8 amfType);
			static double parseDouble(const u8* buf);
		private:
			DSS_FORCE_BY_REF_ONLY(AMFCodec);
		};
	};
};
#endif
