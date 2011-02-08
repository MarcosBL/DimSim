#include "TightDecoder2.h"
#include "corona.h"
namespace dm
{
	TightDecoder2::TightDecoder2(ScreenDecoder2& decoder) : screenDecoder(decoder)
	{
		init();
		
	}
	TightDecoder2::~TightDecoder2()
	{
		cleanup();
	}
	void TightDecoder2::init()
	{
		memset(s_zstream_active, 0, 4 * sizeof(int));
		s_reset_streams = 0;
		s_stream_id = 0;
		s_filter_id = 0;
		s_num_colors = 0;
		memset(s_palette, 0, 256);
		s_compressed_size = s_uncompressed_size = 0;
	}
	void TightDecoder2::cleanup()
	{
		memset(s_zstream_active, 0, 4 * sizeof(int));
		s_reset_streams = 0;
		s_stream_id = 0;
		s_filter_id = 0;
		s_num_colors = 0;
		memset(s_palette, 0, 256);
		s_compressed_size = s_uncompressed_size = 0;
	}
	TightDecoder2::Result TightDecoder2::decode(InputStream& in, ScreenRectangle& r)
	{
		return decodeControlByte(in,r);
	}
	TightDecoder2::Result TightDecoder2::decodeControlByte(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TightEncoder2")<<"Reading control byte...!!!"<<std::endl;
		int stream_id;
		s_reset_streams = 0;
	
		u8 controlByte = 0;
		/* Compression control byte */
		if (!in.readByte(&controlByte))
		{
			DDSS_INPUT_ERROR("TD2", in)<<"Failed to read control byte!"
					<<std::endl;
			return TDR_INCOMPLETE;
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
						DDSS_WARN("TD2")<< "controlByte::inflateEnd("
								<<stream_id <<") failed:"<<s_zstream[stream_id].msg
								<<std::endl;
					}
					else
					{
						DDSS_WARN("TD2")<< "controlByte::inflateEnd("
								<<stream_id <<") failed!"<<std::endl;
					}
				}
				else
				{
					DDSS_VERBOSE("TD2")<<"INFLATE END CALLED FOR : "<<stream_id<<std::endl;
				}
				s_zstream_active[stream_id] = 0;
			}
		}
		controlByte &= 0xF0; // clear bits 3..0 
		
		if (controlByte == RFB_TIGHT_FILL)
		{
			//fill encoding
			return this->decodeTightFill(in, s_rect);
		}
		else if (controlByte == RFB_TIGHT_JPEG)
		{
			//jpeg compression
			return this->decodeTightJPEG(in, s_rect);
		}
		else if (controlByte > RFB_TIGHT_MAX_SUBENCODING)
		{
			DDSS_INPUT_ERROR("TD2", in)
					<<"Encoding exceeds Max Sub encoding!!!"<<std::endl;
			return TDR_FAILED;
		}
		else
		{
			//basic compression

			/* "basic" compression */
			s_stream_id = (controlByte >> 4) & 0x03;
			if (controlByte & RFB_TIGHT_EXPLICIT_FILTER)
			{
				return this->decodeTightFilter(in, s_rect);
			}
			else
			{
				s_filter_id = RFB_TIGHT_FILTER_COPY;
				s_uncompressed_size = this->calculateSize(s_rect.w,s_rect.h);
				
				if (s_uncompressed_size < RFB_TIGHT_MIN_TO_COMPRESS)
				{
					return this->decodeTightRaw(in, s_rect);
				}
				else
				{
					s_compressed_size = 0;
					u8 bread = 0;
					Result res = this->readCompactLen(in, (u32&)s_compressed_size, bread);
					
					if ( res == TDR_OK)
					{
						return this->decodeTightCompressed(in, s_rect);
					}
					else 
					{
						DDSS_INPUT_ERROR("TD2", in)
								<<"Failed to read the compressed byte length!"
								<<std::endl;
						return res;
					}
				}
			}
		}
		return TightDecoder2::TDR_FAILED;
	}
	TightDecoder2::Result TightDecoder2::decodeTightFill(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TightEncoder2")<<"tight fill"<<std::endl;
		DDSS_VERBOSE("TD2")<<" FILL ENCODING"<<std::endl;
		u32 color = 0;
	
		unsigned char octets[3];
		if(this->screenDecoder.screenBuffer.screenInfo.getBitsPerPixel() == 8)
		{
			octets[0] = 0xFF;
			if(in.readByte(&octets[0]) == 1)
			{
				
				color = ColorHelper::getInstance()->lookup(octets[0]);
				screenDecoder.screenBuffer.tight_draw_filled_rect(color, s_rect);
				return TDR_OK;
			}
			else
			{
				DDSS_INPUT_ERROR("TD2",in)<<"Failed to read 8 bit fill color"<<std::endl;
				return TDR_INCOMPLETE;
			}
			//DDSS_VERBOSE("TD2")<<" 8 bit fill : "<<std::hex<<(((u32)octets[0]) & 0xFF)<<std::dec<<" / 0x"<<std::hex<<color<<std::dec<<" for "<<s_rect<<std::endl;
		}
		else if (in.read(octets, 3) != 3)
		{
			DDSS_INPUT_ERROR("TD2", in)<<"failed to read fill color"
					<<std::endl;
			return TDR_INCOMPLETE;
		}
		else
		{
			u32 oc0 = octets[0];
			u32 oc1 = octets[1];
			u32 oc2 = octets[2];
			/* Note: cur_slot->readbuf is unsigned char[]. */
			color = MAKERGB24PIXEL32(oc0,oc1,oc2);
			screenDecoder.screenBuffer.tight_draw_filled_rect(color, s_rect);
		}

		return TightDecoder2::TDR_OK;
	}
	TightDecoder2::Result TightDecoder2::decodeTightFilter(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TD2")<<" FILTER ENCODING"<<std::endl;
		u8 filterId = 0xFF;
		if (!in.readByte(&filterId))
		{
			DDSS_INPUT_ERROR("TD2", in)<<"Failed to read filter id"
					<<std::endl;
			return TDR_INCOMPLETE;
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
						Result result = this->readCompactLen(in, (u32&)this->s_compressed_size,
								bread);
						if (result == TDR_OK)
						{
							return this->decodeTightCompressed(in, s_rect);
						}
						else
						{
							DDSS_INPUT_ERROR("TD2", in)
									<<"Failed to read the compact length for ZLIB data"
									<<std::endl;
							return result;
						}
	
					}
				}
				else
				{
					DDSS_ERROR("TD2")<<"Unknown filter : "
							<<(s32)s_filter_id<<std::endl;
					return TDR_FAILED;
				}
			}
		}
		return TightDecoder2::TDR_FAILED;
	}
	TightDecoder2::Result TightDecoder2::decodeTightNumColors(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TD2")<<" NUM COLORS"<<std::endl;
		u8 numColors = 0;
		if (!in.readByte(&numColors))
		{
			DDSS_INPUT_ERROR("TD2", in)<<"Failed to get num colors!"
					<<std::endl;
			return TDR_INCOMPLETE;
		}
		else
		{
			s_num_colors = numColors + 1;
			DDSS_VERBOSE("TD2")<<" Num Colors : "<<s_num_colors<<std::endl;
			return this->decodeTightPalette(in, s_rect);
		}
		return TightDecoder2::TDR_FAILED;
	}
	TightDecoder2::Result TightDecoder2::decodeTightPalette(InputStream& in, ScreenRectangle& s_rect)
	{

		DDSS_VERBOSE("TD2")<<" PALETTE ENCODING"<<std::endl;
		int i, row_size;
	
		if(screenDecoder.screenBuffer.screenInfo.getBitsPerPixel() == 8)
		{
			DDSS_VERBOSE("TD2")<<"Reading a 8 bit two color pallete! Num Colors = "<<s_num_colors<<std::endl;
			u8 pal[2];
			if(in.read(pal,2) == 2)
			{
				s_palette[0] = ColorHelper::getInstance()->lookup(pal[0]);
				s_palette[1] = ColorHelper::getInstance()->lookup(pal[1]);
				DDSS_VERBOSE("TD2")<<"2COLOR/8BIT Palette : 0x"<<(std::hex)<<s_palette[0]<<(std::dec)<<" 0x"<<(std::hex)<<s_palette[1]<<(std::dec)<<std::endl;
			}
			else
			{
				DDSS_WARNING("TD2")<<"Failed to read two color palette!!!"<<std::endl;
				return TDR_INCOMPLETE;
			}
		}
		else
		{
			DDSS_VERBOSE("TD2")<<"Reading a 24 bit color palette!!!!"<<std::endl;
			ByteBuffer buf(s_num_colors * 3);
			if (in.read(buf.getData(), buf.getLength()) != buf.getLength())
			{
				DDSS_INPUT_ERROR("TD2", in)
						<<"Failed to read the palette entries!!!"<<std::endl;
				return TDR_INCOMPLETE;
			}
		
			for (i = 0; i < s_num_colors; i++)
			{
				s_palette[i] = (buf.getByte(i*3) << 16 | buf.getByte(i*3+1) << 8
						| buf.getByte(i*3+2));
			}
		}
		row_size = (s_num_colors <= 2) ? (s_rect.w + 7) / 8 : s_rect.w;
		DDSS_VERBOSE("TD2")<<"ROW SIZE : "<<row_size<<std::endl;
	
		s_uncompressed_size = s_rect.h * row_size;
		if (s_uncompressed_size < RFB_TIGHT_MIN_TO_COMPRESS)
		{
			return this->decodeTightIndexed(in, s_rect);
		}
		else
		{
			u8 bread = 0;
			Result result = this->readCompactLen(in, (u32&)s_compressed_size, bread);
			if (result == TDR_OK)
			{
				return this->decodeTightCompressed(in, s_rect);
			}
			else
			{
				DDSS_INPUT_ERROR("TD2", in)
						<<"Failed to read indexed compressed byte count!"
						<<std::endl;
				return result;
			}
	
		}
		return TightDecoder2::TDR_FAILED;
	}
	TightDecoder2::Result TightDecoder2::decodeTightRaw(InputStream& in, ScreenRectangle& s_rect)
	{
	
		DDSS_VERBOSE("TD2")<<" RAW ENCODING ("<<s_uncompressed_size<<" bytes)"
				<<std::endl;
		ByteBuffer buf(s_uncompressed_size);
		if (in.read(buf.getData(), buf.getLength()) != buf.getLength())
		{
			DDSS_INPUT_ERROR("TD2", in)<<"Failed to read "
					<<s_uncompressed_size<<" bytes of raw data!"<<std::endl;
			return TDR_INCOMPLETE;
		}
		else
		{
			this->screenDecoder.screenBuffer.tight_draw_truecolor_data(
					buf.getData(), s_rect);
			return TDR_OK;
		}
		return TightDecoder2::TDR_FAILED;
	}
	TightDecoder2::Result TightDecoder2::decodeTightIndexed(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TD2")<<" INDEXED ("<<s_uncompressed_size<<" bytes)"
				<<std::endl;
		ByteBuffer buf(s_uncompressed_size);
		if (in.read(buf.getData(), buf.getLength()) != buf.getLength())
		{
			DDSS_INPUT_ERROR("TD2", in)<<"Failed to read "
					<<s_uncompressed_size<<" bytes of indexed data!"<<std::endl;
			return TDR_INCOMPLETE;
		}
		else
		{
	
			this->screenDecoder.screenBuffer.tight_draw_indexed_data(buf.getData(),
					s_rect, s_num_colors, s_palette);
			
			return TDR_OK;
		}
	}
	TightDecoder2::Result TightDecoder2::readCompactLen(InputStream& in,  u32& len, u8& numBytesRead)
	{

		DDSS_VERBOSE("TightEncoder2")<<"tight compact len"<<std::endl;
		numBytesRead = 0;
		len = 0;
		
		len = 0;
		u8 b = 0xFF;
		if (!in.readByte(&b))
		{
			return TDR_INCOMPLETE;
		}
		else
		{
			//DDSS_VERBOSE("TightEncoder2")<<"compactLen(0) : 0x"<<std::hex<<(u32)b<<std::dec<<std::endl;
			numBytesRead = 1;
		}
		len = (int)b & 0x7F;
	
		if (b & 0x80)
		{
			if (!in.readByte(&b))
			{
				return TDR_INCOMPLETE;
			}
			else
			{
				//DDSS_VERBOSE("TightEncoder2")<<"compactLen(1) : 0x"<<std::hex<<(u32)b<<std::dec<<std::endl;
				numBytesRead = 2;
			}
			len |= ((int)b & 0x7F) << 7;
			if (b & 0x80)
			{
				if (!in.readByte(&b))
				{
					return TDR_INCOMPLETE;
				}
				else
				{
					//DDSS_VERBOSE("TightEncoder2")<<"compactLen(2) : 0x"<<std::hex<<(u32)b<<std::dec<<std::endl;
					numBytesRead = 3;
				}
				len |= ((int)b & 0xFF) << 14;
			}
		}
		return TightDecoder2::TDR_OK;
	}
	TightDecoder2::Result TightDecoder2::decodeTightCompressed(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_INPUT_VERBOSE("TightEncoder2",in)<<" decodeTightCompressed("<<s_stream_id<<") - ZLIB COMPRESSED DATA ( compressed : "
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
			DDSS_INPUT_ERROR("TD2", in)<<"Failed to read compressed data "
					<<s_compressed_size<<" bytes!"<<std::endl;
			return TDR_INCOMPLETE;
		}
	
		/* Initialize compression stream if needed */
	
		DDSS_VERBOSE("TD2")<<"ZStream id : "<<(s16)s_stream_id<<std::endl;
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
					DDSS_ERROR("TD2")
							<<"decodeTightCompressed::inflateInit() failed: "
							<< zs->msg<<std::endl;
				}
				else
				{
					DDSS_ERROR("TD2")
							<<"decodeTightCompressed::inflateInit() failed. "
							<<std::endl;
				}
				return TDR_FAILED;
			}
			else
			{
				DDSS_VERBOSE("TD2")<<"decodeTightCompressed::inflateInit("<<s_stream_id<<")"<<std::endl;
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
	
		DDSS_VERBOSE("SD2")<<"ZStream ["<<s_stream_id<<"] ("<<zs<<") inflate() called!"<<std::endl;
		err = inflate(zs, Z_SYNC_FLUSH);
		if (err != Z_OK && err != Z_STREAM_END)
		{
			if (zs->msg != NULL)
			{
				DDSS_ERROR("TD2")<<"inflate() failed : "<<zs->msg
						<<std::endl;
			}
			else
			{
				DDSS_ERROR("TD2")<<"inflate() failed (null error msg)"<<std::endl;
			}
			return TDR_FAILED;
		}
		DDSS_VERBOSE("SD2")<<"ZStream ["<<s_stream_id<<"] inflate() ended!"<<std::endl;
	
		if (zs->avail_out > 0)
		{
			DDSS_WARN("TD2")
					<<"Decompressed data size is less than expected"<<std::endl;
		}
	
		/* Draw the data on the framebuffer */
	
		if (s_filter_id == RFB_TIGHT_FILTER_PALETTE)
		{
			DDSS_VERBOSE("TightEncoder2")<<"Applying paletted data!"<<std::endl;
			screenDecoder.screenBuffer.tight_draw_indexed_data(buf.getData(),
					s_rect, s_num_colors, s_palette);
		}
		else if (s_filter_id == RFB_TIGHT_FILTER_GRADIENT)
		{
			DDSS_VERBOSE("TightEncoder2")<<"Applying gradient data!"<<std::endl;
			screenDecoder.screenBuffer.tight_draw_gradient_data(buf.getData(),
					s_rect);
		}
		else
		{
			DDSS_VERBOSE("TightEncoder2")<<"Applying true color data!"<<std::endl;
			screenDecoder.screenBuffer.tight_draw_truecolor_data(buf.getData(),
					s_rect);
		}
		return TightDecoder2::TDR_OK;
	}
	TightDecoder2::Result TightDecoder2::decodeTightJPEG(InputStream& in, ScreenRectangle& s_rect)
	{
		DDSS_VERBOSE("TD2")<<" JPEG RECT "<<s_rect<<std::endl;
		u32 jpegLen = 0;
		u8 bread = 0;
		Result result = readCompactLen(in, jpegLen, bread);
		if (result == TDR_OK)
		{
			DDSS_VERBOSE("TD2")<<"JPEG DATA LENGTH : "<<jpegLen<<" bytes!"
					<<std::endl;
			//
			// TODO: add jpeg decoder coder here (mohaps)
			// decode jpeg and apply to screen buffer
			//
			
			ByteBuffer jpegBuf(jpegLen);
			if (in.read(jpegBuf.getData(),jpegLen) != jpegLen)
			{
				DDSS_INPUT_ERROR("TD2", in)<<"Failed to read : "<<jpegLen
						<<" bytes!"<<std::endl;
				return TDR_INCOMPLETE;
			}
			else
			{
												
									 
				if (decompressJPEGBuffer(jpegBuf.getData(), jpegBuf.getLength(),
						s_rect))
				{
					DDSS_VERBOSE("TD2")<<"[JPEG-SUCCESS] "<<jpegLen<<" JPEG DATA DECODED!!!"<<std::endl;
					
					return TDR_OK;
				}
				else
				{
					DDSS_ERROR("TD2")<<"[JPEG-FAIL] Failed to decompress jpeg data : "
							<<s_rect<<std::endl;
					return TDR_FAILED;
				}
				
			}
		}
		else
		{
			DDSS_INPUT_ERROR("TD2", in)<<"Failed to read jpeg length"
					<<std::endl;
			return result;
		}
		return TightDecoder2::TDR_FAILED;
	}
	TightDecoder2::Result TightDecoder2::decompressJPEGBuffer(const u8* buf, size_t len, ScreenRectangle& r)
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
					//DDSS_VERBOSE("TightEncoder2")<<"Converted Image : "<<img2->getWidth()<<"X"<<img2->getHeight()<<std::endl;
					//ScopedPointer<corona::Image> imgPtr2(img2);
					u8* pixelPtr = (u8*)img2->getPixels();
					size_t offset = 0;
					for(int y = 0; y < img2->getHeight(); y++)
					{
						for(int x = 0; x < img2->getWidth(); x++)
						{
							//DDSS_VERBOSE("TightEncoder2")<<"IMG("<<img2->getWidth()<<"X"<<img2->getHeight()<<") / RECT : ("<<x<<","<<y<<")"<<std::endl;
							//DDSS_VERBOSE("TightEncoder2")<<"Setting pixel : "<<(r.x + x)<<" / "<<(r.y + y)<<" from offset : "<<offset<<std::endl;
							CARD32* pixel = (CARD32*)(screenDecoder.screenBuffer.getBuffer(r.x + x, r.y + y));
							u8 r = pixelPtr[offset++];
							u8 g = pixelPtr[offset++];
							u8 b = pixelPtr[offset++];
							//u8 a = pixelPtr[offset+3];
							*pixel = MAKERGB24PIXEL32(r,g,b);
						}
					}
				}
				return TightDecoder2::TDR_OK;
				
			}
		}
		return TightDecoder2::TDR_FAILED;
	}
	
	void TightDecoder2::resetStreams(void)
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
						DDSS_WARN("TD2")<<"inflateEnd() failed:"
								<<s_zstream[stream_id].msg<<std::endl;
					}
					else
					{
						DDSS_WARN("TD2")<<"inflateEnd() failed!"
								<<std::endl;
					}
				}
				s_zstream_active[stream_id] = 0;
				s_reset_streams |= (1 << stream_id);
			}
		}
	}
	size_t TightDecoder2::calculateSize(size_t w, size_t h)
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
}
