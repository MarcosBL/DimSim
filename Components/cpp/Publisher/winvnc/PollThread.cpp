// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

class PollThread;

////////////////////////////////////////////////////////
// System includes

#include "stdhdrs.h"

#ifdef __WIN32__
#include <ws2tcpip.h>
#endif

////////////////////////////////////////////////////////
// Custom includes

#include "VTypes.h"
#include "PollThread.h"

////////////////////////////

CPollThread* CPollThread::instance = NULL;
CPollThread::CPollThread()
{
	m_ePoll = CreateEvent(NULL,true,false,"Poll");
	m_eThreadWait = CreateEvent(NULL,true,false,"ThreadWait");
	m_eThreadStop = CreateEvent(NULL,true,false,"ThreadStop");
	m_eSleepTimer = CreateEvent(NULL,true,false,"SleepTimer");
	in_queue = NULL;
	m_handler = NULL;
	m_handler = curl_easy_init();
}

///////////////////////////

CPollThread::~CPollThread()
{
	SetEvent(m_eThreadWait);
	::WaitForSingleObject(m_eThreadStop, INFINITE);

	{
		omni_mutex_lock l(in_queue_lock);
		while (in_queue)
		{
			VAIOBlock *next = in_queue->next;
			delete in_queue;
			in_queue = next;
		}
	}

	curl_easy_cleanup(m_handler);

	CloseHandle(m_hThread);
	CloseHandle(m_ePoll);
	CloseHandle(m_eThreadStop);
	CloseHandle(m_eThreadWait);
	CloseHandle(m_eSleepTimer);
}

///////////////////////////

CPollThread* CPollThread::getInstance()
{
	if(NULL == instance)
	{
		instance = new CPollThread();
	}

	return instance;
}

///////////////////////////

void CPollThread::removeInstance()
{
	if (instance)
	{
		delete instance;
	}

	instance = NULL;
}

////////////////////////////

void CPollThread::setPollEvent(bool bSet)
{
	omni_mutex_lock l(poll_lock);

	if (bSet)
		SetEvent(m_ePoll);
	else
		ResetEvent(m_ePoll);
}

////////////////////////////

DWORD CPollThread::poller()
{
	DWORD pollInterval = 200;

	while (true)
	{
		if (::WaitForSingleObject(m_ePoll, 100) == WAIT_OBJECT_0)
		{
			VCard bytes_read = Read();
			::WaitForSingleObject(m_eSleepTimer, pollInterval);
		}
		if (::WaitForSingleObject(m_eThreadWait, 0) == WAIT_OBJECT_0)
		{
			::SetEvent(m_eThreadStop);
			break;
		}
	}
	return TRUE;
}

////////////////////////////

DWORD CPollThread::run(LPVOID lParam)
{
	return CPollThread::getInstance()->poller();
}

void CPollThread::start()
{
	m_hThread = CreateThread(NULL,0,run,&m_ePoll,0,NULL);
}

////////////////////////////

VBool CPollThread::ReadQueued(const char *buff, const VCard bufflen)
{
	omni_mutex_lock l(in_queue_lock);
	// Just append new bytes to the input queue
	if (!in_queue) {
		in_queue = new VAIOBlock(bufflen, buff);
	} else {
		VAIOBlock *last = in_queue;
		while (last->next)
			last = last->next;
		last->next = new VAIOBlock(bufflen, buff);
	}

	return VTrue;
}

////////////////////////////

bool CPollThread::IsDataAvailable()
{
	omni_mutex_lock l(in_queue_lock);
	return (NULL != in_queue);
}

