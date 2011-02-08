#ifndef _DDSS_COMMON_RGB_IMAGE_H_
#define _DDSS_COMMON_RGB_IMAGE_H_

#include "ByteBuffer.h"

namespace dm
{
	struct RGBPixel
	{
		u8 r,g,b;
	};
	///
	///	A Simple Representation of an RGBImage
	///
	class RGBImage
	{
	public:
		RGBImage();
		RGBImage(u32 w, u32 h);
		virtual ~RGBImage();

		void create(u32 w, u32 h);
		void destroy();

		void clear(u8 val=0);
		
		bool isValid() const;

		u32 getWidth() const{ return width; }
		u32 getHeight() const{ return height; }
		
		u32 getDataSize() const{ return width * height * 3; }

		u8* getData(){ return data; }
		const u8* getData() const{ return data; }

		void set(u32 x, u32 y, u32 w, u32 h, const u8* buffer);
		void set(u32 x, u32 y, const RGBImage* subImage);
		void set(u32 x, u32 y, const RGBImage* subImage, RGBPixel colorKey);		
		void get(u32 x, u32 y, u32 w, u32 h, u8* buffer) const;
		void get(u32 x, u32 y, RGBImage* subImage) const;

		bool compareSubImage(u32 x, u32 y, u32 w, u32 h,const u8* buffer) const;
		bool compareSubImage(u32 x, u32 y, const RGBImage* subImage) const;

		void loadRandom();

		void scale(RGBImage* scaledImage);
		void scale(u32 width, u32 height, u8* buffer);
		void scale(u32 width, u32 height, RGBPixel* rgbBuffer);

		void grayscale();
		void make256Color();

		static u8 get256ColorValue(u8 color);
		static u32 getRectSize(u32 w, u32 h){ return w*h*sizeof(RGBPixel); }
		void setRGB32(const u32* rgb32);
		void setBGR24Transpose(const u8* rgb24);

		const RGBPixel* getPixel(u32 x, u32 y) const;
		void setPixel(u32 x, u32 y, const RGBPixel* pixel);
	private:

		u8 *data;
		u32 width;
		u32 height;
		DDSS_FORCE_BY_REF_ONLY(RGBImage);
	};
};
#endif


