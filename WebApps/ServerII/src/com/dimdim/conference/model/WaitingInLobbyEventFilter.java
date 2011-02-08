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

package com.dimdim.conference.model;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.messaging.IEvent;
import com.dimdim.messaging.IEventFilter;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class WaitingInLobbyEventFilter	implements	IEventFilter
{
	public	WaitingInLobbyEventFilter()
	{
		
	}
	public	boolean	receiveEvent(IEvent event)
	{
//		System.out.println("IN EVENT FILTER: event:"+event.toString());
		String featureId = event.getSource();
		String eventId = event.getType();
		if (featureId.equals(ConferenceConstants.FEATURE_CONF) ||
				(featureId.equals(ConferenceConstants.FEATURE_ROSTER) &&
						( eventId.equals(ConferenceConstants.EVENT_PARTICIPANT_ENTRY_DENIED) ||
						  eventId.equals(ConferenceConstants.EVENT_PARTICIPANT_USER_ARRIVED) ||
						  eventId.equals(ConferenceConstants.EVENT_PARTICIPANT_USER_ARRIVED) ) ) )
		{
//			System.out.println("IN EVENT FILTER:Allowing event: "+event.toString());
			return	true;
		}
//		System.out.println("IN EVENT FILTER: Filtering out event:"+event.toString());
		return	false;
	}
}
