package com.dimdim.locale;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

public interface ILocaleManager {

	public abstract String getLocalDataDirPath();

	public abstract Locale getDefaultLocale();

	public abstract boolean isLocaleSupported(Locale locale);

	public abstract boolean isLocaleSupported(String localeId);

	public abstract Locale getSupportedLocale(String localeCode);

	public abstract Vector getSupportedLocales();

	public abstract String getHtmlFileBuffer(String component, String fileName,
			Locale locale) throws LocaleNotSupportedException;
	
	public abstract String getHtmlFileBuffer(String component, String fileName,
		Locale locale, String role) throws LocaleNotSupportedException;

	//public abstract ResourceBundle getResourceBundle(String component,
	//		String dictionary, Locale locale);

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
	public abstract String getResourceKeyValue(String component,
			String dictionary, Locale locale, String key, String role);

	public abstract String getResourceKeyValue(String component,
		String dictionary, Locale locale, String key);
	
	//public abstract String getDictionaryBuffer(String component,
	//		String dictionary, Locale locale);

	public abstract void init() throws LocaleNotSupportedException;

	public abstract void initLocaleResources();

	/**
	 * The refresh is expected to read new language files and remove the resources
	 * from memory for any deleted files.
	 * 
	 */
	public abstract void refresh();
	
}