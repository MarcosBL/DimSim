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
    File Name  : dFLVTag.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FLV_TAG_H_
#define _DIMDIM_FLV_TAG_H_
#include "FLVConstants.h"

namespace dm
{
	///
	///	AN FLV Tag class
	///
	class DSSFLVAPI FLVTag
	{
	public:
		static FLVTag* createDummyTag(const long);
		static FLVTag* createMetadataTag(const String methodName, amf::AMFVariant* data);
		static FLVTag* createAVMetaTag(u32 durationMillis, u8 videoCodec = 0x02, u8 audioCodec=0x06, bool canSeekToEnd=true);

		FLVTag();
		virtual ~FLVTag();
		
		void load(InputStream* input);
		void save(OutputStream* output);

		void dumpInfo(std::ostream& os);
		void printAudioInfo(std::ostream& os);
		void printVideoInfo(std::ostream& os);
		void clear();

		bool isEmptyTag() const{ return emptyTag; }
		void setEmptyTag(bool b){ emptyTag = b; }
		
		u8 getTagType() const{ return tagType; }
		u32 getDataSize() const{ return dataSize; }
		u32 getTimeStamp() const{ return timeStamp; }
		u8 getTimeStampExtended() const{ return timeStampExtended; }
		u32 getTimeStampFull() const{ return 256 * 256 * 256 * timeStampExtended + timeStamp; }
		u32 getStreamId() const{ return streamId; }
		
		void setStreamId(u32 id){ streamId = id; }
		const ByteBuffer& getTagData() const{ return tagData; }
		ByteBuffer& getTagData(){ return tagData; }
		
		bool isAudioTag() const{ return tagType == FLVConstants::getInstance()->FLV_TAG_TYPE_AUDIO; }
		bool isVideoTag() const{ return tagType == FLVConstants::getInstance()->FLV_TAG_TYPE_VIDEO; }
		

		void setTimeStamp(u32 ts)
		{ 
			u32 magic = 256 * 256 * 256;
			timeStamp = ts % magic; 
			timeStampExtended = ts / magic;
		}
		void makeVideoTag(){ tagType = FLVConstants::getInstance()->FLV_TAG_TYPE_VIDEO; }
		void makeAudioTag(){ tagType = FLVConstants::getInstance()->FLV_TAG_TYPE_AUDIO; }
		void makeMetadataTag(){ tagType = FLVConstants::getInstance()->FLV_TAG_TYPE_METADATA; }
		void calculateDataSize(){ dataSize = (u32)tagData.getLength(); }
		bool isKeyFrame();
		void makeKeyFrame();
	private:
		bool emptyTag;
		u8  tagType;
		u32 dataSize; //medium
		u32 timeStamp; //medium
		u8	timeStampExtended;
		u32 streamId; //medium
		ByteBuffer tagData;

		DSS_FORCE_BY_REF_ONLY(FLVTag);
	};
};
#endif
