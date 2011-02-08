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
import javax.servlet.http.HttpServletResponse;

import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserSessionDataManager;
import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.UtilMethods;
import com.dimdim.data.application.UIDataManager;
import com.dimdim.locale.LocaleResourceFile;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;
import com.dimdim.util.session.UserSessionData;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class GetReloadedConsoleAction	extends	CommonDimDimAction
		implements	ServletRequestAware, ServletResponseAware 
{
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	protected	String	sessionKey;
	protected	String	uri;
	protected	String 	pubAvailable = "true";
	protected	String	reloadConsole = "true";
	
	public	GetReloadedConsoleAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		System.out.println("In GetReloadedConsoleAction -");
		String	ret = LOGIN;
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
		UserSession userSession = (UserSession)UserSessionDataManager.getDataManager().
			getObject(uri, UserSessionData.ACTIVE_USER_SESSION);
		if (userSession != null)
		{
			IConference conf = userSession.getConference();
			if (conf != null)
			{
//				if (!userSession.isConsoleLoaded())
//				{
//					sessionKey = GetConsolePageAction.idGen.generate();
//					userSession.setSessionKey(sessionKey);
					sessionKey = userSession.getSessionKey();
					session.put(sessionKey,userSession);
//					session.remove(ConferenceConsoleConstants.ACTIVE_USER_SESSION);
					
//					this.servletRequest.getSession().setMaxInactiveInterval(ConferenceConsoleConstants.getAttendeeSessionTimeout());
					
					userSession.setConsoleLoaded(true);
					this.servletRequest.setAttribute("sessionKey",sessionKey);
					this.servletRequest.setAttribute("publisher_enabled", String.valueOf(conf.isPublisherEnabled()) );
					//this action is assuming that publisher is always available
					this.servletRequest.setAttribute("pubAvailable","true");
					System.out.println("Setting up session : -"+sessionKey);
					ret = SUCCESS;
					
					
					setupUserInfo(sessionKey, userSession);
					/******* WARNING: Testing only  ********/
//					ret = this.startChildSession(userSession);
//				}
//				else
//				{
//					System.out.println("Console is already loaded");
//				}
			}
			else
			{
				System.out.println("User session does not have IConference");
			}
		}
		else
		{
			System.out.println("User session as ACTIVE_USER_SESSION is null");
		}
		System.out.println("In GetReloadedConsoleAction : Returning -"+ret);
		return	ret;
	}
	private	void	setupUserInfo(String sessionKey, UserSession userSession)
	{
		String browserType = (String)session.get(ConferenceConsoleConstants.BROWSER_TYPE);
		String browserVersion = (String)session.get(ConferenceConsoleConstants.BROWSER_VERSION);
		
		String userInfo = userSession.getUserInfo(sessionKey,
				this.servletRequest.getLocale(),
				browserType, browserVersion,
				servletRequest.getRequestURL().toString().startsWith("htts"));
		String	dataCacheId = userSession.getDataCacheId();
//		session.put("user_info", userInfo);
		UIDataManager.getUIDataManager().addSessionDataBuffer("user_info"+dataCacheId, userInfo);
		this.servletRequest.setAttribute("dataCacheId", dataCacheId);
		
//		setting up uer type which will be used for resource bundles 
		//	TODO - This may become an issue. The user type needs to be picked up from the old request.
		UserRequest userRequest = UserSessionDataManager.getDataManager().getUserRequest(userSession.getUri());
		this.servletRequest.setAttribute("userType",LocaleResourceFile.FREE);
		if (userRequest != null)
		{
			UserInfo userInfoTemp = userRequest.getUserInfo();
			if ( userInfoTemp != null && 
				LocaleResourceFile.PREMIUM.equalsIgnoreCase(userInfoTemp.getUserType()) )
			{
			    this.servletRequest.setAttribute("userType",LocaleResourceFile.PREMIUM);
			}
		}
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
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	public String getPubAvailable()
	{
		return pubAvailable;
	}
	public void setPubAvailable(String pubAvailable)
	{
		this.pubAvailable = pubAvailable;
	}
	public String getReloadConsole()
	{
		return reloadConsole;
	}
	public void setReloadConsole(String reloadConsole)
	{
		this.reloadConsole = reloadConsole;
	}
}
