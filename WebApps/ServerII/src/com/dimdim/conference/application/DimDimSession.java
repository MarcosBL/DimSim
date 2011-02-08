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

package com.dimdim.conference.application;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.dimdim.util.misc.IDGenerator;
import com.dimdim.util.misc.StringGenerator;
import com.dimdim.util.session.UserSessionDataManager;
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

public class DimDimSession	implements	Comparable
{
	protected	static	final	int	SESSION_ACTIVE  =	0;
	protected	static	final	int	SESSION_CLOSED	=	1;
	
//	private	static	IDGenerator idGen = new IDGenerator("S");
//	private	static	StringGenerator streamingIDGen = new StringGenerator();
	
	protected	String		sessionKey;
	protected	String		streamingSessionKey;
	protected	String		dataCacheId;
	
	protected	Date	creationDate = new Date();
//	protected	DimDimSession		parent;
	protected	Locale		sessionLocale = LocaleManager.getManager().getDefaultLocale();
	protected	long		lastAccessTime = System.currentTimeMillis();
	protected	long		timeout = ConferenceConsoleConstants.getAttendeeSessionTimeout()*1000;
	protected	long		basicTimeout;
	protected	int		currentState = DimDimSession.SESSION_ACTIVE;
	
	protected	String		os;
	protected	String		browserType;
	protected	String		browserVersion;
	
	protected	String		uri;
	
	protected	transient	TimerServiceTaskId	taskId;
	
	public	DimDimSession()
	{
		this(null);
	}
	public	DimDimSession(DimDimSession parent)
	{
//		this.parent = parent;
		sessionKey = UUID.randomUUID().toString();//DimDimSession.idGen.generate();
		streamingSessionKey = UUID.randomUUID().toString();//DimDimSession.streamingIDGen.generateRandomString();
		dataCacheId = UUID.randomUUID().toString();
		this.basicTimeout = timeout;
	}
	public	void	extendSessionTimeout()
	{
		this.timeout = this.basicTimeout * 4;
	}
	public	void	resetSessionTimeout()
	{
		this.timeout = this.basicTimeout;
	}
	public	int	compareTo(Object obj)
	{
		if (obj instanceof DimDimSession)
		{
//			return this.creationDate.compareTo(((UserSession)obj).getCreationDate());
			return this.sessionKey.compareTo(((DimDimSession)obj).getSessionKey());
		}
		else
		{
			return	1;
		}
	}
//	public	void	addToTimerService()
//	{
//		//	Schedule the timerout timer
//		TimerServiceUser tsu = new TimerServiceUser()
//		{
//			public long getTimerDelay()
//			{
//				return  60000;
//			}
//			public boolean timerCall()
//			{
//				boolean	sessionActive = isActive();
//				if (sessionActive)
//				{
//					if (isAbandoned())
//					{
//						sessionActive = false;
//						close();
//					}
//				}
//				return sessionActive;
//			}
//			public void setTimerServiceTaskId(TimerServiceTaskId taskId)
//			{
//				setTaskId(taskId);
//			}
//		};
//		TimerService.getService().addUser(tsu);
//	}
	public String getSessionKey()
	{
		return sessionKey;
	}
	public void setSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	public	String	getDataCacheId()
	{
		return	this.dataCacheId;
	}
	public String getStreamingSessionKey()
	{
		return streamingSessionKey;
	}
	public void setStreamingSessionKey(String streamingSessionKey)
	{
		this.streamingSessionKey = streamingSessionKey;
	}
//	public DimDimSession getParent()
//	{
//		return this.parent;
//	}
	public Date getCreationDate()
	{
		return creationDate;
	}
	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}
	public synchronized	long getTimeout()
	{
		return timeout;
	}
	public synchronized	void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
//	public void setParent(DimDimSession parent)
//	{
//		this.parent = parent;
//	}
	public	boolean	isValid()
	{
		return	true;//((new Date()).getTime() - this.creationDate.getTime())< 9*1000;
	}
	public	boolean	isPresenter()
	{
		return	false;
	}
	public	boolean	isAbandoned()
	{
		long lat = this.lastAccessTime;
		long currentTime = System.currentTimeMillis();
//		if (this.parent != null && this.parent.getLastAccessTime() > lat)
//		{
//			lat = this.parent.getLastAccessTime();
//		}
		boolean abandoned = (currentTime - lat) > timeout;
		if (abandoned)
		{
			System.out.println("Abandoned session last access time was: "+lat);
			System.out.println("Current time is                       : "+currentTime);
		}
		return abandoned;
//		return	( (System.currentTimeMillis() - this.lastAccessTime)>timeout ||
//			(this.parent != null && this.parent.isAbandoned()) );
	}
	public int getCurrentState()
	{
		return this.currentState;
	}
	public	boolean	isActive()
	{
		return	this.currentState == DimDimSession.SESSION_ACTIVE;
	}
	public	synchronized	void	close()
	{
		if (this.currentState == DimDimSession.SESSION_CLOSED)
		{
			return;
		}
		UserSessionManager.getManager().removeSession(this);
		this.currentState  = DimDimSession.SESSION_CLOSED;
	}
	public long getLastAccessTime()
	{
		return lastAccessTime;
	}
	protected void setLastAccessTime(long lastAccessTime)
	{
		this.lastAccessTime = lastAccessTime;
//		if (this.parent != null)
//		{
//			this.parent.setLastAccessTime(this.lastAccessTime);
//		}
	}
	public	void	setLastAccessTime()
	{
		this.lastAccessTime = System.currentTimeMillis();
//		if (this.parent != null)
//		{
//			this.parent.setLastAccessTime(this.lastAccessTime);
//		}
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
	public TimerServiceTaskId	getTimerServiceTaskId()
	{
		return	taskId;
	}
	public void setTaskId(TimerServiceTaskId taskId)
	{
		this.taskId = taskId;
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
		if (this.uri != null)
		{
			UserSessionDataManager.getDataManager().clearUserRequest(this.uri);
		}
		this.uri = uri;
	}
}
