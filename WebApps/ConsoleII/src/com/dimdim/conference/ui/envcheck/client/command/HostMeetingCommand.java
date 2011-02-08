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

package com.dimdim.conference.ui.envcheck.client.command;

import	java.util.HashMap;
import com.google.gwt.user.client.Window;
import com.dimdim.conference.ui.envcheck.client.EnvGlobals;
import com.dimdim.conference.ui.envcheck.client.FormGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class HostMeetingCommand	extends	UserCommand
{
	
	public	HostMeetingCommand()
	{
	}
	public	String		getCommandId()
	{
		return	EnvGlobals.ACTION_HOST_MEETING;
	}
	/**
	 * Meet now command requires all the checks to be performed.
	 */
	public	boolean		isCheckApplicable(int checkIndex)
	{
		if (checkIndex == PublisherCheck.CheckIndex)
		{
			return	EnvGlobals.isPublisherSupportable();
		}
		return	true;
	}
	public	boolean		anyCheckApplicable()
	{
		return	true;
	}
}
