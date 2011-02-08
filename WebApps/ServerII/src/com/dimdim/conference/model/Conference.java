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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : Conference.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.model;

import java.util.Date;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.config.ConferenceConfig;
import com.dimdim.util.misc.IDGenerator;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public	class	Conference
{
	
	protected	Date		startTime;
	
//	protected	Participant			organizer;
	protected	IConference			activeConference;
	protected	ConferenceConfig	conferenceConfig;
	
	protected	UserRoster			userRoster;
	protected	ResourceRoster		resourceRoster;
	
	/**
	 * @param confKey 
	 * 
	 */
	public Conference(IConference activeConference, ConferenceConfig conferenceConfig)
	{
		this.startTime = new Date();
		this.activeConference = activeConference;
		this.conferenceConfig = conferenceConfig;
		this.userRoster = new UserRoster(activeConference);
		this.resourceRoster = new ResourceRoster();
	}
	public	Date	getStartTime()
	{
		return	this.startTime;
	}
	public	Participant	getOrganizer()
	{
//		return	this.organizer;
		return	this.userRoster.getOrganizer();
	}
	public	void	setOrganizer(Participant organizer)
	{
//		this.organizer = organizer;
		this.userRoster.setOrganizer(organizer);
	}
	public	ConferenceConfig	getConferenceConfig()
	{
		return	this.conferenceConfig;
	}
	public	UserRoster	getUserRoster()
	{
		return	this.userRoster;
	}
	public	ResourceRoster	getResourceRoster()
	{
		return	this.resourceRoster;
	}
	public	ConferenceInfo	getConferenceInfo()
	{
		ConferenceInfo ci = new ConferenceInfo(this.conferenceConfig.getConferenceName(),
				this.conferenceConfig.getConferenceKey(),
				this.userRoster.getNumberOfParticipants(),
				this.resourceRoster.getNumberOfResources(),
				this.startTime,
				this.startTime.toString(),
				this.getOrganizer().getDisplayName(),
				this.getOrganizer().getEmail());
		
		return	ci;
	}
	public	IConference	getActiveConference()
	{
		return	this.activeConference;
	}
}
