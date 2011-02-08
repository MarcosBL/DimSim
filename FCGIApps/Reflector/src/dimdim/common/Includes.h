#ifndef _DDSS_INCLUDES_H_
#define _DDSS_INCLUDES_H_


#include <cstdlib>
#ifdef _WIN32
#include <process.h>
#else
#include <unistd.h>
extern char ** environ;
#endif
#include "fcgio.h"
#include "fcgi_config.h"  // HAVE_IOSTREAM_WITHASSIGN_STREAMBUF

#ifdef _WIN32
#pragma comment(lib,"libfcgi")
#endif

#include <string>
#include <fstream>
#include <strstream>
#include <map>
#include <vector>
#include <cmath>

#ifndef _WIN32
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/queue.h>
#include <unistd.h>
#include <sys/time.h>
#else
#include <windows.h>
#endif

#include <fcntl.h>
#include <cstdlib>
#include <cstdio>
#include <cstring>
#include <errno.h>

#include <zlib.h>

#define DSSFRAMEWORKAPI
#define DSSAMFAPI
#define DSSFLVAPI


#define DSS_FORCE_BY_REF_ONLY(cls)	DDSS_FORCE_BY_REF_ONLY(cls)
#define DSS_VAL_OF_BOOL(b)			(b?"true":"false")

//#define DDDSS_PROFILER_TRACE_ENABLED 1
#define DDSS_HEXDUMP_ENABLED 1
#define DDSS_DTP_LOGFILE_ENABLED 1
#define DDSS_FCGI_LOOP_THREADS 5
#define DDSS_FORCE_BY_REF_ONLY(cls)	cls(const cls&); const cls& operator = (const cls&)
#define DDSS_SAFE_DELETE_PTR(ptr)	if(ptr){ delete ptr; ptr = 0; }
#define DDSS_SAFE_DELETE_ARRAY(ptr) if(ptr){ delete []ptr; ptr = 0; }
#define DSS_SAFE_DELETE_PTR(ptr)	DDSS_SAFE_DELETE_PTR(ptr)
#define DSS_SAFE_DELETE_ARRAY(ptr)	DDSS_SAFE_DELETE_ARRAY(ptr)
//#define DDSS_DEBUG_EXIT()	exit(1)
#define DDSS_ENABLE_PUB_DUMP 1
#define DDSS_USE_NEW_DECODER 1

//#define DDSS_RECT_DUMP_ENABLED 1

// Some handy typedefs
namespace dm
{
	typedef unsigned char  	u8;
	typedef char		   	c8;
	typedef unsigned short 	u16;
	typedef unsigned int   	u32;
	typedef int			   	s32;
	typedef short		   	s16;
	typedef float		   	f32;
	typedef double		   	f64;
	typedef std::string		String;
	typedef  std::map<String,String> StringTable;
};

using dm::s16;



#endif /*_DDSS_INCLUDES_H_*/


