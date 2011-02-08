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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                    *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.data.application;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class GeneralResourceDictionary
{
	protected	String	jsonBuffer = "{a:\"b\"}";
	protected	ArrayList keys = new ArrayList();
	
	public	GeneralResourceDictionary(String resourceName, Locale locale)
	{
		try
		{
			int	i = 0;
			StringBuffer buf = new StringBuffer();
			buf.append("{");
			
			keys.add("dimdim.serverAddress");
			keys.add("dimdim.serverPortNumber");
			keys.add("dimdim.webappName");
			
			ResourceBundle rb = ResourceBundle.getBundle(resourceName);
			Enumeration en = rb.getKeys();
			while (en.hasMoreElements())
			{
				String key = (String)en.nextElement();
				if(keys.contains(key)){
					String value = rb.getString(key);
					if (i++ > 0)
					{
						buf.append(",");
					}
					buf.append(key.replace('.', '_'));
					buf.append(":\"");
					buf.append(value);
					buf.append("\"");
				}
			}
			buf.append("}");
			
			this.jsonBuffer = buf.toString();
			System.out.println("inside ... general resource dictionary json = "+jsonBuffer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String getJsonBuffer()
	{
		return jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
}
