#include "TightDecoder.h"
#include "corona.h"
namespace dm
{
	
	/*
	 * JPEG source manager functions for JPEG decompression in Tight decoder.
	 */
	
	void TightDecoder::JpegInitSource(j_decompress_ptr cinfo)
	{
	
		DDSS_VERBOSE("TightDecoder")<<"JPEGInitsource"<<std::endl;
		TightDecoder* decoder = (TightDecoder*)(cinfo->client_data);
		decoder->jpegError = 0;
		DDSS_VERBOSE("TightDecoder")<<"JPEGInitsource end "<<std::endl;
	}
	
	boolean TightDecoder::JpegFillInputBuffer(j_decompress_ptr cinfo)
	{
		DDSS_VERBOSE("TightDecoder")<<"JPEGFillInputBuffer"<<std::endl;
		TightDecoder* decoder = (TightDecoder*)(cinfo->client_data);
		decoder->jpegError = 1;
		decoder->jpegSrcManager.bytes_in_buffer = decoder->jpegBufferLen;
		decoder->jpegSrcManager.next_input_byte = (JOCTET *)decoder->jpegBufferPtr;
	
		return TRUE;
	}
	
	void TightDecoder::JpegSkipInputData(j_decompress_ptr cinfo, long num_bytes)
	{
		DDSS_VERBOSE("TightDecoder")<<"JpegSkipInputData("<<num_bytes<<")"<<std::endl;
		TightDecoder* decoder = (TightDecoder*)(cinfo->client_data);
		if (num_bytes < 0 || num_bytes > (long)(decoder->jpegSrcManager.bytes_in_buffer))
		{
			decoder->jpegError = 1;
			decoder->jpegSrcManager.bytes_in_buffer = decoder->jpegBufferLen;
			decoder->jpegSrcManager.next_input_byte = (JOCTET *)decoder->jpegBufferPtr;
		}
		else
		{
			decoder->jpegSrcManager.next_input_byte += (size_t) num_bytes;
			decoder->jpegSrcManager.bytes_in_buffer -= (size_t) num_bytes;
		}
	}
	
	void TightDecoder::JpegTermSource(j_decompress_ptr cinfo)
	{
		/* No work necessary here. */
		DDSS_VERBOSE("TightDecoder")<<"JPEGTermSource"<<std::endl;
	}
	
	void TightDecoder::JpegSetSrcManager(j_decompress_ptr cinfo,
			CARD8 *compressedData, int compressedLen)
	{
		DDSS_VERBOSE("TightDecoder")<<"JpegSetSrcManager"<<std::endl;
		TightDecoder* decoder = (TightDecoder*)(cinfo->client_data);
		decoder->jpegBufferPtr = (JOCTET *)compressedData;
		decoder->jpegBufferLen = (size_t)compressedLen;
	
		decoder->jpegSrcManager.init_source = JpegInitSource;
		decoder->jpegSrcManager.fill_input_buffer = JpegFillInputBuffer;
		decoder->jpegSrcManager.skip_input_data = JpegSkipInputData;
		decoder->jpegSrcManager.resync_to_restart = jpeg_resync_to_restart;
		decoder->jpegSrcManager.term_source = JpegTermSource;
		decoder->jpegSrcManager.next_input_byte = decoder->jpegBufferPtr;
		decoder->jpegSrcManager.bytes_in_buffer = decoder->jpegBufferLen;
	
		cinfo->src = &(decoder->jpegSrcManager);
		DDSS_VERBOSE("TightDecoder")<<"[END]JpegSetSrcManager"<<std::endl;
	}
	
