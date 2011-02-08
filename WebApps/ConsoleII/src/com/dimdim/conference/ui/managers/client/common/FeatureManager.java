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

package com.dimdim.conference.ui.managers.client.common;

import com.dimdim.conference.ui.model.client.CommandExecProgressListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class FeatureManager
{
	protected	CommandExecProgressListener	progressListener;
	
	public	FeatureManager()
	{
		
	}
	public CommandExecProgressListener getProgressListener()
	{
		return progressListener;
	}
	public void setProgressListener(CommandExecProgressListener progressListener)
	{
		this.progressListener = progressListener;
	}
	protected	void	setProgressMessage(String message)
	{
		if (this.progressListener != null)
		{
			this.progressListener.setProgressMessage(message);
		}
	}
	public	void	commandExecSuccess(String message)
	{
		if (this.progressListener != null)
		{
			this.progressListener.commandExecSuccess(message);
		}
	}
	public	void	commandExecError(String message)
	{
		if (this.progressListener != null)
		{
			this.progressListener.commandExecError(message);
		}
	}
}
