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

package com.dimdim.conference.action.portal;

import java.util.Locale;

import com.dimdim.conference.ConferenceConsoleConstants;
//import com.dimdim.conference.application.portal.UserInfo;
//import com.dimdim.conference.application.portal.UserRequest;
import com.dimdim.conference.action.check.Connect;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.portal.MeetingSettings;
import com.dimdim.conference.application.portal.PortalInterface;
import com.dimdim.conference.application.portal.PortalServerAdapter;
import com.dimdim.conference.model.IConference;

import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.util.misc.Base64;

import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class StartNewMeetingAction extends PortalAdapterAction
{
	//	Input parameters that will be posted to the action by the portal's
	//	start new meeting form.
	
	protected	String	email;
	protected	String	name;
	protected	String	displayName;
	protected	String	key;
	protected	String	password;
	protected	String	userType = LocaleResourceFile.FREE;
	protected	String	preseterPwd;
	protected	String	attendeePwd = null;
	protected	String	toll;
	protected	String	tollFree;
	protected	String	internToll;
	protected	String	internTollFree;
	protected	String	moderatorPassCode;
	protected	String	attendeePasscode;
	private String userId;
	protected	String	meetingName;
	protected	String	uri;
	
	protected	MeetingSettings		settings;
	
	//	The return url. If the meeting can be started.
	
	protected	String		url = "";
	
	public	StartNewMeetingAction()
	{
		
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		System.out.println("Request referrer:"+this.servletRequest.getHeader("Referer"));
		System.out.println("Received start new meeting from portal:"+email+","+name+","+key+","+displayName+","+preseterPwd+","+attendeePwd+","+settings);
		if (settings == null)
		{
			settings = new MeetingSettings();
		}
		else
		{
			settings.readPortalParameters();
		}
		
		//over-riding number of mics
		settings.setMaxAttendeeAudios(ConferenceConsoleConstants.getMaxAttendeeAudios());
		if (key != null && name != null && email != null)
		{
//			  making the confkey as lower case as this was creating dtp stream problems
		    this.key = this.key.toLowerCase();

			//	ok	to start meeting if the key is not already in use.
		    if(name.length() > 0)
		    {
		    	name = (String)(Base64.decodeToObject(name));
		    }
			if (displayName != null && displayName.length() > 0)
			{
				displayName = (String)(Base64.decodeToObject(displayName));
			}
			
//			name = meetingName;
			
			ConferenceManager confManager = ConferenceManager.getManager();
			IConference conf = confManager.getConferenceReturnNull(this.key);
			
//			((ActiveConference)conf).getConfig().setMeetingName(meetingName);
			
			System.out.println("Received start new meeting from portal:"+email+","+name+","+key+","+displayName+","+preseterPwd+","+"attendeePwd"+","+settings);
			if (conf != null)
			{
//				resultCode = KEY_IN_USE;
//				message = getResourceValue("landing_pages","ui_strings",
//					"start_conference_page.error3", userType);
				
				//	Allow rejoin. Security considerations pending discussion.
				String locale = null;
				String requiredPwd = ((ActiveConference)conf).getPreseterPwd();
//				((ActiveConference)conf).getConfig().setMeetingName(meetingName);
				if (null != requiredPwd && requiredPwd.length() > 0)
				{
					if(null == preseterPwd || preseterPwd.length() == 0)
					{
						//message = getResourceValue("landing_pages","ui_strings","start_conference_page.error7", userType);
						//ret = ERROR;
						this.result = "falseHostKeyRequired";
						super.createResultJsonBuffer(false,310,"Host meeting key required");
						return ret;
					}
					if(!requiredPwd.equals(preseterPwd))
					{
						//message = getResourceValue("landing_pages","ui_strings","start_conference_page.error8", userType);
						//ret = ERROR;
						this.result = "falseHostKeyDoNotMatch";
						super.createResultJsonBuffer(false,310,"Host meeting key do not match");
						return ret;
					}
				}
				this.uri = UserSessionDataManager.getDataManager().
				saveJoinMeetingRequestData(Connect.HOST_REJOIN_ACTION, email, displayName, userId, key, locale, userType, attendeePwd);
				this.url = ConferenceConsoleConstants.getServerAddress();
				if (this.isSecure())
				{
					url = ConferenceConsoleConstants.getServerSecureAddress();
				}
				this.url += "/"+ConferenceConsoleConstants.getWebappName();
//				this.url += "/html/envcheck/connect.action?action=hostrejoin&uri="+uri;
				this.url += "/html/envcheck/connect.action?uri="+uri;
				UserRequest ur = UserSessionDataManager.getDataManager().getUserRequest(uri);
				ur.setPortalCall(true);
				System.out.println("attendee pwd is : " + attendeePwd);
				if(null == attendeePwd || attendeePwd.equalsIgnoreCase(""))
				{
					ur.setPortalMeetingJoinUrl(PortalServerAdapter.getAdapter().getJoinMeetingUrl(key));
				}
				else{
					ur.setPortalMeetingJoinUrl(PortalServerAdapter.getAdapter().getJoinMeetingUrl(key, attendeePwd));
				}
				PortalInterface.getPortalInterface().saveMeetingId(key,meeting_id);
				this.result = checkFireFoxEntityPattern(url);
				super.createResultJsonBuffer(true,200,url);
				//this.setupSuccess(Connect.HOST_REJOIN_ACTION);
			}
			else
			{
				//	Form the start form url and return to be submitted
				//	immediately.
				
				this.url = ConferenceConsoleConstants.getServerAddress();
				if (this.isSecure())
				{
					this.url = ConferenceConsoleConstants.getServerSecureAddress();
				}
//				this.url = this.url+"/"+ConferenceConsoleConstants.getWebappName()+
//					"/html/signin/signin.action?"+
//					"action=host&confKey="+key+"&confName="+name+"&email="+email+
//					"&displayName="+displayName+"&submitFormOnLoad=true";
				
//				UserInfo ui = new UserInfo(email,displayName,name,key);
//				UserRequest  ur = new UserRequest(email,meeting_id,"host",ui);
				
				boolean	featurePpt = true;
				
				this.uri = UserSessionDataManager.getDataManager().saveStartnewMeetingRequestData(
						"host",
						email, displayName, userId, key, name, meeting_id,
						settings.getReturnUrlCS(),
						settings.getPresenterAVCS(),
						settings.getImageQualityCS(),
						settings.getNetworkProfileCS(),
						settings.getMaxAttendeeAudios(),
						settings.getMaxParticipants(),
						settings.getParticipantLimit(),
						settings.getMaxHours(),
						settings.getMaxMins(),
						settings.isWaitingAreaEabled(),
						settings.isParticipantListEnabled(),
						settings.isAssistantEnabled(),
						settings.isFeaturePrivateChat(),
						settings.isFeaturePublicChat(),
						featurePpt,
						settings.isFeaturePublisher(),
						settings.isFeatureWhiteboard(),
						settings.getDefaultURLCS(),
						settings.getHeaderText(),
						settings.getFeedbackEmail(),
						"",
						settings.getLocaleCS(),
						userType,
						settings.getToll(), settings.getTollFree(), settings.getInternTollFree(), 
						settings.getInternToll(), settings.getModeratorPassCode(), settings.getAttendeePasscode(), 
						settings.isDisplayDialInfo(), preseterPwd, attendeePwd,
						settings.isAssignMikeOnJoin(), settings.isHandsFreeOnLoad(), settings.isAllowAttendeeInvites()
						,settings.isFeatureRecording(),
						settings.isFeatureCob(),
						settings.isFeatureDoc(), meetingName);
				System.out.println("presenter pwd is : " + preseterPwd);
				System.out.println("attendee pwd is : " + attendeePwd);
/*				this.url = this.url+"/"+ConferenceConsoleConstants.getWebappName()+
						"/html/envcheck/connect.action?uri="+uri+"&preseterPwd="+preseterPwd+"&attendeePwd="+attendeePwd;
*/
				this.url = this.url+"/"+ConferenceConsoleConstants.getWebappName()+
				"/html/envcheck/connect.action?uri="+uri;
				
				//						"&action=host&meeting_id="+meeting_id+"&email="+email;
				this.result = checkFireFoxEntityPattern(url);
				System.out.println("Returning url:"+url);
				
				UserRequest ur = UserSessionDataManager.getDataManager().getUserRequest(uri);
				ur.setPortalCall(true);
				System.out.println("attendee pwd is : " + attendeePwd);
				if(null == attendeePwd || attendeePwd.equalsIgnoreCase(""))
				{
					ur.setPortalMeetingJoinUrl(PortalServerAdapter.getAdapter().getJoinMeetingUrl(key));
				}
				else{
					ur.setPortalMeetingJoinUrl(PortalServerAdapter.getAdapter().getJoinMeetingUrl(key, attendeePwd));
				}
				
//				settings.setReturnUrl((String)(Base64.decodeToObject(settings.getReturnUrl())));
				
//				PortalInterface.getPortalInterface().saveMeetingSettings(key,settings);
				PortalInterface.getPortalInterface().saveMeetingId(key,meeting_id);
//				PortalInterface.getPortalInterface().saveUserInfo(meeting_id,ui);
//				PortalInterface.getPortalInterface().saveUserRequest(ur);
				
				super.createResultJsonBuffer(true,200,url);
			}
/*			else
			{
				this.result = "false";
				System.out.println("Key in use");
				super.createResultJsonBuffer(false,300,"Key in use");
			}
*/		}
		else
		{
			this.result = "false";
			System.out.println("Invalid data");
			super.createResultJsonBuffer(false,310,"Insufficient data");
		}
		return	ret;
	}
	
	protected	String	checkFireFoxEntityPattern(String s)
	{
		String s2 = s;		
		s2 = s.replaceAll("&amp;","&");
	
		return	s2;
	}

	public MeetingSettings getSettings()
	{
		return settings;
	}
	public void setSettings(MeetingSettings settings)
	{
		this.settings = settings;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getUserType()
	{
	    return userType;
	}
	public void setUserType(String userType)
	{
	    this.userType = userType;
	}
	public String getAttendeePwd() {
		return attendeePwd;
	}
	public void setAttendeePwd(String attendeePwd) {
		this.attendeePwd = attendeePwd;
	}
	public String getPreseterPwd() {
		return preseterPwd;
	}
	public void setPreseterPwd(String preseterPwd) {
		this.preseterPwd = preseterPwd;
	}
	public String getPresenterPwd() {
		return preseterPwd;
	}

	public void setPresenterPwd(String presenterPwd) {
		this.preseterPwd = presenterPwd;
	}
	public String getAttendeePasscode() {
		return attendeePasscode;
	}
	public void setAttendeePasscode(String attendeePasscode) {
		this.attendeePasscode = attendeePasscode;
	}
	public String getAttendeePassCode() {
		return attendeePasscode;
	}
	public void setAttendeePassCode(String attendeePasscode) {
		this.attendeePasscode = attendeePasscode;
	}
	public String getInternToll() {
		return internToll;
	}
	public void setInternToll(String internToll) {
		this.internToll = internToll;
	}
	public String getInternTollFree() {
		return internTollFree;
	}
	public void setInternTollFree(String internTollFree) {
		this.internTollFree = internTollFree;
	}
	public String getModeratorPassCode() {
		return moderatorPassCode;
	}
	public void setModeratorPassCode(String moderatorPassCode) {
		this.moderatorPassCode = moderatorPassCode;
	}
	public String getToll() {
		return toll;
	}
	public void setToll(String toll) {
		this.toll = toll;
	}
	public String getTollFree() {
		return tollFree;
	}
	public void setTollFree(String tollFree) {
		this.tollFree = tollFree;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
}
