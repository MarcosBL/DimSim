#ifndef _DDSS_SCREEN_CMD_H_
#define _DDSS_SCREEN_CMD_H_
#include "../common/api.h"

namespace dm
{
	class ScreenCmd
	{
	public:
		ScreenCmd(){}
		virtual ~ScreenCmd(){}
		
		bool parse(const String uriStr);
		
		const String& getAppName() const{ return appName; }
		const String& getConfKey() const{ return confKey; }
		const String& getStreamName() const{ return streamName; }
		const String& getRole() const{ return role; }
		const String& getCmdName() const{ return cmdName; }
		const String& getClientId() const{ return clientId; }
		

	private:
		String appName;
		String confKey;
		String streamName;
		String role;
		String cmdName;
		String clientId;
	};
};

inline
std::ostream& operator << (std::ostream& os, const dm::ScreenCmd& cmd)
{
	return (os<<"CMD :: App="<<cmd.getAppName()<<"/Conf="<<cmd.getConfKey()<<"/Stream="<<cmd.getStreamName()<<"/Role="<<cmd.getRole()<<"/Cmd="<<cmd.getCmdName()<<"/Id="<<cmd.getClientId()<<"]");
}
#endif

