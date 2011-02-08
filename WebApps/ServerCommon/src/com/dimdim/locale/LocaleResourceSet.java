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

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Objects of this class represents all the resources for a single locale.
 * Support for a locale is considered available if any one of the dictionary
 * files is present for that locale.
 * 
 * The set of files and their respective types are a fixed set at present.
 * 
 * The set constructor attempts to construct individual resource files for
 * each of the components and 
 */

public class LocaleResourceSet
{
	protected	Locale	locale;
	protected	HashMap	resourceFiles;
	
	protected	LocaleResourceSet	defaultResourceSet;
	
	public	LocaleResourceSet(String languageDirPath, Vector components,
				Vector supportedDictionaries, Vector supportedHtmlFiles,
				Locale locale, LocaleResourceSet defaultResourceSet)
		throws	LocaleNotSupportedException
	{
		this.locale = locale;
		this.defaultResourceSet = defaultResourceSet;
		this.resourceFiles = new HashMap();
		
		int	numComponents = components.size();
		int numResourceFiles = supportedDictionaries.size();
		int numHtmlFiles = supportedHtmlFiles.size();
		for (int i=0; i<numComponents; i++)
		{
			String component = (String)components.elementAt(i);
			String componentDir = (new File(languageDirPath,component)).getAbsolutePath();
			for (int j=0; j<numResourceFiles; j++)
			{
				String dictionaryName = (String)supportedDictionaries.get(j);
				try
				{
					LocaleResourceFile lrf = new LocaleResourceFile(componentDir,
						locale,component,dictionaryName,LocaleResourceFile.RESOURCE_BUNDLE_FILE);
//					System.out.println("Created locale resource file:");
					if (defaultResourceSet != null)
					{
						lrf.setDefaultResourceFile(defaultResourceSet.
								getLocaleResourceFile(component,dictionaryName));
					}
					this.resourceFiles.put(component+"."+dictionaryName,lrf);
				}
				catch(Exception e)
				{
					//	Locale not supported. Ignore.
				}
			}
			for (int j=0; j<numHtmlFiles; j++)
			{
				String fileName = (String)supportedHtmlFiles.get(j);
				try
				{
					LocaleResourceFile lrf = new LocaleResourceFile(componentDir,
						locale,component,fileName,LocaleResourceFile.HTML_FILE);
//					System.out.println("Created locale resource file:");
					if (defaultResourceSet != null)
					{
						lrf.setDefaultResourceFile(defaultResourceSet.
								getLocaleResourceFile(component,fileName));
					}
					this.resourceFiles.put(component+"."+fileName,lrf);
				}
				catch(Exception e)
				{
					//	Locale not supported. Ignore.
				}
			}
		}
		if (this.resourceFiles.size() == 0)
		{
			throw	new	LocaleNotSupportedException();
		}
	}
	public	ResourceBundle	getResourceBundle(String component, String dictionary, String role)
	{
		ResourceBundle	rb = null;
		LocaleResourceFile lrf = this.getLocaleResourceFile(component,dictionary);
		if (lrf != null)
		{
		    if(LocaleResourceFile.PREMIUM.equalsIgnoreCase(role))
		    {
			rb = lrf.getPaidResourceBundle();
		    }else{
			rb = lrf.getResourceBundle();
		    }
		}
		return	rb;
	}
	public	String	getDictionaryBuffer(String component, String dictionary, String role)
	{
		String	buf = null;
		LocaleResourceFile lrf = this.getLocaleResourceFile(component,dictionary);
		if (lrf != null)
		{
			buf = lrf.getDictionaryBuffer(role);
		}
		return	buf;
	}
	public	String	getDictionaryJsonBuffer(String component, String dictionary, String isFree)
	{
		String	buf = null;
		LocaleResourceFile lrf = this.getLocaleResourceFile(component,dictionary);
		if (lrf != null)
		{
			buf = lrf.getDictionaryJsonBuffer(isFree);
		}
		return	buf;
	}
	public	String	getFileContentBuffer(String component, String dictionary, String isFree)
	{
		String	buf = null;
		LocaleResourceFile lrf = this.getLocaleResourceFile(component,dictionary);
		if (lrf != null)
		{
			buf = lrf.getFileContentBuffer(isFree);
		}
		return	buf;
	}
	protected	LocaleResourceFile	getLocaleResourceFile(String component, String dictionary)
	{
		return (LocaleResourceFile)this.resourceFiles.get(component+"."+dictionary);
	}
	public	synchronized	void	refresh()
	{
		Iterator iter = this.resourceFiles.keySet().iterator();
		while (iter.hasNext())
		{
			String key = (String)iter.next();
			LocaleResourceFile lrf = (LocaleResourceFile)this.resourceFiles.get(key);
			lrf.refresh();
		}
	}
}

