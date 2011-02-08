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

package com.dimdim.conference.ui.envcheck.client.layout;

import	com.google.gwt.json.client.JSONParser;
import	com.google.gwt.json.client.JSONObject;
import	com.google.gwt.json.client.JSONValue;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PostResponse
{
	protected	String	result;
	protected	String	url;
	protected	String	message;
	
	public	PostResponse(String responseText)
	{
		try
		{
			JSONValue responseObject = JSONParser.parse(responseText);
			if (responseObject != null)
			{
				JSONObject obj = responseObject.isObject();
				if (obj != null)
				{
					if (obj.containsKey("result"))
					{
			//			System.out.println("1");
						result = obj.get("result").isString().stringValue();//.stringValue();
					}
					if (obj.containsKey("url"))
					{
			//			System.out.println("1");
						url = obj.get("url").isString().stringValue();//.stringValue();
					}
					if (obj.containsKey("message"))
					{
			//			System.out.println("1");
						message = obj.get("message").isString().stringValue();//.stringValue();
					}
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}
	public	boolean	isSuccess()
	{
		if (this.result != null && result.equals("success") && this.url != null)
		{
			return	true;
		}
		return	false;
	}
	public String getMessage()
	{
		return message;
	}
	public String getResult()
	{
		return result;
	}
	public String getUrl()
	{
		return url;
	}
	public	String	toString()
	{
		return	"result="+result+", url="+url+", message="+message;
	}
}
