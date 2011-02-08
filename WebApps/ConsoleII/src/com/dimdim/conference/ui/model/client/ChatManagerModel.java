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

package com.dimdim.conference.ui.model.client;

import	java.util.HashMap;
import java.util.Iterator;

import	com.dimdim.conference.ui.json.client.UIRosterEntry;
import	com.dimdim.conference.ui.json.client.UIChatEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ChatManagerModel	extends	FeatureModel
{
	protected	HashMap	chatListeners;
	protected	UIRosterEntry	me;
	
	public	ChatManagerModel(UIRosterEntry me)
	{
		super("feature.chat");
		chatListeners = new HashMap();
		this.me = me;
	}
	
	public	void	addChatListener(ChatListener chatListener)
	{
		this.chatListeners.put(chatListener,chatListener);
	}
	public	void	removeChatListener(ChatListener chatListener)
	{
		this.chatListeners.remove(chatListener);
	}
	public	void	addChatListenerWithDelay(FeatureModelListener listener)
	{
		if (this.newListeners == null)
		{
			this.newListeners = new HashMap();
		}
		this.newListeners.put(listener, listener);
//		Window.alert("Number of listeners for feature: "+featureId+", : "+this.listeners.size());
	}
	protected	void	checkNewChatListeners()
	{
		if (this.newListeners != null)
		{
			this.chatListeners.putAll(this.newListeners);
			this.newListeners = null;
		}
	}
//	public	void	removeChatListener(String chatId)
//	{
//		this.chatListeners.remove(chatId);
//	}
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert("Received event:"+eventId+", data:"+data.toString());
		checkNewChatListeners();
		String eventMethod = eventId;
		int index = eventId.lastIndexOf(".");
		if (index > 0)
		{
			eventMethod = eventId.substring(index+1);
		}
//		Window.alert("triggering event:"+eventMethod);
//		actOnEvent(eventMethod,data);
		if (eventMethod.equalsIgnoreCase("message"))
		{
			onmessage(data);
		}
	}
	/**
	 * Forward the events for the invividual chats to the respective chat
	 * listener.
	 */
	public	void	onmessage(Object data)
	{
		UIChatEntry chatMsg = (UIChatEntry)data;
		if (this.me.getUserId().equals(chatMsg.getSenderId()))
		{
			return;
		}
		Iterator iter = this.chatListeners.values().iterator();
		while (iter.hasNext())
		{
			((ChatListener)iter.next()).onChatMessage(chatMsg);
		}
	}
}
