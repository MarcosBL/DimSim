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

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import com.dimdim.conference.ui.model.client.JSInterface;
import com.dimdim.conference.ui.model.client.JSCallbackListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This data reader reads the initial data for the popout window. This
 * initial data refers to current state of the panels that are moved
 * from console to popout and any history that may be associated with
 * them. Same applies also to the feature model objects maintained by
 * the client model.
 * 
 * The data reader always knows exactly the data is expected to read,
 * because the serialized data strings can only be used by its specific
 * target. First entry in the data queue always tells how many further
 * entries are in the queue. This reader reads those many entries and
 * populates the respective objects.
 * 
 * 05/05/07 - The communication between the console and popout has now
 * moved from timers to jsinterface.
 */

public class PopoutPageDataReader implements JSCallbackListener
{
//	protected	int		interval;
//	protected	Timer	timer;
	
	protected	PopoutPageDataTextHandler	dataTextHandler;
	
	public	PopoutPageDataReader(int interval, PopoutPageDataTextHandler dataTextHandler)
	{
//		this.interval = interval;
		this.dataTextHandler = dataTextHandler;
		JSInterface.addCallbackListener(this);
//		this.timer = new Timer()
//		{
//			public	void	run()
//			{
//				try
//				{
//					Window.alert("JSONurlReadingTimer:timer - Calling Event Poll");
//					String dataText = getNextDataText();
//					if (dataText != null)
//					{
//						Window.alert(dataText);
//						if (readDataText(dataText) == 1)
//						{
//							stop();
//						}
//					}
//				}
//				catch(Exception e)
//				{
//				}
//			}
//		};
	}
	protected	final	int	readDataText(String dataText)
	{
		if (dataTextHandler != null)
		{
			return dataTextHandler.onCompletion(dataText);
		}
		return	1;
	}
//	public	void	start()
//	{
//		this.timer.scheduleRepeating(this.interval);
//	}
//	public	void	stop()
//	{
//		Window.alert("Data reading complete. Stopping the data timer");
//		this.timer.cancel();
//	}
//	private	native String	getNextDataText() /*-{
//		return	$wnd.dequeueNextDataText();
//	}-*/;
	public String getListenerName()
	{
		return "POPOUT_DATA";
	}
	public void handleCallFromJS(String data)
	{
//		Window.alert("Handling data in popout:"+data);
		readDataText(data);
	}
	public void handleCallFromJS2(String data1, String data2)
	{
	}
	public void handleCallFromJS3(String data1, String data2, String data3)
	{
	}
}
