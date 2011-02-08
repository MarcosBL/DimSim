//  Copyright (C) 2001-2006 Constantin Kaplinsky. All Rights Reserved.
//  Copyright (C) 2002 Vladimir Vologzhanin. All Rights Reserved.
//  Copyright (C) 2000 Tridia Corporation. All Rights Reserved.
//  Copyright (C) 1999 AT&T Laboratories Cambridge. All Rights Reserved.
//
//  This file is part of the VNC system.
//
//  The VNC system is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
//  USA.
//
// TightVNC distribution homepage on the Web: http://www.tightvnc.com/
//
// If the source code for the VNC system is not available from the place 
// whence you received this file, check http://www.uk.research.att.com/vnc or contact
// the authors on vnc@uk.research.att.com for information on obtaining it.


// vncClient.cpp

// The per-client object.  This object takes care of all per-client stuff,
// such as socket input and buffering of updates.

// vncClient class handles the following functions:
// - Recieves requests from the connected client and
//   handles them
// - Handles incoming updates properly, using a vncBuffer
//   object to keep track of screen changes
// It uses a vncBuffer and is passed the vncDesktop and
// vncServer to communicate with.

// Includes
#include "stdhdrs.h"
#include <omnithread.h>
#include "resource.h"

// Custom
#include "vncClient.h"
#include "CurlSocket.h"
#include "vncDesktop.h"
#include "vncRegion.h"
#include "vncBuffer.h"
#include "vncService.h"
#include "vncPasswd.h"
#include "vncKeymap.h"
#include "Windows.h"
extern "C" {
#include "d3des.h"
}

#include "FileTransferItemInfo.h"
#include "vncMenu.h"

#include <tchar.h>

//
// Normally, using macros is no good, but this macro saves us from
// writing constants twice -- it constructs signature names from codes.
// Note that "code_sym" argument should be a single symbol, not an expression.
//

#define SetCapInfo(cap_ptr, code_sym, vendor)			\
{														\
	rfbCapabilityInfo *pcap = (cap_ptr);				\
	pcap->code = Swap32IfLE(code_sym);					\
	memcpy(pcap->vendorSignature, (vendor),				\
	sz_rfbCapabilityInfoVendor);						\
	memcpy(pcap->nameSignature, sig_##code_sym,			\
	sz_rfbCapabilityInfoName);							\
}

// vncClient thread class

class vncClientThread : public omni_thread
{
public:
	char * ConvertPath(char *path);

	// Init
	virtual BOOL Init(vncClient *client,
					  vncServer *server,
					  CurlSocket *socket,
					  BOOL reverse,
					  BOOL shared);

	// Dimdim Init routines
	//
	// Dimdim screenshare will support only
	// fixed profiles and authentications.
	// This is accomplished by an understanding between
	// screenshare (vnc server), reflector and the client
	//

	virtual UINT DimdimHandshake();
	virtual bool DimdimHandshakeStress();

	// Sub-Init routines
	virtual BOOL InitVersion();
	virtual BOOL InitAuthenticate();
	virtual int GetAuthenticationType();
	virtual void SendConnFailedMessage(const char *reasonString);
	virtual BOOL SendTextStringMessage(const char *str);
	virtual BOOL NegotiateTunneling();
	virtual BOOL NegotiateAuthentication(int authType);
	virtual BOOL AuthenticateNone();
	virtual BOOL AuthenticateVNC();
	virtual BOOL ReadClientInit();
	virtual BOOL SendInteractionCaps();
	virtual UINT validMultiMonSetup();

	// The main thread function
	virtual void run(void *arg);

protected:
	virtual ~vncClientThread();

	// Fields
protected:
	CurlSocket *m_socket;
	vncServer *m_server;
	vncClient *m_client;
	BOOL m_reverse;
	BOOL m_shared;
};

vncClientThread::~vncClientThread()
{
	// If we have a client object then delete it
	if (m_client != NULL)
		delete m_client;
}

BOOL
vncClientThread::Init(vncClient *client, vncServer *server, CurlSocket *socket, BOOL reverse, BOOL shared)
{
	// Save the server pointer and window handle
	m_server = server;
	m_socket = socket;
	m_client = client;
	m_reverse = reverse;
	m_shared = shared;

	// Start the thread
	start();

	return TRUE;
}

BOOL
vncClientThread::InitVersion()
{
	// Generate the server's protocol version
	rfbProtocolVersionMsg protocolMsg;
	sprintf((char *)protocolMsg, rfbProtocolVersionFormat, 3, 8);

	// Send the protocol message
	if (!m_socket->SendExact((char *)&protocolMsg, sz_rfbProtocolVersionMsg))
		return FALSE;

	// Now, get the client's protocol version
	rfbProtocolVersionMsg protocol_ver;
	protocol_ver[12] = 0;
	if (!m_socket->ReadExact((char *)&protocol_ver, sz_rfbProtocolVersionMsg))
		return FALSE;

	// Check the protocol version
	int major, minor;
	sscanf((char *)&protocol_ver, rfbProtocolVersionFormat, &major, &minor);
	if (major != 3) {
		vnclog.Print(LL_CONNERR, VNCLOG("unsupported protocol version %d.%d\n"),
					 major, minor);
		return FALSE;
	}
	int effective_minor = minor;
	if (minor > 8) {						// buggy client
		effective_minor = 8;
	} else if (minor > 3 && minor < 7) {	// non-standard client
		effective_minor = 3;
	} else if (minor < 3) {					// ancient client
		effective_minor = 3;
	}
	if (effective_minor != minor) {
		vnclog.Print(LL_CONNERR,
					 VNCLOG("non-standard protocol version 3.%d, using 3.%d instead\n"),
					 minor, effective_minor);
	}

	// Save the minor number of the protocol version
	m_client->m_protocol_minor_version = effective_minor;

	// TightVNC protocol extensions are not enabled yet
	m_client->m_protocol_tightvnc = FALSE;

	vnclog.Print(LL_INTINFO, VNCLOG("negotiated protocol version, RFB 3.%d\n"),
				 effective_minor);
	return TRUE;
}

BOOL
vncClientThread::InitAuthenticate()
{
	int secType = GetAuthenticationType();
	if (secType == rfbSecTypeInvalid)
		return FALSE;

	if (m_client->m_protocol_minor_version >= 7) {
		CARD8 list[3];
		list[0] = (CARD8)2;					// number of security types
		list[1] = (CARD8)secType;			// primary security type
		list[2] = (CARD8)rfbSecTypeTight;	// support for TightVNC extensions
		if (!m_socket->SendExact((char *)&list, sizeof(list)))
			return FALSE;
		CARD8 type;
		if (!m_socket->ReadExact((char *)&type, sizeof(type)))
			return FALSE;
		if (type == (CARD8)rfbSecTypeTight) {
			vnclog.Print(LL_INTINFO, VNCLOG("enabling protocol extensions\n"));
			m_client->m_protocol_tightvnc = TRUE;
			if (!NegotiateTunneling())
				return FALSE;
			if (!NegotiateAuthentication(secType))
				return FALSE;
		} else if (type != (CARD8)secType) {
			vnclog.Print(LL_CONNERR, VNCLOG("incorrect security type requested\n"));
			return FALSE;
		}
	} else {
		CARD32 authValue = Swap32IfLE(secType);
		if (!m_socket->SendExact((char *)&authValue, sizeof(authValue)))
			return FALSE;
	}

	switch (secType) {
	case rfbSecTypeNone:
		vnclog.Print(LL_CLIENTS, VNCLOG("no authentication necessary\n"));
		return AuthenticateNone();
	case rfbSecTypeVncAuth:
		vnclog.Print(LL_CLIENTS, VNCLOG("performing VNC authentication\n"));
		return AuthenticateVNC();
	}

	return FALSE;	// should not happen but just in case...
}

int
vncClientThread::GetAuthenticationType()
{
	if (!m_reverse && !m_server->ValidPasswordsSet())
	{
		vnclog.Print(LL_CONNERR,
					 VNCLOG("no password specified for server - client rejected\n"));

		// Send an error message to the client
		SendConnFailedMessage("This server does not have a valid password enabled. "
							  "Until a password is set, incoming connections cannot "
							  "be accepted.");
		return rfbSecTypeInvalid;
	}

	// By default we filter out local loop connections, because they're pointless
	if (!m_server->LoopbackOk())
	{
		char *localname = strdup(m_socket->GetSockName());
		char *remotename = strdup(m_socket->GetPeerName());

		// Check that the local & remote names are different!
		if (localname != NULL && remotename != NULL) {
			BOOL ok = strcmp(localname, remotename) != 0;

// FIXME: conceivable memory leak
			free(localname);
			free(remotename);

			if (!ok) {
				vnclog.Print(LL_CONNERR,
							 VNCLOG("loopback connection attempted - client rejected\n"));

				// Send an error message to the client
				SendConnFailedMessage("Local loop-back connections are disabled.");
				return rfbSecTypeInvalid;
			}
		}
	}

	// Verify the peer host name against the AuthHosts string
	vncServer::AcceptQueryReject verified;
	if (m_reverse) {
		verified = vncServer::aqrAccept;
	} else {
		verified = m_server->VerifyHost(m_socket->GetPeerName());
	}

	// If necessary, query the connection with a timed dialog
	BOOL skip_auth = FALSE;

	// The connection should be rejected, either due to AuthHosts settings,
	// or because of the "Reject" action performed in the query dialog
	if (verified == vncServer::aqrReject) {
		vnclog.Print(LL_CONNERR, VNCLOG("Client connection rejected\n"));
		SendConnFailedMessage("Your connection has been rejected.");
		return rfbSecTypeInvalid;
	}

	// Return preferred authentication type
	if (m_reverse || skip_auth || m_server->ValidPasswordsEmpty()) {
		return rfbSecTypeNone;
	} else {
		return rfbSecTypeVncAuth;
	}
}

