//
//  dVNCQueue.m
//  OSXvnc
//
//  Created by Bharat Nadimpalli on 09/05/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import "dVNCQueue.h"
#import "dByteBuffer.h"
#import "dQueue.h"

@implementation dVNCQueue

- (id) init
{
	if (![super init]) 
	{
		return nil;
	}
	self->m_queue = [[dQueue alloc] init];
	self->m_lock = [[NSLock alloc] init];
	[self clear];
	return self;
}

- (void) dealloc
{
	[self clear];
	[self->m_queue release];
	[self->m_lock release];
	[super dealloc];
}

- (void) clear
{
	[self->m_lock lock];
	[self->m_queue clear];
	[self->m_lock unlock];
}

- (void) enqueue : (dByteBuffer*) buf
{
	[self->m_lock lock];
	
	[self->m_queue enqueueItem:buf];
	[self->m_lock unlock];
}

- (bool) empty
{
	return ([self->m_queue length] == 0) ? true : false;
}

- (unsigned int) length
{
	return [self->m_queue length];
}

- (dByteBuffer*) dequeue : (size_t) blockSize
{
	dByteBuffer* data = [[dByteBuffer alloc] initWithLen:0];
	
	[self->m_lock lock];
	while (([data getLength] < blockSize) && ([self->m_queue length] > 0))
	{
		dByteBuffer* deq = [self->m_queue dequeueItem];
		[data append:deq];
		[deq release];
	}
	
	[self->m_lock unlock];
	
	return data;
}

@end
