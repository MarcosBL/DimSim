#ifndef _DDSS_SCREEN_INFO_H_
#define _DDSS_SCREEN_INFO_H_
#include "RFBHelper.h"

namespace dm
{
	class ScreenInfo
	{
		friend class ScreenEncoder;
		friend class ScreenBuffer;
		friend class Screen;
		friend class ScreenSub;
	public:
		ScreenInfo();
		virtual ~ScreenInfo();
		void fill(RFB_PIXEL_FORMAT* fmt);
		ScreenInfo(const ScreenInfo& other);
		const ScreenInfo& operator = (const ScreenInfo& other);
		void load(InputStream& in);
		void save(OutputStream& out) const;
		void populate(RFB_PIXEL_FORMAT& fmt) const;
		void clear();
		u16 getWidth() const{ return width; }
		u16 getHeight() const{ return height; }
		const String& getScreenName() const{ return screenName; }
		u8 getBitsPerPixel() const{ return (dm::u16)bpp; }
		u8 getDepth() const{ return (dm::u16)depth; }
		bool isBigEndian() const{ return bigEndian > 0; }
		bool isTrueColor() const{ return trueColor > 0; }
		u16 getRedMax() const{ return redMax; }
		u16 getGreenMax() const{ return greenMax; }
		u16 getBlueMax() const{ return blueMax; }
		u8 getRedShift() const{ return redShift; }
		u8 getGreenShift() const{ return greenShift; }
		u8 getBlueShift() const{ return blueShift; }
		bool isValid() const{ return (width != 0 && height != 0 && bpp != 0 && depth != 0) ; }
	private:
		u16 width;
		u16 height;
		u8 bpp;
		u8 depth;
		u8 bigEndian;
		u8 trueColor;
		u16 redMax, greenMax, blueMax;
		u8 redShift, greenShift, blueShift;
		u8 padding[3];
		String screenName;
	};
};
inline
std::ostream& operator<<(std::ostream& os, const dm::ScreenInfo& sinfo)
{
	return (os<<"SCREENINFO("<<sinfo.getWidth()<<"X"<<sinfo.getHeight()<<"@"<<(dm::u16)sinfo.getBitsPerPixel()<<"/"<<(dm::u16)sinfo.getDepth()<<"/BE="<<sinfo.isBigEndian()<<"/TC="<<sinfo.isTrueColor()<<"/MAX="<<sinfo.getRedMax()<<","<<sinfo.getGreenMax()<<","<<sinfo.getBlueMax()<<"/SHIFT="<<(dm::u16)sinfo.getRedShift()<<","<<(dm::u16)sinfo.getGreenShift()<<","<<(dm::u16)sinfo.getBlueShift()<<") ["<<sinfo.getScreenName()<<"]");
}
#endif

