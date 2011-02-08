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
 
package com.dimdim.test.action;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.data.action.GetDataAction;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */

public class TestGetDataAction	extends	GetDataAction
{
	protected	String	url;
	protected	String	sessionKey;
	protected	String	dataCacheId;
	protected	String	infoBuffer="";
	
	public	TestGetDataAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = super.execute();
		url = "";
		
		//	Set the url to the first get data action.
		String t = this.type;
		String r = "role";
		String c = this.component;
		String n = this.name;
		
		if (name.equals("ui_strings"))
		{
			n = "tooltips";
		}
		else if (name.equals("tooltips"))
		{
			n = "default_layout";
		}
		else if (name.equals("default_layout"))
		{
			c = "global_strings";
			n = "emoticons";
		}
		else if (name.equals("emoticons"))
		{
			c = "session_string";
			n = "user_info"+this.dataCacheId;
		}
		else if (component.equals("session_string"))
		{
			this.url = "/"+ConferenceConsoleConstants.getWebappName()+"/test/consoleloaded.action?"+
				"sessionKey="+sessionKey+"&t="+System.currentTimeMillis();
		}
		
		if (url.length() == 0)
		{
			this.url = "/"+ConferenceConsoleConstants.getWebappName()+"/test/getdata.action?"+
				"type="+t+"&role="+r+"&component="+c+"&name="+n+
				"&sessionKey="+sessionKey+"&dataCacheId="+dataCacheId;
		}
		
		return	ret;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getDataCacheId()
	{
		return dataCacheId;
	}
	public void setDataCacheId(String dataCacheId)
	{
		this.dataCacheId = dataCacheId;
	}
	public String getSessionKey()
	{
		return sessionKey;
	}
	public void setSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	public String getInfoBuffer()
	{
		return infoBuffer;
	}
	public void setInfoBuffer(String infoBuffer)
	{
		this.infoBuffer = infoBuffer;
	}
}
