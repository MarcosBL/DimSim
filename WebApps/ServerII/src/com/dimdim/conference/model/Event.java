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

package com.dimdim.conference.model;

import	java.util.Date;
import	java.util.List;
import	java.util.HashMap;

import	com.dimdim.messaging.IEvent;
import com.dimdim.messaging.IEventData;

import	com.dimdim.conference.ConferenceConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class Event	implements	IJsonSerializable, IEvent
{
	public int getPriority()
	{
		if (this.featureId.equals(ConferenceConstants.FEATURE_CHAT))
		{
			return	IEvent.PRIORITY_2;
		}
		else if (this.featureId.equals(ConferenceConstants.FEATURE_CONF))
		{
			return	IEvent.PRIORITY_0;
		}
		else
		{
			return	IEvent.PRIORITY_1;
		}
//		Integer	selfFeatureIndex = (Integer)(Event.getFeatureIndexes().get(this.featureId));
//		if (selfFeatureIndex != null)
//		{
//			return	selfFeatureIndex.intValue();
//		}
//		return 100;
	}
	
	private	int	index;
	private	long	creationTime;
	
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int i)
	{
		this.index = i;
	}
	
//	private	static	HashMap	featureIndexes = null;
//	
//	private	static	HashMap	getFeatureIndexes()
//	{
//		if (Event.featureIndexes == null)
//		{
//			Event.setupFeatureIndexes();
//		}
//		return	Event.featureIndexes;
//	}
//	private	synchronized	static	void	setupFeatureIndexes()
//	{
//		Event.featureIndexes = new HashMap();
//		
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_CONF,new Integer(0));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_ROSTER,new Integer(1));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_RESOURCE_MANAGER,new Integer(2));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_PRESENTATION,new Integer(3));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_SHARING,new Integer(4));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_VIDEO,new Integer(5));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_CHAT,new Integer(6));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_QUESTION_MANAGER,new Integer(7));
//		Event.featureIndexes.put(ConferenceConstants.FEATURE_POLL,new Integer(8));
//	}
	
	public	static	final	String	EventType_success = "success";
	public	static	final	String	EventType_error = "error";
	public	static	final	String	EventType_login = "login";
	public	static	final	String	EventType_message = "message";
	public	static	final	String	EventType_alert = "alert";
	public	static	final	String	EventType_event = "event";
	
	public	static	final	String	DataType_array = "array";
	public	static	final	String	DataType_string = "string";
	public	static	final	String	DataType_object = "object";
	
	protected	String		eventType;
	protected	String		featureId;
	protected	String		eventId;
	protected	Object		data;
	
	//	Not strictly required.
//	protected	Date		time;
//	protected	String		from;
	
	//	A flag to indicate if the event is intended for any related object.
	//	Null target means the event is for the main recipient and children.
	//	Non null right now means child only.
	
//	protected	String		target;
	
//	public	static	Event	CreateGenericSuccessEvent()
//	{
//		return	new Event(Event.EventType_success,"",null,ConferenceConstants.WW_TOKEN_SUCCESS);
//	}
//	public	static	Event	CreateGenericErrorEvent()
//	{
//		return	new Event(Event.EventType_error,"",null,ConferenceConstants.WW_TOKEN_ERROR);
//	}
	
//	public Event()
//	{
//	}
	public Event(String featureId, String eventId, Date time, String from, Object data)
	{
		this(Event.EventType_event,featureId,eventId,time,from,data);
	}
	public Event(String eventType, String featureId, String eventId, Object data)
	{
		this(eventType,featureId,eventId,null,null,data);
	}
	private Event(String eventType, String featureId, String eventId, Date time, String from, Object data)
	{
		this.eventType = eventType;
		this.featureId = featureId;
		this.eventId = eventId;
//		this.time = time;
//		this.from = from;
		this.data = data;
		this.creationTime = System.currentTimeMillis();
	}
	/**
	 * This constructor is used to create events out of the server responses. The event id is
	 * same as the action. Result can be either 'response.ok' or 'response.error'. Result code
	 * is important only if the result is 'response.error', though it is optional. Data is also
	 * optional and is available only if the result is 'response.ok'.
	 * 
	 * @param featureId
	 * @param eventId
	 * @param result
	 * @param resultCode
	 * @param data
	 */
