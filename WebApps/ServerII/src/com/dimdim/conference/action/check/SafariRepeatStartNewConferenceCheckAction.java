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

package com.dimdim.conference.action.check;

import java.net.URLEncoder;
import java.util.Date;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserManager;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.core.UserNotAuthorizedToStartConference;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.db.ConferenceDB;
import com.dimdim.conference.db.ConferenceSpec;
import com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
//import com.dimdim.conference.model.MeetingOptions;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.portal.PortalInterface;
import com.dimdim.conference.application.portal.UserInfo;
import com.dimdim.conference.application.portal.UserRequest;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action checks if a new 'meet now' conference can be started.
 */

public class SafariRepeatStartNewConferenceCheckAction	extends	ConferenceCheckAction
{
	protected	String	email= "email";
	protected	String	confKey = "confKey";
	protected	String	displayName = "displayName";
	protected	String	confName = "confName";
	
	protected	String	osType = "";
	protected	String	browserType = "";
	
	public	SafariRepeatStartNewConferenceCheckAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		System.out.println("In SafariRepeatStartNewConferenceCheckAction-- checking for the cached meeting options");
//		MeetingOptions meetingOptions = (MeetingOptions)session.get(MeetingOptions.MEETING_OPTIONS_KEY);
//		if (meetingOptions == null)
//		{
//			System.out.println("Meeting options not found, returning -error-");
//			return	"error";
//		}
//		else
//		{
//			this.email = meetingOptions.getEmail();
//			this.confKey = meetingOptions.getConfKey();
//			this.confName = meetingOptions.getConfName();
//			this.displayName = meetingOptions.getDisplayName();
//		}
//		System.out.println("Meeting options found, returning -success_safari-");
		return	"success_safari";
	}
	public String getOsType()
	{
		return osType;
	}
	public void setOsType(String osType)
	{
		this.osType = osType;
	}
	public String getBrowserType()
	{
		return browserType;
	}
	public void setBrowserType(String browserType)
	{
		this.browserType = browserType;
	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getConfName()
	{
		return confName;
	}
	public void setConfName(String confName)
	{
		this.confName = confName;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
}
