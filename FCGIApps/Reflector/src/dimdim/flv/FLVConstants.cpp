//$$<!--TOFUTAG-->$$//
 /************************************************************************** 	 
  *                                                                        *
  *               DDDDD   iii             DDDDD   iii                      * 
  *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           * 
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
  *                                                                        *
  **************************************************************************
/* *************************************************************************	
	THIS FILE IS PART OF THE DIMDIM CODEBASE. TO VIEW LICENSE AND EULA
	FOR THIS CODE VISIT http://www.dimdim.com
   ************************************************************************ */

/* ------------------------------------------------------ 
    File Name  : dFLVConstants.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "FLVConstants.h"

namespace dm
{
	FLVConstants::FLVConstants()
	{
		FLV_MAGIC					= (u8*)"FLV";
		FLV_TAG_TYPE_AUDIO			= 0x08;
		FLV_TAG_TYPE_VIDEO			= 0x09;
		FLV_TAG_TYPE_METADATA		= 0x12;
		FLV_VIDEO_KEYFRAME			= 1;
		FLV_VIDEO_INTERFRAME		= 2;
		FLV_VIDEO_CODEC_SCREEN_V1	= 3;
	}
};
