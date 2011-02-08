// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

// CurlSocket.cpp

// The CurlSocket class provides a wrapper to use libcurl interfaces and provide HTTP-POST based reverse 
// connections from VNC Server, *specifically* to work with the Dimdim Reflector.
// The class tries to mimic the functionality of VSocket class as much as possible.

class SendThread;

#include "stdhdrs.h"
#ifdef __WIN32__
#include <ws2tcpip.h>
#endif

#include "VTypes.h"
#include "SendThread.h"
#include "PollThread.h"
#include <strstream>
#include <fstream>
#include <string>

#define MAX_TRANSACTIONS 5;

CSendThread* CSendThread::instance = NULL;
CSendThread::CSendThread()
{
	m_bSendFailed = false;	
	m_eSend = CreateEvent(NULL,true,false,"Send");
	m_eThreadWait = CreateEvent(NULL,true,false,"SendThreadWait");
	m_eThreadStop = CreateEvent(NULL,true,false,"SendThreadStop");
	m_eTimer = CreateEvent(NULL,true,false,"SendTimer");
	out_queue = NULL;
	queue_last = NULL;
	m_handler = NULL;
	m_handler = curl_easy_init();

	tg.reset();

	total_data_created = 0;
	total_data_sent = 0;
	postCount = 0;

	m_blockSize = 32 * 1024; // By default, choose 32k blocks
	m_originalBlockSize = m_blockSize;
	m_operationType = 2; //Enable only network by default
	m_sendWaitTime = 20; // 20ms by default
	m_blockWaitTime = 0; // 0ms by default. 

	m_transactionRecord.clear();

	m_bConnected = true;
	m_bTimerSet = false;

	osSend = NULL;

}

UINT CSendThread::getTransactionWaitTime()
{
	// The transaction record always holds the 
	// data for the most recent MAX_TRANSACTIONS.

	// The transaction wait time is calculated with a weighted
	// preference to each of the filed transactions.

	if (m_transactionRecord.size() == 0)
		return 300;

	UINT aWeightedSum = 0;
	std::map<int, UINT>::iterator it;
	for (it = m_transactionRecord.begin(); it != m_transactionRecord.end(); it++)
	{
		aWeightedSum += it->first * it->second;
	}
	UINT aDivisor = (m_transactionRecord.size() * (m_transactionRecord.size() + 1)) / 2;

	// We are accepting a WaitTime of not less than 100 ms. 
	// Reasons - 
	// 1. On LAN networks, transaction time is usually even lesser than 100 ms.
	//		Too small transaction and send intervals create locking problems. This is
	//	not necessarily due to a code problem but more because the over-arching lock
	// which controls enqueue and dequeue doesn't get freed / locked as often as is necessary.

	// 2. For video heavy websites or video-sharing sessions, there is just so much data
	// to be processed and queued up. Regular machines cannot cope with extremely short wait times.
	// The CPU usage starts shooting up.

	// To provide a reasonable performance and experience, we make sure we don't go below 100 ms.

	m_waitTime = aWeightedSum / aDivisor;

	/*if (m_waitTime < 100)
	{
		m_blockSize = m_originalBlockSize*128;
	}
	else
	{
		m_blockSize = m_originalBlockSize;
	}*/

	m_waitTime = m_waitTime * (((total_data_created - total_data_sent) / m_blockSize) + 1);

	return (m_waitTime < 100) ? 100 : m_waitTime; 
}

void CSendThread::fileTransaction(VCard32 ttime)
{
	int index = m_transactionRecord.size() % MAX_TRANSACTIONS;
	std::map<int, UINT>::iterator it;
	it = m_transactionRecord.find(index);
	if (it != m_transactionRecord.end())
		m_transactionRecord.erase(it);
	m_transactionRecord[index] = ttime;
}

float CSendThread::getBitRate(size_t size, VCard32 time)
{
	float dataSize(size);
	float timeTaken(time);

	return (dataSize * 1000 * 8)/(timeTaken * 1024);
}

CSendThread::~CSendThread()
{
	if (m_operationType >= 2)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("\r\nTotal Data Created = %d bytes\r\n\r\n"), getTotalDataCreated());
		vnclog.Print(LL_DIMDIM, VNCLOG("Total Data Sent = %d bytes\r\n\r\n"), getTotalDataSent());
		vnclog.Print(LL_DIMDIM, VNCLOG("Total Time Taken = %d ms, Average Bitrate = %f kbps\r\n\r\n"), tg.getTimeStamp(),getBitRate(getTotalDataSent(),tg.getTimeStamp()));
	}

	m_bConnected = false;
	SetEvent(m_eThreadWait);
	::WaitForSingleObject(m_eThreadStop, INFINITE);

	if (out_queue)
	{
		omni_mutex_lock l(out_queue_lock);
		while (out_queue)
		{
			DataBlock* next = out_queue->next;
			delete out_queue;
			out_queue = next;
		}
	}

	curl_easy_cleanup(m_handler);

	CloseHandle(m_hThread);
	CloseHandle(m_eSend);
	CloseHandle(m_eThreadStop);
	CloseHandle(m_eThreadWait);
	CloseHandle(m_eTimer);
}

