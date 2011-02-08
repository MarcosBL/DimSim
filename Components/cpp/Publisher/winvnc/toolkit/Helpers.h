// TBD - INCLUDE DIMDIM GPLv2 LICENSE HERE
// To contact authors, mail support@dimdim.com

#if (!defined(_DIMDIM_HELPERS_DEFINED))
#define _DIMDIM_HELPERS_DEFINED

#include "..\VTypes.h"
#include <list>
#include <string>
#include <sstream>

#define FORCE_BY_REF_ONLY(cls)	cls(const cls&); const cls& operator = (const cls&)
#define SAFE_DELETE_ARRAY(ptr)	try{if(ptr){ delete[] ptr; ptr = 0; }}catch(...){}
#define SAFE_DELETE_PTR(ptr)	try{if(ptr){ delete ptr; ptr = 0; }}catch(...){}

template<class T> static void clearPtrList(std::list<T*>& plist)
{
	std::list<T*>::iterator it;
	for (it = plist.begin(); it != plist.end(); it++)
		SAFE_DELETE_PTR(*it);
	
	plist.clear();
}

typedef unsigned char u8;

// This class is used as a part of output queue
// This is taken directly from the VSocket class implementation, 
// with some very minor modifications.

class ByteBuffer;
class VAIOBlock
{
public:
	size_t data_size;		// Data size in this block
	char *data_ptr;			// Beginning of the data buffer
	VAIOBlock *next;			// Next block or NULL for the last block
	size_t data_available;		// This is used only during the Read ('poll' on the reflector) calls

	VAIOBlock(int size, const char *data = NULL) {
		next = NULL;
		data_size = size;
		data_ptr = new char[size];
		if (data_ptr && data)
			memcpy(data_ptr, data, size);

		data_available = data_size;
	}
	~VAIOBlock() {
		if (data_ptr)
			delete[] data_ptr;
	}
};


// DimdimReflectorResponse - Utility class to be used for 
// callback functions in curl operations.
struct DimdimReflectorResponse {
  char *data;
  size_t size;
};

// Dump - Utility class to dump binary data (especially packets)

class Dump
{
public:
	static bool hexDump(std::ostream& os, const void* buf, size_t bufLen);
	static bool binDump(const char* fileName, const void* buf, size_t len);
};

// TimeStampGenerator - Utility class to keep track of time

class TimeStampGenerator
{
public:
	TimeStampGenerator();
	virtual ~TimeStampGenerator();
	static VCard32 getCurrentTimeMillis();
	VCard32 getTimeStamp();
	void reset();
private:
	VCard32 startTime;
};

// CurlClientData has credentials of Dimdim Reflector.
// These creds are used for curl operations.
//
// NOTE: When(if) we support multiple simultaneous
// meetings from a single presenter, this class
// needs to be used in a different way.

class CurlClientData
{
private:
	CurlClientData();
	static CurlClientData* instance;

public:
	virtual ~CurlClientData(){};

	static CurlClientData* getInstance();
	static void removeInstance();

	std::string m_subscriptionID;
	std::string m_streamName;

	std::string m_peerAddress;
	std::string m_peerPort;

	VInt	m_maxRetries;
	VCard32 m_maxWaitTime;
};

// DimdimHelpers primarily contains helper methods
// to assist curl operations.,

class DimdimHelpers
{
public:
	static void* customRealloc(void *ptr, size_t size);
	static size_t ContentWriteCallback(void *ptr, size_t size, size_t nmemb, void *data);
	static size_t HeadersWriteCallback(void *ptr, size_t size, size_t nmemb, void *data);

	template <class T>
	static std::string VTypeToString(T tVal)
	{
		std::stringstream out;
		out << tVal;
		return std::string(out.str());
	}

	static unsigned int ValidResponseHeaders(ByteBuffer &dHeaders, std::string cmd);
	static std::string GenerateStreamName(int iLength = 8);
	static void PrepareURL(std::string &url, std::string cmd, 
							   std::string handlerID = "", std::string role = "pub");
	static std::string ParsePACFile(std::string filePath, std::string url);

};


///
/// a scoped pointer class to treat heap ptrs
/// like stack ones
///
template<class T>
class ScopedPointer
{
public:
	ScopedPointer(T* t) : pT(t){}
	virtual ~ScopedPointer(){ SAFE_DELETE_PTR(pT); }
	T* get(){ return pT; }
	const T* get() const{ return pT; }
	T* operator ->(){ return get(); }
	const T* operator ->() const{ return get(); }
	bool isNull() const{ return get() == 0; }
	/// the owned pointer is released and 
	/// is no longer deleted on exit
	T* release(){ T* t = pT; pT = 0; return t; }

#ifndef WIN32
	ScopedPointer(const ScopedPointer&);
#endif

private:
	T* pT;

#ifndef WIN32
	const ScopedPointer& operator= (const ScopedPointer&);
#endif

#ifdef WIN32
	FORCE_BY_REF_ONLY(ScopedPointer);
#endif

};

template<class T> class ScopedArray
{
public:
	ScopedArray(T* t) : pT(t){}
	virtual ~ScopedArray(){ SAFE_DELETE_ARRAY(pT); }
private:
	T* pT;
	FORCE_BY_REF_ONLY(ScopedArray);
};

#endif