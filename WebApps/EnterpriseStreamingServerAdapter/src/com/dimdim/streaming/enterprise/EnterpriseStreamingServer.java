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

package com.dimdim.streaming.enterprise;

import java.util.HashMap;
import java.util.Vector;

import com.dimdim.streaming.StreamingServer;
import com.dimdim.streaming.common.MeetingStreams;
import com.dimdim.streaming.common.ParticipantStreams;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * SA stands for standalone. This essentially means that the server has fixed
 * url. It does not require the knowledge of the meeting information for the
 * streaming urls.
 */

public class EnterpriseStreamingServer implements StreamingServer
{
	protected	HashMap		meetingStreams;
	
	protected	String		rtmpUrl;
	protected	String		rtmptUrl;
	protected	String		avApplicationStreamsDirectory;
	
	protected	int			maxNumberOfStreams;
	protected	Vector		supportedStreamTypes;
	
	protected	int		currentNumberOfStreams;
	
	public	EnterpriseStreamingServer(String rtmpUrl, String rtmptUrl,
				String avApplicationStreamsDirectory,
				int maxNumberOfStreams, Vector supportedStreamTypes)
	{
		this.rtmpUrl = rtmpUrl;
		this.rtmptUrl = rtmptUrl;
		this.avApplicationStreamsDirectory = avApplicationStreamsDirectory;
		this.maxNumberOfStreams = maxNumberOfStreams;
		this.supportedStreamTypes = supportedStreamTypes;
		
		this.meetingStreams = new HashMap();
	}
	public int getAvailableStreams()
	{
		if (this.maxNumberOfStreams < 0)
		{
			return	10;
		}
		return (this.maxNumberOfStreams - this.currentNumberOfStreams);
	}
	public int getMaxStreams()
	{
		return this.maxNumberOfStreams;
	}
	public String getAVApplicationStreamsDirectory()
	{
		return this.avApplicationStreamsDirectory;
	}
	/**
	 * This adapter does not use different urls for different meetings.
	 */
	public String getRTMPUrl(String meetingKey)
	{
		return this.rtmpUrl;
	}
	public String getRTMPTUrl(String meetingKey)
	{
		return this.rtmptUrl;
	}
	
	public boolean isStreamAvailable()
	{
		if (this.maxNumberOfStreams < 0)
		{
			return	true;
		}
		return (this.currentNumberOfStreams < this.maxNumberOfStreams);
	}
	public void meetingClosed(String meetingKey)
	{
		MeetingStreams ms = (MeetingStreams)this.meetingStreams.get(meetingKey);
		if (ms != null)
		{
			this.meetingStreams.remove(meetingKey);
			this.currentNumberOfStreams -= ms.getTotalNumberOfStreams();
		}
	}
	public void meetingStarted(String meetingKey,
			int maxExpectedParticipants, int maxExpectedMeetingLengthMinutes)
	{
		MeetingStreams ms = (MeetingStreams)this.meetingStreams.get(meetingKey);
		if (ms == null)
		{
			this.meetingStreams.put(meetingKey, new MeetingStreams(meetingKey));
		}
	}
	public void participantJoined(String meetingKey, String participantId)
	{
		MeetingStreams ms = (MeetingStreams)this.meetingStreams.get(meetingKey);
		if (ms != null)
		{
			ms.addParticipant(participantId);
			this.currentNumberOfStreams += 1;
		}
	}
	public void participantLeft(String meetingKey, String participantId)
	{
		MeetingStreams ms = (MeetingStreams)this.meetingStreams.get(meetingKey);
		if (ms != null)
		{
			ms.removeParticipant(participantId);
			this.currentNumberOfStreams -= 1;
		}
	}
	public	void	broadcasterStarted(String meetingKey, String participantId, String streamId)
	{
		//	TODO. Counting streams based on the user activity is going to be a bit
		//	more complicated.
	}
	public	void	broadcasterStopped(String meetingKey, String participantId, String streamId)
	{
		//	TODO. Counting streams based on the user activity is going to be a bit
		//	more complicated.
	}
	public boolean supportsStreamType(String streamType)
	{
		if (this.supportedStreamTypes != null &&
					this.supportedStreamTypes.size() > 0)
		{
			if (this.supportedStreamTypes.contains(streamType) ||
						this.supportedStreamTypes.contains(ANY_STREAM))
			{
				return	true;
			}
		}
		return false;
	}
}
