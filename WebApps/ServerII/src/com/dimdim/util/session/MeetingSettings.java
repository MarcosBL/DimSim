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

package com.dimdim.util.session;

import com.dimdim.conference.ConferenceConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MeetingSettings
{
	protected	String		returnUrl = "http://www.dimdim.com";
	protected	String		presenterAV = ConferenceConstants.MEETING_AV_TYPE_AUDIO;		//	av / audio / disabled
	protected	String		imageQuality = "medium";	//	low / medium / high
	protected	String		networkProfile = "2";	//	1, 2, 3
	
	protected	int			maxAttendeeMikes = 1;
	protected	int			maxAttendeeVideos = 0;
	protected	int			maxParticipants = 5;
	protected	int			participantLimit = 20;
	protected	int			meetingHours = 2;
	protected	int			meetingMinutes = 0;
	
	protected	boolean		lobbyEnabled 		= false;
	protected	boolean		assistantEnabled 	= false;
	protected	boolean		featurePrivateChat	= true;
	protected	boolean		featurePublicChat	= true;
	protected	boolean		featurePpt			= true;
	protected	boolean		featurePublisher	= true;
	protected	boolean		featureWhiteboard	= true;
	protected	boolean		participantListEnabled		= true;
	
	//protected	boolean		videoOnly = false;
	protected	boolean		assignMikeOnJoin = false;
	protected	boolean		handsFreeOnLoad = false;
	protected	boolean		allowAttendeeInvites = true;
	
	protected	String		defaultUrl	=	"";
	protected	String		headerText	=	"";
	protected 	String		feedbackEmail = "feedback@dimdim.com";
	String tollFree = "";
	String toll = "";
	String internTollFree = "";
	String internToll = "";
	String moderatorPassCode = "";
	String attendeePasscode = "";
	boolean dialInfoVisible = true;
	
	protected 	boolean featureRecording	= true;
	protected 	boolean featureDoc	= true;
	protected 	boolean featureCob	= true;
//	public	MeetingSettings()
//	{
//	}
	public MeetingSettings(String returnUrl, String presenterAV,
			String imageQuality, String networkProfile,
			int maxAttendeeMikes, int maxAttendeeVideos, int maxParticipants, int participantLimit,
			int meetingHours, int meetingMinutes,
			boolean lobbyEnabled, boolean participantListEnabled, boolean assistantEnabled, boolean featurePrivateChat,
			boolean featurePublicChat, boolean featurePpt,
			boolean featurePublisher, boolean featureWhiteboard,
			String defaultUrl, String headerText, String feedbackEmail, String tollFree, 
			String toll, String internTollFree, String internToll,
			String moderatorPassCode, String attendeePasscode, boolean dialInfoVisible,
			boolean assignMikeOnJoin, boolean handsFreeOnLoad, boolean allowAttendeeInvites
			,boolean featureRecording, boolean featureCob, boolean featureDoc)
	{
		this.returnUrl = returnUrl;
		this.presenterAV = presenterAV;
		this.imageQuality = imageQuality;
		this.networkProfile = networkProfile;
		this.maxAttendeeMikes = maxAttendeeMikes;
		this.maxAttendeeVideos = maxAttendeeVideos;
		this.maxParticipants = maxParticipants;
		this.participantLimit = participantLimit;
		this.meetingHours = meetingHours;
		this.meetingMinutes = meetingMinutes;
		this.lobbyEnabled = lobbyEnabled;
		this.participantListEnabled = participantListEnabled;
		this.assistantEnabled = assistantEnabled;
		this.featurePrivateChat = featurePrivateChat;
		this.featurePublicChat = featurePublicChat;
		this.featurePpt = featurePpt;
		this.featurePublisher = featurePublisher;
		this.featureWhiteboard = featureWhiteboard;
		this.defaultUrl = defaultUrl;
		this.headerText = headerText;
		this.feedbackEmail = feedbackEmail;
		
		this.tollFree = tollFree; 
		this.toll = toll;
		this.internTollFree = internTollFree; 
		this.internToll = internToll;
		this.moderatorPassCode = moderatorPassCode; 
		this.attendeePasscode = attendeePasscode;
		this.dialInfoVisible = dialInfoVisible;
		
		this.assignMikeOnJoin = assignMikeOnJoin;
		this.handsFreeOnLoad = handsFreeOnLoad;
		this.allowAttendeeInvites = allowAttendeeInvites;
		
		this.featureRecording = featureRecording;
		this.featureCob = featureCob;
		this.featureDoc = featureDoc;
	}
	public boolean isAssistantEnabled()
	{
		return assistantEnabled;
	}
	public String getDefaultUrl()
	{
		return defaultUrl;
	}
	public boolean isFeaturePpt()
	{
		return featurePpt;
	}
	public boolean isFeaturePrivateChat()
	{
		return featurePrivateChat;
	}
	public boolean isFeaturePublicChat()
	{
		return featurePublicChat;
	}
	public boolean isFeaturePublisher()
	{
		return featurePublisher;
	}
	public boolean isFeatureWhiteboard()
	{
		return featureWhiteboard;
	}
	public String getFeedbackEmail()
	{
		return feedbackEmail;
	}
	
	
	public String getAttendeePasscode() {
		return attendeePasscode;
	}
	public String getInternToll() {
		return internToll;
	}
	public String getInternTollFree() {
		return internTollFree;
	}
	public String getModeratorPassCode() {
		return moderatorPassCode;
	}
	public String getToll() {
		return toll;
	}
	public String getTollFree() {
		return tollFree;
	}
	public String getImageQuality()
	{
		return imageQuality;
	}
	public boolean isLobbyEnabled()
	{
		return lobbyEnabled;
	}
	public int getMaxAttendeeMikes()
	{
		return maxAttendeeMikes;
	}
	public int getMaxParticipants()
	{
		return maxParticipants;
	}
	public int getMeetingHours()
	{
		return meetingHours;
	}
	public int getMeetingMinutes()
	{
		return meetingMinutes;
	}
	public String getNetworkProfile()
	{
		return networkProfile;
	}
	public int getParticipantLimit()
	{
		return participantLimit;
	}
	public String getPresenterAV()
	{
		return presenterAV;
	}
	public String getReturnUrl()
	{
		return returnUrl;
	}
	public boolean isDialInfoVisible() {
		return dialInfoVisible;
	}
	public void setDialInfoVisible(boolean dialInfoVisible) {
		this.dialInfoVisible = dialInfoVisible;
	}
	public boolean isParticipantListEnabled() {
		return participantListEnabled;
	}
	public boolean isAllowAttendeeInvites()
	{
		return allowAttendeeInvites;
	}
	public boolean isAssignMikeOnJoin()
	{
		return assignMikeOnJoin;
	}
	public boolean isHandsFreeOnLoad()
	{
		return handsFreeOnLoad;
	}
	
	public	String	toString()
	{
		return	"--"+assignMikeOnJoin+"--"+handsFreeOnLoad+"--"+allowAttendeeInvites;
	}
	public boolean isFeatureRecording() {
		return featureRecording;
	}
	
	public boolean isFeatureDoc() {
		return featureDoc;
	}

	public boolean isFeatureCob() {
		return featureCob;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public int getMaxAttendeeVideos() {
		return maxAttendeeVideos;
	}


}
