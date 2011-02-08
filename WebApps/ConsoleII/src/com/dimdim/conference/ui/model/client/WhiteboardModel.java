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
import	com.dimdim.conference.ui.json.client.UIWhiteboardControlEvent;
import	com.dimdim.conference.ui.json.client.JSONurlReader;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class WhiteboardModel	extends	FeatureModel
{
	protected	UIRosterEntry	me;
	public	static	final	String		ModelFeatureId	=	"feature.wb";
//	protected	UIWhiteboardControlEvent	lastEvent;
	
	public	WhiteboardModel(UIRosterEntry me)
	{
		super("feature.wb");
		
		this.me = me;
	}
	public	void	startWhiteboard(String streamId)
	{
		AnalyticsConstants.reportWhiteboardStarted();
		String url = webappRoot+"Whiteboard.action?"+sessionKeyParam+"&cmd=s&si="+streamId;
		this.executeCommand(url);
	}
	public	void	stopWhiteboard(String streamId)
	{
		AnalyticsConstants.reportWhiteboardStopped();
		String url = webappRoot+"Whiteboard.action?"+sessionKeyParam+"&cmd=p&si="+streamId;
		this.executeCommand(url);
	}
	public	void	lockWhiteboard(String streamId)
	{
		String url = webappRoot+"Whiteboard.action?"+sessionKeyParam+"&cmd=l&si="+streamId;
		this.executeCommand(url);
	}
	public	void	unlockWhiteboard(String streamId)
	{
		String url = webappRoot+"Whiteboard.action?"+sessionKeyParam+"&cmd=u&si="+streamId;
		this.executeCommand(url);
	}
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert(eventId);
//		if (this.me.isHost())
//		{
//			return;
//		}
		if (data != null)
		{
//			Window.alert(data.toString());
			if (this.objects.size() > 0)
			{
				this.objects.clear();
			}
			UIWhiteboardControlEvent	event = (UIWhiteboardControlEvent)data;
			if (event.getEventType().equals(UIWhiteboardControlEvent.START))
			{
				this.onStartWhiteboard(event);
				this.objects.add(event);
			}
			else if (event.getEventType().equals(UIWhiteboardControlEvent.STOP))
			{
				this.onStopWhiteboard(event);
			}
			else if (event.getEventType().equals(UIWhiteboardControlEvent.LOCK))
			{
				this.onLockWhiteboard(event);
				this.objects.add(event);
			}
			else if (event.getEventType().equals(UIWhiteboardControlEvent.UNLOCK))
			{
				this.onUnlockWhiteboard(event);
				this.objects.add(event);
			}
		}
		else
		{
//			Window.alert("No data for stream sharing event");
		}
	}
	protected	String		getPopoutJsonEventName()
	{
		return	"wce";
	}
	protected	String		getPopoutJsonEventDataType()
	{
		return	"object";
	}
	protected	void	onStartWhiteboard(UIWhiteboardControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((WhiteboardModelListener)iter.next()).onWhiteboardStarted(event);
		}
	}
	protected	void	onStopWhiteboard(UIWhiteboardControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((WhiteboardModelListener)iter.next()).onWhiteboardStopped(event);
		}
	}
	protected	void	onLockWhiteboard(UIWhiteboardControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((WhiteboardModelListener)iter.next()).onLockWhiteboard(event);
		}
	}
	protected	void	onUnlockWhiteboard(UIWhiteboardControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((WhiteboardModelListener)iter.next()).onUnlockWhiteboard(event);
		}
	}
}

