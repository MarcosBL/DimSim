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
 
package com.dimdim.conference.tag;

import	javax.servlet.jsp.tagext.*;
import	java.util.Locale;
import	java.util.ResourceBundle;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This jsp tag places the localized value of the given key. If the key is
 * not found in the specified dictionary, then the default value is used.
 */
public class I18NDisplayStringTag	extends	SessionLocaleManagementTag
{
	protected	String	component;
	protected	String	dictionary;
	protected	String	key;
	protected	String	userType;
	protected	String	defaultValue;
	
	public	int	doEndTag()
	{
		try
		{
			if (userType == null)
			{
				userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
			}
			String value = defaultValue;
			if (value == null)
			{
				value = key;
			}
			try
			{
				LocaleManager lm = LocaleManager.getManager();
				Locale currentLocale = super.getCurrentLocale();
				if (currentLocale == null)
				{
					currentLocale = lm.getDefaultLocale();
				}
				value = lm.getResourceKeyValue(component,dictionary,currentLocale,key, userType);
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
			
			
			pageContext.getOut().print(value);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	EVAL_PAGE;
	}
	public String getComponent()
	{
		return component;
	}
	public void setComponent(String component)
	{
		this.component = component;
	}
	public String getDictionary()
	{
		return dictionary;
	}
	public void setDictionary(String dictionary)
	{
		this.dictionary = dictionary;
	}
	public String getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getUserType()
	{
		return userType;
	}
	public void setUserType(String userType)
	{
		this.userType = userType;
	}
}
