#ifndef _DDSS_IDGENERATOR_H_
#define _DDSS_IDGENERATOR_H_
#include "Includes.h"
namespace dm
{

	class IDGenerator
	{
	public:
		static const String& generateClientId(String& id);
	};



}

#endif /*IDGENERATOR_H_*/