VInt CPollThread::Read()
{
	if (NULL == m_handler)
	{
		vnclog.Print(LL_DIMDIM, VNCLOG("curl handler not initialized. exiting\n"));
		return VFalse;
	}

	CURLcode res;

	DimdimReflectorResponse chunk;
	DimdimReflectorResponse headers;

	chunk.data = NULL;
	chunk.size = 0;
	headers.data = NULL;
	headers.size = 0;

	std::string url("");
	DimdimHelpers::PrepareURL(url, "poll", CurlClientData::getInstance()->m_subscriptionID);

	curl_easy_setopt(m_handler, CURLOPT_URL, url.c_str());
	curl_easy_setopt(m_handler, CURLOPT_POSTFIELDS, "");
	curl_easy_setopt(m_handler, CURLOPT_WRITEFUNCTION, DimdimHelpers::ContentWriteCallback);
	curl_easy_setopt(m_handler, CURLOPT_WRITEDATA, &chunk);

	curl_easy_setopt(m_handler, CURLOPT_HEADERFUNCTION, DimdimHelpers::HeadersWriteCallback);
	curl_easy_setopt(m_handler, CURLOPT_WRITEHEADER, &headers);

	curl_slist* header_list = NULL;
	header_list = curl_slist_append(header_list, "Expect:");
	header_list = curl_slist_append(header_list, "Content-Type: application/x-dimdim-dtp");
	header_list = curl_slist_append(header_list, "Connection: Keep-Alive");
	
	curl_easy_setopt(m_handler, CURLOPT_HTTPHEADER, header_list);

	curl_easy_setopt(m_handler,CURLOPT_TCP_NODELAY, 1);

	bool bValidResponse = false;

	for (int i = 0; i < CurlClientData::getInstance()->m_maxRetries + 1; i++)
	{
		res = curl_easy_perform(m_handler);
		if (CURLE_OK != res || true != DimdimHelpers::ValidResponseHeaders(headers, "poll") || NULL == chunk.data)
			continue;
		else
		{
			bValidResponse = true;
			break;
		}
	}

	if (false == bValidResponse)
	{
		// Put additional logs here. Why did it fail ?
		return 0;
	}

	// Queue up the data read so far

	ReadQueued(chunk.data, chunk.size);
	return chunk.size; 
}

////////////////////////////

VCard CPollThread::ReadFromQueue(char* buff, VCard bufflen)
{
	VCard bytes_to_be_read = bufflen;
	size_t buff_offset = 0;

	// Is there something to read ?
	if (!in_queue)
	{
		return 0;
	}

	// Does the first node have enough data ?
	if (bytes_to_be_read <= in_queue->data_available)
	{
		size_t in_queue_offset = in_queue->data_size - in_queue->data_available;

		memcpy(buff + buff_offset, in_queue->data_ptr + in_queue_offset, bytes_to_be_read);
		in_queue->data_available -= bytes_to_be_read;
		buff_offset += bytes_to_be_read;
		bytes_to_be_read = 0;
	}
	else
	{
		// We need to get data from multiple nodes delete fully-used nodes on the way.
		while (in_queue && bytes_to_be_read > 0)
		{
			if (bytes_to_be_read > in_queue->data_available)
			{
				size_t in_queue_offset = in_queue->data_size - in_queue->data_available;
				
				memcpy(buff + buff_offset, in_queue->data_ptr + in_queue_offset, in_queue->data_available);
				bytes_to_be_read -= in_queue->data_available;
				buff_offset += in_queue->data_available;

				// Delete this block
				VAIOBlock* read = in_queue;
				in_queue = read->next;
				delete read;
			}
			else
			{
				size_t in_queue_offset = in_queue->data_size - in_queue->data_available;
				
				memcpy(buff + buff_offset, in_queue->data_ptr + in_queue_offset, bytes_to_be_read);
				in_queue->data_available -= bytes_to_be_read;
				buff_offset += bytes_to_be_read;
			}
		}
	}

	if (in_queue && 0 == in_queue->data_available)
	{
		// Delete this block
		VAIOBlock* read = in_queue;
		in_queue = read->next;
		delete read;
	}

	return bufflen - bytes_to_be_read;
}