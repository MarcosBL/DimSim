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
import com.google.gwt.json.client.JSONObject;

import com.dimdim.ui.common.client.json.ServerResponseObjectReader;
import com.dimdim.ui.common.client.json.UIObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UIDataJsonObjectReader implements ServerResponseObjectReader
{
	
	public	UIDataJsonObjectReader()
	{
	}
	
	/**
	 * This method is expected to convert the json object to a specific type.
	 * In this case we know we only will be reading data dictionaries.
	 * 
	 * Expected values of object class are: 'dictionary' and 'SelectList'
	 */
	public UIObject readServerResponseData(String objectClass, JSONObject data)
	{
		UIObject object = null;
		if (objectClass.equals("dictionary"))
		{
			object = new UIDataDictionary(data);
		}
		else if (objectClass.equals("SelectList"))
		{
			object = SelectList.readSelectList(objectClass,data);
		}
		else
		{
			//	Unknown data object class, this is not expected. If this reader
			//	is used the data object must be a supported object class. This is
			//	important to make the module reusable. If a single reader is used
			//	for all model classes, then all of the classes get included in
			//	all the modules which use this module, even if they dont use them.
			//	That is the reason for using seperate and smaller json object
			//	readers.
		}
		return	object;
	}
}
