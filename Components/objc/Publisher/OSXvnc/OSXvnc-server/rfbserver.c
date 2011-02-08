/*
 * rfbserver.c - deal with server-side of the RFB protocol.
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

#include <Cocoa/Cocoa.h>
#include <Carbon/Carbon.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <pwd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <pthread.h>

#include <netdb.h>

#include "rfb.h"


/* Changes in handshake for Dimdim and general workings -- Bharat Varma 
 *
 * 1. Dimdim handshake is pretty simple. We are in a sandbox sort of a situation
 *		and we will take reasonable and supported safe defaults.
 * 2. The complete handshake will be translated to 1 single call to the reflector.
 *		This call will send the information available in ClientInitMessage
 * 3. We do not have any 'read's before sending out framebuffer updates.
 *	In fact, we *only* send framebuffer updates and nothing else.
 * 4. There is not even a single "read" done apart form looking at the response for 
 *	the initial client init request to reflector.
 */

/* Additional includes for Dimdim -- Bharat Varma */

#import <curl/curl.h>
#import "dToolkit.h"
#import "dByteBuffer.h"
#import "DimdimMessageClient.h"

/* Additional includes for Dimdim -- Bharat Varma */

//char updateBuf[UPDATE_BUF_SIZE];
//int ublen;

rfbClientPtr pointerClient = NULL;  /* Mutex for pointer events with buttons down*/

rfbClientPtr rfbClientHead;  /* tight encoding -- GetClient() in tight.c accesses this list, so make it global */

struct rfbClientIterator {
    rfbClientPtr next;
};

static pthread_mutex_t rfbClientListMutex;
static struct rfbClientIterator rfbClientIteratorInstance;

void
rfbClientListInit(void)
{
    rfbClientHead = NULL;
    pthread_mutex_init(&rfbClientListMutex, NULL);
}

rfbClientIteratorPtr
rfbGetClientIterator(void)
{
    pthread_mutex_lock(&rfbClientListMutex);
    rfbClientIteratorInstance.next = rfbClientHead;

    return &rfbClientIteratorInstance;
}

rfbClientPtr
rfbClientIteratorNext(rfbClientIteratorPtr iterator)
{
    rfbClientPtr result = iterator->next;
    if (result)
        iterator->next = result->next;
    return result;
}

void
rfbReleaseClientIterator(rfbClientIteratorPtr iterator)
{
    pthread_mutex_unlock(&rfbClientListMutex);
}

Bool rfbClientsConnected()
{
    return (rfbClientHead != NULL);
}

void rfbSendClientList() {
    pthread_mutex_lock(&rfbClientListMutex);

	NSAutoreleasePool *pool=[[NSAutoreleasePool alloc] init];
	NSMutableArray *clientList = [[NSMutableArray alloc] init];
	rfbClientPtr myClient = rfbClientHead;
	
	while (myClient != NULL) {
		[clientList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
			[NSString stringWithCString: myClient->host], @"clientIP",
			nil]];
		myClient = myClient->next;
	}
	
	[[NSDistributedNotificationCenter defaultCenter] postNotificationName:@"VNCConnections" 
																   object:[NSString stringWithFormat:@"OSXvnc%d",rfbPort] 
																 userInfo:[NSDictionary dictionaryWithObject:clientList forKey:@"clientList"]];
	
	[pool release];

    pthread_mutex_unlock(&rfbClientListMutex);
}

/*
 * rfbNewClientConnection is called from sockets.c when a new connection
 * comes in.
 */

void rfbNewClientConnection(int sock) {
//    rfbNewClient(sock);		-- We should never need this function. Bharat Varma.
}


/*
 * rfbReverseConnection is called to make an outward
 * connection to a "listening" RFB client.
 */
rfbClientPtr rfbReverseConnection(char *host) 
{
	// URL is of type - http://ip:port/screenshare0/confKey/streamID/~dimdim_id/room_id/session_id
    rfbClientPtr cl;
	cl = rfbNewClient();
	cl->host = host;
	if (cl) {
		cl->reverseConnection = TRUE;
	}
	
	// Set Curl Client Data
	
	NSArray* arr = [[NSString stringWithCString:host] componentsSeparatedByString:@"~"];
	[CurlClientData getInstance]->m_peerAddress = [arr objectAtIndex:0];
	if([arr count] > 1)
	{
		// try to set meta data only if there is some meta data available
		[CurlClientData getInstance]->m_metaData = [arr objectAtIndex:1];
	}
	rfbLog("Detected peerAddress = %@\n", [CurlClientData getInstance]->m_peerAddress);
    return cl;
}

/*
 * rfbNewClient is called when a new connection has been made by whatever
 * means.
 */

