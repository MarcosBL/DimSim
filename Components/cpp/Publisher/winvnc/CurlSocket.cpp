// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

class CurlSocket;

#include "stdhdrs.h"
#ifdef __WIN32__
#include <ws2tcpip.h>
#endif
#include "VTypes.h"
#include "Helpers.h"

#include "CurlSocket.h"

CurlSocketSystem::CurlSocketSystem()
{
	m_status = VTrue;
}

CurlSocketSystem::~CurlSocketSystem()
{
	if (m_status)
	{
		m_status = VFalse;
	}
}

// Socket implementation initialisation

CurlSocket::CurlSocket()
{
	// Initialise the curl subsystem
	curl_global_init(CURL_GLOBAL_ALL);

	// Initialize handler and clear out internal fields

	// Primary Initialization
	std::string appFolder(getenv("APPDATA"));
	appFolder.append("\\Dimdim\\dump.bin");
	osDump = new std::ofstream(appFolder.c_str(), std::ios::binary | std::ios::out);	// Handle to dump packets to local storage

	m_handler = NULL;							// Curl Handler

	m_uOperationType = 2;						//Enable only network by default
	m_blockSize = 32 * 1024;					// By default, choose 32KB blocks
	m_transactionWaitTime = 100;				// 100 ms by default. We allow a minimum of 20 ms

	// Auxiliary Initialization

	m_postCount = 0;
	m_totalDataSent = 0;						// In bytes
	m_totalDataCreated = 0;						// In bytes

	tg.reset();
	m_originalBlockSize = m_blockSize;			// A backup block size

	m_bSendFailed = false;
	m_eTimer = CreateEvent(NULL, true, false, "CurlTimer");

	// Set headers list

	m_headerList = NULL;
	m_headerList = curl_slist_append(m_headerList, "Expect:");
	m_headerList = curl_slist_append(m_headerList, "Content-Type: application/x-dimdim-dtp");
	m_headerList = curl_slist_append(m_headerList, "Connection: Keep-Alive");

	// Initialize proxy information to safe defaults

	m_proxyType.assign("DIRECT");
	m_proxyURL.assign("");

	m_bForceNode = false;
}

CurlSocket::~CurlSocket()
{
	// Cleanup
	osDump->close();
	delete osDump;
	osDump = NULL;
	curl_easy_cleanup(m_handler);
	CloseHandle(m_eTimer);

	// uninitialize global curl subsystem
	curl_global_cleanup();

	// free the headers list

	curl_slist_free_all(m_headerList);
	m_headerList = NULL;

}

VBool CurlSocket::Create()
{
	m_handler = curl_easy_init();
	return (NULL != m_handler);
}

bool CurlSocket::IsConnected()
{
	return m_bIsConnected;
}

