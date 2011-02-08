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
public class JoinInvitationEmail	implements	InvitationEmail
{
	protected	static	final	String	InvitationHeadingPattern = "CONFERENCE_INVITATION_HEADING";
//	protected	static	final	String	InvitationHeading = "CONFERENCE_ORGANIZER has invited you to attend a Dimdim Web Meeting.";
	protected	static	final	String	InvitationHeading_1 = "CONFERENCE_ORGANIZER";
	protected	static	final	String	OrganizerCommentPattern = "ORGANIZER_COMMENT";
	protected	static	final	String	OrganizerComment = "&nbsp;";
//	protected	static	final	String	ScheduledConferenceComment = "The meeting is scheduled to start at "+
//		"START_TIME. At the time of the meeting simply click on the following url";
	
	protected	boolean		emailOrganizer = false;
	protected	List		presenters;
	protected	List		attendees;
	protected	String		subject;
	protected	String		emailText;
	protected	boolean		scheduledConference = false;
	protected	Locale		locale;
	protected	String		internToll;		
	protected	String		internTollFree;		
	protected	String		toll;
	protected	String		tollFree;
	protected	String		moderatorPassCode;
	protected	String		attendeePasscode;
	protected	String		attendeePwd;
	
	
	protected	ConferenceInfo	conferenceInfo;
	private String userType;
	
    protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
    protected	static	final	String	MESSAGE_HEADING_2 = "CONFERENCE_ORGANIZER: </font></td></tr>";
//	  	"Personal message from CONFERENCE_ORGANIZER: </font></td></tr>";
    
	public	JoinInvitationEmail(ConferenceInfo conferenceInfo,
			List presenters, List attendees, String message, boolean scheduledConference,
			Locale locale, String userType, String internToll, String internTollFree,
			String toll, String tollFree, String moderatorPassCode, String attendeePasscode, String attendeePwd)
	{
		this.conferenceInfo = conferenceInfo;
		this.presenters = presenters;
		this.attendees = attendees;
		this.scheduledConference = scheduledConference;
		this.locale = locale;
		this.userType = userType;
		subject = conferenceInfo.getOrganizerUTF8();
		this.internToll = internToll;
		this.internTollFree = internTollFree;
		this.toll = toll;
		this.tollFree = tollFree;
		this.moderatorPassCode = moderatorPassCode;
		this.attendeePasscode = attendeePasscode;
		this.attendeePwd = attendeePwd;
		//subject = conferenceInfo.getOrganizer();
		subject += " "+LocaleManager.getManager().getResourceKeyValue("email","ui_strings",this.locale,"subject.join", userType);
		emailText = formatMailText(conferenceInfo, message);
	}
//	public	JoinInvitationEmail(ConferenceInfo conferenceInfo,
//			List presenters, List attendees, String message, Locale locale)
//	{
//		this(conferenceInfo,presenters,attendees,message,false,locale);
//	}
//	public	JoinInvitationEmail(ConferenceInfo conferenceInfo,
//				List attendees, String message)
//	{
//		this(conferenceInfo,null,attendees,message);
//	}
//	public	JoinInvitationEmail(ConferenceInfo conferenceInfo,
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
		String headingComment = LocaleManager.getManager().getResourceKeyValue("email","ui_strings",
			this.locale,"invited_comment", userType);
		
		buf.append(ci.getJoinURL());
		buf.append("&email=EMAIL");
		buf.append("&displayName=DISPLAY_NAME");
		buf.append("&asPresenter=AS_PRESENTER");
		
