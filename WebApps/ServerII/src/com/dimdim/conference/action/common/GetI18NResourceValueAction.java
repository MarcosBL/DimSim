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
 
package com.dimdim.conference.action.common;

import java.util.Locale;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class GetI18NResourceValueAction	extends	CommonDimDimAction
{
	protected	String	localeCode;
	protected	String	component;
	protected	String	dictionary;
	protected	String	key;
	protected	String	defaultValue;
	protected	String	value;
	
	public	GetI18NResourceValueAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		try
		{
			value = defaultValue;
			if (value == null)
			{
				value = key;
			}
			try
			{
				LocaleManager lm = LocaleManager.getManager();
				Locale locale = lm.getSupportedLocale(localeCode);
				if (locale == null)
				{
					locale = lm.getDefaultLocale();
				}
				value = lm.getResourceKeyValue(component, dictionary, locale, 
					key, LocaleResourceFile.FREE);
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return	SUCCESS;
	}
	public String getComponent()
	{
		return component;
	}
	public void setComponent(String component)
	{
		this.component = component;
	}
	public String getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	public String getDictionary()
	{
		return dictionary;
	}
	public void setDictionary(String dictionary)
	{
		this.dictionary = dictionary;
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getLocaleCode()
	{
		return localeCode;
	}
	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
}
