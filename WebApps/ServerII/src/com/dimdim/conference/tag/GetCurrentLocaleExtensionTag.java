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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This jsp tag adds {_xx} extension for the current locale. It is the caller's
 * responsibility to ensure that the file with so generated name is available at
 * the specified location on the server.
 */
public class GetCurrentLocaleExtensionTag	extends	TagSupport
{
	protected	String	moduleName;
	
	public	int	doEndTag()
	{
		try
		{
			String includeLine = "<script language='javascript' src='"+moduleName+".js'/>";
			try
			{
				Locale currentLocale = (Locale)pageContext.getSession().getAttribute("CURRENT_LOCALE");
				if (currentLocale != null)
				{
					//	Some other parts of the conference console webapp is responsible
					//	for setting the locale only if it is fully supported.
					String locale_extn = currentLocale.toString();
					includeLine = "<script language='javascript' src='"+moduleName+"_"+locale_extn+".js'/>";
				}
			}
			catch(Exception e2)
			{
				includeLine = "<script language='javascript' src='"+moduleName+".js'/>";
			}
			pageContext.getOut().println(includeLine);
		}
		catch(Exception e)
		{
		}
		return	EVAL_PAGE;
	}

	public String getModuleName()
	{
		return this.moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}
}
