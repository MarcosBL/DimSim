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

/*
import	java.util.HashMap;
import	java.util.Iterator;

import com.dimdim.util.timer.TimerService;
*/
/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ParticipantSessionManager	
{
	/*
	protected	HashMap		userSessions;
	
	public	ParticipantSessionManager()
	{
		this.userSessions = new HashMap();
	}
	public	int		getNumberOfActiveSessions()
	{
		return	this.userSessions.size();
	}
	public	Iterator	sessions()
	{
		return	this.userSessions.values().iterator();
	}
	public	void	addParticipantSession(ParticipantSession userSession)
	{
		userSession.setLastAccessTime();
		String key = userSession.getSessionKey();
		System.out.println("Adding Session ---- :"+key);
		this.userSessions.put(userSession.getSessionKey(),userSession);
		
		TimerService.getService().addUser(userSession);
	}
	public	ParticipantSession	getParticipantSession(String sessionKey)
	{
		ParticipantSession session = (ParticipantSession)this.userSessions.get(sessionKey);
		return	session;
	}
	protected	void	removeParticipantSession(ParticipantSession userSession)
	{
		String key = userSession.getSessionKey();
		System.out.println("Removing Session ----: "+key);
		this.userSessions.remove(key);
	}
	*/
}
