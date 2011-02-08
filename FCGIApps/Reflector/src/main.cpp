#include "dimdim/common/api.h"
#include "dimdim/screen/api.h"

#define DDSS_VERSION_STR	"4.1.1"


#include "corona.h"
using namespace dm;

#define DDSS_LOG_NAME	"etc/log/dimdimScreenShare"
#define DDSS_LOG_LEVEL  dm::Logger::LL_WARNING
#define DDSS_INI_FILE	"etc/cfg/dimdimScreenShare.ini"

dm::IniFile g_iniFile;

#define DDSS_ARG_MISSING_ERROR(argIndex,argName)	{ std::cerr<<"ERROR: missing argument "<<( argIndex - 2 )<<" -- "<<argName<<"."<<std::endl; printUsage(argv[0],std::cerr); return -1; }



struct SlideEvent
{
	SlideEvent(int idx=0, u32 ts=0){ slideIndex = idx; timeStamp = ts; }
	int slideIndex;
	u32 timeStamp;
};

size_t parseSlideEvents(String seqStr, std::vector<SlideEvent>& events)
{
	events.clear();
	StringTokenizer st(seqStr,";");
	while(st.hasMoreTokens())
	{
		String eventStr = st.nextToken();
		size_t index = eventStr.find_first_of('=');
		String s1 = eventStr.substr(0,index);
		String s2 = eventStr.substr(index+1);
		SlideEvent ev(atoi(s1.c_str()), atoi(s2.c_str()));
		events.push_back(ev);
	}
	return events.size();
}


class FLVMetaDataExtractor : public FLVReadHandler
{
	public:
		FLVMetaDataExtractor(){}
		virtual ~FLVMetaDataExtractor(){}
		void run(const String inputFileName, const String outputFileName)
		{
			this->outFileName = outputFileName;
			FLVFile input;
			input.load(inputFileName.c_str(),this);
			
		}
		void handleReadHeader(FLVHeader* header)
		{
			std::cout<<"Opening output file "<<outFileName<<std::endl;
			outFile.open(outFileName.c_str());
			mtags = 0;
		}
		void handleReadFinish()
		{
			if(outFile.is_open())
			{
				outFile.close();
			}
			std::cout<<"Dumped "<<mtags<<" Metadata tags to "<<outFileName<<std::endl;
		}
		void handleReadTag(FLVTag* tag)
		{
			u32 tagType = (u32)tag->getTagType();
			tagType &= 0xFF;
			if(tagType == 0x12)
			{
				std::cout<<"Metadata Tag"<<std::endl;
				mtags++;
				ByteBufferInputStream bis(tag->getTagData().getData(), tag->getDataSize());
				dumpMetadata(*tag, bis);
			}
			
		}
		void dumpMetadata(FLVTag& tag, InputStream& is)
		{
			if(!outFile.is_open()){ return; }
			else
			{
				amf::AMFVariant* var = amf::AMFCodec::decode(&is);
				ScopedPointer<amf::AMFVariant> vp1(var);
				if(var)
				{
					outFile<<"<METATAG>"<<std::endl;
					outFile<<"TagName="<<var->toString()<<std::endl;
					outFile<<"TagTimeStamp="<<tag.getTimeStampFull()<<std::endl;
					amf::AMFVariant* var2 = amf::AMFCodec::decode(&is);
					ScopedPointer<amf::AMFVariant> vp2(var2);
					if(var2){ var2->dumpAll(std::cout); }
					if(var2)
					if(var2->getType() == amf::AMFConstants::AMF_TYPE_MIXEDARRAY)
					{
							amf::AMFMixedArray* ama = (amf::AMFMixedArray *)var2;
							outFile<<"<ENTRYLIST>"<<std::endl;
							for(u32 i = 0; i < ama->getEntrySize(); i++)
							{
								amf::AMFTableEntry* entry = ama->getEntryAt((int)i);
								outFile<<entry->getKey()->getValue()<<"=";
								amf::AMFVariant* val = entry->getValue();
								if(val)
								{
									val->dump(outFile);
								}
								outFile<<std::endl;
							}
							outFile<<"</ENTRYLIST>"<<std::endl;
							
					}
					else if(var2->getType() == amf::AMFConstants::AMF_TYPE_OBJECT)
					{
						amf::AMFObject* obj = (amf::AMFObject*)var2;
						outFile<<"<ENTRYLIST>"<<std::endl;
						for(u32 i = 0; i < obj->getEntrySize(); i++)
						{
							amf::AMFTableEntry* entry = obj->getEntryAt((int)i);
							outFile<<entry->getKey()->getValue()<<"=";
							amf::AMFVariant* val = entry->getValue();
							if(val)
							{
								val->dump(outFile);
							}
							outFile<<std::endl;
						}
						outFile<<"</ENTRYLIST>"<<std::endl;
						
					}
					outFile<<"</METATAG>"<<std::endl;
					outFile<<std::endl;
				}
			}
		}
				
	private:
		u32 mtags;
		std::ofstream outFile;
		String outFileName;
};
class FLVTagOffsetCounter : public FLVReadHandler
{
public:
	FLVTagOffsetCounter(): tagOffset(0){}
	virtual ~FLVTagOffsetCounter(){}
	void handleReadHeader(FLVHeader* header){}
	void handleReadTag(FLVTag* tag)
	{
		if (!readOffset && tag->isAudioTag() && tag->getDataSize() > 0)
		{
			readOffset = true;
			tagOffset = tag->getTimeStampFull();
			std::cerr<<"FLVTagOffsetCounter: First Audio Tag for ("<<flvFile<<") is at "<<tagOffset<<std::endl;
			std::ofstream fos(outFile.c_str());
			if(fos.is_open())
			{
				fos<<tagOffset<<std::endl;
				fos.flush();
				fos.close();
			}
		}
	}
	void handleReadFinish(){}
	void run(String in, String out)
	{
		readOffset = false;
		flvFile = in;
		outFile = out;
		FLVFile input;
		input.load(flvFile.c_str(),this);
	}
private:
	u32 tagOffset;
	String flvFile;
	String outFile;
	bool readOffset;
};

class FLVDumpTags : public FLVReadHandler
{
public:
	FLVDumpTags(): offset(0){}
	virtual ~FLVDumpTags(){}
	void handleReadHeader(FLVHeader* header){}
	void handleReadFinish(){}
	void run(const String flv, const String dirOut, u32 startOffset)
	{
		this->flvFileName = flv;
		this->outputDir = dirOut;
		this->offset = startOffset;
		FLVFile flvFile;
		flvFile.load(flvFileName.c_str(), this);
	}
	
