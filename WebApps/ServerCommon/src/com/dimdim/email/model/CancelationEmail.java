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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dimdim.email.application.EmailConstants;
import com.dimdim.locale.ILocaleManager;
import com.dimdim.locale.LocaleResourceFile;


/**
 * @author Dilip Vedula
 * @email Dilip.Vedula@dimdim.com
 * 
 * This object is same as the participant object provided by the conference
 * core package. This simple subclass is required because there will be few
 * other users outside of the conferences, such as the system administrators.
 * Also because this object is mapped to user objects in the db.
 */
public class CancelationEmail	implements	InvitationEmail
{
	protected	static	final	String	InvitationHeadingPattern = "CONFERENCE_INVITATION_HEADING";
	protected	static	final	String	InvitationHeading = "CONFERENCE_ORGANIZER has cancelled a Dimdim Web Meeting.";
	protected	static	final	String	InvitationHeading_1 = "CONFERENCE_ORGANIZER";
	protected	static	final	String	OrganizerCommentPattern = "ORGANIZER_COMMENT";
	protected	static	final	String	OrganizerComment = "&nbsp;";
	protected	static	final	String	ScheduledConferenceComment = "The meeting is cancelled ";
		
	
	protected	boolean		emailOrganizer = false;
	protected	List		presenters = new ArrayList();
	protected	List		attendees;
	protected	String		subject;
	protected	String		emailText;
	protected	boolean		scheduledConference = false;
	protected	Locale		locale;
	
	protected	ConferenceInfo	conferenceInfo;
	protected   ILocaleManager localeManager = null;
	String role = LocaleResourceFile.FREE;
	
    protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
    protected	static	final	String	MESSAGE_HEADING_2 = "</font></td></tr>";
//	  	"Personal message from CONFERENCE_ORGANIZER: </font></td></tr>";
    
	public	CancelationEmail(ConferenceInfo conferenceInfo,
			List presenters, List attendees, String message, boolean scheduledConference,
			Locale locale, ILocaleManager localeManager, String role)
	{
		this.conferenceInfo = conferenceInfo;
		//do not send an email to the presenter
		//this.presenters = presenters;
		this.attendees = attendees;
		this.scheduledConference = scheduledConference;
		this.locale = locale;
		this.localeManager = localeManager;
		this.role = role;
		
		subject = conferenceInfo.getOrganizerUTF8();
		subject += " "+localeManager.getResourceKeyValue("email","ui_strings",this.locale,"subject.cancel", role);
		emailText = formatMailText(conferenceInfo, message);
	}

	public boolean getEmailOrganizer()
	{
		return emailOrganizer;
	}
	public void setEmailOrganizer(boolean emailOrganizer)
	{
		this.emailOrganizer = emailOrganizer;
	}
	private	String	formatMailText(ConferenceInfo ci, String msg)
	{
		String baseWebappURL = EmailConstants.getServerAddress()+"/"
			+EmailConstants.getWebappName();
		String text = null;
		StringBuffer buf = new StringBuffer();
		buf.append(ci.getJoinURL());
		//EmailConstants.get
		buf.append("&email=EMAIL");
		buf.append("&displayName=DISPLAY_NAME");
		buf.append("&asPresenter=AS_PRESENTER");
		
		String str = null;
		try
		{
			str = localeManager.getHtmlFileBuffer("email","cancellation_email_template",locale, role);
			//str="test message....";
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String comment = CancelationEmail.OrganizerComment;
		if (this.scheduledConference)
		{
			String ScheduledConferenceComment_1 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"scheduled_comment_1", role);
			String ScheduledConferenceComment_2 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"scheduled_comment_2", role);
			comment = ScheduledConferenceComment_1+" <b>"+ci.getStartTime()+"</b> "+ScheduledConferenceComment_2;
			comment = comment.replaceAll("START_TIME",ci.getStartTime());
		}
		if (str != null && str.length() >0)
		{
			System.out.println("Email Cancellation: From Template");	
			String headingComment = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"cancelation_comment", role);
			String str1_1 = str.replace(CancelationEmail.InvitationHeadingPattern,
					CancelationEmail.InvitationHeading_1+" "+ headingComment);
			String str1_2 = str1_1.replace(CancelationEmail.OrganizerCommentPattern,
					comment);
			String str1_3 = str1_2.replaceAll("CONFERENCE_ACTION","join");
			String str1 = str1_3.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			String str2 = str1.replace("CONFERENCE_LINK",buf.toString());
			String str21 = str2.replaceAll("CONFERENCE_AGENDA",ci.getAgenda());
			String str3 = str21.replace("CONFERENCE_SUBJECT",ci.getName());
			String str31 = str3.replace("CONFERENCE_RECURRENCE",ci.getRecurrenceType());
			String str32 = str31.replace("CONFERENCE_ENDTIME", ci.getEndTimeForMail());
			
