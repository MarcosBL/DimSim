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

import	java.io.File;
import	java.util.Vector;
import	java.util.TimeZone;
import	java.util.Date;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class reads a list of supported timezones from a given csv( comma
 * seperated values ) file. This class expects only 2 fields in the file.
 * 0: time zone id that can be directly used in TimeZome.getTimeZone(id)
 * method.
 * 1: a descriptive name that will be displayed to user for choosing the
 * time zome option from a form.
 */

public class TimeZonesList
{
	protected	String	fileName;
	protected	File	file;
	protected	long	lastUpdateTime = 0;
	
	protected	Vector	timeZoneOptions;
	
	public	TimeZonesList(File file)
	{
		this.file = file;
		this.timeZoneOptions = new Vector();
	}
	public	TimeZonesList(String fileName)
	{
		this.fileName = fileName;
		this.file = new File(fileName);
		this.timeZoneOptions = new Vector();
	}
	public	Vector getTimeZonesList()
	{
		if (this.file.exists())
		{
			if (this.lastUpdateTime == 0 ||
					this.lastUpdateTime < file.lastModified())
			{
				readTimeZoneOptions();
			}
		}
		return	this.timeZoneOptions;
	}
	protected	void	readTimeZoneOptions()
	{
		try
		{
			Vector list = new Vector();
			CSVFileReader reader = new CSVFileReader(this.file,";");
			Vector v = reader.getNextLine();
			while (v != null && v.size() > 1)
			{
				String timeZoneId = (String)v.elementAt(0);
//				System.out.println("Read time zone id:"+timeZoneId);
				String timeZoneDisplayName = (String)v.elementAt(1);
				
				TimeZone tz = TimeZone.getTimeZone(timeZoneId);
				if (tz != null)
				{
					list.addElement(new TimeZoneOption(tz,timeZoneDisplayName));
				}
				
				v = reader.getNextLine();
			}
			
			this.timeZoneOptions = list;
			this.lastUpdateTime = file.lastModified();
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * A Simple main to browse through the timezones list.
	 * @param args
	 */
	public	static	void	main(String[] args)
	{
		Date today = new Date();
		
		// Get all time zone ids
		String[] zoneIds = TimeZone.getAvailableIDs();
		System.out.println("ID, Short Name (today's dst), Long Name(today's dst), Timezone Uses DST, Timezone in DST today");
		// View every time zone
		for (int i=0; i<zoneIds.length; i++)
		{
			// Get time zone by time zone id
			TimeZone tz = TimeZone.getTimeZone(zoneIds[i]);
			
			// Get the display name
			String shortName = tz.getDisplayName(tz.inDaylightTime(today), TimeZone.SHORT);
			String longName = tz.getDisplayName(tz.inDaylightTime(today), TimeZone.LONG);
				
			// Get the number of hours from GMT
			int rawOffset = tz.getRawOffset();
			int hour = rawOffset / (60*60*1000);
			int min = Math.abs(rawOffset / (60*1000)) % 60;
			
			// Does the time zone have a daylight savings time period?
			boolean hasDST = tz.useDaylightTime();
			
			// Is the time zone currently in a daylight savings time?
			boolean inDST = tz.inDaylightTime(today);
			System.out.println(tz.getID()+","+shortName+","+longName+","+hasDST+","+inDST+","+rawOffset+","+hour+","+min );
		}
	}
}