	void handleReadTag(FLVTag* tag)
	{
		u32 tagTs = tag->getTimeStamp();
		u32 newTs = tagTs + offset;
		if(tag->getDataSize() > 0 && tag->isAudioTag())
		{
			String tagFile = this->makeTagFLVName(tagTs);
			FLVWriter writer;
			writer.init(tagFile.c_str(),false,true);
			tag->setTimeStamp(0);
			writer.writeTag(*tag);
			writer.close();
			std::cout<<"("<<tagTs<<")("<<tag->getDataSize()<<" bytes)Wrote : "<<tagFile<<std::endl;
		}
	}
protected:
	String makeTagFLVName(u32 tagTs)
	{
		std::ostrstream os;
		os<<outputDir<<"/"<<(tagTs + offset)<<".flv"<<std::ends;
		String tagName = os.str();
		os.rdbuf()->freeze(false);
		return tagName;
	}
private:
	String flvFileName;
	String outputDir;
	u32 offset;
		
};
class FLVExtractor : public FLVReadHandler
{
public:
	FLVExtractor(u32 startOffset, u32 endOffset) : firstTag(true), begin(startOffset), end(endOffset){}
	virtual ~FLVExtractor(){}
	void handleReadHeader(FLVHeader* header){}
	void handleReadTag(FLVTag* tag)
	{
		if(tag->isAudioTag())
		{
			u32 ts = tag->getTimeStampFull();
			if( ts >= begin && ts <= end)
			{
				if(firstTag)
				{
					if(tag->getTimeStamp() > 0 && tag->getDataSize() > 0)
					{
						tag->setTimeStamp(0);
						flvWriter.writeTag(*tag);
					}
					firstTag = false;
				}
				
				if(tag->getDataSize() > 0)
				{
					u32 newTs = ts - begin;
					tag->setTimeStamp(newTs);
					flvWriter.writeTag(*tag);	
				}
			}
		}
	}
	void handleReadFinish()
	{
		flvWriter.close();
	}
	void run(const String inFile, const String outFile)
	{
		firstTag = true;
		FLVFile input;
		flvWriter.init(outFile.c_str(),false,true);
		input.load(inFile.c_str(),this);
		
	}
	
private:
	bool firstTag;
	u32 begin;
	u32 end;
	FLVWriter flvWriter; 
};


class FLVBacksetWriter : public FLVReadHandler
{
public:
	FLVBacksetWriter() : firstTag(true), begin(0){}
	virtual ~FLVBacksetWriter(){}
	void handleReadHeader(FLVHeader* header){}
	void handleReadTag(FLVTag* tag)
	{
		if(tag->isAudioTag())
		{
			u32 ts = tag->getTimeStampFull();
			if(firstTag)
			{
				if(tag->getTimeStamp() > 0 && tag->getDataSize() > 0)
				{
					begin = tag->getTimeStamp();
					firstTag = false;
				}
			}
			
			if(tag->getDataSize() > 0)
			{
				u32 newTs = ts - begin;
				tag->setTimeStamp(newTs);
				std::cout<<" writing tag (old ts = "<<ts<<") (new ts = "<<newTs<<")"<<std::endl;
				flvWriter.writeTag(*tag);	
			}
			
		}
	}
	void handleReadFinish()
	{
		flvWriter.close();
	}
	void run(const String inFile, const String outFile)
	{
		std::cout<<"FLVBackset : \n\t(input = "<<inFile<<") \n\t(output="<<outFile<<")"<<std::endl;
		firstTag = true;
		FLVFile input;
		flvWriter.init(outFile.c_str(),false,true);
		input.load(inFile.c_str(),this);
		
	}
	
private:
	bool firstTag;
	u32 begin;
	FLVWriter flvWriter; 
};

class FLVPatcher : public FLVReadHandler
{
public:
	FLVPatcher(){}
	virtual ~FLVPatcher(){}
	virtual void handleReadHeader(FLVHeader* header)
	{
		lastVideoTimeStamp = 0;
	}
	virtual void handleReadTag(FLVTag* tag)
	{
		if(tag->isVideoTag())
		{
			u32 ts = tag->getTimeStampFull();
			if(lastVideoTimeStamp -ts > 450)
			{
				tag->makeKeyFrame();
			}
			lastVideoTimeStamp = ts;
		}
		
		flvWriter.writeTag(*tag);
	
	}
	virtual void handleReadFinish()
	{
		flvWriter.close();
	}
	void run(const String inFile, const String outFile)
	{
		FLVFile input;
		flvWriter.init(outFile.c_str(),true,true);
		input.load(inFile,this);
		std::cout<<inFile<<" -> "<<outFile<<std::endl;
	}
private:
	FLVWriter flvWriter;
	u32 lastVideoTimeStamp;
};

class FLVOffsetWriter : public FLVReadHandler
{
public:
	FLVOffsetWriter(){}
	virtual ~FLVOffsetWriter(){}
	virtual void handleReadHeader(FLVHeader* header){}
	virtual void handleReadTag(FLVTag* tag)
	{
		if(tag->isVideoTag() || tag->isAudioTag())
		{
			u32 ts = tag->getTimeStampFull();
			tag->setTimeStamp(ts + offset);
		}
		
		flvWriter.writeTag(*tag);
	
	}
	virtual void handleReadFinish()
	{
		flvWriter.close();
	}
	void run(const String inFile, u32 offsetMs, const String outFile)
	{
		offset = offsetMs;
		FLVFile input;
		flvWriter.init(outFile.c_str(),true,true);
		input.load(inFile,this);
		std::cout<<inFile<<" -> "<<outFile<< "( offset : "<<offset<<" ms)"<<std::endl;
	}
private:
	FLVWriter flvWriter;
	u32 offset;
};	
class FLVTagPrinter : public FLVReadHandler
{
private:
		bool printFlag;
public:
	FLVTagPrinter(bool bPrint = false) : printFlag(bPrint){}
	virtual ~FLVTagPrinter(){}
	virtual void handleReadHeader(FLVHeader* header)
	{
		tags = vtags = atags = mtags = utags = durationMs = 0;
		
		std::cout<<"!------------------------------- BEGIN FLV HEADER"<<std::endl;
		header->dumpInfo(std::cout);
		std::cout<<"!------------------------------- FINISH FLV HEADER"<<std::endl;
	}
	void readMetadata(InputStream& is)
	{
		std::cout<<"Reading Metadata ...."<<std::endl;
		amf::AMFVariant* var = amf::AMFCodec::decode(&is);
		ScopedPointer<amf::AMFVariant> vp1(var);
		std::cout<<"  NAME : "<<(var?var->toString():String("null"))<<std::endl;
		
		amf::AMFVariant* var2 = amf::AMFCodec::decode(&is);
		ScopedPointer<amf::AMFVariant> vp2(var2);
		std::cout<<"  ARG : ("
		<<var2->getTypeName()
		<<")("<<(u32)var2->getType()<<")"
		<<(var2?var2->toString():String("null"))<<std::endl;
		std::cout<<"Metadata read complete!"<<std::endl;
	}
	virtual void handleReadTag(FLVTag* tag)
	{
		u32 tagType = (u32)tag->getTagType();
		tagType &= 0xFF;
		if (printFlag)
		{
			std::cout<<"Tag # "<< (tags) << " / Timestamp : 0x"<<std::hex<<tag->getTimeStampFull()<<std::dec<<"("<<tag->getTimeStampFull()<<" ms) size : "<<tag->getDataSize()<<" bytes!"<<std::endl; 
			std::cout<<" Type : 0x"<<std::hex<<(u32)tag->getTagType()<<std::dec<<std::endl;
		}
		tags++;
		
		if(tag->getTimeStampFull() > durationMs)
		{
			durationMs = tag->getTimeStampFull();
		}
		if(tagType == 0x12)
		{
			std::cout<<" Metadata Tag"<<std::endl;
			mtags++;
			
			//Dump::hexDump(std::cout,tag->getTagData().getData(), tag->getDataSize());
			
			ByteBufferInputStream bis(tag->getTagData().getData(), tag->getDataSize());
			readMetadata(bis);
		}
		else if(tagType == 0x08)
		{
			if(printFlag)
				std::cout<<" Audio Tag"<<std::endl;
			
			//tag->printAudioInfo(std::cout);
			atags++;
		}
		else if(tagType == 0x09)
		{
			if (printFlag)
				std::cout<<" Video Tag"<<std::endl;
			
			//tag->printVideoInfo(std::cout);
			vtags++;
		}
		else
		{
			std::cout<<" Unknown Tag --  Type : 0x"<<std::hex<<(u32)tag->getTagType()<<std::dec<<std::endl;
			utags++;
		}
		if(printFlag)
			std::cout<<"!---------------------------------!"<<std::endl;
		
	}
	
