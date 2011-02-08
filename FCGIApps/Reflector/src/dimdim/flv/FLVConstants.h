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
    File Name  : dFLVConstants.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FLV_CONSTANTS_H_
#define _DIMDIM_FLV_CONSTANTS_H_
#include "../common/api.h"
#include "../amf/api.h"
#include "../common/RGBImage.h"

#define DSSFLVAPI
namespace dm
{
	class DSSFLVAPI FLVConstants
	{
	public:
		u8* FLV_MAGIC;
		u8  FLV_TAG_TYPE_AUDIO;
		u8  FLV_TAG_TYPE_VIDEO;
		u8  FLV_TAG_TYPE_METADATA;
		u8  FLV_VIDEO_KEYFRAME;
		u8  FLV_VIDEO_INTERFRAME;
		u8  FLV_VIDEO_CODEC_SCREEN_V1;

		static FLVConstants* getInstance()
		{
			static FLVConstants* _instance;
			if (NULL == _instance)
				_instance = new FLVConstants();
			return _instance;
		}

		static void removeInstance()
		{
			static FLVConstants* _instance;
			if (NULL != _instance)
				delete _instance;
			_instance = 0;
		}

	private:
		FLVConstants();
	};
};
#endif
