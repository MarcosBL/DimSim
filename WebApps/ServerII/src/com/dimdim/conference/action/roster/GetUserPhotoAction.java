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
import com.dimdim.conference.application.presentation.PresentationManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.application.presentation.PresentationManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class GetUserPhotoAction	extends	HttpAwareConferenceAction
{
	
	protected	String	imageUrl	= "images/blank_full.jpg";
	
	public	GetUserPhotoAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
		if (conf != null)
		{
			String conferenceKey = conf.getConfig().getConferenceKey();
			String photoFileName = user.getId().replaceAll("@","_");
			super.servletRequest.setAttribute("presentationsRoot","presentations");
			super.servletRequest.setAttribute("conferenceKey",conferenceKey);
			super.servletRequest.setAttribute("userId",photoFileName);
			
			if (conferenceKey != null)
			{
				this.imageUrl = "presentations/"+conferenceKey+"/"+photoFileName+".jpg";
			}
			else
			{
				this.imageUrl = "images/blank_full.jpg";
			}
		}
		else
		{
			ret = ERROR;
		}
		return	ret;
	}
	public String getImageUrl()
	{
		return imageUrl;
	}
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}
}
