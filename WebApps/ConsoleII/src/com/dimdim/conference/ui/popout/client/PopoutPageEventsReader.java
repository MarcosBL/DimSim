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

package com.dimdim.conference.ui.popout.client;

import com.dimdim.conference.ui.json.client.UIServerResponse;
import com.google.gwt.user.client.Timer;

import com.dimdim.conference.ui.model.client.JSInterface;
import com.dimdim.conference.ui.model.client.JSCallbackListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class reads the events from the events queue. This object is the
 * equivalent of the event poller for the main console. The events read
 * by the console's event poller are received into the events queue through
 * the events proxy for the popout window.
 * 
 * Each popout window maintains a single events reader, which uses the same
 * basic local events handler as the main console.
 */

public class PopoutPageEventsReader implements JSCallbackListener
{
//	protected	int		interval;
//	protected	Timer	timer;
	protected	PopoutPageEventTextHandler	eventTextHandler;
	
	public	PopoutPageEventsReader(int interval, PopoutPageEventTextHandler eventTextHandler)
	{
//		this.interval = interval;
		this.eventTextHandler = eventTextHandler;
		JSInterface.addCallbackListener(this);
//		this.timer = new Timer()
//		{
//			public	void	run()
//			{
//				try
//				{
//					Window.alert("JSONurlReadingTimer:timer - Calling Event Poll");
//					String eventText = getNextEventText();
//					if (eventText != null)
//					{
//						readEventText(eventText);
//					}
//				}
//				catch(Exception e)
//				{
//				}
//			}
//		};
	}
	protected	final	void	readEventText(String eventText)
	{
		if (eventTextHandler != null)
		{
			eventTextHandler.onCompletion(eventText);
		}
	}
//	public	void	start()
//	{
//		this.timer.scheduleRepeating(this.interval);
//	}
//	public	void	stop()
//	{
//		this.timer.cancel();
//	}
//	private	native UIServerResponse	getNextEvent() /*-{
//		return	$wnd.dequeueNextEvent();
//	}-*/;
//	private	native String	getNextEventText() /*-{
//		return	$wnd.dequeueNextEventText();
//	}-*/;
	public String getListenerName()
	{
		return	"POPOUT_EVENT";
	}
	public void handleCallFromJS(String data)
	{
		readEventText(data);
	}
	public void handleCallFromJS2(String data1, String data2)
	{
	}
	public void handleCallFromJS3(String data1, String data2, String data3)
	{
	}
}