rfbClientPtr rfbNewClient() {
    rfbProtocolVersionMsg pv;
    rfbClientPtr cl;
    BoxRec box;
    int i;
	unsigned int addrlen;
	int bitsPerSample;

    cl = (rfbClientPtr)xalloc(sizeof(rfbClientRec));

    cl->sock = 12345;		// Some placeholder till we get rid of 'sock' completely -- Bharat Varma
	
    pthread_mutex_init(&cl->outputMutex, NULL);
	
	/* Dimdim specific data gets initialized here -- Bharat Varma */
	
	cl->m_bIncremental = NO;
	cl->curlhandle = curl_easy_init();
	
	/* Dimdim specific data gets initialized here -- Bharat Varma */

    cl->state = RFB_PROTOCOL_VERSION;

    /* REDSTONE - Adding some features
        In theory these need not be global, but could be set per client
        */
    cl->disableRemoteEvents = rfbDisableRemote;      // Ignore PB, Keyboard and Mouse events
    cl->swapMouseButtons23 = rfbSwapButtons;         // How to interpret mouse buttons 2 & 3

    cl->needNewScreenSize = NO;
	initPasteboardForClient(cl);

    cl->reverseConnection = FALSE;
    cl->preferredEncoding = rfbEncodingRaw;
    cl->correMaxWidth = 48;
    cl->correMaxHeight = 48;
    cl->zrleData = 0;
    cl->mosData = 0;

    box.x1 = box.y1 = 0;
    box.x2 = rfbScreen.width;
    box.y2 = rfbScreen.height;
    REGION_INIT(pScreen,&cl->modifiedRegion,&box,0);

    pthread_mutex_init(&cl->updateMutex, NULL);
    pthread_cond_init(&cl->updateCond, NULL);

    REGION_INIT(pScreen,&cl->requestedRegion,NullBox,0);

	switch (rfbMaxBitDepth) {
		case 32:
		case 16:
		case 8:		
			cl->format.bitsPerPixel = max(rfbMaxBitDepth, rfbScreen.bitsPerPixel);
			bitsPerSample = cl->format.bitsPerPixel << 2;
			cl->format.depth = bitsPerSample*3;
			cl->format.bigEndian = !littleEndian;
			cl->format.trueColour = TRUE;
			
			cl->format.redMax = (1 << bitsPerSample) - 1;
			cl->format.greenMax = (1 << bitsPerSample) - 1;
			cl->format.blueMax = (1 << bitsPerSample) - 1;
			
			cl->format.redShift = bitsPerSample * 2;
			cl->format.greenShift = bitsPerSample * 1;
			cl->format.blueShift = bitsPerSample * 0;
			break;
		case 0:
		default:
			cl->format = rfbServerFormat;
			break;
	}
	// This will 
	rfbSetTranslateFunctionUsingFormat(cl, rfbServerFormat);

    /* SERVER SCALING EXTENSIONS -- Server Scaling is off by default */
    cl->scalingFactor = 1;
    cl->scalingFrameBuffer = rfbGetFramebuffer();
	cl->scalingPaddedWidthInBytes = rfbScreen.paddedWidthInBytes;

    cl->tightCompressLevel = TIGHT_DEFAULT_COMPRESSION;
    cl->tightQualityLevel = -1;
    for (i = 0; i < 4; i++)
        cl->zsActive[i] = FALSE;

    cl->enableLastRectEncoding = FALSE;
    cl->enableXCursorShapeUpdates = FALSE;
    cl->useRichCursorEncoding = FALSE;
    cl->enableCursorPosUpdates = FALSE;
    cl->desktopSizeUpdate = FALSE;
    cl->immediateUpdate = FALSE;
    
    pthread_mutex_lock(&rfbClientListMutex);
    cl->next = rfbClientHead;
    cl->prev = NULL;
    if (rfbClientHead)
        rfbClientHead->prev = cl;

    rfbClientHead = cl;
    pthread_mutex_unlock(&rfbClientListMutex);

    rfbResetStats(cl);

    cl->compStreamInited = FALSE;
    cl->compStream.total_in = 0;
    cl->compStream.total_out = 0;
    cl->compStream.zalloc = Z_NULL;
    cl->compStream.zfree = Z_NULL;
    cl->compStream.opaque = Z_NULL;

    cl->zlibCompressLevel = 5;

    cl->compStreamRaw.total_in = ZLIBHEX_COMP_UNINITED;
    cl->compStreamHex.total_in = ZLIBHEX_COMP_UNINITED;

    cl->client_zlibBeforeBufSize = 0;
    cl->client_zlibBeforeBuf = NULL;

    cl->client_zlibAfterBufSize = 0;
    cl->client_zlibAfterBuf = NULL;
    cl->client_zlibAfterBufLen = 0;
    
    sprintf(pv,rfbProtocolVersionFormat,rfbProtocolMajorVersion, rfbProtocolMinorVersion);


    return cl;
}


