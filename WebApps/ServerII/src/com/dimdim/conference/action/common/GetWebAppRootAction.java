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

import javax.servlet.http.HttpServletRequest;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.UtilMethods;
import com.opensymphony.webwork.interceptor.ServletRequestAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action is the first one executed for all interactive use. Identifying
 * the browser type is required primarily for active x control access. The
 * server needs to know about the browser type because the html Object tag
 * and embed tag code is different for msie and firefox. Appropriate code
 * section will be added based on the browser type.
 */
public class GetWebAppRootAction	extends	CommonDimDimAction
		implements	ServletRequestAware
{
	protected	HttpServletRequest	servletRequest;
	
	protected	String	browserType;
	protected	String	token;
	
	public	GetWebAppRootAction()
	{
	}
	public	String	execute()	throws	Exception
	{
//		if (this.browserType != null)
//		{
//			this.getSession().put(ConferenceConsoleConstants.BROWSER_TYPE,browserType);
//		}
		
		//	Analyze the user agent and set the browser type.
		browserType = (String)this.getSession().get(ConferenceConsoleConstants.BROWSER_TYPE);
		if (browserType == null)
		{
    		String userAgent = this.servletRequest.getHeader("user-agent");
    		if (userAgent != null)
    		{
    			userAgent = userAgent.toLowerCase();
    		}
    		else
    		{
    			userAgent = UtilMethods.findUserAgent(this.servletRequest);
    		}
			if (userAgent != null)
			{
				UtilMethods.setSessionParameters(getSession(),userAgent);
			}
			else
			{
				System.out.println("ERROR: user agent header not available to interpret os and browser");
			}
			/*
			int indexSafariWindows = userAgent.indexOf("windows");
			int indexIEBrowserCheckVersion6 = userAgent.indexOf("msie 6");
			int indexIEBrowserCheckVersion7 = userAgent.indexOf("msie 7");
			System.out.println("User agent is ---------"+userAgent+"--------");
			System.out.println("-----------@!#!@#----------");
			System.out.println(browserType);
			System.out.println(userAgent.indexOf("firefox"));
			System.out.println(userAgent.indexOf("gecko"));
			System.out.println(userAgent.indexOf("khtml"));
			System.out.println(userAgent.indexOf("safari"));
			System.out.println("-----------@!#!@#----------");
			
		    boolean is_opera = (userAgent.indexOf("opera") != -1);
		    boolean is_firefox = ((userAgent.indexOf("firefox")!=-1) && (userAgent.indexOf("gecko")!=-1));
			boolean is_safari_win = ((userAgent.indexOf("safari") != -1) && (userAgent.indexOf("gecko") != -1));
			boolean is_safari = ((userAgent.indexOf("safari")!=-1)&&(userAgent.indexOf("mac")!=-1))?true:false;
			System.out.println(is_safari);
			System.out.println("Test GetWebAppRootAction Mac safari recognition...");
		    System.out.println("GetWebApRootAction safari windows..." + is_safari_win);

		    System.out.println("valuse of user agent are as follows:" + " " + userAgent.indexOf("safari/419"));
		    System.out.println("valuse of user agent are as follows:" + " " + userAgent.indexOf("safari/522"));
			if(is_safari_win)
			{
//				navigator.appVersion.indexOf("Win")
				System.out.println("Before the version check loop...");
				System.out.println("Check for windows safari... " + userAgent.indexOf("Windows"));
				if(indexSafariWindows > 0)
				{
					System.out.println("Windows safari not supported for now... ");
					return SUCCESS;
				}
				else
				{
					this.getSession().put(ConferenceConsoleConstants.BROWSER_TYPE,
							ConferenceConsoleConstants.BROWSER_TYPE_SAFARI);
					this.getSession().put(ConferenceConsoleConstants.BROWSER_TYPE,
							ConferenceConsoleConstants.BROWSER_TYPE_SAFARI);
				    	if (userAgent.indexOf("safari/522") != -1)
				    	{
					    	System.out.println("Inside the version check loop...");
				    		this.getSession().put(ConferenceConsoleConstants.BROWSER_VERSION,
									ConferenceConsoleConstants.BROWSER_VERSION_SAFARI_3);
				    	}
				    	else if(userAgent.indexOf("safari/419") != -1)
				    	{
				    		System.out.println("Inside the version check loop...");
				    		this.getSession().put(ConferenceConsoleConstants.BROWSER_VERSION,
									ConferenceConsoleConstants.BROWSER_VERSION_SAFARI_2);
				    	}
				    }
			}
			else if (is_firefox)
		    {
				if (userAgent.indexOf("firefox/1.5") != -1 || userAgent.indexOf("firefox/2") != -1){
//				if (userAgent.indexOf("firefox/2") != -1){
					this.getSession().put(ConferenceConsoleConstants.BROWSER_TYPE,
							ConferenceConsoleConstants.BROWSER_TYPE_FIREFOX);
				    	if (userAgent.indexOf("firefox/2") != -1)
				    	{
					    	this.getSession().put(ConferenceConsoleConstants.BROWSER_VERSION,
									ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_2);
				    	}
				    	else if (userAgent.indexOf("firefox/1.5") != -1)
				    	{
				    		System.out.println("FF 1.5 detected..." + userAgent.indexOf("firefox/1.5"));
				    		this.getSession().put(ConferenceConsoleConstants.BROWSER_VERSION,
									ConferenceConsoleConstants.BROWSER_VERSION_FIREFOX_15_OR_LOWER);
				    	}				    
				}
				else
				{
				   		System.out.println("Less than FF 1.5 is not supported");
						return SUCCESS;
					
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
//			    boolean is_safari = ((userAgent.indexOf("safari")!=-1)&&(userAgent.indexOf("mac")!=-1))?true:false;
			    boolean is_khtml  = (is_safari || is_konq);
			    boolean is_ie   = ((iePos!=-1) && (!is_opera) && (!is_khtml));
			    if (is_ie)
			    {
			    	System.out.println("msie 7: " + userAgent.indexOf("msie 7"));
			    	System.out.println("msie 6: " + userAgent.indexOf("msie 6"));
			    	if(indexIEBrowserCheckVersion6 > 0 || indexIEBrowserCheckVersion7 > 0)			    	
			    	{
			    		this.getSession().put(ConferenceConsoleConstants.BROWSER_TYPE,
								ConferenceConsoleConstants.BROWSER_TYPE_IE);
				    	if (userAgent.indexOf("msie 7") != -1)
				    	{
					    	this.getSession().put(ConferenceConsoleConstants.BROWSER_VERSION,
									ConferenceConsoleConstants.BROWSER_VERSION_IE_7);
				    	}
				    	else if (userAgent.indexOf("msie 6") != -1)
				    	{
				    		this.getSession().put(ConferenceConsoleConstants.BROWSER_VERSION,
									ConferenceConsoleConstants.BROWSER_VERSION_IE_7);
				    	}				    		
			    	}	
			    	else
			    	{
			    		System.out.println("Less than IE 6 is not supported");
						return SUCCESS;
			    	}
			    }
		    }
			    */
		}
		return	SUCCESS;
	}
	public String getToken()
	{
		return this.token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public String getBrowserType()
	{
		return browserType;
	}
	public void setBrowserType(String browserType)
	{
		this.browserType = browserType;
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
