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
 
package com.dimdim.conference.application.core;

/*
import java.text.DateFormat;
import	java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import	java.util.Date;

import com.dimdim.conference.config.UIParamsConfig;
import com.dimdim.conference.model.ConferenceInfo;
import	com.dimdim.conference.model.IConferenceParticipant;
import	com.dimdim.conference.model.Participant;
import	com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IStreamingServer;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.model.UserNotInConferenceException;
import com.dimdim.data.application.UIDataManager;
import com.dimdim.util.session.UserSessionDataManager;
*/
/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ParticipantSession	//extends	CommonSession
{
	/*
	protected	IConferenceParticipant	user;
	protected	IConference				conference;
	
	protected	Boolean		useSecureConnection	= new Boolean(false);
	protected	HashMap		applications;
	protected	boolean		consoleLoaded = false;
	protected	boolean		inPopup = false;
	protected	boolean		hasActiveX = false;
	protected	boolean		hasWebcam = false;
	
	/--**
	 * This object is created by the login application. It represents the
	 * session for most users. Only a few specialized user, such as, system
	 * administrators will be working outside the context of a single
	 * conference.
	 * 
	 * The conference may not be available at the time of creating the user
	 * as the user might be the first one and will need to initiate a new
	 * conference.
	 * 
	 * @param user
	 *--/
	public	ParticipantSession(IConferenceParticipant user)
	{
		this.user = user;
		if (this.user != null && this.user.isPresenter())
		{
			this.timeout = ConferenceConsoleConstants.getPresenterSessionTimeout()*1000;
			this.basicTimeout = this.timeout;
		}
		this.applications = new HashMap();
	}
	public	ParticipantSession(IConferenceParticipant user, IConference conference)
	{
		this.user = user;
		if (this.user != null && this.user.isPresenter())
		{
			this.timeout = ConferenceConsoleConstants.getPresenterSessionTimeout()*1000;
		}
		this.conference = conference;
		this.applications = new HashMap();
	}
	public	boolean	isPresenter()
	{
		return	(this.user != null && this.user.isPresenter());
	}
	public	boolean	isAbandoned()
	{
		if (user != null && ((Participant)user).isJoining())
		{
			long it = System.currentTimeMillis() - getLastAccessTime();
			if (it > (30*60*1000))
			{
				//	Dormant in joining for half an hour. This is probably a
				//	browser crash or pub upload delay
				return	true;
			}
			return	false;
		}
		return	super.isAbandoned();
	}
	public	synchronized	void	close()
	{
		if (this.conference != null && this.user != null)
		{
			try
			{
				//	To ensure that the cached user info data is cleared.
				UIDataManager.getUIDataManager().getSessionDataBuffer("session_string", "user_info"+dataCacheId, Locale.US);
				
				//	Clear the user session cached on the session data manager.
				UserSessionDataManager.getDataManager().clearUserRequest(this.getUri());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				System.out.println("User Session is being closed. Removing user:"+
						this.user+", from conference:"+this.conference);
				conference.removeUserFromConference(user);
			}
			catch(UserNotInConferenceException uice)
			{
				uice.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			setUser(null);
			setConference(null);
			super.close();
		}
	}
	public Date getCreationDate()
	{
		return creationDate;
	}
	public IConference getConference()
	{
		return this.conference;
	}
	public void setConference(IConference conference)
	{
		this.conference = conference;
	}
	public IConferenceParticipant getUser()
	{
		return this.user;
	}
	public void setUser(IConferenceParticipant user)
	{
		this.user = user;
	}
	public Boolean getUseSecureConnection()
	{
		return this.useSecureConnection;
	}
	public void setUseSecureConnection(Boolean useSecureConnection)
	{
		this.useSecureConnection = useSecureConnection;
	}
	public	Object	getApplication(String name)
	{
		return	this.applications.get(name);
	}
	public	void	putApplication(String name, Object application)
	{
		this.applications.put(name,application);
	}
	public boolean isConsoleLoaded()
	{
		return this.consoleLoaded;
	}
	public void setConsoleLoaded(boolean consoleLoaded)
	{
		this.consoleLoaded = consoleLoaded;
	}
	public boolean isHasActiveX()
	{
		return this.hasActiveX;
	}
	public void setHasActiveX(boolean hasActiveX)
	{
		this.hasActiveX = hasActiveX;
	}
	public boolean isHasWebcam()
	{
		return this.hasWebcam;
	}
	public void setHasWebcam(boolean hasWebcam)
	{
		this.hasWebcam = hasWebcam;
	}
	public boolean isInPopup()
	{
		return this.inPopup;
	}
	public void setInPopup(boolean inPopup)
	{
		this.inPopup = inPopup;
	}
	public	String	getUserInfo(String sessionKey, Locale clientLocale,
			String browserType, String browserVersion, boolean secure)
	{
		StringBuffer buf = new StringBuffer();
		
		try
		{
			Participant user = (Participant)getUser();
			IConference conf = getConference();
			ConferenceInfo ci = conf.getConferenceInfo();
			
			buf.append("{");
			buf.append("user_id:'"+user.getId()+"',");
			buf.append("user_role:'"+user.getRole()+"',");
			buf.append("user_status:'"+user.getStatus()+"',");
			buf.append("user_name:'"+Participant.getDisplayNameBase64(user.getDisplayName())+"',");
			buf.append("user_mood:'"+user.getMood()+"',");
			buf.append("lobby_enabled:'"+conf.isLobbyEnabled()+"',");
			buf.append("part_list_enabled:'"+conf.isParticipantListEnabled()+"',");
			buf.append("net_profile:'"+user.getNetProfile()+"',");
			buf.append("img_quality:'"+user.getImgQuality()+"',");
			buf.append("browser_type:'"+browserType+"',");
			buf.append("browser_version:'"+browserVersion+"',");
			buf.append("organizer_email:'"+ci.getOrganizerEmail()+"',");
			buf.append("conference_id:'"+conf.getConferenceId()+"',");
			buf.append("start_time_on_server:'"+conf.getStartTimeMillis()+"',");
			buf.append("elapsed_time_millis:'"+(System.currentTimeMillis()-conf.getStartTimeMillis())+"',");
			buf.append("max_attendee_audios:'"+conf.getMaxAttendeeMikes()+"',");
			buf.append("server_max_attendee_audios:'"+ConferenceConsoleConstants.getMaxAttendeeAudios()+"',");
			
			String returnUrl = conf.getReturnUrl();
			if (user.isPresenter())
			{
				if (returnUrl.toLowerCase().startsWith("http://www.dimdim.com"))
				{
					returnUrl = "http://www.dimdim.com/registration/Dimdim_Signin.html";
				}
			}
			buf.append("return_url:'"+returnUrl+"',");
			buf.append("default_url:'"+conf.getDefaultUrl()+"',");
			buf.append("feeback_email:'"+conf.getFeedBackEmail()+"',");
			buf.append("default_logo:'"+conf.getLogo()+"',");
			buf.append("dms_server_address:'"+conf.getDMServerAddress()+"',");
			
			if (conf != null)
			{
				buf.append("conference_key:'"+conf.getConfig().getConferenceKey()+"',");
			}
			buf.append("session_key:'"+sessionKey+"',");
			buf.append("streaming_session_key:'"+getStreamingSessionKey()+"',");
			
			buf.append("max_participants:'"+conf.getMaxParticipants()+"',");
			buf.append("presenter_av:'"+conf.getPresenterAV()+"',");
			buf.append("max_meeting_time:'"+conf.getMaxMeetingTime()+"',");
			buf.append("assistant_enabled:'"+conf.isAssistantEnabled()+"',");
			
//			feature control related settings
			buf.append("public_chat_enabled:'"+conf.isPublicChatEnabled()+"',");
			buf.append("private_chat_enabled:'"+conf.isPrivateChatEnabled()+"',");
			buf.append("whiteboard_enabled:'"+conf.isWhiteboardEnabled()+"',");
			buf.append("publisher_enabled:'"+conf.isPublisherEnabled()+"',");
			buf.append("ppt_enabled:'"+ConferenceConsoleConstants.getResourceKeyValue("dimdim.ppt_enabled", "true")+"',");
		
			String serverAddress = ConferenceConsoleConstants.getServerAddress();
			String serverSecureAddress = ConferenceConsoleConstants.getServerSecureAddress();
			String webappName = ConferenceConsoleConstants.getWebappName();
			String joinURL = conf.getJoinUrl();
			
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,clientLocale);
			String localeDate = dateFormat.format(ci.getStartDate());
			
			buf.append("server_address:'"+serverAddress+"',");
			if (secure)//servletRequest.getRequestURL().toString().startsWith("htts"))
			{
				buf.append("current_server_address:'"+serverSecureAddress+"',");
			}
			else
			{
				buf.append("current_server_address:'"+serverAddress+"',");
			}
			buf.append("webapp_name:'"+webappName+"',");
			buf.append("override_max_participants:'"+ConferenceConsoleConstants.getOverrideMaxParticipants()+"',");
			buf.append("show_invite_links:'"+ConferenceConsoleConstants.getshowInviteLinks()+"',");
			buf.append("video_chat_supported:'"+ConferenceConsoleConstants.getVideoChatSupported()+"',");
			buf.append("large_video_supported:'"+ConferenceConsoleConstants.getLargeVideoSupported()+"',");
			
			//	Add Streaming servers data
			addStreamingServersData(conf,buf);
			
			//	Add all ui parameters
			addUIParams(buf, conf);
			
			buf.append("productName:'"+ConferenceConsoleConstants.getProductName()+"',");
			buf.append("productVersion:'"+ConferenceConsoleConstants.getProductVersion()+"',");
			buf.append("productWebSite:'"+ConferenceConsoleConstants.getProductWebSite()+"',");
			buf.append("subject:'"+ci.getName()+"',");
			buf.append("key:'"+ci.getKey()+"',");
			buf.append("joinURL:'"+joinURL+"',");
			buf.append("organizerName:'"+Participant.getDisplayNameBase64(ci.getOrganizer())+"',");
			buf.append("organizerEmail:'"+ci.getOrganizerEmail()+"',");
			buf.append("startDate:'"+localeDate+"'");
			buf.append("}");
		}
		catch(Exception e)
		{
		}
		
		return	buf.toString();
	}
	private	void	addStreamingServersData(IConference conf, StringBuffer buf)
	{
		IStreamingServer iss = conf.getStreamingServer();
		
		buf.append("streaming_urls_dtp_rtmp_url:\"");
		buf.append(iss.getDtpRtmpUrl());
		buf.append("\",");
		
		buf.append("streaming_urls_dtp_rtmpt_url:\"");
		buf.append(iss.getDtpRtmptUrl());
		buf.append("\",");
		
		if (iss.getWhiteboardRtmpUrl() != null)
		{
			buf.append("streaming_urls_whiteboard_rtmp_url:\"");
			buf.append(iss.getWhiteboardRtmpUrl());
			buf.append("\",");
			
			buf.append("streaming_urls_whiteboard_rtmpt_url:\"");
			buf.append(iss.getWhiteboardRtmptUrl());
			buf.append("\",");
		}
		else
		{
			buf.append("streaming_urls_whiteboard_rtmp_url:\"\",");
			buf.append("streaming_urls_whiteboard_rtmpt_url:\"\",");
		}
		
		buf.append("streaming_urls_av_rtmp_url:\"");
		buf.append(iss.getAvRtmpUrl());
		buf.append("\",");
		
		buf.append("streaming_urls_av_rtmpt_url:\"");
		buf.append(iss.getAvRtmptUrl());
		buf.append("\",");
		
		buf.append("streaming_urls_audio_rtmp_url:\"");
		buf.append(iss.getAudioRtmpUrl());
		buf.append("\",");
		
		buf.append("streaming_urls_audio_rtmpt_url:\"");
		buf.append(iss.getAudioRtmptUrl());
		buf.append("\",");
	}
	private	void	addUIParams(StringBuffer buf, IConference conf)
	{
		UIParamsConfig uiParams = UIParamsConfig.getUIParamsConfig();
		
		buf.append("ui_params_max_participants:\"");
		int participantLimit = conf.getParticipantLimit();
		if(participantLimit == -1){
			participantLimit = ConferenceConsoleConstants.getMaxParticipantsPerConference();
		}
		buf.append(participantLimit);
		buf.append("\",");
		
		HashMap uips = uiParams.getUIParams();
		Iterator keys = uips.keySet().iterator();
		while (keys.hasNext())
		{
			String key = (String)keys.next();
			String value = (String)uips.get(key);
			
			buf.append("ui_params_"+key+":\"");
			buf.append(value);
			buf.append("\",");
		}
		
		buf.append("ui_params_max_meeting_time:\"");
		buf.append(ConferenceConsoleConstants.getMaxMeetingLengthMinutes());
		buf.append("\",");
	}
	*/
}
