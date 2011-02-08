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

package com.dimdim.conference.ui.model.client;

import java.util.HashMap;

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.ui.common.client.data.UIDataDictionary;
import com.dimdim.ui.common.client.data.UIDataDictionaryManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class simply provides some of the global constants and parameters used
 * by all the other gwt modules.
 */
public class ConferenceGlobals implements EntryPoint
{
	protected	static	final	String	BROWSER_TYPE_IE = "ie";
	protected	static	final	String	BROWSER_TYPE_FIREFOX = "firefox";
	protected	static	final	String	BROWSER_TYPE_SAFARI = "safari";
	
	public	static	final	String	OS_WINDOWS	=	"windows";
	public	static	final	String	OS_MAC	=	"mac";
	public	static	final	String	OS_UNIX	=	"unix";
	public	static	final	String	OS_LINUX	=	"linux";
	
	public	static	String	webappName = null;	//	dimdim
	public	static	String	webappRoot = null;
	public	static	String	serverAddress = null;	//e.g. "http://jayantpandit.ca:8080"; always http
	public	static	String	currentServerAddress = null;	//could be http or https
//	public	static	String	rtmpURL = null;
//	public	static	String	avRtmpURL = null;
	public	static	String	conferenceKey = null;
	public	static	String	installationId = null;
	public	static	String	installationPrefix = null;
	public	static	String	conferenceKeyQualified = null;
	public	static	String	dimdimId = null;
	public	static	String	roomId = null;
	public	static	String	conferenceId = null;
	public	static	String	sessionKey = null;
	public	static	String	streamingSessionKey = null;
	public	static	boolean	isInPopup = false;
	public	static	String	baseWebappURL = null;
	public	static	String	currentBaseWebappURL = null;
	public	static	String	browserType = ConferenceGlobals.BROWSER_TYPE_IE;
	public	static	String	browserVersion = "all";
//	public	static	String	organizerEmail;
	public static 	String 	feedbackEmail;
	
	public	static	int		currentMaxParticipants;
	public	static	int		currentMaxMeetingTime;
	
	public	static	UIResourceObject	currentSharedResource = null;
	public	static	boolean				presenterAVAudioOnly = false;
	//public	static	boolean				meetingVideoChat = false;
	public	static	boolean				presenterAVActive = false;
	public	static	boolean				myAudioActive = false;
	public 	static 	boolean				presenterAVAudioDisabled = false; 
	public 	static 	boolean				presenterAVVideoOnly = false;
	public 	static 	boolean				presenterAVAudioVideo = false;
	public	static	boolean				assistantEnabled	= true;
	public	static	boolean				largeVideoSupported = false;
	
	public 	static	String				defaultUrl=""; 
	
	//feature control realted attributes
	public	static	boolean privateChatEnabled	= true;
	public	static	boolean publicChatEnabled	= true;
	public	static	boolean partListEnabled	= true;
	public	static	boolean publisherEnabled	= true;
	public	static	boolean whiteboardEnabled	= true;
	public	static	boolean pptEnabled	= true;
	public	static	boolean blipTVEnabled	= true;
	public	static	boolean	recordingEnabled = false;
	public	static	boolean	fullscreenEnabled = false;
	public	static	boolean	cobEnabled = true;
	public	static	boolean	docEnabled = true;
	
	public static String	userType = "free";
	
//	public	static	UIRosterEntry		currentUserRosterEntry;
	
//	protected	static	Dictionary	uiStringsDictionary;
//	protected	static	Dictionary	tooltipsDictionary;
//	protected	static	Dictionary	layoutDictionary;
	
	public	static	UIDataDictionary	uiStringsDictionary;
	public	static	UIDataDictionary	tooltipsDictionary;
	public	static	UIDataDictionary	layoutDictionary;
	public	static	UIDataDictionary	userInfoDictionary;
	
	private	static	int		contentWidth;
	private	static	int		contentHeight;
	
	protected	static	HashMap		publicVariables;
	
