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

import javax.servlet.http.HttpServletRequest;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSession;
import com.dimdim.locale.LocaleManager;
import com.dimdim.conference.UtilMethods;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This tag sets the session locale only if it is not already set. The locale
 * if set to the client's locale if it is supported. If not it is set to the
 * default locale.
 * 
 * This tag does not add any text to the page.
 */

public class SetSessionLocaleTag	extends	SessionLocaleManagementTag
{
	public	int	doEndTag()
	{
		try
		{
			Locale currentLocale = super.getCurrentLocale();
			if (currentLocale == null)
			{
				//	Check the request's locale against the supported list.
				Locale locale = pageContext.getRequest().getLocale();
				if (LocaleManager.getManager().isLocaleSupported(locale))
				{
					pageContext.getSession().setAttribute(
							ConferenceConsoleConstants.USER_LOCALE,locale);
				}
				else
				{
					locale = LocaleManager.getManager().getDefaultLocale();
					pageContext.getSession().setAttribute(
							ConferenceConsoleConstants.USER_LOCALE,locale);
				}
				currentLocale = locale;
			}
			String localelanguage = currentLocale.getLanguage();
			//System.out.println("TAG Locale Language:"+localelanguage);
			String localeencode = ConferenceConsoleConstants.getLocaleEncode(localelanguage);
			//System.out.println("TAG Locale Encode:"+localeencode);
			//System.out.println("Session locale is:"+currentLocale.toString());
			
			pageContext.getResponse().setLocale(currentLocale);
			pageContext.getResponse().setCharacterEncoding(localeencode);
			pageContext.getResponse().setContentType("text/html; charset="+localeencode);
			
			/**
			 * This tag will also be the first code section run so set the
			 * user browser type from the user-agent header in request.
			 */
			this.setBrowserType();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	EVAL_PAGE;
	}
	protected	void	setBrowserType()
	{
		//	Analyze the user agent and set the browser type.
		String browserType = (String)pageContext.getSession().getAttribute(ConferenceConsoleConstants.BROWSER_TYPE);
		if (browserType == null)
		{
			String userAgent = ((HttpServletRequest)pageContext.getRequest()).getHeader("user-agent");
    		if (userAgent != null)
    		{
    			userAgent = userAgent.toLowerCase();
    		}
    		else
    		{
    			userAgent = UtilMethods.findUserAgent((HttpServletRequest)pageContext.getRequest());
    		}
			if (userAgent != null)
			{
				userAgent = userAgent.toLowerCase();
				pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
						UtilMethods.getBrowserType(userAgent));
				pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
						UtilMethods.getBrowserVersion(userAgent));
				pageContext.getSession().setAttribute(ConferenceConsoleConstants.OS_TYPE,
						UtilMethods.getOsType(userAgent));
			}
			else
			{
				System.out.println("ERROR: user agent header not available to interpret os and browser");
			}
			/*
			String userAgentStr = ((HttpServletRequest)pageContext.getRequest()).getHeader("user-agent");
			String userAgent = userAgentStr;
			if (userAgent != null)
			{
				userAgent = userAgent.toLowerCase();
				int indexSafariWindows = userAgent.indexOf("windows");
				int indexIEBrowserCheckVersion6 = userAgent.indexOf("msie 6");
				int indexIEBrowserCheckVersion7 = userAgent.indexOf("msie 7");
				
				System.out.println("User agent is ---------"+userAgent+"--------");
				System.out.println("msie value we got is: " + indexIEBrowserCheckVersion6);
			    boolean is_opera = (userAgent.indexOf("opera") != -1);
			    boolean is_firefox = ((userAgent.indexOf("firefox")!=-1) && (userAgent.indexOf("gecko")!=-1));
			    System.out.println("-----------@!#!@#----------");
				System.out.println(browserType);
				System.out.println(userAgent.indexOf("firefox"));
				System.out.println(userAgent.indexOf("gecko"));
				System.out.println(userAgent.indexOf("KHTML"));
				System.out.println(userAgent.indexOf("safari"));
				System.out.println("-----------@!#!@#----------");
				boolean is_safari_win = ((userAgent.indexOf("safari") != -1) && (userAgent.indexOf("gecko") != -1));
				boolean is_safari = ((userAgent.indexOf("safari")!=-1)&&(userAgent.indexOf("mac")!=-1))?true:false;
				System.out.println(is_safari);
				System.out.println("Test SetSessionLocaleTag Mac safari recognition...");
			    System.out.println("SetSessionLocaleTag safari windows..." + is_safari_win);
			    
				if(is_safari_win)
				{
					System.out.println("Before the version check loop...");
					System.out.println("Check for windows safari... " + userAgent.indexOf("Windows"));
					if(indexSafariWindows > 0)
					{
						System.out.println("Windows safari not supported for now... ");
						pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
								ConferenceConsoleConstants.BROWSER_TYPE_UNKNOWN);
				    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
								ConferenceConsoleConstants.BROWSER_VERSION_ALL);
					}
					else
					{
						pageContext.setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
								ConferenceConsoleConstants.BROWSER_TYPE_SAFARI);
					    	if (userAgent.indexOf("safari/522") != -1)
					    	{
						    	System.out.println("Inside the version check loop...");
						    	pageContext.setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
										ConferenceConsoleConstants.BROWSER_VERSION_SAFARI_3);
					    	}
					    	else if(userAgent.indexOf("safari/419") != -1)
					    	{
					    		System.out.println("Inside the mac 2.0.x 419 version check loop...");
					    		pageContext.setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
										ConferenceConsoleConstants.BROWSER_VERSION_SAFARI_2);
					    	}					    	
					}	
				}
				else if (is_firefox)
			    {
					if(userAgent.indexOf("firefox/2") != -1 || userAgent.indexOf("firefox/1.5") != -1)					
					{
						pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
								ConferenceConsoleConstants.BROWSER_TYPE_FIREFOX);
					    	if (userAgent.indexOf("firefox/2") != -1)
					    	{
						    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
										ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_2);
					    	}
					    	else if (userAgent.indexOf("firefox/1.5") != -1)
					    	{
					    		System.out.println("FF 1.5 detected..." + userAgent.indexOf("firefox/1.5"));
					    		pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
										ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_15_OR_LOWER);
					    	}						    		
					}
					else
			    	{
			    		System.out.println("Less than FF 1.5 is not supported");
						pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
								ConferenceConsoleConstants.BROWSER_TYPE_UNKNOWN);
				    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
								ConferenceConsoleConstants.BROWSER_VERSION_ALL);
			    	}
			    	
			    }
			    else
			    {
				    int iePos  = userAgent.indexOf("msie");
				    int kqPos   = userAgent.indexOf("konqueror");
				    boolean is_konq = false;
				    if (kqPos != -1)
				    {
				    	is_konq = true;
				    }
//				    boolean is_safari = ((userAgent.indexOf("safari")!=-1)&&(userAgent.indexOf("mac")!=-1))?true:false;
				    boolean is_khtml  = (is_safari || is_konq);
				    boolean is_ie   = ((iePos!=-1) && (!is_opera) && (!is_khtml));
				    if (is_ie)
				    {
				    	System.out.println("msie 7" + userAgent.indexOf("msie 7"));
				    	System.out.println("msie 6" + userAgent.indexOf("msie 6"));
				    	if(indexIEBrowserCheckVersion6 > 0 || indexIEBrowserCheckVersion7 > 0)				    	
				    	{
				    		pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
									ConferenceConsoleConstants.BROWSER_TYPE_IE);
					    	if (userAgent.indexOf("msie 7") != -1)
					    	{
						    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
										ConferenceConsoleConstants.BROWSER_VERSION_IE_7);
					    	}
					    	else if (userAgent.indexOf("msie 6") != -1)
					    	{
					    		pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
										ConferenceConsoleConstants.BROWSER_VERSION_IE_6_OR_LOWER);
					    	}	
				    	}				    	
				    	else
				    	{
				    		System.out.println("Less than IE 6 is not supported");
				    		pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
									ConferenceConsoleConstants.BROWSER_TYPE_UNKNOWN);
					    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
									ConferenceConsoleConstants.BROWSER_VERSION_ALL);
				    	}
				    }
				    else if (is_safari)
				    {
				    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
								ConferenceConsoleConstants.BROWSER_TYPE_SAFARI);
				    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
								ConferenceConsoleConstants.BROWSER_VERSION_ALL);
				    }
				    else
				    {
				    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
								ConferenceConsoleConstants.BROWSER_TYPE_UNKNOWN);
				    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_VERSION,
								ConferenceConsoleConstants.BROWSER_VERSION_ALL);
				    }
			    }
			}
			else
			{
				System.out.println("Null user agent from servlet session. Should never happen");
		    	pageContext.getSession().setAttribute(ConferenceConsoleConstants.BROWSER_TYPE,
						ConferenceConsoleConstants.BROWSER_TYPE_UNKNOWN);
			}
			*/
		}
	}
}
