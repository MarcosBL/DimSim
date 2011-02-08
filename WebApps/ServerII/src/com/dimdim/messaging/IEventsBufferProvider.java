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

import	java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This interface is intended to act as a broker between all the event
 * publishers in the system and the web client servlets or webwork actions
 * which read them periodically. The implementation is expected to buffer
 * the events published and hold them till the clients ask for them.
 */

public interface IEventsBufferProvider
{
	/**
	 * This method returns the buffer of all available events from all sources.
	 * Returns null if no new events are available.
	 * 
	 * @return
	 */
	public	String	drainAllAvailableEvents();
	
	public	void	addEventFilter(IEventFilter eventFilter);
	
	public	void	removeEventFilter(IEventFilter eventFilter);
	
	public	boolean	hasData();
}