//
// Send a "connection failed" message.
//

void
vncClientThread::SendConnFailedMessage(const char *reasonString)
{
	if (m_client->m_protocol_minor_version >= 7) {
		CARD8 zeroCount = 0;
		if (!m_socket->SendExact((char *)&zeroCount, sizeof(zeroCount)))
			return;
	} else {
		CARD32 authValue = Swap32IfLE(rfbSecTypeInvalid);
		if (!m_socket->SendExact((char *)&authValue, sizeof(authValue)))
			return;
	}
	SendTextStringMessage(reasonString);
}

//
// Send a text message preceded with a length counter.
//

BOOL
vncClientThread::SendTextStringMessage(const char *str)
{
	CARD32 len = Swap32IfLE(strlen(str));
	if (!m_socket->SendExact((char *)&len, sizeof(len)))
		return FALSE;
	if (!m_socket->SendExact(str, strlen(str)))
		return FALSE;

	return TRUE;
}

//
// Negotiate tunneling type (protocol versions 3.7t, 3.8t).
//

BOOL
vncClientThread::NegotiateTunneling()
{
	int nTypes = 0;

	// Advertise our tunneling capabilities (currently, nothing to advertise).
	rfbTunnelingCapsMsg caps;
	caps.nTunnelTypes = Swap32IfLE(nTypes);
	return m_socket->SendExact((char *)&caps, sz_rfbTunnelingCapsMsg);

	// Read tunneling type requested by the client (currently, not necessary).
	if (nTypes) {
		CARD32 tunnelType;
		if (!m_socket->ReadExact((char *)&tunnelType, sizeof(tunnelType)))
			return FALSE;
		tunnelType = Swap32IfLE(tunnelType);
		// We cannot do tunneling yet.
		vnclog.Print(LL_CONNERR, VNCLOG("unsupported tunneling type requested\n"));
		return FALSE;
	}

	vnclog.Print(LL_INTINFO, VNCLOG("negotiated tunneling type\n"));
	return TRUE;
}

//
// Negotiate authentication scheme (protocol versions 3.7t, 3.8t).
// NOTE: Here we always send en empty list for "no authentication".
//

BOOL
vncClientThread::NegotiateAuthentication(int authType)
{
	int nTypes = 0;

	if (authType == rfbAuthVNC) {
		nTypes++;
	} else if (authType != rfbAuthNone) {
		vnclog.Print(LL_INTERR, VNCLOG("unknown authentication type\n"));
		return FALSE;
	}

	rfbAuthenticationCapsMsg caps;
	caps.nAuthTypes = Swap32IfLE(nTypes);
	if (!m_socket->SendExact((char *)&caps, sz_rfbAuthenticationCapsMsg))
		return FALSE;

	if (authType == rfbAuthVNC) {
		// Inform the client about supported authentication types.
		rfbCapabilityInfo cap;
		SetCapInfo(&cap, rfbAuthVNC, rfbStandardVendor);
		if (!m_socket->SendExact((char *)&cap, sz_rfbCapabilityInfo))
			return FALSE;

		CARD32 type;
		if (!m_socket->ReadExact((char *)&type, sizeof(type)))
			return FALSE;
		type = Swap32IfLE(type);
		if (type != authType) {
			vnclog.Print(LL_CONNERR, VNCLOG("incorrect authentication type requested\n"));
			return FALSE;
		}
	}

	return TRUE;
}

//
// Handle security type for "no authentication".
//

BOOL
vncClientThread::AuthenticateNone()
{
	if (m_client->m_protocol_minor_version >= 8) {
		CARD32 secResult = Swap32IfLE(rfbAuthOK);
		if (!m_socket->SendExact((char *)&secResult, sizeof(secResult)))
			return FALSE;
	}
	return TRUE;
}

//
// Perform standard VNC authentication
//

BOOL
vncClientThread::AuthenticateVNC()
{
	BOOL auth_ok = FALSE;

	// Retrieve local passwords
	char password[MAXPWLEN];
	BOOL password_set = m_server->GetPassword(password);
	vncPasswd::ToText plain(password);
	BOOL password_viewonly_set = m_server->GetPasswordViewOnly(password);
	vncPasswd::ToText plain_viewonly(password);

	// Now create a 16-byte challenge
	char challenge[16];
	char challenge_viewonly[16];

	vncRandomBytes((BYTE *)&challenge);
	memcpy(challenge_viewonly, challenge, 16);

	// Send the challenge to the client
	if (!m_socket->SendExact(challenge, sizeof(challenge)))
		return FALSE;

	// Read the response
	char response[16];
	if (!m_socket->ReadExact(response, sizeof(response)))
		return FALSE;

	// Encrypt the challenge bytes
	vncEncryptBytes((BYTE *)&challenge, plain);

	// Compare them to the response
	if (password_set && memcmp(challenge, response, sizeof(response)) == 0) {
		auth_ok = TRUE;
	} else {
		// Check against the view-only password
		vncEncryptBytes((BYTE *)&challenge_viewonly, plain_viewonly);
		if (password_viewonly_set && memcmp(challenge_viewonly, response, sizeof(response)) == 0) {
			m_client->EnablePointer(FALSE);
			m_client->EnableKeyboard(FALSE);
			auth_ok = TRUE;
		}
	}

	// Did the authentication work?
	CARD32 secResult;
	if (!auth_ok) {
		vnclog.Print(LL_CONNERR, VNCLOG("authentication failed\n"));

		secResult = Swap32IfLE(rfbAuthFailed);
		m_socket->SendExact((char *)&secResult, sizeof(secResult));
		SendTextStringMessage("Authentication failed");
		return FALSE;
	} else {
		// Tell the client we're ok
		secResult = Swap32IfLE(rfbAuthOK);
		if (!m_socket->SendExact((char *)&secResult, sizeof(secResult)))
			return FALSE;
	}

	return TRUE;
}

//
// Read client initialisation message
//

BOOL
vncClientThread::ReadClientInit()
{
	// Read the client's initialisation message
	rfbClientInitMsg client_ini;
	if (!m_socket->ReadExact((char *)&client_ini, sz_rfbClientInitMsg))
		return FALSE;

	// If the client wishes to have exclusive access then remove other clients
	if (!client_ini.shared && !m_shared)
	{
		// Which client takes priority, existing or incoming?
		if (m_server->ConnectPriority() < 1) {
			// Incoming
			vnclog.Print(LL_INTINFO, VNCLOG("non-shared connection - disconnecting old clients\n"));
			m_server->KillAuthClients();
		} else if (m_server->ConnectPriority() > 1) {
			// Existing
			if (m_server->AuthClientCount() > 0) {
				vnclog.Print(LL_CLIENTS, VNCLOG("connections already exist - client rejected\n"));
				return FALSE;
			}
		}
	}

	// Tell the server that this client is ok
	return m_server->Authenticated(m_client->GetClientId());
}

//
// Advertise our messaging capabilities (protocol version 3.7+).
//

