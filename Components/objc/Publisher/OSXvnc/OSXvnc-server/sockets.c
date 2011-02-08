/*
 * sockets.c - deal with TCP & UDP sockets.
 *
 * This code should be independent of any changes in the RFB protocol.  It just
 * deals with the X server scheduling stuff, calling rfbNewClientConnection and
 * rfbProcessClientMessage to actually deal with the protocol.  If a socket
 * needs to be closed for any reason then rfbCloseClient should be called, and
 * this in turn will call rfbClientConnectionGone.  To make an active
 * connection out, call rfbConnect - note that this does _not_ call
 * rfbNewClientConnection.
 *
 * This file is divided into two types of function.  Those beginning with
 * "rfb" are specific to sockets using the RFB protocol.  Those without the
 * "rfb" prefix are more general socket routines (which are used by the http
 * code).
 *
 * Thanks to Karl Hakimian for pointing out that some platforms return EAGAIN
 * not EWOULDBLOCK.
 */

/*
 *  OSXvnc Copyright (C) 2001 Dan McGuirk <mcguirk@incompleteness.net>.
 *  Original Xvnc code Copyright (C) 1999 AT&T Laboratories Cambridge.  
 *  All Rights Reserved.
 *
 *  This is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 *  USA.
 */


/* Dimdim specific includes -- Bharat Varma */

#import <Foundation/NSStream.h>
#import "dVNCQueue.h"
#import "dByteBuffer.h"
#import "dToolkit.h"

/* Dimdim specific includes -- Bharat Varma */

#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/socket.h>
#include <netinet/in.h>
//#include <netinet/tcp.h> -- This conflicts with Carbon
#include <netdb.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <pthread.h>

#include "rfb.h"

int rfbMaxClientWait = 20000;   /* time (ms) after which we decide client has
                                   gone away - needed to stop us hanging */


/*
 * ReadExact reads an exact number of bytes from a client.  Returns 1 if
 * those bytes have been read, 0 if the other end has closed, or -1 if an error
 * occurred (errno is set to ETIMEDOUT if it timed out).
 */

int
ReadExact(cl, buf, len)
     rfbClientPtr cl;
     char *buf;
     int len;
{
    int sock = cl->sock;
    int n;
    fd_set fds;
    struct timeval tv;

    while (len > 0) {
        n = read(sock, buf, len);

        if (n > 0) {

            buf += n;
            len -= n;

        } else if (n == 0) {

            return 0;

        } else {
            if (errno != EWOULDBLOCK && errno != EAGAIN) {
                return n;
            }

            FD_ZERO(&fds);
            FD_SET(sock, &fds);
            tv.tv_sec = rfbMaxClientWait / 1000;
            tv.tv_usec = (rfbMaxClientWait % 1000) * 1000;
            n = select(sock+1, &fds, NULL, NULL, &tv);
            if (n < 0) {
                rfbLogPerror("ReadExact: select");
                return n;
            }
            if (n == 0) {
                errno = ETIMEDOUT;
                return -1;
            }
        }
    }
    return 1;
}



/*
 * WriteExact writes an exact number of bytes to a client.  Returns 1 if
 * those bytes have been written, or -1 if an error occurred (errno is set to
 * ETIMEDOUT if it timed out).
 */

int
WriteExact(cl, buf, len)
     rfbClientPtr cl;
     char *buf;
     int len;
{
    int sock = cl->sock;
    int n;
    fd_set fds;
    struct timeval tv;
    int totalTimeWaited = 0;


	//    pthread_mutex_lock(&cl->outputMutex);
    while (len > 0) {
        n = write(sock, buf, len);

        if (n > 0) {

            buf += n;
            len -= n;

        } else if (n == 0) {

            rfbLog("WriteExact: write returned 0?\n");
            exit(1);

        } else {
            if (errno != EWOULDBLOCK && errno != EAGAIN) {
                //pthread_mutex_unlock(&cl->outputMutex);
                return n;
            }

            /* Retry every 5 seconds until we exceed rfbMaxClientWait.  We
               need to do this because select doesn't necessarily return
               immediately when the other end has gone away */

            FD_ZERO(&fds);
            FD_SET(sock, &fds);
            tv.tv_sec = 5;
            tv.tv_usec = 0;
            n = select(sock+1, NULL, &fds, NULL, &tv);
            if (n < 0) {
                rfbLogPerror("WriteExact: select");
                //pthread_mutex_unlock(&cl->outputMutex);
                return n;
            }
            if (n == 0) {
                totalTimeWaited += 5000;
                if (totalTimeWaited >= rfbMaxClientWait) {
                    errno = ETIMEDOUT;
                    //pthread_mutex_unlock(&cl->outputMutex);
                    return -1;
                }
            } else {
                totalTimeWaited = 0;
            }
        }
    }
    //pthread_mutex_unlock(&cl->outputMutex);
    return 1;
}

