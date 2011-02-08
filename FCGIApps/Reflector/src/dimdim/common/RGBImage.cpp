#include "RGBImage.h"
#include "Helper.h"
namespace dm
{

	RGBImage::RGBImage() : data(0), width(0), height(0)
	{
	}
	RGBImage::RGBImage(u32 w, u32 h) : data(0), width(0), height(0)
	{
		create(w,h);
	}
	RGBImage::~RGBImage()
	{
		destroy();
	}
	
	void RGBImage::clear(u8 val)
	{
		if(isValid())
		{
			memset(data,(s32)val,getDataSize());
		}
	}
	void RGBImage::create(u32 w, u32 h)
	{
		destroy();
		u32 newSize = w * h * 3;
		if(newSize > 0)
		{
			width = w;
			height = h;
			data = new u8[newSize];
		}
		clear();

	}
	void RGBImage::destroy()
	{
		if(data)
		{
			delete[] data;
			data = 0;
		}
		width = 0;
		height = 0;
	}
	
	bool RGBImage::isValid() const
	{
		return data != 0 && getDataSize() != 0;
	}

	void RGBImage::set(u32 x, u32 y, u32 w, u32 h, const u8* buffer)
	{
		if(isValid())
		{
			u32 subWidth = w * 3;
			u32 fullWidth = width * 3;

			u32 xOffsetFull = x;
			u32 xOffsetSub  = 0;
			for(u32 yOffsetSub = 0; yOffsetSub < h ; yOffsetSub++)
			{
				u32 yOffsetFull = y + yOffsetSub;
				u32 fullIndex = yOffsetFull * fullWidth + xOffsetFull * 3;
				u32 subIndex = yOffsetSub * subWidth + xOffsetSub * 3;
				memcpy(data + fullIndex, buffer + subIndex, subWidth);
			}
		}
	}
	void RGBImage::set(u32 x, u32 y, const RGBImage* subImage)
	{
		set(x,y,subImage->getWidth(),subImage->getHeight(),subImage->getData());
	}

	void RGBImage::set(u32 x, u32 y, const RGBImage* subImage, RGBPixel colorKey)
	{
	//	set(x,y,subImage);
		
		if(isValid() && subImage)
		{
			const u8* buffer = subImage->data;
			u32 w = subImage->getWidth();
			u32 h = subImage->getHeight();

			u32 subWidth = w * 3;
			u32 fullWidth = width * 3;

			u32 xOffsetFull = x;
			u32 xOffsetSub  = 0;
			for(u32 yOffsetSub = 0; yOffsetSub < h ; yOffsetSub++)
			{
				u32 yOffsetFull = y + yOffsetSub;
				u32 fullIndex = yOffsetFull * fullWidth + xOffsetFull * 3;
				u32 subIndex = yOffsetSub * subWidth + xOffsetSub * 3;
				//memcpy(data + fullIndex, buffer + subIndex, subWidth);
				u8* dst = data + fullIndex;
				const u8* src = buffer + subIndex;
				for(u32 i = 0; i < subWidth; i++)
				{
				//	std::cerr<<"pixel "<<i<<" - 0x"<<std::hex<<(u32)src[i]<<std::dec<<std::endl;
					if(src[i] != 0xFF)
					{
						dst[i] = src[i];
					}
				}
		
			}
		}
	}
	
