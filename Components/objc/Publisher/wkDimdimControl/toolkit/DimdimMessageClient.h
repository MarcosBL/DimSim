//
//  DimdimMessageClient.h
//  doc
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@class xproperty;

//  Client Protocol
@protocol DClientMessagingProtocol

- (BOOL) messageFromServer : (bycopy NSString *) msg;

@end

// Server Protocol
@protocol DServerMessagingProtocol

- (BOOL) registerClient : (byref id) client
			   withName : (bycopy NSString*) clientName 
	 withRegistrationID : (bycopy NSString*) registrationID;

- (BOOL) unregisterClient : (bycopy NSString*) clientName;

- (BOOL) sendMessage : (bycopy NSString*) msg 
				from : (bycopy NSString*) sender 
				  to : (bycopy NSString*) recipient;

- (int) isRegistrationAllowed : (bycopy NSString*) clientName 
		   withRegistrationID : (bycopy NSString*) registrationID;

- (BOOL) isClientRegistered : (bycopy NSString*) clientName;
- (BOOL) isProcessRunning : (bycopy NSString*) processName;

@end

@interface DimdimMessageClient : NSObject <DClientMessagingProtocol>
{
	id messageServer;
	BOOL m_isConnected;
	xproperty* m_property;
}

- (BOOL) registerWithServer : (bycopy NSString*) registrationID;;
- (void) deregisterFromServer;
- (BOOL) messageToServer: (NSString *) msg 
					from: (NSString *) sender 
					  to: (NSString *) recipient;
- (BOOL) isConnected;
- (BOOL) isComponentAlive : (NSString*) component;
- (int) isRegistrationAllowed : (NSString*) reg;
- (void) setProperty : (xproperty*) property;
- (BOOL) setupMessageServer;

@end
