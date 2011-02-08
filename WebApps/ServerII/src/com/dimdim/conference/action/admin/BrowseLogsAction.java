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
public class BrowseLogsAction	extends	CommonDimDimAction
	implements	ServletRequestAware, ServletResponseAware 
{
	protected	String	name = "admin";
	protected	String	password;
	
	protected	AdminSession	adminSession;
	
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	public	BrowseLogsAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String ret = LOGIN;
		ServletContext sc = servletRequest.getSession().getServletContext();
		String path = sc.getRealPath("/");
//		ConferenceConsoleConstants.initServerAddress(this.servletRequest);
//		ConferenceConsoleConstants.setWebappLocalPath(path);
//		ConferenceDB.init(path);
		
		this.adminSession = (AdminSession)this.session.get(ConferenceConsoleConstants.ACTIVE_ADMIN_SESSION);
		if (this.adminSession != null)
		{
			ret = SUCCESS;
		}
		else if (name != null && password != null)
		{
			System.out.println("Checking name:"+name+", password:"+password);
			ConferenceDB db = ConferenceDB.getDB();
			ConferenceAdmin ca = db.getConferenceAdmin(name);
			System.out.println("Found: "+ca);
			if (ca != null && ca.getPassword().equals(password))
			{
				this.adminSession = new AdminSession();
				this.session.put(ConferenceConsoleConstants.ACTIVE_ADMIN_SESSION,adminSession);
				ret = SUCCESS;
			}
		}
		return	ret;
	}
	public AdminSession getAdminSession()
	{
		return adminSession;
	}
	public void setAdminSession(AdminSession adminSession)
	{
		this.adminSession = adminSession;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
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
