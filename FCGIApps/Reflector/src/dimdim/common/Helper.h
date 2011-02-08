#ifndef _dss_helper_h_
#define _dss_helper_h_
#include "Includes.h"
#include <stack>
#include <queue>
#include "Timer.h"
namespace dm
{
	class DSSFRAMEWORKAPI Helper
	{
	public:
		static u32 currentTimeMillis();
		static const String getExecutableDirectory();
		template<class T> static void clearPtrVector(std::vector<T*>& ptrs)
		{
			for(size_t s = 0; s < ptrs.size(); s++)
			{
				T* t = (ptrs[s]);
				if(t)
				{
					delete t;
				}
			}
			ptrs.clear();
		}

		template<class T> static void clearPtrMap(std::map<String,T*>& ptrs)
		{
#ifdef WIN32
			std::map<String,T*>::iterator iter;
#else
			typedef typename std::map<String, T*>::iterator iterType;
			iterType iter;
#endif
			iter = ptrs.begin();
			while(iter != ptrs.end())
			{
				T* t = (iter->second);
				if(t){ delete t; }
				iter++;
			}
			ptrs.clear();
		}

		template<class T> static void clearPtrStack(std::stack<T*>& stk)
		{
			while(!stk.empty())
			{
				T* t = (stk.top());
				if(t){ delete t; }
				stk.pop();
			}
		}

		template<class T> static void clearPtrQueue(std::queue<T*>& stk)
		{
			while(!stk.empty())
			{
				T* t = (stk.front());
				if(t){ delete t; }
				stk.pop();
			}
		}
		template<class T> static void clearStack(std::stack<T>& stk)
		{
			while(!stk.empty())
			{
				stk.pop();
			}
		}
		template<class T> static void clearQueue(std::queue<T>& stk)
		{
			while(!stk.empty())
			{
				stk.pop();
			}
		}
		template<class T> static void swapValues(T& a, T& b)
		{
			T c = a;
			a = b;
			b = c;
		}

		static const String trimString(const String str);
		static size_t getFileSize(const String fileName);


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
		virtual ~ScopedPointer(){ if(pT){ delete pT;} }
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
		DDSS_FORCE_BY_REF_ONLY(ScopedPointer);
#endif
		
	};

	template<class T> class ScopedArray
	{
	public:
		ScopedArray(T* t) : pT(t){}
		virtual ~ScopedArray(){ if(pT){ delete[] pT; } }
	private:
		T* pT;
		DDSS_FORCE_BY_REF_ONLY(ScopedArray);
	};


	class DSSFRAMEWORKAPI Random
	{
	public:
		static void seed(const u32 seedValue);
		static void seedCurrentTime();
		static void resetSeed();
		static u32  nextRandom();
	};
	
	
	///
	///	A ZLib based compressor / decompressor
	///
	class DSSFRAMEWORKAPI Compressor
	{
	public:
		static size_t estimateCompressedSize(size_t inSize);
		static size_t compress(const void* inBuffer, size_t inSize, void* outBuffer, size_t outSize);
		static size_t decompress(const void* inBuffer, size_t inSize, void* outBuffer, size_t outSize);
	private:
		DSS_FORCE_BY_REF_ONLY(Compressor);
	};


};
#endif


