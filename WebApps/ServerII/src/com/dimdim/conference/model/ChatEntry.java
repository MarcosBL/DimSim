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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : ChatMessage.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.model;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class ChatEntry extends	MeetingEventData	implements IJsonSerializable
{
	private String	systemMessage = "false";
	private String	privateMessage = "false";
	private String  messageText;
	private String  senderId;
	private String  senderName;
	
	/**
	 * 
	 */
//	public ChatEntry()
//	{
//		super();
//	}
	public	ChatEntry(String senderId, String messageText)
	{
		this(senderId, senderId, messageText);
	}
	public	ChatEntry(String senderId, String senderName, String messageText)
	{
		super(senderId);
		this.systemMessage = "false";
		this.privateMessage = "false";
		this.senderId = senderId;
		this.senderName = senderName;
		this.messageText = messageText;
	}
	public	int	compareTo(Object obj)
	{
		if (obj instanceof ChatEntry)
		{
			return	this.senderId.compareTo(((ChatEntry)obj).getSenderId());
		}
		else
		{
			return	1;
		}
	}
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("ChatEntry"); buf.append("\",");
		buf.append( "\"senderId\":\""); buf.append(this.senderId); buf.append("\",");
		
//		String tmp1 = this.senderName.replaceAll("\"","&quot;");
		buf.append( "\"senderName\":\""); buf.append(this.senderName); buf.append("\",");
		
//		String tmp2 = this.messageText.replaceAll("\"","&quot;");
//		String tmp3 = tmp2.replaceAll(" ","&nbsp;");
		buf.append( "\"messageText\":\""); buf.append(this.messageText); buf.append("\",");
		
		buf.append( "\"systemMessage\":\""); buf.append(this.systemMessage); buf.append("\",");
		buf.append( "\"privateMessage\":\""); buf.append(this.privateMessage); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
	/**
	 * @return Returns the isSystemMessage.
	 */
	public boolean isSystemMessage()
	{
		return Boolean.getBoolean(systemMessage);
	}

	/**
	 * @param isSystemMessage The isSystemMessage to set.
	 */
	public void setSystemMessage(boolean systemMessage)
	{
		this.systemMessage = (new Boolean(systemMessage)).toString();
	}

	/**
	 * @return Returns the messageText.
	 */
	public String getMessageText()
	{
		return messageText;
	}

	/**
	 * @param messageText The messageText to set.
	 */
	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}

	/**
	 * @return Returns the sender.
	 */
	public String getSenderId()
	{
		return senderId;
	}

	/**
	 * @param sender The sender to set.
	 */
	public void setSenderId(String senderId)
	{
		this.senderId = senderId;
	}
	public boolean isPrivateMessage()
	{
		return Boolean.getBoolean(privateMessage);
	}
	public void setPrivateMessage(boolean privateMessage)
	{
		this.privateMessage = (new Boolean(privateMessage)).toString();
	}
	public String getPrivateMessage()
	{
		return privateMessage;
	}
	public void setPrivateMessage(String privateMessage)
	{
		this.privateMessage = privateMessage;
	}
	public String getSystemMessage()
	{
		return systemMessage;
	}
	public void setSystemMessage(String systemMessage)
	{
		this.systemMessage = systemMessage;
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
