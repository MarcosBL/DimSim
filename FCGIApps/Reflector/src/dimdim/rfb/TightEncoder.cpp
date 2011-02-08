#include "TightEncoder.h"
/*
 * Code to guess if given rectangle is suitable for smooth image
 * compression (i.e. JPEG).
 */

#define JPEG_MIN_RECT_SIZE  4096

#define DETECT_SUBROW_WIDTH    7
#define DETECT_MIN_WIDTH       8
#define DETECT_MIN_HEIGHT      8
/*
 * Functions to operate with palette structures.
 */

#define HASH_FUNC16(rgb) ((int)((((rgb) >> 8) + (rgb)) & 0xFF))
#define HASH_FUNC32(rgb) ((int)((((rgb) >> 16) + ((rgb) >> 8)) & 0xFF))

namespace dm
{

TIGHT_CONF TightEncoder::tightConf[] =
{
{ 512, 32, 6, 65536, 0, 0, 0, 0, 0, 0, 4, 5, 10000, 23000 },
{ 2048, 128, 6, 65536, 1, 1, 1, 0, 0, 0, 8, 10, 8000, 18000 },
{ 6144, 256, 8, 65536, 3, 3, 2, 0, 0, 0, 24, 15, 6500, 15000 },
{ 10240, 1024, 12, 65536, 5, 5, 3, 0, 0, 0, 32, 25, 5000, 12000 },
{ 16384, 2048, 12, 65536, 6, 6, 4, 0, 0, 0, 32, 37, 4000, 10000 },
{ 32768, 2048, 12, 4096, 7, 7, 5, 4, 150, 380, 32, 50, 3000, 8000 },
{ 65536, 2048, 16, 4096, 7, 7, 6, 4, 170, 420, 48, 60, 2000, 5000 },
{ 65536, 2048, 16, 4096, 8, 8, 7, 5, 180, 450, 64, 70, 1000, 2500 },
{ 65536, 2048, 32, 8192, 9, 9, 8, 6, 190, 475, 64, 75, 500, 1200 },
{ 65536, 2048, 32, 8192, 9, 9, 9, 6, 200, 500, 96, 80, 200, 500 } };
TightEncoder::TightEncoder(ScreenEncoder& encoder) :
	screenEncoder(encoder)
{
	flashClient = false;
	tightBeforeBufSize = 0;
	tightBeforeBuf = NULL;

	tightAfterBufSize = 0;
	tightAfterBuf = NULL;
}
TightEncoder::~TightEncoder()
{

}

/*
 * Tight encoding implementation.
 */

int TightEncoder::num_rects_tight(FB_RECT *r)
{

	ScreenEncoder::CL_SLOT* cl = &(screenEncoder.slot);
	int maxRectSize, maxRectWidth;
	int subrectMaxWidth, subrectMaxHeight;

	/* No matter how many rectangles we will send if LastRect markers
	 are used to terminate rectangle stream. */
	if (cl->enable_lastrect && r->w * r->h >= MIN_SPLIT_RECT_SIZE)
		return 0;

	maxRectSize = tightConf[cl->compress_level].maxRectSize;
	maxRectWidth = tightConf[cl->compress_level].maxRectWidth;

	if (r->w > maxRectWidth || r->w * r->h > maxRectSize)
	{
		subrectMaxWidth = (r->w > maxRectWidth) ? maxRectWidth : r->w;
		subrectMaxHeight = maxRectSize / subrectMaxWidth;
		return (((r->w - 1) / maxRectWidth + 1) * ((r->h - 1)
				/ subrectMaxHeight + 1));
	}
	else
	{
		return 1;
	}
}

int TightEncoder::encode(OutputStream& out, ScreenRectangle* r)
{

	int nMaxRows;
	CARD32 colorValue;
	FB_RECT rtile, rbest, rtemp;
	int t;

	ScreenEncoder::CL_SLOT* cl = &(screenEncoder.slot);

	compressLevel = cl->compress_level;
	qualityLevel = cl->jpeg_quality;
	usePixelFormat24
			= !(screenEncoder.screenBuffer.screenInfo.getBitsPerPixel() == 8);

	if (r->w * r->h < MIN_SPLIT_RECT_SIZE)
	{
		return SendRectSimple(out, r);
	}

	/* Make sure we can write at least one pixel into tightBeforeBuf. */

	if (tightBeforeBufSize < 4)
	{
		tightBeforeBufSize = 4;
		if (tightBeforeBuf == NULL)
		{
			tightBeforeBuf = (u8*)malloc(tightBeforeBufSize);
		}
		else
		{
			tightBeforeBuf = (u8*)realloc(tightBeforeBuf, tightBeforeBufSize);
		}
	}

	/* Calculate maximum number of rows in one non-solid rectangle. */

	int maxRectSize, maxRectWidth, nMaxWidth;

	maxRectSize = tightConf[compressLevel].maxRectSize;
	maxRectWidth = tightConf[compressLevel].maxRectWidth;
	nMaxWidth = (r->w > maxRectWidth) ? maxRectWidth : r->w;
	nMaxRows = maxRectSize / nMaxWidth;

	/* Try to find large solid-color areas and send them separately. */

	for (rtile.y = r->y; rtile.y < r->y + r->h; rtile.y += MAX_SPLIT_TILE_SIZE)
	{

		/* If a rectangle becomes too large, send its upper part now. */

		if (rtile.y - r->y >= nMaxRows)
		{
			t = r->h - nMaxRows;
			r->h = nMaxRows;
			if (!SendRectSimple(out, r))
				return 0;
			r->y += nMaxRows;
			r->h = t;
		}

		rtile.h = (rtile.y + MAX_SPLIT_TILE_SIZE <= r->y + r->h) ? 
		MAX_SPLIT_TILE_SIZE : (r->y + r->h - rtile.y);

		for (rtile.x = r->x; rtile.x < r->x + r->w; rtile.x
				+= MAX_SPLIT_TILE_SIZE)
		{

			rtile.w = (rtile.x + MAX_SPLIT_TILE_SIZE <= r->x + r->w) ? 
			MAX_SPLIT_TILE_SIZE : (r->x + r->w - rtile.x);

			if (CheckSolidTile(&rtile, &colorValue, 0))
			{

				/* Get dimensions of solid-color area. */

				SET_RECT(&rtemp, rtile.x, rtile.y,
						r->w - (rtile.x - r->x),
						r->h - (rtile.y - r->y));
				FindBestSolidArea(&rtemp, colorValue, &rbest);

				/* Make sure a solid rectangle is large enough
				 (or the whole rectangle is of the same color). */

				if (rbest.w * rbest.h != r->w * r->h && rbest.w * rbest.h
						< MIN_SOLID_SUBRECT_SIZE)
					continue;

				/* Try to extend solid rectangle to maximum size. */

				ExtendSolidArea(r, colorValue, &rbest);

				/* Send rectangles at top and left to solid-color area. */

				SET_RECT(&rtemp, r->x, r->y, r->w, rbest.y - r->y);
				if (rbest.y != r->y && !SendRectSimple(out, &rtemp))
					return 0;
				SET_RECT(&rtemp, r->x, rbest.y, rbest.x - r->x, rbest.h);
				if (rbest.x != r->x && !encode(out, &rtemp))
					return 0;

				/* Send solid-color rectangle. */

				SendTightHeader(out, &rbest);

				SET_RECT(&rtemp, rbest.x, rbest.y, 1, 1);
				if(screenEncoder.screenBuffer.screenInfo.getBitsPerPixel() == 8)
				{
					screenEncoder.screenBuffer.transfunc8(tightBeforeBuf,
						&rtemp, screenEncoder.screenBuffer.table8);
					
				}
				else
				{
					screenEncoder.screenBuffer.transfunc_null(tightBeforeBuf,
						&rtemp, NULL);
				}
				SendSolidRect(out);

				/* Send remaining rectangles (at right and bottom). */

				SET_RECT(&rtemp, rbest.x + rbest.w, rbest.y,
						r->w - (rbest.x - r->x) - rbest.w, rbest.h);
				if (rbest.x + rbest.w != r->x + r->w && !encode(out, &rtemp))
					return 0;
				SET_RECT(&rtemp, r->x, rbest.y + rbest.h,
						r->w, r->h - (rbest.y - r->y) - rbest.h);
				if (rbest.y + rbest.h != r->y + r->h && !encode(out, &rtemp))
					return 0;

				/* Return after all recursive calls are done. */

				return 1;
			}

		}

	}

	/* No suitable solid-color rectangles found. */

	return SendRectSimple(out, r);
}

void TightEncoder::FindBestSolidArea(FB_RECT *r, CARD32 colorValue,
		FB_RECT *result)
{
	FB_RECT rc;
	int w_prev;
	int w_best = 0, h_best = 0;

	w_prev = r->w;

	for (rc.y = r->y; rc.y < r->y + r->h; rc.y += MAX_SPLIT_TILE_SIZE)
	{

		rc.h = (rc.y + MAX_SPLIT_TILE_SIZE <= r->y + r->h) ? 
		MAX_SPLIT_TILE_SIZE : (r->y + r->h - rc.y);
		rc.w = (w_prev > MAX_SPLIT_TILE_SIZE) ? 
		MAX_SPLIT_TILE_SIZE : w_prev;

		rc.x = r->x;
		if (!CheckSolidTile(&rc, &colorValue, 1))
			break;

		for (rc.x = r->x + rc.w; rc.x < r->x + w_prev;)
		{
			rc.w = (rc.x + MAX_SPLIT_TILE_SIZE <= r->x + w_prev) ? 
			MAX_SPLIT_TILE_SIZE : (r->x + w_prev - rc.x);
			if (!CheckSolidTile(&rc, &colorValue, 1))
				break;
			rc.x += rc.w;
		}

		w_prev = rc.x - r->x;
		if (w_prev * (rc.y + rc.h - r->y) > w_best * h_best)
		{
			w_best = w_prev;
			h_best = rc.y + rc.h - r->y;
		}
	}

	SET_RECT(result, r->x, r->y, w_best, h_best);
}

void TightEncoder::ExtendSolidArea(FB_RECT *r_bounds, CARD32 colorValue,
		FB_RECT *r)
{
	FB_RECT rtemp;

	rtemp.x = r->x;
	rtemp.w = r->w;
	rtemp.h = 1;

	/* Try to extend the area upwards. */
	if (r->y > 0)
	{
		for (rtemp.y = r->y - 1; rtemp.y >= r_bounds->y; rtemp.y--)
		{
			if (!CheckSolidTile(&rtemp, &colorValue, 1))
				break;
		}
		r->h += r->y - (rtemp.y + 1);
		r->y = rtemp.y + 1;
	}

	/* ... downwards. */
	for (rtemp.y = r->y + r->h; rtemp.y < r_bounds->y + r_bounds->h; rtemp.y++)
	{
		if (!CheckSolidTile(&rtemp, &colorValue, 1))
			break;
	}
	r->h += rtemp.y - (r->y + r->h);

	rtemp.y = r->y;
	rtemp.h = r->h;
	rtemp.w = 1;

	/* ... to the left. */
	if (r->x > 0)
	{
		for (rtemp.x = r->x - 1; rtemp.x >= r_bounds->x; rtemp.x--)
		{
			if (!CheckSolidTile(&rtemp, &colorValue, 1))
				break;
		}
		r->w += r->x - (rtemp.x + 1);
		r->x = rtemp.x + 1;
	}

	/* ... to the right. */
	for (rtemp.x = r->x + r->w; rtemp.x < r_bounds->x + r_bounds->w; rtemp.x++)
	{
		if (!CheckSolidTile(&rtemp, &colorValue, 1))
			break;
	}
	r->w += rtemp.x - (r->x + r->w);
}

/*
 * Check if a rectangle is all of the same color. If needSameColor is
 * set to non-zero, then also check that its color equals to the
 * *colorPtr value. The result is 1 if the test is successfull, and in
 * that case new color will be stored in *colorPtr.
 */

int TightEncoder::CheckSolidTile(FB_RECT *r, CARD32 *colorPtr, int needSameColor)
{
	CARD32 *fb_ptr;
	CARD32 colorValue;
	int dx, dy;

	fb_ptr
			= &(screenEncoder.screenBuffer.data)[r->y * screenEncoder.screenBuffer.getWidth() + r->x];

	colorValue = *fb_ptr;
	if (needSameColor && colorValue != *colorPtr)
		return 0;

	/* Check the first row. */
	for (dx = 0; dx < r->w; dx++)
	{
		if (colorValue != fb_ptr[dx])
			return 0;
	}

	/* Check other rows -- memcmp() does it faster. */
	for (dy = 1; dy < r->h; dy++)
	{
		if (memcmp(fb_ptr, &fb_ptr[dy * screenEncoder.screenBuffer.getWidth()], r->w * sizeof(CARD32)) != 0)
			return 0;
	}

	*colorPtr = colorValue;
	return 1;
}

int TightEncoder::SendRectSimple(OutputStream& out, FB_RECT *r)
{
	ScreenEncoder::CL_SLOT *cl = &(screenEncoder.slot);
	int maxBeforeSize, maxAfterSize;
	int maxRectSize, maxRectWidth;
	int subrectMaxWidth, subrectMaxHeight;
	FB_RECT sr;

	maxRectSize = tightConf[compressLevel].maxRectSize;
	maxRectWidth = tightConf[compressLevel].maxRectWidth;

	maxBeforeSize = maxRectSize * (cl->format.bits_pixel / 8);
	maxAfterSize = maxBeforeSize + (maxBeforeSize + 99) / 100 + 12;

	if (tightBeforeBufSize < maxBeforeSize)
	{
		tightBeforeBufSize = maxBeforeSize;
		if (tightBeforeBuf == NULL)
			tightBeforeBuf = (u8*)malloc(tightBeforeBufSize);
		else
			tightBeforeBuf = (u8*)realloc(tightBeforeBuf, tightBeforeBufSize);
	}

	if (tightAfterBufSize < maxAfterSize)
	{
		tightAfterBufSize = maxAfterSize;
		if (tightAfterBuf == NULL)
			tightAfterBuf = (u8*)malloc(tightAfterBufSize);
		else
			tightAfterBuf = (u8*)realloc(tightAfterBuf, tightAfterBufSize);
	}

	if (tightBeforeBuf == NULL || tightAfterBuf == NULL)
		return 0;

	if (r->w > maxRectWidth || r->w * r->h > maxRectSize)
	{
		subrectMaxWidth = (r->w > maxRectWidth) ? maxRectWidth : r->w;
		subrectMaxHeight = maxRectSize / subrectMaxWidth;

		for (sr.y = r->y; sr.y < r->y + r->h; sr.y += subrectMaxHeight)
		{
			for (sr.x = r->x; sr.x < r->x + r->w; sr.x += maxRectWidth)
			{
				sr.w = (sr.x - r->x + maxRectWidth < r->w) ? maxRectWidth
						: r->x + r->w - sr.x;
				sr.h
						= (sr.y - r->y + subrectMaxHeight < r->h) ? subrectMaxHeight
								: r->y + r->h - sr.y;
				if (!SendSubrect(out, &sr))
					return 0;
			}
		}
	}
	else
	{
		if (!SendSubrect(out, r))
			return 0;
	}

	return 1;
}

int TightEncoder::SendSubrect(OutputStream& out, FB_RECT *r)
{
	ScreenEncoder::CL_SLOT *cl = &(screenEncoder.slot);
	int success = 0;

	SendTightHeader(out, r);

	if (screenEncoder.screenBuffer.screenInfo.getBitsPerPixel() == 8)
	{
		screenEncoder.screenBuffer.transfunc8(tightBeforeBuf, r,
				screenEncoder.screenBuffer.table8);
	}
	else
	{
		screenEncoder.screenBuffer.transfunc_null(tightBeforeBuf, r, NULL);

	}

	paletteMaxColors = r->w * r->h
			/ tightConf[compressLevel].idxMaxColorsDivisor;
	if (paletteMaxColors < 2 && r->w * r->h
			>= tightConf[compressLevel].monoMinRectSize)
	{
		paletteMaxColors = 2;
	}

	if (cl->format.bits_pixel == 8)
	{
		FillPalette8(r->w * r->h);
	}
	else
	{
		FillPalette32(r->w * r->h);
	}

	switch (paletteNumColors)
	{
	case 0:
		/* Truecolor image */
		if (qualityLevel != -1 && DetectSmoothImage(&cl->format, r))
		{
			success = SendJpegRect(out, r, tightConf[qualityLevel].jpegQuality);
		}
		else
		{
			success = SendFullColorRect(out, r->w, r->h);
		}
		break;
	case 1:
		/* Solid rectangle */
		DDSS_VERBOSE("TightEnc")<<"Found only one color in palette!!!"<<std::endl;
		SendSolidRect(out);
		success = 1;
		break;
	case 2:
		/* Two-color rectangle */
		success = SendMonoRect(out, r->w, r->h);
		break;
	default:
		/* Up to 256 different colors */
		if (paletteNumColors > 96 && qualityLevel != -1 && qualityLevel <= 3
				&& DetectSmoothImage(&cl->format, r) )
		{
			success = SendJpegRect(out, r, tightConf[qualityLevel].jpegQuality);
		}
		else
		{
			//DDSS_VERBOSE("TightEnc")<<"Sending Indexed Rect!!!!"<<std::endl;
			success = SendIndexedRect(out, r->w, r->h);
		}
	}
	return success;
}

void TightEncoder::SendTightHeader(OutputStream& out, FB_RECT *r)
{
	CARD8 rect_hdr[12];
	//DDSS_VERBOSE("TightEnc")<<"SendTightHeader : "<<(*r)<<std::endl;
	r->enc = RFB_ENCODING_TIGHT;
	RFBHelper::put_rect_header(rect_hdr, r);
	out.write(rect_hdr, 12);
}

/*
 * Subencoding implementations.
 */

void TightEncoder::SendSolidRect(OutputStream& out)
{
	ScreenEncoder::CL_SLOT *cl = &(screenEncoder.slot);
	CARD8 buf[5];
	int len;

	if (usePixelFormat24)
	{
		Pack24(tightBeforeBuf, 1);
		len = 3;
	}
	else
	{
		len = 1;
	}

	buf[0] = RFB_TIGHT_FILL;
	memcpy(&buf[1], tightBeforeBuf, len);
	if(len == 3 && tightBeforeBuf[1] == 102 && tightBeforeBuf[2] == 0)
	{
		std::ostream& os = (std::cout<<"Fill Color (Weird): "<<std::endl);
		Dump::hexDump(os,tightBeforeBuf,3);
	}
	out.write(buf, 1 + len);
}

int TightEncoder::SendMonoRect(OutputStream& out, int w, int h)
{

	ScreenEncoder::CL_SLOT* cl = &screenEncoder.slot;
	CARD8 buf[11];
	int streamId = 1;
	int paletteLen, dataLen;

	/* Prepare tight encoding header. */
	dataLen = (w + 7) / 8;
	dataLen *= h;

	buf[0] = RFB_TIGHT_EXPLICIT_FILTER | (streamId << 4);
	buf[1] = RFB_TIGHT_FILTER_PALETTE;
	buf[2] = 1; /* number of colors - 1 */

	/* Prepare palette, convert image. */
	switch (cl->format.bits_pixel)
	{

	case 32:
		EncodeMonoRect32((CARD8 *)tightBeforeBuf, w, h);

		((CARD32 *)tightAfterBuf)[0] = monoBackground;
		((CARD32 *)tightAfterBuf)[1] = monoForeground;
		if (usePixelFormat24)
		{
			Pack24(tightAfterBuf, 2);
			paletteLen = 6;
		}
		else
			paletteLen = 8;

		memcpy(&buf[3], tightAfterBuf, paletteLen);
		out.write(buf, 3 + paletteLen);
		break;

	case 16:
		EncodeMonoRect16((CARD8 *)tightBeforeBuf, w, h);

		((CARD16 *)tightAfterBuf)[0] = (CARD16)monoBackground;
		((CARD16 *)tightAfterBuf)[1] = (CARD16)monoForeground;

		memcpy(&buf[3], tightAfterBuf, 4);
		out.write(buf, 7);
		break;

	default:
		EncodeMonoRect8((CARD8 *)tightBeforeBuf, w, h);

		buf[3] = (CARD8)monoBackground;
		buf[4] = (CARD8)monoForeground;
		out.write(buf, 5);
	}

	return CompressData(out, streamId, dataLen,
			tightConf[compressLevel].monoZlibLevel, Z_DEFAULT_STRATEGY);
}

int TightEncoder::SendIndexedRect(OutputStream& out, int w, int h)
{

	ScreenEncoder::CL_SLOT* cl = &(screenEncoder.slot);
	char buf[3 + 256*4];
	int streamId = 2;
	int i, entryLen;

	buf[0] = RFB_TIGHT_EXPLICIT_FILTER | (streamId << 4);
	buf[1] = RFB_TIGHT_FILTER_PALETTE;
	DDSS_VERBOSE("TightEnc")<<"TightEncoder::SendIndexedRect("<<w<<"X"<<h
			<<") :: paletteNumColors : "<<paletteNumColors<<std::endl;
	//DDSS_DEBUG_EXIT();
	buf[2] = (CARD8)(paletteNumColors - 1);

	/* Prepare palette, convert image. */
	switch (cl->format.bits_pixel)
	{

	case 32:
		EncodeIndexedRect32((CARD8 *)tightBeforeBuf, w * h);

		for (i = 0; i < paletteNumColors; i++)
		{
			((CARD32 *)tightAfterBuf)[i]
					= palette.entry[i].listNode->rgb;
		}
		if (usePixelFormat24)
		{
			Pack24(tightAfterBuf, paletteNumColors);
			entryLen = 3;
		}
		else
			entryLen = 4;

		memcpy(&buf[3], tightAfterBuf, paletteNumColors * entryLen);
		out.write(buf, 3 + paletteNumColors * entryLen);
		break;

	case 16:
		EncodeIndexedRect16((CARD8 *)tightBeforeBuf, w * h);

		for (i = 0; i < paletteNumColors; i++)
		{
			((CARD16 *)tightAfterBuf)[i]
					= (CARD16)palette.entry[i].listNode->rgb;
		}

		memcpy(&buf[3], tightAfterBuf, paletteNumColors * 2);
		out.write(buf, 3 + paletteNumColors * 2);
		break;

	default:
		return 0; /* should never happen */
	}

	return CompressData(out, streamId, w * h,
			tightConf[compressLevel].idxZlibLevel, Z_DEFAULT_STRATEGY);
}

int TightEncoder::SendFullColorRect(OutputStream& out, int w, int h)
{
	ScreenEncoder::CL_SLOT* cl = &(screenEncoder.slot);
	CARD8 *buf[1];
	int streamId = 0;
	int len;

	buf[0] = 0; /* stream id = 0, no flushing, no filter */
	out.write(buf, 1);

	if (usePixelFormat24)
	{
		Pack24(tightBeforeBuf, w * h);
		len = 3;
	}
	else
		len = cl->format.bits_pixel / 8;

	
	return CompressData(out, streamId, w * h * len,
			tightConf[compressLevel].rawZlibLevel, Z_DEFAULT_STRATEGY);
}
size_t TightEncoder::estimateCompressedSize(size_t inSize)
{
	return (size_t)((double)inSize * 1.1) + 12;
}
size_t TightEncoder::compressBuffer(const void* inBuffer, size_t inSize,
		void* outBuffer, size_t outSize, int level)
{
	//DSS_DEBUG("Entering:    Compressor::compress");
	//now compress the data
	uLongf destSize = (uLongf)outSize;

	int result = (int)compress2((Bytef*)outBuffer, (uLongf*)&destSize,
			(const Bytef*)inBuffer, (uLongf)inSize, level);

	if (result != Z_OK)
	{
		if (result == Z_MEM_ERROR)
		{
			//fprintf(stderr,"Compress Failed!!!!Memory Error!!!\n");
		}
		else if (result == Z_BUF_ERROR)
		{
			//std::cerr<<"Not enough room in buffer . out size : "<<destSize<<std::endl;
			//fprintf(stderr,"Compress Failed!!!!Buffer Error!!!Not enough room in buffer. required size=%d bytes\n",destSize);
		}
		else if (result == Z_STREAM_ERROR)
		{
			//fprintf(stderr,"Compress Failed!!!! Stream Error!!!\n");
		}
		//DSS_DEBUG("Leaving:        Compressor::compress");
		return 0;
	}
	else
	{
		//DDSS_VERBOSE("TightEnc")<<"         In Length : "<<inSize<<" --> Compressed Length : "<<destSize<<std::endl;
		//fprintf(stdout,"compressed %d bytes to %d bytes!!!",inSize,destSize);
		//DSS_DEBUG("Leaving:        Compressor::compress");
		return (size_t)destSize;
	}
}
int TightEncoder::CompressDataOneShot(OutputStream& out, int dataLen,
		int zlibLevel)
{
	size_t outSize = estimateCompressedSize(dataLen);
	unsigned char* outBuf = new unsigned char[outSize];
	ScopedArray<unsigned char> outBufPtr(outBuf);
	size_t destSize = this->compressBuffer(tightBeforeBuf, dataLen, outBuf,
			outSize, zlibLevel);
	CARD8 buf[3];
	int len_bytes = 0;

	size_t compressedLen = destSize;
	buf[len_bytes++] = compressedLen & 0x7F;

	if (compressedLen > 0x7F)
	{
		buf[len_bytes-1] |= 0x80;
		buf[len_bytes++] = compressedLen >> 7 & 0x7F;
		if (compressedLen > 0x3FFF)
		{
			buf[len_bytes-1] |= 0x80;
			buf[len_bytes++] = compressedLen >> 14 & 0xFF;
		}
	}
	out.write(buf, len_bytes);
	out.write(outBuf, destSize);
	return 1;
}
int TightEncoder::CompressData(OutputStream& out, int streamId, int dataLen,
		int zlibLevel, int zlibStrategy)
{

	ScreenEncoder::CL_SLOT *cl = &(screenEncoder.slot);
	z_streamp pz;
	int err;

	if (dataLen < RFB_TIGHT_MIN_TO_COMPRESS)
	{
		out.write(tightBeforeBuf, dataLen);
		return 1;
	}
	else if (this->flashClient)
	{
		return CompressDataOneShot(out, dataLen, zlibLevel);
	}

	pz = &cl->zs_struct[streamId];

	/* Initialize compression stream if needed. */
	if (!cl->zs_active[streamId])
	{
		pz->zalloc = Z_NULL;
		pz->zfree = Z_NULL;
		pz->opaque = Z_NULL;

		err = deflateInit2(pz, zlibLevel, Z_DEFLATED, MAX_WBITS, MAX_MEM_LEVEL,
				zlibStrategy);
		if (err != Z_OK)
			return 0;

		cl->zs_active[streamId] = 1;
		cl->zs_level[streamId] = zlibLevel;
	}

	/* Prepare buffer pointers. */
	pz->next_in = (Bytef *)tightBeforeBuf;
	pz->avail_in = dataLen;
	pz->next_out = (Bytef *)tightAfterBuf;
	pz->avail_out = tightAfterBufSize;

	/* Change compression parameters if needed. */
	if (zlibLevel != cl->zs_level[streamId])
	{
		if (deflateParams(pz, zlibLevel, zlibStrategy) != Z_OK)
		{
			return 0;
		}
		cl->zs_level[streamId] = zlibLevel;
	}

	/* Actual compression. */
	if (deflate(pz, Z_SYNC_FLUSH) != Z_OK || pz->avail_in != 0 || pz->avail_out
			== 0)
	{
		return 0;
	}

	SendCompressedData(out, tightAfterBufSize - pz->avail_out);
	return 1;
}

void TightEncoder::SendCompressedData(OutputStream& out, int compressedLen)
{
	CARD8 buf[3];
	int len_bytes = 0;

	buf[len_bytes++] = compressedLen & 0x7F;
	if (compressedLen > 0x7F)
	{
		buf[len_bytes-1] |= 0x80;
		buf[len_bytes++] = compressedLen >> 7 & 0x7F;
		if (compressedLen > 0x3FFF)
		{
			buf[len_bytes-1] |= 0x80;
			buf[len_bytes++] = compressedLen >> 14 & 0xFF;
		}
	}
	out.write(buf, len_bytes);
	out.write(tightAfterBuf, compressedLen);
}

/*
 * Code to determine how many different colors are used in a rectangle.
 */

void TightEncoder::FillPalette8(int count)
{
	//DDSS_VERBOSE("TightEnc")<<"FillPalette8 called!!!"<<std::endl;
	CARD8 *data = (CARD8 *)tightBeforeBuf;
	CARD8 c0, c1;
	int i, n0, n1;

	paletteNumColors = 0;

	c0 = data[0];
	for (i = 1; i < count && data[i] == c0; i++)
		;
	if (i == count)
	{
		DDSS_VERBOSE("TightEnc")<<"SOLID RECTANGLE FOUND with color 0x"<<std::hex<<(((u32)c0) & 0xFF)<<std::dec<<std::endl;
		paletteNumColors = 1;
		return; /* Solid rectangle */
	}

	if (paletteMaxColors < 2)
		return;

	n0 = i;
	c1 = data[i];
	n1 = 0;
	for (i++; i < count; i++)
	{
		if (data[i] == c0)
		{
			n0++;
		}
		else if (data[i] == c1)
		{
			n1++;
		}
		else
			break;
	}
	if (i == count)
	{
		if (n0 > n1)
		{
			monoBackground = (CARD32)c0;
			monoForeground = (CARD32)c1;
		}
		else
		{
			monoBackground = (CARD32)c1;
			monoForeground = (CARD32)c0;
		}
		paletteNumColors = 2; /* Two colors */
	}
}

void TightEncoder::FillPalette16(int count)
{
	int bpp = 16;

	CARD16 *data = (CARD16 *)tightBeforeBuf;
	CARD16 c0, c1, ci;
	int i, n0, n1, ni;

	c0 = data[0];
	for (i = 1; i < count && data[i] == c0; i++)
		;
	if (i >= count)
	{
		paletteNumColors = 1; /* Solid rectangle */
		return;
	}

	if (paletteMaxColors < 2)
	{
		paletteNumColors = 0; /* Full-color encoding preferred */
		return;
	}

	n0 = i;
	c1 = data[i];
	n1 = 0;
	for (i++; i < count; i++)
	{
		ci = data[i];
		if (ci == c0)
		{
			n0++;
		}
		else if (ci == c1)
		{
			n1++;
		}
		else
			break;
	}
	if (i >= count)
	{
		if (n0 > n1)
		{
			monoBackground = (CARD32)c0;
			monoForeground = (CARD32)c1;
		}
		else
		{
			monoBackground = (CARD32)c1;
			monoForeground = (CARD32)c0;
		}
		paletteNumColors = 2; /* Two colors */
		return;
	}

	PaletteReset();
	PaletteInsert(c0, (CARD32)n0, bpp);
	PaletteInsert(c1, (CARD32)n1, bpp);

	ni = 1;
	for (i++; i < count; i++)
	{
		if (data[i] == ci)
		{
			ni++;
		}
		else
		{
			if (!PaletteInsert(ci, (CARD32)ni, bpp))
				return;
			ci = data[i];
			ni = 1;
		}
	}
	PaletteInsert(ci, (CARD32)ni, bpp);
}
void TightEncoder::FillPalette32(int count)
{
	int bpp = 32;
	CARD32 *data = (CARD32 *)tightBeforeBuf;

	CARD32 c0, c1, ci;
	int i, n0, n1, ni;

	c0 = data[0];
	for (i = 1; i < count && data[i] == c0; i++)
		;
	if (i >= count)
	{
		paletteNumColors = 1; /* Solid rectangle */
		return;
	}

	if (paletteMaxColors < 2)
	{
		paletteNumColors = 0; /* Full-color encoding preferred */
		return;
	}

	n0 = i;
	c1 = data[i];
	n1 = 0;
	for (i++; i < count; i++)
	{
		ci = data[i];
		if (ci == c0)
		{
			n0++;
		}
		else if (ci == c1)
		{
			n1++;
		}
		else
			break;
	}
	if (i >= count)
	{
		if (n0 > n1)
		{
			monoBackground = (CARD32)c0;
			monoForeground = (CARD32)c1;
		}
		else
		{
			monoBackground = (CARD32)c1;
			monoForeground = (CARD32)c0;
		}
		paletteNumColors = 2; /* Two colors */
		return;
	}

	PaletteReset();
	PaletteInsert(c0, (CARD32)n0, bpp);
	PaletteInsert(c1, (CARD32)n1, bpp);

	ni = 1;
	for (i++; i < count; i++)
	{
		if (data[i] == ci)
		{
			ni++;
		}
		else
		{
			if (!PaletteInsert(ci, (CARD32)ni, bpp))
				return;
			ci = data[i];
			ni = 1;
		}
	}
	PaletteInsert(ci, (CARD32)ni, bpp);

}

void TightEncoder::PaletteReset(void)
{
	paletteNumColors = 0;
	memset(palette.hash, 0, 256 * sizeof(COLOR_LIST *));
}

int TightEncoder::PaletteInsert(CARD32 rgb, int numPixels, int bpp)
{
	COLOR_LIST *pnode;
	COLOR_LIST *prev_pnode= NULL;
	int hash_key, idx, new_idx, count;

	hash_key = (bpp == 16) ? HASH_FUNC16(rgb) : HASH_FUNC32(rgb);

	pnode = palette.hash[hash_key];

	while (pnode != NULL)
	{
		if (pnode->rgb == rgb)
		{
			/* Such palette entry already exists. */
			new_idx = idx = pnode->idx;
			count = palette.entry[idx].numPixels + numPixels;
			if (new_idx && palette.entry[new_idx-1].numPixels < count)
			{
				do
				{
					palette.entry[new_idx] = palette.entry[new_idx-1];
					palette.entry[new_idx].listNode->idx = new_idx;
					new_idx--;
				} while (new_idx && palette.entry[new_idx-1].numPixels < count);
				palette.entry[new_idx].listNode = pnode;
				pnode->idx = new_idx;
			}
			palette.entry[new_idx].numPixels = count;
			return paletteNumColors;
		}
		prev_pnode = pnode;
		pnode = pnode->next;
	}

	/* Check if palette is full. */
	if (paletteNumColors == 256 || paletteNumColors == paletteMaxColors)
	{
		paletteNumColors = 0;
		return 0;
	}

	/* Move palette entries with lesser pixel counts. */
	for (idx = paletteNumColors; idx > 0 && palette.entry[idx-1].numPixels
			< numPixels; idx--)
	{
		palette.entry[idx] = palette.entry[idx-1];
		palette.entry[idx].listNode->idx = idx;
	}

	/* Add new palette entry into the freed slot. */
	pnode = &palette.list[paletteNumColors];
	if (prev_pnode != NULL)
	{
		prev_pnode->next = pnode;
	}
	else
	{
		palette.hash[hash_key] = pnode;
	}
	pnode->next = NULL;
	pnode->idx = idx;
	pnode->rgb = rgb;
	palette.entry[idx].listNode = pnode;
	palette.entry[idx].numPixels = numPixels;

	return (++paletteNumColors);
}

/*
 * Convert 32-bit color samples into 24-bit colors, in place. Source
 * data should be in the server's pixel format which is RGB888.
 */

void TightEncoder::Pack24(CARD8 *buf, int count)
{

	CARD32 *buf32;
	CARD32 pix;
	buf32 = (CARD32 *)buf;

	/*if (count == 1)
	{
		return;
	}*/

	while (count--)
	{
		pix = *buf32++;
		*buf++ = (CARD8)((pix >> 16) & 0xFF);
		*buf++ = (CARD8)((pix >> 8) & 0xFF);
		*buf++ = (CARD8)(pix & 0xFF);
	}

}

/*
 * Converting truecolor samples into palette indices.
 */

void TightEncoder::EncodeIndexedRect16(CARD8 *buf, int count)
{
	COLOR_LIST *pnode;
	CARD16 *src;
	CARD16 rgb;
	int rep = 0;

	src = (CARD16 *) buf;

	while (count--)
	{
		rgb = *src++;
		while (count && *src == rgb)
		{
			rep++, src++, count--;
		}
		pnode = palette.hash[HASH_FUNC16(rgb)];
		while (pnode != NULL)
		{
			if ((CARD16)pnode->rgb == rgb)
			{
				*buf++ = (CARD8)pnode->idx;
				while (rep)
				{
					*buf++ = (CARD8)pnode->idx;
					rep--;
				}
				break;
			}
			pnode = pnode->next;
		}
	}
}
void TightEncoder::EncodeIndexedRect32(CARD8 *buf, int count)
{
	COLOR_LIST *pnode;
	CARD32 *src;
	CARD32 rgb;
	int rep = 0;

	src = (CARD32 *) buf;

	while (count--)
	{
		rgb = *src++;
		while (count && *src == rgb)
		{
			rep++, src++, count--;
		}
		pnode = palette.hash[HASH_FUNC32(rgb)];
		while (pnode != NULL)
		{
			if ((CARD32)pnode->rgb == rgb)
			{
				*buf++ = (CARD8)pnode->idx;
				while (rep)
				{
					*buf++ = (CARD8)pnode->idx;
					rep--;
				}
				break;
			}
			pnode = pnode->next;
		}
	}
}

void TightEncoder::EncodeMonoRect8(CARD8 *buf, int w, int h)
{
	CARD8 *ptr;
	CARD8 bg;
	unsigned int value, mask;
	int aligned_width;
	int x, y, bg_bits;

	ptr = (CARD8 *) buf;
	bg = (CARD8) monoBackground;
	aligned_width = w - w % 8;

	for (y = 0; y < h; y++)
	{
		for (x = 0; x < aligned_width; x += 8)
		{
			for (bg_bits = 0; bg_bits < 8; bg_bits++)
			{
				if (*ptr++ != bg)
					break;
			}
			if (bg_bits == 8)
			{
				*buf++ = 0;
				continue;
			}
			mask = 0x80 >> bg_bits;
			value = mask;
			for (bg_bits++; bg_bits < 8; bg_bits++)
			{
				mask >>= 1;
				if (*ptr++ != bg)
				{
					value |= mask;
				}
			}
			*buf++ = (CARD8)value;
		}

		mask = 0x80;
		value = 0;
		if (x >= w)
			continue;

		for (; x < w; x++)
		{
			if (*ptr++ != bg)
			{
				value |= mask;
			}
			mask >>= 1;
		}
		*buf++ = (CARD8)value;
	}
}
void TightEncoder::EncodeMonoRect16(CARD8 *buf, int w, int h)
{
	CARD16 *ptr;
	CARD16 bg;
	unsigned int value, mask;
	int aligned_width;
	int x, y, bg_bits;

	ptr = (CARD16 *) buf;
	bg = (CARD16) monoBackground;
	aligned_width = w - w % 8;

	for (y = 0; y < h; y++)
	{
		for (x = 0; x < aligned_width; x += 8)
		{
			for (bg_bits = 0; bg_bits < 8; bg_bits++)
			{
				if (*ptr++ != bg)
					break;
			}
			if (bg_bits == 8)
			{
				*buf++ = 0;
				continue;
			}
			mask = 0x80 >> bg_bits;
			value = mask;
			for (bg_bits++; bg_bits < 8; bg_bits++)
			{
				mask >>= 1;
				if (*ptr++ != bg)
				{
					value |= mask;
				}
			}
			*buf++ = (CARD8)value;
		}

		mask = 0x80;
		value = 0;
		if (x >= w)
			continue;

		for (; x < w; x++)
		{
			if (*ptr++ != bg)
			{
				value |= mask;
			}
			mask >>= 1;
		}
		*buf++ = (CARD8)value;
	}
}
void TightEncoder::EncodeMonoRect32(CARD8 *buf, int w, int h)
{
	CARD32 *ptr;
	CARD32 bg;
	unsigned int value, mask;
	int aligned_width;
	int x, y, bg_bits;

	ptr = (CARD32 *) buf;
	bg = (CARD32) monoBackground;
	aligned_width = w - w % 8;

	for (y = 0; y < h; y++)
	{
		for (x = 0; x < aligned_width; x += 8)
		{
			for (bg_bits = 0; bg_bits < 8; bg_bits++)
			{
				if (*ptr++ != bg)
					break;
			}
			if (bg_bits == 8)
			{
				*buf++ = 0;
				continue;
			}
			mask = 0x80 >> bg_bits;
			value = mask;
			for (bg_bits++; bg_bits < 8; bg_bits++)
			{
				mask >>= 1;
				if (*ptr++ != bg)
				{
					value |= mask;
				}
			}
			*buf++ = (CARD8)value;
		}

		mask = 0x80;
		value = 0;
		if (x >= w)
			continue;

		for (; x < w; x++)
		{
			if (*ptr++ != bg)
			{
				value |= mask;
			}
			mask >>= 1;
		}
		*buf++ = (CARD8)value;
	}
}

int TightEncoder::DetectSmoothImage(RFB_PIXEL_FORMAT *fmt, FB_RECT *r)
{
	unsigned long avgError;

	if (qualityLevel == -1 || fmt->bits_pixel == 8 || r->w < DETECT_MIN_WIDTH
			|| r->h < DETECT_MIN_HEIGHT || r->w * r->h < JPEG_MIN_RECT_SIZE)
	{
		return 0;
	}

	avgError = DetectSmoothImage24(fmt, r);
	return (avgError < tightConf[qualityLevel].jpegThreshold24);
}

unsigned long TightEncoder::DetectSmoothImage24(RFB_PIXEL_FORMAT *fmt,
		FB_RECT *r)
{
	int x, y, d, dx, c;
	int diffStat[256];
	int pixelCount = 0;
	int pix, left[3];
	unsigned long avgError;

	memset(diffStat, 0, 256*sizeof(int));

	y = 0, x = 0;
	while (y < r->h && x < r->w)
	{
		for (d = 0; d < r->h - y && d < r->w - x - DETECT_SUBROW_WIDTH; d++)
		{
			pix = (screenEncoder.screenBuffer.data)[(r->y + y + d) * screenEncoder.screenBuffer.getWidth() + (r->x + x + d)];
			left[0] = pix >> 16 & 0xFF;
			left[1] = pix >> 8 & 0xFF;
			left[2] = pix & 0xFF;
			for (dx = 1; dx <= DETECT_SUBROW_WIDTH; dx++)
			{
				pix = (screenEncoder.screenBuffer.data)[(r->y + y + d) * screenEncoder.screenBuffer.getWidth() + (r->x + x + d + dx)];
				diffStat[abs((pix >> 16 & 0xFF) - left[0])]++;
				diffStat[abs((pix >> 8 & 0xFF) - left[1])]++;
				diffStat[abs((pix & 0xFF) - left[2])]++;
				left[0] = pix >> 16 & 0xFF;
				left[1] = pix >> 8 & 0xFF;
				left[2] = pix & 0xFF;
				pixelCount++;
			}
		}
		if (r->w > r->h)
		{
			x += r->h;
			y = 0;
		}
		else
		{
			x = 0;
			y += r->w;
		}
	}

	if (diffStat[0] * 33 / pixelCount >= 95)
		return 0;

	avgError = 0;
	for (c = 1; c < 8; c++)
	{
		avgError += (unsigned long)diffStat[c] * (unsigned long)(c * c);
		if (diffStat[c] == 0 || diffStat[c] > diffStat[c-1] * 2)
			return 0;
	}
	for (; c < 256; c++)
	{
		avgError += (unsigned long)diffStat[c] * (unsigned long)(c * c);
	}
	avgError /= (pixelCount * 3 - diffStat[0]);

	return avgError;
}

int TightEncoder::SendJpegRect(OutputStream& out, FB_RECT *r, int quality)
{
	CARD8 buf[1];
	struct jpeg_compress_struct cinfo;
	struct jpeg_error_mgr jerr;
	CARD8 *srcBuf;
	JSAMPROW rowPointer[1];
	int dy;

	srcBuf = (CARD8 *)malloc(r->w * 3);
	if (srcBuf == NULL)
		return 0;

	rowPointer[0] = srcBuf;

	cinfo.err = jpeg_std_error(&jerr);
	jpeg_create_compress(&cinfo);

	cinfo.image_width = r->w;
	cinfo.image_height = r->h;
	cinfo.input_components = 3;
	cinfo.in_color_space = JCS_RGB;

	jpeg_set_defaults(&cinfo);
	jpeg_set_quality(&cinfo, quality, TRUE);

	JpegSetDstManager(&cinfo);

	jpeg_start_compress(&cinfo, TRUE);

	for (dy = 0; dy < r->h; dy++)
	{
		PrepareRowForJpeg(srcBuf, r->x, r->y + dy, r->w);
		jpeg_write_scanlines(&cinfo, rowPointer, 1);
		if (jpegError)
			break;
	}

	if (!jpegError)
		jpeg_finish_compress(&cinfo);

	jpeg_destroy_compress(&cinfo);
	free(srcBuf);

	if (jpegError)
		return 0;

	buf[0] = RFB_TIGHT_JPEG;
	out.write(buf, 1);
	SendCompressedData(out, jpegDstDataLen);
	return 1;
}

void TightEncoder::PrepareRowForJpeg(CARD8 *dst, int x, int y, int count)
{
	CARD32 *fb_ptr;
	CARD32 pix;

	fb_ptr
			= &(screenEncoder.screenBuffer.data)[y * screenEncoder.screenBuffer.getWidth() + x];

	while (count--)
	{
		pix = *fb_ptr++;
		*dst++ = (CARD8)(pix >> 16);
		*dst++ = (CARD8)(pix >> 8);
		*dst++ = (CARD8)pix;
	}
}

/*
 * Destination manager implementation for JPEG library.
 */

void TightEncoder::JpegInitDestination(j_compress_ptr cinfo)
{

	TightEncoder* ptr = (TightEncoder*)(cinfo->client_data);
	ptr->jpegError = FALSE;
	ptr->jpegDstManager.next_output_byte = (JOCTET *)ptr->tightAfterBuf;
	ptr->jpegDstManager.free_in_buffer = (size_t)ptr->tightAfterBufSize;
}

boolean TightEncoder::jpegEmptyOutputBuffer(j_compress_ptr cinfo)
{

	TightEncoder* ptr = (TightEncoder*)(cinfo->client_data);
	ptr->jpegError = TRUE;
	ptr->jpegDstManager.next_output_byte = (JOCTET *)ptr->tightAfterBuf;
	ptr->jpegDstManager.free_in_buffer = (size_t)ptr->tightAfterBufSize;

	return TRUE;
}

void TightEncoder::JpegTermDestination(j_compress_ptr cinfo)
{
	TightEncoder* ptr = (TightEncoder*)(cinfo->client_data);

	ptr->jpegDstDataLen = ptr->tightAfterBufSize
			- ptr->jpegDstManager.free_in_buffer;
}

void TightEncoder::JpegSetDstManager(j_compress_ptr cinfo)
{
	cinfo->client_data = this;
	jpegDstManager.init_destination = JpegInitDestination;
	jpegDstManager.empty_output_buffer = (jpegEmptyOutputBuffer);
	jpegDstManager.term_destination = (JpegTermDestination);
	cinfo->dest = &jpegDstManager;
}

}
