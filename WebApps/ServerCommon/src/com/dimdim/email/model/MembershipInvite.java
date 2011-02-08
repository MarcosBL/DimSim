package com.dimdim.email.model;

import java.util.Locale;

import com.dimdim.email.application.EmailConstants;
import com.dimdim.locale.ILocaleManager;
import com.dimdim.locale.LocaleResourceFile;

public class MembershipInvite extends PasswordRecovery{
	protected	static	final	String	InvitationHeadingPattern = "CONFERENCE_INVITATION_HEADING";
	protected	static	final	String	InvitationHeading = "CONFERENCE_ORGANIZER has cancelled a Dimdim Web Meeting.";
	protected	static	final	String	InvitationHeading_1 = "CONFERENCE_ORGANIZER";
	protected	static	final	String	OrganizerCommentPattern = "ORGANIZER_COMMENT";
	protected	static	final	String	OrganizerComment = "&nbsp;";
	protected	static	final	String	ScheduledConferenceComment = "The meeting is cancelled ";
		
	
	protected	String		newPassword;
	protected	String		email;
	protected	String		userName;
	protected	String		subject;
	protected	String		emailText;
	protected 	String 		free_paid;
	
	protected	Locale		locale;
	
	//protected	ConferenceInfo	conferenceInfo;
	protected   ILocaleManager localeManager = null;
	
	String role = LocaleResourceFile.FREE;
	
    protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
    //protected	static	final	String	MESSAGE_HEADING_2 = "</font></td></tr>";
//	  	"Personal message from CONFERENCE_ORGANIZER: </font></td></tr>";
    
	public	MembershipInvite(String email, String newPassword,String userName, String free_paid,
			Locale locale, ILocaleManager localeManager)
	{
		//TODO: wrong use of inheritance will have to refactor the code
		super(email, newPassword, userName, locale, localeManager, null);
		this.email = email;
		this.userName = userName;
		this.newPassword = newPassword;
		this.free_paid = free_paid;
		this.locale = locale;
		this.localeManager = localeManager;
		//subject = conferenceInfo.getOrganizer();
		subject = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"subject.invite", role);
		emailText = formatMailText();
		System.out.println("inside membership emial buffer = "+emailText);
	}

	private	String	formatMailText()
	{
		String baseWebappURL = EmailConstants.getServerAddress()+"/"
			+EmailConstants.getWebappName();
		String text = null;
		
		String str = null;
		try
		{
			str = localeManager.getHtmlFileBuffer("email","membership_invite_template",locale, role);
			//str="test message....";
			//str = ConferenceConsoleConstants.getInvitationEmailTemplateBuffer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String comment = CancelationEmail.OrganizerComment;
		
		if (str != null && str.length() >0)
		{
			System.out.println("Memmber invite: From Template");	
			String headingComment = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"forgotpwd_comment", role);
			String str1_1 = str.replace(CancelationEmail.InvitationHeadingPattern,
					CancelationEmail.InvitationHeading_1+" "+ headingComment);
			String str1_2 = str1_1.replace(CancelationEmail.OrganizerCommentPattern,
					comment);
			String str1_3 = str1_2.replaceAll("CONFERENCE_ORGANIZER","");
			String str1 = str1_3.replaceAll("USER_NAME", userName);
			String str2 = str1.replace("USER_PASSWORD",newPassword);			
			
			String str4 = str2.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
			String str5 = str4.replaceAll("FREE_PAID",free_paid);
			String str6 = str5.replaceAll("EMAIL", email);
			text = str6;//.replace("CONFERENCE_KEY",ci.getKey());
		}
		else
		{
			System.out.println("Email Invitation: From Buffer");
			text = fixedFormatBuffer();
			System.out.println("Email Invitation: text ="+text);
		}
		
		/*System.out.println("-----------------------------------------------------------");
		System.out.println(text);
		System.out.println("-----------------------------------------------------------");*/
		return	text;
	}
	
	private	String	fixedFormatBuffer()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("<html>");
		buf.append("<head>");
		buf.append("</head>");
		buf.append("<body>");
		buf.append("<br>");
		buf.append("Your Dimdim web account password has been changed");
		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);
	
		buf.append(EmailConstants.lineSeparator);
		buf.append(EmailConstants.lineSeparator);
		buf.append("<table>");
		buf.append("<tr><td>");
		buf.append("User Name: ");
		buf.append(userName);
		buf.append(EmailConstants.lineSeparator);
		buf.append("</td></tr><tr><td>");
		buf.append("Password: ");
		buf.append(newPassword);
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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

}
