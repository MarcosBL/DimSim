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

package com.dimdim.conference.ui.layout.client.widget;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIRecordingControlEvent;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.RecordingModelListener;
import com.dimdim.conference.ui.layout.client.main.NewLayout;
import com.dimdim.conference.ui.sharing.client.ResourceSharingPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.dimdim.conference.ui.panels.client.ChatWidget;
import com.dimdim.conference.ui.panels.client.ResourcePlayerAndPublicChatPanelSet;

public class WorkspacePanel extends Composite //implements ClickListener
{
/*
	protected	UIRosterEntry		currentUser;
	//protected	ResourceRoster		resourceRoster;
	
	protected	HorizontalPanel		worspaceContent = new HorizontalPanel();
	//HorizontalPanel resPlayerWrapper = new HorizontalPanel();
	
	//boolean isChatDisplayed = true;
	FocusPanel showChatPanel = null;
	FocusPanel fullScreenLinkPanel = null;
	FocusPanel recordLinkPanel = null;
	//HorizontalPanel resourcePlayerPanel = null;
	Label chatLink = null;
	ChatWidget chatWidget = null;
	//HorizontalPanel topLinksPanel = null;
	boolean isChatDisplayed = true;
	
	int prevWidth = -1;
	int prevHeight = -1;
	int	prevContainerWidth = -1;
	int	prevContainerHeight = -1;
	public final int chatWidgetWidth = 270;

	private ResourceSharingPanel resourcePlayer = null;

	private NewLayout consoleLayout;
	private	ResourcePlayerAndPublicChatPanelSet	panelSet1;
	private	WorkspacePopoutClickListener	fullScreenClickListener;
//	private	FullScreenClickListener	fullScreenClickListener;
	
//	private	ConsoleWidgets	consoleWidgets;
	
	private WorkspacePanel(UIRosterEntry currentUser, ResourceSharingPanel resourcePlayer, NewLayout consoleLayout)
	{
		this.currentUser = currentUser;
		//this.resourceRoster = resourceRoster;
		this.resourcePlayer  = resourcePlayer;
		this.consoleLayout = consoleLayout;
//		this.fullScreenClickListener = new FullScreenClickListener(this);
		initWidget(worspaceContent);
		//worspaceContent.setBorderWidth(1);
		//worspaceContent.setSize("100%", "100%");
		fillContent();
	}
	
	private void fillContent(){
		
		/*HorizontalPanel h2 = new HorizontalPanel();
		h2.setStyleName("console-middle-left-panel-float");
		h2.add(new HTML("&nbsp;"));
		h2.setWidth("10px");
		worspaceContent.add(h2); /
		
		//DisclosurePanel collapsibleChat = new DisclosurePanel();
		
		//DropDownHeaderPanel dropDownHeaderPanel = new DropDownHeaderPanel(UIGlobals.getDiscussTabLabel(), 
		//			null, collapsibleChat);
		
		//collapsibleChat.setHeader(dropDownHeaderPanel);
		//collapsibleChat.setStyleName("tk-DropDownPanel");
		//collapsibleChat.add(chatWidget);
		//collapsibleChat.setSize("100%", "100%");
		
		//RoundedPanel roundedcollabArea = collabArea();
		collabArea();
		//roundedcollabArea.setSize("100%", "100%");
		//worspaceContent.add(roundedcollabArea);
		//worspaceContent.setCellHeight(roundedcollabArea, "100%");
		//worspaceContent.setCellWidth(roundedcollabArea, "100%");
		//worspaceContent.setCellHorizontalAlignment(roundedcollabArea, HorizontalPanel.ALIGN_CENTER);
		
		chatWidget = new ChatWidget(consoleLayout);
//		panelSet1 = new ResourcePlayerAndPublicChatPanelSet(chatWidget);
//		fullScreenClickListener = new WorkspacePopoutClickListener(panelSet1);
//		this.fullScreenLinkPanel.addClickListener(fullScreenClickListener);
//		this.fullScreenLinkPanel.addClickListener();
//		this.consoleWidgets = new ConsoleWidgets(this.chatWidget);
		
		//worspaceContent.add(chatWidget);
		//worspaceContent.setCellHorizontalAlignment(chatWidget, HorizontalPanel.ALIGN_RIGHT);
	}

	

	private void collabArea() {
		//VerticalPanel collabArea = new VerticalPanel();
		
		//collabArea.setStyleName("console-collab-area");
		//RoundedPanel roundedcollabArea = new RoundedPanel(collabArea);
		
		//topLinksPanel = new HorizontalPanel();
		//HorizontalPanel sharingLinkPanel = new HorizontalPanel();
		//HorizontalPanel otherLinksPanel = new HorizontalPanel();
		
		//topLinksPanel.setBorderWidth(1);
		//topLinksPanel.setStyleName("console-collab-header");
		
		//Label link = new Label("Uniqueness of Dimdim");
		//ImageNTextWidget sharing = new ImageNTextWidget("Now Sharing:", "appShared.gif", false);
		
		final Label link1 = new Label("Record");
	//	link1.addClickListener(this);

		recordLinkPanel = new FocusPanel();
		recordLinkPanel.add(link1);
		recordLinkPanel.setWidth("100%");
		//fp.setStyleName("user-list-hover");
		recordLinkPanel.addClickListener(this);
	//	consoleLayout.addWidgetToID("Record", fp1);
		
		
		Label link2 = new Label("Full Screen");
		//link2.addClickListener(this);
		fullScreenLinkPanel = new FocusPanel();
		fullScreenLinkPanel.add(link2);
		fullScreenLinkPanel.setTitle("Click to make it Full Screen");
		fullScreenLinkPanel.setWidth("100%");
		//fp.setStyleName("user-list-hover");
		fullScreenLinkPanel.addClickListener(this);
//		fullScreenLinkPanel.addClickListener(this.consoleWidgets.getWorkspacePopoutClickListener());
		
		chatLink = new Label("Show Chat");
	//	chatLink.addClickListener(this);
		showChatPanel = new FocusPanel();
		showChatPanel.add(chatLink);
		showChatPanel.setWidth("100%");
		//fp.setStyleName("user-list-hover");
		showChatPanel.addClickListener(this);
		
		
		chatLink.addStyleName("inactive_link");
		
		recordLinkPanel.addClickListener(new ClickListener()
		{
			public void onClick(Widget sender)
			{
				ClientModel.getClientModel().getRecordingModel().toggleRecording();
				if(ClientModel.getClientModel().getRecordingModel().isRecordingActive())
				{
					sender.removeStyleName("inactive_link");
					sender.addStyleName("active_link");
				}else{
					sender.removeStyleName("active_link");
					sender.addStyleName("inactive_link");
					
				}
			}
		});
		
		//sharing.getLabel().removeStyleName("console-collab-header-links");
		//sharing.getLabel().setStyleName("common-text");
		//sharing.getLabel().addStyleName("common-bold-text");
		//sharingLinkPanel.add(sharing);
		//sharingLinkPanel.add(link);
		//sharingLinkPanel.setStyleName("console-collab-header");
		
		//otherLinksPanel.add(link1);
		//otherLinksPanel.add(link2);
		//otherLinksPanel.add(chatLink);
		//otherLinksPanel.setBorderWidth(1);
		//otherLinksPanel.setStyleName("console-collab-header");
		
		//topLinksPanel.add(sharingLinkPanel);
		//topLinksPanel.add(otherLinksPanel);
		//topLinksPanel.setCellWidth(sharingLinkPanel, "60%");
		//topLinksPanel.setCellWidth(otherLinksPanel, "40%");
		Label nowSharing = new Label(UIGlobals.getNowSharingText());
		nowSharing.setStyleName("common-text");
		nowSharing.addStyleName("common-bold-text");
		
		//collabArea.add(topLinksPanel);
		consoleLayout.addWidgetToID("now_sharing", nowSharing);
		//consoleLayout.addWidgetToID("res_showing", link);
		
		if(UIGlobals.isPresenter(currentUser))
		{
			consoleLayout.setIDVisibility("record", true);
			consoleLayout.setIDVisibility("record_wrapper", true);
			consoleLayout.addWidgetToID("record", recordLinkPanel);
		}
		consoleLayout.addWidgetToID("full_screen", fullScreenLinkPanel);
		consoleLayout.addWidgetToID("show_chat", showChatPanel);
		//consoleLayout.addWidgetToID("controllers", otherLinksPanel);
		//collabArea.add(resPlayerWrapper);
		//resPlayerWrapper.setStyleName("console-collab-area");
		
		//resourcePlayerPanel = getResourcePlayerPanel();
		consoleLayout.addWidgetToID("collab_area", resourcePlayer);
		//collabArea.add(resourcePlayerPanel);
		//resPlayerWrapper.add(resourcePlayerPanel);
		//resPlayerWrapper.setCellHorizontalAlignment(resourcePlayerPanel, HorizontalPanel.ALIGN_CENTER);
		
		RecordingModelListener rml = new RecordingModelListener()
		{
			public void onRecordingdStarted(UIRecordingControlEvent event)
			{
				link1.addStyleName("recording-on");
			}
			public void onRecordingStopped(UIRecordingControlEvent event)
			{
				link1.removeStyleName("recording-on");
			}
		};
		ClientModel.getClientModel().getRecordingModel().addListener(rml);
		
		//addChatWidget();
		
		//collabArea.setCellWidth(topLinksPanel, "100%");
		
		//sharingLinkPanel.setCellHorizontalAlignment(sharing, HorizontalPanel.ALIGN_CENTER);
		//sharingLinkPanel.setCellHorizontalAlignment(link, HorizontalPanel.ALIGN_CENTER);
		
		//topLinksPanel.setCellHorizontalAlignment(link1, HorizontalPanel.ALIGN_RIGHT);
		//topLinksPanel.setCellHorizontalAlignment(link2, HorizontalPanel.ALIGN_RIGHT);
		//topLinksPanel.setCellHorizontalAlignment(chatLink, HorizontalPanel.ALIGN_RIGHT);
		
		//topLinksPanel.setCellHorizontalAlignment(otherLinksPanel, HorizontalPanel.ALIGN_RIGHT);
		//topLinksPanel.setCellHorizontalAlignment(sharingLinkPanel, HorizontalPanel.ALIGN_RIGHT);
		
		//return roundedcollabArea;
	}

//	private HorizontalPanel getResourcePlayerPanel() {
		//resourcePlayerPanel = new HorizontalPanel();
		//RoundedPanel roundedPlayer = new RoundedPanel(resourcePlayerPanel);
		//resourcePlayerPanel.add(resourcePlayer);
		//resourcePlayerPanel.setBorderWidth(1);
		//resourcePlayerPanel.addStyleName("console-resourcePlayer");
		//resourcePlayerPanel.setCellWidth(resourcePlayer, "100%");
		//resourcePlayerPanel.setCellWidth(resourcePlayer, "100%");
		//resourcePlayerPanel.add(new Label(" Place holder..."));
		//resourcePlayerPanel.setCellHorizontalAlignment(resourcePlayer, HorizontalPanel.ALIGN_CENTER);
		//return resourcePlayerPanel;
//		return null;
//	}

	public void onClick(Widget sender)
	{
		if(showChatPanel == sender )
		{
			if(null != chatWidget)
			{
				chatWidget.addChatWidget();
			}
		}
		else if (fullScreenLinkPanel == sender)
		{
//			Window.alert("clickd on some link "+sender.getTitle());
		}
		else if (recordLinkPanel == sender)
		{
			
		}
	}

	public void setBorder(){
		worspaceContent.setBorderWidth(1);
	}
	
	public void showChatLink()
	{
//		Window.alert("WorkspacePanel:showChatLink::1");
		if(null != chatLink)
		{
			isChatDisplayed = false;
			chatLink.setVisible(true);
	//		consoleLayout.setIDVisibility("show_chat_icon", true);
			consoleLayout.setIDVisibility("show_chat_wrapper", true);
			//topLinksPanel.setCellHorizontalAlignment(chatLink, HorizontalPanel.ALIGN_RIGHT);
			
			//resourcePlayerPanel.removeStyleName("console-resourcePlayer-width");
			//resourcePlayerPanel.addStyleName("console-resourcePlayer-fullwidth");
			//Window.alert("inside show chat link prevWidth = "+prevWidth +"prevHeight ="+prevHeight );
//			panelResized((prevWidth + chatWidgetWidth), prevHeight);
			this.containerResized();
		}
	}
	
	public	void	divHidden(String divId)
	{
		if (divId.equals("show_chat_wrapper"))
		{
			hideChatLink();
		}
	}
	public	void	divShown(String divId)
	{
		if (divId.equals("show_chat_wrapper"))
		{
			showChatLink();
		}
	}
	public void hideChatLink()
	{
//		Window.alert("WorkspacePanel:hideChatLink::1");
		if (null != chatLink)
		{
			isChatDisplayed = true;
			chatLink.setVisible(false);
		//	consoleLayout.setIDVisibility("show_chat_icon", false);
			consoleLayout.setIDVisibility("show_chat_wrapper", false);
			//Window.alert("inside hideChatLink prevWidth = "+prevWidth +"prevHeight ="+prevHeight );
//			panelResized((prevWidth - chatWidgetWidth), prevHeight);
			this.containerResized();
		}
	}
	
	
	public UIRosterEntry getCurrentUser() {
		return currentUser;
	}
	public	void	containerResized()
	{
		this.containerResized(this.prevContainerWidth, this.prevContainerHeight);
	}
	public void containerResized(int containerWidth, int containerHeight)
	{
		this.prevContainerWidth = containerWidth;
		this.prevContainerHeight = containerHeight;
		int	panelWidth = containerWidth;
		int	panelHeight = containerHeight;
//		if (!this.fullScreenClickListener.isModeFullScreen())
//		{
			//	Normal view. width correction is the left column width and
			//	height correction is top panel height and margins.
			panelWidth = panelWidth - 247;
			panelHeight = panelHeight - 80;
//		}
//		else
//		{
//			//	Only height correction for the margins.
//			panelHeight = panelHeight - 20;
//		}
		if(isChatDisplayed)
		{
			panelWidth = panelWidth - chatWidgetWidth;
		}
		
//		DebugPanel.getDebugPanel().addDebugMessage("new middle panel window width:"+panelWidth+", height:"+panelHeight +", workSpace.isChatDisplayed ="+workSpace.isChatDisplayed);
		panelResized(panelWidth, panelHeight);
	}
	public void panelResized(int width, int height)
	{
		if(width < 0 || height < 0)
		{
			return;
		}
		this.prevHeight = height;
		this.prevWidth = width;
		//DebugPanel.getDebugPanel().addDebugMessage("in workspace panel panelResized window width:"+width+", height:"+height);
		//Window.alert("width = "+width);
		//Window.alert("height = "+height);
		//Window.alert("inside show chat link prevWidth = "+prevWidth +"prevHeight ="+prevHeight );
		int overAllCollabWidthDeduction = 70;
//		int resourcePlayerWidthDedcution = 30;
		
		if( (prevWidth-overAllCollabWidthDeduction) > 0)
		{
			//topLinksPanel.setSize((prevWidth - overAllCollabWidthDeduction)+"px", "20px");
		}
		//resPlayerWrapper.setSize( (prevWidth - 45)+"px", (prevHeight-30)+"px");
		
		//if((prevWidth-(overAllCollabWidthDeduction+resourcePlayerWidthDedcution)) > 0 && (prevHeight-60) > 0)
		if((prevWidth-(overAllCollabWidthDeduction)) > 0 && (prevHeight-60) > 0)
		{
			//resourcePlayer.resizePanel((prevWidth - (overAllCollabWidthDeduction+resourcePlayerWidthDedcution)), (prevHeight-60));
			resourcePlayer.resizePanel((prevWidth - overAllCollabWidthDeduction), (prevHeight-60));
		}
		/*if( (prevWidth-overAllCollabWidthDeduction) > 0 && (prevHeight-55) > 0)
		{
			resourcePlayerPanel.setSize((prevWidth-overAllCollabWidthDeduction)+"px", (prevHeight-55)+"px");
		} --/
		
		if(null != chatWidget)
		{
			if((prevHeight-37) > 0)
			{
				chatWidget.panelResized(chatWidgetWidth, (prevHeight-37));
			}
		}
	}
	*/
}
