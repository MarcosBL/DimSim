#ifndef _DIMDIM_TOOLKIT_BYTE_BUFFER_H_
#define _DIMDIM_TOOLKIT_BYTE_BUFFER_H_

#include "Helpers.h"
#include "..\stdhdrs.h"

///
///	A simple dynamic byte buffer class
///
class ByteBuffer
{
public:
	ByteBuffer(size_t len=0);
	ByteBuffer(const void* buffer, size_t buflen, bool createCopy);
	virtual ~ByteBuffer(void);
	ByteBuffer* clone();
	ByteBuffer* cloneAndRelease(){ ByteBuffer* cb = clone(); destroy(); return cb; }
	void reallocate(size_t newLen, bool keepContents=false);
	void zeroMemory();
	void destroy();
	bool isNull() const;
	u8* getData(size_t offset=0);
	const u8* getData(size_t offset=0) const;
	size_t getRemainingLength(size_t offset=0) const;
	size_t getLength() const{ return getRemainingLength(0); }
	void append(const void* buffer, size_t bufLen);
	void append(const ByteBuffer* buffer);
	void copyTo(void* buffer, size_t bufLen, size_t dataOffset = 0);
	void copyFrom(const void* buffer, size_t bufLen, size_t dataOffset = 0);

	u8 getByte(size_t index) const{ return data[index]; }
	void setByte(size_t index, u8 val){ data[index] = val; }
private:
	u8 *data;
	size_t dataLen;
	bool doNotDelete;
	FORCE_BY_REF_ONLY(ByteBuffer);

};

class vncQueue
{
private:
	std::list<ByteBuffer*> m_queue;
public:
	vncQueue()
	{
		clear();
	}
	~vncQueue()
	{
		clear();
	}

	void clear()
	{
		clearPtrList(m_queue);
	}

	void enqueue(ByteBuffer* buf)
	{
		m_queue.push_back(buf);
	}

	bool empty()
	{
		return m_queue.empty();
	}

	void print()
	{
		std::list<ByteBuffer*>::iterator it;
		for (it = m_queue.begin(); it != m_queue.end(); it++)
			vnclog.Print(LL_DIMDIM, VNCLOG("Length Enqueued = %d\r\n"), (*it)->getLength());
	}

	ByteBuffer* dequeue(size_t blockSize)
	{
		// calling function should delete the memory occupied by 'data'
		if (m_queue.empty())
			return NULL;

		ByteBuffer* data = new ByteBuffer();
		data->destroy();

		while (data->getLength() < blockSize && (false == m_queue.empty()))
		{
			ByteBuffer* deq = m_queue.front();
			ScopedPointer<ByteBuffer> deqPtr(deq);
			data->append(deq->getData(), deq->getLength());
			m_queue.pop_front();
		}

	//	vnclog.Print(LL_DIMDIM, VNCLOG("Length Dequeued = %d\r\n"), data->getLength());

		return data;
	}
};

class DataBlock
{
public:
	ByteBuffer buf;
	DataBlock* next;
	DataBlock(int size, const char* data = NULL)
	{
		buf.append(data, size);
		next = NULL;
	}
	virtual ~DataBlock()
	{
		buf.destroy();
	}

	void append(const void* buffer, size_t bufLen)
	{
		buf.append(buffer, bufLen);
	}

	const u8* getData(size_t offset=0) const
	{
		return buf.getData();
	}

	size_t getLength() const{ return buf.getLength(); }

};

#endif
