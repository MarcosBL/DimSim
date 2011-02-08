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

package com.dimdim.conference.ui.common.client.layout;

import java.util.Vector;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;

import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.PopoutModel;
import com.dimdim.conference.ui.model.client.PopoutPanelSet;
import com.dimdim.conference.ui.model.client.PopoutPanelProxy;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.common.client.resource.ResourceSharingPlayer;
import com.dimdim.conference.ui.common.client.resource.PresenterResourceSharingPlayer;
import com.dimdim.conference.ui.common.client.user.NewChatController;
import com.dimdim.conference.ui.common.client.user.NewDiscussionPanel;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.UIGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * As the name suggests this panel is a simple horizontal panel that contains
 * the resource sharing player and the all to all chat discussion panel.
 */

public class ResourcePlayerAndDiscussionPanel //extends Composite implements PopoutPanelSet, ClickListener
{
	/*
	public	static	final	int	DiscussionPanelWidth = 250;
	
	protected	HorizontalPanel	basePanel = new HorizontalPanel();
	
	protected	UIRosterEntry	me;
	protected	int		lastKnownWidth;
	protected	int		lastKnownHeight;
	
	protected	int		workspaceWidth;
//	protected	int		discussionWidth;
	
	protected	NewDiscussionPanel	discussionPanel;
	protected	ResourceSharingPlayer	resourcePlayer;
	
	protected	boolean	discussionPanelVisible = true;
	
	private	ResourcePlayerAndDiscussionPanel(UIRosterEntry me, int width, int height)
	{
		initWidget(this.basePanel);
		
		this.me = me;
//		this.lastKnownWidth = width-20;
//		this.lastKnownHeight = height-20;
		
		discussionPanel = (NewDiscussionPanel)NewChatController.getController().getDiscussionPanel();
		if (UIGlobals.isActivePresenter(me))
		{
			resourcePlayer = new PresenterResourceSharingPlayer(me,width,height);
		}
		else
		{
			resourcePlayer = new ResourceSharingPlayer(me,width,height);
		}
		
		this.basePanel.add(resourcePlayer);
		this.basePanel.add(discussionPanel);
		discussionPanel.addStyleName("common-onepx-border");
		
		this.resizePanel(width,height);
	}
	public	void	hideDiscussionPanel()
	{
		if (this.discussionPanelVisible)
		{
			this.discussionPanel.setVisible(false);
			this.discussionPanelVisible = false;
			setComponentSizes();
		}
	}
	public	void	showDiscussionPanel()
	{
		if (!this.discussionPanelVisible)
		{
			this.discussionPanel.setVisible(true);
			this.discussionPanelVisible = true;
			setComponentSizes();
		}
	}
	public	void	resizePanel(int width, int height)
	{
		this.lastKnownWidth = width-UIParams.getUIParams().getPublicChatWidthAllowance();
		this.lastKnownHeight = height-UIParams.getUIParams().getPublicChatHeightAllowance();
		
		if (this.lastKnownHeight <  300)
		{
			this.lastKnownHeight = 300;
		}
		if (this.lastKnownWidth < 400)
		{
			this.lastKnownWidth = 400;
		}
		setComponentSizes();
	}
	protected	void	setComponentSizes()
	{
		this.setSize(lastKnownWidth+"px",lastKnownHeight+"px");
		if (this.discussionPanelVisible)
		{
			this.workspaceWidth = this.lastKnownWidth - DiscussionPanelWidth;
		}
		else
		{
			this.workspaceWidth = this.lastKnownWidth;
		}
		discussionPanel.setChatPanelSize(DiscussionPanelWidth,lastKnownHeight);
		resourcePlayer.resizeWidget(this.workspaceWidth,lastKnownHeight);
	}
	public	int	getWorkspaceWidth()
	{
		return	workspaceWidth;
	}
	public	int	getDiscussionWidth()
	{
		return	DiscussionPanelWidth;
	}
	
	protected	boolean		inConsole = true;
	protected	boolean		inPopout = false;
	protected	boolean		consolePanelPoppedOut = false;
	
	protected	Vector	panelIds;
	
	public Vector getFeatureModelIds()
	{
		return null;
	}
	public PopoutSupportingPanel getPanel(String panelId)
	{
		if (panelId.equals(this.resourcePlayer.getPanelId()))
		{
			return	this.resourcePlayer;
		}
		else if (panelId.equals(this.discussionPanel.getPanelId()))
		{
			return	this.discussionPanel;
		}
		return null;
	}
	public Vector getPanelIds()
	{
		if (panelIds == null)
		{
			panelIds = new Vector();
			panelIds.addElement(this.resourcePlayer.getPanelId());
			panelIds.addElement(this.discussionPanel.getPanelId());
		}
		return panelIds;
	}
	public String getPanelSetId()
	{
		return PopoutModel.WORKSPACE;
	}
	public void panelSetInConsole()
	{
		this.inConsole = true;
		this.inPopout = false;
	}
	public void panelSetInPopout()
	{
		this.inConsole = false;
		this.inPopout = true;
	}
	public boolean isInConsole()
	{
		return	this.inConsole;
	}
	public boolean isInPopout()
	{
		return	this.inPopout;
	}
	public void panelSetPopedIn()
	{
		//	No action required
	}
	public void panelSetPopedOut()
	{
		//	No action required
	}
	public void	setWindowId(String windowId)
	{
		this.panelSetInPopout();
		
		this.resourcePlayer.panelInPopout();
		this.resourcePlayer.setPopoutPanelProxy(
				new PopoutPanelProxy(windowId,this.resourcePlayer,true));
		this.discussionPanel.panelInPopout();
		this.discussionPanel.setPopoutPanelProxy(
				new PopoutPanelProxy(windowId,this.discussionPanel,true));
	}
	public void popoutWindowClosed()
	{
	}
	public void onClick(Widget sender)
	{
		if (this.discussionPanelVisible)
		{
			this.hideDiscussionPanel();
		}
		else
		{
			this.showDiscussionPanel();
		}
	}
	public void popoutWindowLoaded()
	{
		//	No action required. This method is used only on console side.
	}
	*/
}

