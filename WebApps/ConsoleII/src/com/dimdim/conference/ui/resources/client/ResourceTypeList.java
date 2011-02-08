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

package com.dimdim.conference.ui.resources.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.resource.ResourceList;
import com.dimdim.conference.ui.common.client.resource.ResourceListEntry;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.ResourceModelListener;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The resource type list is a fixed list. There is no need for dynamism as
 * new resource types are not added on the fly.
 * 
 * The number of active types at a moment would change based on whether
 * resources like whiteboard and desktop are supported and used.
 * 
 * The process here is that as the resources are added by the resource roster
 * event, the type list is populated. At the end of the resource roster event
 * the resource list panel will be truely completed. At this the model will
 * call the roster completed method, which will then paint the resource list
 * panel.
 */

public class ResourceTypeList implements ResourceModelListener
{
	protected	Vector			activeTypes;
	protected	ResourceList	resourceList;
	
	protected	ResourceTypeListPanel	listPanel;
	
	public ResourceTypeList(ResourceList resourceList)
	{
		this.resourceList = resourceList;
		this.activeTypes = new Vector();
		ClientModel.getClientModel().getResourceModel().addListener(this);
	}
	public	int	getListSize()
	{
		return	this.activeTypes.size();
	}
	public	ResourceTypeListEntry	getListEntryAt(int i)
	{
		return	(ResourceTypeListEntry)this.activeTypes.elementAt(i);
	}
	public	ResourceList	getResourceList()
	{
		return	this.resourceList;
	}
	
	//	Set the count of the appropriate type entry on resource add and
	//	remove. All other events will be handled by the resource list
	//	The resource added
	public void onResourceAdded(UIResourceObject res)
	{
		String typeName = res.getResourceType();
		ResourceTypeListEntry entry = this.findResourceTypeListEntry(typeName);
		if (entry == null)
		{
			if (typeName.equals(UIResourceObject.RESOURCE_TYPE_DESKTOP) ||
					typeName.equals(UIResourceObject.RESOURCE_TYPE_WHITEBOARD))
			{
				entry = new ResourceTypeListEntry(typeName, res);
			}
			else
			{
				entry = new ResourceTypeListEntry(typeName);
			}
			this.activeTypes.add(entry);
//			this.listPanel.addEntryPanel(entry);
		}
		else
		{
			//	This would mean that the said type has already been found
			//	the respective panel already exists. No action required.
		}
		if (entry.hasMultipleInstances())
		{
			entry.addResourceObject(res);
			//	Reset the instance count on the list entry panel.
			ResourceTypeListEntryPanel panel = this.listPanel.findResourceTypeListEntryPanel(typeName);
			if (panel != null)
			{
				panel.resourceCountChanged(entry.getResources().size(),entry.getDisplayName(false));
			}
		}
	}
	public void onResourceRemoved(UIResourceObject res)
	{
		String typeName = res.getResourceType();
		ResourceTypeListEntry entry = this.findResourceTypeListEntry(typeName);
		if (entry != null)
		{
			if (entry.hasMultipleInstances())
			{
				entry.removeResourceObject(res);
				//	Reset the instance count on the list entry panel.
				ResourceTypeListEntryPanel panel = this.listPanel.findResourceTypeListEntryPanel(typeName);
				if (panel != null)
				{
					panel.resourceCountChanged(entry.getResources().size(),entry.getDisplayName(false));
				}
			}
		}
	}
	public void onResourceRenamed(UIResourceObject res)
	{
	}
	public void onResourceSelected(UIResourceObject res)
	{
	}
	public void onResourceUpdated(UIResourceObject res)
	{
	}
	//	This event is raised on completing the resource roster processing. This is
	//	so that the type based list could add the additional panels for the types
	//	for which no instances are preloaded.
	public void onResourceRosterLoaded()
	{
		ResourceTypeListEntry entry = this.findResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_PRESENTATION);
		if (entry == null )
		{
			entry = new ResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_PRESENTATION);
			this.activeTypes.add(entry);
		}
		if (ConferenceGlobals.cobEnabled)
		{
			//	Add the web pages type entry.
			entry = this.findResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_COBROWSE);
			if (entry == null)
			{
				entry = new ResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_COBROWSE);
				this.activeTypes.add(entry);
			}
		}
		entry = this.findResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_DESKTOP);
		if (entry != null && entry.getResourceObject() != null)
		{
			ResourceListEntry resourceListEntry = (ResourceListEntry)this.resourceList.findEntry(entry.getResourceObject().getResourceId());
			if (resourceListEntry != null)
			{
				ResourceTypeListEntryPanel panel = this.listPanel.addEntryPanel(entry);
				resourceListEntry.setShareListener(panel);
			}
		}
		entry = this.findResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_WHITEBOARD);
		if (entry != null)
		{
			ResourceTypeListEntryPanel panel = this.listPanel.addEntryPanel(entry);
			ResourceListEntry resourceListEntry = (ResourceListEntry)this.resourceList.findEntry(entry.getResourceObject().getResourceId());
			resourceListEntry.setShareListener(panel);
		}
		if(ConferenceGlobals.docEnabled)
		{
			entry = this.findResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_PRESENTATION);
			if (entry != null)
			{
				this.listPanel.addEntryPanel(entry);
			}
		}
		entry = this.findResourceTypeListEntry(UIResourceObject.RESOURCE_TYPE_COBROWSE);
		if (entry != null)
		{
			this.listPanel.addEntryPanel(entry);
		}
	}
	public	ResourceListEntry	findResourceListEntry(String id)
	{
		return	(ResourceListEntry)this.resourceList.findEntry(id);
	}
	public	ResourceTypeListEntry	findResourceTypeListEntry(String typeName)
	{
		ResourceTypeListEntry	entry = null;
		int	size = this.activeTypes.size();
		for (int i=0; i<size; i++)
		{
			entry = (ResourceTypeListEntry)this.activeTypes.elementAt(i);
			if (entry.getTypeName().equals(typeName))
			{
				break;
			}
			else
			{
				entry = null;
			}
		}
		return	entry;
	}
	public void setListPanel(ResourceTypeListPanel listPanel)
	{
		this.listPanel = listPanel;
	}
}


