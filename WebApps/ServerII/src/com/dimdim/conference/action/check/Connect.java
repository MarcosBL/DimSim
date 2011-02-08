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

import java.util.Locale;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.UtilMethods;
import com.dimdim.conference.application.UserManager;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.application.core.UserNotAuthorizedToStartConference;

//import com.dimdim.conference.application.portal.PortalInterface;
//import com.dimdim.conference.application.portal.UserInfo;
//import com.dimdim.conference.application.portal.UserRequest;
//import com.dimdim.conference.application.presentation.PresentationManager;

import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
//import com.dimdim.conference.model.MeetingOptions;
import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;

import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.MeetingSettings;
//import com.dimdim.util.session.UserSessionData;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * This action is a generic action that puts the environment check page
 * in between the connection workflow. 
 */

public class Connect	extends	ConferenceCheckAction
{
	public	static	final	String	HOST_ACTION = "host";
	public	static	final	String	JOIN_ACTION = "join";
	public	static	final	String	RELOAD_ACTION = "reload";
	public	static	final	String	HOST_REJOIN_ACTION = "hostrejoin";
	
	protected	String	action;
	
	protected	String	osType;
	protected	String	browserType;
	protected	String	browserVersion;
	
	protected	String	displayName;
	protected	String	confName = "";
	protected	String	attendees;
	
	protected	String	lobby = "false";
	protected	String	networkProfile = "2";
	protected	String	imageQuality = "medium";
	protected	String	meetingTime = "0";
	protected	String	meetingHours = "1";
	protected	String	meetingMinutes = "0";
	protected	String	maxParticipants = ConferenceConsoleConstants.getMinParticipantsPerConference()+"";
	protected	String	presenterAV = ConferenceConstants.MEETING_AV_TYPE_AUDIO;
	protected	String	maxAttendeeMikes = ConferenceConsoleConstants.getMaxAttendeeAudios()+"";
	protected	String	returnUrl = "";
	
	protected	String	participantListEnabled = "true";
	protected	String	privateChatEnabled = "true";
	protected	String	publicChatEnabled = "true";
	protected	String	screenShareEnabled = "true";
	protected	String	whiteboardEnabled = "true";
	protected	String		preseterPwd = "";
	protected	String		attendeePwd = "";
	protected	String	uri;
	protected	String	lc;
	String collabUrl = "";
	String headerText = "";
	boolean assistantEnabled = true;
	
	
	protected	UserRequest userRequest;
	
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	
	String tollFree = "";
	String toll = "";
	String internTollFree = "";
	String internToll = "";
	String moderatorPassCode = "";
	String attendeePasscode = "";
	boolean dialInfoVisible = true;
	private String userId;
	
	//protected	boolean		videoOnly = false;
	protected	boolean		assignMikeOnJoin = false;
	protected	boolean		handsFreeOnLoad = false;
	protected	boolean		allowAttendeeInvites = true;
	protected 	boolean 	featureRecording	= true;
	protected	boolean		featureCob = true;
	protected	boolean		featureDoc = true;
	protected	String		meetingName = "Welcome to Web Meeting";
	
	public	Connect()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = ERROR;
		UserSession userSession = null;
		
		System.out.println("host key and meeting key are : " + preseterPwd + "---" + attendeePwd);
		
		if (userSession != null)
		{
			this.resultCode = ALREADY_IN_CONFERENCE;
			this.message = "User is already in conference";
		}
		else
		{
			readOsAndBrowserInfo();
			
			confName = this.servletRequest.getParameter("confName");
			if (uri == null)
			{
				uri = this.servletRequest.getParameter("uri");
			}
			if (uri != null)
			{
				//	This request has come through either the portal or the direct form.
				//	Get the user request from cache and set up the parameters.
				System.out.println("Received uri: "+uri);
				if (!this.readUserRequest())
				{
					System.out.println("Invalid uri: ");
					message = "Invalid uri: ";
					return ret;
				}
			}
			
			//making the confkey as lower case as this was creating dtp stream problems
			System.out.println("Action:"+action);
			System.out.println("host key and meeting key are : " + preseterPwd + "---" + attendeePwd);
			if(null != this.confKey)
			{
			    this.confKey = this.confKey.toLowerCase();
			}
			if (null != this.action && this.action.equals(Connect.HOST_ACTION))
			{
				ret = checkStart();
			}
			else if (null != this.action && this.action.equals(Connect.JOIN_ACTION))
			{
				ret = checkJoin();
			}
			else if (null != this.action && this.action.equals(Connect.HOST_REJOIN_ACTION))
			{
				System.out.println("Checking host rejoin");
				ret = checkRejoin();
			}
			else
			{
				
			}
		}
		
