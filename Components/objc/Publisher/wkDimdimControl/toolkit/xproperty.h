//
//  xproperty.h
//  wkDimdimControl
//
//  Created by Bharat Nadimpalli on 22/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface xproperty : NSObject {
	NSString*	m_screencastURL;
	NSString*	m_screencastResult;
	NSString*	m_registrationID;
	NSLock*		propertyLock;
	bool		m_bServerStatus;
}
- (id) init;

- (NSString*) getScreencastResult : (bool) bExecute;

- (void) setScreencastResult : (NSString*) result;

- (void) resetScreencastResult;

- (NSString*) getScreencastURL;

- (void) setScreencastURL : (NSString*) url;

- (double) getVersion;

- (void) setRegistration : (NSString*) reg;

- (NSString*) retrieveRegistration;

@end