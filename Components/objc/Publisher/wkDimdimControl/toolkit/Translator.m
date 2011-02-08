//
//  Translator.m
//  wkDimdimControl
//
//  Created by Bharat Nadimpalli on 22/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import "Translator.h"
#import "ProfileManager.h"
#import	<unistd.h>

void* startScreencaster(void* parameters)
{
	NSString* args = (NSString*) parameters;
	NSString* cmd = [[NSString alloc] initWithFormat:@"\"/Applications/Dimdim/Screencaster.app/Contents/MacOS/OSXvnc-server\" -connectHost %@", args];
	system([cmd UTF8String]);
	[cmd release];
	return NULL;
}

void* startPublisherTray()
{
	system("\"/Applications/Dimdim/Screencaster.app/Contents/MacOS/Screencaster\"");
	
	return NULL;
}

@implementation Translator

+ (bool) jsonArgsToMap : (NSString*) args 
				argMap : (NSMutableDictionary*) argMap
{
	[argMap removeAllObjects];
	
	// Input : {<name>:"<value>",<name>:"<value>"}
	//
	// Note : <value> itself can have ':'
	
	// Step 1: Remove '{' and '}'
	NSCharacterSet* braces = [NSCharacterSet characterSetWithCharactersInString:@"{}"];
	args = [args stringByTrimmingCharactersInSet:braces];
	
	// Step 2: Break up the string with ',' as delimiter.
	NSArray* argArray = [args componentsSeparatedByString:@","];

	// Step 3: Traverse the array and convert each element of type <name>:"<value>" to a name-value pair in argMap
	NSEnumerator* enumerator = [argArray objectEnumerator];
	NSString* argument;
	while (argument = [enumerator nextObject])
	{
		// Now, separate by quotes. This will give us 3 strings -
		// <name>:, <value> and an empty string.
		
		// Since this is JSON format, we are guaranteed to have double-quotes in the input
		
		NSArray* nvPairs = [argument componentsSeparatedByString:@"\""];
		
		NSString* key = [nvPairs objectAtIndex:0];
		NSString* value = [nvPairs objectAtIndex:1];
		
		// Clean up key
		NSCharacterSet* colons = [NSCharacterSet characterSetWithCharactersInString:@":"];
		key = [key stringByTrimmingCharactersInSet:colons];
		
		[argMap setValue:value forKey:key];
	}
	return true;
}

+ (bool) browserArgsToMap : (NSString*) args 
				   argMap : (NSMutableDictionary*) argMap
{	
	// Input : <name>=<value>&<name>="<value>"
	// i.e. value may or may not be enclosed in double-quotes
	
	// Step 1 : Break up the string  with '&' as delimiter
	NSArray* argArray = [args componentsSeparatedByString:@"&"];
	
	// Step 2 : Traverse the array and convert each element of type <name>=<value> or <name>="<value>" to a name-value pair in argMap
	NSEnumerator* enumerator = [argArray objectEnumerator];
	NSString* argument;
	while (argument = [enumerator nextObject])
	{
		NSArray* nvPairs = [argument componentsSeparatedByString:@"="];
		
		NSString* key = [nvPairs objectAtIndex:0];
		NSString* value = [nvPairs objectAtIndex:1];
		
		// Clean up value if necessary
		NSCharacterSet* quotes = [NSCharacterSet characterSetWithCharactersInString:@"\""];
		value = [value stringByTrimmingCharactersInSet:quotes];
		
		[argMap setValue:value forKey:key];
	}
	return true;
}

+ (NSString*) getScreencastProfile
{
	int iVal = [ProfileManager RetrieveConfig:@"BlockSize"];
	int profileValue = 0;
	if (16 == iVal)
	{
		profileValue = 2;
	}
	else if (32 == iVal)
	{
		profileValue = 1;
	}
	else
	{
		profileValue = 0;
	}
	
	NSString* profile = [[NSString alloc] initWithFormat:@"{BWProfile:\"%d\"}", profileValue];
	return profile;
}

+ (bool) setScreencastProfile : (int) iVal
{
	if (iVal == 0)
	{
		[ProfileManager enforceHighBWProfile];
	}
	else if (iVal == 2)
	{
		[ProfileManager enforceLowBWProfile];
	}
	else
	{
		[ProfileManager enforceMediumBWProfile];
	}
	return true;
}

+ (bool) executePublisherRun
{
	pthread_t publisherID;
	pthread_create(&publisherID, NULL, startPublisherTray, NULL);
	return true;
}

+ (bool) executePublisherKill
{
	system("killall Screencaster");
	return true;
}

+ (bool) executeScreencasterShareAndConnect : (NSString*) handle 
								 connectURL : (NSString*) connectURL
{
	// Handle is currently ignored. We always share desktop
	
	pthread_t screencasterID;
	pthread_create(&screencasterID, NULL, startScreencaster, (void*)connectURL);
	
	return true;
}

+ (bool) executeScreencasterShare : (NSString*) handle
{
	// For now, just a placeholder return value
	return true;
}

+ (bool) executeScreencasterStop
{
	system("killall OSXvnc-server");
	return true;
}

+ (bool) executeScreencasterKill
{
	system("killall -9 OSXvnc-server");
	return true;
}

+ (bool) executeMessageServerKill
{
	system("killall dos");
	return true;
}

@end