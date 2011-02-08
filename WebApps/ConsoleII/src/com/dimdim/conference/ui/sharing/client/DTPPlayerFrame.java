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

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DTPPlayerFrame extends	CollaborationWidgetFrame
{
	public	DTPPlayerFrame()
	{
	}
	public void refreshWidget(UIResourceObject resource,int width, int height)
	{
		//	Does not use the resource.
		if (super.currentResource == null)
		{
			String streamName = resource.getMediaId();
			String dtpProtocol = UIGlobals.getStreamingUrlsTable().getDtpProtocol();
			String dtpHost = UIGlobals.getStreamingUrlsTable().getDtpHost();
			String dtpPort = UIGlobals.getStreamingUrlsTable().getDtpPort();
			String dtpShareApp = UIGlobals.getStreamingUrlsTable().getDtpShareApp();
			
			String url = dtpProtocol+"://"+dtpHost+":"+dtpPort+"/screenshareviewer/"+
					dtpProtocol+"/"+dtpHost+"/"+dtpPort+"/"+dtpShareApp+"/"+
					ConferenceGlobals.conferenceKeyQualified+"/"+streamName+"/"+System.currentTimeMillis();
			
			this.setUrl(url);
		}
		//	DTP Player frame does not need to be reloaded for resize.
		super.refreshWidget(resource, width, height);
		this.setSize(width+"px", height+"px");
	}
	public	void	widgetHiding()
	{
		super.currentResource = null;
	}
}