/* Dimdim specific methods -- Bharat Varma */


int hexdump(FILE *fp, void *p, int len, int base)
{
    static char line[] =
	"00000000  xx xx xx xx  xx xx xx xx  "
	"xx xx xx xx  xx xx xx xx  yyyyyyyy yyyyyyyy\n";
    static char hex[] = "0123456789abcdef";
    char *s;
    int thistime;
    char *l;
    int col;
	
    /*
	 0         1         2         3         4         5         6         7
	 0123456789012345678901234567890123456789012345678901234567890123456789012345678
	 xxxxxxxx  xx xx xx xx  xx xx xx xx  xx xx xx xx  xx xx xx xx  ........ ........
     */
	
    base -= (int)p;
    for (s = p; len; len -= thistime) {
        sprintf(line, "%08x", (unsigned int)(s + base));
        line[8] = ' ';
        thistime = len > 16 ? 16 : len;
		
        l = line + 10;
        for (col = 0; col < thistime; col++) {
            *l++ = hex[(*s & 0xf0) >> 4];
            *l++ = hex[*s++ & 0x0f];
            l += ((col & 3) == 3) + 1;
        }
        while (col < 16) {
            *l = l[1] = ' ';
            l += ((col++ & 3) == 3) + 3;
        }
		
        s -= thistime;
        for (col = 0; col < thistime; col++) {
            *l++ = isprint(*s) ? *s : '.';
            l += col == 7;
            s++;
        }
        while (col < 16) {
            *l++ = ' ';
            l += col++ == 7;
        }
		
        fputs(line, fp);
    }
	
    return 0;
}


void rfbCloseClient(rfbClientPtr cl)
{
	// Disconnect from reflector
	
	dByteBuffer* buf = [[dByteBuffer alloc] initWithBuffer:"" buflen:0 createCopy:true];
	
	sendExact(cl, buf, @"close");
	
	[buf release];
	
	// Clean up the curl handle
	curl_easy_cleanup(cl->curlhandle);
	
	// Clean up the rfbQueue
	
	[rfbQueue clear];
	[rfbQueue release];
	rfbQueue = nil;
	cl->sock = -1;
}

