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

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public abstract	class CollaborationWidgetDisplayPanel extends HorizontalPanel implements CollaborationWidget
{
	protected	CollaborationWidgetContainer	container;
	
	protected	UIResourceObject	currentResource;
	
	protected	int		lastKnownWidth;
	protected	int		lastKnownHeight;
	
	protected	int		movieWidth = 0;
	protected	int		movieHeight = 0;
	
	protected	SWFWidget 		movieWidget = null;
	protected	HorizontalPanel	moviePanel;
	
	public	CollaborationWidgetDisplayPanel()
	{
		this.moviePanel = new HorizontalPanel();
	}
	public UIResourceObject getCurrentResource()
	{
		return currentResource;
	}
	public void refreshWidget(UIResourceObject resource, int width, int height)
	{
		this.currentResource = resource;
		this.lastKnownWidth = width;
		this.lastKnownHeight = height;
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
	protected	void	calculateMovieSize(int newWidth, int newHeight, int baseAspectRatio,
			int minimumWidth, int minimumHeight)
	{
		this.movieWidth = newWidth;
		if (this.movieWidth < minimumWidth)
		{
			this.movieWidth = minimumWidth;
		}
		this.movieHeight = newHeight;
		if (this.movieHeight < minimumHeight)
		{
			this.movieHeight = minimumHeight;
		}
		int	moviePanelAspectRatio = this.container.getContainerWidth()*1000/this.container.getContainerHeight();
		if (baseAspectRatio == moviePanelAspectRatio)
		{
			//	Exact size. Will have to be very lucky to get here.
		}
		else if (moviePanelAspectRatio < baseAspectRatio)
		{
			//	Available height is larger than required to match the full
			//	width. Keep the width same and reduce the movie height to
			//	scale.
			this.movieHeight = (this.movieWidth*1000)/baseAspectRatio;
		}
		else
		{
			//	Available width is larger than required to match the full
			//	height. Keep the height same and reduce the width.
			this.movieWidth = (this.movieHeight*baseAspectRatio)/1000;
		}
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
	public int getLastKnownHeight()
	{
		return lastKnownHeight;
	}
	public int getLastKnownWidth()
	{
		return lastKnownWidth;
	}
	public int getMovieHeight()
	{
		return movieHeight;
	}
	public int getMovieWidth()
	{
		return movieWidth;
	}
}
