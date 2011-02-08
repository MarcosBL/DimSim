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

package com.dimdim.conference.ui.resources.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * A resource type list entry may have a single resource associated with it
 * or a list of resources, e.g. desktop and whiteboard types will have only
 * one resource, where as ppt documents and web pages could have any number
 * of them.
 */

public class ResourceTypeListEntry implements Comparable
{
	protected	String	typeName;
	protected	String	displayName;
	
	protected	UIResourceObject	resourceObject;
	protected	Vector				resources;
	
	public ResourceTypeListEntry(String typeName, UIResourceObject resourceObject)
	{
		this.typeName = typeName;
		this.displayName = resourceObject.getResourceName();
		this.resourceObject = resourceObject;
	}
	public ResourceTypeListEntry(String typeName)
	{
		this.typeName = typeName;
		displayName = ConferenceGlobals.getDisplayString(typeName+".type.label","Documents");
		this.resources = new Vector();
	}
	public	int	compareTo(Object o)
	{
		ResourceTypeListEntry e = (ResourceTypeListEntry)o;
		
		return	typeName.compareTo(e.getTypeName());
	}
	public	boolean	equals(Object o)
	{
		return	(this.compareTo(o) == 0);
	}
	public String getDisplayName(boolean sharing)
	{
		String s = this.displayName;
		if (this.resources != null)
		{
//			s = s +" ("+this.resources.size()+")";
		}
		else
		{
			s = this.displayNameWithSharing(sharing);
		}
		return s;
	}
	private	String	displayNameWithSharing(boolean sharing)
	{
		String s = null;
		if (this.resourceObject != null)
		{
			//	This is an instance entry, like desktop or whiteboard. So the name is
			//	subject to sharing status.
			if(this.resourceObject.equals(ResourceGlobals.getResourceGlobals().getDesktopResource()))
			{
				if (sharing)
				{
					s = ConferenceGlobals.getDisplayString("stop.desktop.name","Stop Sharing Desktop");
				}
				else
				{
					s = ConferenceGlobals.getDisplayString("share.desktop.name","Share Desktop");
				}
			}
			else if(this.resourceObject.equals(ResourceGlobals.getResourceGlobals().getWhiteboardResource()))
			{
				if (sharing)
				{
					s = ConferenceGlobals.getDisplayString("stop.wb.name","Stop Sharing WhiteBoard");
				}
				else
				{
					s = ConferenceGlobals.getDisplayString("share.wb.name","Share WhiteBoard");
				}
			}
		}
		return	s;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getTypeName()
	{
		return typeName;
	}
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	public	boolean	hasMultipleInstances()
	{
		return	(this.resources != null);
	}
	public	void	addResourceObject(UIResourceObject res)
	{
		this.resources.addElement(res);
	}
	public	void	removeResourceObject(UIResourceObject res)
	{
		this.resources.removeElement(res);
	}
	public	UIResourceObject	getResourceObject()
	{
		return	this.resourceObject;
	}
	public	Vector	getResources()
	{
		return	this.resources;
	}
	public	Image	getSlot1Image()
	{
		Image imageUrl = null;
		if (typeName == null || typeName.equals(UIConstants.RESOURCE_TYPE_DEFAULT))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getDesktopShareItemImageUrl();
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getWhiteboardShareItemImageUrl();
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getDesktopShareItemImageUrl();
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getPowerpointItemImageUrl();
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getApplicationShareItemImageUrl();
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getShareIcon();
		}
		return imageUrl;
	}
	public	Image	getSlot5Image()
	{
		Image imageUrl = null;
		if (typeName == null || typeName.equals(UIConstants.RESOURCE_TYPE_DEFAULT))
		{
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
		{
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getHideChat();
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getHideChat();
		}
		else if (typeName.equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getHideChat();
		}
		return imageUrl;
	}
}

