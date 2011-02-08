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
 
package com.dimdim.conference.action.roster;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.HttpAwareConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.data.application.UIDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action specifically triggers the events that dispatch the current
 * state of the conference to the newly joined attendee. this is because
 * there could be significant time delay between the user joining and the
 * console loading due to network speeds.
 */
public class GetConsoleAction	extends	HttpAwareConferenceAction
{
	public	GetConsoleAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
		if (conf != null)
		{
			conf.getConsole(user);
			super.servletRequest.setAttribute("sessionKey",super.sessionKey);
			
			String browserType = (String)session.get(ConferenceConsoleConstants.BROWSER_TYPE);
			String browserVersion = (String)session.get(ConferenceConsoleConstants.BROWSER_VERSION);
			
			String userInfo = this.userSession.getUserInfo(sessionKey,
					this.servletRequest.getLocale(),
					browserType, browserVersion,
					servletRequest.getRequestURL().toString().startsWith("htts"));
			String	dataCacheId = this.userSession.getDataCacheId();
//			session.put("user_info", userInfo);
			UIDataManager.getUIDataManager().addSessionDataBuffer("user_info"+dataCacheId, userInfo);
			this.servletRequest.setAttribute("dataCacheId", dataCacheId);
		}
		else
		{
			ret = ERROR;
		}
		return	ret;
	}
}
