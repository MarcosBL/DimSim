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

import java.util.HashMap;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This is a common superclass of all objects in the ui. It provides a few
 * simple common methods.
 */

public class UIObject
{
	public	String	toJson()
	{
		return	"";
	}
	public	String	getStringValue(JSONObject jsonObject, String key)
	{
		key = convertToJsIdString(key);
		if (jsonObject.containsKey(key))
		{
			JSONString s = jsonObject.get(key).isString();
			if (s != null)
			{
				return	s.stringValue();
			}
			else
			{
//				Window.alert("Unexpected value:"+jsonObject.get(key));
			}
		}
		else
		{
//			Window.alert("Key:"+key+", not found in data dictionary");
		}
		return	key;
	}
	public	void	setStringValue(JSONObject jsonObject, String key, String value)
	{
		key = convertToJsIdString(key);
		jsonObject.put(key, new JSONString(value));
	}
	public	int	getIntValue(JSONObject jsonObject, String key, int defaultValue)
	{
		int	value = defaultValue;
		key = convertToJsIdString(key);
		if (jsonObject.containsKey(key))
		{
			JSONString s = jsonObject.get(key).isString();
			if (s != null)
			{
				try
				{
					value = (new Integer(s.stringValue())).intValue();
				}
				catch(Exception e)
				{
//					Window.alert("UIDataDictionary:getIntValue:"+e.getMessage());
					value = defaultValue;
				}
			}
			else
			{
//				Window.alert("Unexpected value:"+jsonObject.get(key));
			}
		}
		else
		{
//			Window.alert("Key:"+key+", not found in data dictionary");
		}
		return	value;
	}
	public	boolean	getBooleanValue(JSONObject jsonObject, String key, boolean defaultValue)
	{
		boolean	value = defaultValue;
		key = convertToJsIdString(key);
		if (jsonObject.containsKey(key))
		{
			JSONString s = jsonObject.get(key).isString();
			if (s != null)
			{
				try
				{
					value = (new Boolean(s.stringValue())).booleanValue();
				}
				catch(Exception e)
				{
//					Window.alert("UIDataDictionary:getIntValue:"+e.getMessage());
					value = defaultValue;
				}
			}
			else
			{
//				Window.alert("Unexpected value:"+jsonObject.get(key));
			}
		}
		else
		{
//			Window.alert("Key:"+key+", not found in data dictionary");
		}
		return	value;
	}
	protected	String	convertToJsIdString(String str)
	{
		String s1 = str;
		int dot = s1.indexOf(".");
		while (dot > 0)
		{
			s1 = s1.substring(0,dot)+"$"+s1.substring(dot+1);
			dot = s1.indexOf(".");
		}
		return	s1;
	}
	/**
	 * This method is expected to return a simple table of all properties
	 * with the given prefix prepended to each name. This is to make the
	 * properties available to the forms to be initialized. The prefix is
	 * required as each form may use one for the properties of a particular
	 * object.
	 * 
	 * @param prefix
	 * @return
	 */
	public	HashMap	getPropertiesMap(String prefix)
	{
		HashMap props = new	HashMap();
		addProperties(props,prefix);
		
		return	props;
	}
	protected	void	addProperties(HashMap props, String prefix)
	{
		return;
	}
}