BOOL
vncClientThread::SendInteractionCaps()
{
	// Update these constants on changing capability lists!
	const int MAX_SMSG_CAPS = 4;
	const int MAX_CMSG_CAPS = 6;
	const int MAX_ENC_CAPS = 14;

	int i;

	// Supported server->client message types
	rfbCapabilityInfo smsg_list[MAX_SMSG_CAPS];
	i = 0;

	if (m_server->FileTransfersEnabled() && m_client->IsInputEnabled()) {
		SetCapInfo(&smsg_list[i++], rfbFileListData,       rfbTightVncVendor);
		SetCapInfo(&smsg_list[i++], rfbFileDownloadData,   rfbTightVncVendor);
		SetCapInfo(&smsg_list[i++], rfbFileUploadCancel,   rfbTightVncVendor);
		SetCapInfo(&smsg_list[i++], rfbFileDownloadFailed, rfbTightVncVendor);
	}

	int nServerMsgs = i;
	if (nServerMsgs > MAX_SMSG_CAPS) {
		vnclog.Print(LL_INTERR,
					 VNCLOG("assertion failed, nServerMsgs > MAX_SMSG_CAPS\n"));
		return FALSE;
	}

	// Supported client->server message types
	rfbCapabilityInfo cmsg_list[MAX_CMSG_CAPS];
	i = 0;

	if (m_server->FileTransfersEnabled() && m_client->IsInputEnabled()) {
		SetCapInfo(&cmsg_list[i++], rfbFileListRequest,    rfbTightVncVendor);
		SetCapInfo(&cmsg_list[i++], rfbFileDownloadRequest,rfbTightVncVendor);
		SetCapInfo(&cmsg_list[i++], rfbFileUploadRequest,  rfbTightVncVendor);
		SetCapInfo(&cmsg_list[i++], rfbFileUploadData,     rfbTightVncVendor);
		SetCapInfo(&cmsg_list[i++], rfbFileDownloadCancel, rfbTightVncVendor);
		SetCapInfo(&cmsg_list[i++], rfbFileUploadFailed,   rfbTightVncVendor);
	}

	int nClientMsgs = i;
	if (nClientMsgs > MAX_CMSG_CAPS) {
		vnclog.Print(LL_INTERR,
					 VNCLOG("assertion failed, nClientMsgs > MAX_CMSG_CAPS\n"));
		return FALSE;
	}

	// Encoding types
	rfbCapabilityInfo enc_list[MAX_ENC_CAPS];
	i = 0;
	SetCapInfo(&enc_list[i++],  rfbEncodingCopyRect,       rfbStandardVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingRRE,            rfbStandardVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingCoRRE,          rfbStandardVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingHextile,        rfbStandardVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingZlib,           rfbTridiaVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingZlibHex,        rfbTridiaVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingTight,          rfbTightVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingCompressLevel0, rfbTightVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingQualityLevel0,  rfbTightVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingXCursor,        rfbTightVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingRichCursor,     rfbTightVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingPointerPos,     rfbTightVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingLastRect,       rfbTightVncVendor);
	SetCapInfo(&enc_list[i++],  rfbEncodingNewFBSize,      rfbTightVncVendor);
	int nEncodings = i;
	if (nEncodings > MAX_ENC_CAPS) {
		vnclog.Print(LL_INTERR,
					 VNCLOG("assertion failed, nEncodings > MAX_ENC_CAPS\n"));
		return FALSE;
	}

	// Create and send the header structure
	rfbInteractionCapsMsg intr_caps;
	intr_caps.nServerMessageTypes = Swap16IfLE(nServerMsgs);
	intr_caps.nClientMessageTypes = Swap16IfLE(nClientMsgs);
	intr_caps.nEncodingTypes = Swap16IfLE(nEncodings);
	intr_caps.pad = 0;
	if (!m_socket->SendExact((char *)&intr_caps, sz_rfbInteractionCapsMsg))
		return FALSE;

	// Send the capability lists
	if (nServerMsgs &&
		!m_socket->SendExact((char *)&smsg_list[0],
		sz_rfbCapabilityInfo * nServerMsgs))
		return FALSE;
	if (nClientMsgs &&
		!m_socket->SendExact((char *)&cmsg_list[0],
		sz_rfbCapabilityInfo * nClientMsgs))
		return FALSE;
	if (nEncodings &&
		!m_socket->SendExact((char *)&enc_list[0],
		sz_rfbCapabilityInfo * nEncodings))
		return FALSE;

	return TRUE;
}

