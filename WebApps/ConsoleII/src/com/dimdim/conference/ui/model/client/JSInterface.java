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

public class JSInterface
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
		if ($wnd.JSInterface === undefined)
		{
			 $wnd.JSInterface = new Object();
		}  
		
		if (typeof JSInterface == "undefined")
		{
			JSInterface = $wnd.JSInterface;
		}
		
		JSInterface.callGWT = $wnd.JSInterface.callGWT = function(obj1,data1)
		{
			@com.dimdim.conference.ui.model.client.JSInterface::callGWT(Ljava/lang/String;Ljava/lang/String;)(""+obj1,""+data1);
		};
		
		JSInterface.callGWT2 = $wnd.JSInterface.callGWT2 = function(obj1,data1,data2)
		{
			@com.dimdim.conference.ui.model.client.JSInterface::callGWT2(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(""+obj1,""+data1,""+data2);
		};
		
		JSInterface.callGWT3 = $wnd.JSInterface.callGWT3 = function(obj1,data1,data2,data3)
		{
			@com.dimdim.conference.ui.model.client.JSInterface::callGWT3(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(""+obj1,""+data1,""+data2,""+data3);
		};
		
	}-*/;
	
	public static void addCallbackListener(JSCallbackListener jsCallbackListener)
	{
		JSInterface.init();
		if (JSInterface.s_initialized)
		{
			JSInterface.jsCallbackListeners.put(jsCallbackListener.getListenerName(),jsCallbackListener);
		}
		//JSCallbackListener listener = (JSCallbackListener)jsCallbackListeners.get(jsCallbackListener.getListenerName());
	}
	public static void callGWT(String callerId, final String data)
	{
//		Window.alert("JSInterface:callGWT: caller:"+callerId+", data:"+data);
		JSInterface.init();
		if (JSInterface.s_initialized)
		{
			final JSCallbackListener listener = (JSCallbackListener)jsCallbackListeners.get(callerId);
			if (listener != null)
			{
				Timer timer = new Timer()
				{
					public	void	run()
					{
						listener.handleCallFromJS(data);
					}
				};
				timer.schedule(1);
			}
			else
			{
//				Window.alert("No Listener available");
			}
		}
		else
		{
//			Window.alert("JSInterface not initialized");
		}
	}
	
	public static void callGWT2(String callerId, final String data1, final String data2)
	{
		JSInterface.init();
		//Window.alert("JSInterface:callGWT: caller:"+callerId+", data1:"+data1+", data2:"+data2);
		if (JSInterface.s_initialized)
		{
			final JSCallbackListener listener = (JSCallbackListener)jsCallbackListeners.get(callerId);
			if (listener != null)
			{
				Timer timer = new Timer()
				{
					public	void	run()
					{
						listener.handleCallFromJS2(data1,data2);
					}
				};
				timer.schedule(1);
			}
			else
			{
				//Window.alert("No Listener available");
			}
		}
		else
		{
			//Window.alert("JSInterface not initialized");
		}
	}
	
	public static void callGWT3(String callerId, final String data1, final String data2, final String data3)
	{
		JSInterface.init();
//		Window.alert("JSInterface:callGWT: caller:"+callerId+", data1:"+data1+", data2:"+data2+", data3:"+data3);
		if (JSInterface.s_initialized)
		{
			final JSCallbackListener listener = (JSCallbackListener)jsCallbackListeners.get(callerId);
			if (listener != null)
			{
				Timer timer = new Timer()
				{
					public	void	run()
					{
						listener.handleCallFromJS3(data1,data2,data3);
					}
				};
				timer.schedule(1);
			}
			else
			{
//				Window.alert("No Listener available");
			}
		}
		else
		{
//			Window.alert("JSInterface not initialized");
		}
	}
}
