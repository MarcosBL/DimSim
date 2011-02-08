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
    File Name  : dScreenVideoFrame.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "ScreenVideoFrame.h"

namespace dm
{
	
	ScreenVideoFrame::ScreenVideoFrame() :   videoHeader(0), 
						imageBlocks(0),
						imageWidth(0),
						imageHeight(0),
						blockWidth(0),
						blockHeight(0),
						blockHeightCount(0),
						blockWidthCount(0),
						blockCountX(0),
						blockCountY(0),
						blockCountTotal(0)


	{
	}
	ScreenVideoFrame::~ScreenVideoFrame()
	{
		clear();
	}

	void ScreenVideoFrame::clear()
	{
		deleteImageBlocks();
		imageWidth = imageHeight = 0;
		blockWidth = blockHeight = 0;
		blockWidthCount = blockHeightCount = 0;
		blockCountX = blockCountY = blockCountTotal = 0;
	}
	void ScreenVideoFrame::allocateImageBlocks(u32 totalBlocks)
	{
		deleteImageBlocks();
		imageBlocks = new ScreenVideoImageBlock*[totalBlocks];
		blockCountTotal = totalBlocks;
		for(u16 row = 0; row < blockCountX; row++)
		{
			for(u16 col = 0; col < blockCountY; col++)
			{
				u32 index = row + col * blockCountX;
				u16 w=0;
				u16 h=0;
				
				getBlockDimensions(row,col,w,h);
				imageBlocks[index] = new ScreenVideoImageBlock();
				imageBlocks[index]->init(w,h);
			}
		}
	}
	void ScreenVideoFrame::deleteImageBlocks()
	{
		if(imageBlocks)
		{
			for(size_t s = 0; s < blockCountTotal; s++)
			{
				imageBlocks[s]->clear();
				delete imageBlocks[s];
				imageBlocks[s] = 0;
			}
			delete[] imageBlocks;
		}
		blockCountTotal = 0;
	}

	ScreenVideoImageBlock* ScreenVideoFrame::getImageBlock(u16 row, u16 col)
	{
//		assert(row < blockCountX && col < blockCountY);
		u32 index = row + col * blockCountX;
		return imageBlocks[index];
	}

	void ScreenVideoFrame::getBlockDimensions(u16 row, u16 col, u16& width, u16& height)
	{
//		assert(row < blockCountX && col < blockCountY);
		width = blockWidth;
		if(row == blockCountX - 1)
		{
			width = imageWidth - (blockCountX - 1)*blockWidth;
		}
		height = blockHeight;
		if(col == blockCountY - 1)
		{
			height = imageHeight - (blockCountY - 1)*blockHeight;

		}

	}
	void   ScreenVideoFrame::makeKeyFrame()
	{
		videoHeader = 0x13;
	}
	void   ScreenVideoFrame::makeInterFrame()
	{
		videoHeader = 0x23;
	}
	void ScreenVideoFrame::load(InputStream* input)
	{
		clear();
		input->readByte(&videoHeader);
		
		u16 widthInfo = 0;
		u16 heightInfo = 0;
		
		input->readShort(&widthInfo);
		input->readShort(&heightInfo);

		blockWidthCount = (widthInfo & 0xF000) >> 12;
		blockWidth = (u8)((blockWidthCount+1)*16);
		imageWidth = (widthInfo &0x0FFF);

		blockHeightCount = (heightInfo & 0xF000) >> 12;
		blockHeight = (u8)((blockHeightCount+1)*16);
		imageHeight = (heightInfo &0x0FFF);


		//initialize the frame
		init(isKeyFrame()?true:false,imageWidth,imageHeight,(u8)blockWidth,(u8)blockHeight);

		for(u32 blockIndex = 0; blockIndex < blockCountTotal; blockIndex++)
		{
			ScreenVideoImageBlock* imageBlock = imageBlocks[blockIndex];
			imageBlock->load(input);
		}


	}
	void ScreenVideoFrame::save(OutputStream* output)
	{
		output->writeByte(videoHeader);
		u16 widthInfo = 0;
		u16 heightInfo = 0;

		widthInfo |= (imageWidth & 0x0FFF);
		widthInfo |= (((u16)blockWidthCount) << 12);
		output->writeShort(widthInfo);
		heightInfo |= (imageHeight & 0x0FFF);
		heightInfo |= (((u16)blockHeightCount) << 12);
		output->writeShort(heightInfo);

		for(u32 blockIndex = 0; blockIndex < blockCountTotal; blockIndex++)
		{
		
			ScreenVideoImageBlock* imageBlock = imageBlocks[blockIndex];
			imageBlock->save(output);
		}

	}

	void ScreenVideoFrame::init(bool keyFrame, u16 imgWidth, u16 imgHeight, u8 blockW, u8 blockH)
	{
		if(keyFrame)
		{
			makeKeyFrame();
		}
		else
		{
			makeInterFrame();
		}

		imageWidth = imgWidth;
		imageHeight = imgHeight;

		if(blockW == 0)
		{
			blockW = 64;
		}
		if(blockH == 0)
		{
			blockH = 64;
		}
		blockWidthCount =  (blockW / 16) - 1;
		blockHeightCount = (blockH / 16) - 1;

		blockWidth = (blockWidthCount + 1) * 16;
		blockHeight = (blockHeightCount + 1) * 16;

		blockCountX = imageWidth / blockWidth;
		blockCountY = imageHeight / blockHeight;

		if(imageWidth % blockWidth > 0)
		{
			blockCountX++;
		}
		if(imageHeight % blockHeight > 0)
		{
			blockCountY++;
		}

		blockCountTotal = blockCountX * blockCountY;
		allocateImageBlocks(blockCountTotal);

	}
	bool ScreenVideoFrame::isKeyFrame() const
	{
		return ((videoHeader & 0xF0) >> 4) == 1;
	}
	void   ScreenVideoFrame::prepareFrame()
	{
		for(u32 blockIndex = 0; blockIndex < blockCountTotal; blockIndex++)
		{
			ScreenVideoImageBlock* block = imageBlocks[blockIndex];
			block->loadRandom();
		}
	}
	void ScreenVideoFrame::dumpInfo(std::ostream& os)
	{
		u32 emptyCount = 0;
		u32 changedCount = 0;
		for(u32 blockIndex = 0; blockIndex < blockCountTotal; blockIndex++)
		{
			if(imageBlocks[blockIndex]->isEmptyBlock())
			{
				emptyCount++;
			}
			else
			{
				changedCount++;
			}
		}

		os<<"	!------------- Begin Screen Video Frame ------------------!"<<std::endl;
		os<<"	!  Image : "<<imageWidth<<" X " <<imageHeight<<std::endl;
		os<<"	!  Block : "<<blockWidth<<" X " <<blockHeight<<std::endl;
		os<<"	!  Block Count : "<<blockCountX<<" X "<<blockCountY<<" = "<<blockCountTotal<<std::endl;
		os<<"   !  Empty Blocks : "<<emptyCount<<" and Changed Blocks : "<<changedCount<<std::endl;
		os<<"	!------------- Finish Screen Video Frame -----------------!"<<std::endl;

	}
};
