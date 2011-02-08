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

import java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This brief note describes the conference state machine.
 * 
 * 1.	The state machine refers only to the active conferences.
 * 2.	Conference is always created in STATE_ACTIVE.
 * 3.	ACTIVE - meeting is on and others can join. If the active presenter
 * 		leaves, then the state is changed to CLOSED. However if he breaks off or
 * 		times out, conference will go to PASSIVE for n minute ( default 2 ).
 * 3.	CLOSED - no one new can join, but existing people can chat and conference
 * 		will be removed only when all current participants either leave or
 * 		timeout. When the conference enters CLOSED state, all the current attendees
 * 		will be notified through the popup box as now.
 * 5.	PASSIVE - after the timeout the conference will be placed in CLOSED state.
 * 		At present technically there is no difference between the PASSIVE and
 * 		closed states, except the additional timeout. When the conference enters
 * 		this state, the active presenter attendee panel state will be changed to
 * 		inform the rest of the participants.
 * 6.	REMOVED - all the participants have either left or timed out.
 * 7.	The client console will maintain the same timeout as the server and when
 * 		there is no communication with the server for the time period, it will
 * 		inform the user that the communication with the server has been lost and
 * 		close the console in same manner as now. 
 */

public class ActiveConferenceStateMachine
{
	//	States
	
	//	Starting just called start / create.
	
	public	static	final	int	STATE_STARTING = 0;
	
	//	Create or start complete.
	
	public	static	final	int	STATE_ACTIVE = 1;
	
	//	No active presenter in conference. In this state only the original
	//	organizer will be allowed to rejoin and be made the active presenter.
	
	public	static	final	int	STATE_PASSIVE = 2;
	
	//	Conference stopped. This state exists for a short time between
	//	passive state and close. When the conference remains passive
	//	for certain amount of time, it will be closed. i.e. all the
	//	attendees will be sent closed event, removed from the conference
	//	and the conference itself will be removed from the active
	//	conferences manager table.
	
	public	static	final	int	STATE_CLOSED = 3;
	
	//	Conference removed from system. This is essentily an exit state. It
	//	should never be seen by any other object.
	
	public	static	final	int	STATE_REMOVED = 4;
	
	/**
	 * 	Legal Transitions -> (ided transition)
	 * 
	 * 	STATE_STARTING -> STATE_ACTIVE
	 * 	STATE_ACTIVE -> STATE_PASSIVE, STATE_CLOSED
	 *  STATE_PASSIVE -> STATE_ACTIVE, STATE_CLOSED
	 *  STATE_CLOSED -> STATE_REMOVED
	 */
	
	protected	ActiveConference	activeConference;
	
	private	ActiveConferenceStateMachine(ActiveConference activeConference)
	{
		this.activeConference = activeConference;
	}
	/**
	 * This method first calls the required method(s) on the conference
	 * object and then fires the listeners.
	 */
	public	void	changeState(int newState)//	throws	Exception
	{
		if (this.verifyStateChange(newState))
		{
//			activeConference.setCurrentState(newState);
			Vector v = activeConference.getActiveConferenceStateChangeListeners();
			int num = v.size();
			if (newState == ActiveConferenceStateMachine.STATE_STARTING)
			{
				//	This is a starting state.
			}
			else if (newState == ActiveConferenceStateMachine.STATE_ACTIVE)
			{
				for (int i=0; i<num; i++)
				{
//					((ActiveConferenceStateChangeListener)v.elementAt(i)).
//							conferenceActive(this.activeConference);
				}
			}
			else if (newState == ActiveConferenceStateMachine.STATE_PASSIVE)
			{
				for (int i=0; i<num; i++)
				{
//					((ActiveConferenceStateChangeListener)v.elementAt(i)).
//							conferencePassive(this.activeConference);
				}
			}
			else if (newState == ActiveConferenceStateMachine.STATE_CLOSED)
			{
				for (int i=0; i<num; i++)
				{
//					((ActiveConferenceStateChangeListener)v.elementAt(i)).
//							conferenceClosed(this.activeConference);
				}
			}
			else if (newState == ActiveConferenceStateMachine.STATE_REMOVED)
			{
				for (int i=0; i<num; i++)
				{
//					((ActiveConferenceStateChangeListener)v.elementAt(i)).
//							conferenceRemoved(this.activeConference);
				}
			}
		}
	}
//	public	void	checkRequiredState(int requiredState)	throws	Exception
//	{
//		if (activeConference.getCurrentState() != requiredState)
//		{
//			
//		}
//	}
	protected	boolean	verifyStateChange(int newState)// throws Exception
	{
		boolean b = false;
//		if (activeConference.getCurrentState() == ActiveConferenceStateMachine.STATE_STARTING)
		{
			if (newState == ActiveConferenceStateMachine.STATE_ACTIVE)
			{
				b = true;
			}
		}
//		else if (activeConference.getCurrentState() == ActiveConferenceStateMachine.STATE_ACTIVE)
		{
			if (newState == ActiveConferenceStateMachine.STATE_PASSIVE ||
					newState == ActiveConferenceStateMachine.STATE_CLOSED)
			{
				b = true;
			}
		}
//		else if (activeConference.getCurrentState() == ActiveConferenceStateMachine.STATE_PASSIVE)
		{
			if (newState == ActiveConferenceStateMachine.STATE_ACTIVE ||
					newState == ActiveConferenceStateMachine.STATE_CLOSED)
			{
				b = true;
			}
		}
//		else if (activeConference.getCurrentState() == ActiveConferenceStateMachine.STATE_CLOSED)
		{
			if (newState == ActiveConferenceStateMachine.STATE_REMOVED)
			{
				b = true;
			}
		}
		return	b;
	}
}
