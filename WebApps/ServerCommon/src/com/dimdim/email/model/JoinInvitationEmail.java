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
import java.util.Locale;

import com.dimdim.email.application.EmailConstants;
import com.dimdim.locale.ILocaleManager;
import com.dimdim.locale.LocaleResourceFile;


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
	protected	static	final	String	InvitationHeading = "CONFERENCE_ORGANIZER has invited you to attend a Dimdim Web Meeting.";
	protected	static	final	String	InvitationHeading_1 = "CONFERENCE_ORGANIZER";
	protected	static	final	String	OrganizerCommentPattern = "ORGANIZER_COMMENT";
	protected	static	final	String	OrganizerComment = "&nbsp;";
	//protected	static	final	String	ScheduledConferenceComment = "The meeting is scheduled to start at "+
	//	"START_TIME. At the time of the meeting simply click on the following url";
	
	protected	boolean		emailOrganizer = false;
	protected	List		presenters;
	protected	List		attendees;
	protected	String		subject;
	protected	String		emailText;
	protected	boolean		scheduledConference = false;
	protected	Locale		locale;
			String		role = LocaleResourceFile.FREE;
	
	protected	ConferenceInfo	conferenceInfo;
	protected   ILocaleManager localeManager = null;
	
    protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
    protected	static	final	String	MESSAGE_HEADING_2 = "</font></td></tr>";
