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

package com.dimdim.conference.ui.model.client.dms;

import com.dimdim.conference.ui.json.client.JSONurlReader;
import com.dimdim.conference.ui.model.client.helper.FlashXmlCallInterface;
import com.dimdim.conference.ui.model.client.helper.FlashXmlCallListener;
//import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * This simple object allows the console to kick off the ppt conversion.
 * Timer is introduced simply to break up the processing and avoid a large
 * script. That has been seen to cause 'script running too slow' problem.
 */

public class PPTConversionStarter	implements	FlashXmlCallListener
{
	public	static	final	String	CONVERSION_START	=	"CONVERSION_START";
	
	protected	String	startUrl;
	protected	int		delayInterval;
	protected	PPTConversionStartListener	conversionStartListener;
	
	protected	JSONurlReader	urlReader;
	protected	Timer	timer;
	
	public PPTConversionStarter(final String startUrl,
				final PPTConversionStartListener conversionStartListener)
	{
		this.delayInterval = 1;
		this.startUrl = startUrl;
		this.conversionStartListener = conversionStartListener;
		FlashXmlCallInterface.getInterface().addUrlListener(CONVERSION_START,this);
		
//		this.urlReader = new JSONurlReader(startUrl,this);
//		this.timer = new Timer()
//		{
//			public	void	run()
//			{
//				try
//				{
////					Window.alert("Calling:"+startUrl);
//					urlReader.doReadURL();
//				}
//				catch(Exception e)
//				{
////					Window.alert(e.toString());
//					conversionStartListener.conversionStartError(e.toString());
//				}
//			}
//		};
	}
	public	void	startConversion()
	{
//		this.timer.schedule(this.delayInterval);
		FlashXmlCallInterface.getInterface().callXmlUrlInFlash(CONVERSION_START,startUrl,120000);
	}
	public void onCompletion(String result, String responseText)
	{
		//Window.alert("after startConversion = "+responseText );
		if (result != null && result.equalsIgnoreCase("ok"))
		{
			this.conversionStartListener.conversionStarted(responseText);
		}
		else
		{
			this.conversionStartListener.conversionStartError(responseText);
		}
	}
	public void onInterfaceResponse(String message)
	{
	}
}

