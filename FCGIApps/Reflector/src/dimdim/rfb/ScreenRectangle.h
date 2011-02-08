#ifndef _dimdim_rfb_ScreenRectangle_h_
#define _dimdim_rfb_ScreenRectangle_h_
#include "RFBHelper.h"


namespace dm
{
	typedef FB_RECT	ScreenRectangle;
	class RectHelper
	{
	public:
		static bool hasValidEncoding(ScreenRectangle& r);
		static String getEncodingName(s32 enc);
		static bool isRectValid(ScreenRectangle& r, size_t screenWidth, size_t screenHeight);
		static void resetRect(ScreenRectangle& r);
		static bool rectsIntersect(const ScreenRectangle& r1, const ScreenRectangle& r2);
		static bool mergeIfTouching(ScreenRectangle& r1, const ScreenRectangle& r2);
	};
};

inline
std::ostream& operator << (std::ostream& os, const dm::ScreenRectangle& r)
{
	return os<<"(RECT : x="<<r.x<<",y="<<r.y<<",dim="<<r.w<<"X"<<r.h<<",enc=0x"<<std::hex<<r.enc<<std::dec<<" {"<<dm::RectHelper::getEncodingName(r.enc)<<"} )";
}
#endif

