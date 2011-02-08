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
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.messaging;

import	java.util.Vector;

import com.dimdim.util.misc.Debug;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * 
 */

public class MessageBox
{
//	private	StringBuffer	buffer;
	private	Vector			events;
	private	int				blockSize;
	private	boolean			dropMessages;
	
	public	MessageBox(int blockSize, boolean dropMessages)
	{
//		this.buffer = null;
		this.events = new Vector();
		this.blockSize = blockSize;
		this.dropMessages = dropMessages;
	}
	public	boolean	hasData()
	{
		return	this.events.size() > 0;
	}
	public	void	bufferEvent(String eventJsonBuffer)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - MessageBox::bufferEvent - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - MessageBox::bufferEvent - "+Thread.currentThread().getId());
				if (this.dropMessages && this.blockSize > 0 && this.events.size() >= this.blockSize*2)
				{
					this.events.removeElementAt(0);
				}
				this.events.addElement(eventJsonBuffer);
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - MessageBox::bufferEvent - "+Thread.currentThread().getId());
		}
//		this.appendToBuffer(eventJsonBuffer);
	}
	public	void	drainBufferedEvents(StringBuffer buf)
	{
		if(Debug.debugEnabled()) Debug.printDebug(">> IN (0) - "+System.currentTimeMillis()+" - MessageBox::drainBufferedEvents - "+Thread.currentThread().getId());
		try
		{
			synchronized (this)
			{
				if(Debug.debugEnabled()) Debug.printDebug(">> IN (1) - "+System.currentTimeMillis()+" - MessageBox::drainBufferedEvents - "+Thread.currentThread().getId());
				this.readAndDeleteBuffer(buf);
			}
		}
		finally
		{
			if(Debug.debugEnabled()) Debug.printDebug(">> OUT (0) - "+System.currentTimeMillis()+" - MessageBox::drainBufferedEvents - "+Thread.currentThread().getId());
		}
	}
//	private	synchronized	void	appendToBuffer(String s)
//	{
//		if (this.buffer == null)
//		{
//			this.buffer = new StringBuffer();
//		}
//		if (this.buffer.length() > 0)
//		{
//			this.buffer.append(",");
//		}
//		this.buffer.append(s);
//	}
	private	void	readAndDeleteBuffer(StringBuffer buf)
	{
		String s = null;
		int i = 0;
		int	max = this.events.size();
		if (this.blockSize > 0 && max > this.blockSize)
		{
			max = this.blockSize;
		}
		for (i=0; i < max; i++)
		{
			s = (String)this.events.elementAt(i);
			if (buf.length() > 0)
			{
				buf.append(",");
			}
			else
			{
				buf.append("[");
			}
			buf.append(s);
		}
		if (max < this.blockSize)
		{
			this.events.clear();
		}
		else
		{
			for (i = max-1; i >= 0; i--)
			{
				this.events.removeElementAt(i);
			}
		}
	}
}
