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
    File Name  : dScreenVideoImageBlock.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "ScreenVideoImageBlock.h"
namespace dm
{
	ScreenVideoImageBlock::ScreenVideoImageBlock() : length(0), width(0), height(0), pixels(0)
	{
	}


	ScreenVideoImageBlock::~ScreenVideoImageBlock()
	{
		destroy();
	}

	void ScreenVideoImageBlock::clear(u8 val)
	{
		if(isValid())
		{
			memset(pixels,(s32)val,getDataSizeUncompressed());
		}
	}

	void ScreenVideoImageBlock::load(InputStream* input)
	{
		clear();
		makeEmptyBlock();

		u16 compressedLength = 0;
		input->readShort(&compressedLength);

		if(compressedLength > 0)
		{
			u8* compressedData = new u8[compressedLength];
			ScopedArray<u8> cdataPtr(compressedData);
			input->read(compressedData,compressedLength);
			setCompressedData(compressedData,compressedLength);
			
		}

	}
	void ScreenVideoImageBlock::save(OutputStream* output)
	{
		
		if(isEmptyBlock())
		{
			output->writeShort(0);
		}
		else
		{
			size_t maxCompressedLength = Compressor::estimateCompressedSize(length);
			u8* compressedData = new u8[maxCompressedLength];
			ScopedArray<u8> cdataPtr(compressedData);
			
			u16 compressedLength =  (u16)getCompressedData(compressedData,maxCompressedLength);
			
			output->writeShort(compressedLength);
			output->write(compressedData,compressedLength);
		}
	}

	void ScreenVideoImageBlock::makeEmptyBlock()
	{
		length = 0;
	}
	void ScreenVideoImageBlock::makeNonEmptyBlock()
	{
		length = width * height * 3;
	}
	void ScreenVideoImageBlock::init(u16 w, u16 h)
	{
		destroy();
		if(w * h > 0)
		{
			width = w;
			height = h;
			length = w * h * 3;
			pixels = new u8[getDataSizeUncompressed()];
			clear();
		}
	}
	void ScreenVideoImageBlock::destroy()
	{
		DSS_SAFE_DELETE_ARRAY(pixels);
		width = 0;
		height = 0;
		length = 0;
	}
	bool ScreenVideoImageBlock::isValid() const
	{
		return pixels > 0 && getDataSizeUncompressed() > 0;
	}
	void ScreenVideoImageBlock::setCompressedData(const u8* compressedData, size_t compressedLength)
	{
		if(isValid())
		{
			size_t decompressedLength = Compressor::decompress(compressedData,compressedLength,pixels,getDataSizeUncompressed());
			length = decompressedLength;
//			assert(decompressedLength == getDataSizeUncompressed());
		}
	}
	size_t ScreenVideoImageBlock::getCompressedData(u8* compressedData, size_t maxCompressedLength)
	{
		if(isValid())
		{
			return Compressor::compress(pixels,getDataSizeUncompressed(),compressedData,maxCompressedLength);
		}
		return 0;
	}
	bool  ScreenVideoImageBlock::updatePixels(const u8* pixelData, size_t pixelDataSize)
	{
		if(isValid() && pixelDataSize <= getDataSizeUncompressed())
		{
			memcpy(pixels,pixelData,pixelDataSize);
			length = getDataSizeUncompressed();
			return true;
		}
		return false;
	}
	bool ScreenVideoImageBlock::isEmptyBlock() const
	{
		return length == 0;
	}

	u8* ScreenVideoImageBlock::getPixels()
	{
		return pixels;
	}
	const u8* ScreenVideoImageBlock::getPixels() const
	{
		return pixels;
	}

	void ScreenVideoImageBlock::loadRandom()
	{
		u8 randomByte = (u8) (rand() % 256);
		clear(randomByte);
	}

	u16 ScreenVideoImageBlock::getWidth() const
	{
		return width;
	}
	u16 ScreenVideoImageBlock::getHeight() const
	{
		return height;
	}

	size_t ScreenVideoImageBlock::getDataSizeUncompressed() const
	{
		return width * height * 3;
	}
};