VBool CurlSocket::Close(bool bForce)
{
	// Clean up any queues

	vnclog.Print(LL_DIMDIM, VNCLOG("Close Issued after approximately %d milliseconds\r\n"), tg.getTimeStamp());

//	if (!bForce && !m_bIsConnected)
//		return VTrue;

	omni_mutex_lock l(curl_lock);
	m_bIsConnected = false;
	out_queue.clear();

	vnclog.Print(LL_DIMDIM, VNCLOG("\r\nTotal Data Created = %d bytes\r\n"), m_totalDataCreated);
	vnclog.Print(LL_DIMDIM, VNCLOG("\r\nTotal Data Sent = %d bytes\r\n"), m_totalDataSent);
	vnclog.Print(LL_DIMDIM, VNCLOG("\r\nTotal Time Taken = %d seconds\r\n"), tg.getTimeStamp()/1000);

	vnclog.Print(LL_DIMDIM, VNCLOG("\r\nSetting connected status to false\r\n"));

	if (m_uOperationType >= 2 || bForce)
	{
		// Network Enabled


		// Issue a close on the reflector

		std::string url("");
		DimdimHelpers::PrepareURL(url, "close", CurlClientData::getInstance()->m_subscriptionID);

		CURLcode res;

		ByteBuffer chunk;
		ByteBuffer headers;

		curl_easy_setopt(m_handler, CURLOPT_URL, url.c_str());
		curl_easy_setopt(m_handler, CURLOPT_POSTFIELDS, "");

		curl_easy_setopt(m_handler, CURLOPT_WRITEFUNCTION, DimdimHelpers::ContentWriteCallback);
		curl_easy_setopt(m_handler, CURLOPT_WRITEDATA, &chunk);
		curl_easy_setopt(m_handler, CURLOPT_MAXFILESIZE, 128);
		curl_easy_setopt(m_handler, CURLOPT_TIMEOUT, (CurlClientData::getInstance()->m_maxWaitTime) * 2);
		curl_easy_setopt(m_handler, CURLOPT_CONNECTTIMEOUT, CurlClientData::getInstance()->m_maxWaitTime);
		//curl_easy_setopt(m_handler, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_0);

		// Set any proxy data if required.

		if (0 != m_proxyType.compare("DIRECT"))
		{
			// DIRECT can be ignored

			curl_easy_setopt(m_handler, CURLOPT_PROXY, m_proxyURL.c_str());

			if (0 == m_proxyType.compare("SOCKS"))
				curl_easy_setopt(m_handler, CURLOPT_PROXYTYPE, CURLPROXY_SOCKS5);
		}

		curl_easy_setopt(m_handler, CURLOPT_HEADERFUNCTION, DimdimHelpers::HeadersWriteCallback);
		curl_easy_setopt(m_handler, CURLOPT_WRITEHEADER, &headers);
		curl_easy_setopt(m_handler, CURLOPT_HTTPHEADER, m_headerList);

		res = curl_easy_perform(m_handler);

		if (CURLE_OK != res || (headers.getData() && 1 != DimdimHelpers::ValidResponseHeaders(headers, "close")))
		{
			if (headers.getData())
				vnclog.Print(LL_DIMDIM, "%s", headers.getData());
			vnclog.Print(LL_DIMDIM, VNCLOG("issuing close on the reflector failed\r\n\r\n"));
		}
		else
		{
			vnclog.Print(LL_DIMDIM, VNCLOG("Disconnected Successfully\r\n\r\n"));
		}
	}

	return VTrue;
}

VBool CurlSocket::Connect(VStringConst address, const VCard port)
{
	CurlClientData::getInstance()->m_peerAddress = DimdimHelpers::VTypeToString(address);
	CurlClientData::getInstance()->m_peerPort = DimdimHelpers::VTypeToString(port);

	m_bIsConnected = true;

	return VTrue;
}

void CurlSocket::dumpPackets(const char* buff, const size_t bufflen)
{
	osDump->seekp(0, std::ios::end);
	osDump->write((const char*)(buff),(std::streamsize)bufflen);
}

VString CurlSocket::GetPeerName()
{
	// Peer Name is the connected client name/ip
	return (VString)(CurlClientData::getInstance()->m_peerAddress.c_str());
}

VString CurlSocket::GetSockName()
{
	return VString("localhost");
}

void CurlSocket::setMaxRetries(VInt retries)
{
	CurlClientData::getInstance()->m_maxRetries = retries;
}

void CurlSocket::setMaxWaitTime(VCard32 secs)
{
	CurlClientData::getInstance()->m_maxWaitTime = secs;
}

VBool CurlSocket::SetTimeout(VCard32 secs)
{
	curl_easy_setopt(m_handler, CURLOPT_TIMEOUT, (CurlClientData::getInstance()->m_maxWaitTime) * 2);
	curl_easy_setopt(m_handler, CURLOPT_CONNECTTIMEOUT, CurlClientData::getInstance()->m_maxWaitTime);
	return VTrue;
}

