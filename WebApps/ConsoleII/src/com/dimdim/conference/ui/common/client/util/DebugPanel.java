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

package com.dimdim.conference.ui.common.client.util;

import java.util.Vector;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ClickListener;

import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.json.client.UIServerResponse;
import com.dimdim.conference.ui.model.client.EventsTracker;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DebugPanel extends CommonModalDialog implements ClickListener, EventsTracker, FlashStreamHandler
{
	private	static	DebugPanel	debugPanel;
	
	public	static	DebugPanel	getDebugPanel()
	{
		if (DebugPanel.debugPanel == null)
		{
			DebugPanel.debugPanel = new DebugPanel();
		}
		return	DebugPanel.debugPanel;
	}
	
	protected	VerticalPanel	basePanel = new VerticalPanel();
	protected	ScrollPanel		scroller = new ScrollPanel();
	protected	int			numMessages = 0;
	protected	boolean		dialogDrawn = false;
	
	public	DebugPanel()
	{
		super("Debug");
//		FlashCallbackHandler.getHandler().addStreamHandler(this);
	}
	public	void	addDebugMessage(String msg)
	{
		if (numMessages++ > 100)
		{
			for (int i=0; i<40; i++)
			{
				this.basePanel.remove(i);
			}
			numMessages -= 40;
		}
		Label entry = new Label(System.currentTimeMillis()+":"+msg);
		entry.setWordWrap(true);
		entry.setStyleName("common-text");
		this.basePanel.add(entry);
	}
	public	void	showDebugPanel()
	{
		if (!dialogDrawn)
		{
			this.drawDialog();
			this.dialogDrawn = true;
		}
		else
		{
			this.show();
		}
	}
	public	void	onClick(Widget w)
	{
		showDebugPanel();
	}
	protected Widget getContent()
	{
		this.scroller.setSize("600px","300px");
		this.scroller.add(this.basePanel);
		this.basePanel.setSize("100%","100%");
		
		return scroller;
	}
	protected Vector getFooterButtons()
	{
		return null;
	}
	public void onEvent(UIServerResponse event)
	{
		this.addDebugMessage(event.toString());
	}
	public String getStreamName()
	{
		return "FLASH_MESSAGE_LOG";
	}
	public void handleEvent(String logMessageEntry)
	{
		addDebugMessage("log message from flash: "+logMessageEntry);
	}
}
