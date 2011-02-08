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

package com.dimdim.conference.action.common;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.model.IConference;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class WelcomeToConferenceAction	extends	CommonDimDimAction
{
	protected	String	wflag;
	
	public	WelcomeToConferenceAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = ERROR;
//		UserSession userSession = (UserSession)session.
//			get(ConferenceConsoleConstants.ACTIVE_USER_SESSION);
//		if (userSession != null)
//		{
//			IConference conf = userSession.getConference();
//			if (conf != null)
//			{
//				if (!userSession.isConsoleLoaded())
//				{
//					ret = SUCCESS;
//				}
//			}
//		}
		return	ret;
	}
	public String getWflag()
	{
		return wflag;
	}
	public void setWflag(String wflag)
	{
		this.wflag = wflag;
	}
}
