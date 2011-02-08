#include "ScreenInfo.h"

namespace dm
{

	ScreenInfo::ScreenInfo()
	{
		clear();
	}
	ScreenInfo::~ScreenInfo()
	{
		clear();
	}
	
	ScreenInfo::ScreenInfo(const ScreenInfo& other)
	{
		*this = other;
	}

	void ScreenInfo::fill(RFB_PIXEL_FORMAT* fmt)
	{
		fmt->big_endian = bigEndian?1:0;
		fmt->true_color = trueColor?1:0;
		fmt->bits_pixel = bpp;
		fmt->r_max = redMax;
		fmt->g_max = greenMax;
		fmt->b_max = blueMax;
		fmt->r_shift = redShift;
		fmt->g_shift = greenShift;
		fmt->b_shift =  blueShift;
		
	}
	const ScreenInfo& ScreenInfo::operator = (const ScreenInfo& other)
	{
		screenName = other.screenName;
		width = other.width;
		height = other.height;
		bpp = other.bpp;
		depth = other.depth;
		bigEndian = other.bigEndian;
		trueColor = other.trueColor;
		redMax = other.redMax;
		greenMax = other.greenMax;
		blueMax = other.blueMax;
		redShift = other.redShift;
		greenShift = other.greenShift;
		blueShift = other.blueShift;
		memcpy(padding,other.padding,3);
		return *this;
	}
	void ScreenInfo::populate(RFB_PIXEL_FORMAT& fmt) const
	{
		fmt.bits_pixel = bpp;
		fmt.color_depth = depth;
		fmt.big_endian = bigEndian;
		fmt.true_color = trueColor;
		fmt.r_max = redMax;
		fmt.g_max = greenMax;
		fmt.b_max = blueMax;
		fmt.r_shift = redShift;
		fmt.g_shift = greenShift;
		fmt.b_shift = blueShift;
	}
	void ScreenInfo::load(InputStream& in)
	{
		clear();
		in.readShort(&this->width);
		in.readShort(&this->height);
		in.readByte(&this->bpp);
		in.readByte(&this->depth);
		in.readByte(&this->bigEndian);
		in.readByte(&this->trueColor);
		in.readShort(&this->redMax);
		in.readShort(&this->greenMax);
		in.readShort(&this->blueMax);
		in.readByte(&this->redShift);
		in.readByte(&this->greenShift);
		in.readByte(&this->blueShift);
		in.read(this->padding,3);
		
		u32 len = 0;
		in.readLong(&len);
		std::cout<<"Length Read : "<<len<<std::endl;
		char *buf = new char[len+1];
		ScopedArray<char> bufPtr(buf);
		memset(buf,0,len+1);
		in.read(buf,len);
		std::cout<<"Buffer : "<<buf<<std::endl;
		screenName = buf;

	}
	void ScreenInfo::save(OutputStream& out) const
	{
		out.writeShort(this->width);
		out.writeShort(this->height);
		out.writeByte(this->bpp);
		out.writeByte(this->depth);
		out.writeByte(this->bigEndian);
		out.writeByte(this->trueColor);
		out.writeShort(this->redMax);
		out.writeShort(this->greenMax);
		out.writeShort(this->blueMax);
		out.writeByte(this->redShift);
		out.writeByte(this->greenShift);
		out.writeByte(this->blueShift);
		out.write(this->padding,3);
		u32 len = (u32)screenName.size();
		out.writeLong(len);
		std::cout<<"Length written : "<<len<<std::endl;
		out.write(screenName.c_str(),screenName.size());
		std::cout<<"Screen Name : "<<screenName<<std::endl;
	}
	void ScreenInfo::clear()
	{
		width = height = 0;
		bpp = depth = bigEndian = trueColor = 0;
		redMax = greenMax = blueMax = 0;
		redShift = greenShift = blueShift = 0;
		screenName = "";
	}
};

