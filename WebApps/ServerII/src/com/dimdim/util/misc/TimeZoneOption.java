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

package com.dimdim.util.misc;

import java.util.TimeZone;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class TimeZoneOption
{
	protected	TimeZone	timeZone;
	protected	String		timeZoneTag;	//	EDT / EST etc.
	protected	String		timeZoneDisplayName;
	protected	int			gmtOffsetHours;
	protected	int			gmtOffsetMinutes;
	protected	String		gmtOffsetTagStr;
	
	public TimeZoneOption()
	{
	}
	public TimeZoneOption(TimeZone timeZone, String timeZoneDisplayName)
	{
		this.timeZone = timeZone;
		this.timeZoneDisplayName = timeZoneDisplayName;
		
		if (!timeZone.useDaylightTime())
		{
			String  shortName1 = timeZone.getDisplayName(false,TimeZone.SHORT);
			this.timeZoneTag = "("+shortName1+") ";
		}
		else
		{
			String  shortName1 = timeZone.getDisplayName(false,TimeZone.SHORT);
			String  shortName2 = timeZone.getDisplayName(true,TimeZone.SHORT);
			this.timeZoneTag = "("+shortName1+"/"+shortName2+") ";
		}
		
		// Get the number of hours from GMT
		int rawOffset = timeZone.getRawOffset();
		gmtOffsetHours = rawOffset / (60*60*1000);
		gmtOffsetMinutes = Math.abs(rawOffset / (60*1000)) % 60;
		
		if (rawOffset == 0)
		{
			this.gmtOffsetTagStr = "(GMT)";
		}
		else
		{
			String sign = "+";
			if (gmtOffsetHours < 0)
			{
				sign = "-";
			}
			String h = Math.abs(gmtOffsetHours)+"";
			if (h.length() == 1)	h = "0"+h;
			String m = gmtOffsetMinutes+"";
			if (m.length() == 1)	m = m+"0";
			
			this.gmtOffsetTagStr = "(GMT"+sign+h+":"+m+")";
		}
	}
	public	int	getGmtOffsetHours()
	{
		return	this.gmtOffsetHours;
	}
	public	int	getGmtOffsetMinutes()
	{
		return	this.gmtOffsetMinutes;
	}
	public	String	getGMTOffsetTagStr()
	{
		return	this.gmtOffsetTagStr;
	}
	public TimeZone getTimeZone()
	{
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone)
	{
		this.timeZone = timeZone;
	}
	public String getTimeZoneDisplayNameWithGMTOffset()
	{
		return this.gmtOffsetTagStr+" "+timeZoneDisplayName;
	}
	public String getTimeZoneDisplayName()
	{
		return timeZoneDisplayName;
	}
	public void setTimeZoneDisplayName(String timeZoneDisplayName)
	{
		this.timeZoneDisplayName = timeZoneDisplayName;
	}
	public String getTimeZoneTag()
	{
		return timeZoneTag;
	}
	public void setTimeZoneTag(String timeZoneTag)
	{
		this.timeZoneTag = timeZoneTag;
	}
}
