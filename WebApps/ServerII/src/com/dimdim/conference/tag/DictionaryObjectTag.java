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

import	java.io.*;
import	javax.servlet.jsp.*;
import	javax.servlet.jsp.tagext.*;

import	java.util.Locale;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This jsp tag adds {_xx} extension for the current locale. It is the caller's
 * responsibility to ensure that the file with so generated name is available at
 * the specified location on the server.
 */
public class DictionaryObjectTag	extends	SessionLocaleManagementTag
{
	protected	String	component;
	protected	String	dictionary;
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	public	int	doEndTag()
	{
		try
		{
			StringBuffer buf = new StringBuffer();
			try
			{
				LocaleManager lm = LocaleManager.getManager();
				if (lm != null)
				{
					Locale currentLocale = super.getCurrentLocale();
					if (currentLocale == null)
					{
						currentLocale = LocaleManager.getManager().getDefaultLocale();
					}
					buf.append("<script language='javascript'>\n");
					//buf.append(lm.getDictionaryBuffer(component,dictionary,currentLocale, userType));
					buf.append(lm.getDictionaryBuffer(component,dictionary,currentLocale, userType));
					buf.append("</script>\n");
				}
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
			pageContext.getOut().println(buf);
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
	public String getUserType()
	{
	    return userType;
	}
	public void setUserType(String userType)
	{
	    this.userType = userType;
	}
}
