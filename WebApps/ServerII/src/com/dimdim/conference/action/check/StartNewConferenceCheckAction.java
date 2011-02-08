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

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.application.UserManager;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.UserNotAuthorizedToStartConference;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
//import com.dimdim.conference.db.ConferenceDB;
//import com.dimdim.conference.db.ConferenceSpec;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.conference.application.portal.PortalInterface;
import com.dimdim.conference.application.portal.UserInfo;
import com.dimdim.conference.application.portal.UserRequest;
import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.application.presentation.PresentationManager;
//import com.dimdim.conference.model.MeetingOptions;

//import com.dimdim.util.session.UserRequest;
//import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.MeetingSettings;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * This action checks if a new 'meet now' conference can be started.
 */

public class StartNewConferenceCheckAction	extends	ConferenceCheckAction
{
	protected	String	displayName;
	protected	String	confName;
	protected	String	attendees;
	
	protected	String	networkProfile = "2";
	protected	String	imageQuality = "medium";
	protected	String	meetingTime = "2";
	protected	String	meetingHours = "1";
	protected	String	meetingMinutes = "0";
	protected	String	maxParticipants = ConferenceConsoleConstants.getMinParticipantsPerConference()+"";
	protected	String	presenterAV = "audio";
	protected	String	maxAttendeeMikes = "3";
	protected	String	returnUrl = "";
	protected	String	osType;
	protected	String	browserType;
	
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	protected	String	lobby = "false";
	protected	String	participantListEnabled = "true";
	protected	String	privateChatEnabled = "true";
	protected	String	publicChatEnabled = "true";
	protected	String	screenShareEnabled = "true";
	protected	String	whiteboardEnabled = "true";
	protected	String		preseterPwd = "";
	protected	String		attendeePwd = "";
	protected	String	uri;
	private String userId;
	
	//protected	String		videoOnly = "false";
	protected	String		assignMikeOnJoin = "false";
	protected	String		handsFreeOnLoad = "false";
	protected	String		allowAttendeeInvites = "true";
	protected	String		featureRecording = "true";
	protected	String		featureCob = "true";
	protected	String		featureDoc = "true";
	
	//String dialInfo = "";
	String tollFree = "";
	String toll = "";
	String internTollFree = "";
	String internToll = "";
	String moderatorPassCode = "";
	String attendeePasscode = "";
	boolean dialInfoVisible = true;

	public	StartNewConferenceCheckAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = ERROR;
//		UserRequest userRequest = null;
		UserSession userSession = null;
		
//		String userAgent = this.servletRequest.getHeader("user-agent").toLowerCase();
		
		if (userSession != null)
		{
			this.resultCode = ALREADY_IN_CONFERENCE;
			this.message = "User is already in conference";
		}
		else
		{
			try
			{
				confName = this.servletRequest.getParameter("confName");
				displayName = this.servletRequest.getParameter("displayName");
				
//				making the confkey as lower case as this was creating dtp stream problems
				if(null != this.confKey)
				{
				    this.confKey = this.confKey.toLowerCase();
				}
				
				UserManager.getManager().authenticatePresenter(this.email,this.securityKey);

					//	Check currently running conferences.
					ConferenceManager confManager = ConferenceManager.getManager();
					//checking for the #of conferences
					if(ConferenceConstants.MAX_MEETINGS_ON_SERVER != -1){
						if(confManager.getNumberOfActiveConferences() >= ConferenceConstants.MAX_MEETINGS_ON_SERVER){
							//if it exceeds the no of permitted meetings then just return error
							message = getResourceValue("landing_pages","ui_strings",
								"start_conference_page.error5", userType);
							ret = ERROR;
							return ret;
						}
					}
					
					IConference conf = confManager.getConferenceReturnNull(this.confKey);
					if (conf != null)
					{
//						resultCode = KEY_IN_USE;
//						message = getResourceValue("landing_pages","ui_strings",
//							"start_conference_page.error3", userType);
						
						//	Allow rejoin. Security considerations pending discussion.
						String locale = null;
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
						this.uri = UserSessionDataManager.getDataManager().
						saveJoinMeetingRequestData(Connect.HOST_REJOIN_ACTION, email, displayName, userId, confKey, locale, userType, attendeePwd);
					
						this.setupSuccess(Connect.HOST_REJOIN_ACTION);
					}
					else
					{
						String meetingId = confKey;
						String defaultUrl = "";
						String feedbackEmail = ConferenceConsoleConstants.getResourceKeyValue("feedback_email_address", "feedback@dimdim.com");;
						String locale = null;
						
						int participantLimit = ConferenceConsoleConstants.getMaxParticipantsPerConference();
						boolean featurePpt = true;
						boolean assistantEnabled = true;
						
						if (this.attendees != null && this.attendees.length() >0)
						{
							attendees = attendees.replace("\r\n", ";");
							attendees = attendees.replace('\n', ';');
						}
						
						this.uri = UserSessionDataManager.getDataManager().saveStartnewMeetingRequestData(
								"host",
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
								defaultUrl, "", feedbackEmail, attendees, locale, 
								userType, tollFree, toll, internTollFree, internToll,
								moderatorPassCode, attendeePasscode, dialInfoVisible, preseterPwd, attendeePwd,
								//Boolean.parseBoolean(videoOnly),
								Boolean.parseBoolean(assignMikeOnJoin),
								Boolean.parseBoolean(handsFreeOnLoad),
								Boolean.parseBoolean(allowAttendeeInvites),
								Boolean.parseBoolean(featureRecording),
								Boolean.parseBoolean(featureCob),
								Boolean.parseBoolean(featureDoc), "");
								
						ret = SUCCESS;
						
						this.setupSuccess(Connect.HOST_ACTION);
					}
			}
			catch(UserNotAuthorizedToStartConference unatsc)
			{
//				unatsc.printStackTrace();
				resultCode = USER_NOT_AUTHORIZED;
				message = "User not authorized";
				ret = ERROR;
			}
			catch(NoConferenceByKeyException ncbk)
			{
				ret = SUCCESS;
				this.setupSuccess(Connect.HOST_ACTION);
				//url = URLEncoder.encode(this.url,"utf-8");
				url = url.trim();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				ret = ERROR;
				message = e.getMessage();
			}
		}
		
