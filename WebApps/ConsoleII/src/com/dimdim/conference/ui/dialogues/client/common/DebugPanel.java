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

package com.dimdim.conference.ui.dialogues.client.common;

import asquare.gwt.tk.client.ui.ModalDialog;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DebugPanel //extends ModalDialog implements ClickListener
{
	/*
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
	
	private	DebugPanel()
	{
		add(this.scroller);
		scroller.add(this.basePanel);
		this.basePanel.setSize("100%","100%");
		
	}
	public	void	setHeight(String s)
	{
		this.scroller.setHeight(s);
		super.setHeight(s);
	}
	public	void	setWidth(String s)
	{
		this.scroller.setWidth(s);
		super.setWidth(s);
	}
	public	void	addDebugMessage(String msg)
	{
		if (numMessages++ > 1000)
		{
			for (int i=0; i<400; i++)
			{
				this.basePanel.remove(i);
			}
			numMessages -= 400;
		}
		this.basePanel.add(new HTML(System.currentTimeMillis()+":"+msg));
		this.numMessages++;
	}
	public	void	showDebugPanel()
	{
		this.show();
	}
	public	void	hideDebugPanel()
	{
		this.hide();
	}
	public	void	onClick(Widget w)
	{
		DebugPanel.getDebugPanel().showDebugPanel();
	}
	*/
}