	void TightDecoder::resetStreams(void)
	{
		int stream_id;
	
		for (stream_id = 0; stream_id < 4; stream_id++)
		{
			if (s_zstream_active[stream_id])
			{
				if (inflateEnd(&s_zstream[stream_id]) != Z_OK)
				{
					if (s_zstream[stream_id].msg != NULL)
					{
						DDSS_WARN("TightDecoder")<<"inflateEnd() failed:"
								<<s_zstream[stream_id].msg<<std::endl;
					}
					else
					{
						DDSS_WARN("TightDecoder")<<"inflateEnd() failed!"
								<<std::endl;
					}
				}
				s_zstream_active[stream_id] = 0;
				s_reset_streams |= (1 << stream_id);
			}
		}
	}
	TightDecoder::TightDecoder(ScreenDecoder& decoder) :
		screenDecoder(decoder)
	{
		init();
	
	}
	TightDecoder::~TightDecoder()
	{
	
	}
	void TightDecoder::init()
	{
		memset(s_zstream_active, 0, 4 * sizeof(int));
		s_reset_streams = 0;
		s_stream_id = 0;
		s_filter_id = 0;
		s_num_colors = 0;
		memset(s_palette, 0, 256);
		s_compressed_size = s_uncompressed_size = 0;
	}
	void TightDecoder::cleanup()
	{
		memset(s_zstream_active, 0, 4 * sizeof(int));
		s_reset_streams = 0;
		s_stream_id = 0;
		s_filter_id = 0;
		s_num_colors = 0;
		memset(s_palette, 0, 256);
		s_compressed_size = s_uncompressed_size = 0;
	}
	bool TightDecoder::decode(InputStream& in, ScreenRectangle& r)
	{
		DDSS_VERBOSE("TightDecoder")<<"Decode() called!!!"<<std::endl;
		return decodeControlByte(in, r);
	}
	
	bool TightDecoder::decodeControlByte(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TightDecoder")<<"decodeControlByte()!!!"<<std::endl;
	
		int stream_id;
		s_reset_streams = 0;
	
		u8 controlByte = 0;
		/* Compression control byte */
		if (!in.readByte(&controlByte))
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to read control byte!"
					<<std::endl;
		}
	
		/* Flush zlib streams if we are told by the server to do so */
		for (stream_id = 0; stream_id < 4; stream_id++)
		{
			if ((controlByte & (1 < stream_id)) && s_zstream_active[stream_id])
			{
				if (inflateEnd(&s_zstream[stream_id]) != Z_OK)
				{
					if (s_zstream[stream_id].msg != NULL)
					{
						DDSS_WARN("TightDecoder")<< "controlByte::inflateEnd("
								<<stream_id <<") failed:"<<s_zstream[stream_id].msg
								<<std::endl;
					}
					else
					{
						DDSS_WARN("TightDecoder")<< "controlByte::inflateEnd("
								<<stream_id <<") failed!"<<std::endl;
					}
				}
				s_zstream_active[stream_id] = 0;
			}
		}
		controlByte &= 0xF0; /* clear bits 3..0 */
	
