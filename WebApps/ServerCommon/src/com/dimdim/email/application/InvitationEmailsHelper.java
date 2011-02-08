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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import com.dimdim.email.model.CancelationEmail;
import com.dimdim.email.model.ConferenceInfo;
import com.dimdim.email.model.EditEmail;
import com.dimdim.email.model.JoinInvitationEmail;
import com.dimdim.email.model.RemovalEmail;
import com.dimdim.email.model.StartInvitationEmail;
import com.dimdim.locale.ILocaleManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class InvitationEmailsHelper
{
	protected	ConferenceInfo	conferenceInfo;
	protected	String			presenterEmails;
	protected	String			attendeeEmails;
	protected	String			message;
	protected   ILocaleManager  localeManager = null;
	
	public static DateFormat formatStartDateUI = new SimpleDateFormat(" MMMM dd, yyyy HH:mm:a z");
	public static DateFormat formatEndDateUI = new SimpleDateFormat("MMMM dd, yyyy");
	
	public InvitationEmailsHelper(ConferenceInfo conferenceInfo,
			String presenterEmails, String attendeeEmails, String message, ILocaleManager localeManager)
	{
		this.conferenceInfo = conferenceInfo;
		this.presenterEmails = presenterEmails;
		this.attendeeEmails = attendeeEmails;
		this.localeManager = localeManager;
		this.message = message;
	}
	
	public void setTime(Calendar calExemption){
	    Date dt = calExemption.getTime();
	    
	    conferenceInfo.setStartTime(formatStartDateUI.format(dt));
	}
	
	public	void	sendInvitationEmails(Locale locale, boolean isScheduled, String role)
	{
		try
		{
			List presenters = readEmailsStr(this.presenterEmails);
			List attendees = readEmailsStr(this.attendeeEmails);
			
			
			/**
			 * Send Organizer an email anyway.
			 */
			if(isScheduled){
				StartInvitationEmail  sie = new StartInvitationEmail(this.conferenceInfo, null, null, "start this meeting", locale, role);
				sie.setEmailOrganizer(true);
				EmailDispatchManager2 emailDispatcherStart = EmailDispatchManager2.getManager();
				emailDispatcherStart.setLocaleManager(localeManager);
				emailDispatcherStart.dispatch(sie,null,null,locale, role, this.conferenceInfo);
			}
			//EmailDispatchManager2.getManager().dispatch(sie,null,null,Locale.US);
//			removing organizer from email so that 2 emails will not be sent
			presenters.remove(conferenceInfo.getOrganizerEmail());
			if(presenters.size() > 0 || attendees.size() > 0){
				JoinInvitationEmail  jie = new JoinInvitationEmail(this.conferenceInfo,
						presenters, attendees, message, isScheduled, locale, localeManager, role);
				//jie.setEmailText("test Message");
				EmailDispatchManager2 emailDispatcher = EmailDispatchManager2.getManager();
				emailDispatcher.setLocaleManager(localeManager);
				emailDispatcher.dispatch(jie,null,null,locale, role, this.conferenceInfo);
			}else{
				System.out.println("Invitation email helper ------- no one to send email ...");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/*
	 * The invitation that has to be sent to attendees while starting a scheduled
	 * meeting 
	 */
	public	void	sendInvitationEmails(Locale locale, String role)
	{
		try
		{
			List presenters = readEmailsStr(this.presenterEmails);
			List attendees = readEmailsStr(this.attendeeEmails);
			//EmailDispatchManager2.getManager().dispatch(sie,null,null,Locale.US);
//			removing organizer from email so that 2 emails will not be sent
			presenters.remove(conferenceInfo.getOrganizerEmail());
			if(presenters.size() > 0 || attendees.size() > 0){
				JoinInvitationEmail  jie = new JoinInvitationEmail(this.conferenceInfo,
						presenters, attendees, message, locale, localeManager, role);
				//jie.setEmailText("test Message");
				EmailDispatchManager2 emailDispatcher = EmailDispatchManager2.getManager();
				emailDispatcher.setLocaleManager(localeManager);
				emailDispatcher.dispatch(jie,null,null,locale, role, this.conferenceInfo);
			}else{
				System.out.println("Invitation email helper ------- no one to send email ...");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public	String	getInvitationEmail(Locale locale, boolean isScheduled, String role)
	{
		try
		{
			List presenters = readEmailsStr(this.presenterEmails);
			List attendees = readEmailsStr(this.attendeeEmails);
			
			
		
//			removing organizer from email so that 2 emails will not be sent
			presenters.remove(conferenceInfo.getOrganizerEmail());
			if(presenters.size() > 0 || attendees.size() > 0){
				JoinInvitationEmail  jie = new JoinInvitationEmail(this.conferenceInfo,
						presenters, attendees, message, isScheduled, locale, localeManager, role);
				return jie.getEmailText();
			}else{
				System.out.println("Invitation email helper ------- no one to send email ...");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "Invitation email helper ------- no one to send email ...";
	}
	
	public	void	sendRemovalEmails(Locale locale, ArrayList removedEmails, String role)
	{
		try
		{
			
			RemovalEmail  removalEmail = new RemovalEmail(this.conferenceInfo,
					removedEmails, message, false, locale, localeManager, role);
			//jie.setEmailText("test Message");
			EmailDispatchManager2 emailDispatcher = EmailDispatchManager2.getManager();
			emailDispatcher.setLocaleManager(localeManager);
			emailDispatcher.dispatch(removalEmail,null,null,locale, role, this.conferenceInfo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public	void	sendInvitationEmails(Locale locale, ArrayList inviteeAttendees, ArrayList inviteePresenters, String role)
	{
		try
		{
			
			JoinInvitationEmail  jie = new JoinInvitationEmail(this.conferenceInfo,
						inviteePresenters, inviteeAttendees, message, true, locale, localeManager, role);
			//jie.setEmailText("test Message");
			EmailDispatchManager2 emailDispatcher = EmailDispatchManager2.getManager();
			emailDispatcher.setLocaleManager(localeManager);
			emailDispatcher.dispatch(jie,null,null,locale, role, this.conferenceInfo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public	void	sendEditEmails(Locale locale, ArrayList inviteeAttendees, ArrayList inviteePresenters, String role)
	{
		try
		{
			
			EditEmail  jie = new EditEmail(this.conferenceInfo,
						inviteePresenters, inviteeAttendees, message, true, locale, localeManager, role);
			//jie.setEmailText("test Message");
			EmailDispatchManager2 emailDispatcher = EmailDispatchManager2.getManager();
			emailDispatcher.setLocaleManager(localeManager);
			emailDispatcher.dispatch(jie,null,null,locale, role, this.conferenceInfo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public	void	sendCancelationEmails( Locale locale, String role)
	{
		try
		{
			List presenters = readEmailsStr(this.presenterEmails);
			List attendees = readEmailsStr(this.attendeeEmails);
			
			if(presenters.size() > 0 || attendees.size() > 0){
				CancelationEmail  cancelEmail = new CancelationEmail(this.conferenceInfo,
						presenters, attendees, message, true, locale, localeManager, role);
				//jie.setEmailText("test Message");
				EmailDispatchManager2 emailDispatcher = EmailDispatchManager2.getManager();
				emailDispatcher.setLocaleManager(localeManager);
				emailDispatcher.dispatch(cancelEmail,null,null,locale, role, this.conferenceInfo);
			}else{
				System.out.println("Invitation email helper ------- no one to send cancleation email ...");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static List	readEmailsStr(String str)
	{
		/*Vector	v = new Vector();
		if (str != null && str.length() >0)
		{
			StringTokenizer parser = new StringTokenizer(str,";");
			while (parser.hasMoreTokens())
			{
				v.addElement(parser.nextToken());
			}
		}
		return	v;*/
		System.out.println("Reading str: -"+str+"-");
		Vector	v = new Vector();
		String tempEmail = null;
		
		if(null == str || str.length() == 0){
			return v;
		}
		//using ',' and '\n' also  as delimeter
		String tempStr = str.replace(',', ';');
		tempStr = tempStr.replace('\n', ';');
		tempStr = tempStr.replace(' ', ';');
		System.out.println("Reading tempStr: -"+tempStr+"-");
		
		if (tempStr != null && (tempStr=tempStr.trim()).length() > 0)
		{
			StringTokenizer parser = new StringTokenizer(tempStr,";");
			while (parser.hasMoreTokens())
			{
				tempEmail =  parser.nextToken().trim();
				if(null != tempEmail && tempEmail.length() > 0){
					v.addElement(tempEmail);
				}
			}
		}
		System.out.println("emails vector = "+v);
		return	v;
	}
}
