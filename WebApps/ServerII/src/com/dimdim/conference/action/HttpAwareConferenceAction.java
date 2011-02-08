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
 
package com.dimdim.conference.action;

import	java.util.Map;
import	java.util.Locale;
import	javax.servlet.http.HttpServletRequest;
import	javax.servlet.http.HttpServletResponse;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.locale.LocaleManager;
import	com.opensymphony.webwork.interceptor.ServletRequestAware;
import	com.opensymphony.webwork.interceptor.ServletResponseAware;

/**
 * @author Saurav Mohapatra
 * @email Saurav.Mohapatra@communiva.com
 *
 */
public class HttpAwareConferenceAction	extends	ConferenceAction
	implements	ServletRequestAware, ServletResponseAware 
{
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
//	protected	Map		servletRequestParameters;
	
	public	HttpAwareConferenceAction()
	{
	}
	
	public HttpServletRequest getServletRequest()
	{
		return this.servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
		this.setRequestLocale();
//		this.servletRequestParameters = this.servletRequest.getParameterMap();
	}
	public HttpServletResponse getServletResponse()
	{
		return this.servletResponse;
	}
	public void setServletResponse(HttpServletResponse servletResponse)
	{
		this.servletResponse = servletResponse;
	}
	protected	void	setRequestLocale()
	{
		Locale currentLocale = (Locale)this.servletRequest.getSession().getAttribute(ConferenceConsoleConstants.USER_LOCALE);
		LocaleManager lm = LocaleManager.getManager();
		if (currentLocale == null)
		{
			currentLocale = lm.getDefaultLocale();
		}
//		Locale currentLocale = this.getCurrentLocale();
		String localelanguage = currentLocale.getLanguage();
//		System.out.println("TAG Locale Language:"+localelanguage);
		String localeencode = ConferenceConsoleConstants.getLocaleEncode(localelanguage);
//		System.out.println("TAG Locale Encode:"+localeencode);
		try
		{
			this.servletRequest.setCharacterEncoding(localeencode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
