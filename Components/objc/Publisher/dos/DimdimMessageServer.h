//
//  DimdimMessageServer.h
//  dos
//
//  Created by Bharat Nadimpalli on 21/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//


/* 
 * DimdimMessageServer is a Distributed Objects based server built *specifically*
 * to enable communication between the browser control, tray icon and the screencaster.
 *
 * The server runs in a separate process and each of the 3 components have a Distributed Objects
 * based client which can communicate with this server.
 *
 * As such, the server needs to be protected and tuned to different scenarios and made as
 * fault-tolerant as possible. The following guidelines should help achieve that -
 *
 * 1. Apart from browser control client, any other component is allowed to register multiple times.
 *			[ This is so that in case a component crashes, registration would still succeed ].
 * 2. When a message is sent by a component, the future receiver is mentioned. The message is received by the server
 *		and then forwarded to the actual receipient.
 * 3. Apart from wkplugin, all other components use the same registrationID for each registration.
 */

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


@interface DimdimMessageServer : NSObject <DServerMessagingProtocol>
{
	NSMutableDictionary* m_clients;
	NSMutableDictionary* m_registrations;
}

- (id) init;

@end