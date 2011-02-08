//
//  DimdimMessageClient.m
//  doc
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import "DimdimMessageClient.h"


@implementation DimdimMessageClient

- (id) init
{
	if (![super init])
		return nil;	
	return self;
}

- (void) connectionDidDie : (NSNotification*) notification
{
	self->m_isConnected = NO;
}

- (BOOL) registerWithServer
{
	m_server = [[NSConnection rootProxyForConnectionWithRegisteredName:@"1B863447-467F-46D2-A6C6-479410378607" host:nil] retain];
	
	if(nil == m_server)
	{
		return NO;
	}
	else
	{
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(connectionDidDie:)
													 name:NSConnectionDidDieNotification
												   object:nil];
		[m_server setProtocolForProxy:@protocol(DServerMessagingProtocol)];
		self->m_isConnected = ([m_server registerClient:self withName:@"dsc" withRegistrationID:@"dsc"] == YES)? YES : NO;
	}
	
	return self->m_isConnected;
	
}

- (BOOL) deregisterFromServer
{
	id tempServer = [[NSConnection rootProxyForConnectionWithRegisteredName:@"1B863447-467F-46D2-A6C6-479410378607" host:nil] retain];
	[m_server unregisterClient:@"dsc"];
	self->m_isConnected = NO;
	return YES;
}


- (BOOL) messageToServer:(NSString *) msg 
					from: (NSString *) sender 
					  to: (NSString*) recipient
{

	id tempServer = [[NSConnection rootProxyForConnectionWithRegisteredName:@"1B863447-467F-46D2-A6C6-479410378607" host:nil] retain];
	
	if (NO == m_isConnected)
	{
		return NO;
	}
	
	@try
	{
		BOOL retval = [tempServer sendMessage:msg from:sender to:recipient];
		[tempServer release];
		return retval;
	}
	@catch(NSException* exception)
	{
		rfbLog("caught exception while sending message to distributed server\n");
		[tempServer release];
		return NO;
	}
}

- (BOOL) messageFromServer : (NSString *) msg
{
	// This component doesn't expect messages from any other component, as of now.
	return YES;
}

- (BOOL) isConnected
{
	return self->m_isConnected;
}

@end
