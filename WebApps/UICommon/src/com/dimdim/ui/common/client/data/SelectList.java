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

import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONArray;

import com.dimdim.ui.common.client.json.UIObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The select list is a single list of select list option objects and
 * is used to present drop down option in forms. The options selection
 * could be single or multiple. That is upto the form. The list itself
 * simply maintains the options.
 * 
 * The returned object must be a select list object, which has name,
 * number of entries in the list and an array of select list option
 * objects. The order of entries in the array is preserved while it
 * is presented to the user. Server must provide the array in the
 * desired display order.
 */

public class SelectList extends	UIObject
{
	protected	String	name;
	protected	Vector	options;
	
	public	static	SelectList	readSelectList(String listName, JSONObject jsonObject)
	{
		String name = null;
		JSONArray options = null;
		if (jsonObject.containsKey("name"))
		{
			JSONString s = jsonObject.get("name").isString();
			if (s != null)
			{
				name = s.stringValue();
			}
		}
		if (jsonObject.containsKey("options"))
		{
			options = jsonObject.get("options").isArray();
		}
		if (name != null && options != null)
		{
			return	new SelectList(name, options);
		}
		else
		{
			return	new	SelectList(listName, null);
		}
	}
	
	public	SelectList(String name, JSONArray options)
	{
		this.name = name;
		this.options = new Vector();
		if (options != null)
		{
			int	num = options.size();
			for (int i=0; i<num; i++)
			{
				JSONValue v = options.get(i);
				JSONObject option = v.isObject();
				if (option != null)
				{
					this.options.addElement(new SelectListOption(option));
				}
			}
		}
	}
	public	String	getName()
	{
		return	name;
	}
	public	Vector	getOptions()
	{
		return	options;
	}
	public	String	toString()
	{
		return	"Select list:"+name+", Options:"+this.options.toString();
	}
}
