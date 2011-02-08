#ifndef _DDSS_RFB_SCREEN_BUFFER_H_
#define _DDSS_RFB_SCREEN_BUFFER_H_
#include "RFBHelper.h"
#include "ScreenRectangle.h"
#include "ScreenInfo.h"

namespace dm
{
	class ScreenDecoderCallback
	{
	public:
		ScreenDecoderCallback(){}
		virtual ~ScreenDecoderCallback(){}
		virtual void rectComplete(ScreenRectangle& r)=0;
		virtual void updateComplete(size_t rectCount, size_t maxRects, bool markerFound)=0;
		virtual void errorOccured(int errorCode, const String errorMsg) = 0;
		virtual void resizeReceived(size_t w, size_t h) = 0;
	};
	///
	///	The Screen Buffer class
	///	represets a 32bit pixel array of w X h dimension
	///
	class ScreenBuffer
	{
		friend class ScreenEncoder;
		friend class ScreenDecoder;
		friend class TightEncoder;
		friend class TightDecoder;
		friend class ScreenDecoder2;
		friend class TightDecoder2;
	public:
		typedef void (*TRANSFUNC_PTR)(void *dst_buf, FB_RECT *r, void *table);
		
		
		ScreenBuffer();
		virtual ~ScreenBuffer();
		
		void init(ScreenInfo& si);
		void resize(size_t w, size_t h);
		
		void destroy();
		void clear();

		const void* getBuffer(size_t x, size_t y) const;
		void* getBuffer(size_t x, size_t y);
		
		const void* getBuffer(size_t offset = 0) const;
		void* getBuffer(size_t offset = 0);
		
		const u32& at(size_t index) const;
		u32& at(size_t index);
		
		const u32& operator[](size_t index) const{ return at(index); }
		u32& operator[](size_t index){ return at(index); }

		size_t getWidth() const{ return width; }
		size_t getHeight() const{ return height; }
		

		// ------------------------------------------------------------- //
		//	Various routines to generate translation tables for incoming //
		//	pixel data as defined by the pixel format					 //
		// ------------------------------------------------------------- //
		static void *gen_trans_table(RFB_PIXEL_FORMAT *fmt);
		static void *gen_trans_table8(u8 r_max,u8 g_max, u8 b_max,u8 b_shift,u8 b_shift, u8 b_shift );
		static void *gen_trans_table16(RFB_PIXEL_FORMAT* fmt);
		static void *gen_trans_table32(RFB_PIXEL_FORMAT* fmt);


		// --------------------------------------------- //
		//	the actual translation function for incoming //
		//	pixel data as defined by the pixel format	 //
		// --------------------------------------------- //
		void transfunc_null(void *dst_buf, ScreenRectangle *r, void *table);
		void transfunc8(void *dst_buf, ScreenRectangle *r, void *table);
		void transfunc16(void *dst_buf, ScreenRectangle *r, void *table);
		void transfunc32(void *dst_buf, ScreenRectangle *r, void *table);
		

		// -----------------------------------------------------------//
		//	Various blitting functions called from the tight decoder  //
		// -----------------------------------------------------------//
		/*
		 * Draw 24-bit truecolor pixel array on the framebuffer.
		 */
		void tight_draw_truecolor_data(CARD8 *src, ScreenRectangle& s_rect);
		/*
		 * Draw indexed data on the framebuffer, each source pixel is either 1
		 * bit (two colors) or 8 bits (up to 256 colors).
		 */
		void tight_draw_indexed_data(CARD8 *src, ScreenRectangle& s_rect, s32 s_num_colors, CARD32* s_palette);
		/*
		 * Restore and draw the data processed with the "gradient" filter.
		 */
		void tight_draw_gradient_data(CARD8 *src, ScreenRectangle& s_rect);
		
		/*
		 * Draw a filled rectangle of said color
		 */
		void tight_draw_filled_rect(u32 color, ScreenRectangle& s_rect);
		
		void copy_rect(ScreenRectangle& s_rect);
		
	protected:
		/*
		 * Draw 24-bit truecolor pixel array on the framebuffer.
		 */
		void tight_draw_truecolor_data24(CARD8 *src, ScreenRectangle& s_rect);
		void tight_draw_truecolor_data8(CARD8 *src, ScreenRectangle& s_rect);
	private:
		ScreenInfo screenInfo;
		size_t width;
		size_t height;
		u32* data;
		Lock sbLock;
		CARD8* table8;
	};
};
#endif

