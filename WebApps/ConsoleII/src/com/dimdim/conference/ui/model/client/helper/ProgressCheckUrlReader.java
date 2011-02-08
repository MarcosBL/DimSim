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

//import com.dimdim.conference.ui.json.client.JSONurlReader;
//import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * This object read a given url at specified interval regularly till the
 * action that initiated the progress check is cancelled or completed.
 */
public class ProgressCheckUrlReader	implements	FlashXmlCallListener
{
	public	static	final	String	PROGRESS_CHECK	=	"PROGRESS_CHECK";
	
	protected	String	checkUrl;
	protected	int		checkInterval;
	protected	ProgressCheckListener	progressCheckListener;
	
//	protected	JSONurlReader	urlReader;
	protected	Timer	timer;
	protected	boolean	checkStopped = false;
	
	public ProgressCheckUrlReader(final String checkUrl, int checkInterval,
				ProgressCheckListener progressCheckListener)
	{
		this.checkInterval = checkInterval;
		this.checkUrl = checkUrl;
		this.progressCheckListener = progressCheckListener;
		FlashXmlCallInterface.getInterface().addUrlListener(PROGRESS_CHECK,this);
		
//		this.urlReader = new JSONurlReader(checkUrl,this);
		this.timer = new Timer()
		{
			public	void	run()
			{
				try
				{
//					Window.alert("JSONurlReader reading url:"+urlReader.toString());
//					urlReader.doReadURL();
					FlashXmlCallInterface.getInterface().callXmlUrlInFlash(PROGRESS_CHECK,checkUrl,20000);
				}
				catch(Exception e)
				{
					Window.alert(e.toString());
				}
			}
		};
	}
	public	void	startCheck()
	{
		this.checkStopped = false;
		this.timer.schedule(this.checkInterval);
	}
	public	void	stopCheck()
	{
		this.checkStopped = true;
	}
	/**
	 * If the 
	 */
	public void onCompletion(String result,String responseText)
	{
//		Window.alert(responseText);
		ProgressCheckResponse pcr = this.progressCheckListener.analyzeResponseText(responseText);
		if (pcr != null)
		{
			if (pcr.isActionComplete() || pcr.isActionCancelled())
			{
				//	The action was either cancelled or completed. Either way
				//	progress checkin most probably will have no action to
				//	take on this case.
			}
			else if (!this.checkStopped)
			{
				//	Schedule the timer again.
				this.timer.schedule(this.checkInterval);
			}
			this.progressCheckListener.processResponse(pcr);
		}
		else
		{
			//	This should never happen. However for the sake of completeness.....
		}
	}
	public void onInterfaceResponse(String message)
	{
		this.progressCheckListener.onInterfaceResponse(message);
	}
}

