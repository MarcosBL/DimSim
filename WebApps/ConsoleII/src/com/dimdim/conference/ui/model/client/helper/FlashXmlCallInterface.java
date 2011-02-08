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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.model.client.helper;

import java.util.HashMap;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.dimdim.conference.ui.model.client.JSInterface;
import com.dimdim.conference.ui.model.client.JSCallbackListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class FlashXmlCallInterface	implements	JSCallbackListener
{
	private	static	FlashXmlCallInterface	theInterface;
	
	public	static	FlashXmlCallInterface	getInterface()
	{
		if (FlashXmlCallInterface.theInterface == null)
		{
			FlashXmlCallInterface.theInterface = new FlashXmlCallInterface();
		}
		return	FlashXmlCallInterface.theInterface;
	}
	
	private	HashMap	urlListeners;
//	private	int		checkCounter = 0;
	private	Timer	responseChecker;
	
	private	FlashXmlCallInterface()
	{
		urlListeners = new HashMap();
		
		JSInterface.addCallbackListener(this);
	}
	public	void	addUrlListener(String code, FlashXmlCallListener listener)
	{
		this.urlListeners.put(code,listener);
	}
	public	void	removeUrlListener(String code)
	{
		this.urlListeners.remove(code);
	}
	public String getListenerName()
	{
		return "DMS";
	}
	public void handleCallFromJS(String data)
	{
	}
	public void handleCallFromJS2(String data1, String data2)
	{
	}
	public void handleCallFromJS3(String code, String result, String response)
	{
		stopResponseChecker();
		FlashXmlCallListener listener = (FlashXmlCallListener)this.urlListeners.get(code);
//		if (listener != null)
//		{
//			listener.onInterfaceResponse("handleCallFromJS3:"+code+":"+result+":"+response);
//		}
			try
			{
				cleanupResponse();
			}
			catch(Exception e)
			{
				
			}
			listener = (FlashXmlCallListener)this.urlListeners.get(code);
			if (listener != null)
			{
				listener.onCompletion(result,response);
			}
	}
	public	void	startResponseChecker(final String code,
			FlashXmlCallListener listener, final int waitMillis)
	{
		if (listener != null)
		{
			this.addUrlListener(code, listener);
		}
		
		stopResponseChecker();
		
		this.responseChecker = new Timer()
		{
			public void run()
			{
				FlashXmlCallListener listener = (FlashXmlCallListener)urlListeners.get(code);
				if (listener != null)
				{
					listener.onCompletion("timeout","No response from server. Please contact support@dimdim.com");
				}
			}
		};
		responseChecker.schedule(waitMillis);
	}
	public	void	stopResponseChecker()
	{
		if (this.responseChecker != null)
		{
			this.responseChecker.cancel();
			this.responseChecker = null;
		}
	}
	public	void	callXmlUrlInFlash(final String code,final String url,final int waitMillis)
	{
//		this.checkCounter = 0;
		try
		{
			cleanupResponse();
		}
		catch(Exception e)
		{
			
		}
		this.startResponseChecker(code, null, waitMillis);
		callXmlUrlInJS(code, url+"&cflag="+System.currentTimeMillis());
	}
	private	native	void	callXmlUrlInJS(String code,String url)/*-{
		$wnd.callDMS(url);
	}-*/;
//	private	native	String	runResponseCheckInJS(String code)/*-{
//		return	$wnd.checkDMSResponse(code);
//	}-*/;
	private	native	void	cleanupResponse()/*-{
		$wnd.clearDMSResponse();
	}-*/;
}
