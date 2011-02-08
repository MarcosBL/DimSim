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

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IResourceObject;
import com.dimdim.conference.model.StreamControlEvent;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class ActiveSharedStreamManager
{
	protected	ActiveResourceManager	activeResourceManager;
	
	public	ActiveSharedStreamManager(ActiveResourceManager arm)
	{
		this.activeResourceManager = arm;
	}
	protected	void	startStreaming(IConferenceParticipant presenter, IResourceObject resource, String streamId)
	{
		Event event = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
				ConferenceConstants.EVENT_RESOURCE_SELECTED,
				new Date(), ConferenceConstants.RESPONSE_OK, resource );
		this.activeResourceManager.getClientEventPublisher().dispatchEventToAllClientsExcept(event,presenter);
		if (streamId == null || streamId.length() == 0)
		{
			streamId = resource.getMediaId();
		}
		StreamControlEvent sce = StreamControlEvent.createStartEvent(
				this.activeResourceManager.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resource.getResourceId(), resource.getResourceType(),
				resource.getResourceType(),streamId,//resource.getMediaId(),
				"200","200","3");
		
		this.dispatchStreamControlEvent(presenter, sce);
	}
	protected	void	stopStreaming(IConferenceParticipant presenter, IResourceObject resource, String streamId)
	{
		if (streamId == null || streamId.length() == 0)
		{
			streamId = resource.getMediaId();
		}
		StreamControlEvent sce = StreamControlEvent.createStopEvent(
				this.activeResourceManager.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resource.getResourceId(),resource.getResourceType(),
				resource.getResourceType(),streamId,//resource.getMediaId(),
				"200","200","3");
		
		this.dispatchStreamControlEvent(presenter, sce);
	}
	protected	void	changeStreaming(IConferenceParticipant presenter, String resourceId)
	{
		StreamControlEvent sce = StreamControlEvent.createChangeEvent(
				this.activeResourceManager.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				resourceId);
		
		Event event = new Event(ConferenceConstants.FEATURE_SHARING,
				ConferenceConstants.EVENT_SCREEN_SHARING_CONTROL, new Date(),
				ConferenceConstants.RESPONSE_OK, sce );
		
		Event lastControlEvent = this.activeResourceManager.getLastControlEvent();
		if (lastControlEvent != null)
		{
			try
			{
				StreamControlEvent lce_data = (StreamControlEvent)lastControlEvent.getData();
				lce_data.setResourceId(resourceId);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		this.activeResourceManager.getClientEventPublisher().dispatchEventToAllClients(event);
	}
	protected	void	dispatchStreamControlEvent(IConferenceParticipant presenter, StreamControlEvent sce)
	{
		Event event = new Event(ConferenceConstants.FEATURE_SHARING,
				ConferenceConstants.EVENT_SCREEN_SHARING_CONTROL, new Date(),
				ConferenceConstants.RESPONSE_OK, sce );
		
		this.activeResourceManager.getClientEventPublisher().dispatchEventToAllClientsExcept(event,presenter);
		this.activeResourceManager.setLastControlMessage(event);
	}
}
