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

import java.util.Date;
import java.util.HashMap;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IRecordingManager;
import com.dimdim.conference.model.RecordingControlEvent;
import com.dimdim.recording.MeetingRecorder;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class RecordingManager extends ConferenceFeatureManager implements IRecordingManager
{
	protected	IConference		conference;
	protected	Event 			lastControlEvent;
	protected	MeetingRecorder	meetingRecorder;
	
	public	RecordingManager(IConference conference)
	{
		this.conference = conference;
		this.setClientEventPublisher(((ActiveConference)conference).getClientEventPublisher());
	}
	public void setMeetingRecorder(MeetingRecorder meetingRecorder)
	{
		this.meetingRecorder = meetingRecorder;
	}
	public	IConference	getConference()
	{
		return	this.conference;
	}
	public Event getLastControlEvent()
	{
		return lastControlEvent;
	}
	public void SetRecordingStopOptions(boolean saveRecording,
			boolean uploadRecordingToBlipTV,
			String title, String description, String category, String keywords)
	{
		if (this.meetingRecorder != null)
		{
			this.meetingRecorder.setStopRecordingOptions(saveRecording,
				uploadRecordingToBlipTV, title, description, category, keywords);
		}
	}
	public	void	startRecording(IConferenceParticipant presenter)
	{
		RecordingControlEvent ave = RecordingControlEvent.createStartEvent(
				this.getConference().getConfig().getConferenceKey(), presenter.getId());
		
		this.lastControlEvent = this.dispatchRecordingControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_RECORDING,
				ConferenceConstants.EVENT_RECORDING_CONTROL);
	}
	public	void	stopRecording(IConferenceParticipant presenter)
	{
		RecordingControlEvent ave = RecordingControlEvent.createStopEvent(
				this.getConference().getConfig().getConferenceKey(), presenter.getId());
		
		this.dispatchRecordingControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_RECORDING,
				ConferenceConstants.EVENT_RECORDING_CONTROL);
		this.lastControlEvent = null;
	}
	protected	Event	dispatchRecordingControlEvent(IConferenceParticipant presenter,
			RecordingControlEvent vce, String featureId, String eventId)
	{
		Event event = new Event(featureId, eventId, new Date(),
				ConferenceConstants.RESPONSE_OK, vce );
		
//		this.getClientEventPublisher().dispatchEventToAllClientsExcept(event,presenter);
		this.getClientEventPublisher().dispatchEventToAllClients(event);
		
		return	event;
	}
}
