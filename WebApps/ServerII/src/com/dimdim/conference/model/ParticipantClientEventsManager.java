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

import com.dimdim.messaging.IEvent;
import com.dimdim.messaging.IEventFilter;
import com.dimdim.messaging.IEventReceiver;
import com.dimdim.messaging.IEventsBufferProvider;

//import org.mortbay.util.ajax.Continuation;
//import org.mortbay.util.ajax.ContinuationSupport;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ParticipantClientEventsManager	implements	IEventsProvider, IEventReceiver
{
	public String drainAllAvailableEventsBuffer()
	{
		if (this.iebf != null && this.iebf.hasData())
		{
			return	this.iebf.drainAllAvailableEvents();
		}
		else
		{
			return null;
		}
	}
	public int getIndex()
	{
		return this.participant.getIndex();
	}
	
	private	IEventsBufferProvider	iebf;
	
	public void setEventsBufferProvider(IEventsBufferProvider iebf)
	{
		System.out.println("Setting buffer provider:"+iebf);
		this.iebf = iebf;
	}
	
	protected	Participant	participant;
	protected	String		id;
	protected	IEventFilter	eventFilter;
	
	protected	ParticipantClientEventsManager	childEventsManager;
	
	public	ParticipantClientEventsManager(Participant participant, String id)
	{
		this.participant = participant;
		this.id = id;
	}
	public	int	compareTo(Object o)
	{
		if (o instanceof ParticipantClientEventsManager)
		{
			return	id.compareTo(((ParticipantClientEventsManager)o).getId());
		}
		else
		{
			return	1;
		}
	}
	public	Participant	getParticipant()
	{
		return	this.participant;
	}
	public	synchronized	void	createChildEventsManager()
	{
		this.childEventsManager = new ParticipantClientEventsManager(this.participant,this.id+"-child");
	}
	public	ParticipantClientEventsManager	getChildEventsManager()
	{
		return	this.childEventsManager;
	}
	public	synchronized	void	closeChildEventsManager()
	{
		this.childEventsManager = null;
	}
	public	Vector	drainAllAvailableEvents()
	{
		Vector v = null;
		//	NO LONGER USED
		return	v;
	}
	public	synchronized	Event	drainOneEvent()
	{
		//	NO LONGER USED
		return	null;
	}
	public	String	getId()
	{
		return	id;
	}
	public	void	receiveEvent(IEvent event)
	{
	}
	public synchronized void setEventFilter(IEventFilter eventFilter)
	{
		this.eventFilter = eventFilter;
		if (this.iebf != null)
		{
			this.iebf.addEventFilter(eventFilter);
		}
	}
	public	synchronized	void	removeEventFilter()
	{
		if (this.eventFilter != null)
		{
			if (this.iebf != null)
			{
				this.iebf.removeEventFilter(eventFilter);
			}
			this.eventFilter = null;
		}
	}
	public Object getMutex()
	{
		return	null;
	}
	/**
	 * This is for the host rejoin support. Objective here is to buffer previous events and
	 * hold them for 4 polls.
	 */
	
}
