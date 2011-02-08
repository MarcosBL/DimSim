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
public class UIRecordingControlEvent extends UIObject
{
	public	static	final	String	START	=	"start";
	public	static	final	String	STOP	=	"stop";
	
	protected	static	String	KEY_EVENT_TYPE = "eventType";
	
	protected	String	eventType;
	
	public UIRecordingControlEvent()
	{
	}
	public UIRecordingControlEvent(String eventType)
	{
		this.eventType = eventType;
	}
	public	static	UIRecordingControlEvent	parseJsonObject(JSONObject pceJson)
	{
		UIRecordingControlEvent sse = new UIRecordingControlEvent();
		
		sse.setEventType(pceJson.get(KEY_EVENT_TYPE).isString().stringValue());
		
		return	sse;
	}
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer("");
		
		buf.append("{");
		buf.append("objClass:\""); buf.append("RecordingControlEvent"); buf.append("\",");
		buf.append("eventType:\""); buf.append(eventType); buf.append("\",");
		buf.append("data:\""); buf.append("dummy"); buf.append("\"");
		buf.append("}");
		
		return	buf.toString();
	}
	public String toString()
	{
		return	toJson();
	}
	public String getEventType()
	{
		return this.eventType;
	}
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
}
