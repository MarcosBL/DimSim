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

import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.ConferenceAction;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class GetConferenceInfoAction	extends	ConferenceAction
{
	protected	ConferenceInfo	info;
	protected	String		localeDate;
	
	public	GetConferenceInfoAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		
		this.info = conf.getConferenceInfo();
//		this.info.setJoinURL(ConferenceConsoleConstants.getJoinURL(this.info.getKey()));
		this.info.setJoinURL(conf.getJoinUrl());
		
		Locale clientLocale = this.userSession.getSessionLocale();
		if (clientLocale != null)
		{
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL,clientLocale);
			this.localeDate = dateFormat.format(this.info.getStartDate());
		}
		else
		{
			this.localeDate = this.info.getStartDate().toString();
		}
		return	ret;
	}
	public ConferenceInfo getInfo()
	{
		return info;
	}
	public void setInfo(ConferenceInfo info)
	{
		this.info = info;
	}
	public String getLocaleDate()
	{
		return localeDate;
	}
	public void setLocaleDate(String localeDate)
	{
		this.localeDate = localeDate;
	}
}
