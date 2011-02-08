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

package com.dimdim.util.session;

import	java.util.HashMap;
import com.dimdim.util.timer.TimerServiceTaskId;
import com.dimdim.util.timer.TimerServiceUser;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserSessionData	implements	TimerServiceUser
{
	public	static	final	String	ACTIVE_USER_SESSION	=	"ACTIVE_USER_SESSION";
	
	protected	String		dataId;
	protected	HashMap		sessionData;
	protected	long		creationTime = System.currentTimeMillis();
	protected	long		accessTime = System.currentTimeMillis();
	
	public	UserSessionData(String dataId)
	{
		this.dataId = dataId;
		this.sessionData = new HashMap();
	}
	public long getCreationTime()
	{
		return creationTime;
	}
	public HashMap getSessionData()
	{
		return sessionData;
	}
	public	void	markAccessTime()
	{
		this.accessTime = System.currentTimeMillis();
	}
	public	void	setCleared()
	{
		this.accessTime = 0;
	}
	public	boolean	isIdleTooLong()
	{
		if ((System.currentTimeMillis() - this.accessTime) > 20 * 60 * 1000)
		{
			//	20 minutes idle.
			return	true;
		}
		return	false;
	}
	public long getTimerDelay()
	{
		return 120000;
	}
	public void setTimerServiceTaskId(TimerServiceTaskId taskId)
	{
	}
	public boolean timerCall()
	{
		boolean	scheduleAgain = !this.isIdleTooLong();
		if (!scheduleAgain)
		{
			UserSessionDataManager.getDataManager().clearUserSessionData(this.dataId);
		}
		return scheduleAgain;
	}
}
