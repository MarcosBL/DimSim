//
//  DimdimMessageServer.m
//  dos
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import "DimdimMessageServer.h"
#import "DSProcessesInfo.h"

@implementation DimdimMessageServer

- (id) init
{
    if (![super init])
		return nil;
	
    m_clients = [[NSMutableDictionary alloc] init];
    [m_clients retain];
	
	m_registrations = [[NSMutableDictionary alloc] init];
	[m_registrations retain];
	    
    return self;
}

- (id) connectionDidDie: (id) val {
    return self;
}

- (BOOL) registerClient : (byref id) client
			   withName : (bycopy NSString*) clientName 
	 withRegistrationID : (bycopy NSString*) registrationID
{
	if ([clientName compare:@"wkplugin"] != NSOrderedSame)
	{
		// Remove any prior registrations
		[self unregisterClient:clientName];
	}
	
    if ([m_clients objectForKey:clientName] == nil) 
	{
		[m_clients setObject:client forKey:clientName];
		[m_registrations setValue:registrationID forKey:clientName];
		NSLog(@"Registered %@ with id = %@", clientName, registrationID);
		return YES;
    } 
	else 
	{
		return NO;
    }
}

- (BOOL) unregisterClient: (NSString *) clientName
{
    if ([m_clients objectForKey:clientName] != nil) 
	{
		[m_clients removeObjectForKey:clientName];	
		[m_registrations removeObjectForKey:clientName];
		return YES;
    } 
	else 
	{
		return NO;
    }
}

- (int) isRegistrationAllowed : (bycopy NSString*) clientName 
			 withRegistrationID : (bycopy NSString*) registrationID
{
	NSString* registration = [m_registrations valueForKey:clientName];
	
	if (registration == nil)
		return 1;
	
	if ([registration compare:registrationID] == NSOrderedSame)
		return 2;
	else
	{
		return 0;
	}
}

- (BOOL) isClientRegistered : (bycopy NSString*) clientName
{
	// Do basic checks first
	id obj = [m_clients objectForKey:clientName];
	if (obj == nil)
		return NO;
	
	// Now try to send a message
	return [obj messageFromServer:@"ACK"];
}

- (BOOL) isProcessRunning : (bycopy NSString*) processName
{
	return isProcessRunningByName(processName);
}

- (BOOL) sendMessage : (bycopy NSString*) msg 
				from : (bycopy NSString*) sender 
				  to : (bycopy NSString*) recipient
{
	id senderObj = [m_clients objectForKey:sender];
	if (senderObj == nil)
	{
		return NO;
	}
	
	id recipientObj = [m_clients objectForKey:recipient];
	
	if (recipientObj == nil)
	{
		return NO;
	}
	
	BOOL value = NO;
	@try
	{
		value =  [recipientObj messageFromServer:msg];
	}
	@catch(NSException* exception)
	{
		[recipientObj release];
		[self unregisterClient:recipient];
	}
	
	return value;
}

@end