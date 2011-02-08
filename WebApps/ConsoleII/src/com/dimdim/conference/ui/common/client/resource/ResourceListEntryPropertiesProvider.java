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

package com.dimdim.conference.ui.common.client.resource;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.list.DefaultListEntryPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceListEntryPropertiesProvider extends DefaultListEntryPropertiesProvider
{
	protected	UIResourceObject	resource;
	
	public	ResourceListEntryPropertiesProvider(UIResourceObject resource)
	{
		this.resource = resource;
	}
	public Image getImage1Url()
	{
		String type = resource.getResourceType();
		Image imageUrl = null;
		if (type == null || type.equals(UIConstants.RESOURCE_TYPE_DEFAULT))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getDesktopShareItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getWhiteboardShareItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getDesktopShareItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getPowerpointItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getApplicationShareItemImageUrl();
		}else if (type.equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getShareIcon();
		}
		return imageUrl;
	}
	
	public String getImage1Tooltip()
	{
	    return resource.getResourceName();
	}
	
	public String getNameLabelStyle()
	{
		return "show";
	}
	public Image getImage2Url()
	{
		//return ResourceGlobals.getResourceGlobals().getShareControlImageUrl();
		return null;
	}
	public String getImage2Tooltip()
	{
		return ConferenceGlobals.getTooltip("resource.share")+" "+
			ResourceGlobals.getResourceGlobals().getResourceTypeNiceName(resource);
	}
//	public Image getImage3Url()
//	{
//		if (ResourceGlobals.allowDelete(this.resource))
//		{
//			return	ResourceGlobals.getResourceGlobals().getDeleteControlImageUrl();
//		}
//		return null;
//	}
//	public String getImage3Tooltip()
//	{
//		if (ResourceGlobals.allowDelete(this.resource))
//		{
//			return ConferenceGlobals.getTooltip("resource.delete")+" "+
//				ResourceGlobals.getResourceGlobals().getResourceTypeNiceName(resource);
//		}
//		return null;
//	}
	/*
	public String getImage4Url()
	{
		if (!this.resource.getOwnerId().equals("SYSTEM"))
		{
			return	ResourceGlobals.getResourceGlobals().getRenameControlImageUrl();
		}
		return null;
	}
	*/
}
