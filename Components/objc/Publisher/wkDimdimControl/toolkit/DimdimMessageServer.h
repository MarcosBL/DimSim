//
//  DimdimMessageServer.h
//  dos
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>
@class xproperty;

//  Client Protocol
@protocol DClientMessagingProtocol

- (BOOL) messageToServer : (bycopy NSString *) msg 
					 from: (bycopy NSString *) sender;

- (BOOL) messageFromServer : (bycopy NSString *) msg;

@end

// Server Protocol
@protocol DServerMessagingProtocol

- (BOOL) registerClient : (byref id) client
			   withName : (bycopy NSString*) clientName;

- (BOOL) unregisterClient : (bycopy NSString*) clientName;

- (BOOL) acceptMessage : (bycopy NSString*) msg 
				  from : (bycopy NSString*) sender;

@end


@interface DimdimMessageServer : NSObject <DServerMessagingProtocol>
{
	NSMutableDictionary *m_clients;
	xproperty* m_property;
	NSString* m_sRegistrationKey;
	BOOL	m_bServerUp;
}

- (BOOL) sendMessage : (NSString*) msg 
				  to : (NSString*) recipient;

- (BOOL) validateRegistration : (xproperty*) property 
				  bAffliction : (bool) bAffliction;

- (void) setPropertyObject : (xproperty*) property;

- (void) setServerStatus : (BOOL) status;

@end