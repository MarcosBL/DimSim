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

package com.dimdim.email.application;

import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.dimdim.email.model.ConfServerNonPortalAttendeeInvite;
import com.dimdim.email.model.ConferenceInfo;
import com.dimdim.email.model.FeedbackEmail;
import com.dimdim.email.model.IConferenceParticipant;
import com.dimdim.email.model.InvitationEmail;
import com.dimdim.email.model.PasswordRecovery;
import com.dimdim.email.model.UploadRecordedFileEmail;
import com.dimdim.locale.ILocaleManager;
import com.dimdim.locale.LocaleResourceFile;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object takes the responsibility of dispatching all the invitation
 * emails. It accepts the invitation object and the list of recipients.
 * It formats the invitation text for each recipient as required and sends
 * out the emails using the MailSender object.
 * 
 * The dispatcher's email auth credentials are read from a resource file.
 */
public class EmailDispatchWorker2	implements	Runnable
{
	protected	String	emailServer;
	protected	String	emailUser;
	protected	String	emailPassword;
	protected	String	emailSender;
	
	protected	int		emailsDispatched = 0;
	
//	protected	MailSender	mailSender;
	protected	Mailer2		mailSender2;
	protected	IErrorReporter	conf;
	protected	IConferenceParticipant	sender;
	protected	Locale		locale;
	protected   	ILocaleManager localeManager = null;
//	protected	SimpleQueue	emailsQueue;
	protected	InvitationEmail	theEmail;
	protected	ConfServerNonPortalAttendeeInvite	theNonPortalAttendeeEmail;
	protected	FeedbackEmail	feedbackEmail;
	protected	PasswordRecovery	pwdRecoveryEmail;
	protected	UploadRecordedFileEmail	uploadRecordedFileEmail;
	String role = LocaleResourceFile.FREE;
	
	public EmailDispatchWorker2(String emailServer, String emailUser,
		String emailPassword, String emailSender, InvitationEmail theEmail,
		IErrorReporter conf, IConferenceParticipant sender, Locale locale, String role, ConferenceInfo conferenceInfo)	throws	Exception
	{
		this.emailServer = emailServer;
		this.emailUser = emailUser;
		this.emailPassword = emailPassword;
		this.emailSender = emailSender;
		this.theEmail = theEmail;
		
		this.conf = conf;
		this.sender = sender;
		this.locale = locale;
		this.localeManager = localeManager;
		this.role = role;
		String target = "";
		if (sender != null)
		{
			target = sender.getId();
		}
		this.mailSender2 = new Mailer2(conf,sender,target);
		this.mailSender2.setConferenceInfo(conferenceInfo);
	}
	public EmailDispatchWorker2(String emailServer, String emailUser,
			String emailPassword, String emailSender, ConfServerNonPortalAttendeeInvite theNonPortalAttendeeEmail,
			Locale locale, String role, ConferenceInfo conferenceInfo)	throws	Exception
		{
		this.emailServer = emailServer;
		this.emailUser = emailUser;
		this.emailPassword = emailPassword;
		this.emailSender = emailSender;
		this.theNonPortalAttendeeEmail = theNonPortalAttendeeEmail;
		this.locale = locale;
		this.role = role;
		this.mailSender2 = new Mailer2(conf,sender,theNonPortalAttendeeEmail.getEmail());
		this.mailSender2.setConferenceInfo(conferenceInfo);
		}
	public EmailDispatchWorker2(String emailServer, String emailUser,
			String emailPassword, String emailSender, FeedbackEmail feedbackEmail,
			IErrorReporter conf, IConferenceParticipant sender, Locale locale, String role)	throws	Exception
		{
			this.emailServer = emailServer;
			this.emailUser = emailUser;
			this.emailPassword = emailPassword;
			this.emailSender = emailSender;
			this.feedbackEmail = feedbackEmail;
			
			this.conf = conf;
			this.sender = sender;
			this.locale = locale;
			this.role = role;
			this.mailSender2 = new Mailer2(conf,sender,"support@dimdim.com");
		}
	