	public static String dmsServerAddress;
//	public static String reflectorPort;
//	public static String reflectorAddress;
	
	public static boolean  showPhoneInfo = true;
	public static String tollFree = "";
	public static String toll = "";
	public static String internTollFree = "";
	public static String internToll = "";
	public static String moderatorPassCode = "";
	public static String attendeePasscode = "";
	
	private	static	ClientLayout	clientLayout;
	
	public	static	void	init()
	{
//		ConferenceGlobals.currentUserRosterEntry = new UIRosterEntry();
//		ConferenceGlobals.uiStringsDictionary = Dictionary.getDictionary("console_ui_strings");
//		ConferenceGlobals.tooltipsDictionary = Dictionary.getDictionary("console_tooltips");
//		ConferenceGlobals.layoutDictionary = Dictionary.getDictionary("console_default_layout");
//		ConferenceGlobals.publicVariables = new HashMap();
		
//		ConferenceGlobals.organizerEmail = ConferenceGlobals.getOrganizerEmail();
		ConferenceGlobals.conferenceId = ConferenceGlobals.getConferenceId();
		ConferenceGlobals.feedbackEmail = ConferenceGlobals.getFeedbackEmail();
//		Window.alert("Reading environment - 2");
		ConferenceGlobals.conferenceKey = ConferenceGlobals.getConferenceKeyFromPage();
		ConferenceGlobals.installationId = userInfoDictionary.getStringValue("installation_id");
		ConferenceGlobals.installationPrefix = userInfoDictionary.getStringValue("installation_prefix");
//		ConferenceGlobals.dimdimId = ConferenceGlobals.conferenceKey;
		ConferenceGlobals.conferenceKeyQualified = ConferenceGlobals.installationId+
			ConferenceGlobals.installationPrefix + ConferenceGlobals.conferenceKey;
		ConferenceGlobals.roomId = "_default";
//		Window.alert("Reading environment - 3");
		ConferenceGlobals.serverAddress = ConferenceGlobals.getServerAddress();
//		ConferenceGlobals.reflectorAddress = ConferenceGlobals.getReflectorAddress();
//		ConferenceGlobals.reflectorPort = ConferenceGlobals.getReflectorPort();
		
		ConferenceGlobals.dmsServerAddress = ConferenceGlobals.getDmsServerAddress();
		ConferenceGlobals.currentServerAddress = ConferenceGlobals.getCurrentServerAddress();
//		Window.alert("Reading environment - 4");
		ConferenceGlobals.webappName = ConferenceGlobals.getWebappNameFromPage();
//		Window.alert("Reading environment - 5");
		ConferenceGlobals.webappRoot = "/"+ConferenceGlobals.getWebappName()+"/";
//		Window.alert("Reading environment - 6");
		ConferenceGlobals.baseWebappURL = ConferenceGlobals.serverAddress+
			ConferenceGlobals.webappRoot;
		ConferenceGlobals.currentBaseWebappURL = ConferenceGlobals.currentServerAddress+
			ConferenceGlobals.webappRoot;
//		Window.alert("Reading environment - 7");
		ConferenceGlobals.sessionKey = ConferenceGlobals.getSessionKey();
		ConferenceGlobals.streamingSessionKey = ConferenceGlobals.getStreamingSessionKey();
		ConferenceGlobals.browserType = ConferenceGlobals.getBrowserTypeFromPage();
		ConferenceGlobals.browserVersion = ConferenceGlobals.getBrowserVersionFromPage();
//		Window.alert("Reading environment - 8");
		ConferenceGlobals.isInPopup = (new Boolean(ConferenceGlobals.isInPopup())).booleanValue();
//		Window.alert("Reading environment complete");
		ConferenceGlobals.setCurrentMaxMeetingTime(ConferenceGlobals.getInitialMaxMeetingTime());
		ConferenceGlobals.setCurrentMaxParticipants(ConferenceGlobals.getInitialMaxParticipants());
		ConferenceGlobals.presenterAVAudioOnly = ConferenceGlobals.getPresenterAVOption().equalsIgnoreCase("audio");
		//ConferenceGlobals.meetingVideoChat = ConferenceGlobals.getPresenterAVOption().equals("videochat");
		ConferenceGlobals.largeVideoSupported = ConferenceGlobals.getLargeVideoOption().equals("true");
		ConferenceGlobals.presenterAVAudioDisabled = ConferenceGlobals.getPresenterAVOption().equalsIgnoreCase("disabled");
		ConferenceGlobals.presenterAVVideoOnly = ConferenceGlobals.getPresenterAVOption().equalsIgnoreCase("video");
		ConferenceGlobals.presenterAVAudioVideo = ConferenceGlobals.getPresenterAVOption().equalsIgnoreCase("av");
		ConferenceGlobals.assistantEnabled = ConferenceGlobals.getIsAssitantEnabled().equals("true");
		ConferenceGlobals.defaultUrl = ConferenceGlobals.getDefaultUrl();
		
		ConferenceGlobals.privateChatEnabled = ConferenceGlobals.getIsPrivateChatEnabled().equals("true");
		ConferenceGlobals.publicChatEnabled = ConferenceGlobals.getIsPublicChatEnabled().equals("true");
		ConferenceGlobals.partListEnabled = ConferenceGlobals.getPartListEnabled().equals("true");
		ConferenceGlobals.publisherEnabled = ConferenceGlobals.getIsPublisherEnabled().equals("true");
		ConferenceGlobals.whiteboardEnabled = ConferenceGlobals.getIsWhiteboardEnabled().equals("true");
		ConferenceGlobals.pptEnabled = ConferenceGlobals.getIsPptEnabled().equals("true");
		ConferenceGlobals.recordingEnabled = ConferenceGlobals.getIsRecordingEnabled().equals("true");
		ConferenceGlobals.cobEnabled = ConferenceGlobals.getIsCobEnabled().equals("true");
		ConferenceGlobals.docEnabled = ConferenceGlobals.getIsDocEnabled().equals("true");
		ConferenceGlobals.fullscreenEnabled = ConferenceGlobals.getIsFullScreenEnabled().equals("true");
		
		ConferenceGlobals.blipTVEnabled = userInfoDictionary.getStringValue("bliptv_upload_supported").equals("true");
		
		ConferenceGlobals.userType = ConferenceGlobals.getUserType();
		
		
		ConferenceGlobals.showPhoneInfo = ConferenceGlobals.isPhoneInfoVisible();
		
		ConferenceGlobals.tollFree = ConferenceGlobals.getTollFree();
		ConferenceGlobals.toll = ConferenceGlobals.getToll();
		ConferenceGlobals.internTollFree = ConferenceGlobals.getInternTollFree();
		ConferenceGlobals.internToll = ConferenceGlobals.getInternToll();
		ConferenceGlobals.moderatorPassCode = ConferenceGlobals.getModeratorPassCode();
		ConferenceGlobals.attendeePasscode = ConferenceGlobals.getAttendeePasscode();
		
	}
	
