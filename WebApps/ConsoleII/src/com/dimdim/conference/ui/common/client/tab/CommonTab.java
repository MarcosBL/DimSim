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

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Each tab has a tab content provider. All the subtabs within the tab also
 * must use the same provider. Each tab always as at least 1 sub tab and a
 * default subtab. Hence the content provider always accepts two parameters,
 * the tab and subtab names. The content provider is invoked only when the
 * subtab is made visible through mouse click.
 */

public class CommonTab extends Composite implements ClickListener
{
	protected	CommonTabGroup				tabGroup;
	protected	CommonTabContentProvider	tabContentProvider;
	protected	CommonTabContentPanel		tabContentPanel;
	
	protected	boolean		tabVisible = true;
	protected	boolean		tabContentVisible = true;
	protected	Label		label = new Label();
//	protected	RoundedPanel	roundCorneredLabel = null;
	protected	int			alignment = CommonTabGroup.LEFT;
	protected	String		name;
	protected	String		labelText;
	protected	Vector		subTabs;
	protected	String		comment;
	protected	Widget		hideControl;
	protected	String		extraBackgroundStyle = null;
	protected   Timer toggle;
	protected Image image;
	protected Image imageChange;
	boolean checkImageFirstTime;
	boolean checkMinimizedMessageAgain = false;
	
	protected	HorizontalPanel	shareControlPanel	=	null;
	protected	HorizontalPanel	subTabsPanel	=	null;
	
	protected	CommonSubTab	lastSelectedSubTab = null;
	protected HorizontalPanel actualPanel = null;
	protected static int counter = 1;
	
	public	CommonTab(CommonTabGroup tabGroup, String name,String tooltip,
				CommonTabContentProvider tabContentProvider,
				CommonTabContentPanel tabContentPanel,
				Widget hideControl, ClickListener openControlListener)
	{
		this(tabGroup,name,tooltip,name,tabContentProvider,
					tabContentPanel,hideControl,openControlListener);
	}
	
