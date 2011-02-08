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
//import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.dimdim.conference.ui.model.client.helper.FlashXmlCallInterface;
import com.dimdim.conference.ui.model.client.helper.FlashXmlCallListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * This simple object allows the console to kick off the ppt conversion.
 * Timer is introduced simply to break up the processing and avoid a large
 * script. That has been seen to cause 'script running too slow' problem.
 */

public class PPTIDGenerator	implements	FlashXmlCallListener
{
	public	static	final	String	PPT_ID = "PPT_ID";
	
	protected	String	generatorUrl;
	protected	int		delayInterval;
	protected	PPTIDGenerationListener	pptIdGenerationListener;
	
	protected	JSONurlReader	urlReader;
	protected	Timer	timer;
	
	public PPTIDGenerator(final String generatorUrl,
			PPTIDGenerationListener conversionStartListener)
	{
		this.delayInterval = 1;
		this.generatorUrl = generatorUrl;
		//Window.alert("generatorUrl = "+generatorUrl);
		this.pptIdGenerationListener = conversionStartListener;
		
//		this.urlReader = new JSONurlReader(generatorUrl,this);
//		this.timer = new Timer()
//		{
//			public	void	run()
//			{
//				try
//				{
////					Window.alert("Calling:"+generatorUrl);
////					urlReader.doReadURL();
////					callXmlUrlInFlash("PPT_ID_GEN",generatorUrl);
//				}
//				catch(Exception e)
//				{
////					Window.alert(e.toString());
//					pptIdGenerationListener.pptIDGenerationFailed(e.toString());
//				}
//			}
//		};
	}
	public	void	generateNewPPTID()
	{
//		this.timer.schedule(this.delayInterval);
		FlashXmlCallInterface.getInterface().addUrlListener(PPT_ID,this);
		FlashXmlCallInterface.getInterface().callXmlUrlInFlash(PPT_ID,generatorUrl,10000);
	}
	public void onCompletion(String result, String responseText)
	{
//		Window.alert("after generate new pptid = result: -"+result+"- response: -"+responseText+"-" );
		FlashXmlCallInterface.getInterface().removeUrlListener(PPT_ID);
		if (result != null && result.equalsIgnoreCase("ok"))
		{
			this.pptIdGenerationListener.pptIDGenerated(responseText);
		}
		else
		{
			this.pptIdGenerationListener.pptIDGenerationFailed(responseText);
		}
	}
	public void onInterfaceResponse(String message)
	{
		this.pptIdGenerationListener.onInterfaceResponse(message);
	}
}

