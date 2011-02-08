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
public class CobrowseControlEvent  extends	MeetingEventData	implements IJsonSerializable
{

	public	static	final	String	START	=	"s";
	public	static	final	String	STOP	=	"p";
	public	static	final	String	NAVIGATE=	"n";
	public	static	final	String	SCROLL	=	"sc";
	public	static	final	String	LOCK	=	"l";
	public	static	final	String	UNLOCK	=	"u";
	public	static	final	String	RENAME	=	"r";
	
	protected	String	conferenceKey;
	protected	String	resourceId;
	protected	String	eventType;
	protected	String	streamId;
	protected	String	width;
	protected	String	height;
	protected	String 	newName;
	
//	public	static	WhiteboardControlEvent
//		createCreateEvent(String confKey, String resourceId,
//				String streamId, String width, String height, String profile)
//	{
//		return	new	WhiteboardControlEvent(confKey, resourceId,
//				WhiteboardControlEvent.CREATE,
//				streamId, width, height, profile);
//	}
	public	static	CobrowseControlEvent
		createStartEvent(String confKey, String presenterId, String resourceId, String streamId)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
				CobrowseControlEvent.START,
				streamId, "", "", "");
	}
	public	static	CobrowseControlEvent
		createStopEvent(String confKey, String presenterId, String resourceId, String streamId)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
				CobrowseControlEvent.STOP,
				streamId, "", "", "");
	}
	public	static	CobrowseControlEvent
		createNavigateEvent(String confKey, String presenterId, String resourceId, String streamId)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
				CobrowseControlEvent.NAVIGATE,
				streamId, "", "", "");
	}
	public	static	CobrowseControlEvent
		createScrollEvent(String confKey, String presenterId, String resourceId, String streamId)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
				CobrowseControlEvent.SCROLL, streamId, "", "", "");
	}
	
	public	static	CobrowseControlEvent createLockEvent(String confKey, String presenterId, String resourceId, String streamId)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
			CobrowseControlEvent.LOCK, streamId, "", "", "");
	}
	
	public	static	CobrowseControlEvent createUnLockEvent(String confKey, String presenterId, String resourceId, String streamId)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
			CobrowseControlEvent.UNLOCK, streamId, "", "", "");
	}
	
	public	static	CobrowseControlEvent createRenameEvent(String confKey, String presenterId, String resourceId, String streamId, String newName)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
			CobrowseControlEvent.RENAME, streamId, "", "", newName);
	}
	
	public	static	CobrowseControlEvent
	createScrollEvent(String confKey, String presenterId, String resourceId, String streamId
			,String horScroll, String verScroll)
	{
		return	new	CobrowseControlEvent(confKey, presenterId, resourceId,
			CobrowseControlEvent.SCROLL, streamId, horScroll, verScroll, "");
	}
	
	public CobrowseControlEvent(String conferenceKey, String presenterId, String resourceId,
			String eventType, String streamId,
			String width, String height, String newName)
	{
		super(presenterId);
		this.conferenceKey = conferenceKey;
		this.resourceId = resourceId;
		this.eventType = eventType;
		this.streamId = streamId;
		this.width = width;
		this.height = height;
		this.newName = newName;
	}
	
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("cbce"); buf.append("\",");
		buf.append( "\"ck\":\""); buf.append(this.conferenceKey); buf.append("\",");
		buf.append( "\"ri\":\""); buf.append(this.resourceId); buf.append("\",");
		buf.append( "\"et\":\""); buf.append(this.eventType); buf.append("\",");
		buf.append( "\"si\":\""); buf.append(this.streamId); buf.append("\",");
		buf.append( "\"w\":\""); buf.append(this.width); buf.append("\",");
		buf.append( "\"h\":\""); buf.append(this.height); buf.append("\",");
		buf.append( "\"nn\":\""); buf.append(this.newName); buf.append("\",");
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
	public String getStreamId()
	{
		return streamId;
	}
	public void setStreamId(String streamId)
	{
		this.streamId = streamId;
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
