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

package com.dimdim.conference.application.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IParticipantChangeListener;
import com.dimdim.conference.model.IClientEventPublisher;
import com.dimdim.conference.model.IResourceObject;
import com.dimdim.conference.model.Participant;
import com.dimdim.conference.model.ParticpantListFilter;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.application.portal.PortalServerAdapter;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This helper class encapsulates all the events dispatches to the participants.
 * All the events are strictsly dispatched based on the state changes for either
 * conference or an individual participant.
 * 
 * The helper requires access to all the data the active conference has. The
 * objective behind the helper is that the major objects analyse the current
 * state and new data and effect the state change. This object does the
 * subsequent required work that must be done as a result of the said state
 * change.
 */

public class ParticipantStateChangeHelper	implements IParticipantChangeListener
{
	protected	ActiveConference	activeConference;
	protected	IClientEventPublisher	clientEventsPublisher;
	protected	HashMap		userSessions;
	int maxObjectsInEvent = -1;
	
	public	ParticipantStateChangeHelper(ActiveConference activeConference)
	{
		this.userSessions = new HashMap();
		this.activeConference = activeConference;
		this.clientEventsPublisher = activeConference.getClientEventPublisher();
		maxObjectsInEvent = ConferenceConsoleConstants.getMaxObjectsInOneEvent();
	}
	public void addUserSession(IConferenceParticipant participant, UserSession session)
	{
		this.userSessions.put(participant.getId(),session);
	}
	public void removeUserSession(IConferenceParticipant participant)
	{
		this.userSessions.remove(participant.getId());
	}
	public ActiveConference getActiveConference()
	{
		return activeConference;
	}
	public void setActiveConference(ActiveConference activeConference)
	{
		this.activeConference = activeConference;
	}
//	public void closeAllUserSessions()
//	{
//		Vector v = new Vector();
//		Iterator iter = this.userSessions.values().iterator();
//		while (iter.hasNext())
//		{
//			v.addElement(iter.next());
//		}
//		int size = v.size();
//		for (int i=0; i<size; i++)
//		{
//			((UserSession)v.elementAt(i)).close();
//		}
//	}
	/**
	 * The real event dispatches on the state changes.
	 */
	/**
	 * Event: roster.arrived	To: All presenters
	 */
	public	void	arrivedInLobby(IConferenceParticipant user)
	{
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_USER_ARRIVED,
					new Date(), ConferenceConstants.RESPONSE_OK, user );
			
//			System.out.println("Firing: "+event);
			
			this.clientEventsPublisher.dispatchEventToAllClients(event);
	}
	public	void	grantedEntry(IConferenceParticipant user)
	{
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_ENTRY_GRANTED,
					new Date(), ConferenceConstants.RESPONSE_OK, user );
			
