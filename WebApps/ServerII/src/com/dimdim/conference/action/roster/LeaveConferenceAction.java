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

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.application.UserSessionManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.UserNotInConferenceException;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Once the user leaves conference the session key is invalid. The
 * User Session Object is removed from the http session.
 */
public class LeaveConferenceAction	extends	ConferenceAction
{
	
	public	LeaveConferenceAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
//		IConference conf = this.userSession.getConference();
//		IConferenceParticipant user = this.userSession.getUser();
		
		try
		{
			getSession().remove(this.sessionKey);
			
			userSession.close();
			/*
			conf.removeUserFromConference(user);
			
			userSession.setUser(null);
			userSession.setConference(null);
			*/
			/*	Remove the user session from timeout monitoring		*/
			
//			UserSessionManager.getManager().removeUserSession(userSession);
		}
//		catch(UserNotInConferenceException uice)
//		{
//			ret = ERROR;
//		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
}
