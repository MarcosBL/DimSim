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
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceListEntry	extends	ListEntry
{
	protected	UIResourceObject	resource;
	
	public	ResourceListEntry(UIResourceObject resource,
		ListEntryControlsProvider	listEntryControlsProvider,
		ListEntryPropertiesProvider	listEntryPropertiesProvider)
	{
		super(resource.getResourceId(),resource.getResourceName(),
				listEntryControlsProvider,listEntryPropertiesProvider);
		this.resource = resource;
	}
	public	UIResourceObject	getResource()
	{
		return	this.resource;
	}
	public	Image	getSharingInProgressImageUrl()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getActiveShareItemImageUrl();
	}
	
	public void onShare()
	{
		if(null != shareListener)
		{
			shareListener.onStartShare();
		}
		if(getResource().equals(ResourceGlobals.getResourceGlobals().getDesktopResource()))
		{
			//setImage1Tooltip(ConferenceGlobals.getDisplayString("stop.desktop.name","Stop Sharing Desktop") );
			setName(ConferenceGlobals.getDisplayString("stop.desktop.name","Stop Sharing Desktop") );
		}else if(getResource().equals(ResourceGlobals.getResourceGlobals().getWhiteboardResource())){
			//setImage1Tooltip(ConferenceGlobals.getDisplayString("stop.wb.name","Stop WhiteBoard") );
			setName(ConferenceGlobals.getDisplayString("stop.wb.name","Stop WhiteBoard") );
		}else{
			//Window.alert("setting display rank.. 2 "+getResource().getResourceName());
			this.setDisplayRank(2);
		}
	}
	
	public void onStopShare()
	{
		if(null != shareListener)
		{
			shareListener.onStopShare();
		}
		if(getResource().equals(ResourceGlobals.getResourceGlobals().getDesktopResource()))
		{
			//setImage1Tooltip(ConferenceGlobals.getDisplayString("share.desktop.name","Share Desktop") );
			setName(ConferenceGlobals.getDisplayString("share.desktop.name","Share Desktop") );
		}else if(getResource().equals(ResourceGlobals.getResourceGlobals().getWhiteboardResource())){
			//setImage1Tooltip(ConferenceGlobals.getDisplayString("share.wb.name", "Share WhiteBoard") );
			setName(ConferenceGlobals.getDisplayString("share.wb.name", "Share WhiteBoard") );
		}else{
			//Window.alert("setting display rank.. 3 "+getResource().getResourceName());
			this.setDisplayRank(4);
		}
	}
}
