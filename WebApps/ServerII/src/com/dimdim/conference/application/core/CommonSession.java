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

package com.dimdim.conference.application.core;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.dimdim.util.timer.TimerService;
import com.dimdim.util.timer.TimerServiceTaskId;
import com.dimdim.util.timer.TimerServiceUser;
import com.dimdim.locale.LocaleManager;
import com.dimdim.conference.ConferenceConsoleConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CommonSession	implements	Comparable, TimerServiceUser
{
	protected	static	final	int	SESSION_ACTIVE  =	0;
	protected	static	final	int	SESSION_CLOSED	=	1;
	
	protected	String		sessionKey;
	protected	String		streamingSessionKey;
	protected	String		dataCacheId;
	
	protected	Date		creationDate = new Date();
	protected	Locale		sessionLocale = LocaleManager.getManager().getDefaultLocale();
	protected	long		lastAccessTime = System.currentTimeMillis();
	protected	long		timeout = ConferenceConsoleConstants.getAttendeeSessionTimeout()*1000;
	protected	long		basicTimeout;
	protected	int			currentState = CommonSession.SESSION_ACTIVE;
	
	protected	String		os;
	protected	String		browserType;
	protected	String		browserVersion;
	
	protected	String		uri;
	
	protected	transient	TimerServiceTaskId	taskId;
	
	public	CommonSession()
	{
		this(null);
	}
	public	CommonSession(CommonSession parent)
	{
		sessionKey = UUID.randomUUID().toString();
		streamingSessionKey = UUID.randomUUID().toString();
		dataCacheId = UUID.randomUUID().toString();
		this.basicTimeout = timeout;
	}
	public	void	extendSessionTimeout()
	{
//		this.timeout = this.basicTimeout * 4;
	}
	public	void	resetSessionTimeout()
	{
//		this.timeout = this.basicTimeout;
	}
	public	int	compareTo(Object obj)
	{
		if (obj instanceof com.dimdim.conference.application.core.CommonSession)
		{
			return this.sessionKey.compareTo(((CommonSession)obj).getSessionKey());
		}
		else
		{
			return	0;
		}
	}
	public	void	addToTimerService()
	{
		TimerService.getService().addUser(this);
	}
	public String getSessionKey()
	{
		return sessionKey;
	}
	public	String	getDataCacheId()
	{
		return	this.dataCacheId;
	}
	public String getStreamingSessionKey()
	{
		return streamingSessionKey;
	}
	public long getTimerDelay()
	{
		return  60000;
	}
	public boolean timerCall()
	{
		boolean	sessionActive = isActive();
		if (sessionActive)
		{
			if (isAbandoned())
			{
				sessionActive = false;
				close();
			}
		}
		return sessionActive;
	}
	public void setTimerServiceTaskId(TimerServiceTaskId taskId)
	{
		this.taskId = taskId;
	}
	public Date getCreationDate()
	{
		return creationDate;
	}
	public	boolean	isPresenter()
	{
		return	false;
	}
	public	boolean	isAbandoned()
	{
		long lat = this.lastAccessTime;
		long currentTime = System.currentTimeMillis();
		boolean abandoned = (currentTime - lat) > timeout;
		if (abandoned)
		{
			System.out.println("Abandoned session last access time was: "+lat);
			System.out.println("Current time is                       : "+currentTime);
		}
		return abandoned;
	}
	public int getCurrentState()
	{
		return this.currentState;
	}
	public	boolean	isActive()
	{
		return	this.currentState == CommonSession.SESSION_ACTIVE;
	}
	public	synchronized	void	close()
	{
		if (this.currentState == CommonSession.SESSION_CLOSED)
		{
			return;
		}
		this.currentState  = CommonSession.SESSION_CLOSED;
	}
	public long getLastAccessTime()
	{
		return lastAccessTime;
	}
	public	void	setLastAccessTime()
	{
		this.lastAccessTime = System.currentTimeMillis();
	}
	public	String	toString()
	{
		return	this.sessionKey+":"+this.creationDate+":"+this.lastAccessTime;
	}
	public Locale getSessionLocale()
	{
		return sessionLocale;
	}
	public void setSessionLocale(Locale sessionLocale)
	{
		this.sessionLocale = sessionLocale;
	}
	public String getBrowserType()
	{
		return browserType;
	}
	public void setBrowserType(String browserType)
	{
		this.browserType = browserType;
	}
	public String getBrowserVersion()
	{
		return browserVersion;
	}
	public void setBrowserVersion(String browserVersion)
	{
		this.browserVersion = browserVersion;
	}
	public String getOs()
	{
		return os;
	}
	public void setOs(String os)
	{
		this.os = os;
	}
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
		this.uri = uri;
	}
}
