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

public class AudioModel	extends	FeatureModel
{
	protected	UIRosterEntry	me;
	
	public	AudioModel(UIRosterEntry me)
	{
		super("feature.audio");
		
		this.me = me;
	}
	public	void	startAudioPublish(String streamId)
	{
//		String url = this.commandsFactory.getStartAudioURL(this.me.getUserId(),streamId);
//		this.executeCommand(url);
	}
	public	void	stopAudioPublish(String StreamId)
	{
//		String url = this.commandsFactory.getStopAudioURL(this.me.getUserId(),streamId);
//		this.executeCommand(url);
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
			((AudioModelListener)iter.next()).onStartAudio(event);
		}
	}
	protected	void	onStopStream(UIStreamControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((AudioModelListener)iter.next()).onStopAudio(event);
		}
	}
}

