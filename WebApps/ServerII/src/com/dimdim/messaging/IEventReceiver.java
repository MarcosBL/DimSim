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
public	interface	IEventReceiver	extends	Comparable
{
	public	int		getIndex();
	
	public	String	getId();
	
	public	void	receiveEvent(IEvent event);
	
	public	void	setEventFilter(IEventFilter eventFilter);
	
	public	void	removeEventFilter();
	
	public	void	setEventsBufferProvider(IEventsBufferProvider iebf);
}
