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

import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ClickListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Each tab group has only 1 tab content. Though each tab may use a different
 * tab content provider. The tab group provides the complete tab layout so that
 * it can be easily embedded in a page.
 */

public class CommonTabGroup 
{
	public	static	final	int		LEFT	=	1;
	public	static	final	int		RIGHT	=	2;
	
	protected	int		orientation;
	protected	String	fillerWidth;
	
	protected	CommonTabbedPage		tabbedPage;
	protected	CommonTabContentPanel	tabContentPanel;
	
//	private	DockPanel		fullTabsPanel;
	private	HorizontalPanel	tabsPanel;
	private	DockPanel		tabsOrientationPanel;
	private	HTML			tabsFiller = null;
//	private	DockPanel		fullSubTabsPanel;
	private	HorizontalPanel	subTabsPanel;
	
	private	VerticalPanel	contentPanel;
	
	protected	String 		name;
	protected	Vector		tabs;
	protected	CommonTab	lastSelectedTab;
	
	protected	int		contentWidth;
	protected	int		contentHeight;
	protected	Widget	hideControl;
	protected	ClickListener	openControlListener;
	
	public	CommonTabGroup(CommonTabbedPage tabbedPage,
				String name, int orientation, HorizontalPanel tabsPanel,
				HorizontalPanel subTabsPanel, VerticalPanel contentPanel,
				int contentWidth, int contentHeight)
	{
//		Window.alert("Creating CommonTabGroup:"+1);
		this.tabbedPage = tabbedPage;
		this.name = name;
		this.orientation = orientation;
		this.tabsPanel = tabsPanel;
		this.subTabsPanel = subTabsPanel;
		this.contentPanel = contentPanel;
		this.tabsOrientationPanel = new DockPanel();
		this.tabsPanel.add(this.tabsOrientationPanel);
		this.tabsPanel.setCellWidth(this.tabsOrientationPanel,"100%");
		this.tabs = new Vector();
		this.contentWidth = contentWidth;
		this.contentHeight = contentHeight;
		
//		Window.alert("Creating CommonTabGroup:"+2);
		this.tabContentPanel = new CommonTabContentPanel(name);
		this.tabContentPanel.setStyleName("tab-content-panel");
//		this.tabContentPanel.setSize("100%","100%");
		this.contentPanel.add(this.tabContentPanel);
//		this.contentPanel.setCellHeight(this.tabContentPanel,"100%");
//		this.contentPanel.setCellWidth(this.tabContentPanel,"100%");
//		Window.alert("Creating CommonTabGroup:"+3);
	}
	public int getMininumWidth()
	{
		return this.tabbedPage.getMinimumWidth();
	}
	public void setMininumWidth(int minimumWidth)
	{
		this.tabbedPage.setMinimumWidth(minimumWidth);
	}
	public int getMinimumHeight()
	{
		return this.tabbedPage.getMinimumWidth();
	}
	public void setMinimumHeight(int minimumHeight)
	{
		this.tabbedPage.setMinimumHeight(minimumHeight);
	}
	public Widget getHideControl()
	{
		return hideControl;
	}
	public void setHideControl(Widget hideControl)
	{
		this.hideControl = hideControl;
	}
	public ClickListener getOpenControlListener()
	{
		return openControlListener;
	}
	public void setOpenControlListener(ClickListener openControlListener)
	{
		this.openControlListener = openControlListener;
	}
	public	CommonTabContentPanel	getTabContentPanel()
	{
		return	this.tabContentPanel;
	}
	public	Widget	getTabContent()
	{
		Widget content = null;
		if (this.lastSelectedTab == null)
		{
			selectDefaultTab();
		}
		if (this.lastSelectedTab != null)
		{
			content = this.lastSelectedTab.getTabContent();
		}
		return	content;
	}
	public	void	selectDefaultTab()
	{
		int	size = this.tabs.size();
		if ( size > 0)
		{
			for (int i=0; i<size; i++)
			{
				CommonTab tab = (CommonTab)this.tabs.elementAt(i);
				if (tab.isTabVisible())
				{
					this.setTabSelected(tab);
					break;
				}
			}
		}
	}
	public	void	showDefaultTab()
	{
		if (this.tabs.size() > 0)
		{
			this.lastSelectedTab = null;
			this.selectDefaultTab();
//			this.setTabSelected((CommonTab)this.tabs.elementAt(0));
		}
	}
	public	CommonTab	addTab(String name, String tooltip, CommonTabContentProvider tabContentProvider)
	{
		return	this.addTab(name, name, tooltip,CommonTabGroup.LEFT, tabContentProvider);
	}
	public	CommonTab	addTab(String name, String labelText, String tooltip, CommonTabContentProvider tabContentProvider)
	{
		return	this.addTab(name, labelText, tooltip, CommonTabGroup.LEFT, tabContentProvider);
	}
	private	CommonTab	addTab(String name, String labelText, String tooltip, int alignment, CommonTabContentProvider tabContentProvider)
	{
		CommonTab tab = new CommonTab(this,name,tooltip,labelText,tabContentProvider,tabContentPanel,
				this.hideControl,this.openControlListener);
		tab.setAlignment(alignment);
		this.tabs.addElement(tab);
//		RoundedPanel roundCorneredTab = new RoundedPanel(tab,RoundedPanel.TOP);
		if (alignment == CommonTabGroup.RIGHT)
		{
			this.tabsOrientationPanel.add(tab,DockPanel.EAST);
		}
		else if (this.orientation == CommonTabGroup.LEFT)
		{
			this.tabsOrientationPanel.add(tab,DockPanel.WEST);
		}
		else
		{
			this.tabsOrientationPanel.add(tab,DockPanel.EAST);
		}
//		this.tabsOrientationPanel.setCellVerticalAlignment(roundCorneredTab,VerticalPanel.ALIGN_MIDDLE);
		
		return	tab;
	}
	public	void	setTabSelected(CommonTab tab)
	{
		boolean	tabChanged = false;
		if (this.tabsFiller == null)
		{
			this.tabsFiller = new HTML("&nbsp;");
			this.tabsOrientationPanel.add(this.tabsFiller,DockPanel.CENTER);
			this.tabsOrientationPanel.setCellWidth(this.tabsFiller,"100%");
		}
		if (lastSelectedTab != null)
		{
			if (lastSelectedTab != tab)
			{
				this.lastSelectedTab.setTabSelected(false);
				this.subTabsPanel.remove(this.lastSelectedTab.getSubtabsPanel());
				this.lastSelectedTab = tab;
				tabChanged = true;
			}
		}
		else
		{
			lastSelectedTab = tab;
			tabChanged = true;
		}
		
		if (tabChanged)
		{
			Widget w = this.lastSelectedTab.getSubtabsPanel();
			this.subTabsPanel.add(w);
			this.subTabsPanel.setCellVerticalAlignment(w,VerticalPanel.ALIGN_MIDDLE);
			if (this.orientation == CommonTabGroup.RIGHT)
			{
				this.subTabsPanel.setCellHorizontalAlignment(w,HorizontalPanel.ALIGN_RIGHT);
			}
			this.lastSelectedTab.setTabSelected(true);
			
			//	Change the tab content.
		}
	}
	public int getContentHeight()
	{
		return contentHeight;
	}
	public int getContentWidth()
	{
		return contentWidth;
	}
	public void setContentSize(int contentWidth, int contentHeight)
	{
		this.contentWidth = contentWidth;
		this.contentHeight = contentHeight;
		int size = this.tabs.size();
		for (int i=0; i<size; i++)
		{
			CommonTab tab = (CommonTab)(tabs.elementAt(i));
			tab.setContentSize(contentWidth,contentHeight);
		}
		/*
		if (this.lastSelectedTab != null)
		{
			this.lastSelectedTab.setContentSize(contentWidth,contentHeight);
		}
		*/
	}
	public	void	setTabContentVisible(boolean b)
	{
		int size = this.tabs.size();
		for (int i=0; i<size; i++)
		{
			CommonTab tab = (CommonTab)(tabs.elementAt(i));
			tab.setTabContentVisible(b);
		}
	}
}
