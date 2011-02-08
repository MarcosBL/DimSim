#ifndef _ddss_rfb_tight_decoder_h_
#define _ddss_rfb_tight_decoder_h_
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <zlib.h>
#include "ScreenDecoder.h"

#include "jpeglib.h"

namespace dm
{
	class TightDecoder;
	class ScreenDecoder;
	class ScreenDecoderCallback;
	
	class TightDecoder
	{
	public:
		TightDecoder(ScreenDecoder& decoder);
		virtual ~TightDecoder();
		void init();
		void cleanup();
		bool decode(InputStream& in, ScreenRectangle& r);
	protected:
		bool decodeControlByte(InputStream& in, ScreenRectangle& s_rect);
		bool decodeTightFill(InputStream& in, ScreenRectangle& s_rect);
		bool decodeTightFilter(InputStream& in, ScreenRectangle& s_rect);
		bool decodeTightNumColors(InputStream& in, ScreenRectangle& s_rect);
		bool decodeTightPalette(InputStream& in, ScreenRectangle& s_rect);
		bool decodeTightRaw(InputStream& in, ScreenRectangle& s_rect);
		bool decodeTightIndexed(InputStream& in, ScreenRectangle& s_rect);
		bool readCompactLen(InputStream& in,  u32& len, u8& numBytesRead);
		bool decodeTightCompressed(InputStream& in, ScreenRectangle& s_rect);
		bool decodeTightJPEG(InputStream& in, ScreenRectangle& s_rect);
		bool decompressJPEGBuffer(const u8* buf, size_t len, ScreenRectangle& r);
		
		void resetStreams(void);
		
		static void JpegInitSource(j_decompress_ptr cinfo);
		
		static boolean JpegFillInputBuffer(j_decompress_ptr cinfo);
		
		static void JpegSkipInputData(j_decompress_ptr cinfo, long num_bytes);
		
		static void JpegTermSource(j_decompress_ptr cinfo);
		
		static void JpegSetSrcManager(j_decompress_ptr cinfo, CARD8 *compressedData,
				  int compressedLen);
	private:
		size_t calculateSize(size_t w, size_t h);
		ScreenDecoder& screenDecoder;
		z_stream s_zstream[4];
		int s_zstream_active[4];
		CARD8 s_reset_streams;
		int s_stream_id;
		int s_filter_id;
		int s_num_colors;
		CARD32 s_palette[256];
		int s_compressed_size, s_uncompressed_size;
		
		
		struct jpeg_source_mgr jpegSrcManager;
		JOCTET *jpegBufferPtr;
		size_t jpegBufferLen;
		int jpegError;
		
		
		
	};
};
#endif

