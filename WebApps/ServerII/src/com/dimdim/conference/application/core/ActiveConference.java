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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.application.core;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.presentation.dms.URLHelper;
import com.dimdim.conference.config.ConferenceConfig;
import com.dimdim.conference.model.Conference;
import com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.model.ConferenceNotActiveException;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IAudioVideoManager;
import com.dimdim.conference.model.IChatManager;
import com.dimdim.conference.model.IClientEventPublisher;
import com.dimdim.conference.model.ICobrowseManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IPermissionsManager;
import com.dimdim.conference.model.IRecordingManager;
import com.dimdim.conference.model.IResourceManager;
import com.dimdim.conference.model.IResourceObject;
import com.dimdim.conference.model.IRosterManager;
import com.dimdim.conference.model.IStreamingServer;
import com.dimdim.conference.model.IWhiteboardManager;
import com.dimdim.conference.model.MaximumParticipantsReached;
import com.dimdim.conference.model.Participant;
import com.dimdim.conference.model.UserInConferenceException;
import com.dimdim.conference.model.UserNotInConferenceException;
import com.dimdim.conference.model.UserRemovedFromConferenceException;
import com.dimdim.conference.model.UserRosterStateMachine;
import com.dimdim.messaging.IMessageEngine;
import com.dimdim.messaging.MessageEngineFactory;
import com.dimdim.recording.MeetingRecorder;


/**
 * @author Jayant Pandit
 * @email  Jayant.Pandit@communiva.com
 */
public class ActiveConference	implements IConference
{
	
	protected	Conference		conference;
	protected	String			conferenceId;
	protected	ActiveConferenceClock	conferenceTimer;
	
//	protected	String		state = IConference.CONF_STATE_NOT_RUNNING;
	protected	int			state = IConference.CONF_STATE_ACTIVE;
//	protected	int			currentState = ActiveConferenceStateMachine.STATE_STARTING;
//	protected	ActiveConferenceStateMachine	acsm;
	
	protected	boolean		lobbyEnabled = false;
	protected	boolean		participantListEnabled = true;
	protected	boolean		assistantEnabled = true;
	protected	boolean		publicChatEnabled = true;
	protected	boolean		privateChatEnabled = true;
	protected	boolean		publisherEnabled = true;
	protected	boolean		whiteboardEnabled = true;
	protected	boolean		pptEnabled = true;
	protected	boolean		recordingEnabled = false;
	
	protected	int			maxMeetingTimeMinutes = 20;
	protected	long		meetingExpiryTime;
	protected	long		startTime = System.currentTimeMillis();
	protected	int			maxParticipants = 5;
	protected	int			participantLimit = -1;
	protected	String		presenterAV = ConferenceConstants.MEETING_AV_TYPE_AUDIO;
	protected	int			maxAttendeeMikes = ConferenceConsoleConstants.getMaxAttendeeAudios();
	protected	int			maxAttendeeVideos = ConferenceConsoleConstants.getMaxAttendeeVideos();
	protected	String		returnUrl = "";
	protected	String		dmsServerAddress = "";
	
	protected	String		preseterPwd = "";
	protected	String		attendeePwd = "";
//	protected	Vector		stateListeners = new Vector();
	protected	Vector		removedUserEmails = new Vector();
	protected	Vector		activeConferenceStateChangeListeners = new Vector();
	
	/**
	 * The rtmp urls.
	 */
	protected	StreamingServerInterface	streamingServerInterface;
	
	protected	IMessageEngine			messageEngine;
	protected	ClientEventPublisher	clientEventPublisher;
	protected	ParticipantStateChangeHelper	participantStateChangeHelper;
	
	protected	ConferenceManager	conferenceManager;
	protected	RosterManager		rosterManager;
	protected	ResourceManager		resourceManager;
	protected	IChatManager		chatManager;
	
	protected	ActiveVideoManager		activeVideoManager;
	protected	CurrentSettingsManager	currentSettingsManager;
	protected	PermissionsManager		permissionsManager;
	protected	WhiteboardManager		whiteboardManager;
	protected	CobrowseManager			cobrowseMAnager;
	protected	RecordingManager		recordingManager;
	private 	String 					defaultUrl = "";
	private 	String 					headerText = "";
	private 	String					logo = "";
	private		String					feedBackEmail = null;
	private		Locale					confLocale = null;
	private		String				joinUrl;
	private		String				reJoinUrl;
	//private String dialInInfo = "";
	
	String tollFree = "";
	String toll = "";
	String internTollFree = "";
	String internToll = "";
	String moderatorPassCode = "";
	String attendeePasscode = "";
	boolean dialInfoVisible = true;
	private String hostId; 
	
	protected	boolean		assignMikeOnJoin = false;
	protected	boolean		handsFreeOnLoad = false;
	protected	boolean		allowAttendeeInvites = false;
	protected	boolean		featureRecording = true;
	protected	boolean		featureDoc = true;
	protected	boolean		featureCob = true;
	
