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
 
package com.dimdim.conference.tag;

import java.util.Locale;
import java.util.Vector;

import javax.servlet.jsp.tagext.TagSupport;
import	javax.servlet.http.HttpServletRequest;
import	javax.servlet.ServletContext;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSession;
import com.dimdim.locale.LocaleManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This is the common superclass of the tags that help manage session locale.
 * It simply provides a few common methods.
 */
public abstract	class SessionLocaleManagementTag	extends	TagSupport
{
	/**
	 * This method returns the current locale. The current locale is taken
	 * from the user session object if one is available for the session key
	 * attribute of the request. If not it is taken from the http session.
	 * This method returns null, if the current locale is not set.
	 * 
	 * @return
	 */
	protected	Locale	getCurrentLocale()
	{
		Locale currentLocale = null;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String sessionKey = (String)request.getAttribute("sessionKey");
		if (sessionKey != null)
		{
			UserSession userSession = (UserSession)pageContext.
						getSession().getAttribute(sessionKey);
			if (userSession != null)
			{
				currentLocale = userSession.getSessionLocale();
			}
		}
		if (currentLocale == null)
		{
			currentLocale = (Locale)pageContext.getSession().getAttribute(
						ConferenceConsoleConstants.USER_LOCALE);
		}
		return	currentLocale;
	}
}
