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
/*
 **************************************************************************
 *	File Name  : IConference.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.model;

import java.util.List;
import java.util.Locale;

import com.dimdim.conference.config.ConferenceConfig;
import com.dimdim.conference.model.UserRemovedFromConferenceException;

/**
 * This represents the basic conference interface
 * 
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public interface IConference
{
	
//	public static final String CONF_STATE_NOT_RUNNING = "conf.state.notRunning";
//	public static final String CONF_STATE_STARTED = "conf.state.started";
//	public static final String CONF_STATE_FINISHED = "conf.state.finished";
	
	public static final int CONF_STATE_STARTING = 0;
	public static final int CONF_STATE_ACTIVE = 1;
	public static final int CONF_STATE_PASSIVE = 2;
	public static final int CONF_STATE_CLOSED = 3;
	
//	public	String	getRtmptURL();
	
//	public	String	getAvRtmpURL();
	
	public	IStreamingServer	getStreamingServer();
	
	/**
	 * Add an attendee
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 *
	 */
	
	public	IConferenceParticipant	addUserToConference(String email,
		String displayName, String userId, String password, String role)
			throws	UserInConferenceException, ConferenceNotActiveException,
				UserRemovedFromConferenceException, MaximumParticipantsReached;
	
	/**
	 * Remove an attendee
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 *
	 */
	
	public	void	removeUserFromConference(IConferenceParticipant user)
				throws	UserNotInConferenceException;
	
	public	void	ejectUserFromConference(IConferenceParticipant user)
				throws	UserNotInConferenceException;

	/**
	 * cleanup the conference
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 *
	 */
	
	public void cleanup();
	
	/**
	 * 
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public ConferenceConfig getConfig();
	
	/**
	 * 
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public ConferenceInfo getConferenceInfo();
	
	/**
	 * 
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public void		getConsole(IConferenceParticipant user);
	
	/**
	 * start the conference. sets state to CONF_STATE_STARTED
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 *
	 */
	
	public void startConference();
	
	/**
	 * stop the conference. sets state to CONF_STATE_FINISHED
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 *
	 */
	
	public void endConference();
	
	/**
	 * add a conference state listener that gets notified every time conference state changes
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param listener
	 *
	 */
	
//	public void addStateListener(IConferenceStateListener listener);
	
	/**
	 * remove state listener
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param listener
	 *
	 */
	
//	public void removeStateListener(IConferenceStateListener listener);
	
	/**
	 * get the resource feature manager
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public	IResourceManager	getResourceManager();
	
	/**
	 * get the roster feature manager
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public	IRosterManager	getRosterManager();
	
	/**
	 * get chat manager 
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public	IChatManager		getChatManager();
	
	/**
	 * get audio video manager 
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public	IAudioVideoManager		getAudioVideoManager();
	
	/**
	 * get a participant from his user id
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param userId
	 * @return
	 *
	 */
	
	public	IConferenceParticipant	getParticipant(String userId);
	
	/**
	 * 
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	
	public	List getParticipants();
	
	/**
	 * get all configured participants for a role
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param role
	 * @return
	 *
	 */
	
	public	List getParticipants(String role);
	
	/**
	 * Lobby management and related interface.
	 */
	public	void	consoleLoaded(IConferenceParticipant user);
	
	public	void	consoleReloaded(IConferenceParticipant user);
	
	public	void	grantWaitingUserEntry(String userId);
	
	public	void	denyWaitingUserEntry(String userId);
	
	public	void	grantAllWaitingUsersEntry();
	
	public	boolean	isLobbyEnabled();
	public boolean isParticipantListEnabled();
	
	public	void	enableLobby();
	
	public	void	disableLobby();
	
	public boolean isAssistantEnabled();
	
	public	IPermissionsManager	getParticipantPermissions();
	
	public	int		getMaxParticipants();
	public 	int 	getParticipantLimit();
	
	public	int		getMaxMeetingTime();
	
	public	long	getStartTimeMillis();
	
	public	String	getPresenterAV();
	
	public	int		getMaxAttendeeMikes();
	public	int		getMaxAttendeeVideos();
	
	public	String	getReturnUrl();
	public	String	getFeedBackEmail();
	
	//for feature control
	public	boolean isPublicChatEnabled();
	public	boolean isPrivateChatEnabled();
	public	boolean isPublisherEnabled();
	public	boolean isWhiteboardEnabled();
	public	boolean	isPptEnabled();
	public	boolean	isRecordingEnabled();
	
	//for locale of a conference
	public Locale getConfLocale();
	public void setConfLocale(Locale confLocale);
	
	public String getDefaultUrl();
	public String getHeaderText();
	
	public String getLogo();
	
	//this is a new method which includes feature controls part
	public	void	setConferenceOptions(boolean enableLobby, boolean participantListEnabled, String organizerNetworkProfile,
			String organizerDisplayImageQuality, Integer meetingTime, Integer maxParticipants, Integer participantLimit,
			String presenterAV, Integer maxAttendeeMikes, Integer maxAttendeeVideos, String returnUrl, boolean assistantEnabled, boolean isPublicChatOn,
			boolean isPrivateChatOn, boolean isPublisherEnabled, boolean isWhiteboardEnabled, boolean pptEnabled, 
			String defaultUrl, String headerText,String tollFree, String toll, String internTollFree, String internToll,
			String moderatorPassCode, String attendeePasscode, boolean showDialInfo, String presenterPwd, String attendeePwd,
			boolean assignMikeOnJoin, boolean handsFreeOnLoad, boolean allowAttendeeInvites,
			boolean featureRecording, boolean featureCob, boolean featureDoc);
	
	//this is an old method beign used in some places
	public	void	setConferenceOptions(boolean enableLobby, String organizerNetworkProfile,
			String organizerDisplayImageQuality, Integer meetingTime, Integer maxParticipants,
			String presenterAV, Integer maxAttendeeMikes, String returnUrl, boolean assistantEnabled);
	
	public	IWhiteboardManager	getWhiteboardManager();
	
	public	IRecordingManager	getRecordingManager();
	
	public	ICobrowseManager	getCobrowseManager();
	
	public	String	getDMServerAddress();
	
	public	String	getConferenceId();
	
	public   String   getJoinUrl();
	public String getJoinUrlEncoded();
	
	public   void   setJoinUrl(String joinUrl);
	public   String   getRejoinUrl();

	public String getAttendeePasscode();
	public String getInternToll();
	public String getInternTollFree();
	public String getModeratorPassCode();
	public String getToll();
	public String getTollFree();
	public boolean isDialInfoVisible();
	public String getAttendeePwd();
	
	public	void	forceStopActiveSharing(IConferenceParticipant presenter);
	public	boolean	isAssignMikeOnJoin();
	public	boolean	isHandsFreeOnLoad();
	public	boolean	isAllowAttendeeInvites();
	public boolean isFeatureRecording() ;
	public boolean isFeatureDoc() ;
	public boolean isFeatureCob() ;
}
