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

package com.dimdim.conference.application.portal;

import com.dimdim.conference.ConferenceConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MeetingSettings
{
	protected	boolean		waitingAreaEabled;
	protected	char		networkProfile;
	protected	int			maxHours; //this is number of hours a meting is supposed to run
	protected	int			maxMins; //this is #of minutes after hours the meeting is going to run
	protected	int			maxParticipants;
	protected	int			participantLimit;
	protected	int			maxAttendeeAudios;
	protected	char		audioVideo;
	protected	String		returnUrl = "";
	protected	String		defaultUrl = "";
	protected	String		headerText = "";
	protected	boolean		assistantEnabled = false;
	protected	String		locale="en_US";
	boolean featureAudio   		= true;
	boolean featureVedio  		= true;
	boolean featurePrivateChat	= true;
	boolean featurePublicChat	= true;
	boolean featurePublisher	= true;
	boolean featureWhiteboard	= true;
	protected 	boolean featureCob	= true;
	protected 	boolean featureDoc	= true;
	protected 	boolean featureRecording	= true;
	boolean	participantListEnabled		= true;
	
	protected	String	lobbyCS = "false";
	protected	String	networkProfileCS = "2";	//	1, 2, 3
	protected	String	imageQualityCS = "medium";
	protected	Integer	meetingHoursCS;	//	1
	protected	Integer	meetingMinutesCS;	//	0
	protected	Integer	maxParticipantsCS;	//	20
	protected	String	presenterAVCS;		//	av / audio
	protected	Integer	maxAttendeeMikesCS;	//	3
	protected	String	returnUrlCS	=	"http://www.dimdim.com";
	protected	String	defaultURLCS	=	"";
	protected	String		assistantEnabledCS = "false";
	protected	String		localeCS="en_US";
	protected 	String	feedbackEmail;
	
	String tollFree = "";
	String toll = "";
	String internTollFree = "";
	String internToll = "";
	String moderatorPassCode = "";
	String attendeePasscode = "";
	protected	boolean		displayDialInfo = true;
	/*boolean featureAudioCS 		= true;
	boolean featureVedioCS 		= true;
	boolean featurePrivateChatCS= true;
	boolean featurePublicChatCS	= true;
	boolean featurePublisherCS	= true;
	boolean featureShiteboardCS	= true;*/
	
	//protected	boolean		videoOnly = false;
	protected	boolean		assignMikeOnJoin = false;
	protected	boolean		handsFreeOnLoad = false;
	protected	boolean		allowAttendeeInvites = true;
	
	
	public	MeetingSettings()
	{
		
	}
	
	public	void	readPortalParameters()
	{
		lobbyCS = this.waitingAreaEabled+"";
		
		if (networkProfile == 'L')	{	networkProfileCS = "1";	}
		else if (networkProfile == 'M')	{	networkProfileCS = "2";	}
		else if (networkProfile == 'H')	{	networkProfileCS = "3";	}
		
		if (maxHours == 0 && maxMins == 0)
		{
			maxHours = 1;
		}
		this.meetingHoursCS = new Integer(maxHours);
		this.meetingMinutesCS = new Integer(maxMins);
		this.maxParticipantsCS = new Integer(maxParticipants);
		this.maxAttendeeMikesCS = new Integer(maxAttendeeAudios);
		
		if (audioVideo == 'A')	{	presenterAVCS = ConferenceConstants.MEETING_AV_TYPE_AUDIO;	}
		else if (audioVideo == 'D')	{	presenterAVCS = ConferenceConstants.MEETING_AV_TYPE_DISABLED;	}
		else if (audioVideo == 'X')	{	presenterAVCS = ConferenceConstants.MEETING_AV_TYPE_VIDEO;	}
		//else if (audioVideo == 'Z')	{	presenterAVCS = ConferenceConstants.MEETING_AV_TYPE_VIDEO_CHAT;	}
		else presenterAVCS = ConferenceConstants.MEETING_AV_TYPE_AV;
		//have stream line these parameters
		/*if(audioVideo == 'X')
		{
			videoOnly = true;
		}*/
		
		if (returnUrl != null)
		{
			returnUrlCS = returnUrl;
		}
		if (defaultUrl != null)
		{
			defaultURLCS = defaultUrl;
		}
		if(assistantEnabled) assistantEnabledCS="true";
		if(null != locale && locale.length() > 0)
		{
			localeCS = locale;
		}
	}
	
	public char getAudioVideo() {
		return audioVideo;
	}
	public void setAudioVideo(char audioVideo) {
		this.audioVideo = audioVideo;
	}
	public int getMaxAttendeeAudios() {
		return maxAttendeeAudios;
	}
	public void setMaxAttendeeAudios(int maxAttendeeAudios) {
		this.maxAttendeeAudios = maxAttendeeAudios;
	}
	public int getMaxParticipants() {
		return maxParticipants;
	}
	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	public int getMaxHours() {
		return maxHours;
	}
	public void setMaxHours(int maxHours) {
		this.maxHours = maxHours;
	}
	public char getNetworkProfile() {
		return networkProfile;
	}
	public void setNetworkProfile(char networkProfile) {
		this.networkProfile = networkProfile;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public boolean isWaitingAreaEabled() {
		return waitingAreaEabled;
	}
	public void setWaitingAreaEabled(boolean waitingAreaEabled) {
		this.waitingAreaEabled = waitingAreaEabled;
	}
	public int getMaxMins() {
		return maxMins;
	}
	public void setMaxMins(int maxMins) {
		this.maxMins = maxMins;
	}
	public String getImageQualityCS()
	{
		return imageQualityCS;
	}
	public String getLobbyCS()
	{
		return lobbyCS;
	}
	public Integer getMaxAttendeeMikesCS()
	{
		return maxAttendeeMikesCS;
	}
	public Integer getMaxParticipantsCS()
	{
		return maxParticipantsCS;
	}
	public Integer getMeetingHoursCS()
	{
		return meetingHoursCS;
	}
	public Integer getMeetingMinutesCS()
	{
		return meetingMinutesCS;
	}
	public String getNetworkProfileCS()
	{
		return networkProfileCS;
	}
	public String getPresenterAVCS()
	{
		return presenterAVCS;
	}
	public String getReturnUrlCS()
	{
		return returnUrlCS;
	}

	public boolean isAssistantEnabled() {
		return assistantEnabled;
	}

	public void setAssistantEnabled(boolean assistantEnabled) {
		this.assistantEnabled = assistantEnabled;
	}

	public String getAssistantEnabledCS() {
		return assistantEnabledCS;
	}

	public void setAssistantEnabledCS(String assistantEnabledCS) {
		this.assistantEnabledCS = assistantEnabledCS;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String toString()
	{
		return	"waitingAreaEabled:"+waitingAreaEabled+",assistantEnabled:"+assistantEnabled+",networkProfile:"+networkProfile+
			",maxHours:"+maxHours+",maxMins:"+maxMins+",maxParticipants:"+maxParticipants+
			",maxAttendeeAudios:"+maxAttendeeAudios+",audioVideo:"+audioVideo+
			",returnUrl:"+returnUrl+"locale="+locale+"featurePrivateChat="+featurePrivateChat+"featurePublicChat="+featurePublicChat+
			"featurePublisher="+featurePublisher+"featureWhiteboard="+featureWhiteboard
			+"defaultUrl="+defaultUrl+"toll="+toll+"tollFree="+tollFree+"internToll="+
			internToll+"internTollFree="+internTollFree+"moderatorPassCode="+moderatorPassCode+"attendeePasscode="+attendeePasscode+"&participantListEnabled="+participantListEnabled;
	}

	public boolean isFeatureAudio() {
		return featureAudio;
	}

	public void setFeatureAudio(boolean featureAudio) {
		this.featureAudio = featureAudio;
	}

	public boolean isFeaturePrivateChat() {
		return featurePrivateChat;
	}

	public void setFeaturePrivateChat(boolean featurePrivateChat) {
		this.featurePrivateChat = featurePrivateChat;
	}

	public boolean isFeaturePublicChat() {
		return featurePublicChat;
	}

	public void setFeaturePublicChat(boolean featurePublicChat) {
		this.featurePublicChat = featurePublicChat;
	}

	public boolean isFeaturePublisher() {
		return featurePublisher;
	}

	public void setFeaturePublisher(boolean featurePublisher) {
		this.featurePublisher = featurePublisher;
	}

	public boolean isFeatureVedio() {
		return featureVedio;
	}

	public void setFeatureVedio(boolean featureVedio) {
		this.featureVedio = featureVedio;
	}

	public boolean isFeatureWhiteboard() {
		return featureWhiteboard;
	}

	public void setFeatureWhiteboard(boolean featureWhiteboard) {
		this.featureWhiteboard = featureWhiteboard;
	}

	public String getDefaultURLCS() {
		return defaultURLCS;
	}

	public void setDefaultURLCS(String defaultURLCS) {
		this.defaultURLCS = defaultURLCS;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public String getFeedbackEmail() {
		return feedbackEmail;
	}

	public void setFeedbackEmail(String feedbackEmail) {
		this.feedbackEmail = feedbackEmail;
	}

	public int getParticipantLimit() {
		return participantLimit;
	}

	public void setParticipantLimit(int participantLimit) {
		this.participantLimit = participantLimit;
	}

	public String getLocaleCS()
	{
		return localeCS;
	}

	public String getAttendeePasscode() {
		return attendeePasscode;
	}

	public void setAttendeePasscode(String attendeePasscode) {
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

	public boolean isParticipantListEnabled() {
		return participantListEnabled;
	}

	public boolean isDisplayDialInfo() {
		return displayDialInfo;
	}

	public void setDisplayDialInfo(boolean displayDialInfo) {
		this.displayDialInfo = displayDialInfo;
	}

	public void setParticipantListEnabled(boolean participantListEnabled) {
		this.participantListEnabled = participantListEnabled;
	}
	public boolean isAllowAttendeeInvites()
	{
		return allowAttendeeInvites;
	}
	public void setAllowAttendeeInvites(boolean allowAttendeeInvites)
	{
		this.allowAttendeeInvites = allowAttendeeInvites;
	}
	public boolean isAssignMikeOnJoin()
	{
		return assignMikeOnJoin;
	}
	public void setAssignMikeOnJoin(boolean assignMikeOnJoin)
	{
		this.assignMikeOnJoin = assignMikeOnJoin;
	}
	public boolean isHandsFreeOnLoad()
	{
		return handsFreeOnLoad;
	}
	public void setHandsFreeOnLoad(boolean handsFreeOnLoad)
	{
		this.handsFreeOnLoad = handsFreeOnLoad;
	}
	/*public boolean isVideoOnly()
	{
		return videoOnly;
	}
	public void setVideoOnly(boolean videoOnly)
	{
		this.videoOnly = videoOnly;
	}
*/
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

	public boolean isFeatureRecording() {
		return featureRecording;
	}

	public void setFeatureRecording(boolean featureRecording) {
		this.featureRecording = featureRecording;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
}