	public	CommonTab(CommonTabGroup tabGroup, String name, String tooltip,String labelText,
				CommonTabContentProvider tabContentProvider,
				CommonTabContentPanel tabContentPanel,
				Widget hideControl, ClickListener openControlListener)
	{
		if (hideControl != null)
		{
			label.setWordWrap(false);
			label.addClickListener(openControlListener);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(hideControl);
			hp.add(label);
			hp.setStyleName("dm-tab-label-image-pair");
			actualPanel = hp;
			initWidget(hp);
		}
		else
		{
			initWidget(label);
		}
		this.hideControl = hideControl;
		this.tabGroup = tabGroup;
		this.name = name;
		this.labelText = labelText;
		this.tabContentPanel = tabContentPanel;
		this.tabContentProvider = tabContentProvider;
		
		this.label.setText(labelText);
		this.label.setTitle(tooltip);
		
		this.label.addClickListener(this);
		this.setStyleName("dm-tab");
//		this.addStyleName("common-anchor");
		this.addStyleName("anchor-cursor");		
		this.addStyleName("dm-tab-unselected");
		DOM.setAttribute(this.label.getElement(),"id",this.name);
		
		this.subTabs = new Vector();
	}
	public boolean isTabVisible()
	{
		return tabVisible;
	}
	public void setTabVisible(boolean tabVisible)
	{
		this.tabVisible = tabVisible;
	}
	public boolean isTabContentVisible()
	{
		return tabContentVisible;
	}
	public HorizontalPanel getShareControlPanel()
	{
		if (shareControlPanel == null)
		{
			shareControlPanel = new	HorizontalPanel();
		}
		return shareControlPanel;
	}
	public void setTabContentVisible(boolean tabContentVisible)
	{
		this.tabContentVisible = tabContentVisible;
		if (tabContentVisible)
		{
			this.removeBackgroundStyle(this.extraBackgroundStyle);
			stopBlinker();
		}
	}
	public	String	getName()
	{
		return	name;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public Widget getHideControl()
	{
		return hideControl;
	}
	public void setHideControl(Widget hideControl)
	{
		this.hideControl = hideControl;
	}
	public int getAlignment()
	{
		return alignment;
	}
	public void setAlignment(int alignment)
	{
		this.alignment = alignment;
	}
	public	Widget	getSubtabsPanel()
	{
		if (this.subTabsPanel == null)
		{
			this.subTabsPanel = new HorizontalPanel();
			int size = this.subTabs.size();
			for (int i=0; i<size; i++)
			{
				CommonSubTab subTab = (CommonSubTab)this.subTabs.elementAt(i);
				if (subTab.isTypeComment())
				{
					subTab.addStyleName("common-text");
				}
				else
				{
					subTab.addStyleName("console-sub-tab-link");
				}
				if (i > 0)
				{
					HTML seperator = new HTML("|");
					seperator.setStyleName("console-sub-tab-seperator");
					this.subTabsPanel.add(seperator);
				}
				
				this.subTabsPanel.add(subTab);
				if (subTab.getImage() != null)
				{
					this.subTabsPanel.add(subTab.getImage());
					this.subTabsPanel.setCellHorizontalAlignment(subTab,HorizontalPanel.ALIGN_RIGHT);
					this.subTabsPanel.setCellHorizontalAlignment(subTab.getImage(),HorizontalPanel.ALIGN_RIGHT);
				}
			}
			if (this.comment != null)
			{
				Label label = new Label(comment);
				label.setStyleName("common-text");
				this.subTabsPanel.add(label);
			}
			if (this.shareControlPanel != null)
			{
				this.subTabsPanel.add(this.shareControlPanel);
				this.subTabsPanel.setCellHorizontalAlignment(shareControlPanel,HorizontalPanel.ALIGN_RIGHT);
			}
		}
		return	this.subTabsPanel;
	}
	public	Label	getLabel()
	{
		return	label;
	}
	public	Widget	getTabContent()
	{
		Widget content = null;
		if (this.lastSelectedSubTab == null)
		{
			selectDefaultSubTab();
		}
		if (this.lastSelectedSubTab != null)
		{
			content = this.lastSelectedSubTab.getTabContent();
		}
		return	content;
	}
	public	void	selectDefaultSubTab()
	{
		if (this.subTabs.size() > 0)
		{
			this.setSubTabSelected((CommonSubTab)(this.subTabs.elementAt(0)));
		}
	}
	public	void	addClickListener(ClickListener clickListener)
	{
		this.label.addClickListener(clickListener);
	}
	public	CommonSubTab	getLastSelectedSubTab()
	{
		return	this.lastSelectedSubTab;
	}
	public	Vector	getSubTabs()
	{
		return	subTabs;
	}
	public CommonTabContentPanel getTabContentPanel()
	{
		return tabContentPanel;
	}
	public CommonTabContentProvider getTabContentProvider()
	{
		return tabContentProvider;
	}
	public	CommonSubTab	addSubTab(String name,String tooltip)
	{
		return	this.addSubTab(name,name,name,tooltip);
	}
	public	int	getContentWidth()
	{
		return	this.tabGroup.getContentWidth();
	}
	public	int	getContentHeight()
	{
		return	this.tabGroup.getContentHeight();
	}
	public	CommonSubTab	addSubTab(String name, String unselectedText, String selectedText,String tooltip)
	{
		CommonSubTab subTab = new CommonSubTab(name, unselectedText,selectedText,tooltip,this);
		this.subTabs.addElement(subTab);
		if (this.subTabsPanel != null)
		{
			this.subTabsPanel.add(subTab);
		}
		return	subTab;
	}
//	public	
	public	void	selectLastSubTab()
	{
		if (this.lastSelectedSubTab != null)
		{
			this.setSubTabSelected(this.lastSelectedSubTab);
		}
		else
		{
			this.selectDefaultSubTab();
		}
	}
	public	void	onClick(Widget w)
	{
		if (w == this.label)
		{
			this.tabGroup.setTabSelected(this);
		}
	}
	/**
	 * Set the styles on the tab accordingly.
	 */
	public	void	setTabSelected(boolean selected)
	{
		if (selected)
		{
			this.removeStyleName("dm-tab-unselected");
			this.addStyleName("dm-tab-selected");
			this.selectLastSubTab();
			if (this.tabContentProvider != null)
			{
				this.tabContentProvider.tabSelected(this.name,this.lastSelectedSubTab.name);
			}
		}
		else
		{
			this.removeStyleName("dm-tab-selected");
			this.addStyleName("dm-tab-unselected");
		}
	}
	public	void	onSubTabClicked(CommonSubTab subTab)
	{
		//	For the subtab to be selected the tab must already be on view.
		//	No other action should be required, but future requirement may
		//	come in.
	}
	/**
	 * If the tab already has a previous active subtab, set that as selected,
	 * otherwise set the default subtab as selected. This will be required only
	 * first time.
	 */
	protected	void	setSubTabSelected(CommonSubTab subTab)
	{
		if (subTab == null)
		{
			return;
		}
		
		if (this.lastSelectedSubTab != null)
		{
			this.lastSelectedSubTab.setSelected(false);
		}
		
		{
			//	First time tab selection.
			this.lastSelectedSubTab = subTab;
			this.lastSelectedSubTab.setSelected(true);
		}
	}
	public void setContentSize(int contentWidth, int contentHeight)
	{
		if (this.lastSelectedSubTab != null)
		{
			this.tabContentProvider.resizeTabContent(this.name,
					this.lastSelectedSubTab.name,contentWidth,contentHeight);
		}
	}
	public	void	addBackgroundStyle(String styleName)
	{
		if (!this.tabContentVisible)
		{
			this.removeStyleName("dm-tab-selected");
			this.addStyleName(styleName);
			this.extraBackgroundStyle = styleName;
		}
	}
	public	void	removeBackgroundStyle(String styleName)
	{
		if (this.extraBackgroundStyle != null)
		{
			this.removeStyleName(extraBackgroundStyle);
			this.addStyleName("dm-tab-selected");
			this.extraBackgroundStyle = null;
		}
	}
	public	void	startBlinker(final String styleName)
	{	
		if(counter > 1)
		{
//			Window.alert("value of counter : " + counter);
			actualPanel.remove(image);
			actualPanel.remove(imageChange);
		}
		image = new Image("images/new-incoming.png");
		image.setStyleName("blink-chat-popup");
		imageChange = new Image("images/no-incoming.png");
		imageChange.setStyleName("blink-chat-popup");
		
		checkImageFirstTime = true;
		
//		Window.alert("Check Image first time : " + checkImageFirstTime + " checkMinimized : " + checkMinimizedMessageAgain);
	
		if (this.toggle == null && !this.tabContentVisible)
		{	
			this.toggle = new Timer() 
			{
				boolean unread = true;				
				
				public void run() 
				{					
//					checkMinimizedMessageAgain = true;					
					if(unread)
					{
//						addBackgroundStyle(styleName);
						if(checkImageFirstTime != true)
						{
							actualPanel.remove(imageChange);
							actualPanel.add(image);
						}
						else
						{
							actualPanel.add(image);
						}
						unread = false;
					}
					else
					{						
//						removeBackgroundStyle(styleName);
						checkImageFirstTime = false;
						actualPanel.remove(image);
						actualPanel.add(imageChange);
						unread = true;
					}										
//					Window.alert("checkImageFirstTime: " + checkImageFirstTime);					
					/*if(tabContentVisible)
					{
						actualPanel.remove(image);
						actualPanel.remove(imageChange);
					}*/					
				}				
			};			
		    this.toggle.scheduleRepeating(1000);	  		    
		}		
	}
	public	void	stopBlinker()
	{
		actualPanel.remove(image);
		actualPanel.remove(imageChange);		
		if (this.toggle != null)
		{
			this.toggle.cancel();
			this.toggle = null;
		}
	}

	public HorizontalPanel getActualPanel() {
		return actualPanel;
	}

	public Image getImage() {
		return image;
	}

	public Image getImageChange() {
		return imageChange;
	}

	public static void setCounter(int counter) {
		CommonTab.counter = counter;
	}
	
	
}
