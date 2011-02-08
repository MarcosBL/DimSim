// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

// CurlSocket.h

// The CurlSocket class provides a wrapper to use libcurl interfaces and provide HTTP-POST based reverse 
// connections from VNC Server, *specifically* to work with the Dimdim Reflector.
// The class tries to mimic the functionality of VSocket class as much as possible.


class CurlSocket;

#if (!defined(_DIMDIM_CURLSOCKET_DEFINED))
#define _DIMDIM_CURLSOCKET_DEFINED

#include <omnithread.h>
#include "VTypes.h"

#ifdef __WIN32__
#include <ws2tcpip.h>
#endif

#include <curl/curl.h>

#include <sstream>
#include <strstream>
#include <fstream>

#include "ByteBuffer.h"

// This will take care of global_init and cleanup for libcurl
class CurlSocketSystem
{
public:
	CurlSocketSystem();
	~CurlSocketSystem();
	VBool Initialised() {return m_status;};
private:
	VBool m_status;
};

// The main wrapper class
class CurlSocket
{
public:
	// Constructor / Destructor
	CurlSocket();
	virtual ~CurlSocket();

	// Create
	//			Initialize the curl handler
	VBool Create();

	// Shutdown
	//        Undefined Function. Included for compatibility with VSocket API
	VBool Shutdown() {return VTrue;}

	// Close
	//			Cleanup the curl handler
	VBool Close(bool bForce = false);

	// Bind
	//		Undefined Function. Included for compatibility with VSocket API
	VBool Bind(const VCard port, const VBool localOnly = VFalse, const VBool checkIfInUse = VFalse) {return VTrue;}

	// Connect
	//		Undefined Function. Included for compatibility with VSocket API
	VBool Connect(VStringConst address, const VCard port);

	// Open
	//		The actual 'open' on the reflector
	VBool CurlSocket::Open(const char* buff, const VCard bufflen);

	// Listen
	//		Undefined Function. Included for compatibility with VSocket API
	VBool Listen() {return VTrue;}

	// Accept
	//		Undefined Function. Included for compatibility with VSocket API
	CurlSocket *Accept() {return NULL;}

	// TryAccept
	//		Undefined Function. Included for compatibility with VSocket API
	VBool TryAccept(CurlSocket **new_socket, long ms){return VTrue;}

	// GetPeerName
	//		If the handler is connected then this returns the name
	//		of the machine to which it is connected.
	//		This string MUST be copied before the next socket call...
	VString GetPeerName();

	// GetSockName
	//		If the handler exists then the name of the local machine
	//		is returned.  This string MUST be copied before the next
	//		socket call!
	VString GetSockName();

	// SetTimeout
	//		Sets the socket timeout on reads and writes.
	VBool SetTimeout(VCard32 secs);

	// I/O routines

	// ReadExact attempts to recieve exactly
	// the specified number of bytes.
	VBool ReadExact(char *buff, const VCard bufflen);

	// SendFromQueue
	//	Retrieves data from queued buffer and sends it out in entirety
	//	to Dimdim Reflector.
	VBool SendFromQueue();

	void SendIfRequired();

	// SendExact
	//	Sends exactly bufflen sized buff to Dimdim Reflector
	//	This method is now deprecated.
	VBool SendExact(const char *buff, const VCard bufflen);

	// SendQueued
	//	Adds bufflen sized buff to the out_queue of CSendThread,
	//	to be processed and sent to the Dimdim Reflector later.
	VBool SendQueued(const char *buff, const VCard bufflen, bool bRFB = false);

	/* Utility Functions */

	//	Get current time in milliseconds from 
	//	the TimeStampGenerator of CSendThread
	VCard32 GetSendTimeStamp();

	void SetTransactionWaitTime(VInt waitTime) 
	{
		m_transactionWaitTime = (waitTime >= 20) ? waitTime:100;
	}

	//	Sent an event so that the SendFromQueue thread
	//	is functional
	void SetSendThreadEvent(bool bSet);

	//	Writes bufflen sized buff to CSendThread's binary dump
	//	This is available to CurlSocket only so that the 
	//	server to client init packet can be made part of the
	//	binary dump.
	void dumpPackets(const char* buff, const size_t bufflen);

	void logDump(const void* buf, size_t buflen, std::string qualifier);

	void SetBlockSize(VInt blockSize);
	void SetOperationType(UINT uType);

	// Intended to convey state of the connection from
	// screencaster to Dimdim Reflector. Currently, since there
	// are no poll requests, the CSendThread class maintains this
	// state.
	bool IsConnected();

	bool isSendFailed() {return m_bSendFailed;}

	void setMaxRetries(VInt retries);
	void setMaxWaitTime(VCard32 secs);

	omni_mutex* getMutex() { return &curl_lock; }

	void importPACFile();

	void setMetaData(std::string metaData)
	{
		m_clientMetaData.assign(metaData);
	}

	std::string getMetaData()
	{
		return m_clientMetaData;
	}

protected:

	CURL* m_handler;
	struct curl_slist* m_headerList;

	// Lock for CurlSocket's internal operations.
	omni_mutex curl_lock;	

private:

	HANDLE m_eTimer;

	std::ofstream* osDump;
	vncQueue out_queue;
	bool m_bForceNode;		// flag to force a fresh enqueue on out_queue

	UINT m_uOperationType;
	VInt m_transactionWaitTime;

	VInt m_blockSize;
	VInt m_originalBlockSize;

	size_t m_postCount;
	size_t m_totalDataCreated;
	size_t m_totalDataSent;

	bool m_bSendFailed;
	bool m_bIsConnected;

	std::string m_proxyType;
	std::string m_proxyURL;

	TimeStampGenerator tg;

	float getBitRate(size_t size, VCard32 time);
	static void dump(const char *text, unsigned char *ptr, size_t size, char nohex);
	static int my_trace(CURL *handle, curl_infotype type, unsigned char *data, size_t size, void *userp);

	void freeCurlCallbackBufs(ByteBuffer& chunk, ByteBuffer& headers);

	void setHeaders();

	struct data {
		char trace_ascii; /* 1 or 0 */
	};

	std::string m_clientMetaData;

};
#endif