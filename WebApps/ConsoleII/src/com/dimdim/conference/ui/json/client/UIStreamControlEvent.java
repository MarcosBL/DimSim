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

package com.dimdim.conference.ui.json.client;

import	com.google.gwt.json.client.JSONObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UIStreamControlEvent extends UIObject
{
	public	static	final	String	START	=	"start";
	public	static	final	String	STOP	=	"stop";
	public	static	final	String	CHANGE	=	"change";
	public	static	final	String	VIDEO_EVENT	=	"resource.video";
	public	static	final	String	AUDIO_EVENT	=	"resource.audio";
	
	protected	static	String	KEY_CONFERENCE_KEY = "conferenceKey";
	protected	static	String	KEY_RESOURCE_ID = "resourceId";
	protected	static	String	KEY_EVENT_TYPE = "eventType";
	protected	static	String	KEY_STREAM_TYPE = "streamType";
	protected	static	String	KEY_STREAM_NAME = "streamName";
	protected	static	String	KEY_WIDTH = "width";
	protected	static	String	KEY_HEIGHT = "height";
	protected	static	String	KEY_PROFILE = "profile";
	
	protected	String	conferenceKey;
	protected	String	resourceId;
	protected	String	eventType;
	protected	String	streamType;
	protected	String	streamName;
	protected	String	width;
	protected	String	height;
	protected	String	profile;
	
	public UIStreamControlEvent()
	{
	}
	public UIStreamControlEvent(String conferenceKey, String resourceId,
			String eventType, String streamType, String streamName,
			String width, String height)
	{
		this.conferenceKey = conferenceKey;
		this.resourceId = resourceId;
		this.eventType = eventType;
		this.streamType = streamType;
		this.streamName = streamName;
		this.width = width;
		this.height = height;
	}
	public	static	UIStreamControlEvent	parseJsonObject(JSONObject pceJson)
	{
		UIStreamControlEvent sse = new UIStreamControlEvent();
		
		sse.setConferenceKey(pceJson.get(KEY_CONFERENCE_KEY).isString().stringValue());
		sse.setResourceId(pceJson.get(KEY_RESOURCE_ID).isString().stringValue());
		sse.setEventType(pceJson.get(KEY_EVENT_TYPE).isString().stringValue());
		sse.setStreamType(pceJson.get(KEY_STREAM_TYPE).isString().stringValue());
		sse.setStreamName(pceJson.get(KEY_STREAM_NAME).isString().stringValue());
		sse.setProfile(pceJson.get(KEY_PROFILE).isString().stringValue());
		sse.setWidth(pceJson.get(KEY_WIDTH).isString().stringValue());
		sse.setHeight(pceJson.get(KEY_HEIGHT).isString().stringValue());
		
		return	sse;
	}
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer("");
		
		buf.append("{");
		buf.append("objClass:\""); buf.append("StreamControlEvent"); buf.append("\",");
		buf.append("conferenceKey:\""); buf.append(conferenceKey); buf.append("\",");
		buf.append("resourceId:\""); buf.append(resourceId); buf.append("\",");
		buf.append("eventType:\""); buf.append(eventType); buf.append("\",");
		buf.append("streamType:\""); buf.append(streamType); buf.append("\",");
		buf.append("streamName:\""); buf.append(streamName); buf.append("\",");
		buf.append("profile:\""); buf.append(profile); buf.append("\",");
		buf.append("width:\""); buf.append(width); buf.append("\",");
		buf.append("height:\""); buf.append(height); buf.append("\",");
		buf.append("data:\""); buf.append("dummy"); buf.append("\"");
		buf.append("}");
		
		return	buf.toString();
	}
	public String toString()
	{
		return	toJson();
	}
	public String getConferenceKey()
	{
		return this.conferenceKey;
	}
	public void setConferenceKey(String conferenceKey)
	{
		this.conferenceKey = conferenceKey;
	}
	public String getEventType()
	{
		return this.eventType;
	}
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
	public String getResourceId()
	{
		return this.resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	public String getStreamName()
	{
		return this.streamName;
	}
	public void setStreamName(String streamName)
	{
		this.streamName = streamName;
	}
	public String getStreamType()
	{
		return this.streamType;
	}
	public void setStreamType(String streamType)
	{
		this.streamType = streamType;
	}
	public String getHeight()
	{
		return height;
	}
	public void setHeight(String height)
	{
		this.height = height;
	}
	public String getWidth()
	{
		return width;
	}
	public void setWidth(String width)
	{
		this.width = width;
	}
	public String getProfile()
	{
		return profile;
	}
	public void setProfile(String profile)
	{
		this.profile = profile;
	}
	
	public boolean isVideoEvent()
	{
		if(streamName != null && streamName.endsWith(".V"))
		{
			return true;
		}
		return false;
	}
	public boolean isAudioEvent()
	{
		if(streamName != null && streamName.endsWith(".A"))
		{
			return true;
		}
		return false;
	}
}