CSendThread* CSendThread::getInstance()
{
	if(NULL == instance)
	{
		instance = new CSendThread();
	}

	return instance;
}

void CSendThread::removeInstance()
{
	if (instance)
	{
		delete instance;
	}

	instance = NULL;
}

void CSendThread::setTimer()
{
	if (false == m_bTimerSet)
	{
		SetEvent(m_eTimer);
		m_bTimerSet = true;
	}
}

void CSendThread::resetTimer()
{
	if (true == m_bTimerSet)
	{
		ResetEvent(m_eTimer);
		m_bTimerSet = false;
	}
}

void CSendThread::setSendEvent(bool bSet)
{
	//	omni_mutex_lock l(send_lock);

	if (bSet)
	{
		std::string appFolder(getenv("APPDATA"));
		appFolder.append("\\Dimdim\\dump.bin");
		osSend = new std::ofstream(appFolder.c_str(), std::ios::binary | std::ios::out);
		SetEvent(m_eSend);
	}
	else
	{
		ResetEvent(m_eSend);

		if (osSend)
		{
			osSend->flush();
			osSend->close();
		}
	}
}

DWORD CSendThread::sender()
{
	while (true)
	{
		if (::WaitForSingleObject(m_eSend, m_sendWaitTime) == WAIT_OBJECT_0)
		{
			SendFromQueue();
		}
		if (::WaitForSingleObject(m_eThreadWait, 0) == WAIT_OBJECT_0)
		{
			::SetEvent(m_eThreadStop);
			break;
		}
	}
	return TRUE;
}

DWORD CSendThread::run(LPVOID lParam)
{
	return CSendThread::getInstance()->sender();
}

void CSendThread::start()
{
	m_hThread = CreateThread(NULL,0,run,&m_eSend,0,NULL);
}

void CSendThread::sendbinWrite(const char* buff, const size_t bufflen)
{
	osSend->write((const char*)(buff),(std::streamsize)bufflen);
}

VBool CSendThread::SendExact(const char *buff, const VCard bufflen)
{
	// Put the data into the queue
	setSendEvent(false);
	SendQueued(buff, bufflen);
	setSendEvent(true);

	while (out_queue) 
	{
		// Actually send some data

		if (!SendFromQueue())
		{
			return VFalse;
		}
	}

	return VTrue;
}

VBool CSendThread::SendQueued(const char *buff, const VCard bufflen)
{
	omni_mutex_lock l(out_queue_lock);

	if (0 == bufflen || NULL == buff)
	{
		return VTrue;
	}

	// Just append new bytes to the output queue
	if (!out_queue) 
	{
		out_queue = new DataBlock(bufflen, buff);
		queue_last = out_queue;
	} 
	else 
	{
		if (queue_last->getLength() >= m_blockSize)
		{
			queue_last->next = new DataBlock(bufflen, buff);
			queue_last = queue_last->next;
		}
		else
			queue_last->append(buff, bufflen);
	}

	total_data_created += bufflen;

	return VTrue;
}

