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

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IResourceObject;
import com.dimdim.conference.model.ResourceObject;

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
 */

public class ActiveResourceManager extends ConferenceFeatureManager
{
	protected	IConference		conference;
	
	protected	ActivePresentationManager	activePresentationManager;
	protected	ActiveSharedStreamManager	activeSharedStreamManager;
	protected	WhiteboardManager			whiteboardManager;
	protected	CobrowseManager				cobrowseMAnager;
	
	protected	Event				lastControlEvent;
	
	public	ActiveResourceManager(ActiveConference conference)
	{
		this.conference = conference;
		this.setClientEventPublisher(conference.getClientEventPublisher());
		this.activePresentationManager = new ActivePresentationManager(this);
		this.activeSharedStreamManager = new ActiveSharedStreamManager(this);
		this.whiteboardManager = new WhiteboardManager(this);
		this.cobrowseMAnager = new CobrowseManager(this);
	}
	public	IConference	getConference()
	{
		return	this.conference;
	}
	public	WhiteboardManager	getWhiteboardManager()
	{
		return	this.whiteboardManager;
	}
	/**
	 * The spare arguments are purely runtime parameters. At present there is
	 * only 1, the slide at which the presenter is starting the presentation.
	 * It will of course be better to accept a simple map, when there are more
	 * such parameters in future.
	 * 
	 * @return
	 */
	public	void	startSharingResource(IConferenceParticipant presenter,
			String resourceId, String streamId, int spare2)
	{
		IResourceObject res = this.getCurrentActiveResource();
		if (res != null)
		{
//			this.stopSharingResource(presenter);
		}
		
		res = this.conference.getResourceManager().getResourceRoster().getResourceObject(resourceId);
		if (res != null)
		{
			this.setCurrentActiveResource(res);
			String resourceType = res.getResourceType();
			
			//	Now based on the type of the resource, pass on the control to
			//	the appropriate sub manager.
			if (resourceType.equals(ConferenceConstants.RESOURCE_TYPE_PRESENTATION))
			{
				this.activePresentationManager.startPresentation(presenter, res, spare2);
			}
			else if (resourceType.equals(ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE) ||
					 resourceType.equals(ConferenceConstants.RESOURCE_TYPE_APP_SHARE))
			{
				this.activeSharedStreamManager.startStreaming(presenter, res, streamId);
			}
		}
	}
	/**
	 * This method does the required work, if any is required, to stop sharing
	 * of the currently active resource. If there is no resource currently
	 * being actively shared, this method does nothing.
	 */
	public	void	stopSharingResource(IConferenceParticipant presenter)
	{
		IResourceObject currentActiveResource = this.conference.getResourceManager().
				getResourceRoster().getActiveResourceObject();
		if (currentActiveResource != null)
		{
			String resourceType = currentActiveResource.getResourceType();
			
			if (resourceType.equals(ConferenceConstants.RESOURCE_TYPE_PRESENTATION))
			{
				this.activePresentationManager.stopPresentation(presenter, currentActiveResource);
			}
			else if (resourceType.equals(ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE) ||
					 resourceType.equals(ConferenceConstants.RESOURCE_TYPE_APP_SHARE))
			{
				this.activeSharedStreamManager.stopStreaming(presenter, currentActiveResource, null);
			}
			else if (resourceType.equals(ConferenceConstants.RESOURCE_TYPE_WHITEBOARD))
			{
				this.whiteboardManager.stopWhiteboard(presenter, currentActiveResource.getResourceId());
			}
			
			//	Set the active resource to null.
			
			synchronized (this.conference.getResourceManager().getResourceRoster())
			{
				this.conference.getResourceManager().
					getResourceRoster().setActiveResourceObject(null);
			}
		}
	}
	public	void	changeSharedResource(IConferenceParticipant presenter, String resourceId)
	{
		IResourceObject currentActiveResource = this.conference.getResourceManager().
				getResourceRoster().getActiveResourceObject();
		if (currentActiveResource != null)
		{
			String resourceType = currentActiveResource.getResourceType();
			
			if (resourceType.equals(ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE) ||
					 resourceType.equals(ConferenceConstants.RESOURCE_TYPE_APP_SHARE))
			{
				this.activeSharedStreamManager.changeStreaming(presenter, resourceId);
			}
		}
	}
	public ActivePresentationManager getActivePresentationManager()
	{
		return activePresentationManager;
	}
	public ActiveSharedStreamManager getActiveSharedStreamManager()
	{
		return activeSharedStreamManager;
	}
	