VBool CurlSocket::Open(const char* buff, const VCard bufflen)
{
	importPACFile();

	CURLcode res;

	ByteBuffer chunk;
	ByteBuffer headers;

	std::string url("");
	DimdimHelpers::PrepareURL(url, "open");

	// reset any previously set options

	curl_easy_reset(m_handler);

	curl_easy_setopt(m_handler, CURLOPT_URL, url.c_str());
	curl_easy_setopt(m_handler, CURLOPT_POSTFIELDSIZE, bufflen);
	curl_easy_setopt(m_handler, CURLOPT_POSTFIELDS, buff);

	curl_easy_setopt(m_handler, CURLOPT_WRITEFUNCTION, DimdimHelpers::ContentWriteCallback);
	curl_easy_setopt(m_handler, CURLOPT_WRITEDATA, &chunk);

	curl_easy_setopt(m_handler, CURLOPT_HEADERFUNCTION, DimdimHelpers::HeadersWriteCallback);
	curl_easy_setopt(m_handler, CURLOPT_WRITEHEADER, &headers);

	curl_easy_setopt(m_handler, CURLOPT_HTTPHEADER, m_headerList);

	curl_easy_setopt(m_handler, CURLOPT_TIMEOUT, (CurlClientData::getInstance()->m_maxWaitTime) * 2);
	curl_easy_setopt(m_handler, CURLOPT_CONNECTTIMEOUT, CurlClientData::getInstance()->m_maxWaitTime);

	//curl_easy_setopt(m_handler, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_0);

	if (0 != m_proxyType.compare("DIRECT"))
	{
		// DIRECT can be ignored

		vnclog.Print(LL_DIMDIM, VNCLOG("Setting Proxy as [%s]\r\n"), m_proxyURL.c_str());
		curl_easy_setopt(m_handler, CURLOPT_PROXY, m_proxyURL.c_str());

		if (0 == m_proxyType.compare("SOCKS"))
			curl_easy_setopt(m_handler, CURLOPT_PROXYTYPE, CURLPROXY_SOCKS5);
	}

	bool bValidResponse = false;

	for (int i = 0; i < CurlClientData::getInstance()->m_maxRetries + 1; i++)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("Subscribing to reflector with URL %s : Attempt No. %d\r\n\r\n"), url.c_str(), (i + 1));
		res = curl_easy_perform(m_handler);

		vnclog.Print(LL_DIMDIM, "CURL RESULT = %d\r\n", res);
		if (headers.getData())
		{
			vnclog.Print(LL_DIMDIM, "%s", headers.getData());
		}
		else
		{
			vnclog.Print(LL_DIMDIM, VNCLOG("Got headers with empty data and length = %d bytes\r\n"), headers.getLength());
		}

		if (CURLE_OK != res || 1 != DimdimHelpers::ValidResponseHeaders(headers, "open") || NULL == chunk.getData())
		{
			if (headers.getData())
				vnclog.Print(LL_DIMDIM, "%s", headers.getData());
			continue;
		}
		else
		{
			bValidResponse = true;
			break;
		}
	}

	if (false == bValidResponse)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("Open on reflector didn't succeed\r\n"));
		m_bIsConnected = false;
		return VFalse;
	}

	std::string response((char*)chunk.getData());

	vnclog.Print(LL_DIMDIM, VNCLOG("Received %s as response\r\n\r\n"), response.c_str());

	int count = (response.length() - 1) - (response.find_last_of("$") + 1);
	CurlClientData::getInstance()->m_subscriptionID = response.substr(response.find_last_of("$") + 1, count);

	return VTrue;
}

VBool CurlSocket::SendExact(const char *buff, const VCard bufflen)
{
	// Put the data into the queue
	SendQueued(buff, bufflen);
	while (!out_queue.empty()) 
	{
		// Actually send some data

		if (!SendFromQueue())
		{
			return VFalse;
		}
	}

	return VTrue;
}

