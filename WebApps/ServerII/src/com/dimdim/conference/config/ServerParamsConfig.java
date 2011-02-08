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

package com.dimdim.conference.config;

import java.util.HashMap;
import java.util.ResourceBundle;

import java.io.*;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ServerParamsConfig
{
	public	static	final	String	maxMeetingTimeMinutes_Name = "maxMeetingTimeMinutes";
	
	private	static	ServerParamsConfig	serverParamsConfig;
	
	public	static	ServerParamsConfig	getServerParamsConfig()
	{
		if (ServerParamsConfig.serverParamsConfig == null)
		{
			ServerParamsConfig.createServerParamsConfig();
		}
		return	ServerParamsConfig.serverParamsConfig;
	}
	protected	synchronized	static	void	createServerParamsConfig()
	{
		if (ServerParamsConfig.serverParamsConfig == null)
		{
			ServerParamsConfig.serverParamsConfig = new ServerParamsConfig();
		}
	}
	
	private	HashMap		serverParams;
	
	private	ServerParamsConfig()
	{
		
	}
	
	/**
	 * This method is used by those who require access generically. This is
	 * at present the ui params tag which will read all the parameters and add
	 * to the console and forms data pages to be used by the console and the
	 * form respectively.
	 * 
	 * @return
	 */
	public	HashMap	getServerParams()
	{
		return	this.serverParams;
	}
	
	/**
	 * The specific methods are more useful and are available for those who need
	 * to actually use the parameters. These methods are always guarrateed to return
	 * a valid value so that the caller is not required to check for the validity
	 * of the return. The values generally considered invalid, such as -ve integers
	 * or null strings are not returned by these methods unless they are valid for
	 * the parameter concerned.
	 */
	/**
	 *	Longest supportable meeting is 5 hours. No presenter will be allowed to
	 *	specify a meeting time longer than this.
	 */
	public	int	getMaxMeetingTimeMinutes()
	{
		return	300;
	}
	/**
	 * This is the maximum number of participants in any meeting at a time for a
	 * Dimdim server. Any presenter will not be allowed to exceed this limit.
	 * 
	 * @return
	 */
	public	int	getMaxParticipantsInMeeting()
	{
		return	50;
	}
}

