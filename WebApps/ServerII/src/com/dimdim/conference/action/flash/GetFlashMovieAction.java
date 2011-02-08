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

package com.dimdim.conference.action.flash;

import com.dimdim.conference.action.HttpAwareConferenceAction;
import com.dimdim.conference.application.presentation.PresentationManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class GetFlashMovieAction	extends	HttpAwareConferenceAction
{
	protected	String	ownerId;
	protected	String	profile;
	protected	String	movieId;
	protected	String	nodeName;
	protected	String	width;
	protected	String	height;
	
	protected	String	movieUrl	= "jsp/flash/blankFlashMovie.jsp";
	
	public	GetFlashMovieAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
//		IConferenceParticipant user = this.userSession.getUser();
		
		if (conf != null)
		{
			String conferenceKey = conf.getConfig().getConferenceKey();
//			if (ownerId.equalsIgnoreCase("SYSTEM"))
//			{
//				conferenceKey = PresentationManager.GlobalSampleConference;
//			}
			super.servletRequest.setAttribute("presentationsRoot","presentations");
			super.servletRequest.setAttribute("conferenceKey",conferenceKey);
			
			if (conferenceKey != null)
			{
			}
		}
		else
		{
			ret = ERROR;
		}
		return	ret;
	}
	public String getOwnerId()
	{
		return ownerId;
	}
	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}
}
