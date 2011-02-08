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
package com.dimdim.conference.model;

/**
 * @author Jayant.Pandit
 * @email  Jayant.Pandit@communiva.com
 */
public class EmailAttemptResult implements IJsonSerializable
{
	private String	sender = "";
	private String	target = "";
	private String  message = "";
	
	/**
	 * 
	 */
	public EmailAttemptResult()
	{
		super();
	}
	public	EmailAttemptResult(String sender, String target, String message)
	{
		this.sender = sender;
		this.target = target;
		this.message = message;
	}
	public	int	compareTo(Object obj)
	{
		if (obj instanceof EmailAttemptResult)
		{
			return	this.sender.compareTo(((EmailAttemptResult)obj).getSender());
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
		buf.append( "\"objClass\":\""); buf.append("EmailAttemptResult"); buf.append("\",");
		buf.append( "\"sender\":\""); buf.append(this.sender); buf.append("\",");
		buf.append( "\"target\":\""); buf.append(this.target); buf.append("\",");
		buf.append( "\"message\":\""); buf.append(this.message); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
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
