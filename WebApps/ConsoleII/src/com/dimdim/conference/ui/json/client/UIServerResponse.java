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

import	java.util.ArrayList;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UIServerResponse
{
	protected	boolean		success = false;
	protected	String		messageText = "";
	protected	String		eventId = "";
	protected	String		featureId = "";
	protected	Object		data;
	protected	ArrayList	arrayList;
	protected	String		eventText;
	
	public	UIServerResponse()
	{
	}
	
	public	Object	getAvailableData()
	{
		if (arrayList != null)
		{
			return	arrayList;
		}
		else
		{
			return	data;
		}
	}
	public	boolean	hasData()
	{
		return	this.getAvailableData()!=null;
	}
	
	public String getMessageText()
	{
		return this.messageText;
	}
	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}
	public boolean isSuccess()
	{
		return this.success;
	}
	public void setSuccess(boolean success)
	{
		this.success = success;
	}
	public Object getData()
	{
		return this.data;
	}
	public void setData(Object data)
	{
		this.data = data;
	}
	public String getEventId()
	{
		return this.eventId;
	}
	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}
	public	boolean	hasArray()
	{
		return	this.arrayList!=null;
	}
	public ArrayList getArrayList()
	{
		return this.arrayList;
	}
	public void setArrayList(ArrayList arrayList)
	{
		this.arrayList = arrayList;
	}
	public String getFeatureId()
	{
		return this.featureId;
	}
	public void setFeatureId(String featureId)
	{
		this.featureId = featureId;
	}
	public String getEventText()
	{
		return eventText;
	}
	public void setEventText(String eventText)
	{
		this.eventText = eventText;
	}
	public	String	toString()
	{
		StringBuffer	buf = new StringBuffer();
		buf.append("Success:"+this.success);
		buf.append(", featureId:");
		buf.append(featureId);
		buf.append(", eventId:");
		buf.append(eventId);
		if (data != null)
		{
			buf.append(", has data:{"+data.toString()+"}");
		}
		else if (arrayList != null)
		{
			buf.append(", has array:"+arrayList);
		}
		else
		{
			buf.append(", no data");
		}
		return	buf.toString();
	}
}
