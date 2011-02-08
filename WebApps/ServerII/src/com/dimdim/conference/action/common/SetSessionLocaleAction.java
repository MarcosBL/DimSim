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

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.locale.LocaleManager;
import com.opensymphony.webwork.interceptor.ServletRequestAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action switches the locale of the current session. The locale change
 * can be done only from the landing pages ( or front server ). Locale can
 * not be changed during an active conference. This is because of technical
 * limitations of how the data is passed from the server to GWT client.
 */

public class SetSessionLocaleAction	extends	CommonDimDimAction
	implements	ServletRequestAware
{
	protected	HttpServletRequest	servletRequest;
	
	protected	String	localeCode;
	
	public	SetSessionLocaleAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		LocaleManager lm = LocaleManager.getManager();
		if (lm != null && localeCode != null)
		{
			System.out.println("Trying to switch to locale:"+localeCode);
			if (lm.isLocaleSupported(localeCode))
			{
				Locale locale = lm.getSupportedLocale(localeCode);
				System.out.println("Changing over to locale:"+locale.toString());
				this.servletRequest.getSession().
					setAttribute(ConferenceConsoleConstants.USER_LOCALE,locale);
			}
			else
			{
				System.out.println("Locale:"+localeCode+" is not supported");
			}
		}
		else
		{
			System.out.println("Locale Manager:"+lm+", locale code:"+localeCode);
		}
		return	ret;
	}
	public String getLocaleCode()
	{
		return localeCode;
	}
	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}
	public HttpServletRequest getServletRequest()
	{
		return servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
}
