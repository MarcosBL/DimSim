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
    File Name  : dScreenVideoFrame.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FLV_SV_FRAME_H_
#define _DIMDIM_FLV_SV_FRAME_H_
#include "FLVConstants.h"
#include "ScreenVideoImageBlock.h"
//#include "cassert.h"
namespace dm
{
	///
	///	A Single Screen Video Encoded Frame
	///
	class DSSFLVAPI ScreenVideoFrame
	{
	public:
		ScreenVideoFrame();
		virtual ~ScreenVideoFrame();

		void clear();

		void load(InputStream* input);
		void save(OutputStream* output);

		void init(bool keyFrame, u16 imgWidth, u16 imgHeight, u8 blockW = 64, u8 blockH=64);
		bool isKeyFrame() const;
		void prepareFrame();

		ScreenVideoImageBlock* getImageBlock(u16 row, u16 col);

		void	getBlockDimensions(u16 row, u16 col, u16& width, u16& height);
		void    getImageDimensions(u32& w, u32& h){ w = imageWidth; h = imageHeight; }
		
		void    dumpInfo(std::ostream& os);
		
		void   makeKeyFrame();
		void   makeInterFrame();

		u16 getRowCount() const{ return (u16)blockCountX; }
		u16 getColumnCount() const{ return (u16)blockCountY; }

	private:
		void   allocateImageBlocks(u32 totalBlocks);
		void   deleteImageBlocks();

		
		u8	videoHeader;
		
		u8	blockWidthCount;
		u16  blockWidth;
		u16	imageWidth;

		u8	blockHeightCount;
		u16  blockHeight;
		u16  imageHeight;

		u32	blockCountX;
		u32	blockCountY;
		u32	blockCountTotal;

		ScreenVideoImageBlock** imageBlocks;

		DSS_FORCE_BY_REF_ONLY(ScreenVideoFrame);
	};
};
#endif
