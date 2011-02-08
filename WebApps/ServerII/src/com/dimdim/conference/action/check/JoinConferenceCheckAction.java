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

package com.dimdim.conference.action.check;

import java.net.URLEncoder;
import java.util.Date;

import com.dimdim.conference.ConferenceConsoleConstants;
//import com.dimdim.conference.ConferenceConstants;
//import com.dimdim.conference.application.UserManager;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.core.UserNotAuthorizedToStartConference;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
//import com.dimdim.conference.db.ConferenceDB;
//import com.dimdim.conference.db.ConferenceSpec;
import com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.portal.PortalInterface;
import com.dimdim.conference.application.portal.UserInfo;
import com.dimdim.conference.application.portal.UserRequest;
import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.util.session.UserSessionData;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action checks if a new 'meet now' conference can be started.
 */

public class JoinConferenceCheckAction	extends	ConferenceCheckAction
{
	protected	String	displayName = "";
	protected	String	asPresenter;	//	'true' / 'false'
	
	protected	String		attendeePwd = "";
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	protected	String	uri;
	private String userId;
	
	public	JoinConferenceCheckAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = ERROR;
//		System.out.println("As Presenter: "+this.asPresenter);
		UserSession userSession = null;
		
		if (userSession != null)
		{
			this.resultCode = ALREADY_IN_CONFERENCE;
			this.message = "User is already in conference";
		}
		else
		{
			try
			{
				displayName = this.servletRequest.getParameter("displayName");
				
//				UserRequest userRequest = PortalInterface.getPortalInterface().getUserRequest(displayName);
//				if (userRequest != null)
//				{
//					email = userRequest.getEmail();
//					UserInfo userInfo = userRequest.getUserInfo();
//					if (userInfo != null)
//					{
//						this.displayName = userInfo.getDisplayName();
//						this.confKey = userInfo.getConfKey();
//					}
//				}
				
//				making the confkey as lower case as this was creating dtp stream problems
				if(null != this.confKey)
				{
				    this.confKey = this.confKey.toLowerCase();
				}
				//	Check currently running conferences.
				ConferenceManager confManager = ConferenceManager.getManager();

				IConference conf = confManager.getConferenceIfValid(this.confKey);
				if (conf != null)
				{
//					checking for the #of allowed participants
					//System.out.println(" inside ...JoinConferenceCheckAction =  MAX_PARTICIPANTS_PER_MEETING = "+ConferenceConstants.MAX_PARTICIPANTS_PER_MEETING);
					//System.out.println("conf.getParticipants().size() = "+conf.getParticipants().size());
					if (!((ActiveConference)conf).hasSpaceForUser())
					{
						//System.out.println("inside.... MAX_PARTICIPANTS_PER_MEETING  != -1");
//						if(conf.getParticipants().size() >= ConferenceConstants.MAX_PARTICIPANTS_PER_MEETING){
							//if it exceeds the no of permitted participants then just return error
							//System.out.println("exceeded no of participants....hence returning error...");
							message = getResourceValue("landing_pages","ui_strings","join_conference_page.error1", userType);
							ret = ERROR;
							return ret;
//						}
					}else 
					{
						String requiredPwd = ((ActiveConference)conf).getAttendeePwd();
						if (null != requiredPwd && requiredPwd.length() > 0)
						{
							if(null == attendeePwd || attendeePwd.length() == 0)
							{
								message = getResourceValue("landing_pages","ui_strings","join_conference_page.error2", userType);
								resultCode = PWD_REQUIRED;
								ret = ERROR;
								return ret;
							}
							if(!requiredPwd.equals(attendeePwd))
							{
								message = getResourceValue("landing_pages","ui_strings","join_conference_page.error3", userType);
								resultCode = PWD_REQUIRED;
								ret = ERROR;
								return ret;
							}
						}
					}
					
					IConferenceParticipant user = conf.getParticipant(this.email);
					if (user != null)
					{
						resultCode = ERROR;
						message = "User in meeting";
					}
					else
					{
						String locale = "en_US";
						if(displayName == null || displayName.length() == 0)
						{
							//displayName = ((ActiveConference)conf).getNewDisplayName();
							displayName = "";
						}
						this.uri = UserSessionDataManager.getDataManager().
							saveJoinMeetingRequestData("join", email, displayName, userId, confKey, locale, userType, attendeePwd);
						
						this.setupSuccess();
					}
				}
//				else
//				{
//					checkScheduledConference();
//				}
			}
			catch(UserNotAuthorizedToStartConference unatsc)
			{
				unatsc.printStackTrace();
				resultCode = USER_NOT_AUTHORIZED;
				message = "User not authorized";
				ret = ERROR;
			}
			catch(NoConferenceByKeyException ncbk)
			{
//				checkScheduledConference();
			    message = "Unable to find a conference by the key: "+this.confKey+". Please check the key and try again";
			    ret = ERROR;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				ret = ERROR;
				message = e.getMessage();
			}
		}
		return	ret;
	}
//	protected	void	checkScheduledConference()
//	{
//		//	Check if this key refers to a valid scheduled conference.
//		//	If yes, check the conference time.
//		ConferenceDB confDB = ConferenceDB.getDB();
//		ConferenceSpec confSpec = confDB.getConferenceSpec(this.confKey);
//		if (confSpec != null)
//		{
//			ConferenceInfo ci = confSpec.getConferenceInfo();
//			long confStartTime = confSpec.getStartTime().getTime();
//			long currentTime = (new Date()).getTime();
//			if (confStartTime < currentTime)
//			{
//				//	Old conference. How old. 30 minutes is ok.
//				message = "Scheduled conference start time was "+ci.getStartTime();
//				resultCode = NOT_CONFERENCE_TIME;
//			}
//			else
//			{
//				message = "Orgnizer has not started the conference. Scheduled conference start time is "+ci.getStartTime();
//				resultCode = NOT_CONFERENCE_TIME;
//			}
//		}
//		else
//		{
//			resultCode = NO_CONFERENCE_BY_KEY;
//			message = "Unable to find a conference by the key: "+this.confKey+". Please check the key and try again";
//		}
//	}
	protected	void	setupSuccess()	throws	Exception
	{
		resultCode = SUCCESS;
		message = "";
		url = "/"+ConferenceConsoleConstants.getWebappName();
		url += "/html/envcheck/connect.action";
		url += "?action=join&email=";
		url += email;//URLEncoder.encode(this.email,"utf-8");
		if (this.securityKey != null && this.securityKey.length() >0)
		{
			url += "&securityKey=";
			url += securityKey;//URLEncoder.encode(securityKey,"utf-8");
		}
		url += "&confKey=";
		url += confKey;//URLEncoder.encode(confKey,"utf-8");
//		url += "&displayName=";
//		url += displayName;//URLEncoder.encode(displayName,"utf-8");
//		url += "&presenter=";
//		url += asPresenter;//URLEncoder.encode(displayName,"utf-8");
		url += "&uri=";
		url += uri;

		url += "&attendeePwd=";
		url += attendeePwd;
		url += "&cflag=";
		url += cflag;//URLEncoder.encode(displayName,"utf-8");
	}
//	public String getDisplayName()
//	{
//		return displayName;
//	}
//	public void setDisplayName(String displayName)
//	{
//		this.displayName = displayName;
//	}
	public String getAsPresenter()
	{
		return asPresenter;
	}
	public void setAsPresenter(String asPresenter)
	{
		this.asPresenter = asPresenter;
	}
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	public String getAttendeePwd() {
		return attendeePwd;
	}
	public void setAttendeePwd(String attendeePwd) {
		this.attendeePwd = attendeePwd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
