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

import com.google.gwt.user.client.Window;

import	com.google.gwt.json.client.JSONValue;
import	com.google.gwt.json.client.JSONObject;
import	com.google.gwt.json.client.JSONArray;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class can be used to read the response and event json buffers received
 * from server into more directly useable objects.
 */
public class ResponseAndEventReader
{
	private	static	final	String	KEY_TYPE	=	"type";
	private	static	final	String	KEY_EVENT_ID	=	"eventId";
	private	static	final	String	KEY_FEATURE_ID	=	"featureId";
	private	static	final	String	KEY_DATA	=	"data";
	private	static	final	String	KEY_DATA_TYPE	=	"dataType";
	private	static	final	String	KEY_DATA_OBJECT_CLASS	=	"objClass";
	
	private	static	final	String	DATA_OBJECT_CLASS_ROSTER_ENTRY = "RosterEntry";
	private	static	final	String	DATA_OBJECT_CLASS_RESOURCE_OBJECT = "ResourceObject";
	private	static	final	String	DATA_OBJECT_CLASS_PRESENTATION_CONTROL_EVENT = "PresentationControlEvent";
	private	static	final	String	DATA_OBJECT_CLASS_SETTINGS_EVENT = "SettingsEvent";
	private	static	final	String	DATA_OBJECT_CLASS_STREAM_CONTROL_EVENT = "StreamControlEvent";
	private	static	final	String	DATA_OBJECT_CLASS_CHAT_ENTRY = "ChatEntry";
	private	static	final	String	DATA_OBJECT_CLASS_POPOUT_PANEL_DATA = "PopoutPanelData";
	private	static	final	String	DATA_OBJECT_CLASS_FORM_DEFAULT_VALUES = "FormDefaultValues";
	private	static	final	String	DATA_OBJECT_CLASS_EMAIL_ATTEMPT_RESULT = "EmailAttemptResult";
	private	static	final	String	DATA_OBJECT_CLASS_WHITEBOARD_CONTROL = "wce";
	private	static	final	String	DATA_OBJECT_CLASS_COBROWSE_CONTROL = "cbce";
	private	static	final	String	DATA_OBJECT_CLASS_RECORDING_CONTROL = "RecordingControlEvent";
	
	private	static	final	String	KEY_VALUE_EVENT	=	"event";
	private	static	final	String	KEY_VALUE_SUCCESS	=	"success";
//	private	static	final	String	KEY_VALUE_ERROR	=	"error";
	private	static	final	String	KEY_VALUE_MESSAGE	=	"message";
	private	static	final	String	KEY_VALUE_ALERT	=	"alert";
	
	private	static	final	String	KEY_VALUE_DATA_TYPE_OBJECT	=	"object";
	private	static	final	String	KEY_VALUE_DATA_TYPE_ARRAY	=	"array";
	private	static	final	String	KEY_VALUE_DATA_TYPE_STRING	=	"string";
	