/*
 * rfbClientConnectionGone is called from sockets.c just after a connection
 * has gone away.
 */

void rfbClientConnectionGone(rfbClientPtr cl) 
{
    int i;

    // RedstoneOSX - Track and release depressed modifier keys whenever the client disconnects
    keyboardReleaseKeysForClient(cl);

	freePasteboardForClient(cl);
	
    pthread_mutex_lock(&rfbClientListMutex);

    /* Release the compression state structures if any. */
    if ( cl->compStreamInited == TRUE ) {
        deflateEnd( &(cl->compStream) );
    }

    for (i = 0; i < 4; i++) {
        if (cl->zsActive[i])
            deflateEnd(&cl->zsStruct[i]);
    }

    if (pointerClient == cl)
        pointerClient = NULL;

    if (cl->prev)
        cl->prev->next = cl->next;
    else
        rfbClientHead = cl->next;
    if (cl->next)
        cl->next->prev = cl->prev;

    pthread_mutex_unlock(&rfbClientListMutex);

    REGION_UNINIT(pScreen,&cl->modifiedRegion);
	
	if (cl->major && cl->minor) {
		// If it didn't get so far as to send a protocol then let's just ignore
		// For Clients with no activity just return with no log
		rfbSendClientList();
	}

    FreeZrleData(cl);
	
//    free(cl->host);	// No need to do this anymore. -- Bharat Varma

    if (cl->translateLookupTable)
        free(cl->translateLookupTable);

    /* SERVER SCALING EXTENSIONS */
    if( cl->scalingFrameBuffer && cl->scalingFrameBuffer != rfbGetFramebuffer() ){
        free(cl->scalingFrameBuffer);
    }

    pthread_cond_destroy(&cl->updateCond);
    pthread_mutex_destroy(&cl->updateMutex);
    pthread_mutex_destroy(&cl->outputMutex);

    xfree(cl);
    // Not sure why but this log message seems to prevent a crash
    // rfbLog("Client gone\n");
}


/*
 * rfbProcessClientMessage is called when there is data to read from a client.
 */

void rfbProcessClientMessage(rfbClientPtr cl) {
    switch (cl->state) {
        case RFB_PROTOCOL_VERSION:
            rfbProcessClientProtocolVersion(cl);
            return;
        case RFB_AUTH_VERSION:
            rfbProcessAuthVersion(cl);
            return;
        case RFB_AUTHENTICATION:
            rfbAuthProcessClientMessage(cl);
            return;
        case RFB_INITIALISATION:
            rfbProcessClientInitMessage(cl);
            return;
        default:
            rfbProcessClientNormalMessage(cl);
            return;
    }
}


/*
 * rfbProcessClientProtocolVersion is called when the client sends its
 * protocol version.
 */

void rfbProcessClientProtocolVersion(rfbClientPtr cl) {
	
	// We have already forced 3.3 in main(). 
	// But we are going to set the version to the client
	// just the same.		-- Bharat Varma
	
	cl->major = 3;
	cl->minor = 3;
	rfbSendClientList();
	
	// No need for further authentication	-- Bharat Varma
	cl->state = RFB_INITIALISATION;
}


/*
 * rfbClientConnFailed is called when a client connection has failed either
 * because it talks the wrong protocol or it has failed authentication.
 */

void rfbClientConnFailed(rfbClientPtr cl, char *reason) {
    char *buf;
    int len = strlen(reason);

    buf = (char *)xalloc(8 + len);
    ((CARD32 *)buf)[0] = Swap32IfLE(rfbConnFailed);
    ((CARD32 *)buf)[1] = Swap32IfLE(len);
    memcpy(buf + 8, reason, len);

    if (WriteExact(cl, buf, 8 + len) < 0)
        rfbLogPerror("rfbClientConnFailed: write");
    xfree(buf);
    rfbCloseClient(cl);
}


/*
 * rfbProcessClientInitMessage is called when the client sends its
 * initialisation message.
 */

