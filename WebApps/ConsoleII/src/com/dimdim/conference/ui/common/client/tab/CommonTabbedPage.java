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

package com.dimdim.conference.ui.common.client.tab;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.util.RunningClockWidget;
import com.dimdim.conference.ui.common.client.resource.ResourceSharingPlayer;

import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PopoutModel;
import com.dimdim.conference.ui.model.client.EventsJsonProxy;
import com.dimdim.conference.ui.model.client.EventsJsonHandler;
import com.dimdim.conference.ui.model.client.PopoutWindowProxy;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.WindowResizeListener;
import org.gwtwidgets.client.ui.*;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Purpose of this class is to provide a combined tab pane content management.
 * A page can comprise either a single or two tab groups as left and right.
 * This is a specific layout and a very specific feature. The right tab group
 * must provide a specific width. This is essentially to provide functionality
 * to provide a combined tab pane area for two distinct sets of tabs. Only the
 * right tab is allowed to have the hide control. When the content associated
 * with the right tab group is hidden the left tab group content is required
 * to expand left and fill the whole tab pane area.
 */

public class CommonTabbedPage	extends	Composite implements ClickListener
{
	protected	String	name;
	
	protected	String	leftGroupName;
	protected	String	rightGroupName;
	protected	String	rightGroupWidth;
	protected	int		rightGroupWidthInt;
	
	protected	int		widthAllowance;
	protected	int		heightAllowance;
	
	protected	CommonTabGroup	leftTabGroup;
	protected	CommonTabGroup	rightTabGroup;
	protected	Image			rightGroupHideControl;
	
	protected	VerticalPanel	tabbedPage;
	protected	DockPanel		subTabsAndContentPanel;
	protected	RoundedPanel	roundedPanel;
	
	protected	DockPanel		tabsPanel;
	protected	HorizontalPanel	leftGroupTabs;
	protected	HorizontalPanel	rightGroupTabs;
	
	protected	DockPanel		subTabsPanel;
	protected	HorizontalPanel	leftGroupSubTabs;
	protected	HorizontalPanel	rightGroupSubTabs;
	
	protected	DockPanel		contentPanel;
	protected	VerticalPanel	leftGroupContentPanel;
	protected	VerticalPanel	rightGroupContentPanel;
	
	protected	HorizontalPanel	poppedOutWorkspaceContent;
	protected	int				poppedOutWorkspaceContentWidth;
	
	protected	RunningClockWidget	meetingClock = new RunningClockWidget();
	protected	ResourceSharingPlayer	resourceSharingPlayer;
	
//	protected	PopoutModel			popoutModel = ClientModel.getClientModel().getPopoutModel();
//	protected	WorkspacePopoutPanelSet	workspacePopoutPanelSet;
//	protected	PopoutWindowProxy	workspacePopoutProxy;// = new PopoutWindowProxy(PopoutModel.WORKSPACE);
//	protected	EventsJsonProxy		workspaceEventsProxy;
//	protected	WorkspacePopoutClickListener	workspacePopoutClickListener;
//	protected	boolean				workspacePopedOut = false;
	
	/**
	 * Content Width and Height management. The height for both groups is
	 * same at present.
	 */
	protected	int		minimumWidth;
	protected	int		minimumHeight;
	
	protected	int		lastKnownWidth;
	protected	int		lastKnownHeight;
	
	protected	int		leftGroupContentWidth;
	protected	int		leftGroupTabWidth;
	protected	int		leftGroupSubTabWidth;
	protected	int		leftGroupContentHeight;
	protected	int		rightGroupContentWidth;
	protected	int		rightGroupTabWidth;
	protected	int		rightGroupSubTabWidth;
	protected	int		rightGroupContentHeight;
	protected	boolean	rightGroupContentVisible = true;
	
