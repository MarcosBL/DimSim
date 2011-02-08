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
public class SettingsEvent implements IJsonSerializable
{
	public	static	final	String	CHANGE	=	"change";	//	true/false
	
	protected	String	eventType;
	protected	boolean	lobby;
	protected	int		maxAttendees;
	protected	int		maxTime;
	protected	String	networkProfile;
	protected	String	imageQuality;
	
	public SettingsEvent()
	{
		
	}
	public SettingsEvent(String eventType, boolean lobby,
		int maxAttendees, int maxTime, String networkProfile,
		String imageQuality)
	{
		this.eventType = eventType;
		this.lobby = lobby;
		this.maxAttendees = maxAttendees;
		this.maxTime = maxTime;
		this.networkProfile = networkProfile;
		this.imageQuality = imageQuality;
	}
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("SettingsEvent"); buf.append("\",");
		buf.append( "\"lobby\":\""+this.lobby); buf.append("\",");
		buf.append( "\"maxAttendees\":\""+this.maxAttendees); buf.append("\",");
		buf.append( "\"maxTime\":\""+this.maxTime); buf.append("\",");
		buf.append( "\"eventType\":\""); buf.append(this.eventType); buf.append("\",");
		buf.append( "\"networkProfile\":\""); buf.append(this.networkProfile); buf.append("\",");
		buf.append( "\"imageQuality\":\""); buf.append(this.imageQuality); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
	public String getEventType()
	{
		return eventType;
	}
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
	public String getImageQuality()
	{
		return imageQuality;
	}
	public void setImageQuality(String imageQuality)
	{
		this.imageQuality = imageQuality;
	}
	public boolean isLobby()
	{
		return lobby;
	}
	public void setLobby(boolean lobby)
	{
		this.lobby = lobby;
	}
	public int getMaxAttendees()
	{
		return maxAttendees;
	}
	public void setMaxAttendees(int maxAttendees)
	{
		this.maxAttendees = maxAttendees;
	}
	public int getMaxTime()
	{
		return maxTime;
	}
	public void setMaxTime(int maxTime)
	{
		this.maxTime = maxTime;
	}
	public String getNetworkProfile()
	{
		return networkProfile;
	}
	public void setNetworkProfile(String networkProfile)
	{
		this.networkProfile = networkProfile;
	}
}

