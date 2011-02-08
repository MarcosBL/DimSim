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

package com.dimdim.conference.action;

import	com.dimdim.conference.model.IConference;
import	com.dimdim.conference.model.IConferenceParticipant;
import	com.dimdim.conference.model.Event;
import	com.dimdim.conference.ConferenceConstants;
import	com.dimdim.conference.application.UserSession;
import	com.dimdim.conference.application.UserSessionManager;
import	com.dimdim.conference.application.core.ActiveConference;
import	com.dimdim.conference.application.core.ConferenceManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * A conference action always works within the context of a single conference
 * Most actions required by conference participants will fall in this category.
 * All other actions will extend this one.
 */
public class ConferenceAction	extends	CommonDimDimAction
{
	protected	String		sessionKey;
	protected	String		confKey;
//	protected	String		featureId;
//	protected	String		eventId;
	
	protected	UserSession		userSession;
//	protected	String			actionResult;
	protected	String			actionReplyText;
	
	protected	Event			resultEvent;
	
	public	ConferenceAction()
	{
	}
	public	String	execute()	throws	Exception
	{
//		System.out.println("Session Key:"+sessionKey);
//		userSession = (UserSession)session.
//			get(ConferenceConsoleConstants.ACTIVE_USER_SESSION);
		String eventId = ConferenceConstants.EVENT_CONFERENCE_NO_KEY;
		String eventMessage = ConferenceConstants.EVENT_CONFERENCE_NO_KEY;
		if (sessionKey != null)
		{
			userSession = null;//(UserSession)session.get(sessionKey);
			if (userSession != null)
			{
				//	Execute action code, which is done later.
			}
			else if (this.confKey != null)
			{
				try
				{
					ActiveConference conf = (ActiveConference)
						ConferenceManager.getManager().getConference(confKey);
					if (conf != null)
					{
						userSession = conf.getParticipantSession(sessionKey);
						if (userSession == null)
						{
							eventId = ConferenceConstants.EVENT_CONFERENCE_ID;
							eventMessage = conf.getConferenceId();
						}
						else
						{
//							session.put(sessionKey, userSession);
						}
					}
					else
					{
						eventId = ConferenceConstants.EVENT_CONFERENCE_NOT_ACTIVE;
						eventMessage = ConferenceConstants.EVENT_CONFERENCE_NOT_ACTIVE;
						System.out.println("No conference by key:"+confKey);
					}
				}
				catch(Exception e)
				{
					
				}
			}
			else
			{
				System.out.println(" ************** INVALID CALL FROM CONSOLE ************** missing conf key ");
				eventId = ConferenceConstants.EVENT_CONFERENCE_NO_KEY;
				eventMessage = ConferenceConstants.EVENT_CONFERENCE_NO_KEY;
				UserSessionManager usm = UserSessionManager.getManager();
				userSession = (UserSession)usm.getSession(sessionKey);
			}
			if (userSession == null)
			{
				//	This means that the confKey was provided but still the meeting
				//	was not found. The conference is closed. Most probably the presenter
				//	has existed or timed out. Allow for 5 more polls so that the
				//	attendee could receive the conference ended event.
				Integer count = (Integer)session.get("CONF_LOST");
				if (count == null)
				{
					count = new Integer(0);
				}
				else
				{
					count = new Integer(count.intValue()+1);
				}
				if (count.intValue() < 5)
				{
					UserSessionManager usm = UserSessionManager.getManager();
					userSession = (UserSession)usm.getSession(sessionKey);
					session.put("CONF_LOST", count);
				}
			}
		}
		else
		{
			System.out.println(" ************** INVALID CALL FROM CONSOLE ************** missing session key ");
		}
//		if (userSession == null)
//		{
//			//	unauthenticated or timed session or erroneous use.
//			System.out.println("####### user session is null");
//			return	attemptSessionRecovery();
//		}
//		else if (userSession.getConference() == null || userSession.getUser() == null)
//		{
//			//	unauthenticated or timed session or erroneous use.
//			System.out.println("####### user session's client is null");
//			return	attemptSessionRecovery();
//		}
		if (userSession == null)
		{
			this.setupLoginResponse(eventId,eventMessage);
			return	SUCCESS;
		}
		else
		{
			return	doActionWork();
		}
	}
	protected	String	doActionWork()	throws	Exception
	{
		synchronized (this)
		{
			userSession.setLastAccessTime();
		}
		String ret = doWork();
		if (this.resultEvent == null)
		{
			if (ret.equals(SUCCESS))
			{
				this.setupSuccessResponse();
			}
			else
			{
				this.setupErrorResponse(ret,ret);
				ret = SUCCESS;
			}
		}
		return	ret;
	}
//	protected	String	attemptSessionRecovery()	throws	Exception
//	{
//		UserSessionManager usm = UserSessionManager.getManager();
//		userSession = (UserSession)usm.getSession(sessionKey);
//		if (userSession != null)
//		{
//			System.out.println("Http Session Changed **** Either a browser error or cluster node change.");
//			session.put(sessionKey,userSession);
//			
//			if (userSession.getTimerServiceTaskId() == null)
//			{
//				System.out.println("Cluster node change.");
//				userSession.addToTimerService();
//			}
//			
//			((ActiveConference)userSession.getConference()).startClock();
//			
//			return	doActionWork();
//		}
//		else
//		{
//			this.setupLoginResponse();
//			return	LOGIN;
//		}
//	}
	protected	void	setupLoginResponse(String eventId, String message)
	{
		this.setupErrorResponse(eventId,message);
	}
	protected	void	setupSuccessResponse()
	{
		this.resultEvent = new Event(Event.EventType_success,ConferenceConstants.FEATURE_CONF,
				ConferenceConstants.EVENT_CONFERENCE_COMMAND_SUCCESS,
				ConferenceConstants.EVENT_CONFERENCE_COMMAND_SUCCESS);
	}
	protected	void	setupErrorResponse(String eventId, String message)
	{
		this.resultEvent = new Event(Event.EventType_error,ConferenceConstants.FEATURE_CONF,eventId,message);
	}
	protected	String	doWork()	throws	Exception
	{
		return	SUCCESS;
	}
	protected	IConference	getConference()
	{
		return	this.userSession.getConference();
	}
	protected	IConferenceParticipant	getUser()
	{
		return	this.userSession.getUser();
	}
	public String getSessionKey()
	{
		return sessionKey;
	}
	public void setSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	public String getActionReplyText()
	{
		return actionReplyText;
	}
	public void setActionReplyText(String actionReplyText)
	{
		this.actionReplyText = actionReplyText;
	}
	public Event getResultEvent()
	{
		return this.resultEvent;
	}
	public void setResultEvent(Event resultEvent)
	{
		this.resultEvent = resultEvent;
	}
//	public String getEventId()
//	{
//		return eventId;
//	}
//	public void setEventId(String eventId)
//	{
//		this.eventId = eventId;
//	}
//	public String getFeatureId()
//	{
//		return featureId;
//	}
//	public void setFeatureId(String featureId)
//	{
//		this.featureId = featureId;
//	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
}
