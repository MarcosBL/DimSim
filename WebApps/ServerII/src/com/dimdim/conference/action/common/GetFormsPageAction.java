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

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.dimdim.util.misc.IDGenerator;
import com.dimdim.util.misc.StringGenerator;
import com.dimdim.conference.action.CommonDimDimAction;
//import com.dimdim.conference.application.ChildSession;
//import com.dimdim.conference.application.UserSession;
//import com.dimdim.conference.application.UserSessionManager;
//import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.model.IConferenceParticipant;
//import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action is generic redirect to the forms page. It is a generic
 * return for all the forms page actions that do not forward the user
 * to the console.
 */
public class GetFormsPageAction	extends	CommonDimDimAction
		implements	ServletRequestAware, ServletResponseAware 
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	protected	String	action;
	
	protected	String	email;
	protected	String	displayName;
	protected	String	confName;
	protected	String	confKey;
	protected	String	asPresenter = "off";
	protected	String	meetNow = "on";
	protected	String	message;
	protected	String	submitFormOnLoad;
	
	public	GetFormsPageAction()
	{
	}
	/**
	 * Data provided by the user always overrides the data in the session object at
	 * any given time.
	 */
	public	String	execute()	throws	Exception
	{
		ActionRedirectData ard = (ActionRedirectData)this.servletRequest.getSession().
			getAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME);
		if (ard == null)
		{
			System.out.println("Action redirect data not found *********** ");
			ard = new ActionRedirectData();
			this.servletRequest.getSession().setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
			this.initializeActionData(ard);
		}
		else
		{
			System.out.println("Action redirect data:" +ard.toString());
			this.refreshActionData(ard);
		}
		this.servletRequest.setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
		ard.setRequestAttributes(this.servletRequest);
		
		return	SUCCESS;
	}
	/**
	 * This method is intended for use only while creating the action data for the
	 * first time.
	 * 
	 * @param ard
	 */
	protected	void	initializeActionData(ActionRedirectData ard)
	{
		if (action != null)
		{
			//	Setup the default values for host meeting, if not provided. If this
			//	url is coming from another page, then the values may be provided.
			if (this.action.equals(ConferenceConstants.ACTION_HOST_MEETING))
			{
				if (email == null)
				{
					email = "admin@dimdim.com"; // Move to config file
				}
				if (displayName == null)
				{
					displayName = "Admin"; // Move to config file
				}
				if (confName == null)
				{
					DateFormat dateFormater = DateFormat.getDateTimeInstance(DateFormat.SHORT,
							DateFormat.SHORT,Locale.US);
					confName = "Web Meeting "+dateFormater.format(new Date());
				}
				if (confKey == null)
				{
					confKey = GetFormsPageAction.idGen.generateRandomString(7,7);
				}
			}
			this.refreshActionData(ard);
		}
	}
	protected	void	refreshActionData(ActionRedirectData ard)
	{
		if (action != null)	ard.setAction(action);
		if (email != null)	ard.setEmail(email);
		if (message != null)	ard.setMessage(message);
		if (displayName != null)	ard.setDisplayName(displayName);
		if (action != null && (action.equals("host") || action.equals("start")))
		{
			if (ard.getDisplayName() == null || ard.getDisplayName().length() == 0)
			{
				ard.setDisplayName("Admin");
			}
		}
		if (confName != null)	ard.setConfName(confName);
		if (confKey != null)	ard.setConfKey(confKey);
		if (asPresenter != null)	ard.setAsPresenter(asPresenter);
		if (meetNow != null)	ard.setMeetNow(meetNow);
		if (submitFormOnLoad != null)	ard.setSubmitFormOnLoad(submitFormOnLoad);
	}
	public HttpServletRequest getServletRequest()
	{
		return this.servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
	public HttpServletResponse getServletResponse()
	{
		return this.servletResponse;
	}
	public void setServletResponse(HttpServletResponse servletResponse)
	{
		this.servletResponse = servletResponse;
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
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getMeetNow()
	{
		return meetNow;
	}
	public void setMeetNow(String meetNow)
	{
		this.meetNow = meetNow;
	}
	public String getSubmitFormOnLoad()
	{
		return submitFormOnLoad;
	}
	public void setSubmitFormOnLoad(String submitFormOnLoad)
	{
		this.submitFormOnLoad = submitFormOnLoad;
	}
}
