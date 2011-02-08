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

package com.dimdim.conference.ui.model.client;

import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class is a simple interface to the UIResource properties file.
 */

public class UIResources
{
	private	static	UIResources	uiResources = new UIResources();
	
	public	static	UIResources	getUIResources()
	{
		return	UIResources.uiResources;
	}
	
	private	UIResources()
	{
	}
	public	String	getConferenceInfo(String param)
	{
		return	ConferenceGlobals.userInfoDictionary.getStringValue(param);	
//		return	getConferenceInfoParam(param);
	}
	public	String	getConferenceInfoAndDecode64(String param)
	{
		String s = ConferenceGlobals.userInfoDictionary.getStringValue(param);	
		return	this.decodeBase64(s);
//		return	getConferenceInfoParam(param);
	}
	private	native	String	decodeBase64(String s) /*-{
		return $wnd.Base64.decode(s);
	}-*/;
	public	int	getIntResource(String one, String two, int defValue)
	{
		int	val = defValue;
		try
		{
			val = getIntResourceValue(one,two);
		}
		catch(Exception e)
		{
			val = defValue;
		}
		return	val;
	}
//	public	String	getResource(String one, String two, String defValue)
//	{
//		String	str = defValue;
//		try
//		{
//			str = getResourceValue(one,two);
//		}
//		catch(Exception e)
//		{
//			str = defValue;
//		}
//		return	str;
//	}
//	public	String	getResource(String one, String two,
//			String three, String defValue)
//	{
//		String	str = defValue;
//		try
//		{
//			str = getResourceValue(one,two,three);
//		}
//		catch(Exception e)
//		{
//			str = defValue;
//		}
//		return	str;
//	}
//	public	String	getResource(String one, String two,
//			String three, String four, String defValue)
//	{
//		String	str = defValue;
//		try
//		{
//			str = getResourceValue(one,two,three,four);
//		}
//		catch(Exception e)
//		{
//			str = defValue;
//		}
//		return	str;
//	}
//	public	String	getResource(String one, String two,
//			String three, String four, String five, String defValue)
//	{
//		String	str = defValue;
//		try
//		{
//			str = getResourceValue(one,two,three,four,five);
//		}
//		catch(Exception e)
//		{
//			str = defValue;
//		}
//		return	str;
//	}
//	private native String getConferenceInfoParam(String one) /*-{
//		return ($wnd.conference_info[one]);
//	}-*/;
//	private native String getResourceValue(String one, String two) /*-{
//		return ($wnd.ui_resources[one][two]);
//	}-*/;
	private native int getIntResourceValue(String one, String two) /*-{
		return ($wnd.ui_resources[one][two]);
	}-*/;
//	private native String getResourceValue(String one, String two, String three) /*-{
//		return ($wnd.ui_resources[one].groups[two][three]);
//	}-*/;
//	private native String getResourceValue(String one, String two, String three, String four) /*-{
//		return ($wnd.ui_resources[one].groups[two].groups[three][four]);
//	}-*/;
//	private native String getResourceValue(String one, String two, String three, String four, String five) /*-{
//		return ($wnd.ui_resources[one].groups[two].groups[three].groups[four][five]);
//	}-*/;
}
