//
//  dQueue.m
//  helpers
//
//  Created by Bharat Nadimpalli on 25/04/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "dQueue.h"


@implementation dQueue

- (id) init
{
	if (self == [super init])
	{
		self->m_queue = [[NSMutableArray alloc] init];
		self->m_lock = [[NSLock alloc] init];
	}
	
	return self;
}

- (void) dealloc
{
	[self->m_queue release];
	[self->m_lock release];
	[super dealloc];
}



- (void) enqueueItem : (id) item
{
	[self->m_lock lock];
	[self->m_queue addObject:item];
	[self->m_lock unlock];
}

- (id) dequeueItem
{
	[self->m_lock lock];
	id item = nil;
	if ([self->m_queue count])
	{
		item = [self->m_queue objectAtIndex:0];
		[item retain];
		[self->m_queue removeObjectAtIndex:0];
	}
	
	[self->m_lock unlock];
	
	return item;
}

- (unsigned int) length
{
	return [self->m_queue count];
}

- (void) clear
{
	[self->m_lock lock];
	while ([self->m_queue count])
	{
		[self->m_queue removeObjectAtIndex:0];
	}
	[self->m_lock unlock];
}

@end
