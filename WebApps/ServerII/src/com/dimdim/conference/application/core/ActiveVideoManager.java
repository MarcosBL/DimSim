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
import java.util.Vector;
import java.util.Iterator;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IAudioVideoManager;
import com.dimdim.conference.model.Participant;
import com.dimdim.conference.model.StreamControlEvent;
import com.dimdim.conference.model.UserRoster;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This application controls the active resources for the clients. Active
 * resource is simply the resource that is being used at the time. This
 * application employs following concepts and enforces following rules.
 * 
 * Primary responsibility of this object is to control the sharing of
 * resources for the system. The resources sharing is strictly only one
 * at a time. Hence all the sharing control requests are handled by this
 * object, which enforces the sharing rules and provides transperent
 * controls to help the presenter.
 * 
 * The video is always presented in the size determined by the system
 * hence the related parameters in the event can be ignored.
 */

public class ActiveVideoManager extends ConferenceFeatureManager	implements	IAudioVideoManager
{
	protected	ActiveConference		conference;
	
	protected	Event		lastControlEvent;
	
	protected	HashMap		enabledAudios;
	protected	HashMap		activeAudios;
	
	protected	HashMap		enabledVideos;
	protected	HashMap		activeVideos;
	
	public	ActiveVideoManager(ActiveConference conference)
	{
		this.conference = conference;
		this.activeAudios = new HashMap();
		this.enabledAudios = new HashMap();
		this.activeVideos = new HashMap();
		this.enabledVideos = new HashMap();
		this.setClientEventPublisher(conference.getClientEventPublisher());
		((UserRoster)this.conference.getRosterManager().getRosterObject()).setAvResourceManager(this);
	}
	public	IConference	getConference()
	{
		return	this.conference;
	}
	
	/**
	 * Resource tracking methods.
	 */
	public	int		getMaxAudioBroadcasters()
	{
		if(ConferenceConstants.MEETING_AV_TYPE_VIDEO.equalsIgnoreCase(conference.getPresenterAV())
				|| ConferenceConstants.MEETING_AV_TYPE_DISABLED.equalsIgnoreCase(conference.getPresenterAV())
		)
			return 0;
		return conference.getMaxAttendeeMikes();
	}
	public	int		getCurrentAudioBroadcastersCount()
	{
		return	this.enabledAudios.size();
	}
	
	public int getCurrentVideoBroadcastersCount() {
		return this.enabledVideos.size();
	}
	
	public int getMaxVideoBroadcasters() {
		if(ConferenceConstants.MEETING_AV_TYPE_AUDIO.equalsIgnoreCase(conference.getPresenterAV())
				|| ConferenceConstants.MEETING_AV_TYPE_DISABLED.equalsIgnoreCase(conference.getPresenterAV())
		)
			return 0;
	
		return	conference.getMaxAttendeeVideos();
	}
	/**
	 * This method is required and used by roster when a user is added to
	 * meeting, i.e. when the status is changed to in meeting.
	 * @return
	 */
	public	boolean	isAudioBroadcasterAvailable()
	{
		return	(this.getCurrentAudioBroadcastersCount() < this.getMaxAudioBroadcasters());
	}
	public	boolean	isVideoBroadcasterAvailable()
	{
		return	(this.getCurrentVideoBroadcastersCount() < this.getMaxVideoBroadcasters());
	}
	public	void	audioBroadcasterAdded(IConferenceParticipant speaker)
	{
		if(!"disabled".equals(conference.getPresenterAV()))
		{
			((Participant)speaker).getPermissions().setAudioOn();
//			if (conference.getPresenterAV().equals(ConferenceConstants.MEETING_AV_TYPE_VIDEO_CHAT))
//			{
//				this.enabledVideos.put(speaker.getId(),speaker);
//			}
//			else
			{
				this.enabledAudios.put(speaker.getId(),speaker);
			}
		}
	}
	
	public	void	broadroadcasterRemoved(Participant speaker)
	{
		if(speaker.getPermissions().isVideoOn())
		{
			videoBroadcasterRemoved(speaker);
		}else if(speaker.getPermissions().isAudoOn())
		{
			audioBroadcasterRemoved(speaker);
		}
	}
	
	public	void	audioBroadcasterRemoved(IConferenceParticipant speaker)
	{
//		if (conference.getPresenterAV().equals(ConferenceConstants.MEETING_AV_TYPE_VIDEO_CHAT))
//		{
//			this.enabledVideos.remove(speaker.getId());
//		}
//		else
		{
			this.enabledAudios.remove(speaker.getId());
		}
	}
	
	public	void	videoBroadcasterAdded(IConferenceParticipant speaker)
	{
		if(!"disabled".equals(conference.getPresenterAV()))
		{
			((Participant)speaker).getPermissions().setVideoOn();
//			if (conference.getPresenterAV().equals(ConferenceConstants.MEETING_AV_TYPE_VIDEO_CHAT))
//			{
//				this.enabledVideos.put(speaker.getId(),speaker);
//			}
//			else
			{
				this.enabledVideos.put(speaker.getId(),speaker);
			}
		}
	}
	
	public	void	videoBroadcasterRemoved(IConferenceParticipant speaker)
	{
//		if (conference.getPresenterAV().equals(ConferenceConstants.MEETING_AV_TYPE_VIDEO_CHAT))
//		{
//			this.enabledVideos.remove(speaker.getId());
//		}
//		else
		{
			this.enabledVideos.remove(speaker.getId());
		}
	}
	
