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

import	java.util.TimerTask;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CommonTimerServiceTask
{
	protected	CommonTimerServiceUser	timerServiceUser;
	protected	CommonTimerService		timerService;
	
	public CommonTimerServiceTask(CommonTimerService timerService,
				CommonTimerServiceUser timerServiceUser)
	{
		this.timerService = timerService;
		this.timerServiceUser = timerServiceUser;
	}
	public CommonTimerServiceUser getTimerServiceUser()
	{
		return timerServiceUser;
	}
	public void setTimerServiceUser(CommonTimerServiceUser timerServiceUser)
	{
		this.timerServiceUser = timerServiceUser;
	}
	public	TimerTask	getTimerTask()
	{
		TimerTask tt = new TimerTask()
		{
			public void run()
			{
				try
				{
					boolean scheduleAgain = timerServiceUser.timerCall();
					if (scheduleAgain)
					{
						CommonTimerServiceTask tst = new CommonTimerServiceTask(timerService,timerServiceUser);
						timerService.getTimer().schedule(tst.getTimerTask(),
								timerServiceUser.getTimerDelay());
					}
				}
				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		};
		return	tt;
	}
}
