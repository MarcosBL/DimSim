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

package com.dimdim.conference.ui.common.client.data;

import java.util.HashMap;

import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.ui.common.client.data.UIDataDictionary;
import com.dimdim.ui.common.client.data.UIDataDictionaryManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * WARNING: use of this class requires following function to be defined in the
 * principle html page that uses the gwt module.
 * 
 * 
			readConsoleData = function(dataSetName,varName)
			{
				return window.document.frames['ConsoleData'].vars[dataSetName][varName];
			}
 * 
 */

public class StringsTable
{
	protected	String		tableName;
	protected	String[]	fieldNames;
	protected	HashMap		fields;
	
	public	StringsTable(String tableName, String[] fieldNames)
	{
		this.tableName = tableName;
		this.fieldNames = fieldNames;
		this.fields = new HashMap();
		int size = this.fieldNames.length;
		for (int i=0; i<size; i++)
		{
			String fieldName = this.fieldNames[i];
			String fieldValue = fieldName;
			try
			{
				fieldValue = getFieldValue(tableName,fieldName);
			}
			catch(Exception e)
			{
				//	Nothing can be done if the value is not available. Use
				//	the name itself so that the error will be visible and
				//	can be noticed and fixed faster.
				fieldValue = fieldName;
			}
			this.fields.put(fieldName,fieldValue);
		}
	}
	protected	String	getFieldValue(String tableName, String fieldName)
	{
		String key = tableName+"_"+fieldName;
		return ConferenceGlobals.userInfoDictionary.getStringValue(key);
//		return	$wnd.readConsoleData(tableName,fieldName);
	}
}
