#ifndef _DDSS_LOGGER_H_
#define _DDSS_LOGGER_H_
#include "Includes.h"
#include "Lock.h"

#define DDSS_VERBOSE(comp)	(dm::Logger::verbose()<<" ["<<comp<<"] ")
#define DDSS_DEBUG(comp)	(dm::Logger::debug()<<" ["<<comp<<"] ")
#define DDSS_INFO(comp)		(dm::Logger::info()<<" ["<<comp<<"] ")
#define DDSS_WARNING(comp)		(dm::Logger::warn()<<" ["<<comp<<"] ")
#define DSS_WARNING	DDSS_WARNING
#define DDSS_ERROR(comp)		(dm::Logger::error()<<" ["<<comp<<"] ")
#define DDSS_ERR(comp) 		DDSS_ERROR(comp)
#define DDSS_WARN(comp) 	DDSS_WARNING(comp)
#define DDSS_AUDIT(comp)	(dm::Logger::audit()<<" ["<<comp<<"] ")
#define DDSS_DETAIL_VERBOSE()	(dm::Logger::verbose()<<__FILE__<<" "<<__LINE__<<" ")
#define DDSS_DETAIL_DEBUG()	(dm::Logger::debug()<<__FILE__<<" "<<__LINE__<<" ")
#define DDSS_DETAIL_INFO()		(dm::Logger::info()<<__FILE__<<" "<<__LINE__<<" ")
#define DDSS_DETAIL_WARN()		(dm::Logger::warn()<<__FILE__<<" "<<__LINE__<<" ")
#define DDSS_DETAIL_ERR()		(dm::Logger::err()<<__FILE__<<" "<<__LINE__<<" ")
#define DDSS_INPUT_ERROR(comp,in) (DDSS_ERROR(comp)<<"[Input Marker : "<<in.getMarkerPosition()<<"/ Remaining : "<<in.getRemainingByteCount()<<"] ")
#define DDSS_INPUT_VERBOSE(comp,in) (DDSS_VERBOSE(comp)<<"[Input Marker : "<<in.getMarkerPosition()<<"/ Remaining : "<<in.getRemainingByteCount()<<"] ")

namespace dm
{

	
		class NullOutputStream : public std::ostream
		{
		public:
			NullOutputStream() : std::ostream(0){}
			virtual ~NullOutputStream() {}
		};
		class Logger
		{
		public:
			 enum Level
			 {
				 LL_VERBOSE = 0x00,
				 LL_DEBUG = 0x01,
				 LL_INFO = 0x02,
				 LL_WARNING = 0x03,
				 LL_ERROR= 0x04,
				 LL_SILENT = 0x05
			 };
			 static void createConsoleLog();
			 static void create(const String logname, bool appendMode = false);
			 static std::ostream& verbose();
			 static std::ostream& debug();
			 static std::ostream& info();
			 static std::ostream& warn();
			 static std::ostream& error();
			 static std::ostream& audit();
			 static void setLevel(Level l){ logLevel = l; }
			 
			 static std::ostream& getLogStream();
			 static void setAuditOn(bool b){ auditOn = b; }
			 static void setRollOver(u32 sizeKB){ rollOverLimit = sizeKB * 1024; }
		private:
			static std::ofstream logFile;
			static bool auditOn;
			static Level logLevel;
			static NullOutputStream nullStream;
			static bool consoleLog;
			static Lock logLock;
			static size_t rollOverLimit;
			static String logFileName;
			static String currentLogName;
			static int rolloverIndex;
			static size_t logCount;
			static size_t getLogFileSize();
			static void rollOver();
			
		};
	
	

}

#endif /*LOGGER_H_*/