		if (controlByte == RFB_TIGHT_FILL)
		{
			return this->decodeTightFill(in, s_rect);
		}
		else if (controlByte == RFB_TIGHT_JPEG)
		{
			return this->decodeTightJPEG(in, s_rect);
		}
		else if (controlByte > RFB_TIGHT_MAX_SUBENCODING)
		{
			DDSS_INPUT_ERROR("TightDecoder", in)
					<<"Encoding exceeds Max Sub encoding!!!"<<std::endl;
			return false;
		}
		else
		{
			/* "basic" compression */
			s_stream_id = (controlByte >> 4) & 0x03;
			if (controlByte & RFB_TIGHT_EXPLICIT_FILTER)
			{
				return this->decodeTightFilter(in, s_rect);
			}
			else
			{
				s_filter_id = RFB_TIGHT_FILTER_COPY;
				s_uncompressed_size = this->calculateSize(s_rect.w,s_rect.h);//s_rect.w * s_rect.h * 3;
				/*if(s_rect.w == 32 && s_rect.h == 32)
				{
					std::cout<<"Tight (32X32) rect : uncompressed : "<<s_uncompressed_size<<" bytes!"<<std::endl;
					
				}*/
				if (s_uncompressed_size < RFB_TIGHT_MIN_TO_COMPRESS)
				{
					return this->decodeTightRaw(in, s_rect);
				}
				else
				{
					s_compressed_size = 0;
					u8 bread = 0;
					if (this->readCompactLen(in, (u32&)s_compressed_size, bread))
					{
						//std::cout<<"Compressed Len : "<<s_compressed_size<<" bytes!"<<std::endl;
						return this->decodeTightCompressed(in, s_rect);
					}
					else
					{
						DDSS_INPUT_ERROR("TightDecoder", in)
								<<"Failed to read the compressed byte length!"
								<<std::endl;
						return false;
					}
				}
			}
		}
		return false;
	}
	bool TightDecoder::decodeTightFill(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TightDecoder")<<" FILL ENCODING"<<std::endl;
		u32 color = 0;
	
		unsigned char octets[3];
		if(this->screenDecoder.screenBuffer.screenInfo.getBitsPerPixel() == 8)
		{
			octets[0] = 0xFF;
			if(in.readByte(&octets[0]) == 1)
			{
				//octets[0] = 0xFF;
				if(octets[0] == 0xFF)
				{
					DDSS_VERBOSE("TightDecoder")<<"GOT FILL COLOR (0x"<<std::hex
									<<((u32)octets[0])<<std::dec<<") / Color : 0x"
									<<std::hex<<ColorHelper::getInstance()->lookup(octets[0])<<std::dec<<" for "<<s_rect<<std::endl;
				}
				//color = ColorHelper::getInstance()->calculateColor(octets[0]);
				color = ColorHelper::getInstance()->lookup(octets[0]);
				screenDecoder.screenBuffer.tight_draw_filled_rect(color, s_rect);
				screenDecoder.rectComplete(s_rect);
				return true;
			}
			else
			{
				DDSS_INPUT_ERROR("TightDecoder",in)<<"Failed to read 8 bit fill color"<<std::endl;
				return false;
			}
			//DDSS_VERBOSE("TightDecoder")<<" 8 bit fill : "<<std::hex<<(((u32)octets[0]) & 0xFF)<<std::dec<<" / 0x"<<std::hex<<color<<std::dec<<" for "<<s_rect<<std::endl;
		}
		else if (in.read(octets, 3) != 3)
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"failed to read fill color"
					<<std::endl;
			return false;
		}
		else
		{
			char tmp[128];
			sprintf(tmp, "%u %u %u", octets[0], octets[1], octets[2]);

			u32 oc0 = octets[0];
			u32 oc1 = octets[1];
			u32 oc2 = octets[2];
			/* Note: cur_slot->readbuf is unsigned char[]. */
			color = MAKERGB24PIXEL32(oc0,oc1,oc2);//(oc2 << 16 | oc1 << 8 | oc0 );
			screenDecoder.screenBuffer.tight_draw_filled_rect(color, s_rect);
			screenDecoder.rectComplete(s_rect);
		}
	
		//color = (octets[2] << 16 | octets[1] << 8 | octets[0]);
		
		return true;
	}
	bool TightDecoder::decodeTightFilter(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TightDecoder")<<" FILTER ENCODING"<<std::endl;
		u8 filterId = 0xFF;
		if (!in.readByte(&filterId))
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to read filter id"
					<<std::endl;
		}
		else
		{
			s_filter_id = filterId;
			if (s_filter_id == RFB_TIGHT_FILTER_PALETTE)
			{
				return decodeTightNumColors(in, s_rect);
			}
			else
			{
				if (s_filter_id == RFB_TIGHT_FILTER_COPY || s_filter_id
						== RFB_TIGHT_FILTER_GRADIENT)
				{
	
					s_uncompressed_size = this->calculateSize(s_rect.w, s_rect.h);
					
					if (s_uncompressed_size < RFB_TIGHT_MIN_TO_COMPRESS)
					{
						return this->decodeTightRaw(in, s_rect);
					}
					else
					{
						u8 bread = 0;
						if (this->readCompactLen(in, (u32&)this->s_compressed_size,
								bread))
						{
							return this->decodeTightCompressed(in, s_rect);
						}
						else
						{
							DDSS_INPUT_ERROR("TightDecoder", in)
									<<"Failed to read the compact length for ZLIB data"
									<<std::endl;
							return false;
						}
	
					}
				}
				else
				{
					DDSS_ERROR("TightDecoder")<<"Unknown filter : "
							<<(s32)s_filter_id<<std::endl;
					return false;
				}
			}
		}
		return false;
	}
	bool TightDecoder::decodeTightNumColors(InputStream& in, ScreenRectangle& s_rect)
	{
	
		DDSS_VERBOSE("TightDecoder")<<" NUM COLORS"<<std::endl;
		u8 numColors = 0;
		if (!in.readByte(&numColors))
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to get num colors!"
					<<std::endl;
			return false;
		}
		else
		{
			s_num_colors = numColors + 1;
			DDSS_VERBOSE("TightDecoder")<<" Num Colors : "<<s_num_colors<<std::endl;
			return this->decodeTightPalette(in, s_rect);
		}
		return false;
	}
	bool TightDecoder::decodeTightPalette(InputStream& in, ScreenRectangle& s_rect)
	{
	
		DDSS_VERBOSE("TightDecoder")<<" PALETTE ENCODING"<<std::endl;
		int i, row_size;
	
		if(screenDecoder.screenBuffer.screenInfo.getBitsPerPixel() == 8)
		{
			DDSS_VERBOSE("TightDecoder")<<"Reading a 8 bit two color pallete! Num Colors = "<<s_num_colors<<std::endl;
			u8 pal[2];
			if(in.read(pal,2) == 2)
			{
				s_palette[0] = ColorHelper::getInstance()->lookup(pal[0]);
				s_palette[1] = ColorHelper::getInstance()->lookup(pal[1]);
				DDSS_VERBOSE("TightDecoder")<<"2COLOR/8BIT Palette : 0x"<<(std::hex)<<s_palette[0]<<(std::dec)<<" 0x"<<(std::hex)<<s_palette[1]<<(std::dec)<<std::endl;
			}
			else
			{
				DDSS_WARNING("TightDecoder")<<"Failed to read two color palette!!!"<<std::endl;
			}
		}
		else
		{
			DDSS_VERBOSE("TightDecoder")<<"Reading a 24 bit color palette!!!!"<<std::endl;
			ByteBuffer buf(s_num_colors * 3);
			if (in.read(buf.getData(), buf.getLength()) != buf.getLength())
			{
				DDSS_INPUT_ERROR("TightDecoder", in)
						<<"Failed to read the palette entries!!!"<<std::endl;
				return false;
			}
		
			for (i = 0; i < s_num_colors; i++)
			{
				s_palette[i] = (buf.getByte(i*3) << 16 | buf.getByte(i*3+1) << 8
						| buf.getByte(i*3+2));
			}
		}
		row_size = (s_num_colors <= 2) ? (s_rect.w + 7) / 8 : s_rect.w;
		DDSS_VERBOSE("TightDecoder")<<"ROW SIZE : "<<row_size<<std::endl;
	
		s_uncompressed_size = s_rect.h * row_size;
		if (s_uncompressed_size < RFB_TIGHT_MIN_TO_COMPRESS)
		{
			return this->decodeTightIndexed(in, s_rect);
		}
		else
		{
			u8 bread = 0;
			if (this->readCompactLen(in, (u32&)s_compressed_size, bread))
			{
				return this->decodeTightCompressed(in, s_rect);
			}
			else
			{
				DDSS_INPUT_ERROR("TightDecoder", in)
						<<"Failed to read indexed compressed byte count!"
						<<std::endl;
				return false;
			}
	
		}
	}
	bool TightDecoder::decodeTightRaw(InputStream& in, ScreenRectangle& s_rect)
	{
	
		DDSS_VERBOSE("TightDecoder")<<" RAW ENCODING"<<std::endl;
	
		DDSS_VERBOSE("TightDecoder")<<" INDEXED ("<<s_uncompressed_size<<" bytes)"
				<<std::endl;
		ByteBuffer buf(s_uncompressed_size);
		if (in.read(buf.getData(), buf.getLength()) != buf.getLength())
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to read "
					<<s_uncompressed_size<<" bytes of raw data!"<<std::endl;
			return false;
		}
		else
		{
			this->screenDecoder.screenBuffer.tight_draw_truecolor_data(
					buf.getData(), s_rect);
			screenDecoder.rectComplete(s_rect);
			return true;
		}
	}
	bool TightDecoder::decodeTightIndexed(InputStream& in, ScreenRectangle& s_rect)
	{
	
		DDSS_VERBOSE("TightDecoder")<<" INDEXED ("<<s_uncompressed_size<<" bytes)"
				<<std::endl;
		ByteBuffer buf(s_uncompressed_size);
		if (in.read(buf.getData(), buf.getLength()) != buf.getLength())
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to read "
					<<s_uncompressed_size<<" bytes of indexed data!"<<std::endl;
			return false;
		}
		else
		{
	
			this->screenDecoder.screenBuffer.tight_draw_indexed_data(buf.getData(),
					s_rect, s_num_colors, s_palette);
			
			screenDecoder.rectComplete(s_rect);
			return true;
		}
	
	}
	bool TightDecoder::readCompactLen(InputStream& in, u32& len, u8& numBytesRead)
	{
	
		numBytesRead = 0;
		len = 0;
		
		len = 0;
		u8 b = 0xFF;
		if (!in.readByte(&b))
		{
			return false;
		}
		else
		{
			//DDSS_VERBOSE("TightDecoder")<<"compactLen(0) : 0x"<<std::hex<<(u32)b<<std::dec<<std::endl;
			numBytesRead = 1;
		}
		len = (int)b & 0x7F;
	
		if (b & 0x80)
		{
			if (!in.readByte(&b))
			{
				return false;
			}
			else
			{
				//DDSS_VERBOSE("TightDecoder")<<"compactLen(1) : 0x"<<std::hex<<(u32)b<<std::dec<<std::endl;
				numBytesRead = 2;
			}
			len |= ((int)b & 0x7F) << 7;
			if (b & 0x80)
			{
				if (!in.readByte(&b))
				{
					return false;
				}
				else
				{
					//DDSS_VERBOSE("TightDecoder")<<"compactLen(2) : 0x"<<std::hex<<(u32)b<<std::dec<<std::endl;
					numBytesRead = 3;
				}
				len |= ((int)b & 0xFF) << 14;
			}
		}
		return true;
	}
	bool TightDecoder::decodeTightCompressed(InputStream& in,
			ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TightDecoder")<<" ZLIB COMPRESSED DATA ( compressed : "
				<< s_compressed_size<<" bytes) ( uncompressed : "
				<<s_uncompressed_size<<" bytes ) "<<s_rect<<std::endl;
		//
		//TODO: READ and INFLATE THE ZLIB DATA
		//
		z_streamp zs;
	
		int err;
	
		ByteBuffer inBuf(s_compressed_size);
	
		if (in.read(inBuf.getData(), inBuf.getLength()) != inBuf.getLength())
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to read compressed data "
					<<s_compressed_size<<" bytes!"<<std::endl;
			return false;
		}
	
		/* Initialize compression stream if needed */
	
		DDSS_VERBOSE("TightDecoder")<<"ZStream id : "<<(s16)s_stream_id<<std::endl;
		zs = &s_zstream[s_stream_id];
		if (!s_zstream_active[s_stream_id])
		{
			zs->zalloc = Z_NULL;
			zs->zfree = Z_NULL;
			zs->opaque = Z_NULL;
			err = inflateInit(zs);
			if (err != Z_OK)
			{
				if (zs->msg != NULL)
				{
					DDSS_ERROR("TightDecoder")
							<<"decodeTightCompressed::inflateInit() failed: "
							<< zs->msg<<std::endl;
				}
				else
				{
					DDSS_ERROR("TightDecoder")
							<<"decodeTightCompressed::inflateInit() failed. "
							<<std::endl;
				}
				return false;
			}
			s_zstream_active[s_stream_id] = 1;
		}
	
		/* Allocate a buffer to put decompressed data into */
	
		ByteBuffer buf(s_uncompressed_size);
	
		/* Decompress the data */
	
		zs->next_in = inBuf.getData();
		zs->avail_in = s_compressed_size;
		zs->next_out = buf.getData();
		zs->avail_out = s_uncompressed_size;
	
		err = inflate(zs, Z_SYNC_FLUSH);
		if (err != Z_OK && err != Z_STREAM_END)
		{
			if (zs->msg != NULL)
			{
				DDSS_ERROR("TightDecoder")<<"inflate() failed : "<<zs->msg
						<<std::endl;
			}
			else
			{
				DDSS_ERROR("TightDecoder")<<"inflate() failed"<<std::endl;
			}
			return false;
		}
	
		if (zs->avail_out > 0)
		{
			DDSS_WARN("TightDecoder")
					<<"Decompressed data size is less than expected"<<std::endl;
		}
	
		/* Draw the data on the framebuffer */
	
		if (s_filter_id == RFB_TIGHT_FILTER_PALETTE)
		{
			screenDecoder.screenBuffer.tight_draw_indexed_data(buf.getData(),
					s_rect, s_num_colors, s_palette);
		}
		else if (s_filter_id == RFB_TIGHT_FILTER_GRADIENT)
		{
			screenDecoder.screenBuffer.tight_draw_gradient_data(buf.getData(),
					s_rect);
		}
		else
		{
			screenDecoder.screenBuffer.tight_draw_truecolor_data(buf.getData(),
					s_rect);
		}
		screenDecoder.rectComplete(s_rect);
		return true;
	}

	size_t TightDecoder::calculateSize(size_t w, size_t h)
	{
		if(this->screenDecoder.screenBuffer.screenInfo.getBitsPerPixel() == 8)
		{
			return w*h;
		}
		else
		{
			return w*h*3;
		}
	}
	bool TightDecoder::decodeTightJPEG(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TightDecoder")<<" JPEG RECT "<<s_rect<<std::endl;
		u32 jpegLen = 0;
		u8 bread = 0;
		bool honorJpeg = true;
		if (readCompactLen(in, jpegLen, bread))
		{
			DDSS_VERBOSE("TightDecoder")<<"JPEG DATA LENGTH : "<<jpegLen<<" bytes!"
					<<std::endl;
			//
			// TODO: add jpeg decoder coder here (mohaps)
			// decode jpeg and apply to screen buffer
			//
			if(!honorJpeg)
			{ 
				in.skip(jpegLen); 
				screenDecoder.rectComplete(s_rect);
				return true;
			
			}
			ByteBuffer jpegBuf(jpegLen);
			if (in.read(jpegBuf.getData(),jpegLen) != jpegLen)
			{
				DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to read : "<<jpegLen
						<<" bytes!"<<std::endl;
				return false;
			}
			else
			{
												
									 
				if (decompressJPEGBuffer(jpegBuf.getData(), jpegBuf.getLength(),
						s_rect))
				{
					DDSS_VERBOSE("TightDecoder")<<"[JPEG-SUCCESS] "<<jpegLen<<" JPEG DATA DECODED!!!"<<std::endl;
					screenDecoder.rectComplete(s_rect);
										return true;
				}
				else
				{
					DDSS_ERROR("TightDecoder")<<"[JPEG-FAIL] Failed to decompress jpeg data : "
							<<s_rect<<std::endl;
					screenDecoder.rectComplete(s_rect);
					return true;
				}
				
			}
		}
		else
		{
			DDSS_INPUT_ERROR("TightDecoder", in)<<"Failed to read jpeg length"
					<<std::endl;
			return false;
		}
	}
	bool TightDecoder::decompressJPEGBuffer(const u8* buf, size_t len, ScreenRectangle& r)
	{
		corona::File* file = corona::CreateMemoryFile(buf,len);
		if(file)
		{
			ScopedPointer<corona::File> filePtr(file);
			corona::Image* img = corona::OpenImage(file,corona::FF_JPEG,corona::PF_R8G8B8);
			if(img)
			{
				ScopedPointer<corona::Image> imgPtr(img);
				corona::Image* img2 = img;//corona::ConvertImage(img, corona::PF_R8G8B8A8);
				if(img2)
				{
					//std::cout<<"Converted Image : "<<img2->getWidth()<<"X"<<img2->getHeight()<<std::endl;
					//ScopedPointer<corona::Image> imgPtr2(img2);
					u8* pixelPtr = (u8*)img2->getPixels();
					size_t offset = 0;
					for(int y = 0; y < img2->getHeight(); y++)
					{
						for(int x = 0; x < img2->getWidth(); x++)
						{
							//std::cout<<"IMG("<<img2->getWidth()<<"X"<<img2->getHeight()<<") / RECT : ("<<x<<","<<y<<")"<<std::endl;
							//std::cout<<"Setting pixel : "<<(r.x + x)<<" / "<<(r.y + y)<<" from offset : "<<offset<<std::endl;
							CARD32* pixel = (CARD32*)(screenDecoder.screenBuffer.getBuffer(r.x + x, r.y + y));
							u8 r = pixelPtr[offset++];
							u8 g = pixelPtr[offset++];
							u8 b = pixelPtr[offset++];
							//u8 a = pixelPtr[offset+3];
							*pixel = MAKERGB24PIXEL32(r,g,b);
						}
					}
				}
				return true;
				
			}
		}
		return false;
	}
	/*
	bool TightDecoder::decompressJPEGBuffer(const u8* buf, size_t len,
			ScreenRectangle& r) 
	{
		
		String id = "";
		dm::IDGenerator::generateClientId(id);
		String fileName = "etc/dat/dump-";
		fileName += id;
		fileName += ".jpg";
		
		std::ofstream out(fileName.c_str(),std::ios::binary | std::ios::out);
		out.write((const c8*)buf,len);
		out.flush();
		out.close();
		std::cout<<"Saved JPEG File to : "<<fileName<<std::endl;
	
		bool jpegHonor = true;
		if (jpegHonor) {
			struct jpeg_decompress_struct cinfo;
			struct jpeg_error_mgr jerr;
			memset(&jerr,0,sizeof(jerr));
			int compressedLen;
			CARD8 *compressedData;
			CARD32 *pixelPtr;
			JSAMPROW rowPointer[1];
			int dx, dy;
	
			compressedLen = len;
			compressedData = (u8*)buf;
	
			std::cout<<"decompressJPEGBuffer : "<<r<<" Buf : "<<buf<<" / len = "
					<<len <<std::endl;
			cinfo.err = jpeg_std_error(&jerr);
			jpeg_create_decompress(&cinfo);
			std::cout<<"Decompress created : "<<std::endl;
			cinfo.client_data = this;
			JpegSetSrcManager(&cinfo, compressedData, compressedLen);
	
			std::cout<<"reading header..."<<std::endl;
			int ret = jpeg_read_header(&cinfo, FALSE);
			if(ret == JPEG_HEADER_OK)
			{
				std::cout<<"success!"<<std::endl;
			}
			else if(ret == JPEG_HEADER_TABLES_ONLY)
			{
				std::cout<<"abbreviated image!!"<<std::endl;
			}
			
			std::cout<<"read header"<<ret<<std::endl;
			cinfo.out_color_space = JCS_RGB;
	
			DDSS_VERBOSE("TightDecoder")<<"decompressJPEGBuffer 1   : header : "
					<<cinfo.output_width<<"X" <<cinfo.output_height<<std::endl;
			jpeg_start_decompress(&cinfo);
			std::cout<<"decompressJPEGBuffer 2 : header : "<<cinfo.output_width<<"X"
					<<cinfo.output_height<<" comp count : "
					<<cinfo.output_components<<std::endl;
			if (cinfo.output_width != r.w || cinfo.output_height != r.h
					|| cinfo.output_components != 3) {
				std::cout<< "[JPEG-ERROR] Tight Encoding: Wrong JPEG data received"<<std::endl;
				jpeg_destroy_decompress(&cinfo);
				jpegError = 0;
				return false;
			}
			std::cout<<">>> ROW DECODE!!!"<<std::endl;
			ByteBuffer tmpBuf(r.w * 3);
			rowPointer[0] = (JSAMPROW)tmpBuf.getData();
			dy = 0;
			while (cinfo.output_scanline < cinfo.output_height) {
	
				std::cout<<"Scan Line : "<<cinfo.output_scanline<<std::endl;
				std::cout<<"Scan number : "<<cinfo.output_scan_number<<std::endl;
				
				jpeg_read_scanlines(&cinfo, rowPointer, 1);
				if (!jpegError) 
				{
					std::cout<<"Failed in read scanlines : "<<jpegError<<std::endl;
					break;
				} 
				else 
				{
					pixelPtr = (CARD32*)screenDecoder.screenBuffer.getBuffer(r.x,
							r.y);
					for (dx = 0; dx < r.w; dx++) {
						*pixelPtr++ = MAKERGB24PIXEL32(tmpBuf.getByte(dx*3),
								tmpBuf.getByte(dx*3+1), tmpBuf.getByte(dx*3+2));
					}
					dy++;
				}
			}
	
			if (!jpegError) {
				jpeg_finish_decompress(&cinfo);
			}
	
			jpeg_destroy_decompress(&cinfo);
			return (!jpegError);
	
		} else {
			return true;
		}
	}*/
}

