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
    File Name  : dssAMFCodec.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFConstants.h"
#include "AMFVariant.h"
#include "AMFNull.h"
#include "AMFBool.h"
#include "AMFDouble.h"
#include "AMFStringUTF8.h"
#include "AMFStringLongUTF8.h"
#include "AMFObject.h"
#include "AMFTypedObject.h"
#include "AMFCodec.h"
#include "AMFMixedArray.h"
namespace dm
{
	namespace amf
	{
		AMFVariant* AMFCodec::newVariant(u8 amfType)
		{
			if(amfType  ==  AMFConstants::AMF_TYPE_DOUBLE)
				return new AMFDouble();
			else if(amfType  ==  AMFConstants::AMF_TYPE_BOOL)
				return new AMFBool();
			else if(amfType  ==  AMFConstants::AMF_TYPE_UTF8)
				return new AMFStringUTF8();
			else if(amfType  ==  AMFConstants::AMF_TYPE_OBJECT)
				return new AMFObject();
			else if(amfType  ==  AMFConstants::AMF_TYPE_NULL)
				return new AMFNull();
			else if(amfType  ==  AMFConstants::AMF_TYPE_LONGUTF8)
				return new AMFStringLongUTF8();
			else if(amfType  ==  AMFConstants::AMF_TYPE_TYPEDOBJECT)
				return new AMFTypedObject();
			else if(amfType	 ==	 AMFConstants::AMF_TYPE_MIXEDARRAY)
				return new AMFMixedArray();
			else
				return 0;

		}
		double AMFCodec::parseDouble(const u8* buf)
		{
			AMFDouble dbl;
			
			ByteBufferInputStream bin((u8*)buf,8);
			dbl.read(&bin);
			return dbl.getValue();
		}
		AMFVariant* AMFCodec::decode(InputStream* input)
		{
			u8 amfType = AMFConstants::AMF_TYPE_NULL;
			input->readByte(&amfType);
			AMFVariant* var = newVariant(amfType);
			if(var)
			{
				var->read(input);
			}
			return var;
		}
		void AMFCodec::encode(OutputStream* output, AMFVariant* var)
		{
			if(var)
			{
				output->writeByte(var->getType());
				var->write(output);
			}
		}
		const char* AMFCodec::getTypeName(u8 amfType)
		{
			if(amfType  ==  AMFConstants::AMF_TYPE_DOUBLE)
				return "amf::double";
			else if(amfType  ==  AMFConstants::AMF_TYPE_BOOL)
				return "amf::bool";
			else if(amfType  ==  AMFConstants::AMF_TYPE_UTF8)
				return "amf::utf8";
			else if(amfType  ==  AMFConstants::AMF_TYPE_OBJECT)
				return "amf::object";
			else if(amfType  ==  AMFConstants::AMF_TYPE_NULL)
				return "amf::null";
			else if(amfType  ==  AMFConstants::AMF_TYPE_LONGUTF8)
				return "amf::longutf8";
			else if(amfType  ==  AMFConstants::AMF_TYPE_TYPEDOBJECT)
				return "amd::typedobject";
			else if(amfType  ==  AMFConstants::AMF_TYPE_ARRAY)
				return "amf::array";
			else if(amfType  ==  AMFConstants::AMF_TYPE_MOVIECLIP)
				return "amf::movieclip";
			else if(amfType  ==  AMFConstants::AMF_TYPE_UNDEFINED)
				return "amf::undefined";
			else if(amfType  ==  AMFConstants::AMF_TYPE_REFERENCE)
				return "amf::reference";
			else if(amfType  ==  AMFConstants::AMF_TYPE_MIXEDARRAY)
				return "amf::mixedarray";
			else if(amfType  ==  AMFConstants::AMF_TYPE_ENDOFOBJECT)
				return "amf::endofobject";
			else if(amfType  ==  AMFConstants::AMF_TYPE_DATE)
				return "amf::date";
			else if(amfType  ==  AMFConstants::AMF_TYPE_UNSUPPORTED)
				return "amf::unsupported";
			else if(amfType  ==  AMFConstants::AMF_TYPE_RECORDSET)
				return "amf::recordset";
			else
				return "amf::unknown";

		}
	};
};
