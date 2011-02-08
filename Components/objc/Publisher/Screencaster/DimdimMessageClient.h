//
//  DimdimMessageClient.h
//  doc
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>

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
	id m_server;
	BOOL m_isConnected;
}

- (BOOL) registerWithServer;
- (BOOL) deregisterFromServer;
- (BOOL) messageToServer: (NSString *) msg 
					from: (NSString *) sender;
- (BOOL) isConnected;

@end
