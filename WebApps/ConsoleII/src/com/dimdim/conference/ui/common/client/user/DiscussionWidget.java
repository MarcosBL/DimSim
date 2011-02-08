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

package com.dimdim.conference.ui.common.client.user;
/*
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.common.client.tab.CommonTab;
*/
/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The discussion widget is a simple combination of av panel and all to all
 * chat panel. 
 * 
 */

public class DiscussionWidget //extends Composite implements	NewChatUnreadMessageListener
{
	/*
	protected	DockPanel	basePanel	=	new DockPanel();
	
	protected	UIRosterEntry	me;
	protected	VerticalPanel		avPanel;
	protected	NewDiscussionPanel	discussionPanel;
	protected	CommonTab		discussTab;
//	protected   Timer toggle;
	protected static int callChatHiddenCounter = 0;
	
	public	DiscussionWidget( UIRosterEntry me, CommonTab discussTab)
	{
		initWidget(basePanel);
		this.me = me;
		this.discussTab = discussTab;
		avPanel = new VerticalPanel();
		avPanel.setStyleName("discussion-av-panel");
		basePanel.add(avPanel,DockPanel.NORTH);
		basePanel.setCellHorizontalAlignment(avPanel,HorizontalPanel.ALIGN_CENTER);
		
		discussionPanel = (NewDiscussionPanel)NewChatController.getController().getDiscussionPanel();
		basePanel.add(discussionPanel,DockPanel.CENTER);
		discussionPanel.setUnreadMessaeListener(this);
		
		{
			this.discussionPanel.setChatPanelSizeWithoutAV();
		}
		
		this.setStyleName("dm-chat-popup");
		this.addStyleName("discussion-chat-top-padding");
	}
	public NewDiscussionPanel getDiscussionPanel()
	{
		return discussionPanel;
	}
	public	void	resizeWidget(int width, int height)
	{
		this.discussionPanel.setChatPanelSize(width,height);
	}
	public	UIRosterEntry	getMe()
	{
		return	this.me;
	}
	public void hasNewUnreadMessage()
	{
		if (this.discussTab != null)
		{
//			Window.alert("callChatHiddenCounter " + callChatHiddenCounter);
			CommonTab.setCounter(callChatHiddenCounter++);
//			this.discussTab.addStyleName("close-chat-popup");			
			this.discussTab.startBlinker("chat-header-message-unread");		
			
		}
	}
	public void seenUnreadMessage()
	{
		callChatHiddenCounter = 1;
		if (this.discussTab != null)
		{
			this.discussTab.stopBlinker();
		}
	}
	public  void hasNewUnreadHiddenMessage()
	{
		
	}
	*/
}
