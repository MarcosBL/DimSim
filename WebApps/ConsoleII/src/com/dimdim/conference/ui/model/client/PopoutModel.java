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

package com.dimdim.conference.ui.model.client;

import java.util.Iterator;

import	com.dimdim.conference.ui.json.client.UIRosterEntry;
import	com.dimdim.conference.ui.json.client.UIStreamControlEvent;
import	com.dimdim.conference.ui.json.client.JSONurlReader;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PopoutModel	extends	FeatureModel
{
	public	static	final	String	WORKSPACE	=	"workspace";
	public	static	final	String	DEBUG	=	"debug";
	public	static	final	String	CHAT	=	"chat";
	
	protected	UIRosterEntry	me;
	
	public	PopoutModel(UIRosterEntry me)
	{
		super("feature.popout");
		
		this.me = me;
	}
	public	String	getPopoutWindowUrl(String panelId, String userId)
	{
		if (panelId.equals(PopoutModel.WORKSPACE) ||
				panelId.equals(PopoutModel.DEBUG) ||
				panelId.equals(PopoutModel.CHAT))
		{
			return this.commandsFactory.getPopoutPanelURL(panelId, userId);
		}
		return	null;
	}
	public	void	popoutPanel(String panelId,String userId)
	{
		String url = this.commandsFactory.getPopoutPanelURL(panelId, userId);
		this.executeCommand(url);
	}
	public	void	popinPanel(String panelId,String userId)
	{
		String url = this.commandsFactory.getPopinPanelURL(panelId, userId);
		this.executeCommand(url);
	}
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert(eventId);
		if (data != null)
		{
		}
		else
		{
//			Window.alert("No data for stream sharing event");
		}
	}
}

