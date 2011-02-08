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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : Roster.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.model;

import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.Date;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */

public	class	UserRoster	extends	ConferenceFeature	implements	IRoster, ParticipantStateChangeListener
{
	/**
	 * Organizer if the participant who started the meeting, there can be only one
	 * and that cannot change during the lifetime of the meeting.
	 */
	private	int		userCounter = 0;
	private	IConference	activeConference;
	private	Participant	previousOrganizer = null;
	private Participant organizer = null;
	private Participant activePresenter = null;
	
	private	HashMap	usersJoining = new HashMap();
	private	HashMap	participants = new HashMap();
	private	Vector	participantsList = new Vector();
	
	private	UserRosterStateMachine			userRosterStateMachine;
//	private	ParticipantStateChangeListener	participantStateChangeListener;
	
	private	ParticipantChangeListenerCollection	pclc;
	private	IAudioVideoManager	avResourceManager;
	
	/**
	 * 
	 */
	public UserRoster(IConference activeConference)
	{
		super();
		this.activeConference = activeConference;
		this.pclc = new ParticipantChangeListenerCollection();
		userRosterStateMachine = new UserRosterStateMachine(this);
	}
	public ParticipantChangeListenerCollection getParticipantChangeListenerCollection()
	{
		return pclc;
	}
	public	void	addParticipantChangeListener(IParticipantChangeListener ipcl)
	{
		this.pclc.addParticipantChangeListener(ipcl);
	}
	public	void	removeParticipantChangeListener(IParticipantChangeListener ipcl)
	{
		this.pclc.removeParticipantChangeListener(ipcl);
	}
	public Participant getOrganizer()
	{
		return organizer;
	}
	public Participant getPreviousOrganizer()
	{
		return previousOrganizer;
	}
	public void setOrganizer(Participant organizer)
	{
		this.previousOrganizer = this.organizer;
		this.organizer = organizer;
	}
	public IAudioVideoManager getAvResourceManager()
	{
		return avResourceManager;
	}
	public void setAvResourceManager(IAudioVideoManager avResourceManager)
	{
		this.avResourceManager = avResourceManager;
	}
	public	Participant	getActivePresenter()
	{
		return	this.activePresenter;
	}
	public void setActivePresenter(Participant activePresenter)
	{
		this.activePresenter = activePresenter;
	}
//	public ParticipantStateChangeListener getParticipantStateChangeListener()
//	{
//		return participantStateChangeListener;
//	}
//	public void setParticipantStateChangeListener(
//			ParticipantStateChangeListener participantStateChangeListener)
//	{
//		this.participantStateChangeListener = participantStateChangeListener;
//	}
	public IRosterEntry getRosterEntry(String userId)
	{
		return (IRosterEntry)this.getParticipantFromList(userId);
	}
	public	synchronized	int	getUserCounter()
	{
		return	this.userCounter++;
	}
	
	/**
	 * Simple check keep the exception creation in one place.
	 * @param email
	 * @throws UserInConferenceException
	 */
//	public	void	throwExceptionIfUserJoined(String email)	throws	UserInConferenceException
//	{
//		if (getParticipantFromList(email) != null || this.usersJoining.get(email) != null)
//		{
//			throw	new	UserInConferenceException();
//		}
//	}
//	public	void	throwExceptionIfUserNotInConference(String email)	throws	UserNotInConferenceException
//	{
//		if (getParticipantFromList(email) == null && this.usersJoining.get(email) == null)
//		{
//			throw	new	UserNotInConferenceException();
//		}
//	}
	
