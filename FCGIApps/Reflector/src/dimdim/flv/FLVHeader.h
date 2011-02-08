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
    File Name  : dFLVHeader.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_FLV_HEADER_H_
#define _DIMDIM_FLV_HEADER_H_
#include "FLVConstants.h"
#include <bitset>
namespace dm
{
	typedef std::bitset<8> Byte;
	///
	///	FLV Header class
	///
	class DSSFLVAPI FLVHeader
	{
	public:

		FLVHeader();
		virtual ~FLVHeader();

		bool hasValidMagic() const{ return magic[0] == 'F' && magic[1] == 'L' && magic[2] == 'V'; }
		void clear(){ memset(magic,0,3); flags = 0; version = 0; dataOffset = 9; }
		void init(){ clear(); memcpy(magic,FLVConstants::getInstance()->FLV_MAGIC,3); version = 1; dataOffset = 9; }
		
		void load(InputStream* input);
		void save(OutputStream* output);
		
		void dumpInfo(std::ostream& os);
		
		bool getHasAudio() const;
		bool getHasVideo() const;
		
		void setHasAudio(bool b);
		void setHasVideo(bool b);

	private:
		u8 magic[3];
		u8 version;
		Byte flags;
		u32 dataOffset;
	};
};
#endif
