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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.ConferenceConstants;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSession;
import com.dimdim.locale.LocaleManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This tag adds the information about the current user to the page, so
 * that the console code knows its own id. This tag adds following lines to
 * the page.
 * ----------------------
 * <script langauage='javascript'>
 * 		window.userid = '<user id>'
 * 		window.userroll = 'PRESENTER/ACTIVE_PRESENTER/ATTENDEE'
 * </script>
 * ----------------------
 */
public class SessionLocaleEncodeTag	extends	TagSupport
{
	public	int	doEndTag()
	{

		try
		{
			String localelanguage;
			String localeencode;
			StringBuffer buf = new StringBuffer();
			
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
			localelanguage = currentLocale.getLanguage();
			System.out.println("TAG Locale Language:"+localelanguage);
			localeencode = ConferenceConsoleConstants.getLocaleEncode(localelanguage);
			System.out.println("TAG Locale Encode:"+localeencode);
			
/*			System.out.println("getDisplayCountry():"+currentLocale.getDisplayCountry());
			System.out.println("getCountry():"+currentLocale.getCountry());
			System.out.println("getDisplayLanguage():"+currentLocale.getDisplayLanguage());
			System.out.println("getDisplayName():"+currentLocale.getDisplayName());
			System.out.println("getDisplayVariant():"+currentLocale.getDisplayVariant());
			System.out.println("getISO3Country():"+currentLocale.getISO3Country());			
			System.out.println("getISO3Language():"+currentLocale.getISO3Language());
*/						
			//<meta http-equiv="Content-Type" content="text/html; charset=euc-jp">
			//				buf.append("<option value=\"");

			buf.append("<meta http-equiv=\"");
			buf.append("Content-Type\"");
			buf.append(" content= \"text/html; charset=");
			buf.append(localeencode);
			buf.append("\">");
		
			pageContext.getOut().println(buf.toString());
			
		}
		catch(Exception e)
		{
		}
		return	EVAL_PAGE;
	}
}
