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
 *	File Name  : AVWindow.java
 *  Created On : Jun 30, 2006
 *  Created By : Saurav Mohapatra
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
import com.dimdim.conference.ui.model.client.AVModelListener;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.AudioModelListener;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * 
 */
public class ActivePresenterAVManager implements AVModelListener, AudioModelListener, FlashStreamHandler
{
	private String rtmpUrl = null;
	private String rtmptUrl = null;
	private String pubProfile = null;
//	private String siteName = null;
	private String movieName = null;
	private	String	movieUrl = null;
	private	String	streamType = "live";
	private boolean audioOnly = false;
	
	private	UIRosterEntry	me;
//	private	UIRosterEntry	currentActivePresenter;
	private	UIRosterEntry	currentHost;
	private	String			presenterAV;
	private	String		captionSuffix = "";
	
	private	ActivePresenterAVWindow		presenterAVFloat;
	private String		currentStreamId;
	private int			broadcasterState = UIConstants.MY_BROADCASTER_STATE_INACTIVE;
	private	ListEntry	myListEntry;
//	protected	ActivePresenterAVListener	activePresenterAVListener;
	
	//	0 - stopped by the movie stop button, 1 - no op, 2 - reload stopped.
	private	int		stoppedFlag = 0;
	
	//	Rtmp state - 0 - n/a, 1 - stopped or connecting, 2 - acive.
	private	int		rtmpState = 0;
	
	//	This is the size factor of the current active presenter's av.
	private	String	presenterVideoSizeFactor = "1";
	
	public	static	ActivePresenterAVManager	presenterAVManager;
	
