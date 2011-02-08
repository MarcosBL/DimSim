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

package com.dimdim.conference.ui.resources.client;

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

import java.util.Vector;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.NonModalPopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.Window;

import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.resource.ResourceList;
import com.dimdim.conference.ui.common.client.resource.ResourceListEntry;

public class ResourceTypeListEntryPopupPanel extends NonModalPopupPanel
			implements MouseListener, FocusListener, ClickListener, PopupListener
{
	protected	FocusPanel	pane = new FocusPanel();
	protected	int		showTime = UIGlobals.getHoverInitialShowTime();
	protected	Timer	timer;
	protected	boolean	hasFocus	=	false;
    
	protected	HorizontalPanel	headerPanel	=	new	HorizontalPanel();
//	protected	HorizontalPanel	contentPanel	=	new	HorizontalPanel();
	protected	HorizontalPanel	linksPanel	=	new	HorizontalPanel();
	
	protected 	boolean subMenuOpen = false;
	protected	String			typeName;
	protected	ResourceList	resourceList;
	protected	ResourceTypePanelsControlsProvider	rtpcp;
	protected	ResourceHoverStyler					hoverStyler;
	protected	int						numberOfItems=0;
	
	public ResourceTypeListEntryPopupPanel(ResourceList resourceList,
			ResourceTypePanelsControlsProvider rtpcp)
	{
		super(false);
		this.setStyleName("dm-hover-popup");
		this.addStyleName("resource-popup-panel");
		this.resourceList = resourceList;
		this.rtpcp = rtpcp;
		this.hoverStyler = new ResourceHoverStyler();
	}
	public	void	paintPanel(String typeName)
	{
		this.typeName = typeName;
		ClickListener headerClickListener = rtpcp.getTypePopupHeaderClickListener(typeName);
		ClickListener footerClickListener = rtpcp.getTypePopupFooterClickListener(this.typeName);
		
		/*if(UIResourceObject.RESOURCE_TYPE_COBROWSE.equalsIgnoreCase(typeName))
		{
			headerClickListener = rtpcp.getShareCobClickListener();
		}*/
		pane.addMouseListener(this);
		pane.addFocusListener(this);
		
		DockPanel outer = new DockPanel();
		outer.setStyleName("dm-hover-popup-body");
		pane.add(outer);
		
		if (headerClickListener != null)
		{
//			headerPanel.setStyleName("dm-hover-popup-header");
			outer.add(headerPanel,DockPanel.NORTH);
			outer.setCellWidth(headerPanel,"100%");
			
			String selectLabel = ConferenceGlobals.getDisplayString(typeName+".select.label","Upload Document");
			Label headerLink = new Label(selectLabel);
//			headerLink.setStyleName("tool-entry");
			headerLink.setStyleName("resource-popup-header-entry");
			headerLink.addStyleName("anchor-cursor");
			headerPanel.add(headerLink);
			headerPanel.setCellWidth(headerLink, "100%");
			headerPanel.setStyleName("resource-popup-header-panel");
			headerPanel.setCellHorizontalAlignment(headerLink, HorizontalPanel.ALIGN_LEFT);
			headerPanel.setCellVerticalAlignment(headerLink, VerticalPanel.ALIGN_MIDDLE);
			headerLink.addClickListener(headerClickListener);
			headerLink.addClickListener(this);
		}
		else
		{
//			Window.alert("No header listener");
		}
		
		this.writeListPanel(outer);
//		this.contentPanel.setStyleName("dm-hover-popup-content");
//		outer.add(contentPanel,DockPanel.NORTH);
//		outer.setCellHeight(contentPanel,"100%");
//		outer.setCellWidth(contentPanel,"100%");
		
		if (footerClickListener != null)
		{
//			this.linksPanel.setStyleName("dm-hover-popup-header");
			outer.add(linksPanel,DockPanel.SOUTH);
			outer.setCellHeight(linksPanel,"100%");
		
			String manageLabel = ConferenceGlobals.getDisplayString(typeName+".manage.label","Manage Document");
			Label footerLink = new Label(manageLabel);
//			footerLink.setStyleName("tool-entry");
			footerLink.setStyleName("resource-popup-footer-entry");
			footerLink.addStyleName("anchor-cursor");
			linksPanel.add(footerLink);
			linksPanel.setCellHorizontalAlignment(footerLink, HorizontalPanel.ALIGN_LEFT);
			linksPanel.setCellVerticalAlignment(footerLink, VerticalPanel.ALIGN_MIDDLE);
			footerLink.addClickListener(footerClickListener);
			footerLink.addClickListener(this);
		}
		else
		{
//			Window.alert("No footer listener");
		}
		
//		if (panel != null)
//		{
//			this.contentPanel.add(panel);
//			this.contentPanel.setCellWidth(panel, "100%");
//			this.contentPanel.setCellHeight(panel, "100%");
//		}
		
		this.add(pane);
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
		if(!subMenuOpen)
		{
			this.hasFocus = false;
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
		this.hide();
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
		this.showTime = UIGlobals.getHoverInitialShowTime();
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
	}
	private	void	writeListPanel(DockPanel outer)
	{
		Vector vec = new Vector();
		UIResourceObject currentActiveResource = ConferenceGlobals.getCurrentSharedResource();
		numberOfItems = 0;
		
		int size = this.resourceList.getListSize();
		for (int i=0; i<size; i++)
		{
			UIResourceObject res = ((ResourceListEntry)this.resourceList.getListEntryAt(i)).getResource();
			if (res.getResourceType().equals(this.typeName))
			{
				Label resLabel = new FixedLengthLabel(res.getResourceName(),26);
//				resLabel.setStyleName("tool-entry");
				resLabel.setStyleName("resource-entry");
				resLabel.addStyleName("anchor-cursor");
				resLabel.addClickListener(this);
				resLabel.addClickListener(this.rtpcp.getNameLabelClickListener(res));
				
				HorizontalPanel h1 = new HorizontalPanel();
				Widget img = new HorizontalPanel();
				
				if (currentActiveResource != null &&
						currentActiveResource.getResourceId().equals(res.getResourceId()))
				{
					img = this.getSharingInProgressImageUrl(); 
//					h2.add(img);
//					h2.setCellVerticalAlignment(img, VerticalPanel.ALIGN_MIDDLE);
//					h2.setCellHorizontalAlignment(img, HorizontalPanel.ALIGN_CENTER);
				}
				else
				{
					img = new Label(" ");
					img.setWidth("18px");
//					h2.add(filler);
				}
//				h2.setWidth("18px");
				h1.add(img);
//				h1.setCellWidth(img, "100%");
				h1.setCellHeight(img, "100%");
				h1.setCellHorizontalAlignment(img, HorizontalPanel.ALIGN_LEFT);
				h1.setCellVerticalAlignment(img, VerticalPanel.ALIGN_MIDDLE);
				h1.add(resLabel);
				h1.setCellWidth(resLabel, "100%");
				h1.setCellHeight(resLabel, "100%");
				h1.setCellVerticalAlignment(resLabel, VerticalPanel.ALIGN_MIDDLE);
				h1.setStyleName("resource-entry-panel");
				if (numberOfItems == 0)
				{
					h1.addStyleName("first-resource-entry-panel");
				}
				resLabel.addMouseListener(new ResourceHoverStyler(h1));
				vec.add(h1);
//				panel.add(h1);
//				panel.setCellWidth(h1, "100%");
//				panel.setCellHorizontalAlignment(h1, HorizontalPanel.ALIGN_LEFT);
//				panel.setCellVerticalAlignment(h1, VerticalPanel.ALIGN_MIDDLE);
				numberOfItems++;
			}
		}
		
		if (numberOfItems != 0)
		{
			int scrollLimit = UIParams.getUIParams().getBrowserParamIntValue("resource_popup_scroll_limit", 5);
			if (numberOfItems > scrollLimit)
			{
				ScrollPanel sp = new ScrollPanel();
				int width = UIParams.getUIParams().getBrowserParamIntValue("resource_popup_scroll_width", 250);
				int barWidth = UIParams.getUIParams().getBrowserParamIntValue("resource_popup_scroll_bar_width", 250);
				int height = UIParams.getUIParams().getBrowserParamIntValue("resource_popup_scroll_height", 150);
				sp.setSize(width+"px", height+"px");
				VerticalPanel panel = new VerticalPanel();
				panel.setSize((width-barWidth)+"px", height+"px");
				sp.add(panel);
				outer.add(sp,DockPanel.NORTH);
				outer.setCellHorizontalAlignment(sp, HorizontalPanel.ALIGN_LEFT);
				outer.setCellVerticalAlignment(sp, VerticalPanel.ALIGN_MIDDLE);
				int size2 = vec.size();
				for (int i=0; i<size2; i++)
				{
					HorizontalPanel h = (HorizontalPanel)vec.elementAt(i);
					panel.add(h);
					panel.setCellWidth(h, "100%");
					panel.setCellHorizontalAlignment(h, HorizontalPanel.ALIGN_LEFT);
					panel.setCellVerticalAlignment(h, VerticalPanel.ALIGN_MIDDLE);
				}
			}
			else
			{
				int size2 = vec.size();
				for (int i=0; i<size2; i++)
				{
					HorizontalPanel h = (HorizontalPanel)vec.elementAt(i);
					outer.add(h,DockPanel.NORTH);
					outer.setCellWidth(h, "100%");
					outer.setCellHorizontalAlignment(h, HorizontalPanel.ALIGN_LEFT);
					outer.setCellVerticalAlignment(h, VerticalPanel.ALIGN_MIDDLE);
				}
			}
		}
		else
		{
		}
	}
	public	Image	getSharingInProgressImageUrl()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getActiveShareItemImageUrl();
	}
}
