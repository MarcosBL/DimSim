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

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IClientEventPublisher;
import com.dimdim.conference.model.IPermissionsManager;
import com.dimdim.conference.model.Participant;
import com.dimdim.conference.config.ConferenceConfig;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The permissions are maintained on each individual participant, because the
 * settings are done less often than lookups. The permissions change event are
 * dispatched to everyone, as all consoles need to know. Enable all and disable
 * all are dispatched as a single event.
 * 
 * The permissions manager manages the permissions changes for participants
 * in a conference as well as those joining and leaving a conference. The roster
 * is given access to the permissions manager so that during the participant
 * state change process, when a state change is decided upong the roster can
 * ask the permissions manager for the permissions of the newly joined user
 * as well as it can report a participant who has left so that the permissions
 * manager can keep track of the available resources such as available audio
 * broadcaster slots or available presenters slots, if such maximums are set.
 */

public class PermissionsManager	implements	IPermissionsManager
{
	
	protected	ActiveConference	activeConference;
	protected	IClientEventPublisher	clientEventsPublisher;
	
//	protected	ConferenceConfig	conferenceConfig;
//	protected	int		currentFilledAudioSlots;
	
	public	PermissionsManager(ActiveConference activeConference)
	{
		this.activeConference = activeConference;
//		this.conferenceConfig = activeConference.getConfig();
//		this.currentFilledAudioSlots = 0;
		this.clientEventsPublisher = activeConference.getClientEventPublisher();
	}
	
	/**
	 * Chat permissions management. Adjust the permissions and dispatch
	 * events to clients.
	 */
	public void disableSendChatMessageFor(Vector ids)
	{
		((ChatManager)this.activeConference.getChatManager()).
			setConferenceChatPermission(ChatManager.CHECK_ATTENDEE);
		
		this.setChatPermissions(false,ids,true);
	}
	public void disableSendChatMessageForAll()
	{
		((ChatManager)this.activeConference.getChatManager()).
			setConferenceChatPermission(ChatManager.DISABLE_ALL);
		
		this.setChatPermissions(false,null,false);
		
		Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_CHAT_DISABLED_FOR_ALL,
				new Date(), ConferenceConstants.RESPONSE_OK, "all" );
		
//		System.out.println("Firing: "+event);
		
		this.clientEventsPublisher.dispatchEventToAllClients(event);
	}
	public void enableSendChatMessageFor(Vector ids)
	{
		((ChatManager)this.activeConference.getChatManager()).
			setConferenceChatPermission(ChatManager.CHECK_ATTENDEE);
		
		this.setChatPermissions(true,ids,true);
	}
	public void enableSendChatMessageForAll()
	{
		((ChatManager)this.activeConference.getChatManager()).
			setConferenceChatPermission(ChatManager.ENABLE_ALL);
		
		this.setChatPermissions(true,null,false);
		
		Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_CHAT_ENABLED_FOR_ALL,
				new Date(), ConferenceConstants.RESPONSE_OK, "all" );
		
//		System.out.println("Firing: "+event);
		
		this.clientEventsPublisher.dispatchEventToAllClients(event);
	}
	protected	void	setChatPermissions(boolean setOn, Vector v, boolean sendEvent)
	{
		List list = this.activeConference.getRosterManager().getRosterObject().getParticipants();
		int	size = list.size();
		String eventCode = "";
		for (int i=0; i<size; i++)
		{
			Participant user = (Participant)list.get(i);
			if (!user.isHost())
			{
				if (v == null || v.contains(user.getId()))
				{
					if (setOn)
					{
						user.getPermissions().setChatOn();
						eventCode = ConferenceConstants.EVENT_CHAT_ENABLED;
					}
					else
					{
						user.getPermissions().setChatOff();
						eventCode = ConferenceConstants.EVENT_CHAT_DISABLED;
					}
					if (sendEvent)
					{
						Event event = new Event(ConferenceConstants.FEATURE_ROSTER, eventCode,
								new Date(), ConferenceConstants.RESPONSE_OK, user );
						
//						System.out.println("Firing: "+event);
						
						this.clientEventsPublisher.dispatchEventToAllClients(event);
					}
				}
			}
		}
	}
	/**
	 * Audio permissions management. TODO - the permissions need to be carried to the
	 * manager who will be responsible for controllng the audio and video events based
	 * on them them.
	 */
	public void disableAudioForAllInList(Vector ids)
	{
		this.setAudioPermissions(false,ids,true);
	}
	public void disableAudioForAll()
	{
		this.setAudioPermissions(false,null,false);
		
		Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_CHAT_DISABLED_FOR_ALL,
				new Date(), ConferenceConstants.RESPONSE_OK, "all" );
		
