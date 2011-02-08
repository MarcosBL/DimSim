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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The timer service always schedules the user for only one time. Each timer
 * call must return true if the timer is to be scheduled again. If the call
 * returns false the timer is not rescheduled. Also the service user could
 * change the timer delay on each call. The Timer reads the delay time each
 * time the timer task is scheduled.
 */

public interface TimerServiceUser
{
	/**
	 * This method returns the time in milliseconds after which the timer task
	 * is to be executed.
	 * 
	 * @return
	 */
	
	public	long	getTimerDelay();
	
	/**
	 * This method must return true if the timer is to scheduled again and
	 * false otherwise.
	 * 
	 * @return
	 */
	
	public	boolean	timerCall();
	
	/**
	 * This sets up the task id which actually is just a flag to help the
	 * clustering policies and objects movement between cluster members.
	 */
	
	public	void	setTimerServiceTaskId(TimerServiceTaskId taskId);
	
}
