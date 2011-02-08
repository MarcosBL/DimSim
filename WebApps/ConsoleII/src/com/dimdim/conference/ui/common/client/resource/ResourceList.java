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

package com.dimdim.conference.ui.common.client.resource;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.ResourceModelListener;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ResourceList extends DefaultList implements ResourceModelListener
{
	public ResourceList(ListControlsProvider listControlsProvider,
		ListPropertiesProvider listPropertiesProvider, boolean registerListener)
	{
		super(-1,ResourceGlobals.getResourceGlobals().getMaxVisibleResources(),
				ResourceGlobals.getResourceGlobals().getMaxVisibleResources(),
				listControlsProvider,listPropertiesProvider);
		if (registerListener)
		{
			ClientModel.getClientModel().getResourceModel().addListener(this);
		}
	}
	public void onResourceAdded(UIResourceObject res)
	{
		ResourceGlobals.getResourceGlobals().setDesktopResourceId(res);
		ListEntry listEntry = new ResourceListEntry(res,
				this.listControlsProvider.getListEntryControlsProvider(res),
				this.listPropertiesProvider.getListEntryPropertiesProvider(res));
		
		if (res.getOwnerId().equals(ResourceGlobals.SystemDefaultOwnerId))
		{
			listEntry.setDisplayRank(1);
		}
		if (res.getResourceType().equalsIgnoreCase(UIResourceObject.RESOURCE_TYPE_DESKTOP))
		{
			if (ConferenceGlobals.isPubSupportable())
			{
				this.addEntry(listEntry);
			}
		}
		else
		{
			this.addEntry(listEntry);
		}
	}
	public void onResourceRemoved(UIResourceObject res)
	{
		this.removeEntry(res.getResourceId());
	}
	public void onResourceRenamed(UIResourceObject res)
	{
		ListEntry entry = this.findEntry(res.getResourceId());
		if (entry != null)
		{
			entry.setName(res.getResourceName());
		}
	}
	public	void	showResourceBeingShared(UIResourceObject res)
	{
		if (res != null)
		{
			int	numResources = this.getListSize();
			//String type = null;
			for (int i=0; i<numResources; i++)
			{
				ResourceListEntry rle = (ResourceListEntry)this.getListEntryAt(i);
				//type = rle.getResource().getResourceType();
				if (rle.getId().equalsIgnoreCase(res.getResourceId()))
				{
					//Window.alert("setting resource as sharing.."+rle.getName()+" new image is ="+rle.getSharingInProgressImageUrl());
					rle.setImage5Url(rle.getSharingInProgressImageUrl(), true);
					rle.onShare();
					
				}
				else
				{
					//Window.alert("setting resource as stopped.."+rle.getName()+" new image is ="+rle.getSharingInProgressImageUrl());
					rle.setImage5Url(rle.getListEntryPropertiesProvider().getImage5Url());
					rle.onStopShare();
					
				}
			}
		}
	}
	public	void	showResourceSharingStopped()
	{
		int	numResources = this.getListSize();
		for (int i=0; i<numResources; i++)
		{
			ResourceListEntry rle = (ResourceListEntry)this.getListEntryAt(i);
			//Window.alert("setting resource as stopped.."+rle.getName()+" new image is ="+rle.getSharingInProgressImageUrl());
			rle.setImage5Url(rle.getListEntryPropertiesProvider().getImage5Url());
			rle.onStopShare();
		}
	}
	/**
	 * This event is raised only on attendee side when the resource is
	 * selected by the presenter on this console.
	 */
	public void onResourceSelected(UIResourceObject res)
	{
	}
	/**
	 * This event is obsolete.
	 */
	public void onResourceUpdated(UIResourceObject res)
	{
	}
	//	This event is raised on completing the resource roster processing. This is
	//	so that the type based list could add the additional panels for the types
	//	for which no instances are preloaded.
	public void onResourceRosterLoaded()
	{
	}
}


