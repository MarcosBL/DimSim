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

package com.dimdim.conference;

import java.util.Map;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class UtilMethods
{
	//	If the user agent is unknown, set os to Linux, browser to ff and version 2.0
	
	private	static	final	String	UNKNOWN_USER_AGENT = "UNKNOWN_USER_AGENT";
	
	public	static	String	findUserAgent(HttpServletRequest servletRequest)
	{
		String	userAgent = null;
		Enumeration headers = servletRequest.getHeaderNames();
		while (headers.hasMoreElements())
		{
			String headerName = (String)headers.nextElement();
			if (headerName.toLowerCase().equals("user-agent"))
			{
				userAgent = servletRequest.getHeader(headerName);
				break;
			}
		}
		if (userAgent == null)
		{
			userAgent = UtilMethods.UNKNOWN_USER_AGENT;
		}
		else
		{
			userAgent = userAgent.toLowerCase();
		}
		return	userAgent;
	}
	public	static	boolean	isPublisherSupportable(String osType, String browserType, String browserVersion)
	{
		boolean	pubSupportable = false;
		if (osType != null && osType.equals(ConferenceConsoleConstants.OS_WINDOWS))
		{
			//	Publisher is only supportable on windows and in ie and firefox.
			if (browserType != null && 
					(browserType.equals(ConferenceConsoleConstants.BROWSER_TYPE_IE) ||
					 browserType.equals(ConferenceConsoleConstants.BROWSER_TYPE_FIREFOX)))
			{
				pubSupportable = true;
			}
		}else if (osType != null && osType.equals(ConferenceConsoleConstants.OS_MAC))
		{
			pubSupportable = true;
		}
		return	pubSupportable;
	}
	public	static	void	setSessionParameters(Map session, String userAgent)
	{
		session.put(ConferenceConsoleConstants.OS_TYPE,UtilMethods.getOsType(userAgent));
		session.put(ConferenceConsoleConstants.BROWSER_TYPE,UtilMethods.getBrowserType(userAgent));
		session.put(ConferenceConsoleConstants.BROWSER_VERSION,UtilMethods.getBrowserVersion(userAgent));
	}
	public static String  getOsType(String userAgent)
	{
		if (null == userAgent)
			return	ConferenceConsoleConstants.OS_LINUX;
		if (userAgent.equals(UtilMethods.UNKNOWN_USER_AGENT))
			return	ConferenceConsoleConstants.OS_LINUX;
		if (userAgent.contains(ConferenceConsoleConstants.OS_WINDOWS))
			return ConferenceConsoleConstants.OS_WINDOWS;
		if (userAgent.contains(ConferenceConsoleConstants.OS_MAC))
			return ConferenceConsoleConstants.OS_MAC;
		if (userAgent.contains(ConferenceConsoleConstants.OS_LINUX))
			return ConferenceConsoleConstants.OS_LINUX;
		if (userAgent.contains(ConferenceConsoleConstants.OS_UNIX))
			return ConferenceConsoleConstants.OS_UNIX;
		
		return ConferenceConsoleConstants.OS_UNKNOWN;
	}
	
	public static String getBrowserVersion(String userAgent)
	{
		if (null == userAgent || userAgent.equals(UtilMethods.UNKNOWN_USER_AGENT))
			return	ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_2;
		
//		int indexSafariWindows = userAgent.indexOf("windows");
		int indexIEBrowserCheckVersion6 = userAgent.indexOf("msie 6");
		int indexIEBrowserCheckVersion7 = userAgent.indexOf("msie 7");
		System.out.println(" inside getBrowserType User agent is ---------"+userAgent+"--------");
		
		boolean is_opera = (userAgent.indexOf("opera") != -1);
		boolean is_firefox = ((userAgent.indexOf("firefox")!=-1) && (userAgent.indexOf("gecko")!=-1));
		boolean is_safari = ((userAgent.indexOf("safari")!=-1)&&(userAgent.indexOf("mac")!=-1))?true:false;
//		System.out.println(is_safari);
		
		if(is_safari)
		{
			if (userAgent.indexOf("safari/522") != -1)
	    	{
		    	return	ConferenceConsoleConstants.BROWSER_VERSION_SAFARI_3;
	    	}
	    	else //if(userAgent.indexOf("safari/419") != -1)
	    	{
	    		return ConferenceConsoleConstants.BROWSER_VERSION_SAFARI_2;
	    	}
		}
		else if (is_firefox)
		{
			 if (userAgent.indexOf("firefox/2") != -1)
	    	{
		    	return ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_2;
	    	}
	    	else if (userAgent.indexOf("firefox/3") != -1)
	    	{
	    		return ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_3;
	    	}	
	    	else if (userAgent.indexOf("iceweasel") != -1)
	    	{
	    		return ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_3;
	    	}	
		    else //if (userAgent.indexOf("firefox/1.5") != -1)
		    {
		    	return ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_15_OR_LOWER;
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
//		    boolean is_safari = ((userAgent.indexOf("safari")!=-1)&&(userAgent.indexOf("mac")!=-1))?true:false;
		    boolean is_khtml  = (is_safari || is_konq);
		    boolean is_ie   = ((iePos!=-1) && (!is_opera) && (!is_khtml));
		    if (is_ie)
		    {
//		    	System.out.println("msie 7: " + userAgent.indexOf("msie 7"));
//		    	System.out.println("msie 6: " + userAgent.indexOf("msie 6"));
		    	if(indexIEBrowserCheckVersion6 > 0 || indexIEBrowserCheckVersion7 > 0)			    	
		    	{
			    	if (userAgent.indexOf("msie 7") != -1)
			    	{
				    	return ConferenceConsoleConstants.BROWSER_VERSION_IE_7;
			    	}
			    	else //if (userAgent.indexOf("msie 6") != -1)
			    	{
			    		return ConferenceConsoleConstants.BROWSER_VERSION_IE_6_OR_LOWER;
			    	}				    		
		    	}
		    	else
		    	{
		    		return ConferenceConsoleConstants.BROWSER_VERSION_IE_6_OR_LOWER;
		    	}
		    }
		    else if(is_opera)
		    {
		    	return userAgent.substring(userAgent.indexOf('/')+1, userAgent.indexOf('('));
		    }
		    	
	    }
		
		return ConferenceConsoleConstants.BROWSER_VERSION_UNKOWN;
	}
	
	public static String getBrowserType(String userAgent)
	{
		if (null == userAgent || userAgent.equals(UtilMethods.UNKNOWN_USER_AGENT))
			return	ConferenceConsoleConstants.BROWSER_TYPE_FIREFOX;
		
		boolean is_opera = (userAgent.indexOf("opera") != -1);
	    boolean is_firefox = (((userAgent.indexOf("firefox")!=-1) || userAgent.indexOf("iceweasel") != -1) && (userAgent.indexOf("gecko")!=-1));
		boolean is_safari = userAgent.indexOf("safari") != -1 ;
		boolean is_ie = userAgent.indexOf("msie") != -1;
		
		
		if(is_safari)
		{
			return ConferenceConsoleConstants.BROWSER_TYPE_SAFARI;
		}
		else if(is_opera)
		{
			return ConferenceConsoleConstants.BROWSER_TYPE_OPERA;
		}
		else if(is_firefox)
		{
			return ConferenceConsoleConstants.BROWSER_TYPE_FIREFOX;
		}
		else if(is_opera)
		{
			return ConferenceConsoleConstants.BROWSER_TYPE_OPERA;
		}
		else if(is_ie)
		{
			return ConferenceConsoleConstants.BROWSER_TYPE_IE;
		}
		else
		{
			return ConferenceConsoleConstants.BROWSER_TYPE_UNKNOWN;
		}
	}
}
