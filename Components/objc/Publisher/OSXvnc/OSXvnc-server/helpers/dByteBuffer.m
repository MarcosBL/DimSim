//
//  dByteBuffer.m
//  helpers
//
//  Created by Bharat Nadimpalli on 05/05/08.
//  Copyright 2008 Dimdim Inc. All rights reserved.
//

#import "dToolkit.h"
#import "dByteBuffer.h"


@implementation dByteBuffer


- (id) initWithLen : (size_t) len
{
	if (![super init])
	{
		return nil;
	}
	
	self->data = 0;
	self->dataLen = 0;
	self->doNotDelete = false;
	[self reallocate:0 keepContents:false];
	return self;
}

- (id) initWithBuffer : (const void*) buffer buflen : (size_t) buflen createCopy : (bool) createCopy
{
	if (![super init])
	{
		return nil;
	}
	
	self->data = 0;
	self->dataLen = 0;
	self->doNotDelete = false;
	
	if (createCopy)
	{
		[self reallocate:buflen keepContents:false];
		[self copyFrom:buffer bufLen:buflen dataOffset:0];
	}
	else
	{
		self->doNotDelete = true;
		self->data = (char*) buffer;
		self->dataLen = buflen;
	}
	
	return self;
}

- (void) dealloc
{
	[self destroy];
	[super dealloc];
}

- (void) reallocate : (size_t) newLen keepContents : (bool) keepContents
{
	assert(!doNotDelete && "Buffer needs to be owned for reallocation");
	if (newLen == self->dataLen)
	{
		if(!keepContents)
		{
			[self zeroMemory];
		}
		return;
	}
	
	bool keep = keepContents && (newLen > self->dataLen) && !([self isNull]);
	char* tmp = 0;
	size_t tmpLen= 0;
	
	dScopedCharArr* tmpPtr = [[dScopedCharArr alloc] initWithCharArr:tmp];
	
	if (keep)
	{
		tmp = (char*) malloc(sizeof(char) * self->dataLen);
		tmpLen = self->dataLen;
		[self copyTo:tmp bufLen:self->dataLen dataOffset:0];
	}
	[self destroy];
	self->data = (char*) malloc(sizeof(char) * newLen);
	self->dataLen = newLen;
	[self zeroMemory];
	
	if (keep)
	{
		[self copyFrom:tmp bufLen:tmpLen dataOffset:0];
		free(tmp);
	}
	[tmpPtr release];
}

- (void) zeroMemory
{
	if (self->data && self->dataLen > 0)
	{
		memset(self->data, 0, self->dataLen);
	}
}

- (void) destroy
{
	if (self->data)
	{
		if (!self->doNotDelete)
		{
			SAFE_DELETE_ARRAY(self->data);
		}
	}
	
	self->data = 0;
	self->dataLen = 0;
}

- (bool) isNull
{
	return (self->data == 0);
}

- (const char*) getData : (size_t) offset
{
	return [self isNull]?0:(self->data + offset);
}

- (size_t) getRemainingLength : (size_t) offset
{
	if (![self isNull])
	{
		if (offset < self->dataLen)
		{
			return (self->dataLen - offset);
		}
	}
	return 0;
}

- (void) append : (const void*) buffer bufLen : (size_t) bufLen
{
	@try
	{
		if (buffer && bufLen > 0)
		{
			size_t oldLen = self->dataLen;
			size_t newLen = oldLen + bufLen;
			
			[self reallocate:newLen keepContents:true];
			[self copyFrom:buffer bufLen:bufLen dataOffset:oldLen];
		}
	}
	@catch(NSException* e)
	{
		// Do nothing
	}
}

- (void) append : (dByteBuffer*) buffer
{
	if (buffer)
	{
		[self append:[buffer getData:0] bufLen:[buffer getLength]];
	}
}

- (void) copyTo : (void*) buffer bufLen : (size_t) bufLen dataOffset : (size_t) dataOffset
{
	size_t len = [self getRemainingLength:dataOffset];
	if (len >= bufLen)
	{
		memcpy(buffer, [self getData:dataOffset], bufLen);
	}
	else
	{
		memcpy(buffer, [self getData:dataOffset], len);
	}
}

- (dByteBuffer*) clone
{
	if (![self isNull])
	{
		return [self initWithBuffer:self->data buflen:self->dataLen createCopy:true];
	}
	else
	{
		return [self initWithLen:0];
	}
}

- (void) copyFrom : (const void*) buffer bufLen : (size_t) bufLen dataOffset : (size_t) dataOffset
{
	size_t len = [self getRemainingLength:dataOffset];
	if (len > bufLen)
	{
		memcpy([self getData:dataOffset], buffer, bufLen);
	}
	else
	{
		memcpy([self getData:dataOffset], buffer, len);
	}
}

- (dByteBuffer*) cloneAndRelease
{
	dByteBuffer* cb = [self clone];
	[self destroy];
	return cb;
}
				
- (size_t) getLength
{
	return [self getRemainingLength:0];
}

- (char) getByte : (size_t) index
{
	return self->data[index];
}

- (void) setByte : (size_t) index val : (char) val
{
	self->data[index] = val;
}
 
@end
