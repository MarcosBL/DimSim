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

import com.dimdim.conference.ui.model.client.PopoutModel;
import com.dimdim.conference.ui.model.client.PopoutPanelSet;
import com.dimdim.conference.ui.model.client.PopoutPanelProxy;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.common.client.user.DiscussionWidget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * As the name suggests this panel is a simple horizontal panel that contains
 * the resource sharing player and the all to all chat discussion panel.
 */

public class WorkspacePanelSet //implements PopoutPanelSet
{
	/*
	protected	DiscussionWidget			discussionWidget;
	protected	WorkspaceTabPanelContent	workspaceTabPanelContent;
	
	public	WorkspacePanelSet(DiscussionWidget discussionWidget,
			WorkspaceTabPanelContent workspaceTabPanelContent)
	{
		this.discussionWidget = discussionWidget;
		this.workspaceTabPanelContent = workspaceTabPanelContent;
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
		if (panelId.equals(this.workspaceTabPanelContent.getResourceSharingPlayer().getPanelId()))
		{
			return	this.workspaceTabPanelContent.getResourceSharingPlayer();
		}
		else if (panelId.equals(this.discussionWidget.getDiscussionPanel().getPanelId()))
		{
			return	this.discussionWidget.getDiscussionPanel();
		}
		return null;
	}
	public Vector getPanelIds()
	{
		if (panelIds == null)
		{
			panelIds = new Vector();
			panelIds.addElement(this.workspaceTabPanelContent.getResourceSharingPlayer().getPanelId());
			panelIds.addElement(this.discussionWidget.getDiscussionPanel().getPanelId());
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
		
		this.workspaceTabPanelContent.getResourceSharingPlayer().panelInPopout();
		this.workspaceTabPanelContent.getResourceSharingPlayer().setPopoutPanelProxy(
				new PopoutPanelProxy(windowId,this.workspaceTabPanelContent.getResourceSharingPlayer(),true));
		this.discussionWidget.getDiscussionPanel().panelInPopout();
		this.discussionWidget.getDiscussionPanel().setPopoutPanelProxy(
				new PopoutPanelProxy(windowId,this.discussionWidget.getDiscussionPanel(),true));
	}
	public void popoutWindowClosed()
	{
	}
	public void popoutWindowLoaded()
	{
		//	No action required. This method is used only on console side.
	}
	*/
}

