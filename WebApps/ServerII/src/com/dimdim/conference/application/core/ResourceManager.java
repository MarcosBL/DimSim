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

package com.dimdim.conference.application.core;

import com.dimdim.conference.model.Conference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IResourceEventListener;
import com.dimdim.conference.model.IResourceObject;
import com.dimdim.conference.model.IResourceManager;
import com.dimdim.conference.model.IResourceRoster;
import com.dimdim.conference.ConferenceConstants;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */

public class ResourceManager extends ConferenceFeatureManager implements IResourceManager
{
	
	protected	ActiveConference		activeConference;
	protected	ActiveResourceManager	activeResourceManager;
	
	/**
	 * 
	 */
	public ResourceManager(ActiveConference activeConference)
	{
		this.activeConference = activeConference;
		this.setClientEventPublisher(activeConference.getClientEventPublisher());
		this.activeConference.getConference().getResourceRoster().setClientEventPublisher(activeConference.getClientEventPublisher());
		this.activeResourceManager = new ActiveResourceManager(activeConference);
	}
	
	public ActiveResourceManager getActiveResourceManager()
	{
		return activeResourceManager;
	}
	public	IResourceObject	getCurrentActiveResource()
	{
		return	this.activeConference.getConference().getResourceRoster().getActiveResourceObject();
	}
	public	IResourceRoster		getResourceRoster()
	{
		return	this.activeConference.getConference().getResourceRoster();
	}
//	public  void	addResourceEventListener(IResourceEventListener listener)
//	{
//		this.activeConference.getConference().getResourceRoster().addResourceEventListener(listener);
//	}
//	public  void	removeResourceEventListener(IResourceEventListener listener)
//	{
//		this.activeConference.getConference().getResourceRoster().removeResourceEventListener(listener);
//	}
	
	public	void	handlePresentationControlMessage(IConferenceParticipant presenter,
			String resourceId, String controlAction, Integer slide, String userId)
	{
		String action = "action.pres."+controlAction;
		if (slide == null)
		{
			slide = new Integer(0);
		}
		
		if (action.equals(ConferenceConstants.ACTION_PRESENTATION_START))
		{
			this.activeResourceManager.startSharingResource(presenter,resourceId,null,slide.intValue());
		}
		else if (action.equals(ConferenceConstants.ACTION_PRESENTATION_STOP))
		{
			this.activeResourceManager.stopSharingResource(presenter);
		}
		else
		{
			IResourceObject ro = this.activeResourceManager.getCurrentActiveResource();
			if (ro != null && ro.getResourceType().equals(ConferenceConstants.RESOURCE_TYPE_PRESENTATION))
			{
				if (action.equals(ConferenceConstants.ACTION_PRESENTATION_ANNOTATIONS_ON))
				{
					this.activeResourceManager.getActivePresentationManager().changeAnnotations(presenter,ro,userId,true);
				}
				else if (action.equals(ConferenceConstants.ACTION_PRESENTATION_ANNOTATIONS_OFF))
				{
					this.activeResourceManager.getActivePresentationManager().changeAnnotations(presenter,ro,userId,false);
				}
				else
				{
					this.activeResourceManager.getActivePresentationManager().showSlide(presenter,ro,action,slide.intValue());
				}
			}
		}
	}
	
	public	void	handleScreenStreamControlMessage(IConferenceParticipant presenter,
			String resourceId, String mediaId, String controlAction,
			String presenterId, String presenterPassKey,
			String	appName, String appHandle)
	{
		if (controlAction.equals(ConferenceConstants.EVENT_STREAM_CONTROL_START))
		{
			this.activeResourceManager.startSharingResource(presenter,resourceId,mediaId,0);
		}
		else if (controlAction.equals(ConferenceConstants.EVENT_STREAM_CONTROL_STOP))
		{
			this.activeResourceManager.stopSharingResource(presenter);
		}
		else if (controlAction.equals(ConferenceConstants.ACTION_PRESENTATION_CHANGE))
		{
			this.activeResourceManager.changeSharedResource(presenter,resourceId);
		}
	}
	
	public	void	handleWhiteboardControlMessage(IConferenceParticipant presenter,
			String command)
	{
		if (command != null)
		{
			if (command.equals(ConferenceConstants.EVENT_WHITEBOARD_CONTROL_START))
			{
				this.activeResourceManager.startSharingWhiteboard(presenter);
			}
			else if (command.equals(ConferenceConstants.EVENT_WHITEBOARD_CONTROL_UNLOCK)
				|| command.equals(ConferenceConstants.EVENT_WHITEBOARD_CONTROL_LOCK))
			{
				this.activeResourceManager.controlWhiteboard(presenter,command);
			}
			else if (command.equals(ConferenceConstants.EVENT_WHITEBOARD_CONTROL_STOP))
			{
				this.activeResourceManager.stopSharingWhiteboard(presenter);
			}
		}
	}
	
	public	void	handleCobrowseControlMessage(IConferenceParticipant presenter,
			String command, String resourceId, String horScroll, String verScroll, String newName)
	{
		if (command != null)
		{
			
			if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_START))
			{
				this.activeResourceManager.startCobrowse(presenter, resourceId);
			}
			else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_STOP))
			{
				this.activeResourceManager.stopCobrowse(presenter, resourceId);
			}
			else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_SCROLL))
			{
				this.activeResourceManager.controlCobrowse(presenter, command, resourceId, horScroll, verScroll);
			}
			else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_NAVIGATE))
			{
				this.activeResourceManager.controlCobrowse(presenter, command, resourceId, horScroll, verScroll);
			}else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_RENAME))
			{
				this.activeResourceManager.renameCobrowse(presenter, command, resourceId, newName);
			}
			else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_LOCK))
			{
				this.activeResourceManager.controlCobrowse(presenter, command, resourceId, horScroll, verScroll);
			}
			else if (command.equals(ConferenceConstants.EVENT_COBROWSE_CONTROL_UNLOCK))
			{
				this.activeResourceManager.controlCobrowse(presenter, command, resourceId, horScroll, verScroll);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConferenceFeatureManager#getConference()
	 */
	public Conference getConference()
	{
		return activeConference.getConference();
	}
	public	void	forceStopActiveSharing(IConferenceParticipant presenter)
	{
		this.activeResourceManager.forceStopActiveSharing(presenter);
	}
}
