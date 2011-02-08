//
//  dQueue.h
//  helpers
//
//  Created by Bharat Nadimpalli on 25/04/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import <Foundation/NSObject.h>
#import <Foundation/NSArray.h>
#import <Foundation/NSLock.h>

@interface dQueue : NSObject
{
	NSMutableArray* m_queue;
	NSLock* m_lock;
}

- (void) enqueueItem:(id)item;
- (id) dequeueItem;
- (unsigned int) length;
- (void) clear;

@end
