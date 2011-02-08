#ifndef _dimdim_rfb_screen_decoder2_h_
#define _dimdim_rfb_screen_decoder2_h_
#include "RFBHelper.h"
#include "ScreenBuffer.h"
#include "TightDecoder.h"

namespace dm
{

	class TightDecoder2;
	class ScreenDecoder2;
	class ScreenDecoderCallback;
	class ScreenDecoder2
	{
		friend class TightDecoder2;
	public:
		enum State
		{
			STATE_INVALID = 0,
			STATE_SERVER_CMD_EXPECTED,
			STATE_RECT_LIST_EXPECTED,
			STATE_RECT_HEADER_EXPECTED,
			STATE_RECT_DATA_EXPECTED,
			STATE_ERROR
		};
		ScreenDecoder2(ScreenBuffer& sbuf);
		virtual ~ScreenDecoder2();
		bool decode(InputStream& in, ScreenDecoderCallback* sdCallback, bool recursiveCall = false);
		State getState() const{ return state; }
	protected:
		void setState(State s){ state = s; }
		void setPendingData(const void* pendingData, size_t pendingDataLen, State s);
		bool hasPendingData() const;
		void clearPendingData();
		
		bool decodeServerCommand(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeServerCommandScreenBuffer(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeServerCommandCutText(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeServerCommandBell(InputStream& in, ScreenDecoderCallback* sdCallback);
		

		bool decodeRectList(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeRectHeader(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeRectData(InputStream& in, ScreenDecoderCallback* sdCallback);
		
		bool decodeRawRectangle(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeTightRectangle(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeCopyRectangle(InputStream& in, ScreenDecoderCallback* sdCallback);
		
		bool decodeLastRectangle(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeCursorPosition(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeRichCursor(InputStream& in, ScreenDecoderCallback* sdCallback);
		bool decodeNewFBSize(InputStream& in, ScreenDecoderCallback* sdCallback);
		
		void resetRects();
		void resetAll();
		
		void rectComplete(ScreenDecoderCallback* sdCallback);
		void updateComplete(ScreenDecoderCallback* sdCallback, bool markerMet);
		void decoderError(ScreenDecoderCallback* sdCallback, const String errorMsg, int errorCode=-1);
	private:
		ScreenBuffer& screenBuffer;
		TightDecoder2* tightDecoder; 
		State state;
		ScreenRectangle currentRect;
		size_t rectCount;
		size_t rectsRemaining;
		size_t rectsTotal;
		bool markerExpected;
		ByteBuffer pendingData;
		size_t overallRectCount;
		size_t overallUpdateCount;
		DDSS_FORCE_BY_REF_ONLY(ScreenDecoder2);
	};
}
#endif

