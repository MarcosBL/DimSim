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

package com.dimdim.conference.model;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class StreamControlEvent  extends	MeetingEventData	implements IJsonSerializable
{
	public	static	String	START	=	"start";
	public	static	String	STOP	=	"stop";
	public	static	String	VOICE	=	"voice";
	public	static	String	CHANGE	=	"change";
	
	protected	String	conferenceKey;
	protected	String	resourceId;
	protected	String	resourceType;
	protected	String	eventType;
	protected	String	streamType;
	protected	String	streamName;
	protected	String	width;
	protected	String	height;
	protected	String 	profile;
	
	public	static	StreamControlEvent
		createStartEvent(String confKey, String presenterId, String resourceId, String resourceType,
				String streamType, String streamName, String width, String height, String profile)
	{
		return	new	StreamControlEvent(confKey, presenterId, resourceId, resourceType,
				StreamControlEvent.START, streamType, streamName, width, height, profile);
	}
	public	static	StreamControlEvent
		createStopEvent(String confKey, String presenterId, String resourceId, String resourceType,
				String streamType, String streamName, String width, String height, String profile)
	{
		return	new	StreamControlEvent(confKey, presenterId, resourceId, resourceType,
				StreamControlEvent.STOP, streamType, streamName, width, height, profile);
	}
	public	static	StreamControlEvent
		createVoiceEvent(String confKey, String presenterId, String resourceId, String resourceType,
				String streamType, String streamName, String width, String height, String profile)
	{
		return	new	StreamControlEvent(confKey, presenterId, resourceId, resourceType,
				StreamControlEvent.VOICE, streamType, streamName, width, height, profile);
	}
	public	static	StreamControlEvent
		createChangeEvent(String confKey, String presenterId, String resourceId)
	{
		return	new	StreamControlEvent(confKey, presenterId, resourceId, "na",
				StreamControlEvent.CHANGE, "na", "na", "200", "200", "3");
	}
	public	static	StreamControlEvent
		createControlEvent(String confKey, String presenterId, String resourceId, String resourceType,
				String streamType, String streamName, String width, String height, String profile, String event)
	{
		return	new	StreamControlEvent(confKey, presenterId, resourceId, resourceType,
				event, streamType, streamName, width, height, profile);
	}
	
//	public StreamControlEvent()
//	{
//	}
	public StreamControlEvent(String conferenceKey, String presenterId, String resourceId, String resourceType,
			String eventType, String streamType, String streamName,
			String width, String height, String profile)
	{
		super(presenterId);
		this.conferenceKey = conferenceKey;
		this.resourceId = resourceId;
		this.resourceType = resourceType;
		this.eventType = eventType;
		this.streamType = streamType;
		this.streamName = streamName;
		this.width = width;
		this.height = height;
		this.profile = profile;
	}
	
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("StreamControlEvent"); buf.append("\",");
		buf.append( "\"conferenceKey\":\""); buf.append(this.conferenceKey); buf.append("\",");
		buf.append( "\"resourceId\":\""); buf.append(this.resourceId); buf.append("\",");
		buf.append( "\"resourceType\":\""); buf.append(this.resourceType); buf.append("\",");
		buf.append( "\"eventType\":\""); buf.append(this.eventType); buf.append("\",");
		buf.append( "\"streamType\":\""); buf.append(this.streamType); buf.append("\",");
		buf.append( "\"streamName\":\""); buf.append(this.streamName); buf.append("\",");
		buf.append( "\"width\":\""); buf.append(this.width); buf.append("\",");
		buf.append( "\"height\":\""); buf.append(this.height); buf.append("\",");
		buf.append( "\"profile\":\""); buf.append(this.profile); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
	
	public String getConferenceKey()
	{
		return conferenceKey;
	}
	public void setConferenceKey(String conferenceKey)
	{
		this.conferenceKey = conferenceKey;
	}
	public String getEventType()
	{
		return eventType;
	}
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
	public String getStreamName()
	{
		return streamName;
	}
	public void setStreamName(String streamName)
	{
		this.streamName = streamName;
	}
	public String getStreamType()
	{
		return streamType;
	}
	public void setStreamType(String streamType)
	{
		this.streamType = streamType;
	}
	public String getResourceId()
	{
		return resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	public String getHeight()
	{
		return height;
	}
	public void setHeight(String height)
	{
		this.height = height;
	}
	public String getProfile()
	{
		return profile;
	}
	public void setProfile(String profile)
	{
		this.profile = profile;
	}
	public String getResourceType()
	{
		return resourceType;
	}
	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}
	public String getWidth()
	{
		return width;
	}
	public void setWidth(String width)
	{
		this.width = width;
	}
}
