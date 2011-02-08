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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class WaitingPageFrame extends	CollaborationWidgetFrame
{
	public	WaitingPageFrame()
	{
	}
	public void refreshWidget(UIResourceObject resource,int width, int height, String url)
	{
		//	Does not use the resource.
		super.refreshWidget(resource, width, height);
		
		this.setUrl(url);
		this.setSize(width+"px", height+"px");
	}
	
	public void refreshWidget(UIResourceObject resource,int width, int height)
	{
		super.refreshWidget(resource, width, height);
		this.setSize(width+"px", height+"px");
	}
}