VBool CurlSocket::SendQueued(const char *buff, const VCard bufflen, bool bRFB)
{
	if (!m_bIsConnected)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("Detected lack of connectivity. Not enqueuing\r\n"));
		return VFalse;
	}

	omni_mutex_lock l(curl_lock);

	if (bufflen == 0 || buff == NULL)
		return VTrue;

	try
	{
		ByteBuffer* buf = new ByteBuffer();
		buf->append(buff, bufflen);
		out_queue.enqueue(buf);
		m_totalDataCreated += bufflen;
	}
	catch(...)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("SendQueued caught an exception!!"));
		return VFalse;
	}

	return VTrue;
}

VBool CurlSocket::SendFromQueue()
{
	// Is there something to send?


	if (!m_bIsConnected)
	{
		vnclog.Print(LL_DIMDIM, "Not Connected. Exiting\r\n");
		return VFalse;
	}

	if (out_queue.empty())
		return VTrue;

	omni_mutex_lock l(curl_lock);

	ByteBuffer* buffer = out_queue.dequeue(m_blockSize);
	ScopedPointer<ByteBuffer> bufPtr(buffer);


	if ((1 == m_uOperationType || 3 == m_uOperationType) && buffer->getLength() > 0)
	{
		dumpPackets((char*)buffer->getData(), buffer->getLength());
	}

	if (m_uOperationType >= 2)
	{
		std::string url("");

		try
		{
			DimdimHelpers::PrepareURL(url, "send", CurlClientData::getInstance()->m_subscriptionID);
			std::stringstream index;
			index.clear();
			index << m_postCount;
			url.append("/");
			url.append(index.str());
			index.clear();
		}
		catch(...)
		{
			vnclog.Print(LL_DIMDIM, VNCLOG("Caught exception in PrepareURL\r\n"));
			return VFalse;
		}

		// Try to send some data

		TimeStampGenerator tempGen;
		tempGen.reset();

		bool bValidResponse = false;
		for (int i = 0; i < CurlClientData::getInstance()->m_maxRetries + 1; i++)
		{
			CURLcode res;
			ByteBuffer chunk;
			ByteBuffer headers;

			try
			{
				curl_easy_setopt(m_handler, CURLOPT_URL, url.c_str());
				curl_easy_setopt(m_handler, CURLOPT_POSTFIELDSIZE, buffer->getLength());
				curl_easy_setopt(m_handler, CURLOPT_COPYPOSTFIELDS, buffer->getData());
				curl_easy_setopt(m_handler, CURLOPT_TIMEOUT, (CurlClientData::getInstance()->m_maxWaitTime) * 2);
				curl_easy_setopt(m_handler, CURLOPT_CONNECTTIMEOUT, CurlClientData::getInstance()->m_maxWaitTime);
				//curl_easy_setopt(m_handler, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_0);

				curl_easy_setopt(m_handler, CURLOPT_WRITEFUNCTION, DimdimHelpers::ContentWriteCallback);
				curl_easy_setopt(m_handler, CURLOPT_WRITEDATA, &chunk);

				curl_easy_setopt(m_handler, CURLOPT_HEADERFUNCTION, DimdimHelpers::HeadersWriteCallback);
				curl_easy_setopt(m_handler, CURLOPT_HEADERDATA, &headers);
				curl_easy_setopt(m_handler, CURLOPT_HTTPHEADER, m_headerList);

				if (0 != m_proxyType.compare("DIRECT"))
				{
					// DIRECT can be ignored

					curl_easy_setopt(m_handler, CURLOPT_PROXY, m_proxyURL.c_str());

					if (0 == m_proxyType.compare("SOCKS"))
						curl_easy_setopt(m_handler, CURLOPT_PROXYTYPE, CURLPROXY_SOCKS5);
				}
				
//				curl_easy_setopt(m_handler, CURLOPT_DEBUGFUNCTION, my_trace);
//				curl_easy_setopt(m_handler, CURLOPT_DEBUGDATA, &config);
//				curl_easy_setopt(m_handler, CURLOPT_VERBOSE, 1);

			}
			catch(...)
			{
				vnclog.Print(LL_DIMDIM, VNCLOG("Caught Exception in setting regular options to curl handle\r\n"));
				return VFalse;
			}

			try
			{
				res = curl_easy_perform(m_handler);
			}
			catch(...)
			{
				vnclog.Print(LL_DIMDIM, VNCLOG("Caught Exception in performing\r\n"));
				return VFalse;
			}

//			if (CURLE_OK != res || true != DimdimHelpers::ValidResponseHeaders(headers, "send"))
			if ((CURLE_OK != res) || (1 != DimdimHelpers::ValidResponseHeaders(headers, "send")))
			{
				// A 200 or a 500 doesn't warrant retries.
				// Don't worry about CURL_RESULT either.

				logDump(buffer->getData(), buffer->getLength(), "SEND PACKET");

				vnclog.Print(LL_DIMDIM, "CURL RESULT = %d\r\n", res);
				if (headers.getData())
				{
					vnclog.Print(LL_DIMDIM, "%s", headers.getData());
				}
				else
				{
					vnclog.Print(LL_DIMDIM, VNCLOG("Got headers with empty data and length = %d bytes\r\n"), headers.getLength());
				}

				curl_easy_cleanup(m_handler);
				m_handler = curl_easy_init();
			}
			else
			{
				bValidResponse = true;
				break;
			}

			::WaitForSingleObject(m_eTimer, 500 * (i + 1));
		}

		if (false == bValidResponse)
		{
			vnclog.Print(LL_DIMDIM, VNCLOG("Setting connected status to false, because of send failures\r\n"));
			m_bIsConnected = false;
			m_bSendFailed = true;

			logDump(buffer->getData(), buffer->getLength(), "LAST FAILED SEND PACKET");

			vnclog.Print(LL_DIMDIM, VNCLOG("\r\nTotal Data Queued before send failure = %d bytes\r\n\r\n"), (m_totalDataCreated - buffer->getLength()));
			vnclog.Print(LL_DIMDIM, VNCLOG("Total Data Sent = %d bytes\r\n\r\n"), m_totalDataSent);
			vnclog.Print(LL_DIMDIM, VNCLOG("Returning false\r\n"));
			return VFalse;	
		}

		m_totalDataSent += buffer->getLength();
		m_postCount += 1;

		//		if (0 == (m_postCount % 10))
		//			vnclog.Print(LL_DIMDIM, "Data sent so far (bytes),%d,Average Bitrate (Kbps),%f\n", m_totalDataSent, getBitRate(m_totalDataSent,tg.getTimeStamp()));

/*
		if (0 == (m_postCount % 100))
		{
			vnclog.Print(LL_DIMDIM, "Cleaning up curl handle every 100 posts\r\n");
			curl_easy_cleanup(m_handler);
			m_handler = curl_easy_init();
			vnclog.Print(LL_DIMDIM, "Curl handle cleaned up and re-initialized\r\n");
		}
*/
	}

	return VTrue;
}	