			String str4 = str32.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			if (msg != null && msg.length() >0)
			{
				//String messageFrom = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"message_from_comment");
				String s = CancelationEmail.MESSAGE_HEADING_1+" "+" "+
					CancelationEmail.MESSAGE_HEADING_2;
				str4 = str4.replaceAll("MESSAGE_HEADING",s);
				str4 = str4.replaceAll("USER_MESSAGE",msg);
				str4 = str4.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			}
			else
			{
				str4 = str4.replaceAll("MESSAGE_HEADING","");
				str4 = str4.replaceAll("USER_MESSAGE","");
			}
			text = str4.replace("CONFERENCE_KEY",ci.getKey());
		}
		else
		{
			System.out.println("Email Invitation: From Buffer");
			text = fixedFormatBuffer(ci,msg);
			//System.out.println("Email Invitation: text ="+text);
		}
		
//		System.out.println("-----------------------------------------------------------");
//		System.out.println(text);
//		System.out.println("-----------------------------------------------------------");
		return	text;
	}
	
	private	String	fixedFormatBuffer(ConferenceInfo ci, String msg)
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("<html>");
		buf.append("<head>");
		buf.append("</head>");
		buf.append("<body>");
		buf.append("<br>");
		buf.append(ci.getOrganizerUTF8()+" has cancelled a Dimdim Web Meeting.");
		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);
		buf.append("<br><h3><a href=\"");
		buf.append(ci.getJoinURL());
		buf.append("&email=EMAIL");
		buf.append("&displayName=DISPLAY_NAME");
		buf.append("\">Click Here to Join Meeting</a></h3><br>");
		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);
		buf.append("<table>");
		buf.append("<tr><td>");
		buf.append("Meeting Name: ");
		buf.append(ci.getName());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("Meeting Key: ");
		buf.append(ci.getKey());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("Role: Attendee");
		buf.append(EmailConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("Join URL: ");
		buf.append(ci.getJoinURL());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</tr></td>");
		buf.append(EmailConstants.lineSeparator);
		buf.append("</table>");
		buf.append("<br>");
		if (msg != null && msg.length() > 0)
		{
			buf.append(msg);
		}
		buf.append(EmailConstants.lineSeparator);
		buf.append("");
		buf.append("</body></html>");
		
		return	buf.toString();
	}
	public	String	getOrganizer()
	{
		return	this.conferenceInfo.getOrganizer();
	}
	public ConferenceInfo getConferenceInfo()
	{
		return conferenceInfo;
	}
	public void setConferenceInfo(ConferenceInfo conferenceInfo)
	{
		this.conferenceInfo = conferenceInfo;
	}
	public String getEmailText()
	{
		return emailText;
	}
	public void setEmailText(String emailText)
	{
		this.emailText = emailText;
	}
	
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
	public boolean isScheduledConference()
	{
		return scheduledConference;
	}
	public void setScheduledConference(boolean scheduledConference)
	{
		this.scheduledConference = scheduledConference;
	}
	public	String	getOrganizerEmail()
	{
	    //organizer will not be sent an email
	    //return	this.conferenceInfo.getOrganizerEmail();
	    return null;
	}

	public List getAttendees()
	{
		return attendees;
	}

	public void setAttendees(List attendees)
	{
		this.attendees = attendees;
	}

	public List getPresenters()
	{
		return presenters;
	}

	public void setPresenters(List presenters)
	{
		this.presenters = presenters;
	}

	public String getCalInfo()
	{
	    // TODO Auto-generated method stub
	    return null;
	}
	
}
