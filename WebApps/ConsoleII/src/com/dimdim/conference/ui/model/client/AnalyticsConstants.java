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

package com.dimdim.conference.ui.model.client;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class AnalyticsConstants
{
	public	static	final	String	FEATURE_MEETING	=	"meeting";
	public	static	final	String	FEATURE_USER	=	"user";
	public	static	final	String	FEATURE_AV	=	"audiovideo";
	public	static	final	String	FEATURE_AUDIO	=	"audio";
	public	static	final	String	FEATURE_UPLOAD	=	"upload";
	public	static	final	String	FEATURE_CHAT	=	"chat";
	public	static	final	String	FEATURE_WHITEBOARD	=	"whiteboard";
	public	static	final	String	FEATURE_DESKTOP_SHARING	=	"desktop_sharing";
	public	static	final	String	FEATURE_FULL_SCREEN	=	"full_screen";
	public	static	final	String	FEATURE_ATTENDEE_OS	=	"attendee_os";
	public	static	final	String	FEATURE_PRESENTER_OS	=	"presenter_os";
	
	public	static	final	String	COMMAND_INVITATION	=	"invitation";
	public	static	final	String	COMMAND_PPT	=	"ppt";
	public	static	final	String	COMMAND_PDF	=	"pdf";
	public	static	final	String	COMMAND_MIKE_ENABLE	=	"mike_enable";
	public	static	final	String	COMMAND_MIKE_DISABLE	=	"mike_disable";
	public	static	final	String	COMMAND_AV	=	"audiovideo";
	public	static	final	String	COMMAND_AUDIO	=	"audio";
	public	static	final	String	COMMAND_START	=	"start";
	public	static	final	String	COMMAND_STOP	=	"stop";
	public	static	final	String	COMMAND_PUBLIC	=	"public";
	public	static	final	String	COMMAND_PRIVATE	=	"private";
	public	static	final	String	COMMAND_FEEDBACK_SENT	=	"feedback_sent";
	public	static	final	String	COMMAND_ASSISTANT_LAUNCH	=	"assistant_launch";
	public	static	final	String	COMMAND_INFO_LAUNCH	=	"info_launch";
	public	static	final	String	COMMAND_ATTENDEE_JOINED	=	"attendee_joined";
	public	static	final	String	COMMAND_PRESENTER_JOINED	=	"presenter_joined";
	public	static	final	String	COMMAND_ATTENDEE_LEFT	=	"attendee_left";
	public	static	final	String	COMMAND_PRESENTER_LEFT	=	"presenter_left";
	public	static	final	String	COMMAND_LINUX	=	"linux";
	public	static	final	String	COMMAND_WINDOWS	=	"windows";
	public	static	final	String	COMMAND_MAC	=	"mac";
	public	static	final	String	COMMAND_UNKNOWN	=	"unknown";
	
	private	static	boolean	ga_supported = true;
	
	public	static	final	void	reportInvitation()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_INVITATION);
	}
	public	static	final	void	reportPPTUpload()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_UPLOAD, COMMAND_PPT);
	}
	public	static	final	void	reportPDFUpload()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_UPLOAD, COMMAND_PDF);
	}
	public	static	final	void	reportMikeEnable()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_USER, COMMAND_MIKE_ENABLE);
	}
	public	static	final	void	reportMikeDisable()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_USER, COMMAND_MIKE_DISABLE);
	}
	public	static	final	void	reportAVMeetingStarted()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_AV);
	}
	public	static	final	void	reportAudioMeetingStarted()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_AUDIO);
	}
	public	static	final	void	reportAVStarted()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_AV, COMMAND_START);
	}
	public	static	final	void	reportAVStopped()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_AV, COMMAND_STOP);
	}
	public	static	final	void	reportAudioStarted()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_AUDIO, COMMAND_START);
	}
	public	static	final	void	reportAudioStopped()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_AUDIO, COMMAND_STOP);
	}
	public	static	final	void	reportPublicChatMessage()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_CHAT, COMMAND_PUBLIC);
	}
	public	static	final	void	reportPrivateChatMessage()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_CHAT, COMMAND_PRIVATE);
	}
	public	static	final	void	reportWhiteboardStarted()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_AUDIO, COMMAND_START);
	}
	public	static	final	void	reportWhiteboardStopped()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_AUDIO, COMMAND_STOP);
	}
	public	static	final	void	reportDesktopShareStarted()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_DESKTOP_SHARING, COMMAND_START);
	}
	public	static	final	void	reportDesktopShareStopped()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_DESKTOP_SHARING, COMMAND_STOP);
	}
	public	static	final	void	reportFullScreenStarted()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_FULL_SCREEN, COMMAND_START);
	}
	public	static	final	void	reportFullScreenStopped()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_FULL_SCREEN, COMMAND_STOP);
	}
	public	static	final	void	reportFeedbackSent()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_FEEDBACK_SENT);
	}
	public	static	final	void	reportAssistantLaunched()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_ASSISTANT_LAUNCH);
	}
	public	static	final	void	reportMeetingInfoLaunched()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_INFO_LAUNCH);
	}
	public	static	final	void	reportPresenterJoined()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_PRESENTER_JOINED);
	}
	public	static	final	void	reportAttendeeJoined()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_ATTENDEE_JOINED);
	}
	public	static	final	void	reportPresenterLeft()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_PRESENTER_LEFT);
	}
	public	static	final	void	reportAttendeeLeft()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_MEETING, COMMAND_ATTENDEE_LEFT);
	}
	public	static	final	void	reportPresenterOS()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_PRESENTER_OS, getOSType());
	}
	public	static	final	void	reportAttendeeOS()
	{
		AnalyticsConstants.reportAjaxUrl(FEATURE_ATTENDEE_OS, getOSType());
	}
	private	static	void	reportAjaxUrl(String feature, String command)
	{
		if (AnalyticsConstants.ga_supported)
		{
			try
			{
				String	ajaxUrl = "/console/"+feature+"/"+command+"/"+
					ConferenceGlobals.conferenceKey+"/"+ConferenceGlobals.conferenceId;
				AnalyticsConstants.trackAjaxCall(ajaxUrl);
			}
			catch(Exception e)
			{
				AnalyticsConstants.ga_supported = false;
			}
		}
	}
	private	static	native	String	trackAjaxCall(String url) /*-{
		return	$wnd.trackAjaxUrl(url);
	}-*/;
	private static	native String getOSType() /*-{
		return	$wnd.os_type;
	}-*/;
}