//		System.out.println("Firing: "+event);
		
		this.clientEventsPublisher.dispatchEventToAllClients(event);
	}
	public void enableAudioForAllInList(Vector ids)
	{
		this.setAudioPermissions(true,ids,true);
	}
	public	void	enableAudioFor(String id)
	{
		
	}
	public	void	disableAudioFor(String id)
	{
		
	}
	protected	void	setAudioPermissions(boolean setOn, Vector v, boolean sendEvent)
	{
		List list = this.activeConference.getRosterManager().getRosterObject().getParticipants();
		int	size = list.size();
		String eventCode = "";
		for (int i=0; i<size; i++)
		{
			Participant user = (Participant)list.get(i);
//			if (!user.isPresenter())
//			{
				if (v == null || v.contains(user.getId()))
				{
					if (setOn)
					{
						user.getPermissions().setAudioOn();
						eventCode = ConferenceConstants.EVENT_AUDIO_ENABLED;
						audioEnabledFor(user);
					}
					else
					{
						user.getPermissions().setAudioOff();
						eventCode = ConferenceConstants.EVENT_AUDIO_DISABLED;
						audioDisabledFor(user);
					}
					if (sendEvent)
					{
						Event event = new Event(ConferenceConstants.FEATURE_ROSTER, eventCode,
								new Date(), ConferenceConstants.RESPONSE_OK, user );
						
//						System.out.println("Firing: "+event);
						
						this.clientEventsPublisher.dispatchEventToAllClients(event);
					}
				}
//			}
		}
	}
	
	public	boolean	hasAudioSlotsAvailable()
	{
		return	this.activeConference.getAudioVideoManager().isAudioBroadcasterAvailable();
	}
	public	void	audioEnabledFor(Participant user)
	{
		this.activeConference.getAudioVideoManager().audioBroadcasterAdded(user);
	}
	public	void	audioDisabledFor(Participant user)
	{
		this.activeConference.getAudioVideoManager().audioBroadcasterRemoved(user);
	}
	
	/*********************************************************************************
	 * 
	 * AT PRESENT VIDEO IS NOT SUPPORTED FOR ANY PARTICIPANTS
	 * 
	 */
	/**
	 * Video permissions management. TODO - the permissions need to be carried to the
	 * manager who will be responsible for controllng the audio and video events based
	 * on them them.
	 */
	public void disableVideoForAllInList(Vector ids)
	{
		this.setVideoPermissions(false,ids,true);
	}
	public void disableVideoForAll()
	{
		this.setVideoPermissions(false,null,false);
		
		Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_CHAT_DISABLED_FOR_ALL,
				new Date(), ConferenceConstants.RESPONSE_OK, "all" );
		
//		System.out.println("Firing: "+event);
		
		this.clientEventsPublisher.dispatchEventToAllClients(event);
	}
	public void enableVideoForAllInList(Vector ids)
	{
		this.setVideoPermissions(true,ids,true);
	}
	public	void	enableVideoFor(String id)
	{
		
	}
	public	void	disableVideoFor(String id)
	{
		
	}
	public	void	videoEnabledFor(Participant user)
	{
		this.activeConference.getAudioVideoManager().videoBroadcasterAdded(user);
	}
	public	void	videoDisabledFor(Participant user)
	{
		this.activeConference.getAudioVideoManager().videoBroadcasterRemoved(user);
	}
	
	protected	void	setVideoPermissions(boolean setOn, Vector v, boolean sendEvent)
	{
		List list = this.activeConference.getRosterManager().getRosterObject().getParticipants();
		int	size = list.size();
		String eventCode = "";
		for (int i=0; i<size; i++)
		{
			Participant user = (Participant)list.get(i);
			if (!user.isHost())
			{
				if (v == null || v.contains(user.getId()))
				{
					if (setOn)
					{
						user.getPermissions().setVideoOn();
						eventCode = ConferenceConstants.EVENT_VIDEO_ENABLED;
						videoEnabledFor(user);
					}
					else
					{
						user.getPermissions().setVideoOff();
						eventCode = ConferenceConstants.EVENT_VIDEO_DISABLED;
						videoDisabledFor(user);
					}
					if (sendEvent)
					{
						Event event = new Event(ConferenceConstants.FEATURE_ROSTER, eventCode,
								new Date(), ConferenceConstants.RESPONSE_OK, user );
						
						System.out.println("Firing: "+event);
						
						this.clientEventsPublisher.dispatchEventToAllClients(event);
					}
				}
			}
		}
	}
}
