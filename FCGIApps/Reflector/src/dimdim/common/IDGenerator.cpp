#include "IDGenerator.h"
#include "Timer.h"
namespace dm
{
	const String& IDGenerator::generateClientId(String& id)
	{
			//TODO: Switch to GUID Generation. Hack/Kludgy 64 bit ID generation
			u32 ts = Timer::currentTimeMillis();
			u32 r = rand();
			char buf[256];
			sprintf(buf,"%x-%x",ts,r);
			id = buf;
			return id;
	}

}


