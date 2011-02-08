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
import com.dimdim.conference.model.CobrowseControlEvent;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.ICobrowseManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IResourceObject;


public class CobrowseManager extends ConferenceFeatureManager implements ICobrowseManager
{
	protected	IConference		conference;
	protected	ActiveResourceManager	activeResourceManager;
	
	public	CobrowseManager(ActiveResourceManager activeResourceManager)
	{
		this.activeResourceManager = activeResourceManager;
		this.conference = activeResourceManager.getConference();
		this.setClientEventPublisher(((ActiveConference)conference).getClientEventPublisher());
	}
	public	IConference	getConference()
	{
		return	this.conference;
	}
	
	public	void	startCobrowse(IConferenceParticipant presenter,
							String resourceId)
	{
		CobrowseControlEvent ave = CobrowseControlEvent.createStartEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, "s");
		
		this.dispatchCBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_COBROWSE,
				ConferenceConstants.EVENT_COBROWSE_CONTROL, true);
	}
	public	void	stopCobrowse(IConferenceParticipant presenter,
							String resourceId)
	{
		CobrowseControlEvent ave = CobrowseControlEvent.createStopEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, "s");
		
		this.dispatchCBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_COBROWSE,
				ConferenceConstants.EVENT_COBROWSE_CONTROL, true);
	}
	public	void	navigate(IConferenceParticipant presenter,
							String resourceId)
	{
		CobrowseControlEvent ave = CobrowseControlEvent.createNavigateEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, "s");
		
		this.dispatchCBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_COBROWSE,
				ConferenceConstants.EVENT_COBROWSE_CONTROL, true);
	}
	public	void	scroll(IConferenceParticipant presenter,
			String resourceId, String horScroll, String verScroll)
	{
		CobrowseControlEvent ave = CobrowseControlEvent.createScrollEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId, horScroll, verScroll, "s");
		
		this.dispatchCBControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_COBROWSE,
				ConferenceConstants.EVENT_COBROWSE_CONTROL, false);
	}
	
	public	void	lock(IConferenceParticipant presenter,
			IResourceObject ro)
	{
		CobrowseControlEvent ave = CobrowseControlEvent.createLockEvent(
		this.getConference().getConfig().getConferenceKey(),
		presenter.getId(),
		ro.getResourceId(), "s");
		
		this.dispatchCBControlEvent(presenter, ave,
		ConferenceConstants.FEATURE_COBROWSE,
		ConferenceConstants.EVENT_COBROWSE_CONTROL, false);
	}
	
	public	void	unlock(IConferenceParticipant presenter,
			IResourceObject ro)
	{
		CobrowseControlEvent ave = CobrowseControlEvent.createUnLockEvent(
		this.getConference().getConfig().getConferenceKey(),
		presenter.getId(),
		ro.getResourceId(), "s");
		
		this.dispatchCBControlEvent(presenter, ave,
		ConferenceConstants.FEATURE_COBROWSE,
		ConferenceConstants.EVENT_COBROWSE_CONTROL, false);
	}
	
	public	void	rename(IConferenceParticipant presenter,
			String resourceId, String newName)
	{
		CobrowseControlEvent ave = CobrowseControlEvent.createRenameEvent(
		this.getConference().getConfig().getConferenceKey(),
		presenter.getId(),
		resourceId, "s", newName);
		
		this.dispatchCBControlEvent(presenter, ave,
		ConferenceConstants.FEATURE_COBROWSE,
		ConferenceConstants.EVENT_COBROWSE_CONTROL, true);
	}
	
	
	protected	void	dispatchCBControlEvent(IConferenceParticipant presenter,
			CobrowseControlEvent vce, String featureId, String eventId,  boolean markControlMessage)
	{
		Event event = new Event(featureId, eventId, new Date(),
				ConferenceConstants.RESPONSE_OK, vce );
		
		this.getClientEventPublisher().dispatchEventToAllClientsExcept(event,presenter);
		if (featureId.equals(ConferenceConstants.FEATURE_COBROWSE ) && markControlMessage)
		{
			this.setLastControlMessage(event);
		}
	}
	
	protected void setLastControlMessage(Event lastControlEvent)
	{
		
		this.activeResourceManager.setLastControlMessage(lastControlEvent);
		
	}
}
