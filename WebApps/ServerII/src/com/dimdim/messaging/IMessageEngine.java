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

package com.dimdim.messaging;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object represents a single message engine. Even though its not a
 * technical restriction, each conference is expected to use one single
 * message engine to dispatch events to all its participants.
 */
public	interface	IMessageEngine
{
	
	public	void	dispatchEvent(IEvent event);
	
	public	void	dispatchEventToReceiver(IEvent event, String onlyToReceiverWithId);
	
	public	void	dispatchEventToAllExcept(IEvent event, String excludeReceiverWithId);
	
	/**
	 * This method is a generalized method that can include or exclude a set
	 * of participants from receiving the event. Since this could get expensive
	 * caller needs to be careful about the use of filter.
	 */
	
	public	void	dispatchEvent(IEvent event, IEventReceiverFilter filter);
	
	/**
	 * This method will add the receiver to receive all events.
	 * @param receiver the event receiver.
	 */
	public	void	addEventReceiver(IEventReceiver receiver);
	
	public	void	removeEventReceiver(IEventReceiver receiver);
	
	/**
	 * This call reports the machine no longer in use. Essentially the
	 * meeting is closed.
	 */
	public	void	shutdown();
}
