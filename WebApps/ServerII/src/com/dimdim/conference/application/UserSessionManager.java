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

import	java.util.TreeMap;
import	java.util.Iterator;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.db.ConferenceDB;
import com.dimdim.locale.LocaleManager;
import com.dimdim.util.timer.TimerService;
import com.dimdim.util.timer.TimerServiceTaskId;
import com.dimdim.util.timer.TimerServiceUser;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UserSessionManager	
{
	protected	static	UserSessionManager	theManager;
	
	public	static	UserSessionManager	getManager()
	{
		if (UserSessionManager.theManager == null)
		{
			UserSessionManager.createManager();
		}
		return	UserSessionManager.theManager;
	}
	protected	synchronized	static	void	createManager()
	{
		if (UserSessionManager.theManager == null)
		{
			UserSessionManager.theManager = new UserSessionManager();
//			UserSessionManager.theManager.start();
		}
	}
	
	protected	TreeMap		userSessions;
	protected	boolean		processStopped = false;
	
	public	UserSessionManager()
	{
		this.userSessions = new TreeMap();
		
		//	Create a continuous task for properties and locale files refreshes.
		TimerServiceUser tsu = new TimerServiceUser()
		{
			public long getTimerDelay()
			{
				return  300000;
			}
			public boolean timerCall()
			{
				try
				{
					if (!isProcessStopped())
					{
						ConferenceConsoleConstants.reInit();
						ConferenceDB.getDB().rereadUserFiles();
						LocaleManager.getManager().refresh();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return true;
			}
			public void setTimerServiceTaskId(TimerServiceTaskId taskId)
			{
			}
		};
		TimerService.getService().addUser(tsu);
		
		Thread shutdownListener = new Thread()
		{
			public	void	run()
			{
				UserSessionManager.theManager.setProcessStopped(true);
//				TimerService.stopService();
//				StreamingServerManager.getManager().shutdown();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownListener);
	}
	public	int		getNumberOfActiveSessions()
	{
		return	this.userSessions.size();
	}
	public boolean isProcessStopped()
	{
		return processStopped;
	}
	public void setProcessStopped(boolean processStopped)
	{
		this.processStopped = processStopped;
	}
	public	Iterator	sessions()
	{
		return	this.userSessions.values().iterator();
	}
	public	void	addUserSession(String httpSessionKey, UserSession userSession)
	{
		userSession.setLastAccessTime();
//		userSession.setHttpSessionKey(httpSessionKey);
		this.addSession(userSession);
	}
//	public	void	addChildSession(ChildSession childSession)
//	{
//		this.addSession(childSession);
//	}
	public	void	addSession(final DimDimSession dms)
	{
		String key = dms.getSessionKey();
		System.out.println("Adding Session ---- :"+key);
		this.userSessions.put(dms.getSessionKey(),dms);
		
		//	Schedule the timerout timer
		TimerServiceUser tsu = new TimerServiceUser()
		{
			public long getTimerDelay()
			{
				return  60000;
			}
			public boolean timerCall()
			{
				boolean	sessionActive = dms.isActive();
				if (sessionActive)
				{
					if (dms.isAbandoned() || isProcessStopped())
					{
						sessionActive = false;
						dms.close();
					}
				}
				return sessionActive;
			}
			public void setTimerServiceTaskId(TimerServiceTaskId taskId)
			{
				dms.setTaskId(taskId);
			}
		};
		TimerService.getService().addUser(tsu);
//		System.out.println("Sessions: "+this.userSessions);
	}
	public	DimDimSession	getSession(String sessionKey)
	{
		DimDimSession dms = (DimDimSession)this.userSessions.get(sessionKey);
		return	dms;
	}
	protected	void	removeSession(DimDimSession userSession)
	{
		String key = userSession.getSessionKey();
		System.out.println("Removing Session ----: "+key);
		this.userSessions.remove(key);
//		System.out.println("Session:"+this.userSessions);
	}
//	protected	void	removeChildSession(ChildSession childSession)
//	{
//		removeSession(childSession);
//	}
	/**
	 * This thread monitors all the sessions for timeouts.
	 */
	/*
	public	void	run()
	{
		int	afterCloseCounter = 0;
		while (true)
		{
//			System.out.println("User Session Manager cleanup round-");
			Vector v = new Vector();
			try
			{
				Iterator iter = this.userSessions.keySet().iterator();
				while (iter.hasNext())
				{
					String	key = (String)iter.next();
					DimDimSession userSession = (DimDimSession)this.userSessions.get(key);
//					System.out.println("Checking session: "+userSession.getSessionKey());
					if (userSession.isAbandoned() || this.isProcessStopped())
					{
//						System.out.println("Abandoned session, close: "+userSession.getSessionKey());
						v.addElement(key);
					}
				}
				int size = v.size();
				for (int i=0; i<size; i++)
				{
					String key = (String)v.elementAt(i);
					System.out.println("Closing abandoned session, key: "+key);
					DimDimSession userSession = (DimDimSession)this.userSessions.get(key);
					if (userSession != null)
					{
						this.userSessions.remove(key);
						userSession.close();
					}
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
			
			if (this.isProcessStopped())
			{
				if (afterCloseCounter++ > 2)
				{
					break;
				}
			}
			
			try
			{
				if (this.isProcessStopped())
				{
					Thread.sleep(2000);
				}
				else
				{
					Thread.sleep(15000);
				}
			}
			catch(Exception e)
			{
			}
			
			try
			{
				if (!this.isProcessStopped())
				{
					ConferenceConsoleConstants.reInit();
					ConferenceDB.getDB().rereadUserFiles();
					LocaleManager.getManager().refresh();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	*/
}
