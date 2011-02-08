//$$<!--TOFUTAG-->$$//
 /************************************************************************** 	 
  *                                                                        *
  *               DDDDD   iii             DDDDD   iii                      * 
  *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           * 
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
  *                                                                        *
  **************************************************************************/
/* *************************************************************************	
	THIS FILE IS PART OF THE DIMDIM CODEBASE. TO VIEW LICENSE AND EULA
	FOR THIS CODE VISIT http://www.dimdim.com
   ************************************************************************ */

/* ------------------------------------------------------ 
    File Name  : dFLVTag.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "FLVTag.h"
namespace dm
{
	FLVTag::FLVTag()
	{
		clear();
	}
	FLVTag::~FLVTag()
	{
		clear();
	}

	FLVTag* FLVTag::createDummyTag(const long dataSize)
	{
		FLVTag* tag = new FLVTag();
		//tag->makeVideoTag();
		tag->makeAudioTag();
		tag->dataSize = dataSize;
		tag->emptyTag = false;
		tag->tagData.reallocate(dataSize);
		tag->tagData.zeroMemory();
		return tag;
	}
	FLVTag* FLVTag::createMetadataTag(const String methodName, amf::AMFVariant* data)
	{
		FLVTag* tag = new FLVTag();//home/mohaps/MyProgs/WorkDev/Reflector/etc/tools
		tag->makeMetadataTag();
		ByteBufferOutputStream bos;


		//method name
		amf::AMFStringUTF8 var1(methodName);
		amf::AMFCodec::encode(&bos,&var1);
		
		//data
		amf::AMFCodec::encode(&bos,data);
		const ByteBuffer* buf = bos.get();
		tag->emptyTag = false;
		tag->dataSize = buf->getLength();
		tag->tagData.append(buf);
		return tag;
	}

	FLVTag* FLVTag::createAVMetaTag(u32 durationMillis, u8 videoCodec, u8 audioCodec, bool canSeekToEnd)
	{
		String methodName = "onMetaData";
		amf::AMFMixedArray data;
		
		/*[0] :: audiocodecid = (amf::double) 6
		[1] :: duration = (amf::double) 19.784
		[2] :: canSeekToEnd = (amf::bool) true
		[3] :: videocodecid = (amf::double) 2*/
		{
			amf::AMFDouble* val = new amf::AMFDouble();
			val->setValue((f64)audioCodec);
			data.put("audiocodecid",val);
		}
		{
			amf::AMFDouble* val = new amf::AMFDouble();
			val->setValue(0.001 * (f64)durationMillis);
			data.put("duration",val);
		}
		{
			amf::AMFBool* val = new amf::AMFBool();
			val->setValue(canSeekToEnd);
			data.put("canSeekToEnd",val);
		}
		{
			amf::AMFDouble* val = new amf::AMFDouble();
			val->setValue((f64)videoCodec);
			data.put("videocodecid",val);
		}
		return createMetadataTag(methodName,&data);
	}
	void FLVTag::load(InputStream* input)
	{
		clear();
		
		input->readByte(&tagType);
		//std::cout<<"Tag Type : "<<(std::hex)<<(u32)tagType<<(std::dec)<<std::endl;
		input->readMedium(&dataSize);
		//std::cout<<"Data Size : "<<dataSize<<std::endl;
		input->readMedium(&timeStamp);
		input->readByte(&timeStampExtended);
		input->readMedium(&streamId);
		tagData.reallocate(dataSize,false);
		input->read(tagData.getData(),tagData.getLength());
		/*
		std::cout<<"Marker : "<<input->getMarkerPosition()<<std::endl;
		std::cout<<"Remaining : "<<input->getRemainingByteCount()<<std::endl;
		
		char buf[36];
		memset(buf,0,36);
		std::cout<<"Peek (36) : "<<input->peek(buf,36)<<std::endl;
		Dump::hexDump(std::cout, buf,36);//tagData.getData(),tagData.getLength());*/
	}
	void FLVTag::save(OutputStream* output)
	{
		output->writeByte(tagType);
		output->writeMedium((u32)tagData.getLength());
		output->writeMedium(timeStamp);
		output->writeByte(timeStampExtended);
		output->writeMedium(streamId);
		if(tagData.getLength() > 0)
		{
			output->write(tagData.getData(),tagData.getLength());
		}
	}

	void FLVTag::dumpInfo(std::ostream& os)
	{
	/*
		os<<"!-------------------------------------------!"<<std::endl;
		if(tagType == FLVConstants::getInstance()->FLV_TAG_TYPE_AUDIO)
		{
			os<<">>AUDIO TAG"<<std::endl;
		}
		else if(tagType == FLVConstants::getInstance()->FLV_TAG_TYPE_VIDEO)
		{
			os<<">>VIDEO TAG"<<std::endl;
		}
		else
		{
			os<<">>TAG :: 0x"<<(std::hex)<<(UINT)tagType<<(std::dec)<<std::endl;
			os<<"!-------------------------------------------!"<<std::endl;
			return;
		}
		os<<"   %%% Data Size (bytes) :: "<<dataSize<<std::endl;
		os<<"   %%% TimeStamp :: "<<timeStamp<<std::endl;
		os<<"   %%% TimeStamp (Extended) :: "<<(UINT)timeStampExtended<<std::endl;
		os<<"   %%% Stream Id :: "<<(UINT)streamId<<std::endl;

		printAudioInfo(os);
		printVideoInfo(os);
		
		os<<"!-------------------------------------------!"<<std::endl;
		*/
	}
	void FLVTag::clear()
	{
		emptyTag = false;
		dataSize = 0;
		streamId = 0;
		tagData.destroy();
		tagType = 0;
		timeStamp = 0;
		timeStampExtended = 0;
	}
	void FLVTag::makeKeyFrame()
	{
		if(isVideoTag() && !this->isKeyFrame())
		{
			u8* hdr=tagData.getData(0);
			*hdr = 0x13;
		}
	}
	void FLVTag::printAudioInfo(std::ostream& os)
	{
		if(isAudioTag())
		{
			BYTE sndHdr = tagData.getByte(0);
			BYTE fmt = (BYTE)(sndHdr >> 4);
			if(fmt == 0)
			{
				os<<"         Codec : NONE (0)"<<std::endl;
			}
			else if(fmt == 1)
			{
				os<<"         Codec : ADPCM (1)"<<std::endl;
			}
			else if(fmt == 2)
			{
				os<<"         Codec : MP3 (2)"<<std::endl;
			}
			else if(fmt == 5)
			{
				os<<"         Codec : NELLYMOSER (8kHz mono) (5)"<<std::endl;
			}
			else if(fmt == 6)
			{
				os<<"         Codec : NELLYMOSER (6)"<<std::endl;
			}
			else
			{
				os<<"         Codec : UNKNOWN ("<<(u32)fmt<<")"<<std::endl;
			}
			BYTE lo  = (BYTE)(sndHdr & 0x0F);

			BYTE rate = (BYTE)(lo >> 2);
			if(rate == 0)
			{
				os<<"         Sampling Rate : 5.5 kHz"<<std::endl;
			}
			else if(rate == 1)
			{
				os<<"         Sampling Rate : 11 kHz"<<std::endl;
			}
			else if(rate == 2)
			{
				os<<"         Sampling Rate : 22 kHz"<<std::endl;
			}
			else if(rate == 3)
			{
				os<<"         Sampling Rate : 44 kHz"<<std::endl;
			}
			else
			{
				os<<"         Sampling Rate : Unknown"<<std::endl;
			}
			BYTE size = (BYTE)(lo & 0x02);
			size = size >> 1;
			if(size == 0)
			{
				os<<"         Sample Size : 32 bit"<<std::endl;
			}
			else if(size == 1)
			{
				os<<"         Sample Size : 16 Bit"<<std::endl;
			}
			else
			{
				os<<"         Sample Size : Unknown"<<std::endl;
			}
			BYTE type = (BYTE)(lo & 0x01);
			if(type == 0)
			{
				os<<"         Type : MONO"<<std::endl;
			}
			else if(type == 1)
			{
				os<<"         Type : STEREO"<<std::endl;
			}
			else
			{
				os<<"         Type : Unknown"<<std::endl;
			}
		}
	}
	bool FLVTag::isKeyFrame()
	{
		if(isVideoTag())
		{
			BYTE vidHdr = tagData.getByte(0);

			BYTE frameTypeVal = (BYTE)(vidHdr >> 4);
			return frameTypeVal == 0x01;
		}
		return false;
	}
	void FLVTag::printVideoInfo(std::ostream& os)
	{
		if(isVideoTag())
		{
			BYTE vidHdr = tagData.getByte(0);

			BYTE frameTypeVal = (BYTE)(vidHdr >> 4);
			switch(frameTypeVal)
			{
			case 1:
				os<<"         FrameType : KEYFRAME"<<std::endl;
				break;
			case 2:
				os<<"         FrameType : INTERFRAME"<<std::endl;
				break;
			case 3:
				os<<"         FrameType : DISPOSABLE INTERFRAME"<<std::endl;
				break;
			default:
				os<<"         FrameType : UNKNOWN"<<std::endl;
				break;
			}
			BYTE codecTypeVal = (BYTE)(vidHdr & 0x0F);
			switch(codecTypeVal)
			{
			case 2:
				os<<"         Codec : SORENSON (2)"<<std::endl;
				break;
			case 3:
				os<<"         Codec : SCREEN VIDEO (3)"<<std::endl;
				break;
			case 4:
				os<<"         Codec : ON2 VP6 (4)"<<std::endl;
				break;
			case 5:
				os<<"         Codec : ON2 VP6 (ALPHA) (5)"<<std::endl;
				break;
			case 6:
				os<<"         Codec : SCREEN VIDEO 2 (6)"<<std::endl;
				break;
			default:
				os<<"         Codec : UNKNOWN ("<<(u32)codecTypeVal<<")"<<std::endl;
				break;
			}
		}

	}
};
