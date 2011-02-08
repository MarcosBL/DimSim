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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.util.session;

import	java.util.HashMap;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.util.timer.TimerService;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserSessionDataManager
{
	private	static	UserSessionDataManager	dataManager;
	
	public	static	synchronized	UserSessionDataManager	getDataManager()
	{
		if (UserSessionDataManager.dataManager == null)
		{
			UserSessionDataManager.createDataManager();
		}
		return	UserSessionDataManager.dataManager;
	}
	private	static	synchronized	void	createDataManager()
	{
		if (UserSessionDataManager.dataManager == null)
		{
			UserSessionDataManager.dataManager = new UserSessionDataManager();
		}
	}
	
	private	HashMap		userSessions = new HashMap();
	
	private	UserSessionDataManager()
	{
		
	}
	public	HashMap	getUserSessions()
	{
		return	this.userSessions;
	}
	private	UserSessionData		getUserSessionData(String dataId)
	{
		return (UserSessionData)this.userSessions.get(dataId);
	}
	private	UserSessionData		setUserSessionData(String dataId)
	{
		UserSessionData usd = new UserSessionData(dataId);
		synchronized (this.userSessions)
		{
			TimerService.getService().addUser(usd);
			this.userSessions.put(dataId, usd);
		}
		return	usd;
	}
	public	void	clearUserSessionData(String dataId)
	{
		UserSessionData usd = (UserSessionData)this.userSessions.get(dataId);
		if (usd != null)
		{
			synchronized (this.userSessions)
			{
				usd.setCleared();
				this.userSessions.remove(dataId);
			}
		}
	}
	/**
	 *	These are the only two top level requests that come from external source.
	 *	The methods create and caches a user request object. The return value
	 *	is the unique request id of the new user request.
	 */
	public	String	saveStartnewMeetingRequestData(String action,
				String email, String displayName, String userId, String confKey, String confName,
				String meetingId, String returnUrl, String presenterAV,
				String imageQuality, String networkProfile,
				int maxAttendeeMikes, int maxParticipants, int participantLimit,
				int meetingHours, int meetingMinutes,
				boolean lobbyEnabled, boolean participantListEnabled, boolean assistantEnabled,
				boolean featurePrivateChat, boolean featurePublicChat,
				boolean featurePpt, boolean featurePublisher,
				boolean featureWhiteboard,
				String defaultURL, String headerText, String feedbackEmail, String attendees, String locale, 
				String userType,  String tollFree, String toll, String internTollFree, String internToll,
				String moderatorPassCode, String attendeePasscode, boolean dialInfoVisible, String preseterPwd, String attendeePwd,
				boolean assignMikeOnJoin, boolean handsFreeOnLoad, boolean allowAttendeeInvites
				, boolean featureRecording, boolean featureCob, boolean featureDoc, String meetingName)
	{
//		System.out.println("1");
		//here if user is premium and video only is selected presenterAv is assumed to be video chat
		//else presenterAv is assumed to be audio-video.
		//this has be looked into much deeply
		int maxAttendeeVideos = 0;
		if(LocaleResourceFile.PREMIUM.equalsIgnoreCase(userType))
		{
			//if it is video only or av set video chat for premium user
			//if(videoOnly || (ConferenceConstants.MEETING_AV_TYPE_AV.equalsIgnoreCase(presenterAV)))
			//{
				System.out.println("since it is a premium user giving video chat");
				maxAttendeeVideos = ConferenceConsoleConstants.getMaxAttendeeVideos();
			//}
		}
		MeetingSettings meetingSettings = new MeetingSettings(returnUrl, presenterAV,
				imageQuality, networkProfile,
				maxAttendeeMikes, maxAttendeeVideos, maxParticipants, participantLimit,
				meetingHours, meetingMinutes,
				lobbyEnabled, participantListEnabled, assistantEnabled, featurePrivateChat,
				featurePublicChat, featurePpt,
				featurePublisher, featureWhiteboard,
				defaultURL, headerText, feedbackEmail, tollFree, toll, internTollFree, internToll,
				moderatorPassCode, attendeePasscode, dialInfoVisible,
				assignMikeOnJoin, handsFreeOnLoad, allowAttendeeInvites
				,featureRecording, featureCob,featureDoc);
		
		UserInfo userInfo = new UserInfo(email, displayName, userType, userId);
		
//		System.out.println("3"+userInfo);
		UserRequest userRequest = new UserRequest(action, userInfo,
				confKey, confName, meetingId, attendees, locale, meetingSettings, preseterPwd, attendeePwd);
		userRequest.setMeetingName(meetingName);
		String requestId = userRequest.getUri();
		
//		System.out.println("4"+requestId);
		UserSessionData userSessionData = this.setUserSessionData(requestId);
//		System.out.println("5");
		synchronized (this.userSessions)
		{
			userSessionData.getSessionData().put(requestId, userRequest);
		}
//		System.out.println("6");
		return	requestId;
	}
	
	public	String	saveJoinMeetingRequestData(String action,
			String email, String displayName, String userId, String confKey, String locale, String userType, String attendeePwd)
	{
		UserInfo userInfo = new UserInfo(email, displayName, userType, userId);
		
		UserRequest userRequest = new UserRequest(action, userInfo,
				confKey, null, null, null, locale, null, null, attendeePwd);
		String requestId = userRequest.getUri();
		
		UserSessionData userSessionData = this.setUserSessionData(requestId);
		synchronized (this.userSessions)
		{
			userSessionData.getSessionData().put(requestId, userRequest);
		}
		return	requestId;
	}
	public	String	saveReloadConsoleRequestData(String action, String objectCode, Object data1)
	{
		UserRequest userRequest = new UserRequest(action, null,
				null, null, null, null, null, null, null, null);
		String requestId = userRequest.getUri();
		
		UserSessionData userSessionData = this.setUserSessionData(requestId);
		synchronized (this.userSessions)
		{
			userSessionData.getSessionData().put(requestId, userRequest);
			userSessionData.getSessionData().put(objectCode, data1);
		}
		return	requestId;
	}
	public	UserRequest	getUserRequest(String requestId)
	{
		UserRequest userRequest = null;
//		System.out.println("getUserRequest: "+1);
		UserSessionData userSessionData = this.getUserSessionData(requestId);
//		System.out.println("getUserRequest: "+2);
		if (userSessionData != null)
		{
//			System.out.println("getUserRequest: "+3);
			userRequest = (UserRequest)userSessionData.getSessionData().get(requestId);
		}
//		System.out.println("getUserRequest: "+4);
		return	userRequest;
	}
	public	void	clearUserRequest(String requestId)
	{
		UserSessionData userSessionData = this.getUserSessionData(requestId);
//		System.out.println("clearUserRequest: "+2);
		if (userSessionData != null)
		{
//			System.out.println("clearUserRequest: "+3);
			synchronized (this.userSessions)
			{
				userSessionData.getSessionData().remove(requestId);
				userSessionData.setCleared();
			}
		}
	}
	/**
	 * Following methods allow the actions and the application code to flag and save
	 * arbitrary objects on the session data map.
	 */
	public	void	saveObject(String requestId, String objectCode, Object object)
	{
		UserSessionData userSessionData = this.getUserSessionData(requestId);
		if (userSessionData != null)
		{
			userSessionData.getSessionData().put(objectCode,object);
		}
	}
	public	Object	getObject(String requestId, String objectCode)
	{
		Object	object = null;
		UserSessionData userSessionData = this.getUserSessionData(requestId);
		if (userSessionData != null)
		{
			object = userSessionData.getSessionData().get(objectCode);
		}
		return	object;
	}
	public	void	clearObject(String requestId, String objectCode)
	{
		UserSessionData userSessionData = this.getUserSessionData(requestId);
		if (userSessionData != null)
		{
			userSessionData.getSessionData().remove(objectCode);
		}
	}
}
