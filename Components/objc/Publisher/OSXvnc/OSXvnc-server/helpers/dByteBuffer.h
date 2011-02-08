//
//  dByteBuffer.h
//  helpers
//
//  Created by Bharat Nadimpalli on 05/05/08.
//  Copyright 2008 Dimdim Inc. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface dByteBuffer : NSObject {

	char* data;
	size_t dataLen;
	bool doNotDelete;

}

- (id) initWithLen : (size_t) len;
- (id) initWithBuffer : (const void*) buffer buflen : (size_t) buflen createCopy : (bool) createCopy;
- (dByteBuffer*) clone;
- (dByteBuffer*) cloneAndRelease;
- (void) reallocate : (size_t) newLen keepContents : (bool) keepContents;
- (void) zeroMemory;
- (void) destroy;
- (bool) isNull;
- (char*) getData : (size_t) offset;
- (size_t) getRemainingLength : (size_t) offset;
- (size_t) getLength;
- (void) append : (const void*) buffer bufLen : (size_t) bufLen;
- (void) append : (dByteBuffer*) buffer;
- (void) copyTo : (void*) buffer bufLen : (size_t) bufLen dataOffset : (size_t) dataOffset;
- (void) copyFrom : (const void*) buffer bufLen : (size_t) bufLen dataOffset : (size_t) dataOffset;
- (char) getByte : (size_t) index;
- (void) setByte : (size_t) index val : (char) val;

@end
