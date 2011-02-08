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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.messaging;

import com.dimdim.util.misc.Debug;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Instances of this object take the responsibility of reading the message
 * board on behalf of the final event recipient, which is an individual
 * participant.
 * 
 * All the events are sequenced strictly in the order received. Unicast and
 * broadcast messages. This object maintains a byte buffer of the received
 * events and keeps appending received events into the array. When a poll
 * from client browser arrives, if the array is not empty, it is closed,
 * given to the poller and a new array is started. This minimizes the work
 * the poller action has to do.
 * 
 * 
 */

public class MessageBoardReader	implements	IEventsBufferProvider, Comparable
{
	public int compareTo(Object arg0)
	{
		int	i = 1;
		try
		{
			MessageBoardReader a = (MessageBoardReader)arg0;
			return	this.eventReceiver.compareTo(a.eventReceiver);
		}
		catch(Exception e)
		{
			
		}
		return i;
	}
	protected	IEventReceiver	eventReceiver;
	
	protected	IEventFilter	eventFilter1;
	
	protected	MessageBox		priority0Events;
	protected	MessageBox		priority1Events;
	protected	MessageBox		priority2Events;
	
	protected	boolean		hasMoreData;
	
	public MessageBoardReader(IEventReceiver eventReceiver, int blockSize)
	{
		this.eventReceiver = eventReceiver;
		this.priority0Events = new MessageBox(-1,false);
		this.priority1Events = new MessageBox(blockSize,false);
		this.priority2Events = new MessageBox(blockSize,true);
	}
	public	int	getIndex()
	{
		return	eventReceiver.getIndex();
	}
	public	void	addEventFilter(IEventFilter eventFilter)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - MessageBoardReader::addEventFilter - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - MessageBoardReader::addEventFilter - "+Thread.currentThread().getId());
//				if (this.eventFilter1 == null)
//				{
					this.eventFilter1 = eventFilter;
//				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - MessageBoardReader::addEventFilter - "+Thread.currentThread().getId());
		}
	}
	public	void	removeEventFilter(IEventFilter eventFilter)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - MessageBoardReader::removeEventFilter - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - MessageBoardReader::removeEventFilter - "+Thread.currentThread().getId());
//				if (this.eventFilter1 != null && this.eventFilter1.equals(eventFilter))
//				{
					this.eventFilter1 = null;
//				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - MessageBoardReader::removeEventFilter - "+Thread.currentThread().getId());
		}
	}
	/**
	 * The buffer is created from the given event of course. It is provided
	 * so that each reader doesn't have to create it again and the event is
	 * provided so that it can be checked against the available filters.
	 * 
	 * @param event
	 * @param eventBuffer
	 */
	protected	void	receivedBroadcastEvent(IEvent event, int priority, String eventBuffer)
	{
		if (acceptEvent(event))
		{
			this.receiveNewEventBuffer(priority,eventBuffer);
			this.eventReceiver.receiveEvent(event);
		}
	}
	protected	void	receivedPrivateEvent(IEvent event, int priority, String eventBuffer)
	{
		if (acceptEvent(event))
		{
			this.receiveNewEventBuffer(priority,eventBuffer);
			this.eventReceiver.receiveEvent(event);
		}
	}
	private	boolean	acceptEvent(IEvent event)
	{
		boolean	accept = true;
		if (this.eventFilter1 != null)
		{
			accept = this.eventFilter1.receiveEvent(event);
		}
		return	accept;
	}
	private	void	receiveNewEventBuffer(int priority, String eventBuffer)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - MessageBoardReader::receiveNewEventBuffer - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - MessageBoardReader::receiveNewEventBuffer - "+Thread.currentThread().getId());
				if (priority == 0)
				{
					this.hasMoreData = true;
					this.priority0Events.bufferEvent(eventBuffer);
				}
				else if (priority == 1)
				{
					this.hasMoreData = true;
					this.priority1Events.bufferEvent(eventBuffer);
				}
				else if (priority == 2)
				{
					this.hasMoreData = true;
					this.priority2Events.bufferEvent(eventBuffer);
				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - MessageBoardReader::receiveNewEventBuffer - "+Thread.currentThread().getId());
		}
	}
	private	String	readAvailableEvents(int lastIndex)
	{
		StringBuffer buf = new StringBuffer();
		
		this.priority0Events.drainBufferedEvents(buf);
		this.priority1Events.drainBufferedEvents(buf);
		this.priority2Events.drainBufferedEvents(buf);
		
		if (buf.length() > 0)
		{
			buf.append("]");
			return	buf.toString();
		}
		else
		{
			//	This would mean that all the events were rejected by the
			//	filter, such as all chat messages and the user is in lobby.
		}
		return	null;
	}
	public	boolean	hasData()
	{
		return	this.hasMoreData;
	}
	public String drainAllAvailableEvents()
	{
		String	buf = null;
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - MessageBoardReader::drainAllAvailableEvents - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - MessageBoardReader::drainAllAvailableEvents - "+Thread.currentThread().getId());
				if (this.hasData())
				{
					buf = readAvailableEvents(0);
					this.hasMoreData = this.priority0Events.hasData();
					if (!this.hasMoreData)
					{
						this.hasMoreData = this.priority1Events.hasData();
					}
					if (!this.hasMoreData)
					{
						this.hasMoreData = this.priority2Events.hasData();
					}
				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - MessageBoardReader::drainAllAvailableEvents - "+Thread.currentThread().getId());
		}
		return buf;
	}
}
