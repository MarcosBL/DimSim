#include "Helper.h"
#include "Timer.h"
#include "Logger.h"
#include <sys/stat.h>
namespace dm
{
	u32 Helper::currentTimeMillis()
	{
		return Timer::currentTimeMillis();
	}
	const String Helper::getExecutableDirectory()
	{
		char fileName[1024];
		memset(fileName,0,1024);
#ifdef _WIN32
		GetModuleFileNameA(0,fileName,1024);

		size_t len = strlen(fileName);
		for(size_t s = len - 1; s > 0; s--)
		{
			if(fileName[s] == '\\')
			{
				fileName[s] = 0;
				return fileName;
			}
		}
		return ".";
#else
                long size;
                char *ptr;
                size = pathconf(".", _PC_PATH_MAX);
                ptr = getcwd(fileName, (size_t)size);

                return fileName;
#endif
	}

	const String Helper::trimString(const String str)
	{
		if(str.size() == 0)
		{
			return str;
		}
		size_t start = str.size();
		size_t end = str.size();
		for(size_t i = 0; i < str.size(); i++)
		{
			if( str[i] != ' ' 
			    && str[i] != '\t'
				&& str[i] != '\b' 
				&& str[i] != '\r' 
				&& str[i] != '\n' )
			{
				start = i;		
				break;
			}
		}
		if(start == str.size())
		{
			return "";
		}
		else
		{
			end = start;
		}
		for(size_t i = str.size()-1; i > start;  i--)
		{
			if( str[i] != ' ' 
			    && str[i] != '\t'
				&& str[i] != '\b' 
			    && str[i] != '\r'
				&& str[i] != '\n')
			{
				end = i;
				break;
			}
		}
		if(end == start)
		{
			return "";
		}
		else
		{
			return str.substr(start, (end - start) + 1);
		}

	}
	void Random::seed(const u32 seedValue)
	{ 
		srand(seedValue); 
	}
	void Random::seedCurrentTime()
	{ 
		seed(Timer::currentTimeMillis());
		
	}
	void Random::resetSeed()
	{ 
		seed(1); 
	}
	u32  Random::nextRandom()
	{ 
		return (u32)rand(); 
	}
	size_t Compressor::estimateCompressedSize(size_t inSize)
	{
		return (size_t)((double)inSize * 1.1) + 12;
	}
	size_t Compressor::compress(const void* inBuffer, size_t inSize, void* outBuffer, size_t outSize)
	{
		//DSS_DEBUG("Entering:	Compressor::compress");
		//now compress the data
		uLongf destSize = (uLongf)outSize;
		
		int result = (int)compress2((Bytef*)outBuffer, (uLongf*)&destSize,(const Bytef*)inBuffer, (uLongf)inSize, Z_BEST_COMPRESSION);
		
		if(result != Z_OK)
		{
			if(result == Z_MEM_ERROR)
			{
				DDSS_WARNING("Compress")<<"Compress Failed!!!!Memory Error!!!"<<std::endl;
			}
			else if(result == Z_BUF_ERROR)
			{
				DDSS_WARNING("Compress")<<"Not enough room in buffer . out size : "<<destSize<<std::endl;
				
			}
			else if(result == Z_STREAM_ERROR)
			{
				DDSS_WARNING("Compress")<<"Compress Failed!!!! Stream Error!!!"<<std::endl;
			}
			//DSS_DEBUG("Leaving:		Compressor::compress");
			return 0;
		}
		else
		{
			//std::cout<<"         In Length : "<<inSize<<" --> Compressed Length : "<<destSize<<std::endl;
			//logger.verbose("compressed %d bytes to %d bytes!!!",inSize,destSize);
			//DSS_DEBUG("Leaving:		Compressor::compress");
			return (size_t)destSize;
		}
	}
	size_t Compressor::decompress(const void* inBuffer, size_t inSize, void* outBuffer, size_t outSize)
	{
		//DSS_DEBUG("Entering:	Compressor::decompress");

		uLongf destSize = (uLongf)outSize;
		//all data we require is ready so compress it into the source buffer, the exact
		//size will be stored in UnCompSize
		int result = uncompress((Bytef*)outBuffer, &destSize, (const Bytef*)inBuffer, (uLongf)inSize);
		if(result != Z_OK)
		{
			if(result == Z_MEM_ERROR)
			{
				DDSS_WARNING("Compress")<<"UnCompress Failed!!!!Memory Error!!!"<<std::endl;
			}
			else if(result == Z_BUF_ERROR)
			{
				//std::cerr<<"Not enough room in buffer . out size : "<<destSize<<std::endl;
				DDSS_WARNING("Compress")<<"UnCompress Failed!!!!Buffer Error!!!"<<std::endl;
			}
			else if(result == Z_STREAM_ERROR)
			{
				DDSS_WARNING("Compress")<<"UnCompress Failed!!!! Stream Error!!!"<<std::endl;
			}
			//DSS_DEBUG("Leaving:		Compressor::decompress");
			return 0;
		}
		else
		{
			//logger.verbose("inflated %d bytes to %d bytes",inSize,destSize);
			//DSS_DEBUG("Leaving:		Compressor::decompress");
			return (size_t)destSize;
		}
	}
	size_t Helper::getFileSize(const String fileName)
	{
		
		struct stat buf;
		if(::stat(fileName.c_str(), &buf) < 0)
		{
			std::cerr<<"Failed to get size for "<<fileName<<". error  = "<<errno<<std::endl;
			return -1;
		}
		return (size_t)buf.st_size;
	}
}



