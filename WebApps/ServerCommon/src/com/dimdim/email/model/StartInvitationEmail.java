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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import com.dimdim.email.application.EmailConstants;
import com.dimdim.locale.LocaleManager;
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
	
	protected	ConferenceInfo	conferenceInfo;
	
	protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
		"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
	protected	static	final	String	MESSAGE_HEADING_2 = "</font></td></tr>";
	
	String role = LocaleResourceFile.FREE;
//	"Personal message sent to all participants: </font></td></tr>";
//    protected	static	final	String	MESSAGE_HEADING = "<tr><td>&nbsp;</td></tr><tr>"+
//    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">"+
//    	"Personal message sent to all participants: </font></td></tr>";
    
	public	StartInvitationEmail(ConferenceInfo conferenceInfo,
			List presenters, List attendees, String message, Locale locale, String role)
	{
		this.conferenceInfo = conferenceInfo;
		this.presenters = presenters;
		this.attendees = attendees;
		this.locale = locale;
		this.role = role;
		//subject = conferenceInfo.getOrganizer();
		subject = LocaleManager.getManager().getResourceKeyValue("email","ui_strings",this.locale,"subject.start", role);
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
		String baseWebappURL = EmailConstants.getServerAddress()+"/"
			+EmailConstants.getWebappName();
		String text = null;
		StringBuffer buf = new StringBuffer();
		buf.append(baseWebappURL);
		buf.append("/JOIN_ACTION");
		buf.append("?email=EMAIL");
		
		if(null != conferenceInfo.getMeetingID()){
			buf.append("&meetingId=");
			buf.append(conferenceInfo.getMeetingID());
			if(null != conferenceInfo.getHost_key()){
				buf.append("&preseterPwd=");
				buf.append(conferenceInfo.getHost_key());
			}
			if(null != conferenceInfo.getMeeting_key()){
				buf.append("&attendeePwd=");
				buf.append(conferenceInfo.getMeeting_key());
			}
		}
		/*if (EmailConstants.isSecurityPolicyCheckKey())
		{
			buf.append("&securityKey=");
			buf.append(EmailConstants.getSecurityKey());
		}*/
		
		String str = null;
		try
		{
			str = LocaleManager.getManager().getHtmlFileBuffer("email","invitation_email_template",locale, role);
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if (str != null && str.length() >0)
		{
			String scheduledComment = LocaleManager.getManager().
				getResourceKeyValue("email","ui_strings",locale,"organizer_scheduled_comment", role);
			String str1_1 = str.replaceAll(StartInvitationEmail.InvitationHeadingPattern,
					scheduledComment+" <b>START_TIME</b>");
			
			String organizerComment = LocaleManager.getManager().
				getResourceKeyValue("email","ui_strings",locale,"organizer_message_1", role);
			String str1_2 = str1_1.replaceAll(StartInvitationEmail.OrganizerCommentPattern,
					organizerComment);
			String str1_3 = str1_2.replaceAll("CONFERENCE_ACTION","start");
			String str1_4 = str1_3.replaceAll("START_TIME",ci.getStartTime());
			/*String xyz = "";
			try
			{
			    xyz = new String(ci.getOrganizer().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}*/
			String str1 = str1_4.replaceAll("CONFERENCE_ORGANIZER", ci.getOrganizerUTF8());
			String str2 = str1.replace("CONFERENCE_LINK",buf.toString());
			String str3 = str2.replace("CONFERENCE_SUBJECT",ci.getName());
			String str31 = str3.replace("CONFERENCE_RECURRENCE",ci.getRecurrenceType());
			String str32 = str31.replace("CONFERENCE_ENDTIME", ci.getEndTimeForMail());
//			String str33 = str32.replace("INT_DIAL_IN", ci.getI);
			
			String str33 = str32.replace("ICAL_URL", EmailConstants.getServerAddress()+"/"
				+EmailConstants.getWebappName()+"/calendar.action?meetingId="+ci.getMeetingID()+"&role=presenter"+"&email=EMAIL"+"&preseterPwd="+ci.getHost_key()+"&attendeePwd="+ci.getMeeting_key());
			
			String str4 = str33.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			String str5 = str4.replaceAll("CONFERENCE_AGENDA",ci.getAgenda());
			if (msg != null && msg.length() >0)
			{
				/*String personalMessageComment = LocaleManager.getManager().
					getResourceKeyValue("email","ui_strings",locale,"organizer_message_2");
				
				str5 = str5.replaceAll("MESSAGE_HEADING",StartInvitationEmail.MESSAGE_HEADING_1+" "+
						personalMessageComment+" "+StartInvitationEmail.MESSAGE_HEADING_2);
						*/
				str5 = str5.replaceAll("MESSAGE_HEADING",StartInvitationEmail.MESSAGE_HEADING_1+" "+
						" "+StartInvitationEmail.MESSAGE_HEADING_2);
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
			
			String str9 = str8.replace("LOCAL_DIAL_IN_TOLL_FREE",ci.getTollFree());
			String str10 = str9.replace("LOCAL_DIAL_IN", ci.getToll());
			
			String str11 = str10.replace("PRESENTER_PASS_CODE", ci.getModeratorPassCode());
			String str12 = str11.replace("ATT_PASS_CODE", ci.getAttendeePasscode());
			
			String str13 = null;
			if(ci.getHost_key() != null)
				str13 = str12.replace("HOST_KEY", ci.getHost_key());
			else
				str13 = str12.replace("HOST_KEY", "");
			
			String str14 = null;
			if(ci.getMeeting_key() != null)
				str14 = str13.replace("MEETING_KEY", ci.getMeeting_key());
			else
				str14 = str13.replace("MEETING_KEY", "");
			
			text = str14;
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
		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);
		buf.append("<br><h3><a href=\"");
		buf.append(ci.getJoinURL());
		buf.append("&email=EMAIL");
		buf.append("&displayName=DISPLAY_NAME");
		buf.append("\">Click Here to Start Meeting</a></h3><br>");
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
		buf.append("<tr><tr><td>");
		buf.append("International Dial In: ");
		buf.append(ci.getInternToll());
		buf.append(EmailConstants.lineSeparator);
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
		//conferenceInfo.get
		TimeZone timezone = registry.getTimeZone("GMT");
		VTimeZone tz = timezone.getVTimeZone();
		
		//DateFormat formatStartDateUI = new SimpleDateFormat(" MMMM dd, yyyy HH:mm:a z");
		//DateFormat formatEndDateUI = new SimpleDateFormat("MMMM dd, yyyy");
		
		//February 11, 2008 05:30:PM IST
		 // Start Date is on: April 1, 2008, 9:00 am
		java.util.Calendar startDate = new GregorianCalendar();
		try
		{
		    startDate.setTime(InvitationEmailsHelper.formatStartDateUI.parse(conferenceInfo.getStartTime()) );
		} catch (ParseException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		
		java.util.Calendar meetingEndCal = new GregorianCalendar();
		meetingEndCal = new GregorianCalendar();
		meetingEndCal.setTime(startDate.getTime());
		meetingEndCal.add(Calendar.HOUR, 2);
		startDate.setTimeZone(timezone);
		startDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
		startDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
		startDate.set(java.util.Calendar.YEAR, 2008);
		startDate.set(java.util.Calendar.HOUR_OF_DAY, 9);
		startDate.set(java.util.Calendar.MINUTE, 0);
		startDate.set(java.util.Calendar.SECOND, 0);

		 // End Date is on: April 1, 2008, 13:00
		java.util.Calendar endDate = new GregorianCalendar();
		try
		{
		    if(null != conferenceInfo.getEndTime() && conferenceInfo.getEndTime().length() > 0)
		    {
			endDate.setTime(InvitationEmailsHelper.formatEndDateUI.parse(conferenceInfo.getEndTime()) );
		    }else{
			endDate.setTime(startDate.getTime());
		    }
		} catch (ParseException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		endDate.setTimeZone(timezone);
		endDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
		endDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
		endDate.set(java.util.Calendar.YEAR, 2008);
		endDate.set(java.util.Calendar.HOUR_OF_DAY, 13);
		endDate.set(java.util.Calendar.MINUTE, 0);	
		endDate.set(java.util.Calendar.SECOND, 0);

//		 Create the event
		Summary summary = new Summary(conferenceInfo.getAgenda());
//	  	 Do the recurrence until December 31st.


		String recurType = conferenceInfo.getRecurrenceTypeConst();
		String icalRecurType = "";
		
		if("SINGLE_EVENT".equals(recurType)){
		    icalRecurType = Recur.DAILY;
		}else if("DAILY".equals(recurType)){
		    icalRecurType = Recur.DAILY;
		}else if("WEEKLY".equals(recurType)){
		    icalRecurType = Recur.WEEKLY;
		}else if("MON_DATE".equals(recurType)){
		    icalRecurType = Recur.MONTHLY;
		}
	  	Recur recur = new Recur(icalRecurType, new net.fortuna.ical4j.model.Date(endDate.getTime()) );
	  	recur.getDayList().add(WeekDay.MO);
	  	recur.getDayList().add(WeekDay.TU);
	  	recur.getDayList().add(WeekDay.WE);
	  	recur.getDayList().add(WeekDay.TH);
	  	recur.getDayList().add(WeekDay.FR);
	  	recur.getDayList().add(WeekDay.SA);
	  	recur.getDayList().add(WeekDay.SU);
	  	recur.setInterval(1);
	  	recur.setWeekStartDay(WeekDay.MO.getDay());
	  	
	  	 RRule rrule = new RRule(recur);
	  	//meeting.getProperties().add(rrule);
	  	VEvent iCalEvent = new VEvent();
	  	iCalEvent.getProperties().add(rrule);
	  	iCalEvent.getProperties().add(summary);
	  	iCalEvent.getProperties().add(new DtStart(new net.fortuna.ical4j.model.Date(startDate.getTime())) );
	  	//iCalEvent.getProperties().add(new DtEnd(new net.fortuna.ical4j.model.Date(meetingEndCal.getTime())) );
	  	
	  	Dur dur = new Dur(0, 2, 0, 0);
	  	Duration duration = new Duration(dur);
	  	iCalEvent.getProperties().add(duration);
	 	 
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
		iCalEvent.getProperties().add(uid);

//		 Create a calendar
		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(Version.VERSION_2_0);


//		 Add the event and print
		icsCalendar.getComponents().add(iCalEvent);
		System.out.println("inside start invitaion Emai..."+icsCalendar);
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
	public	String	getOrganizerEmail()
	{
		return	this.conferenceInfo.getOrganizerEmail();
	}
	
}
