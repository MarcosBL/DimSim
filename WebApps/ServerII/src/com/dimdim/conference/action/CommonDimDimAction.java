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
 
package com.dimdim.conference.action;

import	java.util.Map;

import	com.opensymphony.webwork.interceptor.SessionAware;
import	com.opensymphony.xwork.ActionSupport;
import com.dimdim.locale.LocaleManager;
import com.dimdim.util.misc.StringGenerator;
import	java.util.Locale;
import com.dimdim.conference.ConferenceConsoleConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * A conference action always works within the context of a single conference
 * Most actions required by conference participants will fall in this category.
 * All other actions will extend this one.
 */
public abstract class CommonDimDimAction	extends	ActionSupport
			implements	SessionAware
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	protected	String	cflag;
//	protected	String	flag;
	protected	Map		session;
	
	//	This is some of the important information about the client. Many
	//	initial and later actions need to work sensitive to this information.
	//	All the following three parameters have values 'unknown', 'yes' or 'no'.
	
	protected	String	inPopup = "false";
	protected	String	hasActiveX = "unknown";
	protected	String	hasWebcam = "unknown";
	
	public	CommonDimDimAction()
	{
//		this.flag = CommonDimDimAction.idGen.generateRandomString(7,7);
	}
	public Map getSession()
	{
		return this.session;
	}
	public void setSession(Map session)
	{
		this.session = session;
	}
	public String getFlag()
	{
//		if (this.flag == null)
//		{
//			this.flag = CommonDimDimAction.idGen.generateRandomString(7,7);
//		}
//		return this.flag;
		return	CommonDimDimAction.idGen.generateRandomString(7,7);
	}
//	public void setFlag(String flag)
//	{
//		this.flag = flag;
//	}
	public static StringGenerator getIdGen()
	{
		return idGen;
	}
	public static void setIdGen(StringGenerator idGen)
	{
		CommonDimDimAction.idGen = idGen;
	}
	public String getCflag()
	{
		if (cflag == null || cflag.equals("null"))
		{
			return	getFlag();
		}
		return cflag;
	}
	public void setCflag(String cflag)
	{
		this.cflag = cflag;
	}
	public String getHasActiveX()
	{
		return this.hasActiveX;
	}
	public void setHasActiveX(String hasActiveX)
	{
		this.hasActiveX = hasActiveX;
	}
	public String getHasWebcam()
	{
		return this.hasWebcam;
	}
	public void setHasWebcam(String hasWebcam)
	{
		this.hasWebcam = hasWebcam;
	}
	public String getInPopup()
	{
		return this.inPopup;
	}
	public void setInPopup(String inPopup)
	{
		this.inPopup = inPopup;
	}


	//component = 'landing_pages' 
	//dictionary = 'ui_strings' 
	// key = 'check_x_control_page.comment1'

	public String getResourceValue(String component, String dictionary, String key, String userType)
	{
		String value;
		LocaleManager lm = LocaleManager.getManager();
		//Locale currentLocale = super.getCurrentLocale();
		Locale currentLocale = (Locale)this.session.get(ConferenceConsoleConstants.USER_LOCALE);
		
		if (currentLocale == null)
		{
			currentLocale = lm.getDefaultLocale();
		}
		value = lm.getResourceKeyValue(component,dictionary,currentLocale,key, userType);
		
		return value;
		
	}
	protected	Locale	getCurrentLocale()
	{
		Locale currentLocale = (Locale)this.session.get(ConferenceConsoleConstants.USER_LOCALE);
		LocaleManager lm = LocaleManager.getManager();
		if (currentLocale == null)
		{
			currentLocale = lm.getDefaultLocale();
		}
		return	currentLocale;
	}
}
