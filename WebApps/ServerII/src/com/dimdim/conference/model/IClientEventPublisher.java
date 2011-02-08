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

import com.dimdim.messaging.IEventReceiver;
import com.dimdim.messaging.IEventReceiverFilter;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public interface IClientEventPublisher
{
	public	void	dispatchEventTo(Event event, IConferenceParticipant user);
	
//	public	void	dispatchEventToChild(Event event, IConferenceParticipant user, String childId);
	
	public	void	dispatchEventToAllClients(Event event);
	
//	public	void	dispatchEventToAllClients(Event event, IEventReceiverFilter filter);
	
	public	void	dispatchEventToAllClientsExcept(Event event, IConferenceParticipant exclude);
	
	public	void	addEventReceiver(IEventReceiver receiver);
	
	public	void	removeEventReceiver(IEventReceiver receiver);
	
}
