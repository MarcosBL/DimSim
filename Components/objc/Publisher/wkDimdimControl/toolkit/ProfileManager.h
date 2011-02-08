//
//  ProfileManager.h
//  wkDimdimControl
//
//  Created by Bharat Nadimpalli on 27/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface ProfileManager : NSObject {
}

+ (void) InitializeConfig;
+ (int) RetrieveConfig : (NSString*) key;
+ (void) enforceHighBWProfile;
+ (void) enforceMediumBWProfile;
+ (void) enforceLowBWProfile;
+ (void) UpdateConfig : (NSString*) key withValue : (int) value;

@end
