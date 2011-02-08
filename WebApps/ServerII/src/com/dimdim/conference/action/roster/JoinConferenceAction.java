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

package com.dimdim.conference.action.roster;

import java.util.Locale;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.MaximumParticipantsReached;
import com.dimdim.conference.model.UserInConferenceException;
import com.dimdim.conference.model.UserRemovedFromConferenceException;
import com.dimdim.conference.model.ConferenceNotActiveException;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.UserSessionManager;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
//import com.dimdim.conference.application.portal.PortalInterface;
//import com.dimdim.conference.application.portal.UserInfo;
//import com.dimdim.conference.application.portal.UserRequest;
import com.dimdim.conference.action.SignInUserInputAction;
//import com.dimdim.conference.db.ConferenceDB;
//import com.dimdim.conference.db.ConferenceSpec;
import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;
//import com.opensymphony.webwork.interceptor.ServletRequestAware;
//import com.opensymphony.webwork.interceptor.ServletResponseAware;
import com.dimdim.conference.action.common.ActionRedirectData;

import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.UserSessionData;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action first checks if the user is already in a conference. If the user
 * is in conference, error is returned. If not this action adds the user to conference,
 * creates a user session and adds it to the http session on ACTIVE_USER_SESSION key.
 * After the join conference, the welcome to conference page sets up appropriate flags and
 * objects in the http session.
 */

public class JoinConferenceAction	extends	SignInUserInputAction
{
//	private	static	StringGenerator	idGen = new StringGenerator();
	
	protected	String		email = "";
	protected	String		displayName = "";
	protected	String		presenter;	//	true or false
	protected	String		confKey;
	protected	Boolean		secure = new Boolean(false);
	protected	String		sessionLocale = Locale.US.toString();
	
	protected	String	resultCode = ERROR;
	protected	String	url = "";
	private String userId;
	protected	String	message = "Error";
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	String 	pubAvailable = "";
	
	protected	String	uri;
	