		System.out.println("StartNewConferenceCheck returning:"+ret+", result code:"+resultCode+", message:"+message);
		System.out.println("StartNewConferenceCheck url =: "+url);
		
		//this is for safari on mac
//		boolean is_safari = ((userAgent.indexOf("safari")!=-1)&&(userAgent.indexOf("mac")!=-1))?true:false;
//		if(is_safari){
//			cflag = getFlag();
//			System.out.println("StartNewConferenceCheck retiurning success_safari"); 
//			ret = "success_safari";
//		}
		
		return	ret;
	}
	protected	void	setupSuccess(String action)	throws	Exception
	{
		resultCode = SUCCESS;
		message = "";
		url = "/"+ConferenceConsoleConstants.getWebappName();
		url += "/html/envcheck/connect.action";
		url += "?action="+action+"&email=";
		url += email;//URLEncoder.encode(this.email,"utf-8");
		url += "&confKey=";
		url += confKey;//URLEncoder.encode(confKey,"utf-8");
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
		url += "&uri=";
		url += uri;
		url += "&attendeePwd=";
		url += attendeePwd;
		url += "&preseterPwd=";
		url += preseterPwd;
		url += "&cflag=";
		url += cflag;
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
		//if("videoonly".equalsIgnoreCase(presenterAV))
		//{
		//	this.videoOnly = "true";
		//}else{
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
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
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
	public boolean isDialInfoVisible() {
		return dialInfoVisible;
	}
	public void setDialInfoVisible(boolean dialInfoVisible) {
		this.dialInfoVisible = dialInfoVisible;
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
	public String getAllowAttendeeInvites() {
		return allowAttendeeInvites;
	}
	public void setAllowAttendeeInvites(String allowAttendeeInvites) {
		this.allowAttendeeInvites = allowAttendeeInvites;
	}
	public String getAssignMikeOnJoin() {
		return assignMikeOnJoin;
	}
	public void setAssignMikeOnJoin(String assignMikeOnJoin) {
		this.assignMikeOnJoin = assignMikeOnJoin;
	}
	public String getHandsFreeOnLoad() {
		return handsFreeOnLoad;
	}
	public void setHandsFreeOnLoad(String handsFreeOnLoad) {
		this.handsFreeOnLoad = handsFreeOnLoad;
	}
	/*public String getVideoOnly() {
		return videoOnly;
	}
	public void setVideoOnly(String videoOnly) {
		this.videoOnly = videoOnly;
	}*/
	public String getFeatureRecording() {
		return featureRecording;
	}
	public void setFeatureRecording(String featureRecording) {
		this.featureRecording = featureRecording;
	}
	public String getFeatureCob() {
		return featureCob;
	}
	public void setFeatureCob(String featureCob) {
		this.featureCob = featureCob;
	}
	public String getFeatureDoc() {
		return featureDoc;
	}
	public void setFeatureDoc(String featureDoc) {
		this.featureDoc = featureDoc;
	}
	
}
