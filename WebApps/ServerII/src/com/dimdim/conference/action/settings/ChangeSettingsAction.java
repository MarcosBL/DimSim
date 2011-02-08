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
 
package com.dimdim.conference.action.settings;

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class ChangeSettingsAction	extends	ConferenceAction
{
	protected	int		maxParticipants;
	protected	int		maxMeetingTime;
	protected	String	trackbackURL;
	
	public	ChangeSettingsAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
		try
		{
//			if (mood != null)
//			{
//				conf.getRosterManager().getRosterObject().updateParticipantMood(user.getEmail(),mood);
//			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		
		return	ret;
	}
	public int getMaxMeetingTime()
	{
		return maxMeetingTime;
	}
	public void setMaxMeetingTime(int maxMeetingTime)
	{
		this.maxMeetingTime = maxMeetingTime;
	}
	public int getMaxParticipants()
	{
		return maxParticipants;
	}
	public void setMaxParticipants(int maxParticipants)
	{
		this.maxParticipants = maxParticipants;
	}
}
