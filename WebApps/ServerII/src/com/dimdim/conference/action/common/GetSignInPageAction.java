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

import java.text.DateFormat;
import java.util.Date;
//import java.util.Enumeration;
//import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimdim.locale.LocaleManager;
//import com.dimdim.util.misc.IDGenerator;
import com.dimdim.util.misc.StringGenerator;
import com.dimdim.conference.action.CommonDimDimAction;
//import com.dimdim.conference.application.ChildSession;
//import com.dimdim.conference.application.UserSession;
//import com.dimdim.conference.application.UserSessionManager;
//import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.UtilMethods;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action is generic redirect to the forms page. It is a generic
 * return for all the forms page actions that do not forward the user
 * to the console.
 */
public class GetSignInPageAction	extends	CommonDimDimAction
		implements	ServletRequestAware, ServletResponseAware 
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	protected	String	action;
	
	protected	String	email;
	protected	String	displayName;
	protected	String	confName;
	protected	String	confKey;
	protected	String	asPresenter = "off";
	protected	String	meetNow = "on";
	protected	String	message;
	protected	String	submitFormOnLoad;
	protected	String	lc;
	protected	boolean	publisherEnabled = true;
	
	String osType = null;
	public	GetSignInPageAction()
	{
	}
	/**
	 * Data provided by the user always overrides the data in the session object at
	 * any given time.
	 */
	public	String	execute()	throws	Exception
	{
		String userAgent = this.servletRequest.getHeader("user-agent");
		UtilMethods.setSessionParameters(this.getSession(), userAgent);
		/*
		if (userAgent != null)
		{
			userAgent = userAgent.toLowerCase();
		}
		else
		{
			userAgent = UtilMethods.findUserAgent(this.servletRequest);
		}
		int indexSafariWindows = userAgent.indexOf("windows");
		int indexIEBrowserCheckVersion6 = userAgent.indexOf("msie 6");
		int indexIEBrowserCheckVersion7 = userAgent.indexOf("msie 7");
		System.out.println("User agent is ---------"+userAgent+"--------");
		
		//if(null != osType && osType.length() > 0)
		//{
			if(userAgent.contains(ConferenceConsoleConstants.OS_MAC) || 
					userAgent.contains(ConferenceConsoleConstants.OS_LINUX) || userAgent.contains(ConferenceConsoleConstants.OS_UNIX))
			{
				System.out.println("the os that server knows is "+osType +" hence disabling publisher");
				publisherEnabled = false;
			}
		//}
		
		//	Analyze the user agent and set the browser type.
		String browserType = (String)this.getSession().get(ConferenceConsoleConstants.BROWSER_TYPE);
//		System.out.println("-----------@!#!@#----------");
//		System.out.println(browserType);
//		System.out.println(userAgent.indexOf("firefox"));
//		System.out.println(userAgent.indexOf("gecko"));
//		System.out.println(userAgent.indexOf("KHTML"));
//		System.out.println(userAgent.indexOf("safari"));
//		System.out.println("-----------@!#!@#----------");
		if (browserType == null)
		{
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
		}
		*/
		ActionRedirectData ard = (ActionRedirectData)this.servletRequest.getSession().
			getAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME);
		if (ard == null)
		{
			System.out.println("Action redirect data not found *********** ");
			ard = new ActionRedirectData();
			this.servletRequest.getSession().setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
			this.initializeActionData(ard);
		}
		else
		{
			System.out.println("Action redirect data:" +ard.toString());
			this.refreshActionData(ard);
		}
		this.servletRequest.setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
		ard.setRequestAttributes(this.servletRequest);
		this.servletRequest.getSession().setAttribute("publisher_enabled", String.valueOf(publisherEnabled) );
		setSessionLocale();
		return	SUCCESS;
	}
	/**
	 * This method is intended for use only while creating the action data for the
	 * first time.
	 * 
	 * @param ard
	 */
	protected	void	initializeActionData(ActionRedirectData ard)
	{
		if (action != null)
		{
			//	Setup the default values for host meeting, if not provided. If this
			//	url is coming from another page, then the values may be provided.
			if (this.action.equals(ConferenceConstants.ACTION_HOST_MEETING))
			{
				if (email == null)
				{
					email = "admin@dimdim.com"; // Move to config file
				}
				if (displayName == null)
				{
					displayName = "Admin"; // Move to config file
				}
				if (confName == null)
				{
					DateFormat dateFormater = DateFormat.getDateTimeInstance(DateFormat.SHORT,
							DateFormat.SHORT,Locale.US);
					confName = "Web Meeting "+dateFormater.format(new Date());
				}
				if (confKey == null)
				{
					confKey = GetSignInPageAction.idGen.generateRandomString(7,7);
				}
			}
			this.refreshActionData(ard);
		}
	}
	private void setSessionLocale()
	{
		//this loclae code is set when url comes from portal
		if(null != lc)
		{
			System.out.println("inside GetSigninPageACtion of conf server setting session locale to");
			LocaleManager lm = LocaleManager.getManager();
			Locale locale = lm.getSupportedLocale(lc);
			if(null != locale)
			{
				System.out.println("Changing over to locale:"+locale.toString());
				this.servletRequest.getSession().setAttribute(ConferenceConsoleConstants.USER_LOCALE,locale);
			}
		}
	}
	protected	void	refreshActionData(ActionRedirectData ard)
	{
		if (action != null)	ard.setAction(action);
		if (email != null)	ard.setEmail(email);
		if (message != null)	ard.setMessage(message);
		if (displayName != null)	ard.setDisplayName(displayName);
		if (action != null && (action.equals("host") || action.equals("start")))
		{
			if (ard.getDisplayName() == null || ard.getDisplayName().length() == 0)
			{
				ard.setDisplayName("Admin");
			}
		}
		if (confName != null)	ard.setConfName(confName);
		if (confKey != null)	ard.setConfKey(confKey);
		if (asPresenter != null)	ard.setAsPresenter(asPresenter);
		if (meetNow != null)	ard.setMeetNow(meetNow);
		if (submitFormOnLoad != null)	ard.setSubmitFormOnLoad(submitFormOnLoad);
	
	}
	public HttpServletRequest getServletRequest()
	{
		return this.servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
	public HttpServletResponse getServletResponse()
	{
		return this.servletResponse;
	}
	public void setServletResponse(HttpServletResponse servletResponse)
	{
		this.servletResponse = servletResponse;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getAsPresenter()
	{
		return asPresenter;
	}
	public void setAsPresenter(String asPresenter)
	{
		this.asPresenter = asPresenter;
	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getConfName()
	{
		return confName;
	}
	public void setConfName(String confName)
	{
		this.confName = confName;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getMeetNow()
	{
		return meetNow;
	}
	public void setMeetNow(String meetNow)
	{
		this.meetNow = meetNow;
	}
	public String getSubmitFormOnLoad()
	{
		return submitFormOnLoad;
	}
	public void setSubmitFormOnLoad(String submitFormOnLoad)
	{
		this.submitFormOnLoad = submitFormOnLoad;
	}
	public String getLc() {
		return lc;
	}
	public void setLc(String lc) {
		this.lc = lc;
	}
	public boolean isPublisherEnabled() {
		return publisherEnabled;
	}
	public void setPublisherEnabled(boolean publisherEnabled) {
		this.publisherEnabled = publisherEnabled;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
}
