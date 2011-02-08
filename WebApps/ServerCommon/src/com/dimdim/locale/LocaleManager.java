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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This locale manager compliments the ui by providing dictionary objects
 * that can be directly used by GWT Dictionary object.
 * 
 * The locale manager keeps track of the required files for a complete locale
 * support. If any one of the files required is available and is correct, then
 * it considers that support for the locale is available. Missing files and
 * values will be taken from the default locale.
 * 
 * The locale manager is supported by a set of files. A master file that lists
 * all supported locales and a default locale. It always guarrantees a valid
 * reply so that the users do not have to maintain separate defaults. This
 * manager maintains an extra in memory default in case of file corruptions on
 * disk.
 * 
 * Two types of resources are supported at present. Resource bundles and files.
 * Files are provided in case where large and formatted buffers are required,
 * e.g. email templates.
 * 
 * The locale manager expects the data files in a specific structure. Root
 * directory the manager is initialized with must contain a directory named
 * 'data' with following structure.
 * 
 * data / language / <component name> / <dictionary file name><locale_code>.properties
 * 									  / <html file name>_<locale code>.html
 * 
 * For example for a complete support for American English ( locale code en_US )
 * support following files must be present in following structure -
 * 
 * data / language / common / ui_strings_en_US.properties
 * 				   / forms / ui_strings_en_US.properties
 * 				   / admin / ui_strings_en_US.properties
 * 				   / console / ui_strings_en_US.properties
 * 				   / email / ui_strings_en_US.properties
 * 							/ invitation_email_template_en_US.html
 * 				   / flash / ui_strings_en_US.properties
 * 				   / landing_pages / ui_strings_en_US.properties
 * 				   / publisher / ui_strings_en_US.properties
 * 
 * A support for a new locale can be added on an active installation at any time
 * by simply creating a new set of files for tne new locale. The 'en_US' files
 * are a global default and must not be altered. These files are used as reference
 * to validate files for a new locale as well as to fill in missing keys. This
 * enables a user to provide only a partial set of files and or partial set of
 * keys in a single file.
 */

public class LocaleManager implements ILocaleManager
{
	protected	static	String	defaultLocaleId = "en_US";
	public	static	Locale	defaultLocale = Locale.US;
	
	protected	static	final	String	supportedComponentsList =
			"common,console,email,flash,landing_pages,publisher,admin,portal,forms,notification";
	protected	static	final	String	supportedDictionariesList =
			"ui_strings,default_layout,tooltips";
	protected	static	final	String	supportedHtmlFilesList =
			"invitation_email_template,cancellation_email_template,removal_email_template," +
			"password_email_template,membership_invite_template,membership_enterprise_invite_template,membership_enterprise_invite_template_paid,thankyou_email_template," +
			"invitation_sched_attendee_email_template,invitation_inst_email_template,invitation_sched_email_template,recording_email_template";

	
	protected	static	LocaleManager	theManager;
	
	public	static	LocaleManager	getManager()
	{
		return	LocaleManager.theManager;
	}
	public	static	void	initManager(String localDataDirPath)
		throws	LocaleNotSupportedException
	{
		LocaleManager.createManager(localDataDirPath);
	}
	protected	synchronized	static	void	createManager(String localDataDirPath)
		throws	LocaleNotSupportedException
	{
		LocaleManager.theManager = new LocaleManager(localDataDirPath);
	}
	
	protected	String		localDataDirPath;
	protected	String		localLanguageDirPath;
	
	protected	LocaleResourceSet		defaultLocaleResourceSet;
	
	protected	Vector		supportedLocales;
	
	protected	Vector		supportedComponents;
	protected	Vector		supportedDictionaries;
	protected	Vector		supportedHtmlFiles;
	
	protected	HashMap		availableResourceSets;
	
	private	LocaleManager(String localDataDirPath)
		throws	LocaleNotSupportedException
	{
		this.localDataDirPath = localDataDirPath;
		this.localLanguageDirPath =
			(new File(localDataDirPath,"language")).getAbsolutePath();
		init();
	}
	public String getLocalDataDirPath()
	{
		return localDataDirPath;
	}
	public	Locale	getDefaultLocale()
	{
		return	LocaleManager.defaultLocale;
	}
	public	boolean	isLocaleSupported(Locale locale)
	{
		return	this.supportedLocales.contains(locale);
	}
	public	boolean	isLocaleSupported(String localeId)
	{
		boolean	b = false;
		int	numLocales = this.supportedLocales.size();
		for (int i=0; i<numLocales; i++)
		{
			if (((Locale)this.supportedLocales.
					elementAt(i)).toString().equals(localeId))
			{
				b = true;
				break;
			}
		}
		return	b;
	}
	public	Locale	getSupportedLocale(String localeCode)
	{
		Locale	locale = null;
		int	numLocales = this.supportedLocales.size();
		for (int i=0; i<numLocales; i++)
		{
			locale = (Locale)this.supportedLocales.elementAt(i);
			if (locale.toString().equals(localeCode))
			{
				break;
			}
			else
			{
				locale = null;
			}
		}
		if(null == locale)
		{
			locale = defaultLocale;
		}
		return	locale;
	}
	public	Vector	getSupportedLocales()
	{
		return	this.supportedLocales;
	}
	
