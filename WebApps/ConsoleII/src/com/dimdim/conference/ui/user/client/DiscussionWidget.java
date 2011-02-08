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

package com.dimdim.conference.ui.user.client;

import com.dimdim.conference.ui.common.client.user.NewChatController;
import com.dimdim.conference.ui.common.client.user.NewChatUnreadMessageListener;
import com.dimdim.conference.ui.common.client.user.NewDiscussionPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.panels.client.WorkspacePanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The discussion widget is a simple combination of av panel and all to all
 * chat panel. 
 * 
 */

public class DiscussionWidget extends Composite implements	NewChatUnreadMessageListener
{
	protected	DockPanel	basePanel	=	new DockPanel();
	
//	protected	AVWindow		avWindow;
	protected	UIRosterEntry	me;
//	protected	NewDiscussionAVManager			avManager;
//	protected	VerticalPanel		avPanel;
	protected	NewDiscussionPanel	discussionPanel;
	protected	DiscussionRosterListener	roleChangeListener;
	protected	WorkspacePanel		workSpace;
//	protected   Timer toggle;
	protected static int callChatHiddenCounter = 0;
	
	public	DiscussionWidget( UIRosterEntry me, WorkspacePanel workSpace)
	{
		initWidget(basePanel);
		this.me = me;
		this.workSpace = workSpace;
//		this.discussTab = discussTab;
//		avPanel = new VerticalPanel();
//		avPanel.setStyleName("discussion-av-panel");
//		basePanel.add(avPanel,DockPanel.NORTH);
//		basePanel.setCellHorizontalAlignment(avPanel,HorizontalPanel.ALIGN_CENTER);
		
//		this.avManager = new NewDiscussionAVManager(this,me);
		
		discussionPanel = (NewDiscussionPanel)NewChatController.getController().getDiscussionPanel();
		basePanel.add(discussionPanel,DockPanel.CENTER);
		discussionPanel.setUnreadMessaeListener(this);
//		if (UIGlobals.isActivePresenter(me))
//		{
//			showAVPanel();
//			this.discussionPanel.setChatPanelSizeWithAV();
//		}
//		else
		{
			this.discussionPanel.setChatPanelSizeWithoutAV();
		}
		
//		basePanel.setCellHeight(discussionPanel,"100%");
		this.setStyleName("dm-chat-popup");
		this.addStyleName("discussion-chat-top-padding");
		this.roleChangeListener = new DiscussionRosterListener(me,this);
	}
	public	void	showAVPanel()
	{
//		this.discussionPanel.setChatPanelSizeWithAV();
//		Window.alert(this.avManager.getMovieUrl());
//		avWindow = new AVWindow("AVPlayer","av",this.avManager.getMovieUrl(),246,220);
//		this.avPanel.add(avWindow);
//		this.avPanel.setCellHorizontalAlignment(avWindow,HorizontalPanel.ALIGN_CENTER);
//		this.avPanel.setCellVerticalAlignment(avWindow,VerticalPanel.ALIGN_MIDDLE);
	}
	public	void	becameActivePresenter()
	{
//		this.avManager.setMovieUrl(ConferenceGlobals.getAVStreamId());
	}
	public	void	hideAVPanel()
	{
		/*
		this.avFrame.setVisible(false);
		this.avPanel.remove(this.avFrame);
		*/
//		if (this.avWindow != null)
//		{
//			this.avWindow.setVisible(false);
//			this.avPanel.remove(this.avWindow);
//			this.avWindow = null;
//			this.discussionPanel.setChatPanelSizeWithoutAV();
//		}
	}
	public NewDiscussionPanel getDiscussionPanel()
	{
		return discussionPanel;
	}
	public	void	resizeWidget(int width, int height)
	{
		//Window.alert(" inside chat widget width = "+width +", height = "+height);
		this.discussionPanel.setChatPanelSize(width,height);
	}
	public	UIRosterEntry	getMe()
	{
		return	this.me;
	}
//	public	CommonTabbedPage	getWorkspaceTabs()
//	{
//		return	this.workspaceTabs;
//	}
	public void hasNewUnreadMessage()
	{		
		//Window.alert("callChatHiddenCounter " + callChatHiddenCounter);
		///if (this.discussTab != null)
		//{
			//Window.alert("callChatHiddenCounter " + callChatHiddenCounter);
			//CommonTab.setCounter(callChatHiddenCounter++);
			//this.workSpace.addStyleName("close-chat-popup");			
			this.workSpace.startBlinker("chat-header-message-unread");		
			
		//}
	}
	public void seenUnreadMessage()
	{
		//callChatHiddenCounter = 1;
		//Window.alert("callChatHiddenCounter " + callChatHiddenCounter);
		if (this.workSpace != null)
		{
			this.workSpace.stopBlinker();
		}
	}
	public  void hasNewUnreadHiddenMessage()
	{
		
	}
	
}