void rfbProcessClientInitMessage(rfbClientPtr cl) 
{
	char buf[256];
    rfbServerInitMsg *si = (rfbServerInitMsg *)buf;
	
    // We are going to take care of client format here itself
    // instead of having to go through the regular rfb 
    // handshake.
	
    // The pix format needs to be decided based on a 
    // parameter or a config file -- TBD -- Bharat Varma
	
	int len = 0;
	
    // Set client pixel format here itself. -- Bharat Varma
	
	si->format = rfbServerFormat;
	si->format.bigEndian = !littleEndian;
	
	if (0 == [[dimdimProfile valueForKey:@"EnableRestrictedColors"] intValue])
	{
		// 32 - bit sharing : High or Medium Profile
		si->format.trueColour = 1;
		si->format.bitsPerPixel = 32;
		si->format.depth = 24;
		si->format.redMax = 255;
		si->format.greenMax = 255;
		si->format.blueMax = 255;
		si->format.redShift = 16;
		si->format.greenShift = 8;
		si->format.blueShift = 0;		
	}
	else
	{
		// 8 - bit sharing : Low Profile
		
		si->format = rfbServerFormat;
		si->format.bigEndian = 0;
		si->format.trueColour = 1;
		si->format.bitsPerPixel = 8;
		si->format.depth = 8;
		si->format.redMax = 7;
		si->format.greenMax = 7;
		si->format.blueMax = 3;
		si->format.redShift = 0;
		si->format.greenShift = 3;
		si->format.blueShift = 6;
	}
    
    cl->format = si->format;
    rfbSetTranslateFunction(cl);
	
    si->framebufferWidth = Swap16IfLE(rfbScreen.width);
    si->framebufferHeight = Swap16IfLE(rfbScreen.height);
    si->format.redMax = Swap16IfLE(si->format.redMax);
    si->format.greenMax = Swap16IfLE(si->format.greenMax);
    si->format.blueMax = Swap16IfLE(si->format.blueMax);
	
	
    if (strlen(desktopName) > 128)      /* sanity check on desktop name len */
        desktopName[128] = 0;
	
    strcpy(buf + sz_rfbServerInitMsg, desktopName);
    len = strlen(buf + sz_rfbServerInitMsg);
    si->nameLength = Swap32IfLE(len);
	
	int offset = sz_rfbServerInitMsg + strlen(desktopName);
	
	if ([[CurlClientData getInstance]->m_metaData length] > 0)
	{
		NSArray* metaArr = [[CurlClientData getInstance]->m_metaData componentsSeparatedByString:@"/"];
		NSEnumerator* enumerator = [metaArr objectEnumerator];
		NSString* metaArg;
		while (metaArg = [enumerator nextObject])
		{
			CARD32 argLen = Swap32IfLE([metaArg length]);
			memcpy(buf + offset, (char*)&argLen, sizeof(CARD32));		
			offset += sizeof(CARD32);
			
			strcpy(buf + offset, [metaArg UTF8String]);
			offset += [metaArg length];
		}
	}
	
	
	
	dByteBuffer* handshake = [[dByteBuffer alloc] initWithBuffer:buf buflen:offset createCopy:true];
	
	if (1 == sendExact(cl, handshake, @"open"))
	{
		[msgClient messageToServer:@"{screencastResult:\"1\"}" from:@"dsc" to:@"wkplugin"];
		[msgClient messageToServer:@"progress" from:@"dsc" to:@"Screencaster"];
	}
	else
	{
		[msgClient messageToServer:@"{screencastResult:\"3\"}" from:@"dsc" to:@"wkplugin"];
		[msgClient messageToServer:@"idle" from:@"dsc" to:@"Screencaster"];
	}
	
	[handshake release];
	
    // Set encodings here itself.. Dimdim handshake is mighty different compared to the usual rfb handshake
	
	pthread_mutex_lock(&cl->updateMutex);
	
	cl->immediateUpdate = TRUE;
    cl->enableLastRectEncoding = TRUE;
    cl->enableXCursorShapeUpdates = FALSE;
	cl->preferredEncoding = rfbEncodingTight;
	
	if (1 == [[dimdimProfile valueForKey:@"EnablePointerAlgorithm"] intValue])
	{
		// Don't send rich cursor. Reflector will use the positioning algorithm.
		cl->enableCursorPosUpdates = TRUE;
	}
	else
	{
		// Send Rich Cursor 
		cl->useRichCursorEncoding = TRUE;
		cl->enableCursorPosUpdates = TRUE;
	}
	
	
    cl->zlibCompressLevel = [[dimdimProfile valueForKey:@"CompressLevel"] intValue];   // This needs to be changed based on parameters -- Bharat Varma
	cl->tightCompressLevel = [[dimdimProfile valueForKey:@"CompressLevel"] intValue];
    cl->tightQualityLevel = [[dimdimProfile valueForKey:@"JPEGEncodingLevel"] intValue];
	
	// Force a new update to the client
	if (rfbShouldSendNewCursor(cl) || (rfbShouldSendNewPosition(cl)))
		pthread_cond_signal(&cl->updateCond);
	
	pthread_mutex_unlock(&cl->updateMutex);

    cl->state = RFB_NORMAL;
}


/*
 * rfbProcessClientNormalMessage is called when the client has sent a normal
 * protocol message.
 */

