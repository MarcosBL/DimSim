// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

#if (!defined(_DIMDIM_SENDTHREAD_DEFINED))
#define _DIMDIM_SENDTHREAD_DEFINED

#include <omnithread.h>
#include "VTypes.h"

#include <curl/curl.h>

#include "ByteBuffer.h"

#include "Helpers.h"

class CSendThread
{
private:
	CSendThread();
	static CSendThread* instance;

private:
	HANDLE m_eSend;
	HANDLE m_eThreadWait;
	HANDLE m_eThreadStop;
	HANDLE m_eTimer;
	HANDLE m_hThread;

	omni_mutex send_lock;
	CURL* m_handler;

	// Input queue
	DataBlock* out_queue;
	DataBlock* queue_last;
	omni_mutex out_queue_lock;

	size_t bytes_sent;
	size_t total_data_created;
	size_t total_data_sent;
	size_t postCount;
	UINT m_waitTime;

	TimeStampGenerator tg;
	bool m_bConnected;
	bool m_bTimerSet;

	VInt m_blockSize;
	VInt m_originalBlockSize;
	UINT m_operationType;
	UINT m_sendWaitTime;
	UINT m_blockWaitTime;

	std::map<int, UINT> m_transactionRecord;

	std::ofstream* osSend;

	bool m_bSendFailed;

	float getBitRate(size_t size, VCard32 time);

	struct data {
		char trace_ascii; /* 1 or 0 */
	};
	static void dump(const char *text, unsigned char *ptr, size_t size, char nohex);
	static int my_trace(CURL *handle, curl_infotype type, unsigned char *data, size_t size, void *userp);

public:
	virtual ~CSendThread();

	static CSendThread* getInstance();
	static void removeInstance();

	static DWORD WINAPI run(LPVOID lParam);

	void setSendEvent(bool bSet);
	void setTimer();
	void resetTimer();

	bool IsConnected() { return m_bConnected; }
	void Connected(bool bConnected) { m_bConnected = bConnected; }

	DWORD sender();
	void start();


	// SendExact and attempts to send exactly
	// the specified number of bytes.
	VBool SendExact(const char *buff, const VCard bufflen);

	// SendQueued sends as much data as possible immediately,
	// and puts remaining bytes in a queue, to be sent later.
	VBool SendQueued(const char *buff, const VCard bufflen);

	VBool SendFromQueue();
	void sendbinWrite(const char* buff, const size_t bufflen);

	UINT GetOperationType() { return m_operationType; }
	void SetBlockSize(VInt blockSize) 
	{
		m_blockSize = blockSize;
		m_originalBlockSize = m_blockSize;
	}
	void SetOperationType(UINT uType) {m_operationType = uType;}
	void SetSendWaitTime(UINT uTime) {m_sendWaitTime = uTime;}

	size_t getTotalDataSent() { return total_data_sent; }
	size_t getTotalDataCreated() { return total_data_created; }
	VCard32 getTimeStamp(){ return tg.getCurrentTimeMillis(); }

	omni_mutex* getSendLock() {return &out_queue_lock;}

	bool isSendFailed() { return m_bSendFailed; }

	UINT getTransactionWaitTime();
	void fileTransaction(VCard32 ttime);
};	

#endif