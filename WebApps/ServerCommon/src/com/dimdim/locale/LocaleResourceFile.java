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

package com.dimdim.locale;

import java.io.BufferedInputStream;
import	java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import	java.util.Locale;
import java.util.PropertyResourceBundle;
import	java.util.ResourceBundle;
import java.util.HashMap;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * As the name suggests objects of this class represent a single resource
 * dictionary or a file for a single component. The file could be used as
 * a complete text file, e.g. email template or a property resource bundle.
 * These are the only two types supported at present.
 */

public class LocaleResourceFile
{
	public	static	final	int	HTML_FILE		=	1;
	public	static	final	int	RESOURCE_BUNDLE_FILE	=	2;
	
	public static final String FREE = "free";
	public static final String PREMIUM = "Premium";
	
	protected	String	fileName;
	protected	String	component;
	protected	int		fileType;
	protected	Locale	locale;
	
	protected	LocaleResourceFile	defaultResourceFile;
	
	protected	String			textBuffer;
	protected	ResourceBundle	resourceBundle;
	protected	String			jsDictionaryBuffer;
	protected	String			jsonDictionaryBuffer;
	protected	String			paidJsonDictionaryBuffer;
	
	protected	File	file;
	protected	long	lastUpdateTime = 0;
	protected	String	paidResourceFileExt = "paid";
	private File paidFile;
	private PropertyResourceBundle paidResourceBundle;
	private String paidTextBuffer;
	String paidName = null;
	private String paidJsDictionaryBuffer;
	
	public	LocaleResourceFile(String componentDirPath, Locale locale,
				String component, String fileName, int fileType)
		throws	LocaleNotSupportedException
	{
		this.fileName = fileName;
		this.fileType = fileType;
		this.locale = locale;
		this.component = component;
		
		//	Check if the resource file for the given name, type
		//	and locale exists.
		String name = fileName +"_"+ locale.toString() +".properties";
		paidName = fileName +"_"+paidResourceFileExt+"_"+ locale.toString() +".properties";
		if (fileType == LocaleResourceFile.HTML_FILE)
		{
			name = fileName +"_"+locale.toString()+".html";
			paidName = fileName +"_"+paidResourceFileExt+"_"+locale.toString()+".html";
		}
		this.file = new File(componentDirPath, name);
		this.paidFile = new File(componentDirPath, paidName);
//		System.out.println("Looking for file: "+this.file.getAbsolutePath());
		if (!this.file.exists())
		{
			throw	new	LocaleNotSupportedException();
		}
//		System.out.println(" --------- File exists:"+this.file.getAbsolutePath());
	}
	public LocaleResourceFile getDefaultResourceFile()
	{
		return defaultResourceFile;
	}
	public void setDefaultResourceFile(LocaleResourceFile defaultResourceFile)
	{
		this.defaultResourceFile = defaultResourceFile;
	}
	public	synchronized	void	refresh()
	{
		if (this.file.lastModified() > this.lastUpdateTime)
		{
//			System.out.println("Refreshing dictionary file: "+this.fileName);
//			System.out.println("		Last update time  : "+this.lastUpdateTime);
//			System.out.println("		Last modified time: "+this.file.lastModified());
			this.textBuffer = null;
			this.resourceBundle = null;
			this.jsDictionaryBuffer = null;
			this.paidJsDictionaryBuffer = null;
			this.paidTextBuffer = null;
			this.paidJsonDictionaryBuffer = null;
			this.jsonDictionaryBuffer = null;
			this.getResourceBundle();
			//this.getFileContentBuffer();
			//this.getDictionaryBuffer();
		}
	}
	public	ResourceBundle	getResourceBundle()
	{
		if (this.resourceBundle == null)
		{
			//	Read the file into resource bundle.
			this.readResourceBundle();
		}
		return	this.resourceBundle;
	}
	
	public	ResourceBundle	getPaidResourceBundle()
	{
		if (this.paidResourceBundle == null)
		{
			//	Read the file into resource bundle.
			this.readResourceBundle();
		}
		return	this.paidResourceBundle;
	}
	