	virtual void handleReadFinish()
	{
		std::cout<<"FLV Read Finished!"<<std::endl;
		std::cout<<" Duration (ms) : "<<durationMs<<std::endl;
		std::cout<<" Tags total : "<<tags<<std::endl;
		std::cout<<"      Video : "<<vtags<<std::endl;
		std::cout<<"      Audio : "<<atags<<std::endl;
		std::cout<<"      Meta  : "<<mtags<<std::endl;
		std::cout<<"      Unk   : "<<utags<<std::endl;
		std::cout<<std::endl;
	}
	
	u32 getTotalTags() const{ return tags; }
	u32 getVideoTags() const{ return vtags; }
	u32 getAudioTags() const{ return atags; }
	u32 getMetaTags() const{ return mtags; }
	u32 getDurationMillis() const{ return durationMs; }
private:
	u32 tags;
	u32 vtags;
	u32 atags;
	u32 mtags;
	u32 utags;
	u32 durationMs;
	
};
class FLVMetadataPatcher : public FLVReadHandler
{
public:
	FLVMetadataPatcher(const String fileName) : flvFileName(fileName){}
	virtual ~FLVMetadataPatcher(){}
	virtual void handleReadHeader(FLVHeader* header)
	{
		FLVTag* mtag = FLVTag::createAVMetaTag(printer.getDurationMillis(),0x03);
		ScopedPointer<FLVTag> tagPtr(mtag);
		flvWriter.writeTag(*mtag);
	}
	virtual void handleReadTag(FLVTag* tag)
	{
		flvWriter.writeTag(*tag);
	}
	virtual void handleReadFinish()
	{
		flvWriter.close();
	}
	void run(const String outFileName)
	{
		FLVFile flvFileInput;
		flvFileInput.load(flvFileName,&printer);
		
		
		FLVFile flvFile;
		flvWriter.init(outFileName.c_str(),true,true);
		flvFile.load(flvFileName,this);
		
	}
private:
	FLVTagPrinter printer;
	FLVWriter flvWriter;
	String flvFileName;
	
};
class FLVAppender : public FLVReadHandler
{
public:
	FLVAppender(const std::vector<String> inFiles, String flvOutFile)
	{
		outFileName = flvOutFile;
		for(size_t s = 0; s < inFiles.size(); s++)
		{
			fileNames.push_back(inFiles[s]);
		}
		
	}
	virtual ~FLVAppender(){ flvWriter.close(); }

	virtual void handleReadHeader(FLVHeader* header)
	{
		std::cout<<"Reading File #"<<fcount<<" : "<<fileNames[fcount]<<"..."<<std::endl;
	}
	virtual void handleReadTag(FLVTag* tag)
	{
		u32 timeStamp = tag->getTimeStampFull();
		if(tag->isVideoTag())
		{
			lastTagTimeStamp = timeStamp;
			timeStamp += lastTimeStamp;
		}
		flvWriter.writeTag(*tag);
	}
	virtual void handleReadFinish()
	{
		fcount++;
		lastTimeStamp = lastTagTimeStamp;
		std::cout<<"Finished reading : "<<fileNames[(fcount-1)]<<" / Updated last video tag timestamp to : "<<lastTimeStamp<<std::endl;
	}
	
