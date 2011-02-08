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
 * This tag prints the options list of timezones. Currently selected timezone,
 * if any is available can be passed to this class as 'SelectedTimeZone'
 * attribute on the request.
 * 
 * T
 */
public class SupportedLocalesOptionsListTag	extends	SessionLocaleManagementTag
{
	public	int	doEndTag()
	{
		try
		{
			Locale currentLocale = super.getCurrentLocale();
			if (currentLocale == null)
			{
				currentLocale = LocaleManager.getManager().getDefaultLocale();
			}
			StringBuffer buf = new StringBuffer();
			String currentLocaleId = currentLocale.toString();
			pageContext.getResponse().setLocale(currentLocale);
			
			Vector locales = LocaleManager.getManager().getSupportedLocales();
			int	len = locales.size();
			for (int i=0; i<len; i++)
			{
				Locale locale = (Locale)locales.elementAt(i);
				System.out.println("Current Locale:"+currentLocale);
				System.out.println("Available supported locale:"+locale);
				System.out.println("Available supported locale display name:"+locale.getDisplayName(currentLocale));
				buf.append("<option value=\"");
				buf.append(locale.toString());
				buf.append("\"");
				if (currentLocaleId != null && currentLocaleId.equals(locale.toString()))
				{
					buf.append(" selected=\"true\"");
				}
				buf.append(">");
				buf.append(locale.getDisplayName(currentLocale));
				buf.append("</option>");
				buf.append(ConferenceConsoleConstants.lineSeparator);
			}
			pageContext.getOut().println(buf.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	EVAL_PAGE;
	}
}
