//
//  dToolkit.m
//  helpers
//
//  Created by Bharat Nadimpalli on 29/04/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "dToolkit.h"
#import "dByteBuffer.h"

static CurlClientData* CCDInstance_ = nil;

@implementation dScopedCharArr

- (id) initWithCharArr : (char*) t
{
	if (![super init])
		return nil;
	self->m_arr = t;
	return self;
}

- (void) dealloc
{
	SAFE_DELETE_ARRAY(self->m_arr);
	[super dealloc];
}

@end

@implementation dTimeStampGenerator

- (id) init
{
	if (![super init]) 
	{
		return nil;
	}
	[self reset];
	return self;
}

+ (NSTimeInterval) getCurrentTimeMillis
{
	return ([NSDate timeIntervalSinceReferenceDate] * 1000);
}

- (NSTimeInterval) getTimeStamp
{
	return ([dTimeStampGenerator getCurrentTimeMillis] - self->startTime);
}

- (void) reset
{
	self->startTime = [dTimeStampGenerator getCurrentTimeMillis];
}

@end

@implementation CurlClientData

+ (CurlClientData*) getInstance
{
	@synchronized(self)
	{
		if (!CCDInstance_)
		{
			[[self alloc] init]; // assignment not done here
			CCDInstance_->m_peerAddress = [[NSString alloc] init];
			CCDInstance_->m_peerPort = [[NSString alloc] init];
			CCDInstance_->m_streamName = [[NSString alloc] init];
			CCDInstance_->m_subscriptionID = [[NSString alloc] init];
			CCDInstance_->m_metaData = [[NSString alloc] init];
		}
	}
	
	return CCDInstance_;
}

+ (id)allocWithZone:(NSZone *)zone
{
    @synchronized(self) {
        if (CCDInstance_ == nil) {
            CCDInstance_ = [super allocWithZone:zone];
            return CCDInstance_;  // assignment and return on first allocation
        }
    }
    return nil; //on subsequent allocation attempts return nil
}

- (id)copyWithZone:(NSZone *)zone
{
    return self;
}

- (id)retain
{
    return self;
}

- (unsigned)retainCount
{
    return UINT_MAX;  //denotes an object that cannot be released
}

- (void)release
{
    //do nothing
}

- (id)autorelease
{
    return self;
}

@end

@implementation DimdimHelpers

+ (unsigned int) ValidResponseHeaders : (dByteBuffer*) dHeaders cmd : (NSString*) cmd
{
	if ([dHeaders getLength] <= 0)
	{
		rfbLog("\nDetected zero-length header size!!\n");
		return 0;
	}
	
	if (![dHeaders getData:0])
	{
		rfbLog("\nDetected zero-length header content!!\n");
		return 0;
	}
	
	NSString* response = [[NSString alloc] initWithBytes:[dHeaders getData:0] length:[dHeaders getLength] encoding:NSUTF8StringEncoding];
	response = [response substringWithRange:NSMakeRange(0, 15)];
	
	if ([response rangeOfString:@"200 OK"].location != NSNotFound)
		return 1;
	
	rfbLog("%@ for an issue of \"%@\"\n", response, cmd);
	
	//	if ([response compare:@"HTTP/1.1 500"] == NSOrderedSame)
	//		return 0;
	return 0;
}

+ (NSString*) PrepareURL : (NSString*) cmd handlerID : (NSString*) handlerID role : (NSString*) role
{
	return [NSString stringWithFormat:@"%@%@/%@/%@",[CurlClientData getInstance]->m_peerAddress, role, cmd, ([handlerID length] == 0)?@"0":handlerID];
}

+ (void) EnumerateFileLocation : (const char[]) filename filepath : (char[]) filepath
{
	char dirpath[64];
	sprintf(dirpath, "%s/.Dimdim", getenv("HOME"));	
	mkdir(dirpath, S_IRUSR | S_IWUSR | S_IXUSR);
	sprintf(filepath, "%s/%s", dirpath, filename);
}

+ (void) logWrite : (FILE*) fd nsData : (NSString*) nsData
{
	if (!fd)
		return;
	fwrite([nsData UTF8String], sizeof(char), [nsData length], fd);
}

+ (void) logWrite : (FILE*) fd buf : (const char*) buf len : (const int) len
{
	fwrite(buf, sizeof(char), len, fd);
}

@end

size_t CurlCallback(void* ptr, size_t size, size_t nmemb, void* data)
{
	size_t realsize = size * nmemb;
	[(dByteBuffer*) data append:ptr bufLen:realsize];
    return realsize;
}