int sendExact(rfbClientPtr cl, dByteBuffer* buf, NSString* cmd)
{
	// The actual curl send call
	
	if (nil == rfbQueue)	// Queue is either not ready or client got disconnected.
		return 1;
	
	if ([[dimdimProfile valueForKey:@"OperationType"] intValue] == 1 ||
		[[dimdimProfile valueForKey:@"OperationType"] intValue] == 3)
	{
		// If necessary, do a binary dump
		[DimdimHelpers logWrite:binaryDump buf:[buf getData:0] len:[buf getLength]];
	}
	
	if ([[dimdimProfile valueForKey:@"OperationType"] intValue] < 2)
	{
		// Network sends disabled. returning.
		return 1;
	}
	
	NSString* url = [DimdimHelpers PrepareURL:cmd handlerID:[CurlClientData getInstance]->m_subscriptionID role:@"pub"];
	
	if ([cmd caseInsensitiveCompare:@"open"] == NSOrderedSame)
	{
		rfbLog("%@\n",url);
	}
	
	dByteBuffer* chunk = [[dByteBuffer alloc] initWithLen:0];
	dByteBuffer* headers = [[dByteBuffer alloc] initWithLen:0];
	
    curl_easy_setopt(cl->curlhandle, CURLOPT_URL, [url UTF8String]);
    curl_easy_setopt(cl->curlhandle, CURLOPT_POSTFIELDSIZE, [buf getLength]);
    curl_easy_setopt(cl->curlhandle, CURLOPT_POSTFIELDS, [buf getData:0]);
	
    curl_easy_setopt(cl->curlhandle, CURLOPT_WRITEFUNCTION, CurlCallback);
    curl_easy_setopt(cl->curlhandle, CURLOPT_WRITEDATA, chunk);
	
    curl_easy_setopt(cl->curlhandle, CURLOPT_HEADERFUNCTION, CurlCallback);
    curl_easy_setopt(cl->curlhandle, CURLOPT_WRITEHEADER, headers);
		
	struct curl_slist* headerList = NULL;
    headerList = curl_slist_append(headerList, "Expect:");
    headerList = curl_slist_append(headerList, "Content-Type: application/x-dimdim-dtp");
    headerList = curl_slist_append(headerList, "Connection: Keep-Alive");
    curl_easy_setopt(cl->curlhandle, CURLOPT_HTTPHEADER, headerList);
	
	curl_easy_setopt(cl->curlhandle, CURLOPT_TIMEOUT, [CurlClientData getInstance]->m_maxWaitTime * 2);
	curl_easy_setopt(cl->curlhandle, CURLOPT_CONNECTTIMEOUT, [CurlClientData getInstance]->m_maxWaitTime);

	
	int i = 0;
	BOOL bSuccessful = NO;
	while (i < [CurlClientData getInstance]->m_maxRetries)
	{
		CURLcode res;
		
		res = curl_easy_perform(cl->curlhandle);
		
		if (CURLE_OK != res || 1 != [DimdimHelpers ValidResponseHeaders:headers cmd:cmd])
		{
			rfbLog("\nReceived invalid response!!\n");
			
			NSString* headerData = [[NSString alloc] initWithCString:[headers getData:0] length:[headers getLength]];
			NSString* chunkData = [[NSString alloc] initWithCString:[chunk getData:0] length:[chunk getLength]];
			
			rfbLog("Headers  -----> \n");					
			rfbLog("%@\n",headerData);
			
			rfbLog("Chunk -----> \n");
			rfbLog("%@\n",chunkData);
			
			[headerData release];
			[chunkData release];
			
			
			char dirpath[64];
			sprintf(dirpath, "%s/.Dimdim", getenv("HOME"));
			char filepath[64];
			sprintf(filepath, "%s/error.log", dirpath);	
			mkdir(dirpath, S_IRUSR | S_IWUSR | S_IXUSR);
			
			FILE* fp = fopen(filepath, "w");
			hexdump(fp, [buf getData:0], [buf getLength], 0);
			fclose(fp);
		}
		else
		{
			bSuccessful = YES;
			break;
		}
		
		++i;
	}
	
	if (bSuccessful)
	{
		if ([cmd caseInsensitiveCompare:@"open"] == NSOrderedSame)
		{
			// We need to retrieve client ID from here
			[CurlClientData getInstance]->m_subscriptionID = [[NSString alloc] initWithCString:[chunk getData:0] length:[chunk getLength]];
			rfbLog("Registered with ClientID:%@\n", [CurlClientData getInstance]->m_subscriptionID);
		}
		
		if ([cmd caseInsensitiveCompare:@"close"] == NSOrderedSame)
		{
			rfbLog("ClientID:%@ disconnected.\n", [CurlClientData getInstance]->m_subscriptionID);
		}
	}
	
	curl_slist_free_all(headerList);
    [headers release];
    [chunk release];
    return (bSuccessful == YES) ? 1 : 0;	
	
}

void sendQueued(char* buf, int len)
{
	// Write buffers into queue. This will be flushed later at end of capture cycle
	
	if (0 == len)
		return;			// No point in enqueing 0 byte buffers - This usually happens when there is no change in the screen between captures.
	
	dByteBuffer* inBuf = [[dByteBuffer alloc] initWithBuffer:buf buflen:len createCopy:true];
	[rfbQueue enqueue:inBuf];
}

int flushQueue(rfbClientPtr cl, size_t blockSize)
{
	int retval = 1;
	
	while ([rfbQueue length] > 0)
	{
		dByteBuffer* outBuf = [rfbQueue dequeue:blockSize];
		retval = sendExact(cl, outBuf, @"send");
		[outBuf release];
		if (retval != 1)
			return 0;
	}
	
	[rfbQueue clear];
	
	return retval;
}

/* Dimdim specific methods -- Bharat Varma */
