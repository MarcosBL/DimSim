//
//  xproperty.m
//  wkDimdimControl
//
//  Created by Bharat Nadimpalli on 22/05/08.
//  Copyright 2008 Dimdim, Inc. All rights reserved.
//

#import "xproperty.h"

const double DIMDIM_VERSION_MAJOR	= 4UL;
const double DIMDIM_VERSION_MINOR	= 0UL;
const double DIMDIM_VERSION_MONTH	= 9UL;
const double DIMDIM_VERSION_DATE	= 8UL;
const double DIMDIM_VERSION_YEAR	= 8UL;

const char Screencaster[] = "OSXvnc-server";

@implementation xproperty

- (id) init
{
	if (![super init]) 
	{
		return nil;
	}
	
	m_registrationID	= [[NSString alloc] init];
	m_screencastURL		= [[NSString alloc] init];
	m_screencastResult	= [[NSString alloc] initWithString:@"{screencastResult:\"0\"}"];
	propertyLock		= [[NSLock alloc] init];
	m_bServerStatus		= false;
	
	return self;
}

- (NSString*) getScreencastResult : (bool) bExecute
{
	return self->m_screencastResult;
}

- (void) setScreencastResult : (NSString*) result
{
	[self->propertyLock lock];
	self->m_screencastResult = result;
	[self->propertyLock unlock];
}

- (void) resetScreencastResult
{
	[self->propertyLock lock];
	self->m_screencastResult = @"{screencastResult:\"0\"}";
	[self->propertyLock unlock];
}

- (NSString*) getScreencastURL
{
	return m_screencastURL;
}

- (void) setScreencastURL : (NSString*) url
{
	[self->propertyLock lock];
	self->m_screencastURL = url;
	[self->propertyLock unlock];
}

- (double) getVersion
{
	return DIMDIM_VERSION_YEAR + (DIMDIM_VERSION_DATE * 100) + (DIMDIM_VERSION_MONTH * 10000) +
	(DIMDIM_VERSION_MINOR * 1000000) + (DIMDIM_VERSION_MAJOR * 10000000);
}

- (void) setRegistration : (NSString*) reg
{
	self->m_registrationID = reg;
}

- (NSString*) retrieveRegistration
{
	return self->m_registrationID;
}

@end