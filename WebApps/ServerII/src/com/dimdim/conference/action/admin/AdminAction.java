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
 
package com.dimdim.conference.action.admin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.session.AdminSession;
import com.dimdim.conference.db.ConferenceDB;
import com.dimdim.conference.db.ConferenceAdmin;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class AdminAction	extends	CommonDimDimAction
	implements	ServletRequestAware, ServletResponseAware 
{
	
	protected	String			token;
	protected	AdminSession	adminSession;
	
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	public	AdminAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		if (token != null && (token.equals("logFiles")
				|| token.equals("scheduledConferenceList")
				|| token.equals("activeConferenceList")
				|| token.equals("activeUserSessions")))
		{
			return	token;
		}
		if (token != null)
		{
			return	token;
		}
		return	SUCCESS;
	}
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public AdminSession getAdminSession()
	{
		return adminSession;
	}
	public void setAdminSession(AdminSession adminSession)
	{
		this.adminSession = adminSession;
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
}
