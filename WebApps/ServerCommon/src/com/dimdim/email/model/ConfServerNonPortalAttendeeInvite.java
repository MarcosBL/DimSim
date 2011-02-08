package com.dimdim.email.model;

import java.util.Locale;

import com.dimdim.email.application.EmailConstants;
import com.dimdim.locale.ILocaleManager;
import com.dimdim.locale.LocaleResourceFile;

public class ConfServerNonPortalAttendeeInvite extends PasswordRecovery{
	protected	static	final	String	InvitationHeadingPattern = "CONFERENCE_INVITATION_HEADING";
	protected	static	final	String	InvitationHeading = "CONFERENCE_ORGANIZER has cancelled a Dimdim Web Meeting.";
	protected	static	final	String	InvitationHeading_1 = "CONFERENCE_ORGANIZER";
	protected	static	final	String	OrganizerCommentPattern = "ORGANIZER_COMMENT";
	protected	static	final	String	OrganizerComment = "&nbsp;";
	protected	static	final	String	ScheduledConferenceComment = "The meeting is cancelled ";




	protected	Locale		locale;

	//protected	ConferenceInfo	conferenceInfo;
	protected   ILocaleManager localeManager = null;

    protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
    //protected	static	final	String	MESSAGE_HEADING_2 = "</font></td></tr>";
//	  	"Personal message from CONFERENCE_ORGANIZER: </font></td></tr>";

	public	ConfServerNonPortalAttendeeInvite(String email, String newPassword,String userName,
			Locale locale, ILocaleManager localeManager)
	{
		//TODO: wrong use of inheritance will have to refactor the code
		super(email, newPassword, userName, locale, localeManager, null);
		this.email = email;
		this.userName = " ";
		this.newPassword = " ";
		this.locale = locale;
		this.localeManager = localeManager;
		//subject = conferenceInfo.getOrganizer();
		subject = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"subject.nonPortalInvite", LocaleResourceFile.FREE);
//		System.out.println("inside send non portal attendee subject buffer = "+subject);
		emailText = formatMailText();
//		System.out.println("inside send non portal attendee email buffer = "+emailText);
	}

	private	String	formatMailText()
	{
		String baseWebappURL = EmailConstants.getServerAddress()+"/"
			+EmailConstants.getWebappName();
		String text = null;

		String str = null;
		try
		{
			str = localeManager.getHtmlFileBuffer("email","thankyou_email_template",locale, LocaleResourceFile.FREE);
			//str="test message....";
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			System.out.println("caught exception");
			e.printStackTrace();
		}
		String comment = CancelationEmail.OrganizerComment;
		System.out.println("comment is " + comment);

		if (str != null && str.length() >0)
		{
			System.out.println("NonPortal attendee invite: From Template");
			String headingComment = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"nonPortalInvite_comment", LocaleResourceFile.FREE);
			String str1_1 = str.replace(CancelationEmail.InvitationHeadingPattern,
					CancelationEmail.InvitationHeading_1+" "+ headingComment);
			String str1_2 = str1_1.replace(CancelationEmail.OrganizerCommentPattern,
					comment);
			String str1_3 = str1_2.replaceAll("CONFERENCE_ORGANIZER","");
			//String str1 = str1_3.replaceAll("USER_NAME", userName);
			//String str2 = str1.replace("USER_PASSWORD",newPassword);

			String str4 = str1_3.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			text = str4;//.replace("CONFERENCE_KEY",ci.getKey());
//			System.out.println(text);
		}
		else
		{
			//System.out.println("Email Invitation: From Buffer");
			text = fixedFormatBuffer();
			//System.out.println("Email Invitation: text ="+text);
		}

		/*System.out.println("-----------------------------------------------------------");
		System.out.println(text);
		System.out.println("-----------------------------------------------------------");*/
		return	text;
	}

	private	String	fixedFormatBuffer()
	{
//		System.out.println("comming into fixedformatbuffer");
		StringBuffer buf = new StringBuffer();

		buf.append("<html>");
		buf.append("<head>");
		buf.append("</head>");
		buf.append("<body>");
		buf.append("<br>");
		buf.append("You have successfully attended a Dimdim web  meeting.");
		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);

		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);
		buf.append("<table>");
		buf.append("<tr><td>");
		buf.append("You saw that it only took a few  clicks to <strong>join</strong> the meeting.&nbsp; Did you know that it only takes a few clicks  to <strong>host</strong> your own web meetings?");
		buf.append(EmailConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");

		buf.append("</tr></td>");
		buf.append(EmailConstants.lineSeparator);
		buf.append("</table>");
		buf.append("<br>");

		buf.append(EmailConstants.lineSeparator);
		buf.append("");
		buf.append("</body></html>");

		return	buf.toString();
	}



}
