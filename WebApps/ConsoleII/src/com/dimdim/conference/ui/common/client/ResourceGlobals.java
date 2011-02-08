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

package com.dimdim.conference.ui.common.client;

import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceGlobals
{
	public	static String	SystemDefaultOwnerId = "SYSTEM";
	public	static String	PreloadedDefaultOwnerId = "PRELOADED";
	
	public	static String	SystemDefaultMeetingKey = "global-meeting";
	public	static String	PreloadedDefaultMeetingKey = "preloaded";
	
	public	static	boolean	allowDelete(UIResourceObject resource)
	{
		String ownerId = resource.getOwnerId();
		return !ownerId.equals(ResourceGlobals.SystemDefaultOwnerId) &&
			!ownerId.equals(ResourceGlobals.PreloadedDefaultOwnerId);
	}
	public	static	String	getMeetingIdIfDefaultResource(UIResourceObject resource)
	{
		String meetingId = null;
		String ownerId = resource.getOwnerId();
		if (ownerId.equals(ResourceGlobals.SystemDefaultOwnerId))
		{
			meetingId = ResourceGlobals.SystemDefaultMeetingKey;
		}
		else if (ownerId.equals(ResourceGlobals.PreloadedDefaultOwnerId))
		{
			meetingId = ResourceGlobals.PreloadedDefaultMeetingKey;
		}
		return	meetingId;
	}
	protected	static	ResourceGlobals	globalConstants = null;
	
	public	static	ResourceGlobals	getResourceGlobals()
	{
		if (ResourceGlobals.globalConstants == null)
		{
			ResourceGlobals.globalConstants = new ResourceGlobals();
		}
		return	ResourceGlobals.globalConstants;
	}
	
	protected	int					maxVisibleResources;
	protected	UIResourceObject	desktopResource;
	protected	UIResourceObject	whiteboardResource;
	
	public	ResourceGlobals()
	{
	}
	
	public	void	setDesktopResourceId(UIResourceObject res)
	{
		if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			this.desktopResource = res;
		}
		else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
		{
			this.whiteboardResource = res;
		}
	}
	public	UIResourceObject	getDesktopResource()
	{
		return	this.desktopResource;
	}
	public	UIResourceObject	getWhiteboardResource()
	{
		return	this.whiteboardResource;
	}
	public	String	getDesktopResourceName()
	{
		return ConferenceGlobals.getDisplayString("resource.resourcename","Desktop");
	}
	public	String	getPopupHelpText(UIResourceObject res)
	{
		String s = ConferenceGlobals.getDisplayString("resource.popuphelp","This item has not yet been assigned a document. Import a presentation or an application share to enable sharing the item with others");

		if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
			s = ConferenceGlobals.getDisplayString("resource.popuphelp_app","Click on share to start sharing this application with others. Selecting the item in the list will also start sharing the application frame.");
		}
		else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			s = ConferenceGlobals.getDisplayString("resource.popuphelp_desktop","Click on share to start sharing your desktop with others. Selecting the item in the list will also start sharing the desktop.");	
		}
		else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			s = ConferenceGlobals.getDisplayString("resource.popuphelp_presentation","Click on share to start sharing this presentation with others. Selecting the item in the list will allow you to preview the presentation.");	
		}
		return	s;
	}
	public	String	getResourceTypeNiceName(UIResourceObject res)
	{
		String resType = res.getResourceType();
		String displayType = resType;
		if (resType.equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			displayType = ConferenceGlobals.getDisplayString("resource.nicename1","Powerpoint Presentation");
		}
		else if (resType.equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			displayType = ConferenceGlobals.getDisplayString("resource.nicename2","Desktop Share");
		}
		else if (resType.equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
			displayType = ConferenceGlobals.getDisplayString("resource.nicename3","Application Share");			
		}
		else if (resType.equals(UIConstants.RESOURCE_TYPE_DEFAULT))
		{
			displayType = ConferenceGlobals.getDisplayString("resource.nicename4","Unassigned");
		}
		else if (resType.equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
		{
			displayType = ConferenceGlobals.getDisplayString("resource.nicename5","Whiteboard Share");
		}else if (resType.equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			displayType = ConferenceGlobals.getDisplayString("resource.nicename8","URL Share");
		}
		return	displayType;
	}
	public	String	getRenameComment1(UIResourceObject res)
	{
		String s = ConferenceGlobals.getDisplayString("resource.renamecomment1","Enter the new name:");
		return	s;
	}
	public	String	getDeleteComment1(UIResourceObject res)
	{
		String s = ConferenceGlobals.getDisplayString("resource.deletecomment1","Are you sure you want to delete:");
		return	s;
	}
	public	String	getDeleteComment2(UIResourceObject res)
	{
		String s = null;
		if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			s = ConferenceGlobals.getDisplayString("resource.deletecomment2","This will also delete the uploaded presentation slides.");

		}
		return	s;
	}
	public	String	getSharedApplicationUnavailableHeading()
	{
		return ConferenceGlobals.getDisplayString("resource.shareapp_heading","Application Window Unavailable");
	}
	public	String	getSharedApplicationUnavailableMessage()
	{
		return ConferenceGlobals.getDisplayString("resource.shareapp_message","This shared application window is no longer available. This item will be deleted");	
	}
	public	String	getSelectFileComment1()
	{
		return ConferenceGlobals.getDisplayString("resource.filecomment1","Select a file to open");
	}
	public	String	getSelectFileComment2()
	{
		return ConferenceGlobals.getDisplayString("resource.filecomment2","or create a new file.");
	}
	public	int	getMaxVisibleResources()
	{
		return	ConferenceGlobals.getLayoutParameterAsInt("resources_list.max_visible_length",5);
	}
	public	Image	getShareControlImageUrl()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getShareControlImage();
	}
	public	Image	getDeleteControlImageUrl()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getDeleteControlImage();
	}
	public	Image	getRenameControlImageUrl()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getRenameControlImage();
	}
}

