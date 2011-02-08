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

package com.dimdim.util.misc;

import	java.io.File;
import	java.util.Vector;
import	java.util.HashMap;
import	java.util.Date;

/**
 * @author Rohit Shankar
 * @email rohit.shankar@communiva.com
 * 
 * This class reads a list of supported timezones from a given csv( comma
 * seperated values ) file. This class expects only 2 fields in the file.
 * 0: time zone id that can be directly used in TimeZome.getTimeZone(id)
 * method.
 * 1: a descriptive name that will be displayed to user for choosing the
 * time zome option from a form.
 */

public class LocaleList
{
	protected	String	fileName;
	protected	File	file;
	protected	long	lastUpdateTime = 0;
	protected	HashMap	locales;

//	public	LocaleList(File file)
//	{
//		this.file = file;
//	}
	public	LocaleList(String fileName)
	{
		this.fileName = fileName;
		this.file = new File(fileName);
	}
	public	String	getLocaleEncoding(String locale)
	{
		if (this.lastUpdateTime == 0 ||
				this.lastUpdateTime < file.lastModified())
		{
			readLocaleList();
		}
		return	(String)this.locales.get(locale);
	}
	protected	void	readLocaleList()
	{
		try
		{
			this.locales = new HashMap();
			CSVFileReader reader = new CSVFileReader(this.file,";");
			Vector v = reader.getNextLine();
			while (v != null && v.size() > 1)
			{
				this.locales.put(v.elementAt(0),v.elementAt(1));
				
				v = reader.getNextLine();
			}
			this.lastUpdateTime = file.lastModified();
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
//		return "ISO-8859-1";
	}
}
