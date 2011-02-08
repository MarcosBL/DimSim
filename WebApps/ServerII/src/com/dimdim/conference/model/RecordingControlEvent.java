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
public class RecordingControlEvent  extends	MeetingEventData	implements IJsonSerializable
{
	public	static	final	String	START	=	"start";
	public	static	final	String	STOP	=	"stop";
	
	protected	String	conferenceKey;
	protected	String	eventType;
	
	public	static	RecordingControlEvent	createStartEvent(String confKey, String presenterId)
	{
		return	new	RecordingControlEvent(confKey, presenterId, RecordingControlEvent.START);
	}
	public	static	RecordingControlEvent	createStopEvent(String confKey, String presenterId)
	{
		return	new	RecordingControlEvent(confKey, presenterId, RecordingControlEvent.STOP);
	}
	
	public RecordingControlEvent(String conferenceKey, String presenterId, String eventType)
	{
		super(presenterId);
		this.conferenceKey = conferenceKey;
		this.eventType = eventType;
	}
	
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("RecordingControlEvent"); buf.append("\",");
		buf.append( "\"conferenceKey\":\""); buf.append(this.conferenceKey); buf.append("\",");
		buf.append( "\"eventType\":\""); buf.append(this.eventType); buf.append("\",");
		buf.append( "\"data\":\"d\"");
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
}
