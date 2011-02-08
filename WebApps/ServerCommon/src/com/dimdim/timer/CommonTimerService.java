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

package com.dimdim.timer;

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

public class CommonTimerService
{
	private	static	CommonTimerService	timerService	= null;
	private	static	HashMap			namedTimers	=	new HashMap();
	
	private	static	synchronized	void	createService()
	{
		if (CommonTimerService.timerService == null)
		{
			CommonTimerService.timerService = new CommonTimerService();
		}
	}
	public	static	CommonTimerService	getService()
	{
		if (CommonTimerService.timerService == null)
		{
			CommonTimerService.createService();
		}
		return	CommonTimerService.timerService;
	}
	public	static	void	stopService()
	{
		if (CommonTimerService.timerService != null)
		{
			CommonTimerService.timerService.stopServiceTimer();
		}
		Iterator keys = CommonTimerService.namedTimers.keySet().iterator();
		while (keys.hasNext())
		{
			String key = (String)keys.next();
			CommonTimerService ts = (CommonTimerService)CommonTimerService.namedTimers.get(key);
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
	public	static	CommonTimerService		getService(String name)
	{
		CommonTimerService service = (CommonTimerService)CommonTimerService.namedTimers.get(name);
		if (service == null)
		{
			service = CommonTimerService.createService(name);
		}
		return	service;
	}
	private	static	synchronized	CommonTimerService	createService(String name)
	{
		CommonTimerService service = (CommonTimerService)CommonTimerService.namedTimers.get(name);
		if (service == null)
		{
			service = new CommonTimerService();
			CommonTimerService.namedTimers.put(name, service);
		}
		return	service;
	}
	
	
	private	Timer	timer;
	private	long	timingInterval = 60000;	//	Default 1 minute;
	private	String	serviceId = UUID.randomUUID().toString();
	
	private	CommonTimerService()
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
	public	synchronized	void	addUser(final CommonTimerServiceUser user)
	{
		CommonTimerServiceTask tst = new CommonTimerServiceTask(this,user);
		timer.schedule(tst.getTimerTask(), user.getTimerDelay());
		user.setTimerServiceTaskId(new CommonTimerServiceTaskId());
	}
	public	synchronized	void	removeUser(CommonTimerServiceUser user)
	{
	}
}
