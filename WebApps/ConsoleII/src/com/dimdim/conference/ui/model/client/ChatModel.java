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

import com.dimdim.conference.ui.json.client.JSONurlReader;
import	com.dimdim.conference.ui.json.client.UIChatEntry;
import com.dimdim.conference.ui.json.client.UIEventListener;
import com.dimdim.conference.ui.json.client.UIServerResponse;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ChatModel	extends	UISubModel
{
	
	protected	String	chatId;
	
	public	ChatModel(String chatId)
	{
		this.chatId = chatId;
	}
	
	public String getChatId()
	{
		return this.chatId;
	}
	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}
	
	public	void	sendChatMessage(String chatId, String message)
	{
//		String	url = this.commandsFactory.getPostChatMessageURL(chatId,message);
//		this.executeCommand(url);
	}
	
}