	void run()
	{
		lastTimeStamp = 0;
		fcount = 0;
		flvWriter.init(outFileName.c_str(),true,false);
		while(fcount < fileNames.size())
		{
			String inFile = fileNames[fcount];
			FLVFile reader;
			reader.load(inFile,this);
		}
		std::cout<<"Output written to : "<<outFileName.c_str()<<std::endl;
	}
	
private:
	FLVWriter flvWriter;
	std::vector<String> fileNames;
	String outFileName;
	size_t fcount;
	u32 lastTimeStamp;
	u32 lastTagTimeStamp;
	
};
void fillFLV(String flvName, String flvOutFileName)
{
	FLVFile input;
	std::cout<<"Going to open FLV "<<flvName<<" for filling..."<<std::endl;
	input.open(flvName);
	std::cout<<"Initializing output FLV : "<<flvOutFileName<<std::endl;
	FLVWriter writer;
	writer.init(flvOutFileName.c_str(),false,true);
	FLVTag* lastTag = 0;
	FLVTag* tag = new FLVTag();
	u32 minTagIntervalMs = 50;
	std::cout<<"Starting Tag Reading ..."<<std::endl;
	while(input.getNextTag(*tag))
	{
		if(tag->isAudioTag() && tag->getDataSize() > 0)
		{
			u32 tagTs = tag->getTimeStamp();
			u32 tagDelta = tagTs - (lastTag?lastTag->getTimeStamp():0);
			
			if(!lastTag)
			{
				std::cout<<"First Tag Delta : "<<tagDelta<<std::endl;
				if(tagDelta > 0)
				{
					u32 numTags = tagDelta / minTagIntervalMs;
					if (tagDelta % minTagIntervalMs == 0)
					{
						numTags--;
					}
					for(u32 x = 0; x < numTags; x++)
					{
						std::cout << "  --- adding tag at : "<<tagTs<<std::endl;
						u32 ts = x * minTagIntervalMs;
						tag->setTimeStamp(tagTs);
						writer.writeTag(*tag);
					}
					tag->setTimeStamp(tagTs);
				}
			}
			else
			{
				
				std::cout<<"Tag Delta : "<<tagDelta<<std::endl;
				if(tagDelta > minTagIntervalMs)
				{
					u32 numTags = tagDelta / minTagIntervalMs;
					if (tagDelta % minTagIntervalMs == 0)
					{
						numTags--;
					}
					for(u32 x = 1; x <= numTags; x++)
					{
						u32 ts = x * minTagIntervalMs;
						lastTag->setTimeStamp(ts);
						std::cout << "  --- adding tag at : "<<ts<<std::endl;
						writer.writeTag(*lastTag);
					}
				}
			}
			writer.writeTag(*tag);
			if(lastTag){ delete lastTag; lastTag = 0; }
			lastTag = tag;
			tag = new FLVTag();
		}
		else if ((tag->getTagType() & 0xFF) == 0x12)
		{
			std::cout<<"metadata tag"<<std::endl;
		}
	}
	writer.close();
	if(tag){ delete tag; }
	if(lastTag){ delete lastTag; }

}
void mergeFLV(String videoFileName, String audioFileName, String flvOutFileName)
{
	std::cout<<"Video File : "<<videoFileName<<std::endl;
	std::cout<<"Audio File : "<<audioFileName<<std::endl;
	std::cout<<"Merged File : "<<flvOutFileName<<std::endl;
	FLVFile audioFile;
	FLVFile videoFile;
	audioFile.open(audioFileName);
	videoFile.open(videoFileName);
	FLVWriter writer;
	writer.init(flvOutFileName.c_str(),true,true);
	FLVTag videoTag;
	FLVTag audioTag;
	bool keepGoing = true;
	
	u32 timeStampA = 0;
	u32 timeStampV = 0;

	FLVTag* aTag = audioFile.getNextTag(audioTag);
	FLVTag* vTag = videoFile.getNextTag(videoTag);
	while(keepGoing)
	{
		while(aTag && aTag->getTagType() == 0x12)
		{
			aTag = audioFile.getNextTag(audioTag);
		}
		while( vTag && vTag->getTagType() == 0x12)
		{
			vTag = videoFile.getNextTag(videoTag);
		}
		if(!aTag && !vTag)
		{
			keepGoing = false;
			break;
		}
		else
		{
			if(!aTag)
			{
				//std::cout<<"   writing video tag only!"<<std::endl;
				writer.writeTag(videoTag);
				timeStampV = vTag->getTimeStampFull();
				vTag = videoFile.getNextTag(videoTag);
			}
			else if(!vTag)
			{
				//std::cout<<"   writing audio tag only!"<<std::endl;
				writer.writeTag(audioTag);
				timeStampA += aTag->getTimeStampFull();
				aTag = audioFile.getNextTag(audioTag);
			}
			else
			{
				//std::cout<<"Audio TS : "<<aTag->getTimeStamp()<<std::endl;
				//std::cout<<"Video TS : "<<vTag->getTimeStamp()<<std::endl;
				if(vTag->getTimeStampFull() == 0)
				{
					//std::cout<<"   writing first video tag!"<<std::endl;
					writer.writeTag(videoTag);
					vTag = videoFile.getNextTag(videoTag);
					timeStampV = 0;
				}
				else
				{
					u32 tA = aTag->getTimeStampFull();
					u32 tV = vTag->getTimeStampFull();
					
					u32 tNextA = tA;
					u32 tNextV = tV;
					
					if(tNextA > tNextV)
					{
						//std::cout<<"   writing video tag!"<<std::endl;
						writer.writeTag(videoTag);
						timeStampV = vTag->getTimeStampFull();
						vTag = videoFile.getNextTag(videoTag);
					}
					else
					{
						//std::cout<<"   writing audio tag!"<<std::endl;
						writer.writeTag(audioTag);
						timeStampA += aTag->getTimeStampFull();
						aTag = audioFile.getNextTag(audioTag);
					}
				}
			}
			
		}
	}
	std::cout<<"Files joined!!!"<<std::endl;
	writer.close();
}

void offsetFLV(String flvFile, u32 offset, String flvOutFile)
{
	FLVOffsetWriter writer;
	writer.run(flvFile,offset,flvOutFile);
}
void joinFLV(String flvFile1, String flvFile2, String flvOutFile)
{
	std::cout<<"joining "<<flvFile1<<" and "<<flvFile2<<" to make "<<flvOutFile<<std::endl;
	std::vector<String> vec;
	vec.push_back(flvFile1);
	vec.push_back(flvFile2);
	FLVAppender appender(vec,flvOutFile);
	appender.run();
	
}
void splitFLV(String flvFile)
{
	std::cout<<"splitting flv file : "<<flvFile<<" into audio and video!"<<std::endl;
	String videoFile = flvFile;
	String audioFile = flvFile;
	videoFile += ".video.flv";
	audioFile += ".audio.flv";
	
	FLVSplitter splitter(flvFile);
	splitter.split(videoFile,audioFile);
	std::cout<<"FLV "<<flvFile<<" split to "<<videoFile<<" and "<<audioFile<<std::endl;
}

RGBImage* loadImageFile(const String fileName, int width, int height)
{
	std::cout<<" loading file : "<<fileName<<" ... "<<std::endl;
	RGBImage* rgbImg = new RGBImage(width,height);
	ScopedPointer<RGBImage> rgbPtr(rgbImg);
	rgbImg->clear(0xC0);
	
	corona::File* file = corona::OpenFile(fileName.c_str(),false);
	if(!file)
	{
		std::cerr<<"Failed to open file : "<<fileName<<std::endl;
		return 0;
	}

	ScopedPointer<corona::File> filePtr(file);
	
	corona::Image* img = corona::OpenImage(file,corona::FF_BMP,corona::PF_R8G8B8);
	
	if(!img)
	{
		std::cerr<<"Failed to load image from file : "<<fileName<<std::endl;
		return 0;
	}

	ScopedPointer<corona::Image> imgPtr(img);
	
	if(img->getWidth() != width || img->getHeight() != height)
	{
		std::cerr<<"Image dimensions : "<<img->getWidth()<<"X"<<img->getHeight()<<" don't match "<<width<<"X"<<height<<std::endl;
		return 0;
	}
	rgbImg->setBGR24Transpose((const u8*)img->getPixels());
	return rgbPtr.release();
}

RGBImage* getDefaultScreenImage(int width, int height, const String mboxRoot)
{
	String defaultBmpName = mboxRoot;
	defaultBmpName += "/dimdim-default.bmp";
	RGBImage* img = loadImageFile(defaultBmpName, width, height);
	if(!img)
	{
		img = new RGBImage(width, height);
		img->clear(0xFC);
	}
	return img;
}

RGBImage* getDummyScreenImage(int width, int height, const String mboxRoot)
{
	String defaultBmpName = mboxRoot;
	defaultBmpName += "/dimdim-novideo.bmp";
	RGBImage* img = loadImageFile(defaultBmpName, width, height);
	if(!img)
	{
		return getDefaultScreenImage(width, height, mboxRoot);
	}
	return img;
}
void generateDefaultFLV(u32 duration, String outFile, int width, int height, const String mboxRoot)
{
	FLVWriter flvWriter;
	u32 timeStamp = 0;
	
	flvWriter.init(outFile.c_str(), true, false);
	RGBImage *screenImage = getDefaultScreenImage(width,height, mboxRoot); 
	ScopedPointer<RGBImage> imgPtr(screenImage);
	ScreenVideoFrame svFrame;
	svFrame.init(true, width, height);
	for(size_t s = 0; s < duration / 20; s+=20)
	{
			if(s == 0)
			{
				timeStamp = 0;
			}
			else
			{
				timeStamp = s;
			}		
			
			FLVTag tag;
			
			bool emptyTag = false;
			bool keyFrame = (s==0);
			
			// prepare the frame
			bool kf = ScreenVideoEncoder::encodeFrame(&svFrame, screenImage, keyFrame, emptyTag);
			if(kf)
			{
				std::cout<<"Key Frame encountered!"<<std::endl;
				svFrame.makeKeyFrame();
			}
			
			//write the frame to tag
			ScreenVideoEncoder::writeTag(&tag,&svFrame,timeStamp);
			//write the tag to file
			flvWriter.writeTag(tag);
			
		}
		flvWriter.close();
		std::cout<<"Wrote default flv of length "<<duration<<" ms to : "<<outFile<<std::endl;
	
}

