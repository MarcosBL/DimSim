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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public abstract	class UserCommand
{
	protected	HashMap	checkResults	=	new	HashMap();
	
	public	abstract	String		getCommandId();
	
	public	abstract	boolean		isCheckApplicable(int checkIndex);
	
	/**
	 * This en masse check may apply infrequently.
	 */
	public	boolean	anyCheckApplicable()
	{
		return	true;
	}
	public	void	setCheckResult(Integer checkIndex, Boolean result)
	{
		this.checkResults.put(checkIndex, result);
	}
}
