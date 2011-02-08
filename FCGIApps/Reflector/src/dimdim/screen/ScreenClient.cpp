#include "ScreenClient.h"
#include "Screen.h"

namespace dm
{
	
	ScreenClient::ScreenClient(ScreenClient::Role r, Screen& s): screen(s), role(r)
	{
		IDGenerator::generateClientId(id);
		updateStartTime();
		opCount = 0;
	}
	ScreenClient::~ScreenClient()
	{

	}
	
	bool ScreenClient::handleOpen(const void* buf, size_t len, OutputStream& out)
	{
		updateLastTime();
		return onHandleOpen(buf,len,out);
	}
	bool ScreenClient::handlePost(const void* buf, size_t len, OutputStream& out)
	{
		opCount++;
		updateLastTime();
		return onHandlePost(buf,len,out);
	}
	bool ScreenClient::handlePoll(OutputStream& out)
	{
		updateLastTime();
		return onHandlePoll(out);
	}
	Screen& ScreenClient::getScreen()
	{
		return screen;
	}
	const Screen& ScreenClient::getScreen() const
	{
		return screen;
	}
};