	public	void	onModuleLoad()
	{
	}
	
	private	ConferenceGlobals()
	{
	}
	public static	void	setCurrentMaxParticipants(String s)
	{
		ConferenceGlobals.currentMaxParticipants = (new Integer(s)).intValue();
	}
	
	public static	void	setCurrentMaxParticipants(int count)
	{
		ConferenceGlobals.currentMaxParticipants = count;
	}
	
	public static	void	setCurrentMaxMeetingTime(String s)
	{
		ConferenceGlobals.currentMaxMeetingTime = (new Integer(s)).intValue();
	}
	
	public static	void	setCurrentMaxMeetingTime(int time)
	{
		//Window.alert("inside setCurrentMaxMeetingTime new time = "+time);
		ConferenceGlobals.currentMaxMeetingTime = time;
		//Window.alert("now the updated time is = "+currentMaxMeetingTime);
	}
	
	public static	String getConferenceKey()
	{
		return	ConferenceGlobals.conferenceKey;
	}
	public static UIResourceObject getCurrentSharedResource()
	{
		return currentSharedResource;
	}
	public static void setCurrentSharedResource(
			UIResourceObject currentSharedResource)
	{
		ConferenceGlobals.currentSharedResource = currentSharedResource;
	}
//	public	static	boolean	isSharingActive()
//	{
//		return	(ConferenceGlobals.currentSharedResource != null);
//	}
	public static boolean isPresenterAVActive()
	{
		return presenterAVActive;
	}
	public static void setPresenterAVActive(boolean presenterAVActive)
	{
		ConferenceGlobals.presenterAVActive = presenterAVActive;
	}
	public static boolean isMyAudioActive()
	{
		return myAudioActive;
	}
	public static void setMyAudioActive(boolean myAudioActive)
	{
		ConferenceGlobals.myAudioActive = myAudioActive;
	}
//	public	static	Dictionary	getUIStringsDictionary()
//	{
//		return	ConferenceGlobals.uiStringsDictionary;
//	}
	public static String getBrowserType()
	{
		return browserType;
	}
	public static String getBrowserVersion()
	{
		return browserVersion;
	}
	public static void setBrowserType(String browserType)
	{
		ConferenceGlobals.browserType = browserType;
	}
	public static boolean isBrowserIE()
	{
		return	ConferenceGlobals.browserType.equals(ConferenceGlobals.BROWSER_TYPE_IE);
	}
	public static boolean isBrowserFirefox()
	{
		return	ConferenceGlobals.browserType.equals(ConferenceGlobals.BROWSER_TYPE_FIREFOX);
	}
	public static boolean isBrowserSafari()
	{
		return	ConferenceGlobals.browserType.equals(ConferenceGlobals.BROWSER_TYPE_SAFARI);
	}
	public static String getWebappName()
	{
		return	ConferenceGlobals.webappName;
	}
	public static boolean isPresenterAVAudioOnly()
	{
		return	ConferenceGlobals.presenterAVAudioOnly;
	}
	public static boolean isMeetingVideoChat()
	{
		String attVideosStr = ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_videos");
		int attVideos = Integer.decode(attVideosStr);
		
		if(attVideos > 0)
			return	true;
		return false;
	}
	public static boolean isPresenterAVAudioDisabled()
	{
		return	ConferenceGlobals.presenterAVAudioDisabled;
	}
	
