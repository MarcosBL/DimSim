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
 * This small utility takes a properties file and creates javascript includes
 * for use the gwt modules. The properties file is parsed and made available
 * to the gwt code through a javascript containning the properties contents
 * listed to be readable through the gwt native methods.
 */
public class UIResourcesWriter
{
	public	static	String	lineSeparator = "\n";
	
	static
	{
		try
		{
			UIResourcesWriter.lineSeparator = (String)System.getProperty("line.separator");
		}
		catch(Exception e)
		{
		}
		if (UIResourcesWriter.lineSeparator == null)
		{
			UIResourcesWriter.lineSeparator = "\n";
		}
	}
	
	public	static	void	main(String[] args)
	{
		String	moduleName = "attendeelogin";
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
				outputFileName = outputFileName+"_"+localeName;
			}
			outputFileName += ".js";
			if (rb != null)
			{
				UIResourcesWriter writer = new UIResourcesWriter(moduleName, rb, outputFileName);
				writer.writeFile();
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
	
	public	UIResourcesWriter(String moduleName,
				ResourceBundle bundle, String outputFileName)
	{
		this.moduleName = moduleName;
		this.bundle = bundle;
		this.outputFileName = outputFileName;
	}
	public	void	writeFile()
	{
		try
		{
			//	Open file.
			FileWriter  fw = new FileWriter(this.outputFileName);
			BufferedWriter bfw = new BufferedWriter(fw);
			
			//	Get the widget list.
			String widgetsList = this.bundle.getString(moduleName+".widgets");
			List widgetNames = this.parseCSVString(widgetsList);
			int	size = widgetNames.size();
			for (int i=0; i<size; i++)
			{
				String widgetName = (String)widgetNames.get(i);
				String fieldsList = this.bundle.getString(moduleName+"."+widgetName+".fields");
				List fieldNames = this.parseCSVString(fieldsList);
				
				System.out.println("Writing Widget:"+widgetName);
				writeWidgetToFile(widgetName, fieldNames, bfw);
			}
			
			bfw.flush();
			bfw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected	void	writeWidgetToFile(String widgetName, List fieldNames, BufferedWriter fw )
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("var ");
		buf.append(moduleName);
		buf.append("_");
		buf.append(widgetName);
		buf.append(" = {");
		buf.append(UIResourcesWriter.lineSeparator);
		
		int numFields = fieldNames.size();
		for (int j=0; j<numFields; j++)
		{
			String fieldName = (String)fieldNames.get(j);
			String fieldKey = this.moduleName+"."+widgetName+"."+fieldName+".";
			int	propertyNameStartIndex = fieldKey.length();
			
			System.out.println("Writing field:"+fieldName);
			if (j>0)
			{
				buf.append(",");
				buf.append(UIResourcesWriter.lineSeparator);
			}
			buf.append(fieldName);
			buf.append(":{");
			//	now go through all the keys available for that field. Lets not restrict
			//	the proprties someone might want to provide for the widgets.
			int	propertyCount = 0;
			Enumeration keys = this.bundle.getKeys();
			while (keys.hasMoreElements())
			{
				String key = (String)keys.nextElement();
				System.out.println("Checking key:"+key);
				if (key.startsWith(fieldKey))
				{
					String propertyName = key.substring(propertyNameStartIndex);
					String propertyValue = this.bundle.getString(key);
					
					if (propertyCount++ > 0)
					{
						buf.append(",");
					}
					buf.append(propertyName);
					buf.append(":\"");
					buf.append(propertyValue);
					buf.append("\"");
				}
			}
			
			buf.append("}");
		}
		
		buf.append( UIResourcesWriter.lineSeparator );
		buf.append( "}" );
		buf.append( UIResourcesWriter.lineSeparator );
		
		try
		{
			fw.write(buf.toString());
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	protected	List	parseCSVString(String s)
	{
		Vector v = new Vector();
		StringTokenizer parser = new StringTokenizer(s,",");
		while (parser.hasMoreTokens())
		{
			v.addElement(parser.nextToken());
		}
		return	v;
	}
}
