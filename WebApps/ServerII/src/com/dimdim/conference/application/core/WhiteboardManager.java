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
import java.util.HashMap;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IWhiteboardManager;
import com.dimdim.conference.model.WhiteboardControlEvent;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This application controls the active resources for the clients. Active
 * resource is simply the resource that is being used at the time. This
 * application employs following concepts and enforces following rules.
 * 
 * Primary responsibility of this object is to control the sharing of
 * resources for the system. The resources sharing is strictly only one
 * at a time. Hence all the sharing control requests are handled by this
 * object, which enforces the sharing rules and provides transperent
 * controls to help the presenter.
 * 
 * The video is always presented in the size determined by the system
 * hence the related parameters in the event can be ignored.
 */

public class WhiteboardManager extends ConferenceFeatureManager implements IWhiteboardManager
{
	protected	IConference		conference;
	protected	ActiveResourceManager	activeResourceManager;
	
	public	WhiteboardManager(ActiveResourceManager activeResourceManager)
	{
		this.activeResourceManager = activeResourceManager;
		this.conference = activeResourceManager.getConference();
		this.setClientEventPublisher(((ActiveConference)conference).getClientEventPublisher());
	}
	public	IConference	getConference()
	{
		return	this.conference;
	}
	
	public	void	startWhiteboard(IConferenceParticipant presenter,
							String resourceId)
	{
		WhiteboardControlEvent ave = WhiteboardControlEvent.createStartEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, "s");
		
		this.dispatchWBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_WHITEBOARD,
				ConferenceConstants.EVENT_WHITEBOARD_CONTROL);
	}
	public	void	stopWhiteboard(IConferenceParticipant presenter,
							String resourceId)
	{
		WhiteboardControlEvent ave = WhiteboardControlEvent.createStopEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, "s");
		
		this.dispatchWBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_WHITEBOARD,
				ConferenceConstants.EVENT_WHITEBOARD_CONTROL);
	}
	public	void	lockWhiteboard(IConferenceParticipant presenter,
							String resourceId)
	{
		WhiteboardControlEvent ave = WhiteboardControlEvent.createLockEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, "s");
		
		this.dispatchWBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_WHITEBOARD,
				ConferenceConstants.EVENT_WHITEBOARD_CONTROL);
	}
	public	void	unlockWhiteboard(IConferenceParticipant presenter,
			String resourceId)
	{
		WhiteboardControlEvent ave = WhiteboardControlEvent.createUnlockEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, "s");
		
		this.dispatchWBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_WHITEBOARD,
				ConferenceConstants.EVENT_WHITEBOARD_CONTROL);
	}
	protected	void	dispatchWBControlEvent(IConferenceParticipant presenter,
			WhiteboardControlEvent vce, String featureId, String eventId)
	{
		Event event = new Event(featureId, eventId, new Date(),
				ConferenceConstants.RESPONSE_OK, vce );
		
		this.getClientEventPublisher().dispatchEventToAllClientsExcept(event,presenter);
		if (featureId.equals(ConferenceConstants.FEATURE_WHITEBOARD))
		{
			this.setLastControlMessage(event);
		}
	}
	protected void setLastControlMessage(Event lastControlEvent)
	{
		this.activeResourceManager.setLastControlMessage(lastControlEvent);
	}
}
