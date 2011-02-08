//
//  DimdimMessageClient.m
//  doc
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import "DimdimMessageClient.h"
#import "includes.h"

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
		if ([m_server isClientRegistered:@"Screencaster"] == YES)
			return NO;
		self->m_isConnected = ([m_server registerClient:self withName:@"Screencaster" withRegistrationID:@"Screencaster"] == YES)? YES : NO;
	}
	
	return self->m_isConnected;
}

- (BOOL) deregisterFromServer
{
	[m_server unregisterClient:@"Screencaster"];
	self->m_isConnected = NO;
	return YES;
}


- (BOOL) messageToServer:(NSString *) msg from: (NSString *) sender
{
	if (NO == m_isConnected)
		return NO;
	return [m_server sendMessage:msg from:sender to:@"wkplugin"];
}

- (BOOL) messageFromServer : (NSString *) msg
{	
	if ([msg compare:@"progress"] == NSOrderedSame)
	{
//		[gMenuBarIcon setImage:[NSImage imageNamed:@"green"]];
		[gMenuBarIcon setImage:[NSImage imageNamed:@"MenuBarMacOn.png"]];
		[gMenuBarIcon setToolTip:@"Screencasting in progress"];
	}
	else if ([msg compare:@"idle"] == NSOrderedSame)
	{
//		[gMenuBarIcon setImage:[NSImage imageNamed:@"red"]];
		[gMenuBarIcon setImage:[NSImage imageNamed:@"MenuBarMacOff.png"]];
		[gMenuBarIcon setToolTip:@"Web Meeting Publisher"];
	}
	else
	{
		// Do nothing
	}
	
	return YES;
}

- (BOOL) isConnected
{
	return self->m_isConnected;
}

@end
