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

package com.dimdim.conference.application.email;

import java.util.ResourceBundle;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class MailResourceReader	extends	Exception
{
	private	static	MailResourceReader	reader;
	
	public	static	MailResourceReader	getReader()
	{
		if (MailResourceReader.reader == null)
		{
			MailResourceReader.createReader();
		}
		return	MailResourceReader.reader;
	}
	protected	synchronized	static	void	createReader()
	{
		MailResourceReader.reader = new MailResourceReader();
	}
	
    public static final String DEFAULT_MAILER_RESOURCES = "resources.dimdim";
    public static final String USER_MAILER_RESOURCES = "resources.UserConference";
    
    protected	ResourceBundle	defaultResources;
    protected	ResourceBundle	userResources;
    
	private MailResourceReader()
	{
		try
		{
			this.defaultResources = ResourceBundle.getBundle(MailResourceReader.DEFAULT_MAILER_RESOURCES);
		}
		catch(Exception e)
		{
		}
		try
		{
			this.userResources = ResourceBundle.getBundle(MailResourceReader.USER_MAILER_RESOURCES);
		}
		catch(Exception e)
		{
		}
	}
	
	public	String	getResourceString(String key)
	{
		return	this.getResourceString(key,null);
	}
	public	String	getResourceString(String key, String defaultValue)
	{
		String value = this.getResourceString(this.userResources,key);
		if (value == null)
		{
			value = this.getResourceString(this.defaultResources,key);
			if (value == null)
			{
				value = defaultValue;
			}
		}
		return	value;
	}
	protected	String	getResourceString(ResourceBundle rb, String key)
	{
		String	value = null;
		try
		{
			if (rb != null)
			{
				value = rb.getString(key);
			}
		}
		catch(Exception e)
		{
		}
		return	value;
	}
}
