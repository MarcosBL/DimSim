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

import	java.util.HashMap;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The state machine accounts for some of the features that are not in
 * current version, but will be implemented soon.
 */

public class UserRosterStateMachine
{
	//	States
	
	public	static	final	int	STATE_NOT_IN_CONFERENCE = 0;
	public	static	final	int	STATE_JOINING = 1;
	public	static	final	int	STATE_WAITING_IN_LOBBY = 2;
	public	static	final	int	STATE_LOADING_CONSOLE = 3;
	public	static	final	int	STATE_IN_CONFERENCE = 4;
	public	static	final	int	STATE_IN_CONFERENCE_PRESENTER = 5;
	public	static	final	int	STATE_IN_CONFERENCE_ATTENDEE = 6;
	public	static	final	int	STATE_IN_CONFERENCE_ACTIVE_PRESENTER = 7;
	public	static	final	int	STATE_LEAVING_CONFERENCE = 8;
	public	static	final	int	STATE_LEFT_CONFERENCE = 9;
	public	static	final	int	STATE_ENTRY_DENIED = 10;
	public	static	final	int	STATE_ENTRY_GRANTED = 11;
	public	static	final	int	STATE_REMOVED_FROM_CONFERENCE = 12;
	public	static	final	int	STATE_REJOINING = 13;
	public	static	final	int	STATE_RELOADING = 14;
	
	//	Events - incoming
	
//	public	static	final	int	EVENT_JOINING = 0;
//	public	static	final	int	EVENT_DENY_ENTRY = 1;
//	public	static	final	int	EVENT_ALLOW_ENTRY = 2;
//	public	static	final	int	EVENT_CONSOLE_LOADED = 3;
//	public	static	final	int	EVENT_LEAVING_CONFERENCE = 4;
//	public	static	final	int	EVENT_REMOVE_FROM_CONFERENCE = 5;
	
	//	Events - raised by roster
	
	public	static	final	int	EVENT_NEW_USER_IN_LOBBY = 6;
	
	//	Transitions and effects there of.
	
	
/**
 * Legal Transitions -> (ided transition)
 * 
 * STATE_JOINING -> STATE_IN_CONFERENCE, STATE_LEFT_CONFERENCE
 * STATE_IN_CONFERENCE -> STATE_LEFT_CONFERENCE
 * 
 * Transition Events ->
 * 
 * JOINING -> IN_CONFERENCE
 * 		USER_ROSTER_EVENT -> new user
 * 		RESOURCE_ROSTER_EVENT -> new user
 * 		RESOURCE_SELECT -> new user
 * 		RESOURCE_CONTROL -> new user
 * 		AV_CONTROL -> new user
 *		USER_JOINED -> rest 
 * IN_CONFERENCE -> LEFT_CONFERENCE
 */
	
	protected	UserRoster	userRoster;
	protected	HashMap		statesMap;
	protected	ParticipantChangeListenerCollection	pclc;
	
	public	UserRosterStateMachine(UserRoster userRoster)
	{
		this.userRoster = userRoster;
		this.statesMap = new HashMap();
		this.pclc = userRoster.getParticipantChangeListenerCollection();
	}
	private	boolean	isStateInConference(int	state)
	{
		return	(state == UserRosterStateMachine.STATE_IN_CONFERENCE ||
				 state == UserRosterStateMachine.STATE_IN_CONFERENCE_PRESENTER ||
				 state == UserRosterStateMachine.STATE_IN_CONFERENCE_ACTIVE_PRESENTER ||
				 state == UserRosterStateMachine.STATE_IN_CONFERENCE_ATTENDEE);
	}
	/**
	 * Part of the meeting lobby management and new change listener interface.
	 */
	public	void	changeStateAndRaiseEvents(Participant user, int newState)
	{
		int	currentState = user.getCurrentState();
		System.out.println("Checking "+user.getId()+", current state:"+currentState+", against new state: "+newState);
		if (currentState == UserRosterStateMachine.STATE_JOINING)
		{
			if (this.isStateInConference(newState))
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireJoinedConference(user);
			}
			else if (newState == STATE_WAITING_IN_LOBBY)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				user.getEventReceiver().setEventFilter(new WaitingInLobbyEventFilter());
				this.pclc.fireArrivedInLobby(user);
			}
		}
		else if (currentState == UserRosterStateMachine.STATE_REJOINING)
		{
			if (this.isStateInConference(newState))
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireRejoinedConference(user);
			}
			else if (newState == STATE_WAITING_IN_LOBBY)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				user.getEventReceiver().setEventFilter(new WaitingInLobbyEventFilter());
				this.pclc.fireArrivedInLobby(user);
			}
		}
		else if (currentState == UserRosterStateMachine.STATE_RELOADING)
		{
			if (this.isStateInConference(newState))
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireReloadedConsole(user);
			}
		}
		else if (currentState == STATE_WAITING_IN_LOBBY)
		{
			if (newState == STATE_ENTRY_GRANTED)
			{
				user.getEventReceiver().removeEventFilter();
				user.setMood(Mood.Normal);
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireGrantedEntry(user);
			}
			else if (newState == STATE_ENTRY_DENIED)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireDeniedEntry(user);
			}
			else if (newState == STATE_LEFT_CONFERENCE)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireLeftConference(user);
			}
			else if (newState == STATE_REMOVED_FROM_CONFERENCE)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireRemovedFromConference(user);
			}
		}
		else if (currentState == STATE_ENTRY_GRANTED)
		{
			if (this.isStateInConference(newState))
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireJoinedConference(user);
			}
		}
		else if (this.isStateInConference(currentState))
		{
			/**
			 * This block could get large. It will have deal with the role
			 * based state changes. Transition between attendee and presenter
			 * roles is not yet supported.
			 */
			if (newState == STATE_LEFT_CONFERENCE)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireLeftConference(user);
			}
			else if (newState == STATE_REMOVED_FROM_CONFERENCE)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireRemovedFromConference(user);
			}
			else if (newState == STATE_IN_CONFERENCE_ACTIVE_PRESENTER)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireBecameActivePresenter(user);
			}
			else if (newState == STATE_IN_CONFERENCE_PRESENTER)
			{
				userRoster.setUserPermissions(user,newState);
				user.setCurrentState(newState);
				this.pclc.fireBecamePresenter(user);
			}
		}
	}
}
