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

import	com.dimdim.messaging.IEventReceiver;
import	com.dimdim.messaging.IEvent;
import	com.dimdim.messaging.IEventReceiverFilter;

import	com.dimdim.conference.model.ParticipantClientEventsManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PresentersOnlyMessageFilter	implements	IEventReceiverFilter
{
	public	boolean	sendEventTo(IEventReceiver receiver, IEvent event)
	{
		if (receiver instanceof ParticipantClientEventsManager)
		{
			if (((ParticipantClientEventsManager)receiver).getParticipant().isPresenter())
			{
				return	true;
			}
		}
		return	false;
	}
}
