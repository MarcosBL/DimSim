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
import com.dimdim.streaming.StreamingServerConstants;
import com.dimdim.streaming.StreamingServerAdapter;
import com.dimdim.streaming.StreamingServerAdapterProvider;
import com.dimdim.conference.model.IStreamingServer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class StreamingServerInterface	implements	IStreamingServer
{
	protected	String		confKey;
	protected	StreamingServerAdapter	streamingServerAdapter;
	
	protected	StreamingServer		dtpStreamingServer;
	protected	StreamingServer		avStreamingServer;
	protected	StreamingServer		audioStreamingServer;
	protected	StreamingServer		whiteboardStreamingServer;
	
	protected	boolean		avServerSeperate = false;
	protected	boolean		audioServerSeperate = false;
	
	public	StreamingServerInterface(String confKey)
		throws	StreamingServerTooBusy
	{
		this.confKey = confKey;
		this.streamingServerAdapter = StreamingServerAdapterProvider.getAdapterProvider().getAvailableAdapter();
		this.dtpStreamingServer = this.streamingServerAdapter.
				getAvailableServer(StreamingServerConstants.SCREEN_SHARE_STREAM);
		if (this.dtpStreamingServer == null)
		{
			throw	new	StreamingServerTooBusy();
		}
		
		this.avStreamingServer = this.streamingServerAdapter.
				getAvailableServer(StreamingServerConstants.AUDIO_VIDEO_STREAM);
		if (this.avStreamingServer == null)
		{
			throw	new	StreamingServerTooBusy();
		}
		else if (this.avStreamingServer != this.dtpStreamingServer)
		{
			this.avServerSeperate = true;
		}
		
		this.audioStreamingServer = this.streamingServerAdapter.
				getAvailableServer(StreamingServerConstants.AUDIO_STREAM);
		if (this.audioStreamingServer == null)
		{
			throw	new	StreamingServerTooBusy();
		}
		else if ((this.avStreamingServer != this.audioStreamingServer) &&
					(this.dtpStreamingServer != this.audioStreamingServer))
		{
			this.audioServerSeperate = true;
		}
		
		this.whiteboardStreamingServer = this.streamingServerAdapter.
				getAvailableServer(StreamingServerConstants.WHITEBOARD_STREAM);
	}
	public	void	conferenceStarted(int maxParticipants, int maxDurationMinutes)
	{
		this.dtpStreamingServer.meetingStarted(this.confKey, maxParticipants, maxDurationMinutes);
		if (this.avServerSeperate)
		{
			this.avStreamingServer.meetingStarted(this.confKey, maxParticipants, maxDurationMinutes);
		}
		if (this.audioServerSeperate)
		{
			this.audioStreamingServer.meetingStarted(this.confKey, maxParticipants, maxDurationMinutes);
		}
		if (this.whiteboardStreamingServer != null)
		{
			this.whiteboardStreamingServer.meetingStarted(this.confKey, maxParticipants, maxDurationMinutes);
		}
	}
	public	void	conferenceClosed()
	{
		this.dtpStreamingServer.meetingClosed(this.confKey);
		if (this.avServerSeperate)
		{
			this.avStreamingServer.meetingClosed(this.confKey);
		}
		if (this.audioServerSeperate)
		{
			this.audioStreamingServer.meetingClosed(this.confKey);
		}
		if (this.whiteboardStreamingServer != null)
		{
			this.whiteboardStreamingServer.meetingClosed(this.confKey);
		}
	}
	
	/**
	 * Get the urls from respective streaming servers.
	 * 
	 * @return
	 */
	public	String	getDtpRtmpUrl()
	{
		return	this.dtpStreamingServer.getRTMPUrl(this.confKey);
	}
	public	String	getDtpRtmptUrl()
	{
		return	this.dtpStreamingServer.getRTMPTUrl(this.confKey);
	}
	public	String	getAvRtmpUrl()
	{
		return	this.avStreamingServer.getRTMPUrl(this.confKey);
	}
	public	String	getAvRtmptUrl()
	{
		return	this.avStreamingServer.getRTMPTUrl(this.confKey);
	}
	public	String	getAudioRtmpUrl()
	{
		return	this.audioStreamingServer.getRTMPUrl(this.confKey);
	}
	public	String	getAudioRtmptUrl()
	{
		return	this.audioStreamingServer.getRTMPTUrl(this.confKey);
	}
	public String getAVApplicationStreamsDirectory()
	{
		return this.avStreamingServer.getAVApplicationStreamsDirectory();
	}
	public	String	getWhiteboardRtmpUrl()
	{
		if (this.whiteboardStreamingServer != null)
		{
			return	this.whiteboardStreamingServer.getRTMPUrl(this.confKey);
		}
		return	null;
	}
	public	String	getWhiteboardRtmptUrl()
	{
		if (this.whiteboardStreamingServer != null)
		{
			return	this.whiteboardStreamingServer.getRTMPTUrl(this.confKey);
		}
		return	null;
	}
	
	/**
	 * Following methods report the relevant events to the appropriate streaming
	 * server.
	 * 
	 * @param participantId
	 */
	public	void	participantJoined(String participantId)
	{
		this.dtpStreamingServer.participantJoined(this.confKey, participantId);
		if (this.avServerSeperate)
		{
			this.avStreamingServer.participantJoined(this.confKey,participantId);
		}
		if (this.audioServerSeperate)
		{
			this.audioStreamingServer.participantJoined(this.confKey,participantId);
		}
	}
	public	void	participantLeft(String participantId)
	{
		this.dtpStreamingServer.participantLeft(this.confKey,participantId);
		if (this.avServerSeperate)
		{
			this.avStreamingServer.participantLeft(this.confKey,participantId);
		}
		if (this.audioServerSeperate)
		{
			this.audioStreamingServer.participantLeft(this.confKey,participantId);
		}
	}
	public	void	broadcasterStarted(String participantId, String streamType)
	{
		if (streamType.equals(StreamingServerConstants.SCREEN_SHARE_STREAM))
		{
			this.dtpStreamingServer.broadcasterStarted(this.confKey, participantId, streamType);
		}
		else if (streamType.equals(StreamingServerConstants.AUDIO_VIDEO_STREAM))
		{
			this.avStreamingServer.broadcasterStarted(this.confKey, participantId, streamType);
		}
		else if (streamType.equals(StreamingServerConstants.AUDIO_STREAM))
		{
			this.audioStreamingServer.broadcasterStarted(this.confKey, participantId, streamType);
		}
	}
	public	void	broadcasterStopped(String participantId, String streamType)
	{
		if (streamType.equals(StreamingServerConstants.SCREEN_SHARE_STREAM))
		{
			this.dtpStreamingServer.broadcasterStopped(this.confKey, participantId, streamType);
		}
		else if (streamType.equals(StreamingServerConstants.AUDIO_VIDEO_STREAM))
		{
			this.avStreamingServer.broadcasterStopped(this.confKey, participantId, streamType);
		}
		else if (streamType.equals(StreamingServerConstants.AUDIO_STREAM))
		{
			this.audioStreamingServer.broadcasterStopped(this.confKey, participantId, streamType);
		}
	}
}
