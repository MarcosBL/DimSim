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
import com.google.gwt.user.client.ResponseTextHandler;

import	com.google.gwt.json.client.JSONParser;
import	com.google.gwt.json.client.JSONValue;

import com.dimdim.ui.common.client.json.ServerResponse;
import com.dimdim.ui.common.client.json.ServerResponseReader;
import com.dimdim.ui.common.client.json.ServerResponseObjectReader;
import com.dimdim.ui.common.client.json.JSONReturnUrlReader;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This data reader uses a full url to avoid any referencing location issues.
 * For this a webapp name is required, which is used from a public static in
 * this class itself. If a different webapp uses this class, it must initialize
 * the webapp name before using the data manager.
 */

public class UIDataReader implements ResponseTextHandler
{
	public	static	String	webappName;
	public	static	String	confKey;
	public	static	String	userType = "free";
	
	protected	ServerResponseReader	jsonReader;
	protected	UIDataReadingProgressListener	progressListener;
	protected	ServerResponseObjectReader		responseObjectReader;
	
	public	UIDataReader(UIDataReadingProgressListener progressListener,
				ServerResponseObjectReader responseObjectReader)
	{
		this.progressListener = progressListener;
		this.responseObjectReader = responseObjectReader;
		
		this.jsonReader = new ServerResponseReader();
	}
	public	void	readData(String type, String component, String name)
	{
		String url = "/"+webappName+"/GetData.action?type="+type+"&confKey="+confKey+"&role="+userType+
			"&component="+component+"&name="+name+"&t="+System.currentTimeMillis();
		JSONReturnUrlReader urlReader = new JSONReturnUrlReader(url,this);
		urlReader.doReadURL();
	}
	public void onCompletion(String text)
	{
		ServerResponse resp = null;
//		Window.alert(text);
		if (text != null && text.length() > 0)
		{
//			Window.alert(text);
			try
			{
				JSONValue jsonValue = JSONParser.parse(text);
				if (jsonValue != null)
				{
					resp = this.jsonReader.readServerResponse(jsonValue, this.responseObjectReader);
				}
			}
			catch(Exception e)
			{
//				Window.alert(e.getMessage());
			}
		}
		this.progressListener.dataReadingComplete(resp);
	}
}
