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

package com.dimdim.ui.common.client.data;

import com.google.gwt.user.client.Window;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

import com.dimdim.ui.common.client.json.UIObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * A dictionary is nothing but a simple table of names and values. A dictionary
 * lookup will return the value of the key. In case a int or boolean value is
 * asked for the string in dictionary will be converted. If a value is not
 * found, the key itself will be returned in case of string value and the
 * default value will be returned in case of int and boolean.
 */

public class UIDataDictionary extends	UIObject
{
	protected	JSONObject	jsonObject;
	
	public	UIDataDictionary(JSONObject obj)
	{
		this.jsonObject = obj;
	}
	public	String	getStringValue(String key)
	{
		return	super.getStringValue(this.jsonObject,key);
	}
	public	void	setStringValue(String key, String value)
	{
		super.setStringValue(this.jsonObject,key,value);
	}
	public	int	getIntValue(String key, int defaultValue)
	{
		return	super.getIntValue(this.jsonObject,key,defaultValue);
	}
	public	boolean	getBooleanValue(String key, boolean defaultValue)
	{
		return	super.getBooleanValue(this.jsonObject,key,defaultValue);
	}
	public	JSONArray	getArrayValue(String key)
	{
		JSONArray array = this.jsonObject.get(key).isArray();
		if (array == null)
		{
			JSONObject obj = this.jsonObject.get(key).isObject();
			if (obj != null)
			{
				array = obj.isArray();
				if (array == null)
				{
					array = new JSONArray();
				}
			}
		}
		return	array;
	}
	public	String	toString()
	{
		return	this.jsonObject.toString();
	}
}
