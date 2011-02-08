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

package com.dimdim.conference.ui.common.client.util;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;

/**
 * This widget requires the page to provide elapsed_time_millis variable which
 * must give the time so far passed in the meeting. This will enable all the
 * attendees to see the same elapsed time.
 */
public class RunningClockWidget extends Composite
{
	private	long	startTime = System.currentTimeMillis();
	private	int	sec = 0;
	private	int	min = 0;
	private	int	hr = 0;
	private	Timer	timer;
	private	HTML confTime;
	
	public RunningClockWidget()
	{
		this.confTime = new HTML();
		this.confTime.setStyleName("running-clock");
		this.confTime.setWordWrap(false);
		initWidget(this.confTime);
		try
		{
			String s = getElapsedTimeSoFar();
			int l = (new Integer(s)).intValue();
			this.startTime -= l;
		}
		catch(Exception e)
		{
			
		}
		this.startConfClock();
	}
	private	void	setTime()
	{
		long totalSec = (System.currentTimeMillis()-this.startTime)/1000;
		this.sec = (int)(totalSec % 60);
		int totalMin = (int)(totalSec / 60);
		this.min = totalMin % 60;
		this.hr = totalMin / 60;
		
		String str = "<b>"+printInt(this.hr)+" : "+printInt(this.min)+" : "+printInt(this.sec)+"</b>";
		this.confTime.setHTML(str);
	}
	private	String	printInt(int i)
	{
		String s = Integer.toString(i);
		if (s.length() == 1)
		{
			return	"0"+s;
		}
		else
		{
			return	s;
		}
	}
	public	void	startConfClock()
	{
		timer = new Timer()
		{
			public void run()
			{
				setTime();
			}
		};
		timer.scheduleRepeating(1000);
	}
	private String getElapsedTimeSoFar()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("elapsed_time_millis");
	}
}

