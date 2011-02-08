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
    File Name  : dFLVHeader.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "FLVHeader.h"

namespace dm
{
	
	FLVHeader::FLVHeader()
	{ 
		clear(); 
	}
	FLVHeader::~FLVHeader()
	{
	}
	void FLVHeader::load(InputStream* input)
	{
		clear();

		input->read(magic,3);
		input->readByte(&version);
		
		u8 flagByte = 0;
		input->readByte(&flagByte);
		flags = flagByte;

		input->readLong(&dataOffset);
		
	}
	void FLVHeader::save(OutputStream* output)
	{
		output->write(magic,3);
		output->writeByte(version);

		u8 flagByte = (u8)flags.to_ulong();
		output->writeByte(flagByte);

		output->writeLong(dataOffset);
	}
	void FLVHeader::dumpInfo(std::ostream& os)
	{
		std::cout<<"---------------------------------------------"<<std::endl;
		std::cout<<"* FLV Header"<<std::endl;
		std::cout<<"---------------------------------------------"<<std::endl;
		std::cout<<"+  Magic : "<<magic[0]<<magic[1]<<magic[2]<<std::endl;
		std::cout<<"+  Version : 0x"<<std::hex<<(u32)version<<std::dec<<std::endl;
		std::cout<<"+  Flags : "<<flags<<" ( AUDIO : "<<DSS_VAL_OF_BOOL(getHasAudio())<<" /  VIDEO : "<<DSS_VAL_OF_BOOL(getHasVideo())<<")"<<std::endl;
		std::cout<<"+  Header Size : "<<dataOffset<<std::endl;
		std::cout<<"---------------------------------------------"<<std::endl;
	}
	bool FLVHeader::getHasAudio() const
	{
		return flags.test(2);
	}
	bool FLVHeader::getHasVideo() const
	{
		return flags.test(0);
	}
	void FLVHeader::setHasAudio(bool b)
	{
		flags.set(2,b);

	}
	void FLVHeader::setHasVideo(bool b)
	{
		flags.set(0,b);
	}

};
