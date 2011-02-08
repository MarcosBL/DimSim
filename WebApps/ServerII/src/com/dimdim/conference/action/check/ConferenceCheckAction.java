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

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimdim.util.misc.StringGenerator;

import com.dimdim.conference.action.SignInUserInputAction;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public abstract class ConferenceCheckAction	extends	SignInUserInputAction
{
	protected	static	StringGenerator	idGen = new StringGenerator();
	
	protected	static	String	INVALID_DATA	=	"INVALID_DATA";
	protected	static	String	USER_NOT_AUTHORIZED	=	"USER_NOT_AUTHORIZED";
	protected	static	String	PWD_REQUIRED	=	"PASSWORD_REQUIRED";
	protected	static	String	ALREADY_IN_CONFERENCE	=	"ALREADY_IN_CONFERENCE";
	protected	static	String	PAST_CONFERENCE_TIME	=	"PAST_CONFERENCE_TIME";
	protected	static	String	NOT_CONFERENCE_TIME	=	"NOT_CONFERENCE_TIME";
//	private	static	String	CONFERENCE_START_OK	=	"CONFERENCE_START_OK";
	protected	static	String	NO_CONFERENCE_BY_KEY	=	"NO_CONFERENCE_BY_KEY";
	protected	static	String	KEY_IN_USE	=	"KEY_IN_USE";
	protected	static	String	INVALID_KEY	=	"INVALID_KEY";
	
	protected	String		email;
	protected	String		securityKey;
	protected	String		confKey;
	
	protected	String	resultCode = INVALID_DATA;
	protected	String	url = "";
	protected	String	message = "Invalid data";
	
	public	ConferenceCheckAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		//email = (String)this.servletRequest.getParameter("email");
		//confKey = (String)this.servletRequest.getParameter("confKey");
		
		return	doWork();
	}
	
	protected	abstract	String	doWork()	throws	Exception;
	
	public String getConfKey()
	{
		return this.confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	
	public String getMeetingRoomName()
	{
		return this.confKey;
	}
	public void setMeetingRoomName(String confKey)
	{
		this.confKey = confKey;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getResultCode()
	{
		return resultCode;
	}
	public void setResultCode(String resultCode)
	{
		this.resultCode = resultCode;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getSecurityKey()
	{
		return securityKey;
	}
	public void setSecurityKey(String securityKey)
	{
		this.securityKey = securityKey;
	}
}
