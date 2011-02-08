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

package com.dimdim.conference.db;

import	java.io.Serializable;
import	java.util.Date;
import	java.util.List;
import	java.util.Calendar;
import	java.util.GregorianCalendar;
import	java.util.TimeZone;
import	java.text.DateFormat;

import	com.dimdim.conference.model.ConferenceInfo;

/**
 * This object is used to store created conferences.
 */
public	class	ConferenceSpec	implements	Comparable, Serializable
{
	/**
	 * 
	 */
	
	protected	String	organizerEmail;
	protected	String	organizerDisplayName;
	protected	String	name;
	protected	String	description;
	protected	String	key;
	protected	Date	startTime;
	protected	String	timeZoneId;
	protected	List	presenters;
	protected	List	attendees;
	
	public	ConferenceSpec()
	{
	}
	public ConferenceSpec(String organizerEmail, String organizerDisplayName,
		String name, String description, String key, Date startTime,
		String timeZoneId, List presenters, List attendees)
	{
		this.organizerEmail = organizerEmail;
		this.organizerDisplayName = organizerDisplayName;
		this.name = name;
		this.description = description;
		this.key = key;
		this.startTime = startTime;
		this.timeZoneId = timeZoneId;
		this.presenters = presenters;
		this.attendees = attendees;
	}
	public	ConferenceInfo	getConferenceInfo()
	{
		int numParticipants = 0;
		if (this.presenters != null)
		{
			numParticipants += this.presenters.size();
		}
		if (this.attendees != null)
		{
			numParticipants += this.attendees.size();
		}
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
		TimeZone tz = TimeZone.getTimeZone(this.timeZoneId);
		if (tz != null)
		{
			df.setTimeZone(tz);
		}
		return	new	ConferenceInfo( name, key, numParticipants, 0, startTime,
				df.format(startTime), organizerDisplayName, organizerEmail);
	}
	public	int	compareTo(Object obj)
	{
		if (obj instanceof ConferenceSpec)
		{
			return	this.organizerEmail.compareTo(((ConferenceSpec)obj).getOrganizerEmail());
		}
		else
		{
			return	1;
		}
	}
	public	boolean	isMoreThanDayOld()
	{
		TimeZone tz = TimeZone.getTimeZone(this.timeZoneId);
		Calendar gc = GregorianCalendar.getInstance(tz);
		Date dt = gc.getTime();
		dt.setTime(dt.getTime() - (1000*60*60*6));
		if (this.startTime.before(dt))
		{
			return	true;
		}
		return	false;
	}
	public List getAttendees()
	{
		return attendees;
	}
	public void setAttendees(List attendees)
	{
		this.attendees = attendees;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getOrganizerDisplayName()
	{
		return organizerDisplayName;
	}
	public void setOrganizerDisplayName(String organizerDisplayName)
	{
		this.organizerDisplayName = organizerDisplayName;
	}
	public String getOrganizerEmail()
	{
		return organizerEmail;
	}
	public void setOrganizerEmail(String organizerEmail)
	{
		this.organizerEmail = organizerEmail;
	}
	public List getPresenters()
	{
		return presenters;
	}
	public void setPresenters(List presenters)
	{
		this.presenters = presenters;
	}
	public Date getStartTime()
	{
		return startTime;
	}
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	public String getTimeZoneId()
	{
		return timeZoneId;
	}
	public void setTimeZoneId(String timeZoneId)
	{
		this.timeZoneId = timeZoneId;
	}
}
