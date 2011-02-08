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

package com.dimdim.conference.application.handler;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import	com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.application.UserSessionManager;
import com.dimdim.conference.application.presentation.PresentationManager;
import com.dimdim.conference.db.ConferenceDB;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class ConferenceHttpSessionListener	implements	HttpSessionListener
{
	public	ConferenceHttpSessionListener()
	{
	}
	/**
	 * We must remember all the http sessions.
	 */
	public	void	sessionCreated(HttpSessionEvent hse)
	{
		System.out.println("******************ConferenceHttpSessionListener:sessionCreated**::"+hse.getSession().getId());
		hse.getSession().setMaxInactiveInterval(180);
	}
	
	/**
	 * Get the user session, from it the client and call leave conference
	 * if it has not already been called. This could indicate that the
	 * user's browser was destroyed, crashed or the connection was lost in
	 * some other way.
	 */
	public	void	sessionDestroyed(HttpSessionEvent hse)
	{
		System.out.println("******************ConferenceHttpSessionListener:sessionDestroyed**::"+hse.getSession().getId());
//		HttpSession session = hse.getSession();
//		String httpSessionId = session.getId();
//		Enumeration attrs = session.getAttributeNames();
//		while (attrs.hasMoreElements())
//		{
//			try
//			{
//				String attr = (String)attrs.nextElement();
//				Object value = session.getAttribute(attr);
//				if (value instanceof UserSession)
//				{
//					UserSession us = (UserSession)value;
//					if (us.getHttpSessionKey() != null &&
//							us.getHttpSessionKey().equals(httpSessionId))
//					{
//						System.out.println("Found user session - closing - ");
//						us.close();
//					}
//				}
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
	}
}