	public	String	readMessage(JSONObject obj)
	{
		String message = null;
		if (obj.containsKey(KEY_TYPE) && obj.get(KEY_TYPE).toString().equals(KEY_VALUE_MESSAGE))
		{
			message = "";
			if (obj.containsKey(KEY_DATA))
			{
				message = obj.get(KEY_DATA).isString().stringValue();//.stringValue();
			}
		}
		return	message;
	}
	public	String	readAlert(JSONObject obj)
	{
		String message = null;
		if (obj.containsKey(KEY_TYPE) && obj.get(KEY_TYPE).toString().equals(KEY_VALUE_ALERT))
		{
			message = "";
			if (obj.containsKey(KEY_DATA))
			{
				message = obj.get(KEY_DATA).isString().stringValue();//.stringValue();
			}
		}
		return	message;
	}
	public	UIServerResponse	readGetEventsResponse(JSONValue value)
	{
		JSONArray eventsArray = value.isArray();
		UIServerResponse resp = null;
		if (eventsArray == null)
		{
			JSONObject obj = value.isObject();
//Window.alert("ResponseAndEventReader:readResponse value.isObject()?::"+obj);
			if (obj != null)
			{
				eventsArray = obj.isArray();
//Window.alert("ResponseAndEventReader:readResponse value.isArray()?::"+eventsArray);
				if (eventsArray == null)
				{
					//	Read and make array out of it. We know this is an array.
					eventsArray = new JSONArray();
//				String[] keys = obj.getKeys();
//				for (int i=0; i<keys.length; i++)
//				{
//					eventsArray.add(obj.get(keys[i]));
//				}
				}
			}
		}
//Window.alert("ResponseAndEventReader:readResponse array?::"+eventsArray);
		if (eventsArray != null)
		{
			//Window.alert("Reading array");
			resp = new UIServerResponse();
			resp.setSuccess(true);
			ArrayList aryList = new ArrayList();
			int  size = eventsArray.size();
			for (int i=0; i<size; i++)
			{
				JSONValue arrayMember = eventsArray.get(i);
				//Window.alert("Reading array member:"+arrayMember);
				Object data = readResponse(arrayMember);
				//Window.alert("Read array member:"+data);
				aryList.add(data);
			}
			resp.setArrayList(aryList);
		}
//		else if (obj != null)
//		{
//			resp = readResponse(value);
//		}
		return	resp;
	}
	public	UIServerResponse	readResponse(JSONValue value)
	{
		UIServerResponse resp = new UIServerResponse();
		JSONObject obj = value.isObject();
		//Window.alert("ResponseAndEventReader:readResponse::"+obj);
//		Window.alert("ResponseAndEventReader:readResponse::"+obj.isArray());
//		Window.alert("ResponseAndEventReader:readResponse::"+obj.isObject());
		if (obj != null)
		{
			//	At present we use only object returns for all urls.
			//Window.alert("Reading non array return");
			if (obj.containsKey(KEY_TYPE))
			{
	//			System.out.println("1");
				String str = obj.get(KEY_TYPE).isString().stringValue();//.stringValue();
				//Window.alert(KEY_TYPE+":"+str);
				if (str.equals(KEY_VALUE_SUCCESS) ||
						str.equals(KEY_VALUE_EVENT) || str.equals(KEY_VALUE_MESSAGE))
				{
					resp.setSuccess(true);
				}
			}
			if (obj.containsKey(KEY_FEATURE_ID))
			{
				String featureId = obj.get(KEY_FEATURE_ID).isString().stringValue();//.stringValue();
				//Window.alert(KEY_FEATURE_ID+":"+featureId);
				resp.setFeatureId(featureId);
			}
			if (obj.containsKey(KEY_EVENT_ID))
			{
				String eventId = obj.get(KEY_EVENT_ID).isString().stringValue();//.stringValue();
				//Window.alert(KEY_EVENT_ID+":"+eventId);
				resp.setEventId(eventId);
			}
			JSONValue v = obj.get(KEY_DATA);
			if (v != null)
			{
				//Window.alert(KEY_DATA+":");
				if (obj.containsKey(KEY_DATA_TYPE))
				{
					//	Supported types are array, string and object.
					String dataType = obj.get(KEY_DATA_TYPE).isString().stringValue();//.stringValue();
					//Window.alert(KEY_DATA_TYPE+":"+dataType);
					if (dataType.equals(KEY_VALUE_DATA_TYPE_OBJECT))
					{
						Object data = readObject(obj.get(KEY_DATA).isObject());
						resp.setData(data);
					}
					else if (dataType.equals(KEY_VALUE_DATA_TYPE_STRING))
					{
						//	Assume a simple string.
						String str = obj.get(KEY_DATA).toString();
						resp.setData(str);
					}
					else if (dataType.equals(KEY_VALUE_DATA_TYPE_ARRAY))
					{
						//	Assume a simple string.
						//Window.alert(KEY_DATA+":v:"+v);
						JSONObject vo = v.isObject();
						//Window.alert(KEY_DATA+":v:"+vo);
						JSONArray ary = v.isArray();
						//Window.alert(KEY_DATA+":v:"+ary);
						if (ary == null && vo != null)
						{
							ary = vo.isArray();
						}
						ArrayList aryList = new ArrayList();
						if (ary != null)
						{
							int  size = ary.size();
							for (int i=0; i<size; i++)
							{
								JSONObject jsonObj = ary.get(i).isObject();
								Object data = readObject(jsonObj);
								aryList.add(data);
							}
						}
						resp.setArrayList(aryList);
					}
					else
					{
						//Window.alert("Unrecognized data type:"+dataType);
					}
				}
			}
			else
			{
				//Window.alert("No data found");
			}
		}
		return	resp;
	}
	public	Object	readObject(JSONObject obj)
	{
		Object data = null;
		String dataObjectClass = obj.get(KEY_DATA_OBJECT_CLASS).isString().stringValue(); //.toString();
//		Window.alert("ResponseAndEventReader:readObject:"+KEY_DATA_OBJECT_CLASS+":"+dataObjectClass);
//		Window.alert("11"+dataObjectClass);
		if (dataObjectClass != null)
		{
			if (dataObjectClass.equals(DATA_OBJECT_CLASS_ROSTER_ENTRY))
			{
				data = UIRosterEntry.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_PRESENTATION_CONTROL_EVENT))
			{
				data = UIPresentationControlEvent.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_RESOURCE_OBJECT))
			{
//				Window.alert("Parsing presentation control object");
				data = UIResourceObject.parseJsonObject(obj);
//				Window.alert("done");
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_STREAM_CONTROL_EVENT))
			{
//				Window.alert("Parsing screen share control object");
				data = UIStreamControlEvent.parseJsonObject(obj);
//				Window.alert("done");
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_CHAT_ENTRY))
			{
				data = UIChatEntry.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_SETTINGS_EVENT))
			{
				data = UISettingsEvent.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_POPOUT_PANEL_DATA))
			{
				data = UIPopoutPanelData.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_FORM_DEFAULT_VALUES))
			{
				data = UIFormDefaultValues.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_EMAIL_ATTEMPT_RESULT))
			{
				data = UIEmailAttemptResult.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_WHITEBOARD_CONTROL))
			{
				data = UIWhiteboardControlEvent.parseJsonObject(obj);
			}else if (dataObjectClass.equals(DATA_OBJECT_CLASS_COBROWSE_CONTROL))
			{
				data = UICobrowseControlEvent.parseJsonObject(obj);
			}
			else if (dataObjectClass.equals(DATA_OBJECT_CLASS_RECORDING_CONTROL))
			{
				data = UIRecordingControlEvent.parseJsonObject(obj);
			}
		}
		return	data;
	}
}
