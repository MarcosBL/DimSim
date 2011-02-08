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

import com.dimdim.conference.ui.json.client.UIPopoutPanelData;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Objects of this class help a panel that can be moved between console and
 * a popout to communicate from the popout back to the console. Unlike the
 * window proxy, which is used only on the console side, these proxies are
 * used on both console and popout window. Purpose of this proxy is to
 * faccilitate communication from the panel in the popout window to the
 * panel in the console. Communication from the console to popout is only
 * for events.
 */

public class PopoutPanelProxy
{
	protected	boolean		inPopout;
	protected	String		windowId;
	protected	PopoutSupportingPanel	panel;
	
	public	PopoutPanelProxy(String windowId,
				PopoutSupportingPanel panel, boolean inPopout)
	{
		this.windowId = windowId;
		this.inPopout = inPopout;
		this.panel = panel;
		
		panel.setPopoutPanelProxy(this);
	}
	public	void	sendMessageToConsole(String panelId, String dataText)
	{
		UIPopoutPanelData ppd = new UIPopoutPanelData();
		ppd.setPanelId(panelId);
		ppd.setDataText(dataText);
		ppd.setWindowId(this.windowId);
		
		this.enqueueDataMessage(ppd.toJson());
	}
	private	native void	enqueueDataMessage(String text) /*-{
		$wnd.sendMessageFromPopoutToConsole(text);
	}-*/;
	
}
