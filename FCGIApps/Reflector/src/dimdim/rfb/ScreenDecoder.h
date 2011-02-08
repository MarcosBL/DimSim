#ifndef _ddss_rfb_screen_decoder_h_
#define _ddss_rfb_screen_decoder_h_
#include "RFBHelper.h"
#include "ScreenBuffer.h"
#include "TightDecoder.h"
namespace dm
{
	class TightDecoder;
	class ScreenDecoder;
	class ScreenDecoderCallback;
	
	enum ScreenDecoderState
	{
		SDS_INVALID = 0,
		SDS_EXPECTING_SERVER_CMD ,
		SDS_EXPECTING_RECT_HEADER,
		SDS_EXPECTING_RECT_DATA,
		SDS_ERROR
	};

	
	class ScreenDecoder : public Stateful<ScreenDecoderState>
	{
		friend class TightDecoder;
	public:
		ScreenDecoder(ScreenBuffer& buffer);
		virtual ~ScreenDecoder();
		bool decode(InputStream& in, ScreenDecoderCallback* cb);
		virtual void rectComplete(ScreenRectangle& r);
		virtual void updateComplete(bool markerFound);
		virtual void errorOccured(int errorCode, const String errorMsg);
	protected:
		bool decodeServerCmd(InputStream& in);
		bool decodeRectHeader(InputStream& in);
		bool decodeRectData(InputStream& in);
		bool decodeRawRectangle(InputStream& in,ScreenRectangle& cur_rect);
			
		void resetAll();
		void resetRect();
	private:
		ByteBuffer pendingData;
		ScreenDecoderCallback *callBack;
		u8 serverCommand;
		u16 numRectsMax;
		u16 numRectsCurrent;
		u32 numRectsTotal;
		bool markerExpected;
		ScreenRectangle currentRect;
		ScreenBuffer& screenBuffer;
		TightDecoder *tightDecoder;
		
		size_t updateCount;
		size_t overallRectCount;
		
		DDSS_FORCE_BY_REF_ONLY(ScreenDecoder);
		
	};
};

#endif