void
ClearKeyState(BYTE key)
{
	// This routine is used by the VNC client handler to clear the
	// CAPSLOCK, NUMLOCK and SCROLL-LOCK states.

	BYTE keyState[256];

	GetKeyboardState((LPBYTE)&keyState);

	if(keyState[key] & 1)
	{
		// Simulate the key being pressed
		keybd_event(key, 0, KEYEVENTF_EXTENDEDKEY, 0);

		// Simulate it being release
		keybd_event(key, 0, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
	}
}

bool vncClientThread::DimdimHandshakeStress()
{
	// INIT RFB PROTOCOL VERSION

	// Save the minor number of the protocol version
	// Reflector's protocol version is 3.3
	m_client->m_protocol_minor_version = 3;

	// TightVNC protocol extensions
	m_client->m_protocol_tightvnc = FALSE;

	// CLIENT AUTHENTICATION NOT REQUIRED.

	// READ CLIENT INIT MESSAGE

	// Except, we don't actually read the client's init message :)
	// Just add the client to the authenticated list.

	m_server->Authenticated(m_client->GetClientId());

	// INIT PIXEL FORMAT

	// Get the screen format
	m_client->m_fullscreen = m_client->m_buffer->GetSize();

	// Get the name of this desktop
	char desktopname[MAX_COMPUTERNAME_LENGTH+1];
	DWORD desktopnamelen = MAX_COMPUTERNAME_LENGTH + 1;
	if (GetComputerName(desktopname, &desktopnamelen))
	{
		// Make the name lowercase
		for (size_t x=0; x<strlen(desktopname); x++)
		{
			desktopname[x] = tolower(desktopname[x]);
		}
	}
	else
	{
		strcpy(desktopname, "WinVNC");
	}

	// Send the Dimdim server format message to the client

	rfbServerInitMsg server_ini;
	server_ini.format = m_client->m_buffer->GetLocalFormat();

	/*
	* type = 0
	* format pad1 = 217
	* format pad2 = 66
	* bitsPerPixel = 8/32
	* depth		= 8/24
	* redMax		= 7
	* greenMax		= 7
	* blueMax		= 3
	* redShift		= 0/16
	* greenShift	= 3/8
	* blueShift	= 6/0
	* pad1			= 0/205
	* pad2			= 0/52685
	*/

	if (TRUE == m_server->RestrictedColorsEnabled())
	{
		server_ini.format.bigEndian = 0;
		server_ini.format.trueColour = 1;
		server_ini.format.bitsPerPixel = 8;
		server_ini.format.depth = 8;
		server_ini.format.redMax = 7;
		server_ini.format.greenMax = 7;
		server_ini.format.blueMax = 3;
		server_ini.format.redShift = 0;
		server_ini.format.greenShift = 3;
		server_ini.format.blueShift = 6;
		server_ini.format.pad1 = 0;
		server_ini.format.pad2 = 0;
	}

	m_client->m_buffer->SetClientFormat(server_ini.format);

	// Set the palette-changed flag, just in case...
	m_client->m_palettechanged = TRUE;

	// Endian swaps
	RECT sharedRect;
	sharedRect = m_server->GetSharedRect();
	server_ini.framebufferWidth = Swap16IfLE(sharedRect.right- sharedRect.left);
	server_ini.framebufferHeight = Swap16IfLE(sharedRect.bottom - sharedRect.top);
	server_ini.format.redMax = Swap16IfLE(server_ini.format.redMax);
	server_ini.format.greenMax = Swap16IfLE(server_ini.format.greenMax);
	server_ini.format.blueMax = Swap16IfLE(server_ini.format.blueMax);
	server_ini.nameLength = Swap32IfLE(strlen(desktopname));

	char* buff = (char*) malloc(sizeof(server_ini) + strlen(desktopname));
	memcpy(buff, (char*)&server_ini, sizeof(server_ini));
	memcpy(buff+sizeof(server_ini), desktopname, strlen(desktopname));


	// Set retry count and wait time to the sockets
	m_socket->setMaxRetries(m_server->GetMaxRetries());
	m_socket->setMaxWaitTime(m_server->GetMaxWaitTime());

	vnclog.Print(LL_SOCKINFO, "Starting Stress Test\n");

	for (int i = 0; i < m_server->GetStressCount(); i++)
	{
		vnclog.Print(LL_SOCKINFO, "Stress test %d, opening connection\n", (i+1));
		if (!m_socket->Open(buff, sizeof(server_ini) + strlen(desktopname)))
		{
			vnclog.Print(LL_SOCKINFO, "Stress test %d failed, while opening connection\n", (i+1));
			return false;
		}

		Sleep(500);

		vnclog.Print(LL_SOCKINFO, "Stress test %d, closing connection\n", (i+1));
		if (!m_socket->Close(true))
		{
			vnclog.Print(LL_SOCKINFO, "Stress test %d failed, while closing connection\n", (i+1));
			return false;
		}
	}

	vnclog.Print(LL_SOCKINFO, "Stress Test Ended\n");
	return true;
}

template <class TpFn>
HINSTANCE  LoadNImport(LPCTSTR szDllName, LPCTSTR szFName, TpFn &pfn)
{
	HINSTANCE hDll = LoadLibrary(szDllName);
	if (hDll)
	{
		pfn = (TpFn)GetProcAddress(hDll, szFName);
		if (pfn)
			return hDll;
		FreeLibrary(hDll);
	}
	vnclog.Print(
		LL_INTERR,
		VNCLOG("Can not import '%s' from '%s'.\n"),
		szFName, szDllName);
	return NULL;
}

UINT vncClientThread::validMultiMonSetup()
{
	vnclog.Print(LL_DIMDIM, VNCLOG("Entered monitor detection algorithm\r\n"));
	int totalMonitors = GetSystemMetrics(SM_CMONITORS);
	vnclog.Print(LL_DIMDIM, VNCLOG("No. of monitors detected = %d\r\n"), totalMonitors);
//	if (totalMonitors == 1)
//	{
//		vnclog.Print(LL_DIMDIM, VNCLOG("Only 1 display detected. Valid setup\r\n"));
//		return 1;
//	}

	DISPLAY_DEVICE pDd;
	pEnumDisplayDevices pd = NULL;
	HINSTANCE  hInstUser32 = LoadNImport("User32.DLL", "EnumDisplayDevicesA", pd);
	if (!hInstUser32) 
	{
		// Best guess.. Not much we can do.
		vnclog.Print(LL_DIMDIM, VNCLOG("Unable to load/import library to enumerate displays. Forcing result to valid monitor setup\r\n"));
		return 1;
	}

	ZeroMemory(&pDd, sizeof(DISPLAY_DEVICE));
	pDd.cb = sizeof(DISPLAY_DEVICE);
	BOOL result;
	INT devNum = 0;
	UINT uValid = 1;
	
	while (result = (*pd)(NULL,devNum, &pDd, 0))
	{
		if (devNum > totalMonitors)
		{
			vnclog.Print(LL_DIMDIM, VNCLOG("All displays enumerated\r\n"));
			break;
		}
		vnclog.Print(LL_DIMDIM, VNCLOG("Enumerating display number %d\r\n"), devNum);

		vnclog.Print(LL_DIMDIM, "DevNum:%d\nName:%s\nString:%s\nID:%s\nKey:%s\nFlags=%08X\r\n", devNum, &pDd.DeviceName[0], 
			&pDd.DeviceString[0], &pDd.DeviceID[0], &pDd.DeviceKey[0], pDd.StateFlags);

		// What we need to check is if any display has negative coordinates (Primary display always has coordinates beginning at 0, 0)

		DEVMODE devmode;
		FillMemory(&devmode, sizeof(DEVMODE), 0);
		devmode.dmSize = sizeof(DEVMODE);
		devmode.dmDriverExtra = 0;
		EnumDisplaySettings(pDd.DeviceName, ENUM_CURRENT_SETTINGS, &devmode);

		vnclog.Print(LL_DIMDIM, VNCLOG("Detected display position as %d X %d\r\n"), devmode.dmPosition.x, devmode.dmPosition.y);

		if ( (devmode.dmPosition.x < 0) || (devmode.dmPosition.y < 0) )
		{
			vnclog.Print(LL_DIMDIM, VNCLOG("Unsupported monitor setup\r\n"));
			uValid = 0;
			break;
		}

		devNum++;
	}

	FreeLibrary(hInstUser32);
	return uValid;
}

UINT vncClientThread::DimdimHandshake()
{
	if (1 != validMultiMonSetup())
	{
		std::wstringstream msg;
		msg << _T("{screencastResult:\"9\"}");
		m_server->NotifyDimdim(msg.str());
		return 2;
	}

	if (1 == ::GetSystemMetrics(SM_REMOTESESSION))
	{
		std::wstringstream msg;
		msg << _T("{screencastResult:\"8\"}");
		m_server->NotifyDimdim(msg.str());
		return 2;
	}

	// INIT RFB PROTOCOL VERSION

	// Save the minor number of the protocol version
	// Reflector's protocol version is 3.3
	m_client->m_protocol_minor_version = 3;

	// TightVNC protocol extensions
	m_client->m_protocol_tightvnc = FALSE;

	// CLIENT AUTHENTICATION NOT REQUIRED.

	// READ CLIENT INIT MESSAGE

	// Except, we don't actually read the client's init message :)
	// Just add the client to the authenticated list.

	m_server->Authenticated(m_client->GetClientId());

	int width, height, depth = 0;

	// It is important to call GetScreenInfo *after* Authenticated call.
	// The driver gets initialized twice otherwise.
	m_server->GetScreenInfo(width, height, depth);

	if (depth == 16)
	{
		// we don't support 16 bit currently.
		std::wstringstream msg;
		msg << _T("{screencastResult:\"10\"}");
		m_server->NotifyDimdim(msg.str());
		return 2;
	}


	// INIT PIXEL FORMAT

	// Get the screen format
	m_client->m_fullscreen = m_client->m_buffer->GetSize();


	// Get the name of this desktop
	char desktopname[MAX_COMPUTERNAME_LENGTH+1];
	DWORD desktopnamelen = MAX_COMPUTERNAME_LENGTH + 1;
	if (GetComputerName(desktopname, &desktopnamelen))
	{
		// Make the name lowercase
		for (size_t x=0; x<strlen(desktopname); x++)
		{
			desktopname[x] = tolower(desktopname[x]);
		}
	}
	else
	{
		strcpy(desktopname, "WinVNC");
	}

	// Send the Dimdim server format message to the client

	rfbServerInitMsg server_ini;
	server_ini.format = m_client->m_buffer->GetLocalFormat();

	/*
	 * type = 0
	 * format pad1 = 217
	 * format pad2 = 66
	 * bitsPerPixel = 8/32
	 * depth		= 8/24
	 * redMax		= 7
	 * greenMax		= 7
	 * blueMax		= 3
	 * redShift		= 0/16
	 * greenShift	= 3/8
	 * blueShift	= 6/0
	 * pad1			= 0/205
	 * pad2			= 0/52685
	 */

	if (TRUE == m_server->RestrictedColorsEnabled())
	{
		server_ini.format.bigEndian = 0;
		server_ini.format.trueColour = 1;
		server_ini.format.bitsPerPixel = 8;
		server_ini.format.depth = 8;
		server_ini.format.redMax = 7;
		server_ini.format.greenMax = 7;
		server_ini.format.blueMax = 3;
		server_ini.format.redShift = 0;
		server_ini.format.greenShift = 3;
		server_ini.format.blueShift = 6;
		server_ini.format.pad1 = 0;
		server_ini.format.pad2 = 0;
	}

	m_client->m_buffer->SetClientFormat(server_ini.format);

	// Set the palette-changed flag, just in case...
	m_client->m_palettechanged = TRUE;

	// Endian swaps
	RECT sharedRect;
	sharedRect = m_server->GetSharedRect();
	server_ini.framebufferWidth = Swap16IfLE(sharedRect.right- sharedRect.left);
	server_ini.framebufferHeight = Swap16IfLE(sharedRect.bottom - sharedRect.top);
	server_ini.format.redMax = Swap16IfLE(server_ini.format.redMax);
	server_ini.format.greenMax = Swap16IfLE(server_ini.format.greenMax);
	server_ini.format.blueMax = Swap16IfLE(server_ini.format.blueMax);
	server_ini.nameLength = Swap32IfLE(strlen(desktopname));

	size_t allocSize = sizeof(server_ini) + strlen(desktopname);

	std::string dimdim_id("");
	std::string room_id("");
	std::string session_id("");

	CARD32 didLength = 0;
	CARD32 sidLength = 0;
	CARD32 ridLength = 0;

	if (m_socket->getMetaData().length() > 0)
	{
		dimdim_id.assign(m_socket->getMetaData());
		room_id.assign(m_socket->getMetaData());
		session_id.assign(m_socket->getMetaData());
		
		dimdim_id.assign(dimdim_id.substr(0, dimdim_id.find_first_of("/")));
		session_id.assign(session_id.substr(session_id.find_last_of("/") + 1, session_id.length() - session_id.find_last_of("/")));
		room_id.assign(room_id.substr(dimdim_id.length() + 1, room_id.length() - dimdim_id.length() -session_id.length() - 2));

		didLength = Swap32IfLE(dimdim_id.length());
		sidLength = Swap32IfLE(session_id.length());
		ridLength = Swap32IfLE(room_id.length());

		allocSize += (3 * sizeof(CARD32)) + dimdim_id.length() + session_id.length() + room_id.length();
	}

	char* buff = (char*) malloc(allocSize);
	memcpy(buff, (char*)&server_ini, sizeof(server_ini));
	memcpy(buff+sizeof(server_ini), desktopname, strlen(desktopname));

	if (m_socket->getMetaData().length() > 0)
	{

		// Write size and then the data corresponding to each of dimdim_id, session_id and room_id

		// dimdim_id
		size_t offset = sizeof(server_ini) + strlen(desktopname);
		memcpy(buff + offset, (char*)&didLength, sizeof(CARD32));
		offset = offset + sizeof(CARD32);
		memcpy(buff + offset, dimdim_id.c_str(), dimdim_id.length());

		// room_id
		offset = offset + dimdim_id.length();
		memcpy(buff + offset, (char*)&ridLength, sizeof(CARD32));
		offset = offset + sizeof(CARD32);
		memcpy(buff + offset, room_id.c_str(), room_id.length());

		// session_id
		offset = offset + room_id.length();
		memcpy(buff + offset, (char*)&sidLength, sizeof(CARD32));
		offset = offset + sizeof(CARD32);
		memcpy(buff + offset, session_id.c_str(), session_id.length());

	}

	// Set retry count and wait time to the sockets
	m_socket->setMaxRetries(m_server->GetMaxRetries());
	m_socket->setMaxWaitTime(m_server->GetMaxWaitTime());

	if (2 == m_server->GetOperationType() || 3 == m_server->GetOperationType())
	{
		if (!m_socket->Open(buff, allocSize))
		{
			std::wstringstream msg;
			msg << _T("{screencastResult:\"3\"}");
			m_server->NotifyDimdim(msg.str());
			free(buff);
			return 0;
		}
	}

	// Set minimum transaction wait time to socket
	m_socket->SetTransactionWaitTime(m_server->GetTransactionWaitTime());
	// Set block size to socket
	m_socket->SetBlockSize(m_server->GetBlockSize());
	// Set operation type to socket
	m_socket->SetOperationType(m_server->GetOperationType());

	if (m_server->GetOperationType() == 1 || m_server->GetOperationType() == 3)
	{
		m_socket->dumpPackets(buff, allocSize);
		m_socket->logDump(buff, allocSize, "SERVER INIT PACKET");
	}

	free(buff);

	// UNLOCK INITIAL SETUP
	// Initial negotiation is complete, so set the protocol ready flag
	{
		omni_mutex_lock l(m_client->m_regionLock);
		m_client->m_protocol_ready = TRUE;
	}

	// Clear the CapsLock and NumLock keys
	if (m_client->IsKeyboardEnabled())
	{
		ClearKeyState(VK_CAPITAL);
		// *** JNW - removed because people complain it's wrong
		//ClearKeyState(VK_NUMLOCK);
		ClearKeyState(VK_SCROLL);
	}

	// ENCODINGS AND PROTOCOL EXTENSIONS

	// Enforcing 'tight' encoding -
	m_client->m_buffer->SetEncoding(7);

	// Enable usage of CopyRect
	m_client->m_copyrect_use = m_server->CopyRectEnabled();

	if (TRUE == m_server->RestrictedColorsEnabled())
	{
		m_client->m_buffer->SetQualityLevel(-1); 
	}
	else
	{
		// JPEG encoding is available only for 24-bit screencasts
		// For 8-bit screencasts, JPEG encoding is not considered.

		m_client->m_buffer->SetQualityLevel(m_server->GetJPEGLevel()); 
	}

	// The default compression level is 6
	// But setting this anyway

	m_client->m_buffer->SetCompressLevel(m_server->GetCompressLevel());

	// Enable LastRect protocol extensions
	m_client->m_buffer->EnableLastRect(TRUE);

	// Enable NewFBSize protocol extensions
	m_client->m_use_NewFBSize = TRUE;

	if (TRUE == m_server->PointerAlgorithmEnabled())
	{
		// Enable full-color cursor shape updates
		m_client->m_buffer->EnableRichCursor(TRUE);
		m_client->m_buffer->EnableXCursor(FALSE);

		m_client->m_use_PointerPos = TRUE;

		// Pointer position protocol enabled
		m_client->SetCursorPosChanged();
	}
	else
	{
		m_client->m_buffer->EnableXCursor(FALSE);
		m_client->m_buffer->EnableRichCursor(FALSE);
		m_client->m_use_PointerPos = FALSE;
		m_client->m_cursor_update_pending = FALSE;
		m_client->m_cursor_update_sent = FALSE;
		m_client->m_cursor_pos_changed = FALSE;
	}

	std::wstringstream msg;
	msg << _T("{screencastResult:\"1\"}");
	return (true == m_server->NotifyDimdim(msg.str()));
}

void
vncClientThread::run(void *arg)
{
	// All this thread does is go into a socket-recieve loop,
	// waiting for stuff on the given socket

	// IMPORTANT : ALWAYS call RemoveClient on the server before quitting
	// this thread.

//	vnclog.Print(LL_CLIENTS, VNCLOG("client connected : %s (id %hd)\n"),
//		m_client->GetClientName(), m_client->GetClientId());

	// Save the handle to the thread's original desktop
	HDESK home_desktop = GetThreadDesktop(GetCurrentThreadId());

	BOOL connected = TRUE;
	bool bIncremental = false;

	if (TRUE == m_server->IsStressInvoked())
	{
		DimdimHandshakeStress();
		connected = FALSE;
	}
	else 
	{
		UINT retval = DimdimHandshake();
		if (0 == retval)
			connected = FALSE;
		else if (2 == retval)
		{
			connected = FALSE;
			return;
		}

	}

	// MAIN LOOP

	UINT iStateChanged = 0;

	while (connected)
	{
		// verify if the pipe server is actually running
		std::wstringstream msg;
		msg << _T("ping");
		if (1 != m_server->NotifyDimdim(msg.str()))
		{
			vnclog.Print(LL_CLIENTS, VNCLOG("ping to pipe server failed. Possible browser crash. closing connection.\n"));
			HMODULE moduleHandle = ::GetModuleHandle("dsc.exe");
			char exeLoc[1024];
			::GetModuleFileName(moduleHandle, exeLoc, 1024);
			std::string currentDir(exeLoc);
			currentDir = currentDir.substr(0, currentDir.length() - strlen("dsc.exe"));
			::ShellExecute(NULL, "open", "dpc.exe", "-menu screencaster disable", currentDir.c_str(), 0);
			connected = false;
			break;
		}

		if (1 == ::GetSystemMetrics(SM_REMOTESESSION))
		{
			std::wstringstream msg;
			msg << _T("{screencastResult:\"8\"}");
			m_server->NotifyDimdim(msg.str());
			
			connected = false;
			break;
		}

		// Ensure that we're running in the correct desktop
		if (!vncService::InputDesktopSelected())
		{
			if (!vncService::SelectDesktop(NULL))
			{
				std::wstringstream msg;
				msg << _T("{screencastResult:\"6\"}");
				m_server->NotifyDimdim(msg.str());

				connected = false;
				break;
			}
		}

		if (!m_socket->ReadExact(NULL, 0))
		{
			if (m_socket->isSendFailed())
			{
				std::wstringstream msg;
				msg << _T("{screencastResult:\"3\"}");
				m_server->NotifyDimdim(msg.str());
			}
			else 
			{
				// Ensure that we're running in the correct desktop
				if (!vncService::InputDesktopSelected())
				{
					if (!vncService::SelectDesktop(NULL))
					{
						std::wstringstream msg;
						msg << _T("{screencastResult:\"6\"}");
						m_server->NotifyDimdim(msg.str());

						connected = false;
						break;
					}
				}
			//	else
			//	{
			//		std::wstringstream msg;
			//		msg << _T("{screencastResult:\"2\"}");
			//		m_server->NotifyDimdim(msg.str());
			//	}
			}

			connected = false;
			break;
		}
		
		HWND sharedWnd = m_server->GetWindowShared();
		if (sharedWnd > 0 && FALSE == ::IsWindow(sharedWnd))
		{
			// The window got closed

			std::wstringstream msg;
			msg << _T("{screencastResult:\"4\"}");
			m_server->NotifyDimdim(msg.str());

			connected = false;
			break;
		}

		{
			RECT update;
			RECT sharedRect;
			{
				omni_mutex_lock l(m_client->m_regionLock);

				sharedRect = m_server->GetSharedRect();

				int furX = 0;
				int furY = 0;
				int furW = sharedRect.right - sharedRect.left;
				int furH = sharedRect.bottom - sharedRect.top;

				// Get the specified rectangle as the region to send updates for.
				update.left = furX + sharedRect.left;
				update.top = furY + sharedRect.top;
				update.right = update.left + furW;

				if (update.right > sharedRect.right)
					update.right = sharedRect.right;
				if (update.left < sharedRect.left)
					update.left = sharedRect.left;

				update.bottom = update.top + furH;
				if (update.bottom > sharedRect.bottom)
					update.bottom = sharedRect.bottom;
				if (update.top < sharedRect.top)
					update.top = sharedRect.top;

				// Set the update-wanted flag to true
				m_client->m_updatewanted = TRUE;

				// Clip the rectangle to the screen
				if (IntersectRect(&update, &update, &sharedRect))
				{
					// Is this request for an incremental region?
					if (bIncremental)
					{
						// Yes, so add it to the incremental region
						m_client->m_incr_rgn.AddRect(update);
					}
					else
					{
						// No, so add it to the full update region
						m_client->m_full_rgn.AddRect(update);

						// Disable any pending CopyRect
						m_client->m_copyrect_set = FALSE;

						bIncremental = true;
					}
				}

				// Trigger an update
				m_server->RequestUpdate();
			}
		}
	}

	// Move into the thread's original desktop
	vncService::SelectHDESK(home_desktop);

	// Quit this thread.  This will automatically delete the thread and the
	// associated client.
	vnclog.Print(LL_CLIENTS, VNCLOG("client disconnected : %s (id %hd)\n"),
				 m_client->GetClientName(), m_client->GetClientId());

	// Remove the client from the server, just in case!
	m_server->RemoveClient(m_client->GetClientId());

//	vncService::KillRunningCopy();		// Uncommenting this statement will make screencaster close itself when a single client connection
										// closes. This statement still lies here just to serve as a remainder that this can be done.
										// Avoid usage.
}

// The vncClient itself

vncClient::vncClient()
{
	vnclog.Print(LL_INTINFO, VNCLOG("vncClient() executing...\n"));

	m_socket = NULL;
	m_client_name = 0;
	m_server_name = 0;
	m_buffer = NULL;

	m_keyboardenabled = FALSE;
	m_pointerenabled = FALSE;
	m_inputblocked = FALSE;

	m_copyrect_use = FALSE;

	m_mousemoved = FALSE;
	m_ptrevent.buttonMask = 0;
	m_ptrevent.x = 0;
	m_ptrevent.y = 0;

	m_cursor_update_pending = FALSE;
	m_cursor_update_sent = FALSE;
	m_cursor_pos_changed = FALSE;
	m_pointer_event_time = (time_t)0;
	m_cursor_pos.x = -1;
	m_cursor_pos.y = -1;

	m_thread = NULL;
	m_updatewanted = FALSE;

	m_palettechanged = FALSE;

	m_copyrect_set = FALSE;

	m_remoteevent = FALSE;

	m_bDownloadStarted = FALSE;
	m_bUploadStarted = FALSE;

	// IMPORTANT: Initially, client is not protocol-ready.
	m_protocol_ready = FALSE;
	m_fb_size_changed = FALSE;

	m_use_NewFBSize = FALSE;

}

vncClient::~vncClient()
{
	vnclog.Print(LL_INTINFO, VNCLOG("~vncClient() executing...\n"));

	// We now know the thread is dead, so we can clean up
	if (m_client_name != 0) {
		free(m_client_name);
		m_client_name = 0;
	}
	if (m_server_name != 0) {
		free(m_server_name);
		m_server_name = 0;
	}

	// If we have a socket then kill it
	if (m_socket != NULL)
	{
		vnclog.Print(LL_INTINFO, VNCLOG("deleting socket\n"));

		delete m_socket;
		m_socket = NULL;
	}

	// Kill the screen buffer
	if (m_buffer != NULL)
	{
		vnclog.Print(LL_INTINFO, VNCLOG("deleting buffer\n"));

		delete m_buffer;
		m_buffer = NULL;
	}

}

// Init
BOOL
vncClient::Init(vncServer *server,
				CurlSocket *socket,
				BOOL reverse,
				BOOL shared,
				vncClientId newid)
{
	// Save the server id;
	m_server = server;

	// Save the socket
	m_socket = socket;

	// Save the name/ip of the connecting client
	char *name = m_socket->GetPeerName();
	if (name != 0)
		m_client_name = strdup(name);
	else
		m_client_name = strdup("<unknown>");

	// Save the server name/ip
	name = m_socket->GetSockName();
	if (name != 0)
		m_server_name = strdup(name);
	else
		m_server_name = strdup("<unknown>");

	// Save the client id
	m_id = newid;

	// Spawn the child thread here
	m_thread = new vncClientThread;
	if (m_thread == NULL)
		return FALSE;
	return ((vncClientThread *)m_thread)->Init(this, m_server, m_socket, reverse, shared);

	return FALSE;
}

void
vncClient::Kill()
{
	// Close file transfer
	CloseUndoneFileTransfer();

	// Close the socket
	if (m_socket != NULL)
	{
		m_socket->Close();
		std::wstringstream msg;
		msg << _T("{screencastResult:\"2\"}");
		m_server->NotifyDimdim(msg.str());
	}
}

// Client manipulation functions for use by the server
void
vncClient::SetBuffer(vncBuffer *buffer)
{
	// Until authenticated, the client object has no access
	// to the screen buffer.  This means that there only need
	// be a buffer when there's at least one authenticated client.
	m_buffer = buffer;
}


void
vncClient::TriggerUpdate()
{
	// Lock the updates stored so far
	omni_mutex_lock l(m_regionLock);
	if (!m_protocol_ready)
		return;

	if (m_updatewanted)
	{
		// Check if cursor shape update has to be sent
		m_cursor_update_pending = m_buffer->IsCursorUpdatePending();

		// Send an update if one is waiting
		if (!m_changed_rgn.IsEmpty() ||
			!m_full_rgn.IsEmpty() ||
			m_copyrect_set ||
			m_cursor_update_pending ||
			m_cursor_pos_changed ||
			(m_mousemoved && !m_use_PointerPos))
		{
			// Has the palette changed?
			if (m_palettechanged)
			{
				m_palettechanged = FALSE;
				if (!SendPalette())
					return;
			}

			// Now send the update
			m_updatewanted = !SendUpdate();
		}
	}
}

void
vncClient::UpdateMouse()
{
	if (!m_mousemoved && !m_cursor_update_sent)	{
		omni_mutex_lock l(m_regionLock);

		if (IntersectRect(&m_oldmousepos, &m_oldmousepos, &m_server->GetSharedRect()))
			m_changed_rgn.AddRect(m_oldmousepos);

		m_mousemoved = TRUE;
	} else if (m_use_PointerPos) {
		omni_mutex_lock l(m_regionLock);

		SetCursorPosChanged();
	}
}

void
vncClient::UpdateRect(RECT &rect)
{
	// Add the rectangle to the update region
	if (IsRectEmpty(&rect))
		return;

	omni_mutex_lock l(m_regionLock);

	if (IntersectRect(&rect, &rect, &m_server->GetSharedRect()))
		m_changed_rgn.AddRect(rect);
}

void
vncClient::UpdateRegion(vncRegion &region)
{
	// Merge our current update region with the supplied one
	if (region.IsEmpty())
		return;

	{
		omni_mutex_lock l(m_regionLock);

		// Merge the two
		vncRegion dummy;
		dummy.AddRect(m_server->GetSharedRect());
		region.Intersect(dummy);

		m_changed_rgn.Combine(region);
	}
}

void
vncClient::CopyRect(RECT &dest, POINT &source)
{
	// If copyrect is disabled then just redraw the region!
	if (!m_copyrect_use)
	{
		UpdateRect(dest);
		return;
	}

	{
		omni_mutex_lock l(m_regionLock);

		// Clip the destination to the screen
		RECT destrect;
		if (!IntersectRect(&destrect, &dest, &m_server->GetSharedRect()))
			return;

		// Adjust the source correspondingly
		source.x = source.x + (destrect.left - dest.left);
		source.y = source.y + (destrect.top - dest.top);

		// Work out the source rectangle
		RECT srcrect;

		// Is this a continuation of an earlier window drag?
		if (m_copyrect_set &&
			((source.x == m_copyrect_rect.left) && (source.y == m_copyrect_rect.top)))
		{
			// Yes, so use the old source position
			srcrect.left = m_copyrect_src.x;
			srcrect.top = m_copyrect_src.y;
		}
		else
		{
			// No, so use this source position
			srcrect.left = source.x;
			srcrect.top = source.y;
		}

		// And fill out the right & bottom using the dest rect
		srcrect.right = destrect.right-destrect.left + srcrect.left;
		srcrect.bottom = destrect.bottom-destrect.top + srcrect.top;

		// Clip the source to the screen
		RECT srcrect2;
		if (!IntersectRect(&srcrect2, &srcrect, &m_server->GetSharedRect()))
			return;

		// Correct the destination rectangle
		destrect.left += (srcrect2.left - srcrect.left);
		destrect.top += (srcrect2.top - srcrect.top);
		destrect.right = srcrect2.right-srcrect2.left + destrect.left;
		destrect.bottom = srcrect2.bottom-srcrect2.top + destrect.top;

		// Is there an existing CopyRect rectangle?
		if (m_copyrect_set)
		{
			// Yes, so compare their areas!
			if (((destrect.right-destrect.left) * (destrect.bottom-destrect.top))
				< ((m_copyrect_rect.right-m_copyrect_rect.left) * (m_copyrect_rect.bottom-m_copyrect_rect.top)))
				return;
		}

		// Set the copyrect...
		m_copyrect_rect = destrect;
		m_copyrect_src.x = srcrect2.left;
		m_copyrect_src.y = srcrect2.top;

		m_copyrect_set = TRUE;
	}
}

void
vncClient::UpdateClipText(LPSTR text)
{
	if (!m_protocol_ready) return;

	// Don't send the clipboard contents to a view-only client
	if (!IsKeyboardEnabled() || !IsPointerEnabled())
		return;

	// Lock out any update sends and send clip text to the client
	omni_mutex_lock l(m_sendUpdateLock);

	rfbServerCutTextMsg message;
	message.length = Swap32IfLE(strlen(text));
	if (!SendRFBMsg(rfbServerCutText, (BYTE *) &message, sizeof(message)))
	{
		Kill();
		return;
	}
	if (!m_socket->SendQueued(text, strlen(text)))
	{
		Kill();
		return;
	}
}

void
vncClient::UpdatePalette()
{
	omni_mutex_lock l(m_regionLock);

	m_palettechanged = TRUE;
}

// Functions used to set and retrieve the client settings
const char*
vncClient::GetClientName()
{
	return (m_client_name != NULL) ? m_client_name : "[unknown]";
}

const char*
vncClient::GetServerName()
{
	return (m_server_name != NULL) ? m_server_name : "[unknown]";
}

// Internal methods
BOOL
vncClient::SendRFBMsg(CARD8 type, BYTE *buffer, int buflen)
{
	// Set the message type
	((rfbServerToClientMsg *)buffer)->type = type;

	// Send the message
	if (!m_socket->SendQueued((char *) buffer, buflen))
	{
		Kill();
		return FALSE;
	}
	return TRUE;
}


BOOL vncClient::SendUpdate()
{
#ifndef _DEBUG
	try
	{
#endif
		// First, check if we need to send pending NewFBSize message
		if (m_use_NewFBSize && m_fb_size_changed) {
			SetNewFBSize(TRUE);
			return TRUE;
		}

		vncRegion toBeSent;			// Region to actually be sent
		rectlist toBeSentList;		// List of rectangles to actually send
		vncRegion toBeDone;			// Region to check

		// Prepare to send cursor position update if necessary
		if (m_cursor_pos_changed) {
			POINT cursor_pos;
			if (!GetCursorPos(&cursor_pos)) {
				cursor_pos.x = 0;
				cursor_pos.y = 0;
			}
			RECT shared_rect = m_server->GetSharedRect();
			cursor_pos.x -= shared_rect.left;
			cursor_pos.y -= shared_rect.top;
			if (cursor_pos.x < 0) {
				cursor_pos.x = 0;
			} else if (cursor_pos.x >= shared_rect.right - shared_rect.left) {
				cursor_pos.x = shared_rect.right - shared_rect.left - 1;
			}
			if (cursor_pos.y < 0) {
				cursor_pos.y = 0;
			} else if (cursor_pos.y >= shared_rect.bottom - shared_rect.top) {
				cursor_pos.y = shared_rect.bottom - shared_rect.top - 1;
			}
			if (cursor_pos.x == m_cursor_pos.x && cursor_pos.y == m_cursor_pos.y) {
				m_cursor_pos_changed = FALSE;
			} else {
				m_cursor_pos.x = cursor_pos.x;
				m_cursor_pos.y = cursor_pos.y;
			}
		}

		toBeSent.Clear();
		if (!m_full_rgn.IsEmpty()) {
			m_incr_rgn.Clear();
			m_copyrect_set = false;
			toBeSent.Combine(m_full_rgn);
			m_changed_rgn.Clear();
			m_full_rgn.Clear();
		} else {
			if (!m_incr_rgn.IsEmpty()) {
				// Get region to send from vncDesktop
				toBeSent.Combine(m_changed_rgn);

				// Mouse stuff for the case when cursor shape updates are off
				if (!m_cursor_update_sent && !m_cursor_update_pending) {
					// If the mouse hasn't moved, see if its position is in the rect
					// we're sending. If so, make sure the full mouse rect is sent.
					if (!m_mousemoved) {
						vncRegion tmpMouseRgn;
						tmpMouseRgn.AddRect(m_oldmousepos);
						tmpMouseRgn.Intersect(toBeSent);
						if (!tmpMouseRgn.IsEmpty()) 
							m_mousemoved = TRUE;
					}
					// If the mouse has moved (or otherwise needs an update):
					if (m_mousemoved) {
						// Include an update for its previous position
						if (IntersectRect(&m_oldmousepos, &m_oldmousepos, &m_server->GetSharedRect())) 
							toBeSent.AddRect(m_oldmousepos);
						// Update the cached mouse position
						m_oldmousepos = m_buffer->GrabMouse();
						// Include an update for its current position
						if (IntersectRect(&m_oldmousepos, &m_oldmousepos, &m_server->GetSharedRect())) 
							toBeSent.AddRect(m_oldmousepos);
						// Indicate the move has been handled
						m_mousemoved = FALSE;
					}
				}
				m_changed_rgn.Clear();
			}
		}

		// Get the list of changed rectangles!
		int numrects = 0;
		if (toBeSent.Rectangles(toBeSentList))
		{
			// Find out how many rectangles this update will contain
			rectlist::iterator i;
			int numsubrects;
			for (i=toBeSentList.begin(); i != toBeSentList.end(); i++)
			{
				numsubrects = m_buffer->GetNumCodedRects(*i);

				// Skip remaining rectangles if an encoder will use LastRect extension.
				if (numsubrects == 0) {
					numrects = 0xFFFF;
					break;
				}
				numrects += numsubrects;
			}
		}

		if (numrects != 0xFFFF) {
			// Count cursor shape and cursor position updates.
			if (m_cursor_update_pending)
				numrects++;
			if (m_cursor_pos_changed)
				numrects++;
			// Handle the copyrect region
			if (m_copyrect_set)
				numrects++;
			// If there are no rectangles then return
			if (numrects != 0)
				m_incr_rgn.Clear();
			else
				return FALSE;
		}

		if (numrects == 0)
		{
			vnclog.Print(LL_DIMDIM, "Detected zero rects. Skipping update\n");
			return TRUE;	// Nothing to be done here.
		}

	//	vnclog.Print(LL_DIMDIM, "Entering SendUpdate\r\n");

		omni_mutex_lock l(m_sendUpdateLock);

		// Otherwise, send <number of rectangles> header
		rfbFramebufferUpdateMsg header;
		header.nRects = Swap16IfLE(numrects);
		if (!SendRFBMsg(rfbFramebufferUpdate, (BYTE *) &header, sz_rfbFramebufferUpdateMsg))
			return TRUE;

		// Send mouse cursor shape update
		if (m_cursor_update_pending) {
			if (!SendCursorShapeUpdate())
				return TRUE;
		}

		// Send cursor position update
		if (m_cursor_pos_changed) {
			if (!SendCursorPosUpdate())
				return TRUE;
		}

		// Encode & send the copyrect
		if (m_copyrect_set) {
			m_copyrect_set = FALSE;
			if(!SendCopyRect(m_copyrect_rect, m_copyrect_src))
				return TRUE;
		}

		// Encode & send the actual rectangles
		if (!SendRectangles(toBeSentList))
			return TRUE;

		// Send LastRect marker if needed.
		if (numrects == 0xFFFF) {
			if (!SendLastRect())
				return TRUE;
		}

	//	vnclog.Print(LL_DIMDIM, "Leaving SendUpdate\r\n");

		// Both lists should be empty when we exit
		_ASSERT(toBeSentList.empty());
#ifndef _DEBUG
	}
	catch (...)
	{
		vnclog.Print(LL_INTERR, VNCLOG("vncClient::SendUpdate caught an exception.\n"));
		throw;
	}
#endif

	return TRUE;
}

// Send a set of rectangles
BOOL
vncClient::SendRectangles(rectlist &rects)
{
	RECT rect;

	// Work through the list of rectangles, sending each one
	while(!rects.empty())
	{
		rect = rects.front();
		if (!SendRectangle(rect))
			return FALSE;

		rects.pop_front();
	}
	rects.clear();
	
	return TRUE;
}

// Tell the encoder to send a single rectangle
BOOL vncClient::SendRectangle(RECT &rect)
{
	RECT sharedRect;
	{
		omni_mutex_lock l(m_regionLock);
		sharedRect = m_server->GetSharedRect();
	}

	IntersectRect(&rect, &rect, &sharedRect);
	// Get the buffer to encode the rectangle
	UINT bytes = m_buffer->TranslateRect(
		rect,
		m_socket,
		sharedRect.left,
		sharedRect.top);


    // Send the encoded data
    return m_socket->SendQueued((char *)(m_buffer->GetClientBuffer()), bytes);
}

// Send a single CopyRect message
BOOL vncClient::SendCopyRect(RECT &dest, POINT &source)
{
	RECT rc_shr = m_server->GetSharedRect();

	// Create the message header
	rfbFramebufferUpdateRectHeader copyrecthdr;
	copyrecthdr.r.x = Swap16IfLE(dest.left - rc_shr.left);
	copyrecthdr.r.y = Swap16IfLE(dest.top - rc_shr.top);

	copyrecthdr.r.w = Swap16IfLE(dest.right-dest.left);
	copyrecthdr.r.h = Swap16IfLE(dest.bottom-dest.top);
	copyrecthdr.encoding = Swap32IfLE(rfbEncodingCopyRect);

	// Create the CopyRect-specific section
	rfbCopyRect copyrectbody;
	copyrectbody.srcX = Swap16IfLE(source.x - rc_shr.left);
	copyrectbody.srcY = Swap16IfLE(source.y - rc_shr.top);

	// Now send the message;
	if (!m_socket->SendQueued((char *)&copyrecthdr, sizeof(copyrecthdr)))
		return FALSE;
	if (!m_socket->SendQueued((char *)&copyrectbody, sizeof(copyrectbody)))
		return FALSE;

	return TRUE;
}

// Send LastRect marker indicating that there are no more rectangles to send
BOOL
vncClient::SendLastRect()
{
	// Create the message header
	rfbFramebufferUpdateRectHeader hdr;
	hdr.r.x = 0;
	hdr.r.y = 0;
	hdr.r.w = 0;
	hdr.r.h = 0;
	hdr.encoding = Swap32IfLE(rfbEncodingLastRect);

	// Now send the message;
	if (!m_socket->SendQueued((char *)&hdr, sizeof(hdr)))
		return FALSE;

	m_lastRectCount += 1;

	return TRUE;
}

// Send the encoder-generated palette to the client
// This function only returns FALSE if the SendQueued fails - any other
// error is coped with internally...
BOOL
vncClient::SendPalette()
{
	rfbSetColourMapEntriesMsg setcmap;
	RGBQUAD *rgbquad;
	UINT ncolours = 256;

	// Reserve space for the colour data
	rgbquad = new RGBQUAD[ncolours];
	if (rgbquad == NULL)
		return TRUE;

	// Get the data
	if (!m_buffer->GetRemotePalette(rgbquad, ncolours))
	{
		delete [] rgbquad;
		return TRUE;
	}

	// Compose the message
	omni_mutex_lock l(m_sendUpdateLock);

	setcmap.type = rfbSetColourMapEntries;
	setcmap.firstColour = Swap16IfLE(0);
	setcmap.nColours = Swap16IfLE(ncolours);

	if (!m_socket->SendQueued((char *) &setcmap, sz_rfbSetColourMapEntriesMsg))
	{
		delete [] rgbquad;
		return FALSE;
	}

	// Now send the actual colour data...
	for (UINT i=0; i<ncolours; i++)
	{
		struct _PIXELDATA {
			CARD16 r, g, b;
		} pixeldata;

		pixeldata.r = Swap16IfLE(((CARD16)rgbquad[i].rgbRed) << 8);
		pixeldata.g = Swap16IfLE(((CARD16)rgbquad[i].rgbGreen) << 8);
		pixeldata.b = Swap16IfLE(((CARD16)rgbquad[i].rgbBlue) << 8);

		if (!m_socket->SendQueued((char *) &pixeldata, sizeof(pixeldata)))
		{
			delete [] rgbquad;
			return FALSE;
		}
	}

	// Delete the rgbquad data
	delete [] rgbquad;

	return TRUE;
}

BOOL
vncClient::SendCursorShapeUpdate()
{
	m_cursor_update_pending = FALSE;

	if (!m_buffer->SendCursorShape(m_socket)) {
		m_cursor_update_sent = FALSE;

		return m_buffer->SendEmptyCursorShape(m_socket);
	}

	m_cursor_update_sent = TRUE;
	return TRUE;
}

BOOL
vncClient::SendCursorPosUpdate()
{
	m_cursor_pos_changed = FALSE;

	rfbFramebufferUpdateRectHeader hdr;
	hdr.encoding = Swap32IfLE(rfbEncodingPointerPos);
	hdr.r.x = Swap16IfLE(m_cursor_pos.x);
	hdr.r.y = Swap16IfLE(m_cursor_pos.y);
	hdr.r.w = Swap16IfLE(0);
	hdr.r.h = Swap16IfLE(0);

	return m_socket->SendQueued((char *)&hdr, sizeof(hdr));
}

// Send NewFBSize pseudo-rectangle to notify the client about
// framebuffer size change
BOOL
vncClient::SetNewFBSize(BOOL sendnewfb)
{
	rfbFramebufferUpdateRectHeader hdr;
	RECT sharedRect;

	sharedRect = m_server->GetSharedRect();
	// This may be required for application share. // Change this
//	if (sharedRect.bottom + sharedRect.left + sharedRect.right + sharedRect.top == 0)
//		return TRUE;

	m_full_rgn.Clear();
	m_incr_rgn.Clear();
	m_full_rgn.AddRect(sharedRect);

	if (!m_use_NewFBSize) {
		// We cannot send NewFBSize message right now, maybe later
		m_fb_size_changed = TRUE;

	} else if (sendnewfb) {
		hdr.r.x = 0;
		hdr.r.y = 0;
		hdr.r.w = Swap16IfLE(sharedRect.right - sharedRect.left);
		hdr.r.h = Swap16IfLE(sharedRect.bottom - sharedRect.top);
		hdr.encoding = Swap32IfLE(rfbEncodingNewFBSize);

		rfbFramebufferUpdateMsg header;
		header.nRects = Swap16IfLE(1);
		if (!SendRFBMsg(rfbFramebufferUpdate, (BYTE *)&header,
			sz_rfbFramebufferUpdateMsg))
            return FALSE;

		// Now send the message
		if (!m_socket->SendQueued((char *)&hdr, sizeof(hdr)))
			return FALSE;

		// No pending NewFBSize anymore
		m_fb_size_changed = FALSE;
	}

	return TRUE;
}

void
vncClient::UpdateLocalFormat()
{
	m_buffer->UpdateLocalFormat();
}

char * 
vncClientThread::ConvertPath(char *path)
{
	int len = strlen(path);
	if(len >= 255) return path;
	if((path[0] == '/') && (len == 1)) {path[0] = '\0'; return path;}
	for(int i = 0; i < (len - 1); i++) {
		if(path[i+1] == '/') path[i+1] = '\\';
		path[i] = path[i+1];
	}
	path[len-1] = '\0';
	return path;
}

void 
vncClient::SendFileUploadCancel(unsigned short reasonLen, char *reason)
{
	omni_mutex_lock l(m_sendUpdateLock);

	int msgLen = sz_rfbFileUploadCancelMsg + reasonLen;
	char *pAllFUCMessage = new char[msgLen];
	rfbFileUploadCancelMsg *pFUC = (rfbFileUploadCancelMsg *) pAllFUCMessage;
	char *pFollow = &pAllFUCMessage[sz_rfbFileUploadCancelMsg];
	pFUC->type = rfbFileUploadCancel;
	pFUC->reasonLen = Swap16IfLE(reasonLen);
	memcpy(pFollow, reason, reasonLen);
	m_socket->SendExact(pAllFUCMessage, msgLen);
	delete [] pAllFUCMessage;
}

void 
vncClient::Time70ToFiletime(unsigned int mTime, FILETIME *pFiletime)
{
	LONGLONG ll = Int32x32To64(mTime, 10000000) + 116444736000000000;
	pFiletime->dwLowDateTime = (DWORD) ll;
	pFiletime->dwHighDateTime = (DWORD)(ll >> 32);
}

void 
vncClient::SendFileDownloadFailed(unsigned short reasonLen, char *reason)
{
	omni_mutex_lock l(m_sendUpdateLock);

	int msgLen = sz_rfbFileDownloadFailedMsg + reasonLen;
	char *pAllFDFMessage = new char[msgLen];
	rfbFileDownloadFailedMsg *pFDF = (rfbFileDownloadFailedMsg *) pAllFDFMessage;
	char *pFollow = &pAllFDFMessage[sz_rfbFileDownloadFailedMsg];
	pFDF->type = rfbFileDownloadFailed;
	pFDF->reasonLen = Swap16IfLE(reasonLen);
	memcpy(pFollow, reason, reasonLen);
	m_socket->SendExact(pAllFDFMessage, msgLen);
	delete [] pAllFDFMessage;
}

void 
vncClient::SendFileDownloadData(unsigned int mTime)
{
	omni_mutex_lock l(m_sendUpdateLock);

	int msgLen = sz_rfbFileDownloadDataMsg + sizeof(unsigned int);
	char *pAllFDDMessage = new char[msgLen];
	rfbFileDownloadDataMsg *pFDD = (rfbFileDownloadDataMsg *) pAllFDDMessage;
	unsigned int *pFollow = (unsigned int *) &pAllFDDMessage[sz_rfbFileDownloadDataMsg];
	pFDD->type = rfbFileDownloadData;
	pFDD->compressLevel = 0;
	pFDD->compressedSize = Swap16IfLE(0);
	pFDD->realSize = Swap16IfLE(0);
	memcpy(pFollow, &mTime, sizeof(unsigned int));
	m_socket->SendExact(pAllFDDMessage, msgLen);
	delete [] pAllFDDMessage;
}

void
vncClient::SendFileDownloadPortion()
{
	if (!m_bDownloadStarted) return;
	DWORD dwNumberOfBytesRead = 0;
	m_rfbBlockSize = 8192;
	char *pBuff = new char[m_rfbBlockSize];
	BOOL bResult = ReadFile(m_hFileToRead, pBuff, m_rfbBlockSize, &dwNumberOfBytesRead, NULL);
	if ((bResult) && (dwNumberOfBytesRead == 0)) {
		/* This is the end of the file. */
		SendFileDownloadData(m_modTime);
		vnclog.Print(LL_CLIENTS, VNCLOG("file download complete: %s\n"), m_DownloadFilename);
		CloseHandle(m_hFileToRead);
		m_bDownloadStarted = FALSE;
		return;
	}
	SendFileDownloadData((unsigned short)dwNumberOfBytesRead, pBuff);
	delete [] pBuff;
	PostToWinVNC(fileTransferDownloadMessage, (WPARAM) this, (LPARAM) 0);
}

void 
vncClient::SendFileDownloadData(unsigned short sizeFile, char *pFile)
{
	omni_mutex_lock l(m_sendUpdateLock);

	int msgLen = sz_rfbFileDownloadDataMsg + sizeFile;
	char *pAllFDDMessage = new char[msgLen];
	rfbFileDownloadDataMsg *pFDD = (rfbFileDownloadDataMsg *) pAllFDDMessage;
	char *pFollow = &pAllFDDMessage[sz_rfbFileDownloadDataMsg];
	pFDD->type = rfbFileDownloadData;
	pFDD->compressLevel = 0;
	pFDD->compressedSize = Swap16IfLE(sizeFile);
	pFDD->realSize = Swap16IfLE(sizeFile);
	memcpy(pFollow, pFile, sizeFile);
	m_socket->SendExact(pAllFDDMessage, msgLen);
	delete [] pAllFDDMessage;

}

unsigned int 
vncClient::FiletimeToTime70(FILETIME filetime)
{
	LARGE_INTEGER uli;
	uli.LowPart = filetime.dwLowDateTime;
	uli.HighPart = filetime.dwHighDateTime;
	uli.QuadPart = (uli.QuadPart - 116444736000000000) / 10000000;
	return uli.LowPart;
}

void
vncClient::CloseUndoneFileTransfer()
{
	if (m_bUploadStarted) {
		m_bUploadStarted = FALSE;
		CloseHandle(m_hFileToWrite);
		DeleteFile(m_UploadFilename);
	}
	if (m_bDownloadStarted) {
		m_bDownloadStarted = FALSE;
		CloseHandle(m_hFileToRead);
	}
}
