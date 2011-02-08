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

import	com.google.gwt.json.client.JSONParser;
import	com.google.gwt.json.client.JSONValue;
//import	com.dimdim.conference.ui.json.client.JSONParser;
//import	com.dimdim.conference.ui.json.client.JSONValue;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */

public class DMSjsonResponseHandler
{
	public	DMSjsonResponseHandler()
	{
		
	}
	public	StartConversionResponse	readStartConversionResponse(String responseText)
	{
		StartConversionResponse	scr = null;
		try
		{
			JSONValue jsonResponse = JSONParser.parse(responseText);
			if (jsonResponse.isObject() != null)
			{
				scr = new StartConversionResponse(jsonResponse.isObject());
			}
		}
		catch(Exception e)
		{
		}
		return	scr;
	}
	public	ConversionProgressCheckResponse	readConversionProgressCheckResponse(String responseText)
	{
		ConversionProgressCheckResponse	cpcr = null;
		try
		{
			JSONValue jsonResponse = JSONParser.parse(responseText);
			if (jsonResponse.isObject() != null)
			{
				cpcr = new ConversionProgressCheckResponse(jsonResponse.isObject());
			}
		}
		catch(Exception e)
		{
		}
		return	cpcr;
	}
}
