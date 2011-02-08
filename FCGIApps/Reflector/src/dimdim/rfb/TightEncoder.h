#ifndef _ddss_rfb_tightencoder_h_
#define _ddss_rfb_tightencoder_h_
#include "RFBHelper.h"
#include "ScreenBuffer.h"
#include "ScreenEncoder.h"
#include "jpeglib.h"
/* These parameters may be adjusted. */
#define MIN_SPLIT_RECT_SIZE     8*4096
#define MIN_SOLID_SUBRECT_SIZE  4*2048
#define MAX_SPLIT_TILE_SIZE       16

namespace dm
{
	class ScreenEncoder;
	class TightEncoder;
	
	/* Stuff dealing with palettes. */
	
	typedef struct COLOR_LIST_s
	{
		struct COLOR_LIST_s *next;
		int idx;
		CARD32 rgb;
	} COLOR_LIST;
	
	typedef struct PALETTE_ENTRY_s
	{
		COLOR_LIST *listNode;
		int numPixels;
	} PALETTE_ENTRY;
	
	typedef struct PALETTE_s
	{
		PALETTE_ENTRY entry[256];
		COLOR_LIST *hash[256];
		COLOR_LIST list[256];
	} PALETTE;
	/* Compression level stuff. The following array contains various
	 encoder parameters for each of 10 compression levels (0..9). Last
	 three parameters correspond to JPEG quality levels (0..9). NOTE:
	 This implementation does not include "gradient" filtering because
	 it can be inefficient with many client connections. That is why
	 gradientMinRectSize, gradientZlibLevel, gradientThreshold and
	 gradientThreshold24 fields are not used. Another field,
	 jpegThreshold, is also not used because we operate on the 24-bpp
	 framebuffer and thus always use jpegThreshold24. */
	
	typedef struct TIGHT_CONF_s
	{
		int maxRectSize, maxRectWidth;
		int monoMinRectSize, gradientMinRectSize;
		int idxZlibLevel, monoZlibLevel, rawZlibLevel, gradientZlibLevel;
		int gradientThreshold, gradientThreshold24;
		int idxMaxColorsDivisor;
		int jpegQuality, jpegThreshold, jpegThreshold24;
	} TIGHT_CONF;
	
	class TightEncoder
	{
	
	public:
		static TIGHT_CONF tightConf[];
		TightEncoder(ScreenEncoder& encoder);
		virtual ~TightEncoder();
		int encode(OutputStream& out, ScreenRectangle* r);
		void setFlashClient(bool b){ flashClient = b; }

		 static void Pack24(CARD8 *buf, int count);
	protected:
		int num_rects_tight(FB_RECT *r);
		/* Prototypes for static functions. */

		 void FindBestSolidArea (FB_RECT *r, CARD32 colorValue, FB_RECT *result);
		 void ExtendSolidArea   (FB_RECT *r, CARD32 colorValue, FB_RECT *result);
		 int  CheckSolidTile    (FB_RECT *r, CARD32 *colorPtr,
		                               int needSameColor);

		 int  SendRectSimple    (OutputStream& out, FB_RECT *r);
		 int  SendSubrect       (OutputStream& out, FB_RECT *r);
		 void SendTightHeader   (OutputStream& out, FB_RECT *r);

		 void SendSolidRect     (OutputStream& out);
		 int  SendMonoRect      (OutputStream& out, int w, int h);
		 int  SendIndexedRect   (OutputStream& out, int w, int h);
		 int  SendFullColorRect (OutputStream& out, int w, int h);

		 int  CompressData(OutputStream& out, int streamId, int dataLen,
		                         int zlibLevel, int zlibStrategy);
		 void SendCompressedData(OutputStream& out, int compressedLen);

		 void FillPalette8(int count);
		 void FillPalette16(int count);
		 void FillPalette32(int count);

		 void PaletteReset(void);
		 int  PaletteInsert(CARD32 rgb, int numPixels, int bpp);

		 void EncodeIndexedRect16(CARD8 *buf, int count);
		 void EncodeIndexedRect32(CARD8 *buf, int count);

		 void EncodeMonoRect8(CARD8 *buf, int w, int h);
		 void EncodeMonoRect16(CARD8 *buf, int w, int h);
		 void EncodeMonoRect32(CARD8 *buf, int w, int h);

		 int DetectSmoothImage(RFB_PIXEL_FORMAT *fmt, FB_RECT *r);
		 unsigned long DetectSmoothImage24(RFB_PIXEL_FORMAT *fmt, FB_RECT *r);

		 int SendJpegRect(OutputStream& out, FB_RECT *r, int quality);
		 void PrepareRowForJpeg(CARD8 *dst, int x, int y, int count);

		 static void JpegInitDestination(j_compress_ptr cinfo);
		 static boolean jpegEmptyOutputBuffer(j_compress_ptr cinfo);
		 static void JpegTermDestination(j_compress_ptr cinfo);
		 void JpegSetDstManager(j_compress_ptr cinfo);

		 size_t estimateCompressedSize(size_t inSize);
		 size_t compressBuffer(const void* inBuffer, size_t inSize, void* outBuffer, size_t outSize, int level);		
		 int CompressDataOneShot(OutputStream& out, int dataLen, int zlibLevel);
			
	private:
		bool flashClient;
		/* This variable is set on every encode_tight_block() call. */
		int usePixelFormat24;
		int compressLevel;
		int qualityLevel;
		int paletteNumColors, paletteMaxColors;
		CARD32 monoBackground, monoForeground;
		PALETTE palette;
		/* Pointers to dynamically-allocated buffers. */
		int tightBeforeBufSize;
		CARD8 *tightBeforeBuf;
		int tightAfterBufSize;
		CARD8 *tightAfterBuf;
		
		/*
		 * JPEG compression stuff.
		 */

		struct jpeg_destination_mgr jpegDstManager;
		int jpegError;
		int jpegDstDataLen;
		ScreenEncoder& screenEncoder;
	};
}
#endif
