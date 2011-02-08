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

package com.dimdim.conference.action.roster;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.email.EmailDispatchManager2;
import com.dimdim.conference.model.FeedbackEmail;
import com.dimdim.conference.model.IConference;
import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class SendFeedbackAction	extends	ConferenceAction
{
	protected	String		sender = "";
	protected	String		rating = "";
	protected	String		comment = "";
	protected	String		toEmail;
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	
	public	SendFeedbackAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		System.out.println("Feedback: ***********");
		System.out.println("Sender:"+sender);
		System.out.println("Rating:"+rating);
		System.out.println("Comment:"+comment);
		System.out.println("*******************************");
		IConference conf = this.userSession.getConference();
		
		try
		{
			FeedbackEmail  fe = new FeedbackEmail(this.sender,this.rating,this.comment);
			if(null != toEmail  && toEmail.length() > 0)
			{
				fe.setToEmail(toEmail);
			}
			EmailDispatchManager2.getManager().dispatch(fe,(ActiveConference)conf,
				this.userSession.getUser(),this.getCurrentLocale(), userType);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		
		return	ret;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
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
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getUserType()
	{
	    return userType;
	}
	public void setUserType(String userType)
	{
	    this.userType = userType;
	}
}
