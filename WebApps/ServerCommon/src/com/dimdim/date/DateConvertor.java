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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                    *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.date;

import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This utility class provides a simple function to convert a textual date
 * and time specification into Date object in current timezone.
 * 
 * Conversely it also provides method to conver a given date object into
 * a textual string in required format.
 */

public class DateConvertor
{
	public	static	final	String	DATE_FORMAT = "%B %d, %Y %I:%M:%S %p";
	
	public	DateConvertor()
	{
		
	}
	/**
	 * This function is designed to use the specification exactly as given by the
	 * ui to the server actions.
	 * 
	 * @param dateStr
	 * @param hours
	 * @param minutes
	 * @param amPM
	 * @param timeZoneStr
	 * @return
	 */
	public	Date	readDateString(String dateStr, String hours,
				String minutes, String amPM, String timeZoneStr)
	{
		String timeStr = hours+":"+minutes+":00 "+amPM;
		
		return	readDateString(dateStr, timeStr, timeZoneStr);
	}
	public	Date	getTimeInServerTimeZone(Date dt)
	{
		return	this.getTimeInTimeZone(dt,TimeZone.getDefault());
	}
	public	Date	getTimeInTimeZone(Date dt, TimeZone targetTimeZone)
	{
		try
		{
			DateFormat dateFormater1 = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,Locale.US);
			dateFormater1.setTimeZone(targetTimeZone);
			String s1 = dateFormater1.format(dt);
			
			return	dateFormater1.parse(s1);
		}
		catch(Exception e)
		{
			return	null;
		}
	}
	/**
	 * This function jumps through a few hoops to parse a given string into a 
	 * proper date object.
	 * 
	 * @param dateStr - must be "<full month> <date>, <year>", e.g. "July 01, 2007"
	 * @param timeStr - must be "HH:MM:SS AM/PM", e.g. "09:30:00 AM"
	 * @param timeZoneStr - must be a java code e.g. "America/Toronto"
	 * @return
	 */
	public	Date	readDateString(String dateStr, String timeStr, String timeZoneStr)
	{
		Date dt = new Date();
		try
		{
			DateFormat dateFormater = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,Locale.US);
//			System.out.println("The date string format must be: "+dateFormater.format(new Date()));
			
			String timeStrNoTimeZone = dateStr+" "+timeStr;
			
			TimeZone defaultTz = TimeZone.getDefault();
			boolean serverTimeWithinDST = defaultTz.inDaylightTime(new Date());
			
			String tzCode = defaultTz.getDisplayName(serverTimeWithinDST,TimeZone.SHORT);
			
			String s1 = timeStrNoTimeZone+" "+tzCode;
			dateFormater.setTimeZone(defaultTz);
			Date d1 = dateFormater.parse(s1);
			
//			System.out.println("The date we have for server's timezone:"+s1);
			
			String finalTimeZoneCode = this.getShortTimeZoneCode(dateFormater,d1,timeZoneStr);
			String s2 = timeStrNoTimeZone+" "+finalTimeZoneCode;
//			System.out.println("The date we have for conference timezone:"+s2);
			
			dt = dateFormater.parse(s2);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	dt;
	}
	public	Date	getCurrentTimeInTimeZone(String timeZoneStr)
	{
//		System.out.println("IN: getCurrentTimeInTimeZone");
		Date dt = null;
		try
		{
			TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
//			System.out.println("Time Zone:"+tz.getDisplayName());
			Calendar gc = GregorianCalendar.getInstance(tz);
			
			dt = gc.getTime();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			dt = new Date();
		}
		return	dt;
	}
	public	String	getShortTimeZoneCode(Date dt, String timeZoneStr)
	{
		String	tzCode = "GMT";
		try
		{
//			System.out.println("Looing for timezone: "+timeZoneStr); 
			TimeZone confTz = TimeZone.getTimeZone(timeZoneStr);
			if (confTz != null)
			{
				boolean	zoneSupportsDST = confTz.useDaylightTime();
				if (!zoneSupportsDST)
				{
//					System.out.println("Conference time zone does not use dst");
					tzCode = confTz.getDisplayName(false,TimeZone.SHORT);
				}
				else
				{
//					System.out.println("Conference time zone uses dst");
					boolean confTimeWithinDST = confTz.inDaylightTime(dt);
					tzCode = confTz.getDisplayName(confTimeWithinDST,TimeZone.SHORT);
				}
			}
			else
			{
//				System.out.println("The timezone string seems invalid:"+timeZoneStr);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	tzCode;
	}
	public	String	getShortTimeZoneCode(Date dt, TimeZone timeZone)
	{
		String	tzCode = "GMT";
		try
		{
//			System.out.println("Looing for timezone: "+timeZoneStr); 
			if (timeZone != null)
			{
				boolean	zoneSupportsDST = timeZone.useDaylightTime();
				if (!zoneSupportsDST)
				{
//					System.out.println("Conference time zone does not use dst");
					tzCode = timeZone.getDisplayName(false,TimeZone.SHORT);
				}
				else
				{
//					System.out.println("Conference time zone uses dst");
					boolean confTimeWithinDST = timeZone.inDaylightTime(dt);
					tzCode = timeZone.getDisplayName(confTimeWithinDST,TimeZone.SHORT);
				}
			}
			else
			{
//				System.out.println("The timezone string seems invalid:"+timeZoneStr);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	tzCode;
	}
	public	String	getShortTimeZoneCode(DateFormat df, Date dt, String timeZoneStr)
	{
		String	tzCode = "GMT";
		try
		{
//			System.out.println("Looing for timezone: "+timeZoneStr); 
			TimeZone confTz = TimeZone.getTimeZone(timeZoneStr);
			if (confTz != null)
			{
				df.setTimeZone(confTz);
				boolean	zoneSupportsDST = confTz.useDaylightTime();
				if (!zoneSupportsDST)
				{
//					System.out.println("Conference time zone does not use dst");
					tzCode = confTz.getDisplayName(false,TimeZone.SHORT);
				}
				else
				{
//					System.out.println("Conference time zone uses dst");
					boolean confTimeWithinDST = confTz.inDaylightTime(dt);
					tzCode = confTz.getDisplayName(confTimeWithinDST,TimeZone.SHORT);
				}
			}
			else
			{
//				System.out.println("The timezone string seems invalid:"+timeZoneStr);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	tzCode;
	}
	public	static	void	main(String[] args)
	{
		try
		{
			TimeZone tz1 = TimeZone.getTimeZone("Asia/Calcutta");
			TimeZone tzd = TimeZone.getDefault();
			
			DateConvertor dc = new DateConvertor();
			Date dt = dc.getCurrentTimeInTimeZone("Asia/Calcutta");
			
			DateFormat dateFormater = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,Locale.US);
			System.out.println("dateFormater.format(dt): "+dateFormater.format(dt));
			
			dateFormater.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
			System.out.println("dateFormater.format(dt) after setTimeZone('Asia/Calcutta'): "+dateFormater.format(dt));
			
			dt = dc.readDateString("June 16, 2007","09:00:00 AM","Asia/Calcutta");
			System.out.println("dateFormater.format(dt): "+dateFormater.format(dt));
			
//			Date dt2 = TriggerUtils.translateTime(dt,tz1,tzd);
//			dateFormater.setTimeZone(tzd);
//			System.out.println("dateFormater.format(dt): "+dateFormater.format(dt2));
			
			Date dt3 = dc.getTimeInTimeZone(dt,tzd);
			dateFormater.setTimeZone(tzd);
			System.out.println("dateFormater.format(dt): "+dateFormater.format(dt3));
			
			System.out.println("*************************");
			Date dt4 = dc.readDateString("June 16, 2007","09","00","AM","Asia/Calcutta");
			System.out.println("dateFormater.format(dt): "+dateFormater.format(dt4));
			
			Date dt6 = dc.getTimeInServerTimeZone(dt4);
			System.out.println("dateFormater.format(dt): "+dateFormater.format(dt6));
			System.out.println("*************************");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
