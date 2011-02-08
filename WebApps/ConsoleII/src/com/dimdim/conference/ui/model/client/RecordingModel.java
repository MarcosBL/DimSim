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
import	com.dimdim.conference.ui.json.client.UIRecordingControlEvent;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class RecordingModel	extends	FeatureModel
{
	protected	UIRosterEntry	me;
	public	static	final	String		ModelFeatureId	=	"feature.recording";
	protected	boolean		recordingActive;
	
	public	RecordingModel(UIRosterEntry me)
	{
		super("feature.recording");
		
		this.me = me;
	}
	public	boolean	isRecordingActive()
	{
		return	this.recordingActive;
	}
	public	void	toggleRecording()
	{
		if (this.recordingActive)
		{
			this.stopRecording();
			this.recordingActive = false;
		}
		else
		{
			this.startRecording();
			this.recordingActive = true;
		}
	}
	public	void	startRecording()
	{
//		AnalyticsConstants.reportRecordingStarted();
		String url = webappRoot+"Recording.action?"+sessionKeyParam+"&cmd=start";
		this.executeCommand(url);
	}
	public	void	stopRecording()
	{
//		AnalyticsConstants.reportRecordingStopped();
		String url = webappRoot+"Recording.action?"+sessionKeyParam+"&cmd=stop";
		this.executeCommand(url);
	}
	public	void	SetRecordingStopOptions(boolean saveRecording,
			boolean	uploadRecordingToBlipTV, String title,
			String description, String category, String keywords)
	{
		String url = webappRoot+"SetRecordingStopOptions.action?"+sessionKeyParam+
				"&saveRecording="+saveRecording+
				"&uploadRecordingToBlipTV="+uploadRecordingToBlipTV+
				"&title="+title+
				"&description="+description+
				"&category="+category+
				"&keywords="+keywords;
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
			UIRecordingControlEvent	event = (UIRecordingControlEvent)data;
			if (event.getEventType().equals(UIRecordingControlEvent.START))
			{
				this.onStartRecording(event);
				this.objects.add(event);
			}
			else if (event.getEventType().equals(UIRecordingControlEvent.STOP))
			{
				this.onStopRecording(event);
			}
		}
		else
		{
//			Window.alert("No data for stream sharing event");
		}
	}
	protected	String		getPopoutJsonEventName()
	{
		return	"RecordingControlEvent";
	}
	protected	String		getPopoutJsonEventDataType()
	{
		return	"object";
	}
	protected	void	onStartRecording(UIRecordingControlEvent event)
	{
		this.recordingActive = true;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RecordingModelListener)iter.next()).onRecordingdStarted(event);
		}
	}
	protected	void	onStopRecording(UIRecordingControlEvent event)
	{
		this.recordingActive = false;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((RecordingModelListener)iter.next()).onRecordingStopped(event);
		}
	}
}

