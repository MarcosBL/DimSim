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

package com.dimdim.data.model;

import	java.util.HashMap;
import	java.util.Iterator;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Provided hashmap must contain only strings as keys and values.
 */

public class UIDataDictionary implements JsonSerializable
{
	protected	HashMap	table;
	
	public	UIDataDictionary(HashMap table)
	{
		this.table = table;
	}
	public String getObjectClass()
	{
		return "UIDataDictionary";
	}
	public String toJson()
	{
		StringBuffer buf = new StringBuffer();
		
		Iterator keys = this.table.keySet().iterator();
		int	i=0;
		buf.append("{");
		while (keys.hasNext())
		{
			String key = (String)keys.next();
			String value = (String)this.table.get(key);
			if (i>0)
			{
				buf.append(",");
			}
			i++;
			buf.append(key);
			buf.append(":\"");
			buf.append(value);
			buf.append("\"");
		}
		buf.append("}");
		
		return buf.toString();
	}
	
}
