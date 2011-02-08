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
 
package com.dimdim.conference.ui.json.client;

import	com.google.gwt.json.client.JSONObject;
//
//import java.util.Date;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class UIChatEntry
{
	private	static	final	String	CHAT_KEY_MESSAGE_TEXT	=	"messageText";
	private	static	final	String	CHAT_KEY_SENTER_ID	=	"senderId";
	private	static	final	String	CHAT_KEY_SENTER_NAME	=	"senderName";
	private	static	final	String	CHAT_KEY_SYSTEM_MESSAGE	=	"systemMessage";
	private	static	final	String	CHAT_KEY_PRIVATE_MESSAGE	=	"privateMessage";
	
	protected	String	chatId;
	
	protected	String	messageText;
	protected	String	senderId;
	protected	String	senderName;
	protected	String	systemMessage;
	protected	String	privateMessage;
	
	public UIChatEntry()
	{
	}
	public	static	UIChatEntry		parseJsonObject(JSONObject reJson)
	{
		UIChatEntry	ce	=	new	UIChatEntry();
		
		ce.setMessageText(reJson.get(CHAT_KEY_MESSAGE_TEXT).isString().stringValue());
		ce.setSenderId(reJson.get(CHAT_KEY_SENTER_ID).isString().stringValue());
		ce.setSenderName(reJson.get(CHAT_KEY_SENTER_NAME).isString().stringValue());
		ce.setSystemMessage(reJson.get(CHAT_KEY_SYSTEM_MESSAGE).isString().stringValue());
		ce.setPrivateMessage(reJson.get(CHAT_KEY_PRIVATE_MESSAGE).isString().stringValue());
		
		return	ce;
	}
	public	String	toString()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("message:");
		buf.append(messageText);
		buf.append(",sender:");
		buf.append(senderId);
		buf.append(",senderName:");
		buf.append(senderName);
		buf.append(",system:");
		buf.append(systemMessage);
		buf.append(",private:");
		buf.append(privateMessage);
		
		return	buf.toString();	
	}
	public	boolean	isMessagePrivate()
	{
		if (this.privateMessage != null &&
				this.privateMessage.length() > 0)
		{
			return	(new Boolean(this.privateMessage)).booleanValue();
		}
		return	false;
	}
	public String getChatId()
	{
		return this.chatId;
	}
	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}
	public String getMessageText()
	{
		return messageText;
	}
	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}
	public String getSenderId()
	{
		return senderId;
	}
	public void setSenderId(String senderId)
	{
		this.senderId = senderId;
	}
	public String getSystemMessage()
	{
		return systemMessage;
	}
	public void setSystemMessage(String systemMessage)
	{
		this.systemMessage = systemMessage;
	}
	public String getPrivateMessage()
	{
		return privateMessage;
	}
	public void setPrivateMessage(String privateMessage)
	{
		this.privateMessage = privateMessage;
	}
	public String getSenderName()
	{
		return senderName;
	}
	public void setSenderName(String senderName)
	{
		this.senderName = senderName;
	}
}
