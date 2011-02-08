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
    File Name  : dScreenVideoEncoder.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FLV_SCREEN_VIDEO_ENCODER_H_
#define _DIMDIM_FLV_SCREEN_VIDEO_ENCODER_H_
#include "FLVConstants.h"
#include "ScreenVideoFrame.h"
#include "ScreenVideoImageBlock.h"
#include "FLVTag.h"
//#include "cassert.h"

namespace dm
{
	class DSSFLVAPI ScreenVideoEncoder
	{
	public:
		static void makeNewFrame(ScreenVideoFrame* theFrame, u16 width, u16 height, u8 blockWidth=128, u8 blockHeight=128);
		static bool encodeFrame(ScreenVideoFrame* theFrame, RGBImage* theImage,bool keyFrame,bool& emptyTag);	
		static void writeTag(FLVTag* tag, ScreenVideoFrame* frame, u32 timeStamp);

	};
};

#endif