	public static boolean isPresenterAVVideoOnly()
	{
		return	ConferenceGlobals.presenterAVVideoOnly;
	}
	public static boolean isPresenterAVAudoVideo()
	{
		return	ConferenceGlobals.presenterAVAudioVideo;
	}
	
	public static boolean isLargeVideoSupported()
	{
		return	ConferenceGlobals.largeVideoSupported;
	}
	public	static	String	getDTPStreamId()
	{
		return	getClientGUID();
	}
	public	static	String	getAVStreamId()
	{
		if (ConferenceGlobals.isPresenterAVAudioOnly())
		{
			return	"a"+getClientGUID()+"."+ConferenceGlobals.conferenceKey+".A";
		}
		else
		{
			return	"v"+getClientGUID()+"."+ConferenceGlobals.conferenceKey+".V";
		}
	}
	public	static	String	getAudioStreamId()
	{
		return	"a"+getClientGUID()+"."+ConferenceGlobals.conferenceKey+".A";
	}
	public static native String getClientGUID() /*-{
		return ($wnd.getAGuid());
	}-*/;
	
	public static native String getDataCacheId() /*-{
		return $wnd.data_cache_id;
	}-*/;
	/**
	 * Window size allowances. Experience has shown that Window.getClientWidth and
	 * Window.getClientHeight returns differnt values on IE and firefox. On firefox
	 * if always seems to return a larger value. The allowancc values returned by
	 * these methods are subtracted from the values returned by the api in the windo
	 * resize listener.
	 */
//	public	static	int	getWindowAreaWidthAllowance()
//	{
//		if (ConferenceGlobals.isBrowserFirefox())
//		{
//			return	44;
//		}
//		else
//		{
//			return	0;
//		}
//	}
//	public	static	int	getWindowAreaHeightAllowance()
//	{
//		if (ConferenceGlobals.isBrowserFirefox())
//		{
//			return	10;
//		}
//		else
//		{
//			return	0;
//		}
//	}
	public	static	int	getLHPWidth()
	{
		return	250;
	}
	public	static	int	getTopPanelHeightAndMargin()
	{
		return	70;
	}
	public	static	int	getWorkspaceAndLHPBordersWidth()
	{
		return	28;
	}
	public	static	int	getWorkspaceBordersAndTabsHeight()
	{
		return	40;
	}
//	public	static	int	getPrivateChatTextAreaHeight()
//	{
//		if (ConferenceGlobals.isBrowserFirefox())
//		{
//			return	80;
//		}
//		else
//		{
//			return	60;
//		}
//	}
	/**
	 * @param key
	 * @return
	 */
	public	static	void	readDictionaries()
	{
		if (ConferenceGlobals.uiStringsDictionary == null)
		{
			try
			{
				ConferenceGlobals.uiStringsDictionary = 
					UIDataDictionaryManager.getManager().getDictionary("console", "ui_strings");
				ConferenceGlobals.tooltipsDictionary = 
					UIDataDictionaryManager.getManager().getDictionary("console", "tooltips");
				ConferenceGlobals.layoutDictionary = 
					UIDataDictionaryManager.getManager().getDictionary("console", "default_layout");
				ConferenceGlobals.userInfoDictionary =
					UIDataDictionaryManager.getManager().getDictionary("session_string", "user_info"+getDataCacheId());
			}
			catch(Exception e)
			{
				
			}
		}
	}
	public	static	String	getDisplayString(String key, String defaultValue)
	{
		String ds = defaultValue;
		readDictionaries();
		if (ConferenceGlobals.uiStringsDictionary != null)
		{
			try
			{
				String s = ConferenceGlobals.uiStringsDictionary.getStringValue(key);//(convertToJsIdString(key));
				if (s != null)
				{
					ds = s;
				}
			}
			catch(Exception e)
			{
				//Window.alert(e.getMessage());
			}
		}
		return	ds;
	}
	public	static	String	getTooltip(String key)
	{
		String ds = "";
		readDictionaries();
		if (ConferenceGlobals.tooltipsDictionary != null)
		{
			try
			{
				String s = ConferenceGlobals.tooltipsDictionary.getStringValue(key);//(convertToJsIdString(key));
				if (s != null)
				{
					ds = s;
				}
			}
			catch(Exception e)
			{
				//Window.alert(e.getMessage());
			}
		}
		return	ds;
	}
	public	static	String	getLayoutParameter(String key, String defaultValue)
	{
		String ds = defaultValue;
		readDictionaries();
		if (ConferenceGlobals.layoutDictionary != null)
		{
			try
			{
				String s = ConferenceGlobals.layoutDictionary.getStringValue(key);//(convertToJsIdString(key));
				if (s != null)
				{
					ds = s;
				}
			}
			catch(Exception e)
			{
				//Window.alert(e.getMessage());
			}
		}
		return	ds;
	}
	public	static	int	getLayoutParameterAsInt(String key, int defaultValue)
	{
		int dv = defaultValue;
		readDictionaries();
		if (ConferenceGlobals.layoutDictionary != null)
		{
			try
			{
				String s = ConferenceGlobals.layoutDictionary.getStringValue(key);//(convertToJsIdString(key));
				if (s != null)
				{
					dv = Integer.parseInt(s);
				}
			}
			catch(Exception e)
			{
				dv = defaultValue;
			}
		}
		return	dv;
	}
	public	static	boolean	getLayoutParameterAsBoolean(String key, boolean defaultValue)
	{
		boolean dv = defaultValue;
		readDictionaries();
		if (ConferenceGlobals.layoutDictionary != null)
		{
			try
			{
				String s = ConferenceGlobals.layoutDictionary.getStringValue(key);//(convertToJsIdString(key));
				if (s != null)
				{
					dv = (new Boolean(s)).booleanValue();
				}
			}
			catch(Exception e)
			{
				dv = defaultValue;
			}
		}
		return	dv;
	}
//	private	static	String	convertToJsIdString(String str)
//	{
//		String s1 = str;
//		int dot = s1.indexOf(".");
//		while (dot > 0)
//		{
//			s1 = s1.substring(0,dot)+"$"+s1.substring(dot+1);
//			dot = s1.indexOf(".");
//		}
//		return	s1;
//	}
//	private	boolean	isOrganizer()
//	{
//		if (this.getUserId().equals(this.getOrganizerEmail()))
//		{
//			return	true;
//		}
//		return	false;
//	}
	public	static	String	getOverrideMaxParticipants()
	{
		return	userInfoDictionary.getStringValue("override_max_participants");
	}
	
