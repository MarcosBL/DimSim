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

package com.dimdim.ui.common.client.data;

import java.util.HashMap;
import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONString;

import com.dimdim.ui.common.client.json.ServerResponse;
import com.dimdim.ui.common.client.json.ServerResponseObjectReader;
import com.dimdim.ui.common.client.json.UIObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UIDataDictionaryManager implements ServerResponseObjectReader, UIDataReadingProgressListener
{
	private	static	UIDataDictionaryManager	theManager;
	
	public	static	void	initManager(String webappName, String confKey, String userType,
			String[] types, String[] components, String[] names)
	{
		UIDataReader.webappName = webappName;
		UIDataReader.confKey = confKey;
		UIDataReader.userType = userType;
		UIDataDictionaryManager.theManager = new UIDataDictionaryManager(types,components,names);
	}
	public	static	UIDataDictionaryManager	getManager()
	{
		return	UIDataDictionaryManager.theManager;
	}
	
	
	private	UIDataDictionaryManager(String[] types, String[] components, String[] names)
	{
		this.available_types = types;
		this.available_components = components;
		this.available_names = names;
		
		this.dictionaries = new HashMap();
		this.resourceFiles = new HashMap();
		this.selectLists = new HashMap();
		
		this.dataReader = new UIDataReader(this,this);
	}
	
	protected	String[]	available_types;
	protected	String[]	available_components;
	protected	String[]	available_names;
	
	protected	HashMap		dictionaries;
	protected	HashMap		resourceFiles;
	protected	HashMap		selectLists;
	
	protected	UIDataReader	dataReader;
	
	public	void	saveDictionary(String component, String name, UIObject obj)
	{
//		Window.alert("Saving dictionary:"+component+"_"+name+":"+obj);
		this.dictionaries.put(component+"_"+name,obj);
	}
	public	UIDataDictionary	getDictionary(String component, String name)
	{
		UIDataDictionary obj = (UIDataDictionary)this.dictionaries.get(component+"_"+name);
//		Window.alert("Reading dictionary:"+component+"_"+name+":"+obj);
		return	obj;
	}
	public	void	saveResourceFile(String component, String name, UIObject obj)
	{
		this.resourceFiles.put(component+"_"+name,obj);
	}
//	public	UIObject	getResourceFile(String component, String name)
//	{
//		return	(UIObject)this.resourceFiles.get(component+"_"+name);
//	}
//	public	void	saveSelectList(SelectList list)
//	{
//		this.selectLists.put(list.getName(),list);
//	}
//	public	SelectList	getSelectList(String name)
//	{
//		return	(SelectList)this.selectLists.get(name);
//	}
	
	/**
	 * This method will be called by the portal module load and will give
	 * the portal module time to provide messages to user while data is
	 * being read.
	 */
	
	protected	int		currentlyReading = -1;
	
	protected	UIDataReadingProgressListener	listener;
	
	public	void	readDataDictionaries(UIDataReadingProgressListener listener)
	{
		this.listener = listener;
		this.currentlyReading = 0;
		this.dataReader.readData(this.getDataObjectType(this.currentlyReading),
				this.getDataObjectComponent(this.currentlyReading),
				this.getDataObjectName(this.currentlyReading));
	}
	/**
	 * Few specific data access methods.
	 */
	public void dataReadingComplete(ServerResponse data)
	{
		//Window.alert("Reading dictionary: "+this.currentlyReading+", result:"+data.isSuccess());
		//	If this is a select list we just read, save it in the select lists
		//	table by its name.
		if (this.getDataObjectType(this.currentlyReading).equals("selectlist"))
		{
			SelectList list = (SelectList)data.getDataObject();
			if (list != null)
			{
//				this.saveSelectList(list);
//					Window.alert(list.toString());
			}
		}
		else if (this.getDataObjectType(this.currentlyReading).equals("resourcefile"))
		{
			UIDataDictionary dictionary = (UIDataDictionary)data.getDataObject();
			if (dictionary != null)
			{
				this.saveResourceFile(this.getDataObjectComponent(this.currentlyReading),
						this.getDataObjectName(this.currentlyReading), dictionary);
			}
			//Window.alert("dictionary:"+dictionary.toString());
		}
		else if (this.getDataObjectType(this.currentlyReading).equals("dictionary"))
		{
			UIDataDictionary dictionary = (UIDataDictionary)data.getDataObject();
			if (dictionary != null)
			{
				this.saveDictionary(this.getDataObjectComponent(this.currentlyReading),
						this.getDataObjectName(this.currentlyReading), dictionary);
			}
			//Window.alert("dictionary:"+dictionary.toString());
		}
		else if (this.getDataObjectType(this.currentlyReading).equals("combined"))
		{
//			Window.alert("Reading combined dictionary");
//			UIDataDictionary dictionary = (UIDataDictionary)data.getDataObject();
//			Window.alert("1:"+dictionary);
			ArrayList array = data.getDataArray();
//			Window.alert("2:"+array);
			if (array != null)
			{
				int  size = array.size();
				for (int i=0; i<size; i++)
				{
					UIDataDictionary componentD = (UIDataDictionary)array.get(i++);
					UIDataDictionary nameD = (UIDataDictionary)array.get(i++);
					UIDataDictionary valueD = (UIDataDictionary)array.get(i);
//					Window.alert("Reading array member:"+nameD+":"+valueD);
					String component = componentD.getStringValue("name");
					String name = nameD.getStringValue("name");
					this.saveDictionary(component,name,valueD);
				}
			}
			//Window.alert("dictionary:"+dictionary.toString());
		}
		this.currentlyReading++;
		this.readNextDataBlock(data);
	}
	private	void	readNextDataBlock(ServerResponse data)
	{
		if (this.haveMoreData(this.currentlyReading))
		{
			this.dataReader.readData(this.getDataObjectType(this.currentlyReading),
					this.getDataObjectComponent(this.currentlyReading),
					this.getDataObjectName(this.currentlyReading));
		}
		else
		{
			this.currentlyReading = 0;
			this.listener.dataReadingComplete(data);
		}
	}
	/**
	 * This method is expected to convert the json object to a specific type.
	 * In this case we know we only will be reading data dictionaries.
	 */
	public UIObject readServerResponseData(String objectClass, JSONObject data)
	{
		UIObject dictionary = null;
		if (this.getDataObjectType(this.currentlyReading).equals("selectlist"))
		{
			//	Create and return a select list.
			String listName = this.getDataObjectName(this.currentlyReading);
			SelectList list = SelectList.readSelectList(listName,data);
			if (list != null)
			{
				dictionary = list;
			}
		}
		else
		{
			//	Both the resource files and dictionaries are essentially
			//	dictionries from ui model perspective.
			dictionary = new UIDataDictionary(data);
		}
		if (dictionary != null)
		{
//			Window.alert(dictionary.toJson());
		}
		else
		{
//			Window.alert("Data reader faied");
		}
		return	dictionary;
	}
	private	boolean	haveMoreData(int index)
	{
		return	(index < available_components.length);
	}
	private	String	getDataObjectComponent(int index)
	{
		return	available_components[index];
	}
	private	String	getDataObjectType(int index)
	{
		return	available_types[index];
	}
	private	String	getDataObjectName(int index)
	{
		return	available_names[index];
	}
}
