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

import java.util.HashMap;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jayant Pandit
 * 
 */

public class ResourceTypeListPanel	extends	Composite
{
	protected	HashMap				panels = new HashMap();
	protected	VerticalPanel		basePanel = new VerticalPanel();
	protected	ResourceTypeList	resourceTypeList;
	protected	ResourceTypePanelsControlsProvider rtepcp;
	
	public ResourceTypeListPanel(ResourceTypeList resourceTypeList, ResourceTypePanelsControlsProvider rtepcp)
	{
		initWidget(basePanel);
		this.setStyleName("list-panel");
		
		this.resourceTypeList = resourceTypeList;
		this.rtepcp = rtepcp;
		resourceTypeList.setListPanel(this);
		
		int size = resourceTypeList.getListSize();
		for (int i=0; i<size; i++)
		{
			this.addEntryPanel(resourceTypeList.getListEntryAt(i));
		}
	}
	public	ResourceTypeListEntryPanel	addEntryPanel(ResourceTypeListEntry listEntry)
	{
		ResourceTypeListEntryPanel listEntryPanel = new ResourceTypeListEntryPanel(resourceTypeList.getResourceList(),listEntry, rtepcp);
		this.basePanel.add(listEntryPanel);
		this.panels.put(listEntry.getTypeName(), listEntryPanel);
		
		return	listEntryPanel;
	}
	public	ResourceTypeListEntryPanel	findResourceTypeListEntryPanel(String typeName)
	{
		return	(ResourceTypeListEntryPanel)this.panels.get(typeName);
	}
}
