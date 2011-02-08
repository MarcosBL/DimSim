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

package com.dimdim.util.misc;

import	java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * This queue provides non blocking puts and blocking or nonblocking gets.
 * The gets essentially are on a specifiable timeout.
 */

public class SimpleQueue
{
	protected	Vector	queue;
	
	public	SimpleQueue()
	{
		this.queue = new Vector();
	}
	
	public	void	put(Object obj)
	{
		synchronized	(this.queue)
		{
			this.queue.add(obj);
			this.queue.notify();
		}
	}
	
	public	Object	get()
	{
		return	this.get(0);
	}
	public	Object	get(long timeoutMillisecs)
	{
//		long startTime = System.currentTimeMillis();
		Object nextObject = null;
		if (this.queue.size() > 0)
		{
			synchronized (this.queue)
			{
				nextObject = this.queue.remove(0);
			}
		}
		else
		{
			try
			{
				synchronized (this.queue)
				{
					if (timeoutMillisecs > 0)
					{
						this.queue.wait(timeoutMillisecs);
						if (this.queue.size() > 0)
						{
							nextObject = this.queue.remove(0);
						}
					}
					else
					{
						this.queue.wait();
						if (this.queue.size() > 0)
						{
							nextObject = this.queue.remove(0);
						}
					}
				}
			}
			catch(Exception e)
			{
				//	Timeout was interrupted. This could have been a new
				//	object being available or a problem.
				e.printStackTrace();
			}
		}
		return	nextObject;
	}
}
