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

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.HashMap;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * As the name suggests, objects of this class represent a file with data in
 * some form that the product might use. Three basic types are in use right now:
 * HTML, TEXT and RESOURCE. Technically HTML and TEXT are same the flag is
 * provided for information reason only. The resource file is made available
 * as a resource bundle. The reason for providing a specific reader for the
 * resources rather than using the path based lookup is that the specific
 * reader can refresh the resource bundle on line. The java resource bundle
 * does not seem to refresh the bundle from disk when asked for again.
 */

public class ResourceFile
{
	public	static	final	String	HTML_FILE	=	"HTML";
	public	static	final	String	TEXT_FILE	=	"TEXT";
	public	static	final	String	RESOURCE_BUNDLE_FILE	=	"RESOURCE";
	
	protected	String		filePath = null;
	protected	long		lastFileUpdateTime = 0;
	protected	HashMap		resources;
	
	public	ResourceFile(String filePath)
	{
		this.filePath = filePath;
		this.checkAndReadUpdate();
	}
	public	HashMap	getResources()
	{
		return	this.resources;
	}
	/**
	 * This method returns true if the file was freshly read from into the
	 * resource bundle.
	 * 
	 * @return
	 */
	public	boolean	checkAndReadUpdate()
	{
		boolean	b = false;
		try
		{
			File file = new File(filePath);
			long fileUpdateTime = file.lastModified();
			if (this.lastFileUpdateTime == 0 || this.lastFileUpdateTime < fileUpdateTime)
			{
				this.lastFileUpdateTime = fileUpdateTime;
				b = readFile(file);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			b = false;
		}
		return	b;
	}
	protected	boolean	readFile(File file)
	{
		boolean	b = false;
		try
		{
			this.resources = new HashMap();
			FileInputStream fis = new FileInputStream(file);
			PropertyResourceBundle resourceBundle = new PropertyResourceBundle(fis);
			System.out.println("Reading resource bundle *************** :"+resourceBundle);
			Enumeration e1 = resourceBundle.getKeys();
			while (e1.hasMoreElements())
			{
				String key = (String)e1.nextElement();
				String str = resourceBundle.getString(key);
				this.resources.put(key,str);
			}
			b = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			b = false;
		}
		return	b;
	}
}
