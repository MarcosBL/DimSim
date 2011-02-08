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
public class UISettingsEvent
{
	public	static	final	String	CHANGE	=	"change";
	
	protected	static	String	KEY_EVENT_TYPE	=	"eventType";
	protected	static	String	KEY_LOBBY = "lobby";
	protected	static	String	KEY_MAX_ATTENDEES = "maxAttendees";
	protected	static	String	KEY_MAX_TIME = "maxTime";
	protected	static	String	KEY_NETWORK_PROFILE = "networkProfile";
	protected	static	String	KEY_IMAGE_QUALITY = "imageQuality";
	
	protected	String	eventType;
	protected	String	lobby;
	protected	String	maxAttendees;
	protected	String	maxTime;
	protected	String	networkProfile;
	protected	String	imageQuality;
	
	public UISettingsEvent()
	{
	}
	public	static	UISettingsEvent	parseJsonObject(JSONObject pceJson)
	{
		UISettingsEvent se = new UISettingsEvent();
		
		se.setEventType(pceJson.get(KEY_EVENT_TYPE).isString().stringValue());
		se.setLobby(pceJson.get(KEY_LOBBY).isString().stringValue());
		se.setMaxAttendees(pceJson.get(KEY_MAX_ATTENDEES).isString().stringValue());
		se.setMaxTime(pceJson.get(KEY_MAX_TIME).isString().stringValue());
		se.setNetworkProfile(pceJson.get(KEY_NETWORK_PROFILE).isString().stringValue());
		se.setImageQuality(pceJson.get(KEY_IMAGE_QUALITY).isString().stringValue());
		
		return	se;
	}
	public String getEventType()
	{
		return eventType;
	}
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
	public String getLobby()
	{
		return lobby;
	}
	public void setLobby(String lobby)
	{
		this.lobby = lobby;
	}
	public String getMaxAttendees()
	{
		return maxAttendees;
	}
	public void setMaxAttendees(String maxAttendees)
	{
		this.maxAttendees = maxAttendees;
	}
	public String getImageQuality()
	{
		return imageQuality;
	}
	public void setImageQuality(String imageQuality)
	{
		this.imageQuality = imageQuality;
	}
	public String getMaxTime()
	{
		return maxTime;
	}
	public void setMaxTime(String maxTime)
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
