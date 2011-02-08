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

import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.model.client.JSCallBackCobrowseListener;
import com.dimdim.conference.ui.model.client.JSInterfaceCobrowse;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class CobrowseCallInterface	implements	JSCallBackCobrowseListener
{
	private	static	CobrowseCallInterface	theInterface;
	
	public	static	CobrowseCallInterface	getInterface()
	{
		if (CobrowseCallInterface.theInterface == null)
		{
			CobrowseCallInterface.theInterface = new CobrowseCallInterface();
		}
		return	CobrowseCallInterface.theInterface;
	}
	
	private	HashMap	urlListeners;
//	private	int		checkCounter = 0;
	private	Timer	responseChecker;
	
	private	CobrowseCallInterface()
	{
		urlListeners = new HashMap();
		
		JSInterfaceCobrowse.addCallbackListener(this);
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

	public void syncToURLResource(final String dimdimID)
	{
		stopResponseChecker();
		//Window.alert("inside handleCallFromJS4 from flashxml call back"+resourceID+meetingID+sequenceNo);
		FlashXmlCallListener listener = (FlashXmlCallListener)this.urlListeners.get("COBRO_SYNC");
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
			listener = (FlashXmlCallListener)this.urlListeners.get("COBRO_SYNC");
			if (listener != null)
			{
				String response = dimdimID;
				listener.onCompletion("SUCCESS",  response);
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
		//Window.alert("inside start responce checker urlListeners = "+urlListeners);
		//Window.alert("created response checker..."+urlListeners.get(code));
		//DebugPanel.getDebugPanel().addDebugMessage("created response checker..."+urlListeners.get(code));
		this.responseChecker = new Timer()
		{
			public void run()
			{
				FlashXmlCallListener listener = (FlashXmlCallListener)urlListeners.get(code);
				//Window.alert("inside checker......"+listener);
				//DebugPanel.getDebugPanel().addDebugMessage("inside checker... "+listener);
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
		//Window.alert("url = "+url);
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
