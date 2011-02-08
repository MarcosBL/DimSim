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

package com.dimdim.conference.ui.workspacepopout.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PopoutModel;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.model.client.ResourceModel;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.panels.client.ChatWidget;
import com.dimdim.conference.ui.panels.client.LayoutController;
import com.dimdim.conference.ui.panels.client.ResourcePlayerAndPublicChatPanelSet;
import com.dimdim.conference.ui.panels.client.WorkspacePanel;
import com.dimdim.conference.ui.popout.client.PopoutWindowContent;
import com.dimdim.conference.ui.sharing.client.CollaborationAreaManager;
import com.dimdim.conference.ui.sharing.client.ResourceSharingPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WorkspacePopoutContentPage implements PopoutWindowContent
{
	
	protected	Label	popinLink;
	protected	Image	popinImage;
	private	Label	backgroundMiddleFiller1;
	
	protected	UIRosterEntry	me;
	protected	WorkspacePanel	workspace = null;
	protected	WorkspacePopout	layout;
	
	protected	ChatWidget chatWidget = null;
	protected	CollaborationAreaManager	collaborationAreaManager;
	protected	ResourceSharingPanel		resourceSharingPanel;
	protected	ResourcePlayerAndPublicChatPanelSet	panelSet1;
	
	protected	boolean	pageShown = false;
	
	public WorkspacePopoutContentPage(WorkspacePopout layout)
	{
		this.layout = layout;
	}
	public Vector getRequiredFeatureIds()
	{
		Vector	v = new Vector();
		v.addElement(RosterModel.ModelFeatureId);
		v.addElement(ResourceModel.ModelFeatureId);
//		v.addElement(WhiteboardModel.ModelFeatureId);
		return v;
	}
	public void initializePanelData(String panelId, String panelData)
	{
//		Window.alert("Initializing panel data: id:"+panelId+", data:"+panelData);
		if (panelId.equals(this.chatWidget.getPopoutSupportingChatPanel().getPanelId()))
		{
			PopoutSupportingPanel psp = this.chatWidget.getPopoutSupportingChatPanel();
			if (psp != null)
			{
				psp.readPanelData(panelData);
			}
			else
			{
//				Window.alert("No panel by id:"+panelId);
			}
		}
		else if (panelId.equals(this.resourceSharingPanel.getPanelId()))
		{
			this.resourceSharingPanel.readPanelData(panelData);
		}
	}
	public void preparePopoutWindowContent()
	{
		this.me = ClientModel.getClientModel().getRosterModel().getCurrentUser();
//		Window.alert("WorkspacePopoutContentPage:WorkspacePopoutContentPage:1");
		CollaborationAreaManager.initManager(me, 600, 400, "res_showing");
//		Window.alert("WorkspacePopoutContentPage:WorkspacePopoutContentPage:2");
		this.collaborationAreaManager = CollaborationAreaManager.getManager();
//		Window.alert("WorkspacePopoutContentPage:WorkspacePopoutContentPage:3");
		this.resourceSharingPanel = this.collaborationAreaManager.getResourceSharingPanel();
//		Window.alert("WorkspacePopoutContentPage:WorkspacePopoutContentPage:4");
//		Window.alert("preparePopoutWindowContent:1");
		layout.addPanelToLayout("collab_area", this.resourceSharingPanel);
//		Window.alert("preparePopoutWindowContent:2");
		workspace = new WorkspacePanel(me, this.resourceSharingPanel, layout, null, collaborationAreaManager);
		workspace.setInPopout(true);	
		this.chatWidget = workspace.getChatWidget();
		
		//Window.alert("chat widget creation..");
//		chatWidget = new ChatWidget(layout, workspace);
//		panelSet1 = new ResourcePlayerAndPublicChatPanelSet(chatWidget,this.resourceSharingPanel,this.popinLink);
//		if(!ConferenceGlobals.publicChatEnabled)
//		{
//			Window.alert("public chat disabled so removing the widget");
//			chatWidget.removeChatWidget();			
//		}

		panelSet1 = workspace.getPanelSet1();
		
		panelSet1.setWindowId(PopoutModel.WORKSPACE);
		//Window.alert("inside preparing popout window content workspace.getFullScreenLink() = "+workspace.getFullScreenLink());
		workspace.getFullScreenLink().setText(UIStrings.getPopinWorkspaceLinkText());
		workspace.getFullScreenLink().setTitle(ConferenceGlobals.getTooltip("popin_link"));
		
		//Window.alert("preparePopoutWindowContent:3");
//		this.initWidget(workspace);
	}
	public void resizePopoutWindowContent(int width, int height)
	{
		UIParams uiParams = UIParams.getUIParams();
		if (backgroundMiddleFiller1 == null)
		{
			backgroundMiddleFiller1 = new Label(" ");
			RootPanel.get("background_middle_1").add(backgroundMiddleFiller1);
		}
		int backgroundHeaderHeight = uiParams.getBrowserParamIntValue("background_header_height", 115);
		int backgroundFooterHeight = uiParams.getBrowserParamIntValue("background_footer_height", 115);
		int backgroundMiddleAllowance = uiParams.getBrowserParamIntValue("background_middle_allowance", 0);
		int fillerHeight = height-(backgroundHeaderHeight+backgroundFooterHeight+backgroundMiddleAllowance);
		backgroundMiddleFiller1.setHeight(fillerHeight+"px");
		if (!pageShown)
		{
			pageShown = true;
			this.layout.showPage();
			if (this.workspace != null)
			{
				this.workspace.containerResized(width, height, true);
			}
		}
		else
		{
			if (this.workspace != null)
			{
				this.workspace.containerResized(width, height);
			}
		}
	}
	public	void	divHidden(String divId)
	{
		if (this.workspace != null)
		{
			this.workspace.divHidden(divId);
		}
	}
	public	void	divShown(String divId)
	{
		if (this.workspace != null)
		{
			this.workspace.divShown(divId);
		}
	}
}

