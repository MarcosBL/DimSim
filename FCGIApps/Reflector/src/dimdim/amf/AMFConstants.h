#ifndef _dss_AMF_constants_h_
#define _dss_AMF_constants_h_

#include "../common/api.h"

namespace dm
{
	namespace amf
	{
		class DSSAMFAPI AMFConstants
		{
		public: 
			static const u8 AMF_TYPE_DOUBLE			= 0x00;//0x00
			static const u8 AMF_TYPE_BOOL			= 0x01;//0x01
			static const u8 AMF_TYPE_UTF8			= 0x02;//0x02
			static const u8 AMF_TYPE_OBJECT			= 0x03;//0x03
			static const u8 AMF_TYPE_MOVIECLIP		= 0x04;//0x04
			static const u8 AMF_TYPE_NULL			= 0x05;//0x05
			static const u8 AMF_TYPE_UNDEFINED		= 0x06;//0x06
			static const u8 AMF_TYPE_REFERENCE		= 0x07;//0x07
			static const u8 AMF_TYPE_MIXEDARRAY		= 0x08;//0x08
			static const u8 AMF_TYPE_ENDOFOBJECT	= 0x09;//0x09
			static const u8 AMF_TYPE_ARRAY			= 0x0A;//0x0A
			static const u8 AMF_TYPE_DATE			= 0x0B;//0x0B
			static const u8 AMF_TYPE_LONGUTF8		= 0x0C;//0x0C
			static const u8 AMF_TYPE_UNSUPPORTED	= 0x0D;//0x0D
			static const u8 AMF_TYPE_RECORDSET		= 0x0E;//0x0E
			static const u8 AMF_TYPE_TYPEDOBJECT	= 0x10;//0x10
		};
	};
};
#endif
