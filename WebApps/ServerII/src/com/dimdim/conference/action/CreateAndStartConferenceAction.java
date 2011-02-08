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

package com.dimdim.conference.action;

import java.util.Locale;
//import java.util.Vector;

//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import com.dimdim.util.misc.StringGenerator;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.UtilMethods;
import com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.model.IConferenceParticipant;
//import com.dimdim.conference.model.Presentation;
import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.application.ChildSession;
import com.dimdim.conference.application.UserManager;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.UserSessionManager;
import com.dimdim.conference.application.email.InvitationEmailsHelper;
import com.dimdim.conference.application.presentation.DefaultPresentationsManager;
import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.KeyInUseException;
import com.dimdim.conference.application.core.UserNotAuthorizedToStartConference;
//import com.dimdim.conference.db.ConferenceDB;
//import com.opensymphony.webwork.interceptor.ServletRequestAware;
//import com.opensymphony.webwork.interceptor.ServletResponseAware;

//import com.dimdim.conference.application.portal.UserInfo;
//import com.dimdim.conference.application.portal.UserRequest;
//import com.dimdim.conference.application.portal.MeetingSettings;
//import com.dimdim.conference.application.portal.PortalInterface;
//import com.dimdim.conference.model.MeetingOptions;

import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.MeetingSettings;
import com.dimdim.util.session.UserSessionData;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CreateAndStartConferenceAction	extends	SignInUserInputAction
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	//	Login parameters.
	
	protected	String		email = "";
	protected	String		displayName = "";
	protected	String		securityKey;
	
	protected	String		password = "dimdim123";
	protected	Boolean		secure = new Boolean(false);
	protected	String		sessionLocale = Locale.US.toString();
	
	//	Start Conference parameters
	
	protected	String		confName;
	protected	String		attendees;
	protected	String		confKey = CreateAndStartConferenceAction.idGen.generateRandomString(7,7);
	protected	String		meetingId = null;
	
	protected	String	lobby;
	protected	String	participantListEnabled;
	protected	String	networkProfile;
	protected	String	imageQuality;
	protected	Integer	meetingTime;
	protected	Integer	meetingHours;
	protected	Integer	meetingMinutes;
	protected	Integer	maxParticipants;
	protected	Integer	participantLimit = new Integer(-1);
	protected	String	presenterAV;
	protected	Integer	maxAttendeeMikes;
	protected	Integer	maxAttendeeVideos;
	protected	String	returnUrl;
	protected	String	defaultUrl = "";
	protected	String	headerText = "";
	protected	boolean	assistantEnabled = true;
	
	protected	String	uri;
	
	protected	String	resultCode = ERROR;