	/**
	 * @param component
	 * @param fileName
	 * @param locale
	 * @return
	 * @throws LocaleNotSupportedException
	 * @deprecated
	 */
	public	String	getHtmlFileBuffer(String component,
		String fileName, Locale locale)
	throws	LocaleNotSupportedException
            {
            String buf = null;
            LocaleResourceSet lrs = (LocaleResourceSet)
            			this.availableResourceSets.get(locale);
            if (lrs != null)
            {
            	buf = lrs.getFileContentBuffer(component, fileName, LocaleResourceFile.FREE);
            	if (buf == null)
            	{
            		//	This means that we have only a partial support for this
            		//	locale. It has some of the resource sets but not all.
            		buf = this.defaultLocaleResourceSet.getFileContentBuffer(component, fileName, LocaleResourceFile.FREE);
            	}
            }
            else
            {
            	System.out.println("** Resource set not found for locale:"+locale);
            }
            return	buf;
            }
	
	public	String	getHtmlFileBuffer(String component,
				String fileName, Locale locale, String role)
			throws	LocaleNotSupportedException
	{
		String buf = null;
		LocaleResourceSet lrs = (LocaleResourceSet)
					this.availableResourceSets.get(locale);
		if (lrs != null)
		{
			buf = lrs.getFileContentBuffer(component, fileName, role);
			if (buf == null)
			{
				//	This means that we have only a partial support for this
				//	locale. It has some of the resource sets but not all.
				buf = this.defaultLocaleResourceSet.getFileContentBuffer(component, fileName, role);
			}
		}
		else
		{
			System.out.println("** Resource set not found for locale:"+locale);
		}
		return	buf;
	}
	
	ResourceBundle	getResourceBundle(String component,
				String dictionary, Locale locale, String role)
	{
		ResourceBundle rb = null;
		LocaleResourceSet lrs = (LocaleResourceSet)
					this.availableResourceSets.get(locale);
		if (lrs != null)
		{
			rb = lrs.getResourceBundle(component, dictionary, role);
			if (rb == null)
			{
				//	This means that we have only a partial support for this
				//	locale. It has some of the resource sets but not all.
				rb = this.defaultLocaleResourceSet.getResourceBundle(component, dictionary, role);
			}
		}
		return	rb;
	}
	
	/**
	 * @param component
	 * @param dictionary
	 * @param locale
	 * @param key
	 * @return
	 * @deprecated
	 */
	public	String	getResourceKeyValue(String component,
		String dictionary, Locale locale, String key)
	{
	    return getResourceKeyValue(component, dictionary, locale, key, LocaleResourceFile.FREE);
	}
	
	/**
	 * As per the mission statement this method always guarrantees a valid return
	 * without any exceptions. In case of a total failure to obtain the key value,
	 * the key itself is returned as value.
	 * 
	 * @param component
	 * @param dictionary
	 * @param locale
	 * @param key
	 * @return
	 */
	public	String	getResourceKeyValue(String component,
			String dictionary, Locale locale, String key, String role)
	{
		String	value = null;
		ResourceBundle rb = null;
		
		rb = this.getResourceBundle(component,dictionary,locale, role);
		if (rb != null)
		{
			try
			{
				value = rb.getString(key);
			}
			catch(Exception e)
			{
				value = null;
			}
		}
		//try with normal resource bundle if the value above is null
		if (value == null)
		{
			rb = this.getResourceBundle(component,dictionary, locale, LocaleResourceFile.FREE);
			if (rb != null)
			{
				try
				{
					value = rb.getString(key);
				}
				catch(Exception e)
				{
					value = null;
				}
			}
		}
		//	This should not happen, because the call should never get used
		//	for unsupported locales. This is just an extra safety precaution.
		if (value == null)
		{
			rb = this.defaultLocaleResourceSet.getResourceBundle(component,dictionary, role);
			if (rb != null)
			{
				try
				{
					value = rb.getString(key);
				}
				catch(Exception e)
				{
					value = null;
				}
			}
		}
		if (value == null)
		{
			value = key;
		}
		return	value;
	}
	
