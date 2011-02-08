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

import com.dimdim.conference.action.check.Connect;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */

public class TestConnectAction	extends	Connect
{
	protected	String	infoBuffer="";
	
	public	TestConnectAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = super.execute();
		
		//	Set the url to the first get data action.
		if (action.equals("host"))
		{
			this.url = this.url.replaceFirst("CreateAndStartConference", "test/start");
		}
		else
		{
			this.url = this.url.replaceFirst("JoinConference", "test/join");
		}
		
		return	ret;
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
