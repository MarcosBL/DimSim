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

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;
import com.dimdim.conference.ui.envcheck.client.layout.CheckPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class BrowserTypeCheck	extends	Check
{
	public	static	final	int	CheckIndex	=	1;
	public	static	final	String	CheckName	=	"browser";
	
	protected	String	browserType;
	
	public	BrowserTypeCheck(String browserType)
	{
		this.browserType = browserType;
	}
	/**
	 * This method should be executed only if the check is applicable
	 * The check process itself considers the command and will return
	 * success or failure
	 */
	public	int		runCheck(CheckPanel panel, UserCommand command)
	{
		//	Set the panel text to checking and helptext frame to neutral
		//	check explaination.
		//Window.alert("result of first if = "+command.isCheckApplicable(BrowserTypeCheck.CheckIndex));
		//Window.alert("result of second  if = "+(!browserType.equals(EnvGlobals.BROWSER_IE) &&
		//	!browserType.equals(EnvGlobals.BROWSER_FIREFOX) &&
		//	!browserType.equals(EnvGlobals.BROWSER_SAFARI)));
		if (command.isCheckApplicable(BrowserTypeCheck.CheckIndex) &&
				(!browserType.equals(EnvGlobals.BROWSER_IE) &&
						!browserType.equals(EnvGlobals.BROWSER_FIREFOX) &&
						!browserType.equals(EnvGlobals.BROWSER_SAFARI)))
		{
			//	Set the panel text to check failed and frame to the helptext
			//	url with the failure code.
			panel.setCheckFailed(getFailureMessage(BrowserTypeCheck.CheckIndex,
					command.getCommandId(), this.browserType));
			//Window.alert("returing check fail for browser browserType="+browserType);
			return	Check.CHECK_FAILURE;
		}
		panel.setCheckSucceeded(getSuccessMessage(BrowserTypeCheck.CheckIndex,
				command.getCommandId(), this.browserType ));
		
		//panel.setCheckFailed(getFailureMessage(BrowserTypeCheck.CheckIndex,
		//	command.getCommandId(), this.browserType));
		//Window.alert("returing check success for browser browserType="+getSuccessMessage(BrowserTypeCheck.CheckIndex,
		//	command.getCommandId(), this.browserType));
		return	Check.CHECK_SUCCESS;
	}
}