//	public	Event(String featureId, String eventId, String result, String resultCode, Object data)
//	{
//		this.featureId = featureId;
//		this.eventId = eventId;
//		this.eventType = Event.EventType_error;
//		this.data = "Internal Error";
//		if (result != null)
//		{
//			if (result.equals(ConferenceConstants.RESPONSE_OK))
//			{
//				this.eventType = Event.EventType_success;
//				this.data = data;
//			}
//			else
//			{
//				this.eventType = Event.EventType_error;
//				if (resultCode != null)
//				{
//					this.data = resultCode;
//				}
//			}
//		}
//		this.data = data;
//		this.creationTime = System.currentTimeMillis();
//	}
//	public	int	compareTo(Object obj)
//	{
//		if (obj instanceof com.dimdim.conference.model.Event)
//		{
//			Integer	selfFeatureIndex = (Integer)(Event.getFeatureIndexes().get(this.featureId));
//			Integer	argFeatureIndex = (Integer)(Event.getFeatureIndexes().get(((Event)obj).getFeatureId()));
//			
//			return	selfFeatureIndex.compareTo(argFeatureIndex);
//		}
//		else
//		{
//			return	1;
//		}
//	}
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"type\":\""); buf.append(this.eventType); buf.append("\",");
		buf.append( "\"featureId\":\""); buf.append(this.featureId); buf.append("\",");
		buf.append( "\"eventId\":\""); buf.append(this.eventId); buf.append("\"");
		if (this.data != null)
		{
			if (data instanceof List)
			{
				buf.append( ",\"dataType\":\"array\",\"data\":");
				List list = (List)data;
				int	size = list.size();
				buf.append( "[");
				for (int i=0; i<size; i++)
				{
					if (i>0)	buf.append(",");
					buf.append(getJsonBuffer(list.get(i)));
				}
				buf.append("]");
			}
			else if (data instanceof java.lang.String)
			{
				buf.append( ",\"dataType\":\"string\",\"data\":");
				buf.append("\""); buf.append(data.toString()); buf.append("\"");
			}
			else
			{
				buf.append( ",\"dataType\":\"object\",\"data\":");
				buf.append(getJsonBuffer(data));
			}
		}
		buf.append( "}" );
		
		return	buf.toString();
	}
	public IEventData getEventData()
	{
		if (this.data != null && this.data instanceof IEventData)
		{
			return	(IEventData)this.data;
		}
		return null;
	}
	public String getInitiatorId()
	{
		// TODO
		return "WIP";
	}
	protected	String	getJsonBuffer(Object obj)
	{
		String buffer = "";
		if (obj != null)
		{
			if (obj instanceof IJsonSerializable)
			{
				buffer = ((IJsonSerializable)obj).toJson();
			}
			else
			{
				buffer = obj.toString();
			}
		}
		return	buffer;
	}
	public	boolean	isSuccess()
	{
		return	this.eventType.equals(Event.EventType_success);
	}
	public	String	getWWActionReturn()
	{
		if (this.eventType.equals(Event.EventType_success) || this.eventType.equals(Event.EventType_login))
		{
			return	this.eventType;
		}
		else
		{
			return	Event.EventType_error;
		}
	}
	public	String	getJsonBuffer()
	{
		return	this.toJson();
	}
	public	void	setJsonBuffer(String s)
	{
	}
	public Object getData()
	{
		return data;
	}
	public void setData(Object data)
	{
		this.data = data;
	}
	public String getEventId()
	{
		return eventId;
	}
	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}
	public String getFeatureId()
	{
		return featureId;
	}
	public void setFeatureId(String featureId)
	{
		this.featureId = featureId;
	}
//	public String getFrom()
//	{
//		return from;
//	}
//	public void setFrom(String from)
//	{
//		this.from = from;
//	}
//	public Date getTime()
//	{
//		return time;
//	}
//	public void setTime(Date time)
//	{
//		this.time = time;
//	}
	public	String		getType()
	{
		return	this.eventId;
	}
	public	String		getSource()
	{
		return	this.featureId;
	}
//	public	String		getOriginator()
//	{
//		return	this.from;
//	}
//	public String getTarget()
//	{
//		return target;
//	}
//	public void setTarget(String target)
//	{
//		this.target = target;
//	}
	public	String	toString()
	{
		return	this.toJson();
	}
	public long getCreationTime()
	{
		return creationTime;
	}
}