void rfbProcessClientNormalMessage(rfbClientPtr cl) 
{
	int n;
	
	RegionRec tmpRegion;
	BoxRec box;
	
	//rfbLog("FUR: %d (%d,%d x %d,%d)\n", msg.fur.incremental, msg.fur.x, msg.fur.y,  msg.fur.w, msg.fur.h);
	
	box.x1 = 0;
	box.y1 = 0;
	box.x2 = rfbScreen.width;
	box.y2 = rfbScreen.height;
	
	pthread_mutex_lock(&cl->updateMutex);
	
	SAFE_REGION_INIT(pScreen,&tmpRegion,&box,0);
	REGION_UNION(pScreen, &cl->requestedRegion, &cl->requestedRegion, &tmpRegion);
	if (NO == cl->m_bIncremental) 
	{
		REGION_UNION(pScreen,&cl->modifiedRegion,&cl->modifiedRegion, &tmpRegion);
		cl->m_bIncremental = YES;
	}
	pthread_cond_signal(&cl->updateCond);
	REGION_UNINIT(pScreen,&tmpRegion);

	pthread_mutex_unlock(&cl->updateMutex);     
}

/*
 * rfbSendFramebufferUpdate - send the currently pending framebuffer update to
 * the RFB client.
 */

Bool rfbSendFramebufferUpdate(rfbClientPtr cl, RegionRec updateRegion) {
    int i;
    int nUpdateRegionRects = 0;
    Bool sendRichCursorEncoding = FALSE;
    Bool sendCursorPositionEncoding = FALSE;

    rfbFramebufferUpdateMsg *fu = (rfbFramebufferUpdateMsg *)cl->updateBuf;

    /* Now send the update */

    cl->rfbFramebufferUpdateMessagesSent++;

    if (cl->preferredEncoding == rfbEncodingCoRRE) {
        for (i = 0; i < REGION_NUM_RECTS(&updateRegion); i++) {
            int x = REGION_RECTS(&updateRegion)[i].x1;
            int y = REGION_RECTS(&updateRegion)[i].y1;
            int w = REGION_RECTS(&updateRegion)[i].x2 - x;
            int h = REGION_RECTS(&updateRegion)[i].y2 - y;
            nUpdateRegionRects += (((w-1) / cl->correMaxWidth + 1)
                                   * ((h-1) / cl->correMaxHeight + 1));
        }
    } else if (cl->preferredEncoding == rfbEncodingZlib) {
        for (i = 0; i < REGION_NUM_RECTS(&updateRegion); i++) {
            int x = REGION_RECTS(&updateRegion)[i].x1;
            int y = REGION_RECTS(&updateRegion)[i].y1;
            int w = REGION_RECTS(&updateRegion)[i].x2 - x;
            int h = REGION_RECTS(&updateRegion)[i].y2 - y;
            nUpdateRegionRects += (((h-1) / (ZLIB_MAX_SIZE( w ) / w)) + 1);
        }
    } else if (cl->preferredEncoding == rfbEncodingTight) {
        for (i = 0; i < REGION_NUM_RECTS(&updateRegion); i++) {
            int x = REGION_RECTS(&updateRegion)[i].x1;
            int y = REGION_RECTS(&updateRegion)[i].y1;
            int w = REGION_RECTS(&updateRegion)[i].x2 - x;
            int h = REGION_RECTS(&updateRegion)[i].y2 - y;
            int n = rfbNumCodedRectsTight(cl, x, y, w, h);
            if (n == 0) {
                nUpdateRegionRects = 0xFFFF;
                break;
            }
            nUpdateRegionRects += n;
        }
    } else {
        nUpdateRegionRects = REGION_NUM_RECTS(&updateRegion);
    }

    // Sometimes send the mouse cursor update also

    if (nUpdateRegionRects != 0xFFFF) {
        if (rfbShouldSendNewCursor(cl)) {
            sendRichCursorEncoding = TRUE;
            nUpdateRegionRects++;
        }
        if (rfbShouldSendNewPosition(cl)) {
            sendCursorPositionEncoding = TRUE;
            nUpdateRegionRects++;
        }
		if (cl->needNewScreenSize) {
			nUpdateRegionRects++;
		}        
    }

    fu->type = rfbFramebufferUpdate;
    fu->nRects = Swap16IfLE(nUpdateRegionRects);
	
	if (nUpdateRegionRects == 0)
	{
		// detected zero rects
//		rfbLog("No. of rectangles = %d\n", nUpdateRegionRects);
		return TRUE;
	}
	
    cl->ublen = sz_rfbFramebufferUpdateMsg;
	
    // Sometimes send the mouse cursor update (this can fail with big cursors so we'll try it first
    if (sendRichCursorEncoding) {
        if (!rfbSendRichCursorUpdate(cl)) {
            // rfbLog("Error Sending Cursor\n"); // We'll log at the lower level if it fails and only fail a few times
            // return FALSE;  Since this is the first update we can "skip the cursor update" instead of failing the whole thing
			--nUpdateRegionRects;
			fu->nRects = Swap16IfLE(nUpdateRegionRects);
        }
    }
    if (sendCursorPositionEncoding) {
        if (!rfbSendCursorPos(cl)) {
            rfbLog("Error Sending Cursor Position\n");
            return FALSE;
        }

    }
	if (cl->needNewScreenSize) {
        if (rfbSendScreenUpdateEncoding(cl)) {
            cl->needNewScreenSize = FALSE;
        }
        else {
            rfbLog("Error Sending New Screen Size\n");
            return FALSE;
        }            
    }
	
    for (i = 0; i < REGION_NUM_RECTS(&updateRegion); i++) {
        int x = REGION_RECTS(&updateRegion)[i].x1;
        int y = REGION_RECTS(&updateRegion)[i].y1;
        int w = REGION_RECTS(&updateRegion)[i].x2 - x;
        int h = REGION_RECTS(&updateRegion)[i].y2 - y;

		// Refresh with latest pointer (should be "read-locked" throughout here with CG but I don't see that option)
		if (cl->scalingFactor != 1)
			CopyScalingRect( cl, &x, &y, &w, &h, TRUE);
		else 
			cl->scalingFrameBuffer = rfbGetFramebuffer();
		
        cl->rfbRawBytesEquivalent += (sz_rfbFramebufferUpdateRectHeader
                                      + w * (cl->format.bitsPerPixel / 8) * h);

                if (!rfbSendRectEncodingTight(cl, x, y, w, h)) {
                    return FALSE;
                }
    }

    if (nUpdateRegionRects == 0xFFFF && !rfbSendLastRectMarker(cl))
        return FALSE;

    if (!rfbSendUpdateBuf(cl))
        return FALSE;

    return TRUE;
}

