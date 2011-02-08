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
public class UIRosterEntry	extends	UIObject
{
	public static final String ROLE_ACTIVE_PRESENTER = "role.activePresenter";
	public static final String ROLE_PRESENTER = "role.presenter";
	public static final String ROLE_ATTENDEE = "role.attendee";
	
	public static final String STATUS_HOST = "host";
	public static final String STATUS_PREVIOUS_HOST = "previous_host";
	
	private	static	final	String	ROSTER_KEY_USER_ID	=	"userId";
	private	static	final	String	ROSTER_KEY_EMAIL	=	"email";
	private	static	final	String	ROSTER_KEY_DISPLAY_NAME	=	"displayName";
	private	static	final	String	ROSTER_KEY_PRESENCE	=	"presence";
	private	static	final	String	ROSTER_KEY_MOOD	=	"mood";
	private	static	final	String	ROSTER_KEY_ROLE	=	"role";
	private	static	final	String	ROSTER_KEY_STATUS	=	"status";
	private	static	final	String	ROSTER_KEY_CHAT	=	"chat";
	private	static	final	String	ROSTER_KEY_AUDIO	=	"audio";
	private	static	final	String	ROSTER_KEY_VIDEO	=	"video";
	private	static	final	String	ROSTER_KEY_PHOTO	=	"photo";
	private	static	final	String	ROSTER_KEY_NETPROFILE	=	"netProfile";
	private	static	final	String	ROSTER_KEY_IMGQUALITY	=	"imgQuality";
	private	static	final	String	ROSTER_KEY_HAS_ACTIVEX	=	"hasActiveX";
	
	protected	String	userId = null;
	protected	String	email = "";
	protected	String	displayName = "";
	protected	String	presence = "";
	protected	String	mood = "";
	protected	String	status = "";
	protected	String	role = "";
	protected	String	chat = "0";
	protected	String	audio = "0";
	protected	String	video = "0";
	protected	String	photoUrl = "";
	protected	String	netProfile = "2";
	protected	String	imgQuality = "high";
	protected	String	hasActiveX = "";
	protected   boolean joinedBeforeMe = false;
	
