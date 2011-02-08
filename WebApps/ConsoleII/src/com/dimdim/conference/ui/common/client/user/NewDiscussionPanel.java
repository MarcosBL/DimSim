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

import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;

//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.WindowResizeListener;
//import com.dimdim.conference.ui.common.client.UIConstants;
//import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.data.UIParams;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The difference between the discussion panel and chat panel is that the
 * discussion panel must expand and collapse with the window, it does not
 * 
 */

public class NewDiscussionPanel extends NewChatPanel
{
	protected	boolean		avPanelVisible = false;
	
	protected	int	lastKnownPanelWidth = 250;
	protected	int	lastKnownPanelHeight = -1;
	
	public	NewDiscussionPanel(UIRosterEntry me)
	{
		super(me,null);
		lastKnownPanelHeight = ConferenceGlobals.getContentHeight();
	}
	/**
	 * The height calculation is explicit because otherwise the tab content
	 * panel does not expand to full height in firefox.
	 */
	public	void	setChatPanelSize(int width, int height)
	{
		this.lastKnownPanelWidth = width;
		this.lastKnownPanelHeight = height;//-(2);
		
		if (this.avPanelVisible)
		{
			//	This code path is defunct as the av panel is no longer
			//	integrated wih the chat panel.
			this.setChatPanelSizeWithoutAV();
		}
		else
		{
			this.setChatPanelSizeWithoutAV();
		}
	}
//	public	void	setChatPanelSizeWithAV()
//	{
//		this.avPanelVisible = true;
//		int chatPanelHeight = lastKnownPanelHeight - (UIGlobals.getAVBroadcasterHeight()+
//				UIConstants.TAB_BORDER_ALLOWANCE);
//		this.setHeight((chatPanelHeight)+"px");
//		this.scrollPanel.setHeight((chatPanelHeight-
//				UIParams.getUIParams().getChatTextAreaHeight())+"px");
//	}
	public	void	setChatPanelSizeWithoutAV()
	{
		this.avPanelVisible = false;
		int chatPanelHeight = lastKnownPanelHeight/*-(UIConstants.TAB_BORDER_ALLOWANCE+8)*/;
		//Window.alert("chatPanelHeight = "+chatPanelHeight);
		this.setHeight((chatPanelHeight)+"px");
		if(chatPanelHeight- UIParams.getUIParams().getChatTextAreaHeight() > 0)
		{
			this.scrollPanel.setHeight((chatPanelHeight-
				(UIParams.getUIParams().getChatTextAreaHeight()+
				 UIParams.getUIParams().getBrowserParamIntValue("public_chat_height_allowance", 0)))+"px");
		}
//				ConferenceGlobals.getPrivateChatTextAreaHeight())+"px");
		this.outer.setHeight(chatPanelHeight+"px");
		//this.outer.setBorderWidth(1);
	}
	public	String	getPanelId()
	{
		return	"panel.discussion";
	}
}
