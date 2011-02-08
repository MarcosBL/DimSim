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
    File Name  : dScreenVideoEncoder.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "ScreenVideoEncoder.h"

namespace dm
{
	void ScreenVideoEncoder::makeNewFrame(ScreenVideoFrame* theFrame, u16 width, u16 height, u8 blockWidth, u8 blockHeight)
	{
		theFrame->clear();
		theFrame->init(true,width,height,blockWidth,blockHeight);
	}
	bool ScreenVideoEncoder::encodeFrame(ScreenVideoFrame* theFrame, RGBImage* theImage,bool keyFrame, bool& emptyTag)
	{
		if(keyFrame)
		{
			theFrame->makeKeyFrame();
		}
		else
		{
			theFrame->makeInterFrame();
		}

		RGBImage subImage;
		u16 blockWidthFull = 0;
		u16 blockHeightFull = 0;
		u16 blockWidth = 0;
		u16 blockHeight = 0;
		//initialize the basic block width
		theFrame->getBlockDimensions(0,0,blockWidthFull,blockHeightFull);
		bool isKeyFrame = false;
		u32 cellCount = theFrame->getRowCount() * theFrame->getColumnCount();
		u32 changeCount = 0;
		for(u16 row = 0; row < theFrame->getRowCount(); row++)
		{
			for(u16 col = 0; col < theFrame->getColumnCount(); col++)
			{
				theFrame->getBlockDimensions(row,col,blockWidth,blockHeight);
				

				u32 blockX = row * blockWidthFull;
				u32 blockY = col * blockHeightFull;

				ScreenVideoImageBlock* imgBlock = theFrame->getImageBlock(row,col);
				bool changed = theImage->compareSubImage(blockX,blockY,blockWidth,blockHeight,imgBlock->getPixels());
				

				if(changed)
				{
					//std::cout<<"Updating Region data for ("<<row<<","<<col<<")!!!"<<std::endl;
					theImage->get(blockX,blockY,blockWidth,blockHeight,imgBlock->getPixels());
					imgBlock->makeNonEmptyBlock();
					changeCount++;
				}
				else
				{
					//std::cout<<"No Change For Region  ("<<row<<","<<col<<")!!!"<<std::endl;
					imgBlock->makeEmptyBlock();
					isKeyFrame = false;
				}

			}
		}
		isKeyFrame = (cellCount == changeCount);
		emptyTag = (changeCount == 0);
//		AuditLog::instance()->logCellData(changeCount,cellCount);
		return keyFrame? true:isKeyFrame;
	
	}
	void ScreenVideoEncoder::writeTag(FLVTag* tag, ScreenVideoFrame* frame,u32 timeStamp)
	{
		//reset the tag
		tag->clear();

		//make the tag a video tag
		tag->makeVideoTag();

		//set the timestamp
		tag->setTimeStamp(timeStamp);

		//dump the frame onto the tag data buffer
		ByteBuffer& tagData = tag->getTagData();
		ByteBufferOutputStream output(&tagData,false);
		frame->save(&output);

		//recalculate data size
		tag->calculateDataSize();
		//std::cout<<"Tag Data Size : "<<tag->getDataSize()<<" bytes!"<<std::endl;
			
		
	}
};
