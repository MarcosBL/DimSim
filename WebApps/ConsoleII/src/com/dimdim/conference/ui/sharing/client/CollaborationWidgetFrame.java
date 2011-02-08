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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.sharing.client;

import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.google.gwt.user.client.ui.Frame;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public abstract	class CollaborationWidgetFrame extends Frame implements CollaborationWidget
{
	protected	CollaborationWidgetContainer	container;
	
	protected	UIResourceObject	currentResource;
	
	protected	int		lastKnownWidth;
	protected	int		lastKnownHeight;
	
	public UIResourceObject getCurrentResource()
	{
		return currentResource;
	}
	protected	boolean	requiresRefresh(UIResourceObject resource, int width, int height)
	{
		if (this.currentResource == null)
		{
			return	true;
		}
		if (!this.currentResource.getResourceId().equals(this.currentResource.getResourceId())
				|| this.lastKnownWidth != width || this.lastKnownHeight == height)
		{
			return	true;
		}
		return	false;
	}
	public void refreshWidget(UIResourceObject resource, int width, int height)
	{
		this.currentResource = resource;
		this.lastKnownWidth = width;
		this.lastKnownHeight = height;
	}
	public void setContainer(CollaborationWidgetContainer container)
	{
		this.container = container;
	}
//	public void widgetDeactivating()
//	{
//	}
	public void widgetHiding()
	{
	}
//	public void widgetShowing()
//	{
//	}
}
