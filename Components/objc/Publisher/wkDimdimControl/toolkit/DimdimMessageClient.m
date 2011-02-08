//
//  DimdimMessageClient.m
//  doc
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import "DimdimMessageClient.h"
#import "xproperty.h"
#import <unistd.h>

void* startDimdimMessageServer()
{
	system("\"/Applications/Dimdim/Screencaster.app/Contents/MacOS/dos\"");
	return NULL;
}

@implementation DimdimMessageClient

- (id) init
{
	if (![super init])
		return nil;	
	self->m_isConnected = NO;
	self->m_property = nil;
	
	return self;
}

- (void) connectionDidDie : (NSNotification*) notification
{
	self->m_isConnected = NO;
}

- (BOOL) setupMessageServer
{
	// First see if the server exists -
	
	NSConnection* serverConn = [NSConnection connectionWithRegisteredName:@"1B863447-467F-46D2-A6C6-479410378607" host:nil];
	if (serverConn == nil)
	{
		// Start the server
		pthread_t serverThread;
		pthread_create(&serverThread, NULL, startDimdimMessageServer, NULL);
		
		int i = 0;
		while (i < 50)
		{
			// Wait for a maximum of 5 seconds, while trying to access the server (vended) object
			self->messageServer = [[NSConnection rootProxyForConnectionWithRegisteredName:@"1B863447-467F-46D2-A6C6-479410378607" host:nil] retain];
			[self->messageServer setProtocolForProxy:@protocol(DServerMessagingProtocol)];
			if (self->messageServer != nil)
				return YES;
			
			usleep(100000);		
			i = i + 1;
		}		
	}

	// One shot to get the server (vended) object
	if (self->messageServer == nil)
	{
		self->messageServer = [[NSConnection rootProxyForConnectionWithRegisteredName:@"1B863447-467F-46D2-A6C6-479410378607" host:nil] retain];
		[self->messageServer setProtocolForProxy:@protocol(DServerMessagingProtocol)];
		if (self->messageServer != nil)
			return YES;
	}
	else
	{
		return YES;
	}
	
	return NO;
}

- (BOOL) isComponentAlive : (NSString*) component
{
	return [self->messageServer isProcessRunning:component];
}

- (int) isRegistrationAllowed : (NSString*) reg
{
	if ([self setupMessageServer] == NO)
		return -1;
	
	return [self->messageServer isRegistrationAllowed:@"wkplugin" withRegistrationID:reg];
}

- (BOOL) registerWithServer : (bycopy NSString*) registrationID
{
	if(nil == self->messageServer)
	{
		return NO;
	}
	else
	{
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(connectionDidDie:)
													 name:NSConnectionDidDieNotification
												   object:nil];
		
		self->m_isConnected = ([messageServer registerClient:self withName:@"wkplugin" withRegistrationID:registrationID] == YES)? YES : NO;
	}
	
	return self->m_isConnected;
	
}

- (void) deregisterFromServer
{
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	[messageServer unregisterClient:@"wkplugin"];
	self->m_isConnected = NO;
}

- (BOOL) messageToServer: (NSString *) msg 
					from: (NSString *) sender 
					  to: (NSString *) recipient
{
	if (NO == m_isConnected)
		return NO;
	return [messageServer sendMessage:msg from:sender to:recipient];
}

- (BOOL) messageFromServer : (NSString *) msg
{
	if ([msg rangeOfString:@"{screencast"].location == 0)
	{
		[self->m_property setScreencastResult:msg];
		[msg retain];
	}
	else if ([msg rangeOfString:@"Shutdown"].location == 0)
	{
		system("killall dos");
	}

	return YES;
}

- (BOOL) isConnected
{
	return self->m_isConnected;
}

- (void) setProperty : (xproperty*) property
{
	self->m_property = property;
}

@end
