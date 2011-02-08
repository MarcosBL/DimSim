#ifndef _DDSS_SCREEN_H_
#define _DDSS_SCREEN_H_
#include "../rfb/api.h"
#include "ScreenClient.h"
#include "ScreenPub.h"
#include "ScreenSub.h"
#include "../flv/api.h"

namespace dm
{
	class ScreenClient;
	class ScreenPub;
	class ScreenSub;
	class ScreenInfo;
	class Screen : public ScreenDecoderCallback
	{
	public:
		friend class ScreenSub;
		Screen(const String confKeyStr, const String streamName);
		virtual ~Screen();

		u32 getSubCount() const{ return subs.size(); }
		void setJpegLevelOut(int level){ jpegLevelOut = level; }
		ScreenPub* getPublisher();
		ScreenSub* getSubscriber(const String id);

		ScreenPub* createPublisher();
		ScreenSub* createSubscriber();

		void deletePublisher();
		void deleteSubscriber(const String id);
		void deleteAllSubscribers();
		void init(ScreenInfo& screenInfo);

		const ScreenInfo& getInfo() const{ return info; }


		void cleanup();

		bool postData(const void* buf, size_t len, u32 timeStamp);
		void writeKeyFrame(OutputStream& out);
		

		void rectComplete(ScreenRectangle& r);
		void updateComplete(size_t rectCount, size_t maxRects, bool markerFound);
		void errorOccured(int errorCode, const String errorMsg);
		void resizeReceived(size_t w, size_t h);
		
		void pruneSubs(u32 maxIdleAgeMs);
		bool isPubIdleExceeded(u32 maxIdleAgeMs);

		void lockCheck();
		RWLock& getRWLock(){ return screenRWLock; }
		bool getGenerateFLVFlag() const{ return generateFLV; }
		bool initGenerateFLV(int w, int h);
		
		const String& getConfKey() const{ return confKey; }
		const String& getStreamName() const{ return streamName; }
		void addLastFLVTag(u32 duration, RGBImage* defaultImg);		
		void setCursorImage(RGBImage* cimg){ cursorImg = cimg; }
		const String appendMailbox(const String& root, const String& mbox)
		{
		    String ret = root;
		    ret +="/";
		    ret += mbox[0];

		    ret+= "/";
		    ret+= mbox.substr(0,2);


		    ret+= "/";
		    ret+= mbox.substr(0,3);


		    ret+= "/";
		    ret+= mbox;

		    return ret;

		}
		void setRADParams(String sdimdimId, String sroomId, String ssessionId, String sinstallationId = "")
		{
			DDSS_INFO("Screen")<<"setRADParams("<<sdimdimId<<","<<sroomId<<","<<ssessionId<<") - mbox root : "<<mailboxRoot<<std::endl;
			dimdimId=sdimdimId;
			roomId = sroomId;
			sessionId = ssessionId;
			installationId = sinstallationId;
			
			String rootDir = mailboxRoot;

			if(installationId.size() > 0)
			{
				rootDir += "/";
				rootDir += installationId;
			}
			String modRoot = appendMailbox(rootDir, dimdimId);
			
			std::ostrstream os;
			os<<modRoot<<"/"<<roomId<<"/"<<sessionId<<std::ends;
			radDir = os.str();
			os.rdbuf()->freeze(false);
			
			
            DDSS_INFO("Screen")<<"RAD Dir : "<<radDir<<std::endl;
		}
		const String getRADDir() const
		{
			return radDir;
		}
		void setMailboxRoot(String sroot){ mailboxRoot = sroot; }
	protected:
		bool initFLV(int w, int h);
		void addTag(size_t rectCount, size_t maxRects, bool markerFound, bool keyFrame);
		void closeFLV();
		RGBImage* getScreenImage();
		//parsing methods
		bool parseUpdate(InputStream& in);
		u32  readCompactLen(InputStream& in, OutputStream& dataOut);
	private:
		String dimdimId, roomId, sessionId, installationId, mailboxRoot, radDir;
		RWLock screenRWLock;
		Lock screenLock;
		String confKey, streamName;
		ScreenInfo info;
#ifdef DDSS_USE_NEW_DECODER
		ScreenDecoder2 *decoder;
#else
		ScreenDecoder *decoder;
#endif
		ScreenPub *publisher;
		ScreenBuffer screenBuffer;
		int jpegLevelOut;
		std::map<String,ScreenSub*> subs;
		DDSS_FORCE_BY_REF_ONLY(Screen);
		std::ofstream rectDump;
		int captureIndex;
		void captureScreenToJPEG();
		bool generateFLV;
		FLVWriter flvFile;
		ScreenVideoFrame svFrame;
		s32 updatedRects;
		TimeStampGenerator tsGen;
		u32 tagsEncoded;
		u32 timeStamp;
		u32 timeStampFirst;
		u32 lastTagTimestamp;
		s32 flvWidth, flvHeight;
		RGBImage* cursorImg;
		s32 mouseX, mouseY;
	};
};
#endif

