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
    File Name  : dFLVFile.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "FLVFile.h"
#include "ScreenVideoFrame.h"

namespace dm
{
	FLVFile::FLVFile()
	{
		clear();
	}
	FLVFile::~FLVFile()
	{
		clear();
	}
	void FLVFile::clear()
	{
		header.clear();
		header.init();
	}
	void FLVFile::open(const String fileName)
	{
		flvFileInput.open(fileName);
		InputStream* input = &flvFileInput;
		header.load(input);
		if(!header.hasValidMagic())
		{
			throw FLVErrorBadMagic();
		}
		else
		{
			u32 reserved = 0;
			input->readLong(&reserved);
			if(reserved != 0)
			{
				throw FLVErrorInvalidFormat();
			}
		}
	}
	FLVTag* FLVFile::getNextTag(FLVTag& tag)
	{
		if(!flvFileInput.isValid() || flvFileInput.eof())
		{
			return 0;
		}
		tag.load(&flvFileInput);
		s32 tagLength = 0;
		flvFileInput.readLong((u32 *)&tagLength);
		if(tagLength <= 0)
		{
			return 0;
		}
		return &tag;
	}
	void FLVFile::load(InputStream* input, FLVReadHandler* handler)
	{
		FLVReadHandler* actualHandler = (handler?handler:(FLVReadHandler *)this);
		clear();
		if(!input && !input->isValid())
		{
			throw FLVErrorInvalidFile();
		}
		
		header.load(input);

		if(!header.hasValidMagic())
		{
			throw FLVErrorBadMagic();
		}
		else
		{
			u32 reserved = 0;
			input->readLong(&reserved);
			if(reserved != 0)
			{
				throw FLVErrorInvalidFormat();
			}
			else
			{
				
				//handle the header
				actualHandler->handleReadHeader(&header);

				std::cout<<"Starting tag reading..."<<std::endl;
				bool hasMore = true;
				//till input stream has data
				while(!input->eof() && hasMore)
				{
					//now read one tag
					FLVTag tag;
					tag.load(input);
					s32 tagLength = 0;
					input->readLong((u32 *)&tagLength);
					//std::cout << "  TAG Length : "<<tagLength<<std::endl;
					if(tagLength <= 0)
					{
						hasMore = false;
						break;
					}
					else
					{
						//handle the tag
						actualHandler->handleReadTag(&tag);
					}
				}
				//send the handler the finish call
				actualHandler->handleReadFinish();
			}
		}
	}
	void FLVFile::load(const String fileName,FLVReadHandler* handler)
	{
		FileInputStream fileInput;
		fileInput.open(fileName);
		load(&fileInput,handler);
	}
	void FLVFile::handleReadHeader(FLVHeader* header)
	{
#ifdef _DEBUG
		header->dumpInfo(std::cout);
#endif
	}
	void FLVFile::handleReadTag(FLVTag* tag)
	{
#ifdef _DEBUG
		tag->dumpInfo(std::cout);
		ByteBuffer& tagData = tag->getTagData();
		if(tag->isVideoTag())
		{
			
			u8 vidHdr = tagData.getByte(0);
			u8 codecTypeVal = (u8)(vidHdr & 0x0F);
			if(codecTypeVal == FLVConstants::getInstance()->FLV_VIDEO_CODEC_SCREEN_V1)
			{
				std::cout<<"! --- screen video frame ----!"<<std::endl;
				ByteBufferInputStream bis(&tagData,false);
				ScreenVideoFrame svFrame;
				svFrame.load(&bis);
				svFrame.dumpInfo(std::cout);

				//HexDump::dumpHex(std::cout,tagData.getData(),tagData.getLength());
			}
		}
		else if(tag->isAudioTag())
		{
			//HexDump::dumpHex(std::cout,tagData.getData(),tagData.getLength());
			
		}
#endif
	}
	void FLVFile::handleReadFinish()
	{
#ifdef _DEBUG
		std::cout<<"finished reading FLV File!!!"<<std::endl;
#endif
	}


	FLVWriter::FLVWriter()
	{
	}
	FLVWriter::~FLVWriter()
	{
		close();
	}
	void FLVWriter::init(const char* fileName,bool video, bool audio)
	{
		tsGen.reset();
		timeStamp = 0;
		close();
		outFile.open(fileName);
		if(outFile.isValid())
		{

			header.init();
			header.setHasVideo(video);
			header.setHasAudio(audio);
			header.save(&outFile);
			outFile.writeLong(0);
		}
		else
		{
			throw FLVErrorInvalidFile();
		}
	}
	void FLVWriter::close()
	{
		outFile.close();
	}
	void FLVWriter::writeTag(FLVTag& tag)
	{
		if(outFile.isValid())
		{
			/*timeStamp = tag.getTimeStamp() + tag.getTimeStampExtended() * 256 * 256 * 256
			tag.setTimeStamp(tsThis - timeStamp);*/
			tag.save(&outFile);
			outFile.writeLong(tag.getDataSize() + 9);
			//DSS_PRINTF("Wrote FLV Tag...\n");
			tag.dumpInfo(std::cout);
			//DSS_PRINTF("End Write Tag!!!\n");
		}
		else
		{
			printf("FLVWriter::writeTag(FAILED) - output file is not valid!!!\n\n\n");
		}
	}

};
