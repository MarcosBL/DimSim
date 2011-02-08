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
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class JoinMeetingCommand	extends	UserCommand
{
	protected	boolean		asPresenter	=	false;
	
	public	JoinMeetingCommand()
	{
		
	}
	public	String		getCommandId()
	{
		return	EnvGlobals.ACTION_JOIN;
	}
	/**
	 * An attendee requires only the flash version check and bandwidth.
	 * All operating systems and browsers are supported and publisher
	 * is not required.
	 */
	public	boolean		isCheckApplicable(int checkIndex)
	{
		if (this.asPresenter)
		{
			//	All checks apply for presenters.
		    //Window.alert("joining as presenter  so returning true....");
			return	true;
		}
		else
		{
			//	Only the flash player check and bandwidth chec is required.
		}
		//if (checkIndex == FlashVersionCheck.CheckIndex ||
		//		checkIndex == BrowserTypeCheck.CheckIndex)
		//{
		    //Window.alert("either flash or browser check so returning true....");
		    return	true;
		//}
		//Window.alert(" false....");
		//return	false;
	}
}
