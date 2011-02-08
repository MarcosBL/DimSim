#include "ScreenCmd.h"

namespace dm
{
	
	bool ScreenCmd::parse(const String uriStr)
	{
		StringTokenizer st(uriStr,"/");
		DDSS_VERBOSE("ScreenCmd")<<"Parsing uri : "<<uriStr<<" gave "<<st.countTokens()<<" tokens!"<<std::endl;
		if(st.countTokens() < 2)
		{
			return false;
		}
		else 
		{
			appName = st.nextToken();
			if(appName == "dimdimBWTest")
			{
				cmdName = st.nextToken();
				if(cmdName == "recv")
				{
					clientId = st.nextToken();
				}	
				return true;
			}
			else
			{
				confKey = st.nextToken();
				streamName = st.nextToken();
				role = st.nextToken();
				cmdName = st.nextToken();
				clientId = st.nextToken();
				//std::cout<<(*this)<<std::endl;
				return true;
			}
		}
	}
};