	public	String	getDictionaryBuffer(String component,
				String dictionary, Locale locale, String role)
	{
		String buf = null;
		LocaleResourceSet lrs = (LocaleResourceSet)
					this.availableResourceSets.get(locale);
		if (lrs != null)
		{
			buf = lrs.getDictionaryBuffer(component, dictionary, role);
		}
		if (buf == null)
		{
			//	This means that we have only a partial support for this
			//	locale. It has some of the resource sets but not all.
			buf = this.defaultLocaleResourceSet.getDictionaryBuffer(component, dictionary, role);
		}
		return	buf;
	}
	
	public	String	getDictionaryJsonBuffer(String component,
				String dictionary, Locale locale, String role)
	{
		String buf = null;
		LocaleResourceSet lrs = (LocaleResourceSet)
					this.availableResourceSets.get(locale);
		if (lrs != null)
		{
			buf = lrs.getDictionaryJsonBuffer(component, dictionary, role);
		}
		if (buf == null)
		{
			//	This means that we have only a partial support for this
			//	locale. It has some of the resource sets but not all.
			buf = this.defaultLocaleResourceSet.getDictionaryJsonBuffer(component, dictionary, role);
		}
		return	buf;
	}
	public	synchronized	void	init()	throws	LocaleNotSupportedException
	{
		this.supportedComponents = this.parseCSVString(LocaleManager.supportedComponentsList);
		this.supportedDictionaries = this.parseCSVString(LocaleManager.supportedDictionariesList);
		this.supportedHtmlFiles = this.parseCSVString(LocaleManager.supportedHtmlFilesList);
		
		//	Create the default locale resource set. If the default locale creation fails the
		//	Locale manager will revert to fixed compiled in values for all requests.
		
		this.defaultLocale = Locale.US;
		this.defaultLocaleResourceSet = new LocaleResourceSet(
				this.localLanguageDirPath,
				this.supportedComponents, this.supportedDictionaries,
				this.supportedHtmlFiles, this.defaultLocale, null);
		
		this.initLocaleResources();
	}
	
	public	synchronized	void	initLocaleResources()
	{
		//	Find all the supported locales so that a list can be presented
		//	for choice. 
		
		this.supportedLocales = new Vector();
		this.availableResourceSets = new HashMap();
		
		//	Attempt to construct a resource set for all available locales.
		//	If the construction succeeds the locale is supported. If not,
		//	its not. In the end simply create a vector of the locales
		//	from the set.
		
		Locale[] availableLocales = Locale.getAvailableLocales();
		for (int i=0; i<availableLocales.length; i++)
		{
			try
			{
				LocaleResourceSet lrs = new LocaleResourceSet(
						this.localLanguageDirPath, this.supportedComponents,
						this.supportedDictionaries, this.supportedHtmlFiles,
						availableLocales[i], this.defaultLocaleResourceSet);
				
				this.supportedLocales.addElement(availableLocales[i]);
				this.availableResourceSets.put(availableLocales[i],lrs);
			}
			catch(LocaleNotSupportedException e)
			{
				//	Nothing to do. Ignore.
			}
			catch(Exception e2)
			{
				//	Unexpected exception. Most probably a file reading or parsing
				//	error.
				e2.printStackTrace();
			}
		}
	}
	/**
	 * The refresh is expected to read new language files and remove the resources
	 * from memory for any deleted files.
	 * 
	 */
	public	synchronized	void	refresh()
	{
		Locale[] availableLocales = Locale.getAvailableLocales();
		for (int i=0; i<availableLocales.length; i++)
		{
			Locale locale = availableLocales[i];
			LocaleResourceSet lrs = (LocaleResourceSet)
					this.availableResourceSets.get(locale);
			if (lrs != null)
			{
				lrs.refresh();
			}
			else
			{
				try
				{
					lrs = new LocaleResourceSet(
							this.localLanguageDirPath, this.supportedComponents,
							this.supportedDictionaries, this.supportedHtmlFiles,
							availableLocales[i], this.defaultLocaleResourceSet);
					
					this.supportedLocales.addElement(availableLocales[i]);
					this.availableResourceSets.put(availableLocales[i],lrs);
				}
				catch(LocaleNotSupportedException e)
				{
					//	Nothing to do. Ignore.
				}
				catch(Exception e2)
				{
					//	Unexpected exception. Most probably a file reading or parsing
					//	error.
					e2.printStackTrace();
				}
			}
		}
	}
	private	Vector	parseCSVString(String str)
	{
		Vector v = new Vector();
		StringTokenizer st = new StringTokenizer(str,",");
		while (st.hasMoreTokens())
		{
			v.addElement(st.nextToken());
		}
		return	v;
	}
}
