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
 
package com.dimdim.conference.model;

import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Locale;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.locale.LocaleManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object is same as the participant object provided by the conference
 * core package. This simple subclass is required because there will be few
 * other users outside of the conferences, such as the system administrators.
 * Also because this object is mapped to user objects in the db.
 */
public class StartInvitationEmail	implements	InvitationEmail
{
	protected	static	final	String	InvitationHeadingPattern = "CONFERENCE_INVITATION_HEADING";
//	protected	static	final	String	InvitationHeading = "You have scheduled a Dimdim Web Meeting at START_TIME";
	protected	static	final	String	OrganizerCommentPattern = "ORGANIZER_COMMENT";
//	protected	static	final	String	OrganizerComment = "An email invitation has been sent to all participants. "+
//		"At the time of the meeting simply click on the following url 5 minutes before the scheduled meeting time to start the meeting.";
	
	protected	boolean		emailOrganizer = false;
	protected	List		presenters;
	protected	List		attendees;
	protected	String		subject;
	protected	String		emailText;
	protected	Locale		locale;
	private String userType;
	protected	ConferenceInfo	conferenceInfo;
	
	protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
		"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
	protected	static	final	String	MESSAGE_HEADING_2 = "</font></td></tr>";
//	"Personal message sent to all participants: </font></td></tr>";
//    protected	static	final	String	MESSAGE_HEADING = "<tr><td>&nbsp;</td></tr><tr>"+
//    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">"+
//    	"Personal message sent to all participants: </font></td></tr>";
    
	public	StartInvitationEmail(ConferenceInfo conferenceInfo,
			List presenters, List attendees, String message, Locale locale, String userType)
	{
		this.conferenceInfo = conferenceInfo;
		this.presenters = presenters;
		this.attendees = attendees;
		this.locale = locale;
		this.userType = userType;
		subject = conferenceInfo.getOrganizerUTF8();
		emailText = formatMailText(conferenceInfo, message);
	}
//	public	StartInvitationEmail(ConferenceInfo conferenceInfo,
//				List attendees, String message)
//	{
//		this(conferenceInfo,null,attendees,message);
//	}
//	public	StartInvitationEmail(ConferenceInfo conferenceInfo,
//			String attendeesStr, String message)
//	{
//		this.conferenceInfo = conferenceInfo;
//		if (attendeesStr != null && attendeesStr.length() > 0)
//		{
//			this.attendees = new Vector();
//			StringTokenizer parser = new StringTokenizer(attendeesStr, ";");
//			while (parser.hasMoreTokens())
//			{
//				this.attendees.add(parser.nextToken());
//			}
//			
//			subject = conferenceInfo.getOrganizer();
//			emailText = formatMailText(conferenceInfo, message);
//		}
//	}
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
		String baseWebappURL = ConferenceConsoleConstants.getServerAddress()+"/"
			+ConferenceConsoleConstants.getWebappName();
		String text = null;
		StringBuffer buf = new StringBuffer();
		buf.append(ci.getStartURL());
		buf.append("&confName=");
		buf.append(ci.getName());
		buf.append("&email=EMAIL");
		buf.append("&displayName=DISPLAY_NAME");
		if (ConferenceConsoleConstants.isSecurityPolicyCheckKey())
		{
			buf.append("&securityKey=");
			buf.append(ConferenceConsoleConstants.getSecurityKey());
		}
		
		String str = null;
		try
		{
			str = LocaleManager.getManager().getHtmlFileBuffer("email","invitation_email_template",locale, userType);
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if (str != null && str.length() >0)
		{
			String scheduledComment = LocaleManager.getManager().
				getResourceKeyValue("email","ui_strings",locale,"organizer_scheduled_comment", userType);
			String str1_1 = str.replaceAll(StartInvitationEmail.InvitationHeadingPattern,
					scheduledComment+" START_TIME");
			
			String organizerComment = LocaleManager.getManager().
				getResourceKeyValue("email","ui_strings",locale,"organizer_message_1", userType);
			String str1_2 = str1_1.replaceAll(StartInvitationEmail.OrganizerCommentPattern,
					organizerComment);
			String str1_3 = str1_2.replaceAll("CONFERENCE_ACTION","start");
			String str1_4 = str1_3.replaceAll("START_TIME",ci.getStartTime());
			String str1 = str1_4.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			String str2 = str1.replace("CONFERENCE_LINK",buf.toString());
			String str3 = str2.replace("CONFERENCE_SUBJECT",ci.getName());
			String str4 = str3.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			if (msg != null && msg.length() >0)
			{
				String personalMessageComment = LocaleManager.getManager().
					getResourceKeyValue("email","ui_strings",locale,"organizer_message_2", userType);
				
				str4 = str4.replaceAll("MESSAGE_HEADING",StartInvitationEmail.MESSAGE_HEADING_1+" "+
						personalMessageComment+" "+StartInvitationEmail.MESSAGE_HEADING_2);
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
			text = fixedFormatBuffer(ci,msg);
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
		buf.append(ci.getOrganizerUTF8()+" has invited you to attend a meeting on DimDim");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("<br><h3><a href=\"");
		buf.append(ci.getJoinURL());
		buf.append("&email=EMAIL");
		buf.append("&displayName=DISPLAY_NAME");
		buf.append("\">Click Here to Join Meeting</a></h3><br>");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("<table>");
		buf.append("<tr><td>");
		buf.append("Meeting Name: ");
		buf.append(ci.getName());
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("Meeting Key: ");
		buf.append(ci.getKey());
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("Role: Attendee");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("Join URL: ");
		buf.append(ci.getJoinURL());
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</tr></td>");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</table>");
		buf.append("<br>");
		if (msg != null && msg.length() > 0)
		{
			buf.append(msg);
		}
		buf.append(ConferenceConsoleConstants.lineSeparator);
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
	public List getAttendees()
	{
		return attendees;
	}
	public void setAttendees(List attendees)
	{
		this.attendees = attendees;
	}
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	public List getPresenters()
	{
		return presenters;
	}
	public void setPresenters(List presenters)
	{
		this.presenters = presenters;
	}
	public	String	getOrganizerEmail()
	{
		return	this.conferenceInfo.getOrganizerEmail();
	}
}
