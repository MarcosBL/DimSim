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

import com.dimdim.messaging.IEventReceiver;
//import com.dimdim.messaging.IEventReceiverFilter;
import	com.dimdim.messaging.IMessageEngine;

import com.dimdim.conference.model.Event;
//import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IClientEventPublisher;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.recording.MeetingRecorder;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ClientEventPublisher implements IClientEventPublisher 
{
	
	protected	ActiveConference		conference;
	protected	IMessageEngine		messageEngine;
//	protected	MeetingRecorder		meetingRecorder;
	
	public	ClientEventPublisher(ActiveConference conference, IMessageEngine messageEngine)
	{
		this.conference = conference;
		this.messageEngine = messageEngine;
	}
	public	void	dispatchEventTo(Event event, IConferenceParticipant user)
	{
		if (this.conference.isActive())
		{
			this.messageEngine.dispatchEventToReceiver(event, user.getId());
		}
	}
//	public	void	dispatchEventToChild(Event event, IConferenceParticipant user, String childId)
//	{
//		if (this.conference.isActive())
//		{
//			this.messageEngine.dispatchEventToReceiver(event, user.getId());
//		}
//	}
	/**
	 * For the event messaging system the recorder has no special status. Since the
	 * recorder is not visible to any attendees it cannot receive any private messages.
	 * It only receives all the public messages.
	 */
	public	void	addMeetingRecorder(MeetingRecorder meetingRecorder)
	{
		this.messageEngine.addEventReceiver(meetingRecorder);
	}
	public	void	dispatchEventToAllClients(Event event)
	{
		if (this.conference.isActive())
		{
			this.messageEngine.dispatchEvent(event);
		}
	}
//	public	void	dispatchEventToAllClients(Event event, IEventReceiverFilter filter)
//	{
//		if (this.conference.isActive())
//		{
//			this.messageEngine.dispatchEvent(event, filter);
//		}
//	}
	public	void	dispatchEventToAllClientsExcept(Event event, IConferenceParticipant exclude)
	{
		if (this.conference.isActive())
		{
			this.messageEngine.dispatchEventToAllExcept(event,exclude.getId());
		}
	}
	public	void	addEventReceiver(IEventReceiver receiver)
	{
		if (this.conference.isActive())
		{
			this.messageEngine.addEventReceiver(receiver);
		}
	}
	public	void	removeEventReceiver(IEventReceiver receiver)
	{
		this.messageEngine.removeEventReceiver(receiver);
	}
}