	public	IResourceObject	getCurrentActiveResource()
	{
		return	this.conference.getResourceManager().
			getResourceRoster().getActiveResourceObject();
	}
	public	void	setCurrentActiveResource(IResourceObject ro)
	{
		synchronized (this.conference.getResourceManager().getResourceRoster())
		{
			this.conference.getResourceManager().
				getResourceRoster().setActiveResourceObject(ro);
		}
	}
	
	public	void	startSharingWhiteboard(IConferenceParticipant presenter)
	{
		IResourceObject whiteboard = this.conference.getResourceManager().getResourceRoster().getWhiteboardResource();
		if (whiteboard != null)
		{
			synchronized (this.conference.getResourceManager().getResourceRoster())
			{
				this.conference.getResourceManager().
						getResourceRoster().setActiveResourceObject(whiteboard);
				this.conference.getWhiteboardManager().startWhiteboard(presenter,whiteboard.getResourceId());
			}
		}
	}
	public	void	controlWhiteboard(IConferenceParticipant presenter, String command)
	{
		if (command.equals(ConferenceConstants.EVENT_WHITEBOARD_CONTROL_UNLOCK))
		{
			this.conference.getWhiteboardManager().unlockWhiteboard(presenter,"");
		}
		else if (command.equals(ConferenceConstants.EVENT_WHITEBOARD_CONTROL_LOCK))
		{
			this.conference.getWhiteboardManager().lockWhiteboard(presenter,"");
		}
	}
	public	void	stopSharingWhiteboard(IConferenceParticipant presenter)
	{
		this.conference.getWhiteboardManager().stopWhiteboard(presenter,"");
		synchronized (this.conference.getResourceManager().getResourceRoster())
		{
			this.conference.getResourceManager().
					getResourceRoster().setActiveResourceObject(null);
		}
	}
	
	public	void	startCobrowse(IConferenceParticipant presenter, String resourceId)
	{
		IResourceObject coBrowse = this.conference.getResourceManager().getResourceRoster().getResourceObject(resourceId);
		if (coBrowse != null)
		{
			synchronized (this.conference.getResourceManager().getResourceRoster())
			{
				this.conference.getResourceManager().
						getResourceRoster().setActiveResourceObject(coBrowse);
				this.conference.getCobrowseManager().startCobrowse(presenter,coBrowse.getResourceId());
			}
		}
	}
	
	public	void	renameCobrowse(IConferenceParticipant presenter, String command, String resourceId,
			String newName)
	{
		this.conference.getCobrowseManager().rename(presenter,resourceId ,newName);
	}
	public	void	controlCobrowse(IConferenceParticipant presenter, String command, String resourceId,
			String horScroll, String verScroll)
	{
		//here we are using ANNOTATION_ON = lock ANNOTATION_OFF = unlock
		IResourceObject ro = this.getCurrentActiveResource();
		
		if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_NAVIGATE))
		{
			this.conference.getCobrowseManager().navigate(presenter,resourceId);
		}
		else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_SCROLL))
		{
			this.conference.getCobrowseManager().scroll(presenter,resourceId, horScroll, verScroll);
		}
		else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_LOCK))
		{
			if (ro != null && ro.getResourceType().equals(ConferenceConstants.RESOURCE_TYPE_COBROWSE))
			{
				((ResourceObject)ro).setAnnotation(ResourceObject.ANNOTATION_ON);
				this.conference.getCobrowseManager().lock(presenter, ro);
			}
		}
		else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_UNLOCK))
		{
			if (ro != null && ro.getResourceType().equals(ConferenceConstants.RESOURCE_TYPE_COBROWSE))
			{
				((ResourceObject)ro).setAnnotation(ResourceObject.ANNOTATION_OFF);
				this.conference.getCobrowseManager().unlock(presenter, ro);
			}
		}
	}
	public	void	stopCobrowse(IConferenceParticipant presenter, String resourceId)
	{
		this.conference.getCobrowseManager().stopCobrowse(presenter,resourceId);
		synchronized (this.conference.getResourceManager().getResourceRoster())
		{
			this.conference.getResourceManager().getResourceRoster().setActiveResourceObject(null);
		}
	}
	
	public Event getLastControlEvent()
	{
		return lastControlEvent;
	}
	public void setLastControlMessage(Event lastControlEvent)
	{
		synchronized (this)
		{
			this.lastControlEvent = lastControlEvent;
		}
	}
	
	//	This method is called only on failure of a presenter console, who is not the host.
	//	If the current active resource is not null, then raise the appropriate stop event.
	
	public	void	forceStopActiveSharing(IConferenceParticipant presenter)
	{
		this.stopSharingResource(presenter);
	}
	public CobrowseManager getCobrowseMAnager() {
		return cobrowseMAnager;
	}
}
