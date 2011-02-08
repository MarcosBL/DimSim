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

package com.dimdim.data.action;

import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import com.dimdim.data.model.ServerResponse;
import	com.opensymphony.webwork.interceptor.SessionAware;
import com.opensymphony.xwork.ActionSupport;

import	javax.servlet.http.HttpServletRequest;
import	javax.servlet.http.HttpServletResponse;
import	com.opensymphony.webwork.interceptor.ServletRequestAware;
import	com.opensymphony.webwork.interceptor.ServletResponseAware;

import	com.dimdim.data.application.UIDataManager;
import com.dimdim.locale.LocaleResourceFile;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action provides any and all data to the UI. Each data object is
 * identified by name, component and type. These terms are arbitrary and
 * marginally more informative than string 1, 2 and 3.
 * 
 * Similar to the get events on the conference server, this action is the
 * center point of all data access from ui. The control calls will of course
 * be specific, however the data access can go through a single action like
 * this one. If any other action is required with more parameters, it can
 * be easily derived from this one.
 * 
 * Available data options are:
 * 
 * -----  Type  ----- -----  Component  ----- -----  Name  -----
 * 
 * 		dictionary			portal				ui_strings
 * 		dictionary			portal				default_layout
 * 		resourcefile		portal				tab_layout
 * 		resourcefile		portal				general
 * 		selectlist			meeting				timezones
 * 		selectlist			meeting				emoticons
 * 		selectlist			meeting				networkprofiles
 * 		selectlist			meeting				avoptions
 * 		selectlist			meeting				maxparticipants
 * 		selectlist			meeting				maxmeetingtimehours
 * 		selectlist			meeting				maxmeetingtimeminutes
 * 		selectlist			meeting				waitingareaoptions
 * 		selectlist			meeting				maxattendeemikes
 * 		selectlist			meeting				supportedlocales
 * 		selectlist			meeting				schedulerecurrance
 * 		selectlist			meeting				schedulemeetingtimehours
 * 		selectlist			meeting				schedulemeetingtimeminutes
 * 		selectlist			meeting				timeampm
 * 		selectlist			meeting				schedulesearchtimeoptions
 * 		combined			console				<sessionid> (includes ui_strings, default_layout, emoticons and session)
 * 			dictionary,console,ui_strings
 * 			dictionary,console,tooltips
 * 			dictionary,global_string,emoticons
 * 			dictionary,session_string,<sessionid data id>
 */

