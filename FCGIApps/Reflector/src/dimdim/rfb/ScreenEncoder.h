#ifndef _ddss_screen_encoder_h_
#define _ddss_screen_encoder_h_
#include "RFBHelper.h"
#include "ScreenBuffer.h"
#include "TightEncoder.h"
#include "region.h"
#include "zlib.h"
#define NUM_ENCODINGS  10
namespace dm
{
	class ScreenEncoder;
	class TightEncoder;
	class ScreenEncoder
	{
		friend class TightEncoder;
	public:
		typedef struct CL_SLOT
		{
			  CARD16 fb_width;
			  CARD16 fb_height;
			
			  RFB_PIXEL_FORMAT format;
			  void *trans_table;
			  ScreenBuffer::TRANSFUNC_PTR trans_func;
			
			  RegionRec pending_region;
			  RegionRec copy_region;
			  int copy_dx, copy_dy;
			
			  CARD16 temp_count;
			  unsigned char auth_challenge[16];
			  unsigned char enc_prefer;
			  unsigned char enc_enable[NUM_ENCODINGS];
			  //std::map<int,unsigned char> enc_enable;
			  int compress_level;
			  int jpeg_quality;
			  z_stream zs_struct[4];
			  int zs_active[4];
			  int zs_level[4];
			  size_t cut_len;
			  BoxRec update_rect;
			  unsigned int bgr233_f           :1;
			  unsigned int readonly           :1;
			  unsigned int connected          :1;
			  unsigned int update_requested   :1;
			  unsigned int update_in_progress :1;
			  unsigned int enable_lastrect    :1;
			  unsigned int enable_newfbsize   :1;
			  unsigned int newfbsize_pending  :1;
		
		}CL_SLOT;
		ScreenEncoder(ScreenBuffer& buffer);
		virtual ~ScreenEncoder();
		void addRect(ScreenRectangle& rect);
		void addRectPrivate(ScreenRectangle& rect);
		void setFlashClient(bool b);
		void createUpdate(OutputStream& out);
		CL_SLOT* getSlot(){ return &slot; }
		void sendUpdate(OutputStream& out);
		void clearRects();

		void setJpegLevelOut(int level);
		void bumpMaxSizeUp();
		void bumpMaxSizeDown();
	private:
		std::vector<ScreenRectangle> pendingRectList;
		ScreenBuffer& screenBuffer;
		TightEncoder* tightEncoder;
		bool flashClient;
		CL_SLOT slot;
		int sendCount,overflowCount, underflowCount;
		size_t maxSize;
		Lock rectLock;
		std::ofstream dumpFile;
		int jpegLevelOut;
		DDSS_FORCE_BY_REF_ONLY(ScreenEncoder);
		
	};
};
#endif
