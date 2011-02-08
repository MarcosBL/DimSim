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

package com.dimdim.conference.ui.json.client;

//import com.google.gwt.user.client.ResponseTextHandler;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.ClickListener;
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.Tree;
//import com.google.gwt.user.client.ui.TreeItem;
//import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Timer;

public class JSONurlReadingTimer	implements	JSONServerStatusListener
{
	protected	GetEventResponseTextHandler	responsehandler;
	protected	JSONurlReader	urlReader;
	protected	int		interval;
	protected	Timer	timer;
	
	public	JSONurlReadingTimer(String url, String confKey,
				int interval, GetEventResponseTextHandler responseHandler)
	{
		this.urlReader = new JSONurlReader(url, confKey, responseHandler);
		this.interval = interval;
		this.responsehandler = responseHandler;
		this.timer = new Timer()
		{
			public	void	run()
			{
				try
				{
//					Window.alert("JSONurlReadingTimer:timer - Calling Event Poll");
					urlReader.doReadURL();
				}
				catch(Exception e)
				{
					
				}
			}
		};
		responseHandler.setEventsReadingTimer(this.timer);
	}
	public	void	reStart()
	{
		JSONurlReader.errorCount = 0;
//		this.timer.scheduleRepeating(this.interval);
		this.responsehandler.setTimerRestarted();
		this.timer.schedule(this.interval);
	}
	public	void	start()
	{
//		this.timer.scheduleRepeating(this.interval);
		this.timer.schedule(this.interval);
	}
	public	void	stop()
	{
		this.timer.cancel();
		this.responsehandler.setTimerStopped();
	}
	
	public	void	serverConnectionLost(String message)
	{
		this.stop();
	}
}