	public	JoinConferenceAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		setupActionRedirectData();
		String	ret = ERROR;
		UserSession userSession = (UserSession)UserSessionDataManager.getDataManager().
				getObject(uri, UserSessionData.ACTIVE_USER_SESSION);
		IConferenceParticipant user = null;
		if (userSession != null)
		{
			message =  getResourceValue("landing_pages","ui_strings", "attendee_back_error", userType);
			System.out.println("No user session");
			addFieldError("confKey","* Internal Error");
		}
		else
		{
//			this.servletRequest.getSession().setMaxInactiveInterval(120);
			
			//	User is not in conference. Locate the requested conference,
			//	join and setup the user session.
			try
			{
				displayName = this.servletRequest.getParameter("displayName");
				UserRequest userRequest = UserSessionDataManager.getDataManager().getUserRequest(uri);
				if (userRequest != null)
				{
					UserInfo userInfo = userRequest.getUserInfo();
					if (userInfo != null)
					{
						email = userInfo.getEmail();
						this.displayName = userInfo.getDisplayName();
						this.confKey = userRequest.getConfKey();
						this.userId = userInfo.getUserId();
					}
					else
					{
						System.out.println("Invalid user info");
						confKey = this.servletRequest.getParameter("confKey");
					}
//					PortalInterface.getPortalInterface().clearUserRequest(userRequest.getUri());
				}
				else
				{
					System.out.println("Invalid uri");
					confKey = this.servletRequest.getParameter("confKey");
				}
				
//				System.out.println("User joining: "+email+", "+displayName+", "+confKey);
				ConferenceManager confManager = ConferenceManager.getManager();
//				System.out.println("User joining: "+email+", "+displayName+", "+confKey);
				IConference conf = confManager.getConferenceIfValid(confKey);
//				System.out.println("User joining: "+email+", "+displayName+", "+confKey);
				
//				checking for the #of allowed participants
				if(ConferenceConstants.MAX_PARTICIPANTS_PER_MEETING != -1){
					if(conf.getParticipants().size() >= ConferenceConstants.MAX_PARTICIPANTS_PER_MEETING){
						//if it exceeds the no of permitted participants then just return error
						message = getResourceValue("landing_pages","ui_strings",
							"join_conference_page.error1", userType);
						ret = ERROR;
						return ret;
					}
				}
				
				System.out.println("User: "+email+"- Joining the conference: "+confKey);
				String role = getRole();
				if (role == null)
				{
					//addFieldError("email","* conference requires invitation");
					message = super.getResourceValue("landing_pages","ui_strings",
						"schedule_conference_page.error5", userType);
					addFieldError("email",super.getResourceValue("landing_pages","ui_strings",
						"schedule_conference_page.error5", userType));
				}
				else
				{
					user = conf.addUserToConference(email,displayName, userId, email, role);
					
					userSession = new UserSession(user,conf);
					userSession.setUseSecureConnection(secure);
					Locale locale = (Locale)this.session.get(ConferenceConsoleConstants.USER_LOCALE);
					//if conference has a locale set
					if(null != conf.getConfLocale()){
						locale = conf.getConfLocale();
					}
					if (locale == null)
					{
						locale = LocaleManager.getManager().getDefaultLocale();
					}
					userSession.setSessionLocale(locale);
					userSession.setUri(uri);
					UserSessionDataManager.getDataManager().
							saveObject(uri, UserSessionData.ACTIVE_USER_SESSION,userSession);
					
					//this is setting locale
					this.session.put(ConferenceConsoleConstants.USER_LOCALE, locale);
					((ActiveConference)conf).addParticipantSession(userSession.getUser(),userSession);
					
					/*	Set up the user session for timeout monitoring		*/
				
					String httpSessionKey = this.servletRequest.getSession().getId();
					UserSessionManager.getManager().addUserSession(httpSessionKey,userSession);
					
					ret = SUCCESS;
				}
			}
			catch(NoConferenceByKeyException ncbke)
			{
//				addFieldError("confKey","* Invalid Key");
//				ncbke.printStackTrace();
				message = super.getResourceValue("landing_pages","ui_strings","schedule_conference_page.error6", userType);
				addFieldError("confKey",super.getResourceValue("landing_pages","ui_strings","schedule_conference_page.error6", userType));

				ret = ERROR;
			}
			catch(ConferenceNotActiveException cnae)
			{
//				addFieldError("confKey","* Conference Inactive at present");
//				cnae.printStackTrace();
				message = super.getResourceValue("landing_pages","ui_strings","schedule_conference_page.error7", userType);
				addFieldError("confKey",super.getResourceValue("landing_pages","ui_strings","schedule_conference_page.error7", userType));
				ret = ERROR;
			}
			catch(UserInConferenceException uice)
			{
//				addFieldError("email","* User In Conference");
//				uice.printStackTrace();
				message = super.getResourceValue("landing_pages","ui_strings","schedule_conference_page.error8", userType);
				addFieldError("email",super.getResourceValue("landing_pages","ui_strings","schedule_conference_page.error8", userType));
				ret = ERROR;
			}
			catch(UserRemovedFromConferenceException uice)
			{
//				addFieldError("email","* User In Conference");
//				uice.printStackTrace();
				message = super.getResourceValue("landing_pages","ui_strings","join_conference_page.user_removed_error", userType);
				addFieldError("email",super.getResourceValue("landing_pages","ui_strings","join_conference_page.user_removed_error", userType));
				ret = ERROR;
			}
			catch(MaximumParticipantsReached mpr)
			{
//				mpr.printStackTrace();
				message = super.getResourceValue("landing_pages","ui_strings","join_conference_page.max_users_error", userType);
				addFieldError("email",super.getResourceValue("landing_pages","ui_strings","join_conference_page.max_users_error", userType));
				ret = ERROR;
			}
			catch(Exception e)
			{
//				e.printStackTrace();
				message = e.getMessage();
				addFieldError("confKey","* Internal Error");
				ret = ERROR;
			}
		}
		resultCode = ret;
		if (cflag == null)
		{
			cflag = getFlag();
		}
		return	ret;
	}
	protected	String	getRole()
	{
//		ConferenceDB confDB = ConferenceDB.getDB();
//		String role = ConferenceConstants.ROLE_ATTENDEE;
//		if (this.presenter != null && (this.presenter.equals("true") || this.presenter.equals("on")))
//		{
//			role = ConferenceConstants.ROLE_PRESENTER;
//		}
//		else
//		{
//			ConferenceSpec confSpec = confDB.getConferenceSpec(this.confKey);
//			if (confSpec != null)
//			{
//				if (confSpec.getAttendees().contains(this.email.toLowerCase()))
//				{
//					role = ConferenceConstants.ROLE_ATTENDEE;
//				}
//				else if (confSpec.getPresenters().contains(this.email.toLowerCase()))
//				{
//					role = ConferenceConstants.ROLE_PRESENTER;
//				}
//				else
//				{
//					role = ConferenceConstants.ROLE_ATTENDEE;
//				}
//			}
//		}
		return	ConferenceConstants.ROLE_ATTENDEE;
	}
	protected	void	setupActionRedirectData()
	{
		ActionRedirectData ard = (ActionRedirectData)this.servletRequest.getSession().
			getAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME);
		if (ard == null)
		{
			System.out.println("Action redirect data not found *********** ");
			ard = new ActionRedirectData();
			this.servletRequest.getSession().setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
		}
		else
		{
			System.out.println("Action redirect data:" +ard.toString());
		}
		ard.setEmail(this.email);
		ard.setDisplayName(this.displayName);
		ard.setConfKey(this.confKey);
		ard.setAsPresenter(this.presenter);
		this.servletRequest.setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
		ard.setRequestAttributes(this.servletRequest);
	}
	public String getConfKey()
	{
		return this.confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getDisplayName()
	{
		return this.displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getEmail()
	{
		return this.email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public Boolean getSecure()
	{
		return this.secure;
	}
	public void setSecure(Boolean secure)
	{
		this.secure = secure;
	}
	public String getPresenter()
	{
		return presenter;
	}
	public void setPresenter(String presenter)
	{
		this.presenter = presenter;
	}
	public String getSessionLocale()
	{
		return this.sessionLocale;
	}
	public void setSessionLocale(String sessionLocale)
	{
		this.sessionLocale = sessionLocale;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getResultCode()
	{
		return resultCode;
	}
	public void setResultCode(String resultCode)
	{
		this.resultCode = resultCode;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUserType()
	{
	    return userType;
	}
	public void setUserType(String userType)
	{
	    this.userType = userType;
	}
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	public String getPubAvailable() {
		return pubAvailable;
	}
	public void setPubAvailable(String pubAvailable) {
		this.pubAvailable = pubAvailable;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