	public	CommonTabbedPage(String name, String leftGroupName,
				String rightGroupName, int rightGroupWidthInt)
	{
		this.name = name;
		this.leftGroupName = leftGroupName;
		this.rightGroupName = rightGroupName;
		this.rightGroupWidth = rightGroupWidthInt+"px";
		this.rightGroupWidthInt = rightGroupWidthInt;
//		this.workspacePopoutPanelSet = new WorkspacePopoutPanelSet(this);
//		this.workspacePopoutProxy = new PopoutWindowProxy(this.workspacePopoutPanelSet);
//		this.workspaceEventsProxy = new EventsJsonProxy(null,this.workspacePopoutProxy);
//		this.workspacePopoutClickListener = new WorkspacePopoutClickListener(this,null);
		
		this.tabbedPage = new VerticalPanel();
		initWidget(tabbedPage);
		//	Create the required panels inclusions and sizes etc, and pass
		//	on the panels to the tab groups to use.
		if (this.rightGroupName != null && this.rightGroupWidth != null)
		{
			//	Start with the right group content, if used, as visible.
			this.rightGroupContentVisible = true;
		}
		int	windowWidth = ConferenceGlobals.getContentWidth();
		int windowHeight = ConferenceGlobals.getContentHeight();
		setupContentSize(windowWidth,windowHeight);
		createPanels();
		
	}
	public	void	drawPage()
	{
		int	windowWidth = ConferenceGlobals.getContentWidth();
		int windowHeight = ConferenceGlobals.getContentHeight();
		setupContentSize(windowWidth,windowHeight);
		setPanelSizes();
		
		//Window.alert("hiding the tab = "+ConferenceGlobals.publicChatEnabled);
		if(!ConferenceGlobals.publicChatEnabled){
			this.hideRightGroup();
		}
	}
	protected	void	setupContentSize(int windowWidth, int windowHeight)
	{
		int w = windowWidth;
		int h = windowHeight;
//	    Window.enableScrolling(false);
		if (windowWidth < this.minimumWidth)
		{
			w = this.minimumWidth;
		}
		else
		{
//		    Window.enableScrolling(true);
		}
		if (windowHeight < this.minimumHeight)
		{
			h = this.minimumHeight;
		}
		else
		{
//		    Window.enableScrolling(true);
		}
		int tabStylesWidthAllowance = 20+2;//
		int	tabStylesHeightAllowance = 24+2+20+20;//24+2+20+27+5;
		
		
		if (this.rightGroupContentVisible)
		{
			this.leftGroupContentWidth = w - (rightGroupWidthInt+
				this.getAbsoluteLeft()+tabStylesWidthAllowance);
		}
		else
		{
			this.leftGroupContentWidth = w - (this.getAbsoluteLeft()+tabStylesWidthAllowance);
		}
		this.rightGroupContentWidth = rightGroupWidthInt;
		this.poppedOutWorkspaceContentWidth = w - (this.getAbsoluteLeft()+tabStylesWidthAllowance);
		
		this.rightGroupTabWidth = rightGroupWidthInt;
		this.leftGroupTabWidth = w - (rightGroupTabWidth+this.getAbsoluteLeft()+tabStylesWidthAllowance+2);
		
		this.rightGroupSubTabWidth = rightGroupWidthInt+(20);
		this.leftGroupSubTabWidth = w - (rightGroupTabWidth+this.getAbsoluteLeft()+tabStylesWidthAllowance+2+20);
		
		this.leftGroupContentHeight = h - (this.getAbsoluteTop()+tabStylesHeightAllowance);
		this.rightGroupContentHeight = h - (this.getAbsoluteTop()+tabStylesHeightAllowance);
	}
	protected	void	createPanels()
	{
		this.tabsPanel = new DockPanel();
		this.tabsPanel.setStyleName("dm-tab-bar");
		this.subTabsPanel = new DockPanel();
		this.subTabsPanel.setStyleName("dm-sub-tab-bar");
		this.contentPanel = new DockPanel();
		
		leftGroupTabs = new HorizontalPanel();
		tabsPanel.add(leftGroupTabs,DockPanel.WEST);
		tabsPanel.setCellVerticalAlignment(leftGroupTabs,VerticalPanel.ALIGN_BOTTOM);
		tabsPanel.setCellHorizontalAlignment(leftGroupTabs,HorizontalPanel.ALIGN_LEFT);
		
		leftGroupSubTabs = new HorizontalPanel();
		subTabsPanel.add(leftGroupSubTabs,DockPanel.WEST);
		subTabsPanel.setCellHorizontalAlignment(leftGroupSubTabs,HorizontalPanel.ALIGN_LEFT);
		subTabsPanel.setCellVerticalAlignment(leftGroupSubTabs,VerticalPanel.ALIGN_MIDDLE);
		
		leftGroupContentPanel = new VerticalPanel();
		leftGroupContentPanel.setStyleName("left-group-content-panel");
		contentPanel.add(leftGroupContentPanel,DockPanel.WEST);
		contentPanel.setCellHorizontalAlignment(leftGroupContentPanel,HorizontalPanel.ALIGN_CENTER);
		contentPanel.setCellVerticalAlignment(leftGroupContentPanel,VerticalPanel.ALIGN_TOP);
		
		poppedOutWorkspaceContent = new HorizontalPanel();
		poppedOutWorkspaceContent.add(new Label("."));
		
		leftTabGroup = new CommonTabGroup(this,name,CommonTabGroup.LEFT,
				leftGroupTabs,leftGroupSubTabs,leftGroupContentPanel,
				this.leftGroupContentWidth, this.leftGroupContentHeight);
		
		if (this.rightGroupName != null && this.rightGroupWidth != null)
		{
			this.rightGroupContentVisible = true;
			rightGroupTabs = new HorizontalPanel();
			rightGroupTabs.setWidth(rightGroupWidth);
			
			tabsPanel.add(rightGroupTabs,DockPanel.EAST);
			tabsPanel.setCellVerticalAlignment(rightGroupTabs,VerticalPanel.ALIGN_BOTTOM);
			tabsPanel.setCellHorizontalAlignment(rightGroupTabs,HorizontalPanel.ALIGN_RIGHT);
			
			rightGroupTabs.add(meetingClock);
			rightGroupTabs.setCellVerticalAlignment(meetingClock,VerticalPanel.ALIGN_BOTTOM);
			rightGroupTabs.setCellHorizontalAlignment(meetingClock,HorizontalPanel.ALIGN_RIGHT);
			
			rightGroupSubTabs = new HorizontalPanel();
			rightGroupSubTabs.setWidth(rightGroupWidth);
			subTabsPanel.add(rightGroupSubTabs,DockPanel.EAST);
			subTabsPanel.setCellHorizontalAlignment(rightGroupSubTabs,HorizontalPanel.ALIGN_RIGHT);
			subTabsPanel.setCellVerticalAlignment(rightGroupSubTabs,VerticalPanel.ALIGN_MIDDLE);
			
			rightGroupContentPanel = new VerticalPanel();
			rightGroupContentPanel.setWidth(rightGroupWidth);
			contentPanel.add(rightGroupContentPanel,DockPanel.EAST);
			contentPanel.setCellHorizontalAlignment(rightGroupContentPanel,HorizontalPanel.ALIGN_LEFT);
			contentPanel.setCellVerticalAlignment(rightGroupContentPanel,VerticalPanel.ALIGN_TOP);
			
			rightTabGroup = new CommonTabGroup(this,name,CommonTabGroup.RIGHT,
					rightGroupTabs,rightGroupSubTabs,rightGroupContentPanel,
					this.rightGroupContentWidth, this.rightGroupContentHeight);
			
			this.rightGroupHideControl = new Image("images/opentriangle.gif");//,15,15);
			this.rightGroupHideControl.addClickListener(this);
			this.rightGroupHideControl.setStyleName("hide-discuss-panel-button");
			rightTabGroup.setHideControl(this.rightGroupHideControl);
			rightTabGroup.setOpenControlListener(this);
		}
		else
		{
			//	Just to make sure that the subtabs panel expands horizontally.
			rightGroupSubTabs = new HorizontalPanel();
			rightGroupSubTabs.add(new HTML("&nbsp;"));
			subTabsPanel.add(rightGroupSubTabs,DockPanel.EAST);
			subTabsPanel.setCellHorizontalAlignment(rightGroupSubTabs,HorizontalPanel.ALIGN_RIGHT);
			subTabsPanel.setCellVerticalAlignment(rightGroupSubTabs,VerticalPanel.ALIGN_MIDDLE);
		}
		subTabsAndContentPanel = new DockPanel();
		subTabsAndContentPanel.setStyleName("dm-tab-content");
		subTabsAndContentPanel.add(subTabsPanel,DockPanel.NORTH);
		subTabsAndContentPanel.add(poppedOutWorkspaceContent,DockPanel.NORTH);
		this.poppedOutWorkspaceContent.setVisible(false);
		subTabsAndContentPanel.add(contentPanel,DockPanel.CENTER);
		roundedPanel = new RoundedPanel(subTabsAndContentPanel);
		
		tabbedPage.add(tabsPanel);
		tabbedPage.add(roundedPanel);
	}
	protected	void	setPanelSizes()
	{
		subTabsPanel.setCellHeight(leftGroupSubTabs,"100%");
		
		if (this.rightGroupName != null && this.rightGroupWidth != null)
		{
			subTabsPanel.setCellHeight(rightGroupSubTabs,"100%");
		}
		else
		{
			subTabsPanel.setCellHeight(rightGroupSubTabs,"100%");
		}
		subTabsPanel.setCellWidth(leftGroupSubTabs,"100%");
		contentPanel.setCellWidth(leftGroupContentPanel,"100%");
		subTabsAndContentPanel.setCellWidth(subTabsPanel,"100%");
		subTabsAndContentPanel.setCellWidth(contentPanel,"100%");
		subTabsAndContentPanel.setCellHeight(contentPanel,"100%");
	}
//	public ResourceSharingPlayer getResourceSharingPlayer()
//	{
//		return	(ResourceSharingPlayer)this.getLeftTabGroup().getTabContent();
//	}
	public int getMinimumWidth()
	{
		return minimumWidth;
	}
	public void setMinimumWidth(int minimumWidth)
	{
		this.minimumWidth = minimumWidth;
	}
	public int getMinimumHeight()
	{
		return minimumHeight;
	}
	public void setMinimumHeight(int minimumHeight)
	{
		this.minimumHeight = minimumHeight;
	}
	public	CommonTabGroup	getLeftTabGroup()
	{
		return	leftTabGroup;
	}
	public	CommonTabGroup	getRightTabGroup()
	{
		return	rightTabGroup;
	}
	public void onWindowResized(int width, int height)
	{
//		Window.alert("Width:"+width+", height:"+height);
		this.lastKnownWidth = width;
		this.lastKnownHeight = height;
		this.setupContentSize(width,height);
		
		this.resizeContentPanels();
		if (UIGlobals.amInLobby())
		{
			this.hideRightGroup();
		}
	}
	protected	void	resizeContentPanels()
	{
		this.leftTabGroup.setContentSize(this.leftGroupContentWidth,this.leftGroupContentHeight);
		if (this.rightTabGroup != null && this.rightGroupContentVisible)
		{
			this.rightTabGroup.setContentSize(this.rightGroupContentWidth,this.rightGroupContentHeight);
		}
		this.leftGroupTabs.setWidth(this.leftGroupTabWidth+"px");
		this.rightGroupTabs.setWidth(this.rightGroupTabWidth+"px");
		
		this.leftGroupSubTabs.setWidth(this.leftGroupSubTabWidth+"px");
		this.rightGroupSubTabs.setWidth(this.rightGroupSubTabWidth+"px");
		
		this.poppedOutWorkspaceContent.setSize(this.poppedOutWorkspaceContentWidth+"px",
				this.leftGroupContentHeight+"px");
	}
	public	void	showDefaultTabs()
	{
		this.leftTabGroup.selectDefaultTab();
	}
	public	void	hideRightGroup()
	{
		this.rightGroupContentPanel.setVisible(false);
		this.rightGroupContentVisible = false;
		this.setupContentSize(lastKnownWidth,lastKnownHeight);
		
		this.resizeContentPanels();
		this.rightGroupHideControl.setUrl("images/triangle.gif");
		this.rightTabGroup.setTabContentVisible(false);
	}
	public	void	showRightGroup()
	{
		this.rightGroupContentVisible = true;
		this.setupContentSize(lastKnownWidth,lastKnownHeight);
		this.resizeContentPanels();
		
		this.rightGroupContentPanel.setVisible(true);
		this.rightGroupHideControl.setUrl("images/opentriangle.gif");
		this.rightTabGroup.setTabContentVisible(true);
	}
	public	void	onClick(Widget w)
	{
		//Window.alert("inside clicked..ConferenceGlobals.publicChatEnabled = "+ConferenceGlobals.publicChatEnabled);
		if (UIGlobals.amInLobby())
		{
			//	do nothing
			return;
		}
		if (this.rightGroupContentVisible)
		{
			hideRightGroup();
		}
		else if(ConferenceGlobals.publicChatEnabled)
		{
			showRightGroup();
		}
	}
//	private	native	void	popoutWorkspace(String url) /*-{
//		$wnd.popoutWorkspace(url);
//	}-*/;
//	private	native	void	popinWorkspace() /*-{
//		$wnd.closeWorkspacePopout();
//	}-*/;
	public ResourceSharingPlayer getResourceSharingPlayer()
	{
		return resourceSharingPlayer;
	}
	public void setResourceSharingPlayer(ResourceSharingPlayer resourceSharingPlayer)
	{
		this.resourceSharingPlayer = resourceSharingPlayer;
	}
}
