//
//  Translator.h
//  wkDimdimControl
//
//  Created by Bharat Nadimpalli on 22/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface Translator : NSObject {

}

+ (bool) jsonArgsToMap : (NSString*) args 
				argMap : (NSMutableDictionary*) argMap;

+ (bool) browserArgsToMap : (NSString*) args 
				   argMap : (NSMutableDictionary*) argMap;

+ (NSString*) getScreencastProfile;

+ (bool) setScreencastProfile : (int) iVal;

+ (bool) executePublisherRun;

+ (bool) executePublisherKill;

+ (bool) executeScreencasterShareAndConnect : (NSString*) handle 
								 connectURL : (NSString*) connectURL;

+ (bool) executeScreencasterShare : (NSString*) handle;

+ (bool) executeScreencasterStop;

+ (bool) executeScreencasterKill;

+ (bool) executeMessageServerKill;

@end