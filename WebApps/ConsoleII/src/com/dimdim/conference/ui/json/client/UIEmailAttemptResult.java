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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UIEmailAttemptResult	extends	UIObject
{
	private	static	final	String	EAR_KEY_SENDER	=	"sender";
	private	static	final	String	EAR_KEY_TARGET	=	"target";
	private	static	final	String	EAR_KEY_MESSAGE	=	"message";
	
	protected	String	id;
	protected	String	sender;
	protected	String	target;
	protected	String	message;
	
	public	UIEmailAttemptResult()
	{
	}
	public	static	UIEmailAttemptResult		parseJsonObject(JSONObject reJson)
	{
		UIEmailAttemptResult	ear	=	new	UIEmailAttemptResult();
		
		ear.setSender(reJson.get(EAR_KEY_SENDER).isString().stringValue());
		ear.setTarget(reJson.get(EAR_KEY_TARGET).isString().stringValue());
		ear.setMessage(reJson.get(EAR_KEY_MESSAGE).isString().stringValue());
		
		return	ear;
	}
	public	String	toString()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("sender:");
		buf.append(sender);
		buf.append(",target:");
		buf.append(target);
		buf.append(",message:");
		buf.append(message);
		
		return	buf.toString();	
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getSender()
	{
		return sender;
	}
	public void setSender(String sender)
	{
		this.sender = sender;
	}
	public String getTarget()
	{
		return target;
	}
	public void setTarget(String target)
	{
		this.target = target;
	}
}
