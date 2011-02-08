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

package com.dimdim.conference.ui.managers.client.resource;

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceGlobals
{
	protected	static	ResourceGlobals	globalConstants = null;
	
	public	static	ResourceGlobals	getResourceGlobals()
	{
		if (ResourceGlobals.globalConstants == null)
		{
			ResourceGlobals.globalConstants = new ResourceGlobals();
		}
		return	ResourceGlobals.globalConstants;
	}
	
//	protected	UIResourceObject	currentActiveResource;
	protected	int					maxVisibleResources;
	protected	UIResourceObject	desktopResource;
	protected	UIResourceObject	whiteboardResource;
	
	public	ResourceGlobals()
	{
	}
	
//	public UIResourceObject getCurrentActiveResource()
//	{
//		return currentActiveResource;
//	}
//	public void setCurrentActiveResource(UIResourceObject currentActiveResource)
//	{
//		this.currentActiveResource = currentActiveResource;
//	}
	
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
	public	UIResourceObject	getWhiteboardResource()
	{
		return	this.whiteboardResource;
	}
	public	UIResourceObject	getDesktopResource()
	{
		return	this.desktopResource;
	}
	public	String	getDesktopResourceName()
	{
	//	return	"Desktop";
		return ConferenceGlobals.getDisplayString("resource.resourcename","Desktop");
	}
	public	String	getPopupHelpText(UIResourceObject res)
	{
//		String s = "This item has not yet been assigned a document. Import a presentation "+
//			"or an application share to enable sharing the item with others";
		String s = ConferenceGlobals.getDisplayString("resource.popuphelp","This item has not yet been assigned a document. Import a presentation or an application share to enable sharing the item with others");

		if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
//			s = "Click on share to start sharing this application with others. Selecting the item in the list will also start sharing the application frame.";
			s = ConferenceGlobals.getDisplayString("resource.popuphelp_app","Click on share to start sharing this application with others. Selecting the item in the list will also start sharing the application frame.");
			
		}
		else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
//			s = "Click on share to start sharing your desktop with others. Selecting the item in the list will also start sharing the desktop.";
			s = ConferenceGlobals.getDisplayString("resource.popuphelp_desktop","Click on share to start sharing your desktop with others. Selecting the item in the list will also start sharing the desktop.");	
		}
		else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
//			s = "Click on share to start sharing this presentation with others. Selecting the item in the list will allow you to preview the presentation.";
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
//			displayType = "Powerpoint Presentation";
			displayType = ConferenceGlobals.getDisplayString("resource.nicename1","Powerpoint Presentation");
		}
		else if (resType.equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
//			displayType = "Desktop Share";
			displayType = ConferenceGlobals.getDisplayString("resource.nicename2","Desktop Share");
		}
		else if (resType.equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
//			displayType = "Application Share";
			displayType = ConferenceGlobals.getDisplayString("resource.nicename3","Application Share");			
		}
		else if (resType.equals(UIConstants.RESOURCE_TYPE_DEFAULT))
		{
//			displayType = "Unassigned";
			displayType = ConferenceGlobals.getDisplayString("resource.nicename4","Unassigned");
		}
		return	displayType;
	}
	public	String	getRenameComment1(UIResourceObject res)
	{
//		String s = "Enter the new name:";
		String s = ConferenceGlobals.getDisplayString("resource.renamecomment1","Enter the new name:");
		return	s;
	}
	public	String	getDeleteComment1(UIResourceObject res)
	{
//		String s = "Are you sure you want to delete:";
		String s = ConferenceGlobals.getDisplayString("resource.deletecomment1","Are you sure you want to delete:");
		return	s;
	}
	public	String	getDeleteComment2(UIResourceObject res)
	{
		String s = null;
		if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
//			s = "This will also delete the uploaded presentation slides.";
			s = ConferenceGlobals.getDisplayString("resource.deletecomment2","This will also delete the uploaded presentation slides.");

		}
		return	s;
	}
	public	String	getSharedApplicationUnavailableHeading()
	{
//		return	"Application Window Unavailable";
		return ConferenceGlobals.getDisplayString("resource.shareapp_heading","Application Window Unavailable");
	}
	public	String	getSharedApplicationUnavailableMessage()
	{
//		return	"This shared application window is no longer available. This item will be deleted";
		return ConferenceGlobals.getDisplayString("resource.shareapp_message","This shared application window is no longer available. This item will be deleted");	
	}
	public	String	getSelectFileComment1()
	{
//		return	"Select a file to open";
		return ConferenceGlobals.getDisplayString("resource.filecomment1","Select a file to open");		

	}
	public	String	getSelectFileComment2()
	{
//		return	"or create a new file.";
		return ConferenceGlobals.getDisplayString("resource.filecomment2","or create a new file.");		
	
	}
	public	int	getMaxVisibleResources()
	{
		return	ConferenceGlobals.getLayoutParameterAsInt("resources_list.max_visible_length",5);
	}
}

