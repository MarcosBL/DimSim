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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.dimdim.conference.ui.json.client.JSONurlReaderCallback;
import com.dimdim.conference.ui.json.client.UIServerResponse;
import com.dimdim.conference.ui.json.client.UIEventListener;

/**
 * @author Jayant Pandit
 * 
 * The events reader always expects an array of events, if there are any
 * events to read. This events handler is used directly by event consumers
 * local to the current window. This handler can forward the events to other
 * event consumers. These could be any other objects. At present there is
 * only one type of such consumer, the poped out windows which will contain
 * panels that will consume events for one or more features. One remote
 * possibility is a client side event recorder, which will receive events
 * and record then in some way locally on the client.
 */

public	class	EventsJsonHandler	implements	JSONurlReaderCallback
{
	protected	static	EventsJsonHandler	theHandler;
	
	public	static	EventsJsonHandler	getHandler()
	{
		if (EventsJsonHandler.theHandler == null)
		{
			EventsJsonHandler.theHandler = new EventsJsonHandler();
		}
		return	EventsJsonHandler.theHandler;
	}
	
	protected	HashMap		listeners;
	protected	Vector		eventProxies;
	protected	EventsTracker	eventsTracker;
	protected	boolean			timerStopped = false;
	
	private	EventsJsonHandler()
	{
		this.listeners = new HashMap();
		this.eventProxies = new Vector();
	}
	public EventsTracker getEventsTracker()
	{
		return eventsTracker;
	}
	public void setEventsTracker(EventsTracker eventsTracker)
	{
		this.eventsTracker = eventsTracker;
	}
	public	void	addEventProxy(EventsJsonProxy eventProxy)
	{
		if (!this.eventProxies.contains(eventProxy))
		{
			this.eventProxies.addElement(eventProxy);
		}
	}
	public	void	removeEventProxy(EventsJsonProxy eventProxy)
	{
		this.eventProxies.remove(eventProxy);
	}
	public	void	addFeatureListener(String featureId, UIEventListener listener)
	{
		this.listeners.put(featureId,listener);
	}
	public	void	removeFeatureListener(String featureId)
	{
		this.listeners.remove(featureId);
	}
	public	void	urlReadingComplete(UIServerResponse response)
	{
		if (response != null && response.isSuccess() && response.hasData())
		{
			ArrayList ary = response.getArrayList();
			//Window.alert("DimDimEventsJsonHandler::urlReadingComplete:"+ary);
			if (ary != null)
			{
				int proxyCount = this.eventProxies.size();
				for (int i=0; i<proxyCount; i++)
				{
					((EventsJsonProxy)this.eventProxies.elementAt(i)).receiveEvent(response);
				}
				
				int size = ary.size();
				//Window.alert("Number of events:"+size);
				for (int i=0; i<size; i++)
				{
					UIServerResponse event = (UIServerResponse)ary.get(i);
					if (event != null && this.eventsTracker != null)
					{
						this.eventsTracker.onEvent(event);
					}
					//Window.alert("DimDimEventsJsonHandler::urlReadingComplete:"+event.toString());
					if (event != null && event.isSuccess() && event.hasData())
					{
						UIEventListener listener = (UIEventListener)this.listeners.get(event.getFeatureId());
						if (listener != null)
						{
							try
							{
								if (!this.timerStopped)
								{
									listener.onEvent(event.getEventId(),event.getAvailableData());
								}
							}
							catch(Exception e)
							{
								if (eventsTracker != null)
								{
									eventsTracker.addDebugMessage("EventsJsonHandler:error:"+e.getMessage());
								}
							}
						}
						else
						{
							if (eventsTracker != null)
							{
								eventsTracker.addDebugMessage("EventsJsonHandler:error:No handler");
							}
						}
					}
					else
					{
						if (eventsTracker != null)
						{
							eventsTracker.addDebugMessage("EventsJsonHandler:error in reading event");
						}
					}
				}
			}
		}
//		else
//		{
//			//Window.alert("Empty or null response from GetEvents");
//		}
	}
	public	void	eventTimerStopped()
	{
		this.timerStopped = true;
	}
	public	void	eventTimerRestarted()
	{
		this.timerStopped = false;
	}
	/**
	 * This is a special callback method which is interpreted on the client. This
	 * is not an event data received by the event poller. If the client is not able
	 * get to server 30 times, i.e. for 30 seconds, same timeout period as the server
	 * side session, then this event is raised. The client will so inform the user
	 * and close the console.
	 */
	public	void	serverConnectionLost(String message)
	{
		UIEventListener listener = (UIEventListener)this.listeners.get("feature.conf");
		if (listener != null)
		{
			listener.onEvent(ClientStateModel.SERVER_CONNECTION_LOST,message);
		}
	}
}
