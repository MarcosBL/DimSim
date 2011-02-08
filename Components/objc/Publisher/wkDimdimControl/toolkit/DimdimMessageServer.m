//
//  DimdimMessageServer.m
//  dos
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "DimdimMessageServer.h"
#import "xproperty.h"

@implementation DimdimMessageServer

- (id) init
{
	if (![super init]) 
	{
		return nil;
	}
	
	self->m_bServerUp = NO;
	
	self->m_clients = [[NSMutableDictionary alloc] init];
	[self->m_clients retain];
	
	self->m_property = nil;
	self->m_sRegistrationKey = [[NSString alloc] init];
	
	
	return self;
}

- (void) dealloc
{
	[self->m_clients release];
	[self->m_sRegistrationKey release];
	self->m_property = nil;
	[super dealloc];
}

- (void) setServerStatus : (BOOL) status
{
	self->m_bServerUp = status;
}

- (void) setPropertyObject : (xproperty*) property
{
	self->m_property = property;
}

- (BOOL) validateRegistration : (xproperty*) property 
				  bAffliction : (bool) bAffliction
{
	if (property == nil)
		return NO;
	
	if ([self->m_sRegistrationKey length] > 0)
	{
		if ([[property retrieveRegistration] compare:self->m_sRegistrationKey] == NSOrderedSame)
			return YES;
		return NO;
	}

	if (true == bAffliction)
		return NO; // A mighty destructor called this.
	
	if (NO == self->m_bServerUp)
	{
		self->m_property = property;
		self->m_sRegistrationKey = [self->m_property retrieveRegistration];
		return YES;
	}
	
	return NO;
}

- (id) connectionDidDie: (id) val {
    NSEnumerator * info;
    info = [[val userInfo] keyEnumerator];
    return self;
}

- (BOOL) registerClient: (id) client withName: (NSString *) clientName
{
    if ([self->m_clients objectForKey:clientName] == nil) 
	{
		[self->m_clients setObject:client forKey:clientName];
		return YES;
    } 
	else 
	{
		return NO;
    }
}

- (BOOL) unregisterClient: (NSString *) clientName
{
    if ([self->m_clients objectForKey:clientName] != nil) 
	{
		[self->m_clients removeObjectForKey:clientName];
		return YES;
    } 
	else 
	{
		return NO;
    }
}

- (BOOL) acceptMessage: (NSString *) msg from:(NSString *) sender
{
    id obj = [self->m_clients objectForKey:sender];
    if (obj != nil) 
	{
		// Set this to xproperty
		
		if ([msg rangeOfString:@"{screencast"].location == 0)
		{
			[self->m_property setScreencastResult:msg];
			if ([msg compare:@"{screencastResult:\"1\"}"] == NSOrderedSame)
			{
				// TBD -- Bharat Varma
//				[Translator executeScreencasterMenu:@"enable"];
			}
			else
			{
				// TBD -- Bharat Varma
//				[Translator executeScreencasterMenu:@"disable"];
			}
		}
		else if ([msg rangeOfString:@"ServerUp"].location == 0)
		{
			// Dont' do anything
		}
		else if ([msg rangeOfString:@"Shutdown"].location == 0)
		{
			// TBD -- Bharat Varma
		}
		
		return YES;
    } 
	else 
	{
		return NO;
    }
}

- (BOOL) sendMessage : (NSString*) msg to : (NSString*) recipient
{
	id obj = [self->m_clients objectForKey:recipient];
	if (obj != nil)
	{
		return [obj messageFromServer:msg];
	}
	
	return NO;
}

@end