	public EmailDispatchWorker2(String emailServer, String emailUser,
			String emailPassword, String emailSender, PasswordRecovery pwdRecoveryEmail,
			Locale locale, String role)	throws	Exception
		{
			this.emailServer = emailServer;
			this.emailUser = emailUser;
			this.emailPassword = emailPassword;
			this.emailSender = emailSender;
			this.pwdRecoveryEmail = pwdRecoveryEmail;
			this.locale = locale;
			this.role = role;
			this.mailSender2 = new Mailer2(conf,sender,pwdRecoveryEmail.getEmail());
		}
	
	public EmailDispatchWorker2(String emailServer, String emailUser,
			String emailPassword, String emailSender, UploadRecordedFileEmail uploadRecordedFileEmail,
			Locale locale, String role)	throws	Exception
		{
			this.emailServer = emailServer;
			this.emailUser = emailUser;
			this.emailPassword = emailPassword;
			this.emailSender = emailSender;
			this.uploadRecordedFileEmail = uploadRecordedFileEmail;
			this.locale = locale;
			this.role = role;
			this.mailSender2 = new Mailer2(conf,sender,uploadRecordedFileEmail.getEmail());
		}

	public	synchronized	void	addEmail(InvitationEmail email)
	{
//		this.emails.addElement(email);
//		this.emailsQueue.put(email);
	}
	public	void	run()
	{
		InvitationEmail email = this.theEmail;//(JoinInvitationEmail)this.emailsQueue.get();
		try
		{
			if (email != null)
			{
				System.out.println("Email dispatch worker 2 - sending out email:"+email.getSubject());
				sendEmail(email,conf,sender);
			}
			else if (this.feedbackEmail != null)
			{
				sendEmail(this.feedbackEmail,conf,sender);
			}else if (this.pwdRecoveryEmail != null)
			{
				sendEmail(this.pwdRecoveryEmail,conf,sender);
			}else if (this.theNonPortalAttendeeEmail != null){
				sendEmail(this.theNonPortalAttendeeEmail,conf,sender);
			}else if (this.uploadRecordedFileEmail != null){
				sendEmail(this.uploadRecordedFileEmail, conf, sender);
			}			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected	void	sendEmail(FeedbackEmail email,
			IErrorReporter conf, IConferenceParticipant sender)	throws	Exception
	{
		this.mailSender2.sendMessage("feedback@dimdim.com",email.getSender(),email.getSubject(),email.getEmailText(),conf,sender,"support@dimdim.com");
	}
	
	protected	void	sendEmail(PasswordRecovery email,
			IErrorReporter conf, IConferenceParticipant sender)	throws	Exception
	{
		System.out.println("sening recovery mail...."+email.getSubject());
		//System.out.println("email text = "+email.getEmailText());
		boolean flag = this.mailSender2.sendMessage(email.getEmail(),"DIMDIM_MAILS", email.getSubject(),email.getEmailText(),conf,sender,"support@dimdim.com");
		System.out.println("status = "+flag);
	}
	
	protected	void	sendEmail(UploadRecordedFileEmail email,
			IErrorReporter conf, IConferenceParticipant sender)	throws	Exception
	{
		System.out.println("sening uploaded recording url mail...."+email.getSubject());
		//System.out.println("email text = "+email.getEmailText());
		boolean flag = this.mailSender2.sendMessage(email.getEmail(),"DIMDIM_MAILS", email.getSubject(),email.getEmailText(),conf,sender,"support@dimdim.com");
		System.out.println("status = "+flag);
	}
	
	protected	void	sendEmail(ConfServerNonPortalAttendeeInvite email,
			IErrorReporter conf, IConferenceParticipant sender)	throws	Exception
	{
		//System.out.println("sening non portal attendee email...."+email.getSubject());
		//System.out.println("email text = "+email.getEmailText());
		boolean flag = this.mailSender2.sendMessage(email.getEmail(),"DIMDIM_MAILS", email.getSubject(),email.getEmailText(),conf,sender,"support@dimdim.com");
		System.out.println("status = "+flag);
	}
	
	protected	void	sendEmail(InvitationEmail email,
			IErrorReporter conf, IConferenceParticipant sender)	throws	Exception
	{
		if (email.getEmailOrganizer())
		{
			Vector v = new Vector();
			v.addElement(email.getOrganizerEmail());
			String presenterMessage = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"presenter_message_1", role);
			this.sendEmail(email,v, "Presenter", presenterMessage,
//					"Presenter (You can share Presentations/Applications/Desktop and video & audio as well)",
					conf,sender);
		}
		else
		{
			String presenterMessage = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"presenter_message_1", role);
			String attendeeMessage = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"attendee_message_1", role);
			
			//System.out.println("email dispatcher ------------attendees = "+email.getAttendees());
			this.sendEmail(email,email.getAttendees(), "Attendee", attendeeMessage,
//					"Attendee (You can see Presentation/Application/Desktop shared by the presenter, see & hear the Presenter and participate in chat with other participants in the meeting.)",
					conf,sender);
			//System.out.println("email dispatcher ------------Presenter = "+email.getPresenters());
			this.sendEmail(email,email.getPresenters(), "Presenter", presenterMessage,
//					"Presenter (You can share Presentations/Applications/Desktop and video & audio as well)",
					conf,sender);
		}
	}
	protected	void	sendEmail(InvitationEmail email, List recipients, String role,
			String roleAndMessage, IErrorReporter conf, IConferenceParticipant sender)	throws	Exception
	{
		if (recipients == null || recipients.size() == 0)
		{
			return;
		}
		int	size = recipients.size();
		//System.out.println("Number of recipients:"+size);
		for (int i=0; i<size; i++)
		{
			String recipient = (String)recipients.get(i);
			System.out.println("Recipient:"+recipient);
			String text = email.getEmailText();
//			System.out.print("text:"+text);
			text = text.replaceAll("EMAIL",recipient);
			
			int at = recipient.indexOf("@");
			String r2 = recipient;
			if (at > 0)
			{
				r2 = recipient.substring(0,at);
			}
			text = text.replaceAll("DISPLAY_NAME",r2);
			
			text = text.replace("CONFERENCE_ROLE",roleAndMessage);
			if (role.equals("Attendee"))
			{
 				text = text.replace("AS_PRESENTER","false");
 				text = text.replace("JOIN_ACTION","JoinForm.action");
 				String attendeeMessage =localeManager.getResourceKeyValue("email","ui_strings",this.locale,"attendee_message_2", role);
				  text = text.replace("CONFERENCE_FEATURE_MESSAGE", attendeeMessage);
 				//String attendeeMessage= "attendee message "; 
// 					"As an Attendee you need Internet Explorer 6.0/7.0 Or FireFox 1.5/2.0 or higher and flash player to attend Dimdim Web Meeting. "+
//			      "You can obtain flash from here (<a href=\"http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\">"+
//			      "http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash</a>). "+
//				  "No other installation of software is necessary.");
			}
			else if (role.equals("Presenter"))
			{
				text = text.replace("JOIN_ACTION","GetStartConferenceForm.action");
				text = text.replace("AS_PRESENTER","true");
				String presenterMessage = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"presenter_message_2", role);
				text = text.replace("CONFERENCE_FEATURE_MESSAGE", presenterMessage );
				//String presenterMessage = "test presenter message..";
//						"As a "+
//      "Presenter you would need Flash player in addition to Internet Explorer "+
//      "6.0 on Windows OS (XP/2000/2003) in addition to a web cam and microphone. "+
//      "You can obtain flash from here (<a href=\"http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\">"+
//      "http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash</a>)");
			}
//			this.mailSender.sendHTMLMail(recipient,email.getOrganizer(),email.getSubject(),text);
			//System.out.println("the text is = "+text);
			//boolean flag = this.mailSender2.sendMessage(recipient,email.getOrganizer(),email.getSubject(),text,conf,sender,recipient, email.getCalInfo());
			try{
			    boolean flag = this.mailSender2.sendMessage(recipient,email.getOrganizer(),email.getSubject(),text,conf,sender,recipient);
			    System.out.println("email sent status = "+flag+" for the recipient "+recipient);
			}catch(Exception e){
			    System.out.println("Exception occured while sending...print stack trace and continue with others");
			    e.printStackTrace();
			}
		}
	}
	public void setLocaleManager(ILocaleManager localeManager) {
		this.localeManager = localeManager;
	}
	
}