public class GetDataAction	extends	ActionSupport
	implements SessionAware, ServletRequestAware, ServletResponseAware 
{
	public	static	final	String	DATA_TYPE_DICTIONARY = "dictionary";
	public	static	final	String	DATA_TYPE_RESOURCE_FILE = "resourcefile";
	public	static	final	String	DATA_TYPE_SELECT_LIST = "selectlist";
	public	static	final	String	DATA_TYPE_JSON_BUFFER = "jsonbuffer";
	public	static	final	String	DATA_TYPE_COMBINED = "combined";
	
	protected	Map		session;
	
	protected	String	component;
	protected	String	name;
	protected	String	type;
	
	protected	String	jsonBuffer="{a:\"b\"}";
	
	protected	HttpServletRequest	request;
	String role = LocaleResourceFile.FREE;
	
	public	GetDataAction()
	{
		
	}
	public	String	execute()	throws	Exception
	{
		String	ret	=	SUCCESS;
		Locale locale = (Locale)this.session.get("SESSION_LOCALE");
		if (locale == null)
		{
			locale = Locale.US;
		}
		//	Depending on the type and name try to read the data.
		if (type != null && name != null && component != null)
		{
			if (type.equals(GetDataAction.DATA_TYPE_DICTIONARY))
			{
				String s = null;
				if (component.equals(UIDataManager.SESSION_STRING))
				{
//					s = (String)this.session.get(name);
					s = UIDataManager.getUIDataManager().getSessionDataBuffer(component, name, locale);
				}
				else
				{
					s = UIDataManager.getUIDataManager().getDictionaryJsonBuffer(component, name, locale, role);
				}
				if (s != null)
				{
					ServerResponse sr = new ServerResponse(true,1,DATA_TYPE_DICTIONARY,s);
					this.jsonBuffer = sr.toJson();
				}
				else
				{
					System.out.println("SEVERE ERROR: cached session string not found: "+component+","+name);
				}
			}
			else if (type.equals(GetDataAction.DATA_TYPE_SELECT_LIST))
			{
				String s = UIDataManager.getUIDataManager().getSelectListJsonBuffer(component, name, locale);
				if (s != null)
				{
					ServerResponse sr = new ServerResponse(true,1,DATA_TYPE_SELECT_LIST,s);
					this.jsonBuffer = sr.toJson();
				}
			}
			else if (type.equals(GetDataAction.DATA_TYPE_COMBINED))
			{
				Vector v = new Vector();
				String s1 = UIDataManager.getUIDataManager().getDictionaryJsonBuffer("console", "ui_strings", locale, role);
//				System.out.println("1:"+s1);
				if (s1 != null)
				{
					v.add("{name:\"console\"}");
					v.add("{name:\"ui_strings\"}");
					v.add(s1);
				}
				String s2 = UIDataManager.getUIDataManager().getDictionaryJsonBuffer("console", "tooltips", locale, role);
//				System.out.println("2:"+s2);
				if (s2 != null)
				{
					v.add("{name:\"console\"}");
					v.add("{name:\"tooltips\"}");
					v.add(s2);
				}
				String s3 = UIDataManager.getUIDataManager().getDictionaryJsonBuffer("console", "default_layout", locale, role);
//				System.out.println("3:"+s3);
				if (s3 != null)
				{
					v.add("{name:\"console\"}");
					v.add("{name:\"default_layout\"}");
					v.add(s3);
				}
				String s4 = UIDataManager.getUIDataManager().getDictionaryJsonBuffer("global_string", "emoticons", locale, role);
//				System.out.println("4:"+s4);
				if (s4 != null)
				{
					v.add("{name:\"global_string\"}");
					v.add("{name:\"emoticons\"}");
					v.add(s4);
				}
				String s5 = UIDataManager.getUIDataManager().getSessionDataBuffer(component, name, locale);
//				System.out.println("5:"+s5);
				if (s5 != null)
				{
					v.add("{name:\""+component+"\"}");
					v.add("{name:\""+name+"\"}");
					v.add(s5);
				}
				if (v.size() == 15)
				{
					ServerResponse sr = new ServerResponse(true,1,DATA_TYPE_DICTIONARY,v);
					this.jsonBuffer = sr.toJson();
				}
			}
			
			/*
			else if (type.equals(GetDataAction.DATA_TYPE_RESOURCE_FILE))
			{
				if (name.equals("tab_layout") )
				{
					ResourceFileDictionary rfd = new ResourceFileDictionary("resources.PortalTabsLayout",this.getLocale());
					//System.out.println("DILIP @#!@ "+rfd.jsonBuffer);
					ServerResponse sr = new ServerResponse(true,1,DATA_TYPE_DICTIONARY,rfd.getJsonBuffer());
					this.jsonBuffer = sr.toJson();
				}
				else if(name.equals("general"))
				{
					GeneralResourceDictionary rfd = new GeneralResourceDictionary("resources.portal",this.getLocale());
					ServerResponse sr = new ServerResponse(true,1,DATA_TYPE_DICTIONARY,rfd.getJsonBuffer());
					this.jsonBuffer = sr.toJson();
				}
			}
			else if (type.equals(GetDataAction.DATA_TYPE_JSON_BUFFER))
			{
				String buffer = (String)this.httpSession.get(this.name);
				if (buffer == null)
				{
					ServerResponse resp = new ServerResponse(true,1,new Message("none"));
					buffer = resp.toJson();
				}
				this.httpSession.remove(this.name);
				this.httpSession.remove(this.name+"_id");
				this.httpSession.remove(this.name+"_email");
				
				System.out.println("Returning json buffer for "+this.name+":"+buffer);
				
				this.jsonBuffer = buffer;
			}
			*/
		}
		//System.out.println("Returning data block:"+this.jsonBuffer);
		return	ret;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getComponent()
	{
		return component;
	}
	public void setComponent(String component)
	{
		this.component = component;
	}
	public String getJsonBuffer()
	{
		return jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
	public void setServletResponse(HttpServletResponse response)
	{
		
	}
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}
	public void setSession(Map session)
	{
		this.session = session;
	}
	public String getRole()
	{
	    return role;
	}
	public void setRole(String role)
	{
	    this.role = role;
	}
	
}
