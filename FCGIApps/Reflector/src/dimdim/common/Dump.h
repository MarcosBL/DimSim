#ifndef DUMP_H_
#define DUMP_H_
#include "Includes.h"
namespace dm
{
	class Dump
	{
	public:
		static bool hexDump(std::ostream& out, const void* buf, size_t len);
		static bool binDump(const char* fileName, const void* buf, size_t len);
	};
};

#endif /*DUMP_H_*/


