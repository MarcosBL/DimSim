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

package com.dimdim.conference.ui.common.client.list;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This popup panel is used by the list widget as the popup for list entry
 * mouse over. The popup panel is expected to be small and limited in
 * functionality. Hence a common panel with some specific configurability
 * is expected to be useful and sufficient for all the lists in the ui,
 * which at present are two, users and resources.
 * 
 * The layout of the panel is always the same. It has a header on which
 * maximum of four buttons can be placed. Each button will trigger a
 * specific callback in the list entry.
 * 
 * The layout has a body. This widget must be created by each specific list
 * entry.
 * 
 * And the panel has a footer, which may have additional maximum two links.
 */

//import java.util.Vector;

//import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
//import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.NonModalPopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.Window;

import com.dimdim.conference.ui.common.client.UIGlobals;

public class ListEntryHoverPopupPanel extends NonModalPopupPanel
			implements MouseListener, FocusListener, ClickListener, PopupListener
{
//	protected	FocusPanel	pane = new FocusPanel();
	protected	int		showTime = UIGlobals.getHoverInitialShowTime();
	protected	Timer	timer;
	protected	boolean	hasFocus	=	false;
    
	protected	HorizontalPanel	headerPanel	=	new	HorizontalPanel();
	protected	HorizontalPanel	contentPanel	=	new	HorizontalPanel();
	protected	HorizontalPanel	linksPanel	=	new	HorizontalPanel();
	
	protected 	boolean subMenuOpen = false;
	
	public ListEntryHoverPopupPanel()
	{
		super(false);
		this.setStyleName("dm-hover-popup");
		
		//pane.setSize("100%","100%");
//		pane.addMouseListener(this);
//		pane.addFocusListener(this);
		      
		DockPanel outer = new DockPanel();
//		outer.setSize("100%","100%");
		outer.setStyleName("dm-hover-popup-body");
//		pane.add(outer);
		
		headerPanel.setStyleName("dm-hover-popup-header");
		outer.add(headerPanel,DockPanel.NORTH);
		outer.setCellWidth(headerPanel,"100%");
		
		this.contentPanel.setStyleName("dm-hover-popup-content");
		outer.add(contentPanel,DockPanel.NORTH);
//		outer.setCellWidth(contentPanel,"100%");
		outer.setCellHeight(contentPanel,"100%");
		
		this.linksPanel.setStyleName("dm-hover-popup-links-panel");
		outer.add(linksPanel,DockPanel.SOUTH);
//		outer.setCellWidth(contentPanel,"100%");
		outer.setCellHeight(linksPanel,"100%");
		
//		this.add(pane);
		this.add(outer);
		this.addPopupListener(this);
	}
	public	void	popupVisible()
	{
		this.timer = new Timer()
		{
			public void run()
			{
				timer = null;
				hideHoverPopup();
			}
		};
		this.timer.schedule(this.showTime);
	}
	public void onFocus(Widget sender)
	{
		onMouseEnter(sender);
	}
	public void onLostFocus(Widget sender)
	{
		onMouseLeave(sender);
	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
		this.hasFocus = true;
		this.showTime = UIGlobals.getHoverPostMouseOutShowTime();
	}
	public void closePopup()
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
		if (!this.hasFocus)
		{
			timer = new Timer()
			{
				public void run()
				{
					timer = null;
					hideHoverPopup();
				}
			};
			timer.schedule(this.showTime);
		}
	}
	public void onMouseLeave(Widget sender)
	{
		//Window.alert("inside mouse leave subMenuOpen = "+subMenuOpen);
		if(!subMenuOpen)
		{
			this.hasFocus = false;
			//Window.alert("inside mouse leave of hover popup closing this");
			closePopup();
		}
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
	public void onClick(Widget sender)
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
	public int getShowTime()
	{
		return showTime;
	}
	public void setShowTime(int showTime)
	{
		this.showTime = showTime;
	}
	public	boolean	supportsRefresh()
	{
		return	false;
	}
	public	void	hideHoverPopup()
	{
		//Window.alert("inside ListEntryHoverPanel hiding hover popup..");
		this.showTime = UIGlobals.getHoverInitialShowTime();
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
//		super.hideHoverPopup();
	}
}
