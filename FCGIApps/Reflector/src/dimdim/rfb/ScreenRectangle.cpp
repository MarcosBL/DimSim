#include "ScreenRectangle.h"


namespace dm
{

	bool RectHelper::isRectValid(ScreenRectangle& r, size_t screenWidth, size_t screenHeight)
	{
		if(r.enc == RFB_ENCODING_LASTRECT   || RFB_ENCODING_COPYRECT ||
			   r.enc == RFB_ENCODING_NEWFBSIZE  || r.enc == RFB_ENCODING_RICH_CURSOR || r.enc == RFB_ENCODING_CURSOR_POSITION || r.enc == RFB_ENCODING_RAW)
		{
			return true;
		}
		else
		{
			DDSS_VERBOSE("RectHelper")<<"Rect : "<<r<<" Compared within "<<screenWidth<<" X "<<screenHeight<<std::endl;
			return (r.w * r.h) > 0 && ( (r.x + r.w) <= screenWidth) && ((r.y + r.h) <= screenHeight);
		}
	}
	void RectHelper::resetRect(ScreenRectangle& r)
	{
		r.x = r.y = r.w = r.h = 0;
		r.src_x = r.src_y = 0;
		r.enc = 0xFFFFFFFF;
	}

	bool RectHelper::hasValidEncoding(ScreenRectangle& r)
	{
		return r.enc == RFB_ENCODING_TIGHT 		||
			   r.enc == RFB_ENCODING_RAW   		||
			   r.enc == RFB_ENCODING_COPYRECT	||
			   r.enc == RFB_ENCODING_RRE        ||
			   r.enc == RFB_ENCODING_CORRE		||
			   r.enc == RFB_ENCODING_HEXTILE    ||
			   r.enc == RFB_ENCODING_ZLIB       ||
			   r.enc == RFB_ENCODING_TIGHT      ||
			   r.enc == RFB_ENCODING_ZLIBHEX    ||
			   r.enc == RFB_ENCODING_LASTRECT   ||
			   r.enc == RFB_ENCODING_NEWFBSIZE  ;
	}
	String RectHelper::getEncodingName(s32 enc)
	{
		if(enc == RFB_ENCODING_TIGHT)
		{
			return "TIGHT";
		}
		else if(enc == RFB_ENCODING_RAW)
		{
			return "RAW";
		}
		else if(enc == RFB_ENCODING_COPYRECT)
		{
			return "COPY RECT";
		}
		else if(enc == RFB_ENCODING_RRE)
		{
			return "RRE";
		}
		else if(enc == RFB_ENCODING_CORRE)
		{
			return "CORRE";
		}
		else if(enc == RFB_ENCODING_HEXTILE)
		{
			return "HEXTILE";
		}
		else if(enc == RFB_ENCODING_ZLIB)
		{
			return "ZLIB";
		}
		else if(enc == RFB_ENCODING_TIGHT)
		{
			return "TIGHT";
		}
		else if(enc == RFB_ENCODING_ZLIBHEX)
		{
			return "ZLIBHEX";
		}
		else if(enc == (s32)RFB_ENCODING_LASTRECT)
		{
			return "LASTRECT";
		}
		else if(enc == (s32)RFB_ENCODING_NEWFBSIZE)
		{
			return "NEWFBSIZE";
		}
		else if(enc == (s32)RFB_ENCODING_CURSOR_POSITION)
		{
			return "CURSOR_POSITION";
		}
		else if(enc == (s32)RFB_ENCODING_RICH_CURSOR)
		{
			return "RICH_CURSOR";
		}
		else
		{
			return "UNKNOWN";
		}
	}

	bool RectHelper::rectsIntersect(const ScreenRectangle& r1, const ScreenRectangle& r2)
	{
		u16 xDist = r1.x > r2.x? r1.x - r2.x + r1.w : r2.x - r1.x + r2.w;
		u16 yDist = r1.y > r2.y? r1.y - r2.y + r1.h: r2.y - r1.y + r2.h;
		
		return (xDist <= (r1.w + r2.w) && yDist <= (r1.h + r2.h));
	}
	bool RectHelper::mergeIfTouching(ScreenRectangle& r1, const ScreenRectangle& r2)
	{
		if(rectsIntersect(r1,r2))
		{
			//std::cout<<"Merging : "<<r1<<" with "<<r2<<std::endl;
			ScreenRectangle ret;
			
			if(r1.x > r2.x)
			{
				ret.x = r2.x;
			}
			else 
			{
				ret.x = r1.x;
			}
			if(r1.y > r2.y)
			{
				ret.y = r2.y;
			}
			else 
			{
				ret.y = r1.y;
			}
			u16 x2 = 0;
			u16 y2 = 0;
			if(r1.x + r1.w > r2.x + r2.w)
			{
				x2 = r1.x + r1.w;
			}
			else 
			{
				x2 = r2.x + r2.w;
			}
			ret.w = x2 - ret.x;
			if(r1.y + r1.h > r2.y + r2.h)
			{
				y2 = r1.y + r1.h;
			}
			else 
			{
				y2 = r2.y + r2.h;
			}
			ret.h = y2 - ret.y;
						
			r1.x = ret.x;
			r1.y = ret.y;
			r1.w = ret.w;
			r1.h = ret.h;
			//std::cout<<"Merged Rect : "<<r1<<std::endl;
			return true;
		}
		return false;
	}
	
	
}