	public	String	getFileContentBuffer(String role)
	{
	    if(PREMIUM.equalsIgnoreCase(role))
	    {
		if (this.paidTextBuffer == null)
		{
			this.writeTextBuffer(role);
		}
		return	this.paidTextBuffer;
	    }else{
		if (this.textBuffer == null)
		{
			this.writeTextBuffer(role);
		}
		return	this.textBuffer;
	    }
	    
	}
	public	String	getDictionaryBuffer(String role)
	{
	    System.out.println("role is = "+role);
	    if(PREMIUM.equalsIgnoreCase(role))
	    {
		if (this.paidJsDictionaryBuffer == null)
		{
			this.paidJsDictionaryBuffer = this.writeJsDictionaryBuffer(false, role);
		}
		return	this.paidJsDictionaryBuffer;
	    }else{
		if (this.jsDictionaryBuffer == null)
		{
			this.jsDictionaryBuffer = this.writeJsDictionaryBuffer(false, "free");
		}
		return	this.jsDictionaryBuffer;
	    }
	}
	public	String	getDictionaryJsonBuffer(String role)
	{
	    if(PREMIUM.equalsIgnoreCase(role))
	    {
		if (this.paidJsonDictionaryBuffer == null)
		{
			this.paidJsonDictionaryBuffer = this.writeJsDictionaryBuffer(true, role);
		}
		return	this.paidJsonDictionaryBuffer;
	    }else{
		if (this.jsonDictionaryBuffer == null)
		{
			this.jsonDictionaryBuffer = this.writeJsDictionaryBuffer(true, role);
		}
		return	this.jsonDictionaryBuffer;
	    }
	}
	protected	synchronized	void	readResourceBundle()
	{
		try
		{
			FileInputStream fis = new FileInputStream(this.file);
			this.resourceBundle = new PropertyResourceBundle(fis);
			try
			{
				fis.close();
			}
			catch(Exception e)
			{
			}
			this.lastUpdateTime = this.file.lastModified();
			try
			{
			    if(null != this.paidFile)
			    {
        			    FileInputStream paidFis = new FileInputStream(this.paidFile);
        			    this.paidResourceBundle = new PropertyResourceBundle(paidFis);
        			    paidFis.close();
			    }
			}
			catch(Exception e)
			{
			    System.out.println("Exception locating the paid resource bundle "+paidName+" from component "+component);
			    //e.printStackTrace();
			}
			this.lastUpdateTime = this.file.lastModified();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private	synchronized	String	writeJsDictionaryBuffer(boolean jsonBuffer, String role)
	{
		String nl = "";
		if (!jsonBuffer)
		{
			nl = "\n";
		}
		HashMap keysRead = new HashMap();
		StringBuffer buf = new StringBuffer();
		ResourceBundle rb = this.getResourceBundle();
		ResourceBundle paidRb = this.getPaidResourceBundle();
		
		//here read the paid resource bundle and populate those values
		//if they are not present then follow the normal flow
		
		if (rb != null)
		{
			if (!jsonBuffer)
			{
				buf.append("var ");
				buf.append(component);
				buf.append("_");
				buf.append(fileName);
				buf.append(" = ");
			}
			buf.append("{");
			//trying to fill up the paid resource bundle keys
			Enumeration keys = null;
			int count = 0;
			if(paidRb != null && PREMIUM.equalsIgnoreCase(role))
			{
			    keys = paidRb.getKeys();
				while (keys.hasMoreElements())
				{
				    String key = (String)keys.nextElement();
				    //System.out.println("Adding key from free resource bundle: "+key);
				    String value = paidRb.getString(key);
				    if (key.length() > 0 && value.length() > 0)
					{
						if (count > 0)
						{
							buf.append(",");
							buf.append(nl);
						}
						else
						{
							buf.append(nl);
						}
						if (!jsonBuffer)
						{
							buf.append("\t\t");
						}
						buf.append(convertToJsIdString(key));
						buf.append(":\"");
						buf.append(value.replaceAll("\"","&quot;"));
						buf.append("\"");
						count++;
						keysRead.put(key,key);
					}
				}
			}
			
			//Enumeration 
			keys = rb.getKeys();
			//int count = 0;
			while (keys.hasMoreElements())
			{
				String key = (String)keys.nextElement();
//				System.out.println("Adding key: "+key);
				String value = rb.getString(key);
				if ( (keysRead.get(key) == null) && key.length() > 0 && value.length() > 0 )
				{
					if (count > 0)
					{
						buf.append(",");
						buf.append(nl);
					}
					else
					{
						buf.append(nl);
					}
					if (!jsonBuffer)
					{
						buf.append("\t\t");
					}
					buf.append(convertToJsIdString(key));
					buf.append(":\"");
					buf.append(value.replaceAll("\"","&quot;"));
					buf.append("\"");
					count++;
					keysRead.put(key,key);
				}
			}
			
			if (this.defaultResourceFile != null)
			{
				rb = this.defaultResourceFile.getResourceBundle();
				keys = rb.getKeys();
				while (keys.hasMoreElements())
				{
					String key = (String)keys.nextElement();
					String value = rb.getString(key);
					if (keysRead.get(key) == null)
					{
						//	This key was not in the locale file. Add.
//						System.out.println("Adding key from default set: "+key);
						if (count > 0)
						{
							buf.append(",");
							buf.append(nl);
						}
						else
						{
							buf.append(nl);
						}
						if (!jsonBuffer)
						{
							buf.append("\t\t");
						}
						buf.append(convertToJsIdString(key));
						buf.append(":\"");
						buf.append(value.replaceAll("\"","&quot;"));
						buf.append("\"");
						count++;
					}
				}
			}
			if (!jsonBuffer)
			{
				buf.append("\n};\n");
			}
			else
			{
				buf.append("}");
			}
		}
		return buf.toString();
	}
	protected	synchronized	void	writeTextBuffer(String role)
	{
		try
		{
			StringBuffer buf = new StringBuffer();
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			//make sure that if paid is not there it will revert back to free
			try{
        			if(PREMIUM.equalsIgnoreCase(role) && paidFile != null)
        			{
        			    fis = new FileInputStream(paidFile);
        			}else
        			{
        			    fis = new FileInputStream(file);
        			}
			}catch(Exception e)
			{
			    //this means the paid file is not present
			    fis = new FileInputStream(file);
			}
			bis = new BufferedInputStream(fis);
			byte[] byteBuf = new byte[256];
			int len=0;
			while((len=bis.read(byteBuf,0,256)) > 0)
			{
				buf.append(new String(byteBuf,0,len));
			}
			try
			{
				bis.close(); fis.close();
			}
			catch(Exception e2)
			{
				
			}
			if(PREMIUM.equalsIgnoreCase(role))
			{
			    this.paidTextBuffer = buf.toString();
			}else{
			    this.textBuffer = buf.toString();    
			}
			
			this.lastUpdateTime = this.file.lastModified();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected	String	convertToJsIdString(String str)
	{
		String s1 = str;
		int dot = s1.indexOf(".");
		while (dot > 0)
		{
			s1 = s1.substring(0,dot)+"$"+s1.substring(dot+1);
			dot = s1.indexOf(".");
		}
		return	s1;
	}
}
