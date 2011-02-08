#ifndef _ddss_rfb_tight_decoder2_h_
#define _ddss_rfb_tight_decoder2_h_
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <zlib.h>
#include "ScreenDecoder2.h"

namespace dm
{
	class TightDecoder2;
	class ScreenDecoder2;
	class ScreenDecoderCallback;
	
	class TightDecoder2
	{
	public:
		enum Result
		{
			TDR_FAILED = 0,
			TDR_INCOMPLETE,
			TDR_OK
		};
		TightDecoder2(ScreenDecoder2& decoder);
		virtual ~TightDecoder2();
		void init();
		void cleanup();
		TightDecoder2::Result decode(InputStream& in, ScreenRectangle& r);
	protected:
		TightDecoder2::Result decodeControlByte(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decodeTightFill(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decodeTightFilter(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decodeTightNumColors(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decodeTightPalette(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decodeTightRaw(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decodeTightIndexed(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result readCompactLen(InputStream& in,  u32& len, u8& numBytesRead);
		TightDecoder2::Result decodeTightCompressed(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decodeTightJPEG(InputStream& in, ScreenRectangle& s_rect);
		TightDecoder2::Result decompressJPEGBuffer(const u8* buf, size_t len, ScreenRectangle& r);
		
		void resetStreams(void);
		
	private:
		size_t calculateSize(size_t w, size_t h);
		ScreenDecoder2& screenDecoder;
		z_stream s_zstream[4];
		int s_zstream_active[4];
		CARD8 s_reset_streams;
		int s_stream_id;
		int s_filter_id;
		int s_num_colors;
		CARD32 s_palette[256];
		int s_compressed_size, s_uncompressed_size;
	};
};
#endif

 
