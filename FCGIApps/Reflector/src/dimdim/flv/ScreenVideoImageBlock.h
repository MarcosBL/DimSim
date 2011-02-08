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
    File Name  : dScreenVideoImageBlock.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:58 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FLV_SV_IMG_BLOCK_H_
#define _DIMDIM_FLV_SV_IMG_BLOCK_H_
#include "FLVConstants.h"
//#include "cassert.h"
namespace dm
{
	///
	///	This represents a rectangular image block in a screen video
	///	frame.
	///
	class DSSFLVAPI ScreenVideoImageBlock
	{
	public:
		ScreenVideoImageBlock();
		virtual ~ScreenVideoImageBlock();

		void clear(u8 val=100);

		void load(InputStream* input);
		void save(OutputStream* output);

		void makeEmptyBlock();
		void makeNonEmptyBlock();

		void init(u16 w, u16 h);
		void destroy();
		bool isValid() const;


		void setCompressedData(const u8* compressedData, size_t compressedLength);
		size_t getCompressedData(u8* compressedData, size_t maxCompressedLength);
		bool   updatePixels(const u8* pixelData, size_t pixelDataSize);

		bool isEmptyBlock() const;

		u8* getPixels();
		const u8* getPixels() const;

		void loadRandom();

		u16 getWidth() const;
		u16 getHeight() const;

		size_t getDataSizeUncompressed() const;
		size_t getCurrentLength() const{ return length; }
	private:
		size_t length;
		u16	   width,height;
		u8*	   pixels;

		DSS_FORCE_BY_REF_ONLY(ScreenVideoImageBlock);

	};
};
#endif