	public	static	String	getShowInviteLinks()
	{
		return	userInfoDictionary.getStringValue("show_invite_links");
	}
	
	private static String getSessionKey()
	{
		return	userInfoDictionary.getStringValue("session_key");
	}
	private static String getStreamingSessionKey()
	{
		return	userInfoDictionary.getStringValue("streaming_session_key");
	}
	private static native String isInPopup() /*-{
	 return ($wnd.in_popup);
	}-*/;
	private static String getWebappNameFromPage()
	{
		return	userInfoDictionary.getStringValue("webapp_name");
	}
	public static String getUserId()
	{
		return	userInfoDictionary.getStringValue("user_id");
	}
	public static String getUserStatus()
	{
		return	userInfoDictionary.getStringValue("user_status");
	}
	private static String getConferenceKeyFromPage()
	{
		return	userInfoDictionary.getStringValue("conference_key");
	}
	private static String getConferenceId()
	{
		return	userInfoDictionary.getStringValue("conference_id");
	}
	private static boolean isPhoneInfoVisible()
	{
		return	userInfoDictionary.getBooleanValue("show_phone_info", true);
	}
	private static String getOrganizerEmail()
	{
		return	userInfoDictionary.getStringValue("organizer_email");
	}
	private static String getFeedbackEmail()
	{
		if(null != userInfoDictionary.getStringValue("feeback_email") && userInfoDictionary.getStringValue("feeback_email").length() > 0
				&& !"null".equals(userInfoDictionary.getStringValue("feeback_email")))
		{
			return	userInfoDictionary.getStringValue("feeback_email");
		}else{
			return getOrganizerEmail();
		}
		
	}
	private static String getServerAddress()
	{
		return	userInfoDictionary.getStringValue("server_address");
	}
//	private static String getReflectorAddress()
//	{
//		return	userInfoDictionary.getStringValue("reflector_address");
//	}
//	private static String getReflectorPort()
//	{
//		return	userInfoDictionary.getStringValue("reflector_port");
//	}
	private static String getDmsServerAddress()
	{
		return	userInfoDictionary.getStringValue("dms_server_address");
	}
	private static String getCurrentServerAddress()
	{
		return	userInfoDictionary.getStringValue("current_server_address");
	}
	private static String getBrowserTypeFromPage()
	{
		return	userInfoDictionary.getStringValue("browser_type");
	}
	private static String getBrowserVersionFromPage()
	{
		return	userInfoDictionary.getStringValue("browser_version");
	}
	private static String getInitialMaxParticipants()
	{
		return	userInfoDictionary.getStringValue("max_participants");
	}
	private static String getInitialMaxMeetingTime()
	{
		return	userInfoDictionary.getStringValue("max_meeting_time");
	}
	private static String	getPresenterAVOption()
	{
		return	userInfoDictionary.getStringValue("presenter_av");
	}
	private static String	getLargeVideoOption()
	{
		return	userInfoDictionary.getStringValue("large_video_supported");
	}
	private static String	getIsAssitantEnabled()
	{
		return	userInfoDictionary.getStringValue("assistant_enabled");
	}
	
