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

import	java.util.HashMap;
import	java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class JSONEventsReader	//extends	JSONurlReader
{
	protected	static	String	getNextEventAction = null;
	protected	static	String	getEventsAction = null;
//	protected	static	final	String	getNextTestEventAction = "GetNextTestEvent.action";
	
	protected	static	JSONEventsReader	theEventsReader	=	null;
	
	public	static	void	setGetNextEventAction(String url)
	{
		JSONEventsReader.getNextEventAction = url;
//		Window.alert("Setting up the get events action:"+url);
	}
	
	public	static	void	setGetEventsAction(String url)
	{
		JSONEventsReader.getEventsAction = url;
//		Window.alert("Setting up the get events action:"+url);
	}
	/*
	private class JSONEventsReaderResponseCallback implements JSONurlReaderCallback
	{
		protected	HashMap		listeners;
		
		public	JSONEventsReaderResponseCallback(HashMap map)
		{
			this.listeners = map;
		}
		public	void	urlReadingComplete(UIServerResponse response)
		{
			if (response != null && response.isSuccess() && response.hasData())
			{
				ArrayList ary = response.getArrayList();
				Object data = response.getData();
//				Window.alert("JSONEventsReader::urlReadingComplete:"+ary);
				if (ary != null)
				{
					int size = ary.size();
//					Window.alert("Number of events:"+size);
					for (int i=0; i<size; i++)
					{
						UIServerResponse event = (UIServerResponse)ary.get(i);
						//Window.alert("Event:"+event);
						//Window.alert("JSONEventsReader::urlReadingComplete:"+event);
						if (event != null && event.isSuccess() && event.hasData())
						{
							UIEventListener listener = (UIEventListener)this.listeners.get(event.getFeatureId());
							if (listener != null)
							{
								listener.onEvent(event.getEventId(),event.getAvailableData());
							}
						}
					}
				}
				else if (data != null)
				{
					UIServerResponse event = (UIServerResponse)data;
					if (event != null && event.isSuccess() && event.hasData())
					{
						UIEventListener listener = (UIEventListener)this.listeners.get(event.getFeatureId());
						if (listener != null)
						{
							listener.onEvent(event.getEventId(),event.getAvailableData());
						}
					}
				}
//				/**
//				 * Single event block
//				Window.alert("Received event:"+response.getAvailableData().toString());
//				UIEventListener listener = (UIEventListener)this.listeners.get(response.getFeatureId());
//				if (listener != null)
//				{
//					listener.onEvent(response.getEventId(),response.getAvailableData());
//				}
//				else
//				{
//					Window.alert("No listener for event:"+response.getAvailableData().toString());
//				}
//				*
			}
//			else
//			{
//				this.listener.onEvent(null,null);
//			}
		}
	}
	*/
	/*
	public	static	void	startEventListener()
	{
		if (JSONEventsReader.theEventsReader != null)
		{
			JSONEventsReader.theEventsReader.start();
		}
		else
		{
			Window.alert("Events reader has not been initialized");
		}
	}
	*/
//	public	static	void	removeFeatureListener(String featureId)
//	{
//		if (JSONEventsReader.theEventsReader == null)
//		{
//			JSONEventsReader.theEventsReader.getFeatureListeners().remove(featureId);
//		}
//	}
//	public	static	JSONEventsReader	addFeatureListener(String featureId, int timeMillis,
//			UIEventListener listener)
//	{
//		return	JSONEventsReader.addFeatureListener("./",featureId,timeMillis,listener);
//	}
	/*
	protected	static	JSONEventsReader	addFeatureListener(String urlRoot,String featureId,
			int timeMillis, UIEventListener listener)
	{
//		Window.alert("Creating events reader:"+featureId+" at:"+timeMillis);
//		String url = GWT.getModuleBaseURL()+JSONEventsReader.getEventsAction+"?";
		if (JSONEventsReader.theEventsReader == null && JSONEventsReader.getEventsAction != null)
		{
//			Window.alert("Creating the events reader");
			
//			JSONEventsReader.theEventsReader =
//				new JSONEventsReader(JSONEventsReader.getNextEventAction,timeMillis);
			JSONEventsReader.theEventsReader =
				new JSONEventsReader(JSONEventsReader.getEventsAction,timeMillis);
			
//			JSONEventsReader.theEventsReader.start();
		}
		else if (JSONEventsReader.theEventsReader == null)
		{
//			Window.alert("Unable to create the events reader");
		}
		if (featureId != null && JSONEventsReader.theEventsReader != null)
		{
//			JSONEventsReader.theEventsReader.getFeatureListeners().put(featureId,listener);
//			Window.alert("Added listener for feature:"+featureId);
		}
		else
		{
//			Window.alert("Unable to add feature listener:"+featureId);
		}
		return	JSONEventsReader.theEventsReader;
	}
	*/
//	protected	String	featureId;
	protected	int		timeMillis;
	protected	HashMap	featureListeners;
//	protected	String	eventId;
//	protected	String	listener;
	protected	Timer	timer;
	protected	JSONurlReader	urlReader;
	protected	JSONurlReaderCallback	callback;
//	protected	GetEventResponseTextHandler	textHandler;
	
	private	JSONEventsReader(String url, int pollInterval, int maxFailures)
	{
		this.featureListeners = new HashMap();
		
		this.timeMillis = pollInterval;
		if (this.timeMillis < 1000)
		{
			this.timeMillis = 1000;
		}
		if (this.timeMillis > 2000)
		{
			this.timeMillis = 2000;
		}
		
//		this.callback = new GetEventResponseJsonHandler(this.featureListeners);
//		GetEventResponseTextHandler handler = new GetEventResponseTextHandler(this.callback,
//				this.timeMillis, maxFailures);
//		urlReader = new JSONurlReader(url,handler);
	}
	
	/**
	 * This method triggers the reader. It also starts a timer that will
	 * repeast the reading every given amount of time.
	 */
//	private	boolean	firstEvent = true;
	public	void	start()
	{
//		super.doReadURL();
//		
		timer = new Timer()
		{
			public void run()
			{
//				if (!isBusy())
//				{
//				Window.alert("Calling event poll: ");
					urlReader.doReadURL();
					/*
					if (firstEvent) doReadURL();
					else
					{
						UIServerResponse resp = new UIServerResponse();
						
							resp.isSuccess();
							resp.setFeatureId("feature.chat");
							resp.setEventId("chat.message");
							UIChatEntry msg = new UIChatEntry();
							msg.setChatId("none");
							msg.setMessageText("Hi hi hi");
							msg.setPrivateMessage("false");
							msg.setSenderId("Jack&abc.com");
							msg.setSystemMessage("false");
							
						callback.urlReadingComplete(resp);
					}
					firstEvent = false;
					*/
//				}
//				else
//				{
//					Window.alert("The http coneection busy");
//				}
//				this.schedule(timeMillis);
			}
		};
//		timer.schedule(timeMillis);
		timer.scheduleRepeating((int)timeMillis);
	}
	public	void	stop()
	{
		timer.cancel();
	}
//	public HashMap getFeatureListeners()
//	{
//		return this.featureListeners;
//	}
}