		String str = null;
		try
		{
			str = LocaleManager.getManager().getHtmlFileBuffer("email","invitation_inst_email_template",locale, userType);
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String comment = JoinInvitationEmail.OrganizerComment;
		if (this.scheduledConference)
		{
		    	//resourceName = "invitation_sched_email_template";
			String ScheduledConferenceComment_1 = LocaleManager.getManager().getResourceKeyValue("email","ui_strings",
				this.locale,"scheduled_comment_1", userType);
			String ScheduledConferenceComment_2 = LocaleManager.getManager().getResourceKeyValue("email","ui_strings",
				this.locale,"scheduled_comment_2", userType);
			comment = ScheduledConferenceComment_1+" <b>"+ci.getStartTime()+"</b>"+ScheduledConferenceComment_2;
			comment = comment.replaceAll("START_TIME",ci.getStartTime());
		}else{
		    String ScheduledConferenceComment_1 = LocaleManager.getManager().getResourceKeyValue("email","ui_strings",this.locale,
			    "instant_comment_1", userType);
		    String ScheduledConferenceComment_2 = LocaleManager.getManager().getResourceKeyValue("email","ui_strings",this.locale,
			    "instant_comment_2", userType);
		    headingComment = LocaleManager.getManager().getResourceKeyValue("email","ui_strings",this.locale,
			    "invited_inst_comment", userType);
		    comment = ScheduledConferenceComment_1+" "+ci.getStartTime()+ScheduledConferenceComment_2;
		    comment = comment.replaceAll("START_TIME",ci.getStartTime());
		}
		if (str != null && str.length() >0)
		{
			System.out.println("Email Invitation: From Template");	
			
			String str1_1 = str.replace(JoinInvitationEmail.InvitationHeadingPattern,
					JoinInvitationEmail.InvitationHeading_1+ headingComment);
			String str1_2 = str1_1.replace(JoinInvitationEmail.OrganizerCommentPattern,
					comment);
			String str1_3 = str1_2.replaceAll("CONFERENCE_ACTION","join");
			String str1 = str1_3.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			String str2 = str1.replace("CONFERENCE_LINK",buf.toString());
			String str3 = str2.replace("CONFERENCE_SUBJECT",ci.getName());
			String str4 = str3.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			if (msg != null && msg.length() >0)
			{
				String messageFrom = LocaleManager.getManager().
					getResourceKeyValue("email","ui_strings",this.locale,
						"message_from_comment", userType);
				messageFrom = messageFrom+" "+ci.getOrganizerUTF8()+":";
				str4 = str4.replaceAll("PERSONAL_MESSAGE_HEADER",messageFrom);
				str4 = str4.replaceAll("PERSONAL_MESSAGE",msg);
				//str4 = str4.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			}
			else
			{
				str4 = str4.replaceAll("PERSONAL_MESSAGE_HEADER","");
				str4 = str4.replaceAll("PERSONAL_MESSAGE","");
			}
			String str5 = str4.replace("INT_DIAL_IN_TOLL_FREE", this.internTollFree);
			String str6 = str5.replace("INT_DIAL_IN", this.internToll);
			
			String str7 = str6.replace("LOCAL_DIAL_IN_TOLL_FREE",this.tollFree);
			String str8 = str7.replace("LOCAL_DIAL_IN", this.toll);
			
			String str10 = str8.replace("ATT_PASS_CODE", this.attendeePasscode);
			
//			String str11 = str10.replace("HOST_KEY", ci.getHost_key());
			String str11 = str10.replace("MEETING_KEY", this.attendeePwd);
			
			String str12 = str11.replace("CONFERENCE_KEY", ci.getKey());
			
			text = str12;

		}
		else
		{
			System.out.println("Email Invitation: From Buffer");
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
		buf.append("</td></tr><tr><td>");
		buf.append("International Dial In (Toll Free): INT_DIAL_IN_TOLL_FREE");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("</td></tr><tr><td>");
		buf.append("International Dial In: INT_DIAL_IN");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("</td></tr><tr><td>");
		buf.append("Local Dial In (Toll Free): LOCAL_DIAL_IN_TOLL_FREE");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("</td></tr><tr><td>");
		buf.append("Local Dial In: LOCAL_DIAL_IN");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("</td></tr><tr><td>");
		buf.append("Attendee Pass Code: ATT_PASS_CODE");
		buf.append(ConferenceConsoleConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("</td></tr><tr><td>");
		buf.append("Meeting Key: MEETING_KEY");
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
		return	this.conferenceInfo.getOrganizerEmail();
	}
	public String getAttendeePasscode() {
		return attendeePasscode;
	}
	public void setAttendeePasscode(String attendeePasscode) {
		this.attendeePasscode = attendeePasscode;
	}
	public String getAttendeePwd() {
		return attendeePwd;
	}
	public void setAttendeePwd(String attendeePwd) {
		this.attendeePwd = attendeePwd;
	}
	public String getInternToll() {
		return internToll;
	}
	public void setInternToll(String internToll) {
		this.internToll = internToll;
	}
	public String getInternTollFree() {
		return internTollFree;
	}
	public void setInternTollFree(String internTollFree) {
		this.internTollFree = internTollFree;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getModeratorPassCode() {
		return moderatorPassCode;
	}
	public void setModeratorPassCode(String moderatorPassCode) {
		this.moderatorPassCode = moderatorPassCode;
	}
	public String getToll() {
		return toll;
	}
	public void setToll(String toll) {
		this.toll = toll;
	}
	public String getTollFree() {
		return tollFree;
	}
	public void setTollFree(String tollFree) {
		this.tollFree = tollFree;
	}
}
