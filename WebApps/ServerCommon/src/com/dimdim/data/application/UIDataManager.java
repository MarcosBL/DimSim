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

package com.dimdim.data.application;

import java.util.HashMap;
import java.util.Vector;
import java.util.Locale;
import java.util.Iterator;

import com.dimdim.data.model.SelectList;
import com.dimdim.data.model.UIDataDictionary;
import com.dimdim.locale.LocaleManager;

import com.dimdim.timer.*;

public class UIDataManager	implements	CommonTimerServiceUser
{
	public	static	final	String	GLOBAL_STRING	=	"global_string";
	public	static	final	String	SESSION_STRING	=	"session_string";
	
	private	static	UIDataManager	theManager;
	
	/**
	 * Initialization requires the particular webapp to pass its own disk
	 * location. This is to allow location, loading and reloading of resource
	 * files. Use of java api resource lookup does not reload the file if
	 * it changes on the disk.
	 * 
	 * @param webappDirectory
	 */
	public	static	void	initUIDataManager(String webappDirectory)
	{
		if (UIDataManager.theManager == null)
		{
			UIDataManager.createUIDataManager(webappDirectory);
		}
	}
	public	static	UIDataManager	getUIDataManager()
	{
		if (UIDataManager.theManager == null)
		{
			UIDataManager.createUIDataManager(".");
		}
		return	UIDataManager.theManager;
	}
	private	synchronized	static	void	createUIDataManager(String dir)
	{
		if (UIDataManager.theManager == null)
		{
			UIDataManager.theManager = new UIDataManager(dir);
			CommonTimerService.getService().addUser(UIDataManager.theManager);
		}
	}
	
	private	String	webappDirectory;
	
	private	HashMap		sessionStrings;
	private	HashMap		globalStrings;
	private	SelectListProvider	selectsListsProvider;
	private	UIDataDictionaryProvider	uiDataDictionaryProvider;
	
	private	UIDataManager(String webappDirectory)
	{
		this.webappDirectory = webappDirectory;
		this.globalStrings = new HashMap();
		this.sessionStrings = new HashMap();
	}
	public	void	addGlobalsString(String name, String s)
	{
		this.globalStrings.put(name, s);
	}
	public HashMap getGlobalStrings()
	{
		return globalStrings;
	}
	public HashMap getSessionStrings()
	{
		return sessionStrings;
	}
	public	void	removeGlobalsString(String name)
	{
		this.globalStrings.remove(name);
	}
	public	void	addSessionDataBuffer(String id, String buffer)
	{
		//	INCOMPLETE - go through the table and clear out the old ones
		synchronized (this)
		{
			SessionDataString sds = new SessionDataString(buffer);
			this.sessionStrings.put(id, sds);
		}
	}
	public	String	getDictionaryJsonBuffer(String component, String name, Locale locale, String isFree)
	{
		String s = null;
		if (component.equals(UIDataManager.GLOBAL_STRING))
		{
			s = (String)this.globalStrings.get(name);
		}
		else
		{
			s = LocaleManager.getManager().getDictionaryJsonBuffer(component,name,locale, isFree);
			if (s == null)
			{
				//	This may be a specific dictionary not supported by the locale manager.
				//	Check the dictionary provider if available.
				if (this.uiDataDictionaryProvider != null)
				{
					HashMap table = this.uiDataDictionaryProvider.getUIDataDictionary(component, name,locale);
					if (table != null)
					{
						s = (new UIDataDictionary(table)).toJson();
					}
				}
			}
		}
		return	s;
	}
	public	String	getSelectListJsonBuffer(String component, String name, Locale locale)
	{
		String	buf = null;
		if (this.selectsListsProvider != null)
		{
			SelectList list = this.selectsListsProvider.getSelectList(component, name, locale);
			if (list != null)
			{
				buf = list.toJson();
			}
		}
		return	buf;
	}
	public	String	getSessionDataBuffer(String component, String name, Locale locale)
	{
		String	buf = null;
		SessionDataString sds = (SessionDataString)this.sessionStrings.get(name);
		if (sds != null)
		{
			buf = sds.getDataString();
			synchronized (this)
			{
				this.sessionStrings.remove(name);
			}
		}
		return	buf;
	}
	public long getTimerDelay()
	{
		return 120000;
	}
	public void setTimerServiceTaskId(CommonTimerServiceTaskId taskId)
	{
	}
	public boolean timerCall()
	{
		synchronized (this)
		{
			Vector	keys = new Vector();
			Iterator iter = this.sessionStrings.keySet().iterator();
			while (iter.hasNext())
			{
				String key = (String)iter.next();
				SessionDataString sds = (SessionDataString)this.sessionStrings.get(key);
				if (!sds.isValid())
				{
					keys.addElement(key);
				}
			}
			int	num = keys.size();
			for (int i=0; i<num; i++)
			{
				String key = (String)keys.elementAt(i);
				this.sessionStrings.remove(key);
			}
		}
		return	true;
	}
}
