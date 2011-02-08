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

package com.dimdim.conference.ui.model.client;

import java.util.HashMap;

import	com.google.gwt.json.client.JSONParser;
import	com.google.gwt.json.client.JSONObject;
import	com.google.gwt.json.client.JSONValue;
//import com.dimdim.conference.ui.json.client.JSONParser;
//import com.dimdim.conference.ui.json.client.JSONObject;
import com.dimdim.conference.ui.json.client.ResponseAndEventReader;
//import com.dimdim.conference.ui.json.client.JSONValue;
import com.dimdim.conference.ui.json.client.UIPopoutPanelData;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This reader is a simple timer that waits on the popout callback queue.
 * Panels and widgets from a popped out window, need to communicate back
 * with the console. At present there is only 1 popout window expected,
 * however in near future there could be more.
 * 
 * This reader is used only on the console side. Each created popout window
 * proxy is given to this reader. Each data block posted by a widget from
 * a popout window must identify the window and panel within the window.
 * The reader takes the object and simply forwards it to the window proxy
 * to be further forwarded to the panel itself.
 * 
 * The message itself could be anything that is understood by the window
 * proxy itself or the individual panel.
 * 
 * 05/05/07 - This timer based reading is now switched to more direct calls
 * between javascript functions and gwt code. This is much easier and neater.
 * The message and reply pattern remains in effect because it must to account
 * for timings. Either windows can not send messages to each other till the
 * receiver is reasonably redy and free to be able to receive it.
 * 
 * This object listens for messages from the popout on listener id '
 */

public class PopoutCallbackReader implements JSCallbackListener
{
	protected	static	PopoutCallbackReader	theReader;
	
	public	static	PopoutCallbackReader	getReader()
	{
		if (PopoutCallbackReader.theReader == null)
		{
			PopoutCallbackReader.theReader = new PopoutCallbackReader(100);
		}
		return	PopoutCallbackReader.theReader;
	}
	
//	protected	int		interval;
//	protected	Timer		timer;
	protected	HashMap		popoutWindowProxies;
	protected	ResponseAndEventReader	jsonReader;
	
	private	PopoutCallbackReader(int interval)
	{
//		this.interval = interval;
		this.popoutWindowProxies = new HashMap();
		this.jsonReader = new ResponseAndEventReader();
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
//						readDataText(dataText);
//					}
//				}
//				catch(Exception e)
//				{
//				}
//			}
//		};
	}
	/**
	 * Convert the text into 
	 * @param dataText
	 */
	private	final	void	readDataText(String dataText)
	{
		try
		{
//				Window.alert("-"+dataText+"-");
				JSONValue jsonObject = JSONParser.parse(dataText);
				if (jsonObject != null)
				{
//					Window.alert("-"+jsonObject+"-");
					JSONObject jObj = jsonObject.isObject();
					if (jObj != null)
					{
						UIPopoutPanelData ppd = (UIPopoutPanelData)jsonReader.readObject(jObj);
						if (ppd != null)
						{
//							Window.alert(ppd.toString());
							PopoutWindowProxy pwp = (PopoutWindowProxy)this.popoutWindowProxies.get(ppd.getWindowId());
							if (pwp != null)
							{
								if (ppd.getDataText().equals("POPOUT_CLOSED"))
								{
									pwp.popoutWindowClosed();
								}
								else if (ppd.getDataText().equals("POPOUT_LOADED"))
								{
									pwp.popoutWindowLoaded();
								}
								else if (ppd.getDataText().equals("POPOUT_DATA_RECEIVED"))
								{
									pwp.popoutDataTransferReceived();
								}
								else
								{
									pwp.receivePanelDataFromPopout(ppd);
								}
							}
							else
							{
//								Window.alert("No popout window proxy available for:"+ppd.getWindowId());
							}
						}
						else
						{
//							Window.alert("No callback available to pass on the data:");
						}
					}
				}
				else
				{
//					Window.alert("*** JSONParser.parse returned null");
				}
		}
		catch(Exception e)
		{
//			Window.alert(e.getMessage());
		}
		return;
	}
	public	void	addPopoutWindowProxy(PopoutWindowProxy popoutWindowProxy)
	{
		this.popoutWindowProxies.put(popoutWindowProxy.getWindowId(),popoutWindowProxy);
//		if (this.popoutWindowProxies.size() == 1)
//		{
//			start();
//		}
	}
	public	void	removePopoutWindowProxy(PopoutWindowProxy popoutWindowProxy)
	{
		this.popoutWindowProxies.remove(popoutWindowProxy.getWindowId());
//		if (this.popoutWindowProxies.size() == 0)
//		{
//			stop();
//		}
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
//		return	$wnd.dequeueNextCallbackDataText();
//	}-*/;
	public String getListenerName()
	{
		return	"POPOUT_PARENT";
	}
	public void handleCallFromJS(String data)
	{
		this.readDataText(data);
	}
	public void handleCallFromJS2(String data1, String data2)
	{
	}
	public void handleCallFromJS3(String data1, String data2, String data3)
	{
	}
}
