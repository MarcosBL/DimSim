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

public class MeetingOptions
{
	public	final	static	String	MEETING_CONNECT_CONTINUE_URL	=	"MeetingConnectContinueURL";
	public	final	static	String	MEETING_OPTIONS_KEY	=	"MeetingOptions";
	
	protected	String	lobby;
	protected	String	networkProfile;
	protected	String	imageQuality;
	protected	Integer	meetingHours;
	protected	Integer	meetingMinutes;
	protected	Integer	maxParticipants;
	protected	Integer	maxAttendeeMikes;
	protected	String	returnUrl;
	protected	String	attendees;
	
	protected	String	presenterAV;
	protected	boolean	privateChatEnabled;
	protected	boolean	publicChatEnabled;
	protected	boolean	screenShareEnabled;
	protected	boolean	whiteboardEnabed;
	
	protected	String	osType;
	protected	String	browserType;
	
	protected	String	displayName;
	protected	String	confName;
	protected	String	email;
	protected	String	confKey;
	protected	String	lc;
	
	public	MeetingOptions()
	{
		
	}
	public MeetingOptions(String lobby, String networkProfile, String imageQuality,
			String meetingHours, String meetingMinutes, String maxParticipants,
			String maxAttendeeMikes, String returnUrl, String presenterAV,
			String privateChatEnabled, String publicChatEnabled,
			String screenShareEnabled, String whiteboardEnabed,
			String attendees, String osType, String displayName, String confName,
			String email, String confKey, String browserType)
	{
		super();
		
		this.lobby = lobby;
		this.networkProfile = networkProfile;
		this.imageQuality = imageQuality;
		this.meetingHours = this.readInteger(meetingHours,1);
		this.meetingMinutes = this.readInteger(meetingMinutes,0);
		this.maxParticipants = this.readInteger(maxParticipants,20);
		this.maxAttendeeMikes = this.readInteger(maxAttendeeMikes,2);
		this.returnUrl = returnUrl;
		this.presenterAV = presenterAV;
		this.privateChatEnabled = this.readBoolean(privateChatEnabled,false);
		this.publicChatEnabled = this.readBoolean(publicChatEnabled,false);
		this.screenShareEnabled = this.readBoolean(screenShareEnabled,false);
		this.whiteboardEnabed = this.readBoolean(whiteboardEnabed,false);
		this.attendees = attendees;
		this.osType = osType;
		this.displayName = displayName;
		this.confName = confName;
		this.email = email;
		this.confKey = confKey;
		this.browserType = browserType;
	}
	private	Integer	readInteger(String s, int defaultValue)
	{
		try
		{
			return	new	Integer(s);
		}
		catch(Exception e)
		{
			return	new	Integer(defaultValue);
		}
	}
	private	boolean	readBoolean(String s, boolean defaultValue)
	{
		try
		{
			return	Boolean.parseBoolean(s);
		}
		catch(Exception e)
		{
			return	defaultValue;
		}
	}
	public String getImageQuality()
	{
		return imageQuality;
	}
	public String getLobby()
	{
		return lobby;
	}
	public Integer getMaxAttendeeMikes()
	{
		return maxAttendeeMikes;
	}
	public Integer getMaxParticipants()
	{
		return maxParticipants;
	}
	public Integer getMeetingHours()
	{
		return meetingHours;
	}
	public Integer getMeetingMinutes()
	{
		return meetingMinutes;
	}
	public String getNetworkProfile()
	{
		return networkProfile;
	}
	public String getPresenterAV()
	{
		return presenterAV;
	}
	public boolean isPrivateChatEnabled()
	{
		return privateChatEnabled;
	}
	public boolean isPublicChatEnabled()
	{
		return publicChatEnabled;
	}
	public String getReturnUrl()
	{
		return returnUrl;
	}
	public boolean isScreenShareEnabled()
	{
		return screenShareEnabled;
	}
	public boolean isWhiteboardEnabed()
	{
		return whiteboardEnabed;
	}
	public String getAttendees()
	{
		return attendees;
	}
	public String getOsType()
	{
		return osType;
	}
	public String getConfName()
	{
		return confName;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public String getConfKey()
	{
		return confKey.toLowerCase();
	}
	public String getEmail()
	{
		return email;
	}
	public String getBrowserType()
	{
		return browserType;
	}
	public String getLc()
	{
		return lc;
	}
	public void setLc(String lc)
	{
		this.lc = lc;
	}
}
