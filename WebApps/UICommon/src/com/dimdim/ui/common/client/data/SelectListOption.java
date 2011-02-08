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

import com.dimdim.ui.common.client.json.UIObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONString;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * A select list option is a single option in a drop down list on any of the
 * forms. The option has a value which must be used to send the option value
 * to the server action and a name, which is displayed to the user while
 * choosing the option. The name of the option is already internationalized
 * by the server before sending the data block to the ui so that no additional
 * resource lookup is required before displaying the options to the user.
 * 
 * Since this object, on the ui side, is meant for display only the values
 * are maintained as simple strings. If at all required, each form validator
 * needs to convert the string to a true type if required.
 */

public class SelectListOption extends	UIObject
{
	protected	JSONObject	jsonObject;
	
	public	SelectListOption(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}
	public	String	getName()
	{
		return	getProperty("name");
	}
	public	String	getValue()
	{
		return	getProperty("value");
	}
	private	String	getProperty(String propName)
	{
		JSONValue v = jsonObject.get(propName);
		if (v != null)
		{
			JSONString s = v.isString();
			if (s != null)
			{
				return	s.stringValue();
			}
		}
		return	"";
	}
	public	String	toString()
	{
		return	"name:"+getName()+",value:"+getValue();
	}
}
