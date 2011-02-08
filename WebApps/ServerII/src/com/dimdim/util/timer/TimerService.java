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

package com.dimdim.util.timer;

import java.util.UUID;
import java.util.Timer;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This service provides a alarm method for each of the timer service users.
 * At present only 1 minute interval is supported and is fixed.
 */

public class TimerService //extends Thread
{
	private	static	TimerService	timerService	= null;
	private	static	HashMap			namedTimers	=	new HashMap();
	
	private	static	synchronized	void	createService()
	{
		if (TimerService.timerService == null)
		{
			TimerService.timerService = new TimerService();
		}
	}
	public	static	TimerService	getService()
	{
		if (TimerService.timerService == null)
		{
			TimerService.createService();
		}
		return	TimerService.timerService;
	}
	public	static	void	stopService()
	{
		if (TimerService.timerService != null)
		{
			TimerService.timerService.stopServiceTimer();
		}
		Iterator keys = TimerService.namedTimers.keySet().iterator();
		while (keys.hasNext())
		{
			String key = (String)keys.next();
			TimerService ts = (TimerService)TimerService.namedTimers.get(key);
			if (ts != null)
			{
				ts.stopServiceTimer();
			}
		}
	}
	/**
	 * Named timers should be used by components which are needed to be
	 * independent of others and when the tasks may take some time to execute.
	 * Seperate timers will prevent delays on other tasks in such cases. 
	 */
	public	static	TimerService		getService(String name)
	{
		TimerService service = (TimerService)TimerService.namedTimers.get(name);
		if (service == null)
		{
			service = TimerService.createService(name);
		}
		return	service;
	}
	private	static	synchronized	TimerService	createService(String name)
	{
		TimerService service = (TimerService)TimerService.namedTimers.get(name);
		if (service == null)
		{
			service = new TimerService();
			TimerService.namedTimers.put(name, service);
		}
		return	service;
	}
	
	
	private	Timer	timer;
	private	long	timingInterval = 60000;	//	Default 1 minute;
	private	String	serviceId = UUID.randomUUID().toString();
	
	private	TimerService()
	{
		timer = new Timer();
	}
	public	String	getServiceId()
	{
		return	this.serviceId;
	}
	public Timer	getTimer()
	{
		return	timer;
	}
	public long getTimingInterval()
	{
		return timingInterval;
	}
	public void setTimingInterval(long timingInterval)
	{
		this.timingInterval = timingInterval;
	}
	public	void	stopServiceTimer()
	{
		timer.cancel();
	}
	public	synchronized	void	addUser(final TimerServiceUser user)
	{
		TimerServiceTask tst = new TimerServiceTask(this,user);
		timer.schedule(tst.getTimerTask(), user.getTimerDelay());
		user.setTimerServiceTaskId(new TimerServiceTaskId());
	}
	public	synchronized	void	removeUser(TimerServiceUser user)
	{
	}
}
