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

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IClientEventPublisher;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.util.timer.TimerService;
import com.dimdim.util.timer.TimerServiceTaskId;
import com.dimdim.util.timer.TimerServiceUser;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object does the work of watching for the conference running time.
 * It will send out three warnings before closing the conference on expiry.
 */

public class ActiveConferenceClock implements TimerServiceUser
{
	protected	ActiveConference	conference;
	protected	IClientEventPublisher	clientEventsPublisher;
	protected	boolean		conferenceClosed = false;
	
	protected	long	startTime;
	protected	int		meetingTimeMinutes = 0;
	protected	long	expiryTime;
	
	protected	long	warning1Time;
	protected	boolean	warning1Sent = false;
	
	protected	long	warning2Time;
	protected	boolean	warning2Sent = false;
	
	protected	long	warning3Time;
	protected	boolean	warning3Sent = false;
	
	protected	transient	TimerServiceTaskId	taskId;
	
	public	ActiveConferenceClock(ActiveConference conference, int meetingTimeMinutes)
	{
		this.conference = conference;
		this.startTime = System.currentTimeMillis();
		this.clientEventsPublisher = conference.getClientEventPublisher();
		this.setMeetingTimeMinutes(meetingTimeMinutes);
		this.startClock();
	}
	public	void	setConferenceClosed()
	{
		this.conferenceClosed = true;
		TimerService.getService().removeUser(this);
	}
	public	void	startClock()
	{
		if (this.taskId == null)
		{
			TimerService.getService().addUser(this);
		}
	}
	/**
	 * The closing warnings are sent at 10 minutes, 3 minutes and 1 minute before
	 * closing time. If the meeting time is changed within any of these time slots
	 * there wont be enough time to send warnings. The warning flags are set
	 * accordingly. The timer call method will check the current time against the
	 * expiry time and warning times and send warning event to the organizer and
	 * at right time, force the meeting to close.
	 * 
	 * @param mtm
	 */
	public	synchronized	void	setMeetingTimeMinutes(int mtm)
	{
		System.out.println("Setting the conference time limits. Minutes:"+mtm);
		/*if (this.meetingTimeMinutes < mtm)
		{
			System.out.println("Setting the conference time limits.");
		}
		else
		{
			System.out.println("No change call to settings.");
			return;
		}*/
		
		long currentTime = System.currentTimeMillis();
		
		this.meetingTimeMinutes = mtm;
		if (this.meetingTimeMinutes < 400)
		{
			this.expiryTime = this.startTime + (this.meetingTimeMinutes*60*1000);
		}
		else
		{
			int tm1 = this.meetingTimeMinutes;
			int tm2 = tm1 / 400;
//			System.out.println("mtm/400:"+tm2);
			int	tm3 = tm1 % 400;
//			System.out.println("mtm%400:"+tm3);
			this.expiryTime = this.startTime;
			for (int i=0; i<tm2; i++)
			{
				this.expiryTime += 400*60*1000;
//				System.out.println(i+": expiry time:"+this.expiryTime);
			}
			this.expiryTime += tm3*60*1000;
//			System.out.println(tm3+": expiry time:"+this.expiryTime);
		}
		System.out.println("Expiry time: "+this.expiryTime);
		this.warning1Time = this.expiryTime - (10*60*1000);
		if (this.warning1Time > currentTime)
		{
			this.warning1Sent = false;
		}
		else
		{
			//	Warning 1 time has already passed.
			this.warning1Sent = true;
		}
		this.warning2Time = this.expiryTime - (3*60*1000);
		if (this.warning2Time > currentTime)
		{
			this.warning2Sent = false;
		}
		else
		{
			//	Warning 2 time has already passed.
			this.warning2Sent = true;
		}
		this.warning3Time = this.expiryTime - (1*60*1000);
		if (this.warning3Time > currentTime)
		{
			this.warning3Sent = false;
		}
		else
		{
			//	Warning 3 time has already passed.
			this.warning3Sent = true;
		}
	}
	public synchronized	boolean timerCall()
	{
		if (this.conferenceClosed)
		{
			return false;
		}
		long  currentTime = System.currentTimeMillis();
		if (currentTime >= this.expiryTime)
		{
			//	Close the conference.
			this.notifyOrganizer(ConferenceConstants.EVENT_CONFERENCE_TIME_EXPIRED);
			this.notifyAllAttendeesConferenceClosed();
			System.out.println("********* Conference time limit has passed. Closing the conference.");
			this.conference.endConference();
//			this.setConferenceClosed();
		}
		else if (currentTime >= this.warning3Time)
		{
			if (!this.warning3Sent)
			{
				this.warning3Sent = true;
				//	Send out the third warning.
				this.notifyOrganizer(ConferenceConstants.EVENT_CONFERENCE_TIME_WARNING_3);
				System.out.println("********* Sending out third (1 minute) warning.");
			}
		}
		else if (currentTime >= this.warning2Time)
		{
			if (!this.warning2Sent)
			{
				this.warning2Sent = true;
				//	Send out the second warning.
				this.notifyOrganizer(ConferenceConstants.EVENT_CONFERENCE_TIME_WARNING_2);
				System.out.println("********* Sending out second (3 minute) warning.");
			}
		}
		else if (currentTime >= this.warning1Time)
		{
			if (!this.warning1Sent)
			{
				this.warning1Sent = true;
				//	Send out the first warning.
				this.notifyOrganizer(ConferenceConstants.EVENT_CONFERENCE_TIME_WARNING_1);
				System.out.println("********* Sending out first (10 minute) warning.");
			}
		}
		return	!this.conferenceClosed;
	}
	public	long	getTimerDelay()
	{
		return	60000;
	}
	public TimerServiceTaskId	getTimerServiceTaskId()
	{
		return	taskId;
	}
	public void setTimerServiceTaskId(TimerServiceTaskId taskId)
	{
		this.taskId = taskId;
	}
	private	void	notifyOrganizer(String timerEvent)
	{
		IConferenceParticipant organizer = conference.getOrganizer();
		Event event = new Event(ConferenceConstants.FEATURE_CONF, timerEvent,
				new Date(), ConferenceConstants.RESPONSE_OK,
				conference.getConferenceKey());
		
		this.clientEventsPublisher.dispatchEventTo(event,organizer);
	}
	private	void	notifyAllAttendeesConferenceClosed()
	{
		IConferenceParticipant organizer = conference.getOrganizer();
		Event event = new Event(ConferenceConstants.FEATURE_CONF,
				ConferenceConstants.EVENT_CONFERENCE_CLOSED,
				new Date(), ConferenceConstants.RESPONSE_OK,
				conference.getConferenceKey() );
		this.clientEventsPublisher.dispatchEventToAllClientsExcept(event, organizer);
	}
}
