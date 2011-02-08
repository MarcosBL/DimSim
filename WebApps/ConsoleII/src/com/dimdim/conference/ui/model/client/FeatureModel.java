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

import	java.util.ArrayList;
import	java.util.HashMap;

import	com.google.gwt.json.client.JSONParser;
import	com.google.gwt.json.client.JSONValue;
import	com.google.gwt.json.client.JSONArray;
import com.dimdim.conference.ui.json.client.UIObject;
//import com.dimdim.conference.ui.json.client.JSONParser;
//import com.dimdim.conference.ui.json.client.JSONValue;
//import com.dimdim.conference.ui.json.client.JSONArray;
import com.dimdim.conference.ui.json.client.JSONurlReader;
import com.dimdim.conference.ui.json.client.ResponseAndEventReader;
import com.dimdim.conference.ui.json.client.UIEventListener;
//import com.dimdim.conference.ui.json.client.JSONEventsReader;
//import com.dimdim.conference.ui.json.client.JSONurlReaderCallback;
import com.dimdim.conference.ui.json.client.UIServerResponse;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ResponseTextHandler;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class FeatureModel	implements	UIEventListener, ResponseTextHandler
{
	protected	String		featureId;
	protected	ArrayList	objects = new ArrayList();
	protected	HashMap		newListeners = null;
	protected	HashMap		listeners = new HashMap();
	protected	CommandURLFactory		commandsFactory = new CommandURLFactory();
	
	protected	ResponseAndEventReader	jsonReader;
	protected	String		lastCommandUrl = null;
	protected	UIServerResponse	lastCommandResponse = null;
	protected	CommandExecListener		commandExecListener;
	
	protected	String	webappRoot = null;
	protected	String	sessionKeyParam = null;
	
	/**
	 * This parameter is significant only if the feature supports the
	 * concept of a current object. Each feature may have a different
	 * meaning for the current object.
	 */
	protected	Object		currentObject = null;
	
	public	FeatureModel(String featureId)
	{
		this.featureId = featureId;
		this.jsonReader = new ResponseAndEventReader();
		this.webappRoot = ConferenceGlobals.webappRoot;
		this.sessionKeyParam = "sessionKey="+ConferenceGlobals.sessionKey;
		startEventListener();
	}
	public CommandExecListener getCommandExecListener()
	{
		return commandExecListener;
	}
	public void setCommandExecListener(CommandExecListener commandExecListener)
	{
		this.commandExecListener = commandExecListener;
	}
	public	void	addListener(FeatureModelListener listener)
	{
		this.listeners.put(listener, listener);
//		Window.alert("Number of listeners for feature: "+featureId+", : "+this.listeners.size());
	}
	public	void	addListenerWithDelay(FeatureModelListener listener)
	{
		if (this.newListeners == null)
		{
			this.newListeners = new HashMap();
		}
		this.newListeners.put(listener, listener);
//		Window.alert("Number of listeners for feature: "+featureId+", : "+this.listeners.size());
	}
	protected	void	checkNewListeners()
	{
		if (this.newListeners != null)
		{
			this.listeners.putAll(this.newListeners);
			this.newListeners = null;
		}
	}
	public	void	removeListener(FeatureModelListener listener)
	{
		this.listeners.remove(listener);
	}
	public	Integer	getModelIndex()
	{
		return	new	Integer(-1);
	}
	public	Object	getCurrentObject()
	{
		return	null;
	}
	public	void	setSelected(int listIndex)
	{
		if (listIndex >= 0 && listIndex < this.objects.size())
		{
			if (this.objects.get(listIndex) != null)
			{
				//	Trigger the selection listener for the old object.
				//	to remove the UI highlights .
				//	TODO
				
				this.currentObject = this.objects.get(listIndex);
				
				//	Trigger the selection listeners for new object.
				//	This is to remove UI highlights and enable and
				//	disable various user functions.
				//	TODO
			}
		}
	}
	/**
	 * This method must be implemented by each model or manager. Execution
	 * of the function may require additional data, which may come from
	 * either selections from lists or intermediate forms to be presented
	 * to user. This will be done by the model functions.
	 */
	public	void	onCall(int functionIndex)
	{
		return;
	}
	/**
	 * The AJAX event listener is started for this feature only if a login
	 * or connect to conference succeeds. The listener is closed on logout
	 * or disconnect in case of an external user.
	 */
	public	void	startEventListener()
	{
		EventsJsonHandler.getHandler().addFeatureListener(featureId,this);
//		JSONEventsReader.addFeatureListener(featureId, 100, this);
	}
	public	void	stopEventListener()
	{
		EventsJsonHandler.getHandler().removeFeatureListener(featureId);
//		JSONEventsReader.removeFeatureListener(this.featureId);
	}
	
	public	void	onCompletion(String text)
	{
		if (text != null)
		{
			if (this.commandExecListener != null)
			{
				try
				{
					JSONValue jsonObject = JSONParser.parse(text);
					UIServerResponse response = this.jsonReader.readResponse(jsonObject);
//					Window.alert(response.toString());
					this.commandExecListener.onExecComplete(response);
				}
				catch(Exception e)
				{
					
				}
			}
		}
		else
		{
//			Window.alert("Received null response to command: "+this.lastCommandUrl);
		}
	}
	protected	void	executeCommand(String url)
	{
		this.lastCommandUrl = url;
		JSONurlReader reader = new JSONurlReader(url,ConferenceGlobals.getConferenceKey(),this);
		reader.doReadURL();
	}
//	protected	void	executeCommandPost(String url, String urlData)
//	{
//		this.lastCommandUrl = url;
//		JSONurlReader reader = new JSONurlReader(url,ConferenceGlobals.getConferenceKey(),this);
//		reader.doPostURL(urlData);
//	}
//	public	void	urlReadingComplete(UIServerResponse response)
//	{
//		lastCommandResponse = response;
//	}
	
	/**
	 * The generic onEvent implementation that will call the specific
	 * methods on the specific models.
	 */
	public	void	onEvent(String eventId, Object data)
	{
		/**
		 * This little trick does not work.
		Window.alert("Received event:"+eventId+", data:"+data.toString());
		String eventMethod = eventId;
		int index = eventId.lastIndexOf(".");
		if (index > 0)
		{
			eventMethod = "on"+eventId.substring(index+1);
		}
		Window.alert("Attempting to trigger method:"+eventMethod);
		actOnEvent(eventMethod,data);
		Window.alert("Trigger worked?");
		*/
	}
	
	public	native	void	actOnEvent(String eventMethod, Object data)/*-{
		this[eventMethod](data);
	}-*/;
	
	/**
	 * These following methods are part of popout support. This allows
	 * console to transfer the contents of the feature model, if any,
	 * from the console to popout window.
	 */
	public	String	getPopoutJsonData()
	{
		int	limit = getPopoutJsonDataArrayLimit();
		String eventName = this.getPopoutJsonEventName();
		String eventDataType = this.getPopoutJsonEventDataType();
		if (eventName == null)
		{
			return	null;
		}
		StringBuffer buf = new StringBuffer();
		
		//	Add the event parameters.
		buf.append("[{");
		buf.append("type:\"event\",featureId:\"");
		buf.append(this.featureId);
		buf.append("\",eventId:\"");
		buf.append(eventName);
		buf.append("\",dataType:\"");
		buf.append(eventDataType);
		buf.append("\",data:");
		
		//	Add the data array or object.
		
		if (eventDataType.equals("array"))
		{
			buf.append("[");
		}
		int size = this.objects.size();
		for (int i=0; i<size && i<limit; i++)
		{
			if (i>0)
			{
				if (eventDataType.equals("array"))
				{
					buf.append(",");
				}
				else
				{
					break;
				}
			}
			UIObject obj = (UIObject)this.objects.get(i);
			buf.append(obj.toJson());
		}
		if (eventDataType.equals("array"))
		{
			buf.append("]");
		}
		
		//	Complete the event
		
		buf.append("}]");
		
		return	buf.toString();
	}
	/**
	 * This method most probably will not be required, because the popout data
	 * is being created as the roster event and can be read through the same
	 * way as the event.
	 * 
	 * @param dataText
	 */
	public	void	readPopoutJsonData(String dataText)
	{
		try
		{
			JSONValue jsonObject = JSONParser.parse(dataText);
			JSONArray ary = jsonObject.isArray();
			if (ary != null)
			{
				
			}
		}
		catch(Exception e)
		{
			Window.alert(e.getMessage());
		}
	}
	public	int	getNumberOfObjects()
	{
		return	this.objects.size();
	}
	protected	String		getPopoutJsonEventName()
	{
		return	null;
	}
	protected	String		getPopoutJsonEventDataType()
	{
		return	"array";
	}
	protected	UIObject	readPopoutJson(JSONValue objStr)
	{
		return	null;
	}
	private	native	int	getPopoutJsonDataArrayLimit() /*-{
		try
		{
			if ($wnd.popout_json_data_array_limit)
			{
				return	$wnd.popout_json_data_array_limit;
			}
			else
			{
				return	25;
			}
		}
		catch(e)
		{
			return	25;
		}
	}-*/;
}
