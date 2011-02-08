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

import	java.util.Vector;

//import org.mortbay.util.ajax.Continuation;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This interface is intended to act as a broker between all the event
 * publishers in the system and the web client servlets or webwork actions
 * which read them periodically. The implementation is expected to buffer
 * the events published and hold them till the clients ask for them.
 */

public interface IEventsProvider
{
	/**
	 * This method all the available events for the specified category
	 * and clears them out of the cache. This is provided so that different
	 * 
	 * @return
	 */
//	public	SortedSet	drainAvailableEvents(String featureId);
	
	/**
	 * This method returns all available events from all sources.
	 * 
	 * @return
	 */
	public	Vector	drainAllAvailableEvents();
	
	public	String	drainAllAvailableEventsBuffer();
	
//	public	Event	drainOneEvent(String type);
	
	public	Event	drainOneEvent();
	
	/**
	 * Methods required for jetty continuation based efficient event polling.
	 * 
	 * @return
	 */
	public	Object	getMutex();
	
//	public void setContinuation(Continuation continuation);
}
