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

import java.util.Iterator;

import	com.dimdim.conference.ui.json.client.UIRosterEntry;
import	com.dimdim.conference.ui.json.client.UISettingsEvent;
import	com.dimdim.conference.ui.json.client.JSONurlReader;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The initial values of these parameters will be set by console while
 * loading. The network profile and image quality truely belongs to the
 * current publisher, hence they are maintained in roster entry as well
 * as here.
 */

public class SettingsModel	extends	FeatureModel
{
	protected	UIRosterEntry	me;
	
	protected	boolean		lobbyEnabled	=	false;
	protected	int			maxAttendees	=	20;
	protected	int			maxTime	=	60;	//	minutes
	protected	int			networkProfile	=	2;	//	1 dial up, 2 cable, 3 lan.
	protected	String		imageQuality	=	"high";	//	low / medium / high
	protected 	boolean 	mouseTrack = false;
	
	public	SettingsModel(UIRosterEntry me)
	{
		super("feature.settings");
		
		this.me = me;
		String s = this.getLobbyEnabled();
		if (s != null && s.equals("true"))
		{
			this.lobbyEnabled = true;
		}
	}
	public String getImageQuality()
	{
		return imageQuality;
	}
	public void setImageQuality(String imageQuality)
	{
		this.imageQuality = imageQuality;
	}
	public boolean isLobbyEnabled()
	{
		return lobbyEnabled;
	}
	public void setLobbyEnabled(boolean lobbyEnabled)
	{
		this.lobbyEnabled = lobbyEnabled;
	}

	public boolean isMouseTrackEnabled()
	{
		return mouseTrack;
	}
	
	public void setMouseTrackEnabled(boolean mouseTrack)
	{
		this.mouseTrack = mouseTrack;
	}
	
	public int getMaxAttendees()
	{
		return maxAttendees;
	}
	public void setMaxAttendees(int maxAttendees)
	{
		this.maxAttendees = maxAttendees;
	}
	public int getMaxTime()
	{
		return maxTime;
	}
	public void setMaxTime(int maxTime)
	{
		this.maxTime = maxTime;
	}
	public int getNetworkProfile()
	{
		return networkProfile;
	}
	public void setNetworkProfile(int networkProfile)
	{
		this.networkProfile = networkProfile;
	}
	public	void	changeSettings(UISettingsEvent event)
	{
//		String url = this.commandsFactory.getChangeSettingsURL(event);
//		this.executeCommand(url);
	}
	/**
	 * individual commands to change the settings at a more granular level
	 * are also supported. However if user changes more than one parameter
	 * at a time the en masse change is better than multiple commans.
	 */
	public	void	enableLobby()
	{
//		String url = this.commandsFactory.getStartVideoURL(ConferenceGlobals.conferenceKey,
//				this.me.getUserId(),mediaId);
//		this.executeCommand(url);
	}
	public	void	disableLobby()
	{
//		String url = this.commandsFactory.getStopVideoURL(ConferenceGlobals.conferenceKey,
//				this.me.getUserId(),mediaId);
//		this.executeCommand(url);
	}
	public	void	setMaxParticipants(int maxNum)
	{
//		String url = this.commandsFactory.getStartAudioURL(userId);
//		this.executeCommand(url);
	}
	/**
	 * the settings change event is raised
	 */
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert(eventId);
		if (data != null)
		{
//			Window.alert(data.toString());
			UISettingsEvent	event = (UISettingsEvent)data;
			if (event.getEventType().equals(UISettingsEvent.CHANGE))
			{
				//	Compare the old values against new values and raise events
				//	accordingly.
				this.lobbyEnabled = (new Boolean(event.getLobby())).booleanValue();
			}
		}
		else
		{
//			Window.alert("No data for stream sharing event");
		}
	}
	protected	void	raiseLobbyChangedEvent()
	{
		
	}
	protected	void	raiseNetworkProfileChangedEvent(String oldValue, String newValue)
	{
		
	}
	protected	void	raiseImageQualityChangedEvent(String oldValue, String newValue)
	{
		
	}
    private String getLobbyEnabled()
    { 
		return ConferenceGlobals.userInfoDictionary.getStringValue("lobby_enabled");
	}
}

