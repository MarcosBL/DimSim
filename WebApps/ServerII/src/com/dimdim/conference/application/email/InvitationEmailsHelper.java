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

package com.dimdim.conference.application.email;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Locale;
import java.util.Vector;

import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.InvitationEmail;
import com.dimdim.conference.model.JoinInvitationEmail;
import com.dimdim.conference.model.FeedbackEmail;
import	com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.ConferenceConsoleConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class InvitationEmailsHelper
{
	protected	ConferenceInfo	conferenceInfo;
	protected	String			presenterEmails;
	protected	String			attendeeEmails;
	protected	String			message;
	
	public InvitationEmailsHelper(ConferenceInfo conferenceInfo,
			String presenterEmails, String attendeeEmails, String message)
	{
		this.conferenceInfo = conferenceInfo;
		this.presenterEmails = presenterEmails;
		this.attendeeEmails = attendeeEmails;
		this.message = message;
	}
	public	void	sendInvitationEmails(ActiveConference conf, IConferenceParticipant sender, Locale locale, String userType,
			String internToll, String internTollFree, String toll, String tollFree, String moderatorPassCode,String attendeePasscode, String attendeePwd)
	{
		try
		{
			List presenters = readEmailsStr(this.presenterEmails);
			List attendees = readEmailsStr(this.attendeeEmails);
			JoinInvitationEmail  jie = new JoinInvitationEmail(this.conferenceInfo,
					presenters, attendees, message, false, locale, userType, internToll, internTollFree,
					toll, tollFree, moderatorPassCode, attendeePasscode, attendeePwd);
			EmailDispatchManager2.getManager().dispatch(jie,conf,sender,locale, userType);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static List	readEmailsStr(String str)
	{
		/*Vector	v = new Vector();
		if (str != null && str.length() >0)
		{
			StringTokenizer parser = new StringTokenizer(str,";");
			while (parser.hasMoreTokens())
			{
				v.addElement(parser.nextToken());
			}
		}
		return	v;*/
		System.out.println("Reading str: -"+str+"-");
		Vector	v = new Vector();
		//using ',' and '\n' also  as delimeter
		String tempStr = str.replace(',', ';');
		tempStr = tempStr.replace('\n', ';');
		tempStr = tempStr.replace(' ', ';');
		System.out.println("Reading tempStr: -"+tempStr+"-");
		
		if (tempStr != null && (tempStr=tempStr.trim()).length() > 0)
		{
			StringTokenizer parser = new StringTokenizer(tempStr,";");
			while (parser.hasMoreTokens())
			{
				v.addElement(parser.nextToken().trim());
			}
		}
		System.out.println("DILIP !@#$!@ vector = "+v);
		return	v;
	}
}
