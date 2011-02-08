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

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.db.ConferenceDB;
import com.dimdim.util.misc.StringGenerator;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class CommonGetFormAction	extends	CommonDimDimAction
		implements	ServletRequestAware, ServletResponseAware 
{
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	public	CommonGetFormAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		
		ServletContext sc = servletRequest.getSession().getServletContext();
		String path = sc.getRealPath("/");
		System.out.println("************************************ REAL PATH: "+path);
//		ConferenceConsoleConstants.setWebappLocalPath(path);
//		ConferenceConsoleConstants.initServerAddress(this.servletRequest);
//		ConferenceDB.init(path);
		
		return	ret;
	}
	public HttpServletRequest getServletRequest()
	{
		return servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
	public HttpServletResponse getServletResponse()
	{
		return servletResponse;
	}
	public void setServletResponse(HttpServletResponse servletResponse)
	{
		this.servletResponse = servletResponse;
	}
}
