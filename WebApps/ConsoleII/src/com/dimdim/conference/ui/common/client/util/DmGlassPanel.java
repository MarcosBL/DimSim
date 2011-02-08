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
import com.google.gwt.user.client.Window;

import asquare.gwt.tk.client.ui.ModalDialog;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@dimdim.com
 * 
 */

public class DmGlassPanel extends PopupPanel implements ClickListener, PopupListener
{
	protected	VerticalPanel	basePanel = new VerticalPanel();
	protected	ScrollPanel		scrollPanel = new ScrollPanel();
	
	protected	ModalDialog		modalDialog;
	
	public	DmGlassPanel(ModalDialog modalDialog)
	{
		this.modalDialog = modalDialog;
		
		Label l = new Label(" ");
		add(basePanel);
		l.addClickListener(this);
		basePanel.add(scrollPanel);
		scrollPanel.add(l);
		
		basePanel.setSize(Window.getClientWidth()+"px",Window.getClientHeight()+"px");
		scrollPanel.setSize(Window.getClientWidth()+"px",Window.getClientHeight()+"px");
		basePanel.setStyleName("common-glass-panel");
		basePanel.setCellHorizontalAlignment(l,HorizontalPanel.ALIGN_CENTER);
		basePanel.setCellVerticalAlignment(l,VerticalPanel.ALIGN_MIDDLE);
		this.setPopupPosition(0,0);
	}
	public	void	onClick(Widget sender)
	{
		hide();
	}
	public	void	show()
	{
		super.show();
		if (this.modalDialog != null)
		{
			this.modalDialog.addPopupListener(this);
			this.modalDialog.show();
		}
	}
	public void onPopupClosed(PopupPanel modalDialog, boolean autoClosed)
	{
		hide();
	}
}