Bool rfbSendScreenUpdateEncoding(rfbClientPtr cl) {
    rfbFramebufferUpdateRectHeader rect;
				
    if (cl->ublen + sz_rfbFramebufferUpdateRectHeader > UPDATE_BUF_SIZE) {
        if (!rfbSendUpdateBuf(cl))
            return FALSE;
    }

    rect.r.x = 0;
    rect.r.y = 0;
    rect.r.w = Swap16IfLE((rfbScreen.width +cl->scalingFactor-1) / cl->scalingFactor);
    rect.r.h = Swap16IfLE((rfbScreen.height+cl->scalingFactor-1) / cl->scalingFactor);
    rect.encoding = Swap32IfLE(rfbEncodingDesktopResize);

    memcpy(&cl->updateBuf[cl->ublen], (char *)&rect,sz_rfbFramebufferUpdateRectHeader);
    cl->ublen += sz_rfbFramebufferUpdateRectHeader;

    cl->rfbRectanglesSent[rfbStatsDesktopResize]++;
    cl->rfbBytesSent[rfbStatsDesktopResize] += sz_rfbFramebufferUpdateRectHeader;

    // Let's push this out right away
    return rfbSendUpdateBuf(cl);
}

/*
 * Send a given rectangle in raw encoding (rfbEncodingRaw).
 */

Bool rfbSendRectEncodingRaw(rfbClientPtr cl, int x, int y, int w, int h) {
    rfbFramebufferUpdateRectHeader rect;
    int nlines;
    int bytesPerLine = w * (cl->format.bitsPerPixel / 8);
    char *fbptr = (cl->scalingFrameBuffer + (cl->scalingPaddedWidthInBytes * y)
                   + (x * (rfbScreen.bitsPerPixel / 8)));

    if (cl->ublen + sz_rfbFramebufferUpdateRectHeader > UPDATE_BUF_SIZE) {
        if (!rfbSendUpdateBuf(cl))
            return FALSE;
    }

    rect.r.x = Swap16IfLE(x);
    rect.r.y = Swap16IfLE(y);
    rect.r.w = Swap16IfLE(w);
    rect.r.h = Swap16IfLE(h);
    rect.encoding = Swap32IfLE(rfbEncodingRaw);

    memcpy(&cl->updateBuf[cl->ublen], (char *)&rect,sz_rfbFramebufferUpdateRectHeader);
    cl->ublen += sz_rfbFramebufferUpdateRectHeader;

    cl->rfbRectanglesSent[rfbEncodingRaw]++;
    cl->rfbBytesSent[rfbEncodingRaw]
        += sz_rfbFramebufferUpdateRectHeader + bytesPerLine * h;

    nlines = (UPDATE_BUF_SIZE - cl->ublen) / bytesPerLine;

    while (TRUE) {
        if (nlines > h)
            nlines = h;

        (*cl->translateFn)(cl->translateLookupTable, &rfbServerFormat,
                           &cl->format, fbptr, &cl->updateBuf[cl->ublen],
                           cl->scalingPaddedWidthInBytes, w, nlines);

        cl->ublen += nlines * bytesPerLine;
        h -= nlines;

        if (h == 0)     /* rect fitted in buffer, do next one */
            return TRUE;

        /* buffer full - flush partial rect and do another nlines */

        if (!rfbSendUpdateBuf(cl))
            return FALSE;

        fbptr += (cl->scalingPaddedWidthInBytes * nlines);

        nlines = (UPDATE_BUF_SIZE - cl->ublen) / bytesPerLine;
        if (nlines == 0) {
            rfbLog("rfbSendRectEncodingRaw: send buffer too small for %d "
                   "bytes per line\n", bytesPerLine);
            rfbCloseClient(cl);
            return FALSE;
        }
    }
}



