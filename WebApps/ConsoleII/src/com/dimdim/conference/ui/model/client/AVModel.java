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
import	com.dimdim.conference.ui.json.client.UIStreamControlEvent;
import	com.dimdim.conference.ui.json.client.JSONurlReader;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class AVModel	extends	FeatureModel
{
	protected	UIRosterEntry	me;
	
	public	AVModel(UIRosterEntry me)
	{
		super("feature.video");
		
		this.me = me;
	}
	public	void	startAVPublish(String mediaId,String profile)
	{
		this.trackAjaxCall("/console/av/start/"+ConferenceGlobals.conferenceKey);
		String url = this.commandsFactory.getStartVideoURL(mediaId, profile);
		this.executeCommand(url);
	}
	public	void	stopAVPublish(String mediaId)
	{
		this.trackAjaxCall("/console/av/stop/"+ConferenceGlobals.conferenceKey);
		String url = this.commandsFactory.getStopVideoURL(mediaId);
		this.executeCommand(url);
	}
	public	void	startAudioPublish(String mediaId)
	{
		this.trackAjaxCall("/console/audio/start/"+ConferenceGlobals.conferenceKey);
		String url = this.commandsFactory.getStartAudioURL(mediaId);
		this.executeCommand(url);
	}
	public	void	stopAudioPublish(String mediaId)
	{
		this.trackAjaxCall("/console/audio/stop/"+ConferenceGlobals.conferenceKey);
		String url = this.commandsFactory.getStopAudioURL(mediaId);
		this.executeCommand(url);
	}
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert(eventId);
		if (data != null)
		{
//			Window.alert(data.toString());
			UIStreamControlEvent	event = (UIStreamControlEvent)data;
			if (event.getEventType().equals(UIStreamControlEvent.START))
			{
				this.onStartStream(event);
			}
			else if (event.getEventType().equals(UIStreamControlEvent.STOP))
			{
				this.onStopStream(event);
			}
		}
		else
		{
//			Window.alert("No data for stream sharing event");
		}
	}
	protected	void	onStartStream(UIStreamControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((AVModelListener)iter.next()).onStartVideo(event.getConferenceKey(),
					event.getResourceId(),
					event.getStreamType(),event.getStreamName(),event.getProfile(),
					event.getWidth());
		}
	}
	protected	void	onStopStream(UIStreamControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((AVModelListener)iter.next()).onStopVideo(event.getConferenceKey(),
					event.getResourceId(),
					event.getStreamType(),event.getStreamName());
		}
	}
	private	native	String	trackAjaxCall(String url) /*-{
		return	$wnd.trackAjaxUrl(url);
	}-*/;
}

