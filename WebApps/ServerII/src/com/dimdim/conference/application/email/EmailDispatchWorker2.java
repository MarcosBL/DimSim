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
import java.util.Vector;
import java.util.Locale;

import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.InvitationEmail;
import com.dimdim.conference.model.FeedbackEmail;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.locale.LocaleManager;

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
	protected	ActiveConference	conf;
	protected	IConferenceParticipant	sender;
	protected	Locale		locale;
	
//	protected	SimpleQueue	emailsQueue;
	protected	InvitationEmail	theEmail;
	protected	FeedbackEmail	feedbackEmail;
	private String userType;
	
	public EmailDispatchWorker2(String emailServer, String emailUser,
		String emailPassword, String emailSender, InvitationEmail theEmail,
		ActiveConference conf, IConferenceParticipant sender, Locale locale, String userType)	throws	Exception
	{
		this.emailServer = emailServer;
		this.emailUser = emailUser;
		this.emailPassword = emailPassword;
		this.emailSender = emailSender;
		this.theEmail = theEmail;
		this.userType = userType;
		this.conf = conf;
		this.sender = sender;
		this.locale = locale;
		String target = "";
		if (sender != null)
		{
			target = sender.getId();
		}
		this.mailSender2 = new Mailer2(conf,sender,target);
	}
	public EmailDispatchWorker2(String emailServer, String emailUser,
			String emailPassword, String emailSender, FeedbackEmail feedbackEmail,
			ActiveConference conf, IConferenceParticipant sender, Locale locale, String userType)	throws	Exception
		{
			this.emailServer = emailServer;
			this.emailUser = emailUser;
			this.emailPassword = emailPassword;
			this.emailSender = emailSender;
			this.feedbackEmail = feedbackEmail;
			this.userType = userType;
			
			this.conf = conf;
			this.sender = sender;
			this.locale = locale;
			this.mailSender2 = new Mailer2(conf,sender,"support@dimdim.com");
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
				sendEmail(email,conf,sender, userType);
			}
			else if (this.feedbackEmail != null)
			{
				sendEmail(this.feedbackEmail,conf,sender);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected	void	sendEmail(FeedbackEmail email,
			ActiveConference conf, IConferenceParticipant sender)	throws	Exception
	{
		String feedbackEmailAddress = null;
		//String feedbackEmailAddress = ConferenceConsoleConstants.getResourceKeyValue("feedback_email_address","feedback@dimdim.com");
		if(null != email.getToEmail() && email.getToEmail().length()> 0)
		{
			feedbackEmailAddress = email.getToEmail();
		}else{
			feedbackEmailAddress = ConferenceConsoleConstants.getResourceKeyValue("feedback_email_address","feedback@dimdim.com");
		}
//		this.mailSender2.sendMessage(feedbackEmailAddress,email.getSender(),email.getSubject(),email.getEmailText(),conf,sender,"support@dimdim.com");
		this.mailSender2.sendMessage(feedbackEmailAddress,email.getSender(),"Feedback from Web Meeting - ",email.getEmailText(),conf,sender,"support@dimdim.com");		
	}
	protected	void	sendEmail(InvitationEmail email,
			ActiveConference conf, IConferenceParticipant sender, String userType)	throws	Exception
	{
		if (email.getEmailOrganizer())
		{
			Vector v = new Vector();
			v.addElement(email.getOrganizerEmail());
			String presenterMessage = LocaleManager.getManager().
					getResourceKeyValue("email","ui_strings",this.locale,"presenter_message_1", userType);
			this.sendEmail(email,v, "Presenter", presenterMessage,
//					"Presenter (You can share Presentations/Applications/Desktop and video & audio as well)",
					conf,sender);
		}
		else
		{
			String presenterMessage = LocaleManager.getManager().
				getResourceKeyValue("email","ui_strings",this.locale,"presenter_message_1", userType);
			String attendeeMessage = LocaleManager.getManager().
				getResourceKeyValue("email","ui_strings",this.locale,"attendee_message_1", userType);
			
			this.sendEmail(email,email.getAttendees(), "Attendee", attendeeMessage,
//					"Attendee (You can see Presentation/Application/Desktop shared by the presenter, see & hear the Presenter and participate in chat with other participants in the meeting.)",
					conf,sender);
			this.sendEmail(email,email.getPresenters(), "Presenter", presenterMessage,
//					"Presenter (You can share Presentations/Applications/Desktop and video & audio as well)",
					conf,sender);
		}
	}
	protected	void	sendEmail(InvitationEmail email, List recipients, String role,
			String roleAndMessage, ActiveConference conf, IConferenceParticipant sender)	throws	Exception
	{
		if (recipients == null || recipients.size() == 0)
		{
			return;
		}
		int	size = recipients.size();
		System.out.println("Number of recipients:"+size);
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
 				String attendeeMessage = LocaleManager.getManager().
					getResourceKeyValue("email","ui_strings",this.locale,"attendee_message_2", userType);
				  text = text.replace("CONFERENCE_FEATURE_MESSAGE", attendeeMessage);
//				  "As an Attendee you need Internet Explorer 6.0/7.0 Or FireFox 1.5/2.0 or higher and flash player to attend Dimdim Web Meeting. "+
//			      "You can obtain flash from here (<a href=\"http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\">"+
//			      "http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash</a>). "+
//				  "No other installation of software is necessary.");
			}
			else if (role.equals("Presenter"))
			{
				text = text.replace("AS_PRESENTER","true");
				String presenterMessage = LocaleManager.getManager().
					getResourceKeyValue("email","ui_strings",this.locale,"presenter_message_2", userType);
				text = text.replace("CONFERENCE_FEATURE_MESSAGE", presenterMessage );
//						"As a "+
//      "Presenter you would need Flash player in addition to Internet Explorer "+
//      "6.0 on Windows OS (XP/2000/2003) in addition to a web cam and microphone. "+
//      "You can obtain flash from here (<a href=\"http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\">"+
//      "http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash</a>)");
			}
//			this.mailSender.sendHTMLMail(recipient,email.getOrganizer(),email.getSubject(),text);
		try{
		    boolean flag = this.mailSender2.sendMessage(recipient,email.getOrganizer(),email.getSubject(),text,conf,sender,recipient);
		    System.out.println("email sent status = "+flag+" for the recipient "+recipient);
		}catch(Exception e){
		    System.out.println("Exception occured while sending...print stack trace and continue with others");
		    e.printStackTrace();
		}
		}
	}
}
