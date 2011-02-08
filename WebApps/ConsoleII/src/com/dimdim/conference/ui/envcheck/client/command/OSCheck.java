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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Frame;

import com.dimdim.conference.ui.envcheck.client.layout.CheckPanel;
import com.dimdim.conference.ui.envcheck.client.EnvGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class OSCheck	extends	Check
{
	public	static	final	int	CheckIndex	=	0;
	public	static	final	String	CheckName	=	"os";
	
	protected	String	osType;
	
	public	OSCheck(String osType)
	{
		this.osType = osType;
	}
	/**
	 * This method should be executed only if the check is applicable
	 * The check process itself considers the command and will return
	 * success or failure.
	 */
	public	int		runCheck(CheckPanel panel, UserCommand command)
	{
		//	Set the panel text to checking and helptext frame to neutral
		//	check explaination.
		//Window.alert("os check..... "+command.isCheckApplicable(OSCheck.CheckIndex));
		if (command.isCheckApplicable(OSCheck.CheckIndex))
		{
			if(osType.equals(EnvGlobals.OS_WINDOWS) || osType.equals(EnvGlobals.OS_MAC)
					|| osType.equals(EnvGlobals.OS_LINUX) || osType.equals(EnvGlobals.OS_UNIX) )
			{
			    //Window.alert("before setting check suceeded panel = "+panel);
			    //Window.alert("message = "+getSuccessMessage(OSCheck.CheckIndex,
				//	command.getCommandId(), this.osType));
				panel.setCheckSucceeded(getSuccessMessage(OSCheck.CheckIndex,
						command.getCommandId(), this.osType));
				return	Check.CHECK_SUCCESS;	
			}else{
				//	Set the panel text to check failed and frame to the helptext
				//	url with the failure code.
				//panel.setCheckFailed(getFailureMessage(OSCheck.CheckIndex,command.getCommandId(), this.osType));
				
				panel.setCheckFailed(EnvGlobals.getCheckFailedText(OSCheck.CheckIndex,command.getCommandId(), this.osType,null,null,0,0));
	
				return	Check.CHECK_FAILURE;
			}
		}else{
		    //panel.setCheckNotApplicable();
		}
		//panel.setCheckSucceeded(getSuccessMessage(OSCheck.CheckIndex,
		//		command.getCommandId(), this.osType));
		return	Check.CHECK_SUCCESS;
	}
}
