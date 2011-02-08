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

package com.dimdim.conference.ui.publisher.client;

import	com.google.gwt.json.client.JSONObject;
import	com.dimdim.conference.ui.model.client.helper.ProgressCheckResponse;
//import	com.dimdim.conference.ui.json.client.JSONObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */


public class ConversionProgressCheckResponse
{
	protected	JSONObject	jsonObject;
	
	public	ConversionProgressCheckResponse(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}
	public	int	getResult()
	{
		return	getIntValue("conversionResult",0);
	}
	public	boolean	getConversionComplete()
	{
		return	getBooleanValue("conversionComplete",false);
	}
	public	int	getTotalSlides()
	{
		return	getIntValue("totalPages",-1);
	}
	public	int	getSlidesConverted()
	{
		return	getIntValue("pagesConverted",-1);
	}
	public	int	getError()
	{
		return	getIntValue("error",7500);
	}
	public	boolean	isActionComplete()
	{
		boolean b =	this.getConversionComplete();
		int	st = this.getTotalSlides();
		int sc = this.getSlidesConverted();
		if (!b)
		{
			
		}
		return	b;
	}
	public	boolean	isActionCancelled()
	{
		return	this.getBooleanValue("conversionCancelled", false);
	}
	private	boolean	getBooleanValue(String keyName, boolean defaultValue)
	{
		boolean b = defaultValue;
		try
		{
			if (this.jsonObject.containsKey(keyName))
			{
				String s = this.jsonObject.get(keyName).isString().stringValue();
				b = (new Boolean(s.toLowerCase())).booleanValue();
			}
		}
		catch(Exception e)
		{
			b = defaultValue;
		}
		return	b;
	}
	private	int	getIntValue(String keyName, int defaultValue)
	{
		int i = defaultValue;
		try
		{
			if (this.jsonObject.containsKey(keyName))
			{
				String s = this.jsonObject.get(keyName).isString().stringValue();
				i = (new Integer(s)).intValue();
			}
		}
		catch(Exception e)
		{
			i = defaultValue;
		}
		return	i;
	}
	
	public String toString(){
	    return jsonObject.toString();
	}
}