//			System.out.println("Firing: "+event);
			
			this.clientEventsPublisher.dispatchEventToAllClients(event);
	}
	public	void	deniedEntry(IConferenceParticipant user)
	{
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_ENTRY_DENIED,
					new Date(), ConferenceConstants.RESPONSE_OK, user );
			
			this.clientEventsPublisher.dispatchEventToAllClients(event);
			
			this.activeConference.userLeftConference(user);
	}
	public	void	joinedConference(IConferenceParticipant user)
	{
		this.dispatchJoinedConferenceEvents(user, ConferenceConstants.EVENT_PARTICIPANT_JOINED, true);
	}
	public	void	rejoinedConference(IConferenceParticipant user)
	{
		this.dispatchJoinedConferenceEvents(user, ConferenceConstants.EVENT_PARTICIPANT_REJOINED, false);
	}
	public void reloadedConsole(IConferenceParticipant user)
	{
		Participant participant = (Participant)user;
		if (!participant.getStatus().equals(ConferenceConstants.STATUS_PREVIOUS_HOST))
		{
			//	This would mean that it was the host who was trying to reload console
			//	and has rejoined from another location already.
			this.dispatchJoinedConferenceEvents(user, ConferenceConstants.EVENT_PARTICIPANT_JOINED, false);
		}
	}
	private	void	dispatchJoinedConferenceEvents(IConferenceParticipant user, String eventType, boolean newUser)
	{
		//	First notify all clients of the new or rejoining user.
		if(!this.activeConference.participantListEnabled)
		{
			System.out.println("adding Participant list filter");
			
			//have to remove this once somone becomes an active presenter and add this when he becomes normal again
			user.getEventReceiver().setEventFilter(new ParticpantListFilter(user, activeConference));
		}
		
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					eventType, new Date(), ConferenceConstants.RESPONSE_OK, user );
			
		this.clientEventsPublisher.dispatchEventToAllClients(event);
			
		List fullRoster = null;
		if(!this.activeConference.participantListEnabled && !user.getId().equals(this.activeConference.getHost().getId()))
		{
			fullRoster = new ArrayList();
			fullRoster.add(this.activeConference.getHost());
			Participant activePresnter = this.activeConference.getActivePresenter();
			if(! activePresnter.getId().equals(this.activeConference.getHost().getId()))
			{
				//activePresnter.getPermissions().setChatOff();
				fullRoster.add(activePresnter);
			}
		}
		else
		{
			fullRoster = this.activeConference.getConference().getUserRoster().getParticipants();
		}
		//	Now send the current state of the meeting to the new console.
		
		//	Break up the roster event if there are too many attendees.
		//	This is because when the event listener script gets too busy
		//	the browser throws up warnings.
		int size = fullRoster.size();
		for (int i=0; i<(size+maxObjectsInEvent); i+=maxObjectsInEvent)
			{
				Vector v = new Vector();
				for (int j=i; (j<(i+maxObjectsInEvent) && j<size); j++)
				{
					v.addElement(fullRoster.get(j));
				}
				if (v.size() > 0)
				{
					Event fullRosterEvent = new Event(ConferenceConstants.FEATURE_ROSTER,
							ConferenceConstants.EVENT_PARTICIPANTS_LIST,
							new Date(), ConferenceConstants.RESPONSE_OK, v );
					this.clientEventsPublisher.dispatchEventTo(fullRosterEvent,user);
				}
				else
				{
					break;
				}
			}
			
			List resourceList = this.activeConference.getConference().getResourceRoster().getResources();
			Event resourceListEvent = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
					ConferenceConstants.EVENT_RESOURCES_LIST,
					new Date(), ConferenceConstants.RESPONSE_OK, resourceList );
			this.clientEventsPublisher.dispatchEventTo(resourceListEvent,user);
			
			IResourceObject ro = ((ResourceManager)this.activeConference.getResourceManager()).
					getActiveResourceManager().getCurrentActiveResource();
			if (ro != null)
			{
				//	Send the resource selected event and the last control event to
				//	the new attendee.
				Event event1 = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
						ConferenceConstants.EVENT_RESOURCE_SELECTED,
						new Date(), ConferenceConstants.RESPONSE_OK, ro );
				this.clientEventsPublisher.dispatchEventTo(event1,user);
				
				Event event2 = ((ResourceManager)this.activeConference.getResourceManager()).
						getActiveResourceManager().getLastControlEvent();
				if (event2 != null)
				{
					this.clientEventsPublisher.dispatchEventTo(event2,user);
				}
			}
		if (eventType.equals(ConferenceConstants.EVENT_PARTICIPANT_JOINED))
		{
			Event videoEvent = this.activeConference.getActiveVideoManager().getLastControlEvent();
			if (videoEvent != null)
			{
				this.clientEventsPublisher.dispatchEventTo(videoEvent,user);
			}
			
			Event recordingEvent = ((RecordingManager)this.activeConference.getRecordingManager()).getLastControlEvent();
			if (recordingEvent != null)
			{
				this.clientEventsPublisher.dispatchEventTo(recordingEvent,user);
			}
		}
			Vector audioEvents = this.activeConference.getActiveVideoManager().getLastAudioControlEvents();
			try
			{
//				System.out.println("Audio events list in participants state change helper:"+audioEvents);
				if (audioEvents != null)
				{
					size = audioEvents.size();
					for (int i=0; i<size; i++)
					{
						Event audioEvent = (Event)audioEvents.elementAt(i);
//						System.out.println("Sending audio event from participants state change helper:"+audioEvent);
						this.clientEventsPublisher.dispatchEventTo(audioEvent,user);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//	Send out an event to notify the console that all the data has been transfered.
			//	This is required because not all of the events above may always apply, hence
			//	none can be considered as a positive indication that all the data is now
			//	available on the console.
			Event dataSentEvent = new Event(ConferenceConstants.FEATURE_CONF,
					ConferenceConstants.EVENT_CONFERENCE_CONSOLE_DATA_SENT,
					new Date(), ConferenceConstants.RESPONSE_OK, "OK" );
			this.clientEventsPublisher.dispatchEventTo(dataSentEvent,user);
		if (newUser)
		{
			PortalServerAdapter.getAdapter().reportUserJoined(this.activeConference.getConferenceKey(),user.getId());
		}
	}
	public	void	leftConference(IConferenceParticipant user)
	{
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_LEFT,
					new Date(), ConferenceConstants.RESPONSE_OK, user );
			
			this.clientEventsPublisher.dispatchEventToAllClients(event);
			
			this.activeConference.userLeftConference(user);
			PortalServerAdapter.getAdapter().reportUserLeft(this.activeConference.getConferenceKey(),user.getId());
	}
	public	void	removedFromConference(IConferenceParticipant user)
	{
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_LEFT,
					new Date(), ConferenceConstants.RESPONSE_OK, user );
			
			this.clientEventsPublisher.dispatchEventToAllClientsExcept(event,user);
			
			Event youRemovedEvent = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_REMOVED,
					new Date(), ConferenceConstants.RESPONSE_OK, user );
			
			this.clientEventsPublisher.dispatchEventTo(youRemovedEvent,user);
			PortalServerAdapter.getAdapter().reportUserLeft(this.activeConference.getConferenceKey(),user.getId());
	}
	
	public	void	becameAttendee(IConferenceParticipant user)
	{
		Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_PARTICIPANT_ROLE_CHANGE,
				new Date(), ConferenceConstants.RESPONSE_OK, user );
		
		this.clientEventsPublisher.dispatchEventToAllClients(event);
		//sendRemovalEvent(user);
	}
	
	public	void	becamePresenter(IConferenceParticipant user)
	{
		Participant participant = (Participant)user;
		if(!participant.isHost() && !this.activeConference.participantListEnabled)
		{
			user.getEventReceiver().setEventFilter(new ParticpantListFilter(user, activeConference));
		}
		
		Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_PARTICIPANT_ROLE_CHANGE,
				new Date(), ConferenceConstants.RESPONSE_OK, user );
		
		this.clientEventsPublisher.dispatchEventToAllClients(event);
		
		//sendRemovalEvent(user);
	}
	
	/*private void sendRemovalEvent(IConferenceParticipant user) {
		//now sending the active presenter in roster list to all users
		System.out.println("some one is made a non active presenter so sending the uiser too");
		if(!this.activeConference.participantListEnabled)
		{
			Vector v = new Vector();
			v.add(user);
			Event fullRosterEvent = new Event(ConferenceConstants.FEATURE_ROSTER,
					ConferenceConstants.EVENT_PARTICIPANT_LEFT,
					new Date(), ConferenceConstants.RESPONSE_OK, v );
			this.clientEventsPublisher.dispatchEventToAllClientsExcept(fullRosterEvent, user);
		}
	}*/
	
	public	void	becameActivePresenter(IConferenceParticipant user)
	{
		//removing the event filter coz this user should now get the attendees joining
		user.getEventReceiver().removeEventFilter();
		
		Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_PARTICIPANT_ROLE_CHANGE,
				new Date(), ConferenceConstants.RESPONSE_OK, user );
		
		this.clientEventsPublisher.dispatchEventToAllClients(event);
		
		List fullRoster = this.activeConference.getConference().getUserRoster().getParticipants();
		
		//	Now send the current user roster to the newly made presenter
		
		//	Break up the roster event if there are too many attendees.
		//	This is because when the event listener script gets too busy
		//	the browser throws up warnings.
		int size = fullRoster.size();
		for (int i=0; i<(size+maxObjectsInEvent); i+=maxObjectsInEvent)
			{
				Vector v = new Vector();
				for (int j=i; (j<(i+maxObjectsInEvent) && j<size); j++)
				{
					v.addElement(fullRoster.get(j));
				}
				if (v.size() > 0)
				{
					Event fullRosterEvent = new Event(ConferenceConstants.FEATURE_ROSTER,
							ConferenceConstants.EVENT_PARTICIPANTS_LIST,
							new Date(), ConferenceConstants.RESPONSE_OK, v );
					this.clientEventsPublisher.dispatchEventTo(fullRosterEvent,user);
				}
				else
				{
					break;
				}
			}
		
	}
}
