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

import	com.dimdim.messaging.IEventReceiver;
import	com.dimdim.conference.ConferenceConstants;
import	com.dimdim.util.misc.Base64;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object is same as the participant object provided by the conference
 * core package. This simple subclass is required because there will be few
 * other users outside of the conferences, such as the system administrators.
 * Also because this object is mapped to user objects in the db.
 */

public class Participant	 extends	MeetingEventData	implements	IConferenceParticipant
{
//	protected	String	clientId;
	protected	int		index;
	protected	String	id;
	protected	String	email;
	protected	String	password;
	protected	String	displayName;
	protected	String	role;
	protected	String	mood;
	protected	String	status = "";
	protected	String	photo;
	protected	String	netProfile;
	protected	String	imgQuality;
	
	protected	ParticipantPermissions	permissions;
//	protected	Mood		mood;
	protected	Presence	presence;
	
	protected	int		currentState;
	
	protected	ParticipantClientEventsManager	eventsManager;
	private String portalId;
	
//	public Participant()
//	{
//	}
	public Participant(int index, String email, String displayName, String password, String role, String userId)
	{
		super(index+"");
		this.index = index;
		this.id = index+"";
		
		if(null != userId && userId.length() > 0)
		{
			this.portalId = userId;
		}else{
			this.portalId = null;
		}
		
		this.email = email;
		this.password = password;
		if(null != displayName && displayName.length() == 0)
		{
			this.displayName = "Attendee"+(index);
		}else{
			this.displayName = displayName;
		}
		this.role = role;
		this.currentState = UserRosterStateMachine.STATE_JOINING;
		
		this.permissions = new ParticipantPermissions();
		this.mood = Mood.Normal;//Mood.getDefaultMood();
		this.photo = "";
		this.presence = new Presence();
		this.netProfile = "2";
		this.imgQuality = "high";
		
		this.eventsManager = new ParticipantClientEventsManager(this,this.id);
	}
	public	int	compareTo(Object obj)
	{
		if (obj instanceof com.dimdim.conference.model.Participant)
		{
			return	this.getId().compareTo(((Participant)obj).getId());
		}
		else
		{
			return	1;
		}
	}
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("RosterEntry"); buf.append("\",");
		buf.append( "\"userId\":\""); buf.append(this.id); buf.append("\",");
		buf.append( "\"email\":\""); buf.append(this.email); buf.append("\",");
		buf.append( "\"displayName\":\""); buf.append(getDisplayNameBase64(this.displayName)); buf.append("\",");
		buf.append( "\"presence\":\""); buf.append(this.presence.getNiceName()); buf.append("\",");
		buf.append( "\"mood\":\""); buf.append(this.mood); buf.append("\",");
		buf.append( "\"status\":\""); buf.append(this.status); buf.append("\",");
		buf.append( "\"chat\":\""); buf.append(this.permissions.getChat()); buf.append("\",");
		buf.append( "\"audio\":\""); buf.append(this.permissions.getAudio()); buf.append("\",");
		buf.append( "\"video\":\""); buf.append(this.permissions.getVideo()); buf.append("\",");
		buf.append( "\"photo\":\""); buf.append(this.photo); buf.append("\",");
		buf.append( "\"role\":\""); buf.append(this.role); buf.append("\",");
		buf.append( "\"netProfile\":\""); buf.append(this.netProfile); buf.append("\",");
		buf.append( "\"imgQuality\":\""); buf.append(this.imgQuality); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
//	public String getClientId()
//	{
//		return clientId;
//	}
//	public void setClientId(String clientId)
//	{
//		this.clientId = clientId;
//	}
	public	boolean		isPresenter()
	{
		return	this.role.equals(ConferenceConstants.ROLE_PRESENTER) ||
				this.role.equals(ConferenceConstants.ROLE_ACTIVE_PRESENTER);
	}
	public	boolean		isActivePresenter()
	{
		return	this.role.equals(ConferenceConstants.ROLE_ACTIVE_PRESENTER);
	}
	public	boolean		isAttendee()
	{
		return	(this.role.equals(ConferenceConstants.ROLE_ATTENDEE));
	}
	public	boolean		isInLobby()
	{
		return	(this.currentState == UserRosterStateMachine.STATE_WAITING_IN_LOBBY);
	}
	public	boolean		isInConference()
	{
		return	(currentState == UserRosterStateMachine.STATE_IN_CONFERENCE ||
				currentState == UserRosterStateMachine.STATE_IN_CONFERENCE_PRESENTER ||
				currentState == UserRosterStateMachine.STATE_IN_CONFERENCE_ACTIVE_PRESENTER ||
				currentState == UserRosterStateMachine.STATE_IN_CONFERENCE_ATTENDEE);
	}
	public	boolean		isJoining()
	{
		return	this.currentState == UserRosterStateMachine.STATE_JOINING ||
				this.currentState == UserRosterStateMachine.STATE_REJOINING;
	}
	public	boolean		isHost()
	{
		return	this.status.equals(ConferenceConstants.STATUS_HOST) ||
				this.status.equals(ConferenceConstants.STATUS_PREVIOUS_HOST);
	}
	public String getDisplayName()
	{
		return this.displayName;
	}
	public static String getDisplayNameBase64(String displayName)
	{
		byte[] bytes = displayName.getBytes();
		String d = Base64.encodeBytes(bytes,0,bytes.length,Base64.DONT_BREAK_LINES);
		return d;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public	String	getId()
	{
		return	this.id;
	}
	public String getEmail()
	{
		return this.email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getPassword()
	{
		return this.password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getRole()
	{
		return this.role;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	public	String	getMood()
	{
		return	this.mood;
	}
	public	void	setMood(String mood)
	{
		this.mood = mood;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public int getIndex()
	{
		return index;
	}
	public	String	getPresence()
	{
		return	this.presence.getNiceName();
	}
	public String getPhoto()
	{
		return photo;
	}
	public void setPhoto(String photo)
	{
		this.photo = photo;
	}
	public String getImgQuality()
	{
		return imgQuality;
	}
	public void setImgQuality(String imgQuality)
	{
		this.imgQuality = imgQuality;
	}
	public String getNetProfile()
	{
		return netProfile;
	}
	public void setNetProfile(String netProfile)
	{
		this.netProfile = netProfile;
	}
	public	void	setPresence(String presence)
	{
		
	}
	public ParticipantPermissions getPermissions()
	{
		return permissions;
	}
	public void setPermissions(ParticipantPermissions permissions)
	{
		this.permissions = permissions;
	}
	
	/**
	 * The remote client events management support
	 */
	public	IEventsProvider	getEventsProvider()
	{
		return	this.eventsManager;
	}
	public	IEventReceiver	getEventReceiver()
	{
		return	this.eventsManager;
	}
	public	void	addChild()
	{
		this.eventsManager.createChildEventsManager();
	}
	public	boolean	hasChild()
	{
		return	(this.eventsManager.getChildEventsManager() != null);
	}
	public	void	closeChild()
	{
		System.out.println("Closing Child event manager for:"+this.displayName);
		this.eventsManager.closeChildEventsManager();
	}
	public	IEventsProvider	getChildEventsProvider()
	{
		return	this.eventsManager.getChildEventsManager();
	}
	public	IEventReceiver	getChildEventReceiver()
	{
		return	this.eventsManager.getChildEventsManager();
	}
	public int getCurrentState()
	{
		return currentState;
	}
	public void setCurrentState(int currentState)
	{
		this.currentState = currentState;
		if (currentState == UserRosterStateMachine.STATE_IN_CONFERENCE_ACTIVE_PRESENTER)
		{
			this.role = ConferenceConstants.ROLE_ACTIVE_PRESENTER;
			System.out.println("New role for:"+this.email+": active presenter");
		}
		else if (currentState == UserRosterStateMachine.STATE_IN_CONFERENCE_PRESENTER)
		{
			this.role = ConferenceConstants.ROLE_PRESENTER;
			System.out.println("New role for:"+this.email+": presenter");
		}
	}
}