	public  IConferenceParticipant	addParticipant(String email, String displayName,
			String password, String role, boolean lobbyEnabled, String userId)	throws	UserInConferenceException
	{
//		throwExceptionIfUserJoined(email);
		
		Participant user = new Participant(getUserCounter(), email, displayName, password, role, userId);
		if (role.equals(ConferenceConstants.ROLE_ATTENDEE) && lobbyEnabled)
		{
			user.setMood(Mood.WaitingInLobby);
		}
		this.usersJoining.put(user.getId(),user);
		
		return	user;
	}
	private	void	moveParticipantFromJoiningToRoster(String email)
	{
		Participant user = (Participant)this.usersJoining.get(email);
		if (user != null)
		{
			this.usersJoining.remove(email);
			if (user.getRole().equals(ConferenceConstants.ROLE_ACTIVE_PRESENTER))
			{
				this.activePresenter = user;
			}
			/**
			 * The new participant is not added to the event publisher due to the
			 * workflow required at the higher level. A seperate client side action
			 * is required to ensure that the new user is added to the event publisher
			 * only after certain events are dispatched the rest of the current
			 * attendees.
			 */
			this.participantsList.add(user);
			this.participants.put(user.getId(),user);
		}
	}
	public	void	makeActivePresenter(String email)
		throws	UserNotInConferenceException
	{
		Participant user = (Participant)getRosterEntry(email);
		if(user != null)
		{
			this.userRosterStateMachine.changeStateAndRaiseEvents(
					this.activePresenter,
					UserRosterStateMachine.STATE_IN_CONFERENCE_PRESENTER);
			
			this.activePresenter = user;
			this.userRosterStateMachine.changeStateAndRaiseEvents(user,
					UserRosterStateMachine.STATE_IN_CONFERENCE_ACTIVE_PRESENTER);
		}
	}
	public	void	makePresenter(String email)
		throws	UserNotInConferenceException
	{
		Participant user = (Participant)getRosterEntry(email);
		if(user != null)
		{
			this.userRosterStateMachine.changeStateAndRaiseEvents(
					this.activePresenter,
					UserRosterStateMachine.STATE_IN_CONFERENCE_PRESENTER);
			
			this.activePresenter = (Participant)organizer;
			this.userRosterStateMachine.changeStateAndRaiseEvents(this.activePresenter,
					UserRosterStateMachine.STATE_IN_CONFERENCE_ACTIVE_PRESENTER);
		}
	}
	public	IConferenceParticipant	updateParticipantMood(String email,
				String mood)	throws	UserNotInConferenceException
	{
		Participant user = (Participant)getRosterEntry(email);
		if(user != null)
		{
			user.setMood(mood);
			
			/**
			 * Raise events to local listeners and the remote clients.
			 */
			if (this.clientEventPublisher != null)
			{
				Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
						ConferenceConstants.EVENT_PARTICIPANT_STATUS_CHANGE,
						new Date(), ConferenceConstants.RESPONSE_OK, user );
				
				this.clientEventPublisher.dispatchEventToAllClients(event);
			}
		}
		else
		{
			throw	new	UserNotInConferenceException();
		}
		return	user;
	}
	public	IConferenceParticipant	updateParticipantPhoto(String email,
				String photo)	throws	UserNotInConferenceException
	{
		Participant user = (Participant)getRosterEntry(email);
		if(user != null)
		{
			user.setPhoto(photo);
			
			/**
			 * Raise events to local listeners and the remote clients.
			 */
			if (this.clientEventPublisher != null)
			{
				Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
						ConferenceConstants.EVENT_PARTICIPANT_STATUS_CHANGE,
						new Date(), ConferenceConstants.RESPONSE_OK, user );
				
				this.clientEventPublisher.dispatchEventToAllClients(event);
			}
		}
		else
		{
			throw	new	UserNotInConferenceException();
		}
		return	user;
	}
	
	public	IConferenceParticipant	updateDisplayName(String email,
			String name)	throws	UserNotInConferenceException
{
	Participant user = (Participant)getRosterEntry(email);
	if(user != null)
	{
		user.setDisplayName(name);
		
		/**
		 * Raise events to local listeners and the remote clients.
		 */
		if (this.clientEventPublisher != null)
		{
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_STATUS_CHANGE,
					new Date(), ConferenceConstants.RESPONSE_OK, user );
			
			this.clientEventPublisher.dispatchEventToAllClients(event);
		}
	}
	else
	{
		throw	new	UserNotInConferenceException();
	}
	return	user;
}
	
	public	IConferenceParticipant	removeParticipant(String userId)
	{
		return	this.removeParticipantFromList(userId);
	}
	public	IConferenceParticipant	getParticipant(String userId)
	{
		IConferenceParticipant user = (IConferenceParticipant)this.usersJoining.get(userId);
		if (user == null)
		{
			user = this.getParticipantFromList(userId);
		}
		return	user;
	}
	protected	IConferenceParticipant	getParticipantFromList(String userId)
	{
		IConferenceParticipant p = (IConferenceParticipant)this.participants.get(userId);
		return	p;
	}
	protected	IConferenceParticipant	removeParticipantFromList(String userId)
	{
		Participant user = null;
		int	size = this.participantsList.size();
		for (int i=0; i<size; i++)
		{
			user = (Participant)this.participantsList.elementAt(i);
			if (user.getId().equals(userId))
			{
				this.participantsList.remove(user);
				this.participants.remove(userId);
				this.avResourceManager.broadroadcasterRemoved(user);
				this.clientEventPublisher.removeEventReceiver(user.getEventReceiver());
				
				System.out.println("Removing user:"+userId);
				if (userId.equals(this.activePresenter.getId()) && !userId.equals(this.organizer.getId()))
				{
					//	The active presenter has either left or crashed. Two actions are required at
					//	this point.
					
					//	One - Raise stop resource sharing event if a resource is being shared actively
					//	at present.
					this.activeConference.forceStopActiveSharing(user);
					
					//	Two - make the organizer the active presenter.
					System.out.println("Current Active Presenter left. Handing control back to organizer");
					this.activePresenter = (Participant)organizer;
					this.userRosterStateMachine.changeStateAndRaiseEvents(this.activePresenter,
							UserRosterStateMachine.STATE_IN_CONFERENCE_ACTIVE_PRESENTER);
				}
				break;
			}
			else
			{
				user = null;
			}
		}
		return	user;
	}
	public	int		getNumberOfPresenters()
	{
		int	num = 0;
		IConferenceParticipant p = null;
		int	size = this.participantsList.size();
		for (int i=0; i<size; i++)
		{
			p = (IConferenceParticipant)this.participantsList.elementAt(i);
			if (p.isPresenter())
			{
				num++;
			}
		}
		return	num;
	}
	public	int		getNumberOfParticipants()
	{
		return	this.participantsList.size();
	}
	
	/**
	 * This would return number of participants who already joined + joining users
	 * @return
	 */
	public	int		getTotalNumberOfParticipants()
	{
		return	this.participantsList.size() + this.usersJoining.size();
	}
	public	List	getParticipants()
	{
		return	this.participantsList;
	}
	public	List	getAllPresenters()
	{
		return	this.getParticipants(ConferenceConstants.ROLE_PRESENTER);
	}
	public	List	getParticipants(String role)
	{
		Vector list = new Vector();
		IConferenceParticipant p = null;
		int	size = this.participantsList.size();
		for (int i=0; i<size; i++)
		{
			p = (IConferenceParticipant)this.participantsList.elementAt(i);
			if (p.getRole().equals(role))
			{
				list.addElement(p);
			}
		}
		return	list;
	}
	
	public	String	createParticipantChild(IConferenceParticipant user)
	{
		return	null;
	}
	
	/**
	 * This method sets the participant permissions associated with the state
	 * changes. At present only permission in use is audio. This method is
	 * called only when the state change is decided to be valid and before
	 * the state change itself. This is because the state change itself will
	 * trigger the events to be dispatched to other participants.
	 */
	public	void	setUserPermissions(Participant user, int newState)
	{
		if (newState == UserRosterStateMachine.STATE_IN_CONFERENCE)
		{
			if (this.organizer != null)
			{
				//	Otherwise it would mean that this user is the organizer
				if (!user.getId().equals(this.organizer.getId()))
				{
					//if participant list is not enabled then do not assign 
					if(this.activeConference.isAssignMikeOnJoin() && this.activeConference.isParticipantListEnabled())
					{
						if(this.avResourceManager.isVideoBroadcasterAvailable())
						{
							this.avResourceManager.videoBroadcasterAdded(user);
						}else if(this.avResourceManager.isAudioBroadcasterAvailable())
						{
							this.avResourceManager.audioBroadcasterAdded(user);
						}
					}
					//	Audio permissions apply only to the attendees. The organizer
					//	always has a special status and can take control of the
					//	meeting any time.
					//user.getPermissions().setAudioOn();
					
				}
			}
		}
	}
	/**
	 * Participant state machine change listener.
	 */
	public void participantInConference(Participant user, int oldState)
	{
		this.moveParticipantFromJoiningToRoster(user.getId());
	}
	public void participantLeftConference(Participant user, int oldState)
	{
		System.out.println("User: "+user.displayName+", leaving conference");
		if (oldState == UserRosterStateMachine.STATE_JOINING)
		{
			this.usersJoining.remove(user.getId());
		}
		else if (user.isInConference() || user.isInLobby())
		{
			//	Dispatch the user left event to 
			this.removeParticipant(user.getId());
		}
	}
	/**
	 * Part of the new implementations for the meeting lobby management.
	 */
	/**
	 * If the lobby is enabled raise user arrived else add the user and raise
	 * joined. Use the state machine to verify the state changes. It is possible
	 * to receive multiple commands as two presenters could try to accept or
	 * deny at the same time.
	 */
	public	void	participantConsoleLoaded(IConferenceParticipant user, boolean lobbyEnabled)
	{
		System.out.println("Current State: "+((Participant)user).getCurrentState());
		this.moveParticipantFromJoiningToRoster(user.getId());
		if (user.getRole().equals(ConferenceConstants.ROLE_ACTIVE_PRESENTER))
		{
			//	This is the first user who started the conference requesting
			//	console. Accept irrespective of the lobby settings.
			this.userRosterStateMachine.changeStateAndRaiseEvents((Participant)user,
					UserRosterStateMachine.STATE_IN_CONFERENCE);
		}
		else
		{
			if (lobbyEnabled)
			{
				this.userRosterStateMachine.changeStateAndRaiseEvents((Participant)user,
						UserRosterStateMachine.STATE_WAITING_IN_LOBBY);
			}
			else
			{
				this.userRosterStateMachine.changeStateAndRaiseEvents((Participant)user,
						UserRosterStateMachine.STATE_IN_CONFERENCE);
			}
		}
	}
	/**
	 * Console can be reloaded only by in meeting participants. Accept the load irrespective
	 * of the lobby settings.
	 * 
	 * @param user
	 * @param lobbyEnabled
	 */
	public	void	participantConsoleReloaded(IConferenceParticipant user, boolean lobbyEnabled)
	{
		System.out.println("Current State: "+((Participant)user).getCurrentState());
		
		this.userRosterStateMachine.changeStateAndRaiseEvents((Participant)user,
				UserRosterStateMachine.STATE_IN_CONFERENCE);
	}
	public	void	acceptedUserRequestingConsole(IConferenceParticipant user)
	{
		System.out.println("Current State: "+((Participant)user).getCurrentState());
		this.userRosterStateMachine.changeStateAndRaiseEvents((Participant)user,
				UserRosterStateMachine.STATE_IN_CONFERENCE);
	}
	public	void	grantUserEntry(String userId)
	{
		Participant user = (Participant)this.getParticipant(userId);
		System.out.println("Current State: "+((Participant)user).getCurrentState());
		if (user != null)
		{
			this.userRosterStateMachine.changeStateAndRaiseEvents(user,
					UserRosterStateMachine.STATE_ENTRY_GRANTED);
		}
	}
	public	void	grantAllUsersEntry()
	{
		int	size = this.participantsList.size();
		for (int i=0; i<size; i++)
		{
			Participant user = (Participant)this.participantsList.elementAt(i);
			this.userRosterStateMachine.changeStateAndRaiseEvents(user,
					UserRosterStateMachine.STATE_ENTRY_GRANTED);
		}
	}
	public	void	denyUserEntry(String userId)
	{
		Participant user = (Participant)this.getParticipant(userId);
		if (user != null)
		{
			this.userRosterStateMachine.changeStateAndRaiseEvents(user,
					UserRosterStateMachine.STATE_ENTRY_DENIED);
			this.removeParticipantFromList(userId);
		}
	}
	public	void	participantLeaving(IConferenceParticipant user)
	{
		Participant participant = (Participant)user;
		if (participant.getCurrentState() == UserRosterStateMachine.STATE_JOINING)
		{
			this.usersJoining.remove(user.getId());
		}
		else if (participant.isInConference() || participant.isInLobby())
		{
			//	Dispatch the user left event to 
			this.removeParticipant(user.getId());
		}
		this.userRosterStateMachine.changeStateAndRaiseEvents(participant,
				UserRosterStateMachine.STATE_LEFT_CONFERENCE);
	}
	public	void	participantRemoved(IConferenceParticipant user)
	{
		Participant participant = (Participant)user;
		if (participant.getCurrentState() == UserRosterStateMachine.STATE_JOINING)
		{
			this.usersJoining.remove(user.getId());
		}
		else if (participant.isInConference() ||
				participant.getCurrentState() == UserRosterStateMachine.STATE_WAITING_IN_LOBBY)
		{
			//	Dispatch the user left event to 
			this.userRosterStateMachine.changeStateAndRaiseEvents(participant,
					UserRosterStateMachine.STATE_REMOVED_FROM_CONFERENCE);
			this.removeParticipant(user.getId());
		}
	}
}

