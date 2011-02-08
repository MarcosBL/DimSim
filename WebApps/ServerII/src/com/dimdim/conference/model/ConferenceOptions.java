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

package com.dimdim.conference.model;

import com.dimdim.conference.ConferenceConsoleConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This simple bean is used to maintain and carry various options that can
 * be set by presenter while starting a conference.
 */

public class ConferenceOptions
{
	public	static	final	String	CONFERENCE_OPTIONS	=	"CONFERENCE_OPTIONS";
	
	protected	String	lobby = "false";	//	Values 'true' / 'false'
	protected	String	networkProfile = "2";	//	Values '1', '2', '3'
	protected	String	imageQuality = "medium";	//	Values 'low', 'medium', 'high
	protected	String	meetingHours = "2";		//	Limited to server wide max
	protected	String	meetingMinutes = "0";	//	Limited to server wide max
	protected	String	maxParticipants = ConferenceConsoleConstants.getMinParticipantsPerConference()+"";	//	Limited to server wide max
	protected	String	presenterAV = "audio";
	protected	String	attendees = "";
	protected	String	returnUrl = "";	//	Value 'DEFAULT' means root url of the server
	protected	String	maxAttendeeAudios = "0";	//	Limited to server wide max
	
	public	ConferenceOptions()
	{
	}
	public ConferenceOptions(String lobby, String networkProfile,
			String imageQuality, String meetingHours,
			String meetingMinutes, String maxParticipants, String presenterAV,
			String attendees, String returnUrl, String maxAttendeeAudios)
	{
		this.lobby = lobby;
		this.networkProfile = networkProfile;
		this.imageQuality = imageQuality;
		this.meetingHours = meetingHours;
		this.meetingMinutes = meetingMinutes;
		this.maxParticipants = maxParticipants;
		this.presenterAV = presenterAV;
		this.attendees = attendees;
		this.returnUrl = returnUrl;
		this.maxAttendeeAudios = maxAttendeeAudios;
	}
	public String getAttendees()
	{
		return attendees;
	}
	public void setAttendees(String attendees)
	{
		this.attendees = attendees;
	}
	public String getImageQuality()
	{
		return imageQuality;
	}
	public void setImageQuality(String imageQuality)
	{
		this.imageQuality = imageQuality;
	}
	public String getLobby()
	{
		return lobby;
	}
	public void setLobby(String lobby)
	{
		this.lobby = lobby;
	}
	public String getMaxAttendeeAudios()
	{
		return maxAttendeeAudios;
	}
	public void setMaxAttendeeAudios(String maxAttendeeAudios)
	{
		this.maxAttendeeAudios = maxAttendeeAudios;
	}
	public String getMaxParticipants()
	{
		return maxParticipants;
	}
	public void setMaxParticipants(String maxParticipants)
	{
		this.maxParticipants = maxParticipants;
	}
	public String getMeetingHours()
	{
		return meetingHours;
	}
	public void setMeetingHours(String meetingHours)
	{
		this.meetingHours = meetingHours;
	}
	public String getMeetingMinutes()
	{
		return meetingMinutes;
	}
	public void setMeetingMinutes(String meetingMinutes)
	{
		this.meetingMinutes = meetingMinutes;
	}
	public String getNetworkProfile()
	{
		return networkProfile;
	}
	public void setNetworkProfile(String networkProfile)
	{
		this.networkProfile = networkProfile;
	}
	public String getPresenterAV()
	{
		return presenterAV;
	}
	public void setPresenterAV(String presenterAV)
	{
		this.presenterAV = presenterAV;
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