	public UIRosterEntry()
	{
	}
	public	static	UIRosterEntry	parseJsonObject(JSONObject reJson)
	{
		UIRosterEntry re = new UIRosterEntry();
		
		String d64 = reJson.get(ROSTER_KEY_DISPLAY_NAME).isString().stringValue();
		String d = UIRosterEntry.decodeBase64(d64);
		
		re.setUserId(reJson.get(ROSTER_KEY_USER_ID).isString().stringValue());
		re.setEmail(reJson.get(ROSTER_KEY_EMAIL).isString().stringValue());
		re.setDisplayName(d);
		re.setPresence(reJson.get(ROSTER_KEY_PRESENCE).isString().stringValue());
		re.setMood(reJson.get(ROSTER_KEY_MOOD).isString().stringValue());
		re.setStatus(reJson.get(ROSTER_KEY_STATUS).isString().stringValue());
		re.setRole(reJson.get(ROSTER_KEY_ROLE).isString().stringValue());
		re.setChat(reJson.get(ROSTER_KEY_CHAT).isString().stringValue());
		re.setAudio(reJson.get(ROSTER_KEY_AUDIO).isString().stringValue());
		re.setVideo(reJson.get(ROSTER_KEY_VIDEO).isString().stringValue());
		re.setPhotoUrl(reJson.get(ROSTER_KEY_PHOTO).isString().stringValue());
		re.setNetProfile(reJson.get(ROSTER_KEY_NETPROFILE).isString().stringValue());
		re.setImgQuality(reJson.get(ROSTER_KEY_IMGQUALITY).isString().stringValue());
//		re.setHasActiveX(reJson.get(ROSTER_KEY_HAS_ACTIVEX).isString().stringValue());
		
		return	re;
	}
	public	static	int	getRoleIndex(String role)
	{
		if (role.equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))	return	0;
		else if (role.equals(UIRosterEntry.ROLE_PRESENTER))	return	1;
		else if (role.equals(UIRosterEntry.ROLE_ATTENDEE))	return	2;
		else	return	2;
	}
	/**
	 * Other parameters can not change during the lifetime of a session.
	 * @param changes
	 */
	public	void	refreshWithChanges(UIRosterEntry changes)
	{
		this.setUserId(changes.getUserId());
		this.setEmail(changes.getEmail());
		this.setDisplayName(changes.getDisplayName());
		this.setPresence(changes.getPresence());
		this.setStatus(changes.getStatus());
		this.setMood(changes.getMood());
		this.setRole(changes.getRole());
		this.setChat(changes.getChat());
		this.setAudio(changes.getAudio());
		this.setVideo(changes.getVideo());
		this.setPhotoUrl(changes.getPhotoUrl());
		this.setNetProfile(changes.getNetProfile());
		this.setImgQuality(changes.getImgQuality());
	}
	public	boolean	isHost()
	{
		return	this.status.equals(UIRosterEntry.STATUS_HOST) ||
				this.status.equals(UIRosterEntry.STATUS_PREVIOUS_HOST);
	}
	public	boolean	isPreviousHost()
	{
		return	this.status.equals(UIRosterEntry.STATUS_PREVIOUS_HOST);
	}
	public	boolean	isActivePresenter()
	{
		return	role.equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER);
	}
	public	boolean	isPresenter()
	{
		return	role.equals(UIRosterEntry.ROLE_PRESENTER) ||
			role.equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER);
	}
	public	boolean	isChatOn()
	{
		return	this.chat.equals("1");
	}
	public	boolean	isAudioOn()
	{
		return	this.audio.equals("1");
	}
	public	boolean	isVideoOn()
	{
		return	this.video.equals("1");
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getDisplayName()
	{
		return this.displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getMood()
	{
		return this.mood;
	}
	public void setMood(String mood)
	{
		this.mood = mood;
	}
	public String getPresence()
	{
		return this.presence;
	}
	public void setPresence(String presence)
	{
		this.presence = presence;
	}
	public String getRole()
	{
		return this.role;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getUserId()
	{
		return this.userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getHasActiveX()
	{
		return hasActiveX;
	}
	public void setHasActiveX(String hasActiveX)
	{
		this.hasActiveX = hasActiveX;
	}
	public String getAudio()
	{
		return audio;
	}
	public void setAudio(String audio)
	{
		this.audio = audio;
	}
	public String getChat()
	{
		return chat;
	}
	public void setChat(String chat)
	{
		this.chat = chat;
	}
	public String getPhotoUrl()
	{
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl = photoUrl;
	}
	public String getVideo()
	{
		return video;
	}
	public void setVideo(String video)
	{
		this.video = video;
	}
	public String getImgQuality()
	{
		return imgQuality;
	}
	public void setImgQuality(String imgQuality)
	{
		this.imgQuality = imgQuality;
	}
	public boolean getJoinedStatus()
	{
		return joinedBeforeMe;
	}
	public void setJoinedStatus(boolean status)
	{
		this.joinedBeforeMe = status;
	}
	public String getNetProfile()
	{
		return netProfile;
	}
	public void setNetProfile(String netProfile)
	{
		this.netProfile = netProfile;
	}
	public	String	toString()
	{
		return	toJson();
	}
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer("");
		
		String d = UIRosterEntry.encodeBase64(this.displayName);
		
		buf.append("{");
		buf.append("objClass:\""); buf.append("RosterEntry"); buf.append("\",");
		buf.append("userId:\""); buf.append(userId); buf.append("\",");
		buf.append("email:\""); buf.append(email); buf.append("\",");
		buf.append("displayName:\""); buf.append(d); buf.append("\",");
		buf.append("presence:\""); buf.append(presence); buf.append("\",");
		buf.append("mood:\""); buf.append(mood); buf.append("\",");
		buf.append("role:\""); buf.append(role); buf.append("\",");
		buf.append("status:\""); buf.append(status); buf.append("\",");
		buf.append("chat:\""); buf.append(chat); buf.append("\",");
		buf.append("audio:\""); buf.append(audio); buf.append("\",");
		buf.append("video:\""); buf.append(video); buf.append("\",");
		buf.append("photoUrl:\""); buf.append(photoUrl); buf.append("\",");
		buf.append("netProfile:\""); buf.append(netProfile); buf.append("\",");
		buf.append("imgQuality:\""); buf.append(imgQuality); buf.append("\",");
		buf.append("hasActiveX:\""); buf.append(hasActiveX); buf.append("\",");
		buf.append("data:\""); buf.append("dummy"); buf.append("\"");
		buf.append("}");
		
		return	buf.toString();
	}
	public	static	native	String	encodeBase64(String s) /*-{
		return $wnd.Base64.encode(s);
	}-*/;
	public	static	native	String	decodeBase64(String s) /*-{
		return $wnd.Base64.decode(s);
	}-*/;
}
