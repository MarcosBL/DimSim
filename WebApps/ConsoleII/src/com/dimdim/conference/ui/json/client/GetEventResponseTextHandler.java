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

//import java.util.ArrayList;
//import java.util.HashMap;

//import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

//import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.ClickListener;
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.Tree;
//import com.google.gwt.user.client.ui.TreeItem;
//import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Timer;

public	class	GetEventResponseTextHandler	implements	ResponseTextHandler, ServerAvailabilityListener
{
//	protected	UIServerResponse		ret;
	protected	JSONServerStatusListener	serverStatusListener;
	protected	JSONurlReaderCallback	callback;
	protected	ResponseAndEventReader	jsonReader;
	
	protected	int		eventPollInteval;
	protected	int		maxEventFailures;
	
	protected	Timer		eventsReadingTimer;
	protected	boolean		timerStopped = false;
	protected	String		serverPingURL;
	protected	ServerAvailabilityChecker	serverAvailabilityChecker;
	
	protected	int		connectFailuresBlock = 0;
	
	protected	String	maxConnectFailuresComment = "Maximum reconnect attempt failures reached.";
	protected	String	serverUnreachableComment = "Server is not reachable.";
	protected	String	webappUnreachableComment = "Server is reachable. Dimdim web application not responsive.";
	
	public	GetEventResponseTextHandler(JSONurlReaderCallback callback,
			int pollInterval, int maxFailures)
	{
		this.callback = callback;
		this.eventPollInteval = pollInterval;
		this.maxEventFailures = maxFailures;
		this.jsonReader = new ResponseAndEventReader();
	}
	public	void	onCompletion(String responseText)
	{
//		Window.alert("GetEventResponseTextHandler:onCompletion: "+responseText);
//		ret = null;
//		String debugText = responseText;//"{type:\"event\",feature:\"feature.roster\",id:\"participant.joined\",dataType:\"object\",data:{objClass:\"RosterEntry\",userId:\"email_29321385_2\",displayName:\"John Smith\",presence:\"inmeeting\",mood:\"normal\",role:\"role.attendee\"}}";
//		String debugText = "{type:\"event\",feature:\"feature.roster\",id:\"participant.joined\",dataType:\"object\",data:{objClass:\"RosterEntry\"}}";
		try
		{
			if (responseText == null || responseText.length() == 0)
			{
				JSONurlReader.errorCount++;
			}
			else
			{
				if (responseText.startsWith("[") && responseText.endsWith("]"))
				{
					//	Then we have our buffer. This nominal check need to expand to
					//	a full crc check.
					if (responseText.length() > 10)
					{
						//Window.alert("-"+responseText+"-");
						JSONValue jsonObject = JSONParser.parse(responseText);
						if (jsonObject != null)
						{
							//Window.alert("-"+jsonObject+"-");
							UIServerResponse ret = jsonReader.readGetEventsResponse(jsonObject);
							//Window.alert("-"+ret+"-");
							if (callback != null && ret != null)
							{
								ret.setEventText(responseText);
								callback.urlReadingComplete(ret);
							}
							else
							{
								//Window.alert("No callback available to pass on the data:"+ret.toString());
							}
						}
						else
						{
							//Window.alert("*** JSONParser.parse returned null");
						}
					}
					JSONurlReader.errorCount = 0;
					connectFailuresBlock = 0;
				}
				else
				{
					//	We have an unexpected bad return. Like a bad proxy inbetween.
					JSONurlReader.errorCount++;
				}
			}
		}
		catch (Exception e)
		{
			JSONurlReader.errorCount++;
//			Window.alert(e.toString()+":"+responseText);
//			ret = new UIServerResponse();
//			ret.setSuccess(false);
		}
		
		/**
		 * Analyse the server availability based on the number of consequetive
		 * failures. Every 5 event polls, use a direct server ping url that does nothing
		 * except return a fixed length buffer at all times. This is to check if the
		 * server is available when the dimdim webapp seems to be having trouble.
		 */
		if (JSONurlReader.errorCount > 0)
		{
//			Window.alert("Client has lost the connection to server");
			if (this.serverPingURL != null)
			{
				if (JSONurlReader.errorCount%5 == 0)
				{
					this.connectFailuresBlock++;
					this.serverAvailabilityChecker = new ServerAvailabilityChecker(this.serverPingURL,
							1,this.eventPollInteval+(this.connectFailuresBlock*200),this);
					this.serverAvailabilityChecker.start();
				}
				else
				{
					scheduleNextPoll();
				}
			}
			else if (JSONurlReader.errorCount > this.maxEventFailures)
			{
				declareServerConnectionLost(maxConnectFailuresComment);
			}
			else
			{
				scheduleNextPoll();
			}
		}
		else
		{
			scheduleNextPoll();
		}
	}
	private	void	declareServerConnectionLost(String message)
	{
		if (this.serverStatusListener != null)
		{
			this.serverStatusListener.serverConnectionLost(message);
		}
		if (callback != null)
		{
			callback.serverConnectionLost(message);
		}
	}
	private	void	scheduleNextPoll()
	{
		if (this.eventsReadingTimer != null && !this.timerStopped)
		{
			this.eventsReadingTimer.schedule(eventPollInteval);
		}
	}
	public void	setTimerStopped()
	{
		this.timerStopped = true;
		if (this.callback != null)
		{
			this.callback.eventTimerStopped();
		}
	}
	public void	setTimerRestarted()
	{
		this.timerStopped = false;
		if (this.callback != null)
		{
			this.callback.eventTimerRestarted();
		}
	}
	public JSONServerStatusListener getServerStatusListener()
	{
		return serverStatusListener;
	}
	public void setServerStatusListener(
			JSONServerStatusListener serverStatusListener)
	{
		this.serverStatusListener = serverStatusListener;
	}
	public Timer getEventsReadingTimer()
	{
		return eventsReadingTimer;
	}
	public void setEventsReadingTimer(Timer eventsReadingTimer)
	{
		this.eventsReadingTimer = eventsReadingTimer;
	}
	public String getServerPingURL()
	{
		return serverPingURL;
	}
	public void setServerPingURL(String serverPingURL)
	{
		this.serverPingURL = serverPingURL;
	}
	public void setMaxConnectFailuresComment(String maxConnectFailuresComment)
	{
		this.maxConnectFailuresComment = maxConnectFailuresComment;
	}
	public void setServerUnreachableComment(String serverUnreachableComment)
	{
		this.serverUnreachableComment = serverUnreachableComment;
	}
	public void setWebappUnreachableComment(String webappUnreachableComment)
	{
		this.webappUnreachableComment = webappUnreachableComment;
	}
	public void serverAvailable()
	{
//		Window.alert("Server Ping Succeeded. round:"+this.connectFailuresBlock);
		if (this.connectFailuresBlock > 3)
		{
			//	The server seems available but the dimdim web application is not.
			this.declareServerConnectionLost(webappUnreachableComment);
		}
		else
		{
			if (this.eventsReadingTimer != null)
			{
				this.eventsReadingTimer.schedule(eventPollInteval+(this.connectFailuresBlock*200));
			}
		}
	}
	public void serverUnavailable()
	{
//		Window.alert("Server Ping Failed. round:"+this.connectFailuresBlock);
		if (this.connectFailuresBlock > 3)
		{
			this.declareServerConnectionLost(serverUnreachableComment);
		}
		else
		{
			//	Continue with the polling.
			if (this.eventsReadingTimer != null)
			{
				this.eventsReadingTimer.schedule(eventPollInteval+(this.connectFailuresBlock*200));
			}
		}
	}
}