/*
 * Send an empty rectangle with encoding field set to value of
 * rfbEncodingLastRect to notify client that this is the last
 * rectangle in framebuffer update ("LastRect" extension of RFB
                                    * protocol).
 */

Bool rfbSendLastRectMarker(rfbClientPtr cl) {
    rfbFramebufferUpdateRectHeader rect;

    if (cl->ublen + sz_rfbFramebufferUpdateRectHeader > UPDATE_BUF_SIZE) {
        if (!rfbSendUpdateBuf(cl))
            return FALSE;
    }
	

    rect.encoding = Swap32IfLE(rfbEncodingLastRect);
    rect.r.x = 0;
    rect.r.y = 0;
    rect.r.w = 0;
    rect.r.h = 0;

    memcpy(&cl->updateBuf[cl->ublen], (char *)&rect,sz_rfbFramebufferUpdateRectHeader);
    cl->ublen += sz_rfbFramebufferUpdateRectHeader;

    cl->rfbLastRectMarkersSent++;
    cl->rfbLastRectBytesSent += sz_rfbFramebufferUpdateRectHeader;

    return TRUE;
}


/*
 * Send the contents of updateBuf.  Returns 1 if successful, -1 if
 * not (errno should be set).
 */

Bool rfbSendUpdateBuf(rfbClientPtr cl) {

	if (cl->ublen == 0)
	{
	//	rfbLog("No data to be enqueued\n");
		return TRUE;
	}
	
	sendQueued(cl->updateBuf, cl->ublen);	
    cl->ublen = 0;
	
    return TRUE;
}


/*
 * rfbSendServerCutText sends a ServerCutText message to all the clients.
 */

void rfbSendServerCutText(rfbClientPtr cl, char *str, int len) {
    rfbServerCutTextMsg sct;

    sct.type = rfbServerCutText;
    sct.length = Swap32IfLE(len);

    if (WriteExact(cl, (char *)&sct, sz_rfbServerCutTextMsg) < 0) {
        rfbLogPerror("rfbSendServerCutText: write");
        rfbCloseClient(cl);
    }

    if (WriteExact(cl, str, len) < 0) {
        rfbLogPerror("rfbSendServerCutText: write");
        rfbCloseClient(cl);
    }
}
/*
 void
 rfbSendServerCutText(char *str, int len)
 {
     rfbClientPtr cl;
     rfbServerCutTextMsg sct;
     rfbClientIteratorPtr iterator;

     // XXX bad-- writing with client list lock held
     iterator = rfbGetClientIterator();
     while ((cl = rfbClientIteratorNext(iterator)) != NULL) {
         sct.type = rfbServerCutText;
         sct.length = Swap32IfLE(len);
         if (WriteExact(cl, (char *)&sct,
                        sz_rfbServerCutTextMsg) < 0) {
             rfbLogPerror("rfbSendServerCutText: write");
             rfbCloseClient(cl);
             continue;
         }
         if (WriteExact(cl, str, len) < 0) {
             rfbLogPerror("rfbSendServerCutText: write");
             rfbCloseClient(cl);
         }
     }
     rfbReleaseClientIterator(iterator);
 }
 */

