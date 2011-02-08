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

import com.dimdim.conference.action.HttpAwareConferenceAction;
import com.dimdim.conference.action.check.Connect;
import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserSessionData;
import com.dimdim.util.session.UserSessionDataManager;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.model.Participant;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.UserRosterStateMachine;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.data.application.UIDataManager;
import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action needs to do what the start or join sequence does in terms of
 * simply setting up the user session information to be picked up by the
 * subsequent action that loads the actual console page.
 */
public class ReloadConsoleAction	extends	HttpAwareConferenceAction
{
	
	protected	String	uri;
	protected	String	reloadConsole = "true";
	
	public	ReloadConsoleAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		
		//	Simply save the user session on a new user request.
		this.uri = UserSessionDataManager.getDataManager().
			saveReloadConsoleRequestData(Connect.RELOAD_ACTION,
					UserSessionData.ACTIVE_USER_SESSION, this.userSession);
		this.userSession.setUri(uri);
		((Participant)this.userSession.getUser()).setCurrentState(UserRosterStateMachine.STATE_RELOADING);
		
		return	ret;
	}
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	public String getReloadConsole()
	{
		return reloadConsole;
	}
	public void setReloadConsole(String reloadConsole)
	{
		this.reloadConsole = reloadConsole;
	}
}