	const RGBPixel* RGBImage::getPixel(u32 x, u32 y) const
	{
		static RGBPixel pixel;
		pixel.r = pixel.g = pixel.b = 0;
		if(isValid() && x >=0 && x < getWidth() && y >=0 && y < getHeight())
		{
			const RGBPixel* pixels = (const RGBPixel *)data;
			return	&pixels[y * getWidth() + x];
		}
		return &pixel;
	}
	void RGBImage::setPixel(u32 x, u32 y, const RGBPixel* pixel)
	{
	
		if(isValid() && pixel && x >=0 && x < getWidth() && y >=0 && y < getHeight())
		{
			RGBPixel* pixels = (RGBPixel *)data;
			memcpy(&pixels[y * getWidth() + x], pixel, sizeof(RGBPixel));
		}
		
	}
	void RGBImage::loadRandom()
	{
		size_t imgSize = getDataSize();
		u8 randomByte = (u8)(rand() % 256);
		memset(data,(s32)randomByte,imgSize);
	}
	void RGBImage::get(u32 x, u32 y, u32 w, u32 h, u8* buffer) const
	{
		if(isValid())
		{
			u32 subWidth = w * 3;
			u32 fullWidth = width * 3;

			u32 xOffsetFull = x;
			u32 xOffsetSub  = 0;
			for(u32 yOffsetSub = 0; yOffsetSub < h ; yOffsetSub++)
			{
				u32 yOffsetFull = y + yOffsetSub;
				u32 fullIndex = yOffsetFull * fullWidth + xOffsetFull * 3;
				u32 subIndex = yOffsetSub * subWidth + xOffsetSub * 3;
				memcpy(buffer + subIndex, data + fullIndex, subWidth);
			}
		}
		
	}
	void RGBImage::get(u32 x, u32 y, RGBImage* subImage) const
	{
		get(x,y,subImage->getWidth(),subImage->getHeight(),subImage->getData());
	}


	void RGBImage::setBGR24Transpose(const u8* rgb24)
	{
		RGBPixel* pixels = (RGBPixel*)data;
		RGBPixel* input = (RGBPixel*)rgb24;
		
		//std::cout<<"width = "<<width<<" X height = "<<height<<std::endl;
		for(u32 y = 0; y < height; y++)
		{
			for(u32 x = 0; x < width; x++)
			{
				size_t index = y * width + x;
				size_t index2 = (height -1 - y) * width + x;
				//std::cout<<"doing index "<<index<<"/("<<(width * height)<<" = "<<width<<"X"<<height<<")"<<std::endl;
				
				pixels[index2].b = input[index].r;
				pixels[index2].g = input[index].g;
				pixels[index2].r = input[index].b;
			}
		}
		//std::cout<<"setBGR24Transpose done!"<<std::endl;
	}
	void RGBImage::setRGB32(const u32* buf)
	{
		RGBPixel* pixels = (RGBPixel*)data;
		//std::cout<<"width = "<<width<<" X height = "<<height<<std::endl;
		for(u32 y = 0; y < height; y++)
		{
			for(u32 x = 0; x < width; x++)
			{
				size_t index = y * width + x;
				size_t index2 = (height -1 - y) * width + x;
				//std::cout<<"doing index "<<index<<"/("<<(width * height)<<" = "<<width<<"X"<<height<<")"<<std::endl;
				u32 pix = buf[index];
				pixels[index2].b = (u8)((pix >> 16) & 0xFF);
				pixels[index2].g = (u8)((pix >> 8) & 0xFF);
				pixels[index2].r = (u8)(pix & 0xFF);
			}
		}
		//std::cout<<"setRGB32 done!"<<std::endl;
	}
	bool RGBImage::compareSubImage(u32 x, u32 y, u32 w, u32 h, const u8* buffer) const
	{
		
		if(!isValid())
		{
			return false;
		}
		u32 subWidth = w * 3;
		u32 fullWidth = width * 3;

		u32 xOffsetFull = x;
		u32 xOffsetSub  = 0;
		for(u32 yOffsetSub = 0; yOffsetSub < h ; yOffsetSub++)
		{
			u32 yOffsetFull = y + yOffsetSub;
			u32 fullIndex = yOffsetFull * fullWidth + xOffsetFull * 3;
			u32 subIndex = yOffsetSub * subWidth + xOffsetSub * 3;
			int result = memcmp(buffer + subIndex, data + fullIndex, subWidth);
			if( result != 0)
			{
				return true;
			}
		}
		return false;
	}
	bool RGBImage::compareSubImage(u32 x, u32 y, const RGBImage* subImage) const
	{
		return compareSubImage(x,y,subImage->getWidth(),subImage->getHeight(),subImage->getData());
	}

