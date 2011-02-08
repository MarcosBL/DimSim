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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.model.client.dms;

//import	com.google.gwt.json.client.JSONParser;
import	com.google.gwt.json.client.JSONObject;
//import	com.dimdim.conference.ui.json.client.JSONObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */

public class StartConversionResponse
{
	protected	JSONObject	jsonObject;
	
	public	StartConversionResponse(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}
	public	boolean	getResult()
	{
		boolean b = false;
		try
		{
			if (this.jsonObject.containsKey("result"))
			{
				String s = this.jsonObject.get("result").isString().stringValue();
				b = (new Boolean(s)).booleanValue();
			}
		}
		catch(Exception e)
		{
			
		}
		return	b;
	}
}
