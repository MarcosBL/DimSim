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

package com.dimdim.conference.ui.envcheck.client.layout;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import asquare.gwt.tk.client.ui.*;

public class TimeSensitiveModalDialog extends ModalDialog
			implements MouseListener, PopupListener
{
	protected	FocusPanel	pane = new FocusPanel();
	protected	Timer	timer;
	protected	int		showTime = 500;
	
	public TimeSensitiveModalDialog()
	{
		pane.setSize("100%","100%");
		pane.addMouseListener(this);
		this.addPopupListener(this);
		
		this.add(pane);
		
		timer = new Timer()
		{
			public void run()
			{
				timer = null;
				hide();
			}
		};
	}
	public	void	setDialogPosition(int left, int top)
	{
		//	Move the popup from its default center of the page position if required.
		removeController(getController(ModalDialog.PositionDialogController.class));
		setPopupPosition(left, top);
	}
	public int getShowTime()
	{
		return showTime;
	}
	public void setShowTime(int showTime)
	{
		this.showTime = showTime;
	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		if (this.timer != null)
		{
			this.timer.cancel();
		}
	}
	public void closePopup()
	{
		if (this.timer != null)
		{
			this.timer.schedule(this.showTime);
		}
	}
	public void onMouseLeave(Widget sender)
	{
		if (sender.equals(pane))
		{
			closePopup();
		}
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
	public	void	onPopupClosed(PopupPanel popup, boolean autoClosed)
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
	}
}