	private static String	getDefaultUrl()
	{
		if("default_url".equals(userInfoDictionary.getStringValue("default_url")))
			{
				return "";
			}
		return	userInfoDictionary.getStringValue("default_url");
	}

	private static String	getIsPublicChatEnabled()
	{
		return	userInfoDictionary.getStringValue("public_chat_enabled");
	}
	
	private static String	getPartListEnabled()
	{
		return	userInfoDictionary.getStringValue("part_list_enabled");
	}
	
	private static String	getIsPrivateChatEnabled()
	{
		return	userInfoDictionary.getStringValue("private_chat_enabled");
	}
	private static String	getIsPublisherEnabled()
	{
		return	userInfoDictionary.getStringValue("publisher_enabled");
	}
	private static String	getIsWhiteboardEnabled()
	{
		return	userInfoDictionary.getStringValue("whiteboard_enabled");
	}
	private static String	getIsPptEnabled()
	{
		return	userInfoDictionary.getStringValue("ppt_enabled");
	}
	private static String	getIsRecordingEnabled()
	{
		return	userInfoDictionary.getStringValue("recording_enabled");
	}
	private static String	getIsCobEnabled()
	{
		return	userInfoDictionary.getStringValue("cob_enabled");
	}
	private static String	getIsDocEnabled()
	{
		return	userInfoDictionary.getStringValue("doc_enabled");
	}
	private static String	getIsFullScreenEnabled()
	{
		return	userInfoDictionary.getStringValue("fullscreen_enabled");
	}
	public static boolean isAssistantEnabled()
	{
		return assistantEnabled;
	}
	public static int getContentHeight()
	{
		return contentHeight;
	}
	public static void setContentHeight(int contentHeight)
	{
		ConferenceGlobals.contentHeight = contentHeight;
	}
	public static int getContentWidth()
	{
		return contentWidth;
	}
	public static void setContentWidth(int contentWidth)
	{
		ConferenceGlobals.contentWidth = contentWidth;
	}
	
