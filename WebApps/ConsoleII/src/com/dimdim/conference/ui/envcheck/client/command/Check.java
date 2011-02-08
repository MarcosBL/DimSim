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

public abstract	class Check
{
	public	static	final	int	CHECK_SUCCESS	=	1;
	public	static	final	int	CHECK_FAILURE	=	2;
	public	static	final	int	CHECK_REPEAT	=	3;
	
	protected	int		panelWidth;
	protected	int		panelHeight;
	
	public	Check()
	{
	}
	public void setPanelHeight(int panelHeight)
	{
		this.panelHeight = panelHeight;
	}
	public void setPanelWidth(int panelWidth)
	{
		this.panelWidth = panelWidth;
	}
	/**
	 * This method returns success or failure for the check.
	 * 
	 * @param panel
	 * @param helptextFrame
	 * @param command
	 * @return
	 */
	public	abstract	int		runCheck(CheckPanel panel, UserCommand command);
	
	protected	String	getInProgressMessage(int checkIndex, String actionId, String s1)
	{
		return	EnvGlobals.getCheckInProgressText(checkIndex, actionId, s1, null, null, null, 0);
	}
	protected	String	getSuccessMessage(int checkIndex, String actionId, String s1)
	{
		return	EnvGlobals.getCheckSucceededText(checkIndex, actionId, s1, null, 0, 0);
	}
	
	protected	String	getSuccessMessage(int checkIndex, String actionId, String s1, String s2)
	{
		return	EnvGlobals.getCheckSucceededText(checkIndex, actionId, s1, s2, 0, 0);
	}
	
	protected	String	getFailureMessage(int checkIndex, String actionId, String s1)
	{
		return	EnvGlobals.getCheckSucceededText(checkIndex, actionId, s1, null, 0, 0);
	}
	protected	String	getSuccessMessage(int checkIndex, String actionId, int i1, int i2)
	{
		return	EnvGlobals.getCheckSucceededText(checkIndex, actionId, null, null, i1, i2);
	}
	protected	String	getFailureMessage(int checkIndex, String actionId, int i1, int i2)
	{
		return	EnvGlobals.getCheckSucceededText(checkIndex, actionId, null, null, i1, i2);
	}
}
