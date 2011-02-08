#include "FLVSplitter.h"

namespace dm
{

	FLVSplitter::FLVSplitter(String flvFile) : flvFileName(flvFile)
	{
		audioTagCount = videoTagCount = totalTagCount = 0;
	}
	FLVSplitter::~FLVSplitter()
	{
		
	}
	void FLVSplitter::split(String videoFile, String audioFile)
	{
		audioTagCount = videoTagCount = totalTagCount = 0;
		flvWriterVideo.init(videoFile.c_str(),true,false);
		flvWriterAudio.init(audioFile.c_str(),true,false);
		FLVFile flvReader;
		flvReader.load(flvFileName,this);
	}
	
	void FLVSplitter::handleReadHeader(FLVHeader* header)
	{
		//std::cout<<"Tag reading begins!!!"<<std::endl;
		std::cout<<"AVSplitter - started"<<std::endl;
		audioTagCount = videoTagCount = totalTagCount = 0;
		
	}
	void FLVSplitter::handleReadTag(FLVTag* tag)
	{
		//std::cout<<"Handling FLV Tag : "<<std::endl;
		if(tag->isAudioTag())
		{
			//std::cout<<">>>> Audio Tag...("<<tag->getDataSize()<<" bytes)"<<std::endl;
			flvWriterAudio.writeTag(*tag);
			audioTagCount++;
		}
		else if(tag->isVideoTag())
		{
			//std::cout<<">>>> Video Tag...("<<tag->getDataSize()<<" bytes)"<<std::endl;
			flvWriterVideo.writeTag(*tag);
			videoTagCount++;
		}
		totalTagCount++;
	}
	void FLVSplitter::handleReadFinish()
	{
		std::cout<<"AVSplitter - finished (Total Tags:"<<totalTagCount<<")(Audio:"<<audioTagCount<<")(Video:"<<videoTagCount<<")!!"<<std::endl;
		flvWriterVideo.close();
		flvWriterAudio.close();
		
	}
}
