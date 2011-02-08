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

import java.util.Date;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class UIMessage
{
	protected	String	messageText;
	protected	String	senderId;
	protected	Date	timeSent;
	public UIMessage()
	{
	}
	public String getMessageText()
	{
		return this.messageText;
	}
	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}
	public String getSenderId()
	{
		return this.senderId;
	}
	public void setSenderId(String senderId)
	{
		this.senderId = senderId;
	}
	public Date getTimeSent()
	{
		return this.timeSent;
	}
	public void setTimeSent(Date timeSent)
	{
		this.timeSent = timeSent;
	}
}
