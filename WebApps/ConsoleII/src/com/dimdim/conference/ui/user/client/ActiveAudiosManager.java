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

package com.dimdim.conference.ui.user.client;

import com.dimdim.conference.ui.common.client.LayoutGlobals;
import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.common.client.util.FlashCallbackHandler;
import com.dimdim.conference.ui.common.client.util.FlashStreamHandler;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIStreamControlEvent;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.AudioModelListener;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;
import com.dimdim.conference.ui.model.client.RosterModelAdapter;
import com.google.gwt.user.client.Window;

import pl.rmalinowski.gwt2swf.client.ui.SWFCallableWidget;
import pl.rmalinowski.gwt2swf.client.ui.SWFParams;
import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ActiveAudiosManager	extends	RosterModelAdapter implements FlashStreamHandler, AudioModelListener
{
	protected	UIRosterEntry	me;
//	protected	DmFlashWidget2	movie = null;
	protected	boolean			myAudioEnabled = false;
	protected	boolean			myVideoEnabled = false;
	protected	AttendeeAudioBroadcasterFloat	myAudioBroadcaster;
	protected	String		currentStreamId;
	protected	String		currentMovieId;
	protected	String		streamType = "live";
	protected	boolean		attendeeModeVideo = false;
	
	protected	SWFCallableWidget	movie;
	
	protected	static	ActiveAudiosManager	theManager;
	private int			broadcasterState = UIConstants.MY_BROADCASTER_STATE_INACTIVE;
	private	ListEntry	myListEntry;
	
	//	0 - stop from movie, 1 - stopped by permission change or close, 2 - stopped for reload.
	private	int		stoppedFlag = 0;
	
	//	Rtmp state - 0 - n/a, 1 - stopped or connecting, 2 - acive.
	private	int		rtmpState = 0;
	
	private	ActivePresenterAVWindow	attendeeVideoPlayer;
	
	public	static	ActiveAudiosManager	getManager(UIRosterEntry me)
	{
		if (ActiveAudiosManager.theManager == null)
		{
			ActiveAudiosManager.theManager = new ActiveAudiosManager(me);
		}
		return	ActiveAudiosManager.theManager;
	}
	private	ActiveAudiosManager(UIRosterEntry me)
	{
		this.me = me;
		this.attendeeModeVideo = ConferenceGlobals.isMeetingVideoChat();
		
		ClientModel.getClientModel().getRosterModel().addListener(this);
		ClientModel.getClientModel().getAudioModel().addListener(this);
		if (this.attendeeModeVideo)
		{
			currentStreamId = ConferenceGlobals.getAVStreamId();
		}
		else
		{
			currentStreamId = ConferenceGlobals.getAudioStreamId();
		}
		if (ClientModel.getClientModel().getRecordingModel().isRecordingActive())
		{
			this.streamType = "record";
		}
		currentMovieId = UIConstants.AUDIO_BROADCASTER_PLAYER_MOVIE_ID;//"m"+ConferenceGlobals.getClientGUID();
		if (!UIGlobals.isOrganizer(me))
		{
			FlashCallbackHandler.getHandler().addStreamHandler(this);
		}
	}
	public ListEntry getMyListEntry()
	{
		return myListEntry;
	}
	public void setMyListEntry(ListEntry myListEntry)
	{
		this.myListEntry = myListEntry;
	}
	public	void	setBroadcasterActive()
	{
		//DebugPanel.getDebugPanel().addDebugMessage("ActiveAudiosManager #### inside setBroadcasterActive = me = "+me);
		this.broadcasterState = UIConstants.MY_BROADCASTER_STATE_ACTIVE;
		if (!this.me.isAudioOn())
		{
			this.myListEntry.setImage3Url(null, null);
		}
		else
		{
			if (this.myListEntry != null)
			{
				//DebugPanel.getDebugPanel().addDebugMessage("ActiveAudiosManager #### inside setBroadcasterActive setting audio enabled");
				this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl()
						,ConferenceGlobals.getTooltip("presenter.disable_audio"));
			}
			else
			{
	//			Window.alert("My list entry not available");
			}
		}
		if (this.me.isVideoOn())
		{
			if (this.myListEntry != null)
			{
				//DebugPanel.getDebugPanel().addDebugMessage("ActiveAudiosManager #### inside setBroadcasterActive setting cam enabled");
				this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl()
						,ConferenceGlobals.getTooltip("presenter.disable_video"));
			}
		}
		if (myAudioBroadcaster != null)
		{
			myAudioBroadcaster.setPosition();
		}
	}
	public	void	setBroadcasterConnecting()
	{
		this.broadcasterState = UIConstants.MY_BROADCASTER_STATE_CONNECTING;
		if (this.myListEntry != null)
		{
			//DebugPanel.getDebugPanel().addDebugMessage("ActiveAudiosManager #### inside setBroadcasterConnecting setting audio disabled");
			this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl()
					,ConferenceGlobals.getTooltip("presenter.enable_audio"));
		}
		else
		{
//			Window.alert("My list entry not available");
		}
	}
	public	void	setBroadcasterInactive()
	{
		this.broadcasterState = UIConstants.MY_BROADCASTER_STATE_INACTIVE;
		if (!this.me.isAudioOn())
		{
			//DebugPanel.getDebugPanel().addDebugMessage("ActiveAudiosManager #### inside setBroadcasterInactive setting null");
			this.myListEntry.setImage3Url(null, null);
		}
		else
		{
			if (this.myListEntry != null)
			{
				//DebugPanel.getDebugPanel().addDebugMessage("ActiveAudiosManager #### inside setBroadcasterInactive setting audio disabled");
				this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl()
						,ConferenceGlobals.getTooltip("presenter.enable_audio"));
			}
			else
			{
	//				Window.alert("My list entry not available");
			}
		}
	}
	public	void	setBroadcasterInactiveIfNotConnecting()
	{
		if (this.broadcasterState != UIConstants.MY_BROADCASTER_STATE_CONNECTING)
		{
			this.broadcasterState = UIConstants.MY_BROADCASTER_STATE_INACTIVE;
			if (this.myListEntry != null)
			{
				//DebugPanel.getDebugPanel().addDebugMessage("ActiveAudiosManager #### inside setBroadcasterInactiveIfNotConnecting setting audio disabled");
				this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl()
						,ConferenceGlobals.getTooltip("presenter.enable_audio"));
			}
			else
			{
//				Window.alert("My list entry not available");
			}
		}
	}
	public	void	reloadBroadcaster()
	{
		//DebugPanel.getDebugPanel().addDebugMessage("####inside reloadBroadcaster me = "+me);
		if ((me.isAudioOn() || me.isVideoOn())
				&& this.broadcasterState != UIConstants.MY_BROADCASTER_STATE_CONNECTING)
		{
			if (this.myAudioBroadcaster != null)
			{
				if (this.rtmpState == 2)
				{
					//DebugPanel.getDebugPanel().addDebugMessage("rtmpState = 2");
					//	broadcaster is running;
					this.stoppedFlag = 2;
					this.removeAudioBroadcaster(me);
				}
				else
				{
					//DebugPanel.getDebugPanel().addDebugMessage("forceRemoveAudioBroadcaster and add broadcaster");
					//	Broadcaster is stopped or connecting. Close immediately and restart.
					this.forceRemoveAudioBroadcaster();
					this.addBroadcaster(me);
				}
			}
			else
			{
				//DebugPanel.getDebugPanel().addDebugMessage("just add broadcaster");
				this.addBroadcaster(me);
			}
//			this.addAudioBroadcaster(me);
		}
		//DebugPanel.getDebugPanel().addDebugMessage("$$$$inside reloadBroadcaster");
	}
	/**
	 * If this is me and my audio is on, then add the broadcaster panel, if
	 * I am not the active presenter at the same time. The active presenter
	 * gets the audio and video broadcaster in the discussion bar.
	 */
	public	void	onUserJoined(UIRosterEntry user)
	{
		if (user.getUserId().equals(this.me.getUserId())
				&& !UIGlobals.isOrganizer(user)
				)
		{
			if (!UIGlobals.amInLobby())
			{
				if( user.isAudioOn())
				{
					if (!this.myAudioEnabled)
					{
						//	Double joined event. This should never happen, but has been
						//	seen. So just an extra protection.
						this.addBroadcaster(user);
						this.myAudioEnabled = true;
					}
				}else if(user.isVideoOn())
				{
					if (!this.myVideoEnabled)
					{
						this.addVideoBroadcaster(user);
						this.myVideoEnabled = true;
					}
				}
			}
		}
	}
	public	void	onEntryGranted(UIRosterEntry user)
	{
		if (user.getUserId().equals(this.me.getUserId())
				&& !UIGlobals.isOrganizer(user)
				)
		{
			if( user.isAudioOn())
			{
				if (!this.myAudioEnabled)
				{
					//	Double event. This should never happen, but has been
					//	seen. So just an extra protection.
					this.addBroadcaster(user);
					this.myAudioEnabled = true;
				}
			}else if(user.isVideoOn())
			{
				if (!this.myVideoEnabled)
				{
					this.addVideoBroadcaster(user);
					this.myVideoEnabled = true;
				}
			}
		}
	}
	/**
	 * This event is raised if a user's audio permission is changed. If my
	 * audio is enabled, then add the audio broadcaster, if disabled remove
	 * the broadcaster. The active presenter check here is redundant safety.
	 * No one is authorized to change the permissions of an active presenter.
	 */
	public	void	onAudioVidoPermissionsChanged(UIRosterEntry user)
	{
		if (user.getUserId().equals(this.me.getUserId())
				&& !UIGlobals.isOrganizer(user))
		{
			if(user.isVideoOn())
			{
				if (!this.myVideoEnabled)
				{
					this.addVideoBroadcaster(user);
					this.myVideoEnabled = true;
				}
			}
			else if (user.isAudioOn())
			{
				if (!this.myAudioEnabled)
				{
					this.addBroadcaster(user);
					this.myAudioEnabled = true;
				}
			} 
			else
			{
				if ((this.myAudioEnabled || this.myVideoEnabled) && movie != null)
				{
					this.stoppedFlag = 1;
					this.removeAudioBroadcasterAfterRtmpCheck(user);
					this.myAudioEnabled = false;
					this.myVideoEnabled = false;
				}
			}
		}
	}
	public void onUserLeft(UIRosterEntry user)
	{
		if (user.isAudioOn() || user.isVideoOn())
		{
			closeAttendeeVideo();
		}
	}
	public void onUserRemoved(UIRosterEntry user)
	{
		if (user.isAudioOn() || user.isVideoOn())
		{
			closeAttendeeVideo();
		}
	}
	public	void	consoleClosing()
	{
		this.stoppedFlag = 1;
		removeAudioBroadcasterAfterRtmpCheck(me);
	}
	private	void	continueReloadBroadcaster()
	{
		this.addBroadcaster(this.me);
	}
	
	private	void	addBroadcaster(UIRosterEntry user)
	{
		//DebugPanel.getDebugPanel().addDebugMessage("inside addBroadcaster user = "+user);
		if(user.isVideoOn())
		{
			addVideoBroadcaster(user);
			this.myVideoEnabled = true;
		}else{
			addAudioBroadcaster(user);
			this.myAudioEnabled = true;
		}
	}
	
	private	void	addAudioBroadcaster(UIRosterEntry user)
	{
		//	In case of multiple timing issues, the broadcaster does not go away.
		//	This is a simple additional safety.
		this.forceRemoveAudioBroadcaster();
		this.setBroadcasterConnecting();
		int	width = UIGlobals.getAudioBroadcasterWidth();
		int	height = UIGlobals.getAudioBroadcasterHeight();
		String swfUrl = "swf/audioBroadcaster.swf";
		String captionKey = "audio_broadcaster_caption";
		
		/*if (this.attendeeModeVideo)
		{
			//	Attendee are not allowed to broadcast large videos yet.
			String sizeFactor = "1";
			
			captionKey = "video_broadcaster_caption";
			currentStreamId = ConferenceGlobals.getAVStreamId();
			width = UIGlobals.getAVBroadcasterWidth(sizeFactor);
			height = UIGlobals.getAVBroadcasterHeight(sizeFactor);
			swfUrl = "swf/avBroadcaster.swf";
		}
		else
		{
			currentStreamId = ConferenceGlobals.getAudioStreamId();
		}*/
		currentStreamId = ConferenceGlobals.getAudioStreamId();
		FlashCallbackHandler.getHandler().removeStreamHandler(this);
		if (!UIGlobals.isOrganizer(me))
		{
			FlashCallbackHandler.getHandler().addStreamHandler(this);
		}
		
		String movieUrl = this.getMovieUrl(user,swfUrl,currentStreamId);
		
		SWFParams wbWidgetParams = new SWFParams(movieUrl,width,height);
		movie = new SWFCallableWidget(wbWidgetParams);
		
//		movie = new DmFlashWidget2(currentMovieId+"_id",currentMovieId,
//				width+"",height+"",movieUrl,"white");
		
		myAudioBroadcaster = new AttendeeAudioBroadcasterFloat(this.me,movie,width,height,"1",captionKey,movieUrl);
		myAudioBroadcaster.setPositionToCenter();
		myAudioBroadcaster.show();
		rtmpState = 1;
		movie.show();
		stoppedFlag = 0;
	}
	
	private	void	addVideoBroadcaster(UIRosterEntry user)
	{
		//	In case of multiple timing issues, the broadcaster does not go away.
		//	This is a simple additional safety.
		this.forceRemoveAudioBroadcaster();
		this.setBroadcasterConnecting();
		int	width = UIGlobals.getAudioBroadcasterWidth();
		int	height = UIGlobals.getAudioBroadcasterHeight();
		String swfUrl = "swf/audioBroadcaster.swf";
		String captionKey = "audio_broadcaster_caption";
		
			//	Attendee are not allowed to broadcast large videos yet.
			String sizeFactor = "1";
			
			captionKey = "video_broadcaster_caption";
			currentStreamId = ConferenceGlobals.getAVStreamId();
			width = UIGlobals.getAVBroadcasterWidth(sizeFactor);
			height = UIGlobals.getAVBroadcasterHeight(sizeFactor);
			swfUrl = "swf/avBroadcaster.swf";
		
		FlashCallbackHandler.getHandler().removeStreamHandler(this);
		if (!UIGlobals.isOrganizer(me))
		{
			FlashCallbackHandler.getHandler().addStreamHandler(this);
		}
		
		String movieUrl = this.getMovieUrl(user,swfUrl,currentStreamId);
		
		SWFParams wbWidgetParams = new SWFParams(movieUrl,width,height);
		movie = new SWFCallableWidget(wbWidgetParams);
		
//		movie = new DmFlashWidget2(currentMovieId+"_id",currentMovieId,
//				width+"",height+"",movieUrl,"white");
		
		myAudioBroadcaster = new AttendeeAudioBroadcasterFloat(this.me,movie,width,height,"1",captionKey,movieUrl);
		myAudioBroadcaster.setPositionToCenter();
		myAudioBroadcaster.show();
		rtmpState = 1;
		movie.show();
		stoppedFlag = 0;
	}
	private	void	removeAudioBroadcasterAfterRtmpCheck(UIRosterEntry user)
	{
		if (rtmpState == 2)
		{
			this.removeAudioBroadcaster(user);
		}
		else
		{
			this.forceRemoveAudioBroadcaster();
		}
	}
	protected	void	removeAudioBroadcaster(UIRosterEntry user)
	{
		if (this.movie != null)
		{
			try
			{
				movie.call("stopBroadcastingOrReceiving");
//				if (!movie.stop(currentMovieId))
//				{
//					handleEvent("stopAudio");
//				}
			}
			catch(Exception e)
			{
//				Window.alert(e.getMessage());
				handleEvent("stopAudio");
			}
		}
	}
	private	void	forceRemoveAudioBroadcaster()
	{
		if (this.myAudioBroadcaster != null)
		{
			LayoutGlobals.setAVFloatSlotAvailable(this.myAudioBroadcaster.getFloatPositionIndex());
			this.myAudioBroadcaster.hide();
			this.myAudioBroadcaster = null;
			this.movie = null;
		}
	}
	
	
	private	String	getMovieUrl(UIRosterEntry user, String swfUrl, String streamName)
	{
		String	rtmpUrl = UIGlobals.getStreamingUrlsTable().getAudioRtmpUrl();
		String	rtmptUrl = UIGlobals.getStreamingUrlsTable().getAudioRtmptUrl();
		String	siteName = "A";
		if (this.attendeeModeVideo)
		{
			siteName = "AV";//"attendee.video";
		}
		boolean videoOnly = ConferenceGlobals.presenterAVVideoOnly;
		if (videoOnly)
		{
			siteName += "C";
		}
		boolean handsFreeOnLoad = UIResources.getUIResources().getConferenceInfo("hands_free_on_load").equals("true");
		if (handsFreeOnLoad)
		{
			siteName += "H";
		}
		String movieName = swfUrl;
		String pubName = user.getDisplayName();
		String pubProfile = UIGlobals.getDefaultAudioNetworkProfile();
		if (movieName != null)
		{
			StringBuffer buf = new StringBuffer(movieName);
			buf.append("?");
			buf.append(rtmpUrl);
			buf.append("$");
			buf.append(streamName);
			buf.append("$");
			buf.append(pubProfile);
			buf.append("$");
			buf.append(pubName);
			buf.append("$");
			buf.append(siteName+"."+this.streamType);
			buf.append("$");
			buf.append(rtmptUrl);
			buf.append("$");
			buf.append(ConferenceGlobals.webappRoot);
			buf.append("$");
			buf.append(ConferenceGlobals.sessionKey);
			buf.append("$");
			buf.append(ConferenceGlobals.conferenceKey);
			buf.append("$");
			buf.append(this.me.getUserId());
			
			movieName = buf.toString();
		}
		return	movieName;
	}
	public String getStreamName()
	{
		return this.currentStreamId;
	}
	public void handleEvent(String eventName)
	{
		if (eventName.startsWith("start"))
		{
//			Window.alert("Sending av start to all participants");
			ClientModel.getClientModel().getAVModel().
				startAudioPublish(this.currentStreamId);
			ConferenceGlobals.setMyAudioActive(true);
			AnalyticsConstants.reportAudioStarted();
		}
		else if (eventName.startsWith("stop"))
		{
//			Window.alert("Sending av stop to all participants");
			ClientModel.getClientModel().getAVModel().
				stopAudioPublish(this.currentStreamId);
			ConferenceGlobals.setMyAudioActive(false);
			AnalyticsConstants.reportAudioStopped();
		}
		else if (eventName.startsWith("close"))
		{
			//	This event is raised by the movie if user denies
			//	access to the mike or camera by pressing on the
			//	deny option of the flash device access security
			//	warning. The movie can not recover from this
			//	choice selection and only option is to close
			//	the broadcaster altogether.
			this.setBroadcasterInactive();
			this.stoppedFlag = 1;
//			this.removeAudioBroadcaster(me);
			this.forceRemoveAudioBroadcaster();
		}
		else if (eventName.startsWith("no"))
		{
			this.setBroadcasterInactive();
			this.stoppedFlag = 1;
			this.forceRemoveAudioBroadcaster();
			
			DefaultCommonDialog.showMessage("No Devices","Suitable devices were not found. Please attach approripate devices and restart the broadcaster by clicking on the mike button next to your name.");
		}
		else if (eventName.startsWith("error"))
		{
			this.setBroadcasterInactive();
		}
	}
	public void onStartAudio(UIStreamControlEvent event)
	{
		//Window.alert("inside active audio manager onStartAudio");
		DebugPanel.getDebugPanel().addDebugMessage("inside active audio manager onStartAudio");
		String userId = event.getResourceId();
		if (userId.equals(this.me.getUserId()))
		{
			//DebugPanel.getDebugPanel().addDebugMessage("Self event");
			if (!UIGlobals.isOrganizer(me))
			{
				//DebugPanel.getDebugPanel().addDebugMessage("For active presenter");
				this.setBroadcasterActive();
				rtmpState = 2;
				if (this.stoppedFlag == 1)
				{
					this.removeAudioBroadcaster(this.me);
				}
			}
		}
		else
		{
			//	Another user raised audio started event. If the meeting
			//	is in video chat mode, treat this like video start event
			//	and start a video player like the presenter's video start.
			DebugPanel.getDebugPanel().addDebugMessage("Received start audio event for: "+userId);
			//if (this.attendeeModeVideo)
			//{
				//DebugPanel.getDebugPanel().addDebugMessage("In video chat mode - starting attendee video player");
				String streamName = event.getStreamName();
				String profile = event.getProfile();
				
				UIRosterEntry speaker = ClientModel.getClientModel().
							getRosterModel().findRosterEntry(userId);
				//DebugPanel.getDebugPanel().addDebugMessage("speaker: "+speaker);
				//if video is on for the other person then show this
				if(speaker != null)
				{
					if(event.isVideoEvent())
					{
						speaker.setVideo("1");
						//DebugPanel.getDebugPanel().addDebugMessage("Playing stream:"+streamName+", profile:"+profile);
						String movieUrl = this.getMovieUrl(speaker.getDisplayName(), streamName, profile);
						//DebugPanel.getDebugPanel().addDebugMessage("Player url: "+movieUrl);
						showAttendeeVideoPlayer(movieUrl,speaker.getDisplayName());
						DebugPanel.getDebugPanel().addDebugMessage("Attendee video player launch complete");
					}else if(event.isAudioEvent()){
						speaker.setAudio("1");
						DebugPanel.getDebugPanel().addDebugMessage("must be an audio start event...");
					}
				}else{
					DebugPanel.getDebugPanel().addDebugMessage("user entry not found in the roster.. Participant list might be disabled");
				}
			//}
		}
	}
	private	String	getMovieUrl(String speakerName, String streamName,String profile)
	{
		String	rtmpUrl = UIGlobals.getStreamingUrlsTable().getAudioRtmpUrl();
		String	rtmptUrl = UIGlobals.getStreamingUrlsTable().getAudioRtmptUrl();
		
		String siteName = "P";
		boolean videoOnly = ConferenceGlobals.presenterAVVideoOnly;
		if (videoOnly)
		{
			siteName += "C";
		}
		
		String	movieName = "swf/avPlayer.swf";
		
			StringBuffer buf = new StringBuffer(movieName);
			buf.append("?");
			buf.append(rtmpUrl);
			buf.append("$");
			buf.append(streamName);
			buf.append("$");
			buf.append(profile);
			buf.append("$");
			buf.append(speakerName);
			buf.append("$");
			buf.append(siteName+"."+this.streamType);
			buf.append("$");
			buf.append(rtmptUrl);
			
		return	buf.toString();
	}
	private	void	showAttendeeVideoPlayer(String movieUrl, String speakerName)
	{
		DebugPanel.getDebugPanel().addDebugMessage("showAttendeeVideoPlayer -- 1");
		this.closeAttendeeVideo();
		String caption = speakerName;
		if (caption.length() > 10)
		{
			caption = caption.substring(0,10)+"...";
		}
		
		//	Attendee are not allowed to broadcast large videos yet.
		int movieWidth = UIGlobals.getAVBroadcasterWidth("1");
		int movieHeight = UIGlobals.getAVBroadcasterHeight("1");
		DebugPanel.getDebugPanel().addDebugMessage("showAttendeeVideoPlayer -- 2");
		attendeeVideoPlayer = new ActivePresenterAVWindow(this.me,caption,movieUrl,
				movieWidth,movieHeight,"A","1",null);
		DebugPanel.getDebugPanel().addDebugMessage("showAttendeeVideoPlayer -- 3");
		attendeeVideoPlayer.setPosition();
		attendeeVideoPlayer.show();
		DebugPanel.getDebugPanel().addDebugMessage("showAttendeeVideoPlayer -- 4");
		attendeeVideoPlayer.startPresenterAudioVideo();
	}
	public void onStopAudio(UIStreamControlEvent event)
	{
		DebugPanel.getDebugPanel().addDebugMessage("inside active audio manager onStopAudio event = "+event);
		String userId = event.getResourceId();

		//UIRosterEntry speaker = ClientModel.getClientModel().getRosterModel().findRosterEntry(userId);
		//DebugPanel.getDebugPanel().addDebugMessage("inside active audio manager onStopAudio speaker = "+speaker);
		//Window.alert("inside active audio manager onStopAudio");
		if (userId.equals(this.me.getUserId()))
		{
			//Window.alert("me equals to user...");
			if (!UIGlobals.isOrganizer(me))
			{
				//Window.alert("me not organizer");
				this.setBroadcasterInactive();
				rtmpState = 1;
				if (this.stoppedFlag == 2)
				{
					//Window.alert("stoppedflag==2 forceremove audio broadcaster..");
					this.forceRemoveAudioBroadcaster();
					if (!UIGlobals.isOrganizer(me))
					{
						//Window.alert("stoppedflag==2 and me is not organizer.."+me);
						this.stoppedFlag = 0;
						this.continueReloadBroadcaster();
					}
				}
				else if (this.stoppedFlag == 1)
				{
					//Window.alert("forceRemoveAudioBroadcaster...");
					this.forceRemoveAudioBroadcaster();
				}
			}
		}
		else
		{
			//	Another user raised audio stopped event. If the meeting
			//	is in video chat mode, treat this like video stop event
			//	and close the video player like the presenter's video stop.
			if(event.isVideoEvent())
			{
				DebugPanel.getDebugPanel().addDebugMessage("closing attendee video...");
				this.closeAttendeeVideo();
			}
		}
		//Window.alert("end...");
	}
	private	void	closeAttendeeVideo()
	{
		if (attendeeVideoPlayer != null)
		{
			attendeeVideoPlayer.stopPresenterAudioVideo();
			attendeeVideoPlayer.hide();
			attendeeVideoPlayer = null;
		}
	}
	public	void	restartAsRecord()
	{
//		if (UIGlobals.isActivePresenter(me))
//		{
			this.streamType = "record";
			if (this.myAudioBroadcaster != null)
			{
				this.myAudioBroadcaster.restartAsRecord();
			}
//		}
	}
	public	void	restartAsLive()
	{
//		if (UIGlobals.isActivePresenter(me))
//		{
			this.streamType = "live";
			if (this.myAudioBroadcaster != null)
			{
				this.myAudioBroadcaster.restartAsLive();
			}
//		}
	}
}

