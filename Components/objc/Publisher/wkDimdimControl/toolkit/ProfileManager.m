//
//  ProfileManager.m
//  wkDimdimControl
//
//  Created by Bharat Nadimpalli on 27/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import "ProfileManager.h"


@implementation ProfileManager

+ (void) InitializeConfig
{
	NSString* cfgPath = [[NSString alloc] initWithFormat:@"/Applications/Dimdim/Screencaster.app/Contents/MacOS/DesktopShare.cfg", getenv("HOME")];
	NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithContentsOfFile:cfgPath];

	if (dict == nil)
	{
		dict = [[NSMutableDictionary alloc] init];
		
		[dict setValue:[NSNumber numberWithInt:2]	forKey:@"Logging"];
		[dict setValue:[NSNumber numberWithInt:10]	forKey:@"LogLevel"];
		[dict setValue:[NSNumber numberWithInt:2]	forKey:@"OperationType"];
		[dict setValue:[NSNumber numberWithInt:1]	forKey:@"EnablePointerAlgorithm"];
		
		[dict setValue:[NSNumber numberWithInt:1]	forKey:@"EnableRestrictedColors"];
		[dict setValue:[NSNumber numberWithInt:6]	forKey:@"CompressLevel"];
		[dict setValue:[NSNumber numberWithInt:6]	forKey:@"JPEGEncodingLevel"];
		[dict setValue:[NSNumber numberWithInt:3]	forKey:@"MaxRetries"];
		
		[dict setValue:[NSNumber numberWithInt:60]	forKey:@"MaxWaitTime"];
		[dict setValue:[NSNumber numberWithInt:32]	forKey:@"BlockSize"];
		[dict setValue:[NSNumber numberWithInt:100]	forKey:@"MinTransactionWait"];
		
		[dict writeToFile:cfgPath atomically:YES];
		[dict release];
	}
	
	[cfgPath release];
}

+ (int) RetrieveConfig : (NSString*) key
{
	[ProfileManager InitializeConfig];
	
	NSString* cfgPath = [[NSString alloc] initWithFormat:@"/Applications/Dimdim/Screencaster.app/Contents/MacOS/DesktopShare.cfg", getenv("HOME")];
	NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithContentsOfFile:cfgPath];
	int val = [[dict valueForKey:key] intValue];
	
	[cfgPath release];
	return val;
}

+ (void) enforceHighBWProfile
{
	[ProfileManager InitializeConfig];
	
	NSString* cfgPath = [[NSString alloc] initWithFormat:@"/Applications/Dimdim/Screencaster.app/Contents/MacOS/DesktopShare.cfg", getenv("HOME")];
	
	NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithContentsOfFile:cfgPath];
	[dict setValue:[NSNumber numberWithInt:0]	forKey:@"EnableRestrictedColors"];
	[dict setValue:[NSNumber numberWithInt:64]	forKey:@"BlockSize"];
	[dict writeToFile:cfgPath atomically:YES];
	
	[cfgPath release];
}

+ (void) enforceMediumBWProfile
{
	[ProfileManager InitializeConfig];
	
	NSString* cfgPath = [[NSString alloc] initWithFormat:@"/Applications/Dimdim/Screencaster.app/Contents/MacOS/DesktopShare.cfg", getenv("HOME")];
	
	NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithContentsOfFile:cfgPath];
	[dict setValue:[NSNumber numberWithInt:0]	forKey:@"EnableRestrictedColors"];
	[dict setValue:[NSNumber numberWithInt:32]	forKey:@"BlockSize"];
	[dict writeToFile:cfgPath atomically:YES];
	
	[cfgPath release];
}

+ (void) enforceLowBWProfile
{
	[ProfileManager InitializeConfig];
	
	NSString* cfgPath = [[NSString alloc] initWithFormat:@"/Applications/Dimdim/Screencaster.app/Contents/MacOS/DesktopShare.cfg", getenv("HOME")];
	
	NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithContentsOfFile:cfgPath];
	[dict setValue:[NSNumber numberWithInt:1]	forKey:@"EnableRestrictedColors"];
	[dict setValue:[NSNumber numberWithInt:16]	forKey:@"BlockSize"];
	[dict writeToFile:cfgPath atomically:YES];
	
	[cfgPath release];
}

+ (void) UpdateConfig : (NSString*) key withValue : (int) value
{
	[ProfileManager InitializeConfig];
	
	NSString* cfgPath = [[NSString alloc] initWithFormat:@"/Applications/Dimdim/Screencaster.app/Contents/MacOS/DesktopShare.cfg", getenv("HOME")];
	
	NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithContentsOfFile:cfgPath];
	[dict setValue:[NSNumber numberWithInt:value]	forKey:key];
	[dict writeToFile:cfgPath atomically:YES];
	
	[cfgPath release];
}

@end