void generateDummyFLV(u32 duration, String outFile, int width, int height, const String mboxRoot)
{
	FLVWriter flvWriter;
	u32 timeStamp = 0;
	
	flvWriter.init(outFile.c_str(), true, false);
	RGBImage *screenImage = getDummyScreenImage(width,height, mboxRoot); 
	ScopedPointer<RGBImage> imgPtr(screenImage);
	ScreenVideoFrame svFrame;
	svFrame.init(true, width, height);
	for(size_t s = 0; s < duration / 20; s+=20)
	{
			if(s == 0)
			{
				timeStamp = 0;
			}
			else
			{
				timeStamp = s;
			}		
			
			FLVTag tag;
			
			bool emptyTag = false;
			bool keyFrame = (s==0);
			
			// prepare the frame
			bool kf = ScreenVideoEncoder::encodeFrame(&svFrame, screenImage, keyFrame, emptyTag);
			if(kf)
			{
				std::cout<<"Key Frame encountered!"<<std::endl;
				svFrame.makeKeyFrame();
			}
			
			//write the frame to tag
			ScreenVideoEncoder::writeTag(&tag,&svFrame,timeStamp);
			//write the tag to file
			flvWriter.writeTag(tag);
			
		}
		flvWriter.close();
		std::cout<<"Wrote dummy flv of length "<<duration<<" ms to : "<<outFile<<std::endl;
	
}
bool createPowerPointFLV(std::vector<String> fileNames, int width, int height, int intervalMs, String flvName)
{
	std::cout<<"creating power point FLV ..."<<std::endl;
	std::cout<<" slide count : "<<fileNames.size()<<std::endl;
	std::cout<<" dimension : "<<width<<"X"<<height<<std::endl;
	std::cout<<" interval (ms): "<<intervalMs<<std::endl;
	std::cout<<" output file : "<<flvName<<std::endl;
	
	ScreenVideoFrame svFrame;
	FLVWriter flvWriter;
	u32 timeStamp = 0; //Helper::currentTimeMillis();
	
	
	flvWriter.init(flvName.c_str(),true,false);
	svFrame.init(true,width,height);
	
	u32 tagsEncoded = 0;
	
	for(size_t s = 0; s < fileNames.size(); s++)
	{
		if(s == 0)
		{
			timeStamp = 0;
		}
		else
		{
			timeStamp = intervalMs * s;
		}
		std::cout<<"Timestamp for Tag #"<<tagsEncoded<<" (ms) : "<<timeStamp<<std::endl;
		RGBImage *screenImage = loadImageFile(fileNames[s], width, height);
		ScopedPointer<RGBImage> imgPtr(screenImage);
		
		if(!screenImage)
		{
			std::cerr<<"could not load slide image : "<<fileNames[s]<<std::endl;
			return false;
		}
		
		
		
		
		FLVTag tag;
		
		bool emptyTag = false;
		bool keyFrame = true;
		tagsEncoded++;
		
		// prepare the frame
		bool kf = ScreenVideoEncoder::encodeFrame(&svFrame, screenImage, keyFrame, emptyTag);
		if(kf)
		{
			std::cout<<"Key Frame encountered!"<<std::endl;
			svFrame.makeKeyFrame();
		}
		
		//write the frame to tag
		ScreenVideoEncoder::writeTag(&tag,&svFrame,timeStamp);
		//write the tag to file
		flvWriter.writeTag(tag);
		std::cout<<"Tag Written!"<<std::endl;
		
	}
	flvWriter.close();
	std::cout<<"FLV File : "<<flvName<<std::endl;
	std::cout<<"Tags Written : "<<tagsEncoded<<std::endl;
	
	return true;
	
}
RGBImage* loadPPTSlideImage(String fileName,int width, int height)
{
	std::cout<<" loading file : "<<fileName<<" ... "<<std::endl;
	
	
	
	
	corona::File* file = corona::OpenFile(fileName.c_str(),false);
	if(!file)
	{
		std::cerr<<"Failed to open file : "<<fileName<<std::endl;
		return 0;
	}

	ScopedPointer<corona::File> filePtr(file);
	
	corona::Image* img = corona::OpenImage(file,corona::FF_BMP,corona::PF_R8G8B8);
	
	if(!img)
	{
		std::cerr<<"Failed to load image from file : "<<fileName<<std::endl;
		return 0;
	}

	ScopedPointer<corona::Image> imgPtr(img);
	
	if(img->getWidth() > width || img->getHeight() > height)
	{
		std::cerr<<"Image dimensions "<<img->getWidth()<<"X"<<img->getHeight()<<"are bigger than specified dimension = "<<width<<"X"<<height<<"!!!"<<std::endl;
		return 0;
	}
	else if(img->getWidth() == width && img->getHeight() == height)
	{
		RGBImage* finalImg = new RGBImage(width,height);
		ScopedPointer<RGBImage> fimPtr(finalImg);
		finalImg->clear(0xC0);
		finalImg->setBGR24Transpose((const u8*)img->getPixels());
		return fimPtr.release();
	}
	else
	{
		
			RGBImage* tmpImg = new RGBImage(img->getWidth(), img->getHeight());
			ScopedPointer<RGBImage> tmpPtr(tmpImg);
			tmpImg->clear(0xC0);
			tmpImg->setBGR24Transpose((const u8*)img->getPixels());
			
			RGBImage* finalImg = new RGBImage(width,height);
			ScopedPointer<RGBImage> fimPtr(finalImg);
			finalImg->clear(0xC0);
			
			int x = (width - img->getWidth()) / 2;
			int y = (height - img->getHeight()) / 2;
			finalImg->set( x, y, tmpImg);
			return fimPtr.release();
	}
}
bool createPPTSlideFLV(String docId, String slideDir, size_t slideCount, String suffix, std::vector<SlideEvent>& events, int width, int height, String flvOut, int duration, String mboxRoot)
{
	std::vector<String> slideFiles;
	for(size_t s = 0; s < slideCount; s++)
	{
		std::ostrstream os;
		os<<slideDir<<"/"<<docId<<"-"<<s<<"."<<suffix<<std::ends;
		const char* slideFile = os.str();
		os.rdbuf()->freeze(false);
		slideFiles.push_back(slideFile);
		std::cout<<" SLIDE #"<<s<<" : "<<slideFile<<std::endl;
	}
	
	FLVWriter flvWriter;
	flvWriter.init(flvOut.c_str(),true,false);
	
	ScreenVideoFrame svFrame;
	svFrame.init(true,width,height);
	u32 timeStamp = 0;
	size_t slideIndex = 0;	
	u32 lastTimeStamp = 0;
	RGBImage* lastImage = 0;
	for(size_t e = 0; e <=  events.size(); e++)
	{
		RGBImage* screenImage = 0;
		
		if(e < events.size())
		{
			slideIndex = (size_t)events[e].slideIndex;
			timeStamp = events[e].timeStamp;
			std::cout<<" Slide Frame # "<<e<<" / Slide : "<<events[e].slideIndex<<" / Timestamp : "<<events[e].timeStamp<<std::endl;
			screenImage = loadPPTSlideImage(slideFiles[slideIndex],width,height);
		}
		else
		{

			screenImage = getDefaultScreenImage(width,height, mboxRoot); 
			if(e == events.size())
			{
				timeStamp = duration;
			}
		}	
		ScopedPointer<RGBImage> imgPtr(screenImage);
		if(!screenImage)
		{
			std::cerr<<"could not load slide image : "<<slideFiles[slideIndex]<<std::endl;
			return false;
		}
		

		if(lastImage)
		{
				FLVTag itag;
				bool emptyTag = false;
				bool keyFrame = true;
				ScopedPointer<RGBImage> lastPtr(lastImage);
				u32 deltaTime = timeStamp - lastTimeStamp;
				if(deltaTime > 1000)
				{
						u32 ifCount = deltaTime / 1000;
						if(deltaTime % 1000 == 0){ ifCount--; }
						for(u32 i = 1; i < ifCount; i++)
						{
								u32 curTime = lastTimeStamp + i  * 1000;
								ScreenVideoEncoder::encodeFrame(&svFrame,lastImage, keyFrame, emptyTag);
								ScreenVideoEncoder::writeTag(&itag, &svFrame, curTime);
								flvWriter.writeTag(itag);

						}
				}
		}

		FLVTag tag;
		
		bool emptyTag = false;
		bool keyFrame = true;
		// prepare the frame
		bool kf = ScreenVideoEncoder::encodeFrame(&svFrame, screenImage, keyFrame, emptyTag);
		if(kf)
		{
			std::cout<<"Key Frame encountered!"<<std::endl;
			svFrame.makeKeyFrame();
		}
		
		//write the frame to tag
		ScreenVideoEncoder::writeTag(&tag,&svFrame,timeStamp);
		//write the tag to file
		flvWriter.writeTag(tag);
		
		imgPtr.release();
        lastImage = screenImage;
        lastTimeStamp = timeStamp;

		
	}
	
	std::cout<<"Wrote Powerpoint FLV to : "<<flvOut<<std::endl;
	flvWriter.close();
	return true;
	
	
}

