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
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.panels.client;

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.user.client.DiscussionWidget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatWidget  implements ClickListener
{
	
	protected	LayoutController	workspaceLayout = null;
//	protected	HorizontalPanel		basePanel = new HorizontalPanel();
	protected	VerticalPanel		verticalPanel = new VerticalPanel();
	protected	HorizontalPanel		chatPanel = new HorizontalPanel();
	
	Label chatLink = null;
	Label chatLabel = null;
	
	DiscussionWidget chatWidget = null;
	WorkspacePanel workSpace = null;
	
	public ChatWidget(LayoutController workspaceLayout, WorkspacePanel workSpace)
	{
		this.workspaceLayout = workspaceLayout;
		this.workSpace = workSpace;
//		initWidget(basePanel);
		
		fillContent(workSpace);
	}
	private void fillContent(WorkspacePanel workSpace)
	{
		chatWidget = new DiscussionWidget(ClientModel.getClientModel().getRosterModel().getCurrentUser(), workSpace);
		chatWidget.setSize("100%", "100%");
		chatPanel.add(chatWidget);
		chatPanel.setCellHorizontalAlignment(chatWidget, HorizontalPanel.ALIGN_CENTER);
		FocusPanel otherLinksPanel = new FocusPanel();
		HorizontalPanel h1 = new HorizontalPanel();
		
		chatLink = new Label(ConferenceGlobals.getDisplayString("workspace.hide.chat.label","Public Chat"));
		chatLink.setStyleName("common-text");
		chatLink.addStyleName("common-bold-text");
		
		h1.setWidth("100%");
		h1.add(chatLink);
		h1.setCellWidth(chatLink, "100%");
		h1.setCellHorizontalAlignment(chatLink, HorizontalPanel.ALIGN_CENTER);
		Image img = getPublicChatCloseImage();
		h1.add(img);
		Label l = new Label(" ");
		l.setWidth("10px");
		h1.add(l);
		
		otherLinksPanel.add(h1);
		otherLinksPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.hide.chat.title","Click to hide Chat"));
		
		verticalPanel.add(chatWidget);
		
		otherLinksPanel.setWidth("100%");
		otherLinksPanel.addStyleName("anchor-cursor");
		otherLinksPanel.addClickListener(this);
		
		workspaceLayout.addPanelToLayout("chat_pod_header", otherLinksPanel);
		workspaceLayout.addPanelToLayout("chat_pod_content", verticalPanel);
		
		addChatWidget();
	}
	public void setChatVisible(boolean visible)
	{
		this.chatWidget.setVisible(visible);
	}
	public void addChatWidget()
	{
		this.workspaceLayout.showPanel("main_chat_td");
//		this.workspaceLayout.showPanel("main_chat_seperator");
		this.workspaceLayout.hidePanel("show_chat_wrapper");
		workSpace.stopBlinker();
	}
	public void removeChatWidget()
	{
		this.workspaceLayout.hidePanel("main_chat_td");
//		this.workspaceLayout.hidePanel("main_chat_seperator");
		this.workspaceLayout.showPanel("show_chat_wrapper");
	}
	public void onClick(Widget sender)
	{
		removeChatWidget();
	}
	public void panelResized(int width, int height)
	{
//		if((height-15) > 0)
//		{
			chatPanel.setSize(width+"px", height+"px");
//		}
//		if((height-25) > 0)
//		{
			chatWidget.resizeWidget(width, height);
//		}
	}
	public	PopoutSupportingPanel	getPopoutSupportingChatPanel()
	{
		return	this.chatWidget.getDiscussionPanel();
	}
	private	Image	getPublicChatCloseImage()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getPublicChatCloseImage();
	}
}
