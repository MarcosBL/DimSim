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
	public class UploadRecordedFileEmail
	{
		protected	static	final	String	InvitationHeadingPattern = "CONFERENCE_INVITATION_HEADING";
		protected	static	final	String	InvitationHeading = "CONFERENCE_ORGANIZER has recoreded a Dimdim Web Meeting.";
		protected	static	final	String	InvitationHeading_1 = "CONFERENCE_ORGANIZER";
		protected	static	final	String	OrganizerCommentPattern = "ORGANIZER_COMMENT";
		protected	static	final	String	OrganizerComment = "&nbsp;";
		protected	static	final	String	ScheduledConferenceComment = "The meeting is recorded ";
			
		protected	String		url;
		protected	String		email;
		protected	String		userName;
		protected	String		subject;
		protected	String		emailText;
		protected	String		chatURL;
		protected	String		videoViewURL;
		
		protected	Locale		locale;
		String role = LocaleResourceFile.FREE;
		
		//protected	ConferenceInfo	conferenceInfo;
		protected   ILocaleManager localeManager = null;
		
	    protected	static	final	String	MESSAGE_HEADING_1 = "<tr><td>&nbsp;</td></tr><tr>"+
	    	"<td><font face=\"Verdana, Arial, sans-serif\" size=\"2\" color=\"#333333\">";
	    //protected	static	final	String	MESSAGE_HEADING_2 = "</font></td></tr>";
//		  	"Personal message from CONFERENCE_ORGANIZER: </font></td></tr>";
	    
		public	UploadRecordedFileEmail(String email, String userName, Locale locale, ILocaleManager localeManager, String url, String chatURL, String videoViewURL)
		{
			//this.conferenceInfo = conferenceInfo;
			this.email = email;
			this.locale = locale;
			this.userName = userName;
			this.localeManager = localeManager;
			this.url = url;
			this.chatURL = chatURL;
			this.videoViewURL = videoViewURL;
			//subject = conferenceInfo.getOrganizer();
			subject = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"subject.recording", role);
			emailText = formatMailText();
		}

		private	String	formatMailText()
		{
		    	String baseWebappURL = EmailConstants.getServerAddress()+"/"
			+EmailConstants.getWebappName();
			String text = null;
			String str = null;
			try
			{
				str = localeManager.getHtmlFileBuffer("email","recording_email_template",locale, role);
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
				System.out.println("Email recorded upload url: From Template");	
				String headingComment = localeManager.getResourceKeyValue("email","ui_strings",this.locale,"recording_comment", role);
				String str1_1 = str.replace(CancelationEmail.InvitationHeadingPattern,
						CancelationEmail.InvitationHeading_1+" "+ headingComment);
				String str1_2 = str1_1.replace(CancelationEmail.OrganizerCommentPattern,
						comment);
				String str1_3 = str1_2.replaceAll("CONFERENCE_ORGANIZER","");
				String str1 = str1_3.replaceAll("USER_NAME", userName);

				
				String str4 = str1.replaceAll("BASE_WEBAPP_URL",baseWebappURL);
				String str4_2 = null;
				if(null != chatURL){
					str4_2 = str4.replaceAll("DOWNLOAD_CHAT_URL", "<td colspan='3'>Click here to download <a href='"+chatURL+"'>Chat Transcript</a></td>");
				}
				else
				{
					str4_2 = str4.replaceAll("DOWNLOAD_CHAT_URL", "");
				}
				String str4_1 = null;
				if(null != chatURL)
				{
					str4_1 = str4_2.replaceAll("CHAT_URL", "<td colspan='3'>Click here to view <a href='"+chatURL+"'>Chat Transcript</a></td>");					
				}
				else
				{
					str4_1 = str4_2.replaceAll("CHAT_URL", "");
				}
				
				String str5_1 = null;
				if(null != url){
					str5_1 = str4_1.replaceAll("DOWNLOAD_RECORDED_URL", "<td colspan='3'>Click here to download <a href='"+url+"'>Recording</a></td>");
				}
				else
				{
					str5_1 = str4_1.replaceAll("DOWNLOAD_RECORDED_URL", "");
				}
				String str5 = null;
				if(null != videoViewURL)
				{
				    str5 = str5_1.replaceAll("RECORDED_URL", "<td colspan='3'>Click here to view <a href='"+videoViewURL+"'>Recording</a></td>");
				    text = str5;
				}else{
					str5 = str5_1.replaceAll("RECORDED_URL", "");
				    text = str5;//.replace("CONFERENCE_KEY",ci.getKey());    
				}
				
				
			}
			else
			{
				System.out.println("Email Invitation: From Buffer");
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
			StringBuffer buf = new StringBuffer();
			
			buf.append("<html>");
			buf.append("<head>");
			buf.append("</head>");
			buf.append("<body>");
			buf.append("<br>");
			buf.append("Your Dimdim Web Meeting session is recorded and available to view");
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
			buf.append("Uploaded Recorded URL: ");
			buf.append(url);
			buf.append(EmailConstants.lineSeparator);
			buf.append("</td></tr><tr><td>");
			buf.append("Chat Recorded URL: ");
			buf.append(chatURL);
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

		public String getUserName()
		{
			return userName;
		}

		public void setUserName(String userName)
		{
			this.userName = userName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getChatURL() {
			return chatURL;
		}

		public void setChatURL(String chatURL) {
			this.chatURL = chatURL;
		}

		public String getVideoViewURL() {
			return videoViewURL;
		}

		public void setVideoViewURL(String videoViewURL) {
			this.videoViewURL = videoViewURL;
		}
	

	}