/* SERVER SCALING EXTENSIONS */
void CopyScalingRect( rfbClientPtr cl, int* x, int* y, int* w, int* h, Bool bDoScaling ){
    unsigned long cx, cy, cw, ch;
    unsigned long rx, ry, rw, rh;
    unsigned char* srcptr;
    unsigned char* dstptr;
    unsigned char* tmpptr;
    unsigned long pixel_value=0, red, green, blue;
    unsigned long xx, yy, u, v;
    const unsigned long bytesPerPixel = rfbScreen.bitsPerPixel/8;
    const unsigned long csh = (rfbScreen.height+cl->scalingFactor-1)/ cl->scalingFactor;
    const unsigned long csw = (rfbScreen.width +cl->scalingFactor-1)/ cl->scalingFactor;

    cy = (*y) / cl->scalingFactor;
    ch = (*h+cl->scalingFactor-1) / cl->scalingFactor+1;
    cx = (*x) / cl->scalingFactor;
    cw = (*w+cl->scalingFactor-1) / cl->scalingFactor+1;

    if( cy > csh ){
        cy = csh;
    }
    if( cy + ch > csh ){
        ch = csh - cy;
    }
    if( cx > csw ){
        cx = csw;
    }
    if( cx + cw > csw ){
        cw = csw - cx;
    }

    if( bDoScaling ){
        ry = cy * cl->scalingFactor;
        rh = ch * cl->scalingFactor;
        rx = cx * cl->scalingFactor;
        rw = cw * cl->scalingFactor;

        /* Copy and scale data from screen buffer to scaling buffer */
        srcptr = (unsigned char*)rfbGetFramebuffer() + (ry * rfbScreen.paddedWidthInBytes ) + (rx * bytesPerPixel);
        dstptr = (unsigned char*)cl->scalingFrameBuffer+ (cy * cl->scalingPaddedWidthInBytes) + (cx * bytesPerPixel);

        if( cl->format.trueColour ) { /* Blend neighbouring pixels together */
            for( yy=0; yy < ch; yy++ ){
                for( xx=0; xx < cw; xx++ ){
                    red = green = blue = 0;
                    for( v = 0; v < (unsigned long)cl->scalingFactor; v++ ){
                        tmpptr = srcptr;
                        for( u = 0; u < (unsigned long)cl->scalingFactor; u++ ){
                            switch( bytesPerPixel ){
                            case 1:
                                pixel_value = (unsigned long)*(unsigned char* )tmpptr;
                                break;
                            case 2:
                                pixel_value = (unsigned long)*(unsigned short*)tmpptr;
                                break;
                            case 3:    /* 24bpp may cause bus error? */
                            case 4:
                                pixel_value = (unsigned long)*(unsigned long* )tmpptr;
                                break;
                            }
                            red   += (pixel_value >> rfbServerFormat.redShift  )& rfbServerFormat.redMax;
                            green += (pixel_value >> rfbServerFormat.greenShift)& rfbServerFormat.greenMax;
                            blue  += (pixel_value >> rfbServerFormat.blueShift )& rfbServerFormat.blueMax;
                            tmpptr  += rfbScreen.paddedWidthInBytes;
                        }
                        srcptr  += bytesPerPixel;
                    }
                    red   /= cl->scalingFactor * cl->scalingFactor;
                    green /= cl->scalingFactor * cl->scalingFactor;
                    blue  /= cl->scalingFactor * cl->scalingFactor;

                    pixel_value = (red   << rfbServerFormat.redShift)
                                + (green << rfbServerFormat.greenShift)
                                + (blue  << rfbServerFormat.blueShift);

                    switch( bytesPerPixel ){
                    case 1:
                        *(unsigned char* )dstptr = (unsigned char )pixel_value;
                        break;
                    case 2:
                        *(unsigned short*)dstptr = (unsigned short)pixel_value;
                        break;
                    case 3:    /* 24bpp may cause bus error? */
                    case 4:
                        *(unsigned long* )dstptr = (unsigned long )pixel_value;
                        break;
                    }
                    dstptr += bytesPerPixel;
                }
                srcptr += (rfbScreen.paddedWidthInBytes - cw * bytesPerPixel)* cl->scalingFactor;
                dstptr += cl->scalingPaddedWidthInBytes - cw * bytesPerPixel;
            }
        }else{ /* Not truecolour, so we can't blend. Just use the top-left pixel instead */
            for( yy=0; yy < ch; yy++ ){
                for( xx=0; xx < cw; xx++ ){
                    memcpy( dstptr, srcptr, bytesPerPixel);
                    srcptr += bytesPerPixel * cl->scalingFactor;
                    dstptr += bytesPerPixel;
                }
                srcptr += (rfbScreen.paddedWidthInBytes - cw * bytesPerPixel)* cl->scalingFactor;
                dstptr += cl->scalingPaddedWidthInBytes - cw * bytesPerPixel;
            }
        }
    }

    *y = cy;
    *h = ch;
    *x = cx;
    *w = cw;
}

/* Dimdim specific methods -- Bharat Varma */

void closeAllClients()
{
	pthread_mutex_trylock(&rfbClientListMutex);
	pthread_mutex_unlock(&rfbClientListMutex);
	rfbClientIteratorPtr iter = rfbGetClientIterator();
	while (true)
	{
		rfbClientPtr cl = rfbClientIteratorNext(iter);
		if (!cl)
		{
			rfbLog("All clients disconnected\n");
			break;
		}
	
		pthread_mutex_lock(&cl->updateMutex);
		rfbCloseClient(cl);
		rfbPrintStats(cl);
		pthread_mutex_unlock(&cl->updateMutex);
	}
	rfbReleaseClientIterator(iter);
}


/* Dimdim specific methods -- Bharat Varma */
