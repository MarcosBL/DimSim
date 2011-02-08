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

package com.dimdim.conference.action.common;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ActionRedirectData
{
	public	static	final	String	SESSION_ATTRIBUTE_NAME	=	"ActionRedirectData";
	
	protected	String	action;
	protected	String	cflag;
	
	protected	String	email;
	protected	String	displayName;
	protected	String	confKey;
	protected	String	confName;
	protected	String	asPresenter;
	protected	String	meetNow;
	protected	String	message;
	protected	String	submitFormOnLoad;
	protected	String	attendeePwd ;
	
	public	ActionRedirectData()
	{
		
	}
	public ActionRedirectData(String action, String cflag, String email,
			String displayName, String confKey, String confName, String asPresenter, String meetNow)
	{
		this.action = action;
		this.email = email;
		this.displayName = displayName;
		this.confKey = confKey;
		this.confName = confName;
		this.cflag = cflag;
		this.asPresenter = asPresenter;
		this.meetNow = meetNow;
	}
	public	void	setRequestAttributes(HttpServletRequest	servletRequest)
	{
		if (action != null)	servletRequest.setAttribute("action",action);
		if (email != null)	servletRequest.setAttribute("email",email);
		if (displayName != null)	servletRequest.setAttribute("displayName",displayName);
		if (confName != null)	servletRequest.setAttribute("confName",confName);
		if (confKey != null)	servletRequest.setAttribute("confKey",confKey);
		if (asPresenter != null)	servletRequest.setAttribute("asPresenter",asPresenter);
		if (meetNow != null)	servletRequest.setAttribute("meetNow",meetNow);
		if (message != null)	servletRequest.setAttribute("asPresenter",message);
		if (submitFormOnLoad != null)	servletRequest.setAttribute("submitFormOnLoad",submitFormOnLoad);
		
		if (attendeePwd != null && attendeePwd.length() > 0)	servletRequest.setAttribute("attendeePwd",attendeePwd);
	}
	public	void	setPropertyValue(String name, String value)
	{
		if (name.equals("submitFormOnLoad"))
		{
			this.submitFormOnLoad = value;
			//	Submit form is a special case.
			if (this.submitFormOnLoad.equals("false"))
			{
//				this.action = "";
				this.email = "";
				this.displayName = "";
				this.confKey = "";
				this.confName = "";
				this.cflag = "";
				this.asPresenter = "";
				this.meetNow = "";
			}
		}
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getAsPresenter()
	{
		return asPresenter;
	}
	public void setAsPresenter(String asPresenter)
	{
		this.asPresenter = asPresenter;
	}
	public String getCflag()
	{
		return cflag;
	}
	public void setCflag(String cflag)
	{
		this.cflag = cflag;
	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
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
	public String getConfName()
	{
		return confName;
	}
	public void setConfName(String confName)
	{
		this.confName = confName;
	}
	public String getMeetNow()
	{
		return meetNow;
	}
	public void setMeetNow(String meetNow)
	{
		this.meetNow = meetNow;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getSubmitFormOnLoad()
	{
		return submitFormOnLoad;
	}
	public void setSubmitFormOnLoad(String submitFormOnLoad)
	{
		this.submitFormOnLoad = submitFormOnLoad;
	}
	public	String	toString()
	{
		return	this.action+"-"+this.cflag+"-"+this.confKey+"-"+
					this.displayName+"-"+this.email+"-"+this.asPresenter+"-"+this.message;
	}
	public String getAttendeePwd() {
		return attendeePwd;
	}
	public void setAttendeePwd(String attendeePwd) {
		this.attendeePwd = attendeePwd;
	}
}
