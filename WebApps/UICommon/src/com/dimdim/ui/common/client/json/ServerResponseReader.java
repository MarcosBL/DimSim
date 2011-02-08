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

package com.dimdim.ui.common.client.json;

import	java.util.ArrayList;

import com.google.gwt.user.client.Window;

import	com.google.gwt.json.client.JSONValue;
import	com.google.gwt.json.client.JSONString;
import	com.google.gwt.json.client.JSONObject;
import	com.google.gwt.json.client.JSONArray;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class can be used to read the response and event json buffers received
 * from server into more directly useable objects.
 * 
 * The server response for any command will be a single object and will have
 * following format and content. All the parameters must always be present and
 * values as indicated.
 * 
 * {success:"true/false",dataType:"string/array/object",objClass:"<name>",
 * 	data:"<objectJsonBuffer> or empty"}
 * 
 * The object class is a simple string that is recognized by the object reader
 * implementation. The data type refers to the array members where all must be of
 * same type.
 */

public class ServerResponseReader
{
	private	static	final	String	KEY_SUCCESS	=	"success";
	private	static	final	String	KEY_CODE	=	"code";
	private	static	final	String	KEY_DATA	=	"data";
	private	static	final	String	KEY_DATA_TYPE	=	"dataType";
	private	static	final	String	KEY_DATA_OBJECT_CLASS	=	"objClass";
	
//	private	static	final	String	KEY_VALUE_SUCCESS_TRUE	=	"true";
//	private	static	final	String	KEY_VALUE_SUCCESS_FALSE	=	"false";
	
//	private	static	final	String	KEY_VALUE_DATA_TYPE_STRING	=	"string";
	private	static	final	String	KEY_VALUE_DATA_TYPE_OBJECT	=	"object";
	private	static	final	String	KEY_VALUE_DATA_TYPE_ARRAY	=	"array";
	
	public	ServerResponse	readServerResponse(JSONValue value,
				ServerResponseObjectReader objectReader)
	{
		ServerResponse resp = null;
		JSONObject obj = value.isObject();
		//Window.alert("ResponseAndEventReader:readResponse value.isObject()?::"+obj);
		if (obj != null)
		{
			String	dataType = "";
			String	successStr = "false";
			String	codeStr = "0";
			boolean	success = false;
			int	code = 0;
			String	objClass = "";
			
			if (obj.containsKey(KEY_SUCCESS))
			{
				JSONString v = obj.get(KEY_SUCCESS).isString();
				if (v != null)
				{
					successStr = v.stringValue();
					if (successStr.equals("true") || successStr.equals("false"))
					{
						success = (new Boolean(successStr)).booleanValue();
					}
				}
			}
			if (obj.containsKey(KEY_CODE))
			{
				JSONString v = obj.get(KEY_CODE).isString();
				if (v != null)
				{
					codeStr = v.stringValue();
					try
					{
						code = (new Integer(codeStr)).intValue();
					}
					catch(Exception e)
					{
						code = 0;
					}
				}
			}
			if (obj.containsKey(KEY_DATA_TYPE))
			{
				JSONString v = obj.get(KEY_DATA_TYPE).isString();
				if (v != null)
				{
					dataType = v.stringValue();
				}
			}
			if (obj.containsKey(KEY_DATA_OBJECT_CLASS))
			{
				JSONString v = obj.get(KEY_DATA_OBJECT_CLASS).isString();
				if (v != null)
				{
					objClass = v.stringValue();
				}
			}
			if (dataType != null && objClass != null)
			{
				//	Read the object.
				if (obj.containsKey(KEY_DATA))
				{
					if (dataType.equals(KEY_VALUE_DATA_TYPE_ARRAY))
					{
						JSONValue v = obj.get(KEY_DATA);
						JSONArray ary = v.isArray();
						if (ary != null)
						{
							ArrayList vec = readArray(ary,objClass,objectReader);
							resp = new ServerResponse(success,code,vec);
						}
						else
						{
//							Window.alert("Received null array");
						}
					}
					else if (dataType.equals(KEY_VALUE_DATA_TYPE_OBJECT))
					{
						JSONValue v = obj.get(KEY_DATA);
						JSONObject o = v.isObject();
						if (o != null)
						{
							UIObject uo = objectReader.readServerResponseData(objClass, o);
							resp = new ServerResponse(success,code,uo);
						}
					}
				}
			}
			if (resp == null)
			{
				resp = new ServerResponse(false,-1,"Server response reading failure");
			}
		}
		return	resp;
	}
	private	ArrayList	readArray(JSONArray ary, String objectClass,
				ServerResponseObjectReader objectReader)
	{
		ArrayList aryList = new ArrayList();
		int  size = ary.size();
		for (int i=0; i<size; i++)
		{
			JSONObject jsonObj = ary.get(i).isObject();
			Object data = objectReader.readServerResponseData(objectClass,jsonObj);
			aryList.add(data);
		}
		return	aryList;
	}
}