	/**
	 * @param confKey 
	 * 
	 */
	public ActiveConference(String email, String displayName, String hostId, ConferenceConfig confConfig, String conferenceId)
		throws	StreamingServerTooBusy
	{
		super();
		System.out.println("Creating conference: "+confConfig.getConferenceKey());
		this.dmsServerAddress = ConferenceConsoleConstants.getDMServerAddress();
//		this.acsm = new ActiveConferenceStateMachine(this);
		this.conference = new Conference(this,confConfig);
		if (conferenceId == null)
		{
			this.conferenceId = UUID.randomUUID().toString();
		}
		else
		{
			this.conferenceId = conferenceId;
		}
//		this.conference.getUserRoster().setParticipantStateChangeListener(this);
		
		this.messageEngine = MessageEngineFactory.getFactory(ConferenceConsoleConstants.getMaxObjectsInOneEvent()).getMessageEngine();
		this.clientEventPublisher = new ClientEventPublisher(this,this.messageEngine);
		this.participantStateChangeHelper = new ParticipantStateChangeHelper(this);
		this.permissionsManager = new PermissionsManager(this);
		this.conference.getUserRoster().addParticipantChangeListener(this.participantStateChangeHelper);
		
		this.rosterManager = new RosterManager(this);
		this.resourceManager = new ResourceManager(this);
		this.chatManager = new ChatManager(this);
		
		this.currentSettingsManager = new CurrentSettingsManager(this);
		this.activeVideoManager = new ActiveVideoManager(this);
		this.whiteboardManager = this.resourceManager.getActiveResourceManager().getWhiteboardManager();
		this.cobrowseMAnager = this.resourceManager.getActiveResourceManager().getCobrowseMAnager();
		this.recordingManager = new RecordingManager(this);
		try
		{
			Participant organizer = (Participant)this.conference.getUserRoster().addParticipant(email,
					displayName,email,ConferenceConstants.ROLE_ACTIVE_PRESENTER,this.lobbyEnabled, hostId);
			organizer.setStatus(ConferenceConstants.STATUS_HOST);
			this.conference.setOrganizer(organizer);
			this.hostId = hostId;
			String key = confConfig.getConferenceKey();
			this.streamingServerInterface = new StreamingServerInterface(key);
			this.streamingServerInterface.conferenceStarted(
					ConferenceConsoleConstants.getMaxParticipantsPerConference(),
					ConferenceConsoleConstants.getMaxMeetingLengthMinutes());
			
			String serverAddress = ConferenceConsoleConstants.getServerAddress();
			String webappName = ConferenceConsoleConstants.getWebappName();
			joinUrl = serverAddress+"/"+webappName+
				"/GetJoinConferenceForm.action?meetingRoomName="+key;
			reJoinUrl = serverAddress+"/"+webappName+
				"/html/envcheck/connect.action?action=join&confKey="+key;
		}
		catch(UserInConferenceException uice)
		{
			//	We know for certain that this is the first user being added.
		}
		catch(StreamingServerTooBusy sstb)
		{
			throw	sstb;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public	void	addMeetingRecorder(MeetingRecorder meetingRecorder)
	{
		meetingRecorder.setIndex(this.rosterManager.getUserRoster().getUserCounter());
		this.clientEventPublisher.addMeetingRecorder(meetingRecorder);
		this.recordingEnabled = true;
		this.recordingManager.setMeetingRecorder(meetingRecorder);
	}
	public	boolean	isRecordingEnabled()
	{
		return	this.recordingEnabled;
	}
	public	Participant	getHost()
	{
		return	this.conference.getOrganizer();
	}
	
	public	Participant	getActivePresenter()
	{
		return this.conference.getUserRoster().getActivePresenter();
	}
	
	public	String	getConferenceId()
	{
		return	conferenceId;
	}
	
	public	boolean	isActive()
	{
		return	state == IConference.CONF_STATE_ACTIVE;
	}
	public	boolean	isActivePresenter(Participant participant)
	{
		return	this.rosterManager.getUserRoster().getActivePresenter() == participant;
	}
	public	long	getStartTimeMillis()
	{
		return	this.startTime;
	}
	public String getJoinUrl()
	{
		return joinUrl;
	}
	public String getJoinUrlEncoded()
	{
		String retVal = "";
		try{
			retVal = URLEncoder.encode(joinUrl, "utf-8");
		}catch (Exception e) {
			System.out.println("join url has some prob could not encode it");
		}
		return retVal;
	}
	public String getRejoinUrl()
	{
		return reJoinUrl;
	}
	public void setJoinUrl(String joinUrl)
	{
		this.joinUrl = joinUrl;
	}
	public	IConferenceParticipant	getOrganizer()
	{
		return	this.conference.getOrganizer();
	}
	public	void	setConferenceOptions(boolean enableLobby, boolean participantListEnabled, String organizerNetworkProfile,
			String organizerDisplayImageQuality, Integer meetingTime, Integer maxParticipants, Integer participantLimit,
			String presenterAV, Integer maxAttendeeMikes, Integer maxAttendeeVideos, String returnUrl, boolean isAssistantEnabled, boolean isPublicChatOn,
			boolean isPrivateChatOn, boolean isPublisherEnabled, boolean isWhiteboardEnabled, 
			boolean pptEnabled, String defaultUrl, String headerText,String tollFree, String toll, String internTollFree, String internToll,
			String moderatorPassCode, String attendeePasscode, boolean showDialInfo, String presenterPwd, String attendeePwd,
			 boolean assignMikeOnJoin, boolean handsFreeOnLoad, boolean allowAttendeeInvites, boolean featureRecording
			, boolean featureCob, boolean featureDoc)
	{
		if (organizerDisplayImageQuality == null)
		{
			organizerDisplayImageQuality = "medium";
		}
		if (organizerNetworkProfile == null)
		{
			organizerNetworkProfile = "2";
		}
		if (presenterAV == null)
		{
			presenterAV = this.presenterAV;
		}
		this.lobbyEnabled = enableLobby;
		this.participantListEnabled = participantListEnabled;
		this.assistantEnabled = isAssistantEnabled;
		this.conference.getOrganizer().setNetProfile(organizerNetworkProfile);
		this.conference.getOrganizer().setImgQuality(organizerDisplayImageQuality);
		this.maxMeetingTimeMinutes = meetingTime.intValue();
		this.presenterAV = presenterAV;
		//if null use the one in properties file
		if (maxAttendeeMikes != null)
		{
			this.maxAttendeeMikes = maxAttendeeMikes.intValue();
		}
		if (maxAttendeeVideos != null)
		{
			this.maxAttendeeVideos = maxAttendeeVideos.intValue();
		}
		overRideMikesNVideos();
		//feature control realted
		this.privateChatEnabled = isPrivateChatOn;
		this.publicChatEnabled = isPublicChatOn;
		this.publisherEnabled = isPublisherEnabled;
		this.whiteboardEnabled = isWhiteboardEnabled;
		this.pptEnabled = pptEnabled;
		
		if (this.maxMeetingTimeMinutes > ConferenceConsoleConstants.getMaxMeetingLengthMinutes())
		{
			this.maxMeetingTimeMinutes = ConferenceConsoleConstants.getMaxMeetingLengthMinutes();
		}
		meetingExpiryTime = this.startTime + (this.maxMeetingTimeMinutes*60*1000);
		this.maxParticipants = maxParticipants.intValue();
		
		if (null != participantLimit && 0 != participantLimit.intValue())
		{
			this.participantLimit = participantLimit.intValue();
		}
		overRideMaxPart();
		
		if (this.conferenceTimer == null)
		{
			this.conferenceTimer = new ActiveConferenceClock(this,this.maxMeetingTimeMinutes);
		}
		else
		{
			this.conferenceTimer.setMeetingTimeMinutes(this.maxMeetingTimeMinutes);
		}
		
		this.defaultUrl  = defaultUrl; 
		if(null != headerText && headerText.length() > 0)
		{
			this.headerText = headerText;
		}
		if (returnUrl != null && !returnUrl.equals(this.returnUrl))
		{
			this.returnUrl = returnUrl;
			
			Event event = new Event(ConferenceConstants.FEATURE_CONF,
					ConferenceConstants.EVENT_CONFERENCE_TRACKBACK_URL,
					new Date(), ConferenceConstants.RESPONSE_OK,
					this.returnUrl );
			this.clientEventPublisher.dispatchEventToAllClients(event);
		}
		/*if (this.presenterAV.equals(ConferenceConstants.MEETING_AV_TYPE_VIDEO_CHAT))
		{
			this.maxAttendeeMikes = 1;
		}*/
		
		this.dialInfoVisible = showDialInfo;
		this.tollFree = tollFree; 
		this.toll = toll;
		this.internTollFree = internTollFree; 
		this.internToll = internToll;
		this.moderatorPassCode = moderatorPassCode; 
		this.attendeePasscode = attendeePasscode;
		
		this.preseterPwd = presenterPwd;
		this.attendeePwd = attendeePwd;
		if (this.attendeePwd != null && this.attendeePwd.length() > 0)
		{
			this.joinUrl = this.joinUrl + "&attendeePwd="+attendeePwd;
			this.reJoinUrl = this.reJoinUrl + "&attendeePwd="+attendeePwd;
		}
		//this.videoOnly = videoOnly;
		this.assignMikeOnJoin = assignMikeOnJoin;
		this.handsFreeOnLoad = handsFreeOnLoad;
		this.allowAttendeeInvites = allowAttendeeInvites;
		this.featureRecording = featureRecording;
		boolean cobEnabled = ConferenceConsoleConstants.getResourceKeyValue("dimdim.show.cob", "true").equalsIgnoreCase("true");
		cobEnabled = cobEnabled && featureCob;
		this.featureCob = cobEnabled;
		this.featureDoc = featureDoc;
	}
	private void overRideMaxPart() {
		//if over-ride is true then ignore else if these numbers are bigger than
		//the numbers mentioned in props bring them back.
		if (this.maxParticipants > ConferenceConsoleConstants.getMaxParticipantsPerConference()
				&& !ConferenceConsoleConstants.getOverrideMaxParticipants().equalsIgnoreCase("true"))
		{
			this.maxParticipants = ConferenceConsoleConstants.getMaxParticipantsPerConference();
		}
		if (this.participantLimit > ConferenceConsoleConstants.getMaxParticipantsPerConference()
				&& !ConferenceConsoleConstants.getOverrideMaxParticipants().equalsIgnoreCase("true"))
		{
			this.participantLimit = ConferenceConsoleConstants.getMaxParticipantsPerConference();
		}
		//if participant limit is less then the max participant make it same, else in ui combo box that gets shown 
		//will have a lesser number than expected.
		if(this.participantLimit < this.maxParticipants)
		{
			this.participantLimit = this.maxParticipants;
		}
	}
	private void overRideMikesNVideos() {
		if(ConferenceConstants.MEETING_AV_TYPE_AUDIO.equalsIgnoreCase(this.presenterAV)
				|| ConferenceConstants.MEETING_AV_TYPE_DISABLED.equalsIgnoreCase(this.presenterAV))
		{
			this.maxAttendeeVideos = 0;
		}
		if(ConferenceConstants.MEETING_AV_TYPE_VIDEO.equalsIgnoreCase(this.presenterAV)
				|| ConferenceConstants.MEETING_AV_TYPE_DISABLED.equalsIgnoreCase(this.presenterAV))
		{
			this.maxAttendeeMikes = 0;
		}
	}
	public	void	startClock()
	{
		if (this.conferenceTimer != null)
		{
			this.conferenceTimer.startClock();
		}
	}
	public	void	setConferenceOptions(boolean enableLobby, String organizerNetworkProfile,
			String organizerDisplayImageQuality, Integer meetingTime, Integer maxParticipants,
			String presenterAV, Integer maxAttendeeMikes, String returnUrl, boolean isAssistantEnabled)
	{
		if (organizerDisplayImageQuality == null)
		{
			organizerDisplayImageQuality = "medium";
		}
		if (organizerNetworkProfile == null)
		{
			organizerNetworkProfile = "2";
		}
		if (presenterAV == null)
		{
			presenterAV = this.presenterAV;
		}
		
		if(this.lobbyEnabled  != enableLobby)
		{
			this.lobbyEnabled = enableLobby;
			this.currentSettingsManager.lobbySettingChanged();
			System.out.println("...................settingenableLobby = "+enableLobby);
		}
		this.assistantEnabled = isAssistantEnabled;
		this.conference.getOrganizer().setNetProfile(organizerNetworkProfile);
		this.conference.getOrganizer().setImgQuality(organizerDisplayImageQuality);
		try
		{
			this.rosterManager.getRosterObject().updateDisplayName(this.conference.getOrganizer().getId(),
				this.conference.getOrganizer().getDisplayName());
		}
		catch(Exception e)
		{
			
		}
		this.maxMeetingTimeMinutes = meetingTime.intValue();
		this.presenterAV = presenterAV;
//		if null use the one in properties file
		if (maxAttendeeMikes != null)
		{
			this.maxAttendeeMikes = maxAttendeeMikes.intValue();
		}
		overRideMikesNVideos();
		if (this.maxMeetingTimeMinutes > ConferenceConsoleConstants.getMaxMeetingLengthMinutes())
		{
			this.maxMeetingTimeMinutes = ConferenceConsoleConstants.getMaxMeetingLengthMinutes();
		}
		meetingExpiryTime = this.startTime + (this.maxMeetingTimeMinutes*60*1000);
		this.maxParticipants = maxParticipants.intValue();
		overRideMaxPart();
		
		if (this.conferenceTimer == null)
		{
			this.conferenceTimer = new ActiveConferenceClock(this,this.maxMeetingTimeMinutes);
		}
		else
		{
			this.conferenceTimer.setMeetingTimeMinutes(this.maxMeetingTimeMinutes);
		}
		Event timeEvent = new Event(ConferenceConstants.FEATURE_CONF,
				ConferenceConstants.EVENT_CONFERENCE_TIME_CHANGE,
				new Date(), ConferenceConstants.RESPONSE_OK,
				this.maxMeetingTimeMinutes+"" );
		
		Event partCountChangeEvent = new Event(ConferenceConstants.FEATURE_CONF,
				ConferenceConstants.EVENT_CONFERENCE_PARTICIPANTS_COUNT_CHANGE,
				new Date(), ConferenceConstants.RESPONSE_OK,
				this.maxParticipants+"" );
		
		this.clientEventPublisher.dispatchEventToAllClients(timeEvent);
		this.clientEventPublisher.dispatchEventToAllClients(partCountChangeEvent);
		
		
		if (returnUrl != null && !returnUrl.equals(this.returnUrl))
		{
			this.returnUrl = returnUrl;
			
			Event event = new Event(ConferenceConstants.FEATURE_CONF,
					ConferenceConstants.EVENT_CONFERENCE_TRACKBACK_URL,
					new Date(), ConferenceConstants.RESPONSE_OK,
					this.returnUrl );
			this.clientEventPublisher.dispatchEventToAllClients(event);
		}
	}
	public	int		getMaxParticipants()
	{
		return	this.maxParticipants;
	}
	public	int		getMaxMeetingTime()
	{
		return	this.maxMeetingTimeMinutes;
	}
	public	IStreamingServer	getStreamingServer()
	{
		return	this.streamingServerInterface;
	}
	public boolean isLobbyEnabled()
	{
		return lobbyEnabled;
	}
	public String getPresenterAV()
	{
		return presenterAV;
	}
	public int getMaxAttendeeMikes()
	{
		return maxAttendeeMikes;
	}
	public int getMaxAttendeeVideos()
	{
		return maxAttendeeVideos;
	}
	public String getReturnUrl()
	{
		if(null != returnUrl && returnUrl.length() > 0)
			return returnUrl;
		
		return ConferenceConsoleConstants.getTrackbackURL();
	}
	public void setLobbyEnabled(boolean lobbyEnabled)
	{
		this.lobbyEnabled = lobbyEnabled;
	}
	public String getConferenceKey()
	{
		return	this.conference.getConferenceConfig().getConferenceKey();
	}
	public	Conference	getConference()
	{
		return	this.conference;
	}
	public ActiveVideoManager getActiveVideoManager()
	{
		return activeVideoManager;
	}
	public IWhiteboardManager getWhiteboardManager()
	{
		return this.whiteboardManager;
	}
	public IRecordingManager getRecordingManager()
	{
		return this.recordingManager;
	}
	public	IClientEventPublisher	getClientEventPublisher()
	{
		return	this.clientEventPublisher;
	}
//	public int getCurrentState()
//	{
//		return currentState;
//	}
	public IPermissionsManager getParticipantPermissions()
	{
		return permissionsManager;
	}
	public	ConferenceInfo	getConferenceInfo()
	{
		return	this.conference.getConferenceInfo();
	}
	//	On rejoin the host's role should be picked from the current host object.
	public	synchronized	IConferenceParticipant	rejoinHostToConference(String email,
			String displayName, String userId, String password, String role)
		throws	UserInConferenceException, ConferenceNotActiveException,
			UserRemovedFromConferenceException, MaximumParticipantsReached
	{
		if (this.state != IConference.CONF_STATE_ACTIVE)
		{
			throw	new	ConferenceNotActiveException();
		}
//		this.conference.getUserRoster().throwExceptionIfUserJoined(email);
		
		Participant newUser = (Participant)this.conference.getUserRoster().
				addParticipant(email,displayName,password,role,false, userId);
		
		newUser.setCurrentState(UserRosterStateMachine.STATE_REJOINING);
		newUser.setStatus(ConferenceConstants.STATUS_HOST);
		
//		Participant currentHost = this.getHost();
		this.conference.setOrganizer(newUser);
		if (role.equals(ConferenceConstants.ROLE_ACTIVE_PRESENTER))
		{
			this.conference.getUserRoster().setActivePresenter(newUser);
		}
//		try
//		{
//			this.removeUserFromConference(currentHost);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		
		return	newUser;
	}
	public	synchronized	IConferenceParticipant	addUserToConference(String email,
			String displayName, String userId, String password, String role)
		throws	UserInConferenceException, ConferenceNotActiveException,
			UserRemovedFromConferenceException, MaximumParticipantsReached
	{
		if (this.state != IConference.CONF_STATE_ACTIVE)
		{
			throw	new	ConferenceNotActiveException();
		}
//		this.conference.getUserRoster().throwExceptionIfUserJoined(email);
		if (this.removedUserEmails.contains(email))
		{
			throw	new	UserRemovedFromConferenceException();
		}
		if (!hasSpaceForUser())
		{
			throw	new	MaximumParticipantsReached();
		}
		IConferenceParticipant newUser = this.conference.getUserRoster().
				addParticipant(email,displayName,password,role,this.lobbyEnabled, userId);
		
		return	newUser;
	}
	public	boolean	hasSpaceForUser()
	{
		//+1 is for the host who started the meeting
		if (this.conference.getUserRoster().getTotalNumberOfParticipants() >= (this.maxParticipants + 1))
		{
			return	false;
		}
		return	true;
	}
	public void addParticipantSession(IConferenceParticipant user, UserSession session)
	{
		this.participantStateChangeHelper.addUserSession(user,session);
		this.addParticipantSession(session);
	}
	/**
	 * User roster removes ensures that the participant is removed from the
	 * message engine as well so that that session receives no more events.
	 */
	public	synchronized	void	ejectUserFromConference(IConferenceParticipant user)
		throws	UserNotInConferenceException
	{
		this.removedUserEmails.add(user.getId());
		this.conference.getUserRoster().participantRemoved(user);
	}
	public	synchronized	void	removeUserFromConference(IConferenceParticipant user)
		throws	UserNotInConferenceException
	{
		this.participantStateChangeHelper.removeUserSession(user);
		this.conference.getUserRoster().participantLeaving(user);
	}
	public	synchronized	void	userLeftConference(IConferenceParticipant user)
	{
		if (user.getId().equals(this.conference.getOrganizer().getId()))
		{
			//	The organizer has left the conference. Close the conference.
			//	Conference closed event will be received by the conference
			//	manager and the conference will be removed from the system.
			//	This will also clean up the presentations uploaded to the
			//	conference.
			if (this.rosterManager.getRosterObject().getNumberOfParticipants() > 0)
			{
				Event event = new Event(ConferenceConstants.FEATURE_CONF,
						ConferenceConstants.EVENT_CONFERENCE_CLOSED,
						new Date(), ConferenceConstants.RESPONSE_OK,
						this.getConferenceKey() );
				this.clientEventPublisher.dispatchEventToAllClients(event);
			}
			this.endConference();
		}
		else if (this.conference.getUserRoster().getNumberOfParticipants() == 0 && this.isActive())
		{
			//	Close the conference. This will raise only local events.
			System.out.println("Number of participants zero");
//			this.endConference();
		}
	}
	
	/**
	 * The get console request essentially dispatches the current state of the conference
	 * to a newly joined attendee. This means, send to the new attendee, in the order:
	 * 
	 * 1. Current complete user and resource rosters.
	 * 2. Current state of the av stream
	 * 3. Current active resource state, if one is active
	 * 4. Last resource control event, if exists
	 * 5. Last control message for av, if exists
	 */
	
	protected	synchronized	void	userInConference(IConferenceParticipant user)
	{
		this.messageEngine.addEventReceiver(user.getEventReceiver());
		
		List fullRoster = null;
		if(participantListEnabled)
		{
			fullRoster = this.conference.getUserRoster().getParticipants();
		}else{
			fullRoster = new ArrayList();
			fullRoster.add(getHost());
		}
		Event fullRosterEvent = new Event(ConferenceConstants.FEATURE_ROSTER,
				ConferenceConstants.EVENT_PARTICIPANTS_LIST,
				new Date(), ConferenceConstants.RESPONSE_OK, fullRoster );
		this.clientEventPublisher.dispatchEventTo(fullRosterEvent,user);
		
		List resourceList = this.conference.getResourceRoster().getResources();
		Event resourceListEvent = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
				ConferenceConstants.EVENT_RESOURCES_LIST,
				new Date(), ConferenceConstants.RESPONSE_OK, resourceList );
		this.clientEventPublisher.dispatchEventTo(resourceListEvent,user);
		
		IResourceObject ro = this.resourceManager.getActiveResourceManager().getCurrentActiveResource();
		if (ro != null)
		{
			//	Send the resource selected event and the last control event to
			//	the new attendee.
			Event event1 = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
					ConferenceConstants.EVENT_RESOURCE_SELECTED,
					new Date(), ConferenceConstants.RESPONSE_OK, ro );
			this.clientEventPublisher.dispatchEventTo(event1,user);
			
			Event event2 = this.resourceManager.getActiveResourceManager().getLastControlEvent();
			if (event2 != null)
			{
				this.clientEventPublisher.dispatchEventTo(event2,user);
			}
		}
		Event event = this.activeVideoManager.getLastControlEvent();
		if (event != null)
		{
			this.clientEventPublisher.dispatchEventTo(event,user);
		}
		List audioEvents = this.activeVideoManager.getLastAudioControlEvents();
		System.out.println("Audio events list in active conference:"+audioEvents);
		if (audioEvents != null)
		{
			int size = audioEvents.size();
			for (int i=0; i<size; i++)
			{
				Event audioEvent = (Event)audioEvents.get(i);
				System.out.println("Sending audio event from active conference: "+audioEvent);
				this.clientEventPublisher.dispatchEventTo(audioEvent,user);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#cleanup()
	 */
	public void cleanup()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#getConfig()
	 */
	public ConferenceConfig getConfig()
	{
		return this.conference.getConferenceConfig();
	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#startConference()
	 */
	public void startConference()
	{
		int	oldState = this.state;
		state = IConference.CONF_STATE_ACTIVE;
//		this.acsm.changeState(ActiveConferenceStateMachine.STATE_ACTIVE);
//		this.fireStateChanged(oldState,state);
		this.conferenceManager.conferenceStateChanged(this, oldState, state);
	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#endConference()
	 */
	public void endConference()
	{
		System.out.println("ActiveConference::endConference");
		int	oldState = this.state;
		state = IConference.CONF_STATE_CLOSED;
//		this.fireStateChanged(oldState,state);
//		this.acsm.changeState(ActiveConferenceStateMachine.STATE_CLOSED);
		this.conferenceTimer.setConferenceClosed();
		this.conferenceManager.conferenceStateChanged(this, oldState, state);
	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#activateLobby()
	 */
	public void activateLobby()
	{
		//	This feature is not yet developed.
	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#getState()
	 */
//	public String getState()
//	{
//		return state;
//	}
//	public void setState(String state)
//	{
//		this.state = state;
//	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#addStateListener(com.dimdim.conference.IConferenceStateListener)
	 */
//	public void addStateListener(IConferenceStateListener listener)
//	{
//		stateListeners.add(listener);
//	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#removeStateListener(com.dimdim.conference.IConferenceStateListener)
	 */
//	public void removeStateListener(IConferenceStateListener listener)
//	{
//		stateListeners.remove(listener);
//	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#fireStateChanged(java.lang.String, java.lang.String)
	 */
//	protected void fireStateChanged(String oldState, String newState)
//	{
//		for(int i = 0; i < stateListeners.size(); i++)
//		{
//			IConferenceStateListener listener = (IConferenceStateListener)stateListeners.get(i);
//			listener.conferenceStateChanged(this,oldState,newState);
//		}
//	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#getRosterManager()
	 */
	public IRosterManager getRosterManager()
	{
		return this.rosterManager;
	}
	public IResourceManager	getResourceManager()
	{
		return	this.resourceManager;
	}
	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#getChatManager()
	 */
	public IChatManager getChatManager()
	{
		return this.chatManager;
	}
	public	IAudioVideoManager	getAudioVideoManager()
	{
		return	this.activeVideoManager;
	}
	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#getParticipant(java.lang.String)
	 */
	public IConferenceParticipant getParticipant(String userId)
	{
		return this.getRosterManager().getRosterObject().getParticipant(userId);
	}
	
	
	public String getNewDisplayName()
	{
		int count = this.getRosterManager().getRosterObject().getNumberOfParticipants();
		return "Attendee"+(count+1);
	}
	
	/* (non-Javadoc)
	 * @see com.dimdim.conference.IConference#getParticipants(java.lang.String)
	 */
	public List getParticipants(String role)
	{
		return this.getRosterManager().getRosterObject().getParticipants(role);
	}
	public List getParticipants()
	{
		return this.getRosterManager().getRosterObject().getParticipants();
	}
	
	/**
	 * Active conference state machine change listeners
	 */
//	public void addActiveConferenceStateChangeListener(ActiveConferenceStateChangeListener listener)
//	{
//		this.activeConferenceStateChangeListeners.add(listener);
//	}
//	public void removeActiveConferenceStateChangeListener(ActiveConferenceStateChangeListener listener)
//	{
//		this.activeConferenceStateChangeListeners.remove(listener);
//	}
	public Vector getActiveConferenceStateChangeListeners()
	{
		return activeConferenceStateChangeListeners;
	}
	/**
	 * Following methods are used by the session manager which monitors the activity
	 * of user sessions to notify the conference of the significant state changes in
	 * user sessions, which affect the state of the conference.
	 */
//	public	void	presenterSessionTimedOut(IConferenceParticipant presenter)
//	{
//		int numPresenters = this.rosterManager.getRosterObject().getNumberOfPresenters();
//		if (numPresenters == 0)
//		{
//			this.acsm.changeState(ActiveConferenceStateMachine.STATE_PASSIVE);
//		}
//	}
//	public	void	presenterJoined(IConferenceParticipant presenter)
//	{
//		this.acsm.changeState(ActiveConferenceStateMachine.STATE_ACTIVE);
//	}
//	public	void	presenterLeft(IConferenceParticipant presenter)
//	{
//		int numPresenters = this.rosterManager.getRosterObject().getNumberOfPresenters();
//		if (numPresenters == 0)
//		{
//			this.acsm.changeState(ActiveConferenceStateMachine.STATE_CLOSED);
//		}
//	}
	/**
	 * Roster state machine: Participant state change listener implementation.
	 */
//	public void participantInConference(Participant user, int oldState)
//	{
//		if (user.isInConference())
//		{
//			this.userInConference(user);
//		}
//	}
//	public void participantLeftConference(Participant user, int oldState)
//	{
//		//	The user left event is already dispatched by the roster.
//		userLeftConference(user);
//	}
	
	public ConferenceManager getConferenceManager()
	{
		return conferenceManager;
	}
	public void setConferenceManager(ConferenceManager conferenceManager)
	{
		this.conferenceManager = conferenceManager;
	}
	/**
	 * Part of the new implementation for meeting lobby management.
	 */
	/**
	 * Once the console is loaded a new user is ready to accept the events.
	 */
	public	synchronized	void	consoleLoaded(IConferenceParticipant user)
	{
		this.messageEngine.addEventReceiver(user.getEventReceiver());
		Participant previousHost = this.conference.getUserRoster().getPreviousOrganizer();
		if (previousHost != null && ((Participant)user).isHost())
		{
			//	The previous host is set only when the host rejoins from another
			//	location. If this point is reached it would mean that the host
			//	has rejoined while the console was being reloaded or loaded.
			previousHost.setStatus(ConferenceConstants.STATUS_PREVIOUS_HOST);
		}
		this.conference.getUserRoster().participantConsoleLoaded(user,this.lobbyEnabled);
	}

	public	synchronized	void	consoleReloaded(IConferenceParticipant user)
	{
		Participant previousHost = this.conference.getUserRoster().getPreviousOrganizer();
		if (previousHost != null && ((Participant)user).isHost())
		{
			//	The previous host is set only when the host rejoins from another
			//	location. If this point is reached it would mean that the host
			//	has rejoined while the console was being reloaded or loaded.
			previousHost.setStatus(ConferenceConstants.STATUS_PREVIOUS_HOST);
		}
		this.conference.getUserRoster().participantConsoleReloaded(user,this.lobbyEnabled);
	}

	public	synchronized	void	grantWaitingUserEntry(String userId)
	{
		this.conference.getUserRoster().grantUserEntry(userId);
	}
	
	public	synchronized	void	denyWaitingUserEntry(String userId)
	{
		this.conference.getUserRoster().denyUserEntry(userId);
	}
	
	public	synchronized	void	grantAllWaitingUsersEntry()
	{
		this.conference.getUserRoster().grantAllUsersEntry();
	}
	
	public	synchronized	void	getConsole(IConferenceParticipant user)
	{
		this.conference.getUserRoster().acceptedUserRequestingConsole(user);
	}
	
	/**
	 * Enabling lobby has no impact on the current participants of the meeting.
	 * Hence this is simply a flag.
	 */
	public	synchronized	void	enableLobby()
	{
		this.lobbyEnabled = true;
		this.currentSettingsManager.lobbySettingChanged();
	}
	/**
	 * When the lobby is disabled, any and all users waiting in lobby will be
	 * granted entry into the conference.
	 */
	public	synchronized	void	disableLobby()
	{
		this.lobbyEnabled = false;
		this.currentSettingsManager.lobbySettingChanged();
	}
	public boolean isAssistantEnabled() {
		return assistantEnabled;
	}
	public void setAssistantEnabled(boolean assistantEnabled) {
		this.assistantEnabled = assistantEnabled;
	}
	public boolean isPrivateChatEnabled() {
		return privateChatEnabled;
	}
	public void setPrivateChatEnabled(boolean privateChatEnabled) {
		this.privateChatEnabled = privateChatEnabled;
	}
	public boolean isPublicChatEnabled() {
		return publicChatEnabled;
	}
	public void setPublicChatEnabled(boolean publicChatEnabled) {
		this.publicChatEnabled = publicChatEnabled;
	}
	public boolean isPublisherEnabled() {
		return publisherEnabled;
	}
	public void setPublisherEnabled(boolean publisherEnabled) {
		this.publisherEnabled = publisherEnabled;
	}
	public boolean isWhiteboardEnabled() {
		return whiteboardEnabled;
	}
	public void setWhiteboardEnabled(boolean whiteboardEnabled) {
		this.whiteboardEnabled = whiteboardEnabled;
	}
	public boolean isPptEnabled()
	{
		return pptEnabled;
	}
	public void setPptEnabled(boolean pptEnabled)
	{
		this.pptEnabled = pptEnabled;
	}
	public String getDefaultUrl() {
		return defaultUrl;
	}
	public String getLogo()
	{
		if (this.logo == null || this.logo.length() == 0)
		{
			this.logo = URLHelper.getLogoUrl(conference.getConferenceConfig().getConferenceKey(), hostId);
		}
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getFeedBackEmail() {
		return feedBackEmail;
	}
	public void setFeedBackEmail(String feedBackEmail) {
		this.feedBackEmail = feedBackEmail;
	}
	public Locale getConfLocale() {
		return confLocale;
	}
	public void setConfLocale(Locale confLocale) {
		this.confLocale = confLocale;
	}
	public int getParticipantLimit() {
		return participantLimit;
	}
	public	String	getDMServerAddress()
	{
		return	this.dmsServerAddress;
	}
	public String getPreseterPwd() {
		return preseterPwd;
	}
	public String getAttendeePwd() {
		return attendeePwd;
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
	public boolean isDialInfoVisible() {
		return dialInfoVisible;
	}
	public void setDialInfoVisible(boolean dialInfoVisible) {
		this.dialInfoVisible = dialInfoVisible;
	}
	public boolean isParticipantListEnabled() {
		return participantListEnabled;
	}
	public void setParticipantListEnabled(boolean participantListEnabled) {
		this.participantListEnabled = participantListEnabled;
	}
	public	void	forceStopActiveSharing(IConferenceParticipant presenter)
	{
		this.resourceManager.forceStopActiveSharing(presenter);
	}
	
	protected	HashMap		participantSessions	=	new HashMap();
	
	private	void	addParticipantSession(UserSession session)
	{
		this.participantSessions.put(session.getSessionKey(), session);
	}
	public	void	removeParticipantSession(String sessionKey)
	{
		this.participantSessions.remove(sessionKey);
	}
	public	UserSession	getParticipantSession(String sessionKey)
	{
		return	(UserSession)this.participantSessions.get(sessionKey);
	}
	
	public	UserSession	getHostSession()
	{
		Set keys = participantSessions.keySet();
		UserSession hostSession = null;
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			UserSession userSes = (UserSession)participantSessions.get(key);
			if(((Participant)userSes.getUser()).isHost())
			{
				hostSession = userSes;
			}
			
		}
		return	hostSession;
	}
	public ICobrowseManager getCobrowseManager() {
		return this.cobrowseMAnager;
	}
	/*public	boolean	isVideoOnly()
	{
		return	this.videoOnly;
	}*/
	public	boolean	isAssignMikeOnJoin()
	{
		return	this.assignMikeOnJoin;
	}
	public	boolean	isHandsFreeOnLoad()
	{
		return	this.handsFreeOnLoad;
	}
	public	boolean	isAllowAttendeeInvites()
	{
		return	this.allowAttendeeInvites;
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
}
