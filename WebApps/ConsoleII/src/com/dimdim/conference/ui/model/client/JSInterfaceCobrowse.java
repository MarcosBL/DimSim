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

import java.util.HashMap;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class JSInterfaceCobrowse
{
	private static boolean s_initialized = false;
	
	private static HashMap jsCallbackListeners = new HashMap();
	
	static
	{
		init();
	}
	
	public static void init()
	{
		if (! s_initialized)
		{
			jsInit();
			
			s_initialized = true;
		}
	}
	
	private static native void jsInit() /*-{

		if ($wnd.JSInterfaceCobrowse === undefined)
		{
			 $wnd.JSInterfaceCobrowse = new Object();
		}  
		
		if (typeof JSInterfaceCobrowse == "undefined")
		{
			JSInterfaceCobrowse = $wnd.JSInterfaceCobrowse;
		}
		
		JSInterfaceCobrowse.callGWT4 = $wnd.JSInterfaceCobrowse.callGWT4 = function(data1)
		{
			@com.dimdim.conference.ui.model.client.JSInterfaceCobrowse::callGWT4(Ljava/lang/String;)(""+data1);
		};
		
		JSInterfaceCobrowse.scroll = $wnd.JSInterfaceCobrowse.scroll = function(data1,data2)
		{
			@com.dimdim.conference.ui.model.client.JSInterfaceCobrowse::scroll(Ljava/lang/String;Ljava/lang/String;)(""+data1,""+data2);
		};
		
	}-*/;
	
	public static void addCallbackListener(JSCallBackCobrowseListener jsCallbackListener)
	{
		JSInterfaceCobrowse.init();
		if (JSInterfaceCobrowse.s_initialized)
		{
			JSInterfaceCobrowse.jsCallbackListeners.put(jsCallbackListener.getListenerName(),jsCallbackListener);
		}
	}
	
	public static void callGWT4(final String dimdimID)
	{
		JSInterfaceCobrowse.init();
		//Window.alert("JSInterfaceCobrowse:callGWT4: meetingID:"+meetingID);
		if (JSInterfaceCobrowse.s_initialized)
		{
			final JSCallBackCobrowseListener listener = (JSCallBackCobrowseListener)jsCallbackListeners.get("DMS");
			if (listener != null)
			{
				Timer timer = new Timer()
				{
					public	void	run()
					{
						listener.syncToURLResource( dimdimID);
					}
				};
				timer.schedule(100);
			}
			else
			{
				Window.alert("No Listener available");
			}
		}
		else
		{
//			Window.alert("JSInterface not initialized");
		}
	}
	
	public static void scroll(String horScroll, String verScroll)
	{
		//Window.alert("inside scroll of JSinterface"+horScroll+verScroll);
		ClientModel.getClientModel().getCobrowseModel().scroll(horScroll, verScroll);
	}
	
	public static void navigate()
	{
		
	}
}
