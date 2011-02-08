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
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class SetProfileOptionsAction	extends	ConferenceAction
{
	protected	String	netProfile;
	protected	String	imgQuality;
	protected	Integer	maxParticipants;
	protected	Integer	maxMeetingTime;
	protected	Integer	maxAttendeeMikes;
	protected	String	returnUrl;
	
	public	SetProfileOptionsAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		System.out.println("Net Profile:"+netProfile+", Image Quality: "+imgQuality+", max participants:"+maxParticipants+", max meeting time:"+maxMeetingTime);
		try
		{
			if (netProfile != null && imgQuality != null &&
					maxParticipants != null && maxMeetingTime != null)
			{
				if (maxAttendeeMikes == null)
				{
					maxAttendeeMikes = new Integer(conf.getMaxAttendeeMikes());
				}
				if (returnUrl == null)
				{
					returnUrl = conf.getReturnUrl();
				}
				conf.setConferenceOptions(conf.isLobbyEnabled(),netProfile,
						imgQuality,maxMeetingTime,maxParticipants,null,maxAttendeeMikes,returnUrl, true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		
		return	ret;
	}
	public String getImgQuality()
	{
		return imgQuality;
	}
	public void setImgQuality(String imgQuality)
	{
		this.imgQuality = imgQuality;
	}
	public String getNetProfile()
	{
		return netProfile;
	}
	public void setNetProfile(String netProfile)
	{
		this.netProfile = netProfile;
	}
	public Integer getMaxMeetingTime()
	{
		return maxMeetingTime;
	}
	public void setMaxMeetingTime(Integer maxMeetingTime)
	{
		this.maxMeetingTime = maxMeetingTime;
	}
	public Integer getMaxParticipants()
	{
		return maxParticipants;
	}
	public void setMaxParticipants(Integer maxParticipants)
	{
		this.maxParticipants = maxParticipants;
	}
	public Integer getMaxAttendeeMikes()
	{
		return maxAttendeeMikes;
	}
	public void setMaxAttendeeMikes(Integer maxAttendeeMikes)
	{
		this.maxAttendeeMikes = maxAttendeeMikes;
	}
	public String getReturnUrl()
	{
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl)
	{
		this.returnUrl = returnUrl;
	}
}
