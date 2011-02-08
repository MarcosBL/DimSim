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

package com.dimdim.conference.action.roster;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.application.UserManager;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.email.EmailDispatchManager2;
import com.dimdim.conference.model.JoinInvitationEmail;
import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class SendJoinInvitationAction	extends	ConferenceAction
{
	protected	String		emails = "";
	protected	String		presenters = "";
	protected	String		message = "";
	protected	ConferenceInfo	info;
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();;
	
	public	SendJoinInvitationAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		if ((emails == null || emails.length() == 0 ) &&
				(presenters == null || presenters.length() == 0))
		{
			return	ret;
		}
		System.out.println("*******************************");
		System.out.println("Attendees:"+emails);
		System.out.println("Presenters:"+presenters);
		System.out.println("Message:"+message);
		System.out.println("*******************************");
		IConference conf = this.userSession.getConference();
		
		try
		{
			this.info = conf.getConferenceInfo();
//			this.info.setJoinURL(ConferenceConsoleConstants.getJoinURL(this.info.getKey()));
			this.info.setJoinURL(conf.getJoinUrl());
			
			//	Format an email out of the conference info and the user provided
			//	parameters and send the email out.
			
			List presenterEmails = readEmailsStr(this.presenters);
			List attendeeEmails = readEmailsStr(this.emails);
			JoinInvitationEmail  jie = null;
			if(conf.isDialInfoVisible()){
				jie = new JoinInvitationEmail(this.info, 
						presenterEmails, attendeeEmails, message, false,this.getCurrentLocale(), userType, conf.getInternToll(), conf.getInternTollFree(),
						conf.getToll(), conf.getTollFree(), conf.getModeratorPassCode(), conf.getAttendeePasscode(), conf.getAttendeePwd());
			}
			else
			{
				jie = new JoinInvitationEmail(this.info, 
						presenterEmails, attendeeEmails, message, false,this.getCurrentLocale(), userType, "Not Applicable", "Not Applicable",
						"Not Applicable", "Not Applicable", "Not Applicable", "Not Applicable", conf.getAttendeePwd());
			}
			EmailDispatchManager2.getManager().dispatch(jie,(ActiveConference)conf,
				this.userSession.getUser(),this.getCurrentLocale(), userType);
			
//			System.out.println("*******************************");
//			System.out.println("Attendees:"+emails);
//			System.out.println("Presenters:"+presenters);
//			System.out.println("Message:"+message);
//			System.out.println("*******************************");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		
		return	ret;
	}
	protected	List	readEmailsStr(String str)
	{
		Vector	v = new Vector();
		if (str != null && str.length() >0)
		{
			StringTokenizer parser = new StringTokenizer(str,";");
			while (parser.hasMoreTokens())
			{
				v.addElement(parser.nextToken());
			}
		}
		return	v;
	}
	public ConferenceInfo getInfo()
	{
		return info;
	}
	public void setInfo(ConferenceInfo info)
	{
		this.info = info;
	}
	public String getEmails()
	{
		return emails;
	}
	public void setEmails(String emails) 
	{
		this.emails = emails;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getPresenters()
	{
		return presenters;
	}
	public void setPresenters(String presenters)
	{
		this.presenters = presenters;
	}
	public String getUserType()
	{
	    return userType;
	}
	public void setUserType(String userType)
	{
	    this.userType = userType;
	}
}
