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

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UICobrowseControlEvent extends UIObject
{
	public	static	final	String	START	=	"s";
	public	static	final	String	STOP	=	"p";
	public	static	final	String	SCROLL	=	"sc";
	public	static	final	String	NAVIGATE=	"n";
	public	static	final	String	LOCK	=	"l";
	public	static	final	String	UNLOCK	=	"u";
	public	static	final	String	RENAME	=	"r";
	
	
	protected	static	String	KEY_CONFERENCE_KEY = "ck";
	protected	static	String	KEY_RESOURCE_ID = "ri";
	protected	static	String	KEY_EVENT_TYPE = "et";
	protected	static	String	KEY_STREAM_ID = "si";
	protected	static	String	KEY_WIDTH = "w";
	protected	static	String	KEY_HEIGHT = "h";
	protected	static	String	KEY_NEWNAME = "nn";
	
	protected	String	conferenceKey;
	protected	String	resourceId;
	protected	String	eventType;
	protected	String	streamId;
	protected	String	width;
	protected	String	height;
	protected	String	newName;
	
	public UICobrowseControlEvent()
	{
	}
	public UICobrowseControlEvent(String conferenceKey, String resourceId,
			String eventType, String streamId, String width, String height, String newName)
	{
		this.conferenceKey = conferenceKey;
		this.resourceId = resourceId;
		this.eventType = eventType;
		this.streamId = streamId;
		this.width = width;
		this.height = height;
		this.newName = newName;
	}
	public	static	UICobrowseControlEvent	parseJsonObject(JSONObject pceJson)
	{
		UICobrowseControlEvent sse = new UICobrowseControlEvent();
		//Window.alert("inside UICobrowseControlEvent json = "+pceJson);
		sse.setConferenceKey(pceJson.get(KEY_CONFERENCE_KEY).isString().stringValue());
		sse.setResourceId(pceJson.get(KEY_RESOURCE_ID).isString().stringValue());
		sse.setEventType(pceJson.get(KEY_EVENT_TYPE).isString().stringValue());
		sse.setStreamId(pceJson.get(KEY_STREAM_ID).isString().stringValue());
		sse.setWidth(pceJson.get(KEY_WIDTH).isString().stringValue());
		sse.setHeight(pceJson.get(KEY_HEIGHT).isString().stringValue());
		sse.setNewName(pceJson.get(KEY_NEWNAME).isString().stringValue());
		
		return	sse;
	}
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer("");
		
		buf.append("{");
		buf.append("objClass:\""); buf.append("cbce"); buf.append("\",");
		buf.append("ck:\""); buf.append(conferenceKey); buf.append("\",");
		buf.append("ri:\""); buf.append(resourceId); buf.append("\",");
		buf.append("et:\""); buf.append(eventType); buf.append("\",");
		buf.append("si:\""); buf.append(streamId); buf.append("\",");
		buf.append("w:\""); buf.append(width); buf.append("\",");
		buf.append("h:\""); buf.append(height); buf.append("\",");
		buf.append("nn:\""); buf.append(newName); buf.append("\",");
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
	public String getStreamId()
	{
		return streamId;
	}
	public void setStreamId(String streamId)
	{
		this.streamId = streamId;
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
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
}