void CurlSocket::SetBlockSize(VInt blockSize)
{
	m_blockSize = blockSize;
}

void CurlSocket::SetOperationType(UINT uType)
{
	m_uOperationType = uType;
}

VBool CurlSocket::ReadExact(char *buff, const VCard bufflen)
{
	// Reads exactly bufflen amount of data.

	// We don't have poll support yet. So just flush the queue and
	// simulate appropriate read.

	if (!m_bIsConnected)
		return VFalse;

	TimeStampGenerator tip;
	tip.reset();

	while (!out_queue.empty())
	{
		if (!SendFromQueue())
			return VFalse;
	}

	if (tip.getTimeStamp() < 20)
		::WaitForSingleObject(m_eTimer, m_transactionWaitTime);

	return VTrue;
}

void CurlSocket::dump(const char *text, unsigned char *ptr, size_t size, char nohex)
{
	size_t i;
	size_t c;

	unsigned int width=0x10;

	if(nohex)
	{
		/* without the hex output, we can fit more on screen */
		width = 0x40;
	}

	vnclog.Print(LL_DIMDIM, "%s, %zd bytes (0x%zx)\n", text, size, size);

	for(i=0; i<size; i+= width) 
	{
		vnclog.Print(LL_DIMDIM, "%04zx: ", i);

		if(!nohex) {
			/* hex not disabled, show it */
			for(c = 0; c < width; c++)
				if(i+c < size)
					vnclog.Print(LL_DIMDIM, "%02x ", ptr[i+c]);
				else
					vnclog.Print(LL_DIMDIM, "   ");
		}

		for(c = 0; (c < width) && (i+c < size); c++) {
			/* check for 0D0A; if found, skip past and start a new line of output */
			if (nohex && (i+c+1 < size) && ptr[i+c]==0x0D && ptr[i+c+1]==0x0A) {
				i+=(c+2-width);
				break;
			}
			vnclog.Print(LL_DIMDIM, "%c",
				(ptr[i+c]>=0x20) && (ptr[i+c]<0x80)?ptr[i+c]:'.');
			/* check again for 0D0A, to avoid an extra \n if it's at width */
			if (nohex && (i+c+2 < size) && ptr[i+c+1]==0x0D && ptr[i+c+2]==0x0A) {
				i+=(c+3-width);
				break;
			}
		}
		vnclog.Print(LL_DIMDIM, "\r\n"); /* newline */
	}
}

