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

package com.dimdim.conference.action.portal;

import com.dimdim.conference.application.portal.MeetingSettings;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class StartScheduledMeetingAction extends PortalAdapterAction
{
	//	Input parameters that will be posted to the action by the portal's
	//	start new meeting form.
	
	protected	String	name;
	protected	String	displayName;
	protected	String	key;
	protected	String	password;
	
	protected	MeetingSettings		settings;
	
	//	The return url. If the meeting can be started.
	
	protected	String		url = "";
	
	public	StartScheduledMeetingAction()
	{
		
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		if (settings == null)
		{
			settings = new MeetingSettings();
		}
		return	ret;
	}
	public MeetingSettings getSettings()
	{
		return settings;
	}
	public void setSettings(MeetingSettings settings)
	{
		this.settings = settings;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
}
