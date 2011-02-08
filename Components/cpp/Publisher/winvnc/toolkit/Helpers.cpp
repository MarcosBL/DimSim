// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

#include "../stdhdrs.h"
#include "ByteBuffer.h"
#include "Helpers.h"
#include <strstream>
#include <fstream>

extern "C"
{
	#include "pacparser.h"
}

void* DimdimHelpers::customRealloc(void *ptr, size_t size)
{
	/* There might be a realloc() out there that doesn't like reallocing
	NULL pointers, so we take care of it here */
	if(ptr)
		return realloc(ptr, size);
	else
		return malloc(size);
}

size_t DimdimHelpers::ContentWriteCallback(void *ptr, size_t size, size_t nmemb, void *data)
{
	size_t realsize = size * nmemb;
	ByteBuffer* buf = (ByteBuffer*)data;
	buf->append(ptr, realsize);
	return realsize;
}

size_t DimdimHelpers::HeadersWriteCallback(void *ptr, size_t size, size_t nmemb, void *data)
{
	size_t realsize = size * nmemb;
	ByteBuffer* buf = (ByteBuffer*)data;
	buf->append(ptr, realsize);
	return realsize;
}

unsigned int DimdimHelpers::ValidResponseHeaders(ByteBuffer& dHeaders, std::string cmd)
{
	if (dHeaders.getLength() <= 0)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("Detected zero-length header size!!")); 
		return 0;
	}

	if (!dHeaders.getData())
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("Detected zero-length header content!!"));
		return 0;
	}

	std::string response((char*)dHeaders.getData());
	
	// HTTP/1.1 <3-DIGIT RETURN CODE>
	response = response.substr(0, 15);
	if (response.find("200 OK") > 0 && response.find("200 OK") <= response.length())
	{
		return 1;
	}

	// Log as ERROR
	vnclog.Print(LL_DIMDIM, VNCLOG("Received %s for an issue of \"%s\"\n"), response.c_str(), cmd.c_str());

//	if (0 == response.compare("HTTP/1.1 500"))
//		return 2;

	return 0;

}

std::string DimdimHelpers::GenerateStreamName(int iLength)
{
	std::string strReturn;

	for( int i = 0 ; i < iLength ; ++i )
	{
		int iNumber;

		// Seed the random-number generator with TickCount so that
		// the numbers will be different every time we run.
		srand( (unsigned int)( (i+1)*iLength*GetTickCount() ) );

		iNumber = rand()%122;
		if( 48 > iNumber )
			iNumber += 48;
		if( ( 57 < iNumber ) &&
			( 65 > iNumber ) )
			iNumber += 7;
		if( ( 90 < iNumber ) &&
			( 97 > iNumber ) )
			iNumber += 6;
		strReturn.push_back((char)iNumber);
	}

	return strReturn;
}


void DimdimHelpers::PrepareURL(std::string &url, std::string cmd, 
							   std::string handlerID, std::string role)
{
	url.append(CurlClientData::getInstance()->m_peerAddress);
	url.append(role);
	url.append("/");
	url.append(cmd);
	url.append("/");
	if (0 == handlerID.size())
	{
		url.append("0");
	}
	else
	{
		url.append(handlerID);
	}
}

std::string DimdimHelpers::ParsePACFile(std::string filePath, std::string url)
{
	// filePath is the location of PAC File in %APPDATA%
	// Assuming that url is of the form http://ip:port/screenshare/....

	pacparser_init();
	char *proxy = NULL;
	pacparser_parse_pac(filePath.c_str());
	std::string host(url);
	host.assign(host.substr(7, host.length()));
	host.assign(host.substr(0, host.find_first_of('/')));
	proxy = pacparser_find_proxy(url.c_str(), host.c_str());

	std::string proxyURL("");
	
	if(proxy) 
	{
		proxyURL.assign(proxy);
	}
	pacparser_cleanup();
	return proxyURL;
}

bool Dump::hexDump(std::ostream& os, const void* buf, size_t bufLen)
{
	
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
	return true;
}

bool Dump::binDump(const char* fileName, const void* buf, size_t len)
{
	
	if(fileName && buf && len)
	{
		std::ofstream os(fileName, std::ios::binary | std::ios::out | std::ios::app);
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

TimeStampGenerator::TimeStampGenerator()
{
	reset();
}
TimeStampGenerator::~TimeStampGenerator()
{
}
VCard32 TimeStampGenerator::getCurrentTimeMillis()
{
	return (VCard32)timeGetTime();
}
VCard32 TimeStampGenerator::getTimeStamp()
{
	return getCurrentTimeMillis() - startTime;
}
void TimeStampGenerator::reset()
{
	startTime = getCurrentTimeMillis();
}

CurlClientData* CurlClientData::instance = NULL;

CurlClientData::CurlClientData()
{
	m_subscriptionID.clear();
	m_streamName.clear();

	m_peerAddress.clear();
	m_peerPort.clear();

	// Default values
	m_maxRetries = 2;
	m_maxWaitTime = 7;
}

CurlClientData* CurlClientData::getInstance()
{
	if(NULL == instance)
	{
		instance = new CurlClientData();
	}

	return instance;
}

void CurlClientData::removeInstance()
{
	if (instance)
	{
		delete instance;
	}

	instance = NULL;
}