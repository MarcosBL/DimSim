//
//  dToolkit.h
//  helpers
//
//  Created by Bharat Nadimpalli on 29/04/08.
//  Copyright 2008 Dimdim Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>

// Forward declare byte buffer
@class dByteBuffer;

// Curl callback function
size_t CurlCallback(void* ptr, size_t size, size_t nmemb, void* data);

#define SAFE_DELETE_PTR(ptr) if (ptr){ @try{free(ptr); ptr = 0;} @catch(NSException* nse) {NSLog(@"Exception caught in release");} }
#define SAFE_DELETE_ARRAY(ptr) if (ptr){ @try{free(ptr); ptr = 0;} @catch(NSException* nse) {NSLog(@"Exception caught in release");} }

struct DimdimReflectorResponse
{
	char* data;
	size_t size;
};

@interface dScopedCharArr : NSObject
{
@private
	char* m_arr;
}

- (id) initWithCharArr : (char*) t;

@end

@interface dTimeStampGenerator : NSObject
{
@private
	NSTimeInterval startTime;
}

+ (NSTimeInterval) getCurrentTimeMillis;
- (NSTimeInterval) getTimeStamp;
- (void) reset;

@end

@interface DimdimHelpers : NSObject
{
}

+ (unsigned int) ValidResponseHeaders : (dByteBuffer*) dHeaders cmd : (NSString*) cmd;
+ (NSString*) PrepareURL : (NSString*) cmd handlerID : (NSString*) handlerID role : (NSString*) role;
+ (void) EnumerateFileLocation : (const char[]) filename filepath : (char[]) filepath;
+ (void) logWrite : (FILE*) fd nsData : (NSString*) nsData;
+ (void) logWrite : (FILE*) fd buf : (const char*) buf len : (const int) len;

@end

@interface CurlClientData : NSObject
{
@public
	NSString*	m_subscriptionID;
	NSString*	m_streamName;
	NSString*	m_peerAddress;
	NSString*	m_peerPort;
	NSString*	m_metaData;
	int			m_maxRetries;
	double		m_maxWaitTime;
}

+ (CurlClientData*) getInstance;

@end