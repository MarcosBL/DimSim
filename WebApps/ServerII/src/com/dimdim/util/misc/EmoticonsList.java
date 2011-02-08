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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class reads a list of supported emoticons from a given csv( comma
 * seperated values ) file. This class expects only 2 fields in the file.
 * 0: time zone id that can be directly used in TimeZome.getTimeZone(id)
 * method.
 * 1: a descriptive name that will be displayed to user for choosing the
 * time zome option from a form.
 */

public class EmoticonsList
{
	protected	String	fileName;
//	protected	File	file;
	protected	long	lastUpdateTime = 0;
	
	protected	Vector	emoticonsList;
	
//	public	EmoticonsList(File file)
//	{
//		this.file = file;
//		this.emoticonsList = new Vector();
//	}
	public	EmoticonsList(String fileName)
	{
		this.fileName = fileName;
//		this.file = new File(fileName);
	}
	public	Vector getEmoticonsList()
	{
		if (this.emoticonsList == null)
		{
			this.emoticonsList = new Vector();
			File file = new File(fileName);
			if (file.exists())
			{
				if (this.lastUpdateTime == 0 ||
						this.lastUpdateTime < file.lastModified())
				{
					readEmoticons(file);
				}
			}
		}
		return	this.emoticonsList;
	}
	protected	void	readEmoticons(File file)
	{
		try
		{
			CSVFileReader reader = new CSVFileReader(file,",");
			Vector v = reader.getNextLine();
			while (v != null && v.size() > 1)
			{
				String emoticonName = (String)v.elementAt(0);
//				System.out.println("Read time zone id:"+timeZoneId);
				String emoticonValue = (String)v.elementAt(1);
				
				emoticonsList.addElement(new Emoticon(emoticonName, emoticonValue));
				
				v = reader.getNextLine();
			}
			
			this.lastUpdateTime = file.lastModified();
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