int CurlSocket::my_trace(CURL *handle, curl_infotype type, unsigned char *data, size_t size, void *userp)
{
	struct data *config = (struct data *)userp;
	const char *text;
	(void)handle; /* prevent compiler warning */

	switch (type) 
	{
//	case CURLINFO_TEXT:
//		vnclog.Print(LL_DIMDIM, "\r\n== Info: %s", data);
//		break;
	case CURLINFO_HEADER_OUT:
		text = "=> Send header";
		break;
	case CURLINFO_DATA_OUT:
		text = "=> Send data";
		break;
//	case CURLINFO_HEADER_IN:
//		text = "<= Recv header";
//		break;
//	case CURLINFO_DATA_IN:
//		text = "<= Recv data";
//		break;
//	case CURLINFO_SSL_DATA_IN:
//		text = "<= Recv SSL data";
//		break;
//	case CURLINFO_SSL_DATA_OUT:
//		text = "<= Send SSL data";
//		break;
	default: /* in case a new one is introduced to shock us */
		return 0;

	}

	dump(text, data, size, config->trace_ascii);
	return 0;
}

float CurlSocket::getBitRate(size_t size, VCard32 time)
{
	float dataSize((float)size);
	float timeTaken((float)time);

	return (dataSize * 1000 * 8)/(timeTaken * 1024);
}

void CurlSocket::logDump(const void* buf, size_t buflen, std::string qualifier)
{
	if (vnclog.GetLevel() < LL_INTINFO)
		return;

	std::ostrstream os;
	Dump::hexDump(os, buf, buflen);

	std::string qualifierString("\r\n<--------------------BEGIN ");
	qualifierString.append(qualifier);
	qualifierString.append("-------------------->\r\n");
	char beginString[128];
	sprintf(beginString, qualifierString.c_str());

	vnclog.Print(LL_INTINFO, beginString, os.str());

	vnclog.Print(LL_INTINFO, "%s\n", os.str());

	qualifierString.assign("\r\n<--------------------END ");
	qualifierString.append(qualifier);
	qualifierString.append("-------------------->\r\n");

	char endString[128];
	sprintf(endString, qualifierString.c_str());

	vnclog.Print(LL_INTINFO, endString, os.str());

	os.rdbuf()->freeze(false);
}

void CurlSocket::freeCurlCallbackBufs(ByteBuffer& chunk, ByteBuffer& headers)
{
	try
	{
		chunk.destroy();
		headers.destroy();
	}
	catch(...)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("Caught exception in cleaning up curl callback buffers\r\n"));
	}
}

