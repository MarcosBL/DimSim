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
import com.dimdim.conference.action.roster.JoinConferenceAction;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */

public class JoinMeetingTestAction	extends	JoinConferenceAction
{
	protected	String	infoBuffer="";
	
	public	JoinMeetingTestAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = super.execute();
		
		this.url = "/"+ConferenceConsoleConstants.getWebappName()+"/test/getconsolepage.action?"+
			"confKey="+this.confKey+"&t="+System.currentTimeMillis();
	
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
