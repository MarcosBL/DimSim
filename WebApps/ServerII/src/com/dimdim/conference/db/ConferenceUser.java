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

package com.dimdim.conference.db;

import	java.io.Serializable;

/**
 * 
 */

public	class	ConferenceUser	implements	Comparable, Serializable
{
	
	/**
	 * 
	 */
	protected	String	email;
	protected	String	password;
	
	public	ConferenceUser()
	{
	}
	public ConferenceUser(String email, String password)
	{
		this.email = email;
		this.password = password;
	}
	public	int	compareTo(Object obj)
	{
		if (obj instanceof ConferenceUser)
		{
			return	this.email.compareTo(((ConferenceUser)obj).getEmail());
		}
		else
		{
			return	1;
		}
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String toString()
	{
		return	"email: "+this.email+", password:"+this.password;
	}
}
