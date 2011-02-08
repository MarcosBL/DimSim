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

import java.text.DateFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.TimeZone;

import javax.servlet.jsp.tagext.TagSupport;
import	javax.servlet.http.HttpServletRequest;
import	javax.servlet.ServletContext;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.UserManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.locale.LocaleManager;

import com.dimdim.util.misc.TimeZoneOption;
import com.dimdim.util.misc.TimeZonesList;

import com.dimdim.util.misc.Emoticon;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This tag prints the options list of emoticons. 
 * 
 * T
 */
public class SupportedLocalesGWTDictionaryTag	extends	SessionLocaleManagementTag
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
			buf.append("<script language=\"javascript\">");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			
			buf.append("var supportedLocales = {");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			
			
			Vector locales = LocaleManager.getManager().getSupportedLocales();
			int	len = locales.size();
			buf.append("num:\"");
			buf.append(len+"");
			buf.append("\",");
			for (int i=0; i<len; i++)
			{
				Locale locale = (Locale)locales.elementAt(i);
				String name = locale.getDisplayName(currentLocale);
				String value = locale.toString();
				
				buf.append("t"+i+"_name:\"");
				buf.append(name);
				buf.append("\",");
				buf.append(ConferenceConsoleConstants.lineSeparator);
				
				buf.append("t"+i+"_value:\"");
				buf.append(value);
				buf.append("\"");
				if (i<(len-1))
				{
					buf.append(",");
				}
				buf.append(ConferenceConsoleConstants.lineSeparator);
			}
			
			
			buf.append("}");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			
			buf.append("</script>");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			
			pageContext.getOut().println(buf.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	EVAL_PAGE;
	}
}