//	protected	String	url = "";
	protected	String	message = "Error";
	
	boolean featurePrivateChat	= true;
	boolean featurePublicChat	= true;
	boolean featurePublisher	= true;
	boolean featureWhiteboard	= true;
	boolean	featurePpt			= true;
	
	String osType = null;
	String browserType = null;
	String browserVersion = null;
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	private String feedbackEmail;
	private	String	joinUrl;

	String tollFree = "";
	String toll = "";
	String internTollFree = "";
	String internToll = "";
	String moderatorPassCode = "";
	String attendeePasscode = "";
	protected	String		preseterPwd = "";
	protected	String		attendeePwd = "";
	//this field is set by env check page
	String 	pubAvailable = "";
	private boolean dialInfoVisible;

	private String userId;
	
	//protected	boolean		videoOnly = false;
	protected	boolean		assignMikeOnJoin = false;
	protected	boolean		handsFreeOnLoad = false;
	protected	boolean		allowAttendeeInvites = true;
	protected	boolean		featureRecording = true;
	protected	boolean		featureCob = true;
	protected	boolean		featureDoc = true;
	
	public	CreateAndStartConferenceAction()
	{
//		super.featureId = IConferenceConstants.FEATURE_CONF;
//		super.eventId = IConferenceConstants.ACTION_LOGIN;
	}
	public	String	execute()	throws	Exception
	{
		String ret = LOGIN;
		if (uri == null)
		{
			addFieldError("confKey","* Internal error");
			ret = ERROR;
			return	ret;
		}
//		UserManager manager = UserManager.getManager();
		ConferenceManager confManager = ConferenceManager.getManager();
		try
		{
//			this.servletRequest.getSession().setMaxInactiveInterval(120);
			
			email = this.servletRequest.getParameter("email");
			confKey = this.servletRequest.getParameter("confKey");
			confName = this.servletRequest.getParameter("confName");
			displayName = this.servletRequest.getParameter("displayName");
			
			System.out.println("############################ inside CreateAndStartConferenceAction, confKey:"+confKey);
			osType = this.servletRequest.getParameter("osType");
//			System.out.println("************************************ osType: "+osType);
			
//			if(! this.readPortalOptions())
//			{
//				this.readDirectFormOptions();
//			}
			if (!readUserRequest())
			{
				message =  getResourceValue("landing_pages","ui_strings", "presenter_back_error", userType);
				addFieldError("confKey","* Internal error");
				ret = ERROR;
				return	ret;
			}
			
			checkSessionForOsAndBrowserType();
			
			/*if(null != osType && osType.length() > 0)
			{
				if(ConferenceConsoleConstants.OS_LINUX.equals(osType) || 
						ConferenceConsoleConstants.OS_UNIX.equals(osType))
				{
//					System.out.println("the os that server knows is "+osType +" hence disabling publisher");
					this.featurePublisher = false;
				}
			}
			if(null != browserType && browserType.length() > 0)
			{
				if(!ConferenceConsoleConstants.BROWSER_TYPE_IE.equals(browserType) &&
						!ConferenceConsoleConstants.BROWSER_TYPE_FIREFOX.equals(browserType))
				{
//					System.out.println("the browser that server knows is "+browserType +" hence disabling publisher");
					this.featurePublisher = false;
				}
			}*/
			
			UtilMethods.isPublisherSupportable(osType, browserType, browserVersion);
			//this.servletRequest.getSession().setAttribute("pubEnabled", String.valueOf(featurePublisher) );
			//this.servletRequest.getSession().setAttribute("defaultUrl", defaultUrl );
//			ServletContext sc = servletRequest.getSession().getServletContext();
//			String path = sc.getRealPath("/");
//			System.out.println("************************************ REAL PATH: "+path);
			
			UserManager.getManager().authenticatePresenter(this.email,this.securityKey);
			
//			checking for the #of conferences
			if(ConferenceConstants.MAX_MEETINGS_ON_SERVER != -1){
				if(confManager.getNumberOfActiveConferences() >= ConferenceConstants.MAX_MEETINGS_ON_SERVER){
					//if it exceeds the no of permitted meetings then just return error
					message = getResourceValue("landing_pages","ui_strings","start_conference_page.error5", userType);
					ret = ERROR;
					return ret;
				}
			}
			
			Locale locale = (Locale)this.session.get(ConferenceConsoleConstants.USER_LOCALE);
			if (locale == null)
			{
				locale = LocaleManager.getManager().getDefaultLocale();
			}
			IConference conf = confManager.createConference(email,displayName,userId,confName,confKey,meetingId,locale);
			this.setOptions(conf);
			((ActiveConference)conf).setFeedBackEmail(feedbackEmail);
			((ActiveConference)conf).getLogo();
			if (this.joinUrl != null && this.joinUrl.length() > 0)
			{
				((ActiveConference)conf).setJoinUrl(joinUrl);
			}
			
			IConferenceParticipant presenter = ((ActiveConference)conf).getHost();
			UserSession userSession = new UserSession(presenter,conf);
			userSession.setUri(uri);
			userSession.setUseSecureConnection(secure);
			userSession.setSessionLocale(locale);
			//setting locale in the active conference as all joinees should have this session
			conf.setConfLocale(locale);
			ret = SUCCESS;
			
//			getSession().put(ConferenceConsoleConstants.ACTIVE_USER_SESSION,userSession);
			UserSessionDataManager.getDataManager().saveObject(uri, UserSessionData.ACTIVE_USER_SESSION, userSession);
			
			/**	Start the conference right away.	**/
			
			conf.startConference();
			((ActiveConference)conf).addParticipantSession(userSession.getUser(),userSession);
			
			/**	Add the global presentations to the new conference.	**/
			
			String desktopResName = getResourceValue("console","ui_strings","share.desktop.name", userType);
			String wbResName = getResourceValue("console","ui_strings","share.wb.name", userType);
			String coBrowseResName = "Co-Browse";
			DefaultPresentationsManager dpm = new DefaultPresentationsManager();
			dpm.addDefaultItemsToMeeting(conf, desktopResName, wbResName, coBrowseResName);
			
			/*	Set up the user session for timeout monitoring		*/
			
			String httpSessionKey = this.servletRequest.getSession().getId();
			UserSessionManager.getManager().addUserSession(httpSessionKey,userSession);
			
			this.inviteInitialAttendees(conf,presenter);
		}
		catch(KeyInUseException kiue)
		{
			//addFieldError("confKey","* Key in use");
			message = getResourceValue("landing_pages","ui_strings","schedule_conference_page.error3", userType);
			addFieldError("confKey",getResourceValue("landing_pages","ui_strings","start_conference_page.error3", userType));
			ret = ERROR;
		}
		catch(UserNotAuthorizedToStartConference unatsc)
		{
			//addFieldError("email","* Not Authorized to Start Conference");
			message = getResourceValue("landing_pages","ui_strings","schedule_conference_page.error4", userType);
			addFieldError("email",getResourceValue("landing_pages","ui_strings","start_conference_page.error4", userType));
			ret = ERROR;
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
			message = e.getMessage();
			addFieldError("confKey","* Internal error");
			ret = ERROR;
		}
		System.out.println("Returning from LoginAndCreateConference action result code:"+ret);
		resultCode = ret;
		if (cflag == null)
		{
			cflag = getFlag();
		}
		return	ret;
	}
	
	protected	boolean	readUserRequest()
	{
		boolean	b = false;
		UserRequest userRequest = UserSessionDataManager.getDataManager().getUserRequest(this.uri);
		if (userRequest != null)
		{
			this.meetingId = userRequest.getMeetingId();
			this.joinUrl = userRequest.getPortalMeetingJoinUrl();
			UserInfo userInfo = userRequest.getUserInfo();
			if (userInfo != null)
			{
				this.email = userInfo.getEmail();
				this.displayName = userInfo.getDisplayName();
				this.userId = userInfo.getUserId();
				this.confName = userRequest.getConfName();
				this.confKey = userRequest.getConfKey();
				this.userType = userRequest.getUserInfo().getUserType();
				this.attendees = userRequest.getAttendees();
				this.attendeePwd = userRequest.getAttendeePwd();
				this.preseterPwd = userRequest.getPreseterPwd();
			}
			if (meetingId != null && meetingId.equalsIgnoreCase(confKey))
			{
				meetingId = null;
			}
			String locale = userRequest.getLocale();
			if (locale != null)
			{
				Locale lc = LocaleManager.getManager().getSupportedLocale(locale);
				if(null != locale)
				{
					this.session.put(ConferenceConsoleConstants.USER_LOCALE, lc);
				}
			}
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
				
				this.maxAttendeeMikes = new Integer(settings.getMaxAttendeeMikes());
				this.maxAttendeeVideos = new Integer(settings.getMaxAttendeeVideos());
				this.maxParticipants = new Integer(settings.getMaxParticipants());
				this.participantLimit = new Integer(settings.getParticipantLimit());
				this.meetingHours = new Integer(settings.getMeetingHours());
				this.meetingMinutes = new Integer(settings.getMeetingMinutes());
				
				this.assistantEnabled = settings.isAssistantEnabled();
				this.featurePrivateChat = settings.isFeaturePrivateChat();
				this.featurePublicChat = settings.isFeaturePublicChat();
				this.featurePpt = settings.isFeaturePpt();
				this.featurePublisher = settings.isFeaturePublisher() && this.featurePublisher;
				this.featureWhiteboard = settings.isFeatureWhiteboard();
				
				this.defaultUrl	=	settings.getDefaultUrl();
				this.headerText	=	settings.getHeaderText();
				this.feedbackEmail = settings.getFeedbackEmail();
				this.tollFree = settings.getTollFree();
				this.toll = settings.getToll();
				this.internTollFree = settings.getInternTollFree();
				this.internToll = settings.getInternToll();
				this.moderatorPassCode = settings.getModeratorPassCode();
				this.attendeePasscode = settings.getAttendeePasscode();
				this.dialInfoVisible = settings.isDialInfoVisible();
				
				//this.videoOnly = settings.isVideoOnly();
				this.assignMikeOnJoin = settings.isAssignMikeOnJoin();
				this.handsFreeOnLoad = settings.isHandsFreeOnLoad();
				this.allowAttendeeInvites = settings.isAllowAttendeeInvites();
				this.featureRecording = settings.isFeatureRecording();
				this.featureCob = settings.isFeatureCob();
				this.featureDoc = settings.isFeatureDoc();
			}
		}
		return b;
	}
	
	/**
	 * This reads the hasmap in whihc poratl options are saved temporarily
	 * If it is not found it this method returns false else true
	 * @return
	 */
	/*
	protected	boolean	readPortalOptions()
	{
		UserRequest userRequest = PortalInterface.getPortalInterface().getUserRequest(displayName);
		if (userRequest != null)
		{
			email = userRequest.getEmail();
			UserInfo userInfo = userRequest.getUserInfo();
			if (userInfo != null)
			{
				this.displayName = userInfo.getDisplayName();
				this.confName = userInfo.getName();
				this.confKey = userInfo.getConfKey();
				this.userType = userInfo.getRole();
			}
			PortalInterface.getPortalInterface().clearUserRequest(userRequest.getUri());
		}
		MeetingSettings settings = PortalInterface.getPortalInterface().getMeetingSettings(this.confKey);
		if (settings != null)
		{
			try
			{
				settings.readPortalParameters();
				System.out.println("!!!!!!!!!!!!!!!!!!!!reading portal settings !!!!!!!!!!"+settings);
				
				this.returnUrl = settings.getReturnUrlCS();
				//this.defaultUrl = settings.getDefaultURLCS();
				this.maxAttendeeMikes = settings.getMaxAttendeeMikesCS();
				this.presenterAV = settings.getPresenterAVCS();
				this.maxParticipants = settings.getMaxParticipantsCS();
				this.participantLimit = new Integer(settings.getParticipantLimit());
				this.meetingHours = settings.getMeetingHoursCS();
				this.meetingMinutes = settings.getMeetingMinutesCS();
				this.imageQuality = settings.getImageQualityCS();
				this.networkProfile = settings.getNetworkProfileCS();
				this.lobby = settings.getLobbyCS();
				this.assistantEnabled = settings.isAssistantEnabled();
				this.featurePrivateChat = settings.isFeaturePrivateChat();
				this.featurePublicChat = settings.isFeaturePublicChat();
				this.featurePpt = settings.isFeaturePpt();
				//if os does not suport or the user does not want, in any case disable publisher
				this.featurePublisher = settings.isFeaturePublisher() && this.featurePublisher;
				this.featureWhiteboard = settings.isFeatureWhiteboard();
				this.defaultUrl	=	settings.getDefaultUrl();
				feedbackEmail = settings.getFeedbackEmail();
				
				String locale = settings.getLocale();
				if (locale != null)
				{
					Locale lc = LocaleManager.getManager().getSupportedLocale(locale);
					if(null != locale)
					{
						this.session.put(ConferenceConsoleConstants.USER_LOCALE, lc);
					}
				}
				return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	protected	void	readDirectFormOptions()
	{
		MeetingOptions meetingOptions = (MeetingOptions)session.get(MeetingOptions.MEETING_OPTIONS_KEY);
		if (meetingOptions != null)
		{
			lobby = meetingOptions.getLobby();
			networkProfile = meetingOptions.getNetworkProfile();
			imageQuality = meetingOptions.getImageQuality();
			meetingHours = meetingOptions.getMeetingHours();
			meetingMinutes = meetingOptions.getMeetingMinutes();
			maxParticipants = meetingOptions.getMaxParticipants();
			presenterAV = meetingOptions.getPresenterAV();
			maxAttendeeMikes = meetingOptions.getMaxAttendeeMikes();
			returnUrl = meetingOptions.getReturnUrl();
			featurePrivateChat	= meetingOptions.isPrivateChatEnabled();
			featurePublicChat	= meetingOptions.isPublicChatEnabled();
			featurePublisher	= meetingOptions.isScreenShareEnabled();
			featureWhiteboard	= meetingOptions.isWhiteboardEnabed();
			attendees = meetingOptions.getAttendees();
			if (meetingOptions.getOsType() != null)
			{
				osType = meetingOptions.getOsType();
			}
			if (meetingOptions.getBrowserType() != null)
			{
				browserType = meetingOptions.getBrowserType();
			}
			displayName = meetingOptions.getDisplayName();
			confName = meetingOptions.getConfName();
			email = meetingOptions.getEmail();
			confKey = meetingOptions.getConfKey();
			
			session.remove(MeetingOptions.MEETING_OPTIONS_KEY);
			
			System.out.println("Meeting options are:"+meetingOptions.toString());
		}
	}
	*/
	protected	void	checkSessionForOsAndBrowserType()
	{
		if (this.osType == null)
		{
			String s1 = (String)this.getSession().get(ConferenceConsoleConstants.OS_TYPE);
			if (s1 != null)
			{
				this.osType = s1;
			}
		}
		if (this.browserType == null)
		{
			String s1 = (String)this.getSession().get(ConferenceConsoleConstants.BROWSER_TYPE);
			if (s1 != null)
			{
				this.browserType = s1;
			}
		}
	}
	protected	void	setOptions(IConference conf)
	{
		boolean	enableLobby = false;
		if (this.lobby != null && this.lobby.equals("true"))
		{
			enableLobby = true;
		}
		
		boolean	partListEnable = true;
		if (this.participantListEnabled != null && this.participantListEnabled.equals("false"))
		{
			partListEnable = false;
		}
		
		this.meetingTime = new Integer(this.meetingHours.intValue()*60 + this.meetingMinutes.intValue());
		
		System.out.println("Features availability is: publisher:"+this.featurePublisher+", whiteboard:"+this.featureWhiteboard);
		conf.setConferenceOptions(enableLobby, partListEnable, this.networkProfile, this.imageQuality,
				this.meetingTime, this.maxParticipants, this.participantLimit, this.presenterAV,
				this.maxAttendeeMikes, this.maxAttendeeVideos, this.returnUrl, this.assistantEnabled, this.featurePublicChat, this.featurePrivateChat, 
				this.featurePublisher, this.featureWhiteboard, this.featurePpt, this.defaultUrl, this.headerText, tollFree, toll, internTollFree, internToll,
				moderatorPassCode, attendeePasscode, dialInfoVisible, preseterPwd, attendeePwd,
				assignMikeOnJoin, handsFreeOnLoad, allowAttendeeInvites, featureRecording, featureCob, featureDoc);
	}
	protected	void	inviteInitialAttendees(IConference conf, IConferenceParticipant presenter)
	{
		if (this.attendees != null && this.attendees.length() > 0)
		{
			ConferenceInfo info = conf.getConferenceInfo();
//			info.setJoinURL(ConferenceConsoleConstants.getJoinURL(info.getKey()));
			info.setJoinURL(conf.getJoinUrl());
			
			System.out.println("Inviting initial list of attendees: "+this.attendees);
			InvitationEmailsHelper emailsHelper = new InvitationEmailsHelper(info,"",attendees,"");
			System.out.println("is dialin Info enabled : " + conf.isDialInfoVisible());
			if(conf.isDialInfoVisible())
			{
				emailsHelper.sendInvitationEmails((ActiveConference)conf,presenter,this.getCurrentLocale(), userType, conf.getInternToll(),
						conf.getInternTollFree(), conf.getToll(), conf.getTollFree(),
						conf.getModeratorPassCode(), conf.getAttendeePasscode(),conf.getAttendeePwd());				
			}
			else{
				emailsHelper.sendInvitationEmails((ActiveConference)conf,presenter,this.getCurrentLocale(), userType, "Not Applicable",
						"Not Applicable", "Not Applicable", "Not Applicable",
						"Not Applicable", "Not Applicable",conf.getAttendeePwd());
			}
		}
	}
	public String getConfKey()
	{
		return this.confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getConfName()
	{
		return this.confName;
	}
	public void setConfName(String confName)
	{
		this.confName = confName;
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
	public String getPassword()
	{
		return this.password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public Boolean getSecure()
	{
		return this.secure;
	}
	public void setSecure(Boolean secure)
	{
		this.secure = secure;
	}
	public String getSessionLocale()
	{
		return this.sessionLocale;
	}
	public void setSessionLocale(String sessionLocale)
	{
		this.sessionLocale = sessionLocale;
	}
	public String getSecurityKey()
	{
		return securityKey;
	}
	public void setSecurityKey(String securityKey)
	{
		this.securityKey = securityKey;
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
//	public String getUrl()
//	{
//		return url;
//	}
//	public void setUrl(String url)
//	{
//		this.url = url;
//	}
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
	public Integer getMaxParticipants()
	{
		return maxParticipants;
	}
	public void setMaxParticipants(Integer maxParticipants)
	{
		this.maxParticipants = maxParticipants;
	}
	public Integer getMeetingTime()
	{
		return meetingTime;
	}
	public void setMeetingTime(Integer meetingTime)
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
	public Integer getMeetingHours()
	{
		return meetingHours;
	}
	public void setMeetingHours(Integer meetingHours)
	{
		this.meetingHours = meetingHours;
	}
	public Integer getMeetingMinutes()
	{
		return meetingMinutes;
	}
	public void setMeetingMinutes(Integer meetingMinutes)
	{
		this.meetingMinutes = meetingMinutes;
	}
	public String getPresenterAV()
	{
		return presenterAV;
	}
	public void setPresenterAV(String presenterAV)
	{
		this.presenterAV = presenterAV;
	}
	public String getAttendees()
	{
		return attendees;
	}
	public void setAttendees(String attendees)
	{
		this.attendees = attendees;
	}
	public Integer getMaxAttendeeMikes()
	{
		return maxAttendeeMikes;
	}
	public void setMaxAttendeeMikes(Integer maxAttendeeMikes)
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
	public void setAssistantEnabled(boolean assistantEnabled)
	{
		this.assistantEnabled = assistantEnabled;
	}
	public boolean isAssistantEnabled()
	{
		return assistantEnabled;
	}
	public String getDefaultUrl()
	{
		return defaultUrl;
	}
	public void setDefaultUrl(String defaultUrl)
	{
		this.defaultUrl = defaultUrl;
	}
	public String getBrowserType()
	{
		return browserType;
	}
	public void setBrowserType(String browserType)
	{
		this.browserType = browserType;
	}
	public String getOsType()
	{
		return osType;
	}
	public void setOsType(String osType)
	{
		this.osType = osType;
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
	public String getPubAvailable() {
		return pubAvailable;
	}
	public void setPubAvailable(String pubAvailable) {
		this.pubAvailable = pubAvailable;
	}
	public String getBrowserVersion() {
		return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public Integer getMaxAttendeeVideos() {
		return maxAttendeeVideos;
	}
	public void setMaxAttendeeVideos(Integer maxAttendeeVideos) {
		this.maxAttendeeVideos = maxAttendeeVideos;
	}
	
}
