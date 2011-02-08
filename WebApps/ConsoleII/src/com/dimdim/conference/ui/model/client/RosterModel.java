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

package com.dimdim.conference.ui.model.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIAttendeePermissions;
import com.dimdim.conference.ui.json.client.UIEmailAttemptResult;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class RosterModel	extends	FeatureModel
{
	public	static	final	Integer		ModelIndex	=	new Integer(7);
	public	static	final	String		ModelFeatureId	=	"feature.roster";
	
//	protected	UserModel		localUserModel;
	protected	UIRosterEntry	currentHost;
	protected	UIRosterEntry	currentUser;
	protected	UIRosterEntry	currentActivePresenter;
//	protected	ArrayList		lobbyList = new ArrayList();
	protected	UIAttendeePermissions	currentPermissions;
	protected 	String userJoined;
	
	protected	HashMap	objectsMap = new HashMap();
	
	protected	int		maxAttedeeAudios = 0;
	protected	int		currentAttendeeAudios = 0;
	
	protected	int		maxAttedeeVideos = 0;
	protected	int		currentAttendeeVideos = 0;
	
	public	RosterModel()
	{
		super("feature.roster");
		
		this.currentUser = new UIRosterEntry();
		
		this.currentUser.setUserId(this.getUserId());
		this.currentUser.setRole(this.getUserRole());
		this.currentUser.setDisplayName(this.getUserName());
		this.currentUser.setMood(this.getUserMood());
		this.currentUser.setNetProfile(this.getNetProfile());
		this.currentUser.setImgQuality(this.getImgQuality());
		this.currentUser.setStatus(this.getUserStatus());
		
		this.currentActivePresenter = new UIRosterEntry();
		
		this.currentActivePresenter.setUserId("Not Available");
		this.currentActivePresenter.setRole(UIRosterEntry.ROLE_ACTIVE_PRESENTER);
		this.currentActivePresenter.setDisplayName("Not Available");
		this.currentActivePresenter.setMood("Ready");
		this.currentActivePresenter.setPresence("on-line");
		
		this.currentHost = new UIRosterEntry();
		
		this.currentHost.setUserId("Not Available");
		this.currentHost.setRole(UIRosterEntry.ROLE_ACTIVE_PRESENTER);
		this.currentHost.setDisplayName("Not Available");
		this.currentHost.setMood("Ready");
		this.currentHost.setPresence("on-line");
		
		if (this.getUserRole().equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
		{
			this.currentActivePresenter.setUserId(this.currentUser.getUserId());
			this.currentActivePresenter.refreshWithChanges(this.currentUser);
		}
		
		if (this.getUserStatus().equals(UIRosterEntry.STATUS_HOST))
		{
			this.currentHost.setUserId(this.currentUser.getUserId());
			this.currentHost.refreshWithChanges(this.currentUser);
		}
		
		String s = this.getMaxAttendeeAudios();
		this.maxAttedeeAudios = (new Integer(s)).intValue();
		
		s = this.getMaxAttendeeVideos();
		this.maxAttedeeVideos = (new Integer(s)).intValue();
	}
	public	UIRosterEntry	getCurrentUser()
	{
		return	this.currentUser;
	}
	public	int	getNumberOfParticipants()
	{
		return	this.objects.size();
	}
	/**
	 * If no presenter has yet joined the conference, this method may return
	 * null.
	 */
//	public	UIRosterEntry	getActivePresenter()
//	{
//		return	(UIRosterEntry)this.currentObject;
//	}

//	public UserModel getLocalUserModel()
//	{
//		return this.localUserModel;
//	}
//	public void setLocalUserModel(UserModel currentUserModel)
//	{
//		this.localUserModel = currentUserModel;
//	}
	public	UIRosterEntry	getCurrentActivePresenter()
	{
		return	this.currentActivePresenter;
	}
	public UIAttendeePermissions getCurrentPermissions()
	{
		return this.currentPermissions;
	}
	public void setCurrentPermissions(UIAttendeePermissions currentPermissions)
	{
		this.currentPermissions = currentPermissions;
	}
//	public ArrayList getLobbyList()
//	{
//		return this.lobbyList;
//	}
//	public void setLobbyList(ArrayList lobbyList)
//	{
//		this.lobbyList = lobbyList;
//	}
//	public void setCurrentActivePresenter(UIRosterEntry currentActivePresenter)
//	{
//		this.currentActivePresenter = currentActivePresenter;
//	}

	/**
	 * Methods to dispatch commands to server.
	 */
	public	ArrayList	getRoster()
	{
		return	this.objects;
	}
//	public	ArrayList	getLobby()
//	{
//		return	this.lobbyList;
//	}
//	public	void	setPresence(String presence)
//	{
//		String url = this.commandsFactory.getSetPresenceURL(presence);
//		this.executeCommand(url);
//	}
	public	void	sendJoinInvitations(String attendees, String presenters, String message)
	{
		String url = this.commandsFactory.getSendInvitationURL(attendees,presenters,message);
//		Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	sendFeedback(int rating, String comment, String toEmail)
	{
		AnalyticsConstants.reportFeedbackSent();
		String url = this.commandsFactory.getSendFeedbackURL(rating,comment,this.currentUser.getUserId(), toEmail);
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	setMood(String mood)
	{
//		this.trackAjaxCall("/console/user/mood/"+ConferenceGlobals.conferenceKey);
		String url = this.commandsFactory.getSetMoodURL(mood);
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	
	public	void	setDisplayName(String name)
	{
		String url = this.commandsFactory.getSetDisplayNameURL(name);
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	
	public	void	setProfileOptions(String netProfile, String imgQuality,
			String maxParticipants, String maxMeetingTime,String returnUrl)
	{
		String url = this.commandsFactory.getSetProfileOptionsURL(netProfile,
				imgQuality,maxParticipants,maxMeetingTime,returnUrl);
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	setPhotoToDefault()
	{
		String url = this.commandsFactory.getSetPhotoToDefaultURL();
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	createChildSession()
	{
		String url = this.commandsFactory.getCreateChildSessionURL();
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	reportConsoleLoaded()
	{
		if (this.getUserRole().equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
		{
			AnalyticsConstants.reportPresenterJoined();
			if (ConferenceGlobals.isPresenterAVAudioOnly())
			{
				AnalyticsConstants.reportAudioMeetingStarted();
			}
			else
			{
				AnalyticsConstants.reportAVMeetingStarted();
			}
			AnalyticsConstants.reportPresenterOS();
		}
		else
		{
			AnalyticsConstants.reportAttendeeJoined();
			AnalyticsConstants.reportAttendeeOS();
		}
		//call reloaded or loaded based on flag
		String url = "";
		if("true".equalsIgnoreCase(ConferenceGlobals.getReloadWindow()) )
		{
			url = this.commandsFactory.getConsoleReloadedLoadedURL();
		}else{
			url = this.commandsFactory.getConsoleLoadedURL();
		}
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	getConsole()
	{
		String url = this.commandsFactory.getConsoleURL();
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	leaveConference()
	{
		if (this.getUserRole().equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
		{
			AnalyticsConstants.reportPresenterLeft();
		}
		else
		{
			AnalyticsConstants.reportAttendeeLeft();
		}
		String url = this.commandsFactory.getLeaveConferenceURL();
		this.executeCommand(url);
	}
	public	void	extendSessionTimeout()
	{
		String url = this.commandsFactory.getExtendSessionTimeoutURL();
		this.executeCommand(url);
	}
	public	void	resetSessionTimeout()
	{
		String url = this.commandsFactory.getResetSessionTimeoutURL();
		this.executeCommand(url);
	}
	public	void	unloadConsole()
	{
		String url = this.commandsFactory.getUnloadConsoleURL();
		//Window.alert("-"+url+"-");
		this.executeCommand(url);
	}
	public	void	removeAttendee(UIRosterEntry user)
	{
		/*if (user.isAudioOn())
		{
			decreaseCurrentAttendeeAudios();
			
		}else if(user.isVideoOn())
		{
			decreaseCurrentAttendeeVideos();
		}*/
		String url = this.commandsFactory.getRemoveAttendeeURL(user);
		this.executeCommand(url);
	}
	public	void	makePresender(UIRosterEntry user)
	{
		String url = this.commandsFactory.getMakePresenterURL(user);
		this.executeCommand(url);
	}
	public	void	makeActivePresenter(UIRosterEntry user)
	{
		String url = this.commandsFactory.getMakeActivePresenterURL(user);
		this.executeCommand(url);
	}
	public	void	grantAttendeeEntry(UIRosterEntry user)
	{
		String url = this.commandsFactory.getGrantEntryToAttendeeURL(user);
		this.executeCommand(url);
	}
	public	void	grantEntryToAll()
	{
		String url = this.commandsFactory.getGrantEntryToAllURL();
		this.executeCommand(url);
	}
	public	void	denyAttendeeEntry(UIRosterEntry user)
	{
		String url = this.commandsFactory.getDenyEntryToAttendeeURL(user);
		this.executeCommand(url);
	}
	public	void	enableLobby()
	{
		String url = this.commandsFactory.getEnableLobbyURL();
		this.executeCommand(url);
	}
	public	void	disableLobby()
	{
		String url = this.commandsFactory.getDisableLobbyURL();
		this.executeCommand(url);
	}
//	public	void	sendInvitation(String email)
//	{
//		String url = this.commandsFactory.getSendInvitationURL(email);
//		this.executeCommand(url);
//	}
	public	void	sendChatMessage(String userId, String chatText)
	{
		if (userId == null || userId.length() == 0)
		{
			AnalyticsConstants.reportPublicChatMessage();
		}
		else
		{
			AnalyticsConstants.reportPrivateChatMessage();
		}
		String url = this.commandsFactory.getSendChatMessageURL(userId,chatText);
//		String urlData = this.commandsFactory.getSendChatMessageURLData(userId,chatText);
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	enableChatForAll()
	{
		String url = this.commandsFactory.getSetChatPermissionForAllUrl(true);
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	enableChatFor(String id)
	{
		String url = this.commandsFactory.getSetChatPermissionUrl(id,true);
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	disableChatForAll()
	{
		String url = this.commandsFactory.getSetChatPermissionForAllUrl(false);
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	disableChatFor(String id)
	{
		String url = this.commandsFactory.getSetChatPermissionUrl(id,false);
		//Window.alert(url);
		this.executeCommand(url);
	}
	/**
	 * Audio permission set commands.
	 */
	public	int	getMaximumAttendeeAudios()
	{
		return	this.maxAttedeeAudios;
	}
	
	public	int	getAvailableAttendeeAudios()
	{
		//DebugPanel.getDebugPanel().addDebugMessage("#####maxAttedeeAudios= "+maxAttedeeAudios +" currentAttendeeAudios="+currentAttendeeAudios);
		return	this.maxAttedeeAudios - currentAttendeeAudios;
	}
	
	public	void	setMaximumAttendeeAudios(int i)
	{
		this.maxAttedeeAudios = i;
	}
	private	void	decreaseCurrentAttendeeAudios()
	{	this.currentAttendeeAudios--;
	
		if (this.currentAttendeeAudios < 0)
		{
			this.currentAttendeeAudios = 0;
		}
	}
	private	void	increaseCurrentAttendeeAudios()
	{
		this.currentAttendeeAudios = this.currentAttendeeAudios + 1 ;
	}
	private	void	changeCurrentAttendeeAudios(String increase)
	{
		if("1".equalsIgnoreCase(increase))
		{
			increaseCurrentAttendeeAudios();
		}else{
			decreaseCurrentAttendeeAudios();
		}
	}
	public	boolean	canEnableAudioFor(String id)
	{
		return	(this.currentAttendeeAudios < this.maxAttedeeAudios);
	}
	
	public	int	getMaximumAttendeeVideos()
	{
		return	this.maxAttedeeVideos;
	}
	
	public	int	getAvailableAttendeeVideos()
	{
		return	this.maxAttedeeVideos - currentAttendeeVideos;
	}
	
	public	void	setMaximumAttendeeVideos(int i)
	{
		this.maxAttedeeVideos = i;
	}
	private	void	decreaseCurrentAttendeeVideos()
	{
		this.currentAttendeeVideos--;
		if (this.currentAttendeeVideos < 0)
		{
			this.currentAttendeeVideos = 0;
		}
	}
	private	void	increaseCurrentAttendeeVideos()
	{
		this.currentAttendeeVideos = this.currentAttendeeVideos + 1;
	}
	private	void	changeCurrentAttendeeVideos(String increase)
	{
		if("1".equalsIgnoreCase(increase))
		{
			increaseCurrentAttendeeVideos();
		}else{
			decreaseCurrentAttendeeVideos();
		}
	}
	public	boolean	canEnableVideoFor(String id)
	{
		return	(this.currentAttendeeVideos < this.maxAttedeeVideos);
	}
	
	public	void	enableAudioFor(String id)
	{
		AnalyticsConstants.reportMikeEnable();
		//increaseCurrentAttendeeAudios();
		String url = this.commandsFactory.getSetAudioPermissionUrl(id,true);
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	disableAudioForAll()
	{
		AnalyticsConstants.reportMikeDisable();
		this.currentAttendeeAudios = 0;
		String url = this.commandsFactory.getSetAudioPermissionForAllUrl(false);
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	disableAudioFor(String id)
	{
		AnalyticsConstants.reportMikeDisable();
		//decreaseCurrentAttendeeAudios();
		
		String url = this.commandsFactory.getSetAudioPermissionUrl(id,false);
		//Window.alert(url);
		this.executeCommand(url);
	}
	/**
	 * Video permission set commands.
	 */
	public	void	enableVideoFor(String id)
	{
		String url = this.commandsFactory.getSetVideoPermissionUrl(id,true);
		//increaseCurrentAttendeeVideos();
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	disableVideoForAll()
	{
		String url = this.commandsFactory.getSetVideoPermissionForAllUrl(false);
		this.currentAttendeeVideos = 0;
		//Window.alert(url);
		this.executeCommand(url);
	}
	public	void	disableVideoFor(String id)
	{
		//decreaseCurrentAttendeeVideos();
		String url = this.commandsFactory.getSetVideoPermissionUrl(id,false);
		//Window.alert(url);
		this.executeCommand(url);
	}
	/**
	 * Conference events handling methods. The naming convention of these
	 * methods is decided by the real strings defined in the IConferenceConstants
	 * interface. These names must never be changed except due to a change
	 * in the said file.
	 * 
	 * Roster event set:
	 * 
	 * roster.roster
	 * roster.userArrived
	 * roster.entryGranted
	 * roster.entryDenied
	 * roster.joined
	 * roster.left
	 * roster.lobbyEnabled
	 * roster.lobbyDisabled
	 * roster.entryChanged
	 * roster.roleChanged
	 * 
	 * @param data
	 */
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert("Received event:"+eventId+", data:"+data.toString());
		String eventMethod = eventId;
		int index = eventId.lastIndexOf(".");
		if (index > 0)
		{
			eventMethod = eventId.substring(index+1);
		}
		//Window.alert("triggering event:"+eventMethod);
//		actOnEvent(eventMethod,data);
		
		if (eventMethod.equalsIgnoreCase("roster"))
		{
			onroster(data);
		}
		else if (eventMethod.equalsIgnoreCase("userArrived"))
		{
			onuserArrived(data);
		}
		else if (eventMethod.equalsIgnoreCase("entryGranted"))
		{
			onentryGranted(data);
		}
		else if (eventMethod.equalsIgnoreCase("entryDenied"))
		{
			onentryDenied(data);
		}
		else if (eventMethod.equalsIgnoreCase("joined"))
		{
			onjoined(data, true);
		}
		else if (eventMethod.equalsIgnoreCase("rejoined"))
		{
			onrejoined(data);
		}
		else if (eventMethod.equalsIgnoreCase("left"))
		{
			onleft(data);
		}
		else if (eventMethod.equalsIgnoreCase("removed"))
		{
			onremoved(data);
		}
		else if (eventMethod.equalsIgnoreCase("roleChanged"))
		{
			onroleChanged(data);
		}
		else if (eventMethod.equalsIgnoreCase("entryChanged"))
		{
			onentryChanged(data);
		}
		else if (eventMethod.equalsIgnoreCase("disableAllChat"))
		{
			ondisableAllChat(data);
		}
		else if (eventMethod.equalsIgnoreCase("disableChat"))
		{
			ondisableChat(data);
		}
		else if (eventMethod.equalsIgnoreCase("enableAllChat"))
		{
			onenableAllChat(data);
		}
		else if (eventMethod.equalsIgnoreCase("enableChat"))
		{
			onenableChat(data);
		}
		else if (eventMethod.equalsIgnoreCase("disableAllAudio"))
		{
			ondisableAllAudio(data);
		}
		else if (eventMethod.equalsIgnoreCase("disableAudio"))
		{
			ondisableAudio(data);
		}
		else if (eventMethod.equalsIgnoreCase("enableAudio"))
		{
			onenableAudio(data);
		}
		else if (eventMethod.equalsIgnoreCase("emailOK"))
		{
			onemailOK(data);
		}
		else if (eventMethod.equalsIgnoreCase("emailError"))
		{
			onemailError(data);
		}
		
		else if (eventMethod.equalsIgnoreCase("disableAllVideo"))
		{
			ondisableAllVideo(data);
		}
		else if (eventMethod.equalsIgnoreCase("disableVideo"))
		{
			ondisableVideo(data);
		}
		else if (eventMethod.equalsIgnoreCase("enableVideo"))
		{
			onenableVideo(data);
		}
		
		// Window.alert("Trigger worked?");
	}
	public	native	void	actOnEvent(String eventMethod, Object data)/*-{
		this[eventMethod](data);
	}-*/;
	public	void	onroster(Object data)
	{
		
		//the if else condition was not quite clear  commented the if else for now but have to look into it once
		//this could be probably to make the host join at the last, instead now making him join him first
		ArrayList oldUsers = (ArrayList)data;
		int	size = oldUsers.size();
		//UIRosterEntry host = null;
		for (int i=0; i<size; i++)
		{
			UIRosterEntry oldUser = (UIRosterEntry)oldUsers.get(i);
			oldUser.setJoinedStatus(true);
			//if (oldUser.isHost())
			//{
			//	host = oldUser;
			//}
			//else
			//{
				//Window.alert("joining user ="+oldUser);
				onjoined(oldUser, false);
			//}
		}
		//if (host != null)
		//{
			//Window.alert("joining host ="+host);
		//	onjoined(host);
		//}
	}
	public	void	onjoined(Object data, boolean considerAudio)
	{

		UIRosterEntry newUser = (UIRosterEntry)data;
		UIRosterEntry ourEntry = findRosterEntry(newUser.getUserId());

		//Window.alert("In RosterModel:onjoined: received:"+newUser.getDisplayName());
		//Window.alert("In RosterModel:onjoined: received:"+newUser);
		
		if (ourEntry == null)
		{
			this.objects.add(newUser);
			this.objectsMap.put(newUser.getUserId(), newUser);
		}
		else
		{
			ourEntry.refreshWithChanges(newUser);
			newUser = ourEntry;
		}
		if(considerAudio)
		{
			if (newUser.isAudioOn())
			{
				increaseCurrentAttendeeAudios();
			}else if (newUser.isVideoOn())
			{
				increaseCurrentAttendeeVideos();
			}
		}
		
		if (newUser.getUserId().equals(this.getUserId()))
		{
			this.currentUser.refreshWithChanges(newUser);
		}
		if (newUser.getRole().equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
		{
//			Window.alert("In RosterModel:onjoined: received active presenter:"+data);
			this.currentActivePresenter.setUserId(newUser.getUserId());
			this.currentActivePresenter.refreshWithChanges(newUser);
//			Window.alert("In RosterModel:onjoined: current active presenter:"+currentActivePresenter);
		}
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RosterModelListener)iter.next()).onUserJoined(newUser);
		}
		
		// Send Notification to everyone that User has joined
		
		/*Window.alert("User Joined");
		if(userJoined != newUser.getDisplayName())
			this.sendChatMessage(null,newUser.getDisplayName()+" has joined");
		
		userJoined = newUser.getDisplayName();
		
		*/
		//Window.alert("newUser.getStatus() = "+newUser.getStatus());
		if (newUser.getStatus().equals(UIRosterEntry.STATUS_HOST))
		{
			this.currentHost.setUserId(newUser.getUserId());
			this.currentHost.refreshWithChanges(newUser);
		}
		if (newUser.getStatus().equals(UIRosterEntry.STATUS_PREVIOUS_HOST))
		{
			//	This could happen only when the new host console receives the
			//	initial roster list.
			this.onleft(newUser);
		}
	}
	public	void	onrejoined(Object data)
	{
		UIRosterEntry newUser = (UIRosterEntry)data;
		UIRosterEntry ourEntry = findRosterEntry(newUser.getUserId());
//		boolean	newConsole = this.objects.size() == 0;
//		Window.alert("In RosterModel:onjoined: received:"+newUser.getDisplayName());
		
		if (newUser.getStatus().equals(UIRosterEntry.STATUS_HOST))
		{
			//	Remove the currentHost on all the consoles except the new host's/
			//	New hosts console is identified as the one with empty roster.
//			if (!newConsole)
//			{
				UIRosterEntry oldHostEntry = findCurrentHost();
				if (oldHostEntry != null)
				{
					this.objects.remove(oldHostEntry);
					this.objectsMap.remove(oldHostEntry.getUserId());
					Iterator iter = this.listeners.values().iterator();
					while (iter.hasNext())
					{
						((RosterModelListener)iter.next()).onUserLeft(oldHostEntry);
					}
				}
//			}
			this.currentHost.setUserId(newUser.getUserId());
			this.currentHost.refreshWithChanges(newUser);
		}
		if (ourEntry == null)
		{
			this.objects.add(newUser);
			this.objectsMap.put(newUser.getUserId(), newUser);
		}
		else
		{
			ourEntry.refreshWithChanges(newUser);
			newUser = ourEntry;
		}
		if (newUser.getUserId().equals(this.getUserId()))
		{
			this.currentUser.refreshWithChanges(newUser);
		}
		if (newUser.getRole().equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
		{
			this.currentActivePresenter.setUserId(newUser.getUserId());
			this.currentActivePresenter.refreshWithChanges(newUser);
		}
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RosterModelListener)iter.next()).onUserRejoined(newUser);
		}
	}
	public	void	onleft(Object data)
	{
//		Window.alert("In RosterModel:onleft: received:"+data);
		UIRosterEntry user = (UIRosterEntry)data;
		UIRosterEntry ourEntry = findRosterEntry(user.getUserId());
		if (ourEntry != null)
		{
			if (ourEntry.isAudioOn())
			{
				decreaseCurrentAttendeeAudios();
			}
			if (ourEntry.isVideoOn())
			{
				decreaseCurrentAttendeeVideos();
			}
			this.objects.remove(ourEntry);
			this.objectsMap.remove(ourEntry.getUserId());
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((RosterModelListener)iter.next()).onUserLeft(ourEntry);
			}
		}
		
		//this.sendChatMessage(null,user.getDisplayName()+" has left");
				
	}
	public	void	onremoved(Object data)
	{
//		Window.alert("In RosterModel:onremoved: received:"+data);

		UIRosterEntry user = (UIRosterEntry)data;
		UIRosterEntry ourEntry = findRosterEntry(user.getUserId());
		if (ourEntry != null)
		{
			this.objects.remove(ourEntry);
			this.objectsMap.remove(ourEntry.getUserId());
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((RosterModelListener)iter.next()).onUserRemoved(ourEntry);
			}
		}
	}
	public	void	onuserArrived(Object data)
	{
		UIRosterEntry newUser = (UIRosterEntry)data;
		this.objects.add(newUser);
		this.objectsMap.put(newUser.getUserId(), newUser);
		if (newUser.getUserId().equals(this.getUserId()))
		{
			this.currentUser.refreshWithChanges(newUser);
		}
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RosterModelListener)iter.next()).onUserArrived(newUser);
		}
	}
	
	//this is to be used in case of participant list is disabled 
	public void addToRoster(UIRosterEntry user)
	{
		objects.add(user);
		objectsMap.put(user.getUserId(), user);
	}
	
	public	void	onentryGranted(Object data)
	{
//		Window.alert("1");
		UIRosterEntry user = (UIRosterEntry)data;
//		Window.alert("2"+user.getUserId());
		UIRosterEntry ourEntry = findRosterEntry(user.getUserId());
//		Window.alert("3");
		if (user.getUserId().equals(this.getUserId()))
		{
//			Window.alert("5");
			this.currentUser.refreshWithChanges(user);
//			Window.alert("6");
			if (ourEntry == null)
			{
				objects.add(user);
				objectsMap.put(user.getUserId(), user);
				ourEntry = findRosterEntry(user.getUserId());
			}
		}
		if (ourEntry != null)
		{
//			Window.alert("4");
			if (user.getUserId().equals(this.getUserId()))
			{
//				Window.alert("5");
				this.currentUser.refreshWithChanges(user);
//				Window.alert("6");
			}
//			Window.alert("7");
			if (user.getUserId().equals(this.getUserId()))
			{
//				Window.alert("8");
				this.getConsole();
//				Window.alert("9");
			}
//			Window.alert("10");
			Iterator iter = this.listeners.values().iterator();
//			Window.alert("11");
			while (iter.hasNext())
			{
//				Window.alert("12");
				try
				{
//					Window.alert("13");
					((RosterModelListener)iter.next()).onEntryGranted(ourEntry);
//					Window.alert("14");
				}
				catch(Exception e)
				{
//					Window.alert(e.getMessage());
				}
			}
		}
	}
	public	void	onentryDenied(Object data)
	{
		UIRosterEntry user = (UIRosterEntry)data;
		UIRosterEntry ourEntry = findRosterEntry(user.getUserId());
		if (ourEntry != null)
		{
			this.objects.remove(ourEntry);
			this.objectsMap.remove(ourEntry.getUserId());
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((RosterModelListener)iter.next()).onEntryDenied(ourEntry);
			}
		}
	}
	
	private	void	onentryChanged(Object data)
	{
		//Window.alert("In RosterModel:onentryChanged: received:"+data);
		UIRosterEntry user = (UIRosterEntry)data;
		UIRosterEntry ourEntry = findRosterEntry(user.getUserId());
		if (ourEntry != null)
		{
			//Window.alert("before refressing In RosterModel:onentryChanged: our entry:"+ourEntry);
			//Window.alert("In RosterModel:onentryChanged: user:"+user);
			//Window.alert("In RosterModel:onentryChanged: user:"+user.getDisplayName());
			ourEntry.refreshWithChanges(user);
			
			if (user.getUserId().equals(this.getUserId()))
			{
				//Window.alert("In RosterModel:onentryChanged: our entry:"+ourEntry);
				this.currentUser.refreshWithChanges(user);
			}
			if (ourEntry.getRole().equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
			{
				this.currentActivePresenter.setUserId(ourEntry.getUserId());
				this.currentActivePresenter.refreshWithChanges(ourEntry);
			}
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((RosterModelListener)iter.next()).onUserChanged(ourEntry);
			}
			
			if (ourEntry.getStatus().equals(UIRosterEntry.STATUS_HOST))
			{
				this.currentHost.setUserId(ourEntry.getUserId());
				this.currentHost.refreshWithChanges(ourEntry);
			}
		}
	}
	public	void	onemailOK(Object data)
	{
		UIEmailAttemptResult emailResult = (UIEmailAttemptResult)data;
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((RosterModelListener)iter.next()).onEmailOK(emailResult);
			}
	}
	public	void	onemailError(Object data)
	{
		UIEmailAttemptResult emailResult = (UIEmailAttemptResult)data;
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((RosterModelListener)iter.next()).onEmailError(emailResult);
			}
	}
	private void	ondisableAllChat(Object data)
	{
		setChatPermission("0",null);
	}
	private void	ondisableChat(Object data)
	{
		setChatPermission("0",(UIRosterEntry)data);
	}
	private void	onenableAllChat(Object data)
	{
		setChatPermission("1",null);
	}
	private void	onenableChat(Object data)
	{
		setChatPermission("1",(UIRosterEntry)data);
	}
	private void	setChatPermission(String value, UIRosterEntry changedUser)
	{
		DebugPanel.getDebugPanel().addDebugMessage("set setChatPermission changedUser = "+changedUser);
		DebugPanel.getDebugPanel().addDebugMessage("value= "+value);
		UIRosterEntry user = null;
		int	size = this.objects.size();
		for (int i=0; i<size; i++)
		{
			user = (UIRosterEntry)this.objects.get(i);
			if (changedUser == null || user.getUserId().equals(changedUser.getUserId()))
			{
				if (!user.isHost())
				{
					user.setChat(value);
					this.fireChatPermissionsChanged(user);
				}
			}
		}
		if (!this.currentUser.isHost())
		{
			if (changedUser == null || changedUser.getUserId().
					equals(this.currentUser.getUserId()))
			{
				this.currentUser.setChat(value);
				this.fireChatPermissionsChanged(this.currentUser);
			}
		}
	}
	private void	ondisableAllAudio(Object data)
	{
		setAudioPermission("0",null);
	}
	private void	ondisableAudio(Object data)
	{
		setAudioPermission("0",(UIRosterEntry)data);
	}
	private void	onenableAudio(Object data)
	{
		setAudioPermission("1",(UIRosterEntry)data);
	}
	private void	setAudioPermission(String value, UIRosterEntry changedUser)
	{
		DebugPanel.getDebugPanel().addDebugMessage("set audio permission changedUser = "+changedUser);
		DebugPanel.getDebugPanel().addDebugMessage("value= "+value);
		UIRosterEntry user = null;
		int	size = this.objects.size();
		for (int i=0; i<size; i++)
		{
			user = (UIRosterEntry)this.objects.get(i);
			if (changedUser == null || user.getUserId().equals(changedUser.getUserId()))
			{
//				if (!user.isPresenter())
				{
					//Window.alert("before calling fire.. setAudioPermission user = "+user+" value="+value);
					user.setAudio(value);
					this.fireAudioVideoPermissionsChanged(user);
				}
			}
		}
//		if (!this.currentUser.isPresenter())
		{
			if (changedUser == null || changedUser.getUserId().
					equals(this.currentUser.getUserId()))
			{
				//Window.alert("setAudioPermission user = "+user+" value="+value);
				this.currentUser.setAudio(value);
			}
		}
		//this is to change the count of available mics
		if(currentUser.isHost())
		{
			changeCurrentAttendeeAudios(value);
		}
	}
	
	private void	ondisableAllVideo(Object data)
	{
		setVideoPermission("0",null);
	}
	private void	ondisableVideo(Object data)
	{
		setVideoPermission("0",(UIRosterEntry)data);
	}
	private void	onenableVideo(Object data)
	{
		setVideoPermission("1",(UIRosterEntry)data);
	}
	private void	setVideoPermission(String value, UIRosterEntry changedUser)
	{
		DebugPanel.getDebugPanel().addDebugMessage("set video permission changedUser = "+changedUser);
		DebugPanel.getDebugPanel().addDebugMessage("value= "+value);
		UIRosterEntry user = null;
		int	size = this.objects.size();
		for (int i=0; i<size; i++)
		{
			user = (UIRosterEntry)this.objects.get(i);
			if (changedUser == null || user.getUserId().equals(changedUser.getUserId()))
			{
//				if (!user.isPresenter())
				{
					//Window.alert("before calling fire..setVideoPermission user = "+user+" value="+value);
					user.setVideo(value);
					this.fireAudioVideoPermissionsChanged(user);
				}
			}
		}
//		if (!this.currentUser.isPresenter())
		{
			if (changedUser == null || changedUser.getUserId().
					equals(this.currentUser.getUserId()))
			{
				//Window.alert("setVideoPermission user = "+user+" value="+value);
				this.currentUser.setVideo(value);
				//this.firePermissionsChanged(user);
			}
		}
		if(currentUser.isHost() )
		{
			//if(changedUser.isActivePresenter())
			//if user was having an audio player already then disabling it
			//this typically happens when an attendee has audio and you are making him a presenter
			if(changedUser.isAudioOn() && "1".equalsIgnoreCase(value))
			{
				DebugPanel.getDebugPanel().addDebugMessage("if user was having an audio player already then disabling it this typically happens when an attendee has audio and you are making him a presenter");
				changedUser.setAudio("0");
				decreaseCurrentAttendeeAudios();
			}
			changeCurrentAttendeeVideos(value);
		}
	}
	
	protected	void	fireChatPermissionsChanged(UIRosterEntry user)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RosterModelListener)iter.next()).onChatPermissionsChanged(user);
		}
	}
	
	protected	void	fireAudioVideoPermissionsChanged(UIRosterEntry user)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RosterModelListener)iter.next()).onAudioVidoPermissionsChanged(user);
		}
	}

	public	void	onroleChanged(Object data)
	{
		UIRosterEntry user = (UIRosterEntry)data;
		UIRosterEntry ourEntry = findRosterEntry(user.getUserId());
		//DebugPanel.getDebugPanel().addDebugMessage("inside role changed data ="+data);
		//DebugPanel.getDebugPanel().addDebugMessage("audios available ="+getAvailableAttendeeAudios()+" videos available = "+getAvailableAttendeeVideos());
		if (ourEntry != null)
		{
			ourEntry.refreshWithChanges(user);
			if (user.getUserId().equals(this.getUserId()))
			{
				this.currentUser.refreshWithChanges(user);
			}
			if (user.getRole().equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
			{
				this.currentActivePresenter.setUserId(user.getUserId());
				this.currentActivePresenter.refreshWithChanges(user);
			}
		}
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RosterModelListener)iter.next()).onUserRoleChanged(user);
		}
	}
	protected	String		getPopoutJsonEventName()
	{
		return	"roster.roster";
	}
	public	UIRosterEntry	findRosterEntry(String userId)
	{
//		Window.alert("Searching for:"+userId);
		UIRosterEntry user = (UIRosterEntry)this.objectsMap.get(userId);
//		int	size = this.objects.size();
//		for (int i=0; i<size; i++)
//		{
//			user = (UIRosterEntry)this.objects.get(i);
////			Window.alert("We have:"+user.getUserId());
//			if (user.getUserId().equals(userId))
//			{
//				break;
//			}
//			else
//			{
//				user = null;
//			}
//		}
		return	user;
	}
	public	UIRosterEntry	findCurrentHost()
	{
//		Window.alert("Searching for:"+userId);
		UIRosterEntry user = null;
		int	size = this.objects.size();
		for (int i=0; i<size; i++)
		{
			user = (UIRosterEntry)this.objects.get(i);
//			Window.alert("We have:"+user.getUserId());
			if (user.isHost())
			{
				break;
			}
			else
			{
				user = null;
			}
		}
		return	user;
	}
	private String getUserId()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_id");
	}
	private String getUserRole()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_role");
	}
	private String getUserStatus()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_status");
	}
	private String getUserName()
	{
		String s = ConferenceGlobals.userInfoDictionary.getStringValue("user_name");
		return	UIRosterEntry.decodeBase64(s);
	}
	private String getUserMood()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_mood");
	}
	private String getNetProfile()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("net_profile");
	}
	private String getImgQuality()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("img_quality");
	}
	private String getMaxAttendeeAudios()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_audios");
	}
	private String getMaxAttendeeVideos()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_videos");
	}
	public UIRosterEntry getCurrentHost() {
		return currentHost;
	}
}
