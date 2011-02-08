#include "ScreenBuffer.h"

namespace dm
{
void *ScreenBuffer::gen_trans_table(RFB_PIXEL_FORMAT *fmt)
{
	switch (fmt->bits_pixel)
	{
	case 8:
		return gen_trans_table8(fmt->r_max, fmt->g_max, fmt->b_max,
				fmt->r_shift, fmt->g_shift, fmt->b_shift);
	case 16:
		return gen_trans_table16(fmt);
	case 32:
		return gen_trans_table32(fmt);
	}
	return 0;
}
void *ScreenBuffer::gen_trans_table8(u8 r_max, u8 g_max, u8 b_max, u8 r_shift,
		u8 g_shift, u8 b_shift)
{

	CARD8 *table;
	CARD8 r, g, b;
	int c;

	/* Allocate space for 3 tables for 8-bit R, G, B components */
	table = (CARD8*)malloc(256 * 3 * sizeof(CARD8));

	/* Fill in translation tables */
	if (table != NULL)
	{
		for (c = 0; c < 256; c++)
		{
			r = (CARD8)((c * r_max + 127) / 255 << r_shift);
			g = (CARD8)((c * g_max + 127) / 255 << g_shift);
			b = (CARD8)((c * b_max + 127) / 255 << b_shift);
			/*if ((fmt->big_endian != 0) ==                                     
			 (!ByteOrder::getInstance().isBigEndian())) 
			 {                  
			 table[c] = r;                                                   
			 table[256 + c] = g;                                             
			 table[512 + c] = b;                                             
			 } 
			 else*/
			{
				table[c] = SWAP_PIXEL8(r);
				table[256 + c] = SWAP_PIXEL8(g);
				table[512 + c] = SWAP_PIXEL8(b);
			}
		}
	}

	return (void *)table;
}
void *ScreenBuffer::gen_trans_table16(RFB_PIXEL_FORMAT* fmt)
{
	CARD16 *table;
	CARD16 r, g, b;
	int c;

	/* Allocate space for 3 tables for 8-bit R, G, B components */
	table = new CARD16[256 * 3 * sizeof(CARD16)];

	/* Fill in translation tables */
	if (table != NULL)
	{
		for (c = 0; c < 256; c++)
		{
			r = (CARD16)((c * fmt->r_max + 127) / 255 << fmt->r_shift);
			g = (CARD16)((c * fmt->g_max + 127) / 255 << fmt->g_shift);
			b = (CARD16)((c * fmt->b_max + 127) / 255 << fmt->b_shift);
			if ((fmt->big_endian != 0) == (!ByteOrder::getInstance().isBigEndian()))
			{
				table[c] = r;
				table[256 + c] = g;
				table[512 + c] = b;
			}
			else
			{
				table[c] = SWAP_PIXEL16(r);
				table[256 + c] = SWAP_PIXEL16(g);
				table[512 + c] = SWAP_PIXEL16(b);
			}
		}
	}

	return (void *)table;
}
void *ScreenBuffer::gen_trans_table32(RFB_PIXEL_FORMAT* fmt)
{
	CARD32 *table;
	CARD32 r, g, b;
	int c;

	/* Allocate space for 3 tables for 8-bit R, G, B components */
	table = new CARD32[256 * 3 * sizeof(CARD32)];

	/* Fill in translation tables */
	if (table != NULL)
	{
		for (c = 0; c < 256; c++)
		{
			r = (CARD32)((c * fmt->r_max + 127) / 255 << fmt->r_shift);
			g = (CARD32)((c * fmt->g_max + 127) / 255 << fmt->g_shift);
			b = (CARD32)((c * fmt->b_max + 127) / 255 << fmt->b_shift);
			if ((fmt->big_endian != 0) == (!ByteOrder::getInstance().isBigEndian()))
			{
				table[c] = r;
				table[256 + c] = g;
				table[512 + c] = b;
			}
			else
			{
				table[c] = SWAP_PIXEL32(r);
				table[256 + c] = SWAP_PIXEL32(g);
				table[512 + c] = SWAP_PIXEL32(b);
			}
		}
	}

	return (void *)table;
}

void ScreenBuffer::transfunc_null(void *dst_buf, ScreenRectangle *r,
		void *table)
{
	//std::cout<<"ScreenBuffer::transfunc_null() - "<<(*r)<<std::endl;
	CARD32 *fb_ptr;
	CARD32 *dst_ptr = (CARD32 *)dst_buf;
	int y;

	fb_ptr = &data[r->y * width + r->x];

	for (y = 0; y < r->h; y++)
	{
		memcpy(dst_ptr, fb_ptr, r->w * sizeof(CARD32));
		fb_ptr += width;
		dst_ptr += r->w;
	}
}
void ScreenBuffer::transfunc8(void *dst_buf, ScreenRectangle *r, void *table)
{
	CARD32 *fb_ptr;
	CARD32 fb_pixel;
	CARD8 *dst_ptr = (CARD8 *)dst_buf;
	CARD8 *tbl_r = (CARD8 *)table;
	CARD8 pixel = 0;
	int x, y, w, h;

	fb_ptr = &data[r->y * width + r->x];
	w = r->w;
	h = r->h;

	/* Make sure fb_pixel != *fb_ptr */
	fb_pixel = ~(*fb_ptr);

	for (y = 0; y < h; y++)
	{
		for (x = 0; x < w; x++)
		{
			if (fb_pixel != *fb_ptr++)
			{
				fb_pixel = *(fb_ptr - 1);
				pixel = ColorHelper::getInstance()->reverseByte(fb_pixel);//(tbl_r[fb_pixel >> 16 & 0xFF] | tbl_r[256 + (fb_pixel >> 8 & 0xFF)] | tbl_r[512 + (fb_pixel & 0xFF)]);
			}
			*dst_ptr++ = pixel;
		}
		fb_ptr += (width - w);
	}
}
void ScreenBuffer::transfunc16(void *dst_buf, ScreenRectangle *r, void *table)
{

	CARD32 *fb_ptr;
	CARD32 fb_pixel;
	CARD16 *dst_ptr = (CARD16 *)dst_buf;
	CARD16 *tbl_r = (CARD16 *)table;
	CARD16 pixel = 0;
	int x, y, w, h;

	fb_ptr = &data[r->y * width + r->x];
	w = r->w;
	h = r->h;

	/* Make sure fb_pixel != *fb_ptr */
	fb_pixel = ~(*fb_ptr);

	for (y = 0; y < h; y++)
	{
		for (x = 0; x < w; x++)
		{
			if (fb_pixel != *fb_ptr++)
			{
				fb_pixel = *(fb_ptr - 1);
				pixel = (tbl_r[fb_pixel >> 16 & 0xFF] | tbl_r[256 + (fb_pixel >> 8 & 0xFF)] | tbl_r[512 + (fb_pixel & 0xFF)]);
			}
			*dst_ptr++ = pixel;
		}
		fb_ptr += (width - w);
	}

}
void ScreenBuffer::transfunc32(void *dst_buf, ScreenRectangle *r, void *table)
{
	CARD32 *fb_ptr;
	CARD32 fb_pixel;
	CARD32 *dst_ptr = (CARD32 *)dst_buf;
	CARD32 *tbl_r = (CARD32 *)table;
	CARD32 pixel = 0;
	int x, y, w, h;

	fb_ptr = &data[r->y * width + r->x];
	w = r->w;
	h = r->h;

	/* Make sure fb_pixel != *fb_ptr */
	fb_pixel = ~(*fb_ptr);

	for (y = 0; y < h; y++)
	{
		for (x = 0; x < w; x++)
		{
			if (fb_pixel != *fb_ptr++)
			{
				fb_pixel = *(fb_ptr - 1);
				pixel = (tbl_r[fb_pixel >> 16 & 0xFF] | tbl_r[256 + (fb_pixel >> 8 & 0xFF)] | tbl_r[512 + (fb_pixel & 0xFF)]);
			}
			*dst_ptr++ = pixel;
		}
		fb_ptr += (width - w);

	}

}

ScreenBuffer::ScreenBuffer() :
	width(0), height(0), data(0), table8(0)
{

}
ScreenBuffer::~ScreenBuffer()
{
	destroy();
}
void ScreenBuffer::resize(size_t w, size_t h)
{
	ScopedLock s(sbLock);

	std::cout<<"ScreenBuf"<<"RESIZING : Old Dimension : "<<width<<" X "<<height<<" and requested : "<<w<<"X"<<h<<std::endl;
	if (h > height || w > width)
	{
		if (data)

		{
			delete[] data;
			data = 0;
		}
		if (h*w != 0)
		{
			width = w;
			height = h;
			data = new u32[width*height];
			memset(data, 0, width*height*sizeof(u32));
			std::cout<<"ScreenBuf"<<"[RESIZED] New Dimension : "<<width<<" X "<<height<<std::endl;
		}
		else
		{
			width = 0;
			height = 0;
		}
	}
	else
	{
		memset(data, 0, width*height*sizeof(u32));
		std::cout<<"ScreenBuf"<<"[RESIZE-FAKED] "<<"old : "<<width<<" X "<<height<<" can accomodate "<<w<<"X"<<h<<std::endl;
	}
	width = w;
	height = h;

	std::cout<<"RESIZED TO : "<<w<<"X"<<h<<std::endl;
	screenInfo.width = (u16)w;
	screenInfo.height = (u16)h;
}
void ScreenBuffer::init(ScreenInfo& si)
{
	{ScopedLock s(sbLock);

	std::cout<<"ScreenBuf"<<"Old Dimension : "<<width<<" X "<<height<<std::endl;
	screenInfo = si;
	size_t w = (size_t)(si.getWidth());
	size_t h = (size_t)(si.getHeight());
	std::cout<<"ScreenBuf"<<" Init called with "<<si<<std::endl;
	std::cout<<"ScreenBuf"<<"RESIZING : Old Dimension : "<<width<<" X "<<height<<" and requested : "<<w<<"X"<<h<<std::endl;
	if (h > height || w > width)
	{
		if (data)

		{
			delete[] data;
			data = 0;
		}
		if (h*w != 0)
		{
			width = w;
			height = h;
			data = new u32[width*height];
			memset(data, 0, width*height*sizeof(u32));
			std::cout<<"ScreenBuf"<<"[RESIZED] New Dimension : "<<width<<" X "<<height<<std::endl;
		}
		else
		{
			width = 0;
			height = 0;
		}
	}
	else
	{
		std::cout<<"ScreenBuf"<<"[RESIZE-FAKED] "<<"old : "<<width<<" X "<<height<<" can accomodate "<<w<<"X"<<h<<std::endl;
	}
	width = w;
	height = h;

	if (si.getBitsPerPixel() == 8)
	{
		table8 = (u8*)this->gen_trans_table8(si.redMax, si.greenMax,
				si.blueMax, si.redShift, si.greenShift, si.blueShift);
	}
	else
	{
		table8 = 0;
	}
	}
	ScreenRectangle fullRect;
	fullRect.x = fullRect.y = 0;
	fullRect.w = width;
	fullRect.h = height;
	this->tight_draw_filled_rect(MAKERGB24PIXEL32(0,0,255),fullRect);
}

void ScreenBuffer::destroy()
{
	ScopedLock s(sbLock);
	if (data)
	{
		delete[] data;
		data = 0;
	}

	if (table8)
	{
		free(table8);
		table8 = 0;
	}
}
void ScreenBuffer::clear()
{
	ScopedLock s(sbLock);
	if (data)
	{
		memset(data, 0, width*height*sizeof(u32));
	}
}

const void* ScreenBuffer::getBuffer(size_t x, size_t y) const
{
	return getBuffer(y*width+x);
}
void* ScreenBuffer::getBuffer(size_t x, size_t y)
{
	return getBuffer(y*width+x);
}
const void* ScreenBuffer::getBuffer(size_t offset) const
{
	return data + offset;
}
void* ScreenBuffer::getBuffer(size_t offset)
{
	return data+offset;
}

const u32& ScreenBuffer::at(size_t index) const
{
	return data[index];
}
u32& ScreenBuffer::at(size_t index)
{
	return data[index];
}

/*
 * Draw 24-bit truecolor pixel array on the framebuffer.
 */

void ScreenBuffer::tight_draw_truecolor_data(CARD8 *src, ScreenRectangle& s_rect)
{
	if (this->screenInfo.getBitsPerPixel() == 8)
	{
		tight_draw_truecolor_data8(src, s_rect);
	}
	else
	{
		tight_draw_truecolor_data24(src, s_rect);

	}

}

/*
 * Draw 24-bit truecolor pixel array on the framebuffer.
 */

void ScreenBuffer::tight_draw_truecolor_data8(CARD8 *src,
		ScreenRectangle& s_rect)
{

	ScopedLock sl(sbLock);
	int x, y;
	CARD32 *fb_ptr;
	CARD8 *read_ptr;

	fb_ptr = &data[s_rect.y * (int)width + s_rect.x];
	read_ptr = src;
	u32 i = 0;

	for (y = 0; y < s_rect.h; y++)
	{
		for (x = 0; x < s_rect.w; x++)
		{
			*fb_ptr++ = ColorHelper::getInstance()->lookup(read_ptr[i++]);
		}
		fb_ptr += width - s_rect.w;
	}
}
/*
 * Draw 24-bit truecolor pixel array on the framebuffer.
 */

void ScreenBuffer::tight_draw_truecolor_data24(CARD8 *src,
		ScreenRectangle& s_rect)
{

	ScopedLock sl(sbLock);
	int x, y;
	CARD32 *fb_ptr;
	CARD8 *read_ptr;

	fb_ptr = &data[s_rect.y * (int)width + s_rect.x];
	read_ptr = src;

	for (y = 0; y < s_rect.h; y++)
	{
		for (x = 0; x < s_rect.w; x++)
		{
			*fb_ptr++ = read_ptr[0] << 16 | read_ptr[1] << 8 | read_ptr[2];
			read_ptr += 3;
		}
		fb_ptr += width - s_rect.w;
	}
}

/*
 * Draw indexed data on the framebuffer, each source pixel is either 1
 * bit (two colors) or 8 bits (up to 256 colors).
 */

void ScreenBuffer::tight_draw_indexed_data(CARD8 *src, ScreenRectangle& s_rect,
		s32 s_num_colors, CARD32* s_palette)
{
	int x, y, b, w;
	ScopedLock sl(sbLock);
	CARD32 *fb_ptr;
	CARD8 *read_ptr;

	fb_ptr = &data[s_rect.y * (int)width + s_rect.x];
	read_ptr = src;

	if (s_num_colors <= 2)
	{
		w = (s_rect.w + 7) / 8;
		for (y = 0; y < s_rect.h; y++)
		{
			for (x = 0; x < s_rect.w / 8; x++)
			{
				for (b = 7; b >= 0; b--)
				{
					*fb_ptr++ = s_palette[*read_ptr >> b & 1];
				}
				read_ptr++;
			}
			for (b = 7; b >= 8 - s_rect.w % 8; b--)
			{
				*fb_ptr++ = s_palette[*read_ptr >> b & 1];
			}
			if (s_rect.w & 0x07)
				read_ptr++;
			fb_ptr += width - s_rect.w;
		}
	}
	else
	{
		for (y = 0; y < s_rect.h; y++)
		{
			for (x = 0; x < s_rect.w; x++)
			{
				*fb_ptr++ = s_palette[*read_ptr++];
			}
			fb_ptr += width - s_rect.w;
		}
	}
}

/*
 * Restore and draw the data processed with the "gradient" filter.
 */

void ScreenBuffer::tight_draw_gradient_data(CARD8 *src, ScreenRectangle& s_rect)
{
	ScopedLock sl(sbLock);
	int x, y, c;
	CARD32 *fb_ptr;
	CARD8 prev_row[2048*3];
	CARD8 this_row[2048*3];
	CARD8 pix[3];
	int est;
	fb_ptr = &data[s_rect.y * (int)width + s_rect.x];
	memset(prev_row, 0, s_rect.w * 3);
	for (y = 0; y < s_rect.h; y++)
	{
		/* First pixel in a row */
		for (c = 0; c < 3; c++)
		{
			pix[c] = prev_row[c] + src[y*s_rect.w*3+c];
			this_row[c] = pix[c];
		}
		*fb_ptr++ = pix[0] << 16 | pix[1] << 8 | pix[2];
		/* Remaining pixels of a row */
		for (x = 1; x < s_rect.w; x++)
		{
			for (c = 0; c < 3; c++)
			{
				est = (int)prev_row[x*3+c] + (int)pix[c] - (int)prev_row[(x-1)*3+c];
				if (est > 0xFF)
				{
					est = 0xFF;
				}
				else if (est < 0x00)
				{
					est = 0x00;
				}
				pix[c] = (CARD8)est + src[(y*s_rect.w+x)*3+c];
				this_row[x*3+c] = pix[c];
			}
			*fb_ptr++ = pix[0] << 16 | pix[1] << 8 | pix[2];
		}
		fb_ptr += width - s_rect.w;
		memcpy(prev_row, this_row, s_rect.w * 3);
	}
}
/*
 * Draw a filled rectangle of said color
 */
void ScreenBuffer::tight_draw_filled_rect(u32 color, ScreenRectangle& s_rect)
{
	
	ScopedLock sl(sbLock);
	int x, y;
	CARD32 *fb_ptr;

	u32 color2 = color;

	//if(s_rect.x == 0){ std::cout<<"FILL RECT : "<<s_rect<<" / Color : 0x"<<std::hex<<color<<std::dec<<std::endl; }

	fb_ptr = &data[s_rect.y * (int)width + s_rect.x];

	/* Fill the first row */
	for (x = 0; x < s_rect.w; x++)
		fb_ptr[x] = color2;

	u32 *row1 = fb_ptr;
	/* Copy the first row into all other rows */
	for (y = 1; y < s_rect.h; y++)
	{
		fb_ptr = &data[(s_rect.y + y)* (int)width + s_rect.x];
		memcpy(fb_ptr, row1, s_rect.w * sizeof(CARD32));
	}
}

void ScreenBuffer::copy_rect(ScreenRectangle& s_rect)
{
	ScopedLock sl(sbLock);

	if (s_rect.y + s_rect.h <= height && s_rect.src_y + s_rect.h <= height
			&& s_rect.x + s_rect.w <= width && s_rect.src_x + s_rect.w <= width)
	{
		//std::cout<<"Applying COPY RECT : "<<s_rect<<" (src="<<s_rect.src_x<<","<<s_rect.src_y<<") to  Screen : "<<width<<"X"<<height<<std::endl;			
		//init a tmp var for our copy logic
		u32 *tmpArr = new u32[s_rect.w * s_rect.h];
		ScopedArray<u32> tmpPtr(tmpArr);

		for (int i = 0; i < s_rect.h; i++)
		{
			u32* tmp = &tmpArr[i * s_rect.w];
			u32* fb_ptr_src = &data[(s_rect.src_y+i) * (int)width + s_rect.src_x];
			memcpy(tmp, fb_ptr_src, s_rect.w * sizeof(CARD32));
		}

		for (int i = 0; i < s_rect.h; i++)
		{
			u32* tmp = &tmpArr[i * s_rect.w];
			u32* fb_ptr_dst = &data[(s_rect.y+i) * (int)width + s_rect.x];
			memcpy(fb_ptr_dst, tmp, s_rect.w * sizeof(CARD32));
		}

/*		for(int i = 0; i < s_rect.h; i++)
		{
			u32* src = &(data[(s_rect.src_y+i) * (int)width + s_rect.src_x]);
			u32* dst = &(data[(s_rect.y+i) * (int)width + s_rect.x]);
			memmove(dst,src, s_rect.w * sizeof(CARD32));
		}*/
	}
	else
	{
		std::cout<<"Invalid Copy Rect : "<<s_rect<<" (src="<<s_rect.src_x<<","
				<<s_rect.src_y<<") / Screen : "<<width<<"X"<<height<<std::endl;
	}
}
}
;

