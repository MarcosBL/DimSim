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
import com.dimdim.conference.action.roster.ConsoleLoadedAction;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */

public class TestConsoleLoadedAction	extends	ConsoleLoadedAction
{
	protected	String	url;
	protected	String	infoBuffer="";
	
	public	TestConsoleLoadedAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = super.execute();
		
		//	Set the url to the first get data action.
		this.url = "/"+ConferenceConsoleConstants.getWebappName()+"/test/getevents.action?"+
			"sessionKey="+sessionKey+"&cflag="+System.currentTimeMillis();
		
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
	public String getInfoBuffer()
	{
		return infoBuffer;
	}
	public void setInfoBuffer(String infoBuffer)
	{
		this.infoBuffer = infoBuffer;
	}
}
