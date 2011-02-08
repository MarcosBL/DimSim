#ifndef _dimdim_flv_flvsplitter_h_
#define _dimdim_flv_flvsplitter_h_

#include "FLVHeader.h"
#include "FLVTag.h"
#include "FLVFile.h"
namespace dm
{
	class FLVSplitter : public FLVReadHandler
	{
	public:
		FLVSplitter(String flvFile);
		virtual ~FLVSplitter();
		void split(String videoFile, String audioFile);
		
		void handleReadHeader(FLVHeader* header);
		void handleReadTag(FLVTag* tag);
		void handleReadFinish();
	private:
		String flvFileName;
		FLVWriter flvWriterVideo;
		FLVWriter flvWriterAudio;
		u32 audioTagCount;
		u32 videoTagCount;
		u32 totalTagCount;
	};
}
#endif