	public	void	startAVStreaming(IConferenceParticipant presenter, String streamId, String profile,
				String width, String height)
	{
		StreamControlEvent ave = StreamControlEvent.createStartEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				presenter.getId(),"t",ConferenceConstants.RESOURCE_TYPE_VIDEO,streamId,width,height,profile);
		this.dispatchStreamControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_VIDEO, ConferenceConstants.EVENT_VIDEO_CONTROL,null);
	}
	public	void	stopAVStreaming(IConferenceParticipant presenter, String streamId)
	{
		StreamControlEvent ave = StreamControlEvent.createStopEvent(
				this.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				presenter.getId(),"t",ConferenceConstants.RESOURCE_TYPE_VIDEO,streamId,"0","0","3");
		
		this.dispatchStreamControlEvent(presenter, ave,
				ConferenceConstants.FEATURE_VIDEO, ConferenceConstants.EVENT_VIDEO_CONTROL,null);
	}
	public	void	startAudioStreaming(IConferenceParticipant speaker, String streamId)
	{
//		System.out.println("Starting audio streaming for:"+speaker.getId());
		StreamControlEvent ave = StreamControlEvent.createStartEvent(
				this.getConference().getConfig().getConferenceKey(),
				speaker.getId(),
				speaker.getId(),"t",ConferenceConstants.RESOURCE_TYPE_AUDIO,streamId,"0","0","3");
		
		this.dispatchStreamControlEvent(speaker, ave,
				ConferenceConstants.FEATURE_AUDIO, ConferenceConstants.EVENT_AUDIO_CONTROL,speaker.getId());
	}
	public	void	stopAudioStreaming(IConferenceParticipant speaker, String streamId)
	{
//		System.out.println("Stoping audio streaming for:"+speaker.getId());
		this.activeAudios.remove(speaker.getId());
		StreamControlEvent ave = StreamControlEvent.createStopEvent(
				this.getConference().getConfig().getConferenceKey(),
				speaker.getId(),
				speaker.getId(),"t",ConferenceConstants.RESOURCE_TYPE_AUDIO,streamId,"0","0","3");
		
		this.dispatchStreamControlEvent(speaker, ave,
				ConferenceConstants.FEATURE_AUDIO, ConferenceConstants.EVENT_AUDIO_CONTROL,speaker.getId());
	}
	public	void	audioVoiceStarted(IConferenceParticipant speaker, String streamId)
	{
//		System.out.println("Stoping audio streaming for:"+speaker.getId());
		StreamControlEvent ave = StreamControlEvent.createVoiceEvent(
				this.getConference().getConfig().getConferenceKey(),
				speaker.getId(),
				speaker.getId(),"t",ConferenceConstants.RESOURCE_TYPE_AUDIO,streamId,"0","0","3");
		
		this.dispatchStreamControlEvent(speaker, ave,
				ConferenceConstants.FEATURE_AUDIO, ConferenceConstants.EVENT_AUDIO_CONTROL,speaker.getId());
	}
	public void audioControlCommand(IConferenceParticipant speaker, String streamId, String event)
	{
		StreamControlEvent ave = StreamControlEvent.createControlEvent(
				this.getConference().getConfig().getConferenceKey(),
				speaker.getId(),
				speaker.getId(),"t",ConferenceConstants.RESOURCE_TYPE_AUDIO,streamId,"0","0","3",event);
		
		this.dispatchStreamControlEvent(speaker, ave,
				ConferenceConstants.FEATURE_AUDIO, ConferenceConstants.EVENT_AUDIO_CONTROL,speaker.getId());
	}
	
	protected	void	dispatchStreamControlEvent(IConferenceParticipant presenter,
			StreamControlEvent vce, String featureId, String eventId, String userId)
	{
		Event event = new Event(featureId, eventId, new Date(),
				ConferenceConstants.RESPONSE_OK, vce );
		
		this.getClientEventPublisher().dispatchEventToAllClients(event);
		if (featureId.equals(ConferenceConstants.FEATURE_VIDEO))
		{
			if (this.conference.isActivePresenter((Participant)presenter))
			{
				this.setLastControlMessage(event);
			}
			else
			{
				if (vce.getEventType().equals(StreamControlEvent.START))
				{
//					System.out.println("Remembering audio start raised by:"+userId);
					this.activeVideos.put(userId,event);
				}
				else if (vce.getEventType().equals(StreamControlEvent.STOP))
				{
//					System.out.println("Audio broadcasting stopped by:"+userId);
					this.activeVideos.remove(userId);
				}
			}
		}
		else if (featureId.equals(ConferenceConstants.FEATURE_AUDIO))
		{
			if (vce.getEventType().equals(StreamControlEvent.START))
			{
//				System.out.println("Remembering audio start raised by:"+userId);
				this.activeAudios.put(userId,event);
			}
			else if (vce.getEventType().equals(StreamControlEvent.STOP))
			{
//				System.out.println("Audio broadcasting stopped by:"+userId);
				this.activeAudios.remove(userId);
			}
		}
	}
	
	public Event getLastControlEvent()
	{
		return lastControlEvent;
	}
	protected void setLastControlMessage(Event lastControlEvent)
	{
		synchronized (this)
		{
			this.lastControlEvent = lastControlEvent;
		}
	}
	public	Vector	getLastAudioControlEvents()
	{
		Vector	v = new	Vector();
		if (this.activeAudios.size() > 0)
		{
			Iterator iter = this.activeAudios.values().iterator();
			while (iter.hasNext())
			{
				v.addElement(iter.next());
			}
		}
//		if (this.activeVideos.size() > 0)
//		{
//			Iterator iter = this.activeVideos.values().iterator();
//			while (iter.hasNext())
//			{
//				v.addElement(iter.next());
//			}
//		}
//		System.out.println("Current track of audio events: "+v);
		return	v;
	}
	public HashMap getEnabledAudios() {
		return enabledAudios;
	}
	public HashMap getEnabledVideos() {
		return enabledVideos;
	}

}
