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

import com.dimdim.util.misc.Debug;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This message engine supports only broadcast and unicast messages. Multicast
 * messages are not allowed.
 */

public	class	BoardBasedMessageEngine	implements	IMessageEngine
{
	private	static	final	int		MAX_NUMBER_OF_ROWS	=	500;
	private	static	final	int		MAX_NUMBER_OF_COLS	=	10;
	
	protected	int		eventIndex = 0;
	protected	int		blockSize;
	protected	int			highestIndex;
	protected	int			currentAllocatedLastRow;
	protected	MessageBoardReader[][]	receivers;
	
	public	BoardBasedMessageEngine(int blockSize)
	{
		this.blockSize = blockSize;
		receivers = new MessageBoardReader[MAX_NUMBER_OF_ROWS][];
		this.currentAllocatedLastRow = 0;
		this.receivers[currentAllocatedLastRow] = new MessageBoardReader[MAX_NUMBER_OF_COLS];
		for (int i=1; i<MAX_NUMBER_OF_ROWS; i++)
		{
			this.receivers[i] = null;
		}
	}
	
	/**
	 * This method will add the receiver to receive all events.
	 * @param receiver the event receiver.
	 */
	public	void	addEventReceiver(IEventReceiver receiver)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::addEventReceiver - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::addEventReceiver - "+Thread.currentThread().getId());
				int	receiverIndex = receiver.getIndex();
				MessageBoardReader mbr = new MessageBoardReader(receiver,this.blockSize);
				receiver.setEventsBufferProvider(mbr);
				this.receivers[getRowIndex(receiverIndex)][getColIndexCheckSpace(receiverIndex)] = mbr;
				if (receiverIndex > this.highestIndex)
				{
					//	A redundant safety precaution. Indexes should never come out of order.
					this.highestIndex = receiverIndex;
				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::addEventReceiver - "+Thread.currentThread().getId());
		}
	}
	public	void	removeEventReceiver(IEventReceiver receiver)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::removeEventReceiver - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::removeEventReceiver - "+Thread.currentThread().getId());
				int	receiverIndex = receiver.getIndex();
				this.receivers[getRowIndex(receiverIndex)][getColIndex(receiverIndex)] = null;
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::removeEventReceiver - "+Thread.currentThread().getId());
		}
	}
	private	int	getNextEventIndex()
	{
		int i = eventIndex++;
		return	i;
	}
	public	void	dispatchEvent(IEvent event)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEvent - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEvent - "+Thread.currentThread().getId());
				event.setIndex(this.getNextEventIndex());
				int	size = this.highestIndex+1;
				int priority = event.getPriority();
				String eventStringBuffer = event.toJson();
				for (int i=0; i<size; i++)
				{
					MessageBoardReader mbr = this.receivers[getRowIndex(i)][getColIndex(i)];
					if (mbr != null)
					{
						mbr.receivedBroadcastEvent(event,priority,eventStringBuffer);
					}
					else
					{
		//				System.out.println("User has left the meeting");
					}
				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEvent - "+Thread.currentThread().getId());
		}
	}
	public	synchronized	void	dispatchEvent(IEvent event, IEventReceiverFilter filter)
	{
		System.out.println("********************************* OBSOLETE METHOD *********** ");
	}
	public	void	dispatchEventToAllExcept(IEvent event, String excludeReceiverWithId)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEventToAllExcept - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEventToAllExcept - "+Thread.currentThread().getId());
				int	excludeId = -1;
				try
				{
					excludeId = (new Integer(excludeReceiverWithId)).intValue();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				event.setIndex(this.getNextEventIndex());
				int	size = this.highestIndex+1;
				int priority = event.getPriority();
				String eventStringBuffer = event.toJson();
				for (int i=0; i<size; i++)
				{
					if (i == excludeId)
					{
						continue;
					}
					MessageBoardReader mbr = this.receivers[getRowIndex(i)][getColIndex(i)];
					if (mbr != null)
					{
						mbr.receivedBroadcastEvent(event,priority,eventStringBuffer);
					}
					else
					{
					}
				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEventToAllExcept - "+Thread.currentThread().getId());
		}
	}
	public	void	dispatchEventToReceiver(IEvent event, String onlyToReceiverWithId)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEventToReceiver - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEventToReceiver - "+Thread.currentThread().getId());
				try
				{
					int	receiverIndex = Integer.parseInt(onlyToReceiverWithId);
					MessageBoardReader mbr = this.receivers[getRowIndex(receiverIndex)][getColIndex(receiverIndex)];
					if (mbr != null)
					{
						int priority = event.getPriority();
						mbr.receivedPrivateEvent(event,priority,event.toJson());
					}
					else
					{
		//				System.out.println("Receiver has left the meeting");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - BoardBasedMessageEngine::dispatchEventToReceiver - "+Thread.currentThread().getId());
		}
	}
	public	void	shutdown()
	{
	}
	private	int	getRowIndex(int i)
	{
		int rowIndex = i/MAX_NUMBER_OF_COLS;
		return	rowIndex;
	}
	private	int	getColIndex(int i)
	{
		int	colIndex = i%MAX_NUMBER_OF_COLS;
		return	colIndex;
	}
	private	int	getColIndexCheckSpace(int i)
	{
		int	colIndex = i%MAX_NUMBER_OF_COLS;
		if (colIndex >= MAX_NUMBER_OF_COLS/2)
		{
			int r = getRowIndex(i);
			if (r == this.currentAllocatedLastRow &&
						this.receivers[this.currentAllocatedLastRow+1] == null)
			{
				this.allocateNewRow(i);
			}
		}
		return	colIndex;
	}
	private	void	allocateNewRow(int i)
	{
		int	colIndex = i%MAX_NUMBER_OF_COLS;
		if (colIndex >= MAX_NUMBER_OF_COLS/2)
		{
			int r = getRowIndex(i);
			if (r == this.currentAllocatedLastRow &&
					this.receivers[this.currentAllocatedLastRow+1] == null)
			{
				this.currentAllocatedLastRow++;
				this.receivers[this.currentAllocatedLastRow] = new MessageBoardReader[MAX_NUMBER_OF_COLS];
			}
		}
	}
}