	public	static	ActivePresenterAVManager	getPresenterAVManager(UIRosterEntry me)
	{
		if (ActivePresenterAVManager.presenterAVManager == null)
		{
			ActivePresenterAVManager.presenterAVManager = new ActivePresenterAVManager(me);
		}
		return	ActivePresenterAVManager.presenterAVManager;
	}
	private ActivePresenterAVManager(UIRosterEntry me)
	{
//		Window.alert("Constructing presenter av manager");
		this.presenterAV = getPresenterAVOption();
		this.me = me;
		
		ClientModel.getClientModel().getAVModel().addListener(this);
		ClientModel.getClientModel().getAudioModel().addListener(this);
		
		this.rtmpUrl = UIGlobals.getStreamingUrlsTable().getAvRtmpUrl();
		this.rtmptUrl = UIGlobals.getStreamingUrlsTable().getAvRtmptUrl();
//		this.siteName = ConferenceGlobals.serverAddress;
		currentStreamId = ConferenceGlobals.getAVStreamId();
		this.setMovieUrl(currentStreamId);
		
		if (UIGlobals.isOrganizer(me))
		{
			FlashCallbackHandler.getHandler().addStreamHandler(this);
			this.setMovieUrl(currentStreamId);
			
			String captionKey = "video_broadcaster_caption";
			if (ConferenceGlobals.isPresenterAVAudioOnly())
			{
				captionKey = "audio_broadcaster_caption";
			}
			captionSuffix = ConferenceGlobals.uiStringsDictionary.getStringValue(captionKey);
			if (captionSuffix == null || captionSuffix.equals(captionKey))
			{
				captionSuffix = "";
			}
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
	public	void	setBroadcasterActive(boolean isVideo)
	{
		this.broadcasterState = UIConstants.MY_BROADCASTER_STATE_ACTIVE;
		//Window.alert("inside setBroadcasterActive setting UserAudioEnabledImage");
		if (this.myListEntry != null)
		{
			if (isVideo) 
				{
				//DebugPanel.getDebugPanel().addDebugMessage("ActivePresenterAVManager #### inside setBroadcasterActive setting cam enabled");
					this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl()
							,ConferenceGlobals.getTooltip("presenter.disable_video"));
				}else{
					//DebugPanel.getDebugPanel().addDebugMessage("ActivePresenterAVManager #### inside setBroadcasterActive setting audio enabled");
					this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl()
							,ConferenceGlobals.getTooltip("presenter.disable_audio"));
				}
			//this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl());
		}
		else
		{
//			Window.alert("My list entry not available");
		}
		//DebugPanel.getDebugPanel().addDebugMessage("this.presenteravfloat = "+presenterAVFloat);
		if (this.presenterAVFloat != null)
		{
			this.presenterAVFloat.setPosition();
		}
	}
	public	void	setBroadcasterConnecting()
	{
		this.broadcasterState = UIConstants.MY_BROADCASTER_STATE_CONNECTING;
		//Window.alert("inside setBroadcasterConnecting setting UserAudioDisabledImage");
		if (this.myListEntry != null)
		{
			//DebugPanel.getDebugPanel().addDebugMessage("ActivePresenterAVManager #### inside setBroadcasterConnecting setting audio disabled");
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
		//Window.alert("inside setBroadcasterInactive setting getUserAudioEnabledImage");
		if (this.myListEntry != null)
		{
			if (ConferenceGlobals.isPresenterAVVideoOnly()) 
			{
				//DebugPanel.getDebugPanel().addDebugMessage("ActivePresenterAVManager #### inside setBroadcasterInactive setting cam disabled");
				this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserCamDisabledImageUrl()
						,ConferenceGlobals.getTooltip("presenter.enable_audio"));
			}else {
				//DebugPanel.getDebugPanel().addDebugMessage("ActivePresenterAVManager #### inside setBroadcasterInactive setting audio disabled");
				this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl()
						,ConferenceGlobals.getTooltip("presenter.enable_video"));
			}
			//this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl());
		}
		else
		{
//			Window.alert("My list entry not available");
		}
	}
	public	void	setBroadcasterInactiveIfNotConnecting()
	{
		//Window.alert("inside setBroadcasterInactiveIfNotConnecting setting UserAudioEnabledImage");
		if (this.broadcasterState != UIConstants.MY_BROADCASTER_STATE_CONNECTING)
		{
			this.broadcasterState = UIConstants.MY_BROADCASTER_STATE_INACTIVE;
			if (this.myListEntry != null)
			{
				//DebugPanel.getDebugPanel().addDebugMessage("ActivePresenterAVManager #### inside setBroadcasterInactiveIfNotConnecting setting audio disabled");
				this.myListEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl()
						,ConferenceGlobals.getTooltip("presenter.enable_audio"));
			}
			else
			{
//				Window.alert("My list entry not available");
			}
		}
	}
	public	void	stopPresenterAV()
	{
		if (UIGlobals.isOrganizer(me) &&
				this.broadcasterState != UIConstants.MY_BROADCASTER_STATE_CONNECTING)
		{
			if (rtmpState == 2)
			{
				//	av is actively running right now.
				this.stoppedFlag = 1;
				this.setBroadcasterConnecting();
				this.hidePresenterAVFloat();
				FlashCallbackHandler.getHandler().removeStreamHandler(this);
			}
			else
			{
				FlashCallbackHandler.getHandler().removeStreamHandler(this);
				this.forceHidePresenterAVFloat();
			}
		}
	}
	public	void	reloadPresenterAV()
	{
//		Window.alert("Broadcaster current state:"+this.broadcasterState);
		if (UIGlobals.isOrganizer(me) &&
				this.broadcasterState != UIConstants.MY_BROADCASTER_STATE_CONNECTING)
		{
			if (rtmpState == 2)
			{
				//	av is actively running right now.
				this.stoppedFlag = 2;
				this.setBroadcasterConnecting();
				this.hidePresenterAVFloat();
				FlashCallbackHandler.getHandler().removeStreamHandler(this);
			}
			else
			{
				FlashCallbackHandler.getHandler().removeStreamHandler(this);
				this.forceHidePresenterAVFloat();
				this.continueReloadPresenterAV();
			}
		}
	}
	private	void	continueReloadPresenterAV()
	{
		this.stoppedFlag = 1;
		currentStreamId = ConferenceGlobals.getAVStreamId();
		this.setMovieUrl(currentStreamId);
		FlashCallbackHandler.getHandler().addStreamHandler(this);
		this.showPresenterAVFloat();
	}
	public	String	getMovieUrl()
	{
		return	this.movieUrl;
	}
	public	boolean	isAudioOnly()
	{
		return	this.audioOnly;
	}
	public	void	setMovieUrl(String streamName)
	{
//		currentActivePresenter = ClientModel.getClientModel().getRosterModel().
//			getCurrentActivePresenter();
		currentHost = ClientModel.getClientModel().getRosterModel().getCurrentHost();
		
//		this.pubProfile = currentActivePresenter.getNetProfile();
		this.pubProfile = currentHost.getNetProfile();
		this.setMovieUrl(streamName,pubProfile);
	}
	public	void	setMovieUrl(String streamName,String profile)
	{
//		UIRosterEntry currentActivePresenter = ClientModel.getClientModel().getRosterModel().
//			getCurrentActivePresenter();
		currentHost = ClientModel.getClientModel().getRosterModel().getCurrentHost();
		boolean broadcaster = false;
//		String pubName = currentActivePresenter.getDisplayName();
		String pubName = currentHost.getDisplayName();
//		this.pubProfile = currentActivePresenter.getNetProfile();
		//Window.alert("presenterAV = "+presenterAV);
		if (UIGlobals.isOrganizer(this.me)/* ||
				this.me.getUserId().equals(currentActivePresenter.getUserId())*/)
		{ 
			if (this.presenterAV.equals("disabled"))
			{
				this.audioOnly = false;
				this.movieName = null;
				broadcaster = false;
			}
			else
			{
				if (this.presenterAV.equals("audio"))
				{
					this.audioOnly = true;
					this.movieName = "swf/audioBroadcaster.swf";
				}
				else
				{
					this.audioOnly = false;
					this.movieName = getAVBroadcasterUrl();
				}
				broadcaster = true;
			}
		}
		else
		{
			if (this.presenterAV.equals("av") || this.presenterAV.equals("video"))
			{
				this.audioOnly = false;
				this.movieName = getAVPlayerUrl();
			}
			else
			{
				this.movieName = null;
			}
		}
		String siteName = "P";
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
		if (this.movieName != null)
		{
			StringBuffer buf = new StringBuffer(this.movieName);
			buf.append("?");
			buf.append(this.rtmpUrl);
			buf.append("$");
			buf.append(streamName);
			buf.append("$");
			buf.append(profile);
			buf.append("$");
			buf.append(pubName);
			buf.append("$");
			buf.append(siteName+".");
			buf.append(this.streamType);
			buf.append("$");
			buf.append(this.rtmptUrl);
			if (broadcaster)
			{
				buf.append("$");
				buf.append(ConferenceGlobals.webappRoot);
				buf.append("$");
				buf.append(ConferenceGlobals.sessionKey);
				buf.append("$");
				buf.append(ConferenceGlobals.conferenceKey);
				buf.append("$");
				buf.append(this.me.getUserId());
			}
			this.movieUrl =	buf.toString();
		}
		//Window.alert(this.movieUrl);
	}
	private	String	getAVBroadcasterUrl()
	{
		if (this.presenterVideoSizeFactor.equals("4"))
		{
			return	"swf/avBroadcaster_size4x.swf";
		}
		else if (this.presenterVideoSizeFactor.equals("25"))
		{
			return	"swf/avBroadcaster_size25x.swf";
		}
		return	"swf/avBroadcaster.swf";
	}
	private	String	getAVPlayerUrl()
	{
		if (this.presenterVideoSizeFactor.equals("4"))
		{
			return	"swf/avPlayer_size4x.swf";
		}
		else if (this.presenterVideoSizeFactor.equals("25"))
		{
			return	"swf/avPlayer_size25x.swf";
		}
		return	"swf/avPlayer.swf";
	}
	public	String	getStreamName()
	{
		return	currentStreamId;
	}
	public	void	handleEvent(String eventName)
	{
		if (eventName.startsWith("start"))
		{
//			Window.alert("Sending av start to all participants");
			this.startPublication();
			ConferenceGlobals.setPresenterAVActive(true);
			AnalyticsConstants.reportAVStarted();
		}
		else if (eventName.startsWith("stop"))
		{
//			Window.alert("Sending av stop to all participants");
			this.stopPublication();
			ConferenceGlobals.setPresenterAVActive(false);
			AnalyticsConstants.reportAVStopped();
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
			this.forceHidePresenterAVFloat();
			this.reportActivePresenterAVResponse();
		}
		else if (eventName.startsWith("no"))
		{
			this.setBroadcasterInactive();
			this.stoppedFlag = 1;
			this.forceHidePresenterAVFloat();
			
			DefaultCommonDialog.showMessage("No Devices","Suitable devices were not found. Please attach approripate devices and restart the broadcaster by clicking on the mike button next to your name.",
					new PopupListener(){
						public void onPopupClosed(PopupPanel popup, boolean autoClosed)
						{
							reportActivePresenterAVResponse();
						}
					});
		}
		else if (eventName.startsWith("error"))
		{
			this.setBroadcasterInactive();
		}
	}
	public void consoleClosing()
	{
		this.stoppedFlag = 1;
		if (this.rtmpState == 2)
		{
			this.hidePresenterAVFloat();
		}
		else
		{
			this.hidePresenterAVFloat();
			this.forceHidePresenterAVFloat();
		}
	}
	private void startPublication()
	{
		ClientModel.getClientModel().getAVModel().startAVPublish(currentStreamId,this.pubProfile);
	}
	private void stopPublication()
	{
		ClientModel.getClientModel().getAVModel().stopAVPublish(currentStreamId);
		
		//	Check the current user's network profile setting. If it has changed then
		//	reload the movie.
		
//		UIRosterEntry currentActivePresenter = ClientModel.getClientModel().getRosterModel().
//			getCurrentActivePresenter();
//		String currentPubProfile = currentActivePresenter.getNetProfile();
//		if (!currentPubProfile.equals(this.pubProfile))
//		{
//			this.setMovieUrl(currentStreamId);
//		}
	}
	/**
	 * These two events are received from the server on active presenter's actions.
	 */
	public	void	onStartVideo(String conferenceKey,
			String resourceId, String streamType, String streamName,String profile, String sizeFactor)
	{
//		Window.alert("Starting AV Player");
		DebugPanel.getDebugPanel().addDebugMessage("inside onStartVideo activepresenter av manager = "+streamName + "sizefactor = "+sizeFactor);
		if (!ConferenceGlobals.isPresenterAVAudioOnly())
		{
			//DebugPanel.getDebugPanel().addDebugMessage("inside if audio only");
			String userId = resourceId;
			this.presenterVideoSizeFactor = sizeFactor;
			//DebugPanel.getDebugPanel().addDebugMessage("me = "+me.getUserId()+" userid = "+userId);
			if (!userId.equals(this.me.getUserId()))
			{
				this.setMovieUrl(streamName,profile);
				if (this.movieUrl != null)
				{
					this.hidePresenterAVFloat();
					this.forceHidePresenterAVFloat();
					this.showPresenterAVFloat();
					if (!this.presenterVideoSizeFactor.equals("1"))
					{
						this.presenterAVFloat.setPositionToWorkspaceCorner();
					}
					else
					{
						this.presenterAVFloat.setPosition();
					}
				}
			}
			else
			{
				//DebugPanel.getDebugPanel().addDebugMessage("inside else");
				rtmpState = 2;
				this.setBroadcasterActive(true);
				this.reportActivePresenterAVResponse();
			}
		}
	}
	public	void	onStopVideo(String conferenceKey,
			String resourceId, String streamType, String streamName)
	{
		if (!ConferenceGlobals.isPresenterAVAudioOnly())
		{
			String userId = resourceId;
			if (!userId.equals(this.me.getUserId()))
			{
				if (!UIGlobals.isOrganizer(this.me))
				{
					this.hidePresenterAVFloat();
					this.presenterAVFloat.hide();
					this.presenterAVFloat = null;
				}
				else
				{
					//	Only an active presenter could raise a video stop
					//	event. This case would mean a stop event raised by
					//	a closing console of a previous host.
				}
			}
			else
			{
				if (userId.equals(this.me.getUserId()) &&
						UIGlobals.isOrganizer(this.me))
				{
					rtmpState = 1;
					this.setBroadcasterInactive();
					if (this.stoppedFlag == 2)
					{
						this.forceHidePresenterAVFloat();
						this.stoppedFlag = 0;
						this.continueReloadPresenterAV();
					}
					else if (this.stoppedFlag == 1)
					{
						this.forceHidePresenterAVFloat();
					}
				}
			}
		}
	}
	protected	void	showPresenterAVFloat()
	{
		//	Show the presenter av float popup. Create if for the first time.
		if (this.movieUrl != null)
		{
			String caption = this.me.getDisplayName();
			if (this.currentHost != null)
			{
				String s = this.currentHost.getDisplayName();
				if (s.length() > 10)
				{
					s = s.substring(0,10)+"...";
				}
				caption = s+ " " +captionSuffix;
			}
			int movieWidth = UIGlobals.getAVBroadcasterWidth(presenterVideoSizeFactor);
			int movieHeight = UIGlobals.getAVBroadcasterHeight(presenterVideoSizeFactor);
			if (isAudioOnly())
			{
				movieHeight = UIGlobals.getAudioBroadcasterHeight();
				movieWidth = UIGlobals.getAudioBroadcasterWidth();
			}
			ClickListener cl = null;
			if (UIGlobals.isOrganizer(this.me) &&
					ConferenceGlobals.isMeetingVideoChat() &&
					ConferenceGlobals.isLargeVideoSupported())
			{
				cl = new ClickListener()
				{
					public void onClick(Widget sender)
					{
						if (presenterVideoSizeFactor.equals("1"))
						{
							presenterVideoSizeFactor = "25";
						}
						else
						{
							presenterVideoSizeFactor = "1";
						}
						reloadPresenterAV();
					}
				};
			}
			presenterAVFloat = new ActivePresenterAVWindow(this.me,caption,this.movieUrl,
					movieWidth,movieHeight,"P",presenterVideoSizeFactor,cl);
			presenterAVFloat.setPositionToCenter();
//			presenterAVFloat.addStyleName("second-level-popup-z-index");
			presenterAVFloat.show();
			rtmpState = 1;
			stoppedFlag = 0;
			presenterAVFloat.startPresenterAudioVideo();
		}
	}
	protected	void	hidePresenterAVFloat()
	{
		//	Simply hide the presenter av float popup.
		if (this.presenterAVFloat != null)
		{
			LayoutGlobals.setAVFloatSlotAvailable(this.presenterAVFloat.getFloatPositionIndex());
			if (!this.presenterAVFloat.stopPresenterAudioVideo())
			{
				if (ConferenceGlobals.isPresenterAVAudioOnly())
				{
					ClientModel.getClientModel().getAVModel().
							stopAudioPublish(this.currentStreamId);
				}
				else
				{
					this.stopPublication();
				}
			}
		}
	}
	private	void	forceHidePresenterAVFloat()
	{
		if (this.presenterAVFloat != null)
		{
			LayoutGlobals.setAVFloatSlotAvailable(this.presenterAVFloat.getFloatPositionIndex());
			this.presenterAVFloat.hide();
			this.presenterAVFloat = null;
		}
	}
	public	void	onStartAudio(UIStreamControlEvent event)
	{
		//Window.alert("inside active presenter audio manager onStartAudio");
		//DebugPanel.getDebugPanel().addDebugMessage("inside active presenter audio manager onStartAudio");
		if (ConferenceGlobals.isPresenterAVAudioOnly())
		{
			String userId = event.getResourceId();
			if (userId.equals(this.me.getUserId()) &&
					UIGlobals.isOrganizer(this.me))
			{
				this.setBroadcasterActive(false);
				this.rtmpState = 2;
				this.reportActivePresenterAVResponse();
			}
		}
	}
	/**
	 * This method must continue the broadcaster reload in case where the broadcaster
	 * is an audio broadcaster. The event raised would be stop audio. If the console
	 * is active presenter then continue load
	 */
	public	void	onStopAudio(UIStreamControlEvent event)
	{
		if (ConferenceGlobals.isPresenterAVAudioOnly())
		{
			String userId = event.getResourceId();
			if (userId.equals(this.me.getUserId()) &&
					UIGlobals.isOrganizer(this.me))
			{
				rtmpState = 1;
				this.setBroadcasterInactive();
				if (this.stoppedFlag == 2)
				{
					this.forceHidePresenterAVFloat();
					this.stoppedFlag = 0;
					this.continueReloadPresenterAV();
				}
				else if (this.stoppedFlag == 1)
				{
					this.forceHidePresenterAVFloat();
				}
			}
		}		
	}
	private String getPresenterAVOption()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("presenter_av");
	}
//	public ActivePresenterAVListener getActivePresenterAVListener()
//	{
//		return activePresenterAVListener;
//	}
//	public void setActivePresenterAVListener(
//			ActivePresenterAVListener activePresenterAVListener)
//	{
//		this.activePresenterAVListener = activePresenterAVListener;
//	}
	public	void	reportActivePresenterAVResponse()
	{
//		if (this.activePresenterAVListener != null)
//		{
//			this.activePresenterAVListener.activePresenterAVResponseReceived();
//		}
	}
	public	boolean	isBroadcasterActive()
	{
		return	this.presenterAVFloat != null;
	}
	public	void	restartAsRecord()
	{
		if (UIGlobals.isActivePresenter(me))
		{
			this.streamType = "record";
			if (this.presenterAVFloat != null)
			{
				this.presenterAVFloat.restartAsRecord();
			}
		}
	}
	public	void	restartAsLive()
	{
		if (UIGlobals.isActivePresenter(me))
		{
			this.streamType = "live";
			if (this.presenterAVFloat != null)
			{
				this.presenterAVFloat.restartAsLive();
			}
		}
	}
}
