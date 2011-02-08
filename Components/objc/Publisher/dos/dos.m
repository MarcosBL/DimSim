#import <Foundation/Foundation.h>
#import "DimdimMessageServer.h"

NSAutoreleasePool* pool;
DimdimMessageServer* server;

void disconnect()
{	
	[server release];
	[pool drain];
	
	exit(1);
}

int main (int argc, const char * argv[]) 
{
	signal(SIGHUP, SIG_IGN);
    signal(SIGPIPE, SIG_IGN);
    signal(SIGTERM, disconnect);
    signal(SIGINT, disconnect);
    signal(SIGQUIT, disconnect);
	
	pool = [[NSAutoreleasePool alloc] init];

	server = [[DimdimMessageServer alloc] init];
	NSConnection *defaultConnection = [NSConnection defaultConnection];
	[[NSNotificationCenter defaultCenter] addObserver:server
											 selector:@selector(connectionDidDie:)
												 name:NSConnectionDidDieNotification
											   object:defaultConnection];
	[defaultConnection setRootObject:server];
	if ([defaultConnection registerName:@"1B863447-467F-46D2-A6C6-479410378607"] == NO)
	{
		[server release];
		[pool drain];
		return 0;
	}

	[[NSRunLoop currentRunLoop] run];
	
	[server release];
    [pool drain];
    return 0;
}
