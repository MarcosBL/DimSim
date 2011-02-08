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

package com.dimdim.conference.application.portal;

import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object caches the user information so that it doesn't have to be
 * carried back and forth between the signin page and server.
 */

public class UserInfo
{
	protected	String	email;
	protected	String	displayName;
	protected	String	name;
	protected	String	confKey;
	protected	String 	role = LocaleResourceFile.FREE;
	
	public UserInfo()
	{
	}
	public UserInfo(String email, String displayName,
				String name, String confKey, String role)
	{
		this.email = email;
		this.displayName = displayName;
		this.name = name;
		this.confKey = confKey;
		this.role = role;
	}
	public String getConfKey()
	{
		return confKey.toLowerCase();
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getRole()
	{
	    return role;
	}
}

