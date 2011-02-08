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
 
package com.dimdim.email.model;

import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;

import com.dimdim.email.application.EmailConstants;

//import com.dimdim.conference.ConferenceConsoleConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */
public class FeedbackEmail	implements	IEmail
{
	protected	String		subject = "";
	protected	String		emailText = "";
	
	protected	String		sender = "";
	protected	String		rating = "";
	protected	String		comment = "";
	
	public FeedbackEmail(String sender, String rating, String comment)
	{
		this.sender = sender;
		this.rating = rating;
		this.comment = comment;
		
		this.subject = "Feedback from: "+this.sender;
		this.emailText = this.formatEmailText();
	}
	private	String	formatEmailText()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("Rating:");
		buf.append(this.rating);
		buf.append(EmailConstants.lineSeparator);
		buf.append("Comment:");
		buf.append(this.comment);
		buf.append(EmailConstants.lineSeparator);
		
		return	buf.toString();
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public String getEmailText()
	{
		return emailText;
	}
	public void setEmailText(String emailText)
	{
		this.emailText = emailText;
	}
	public String getRating()
	{
		return rating;
	}
	public void setRating(String rating)
	{
		this.rating = rating;
	}
	public String getSender()
	{
		return sender;
	}
	public void setSender(String sender)
	{
		this.sender = sender;
	}
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
}
