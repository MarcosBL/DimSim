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
import com.dimdim.conference.action.common.GetConsolePageAction;
import com.dimdim.conference.application.UserSession;
import com.dimdim.util.session.UserSessionData;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */

public class TestGetConsolePageAction	extends	GetConsolePageAction
{
	protected	String	url;
	protected	String	confKey;
	protected	String	sessionKey;
	protected	String	dataCacheId;
	protected	String	infoBuffer="";
	
	public	TestGetConsolePageAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		UserSession userSession = (UserSession)UserSessionDataManager.getDataManager().
				getObject(uri, UserSessionData.ACTIVE_USER_SESSION);
		this.sessionKey = userSession.getSessionKey();
		
		String	ret = super.execute();
		dataCacheId = (String)servletRequest.getAttribute("dataCacheId");
		
		//	Set the url to the first get data action.
		this.url = "/"+ConferenceConsoleConstants.getWebappName()+"/test/getdata.action?"+
			"type=dictionary&confKey=xyz&role=free&component=console&name=ui_strings"+
			"&sessionKey="+sessionKey+"&dataCacheId="+dataCacheId;
		
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
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
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