//	  	"Personal message from CONFERENCE_ORGANIZER: </font></td></tr>";
    
	public	JoinInvitationEmail(ConferenceInfo conferenceInfo,
			List presenters, List attendees, String message, boolean scheduledConference,
			Locale locale, ILocaleManager localeManager, String role)
	{
		this.conferenceInfo = conferenceInfo;
		this.presenters = presenters;
		this.attendees = attendees;
		this.scheduledConference = scheduledConference;
		this.locale = locale;
		this.localeManager = localeManager;
		this.role = role;
		
		subject = conferenceInfo.getOrganizerUTF8();
		subject += " "+localeManager.getResourceKeyValue("email","ui_strings",this.locale,"subject.join", role);
		emailText = formatMailText(conferenceInfo, message);
	}
	
	public	JoinInvitationEmail(ConferenceInfo conferenceInfo,
			List presenters, List attendees, String message, 
			Locale locale, ILocaleManager localeManager, String role)
	{
		this.conferenceInfo = conferenceInfo;
		this.presenters = presenters;
		this.attendees = attendees;
		this.scheduledConference = scheduledConference;
		this.locale = locale;
		this.localeManager = localeManager;
		this.role = role;
		//subject = conferenceInfo.getOrganizer();
		subject = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"subject.joinSchedule", role);
		emailText = formatMailTextScheduledStartAttendeeInvite(conferenceInfo, message);
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
		String baseWebappURL = EmailConstants.getServerAddress()+"/"
			+EmailConstants.getWebappName();
		String text = null;
		String resourceName = "invitation_inst_email_template";
		String headingComment = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"invited_comment", role);
		
		StringBuffer buf = new StringBuffer();
		//buf.append(ci.getJoinURL());
		buf.append(baseWebappURL);
		buf.append("/JOIN_ACTION?confKey=");
		//EmailConstants.get
		buf.append(ci.getKey());
		buf.append("&email=EMAIL");
		buf.append("&asPresenter=AS_PRESENTER");
		if(null != conferenceInfo.getMeetingID()){
			buf.append("&meetingId=");
			buf.append(conferenceInfo.getMeetingID());
			if(null != conferenceInfo.getMeeting_key()){
				buf.append("&attendeePwd=");
				buf.append(conferenceInfo.getMeeting_key());
			}
		}
		
		String comment = JoinInvitationEmail.OrganizerComment;
		if (this.scheduledConference)
		{
		    	resourceName = "invitation_sched_email_template";
			String ScheduledConferenceComment_1 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"scheduled_comment_1", role);
			String ScheduledConferenceComment_2 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"scheduled_comment_2", role);
			comment = ScheduledConferenceComment_1+" <b>"+ci.getStartTime()+"</b>"+ScheduledConferenceComment_2;
			comment = comment.replaceAll("START_TIME",ci.getStartTime());
		}else{
		    String ScheduledConferenceComment_1 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"instant_comment_1", role);
		    String ScheduledConferenceComment_2 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"instant_comment_2", role);
		    headingComment = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"invited_inst_comment", role);
		    comment = ScheduledConferenceComment_1+" "+ci.getStartTime()+ScheduledConferenceComment_2;
		    comment = comment.replaceAll("START_TIME",ci.getStartTime());
		}
		
		String str = null;
		try
		{
			str = localeManager.getHtmlFileBuffer("email",resourceName,locale, role);
			//str="test message....";
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			String str31 = str3.replace("CONFERENCE_RECURRENCE",ci.getRecurrenceType());
			String str32 = str31.replace("CONFERENCE_ENDTIME", ci.getEndTimeForMail());
			String str33 = str32.replace("ICAL_URL", EmailConstants.getServerAddress()+"/"
				+EmailConstants.getWebappName()+"/calendar.action?meetingId="+ci.getMeetingID()+"&email=EMAIL"+"&attendeePwd="+ci.getMeeting_key());
			
			String str4 = str33.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			String str5 = str4.replaceAll("CONFERENCE_AGENDA",ci.getAgenda());
			if (msg != null && msg.length() >0)
			{
				//String messageFrom = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"message_from_comment");
				String s = JoinInvitationEmail.MESSAGE_HEADING_1+" "+" "+
					JoinInvitationEmail.MESSAGE_HEADING_2;
				str5 = str5.replaceAll("MESSAGE_HEADING",s);
				str5 = str5.replaceAll("USER_MESSAGE",msg);
				str5 = str5.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			}
			else
			{
				str5 = str5.replaceAll("MESSAGE_HEADING","");
				str5 = str5.replaceAll("USER_MESSAGE","");
			}
			String str6 = str5.replace("CONFERENCE_KEY",ci.getKey());
			
			String str7 = str6.replace("INT_DIAL_IN_TOLL_FREE", ci.getInternTollFree());
			String str8 = str7.replace("INT_DIAL_IN", ci.getInternToll());
			
			String str9 = str8.replace("LOCAL_DIAL_IN_TOLL_FREE",ci.getToll());
			String str10 = str9.replace("LOCAL_DIAL_IN", ci.getTollFree());
			
			String str12 = str10.replace("ATT_PASS_CODE", ci.getAttendeePasscode());
			String str13 = null;
			if(ci.getHost_key() == null)
				str13 = str12.replace("HOST_KEY", "");
			else
				str13 = str12.replace("HOST_KEY", ci.getHost_key());
			String str14 = null;
			if(ci.getMeeting_key() == null)
				str14 = str13.replace("MEETING_KEY", "");
			else
				str14 = str13.replace("MEETING_KEY", ci.getMeeting_key());
			
			text = str14;
		}
		else
		{
			System.out.println("Email Invitation: From Buffer");
			text = fixedFormatBuffer(ci,msg);
			//System.out.println("Email Invitation: text ="+text);
		}
		
		/*System.out.println("-----------------------------------------------------------");
		System.out.println(text);
		System.out.println("-----------------------------------------------------------");*/
		return	text;
	}
	
	private	String	formatMailTextScheduledStartAttendeeInvite(ConferenceInfo ci, String msg)
	{
		String baseWebappURL = EmailConstants.getServerAddress()+"/"
			+EmailConstants.getWebappName();
		String text = null;
		StringBuffer buf = new StringBuffer();
		//buf.append(ci.getJoinURL());
		buf.append(baseWebappURL);
		buf.append("/JOIN_ACTION?confKey=");
		//EmailConstants.get
		buf.append(ci.getKey());
		buf.append("&email=EMAIL");
		buf.append("&asPresenter=AS_PRESENTER");
		if(null != conferenceInfo.getMeetingID()){
			buf.append("&meetingId=");
			buf.append(conferenceInfo.getMeetingID());
			if(null != conferenceInfo.getMeeting_key()){
				buf.append("&attendeePwd=");
				buf.append(conferenceInfo.getMeeting_key());
			}
		}
		
		String str = null;
		try
		{
			str = localeManager.getHtmlFileBuffer("email","invitation_sched_attendee_email_template",locale, role);
			//str="test message....";
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String comment = JoinInvitationEmail.OrganizerComment;
		if (this.scheduledConference)
		{
			String ScheduledConferenceComment_1 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"scheduled_comment_1", role);
			String ScheduledConferenceComment_2 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"scheduled_comment_2", role);
			comment = ScheduledConferenceComment_1+" "+ci.getStartTime()+" "+ScheduledConferenceComment_2;
			comment = comment.replaceAll("START_TIME",ci.getStartTime());
		}else{
		    String ScheduledConferenceComment_1 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"instant_comment_1", role);
		    String ScheduledConferenceComment_2 = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"instant_comment_2", role);
		    comment = ScheduledConferenceComment_1+" "+ci.getStartTime()+" "+ScheduledConferenceComment_2;
		    comment = comment.replaceAll("START_TIME",ci.getStartTime());
		}
		if (str != null && str.length() >0)
		{
			System.out.println("Email Invitation: From Template");	
			String headingComment = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"invited_comment", role);
			String str1_1 = str.replace(JoinInvitationEmail.InvitationHeadingPattern,
					JoinInvitationEmail.InvitationHeading_1+headingComment);
			String str1_2 = str1_1.replace(JoinInvitationEmail.OrganizerCommentPattern,
					comment);
			String str1_3 = str1_2.replaceAll("CONFERENCE_ACTION","join");
			String str1 = str1_3.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			String str2 = str1.replace("CONFERENCE_LINK",buf.toString());
			String str3 = str2.replace("CONFERENCE_SUBJECT",ci.getName());
			String str31 = str3.replace("CONFERENCE_RECURRENCE",ci.getRecurrenceType());
			String str32 = str31.replace("CONFERENCE_ENDTIME", ci.getEndTimeForMail());
			
			String str4 = str32.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			String str5 = str4.replaceAll("CONFERENCE_AGENDA",ci.getAgenda());
			if (msg != null && msg.length() >0)
			{
				//String messageFrom = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"message_from_comment");
				String s = JoinInvitationEmail.MESSAGE_HEADING_1+" "+" "+
					JoinInvitationEmail.MESSAGE_HEADING_2;
				str5 = str5.replaceAll("MESSAGE_HEADING",s);
				str5 = str5.replaceAll("USER_MESSAGE",msg);
				str5 = str5.replaceAll("CONFERENCE_ORGANIZER",ci.getOrganizerUTF8());
			}
			else
			{
				str5 = str5.replaceAll("MESSAGE_HEADING","");
				str5 = str5.replaceAll("USER_MESSAGE","");
			}
			String str6 = str5.replace("CONFERENCE_KEY",ci.getKey());
			
			String str7 = str6.replace("INT_DIAL_IN_TOLL_FREE", ci.getInternTollFree());
			String str8 = str7.replace("INT_DIAL_IN", ci.getInternToll());
			
			String str9 = str8.replace("LOCAL_DIAL_IN_TOLL_FREE",ci.getToll());
			String str10 = str9.replace("LOCAL_DIAL_IN", ci.getTollFree());
			
			String str12 = str10.replace("ATT_PASS_CODE", ci.getAttendeePasscode());
			String str13 = null;
			if(ci.getHost_key() != null)
				str13 = str12.replace("HOST_KEY", ci.getHost_key());
			else
				str13 = str12.replace("HOST_KEY", "");
			String str14 = null;
			if(null != ci.getMeeting_key())
				str14 = str13.replace("MEETING_KEY", ci.getMeeting_key());
			else
				str14 = str13.replace("MEETING_KEY", "");
			
			text = str14;


		}
		else
		{
			System.out.println("Email Invitation: From Buffer");
			text = fixedFormatBuffer(ci,msg);
			//System.out.println("Email Invitation: text ="+text);
		}
		
		/*System.out.println("-----------------------------------------------------------");
		System.out.println(text);
		System.out.println("-----------------------------------------------------------");*/
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
		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);
		buf.append("<br><h3><a href=\"");
		buf.append(ci.getJoinURL());
