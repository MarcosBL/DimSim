#include "Logger.h"
#include "Timer.h"

#define DDSS_LOG_ROLLOVER_LIMIT	(512*1024)

namespace dm
{

		bool Logger::consoleLog = true;
		std::ofstream Logger::logFile;
		Logger::Level Logger::logLevel = Logger::LL_WARNING;
		NullOutputStream Logger::nullStream;
		bool Logger::auditOn = false;
		Lock Logger::logLock;
		size_t Logger::rollOverLimit = DDSS_LOG_ROLLOVER_LIMIT;

		String Logger::logFileName = "";
		String Logger::currentLogName = "";
		int Logger::rolloverIndex = 0;
		size_t Logger::logCount = 0;
		void Logger::createConsoleLog()
		{
			consoleLog = true;
		}
		std::ostream& Logger::getLogStream()
		{
			if(consoleLog)
			{
				return std::cout;
			}
			else
			{
				ScopedLock sl(logLock);
				logCount++;

				if(rollOverLimit > 0)
				{
					rollOver();
				}
				return logFile;
			}
		}
		void Logger::create(const String logName, bool appendMode)
		{
			consoleLog = false;
			if(logFile.is_open())
			{
				logFile.close();
			}
			rolloverIndex = 0;

			std::ostrstream os;
			os<<logName<<"-"<<getpid()<<"-"<<rolloverIndex<<"-"<<Timer::currentTimeMillis()<<".log"<<std::ends;
			String logName2 = os.str();
			os.rdbuf()->freeze(false);
			logFile.open(logName2.c_str(), (appendMode?(std::ios::out | std::ios::app) : (std::ios::out)) );
			consoleLog = !(logFile.is_open());
			logFileName = logName;
			currentLogName = logName2;
			std::cout<<"Log File : "<<currentLogName<<std::endl;
		}
		size_t Logger::getLogFileSize()
		{
			struct stat sbuf;
			if(stat((char *)(currentLogName.c_str()),&sbuf) == 0)
			{
				return (size_t)sbuf.st_size;
			}
			return 0;
		}
		void Logger::rollOver()
		{
			//ScopedLock ll(logLock);
			if(!consoleLog)
			{
				if(logCount == 100)
				{
					logCount = 0;
					size_t len = getLogFileSize();
					if(len >= Logger::rollOverLimit)
					{
						std::cout<<"rolling over log file..."<<std::endl;
						Logger::rolloverIndex++;
						logFile.close();
						std::ostrstream os;
						os<<logFileName<<"-"<<getpid()<<"-"<<rolloverIndex<<"-"<<Timer::currentTimeMillis()<<".log"<<std::ends;
						String logName = os.str();
						os.rdbuf()->freeze(false);
						bool appendMode= false;
						logFile.open(logName.c_str(), (appendMode?(std::ios::out | std::ios::app) : (std::ios::out)) );
						currentLogName = logName;

						std::cout<<"rolled over to..."<<currentLogName<<std::endl;
					}
				}
			}
		}
		std::ostream& Logger::verbose()
		{
			if(logLevel == LL_VERBOSE && (consoleLog || logFile.is_open()))
			{
				return (getLogStream()<< "<VERBOSE> "<<Timer::timeStamp()<<" - ");
			}
			else
			{
				return nullStream;
			}
			
			
		}
		std::ostream& Logger::audit()
		{
			return (auditOn?(getLogStream()<<"[AUDIT] ") : nullStream);
		}
		std::ostream& Logger::debug()
		{
			if(logLevel <= LL_DEBUG && (consoleLog || logFile.is_open()))
			{
				logFile.flush();
				return (getLogStream()<< "<DEBUG> "<<Timer::timeStamp()<<" - ");
			}
			else
			{
				return nullStream;
			}
			
			
		}
		std::ostream& Logger::info()
		{
			if(logLevel <= LL_INFO && (consoleLog || logFile.is_open()))
			{
				logFile.flush();
				return (getLogStream()<< "<INFO> "<<Timer::timeStamp()<<" - ");
			}
			else
			{
				return nullStream;
			}
			
			
		}
		std::ostream& Logger::warn()
		{
			if(logLevel <= LL_WARNING && (consoleLog || logFile.is_open()))
			{
				logFile.flush();
				return (getLogStream()<< "<WARNING> "<<Timer::timeStamp()<<" - ");
			}
			else
			{
				return nullStream;
			}
			
			
		}
		std::ostream& Logger::error()
		{
			if(logLevel <= LL_ERROR && (consoleLog || logFile.is_open()))
			{
				logFile.flush();
				return (getLogStream()<< "<ERROR> "<<Timer::timeStamp()<<" - ");
			}
			else
			{
				return nullStream;
			}
			
			
		}

	
	

}


