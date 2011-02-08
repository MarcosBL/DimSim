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

package com.dimdim.util.resources;

import	java.io.FileWriter;
import	java.io.BufferedWriter;

import	java.util.List;
import	java.util.Vector;
import	java.util.HashMap;
import	java.util.Locale;
import	java.util.ResourceBundle;
import	java.util.StringTokenizer;
import	java.util.Enumeration;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This generic parser provides a heirarchical stricture out of a properties
 * file. 
 */
public class PropertiesParser
{
	public	static	void	main(String[] args)
	{
		String	moduleName = "com.dimdim.conference.ui.common.server.attendeelogin";
		String	outputFileName = null;
		String	localeName = null;
		ResourceBundle rb = null;
		
		try
		{
			Locale locale = null;
			if (localeName != null)
			{
				try
				{
					locale = new Locale(localeName);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			if (locale != null)
			{
				rb = ResourceBundle.getBundle(moduleName, locale);
			}
			else
			{
				rb = ResourceBundle.getBundle(moduleName);
			}
			outputFileName = moduleName;
			if (locale != null)
			{
				outputFileName += outputFileName+"_"+localeName;
			}
			outputFileName += ".js";
			if (rb != null)
			{
				PropertiesParser gpp = new PropertiesParser(rb);
				HashMap map = gpp.parseBundle();
				System.out.println("The property bundle as a tree:"+map);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected	String			moduleName;
	protected	ResourceBundle	bundle;
	protected	String			outputFileName;
	
	public	PropertiesParser(ResourceBundle rb)
	{
		this.bundle = rb;
	}
	
	public	HashMap	parseBundle()
	{
		Enumeration keys = this.bundle.getKeys();
		while (keys.hasMoreElements())
		{
			String	key = (String)keys.nextElement();
			Vector v = this.parseKey(key);
			
		}
		return	null;
	}
	
	protected	Vector	parseKey(String key)
	{
		Vector	v = new Vector();
		StringTokenizer st = new StringTokenizer(key,".");
		while (st.hasMoreTokens())
		{
			v.addElement(st.nextToken());
		}
		return	v;
	}
}
