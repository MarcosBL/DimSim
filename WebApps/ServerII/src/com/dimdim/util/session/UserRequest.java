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

package com.dimdim.util.session;

import java.util.UUID;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object caches the user information so that it doesn't have to be
 * carried back and forth between the signin page and server.
 */

public class UserRequest
{
	public	static	final	String	MEETING_CONNECT_CONTINUE_URL	=	"MEETING_CONNECT_CONTINUE_URL";
	
	protected	String		uri;
	
	protected	String		action;
	protected	UserInfo	userInfo;
	
	protected	String		confKey;
	protected	String		confName = "";
	protected	String		meetingId;
	protected	String		attendees;
	protected	String		locale = "en_US";
	
	protected	MeetingSettings		meetingSettings;
	
	protected	String		url;
	
	protected	boolean		portalCall;
	protected	String		portalMeetingJoinUrl;
	protected	String		preseterPwd;
	protected	String		attendeePwd;
	protected	String		meetingName;
	
//	public UserRequest()
//	{
//	}
	public UserRequest(String action, UserInfo userInfo,
		String confKey, String confName,
		String meetingId, String attendees, String locale,
		MeetingSettings meetingSettings, String preseterPwd, String attendeePwd)
	{
		this.uri = UUID.randomUUID().toString();
		
		this.action = action;
		this.userInfo = userInfo;
		
		this.confKey = confKey;
		if(null != confName)
		{
			this.confName = confName;
		}
		this.meetingId = meetingId;
		this.attendees = attendees;
		this.locale = locale;
		this.attendeePwd = attendeePwd;
		this.preseterPwd = preseterPwd;
		
		this.meetingSettings = meetingSettings;
	}
	public String getAction()
	{
		return action;
	}
	public String getConfKey()
	{
		return confKey;
	}
	public String getConfName()
	{
		return confName;
	}
	public String getMeetingId()
	{
		return meetingId;
	}
	public MeetingSettings getMeetingSettings()
	{
		return meetingSettings;
	}
	public String getUri()
	{
		return uri;
	}
	public UserInfo getUserInfo()
	{
		return userInfo;
	}
	public String getAttendees()
	{
		return attendees;
	}
	public String getLocale()
	{
		return locale;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public boolean isPortalCall()
	{
		return portalCall;
	}
	public void setPortalCall(boolean portalCall)
	{
		this.portalCall = portalCall;
	}
	public String getPortalMeetingJoinUrl()
	{
		return portalMeetingJoinUrl;
	}
	public void setPortalMeetingJoinUrl(String portalMeetingJoinUrl)
	{
		this.portalMeetingJoinUrl = portalMeetingJoinUrl;
	}
	public String getAttendeePwd() {
		return attendeePwd;
	}
	public String getPreseterPwd() {
		return preseterPwd;
	}
	public	String	toString()
	{
		return	this.uri+"--"+this.meetingSettings.toString();
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
}

