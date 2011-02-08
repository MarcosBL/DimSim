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

import	java.util.HashMap;
import java.util.Iterator;

import com.dimdim.conference.ui.common.client.util.DimdimPopupsLayoutManager;
import com.dimdim.conference.ui.json.client.UIChatEntry;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.RosterModelAdapter;
import com.dimdim.conference.ui.model.client.ChatListener;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class NewChatController	implements	ChatListener
{
	protected	static	NewChatController	controller;
	protected   String lastUserJoined;
	protected   String lastUserLeft;	
	protected   String lastUserArrived;
	public	static	NewChatController	getController()
	{
		if (NewChatController.controller == null)
		{
			NewChatController.controller = new NewChatController();
		}
		return	NewChatController.controller;
	}
	
	protected	HashMap			chats;
	protected	UIRosterEntry	me;
	protected	RosterModel		rosterModel;
	protected	NewDiscussionPanel		allToAllChat;
	
	protected	NewChatController()
	{
		this.chats = new HashMap();
		this.me = ClientModel.getClientModel().getRosterModel().getCurrentUser();
		this.rosterModel = ClientModel.getClientModel().getRosterModel();
		ClientModel.getClientModel().getChatManagerModel().addChatListener(this);
		RosterModelAdapter rml = new RosterModelAdapter()
		{
			public	void	onChatPermissionsChanged(UIRosterEntry user)
			{
				chatPermissionChanged(user);
			}
			public	void	onUserLeft(UIRosterEntry user)
			{
				userLeft(user);
			}
			public	void	onUserJoined(UIRosterEntry user)
			{
				//Window.alert("====User Has Joined---");
				userJoined(user);
			}
			public	void	onUserArrived(UIRosterEntry user)
			{
//				Window.alert("====User is waiting in the Lobby---");
				userArrived(user);
			}
			
		};
		this.rosterModel.addListener(rml);
	}
	
	public	void	userArrived(UIRosterEntry user)
	{
		// Add a message to Discuss Panel that User has joined
		
		NewChatPanel panel = this.getDiscussionPanel();
	
		String arrivedUser = user.getDisplayName();
		String arrivedUserId = user.getUserId();
		
		String senderId = this.rosterModel.getCurrentActivePresenter().getUserId();
		String senderName = this.rosterModel.getCurrentActivePresenter().getDisplayName();
				
		
		if((arrivedUserId != senderId ) && (arrivedUserId != lastUserArrived) && (senderName != "Not Available"))
		{
		//	panel.receiveChatMessage("", sender,joinedUser  + " has joined the conference");
			panel.receiveWelcomeMessage(arrivedUser  + " "+ConferenceGlobals.getDisplayString("user_waiting_message"," is waiting in the Waiting Area."));
			lastUserArrived = arrivedUserId;
		}
	}
	
	public	void	userJoined(UIRosterEntry user)
	{
		// Add a message to Discuss Panel that User has joined
		
		NewChatPanel panel = this.getDiscussionPanel();
	
		String joinedUserName = user.getDisplayName();
		String joinedUserId = user.getUserId();
		
		String senderId = this.rosterModel.getCurrentActivePresenter().getUserId();
		String senderName = this.rosterModel.getCurrentActivePresenter().getDisplayName();
		
		//Window.alert("senderId = "+senderId+":senderName:"+senderName);
		//Window.alert("User Joined:joinedUserName :"+joinedUserName+":joinedUser Status:"+user.getJoinedStatus());
		//Window.alert("lastUserJoined = "+lastUserJoined);
		if((joinedUserId != senderId ) && (joinedUserId != lastUserJoined ) && (senderName != "Not Available") && (joinedUserId != me.getUserId()) && (!user.getJoinedStatus()))
		{
		//	panel.receiveChatMessage("", sender,joinedUser  + " has joined the conference");
	 	    String s = ConferenceGlobals.getDisplayString("user_joined_message","has joined the dimdim web meeting.");
	 	    //Window.alert("adding "+s);
			panel.receiveWelcomeMessage(joinedUserName  + " "+s);
			lastUserJoined = joinedUserId;
		}
		else if((joinedUserId == senderId) && lastUserJoined == null && (joinedUserId != lastUserJoined))
		{
	 	    String s = ConferenceGlobals.getDisplayString("user_welcome_message","Welcome to the Dimdim Web Meeting.");
	 	    //Window.alert("adding welcom message...");
			panel.receiveWelcomeMessage(s);
			lastUserJoined = joinedUserId;
		}
	}
	
	public	void	userLeft(UIRosterEntry user)
	{
		NewChatPopup popup = (NewChatPopup)this.chats.get(user.getUserId());
		if (popup != null)
		{
			popup.userLeft();
		}
		this.chats.remove(user.getUserId());
		
		NewChatPanel panel = this.getDiscussionPanel();
		
		// Add a message to Discuss Panel that User is Left
		
		String leftUserName = user.getDisplayName();
		String leftUserId = user.getUserId();
		
		String senderId = this.rosterModel.getCurrentActivePresenter().getUserId();
		String senderName = this.rosterModel.getCurrentActivePresenter().getDisplayName();
		
		if((leftUserId != senderId ) && (leftUserId != lastUserLeft ))
		{
	 	    String s = ConferenceGlobals.getDisplayString("user_left_message","has left the dimdim web meeting.");
			panel.receiveWelcomeMessage(leftUserName  + " "+s);
		}
			
		lastUserLeft = leftUserId;
		
		
	}
	private	void	chatPermissionChanged(UIRosterEntry other)
	{
		boolean chatOn = other.isChatOn();
		if (other.getUserId().equals(this.me.getUserId()))
		{
			Iterator iter = this.chats.values().iterator();
			while (iter.hasNext())
			{
				NewChatPanel chatPanel = ((NewChatPopup)iter.next()).getChatPanel();
				if (chatOn)
				{
					chatPanel.currentUserChatPermissionGranted();
				}
				else
				{
					chatPanel.currentUserChatPermissionRevoked();
				}
			}
		}
		else
		{
			NewChatPopup popup = (NewChatPopup)this.chats.get(other.getUserId());
			if (popup != null)
			{
				NewChatPanel chatPanel = popup.getChatPanel();
				if (chatOn)
				{
					chatPanel.otherUserChatPermissionGranted();
				}
				else
				{
					chatPanel.otherUserChatPermissionRevoked();
				}
			}
		}
	}
	public	void	onChatMessage(UIChatEntry chatEntry)
	{
		String senderId = chatEntry.getSenderId();
		if (senderId != null && senderId.length() > 0)
		{
			if (chatEntry.isMessagePrivate())
			{
				NewChatPopup chatPopup = (NewChatPopup)this.chats.get(senderId);
				if (chatPopup == null)
				{
					//	First message from another participant. Try to
					//	construct a panel and show. It may fail if the current
					//	user is already in three previous chats.
					UIRosterEntry other = this.rosterModel.findRosterEntry(senderId);
					if (other != null)
					{
						chatPopup = this.getChatPopup(other);
						if (chatPopup != null)
						{
							chatPopup.getChatPanel().receiveChatMessage(other.getUserId(),
									other.getDisplayName(),chatEntry.getMessageText());
							this.showChatPopupIfPossible(chatPopup);
						}
					}
				}
				else
				{
					//	This means that the panel is already constructed and is
					//	listening on the chat messages. No need to do anything. If the
					//	chat panel is constructed and closed by user, there is no way
					//	of giving an indication of an unread message. May be the status
					//	bar could be used.
					chatPopup.getChatPanel().onChatMessage(chatEntry);
					this.showChatPopupIfPossible(chatPopup);
				}
			}
			else
			{
				NewChatPanel panel = this.getDiscussionPanel();
				panel.receiveChatMessage(chatEntry.getSenderId(),
						chatEntry.getSenderName(),chatEntry.getMessageText());
				/*
				if (this.allToAllChat == null)
				{
					NewChatPanel panel = this.getDiscussionPanel();
					panel.receiveChatMessage(chatEntry.getSenderId(),
							chatEntry.getSenderName(),chatEntry.getMessageText());
				}
				else
				{
					NewChatPanel panel = this.getDiscussionPanel();
					panel.receiveChatMessage(chatEntry.getSenderId(),
							chatEntry.getSenderName(),chatEntry.getMessageText());
				}
				*/
			}
		}
	}
	public	NewDiscussionPanel	getDiscussionPanel()
	{
		if (this.allToAllChat == null)
		{
			this.allToAllChat = new NewDiscussionPanel(this.me);
		}
		return	this.allToAllChat;
	}
	/**
	 * This method creates the popup if one is is not already available. Hence
	 * it always returns a valid object.
	 * 
	 * @param other
	 * @return
	 */
	private	NewChatPopup	getChatPopup(UIRosterEntry other)
	{
		//	First check if it is already active. If not then construct if
		//	possible.
		
		NewChatPopup popup = (NewChatPopup)this.chats.get(other.getUserId());
		if (popup == null)
		{
			NewChatPanel panel = new NewChatPanel(this.me,other);
			popup = new NewChatPopup(panel, -1);
			
			this.chats.put(other.getUserId(),popup);
		}
		popup.refreshName();
		return	popup;
	}
	private	void	showChatPopupIfPossible(NewChatPopup chatPopup)
	{
		//	Now see if it can be shown.
		if (chatPopup.getIndex() == -1)
		{
			//	Means its either new or being reactivated.
			int	index = this.getFreeChatPopupIndex();
			if (index != -1)
			{
				chatPopup.setIndex(index);
			}
			else
			{
				//	Get the oldest chat window, close it and show the new one.
				NewChatPopup leastActiveChatPopup = this.getLeastActiveChat();
				this.closeChat(leastActiveChatPopup);
				index = this.getFreeChatPopupIndex();
				if (index != -1)
				{
					chatPopup.setIndex(index);
				}
			}
			//initial chat window should have default location
			chatPopup.setPosition();
			chatPopup.onShow();
		}
		
		if (chatPopup.getIndex() != -1)
		{	//the below line commented otherwise every new message would get the chat window to default location
			//chatPopup.setPosition();
			chatPopup.show();
			chatPopup.setVisible(true);
			//chatPopup.onShow();
		}
	}
	private	NewChatPopup	getLeastActiveChat()
	{
		Iterator iter = this.chats.values().iterator();
		long	tm = 0;
		NewChatPopup lancp = null;
		NewChatPopup ret = null;
		while (iter.hasNext())
		{
			lancp = (NewChatPopup)iter.next();
			if (lancp.getIndex() != -1)
			{
				long tm2 = lancp.getChatPanel().getLastActivityTime();
				if (tm == 0 || tm > tm2)
				{
					ret = lancp;
					tm = tm2;
				}
			}
		}
		return	ret;
	}
	public	void	showChatPopupIfPossible(UIRosterEntry other)
	{
		NewChatPopup popup = getChatPopup(other);
		if (popup != null)
		{
			this.showChatPopupIfPossible(popup);
		}
	}
	public	void	closeChat(NewChatPopup popup)
	{
		DimdimPopupsLayoutManager.getManager(me).dimdimPopupClosed(popup);
		String otherUserId = popup.getChatPanel().getOther().getUserId();
		this.setChatPopupIndexFree(popup.getIndex());
		popup.setIndex(-1);
		if (this.chats.get(otherUserId) == null)
		{
			//	Means that the user has left. Close the chat.
			popup.hide();
		}
		else
		{
			popup.setVisible(false);
		}
	}
	
	protected	boolean	chatPopup1InUse = false;
	protected	boolean	chatPopup2InUse = false;
	protected	boolean	chatPopup3InUse = false;
	protected	int		maxPrivateChats = 3;
	
	public	void	setMaxPrivateChats(int max)
	{
		this.maxPrivateChats = max;
	}
	public	int	getFreeChatPopupIndex()
	{
		int	index = -1;
		if (!chatPopup1InUse)
		{
			index = 1;
			chatPopup1InUse = true;
		}
		else if (!chatPopup2InUse && maxPrivateChats > 1)
		{
			index = 2;
			chatPopup2InUse = true;
		}
		else if (!chatPopup3InUse && maxPrivateChats > 2)
		{
			index = 3;
			chatPopup3InUse = true;
		}
		return	index;
	}
	public	void	setChatPopupIndexFree(int index)
	{
		if (index == 1)
		{
			chatPopup1InUse = false;
		}
		else if (index == 2)
		{
			chatPopup2InUse = false;
		}
		else if (index == 3)
		{
			chatPopup3InUse = false;
		}
	}
}