void CurlSocket::importPACFile()
{
	// location information of the PAC File is always in %APPDATA%\Dimdim
	std::string sFileName(getenv("APPDATA"));
	sFileName.append("\\Dimdim\\proxyloc");

	std::string sProxyFile(sFileName);
	sProxyFile.append(".dat");

	std::ifstream fis(sFileName.c_str());

	if (false == fis.is_open())
	{
		// File not found
		vnclog.Print(LL_DIMDIM, "No proxy file imported. Will attempt DIRECT connection\r\n");
		return;
	}

	std::string location("");
	std::getline(fis, location);
	fis.close();

	if (location.length() <= 0)
	{
		// Invalid location.
		vnclog.Print(LL_DIMDIM, "Invalid location. No proxy file imported. Will attempt DIRECT connection\r\n");
		return;
	}

	if (location.find("LOCALFILE") != location.npos){}
	else
	{
		// Not stored locally

		CURLcode res;
		ByteBuffer chunk;

		CURL* proxyHandler = curl_easy_init();	
		curl_easy_setopt(proxyHandler, CURLOPT_URL, location.c_str());
		curl_easy_setopt(proxyHandler, CURLOPT_WRITEFUNCTION, DimdimHelpers::ContentWriteCallback);
		curl_easy_setopt(proxyHandler, CURLOPT_WRITEDATA, &chunk);

		res = curl_easy_perform(proxyHandler);

		if (res != CURLE_OK)
		{
			// Operation failed somehow
			curl_easy_cleanup(proxyHandler);
			vnclog.Print(LL_DIMDIM, "Unable to fetch PAC file. Will attempt DIRECT connection\r\n");
			return;
		}

		std::ofstream fs(sProxyFile.c_str());
		std::stringstream fdata;
		fdata.clear();
		fdata << chunk.getData();
		std::string fsdata(fdata.str());
		fsdata.assign(fsdata.substr(0, fsdata.find_last_of("}") + 1));
		fs << fsdata.c_str();
		fs.close();
		curl_easy_cleanup(proxyHandler);
	}
	
	std::string url("");
	DimdimHelpers::PrepareURL(url, "open");
	std::string proxyInfo = DimdimHelpers::ParsePACFile(sProxyFile, url);

	if (proxyInfo.length() > 0)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("parsing PAC file gave %s \r\n"), proxyInfo.c_str());
	}

	// Try to use DIRECT if possible

	if (proxyInfo.find("DIRECT") != proxyInfo.npos)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("PAC file parsed successfully. Detected DIRECT.\r\n"));
		return;
	}

	// Check if multiple proxies have been returned. In that case, just pick the data before the first
	// ';' and use it.

	if (proxyInfo.find("PROXY") != proxyInfo.npos)
	{
		proxyInfo.assign(proxyInfo.substr(proxyInfo.find_first_of("PROXY", 0), proxyInfo.find_first_of(";", proxyInfo.find("PROXY"))));
	}
	else
	{
		proxyInfo.assign(proxyInfo.substr(0, proxyInfo.find_first_of(";")));
	}

	if (proxyInfo.find("PROXY") != proxyInfo.npos)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("PAC file parsed successfully. Detected PROXY.\r\n"));
		m_proxyType.assign("PROXY");
	}
	else if (proxyInfo.find("SOCKS") != proxyInfo.npos)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("PAC file parsed successfully. Detected SOCKS.\r\n"));
		m_proxyType.assign("SOCKS");
	}
	else
	{
		// Unsupported
		vnclog.Print(LL_DIMDIM, VNCLOG("Unable to parse PAC file. Defaulting to DIRECT.\r\n"));
		return;
	}

	m_proxyURL.assign(proxyInfo.substr(proxyInfo.find_first_of(" ") + 1, proxyInfo.length()));
}