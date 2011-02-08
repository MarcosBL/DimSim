#include "Dump.h"

namespace dm
{

	bool Dump::hexDump(std::ostream& os, const void* buf, size_t bufLen)
	{
	#ifdef DDSS_HEXDUMP_ENABLED	
		const unsigned char* buffer = (const unsigned char*)buf;
		unsigned int lineWidth = 24;
		bool isAsciiEnabled = true;
		
		unsigned int width = (unsigned int)lineWidth;
		unsigned int maxLines = (unsigned int)(bufLen / lineWidth);
		if(bufLen % lineWidth != 0)
		{
			maxLines++;
		}
		for(unsigned int lineIndex=0; lineIndex < maxLines; lineIndex++)
		{
			std::ostrstream hexLine;
			std::ostrstream txtLine;

			for(unsigned int i = 0; i < width; i++)
			{
				unsigned int index = lineIndex * width + i;
				//std::cout<<"index : "<<i<<std::endl;
				
				if(index < bufLen)
				{
					unsigned char octet = buffer[index];
					if(octet >= 16)
					{
						hexLine<<" "<<(std::hex)<<(unsigned short)octet<<(std::dec);
					}
					else
					{
						hexLine<<" 0"<<(std::hex)<<(unsigned short)octet<<(std::dec);
					}
					if(isAsciiEnabled)
					{
						if(octet > 23 && octet < 127)
						{
							txtLine<<octet;						
						}
						else
						{
							txtLine<<".";
						}
					}
				}
				else
				{
					hexLine<<"   ";
				}



			}
			hexLine<<std::ends;
			txtLine<<std::ends;
			
			
			{
				char lineNum[32];
				sprintf(lineNum,"-%6x-",lineIndex);
				for(size_t s = 0; s < strlen(lineNum); s++)
				{
					if(lineNum[s] == ' ')
					{
						lineNum[s] = '0';
					}
				}
				os<<lineNum<<"  ";
			}
			os<<hexLine.str();
			if(isAsciiEnabled)
			{
				os<<"     "<<txtLine.str();
			}
			os<<std::endl;
		}
	#endif
		return true;
	}
	bool Dump::binDump(const char* fileName, const void* buf, size_t len)
	{
		
		if(fileName && buf && len)
		{
			std::ofstream os(fileName, std::ios::binary | std::ios::out);
			if(os.is_open())
			{
				os.write((const char*)buf,(std::streamsize)len);
				os.flush();
				os.close();
				return true;
			}
		}
		return false;
	}
	
	

}