		System.out.println("Connect returning:"+ret+", result code:"+resultCode+", message:"+message);
		System.out.println("Connect continue url =: "+url);
		
		return	ret;
	}
	protected	void	readOsAndBrowserInfo()
	{
		if (this.osType == null || this.browserType == null)
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
				userAgent = userAgent.toLowerCase();
				this.osType = UtilMethods.getOsType(userAgent);
				this.browserType = UtilMethods.getBrowserType(userAgent);
				this.browserVersion = UtilMethods.getBrowserVersion(userAgent);
			}
			else
			{
				System.out.println("Error: user agent header not available to detect os and browser type");
			}
		}
	}
	protected	String	checkStart()	throws	Exception
	{
		String	ret = ERROR;
		try
		{
			UserManager.getManager().authenticatePresenter(this.email,this.securityKey);
			
//			PresentationManager.validateKey(this.confKey);
			//	Check currently running conferences.
			ConferenceManager confManager = ConferenceManager.getManager();
			
//			checking for the #of conferences
			if(ConferenceConstants.MAX_MEETINGS_ON_SERVER != -1){
				if(confManager.getNumberOfActiveConferences() >= ConferenceConstants.MAX_MEETINGS_ON_SERVER){
					//if it exceeds the no of permitted meetings then just return error
					message = getResourceValue("landing_pages","ui_strings","start_conference_page.error5", userType);
					ret = ERROR;
					return	ret;
				}
			}
			IConference conf = confManager.getConferenceReturnNull(this.confKey);
			if (conf != null)
			{
				//	Check for rejoin.
				String requiredPwd = ((ActiveConference)conf).getPreseterPwd();
				if (null != requiredPwd && requiredPwd.length() > 0)
				{
					if(null == preseterPwd || preseterPwd.length() == 0)
					{
						message = getResourceValue("landing_pages","ui_strings","start_conference_page.error7", userType);
						ret = ERROR;
						return ret;
					}
					if(!requiredPwd.equals(preseterPwd))
					{
						message = getResourceValue("landing_pages","ui_strings","start_conference_page.error8", userType);
						ret = ERROR;
						return ret;
					}
				}
				this.action = HOST_REJOIN_ACTION;
				ret = this.checkRejoin();
//				resultCode = KEY_IN_USE;
//				message = "* Given meeting key is already associated with a scheduled meeting. Please give a different meeting key";
			}
			else
			{
				ret = SUCCESS;
				if (this.uri == null)
				{
					String meetingId = confKey;
					
					String feedbackEmail = ConferenceConsoleConstants.getResourceKeyValue("feedback_email_address", "feedback@dimdim.com");
					//String locale = "en_US";
					int participantLimit = ConferenceConsoleConstants.getMaxParticipantsPerConference();
					boolean featurePpt = true;
					
					if(displayName == null || displayName.length() == 0)
					{
						displayName = "Host";
					}
					
					if (lc != null && lc.length() > 0)
					{
						Locale l = LocaleManager.getManager().getSupportedLocale(lc);
						if(null != lc)
						{
							this.session.put(ConferenceConsoleConstants.USER_LOCALE, l);
						}
					}
					
					this.uri = UserSessionDataManager.getDataManager().saveStartnewMeetingRequestData(
							Connect.HOST_ACTION,
							email, displayName, userId, confKey, confName, meetingId,
							returnUrl, presenterAV,
							imageQuality, networkProfile,
							Integer.parseInt(maxAttendeeMikes),
							Integer.parseInt(maxParticipants),
							participantLimit,
							Integer.parseInt(meetingHours),
							Integer.parseInt(meetingMinutes),
							Boolean.parseBoolean(this.lobby),
							Boolean.parseBoolean(this.participantListEnabled),
							assistantEnabled,
							Boolean.parseBoolean(privateChatEnabled),
							Boolean.parseBoolean(publicChatEnabled),
							featurePpt,
							Boolean.parseBoolean(screenShareEnabled),
							Boolean.parseBoolean(whiteboardEnabled),
							collabUrl, headerText, feedbackEmail, attendees, lc, 
							userType, tollFree, toll, internTollFree, internToll,
							moderatorPassCode, attendeePasscode, dialInfoVisible, preseterPwd, attendeePwd,
							assignMikeOnJoin, handsFreeOnLoad, allowAttendeeInvites
							,featureRecording, featureCob, featureDoc, meetingName);
					
					this.userRequest = UserSessionDataManager.getDataManager().getUserRequest(this.uri);
				}
				this.setupSuccess();
			}
		}
		catch(UserNotAuthorizedToStartConference unatsc)
		{
			resultCode = USER_NOT_AUTHORIZED;
			message = "User not authorized";
			ret = ERROR;
		}
		catch(NoConferenceByKeyException ncbk)
		{
			ret = SUCCESS;
			this.setupSuccess();
			url = url.trim();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
			message = e.getMessage();
		}
		return	ret;
	}
	protected	String	checkJoin()
	{
		String	ret = SUCCESS;
		try
		{
			//	Check currently running conferences.
			ConferenceManager confManager = ConferenceManager.getManager();
			IConference conf = confManager.getConferenceIfValid(this.confKey);
			if (conf != null)
			{
				if (!((ActiveConference)conf).hasSpaceForUser())
				{
					//System.out.println("inside.... MAX_PARTICIPANTS_PER_MEETING  != -1");
//					if(conf.getParticipants().size() >= ConferenceConstants.MAX_PARTICIPANTS_PER_MEETING){
						//if it exceeds the no of permitted participants then just return error
						//System.out.println("exceeded no of participants....hence returning error...");
						message = getResourceValue("landing_pages","ui_strings","join_conference_page.error1", userType);
						ret = ERROR;
						return ret;
//					}
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
//				checking for the #of allowed participants
				if(ConferenceConstants.MAX_PARTICIPANTS_PER_MEETING != -1){
					if(conf.getParticipants().size() >= ConferenceConstants.MAX_PARTICIPANTS_PER_MEETING){
						//if it exceeds the no of permitted participants then just return error
						message = getResourceValue("landing_pages","ui_strings","join_conference_page.error1", userType);
						ret = ERROR;
						return ret;
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
					if (this.uri == null)
					{
						String locale = "en_US";
						if(displayName == null || displayName.length() == 0)
						{
							displayName = ((ActiveConference)conf).getNewDisplayName();
						}
						this.uri = UserSessionDataManager.getDataManager().
							saveJoinMeetingRequestData(Connect.JOIN_ACTION,
									email, displayName, userId, confKey, locale, userType, attendeePwd);
						
						this.userRequest = UserSessionDataManager.getDataManager().getUserRequest(this.uri);
					}
					if(displayName == null || displayName.length() == 0)
					{
						displayName = ((ActiveConference)conf).getNewDisplayName();
					}

					this.setupSuccess();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
			message = e.getMessage();
		}
		return	ret;
	}
	protected	String	checkRejoin()
	{
		String	ret = SUCCESS;
		try
		{
			//	Check currently running conferences.
			ConferenceManager confManager = ConferenceManager.getManager();
			IConference conf = confManager.getConferenceIfValid(this.confKey);
			if (conf != null)
			{
				if (this.uri == null)
				{
					String locale = "en_US";
					
					this.uri = UserSessionDataManager.getDataManager().
						saveJoinMeetingRequestData(Connect.HOST_REJOIN_ACTION,
								email, displayName, userId, confKey, locale, userType, attendeePwd);
					
					this.userRequest = UserSessionDataManager.getDataManager().getUserRequest(this.uri);
				}
				this.setupSuccess();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
			message = e.getMessage();
		}
		return	ret;
	}
	protected	void	setupSuccess()	throws	Exception
	{
		resultCode = SUCCESS;
		message = "";
		
		System.out.println("Setting up connect success return:");
		
		if (this.action.equals(Connect.HOST_ACTION))
		{
			url = "/"+ConferenceConsoleConstants.getWebappName();
			url += "/CreateAndStartConference.action";
		}
		else if (this.action.equals(Connect.HOST_REJOIN_ACTION))
		{
			url = "/"+ConferenceConsoleConstants.getWebappName();
			url += "/RejoinConference.action";
		}
		else
		{
			url = "/"+ConferenceConsoleConstants.getWebappName();
			url += "/JoinConference.action";
		}
		if(email == null)
		{
			email = "";
		}
		url += "?email=";
		url += email;
		
		url += "&confKey=";
		url += confKey;
		if (this.osType != null && this.osType.length() >0)
		{
			url += "&osType=";
			url += osType;
		}
		if (this.browserType != null && this.browserType.length() >0)
		{
			url += "&browserType=";
			url += browserType;
		}
		if (this.browserVersion != null && this.browserVersion.length() >0)
		{
			url += "&browserVersion=";
			url += browserVersion;
		}
		if (this.securityKey != null && this.securityKey.length() >0)
		{
			url += "&securityKey=";
			url += securityKey;
		}
		url += "&userType=";
		url += userType;
		url += "&uri=";
		url += uri;
		url += "&cflag=";
		url += cflag;
		
//		url += "&attendeePwd=";
//		url += attendeePwd;
//		url += "&preseterPwd=";
//		url += preseterPwd;
		
		if (this.attendees != null && this.attendees.length() >0)
		{
			attendees = attendees.replace("\r\n", ";");
			attendees = attendees.replace('\n', ';');
		}
		
		System.out.println("Continue url: "+url);
		this.servletRequest.setAttribute(UserRequest.MEETING_CONNECT_CONTINUE_URL,url);
		this.userRequest.setUrl(url);
	}
	protected	boolean	readUserRequest()
	{
		boolean	b = false;
		userRequest = UserSessionDataManager.getDataManager().getUserRequest(this.uri);
		if (userRequest != null)
		{
			this.action = userRequest.getAction();
			UserInfo userInfo = userRequest.getUserInfo();
			this.confName = userRequest.getConfName();
			this.confKey = userRequest.getConfKey();
			this.preseterPwd = userRequest.getPreseterPwd();
			this.attendeePwd = userRequest.getAttendeePwd();
			if (userInfo != null)
			{
				this.email = userInfo.getEmail();
				this.displayName = userInfo.getDisplayName();
				this.userId = userInfo.getUserId();
				this.userType = userInfo.getUserType();
			}
			String locale = userRequest.getLocale();
			if (locale != null)
			{
				Locale l = LocaleManager.getManager().getSupportedLocale(locale);
				if(null != locale)
				{
					this.session.put(ConferenceConsoleConstants.USER_LOCALE, l);
				}
			}
			if (userRequest.getAction().equals(Connect.HOST_ACTION))
			{
				MeetingSettings settings = userRequest.getMeetingSettings();
				if (settings != null)
				{
					b = true;
					this.returnUrl = settings.getReturnUrl();
					this.presenterAV = settings.getPresenterAV();
					this.imageQuality = settings.getImageQuality();
					this.networkProfile = settings.getNetworkProfile();
					this.lobby = settings.isLobbyEnabled()+"";
					this.participantListEnabled = settings.isParticipantListEnabled()+"";
					
					this.maxAttendeeMikes = settings.getMaxAttendeeMikes()+"";
					this.maxParticipants = settings.getMaxParticipants()+"";
					this.meetingHours = settings.getMeetingHours()+"";
					this.meetingMinutes = settings.getMeetingMinutes()+"";
					
					this.privateChatEnabled = settings.isFeaturePrivateChat()+"";
					this.publicChatEnabled = settings.isFeaturePublicChat()+"";
					this.screenShareEnabled = settings.isFeaturePublisher()+"";
					this.whiteboardEnabled = settings.isFeatureWhiteboard()+"";
				}
			}
			else
			{
				b = true;
			}
		}
		return b;
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
	public String getImageQuality()
	{
		return imageQuality;
	}
	public void setImageQuality(String imageQuality)
	{
		this.imageQuality = imageQuality;
	}
	public String getLobby()
	{
		return lobby;
	}
	public void setLobby(String lobby)
	{
		this.lobby = lobby;
	}
	public String getMaxParticipants()
	{
		return maxParticipants;
	}
	public void setMaxParticipants(String maxParticipants)
	{
		this.maxParticipants = maxParticipants;
	}
	public String getMeetingTime()
	{
		return meetingTime;
	}
	public void setMeetingTime(String meetingTime)
	{
		this.meetingTime = meetingTime;
	}
	public String getNetworkProfile()
	{
		return networkProfile;
	}
	public void setNetworkProfile(String networkProfile)
	{
		this.networkProfile = networkProfile;
	}
	public String getMeetingHours()
	{
		return meetingHours;
	}
	public void setMeetingHours(String meetingHours)
	{
		this.meetingHours = meetingHours;
	}
	public String getMeetingMinutes()
	{
		return meetingMinutes;
	}
	public void setMeetingMinutes(String meetingMinutes)
	{
		this.meetingMinutes = meetingMinutes;
	}
	public String getPresenterAV()
	{
		return presenterAV;
	}
	public void setPresenterAV(String presenterAV)
	{
		/*if("videoonly".equalsIgnoreCase(presenterAV))
		{
			this.videoOnly = true;
		}else{*/
			this.presenterAV = presenterAV;
		//}
	}
	public String getAttendees()
	{
		return attendees;
	}
	public void setAttendees(String attendees)
	{
		this.attendees = attendees;
	}
	public String getMaxAttendeeMikes()
	{
		return maxAttendeeMikes;
	}
	public void setMaxAttendeeMikes(String maxAttendeeMikes)
	{
		this.maxAttendeeMikes = maxAttendeeMikes;
	}
	public String getReturnUrl()
	{
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl)
	{
		this.returnUrl = returnUrl;
	}
	public String getOsType()
	{
		return osType;
	}
	public void setOsType(String osType)
	{
		this.osType = osType;
	}
	public String getPrivateChatEnabled()
	{
		return privateChatEnabled;
	}
	public void setPrivateChatEnabled(String privateChatEnabled)
	{
		this.privateChatEnabled = privateChatEnabled;
	}
	public String getPublicChatEnabled()
	{
		return publicChatEnabled;
	}
	public void setPublicChatEnabled(String publicChatEnabled)
	{
		this.publicChatEnabled = publicChatEnabled;
	}
	public String getScreenShareEnabled()
	{
		return screenShareEnabled;
	}
	public void setScreenShareEnabled(String screenShareEnabled)
	{
		this.screenShareEnabled = screenShareEnabled;
	}
	public String getWhiteboardEnabled()
	{
		return whiteboardEnabled;
	}
	public void setWhiteboardEnabled(String whiteboardEnabled)
	{
		this.whiteboardEnabled = whiteboardEnabled;
	}
	public String getBrowserType()
	{
		return browserType;
	}
	public void setBrowserType(String browserType)
	{
		this.browserType = browserType;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	public String getLc()
	{
		return lc;
	}
	public void setLc(String lc)
	{
		this.lc = lc;
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
	public boolean isDialInfoVisible() {
		return dialInfoVisible;
	}
	public void setDialInfoVisible(boolean dialInfoVisible) {
		this.dialInfoVisible = dialInfoVisible;
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
	public String getParticipantListEnabled() {
		return participantListEnabled;
	}
	public void setParticipantListEnabled(String participantListEnabled) {
		this.participantListEnabled = participantListEnabled;
	}
	public boolean isAssistantEnabled() {
		return assistantEnabled;
	}
	public void setAssistantEnabled(boolean assistantEnabled) {
		this.assistantEnabled = assistantEnabled;
	}
	public String getCollabUrl() {
		return collabUrl;
	}
	public void setCollabUrl(String collabUrl) {
		this.collabUrl = collabUrl;
	}
	public boolean isAllowAttendeeInvites() {
		return allowAttendeeInvites;
	}
	public void setAllowAttendeeInvites(boolean allowAttendeeInvites) {
		this.allowAttendeeInvites = allowAttendeeInvites;
	}
	public boolean isAssignMikeOnJoin() {
		return assignMikeOnJoin;
	}
	public void setAssignMikeOnJoin(boolean assignMikeOnJoin) {
		this.assignMikeOnJoin = assignMikeOnJoin;
	}
	public boolean isHandsFreeOnLoad() {
		return handsFreeOnLoad;
	}
	public void setHandsFreeOnLoad(boolean handsFreeOnLoad) {
		this.handsFreeOnLoad = handsFreeOnLoad;
	}
	/*public boolean isVideoOnly() {
		return videoOnly;
	}
	public void setVideoOnly(boolean videoOnly) {
		this.videoOnly = videoOnly;
	}*/
	public boolean isFeatureRecording() {
		return featureRecording;
	}
	public void setFeatureRecording(boolean featureRecording) {
		this.featureRecording = featureRecording;
	}
	public boolean isFeatureCob() {
		return featureCob;
	}
	public void setFeatureCob(boolean featureCob) {
		this.featureCob = featureCob;
	}
	public boolean isFeatureDoc() {
		return featureDoc;
	}
	public void setFeatureDoc(boolean featureDoc) {
		this.featureDoc = featureDoc;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	
}
