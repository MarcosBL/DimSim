//
//  dVNCQueue.h
//  OSXvnc
//
//  Created by Bharat Nadimpalli on 09/05/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@class dQueue;
@class dByteBuffer;

@interface dVNCQueue : NSObject {

	dQueue* m_queue;
	NSLock* m_lock;
}

- (void) clear;
- (void) enqueue : (dByteBuffer*) buf;
- (bool) empty;
- (dByteBuffer*) dequeue : (size_t) blockSize;
- (unsigned int) length;

@end
