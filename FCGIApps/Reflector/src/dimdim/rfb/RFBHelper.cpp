#include "RFBHelper.h"

namespace dm
{
	void RFBHelper::buf_get_pixfmt(void *buf, RFB_PIXEL_FORMAT *format)
	{
	  CARD8 *bbuf = (CARD8*)buf;

	  memcpy(format, buf, SZ_RFB_PIXEL_FORMAT);
	  format->r_max = buf_get_CARD16(&bbuf[4]);
	  format->g_max = buf_get_CARD16(&bbuf[6]);
	  format->b_max = buf_get_CARD16(&bbuf[8]);
	}

	void RFBHelper::buf_put_pixfmt(void *buf, RFB_PIXEL_FORMAT *format)
	{
	  CARD8 *bbuf = (CARD8*)buf;

	  memcpy(buf, format, SZ_RFB_PIXEL_FORMAT);
	  buf_put_CARD16(&bbuf[4], format->r_max);
	  buf_put_CARD16(&bbuf[6], format->g_max);
	  buf_put_CARD16(&bbuf[8], format->b_max);
	}
	size_t RFBHelper::buf_put_len_and_string(void *buf, const String str)
	{
		CARD8* bbuf = (CARD8*)buf;
		size_t offset = 0;
		CARD32 slen = str.size();
		buf_put_CARD32(&bbuf[offset], str.size());
		if(slen > 0)
		{
			offset += 4;
			memcpy(&bbuf[offset], str.c_str(), slen);
		}
		return (4 + slen);
		
	}
	size_t RFBHelper::buf_get_len_and_string(void* buf, String& str)
	{
		str = "";
		CARD8* bbuf = (CARD8*)buf;
		size_t offset = 0;
		size_t slen = 0;
		slen = buf_get_CARD32(&bbuf[offset]);
		offset += 4;
		Logger::verbose()<<"RFBHelper::buf_get_len_and_string() - stringlen = "<<slen<<std::endl;
		
		
		if(slen > 0)
		{
			c8* sbuf = new c8[slen+1];
			memset(sbuf,0,slen+1);
			memcpy(sbuf,&bbuf[offset],slen);
			str = sbuf;
			delete[] sbuf;
			offset += slen;
		}
		return offset;
		
	}
	ColorHelper* ColorHelper::sInstance = 0;
	ColorHelper* ColorHelper::getInstance()
	{
		if(sInstance == 0)
		{
			sInstance = new ColorHelper();
		}
		return sInstance;
	}
	u32 ColorHelper::lookup(u8 val)
	{
		return colorTable[val];
	}

	u8 ColorHelper::reverseByte(u32 val)
	{
		//return this->byteTable[val];
		
		for(std::map<u32,u8>::iterator iter = reverseTable.begin(); iter != reverseTable.end(); iter++)
		{
			u32 mappedColor = iter->first;
			u8 byteValue = iter->second;
			if(val == mappedColor)
			{
				return byteValue;
			}
			else if(val < mappedColor)
			{
				return byteValue;
			}
		}
		return 0xFF;
	}
	ColorHelper::~ColorHelper(){}
	u32 ColorHelper::calculateColor(u8 val)
	{
		return colorTable[val];
	}
	ColorHelper::ColorHelper()
	{

		{
			u32 idx = 0;
			u32 val8[]={0, 36, 73, 109, 146, 182, 219, 255};
			u32 val4[]={0, 85, 170, 255};
			/*
			while(idx < 256)
			{
#define DDSS_BGR_233
#ifdef DDSS_BGR_233
				//233 masks
				u32 red = ((idx & 0xFF) & 0xE0) >> 5;
				u32 green = ((idx & 0xFF) & 0x1C) >> 2;
				u32 blue = (idx & 0xFF) & 0x03;
#else				
				//332 masks
				u32 blue = ((idx & 0xFF) & 0xC0) >> 6;
				u32 green = ((idx & 0xFF) & 0x38) >> 3;
				u32 red = (idx & 0xFF) & 0x07;
#endif				
				//calculate the normalized values
				u32 rv = val8[red];
				u32 gv = val8[green];
				u32 bv = val4[blue];

				u32 color = MAKERGB24PIXEL32(rv,gv,bv);
				std::cout<<"["<<idx<<"] = "<<(std::hex)<<color<<(std::dec)<<"(R="<<rv<<",G="<<gv<<",B="<<bv<<")"<<std::endl;
				colorTable[idx++] = color;
			}*/
			for(u32 b = 0; b <= 3; b++)
			{
				for(u32 g = 0; g <= 7; g++)
				{
					for(u32 r = 0; r <= 7; r++)
					{
						u32 rv = val8[r];
						u32 gv = val8[g];
						u32 bv = val4[b];
	
						u32 color = MAKERGB24PIXEL32(rv,gv,bv);
						DDSS_VERBOSE("ColorHelper")<<"["<<idx<<"] = "<<(std::hex)<<color<<(std::dec)<<"(R="<<rv<<",G="<<gv<<",B="<<bv<<")"<<std::endl;
						colorTable[idx] = color;
						reverseTable[color] = idx;
						idx++;
					}
				}
			}
			
		}
		
		
	}
	
}