/*		buf.append("&email=EMAIL");
		buf.append("&displayName=DISPLAY_NAME");
*/		buf.append("\">Click Here to Join Meeting</a></h3><br>");
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
		buf.append("</td><td><tr>");
		buf.append("International Dial In (Toll Free):");
		buf.append(ci.getInternTollFree());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</tr><tr>");
		buf.append("LOCAL_DIAL_IN");
		buf.append(ci.getToll());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</tr><tr>");

		buf.append("LOCAL_DIAL_IN_TOLL_FREE");
		buf.append(ci.getTollFree());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</tr><tr>");

		buf.append("Moderator Pass Code:");
		buf.append(ci.getModeratorPassCode());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</tr><tr>");

		buf.append("Attendee Pass Code:");
		buf.append(ci.getAttendeePasscode());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</tr><tr>");
		
		buf.append("Host Key:");
		buf.append(ci.getHost_key());
		buf.append(EmailConstants.lineSeparator);
		buf.append("</tr><tr>");
		
		buf.append("Meeting Key:");
		buf.append(ci.getMeeting_key());
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
	
	/*public String getCalInfo(){
//		 Create a TimeZone
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("America/Mexico_City");
		VTimeZone tz = timezone.getVTimeZone();

		 // Start Date is on: April 1, 2008, 9:00 am
		java.util.Calendar startDate = new GregorianCalendar();
		startDate.setTimeZone(timezone);
		startDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
		startDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
		startDate.set(java.util.Calendar.YEAR, 2008);
		startDate.set(java.util.Calendar.HOUR_OF_DAY, 9);
		startDate.set(java.util.Calendar.MINUTE, 0);
		startDate.set(java.util.Calendar.SECOND, 0);

		 // End Date is on: April 1, 2008, 13:00
		java.util.Calendar endDate = new GregorianCalendar();
		endDate.setTimeZone(timezone);
		endDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
		endDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
		endDate.set(java.util.Calendar.YEAR, 2008);
		endDate.set(java.util.Calendar.HOUR_OF_DAY, 13);
		endDate.set(java.util.Calendar.MINUTE, 0);	
		endDate.set(java.util.Calendar.SECOND, 0);

//		 Create the event
		String eventName = "For Rohit(";
		DateTime start = new DateTime(startDate.getTime());
		DateTime end = new DateTime(endDate.getTime());
		VEvent meeting = new VEvent(start, end, eventName);
		meeting.getProperties().add(tz.getTimeZoneId());
		
		UidGenerator ug = null;;
		try
		{
		    ug = new UidGenerator("uidGen");
		} catch (SocketException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		Uid uid = ug.generateUid();
		meeting.getProperties().add(uid);

//		 Create a calendar
		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(Version.VERSION_2_0);


//		 Add the event and print
		icsCalendar.getComponents().add(meeting);
		System.out.println("insidew Join invitaion Emai..."+icsCalendar);
		return icsCalendar.toString();
	}*/

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
}