VBool CSendThread::SendFromQueue()
{
	omni_mutex_lock l(out_queue_lock);

	// Is there something to send?
	if (!out_queue)
		return VTrue;

	if (!m_bConnected)
		return VFalse;

	if (m_operationType >= 2)
	{
		std::string url("");
		DimdimHelpers::PrepareURL(url, "send", CurlClientData::getInstance()->m_subscriptionID);

		// Try to send some data

		CURLcode res;
		DimdimReflectorResponse chunk;
		DimdimReflectorResponse headers;

		chunk.data = NULL;
		chunk.size = 0;
		headers.data = NULL;
		headers.size = 0;

		curl_easy_setopt(m_handler, CURLOPT_URL, url.c_str());
		curl_easy_setopt(m_handler, CURLOPT_POSTFIELDSIZE, out_queue->getLength());
		curl_easy_setopt(m_handler, CURLOPT_COPYPOSTFIELDS, out_queue->getData());

		curl_easy_setopt(m_handler, CURLOPT_WRITEFUNCTION, DimdimHelpers::ContentWriteCallback);
		curl_easy_setopt(m_handler, CURLOPT_WRITEDATA, &chunk);

		curl_easy_setopt(m_handler, CURLOPT_HEADERFUNCTION, DimdimHelpers::HeadersWriteCallback);
		curl_easy_setopt(m_handler, CURLOPT_HEADERDATA, &headers);

		//	curl_easy_setopt(m_handler, CURLOPT_DEBUGFUNCTION, my_trace);
		//	curl_easy_setopt(m_handler, CURLOPT_DEBUGDATA, &config);
		//	curl_easy_setopt(m_handler, CURLOPT_VERBOSE, 1);

		curl_slist* header_list = NULL;
		header_list = curl_slist_append(header_list, "Expect:");
		header_list = curl_slist_append(header_list, "Content-Type: application/x-dimdim-dtp");
		header_list = curl_slist_append(header_list, "Connection: Keep-Alive");
		curl_easy_setopt(m_handler, CURLOPT_HTTPHEADER, header_list);

		curl_easy_setopt(m_handler,CURLOPT_TCP_NODELAY, 1);

		TimeStampGenerator tempGen;
		tempGen.reset();

		bool bValidResponse = false;

		for (int i = 0; i < CurlClientData::getInstance()->m_maxRetries + 1; i++)
		{
			res = curl_easy_perform(m_handler);
			if (CURLE_OK != res || true != DimdimHelpers::ValidResponseHeaders(headers, "send"))
				continue;
			else
			{
				bValidResponse = true;
				break;
			}
		}

		if (false == bValidResponse)
		{
			m_bSendFailed = true;
			m_bConnected = false;

			std::ostrstream os;
			Dump::hexDump(os, out_queue->getData(), out_queue->getLength());
			vnclog.Print(LL_DIMDIM, VNCLOG("<--------------------LAST FAILED SEND PACKET-------------------->\r\n\r\n"), os.str());
			vnclog.Print(LL_DIMDIM, "%s\n", os.str());
			vnclog.Print(LL_DIMDIM, VNCLOG("<--------------------LAST FAILED SEND PACKET-------------------->\r\n\r\n"), os.str());
			os.rdbuf()->freeze(false);

			if ((1 == m_operationType || 3 == m_operationType) && out_queue->getLength() > 0)
			{
				sendbinWrite((char*)out_queue->getData(), out_queue->getLength());
			}

			vnclog.Print(LL_DIMDIM, VNCLOG("\r\nTotal Data Queued before send failure = %d bytes\r\n\r\n"), (getTotalDataCreated() - out_queue->getLength()));
			vnclog.Print(LL_DIMDIM, VNCLOG("Total Data Sent = %d bytes\r\n\r\n"), getTotalDataSent());
			return VFalse;
		}

		fileTransaction(tempGen.getTimeStamp());

		total_data_sent += out_queue->getLength();


		DataBlock* sent = out_queue;
		out_queue = out_queue->next;
		delete sent;
		sent = 0;

		postCount += 1;
		if (0 == (postCount % 10))
		{
			vnclog.Print(LL_DIMDIM, "Data sent so far (bytes),%d,Average Bitrate (Kbps),%f\n", total_data_sent, getBitRate(total_data_sent,tg.getTimeStamp()));
		}
	}
	else
	{
		vnclog.Print(LL_DIMDIM, "timestamp = %d, bytes,%d,posts,%d\r\n\r\n", tg.getTimeStamp(), out_queue->getLength(), postCount);
	}

	if ((1 == m_operationType || 3 == m_operationType) && out_queue->getLength() > 0)
	{
		sendbinWrite((char*)out_queue->getData(), out_queue->getLength());
	}


	return VTrue;
}

void CSendThread::dump(const char *text, unsigned char *ptr, size_t size, char nohex)
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

int CSendThread::my_trace(CURL *handle, curl_infotype type, unsigned char *data, size_t size, void *userp)
{
	struct data *config = (struct data *)userp;
	const char *text;
	(void)handle; /* prevent compiler warning */

	switch (type) 
	{
	case CURLINFO_TEXT:
		vnclog.Print(LL_DIMDIM, "== Info: %s", data);
	default: /* in case a new one is introduced to shock us */
		return 0;

	case CURLINFO_HEADER_OUT:
		text = "=> Send header";
		break;
		/*  case CURLINFO_DATA_OUT:
		text = "=> Send data";
		break;
		case CURLINFO_HEADER_IN:
		text = "<= Recv header";
		break;
		case CURLINFO_DATA_IN:
		text = "<= Recv data";
		break;
		case CURLINFO_SSL_DATA_IN:
		text = "<= Recv SSL data";
		break;
		case CURLINFO_SSL_DATA_OUT:
		text = "<= Send SSL data";
		break;*/
	}

	dump(text, data, size, config->trace_ascii);
	return 0;
}