	private static  String getAttendeePasscode() {
		return	userInfoDictionary.getStringValue("att_pass_code");
	}
	private static  String getInternToll() {
		return	userInfoDictionary.getStringValue("intl_toll");
	}
	private static  String getInternTollFree() {
		return	userInfoDictionary.getStringValue("intl_tollfree");
	}
	private static  String getModeratorPassCode() {
		return	userInfoDictionary.getStringValue("mod_pass_code");
	}
	private static  String getToll() {
		return	userInfoDictionary.getStringValue("toll");
	}
	private static  String getTollFree() {
		return	userInfoDictionary.getStringValue("tollfree");
	}
	public	static	boolean	isPubSupportable()
	{
		String	os = ConferenceGlobals.getOSType();
		return	os.equalsIgnoreCase("windows") || os.equalsIgnoreCase("mac");
	}
	public static ClientLayout getClientLayout()
	{
		return clientLayout;
	}
	public static void setClientLayout(ClientLayout clientLayout)
	{
		ConferenceGlobals.clientLayout = clientLayout;
	}
	public static native String getOSType() /*-{
		return $wnd.os_type;
	}-*/;
	
	public static native String getPubAvailable() /*-{
		return $wnd.pubAvailable;
	}-*/;
	
	public static native String getPubVersion() /*-{
		return $wnd.pubVersion;
	}-*/;
	
	public static native String getReloadWindow() /*-{
		return $wnd.reloadConsole;
	}-*/;
	private static native String getUserType() /*-{
	 return ($wnd.userType);
	}-*/;
}
