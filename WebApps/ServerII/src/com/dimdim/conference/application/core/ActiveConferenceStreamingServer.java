/*
 **************************************************************************
 *                                                                        *
 *               DDDDD   iii             DDDDD   iii                      *
 *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           *
 *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
 *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
 *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
 *                                                                        *
 **************************************************************************
 **************************************************************************
 *                                                                        *
 * Part of the DimDim V 1.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.application.core;

import com.dimdim.streaming.StreamingServer;
import com.dimdim.streaming.StreamingServerAdapter;
import com.dimdim.streaming.StreamingServerConstants;
import com.dimdim.streaming.StreamingServerAdapterProvider;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Objects of this class represents the streaming server used by a single
 * conference. The streaing servers are shared by other conferences. The limit
 * of streams on the streaming server is checked only while creating a new
 * conference. Not while creating individual streams.
 * 
 * This class does the work of managing different streaming servers for a
 * single conference if such a scheme is ever employed by the conference
 * server.
 */

public class ActiveConferenceStreamingServer
{
	protected	String		confKey;
	protected	StreamingServerAdapter	streamingServerAdapter;
	protected	StreamingServer		streamingServer;
	
	private	ActiveConferenceStreamingServer(String confKey)
		throws	StreamingServerTooBusy
	{
		this.confKey = confKey;
		this.streamingServerAdapter = StreamingServerAdapterProvider.getAdapterProvider().getAvailableAdapter();
		this.streamingServer = this.streamingServerAdapter.getAvailableServer();
		if (this.streamingServer == null)
		{
			throw	new	StreamingServerTooBusy();
		}
	}
	public	void	meetingStarted()
	{
		this.streamingServer.meetingStarted(this.confKey, 20, 60);
	}
	public	void	conferenceClosed()
	{
		this.streamingServer.meetingClosed(this.confKey);
	}
//	public	String	getRtmpUrl()
//	{
//		return	this.streamingServer.getRTMPUrl();
//	}
//	public	String	getRtmptUrl()
//	{
//		return	this.streamingServer.getRTMPTUrl();
//	}
	public	void	participantJoined(String participantId)
	{
		this.streamingServer.participantJoined(this.confKey, participantId);
	}
	public	void	participantLeft(String participantId)
	{
		this.streamingServer.participantLeft(this.confKey,participantId);
	}
	public	void	broadcasterStarted(String participantId, String streamType)
	{
		this.streamingServer.broadcasterStarted(this.confKey, participantId, streamType);
	}
	public	void	broadcasterStopped(String participantId, String streamType)
	{
		this.streamingServer.broadcasterStopped(this.confKey, participantId, streamType);
	}
}