	void RGBImage::scale(RGBImage* scaledImage)
	{
		scale(scaledImage->getWidth(),scaledImage->getHeight(),scaledImage->getData());
	}
	void RGBImage::scale(u32 w, u32 h, u8* buffer)
	{
		scale(w,h,(RGBPixel*)buffer);
	}
	void RGBImage::scale(u32 w,u32 h,RGBPixel* rgbBuffer)
	{
		if(w == width && h == height)
		{
			memcpy(rgbBuffer,data,getDataSize());
			return;
		}

		RGBPixel* srcBuffer = (RGBPixel*)data;
		f32 wRatio = (f32)width / (f32)w;
		f32 hRatio = (f32)height / (f32)h;
		for(u32 x = 0; x < w; x++)
		{
			u32 xIndex = (u32)((f32)x * wRatio);
			for(u32 y = 0; y < h; y++)
			{
				u32 scaleIndex = x + y * w;
				
				u32 yIndex = (u32)((f32)y * hRatio);

				u32 avgRed = 0;
				u32 avgGreen = 0;
				u32 avgBlue = 0;
				u32 sampleCount = 0;
				{

					{
						u32 index = xIndex + yIndex * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed +=rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
					}
					if(yIndex > 0)
					{
						
						u32 index = (xIndex) + (yIndex-1) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
						
					}
					if(yIndex < width-1)
					{
						
						u32 index = (xIndex) + (yIndex+1) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
						
					}
				}
				if(xIndex > 0)
				{
					
					{
						u32 index = (xIndex-1) + (yIndex) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
					}

					
					if(yIndex > 0)
					{
						
						u32 index = (xIndex-1) + (yIndex-1) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
						
					}
					if(yIndex < width-1)
					{
						
						u32 index = (xIndex - 1) + (yIndex+1) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
						
					}
					
				}
				if(xIndex < width-1)
				{
					
					{
						u32 index = (xIndex + 1) + (yIndex) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
					}
					if(yIndex > 0)
					{
						
						u32 index = (xIndex+1) + (yIndex-1) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
						
					}
					if(yIndex < width-1)
					{
						
						u32 index = (xIndex + 1) + (yIndex+1) * width;
						RGBPixel& rgb = srcBuffer[index];
						avgRed += rgb.r;
						avgGreen += rgb.g;
						avgBlue += rgb.b;
						sampleCount++;
						
					}
					
				}

				rgbBuffer[scaleIndex].r = (u8)(avgRed / sampleCount);
				rgbBuffer[scaleIndex].g = (u8)(avgGreen / sampleCount);
				rgbBuffer[scaleIndex].b = (u8)(avgBlue / sampleCount);

			}
		}
	}
	
		void RGBImage::grayscale()
		{
			RGBPixel* rgbData = (RGBPixel*)data;
			for(u32 x = 0; x < width; x++)
			{
				for(u32 y = 0; y < height; y++)
				{
					RGBPixel& rgb = rgbData[x + y * width];
					
					rgb.r = get256ColorValue(rgb.r);
					rgb.g = get256ColorValue(rgb.g);
					rgb.b = get256ColorValue(rgb.b);

					u16 total = rgb.r + rgb.g + rgb.b;
					u8 v = (u8)(total / 3);
					v = get256ColorValue(v);
					rgb.r = rgb.g = rgb.b = v;

				}
			}
		}
		void RGBImage::make256Color()
		{
			RGBPixel* rgbData = (RGBPixel*)data;
			for(u32 x = 0; x < width; x++)
			{
				for(u32 y = 0; y < height; y++)
				{
					RGBPixel& rgb = rgbData[x + y * width];
					
					
					rgb.r = get256ColorValue(rgb.r);
					rgb.g = get256ColorValue(rgb.g);
					rgb.b = get256ColorValue(rgb.b);


				}
			}
		}
		u8 RGBImage::get256ColorValue(u8 color)
		{
			u8 diff = 32;
			u16 minVal = 0;
			u16 maxVal = minVal + diff;
			while(maxVal <= 256)
			{
				if(color == minVal)
				{
					return (u8)minVal;
				}
				else if(color > minVal && color < maxVal)
				{
					return maxVal - 1;
				}
				else
				{
					minVal = maxVal;
					maxVal += 32;
				}
			}
			return color;
		}

};


