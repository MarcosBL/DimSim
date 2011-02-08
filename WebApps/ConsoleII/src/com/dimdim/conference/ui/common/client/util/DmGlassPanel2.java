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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                    *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.common.client.util;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.NonModalPopupPanel;
import com.google.gwt.user.client.Window;

import asquare.gwt.tk.client.ui.ModalDialog;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@dimdim.com
 * 
 */

public class DmGlassPanel2 extends PopupPanel implements ClickListener, PopupListener
{
	protected	VerticalPanel	basePanel = new VerticalPanel();
	protected	ScrollPanel		scrollPanel = new ScrollPanel();
	
	protected	NonModalPopupPanel		popup;
	
	public	DmGlassPanel2(NonModalPopupPanel popup)
	{
		this.popup = popup;
		
		Label l = new Label(" ");
		add(basePanel);
		l.addClickListener(this);
		basePanel.add(scrollPanel);
		scrollPanel.add(l);
		
		basePanel.setSize(Window.getClientWidth()+"px",Window.getClientHeight()+"px");
		l.setSize(Window.getClientWidth()+"px",Window.getClientHeight()+"px");
		scrollPanel.setSize(Window.getClientWidth()+"px",Window.getClientHeight()+"px");
		basePanel.setStyleName("common-glass-panel-2");
		basePanel.setCellHorizontalAlignment(l,HorizontalPanel.ALIGN_CENTER);
		basePanel.setCellVerticalAlignment(l,VerticalPanel.ALIGN_MIDDLE);
		this.setPopupPosition(0,0);
	}
	public	void	onClick(Widget sender)
	{
		if (this.popup != null)
		{
			popup.hide();
		}
	}
	public	void	show(int x, int y)
	{
		super.show();
		if (this.popup != null)
		{
			this.popup.addPopupListener(this);
			this.popup.show();
			this.popup.setPopupPosition(x, y);
		}
	}
	public void onPopupClosed(PopupPanel modalDialog, boolean autoClosed)
	{
		if (this.isVisible())
		{
			hide();
		}
	}
}