void printUsage(const String progName, std::ostream& os)
{
	os<<"dimdimReflector - Version "<<DDSS_VERSION_STR<<" (compiled on "<<__DATE__<<" "<<__TIME__<<")"<<std::endl;
	os<<"Usage : "<<progName<<" [cmd] [params]"<<std::endl;
	os<<"no parameters passed means reflector will start in FCGI mode!"<<std::endl;
	os<<std::endl;
	os<<"Command list : "<<std::endl;
	
	os<<"  default  		( param list = <duration(ms)> <flv-file> [width=640] [height=480])"<<std::endl;
	os<<"  flvinfo  		( param list = <flv-file>)"<<std::endl;
	os<<" dumpmetadata (param list = <flv-file> [out-file]"<<std::endl;
	os<<"  split    		( param list = <flv-file>)"<<std::endl;
	os<<"  offset   		( param list = <flv-file> <offset(ms)> <out-file> )"<<std::endl;
	os<<"  append   		( param list = <file1> <file2> <out-file> )"<<std::endl;
	os<<"  merge    		( param list = <file1> <file2> <out-file> )"<<std::endl;
	os<<"  addmetadata   	( param list = <flv-file> <out-file> )"<<std::endl;
	os<<"  findoffset	   	( param list = <flv-file> <out-file> )"<<std::endl;
	os<<"  patch    		( param list = <flv-file> <out-file> )"<<std::endl;
	os<<"  fill    		( param list = <flv-file> <out-file> )"<<std::endl;
	os<<"  dtp2flv    		( param list = <dump-file> <conf-key> <stream-name> <flv-width> <flv-height>  <duration> <mbox-root>)"<<std::endl;
	os<<"  ppt2flv    		( param list = <config-file>)"<<std::endl;
	os<<"  help				( no args. prints this message )"<<std::endl;
}
int main(int argc, char **argv)
{
	//load the ini file
	std::ifstream in(DDSS_INI_FILE);
	if(!in.is_open())
	{
		std::cerr<<"[ERROR] Could not load ini file : "<<DDSS_INI_FILE<<std::endl;
		return -1;
	}
	else
	{
		g_iniFile.read(in);
#ifdef _DEBUG
			g_iniFile.write(std::cout);//(DDSS_INFO("main")<<"Loaded ini file"<<std::endl));
#endif
		
		//read log config from ini file
		String logFileName = g_iniFile.get("LOG","file",DDSS_LOG_NAME);
		
		Logger::Level level = (Logger::Level)g_iniFile.getInt("LOG","level",DDSS_LOG_LEVEL);
		bool loggingOn = g_iniFile.getBool("LOG","enabled",true);
		bool consoleLog = false;
		consoleLog = (g_iniFile.get("LOG","type","file") == "console");
		bool appendMode = g_iniFile.getBool("LOG","appendMode",false);
		
		bool auditOn = g_iniFile.getBool("LOG","auditOn",false);
		Logger::setAuditOn(auditOn);
		
		u32 rollOver = g_iniFile.getInt("LOG","rollOver",0);
		if(rollOver >= 4096)
		{
			rollOver = 4096;
		}
		
		
		//
		//create the logger
		//
		if(loggingOn)
		{
			if(!consoleLog)
			{
				dm::Logger::create(logFileName,appendMode);
				dm::Logger::setRollOver(rollOver);
				
			}
			else
			{
				dm::Logger::createConsoleLog();
			}
			dm::Logger::setLevel(level);
		}
		
		DDSS_INFO("MainApp")<<" ..........................................."<<std::endl;
		DDSS_INFO("MainApp")<<" ............... STARTS ...................."<<std::endl;
		DDSS_INFO("MainApp")<<" ..........................................."<<std::endl;
		
		
		dm::ScreenShareApp app;
		if(argc > 1)
		{
			app.setToolMode(true);
		}
		if(!app.init(g_iniFile))
		{
			DDSS_ERROR("Main")<<"Failed to initialize app"<<std::endl;
			return -1;
		}	
		else
		{	
			
			if(argc == 1)
			{
				std::cout<<"Running reflector server..."<<std::endl;
				app.run();
			}
			
			else
			{
				String cmdStr = argv[1];
				if(cmdStr == "default")
				{
					u32 offset = 0;
					int width = 640;
					int height = 480;
					if(argc > 2)
					{
						offset = (u32)atoi(argv[2]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Duration (ms)");
					}
					String outFile = "";
					if(argc > 3)
					{
						outFile = (argv[3]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(3,"Output File");
					}

					if(argc > 4)
					{
						width = atoi(argv[4]);
					}
					else
					{
						//DDSS_ARG_MISSING_ERROR(4,"flv width");
					}

					if(argc > 5)
					{
						height = atoi(argv[5]);
					}
					else
					{
						//DDSS_ARG_MISSING_ERROR(5,"flv height");
					}
					
				        String mboxRoot = app.getMailBoxRoot();	
					
					generateDefaultFLV(offset, outFile, width, height, mboxRoot);
				}
				else if(cmdStr == "dummy")
				{
					u32 offset = 0;
					int width = 640;
					int height = 480;
					if(argc > 2)
					{
						offset = (u32)atoi(argv[2]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Duration (ms)");
					}
					String outFile = "";
					if(argc > 3)
					{
						outFile = (argv[3]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(3,"Output File");
					}

					if(argc > 4)
					{
						width = atoi(argv[4]);
					}
					else
					{
						//DDSS_ARG_MISSING_ERROR(4,"flv width");
					}

					if(argc > 5)
					{
						height = atoi(argv[5]);
					}
					else
					{
						//DDSS_ARG_MISSING_ERROR(5,"flv height");
					}
					
				        String mboxRoot = app.getMailBoxRoot();	
					
					generateDummyFLV(offset, outFile, width, height, mboxRoot);
				}
				else if(cmdStr == "split")
				{
					if(argc < 2)
					{
						std::cerr<<"cmd (split) requires FLV file!"<<std::endl;
						return -1;
					}
					String inputFLVFile = argv[2];
					splitFLV(inputFLVFile); 
				}
				else if(cmdStr == "offset")
				{
					String flvName = "etc/dat/flv/screen.flv";
					if(argc > 2)
					{
						flvName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
						
					}

					u32 offsetMs = 0;
					if( argc > 3)
					{
						offsetMs = atoi(argv[3]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(3,"Offset (ms)");
						
					}
					String flvOut = flvName + String(".out.flv");
					if( argc > 4)
					{
						flvOut = argv[4];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(4,"Output FLV Name");
						
					}
					offsetFLV(flvName,offsetMs,flvOut);
				}
				else if(cmdStr == "extract")
				{
					if(argc <= 5)
					{
						DDSS_ARG_MISSING_ERROR(5,"input begin end output");
					}
					else
					{
						String inFile = argv[2];
						u32 begin = (u32)atoi(argv[3]);
						u32 end = (u32)atoi(argv[4]);
						String outFile = argv[5];
						std::cerr<<"[MOHAPS] EXTRACTION ("<<begin<<"-"<<end<<") from "<<inFile<<" to "<<outFile<<std::endl;
						FLVExtractor ext(begin,end);
						ext.run(inFile, outFile);
						std::cout<<"EXTRACTION FINISHED!"<<std::endl;
					}
				}
				else if(cmdStr == "append")
				{
					String videoFile = "etc/dat/flv/screen.flv";
					if(argc > 2)
					{
						videoFile = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"First Input FLV Name");
						
					}

					String audioFile = "etc/dat/flv/audio2.flv";
					if( argc > 3)
					{
						audioFile = argv[3];
					}

					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Second Input FLV Name");
						
					}
					String outputFile = videoFile + String(".out.flv");
					if( argc > 4)
					{
						outputFile = argv[4];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(4,"Output FLV Name");
						
					}
					joinFLV(videoFile, audioFile, outputFile);
				}
				else if(cmdStr == "merge")
				{
					String videoFile = "etc/dat/flv/screen.flv";
					if(argc > 2)
					{
						videoFile = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"First Input FLV Name");
						
					}

					String audioFile = "etc/dat/flv/audio2.flv";
					if( argc > 3)
					{
						audioFile = argv[3];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(3,"Second Input FLV Name");
						
					}
					String outputFile = videoFile + String(".out.flv");
					if( argc > 4)
					{
						outputFile = argv[4];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(4,"Output FLV Name");
						
					}
					mergeFLV(videoFile, audioFile, outputFile);
					
				}
				else if(cmdStr == "dumpmetadata")
				{
					String flvName, outputName;
					if( argc > 2)
					{
						flvName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
					}
					if(argc > 3)
					{
						outputName = argv[3];
					}
					else
					{
						outputName = flvName;
						outputName += ".metadata";
					}
					FLVMetaDataExtractor mdx;
					mdx.run(flvName, outputName);
				}
				else if(cmdStr == "findoffset")
				{
					String flvName, outputName;
					if( argc > 2)
					{
						flvName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
					}
					if(argc > 3)
					{
						outputName = argv[3];
					}
					else
					{
						outputName = flvName;
						outputName += ".offset.txt";
					}
					FLVTagOffsetCounter mdx;
					mdx.run(flvName, outputName);
				}
				else if(cmdStr == "backset")
				{
					String flvName, outputName;
					u32 offset = 0;
					if( argc > 2)
					{
						flvName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
					}
					if(argc > 3)
					{
						outputName = argv[3];
					}
					else
					{
						outputName = flvName;
						outputName += ".backset.flv";
					}
					FLVBacksetWriter mdx;
					mdx.run(flvName, outputName);
				}
				else if (cmdStr == "dumptags")
				{
					String flvName, outDir;
					u32 offset = 0;
					if(argc > 2){ flvName = argv[2]; }
					else{ DDSS_ARG_MISSING_ERROR(2,"Input FLV Name"); }
					if(argc > 3){ outDir = argv[3]; }
					else{ DDSS_ARG_MISSING_ERROR(3,"Output Dir"); }
					if(argc > 4){ offset = (u32)atoi(argv[4]); }
					else{ DDSS_ARG_MISSING_ERROR(4,"Start Offset"); }
					
					FLVDumpTags fdt;
					fdt.run(flvName, outDir, offset);
				}
				else if(cmdStr == "flvinfo")
				{
					String flvName = "etc/dat/flv/screen.flv";
					if(argc > 2)
					{
						flvName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
					}

					std::cout<<"FLV File : "<<flvName<<std::endl;
					FLVTagPrinter ftp(true);
					FLVFile f2;
					f2.load(flvName.c_str(),&ftp);
					std::cout<<"XXX dump ends XXX"<<std::endl;
				}
				else if(cmdStr == "addmetadata")
				{
					String flvName = "etc/dat/flv/screen.flv";
					if(argc > 2)
					{
						flvName = argv[2];
						
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
						
					}

					String flvOut = flvName + String(".out.flv");
					if( argc > 3)
					{
						flvOut = argv[3];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(3,"Output FLV Name");
						
					}
					FLVMetadataPatcher fmp(flvName);
					fmp.run(flvOut);					
					FLVTagPrinter ftp;
					FLVFile f2;
					f2.load(flvOut.c_str(),&ftp);

					std::cout<<"Dumped patched file to : "<<flvOut;
					
				}
				else if(cmdStr == "patch")
				{
					String flvName;
					if(argc > 2)
					{
						flvName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
						
					}

					String flvOut = flvName + String(".out.flv");
					if(argc > 3)
					{
						flvOut = argv[3];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Output FLV Name");
						
					}
					FLVPatcher fmp;
					fmp.run(flvName,flvOut);
					std::cout<<"Dumped patched file to : "<<flvOut<<std::endl;
				}
				else if(cmdStr == "fill")
				{
					String flvName = "etc/dat/flv/rad-out.flv";
					if(argc > 2)
					{
						flvName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Input FLV Name");
						
					}

					String flvOut = flvName + String(".out.flv");
					if(argc > 3)
					{
						flvOut = argv[3];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(3,"Output FLV Name");
						
					}
					std::cout<<"FillFLV() : "<<std::endl;
					std::cout<<"\tinput : "<<flvName<<std::endl;
					std::cout<<"\toutput : "<<flvOut<<std::endl;
					fillFLV(flvName,flvOut);
					std::cout<<"Dumped filled FLV file to : "<<flvOut<<std::endl;
				}
				else if(cmdStr == "ppt2flv")
				{
					std::cout<<"converting ppt slides to an FLV..."<<std::endl;
					int argCount = argc - 2;
					for(int i = 0; i < argCount; i++)
					{
						std::cout<<"Arg ["<<(i)<<"] - {"<<argv[i+2]<<"}"<<std::endl;
					}
					
					int width = 640;
					int height = 480;
					int duration = 0;
					String docId, slideDir, flvOut, suffix, slideSequenceStr, mboxRootDir;
					int slideCount = 0;
					
					int paramIndex = 2;
					if(argc > paramIndex)
					{
						docId = argv[paramIndex];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"doc id");
					}
					paramIndex++;
					
					if(argc > paramIndex)
					{
						slideDir = argv[paramIndex];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"slideDir");
					}
					paramIndex++;
					
					if(argc > paramIndex)
					{
						suffix = argv[paramIndex];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"pic suffix");
					}
					paramIndex++;
					
					if(argc > paramIndex)
					{
						slideCount = atoi(argv[paramIndex]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"slideCount");
					}
					paramIndex++;
					
					if(argc > paramIndex)
					{
						slideSequenceStr = argv[paramIndex];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"slide sequence str");
					}
					paramIndex++;
					
					if(argc > paramIndex)
					{
						flvOut = argv[paramIndex];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"out put flv name");
					}
					paramIndex++;
					
					
					
					
					if(argc > paramIndex)
					{
						width = atoi(argv[paramIndex]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"width");
					}
					paramIndex++;
					if(argc > paramIndex)
					{
						height = atoi(argv[paramIndex]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"height");
					}	
					paramIndex++;
					
					
					if(argc > paramIndex)
					{
						duration = atoi(argv[paramIndex]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"duration (ms)");
					}	
					paramIndex++;
					
					
					if(argc > paramIndex)
					{
						mboxRootDir = argv[paramIndex];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(paramIndex,"Mailbox Root");
					}	
					paramIndex++;
					
					
					std::vector<SlideEvent> events;
					parseSlideEvents(slideSequenceStr,events);
					std::cout<<"Slide Transitions Found : "<<events.size()<<std::endl;
					createPPTSlideFLV(docId, slideDir, slideCount, suffix, events,width, height, flvOut,duration, mboxRootDir);
					
					
				}
				else if (cmdStr == "dtp2flv") 
				{
					
					std::cout<<"Argc : "<<argc<<std::endl;
					
					String dumpFileName = ""; 
					if(argc > 2)
					{
						dumpFileName = argv[2];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(2,"Dump File Name");
					}
					std::cout<<"Parsing Dump File : ["<<dumpFileName<<"]"<<std::endl;
					
					String confKey = "conf1";
					if( argc > 3)
					{
						confKey = argv[3];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(3,"Conference Key");
					}
					String streamName = "screen1";
					if( argc > 4)
					{
						streamName = argv[4];
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(4,"Stream Name");
					}
					
					int width = 320;
					if(argc > 5)
					{
						width = atoi(argv[5]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(5,"FLV Width");
					}
					int height = 240;
					if(argc > 6)
					{
						height = atoi(argv[6]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(6,"FLV Height");
					}
					int duration = 0;
					String mboxRoot = "";
					
					if(argc > 7)
					{
						duration = atoi(argv[7]);
					}
					else
					{
						DDSS_ARG_MISSING_ERROR(7,"Duration (ms)");
					}

					if(argc > 8)
					{
						mboxRoot = argv[8];
					}	
					else
					{
						DDSS_ARG_MISSING_ERROR(8,"Mailbox Root");
					}
					
					
					//UNCOMMENT FOR CHUNK PROCESSING FROM DUMP FILE
					Screen* screen = app.newScreen(confKey, streamName);
					String cursorFile = mboxRoot;
					cursorFile += "/arrow.bmp";
					RGBImage* cimg = loadImageFile(cursorFile,14,21);
					ScopedPointer<RGBImage> cimgPtr(cimg);
					screen->setCursorImage(cimg);
					if(!cimg)
					{
						std::cerr<<"Failed to load cursor image : "<<cursorFile<<std::endl;
					}
					//ScopedPointer<Screen> screenPtr(screen);
					ScreenPub* pub = screen->createPublisher();
					pub->setDumpOn(false); //offline run no need to dump bin file
					pub->init();
					std::ifstream dumpFile(dumpFileName.c_str(),std::ios::binary | std::ios::in);
					int chunkIndex = 0;
					
					size_t fileSize = Helper::getFileSize(dumpFileName);
					std::cout<<"DUMP FILE SIZE : "<<fileSize<<" bytes!"<<std::endl;
					u32 firstTimeStamp = 0;
					u32 lastTimeStamp = 0;
					while(dumpFile.is_open() && !dumpFile.eof())
					{
						ByteBufferOutputStream bos;
						int len = 0;
						std::cout<<"Going to read chunk : "<<chunkIndex<<" ..."<<std::endl;
						dumpFile.read((char*)&len,4);
						
						u32 timeStamp = 0;
						dumpFile.read((char*)&timeStamp,4);
						if(firstTimeStamp == 0)
						{
							firstTimeStamp = timeStamp;
						}
						
						std::cout<<"Time stamp : "<<timeStamp<<std::endl;
						std::cout<<"Chunk ("<<(chunkIndex)<<")Length : "<<len<<std::endl;
						if(len > 0)
						{
							lastTimeStamp = timeStamp;
							std::cout<<"DURATION till now : "<<(lastTimeStamp - firstTimeStamp)<<"ms"<<std::endl;
							char* buf = new char[len];
							ScopedArray<char> bufPtr(buf);
							dumpFile.read(buf,len);
							
							if(chunkIndex == 0)
							{
								pub->handleOpen(buf,len, bos);
								screen->initGenerateFLV(width,height);
							}
							else
							{
								screen->postData(buf,len,timeStamp);
							}
							chunkIndex++;
						}
						else
						{
							std::cerr<<"Chunk ("<<chunkIndex<<") length is 0"<<std::endl;
							break;
						}
						
						std::cout<<"!!!! ---- File Position : "<<dumpFile.tellg()<<"/"<<fileSize<<std::endl;
					}
					RGBImage* defImg = getDefaultScreenImage(width,height,mboxRoot);
					ScopedPointer<RGBImage> defImgPtr(defImg);
					screen->addLastFLVTag(duration, defImg); //getDefaultScreenImage(width,height,mboxRoot));
				}
				else if(cmdStr == "help")
				{
					::printUsage(argv[0],std::cout);
					return 0;
				}
				else
				{
					std::cerr<<"[ERROR] Unknown Cmd String : "<<cmdStr<<std::endl;
					::printUsage(argv[0],std::cerr);
					return -1;
				}
			}
		}
		
	}
	DDSS_VERBOSE("main")<<"bye bye!"<<std::endl;
	return 0;
	
}


