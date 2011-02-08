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

package com.dimdim.conference.ui.json.client;

import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * As the name suggests, this class checks the server availability and
 * accessibility by using a given fixed url. The url must return a string
 * with non zero length. This string can be anything. It is not analyzed
 * by this class except to check its length.
 * 
 * The checker accepts the url, the timeout and the number of max failures
 * as parameters. It returns success on any valid return.
 */

public class ServerAvailabilityChecker
{
	protected	String	url;
	protected	ServerAvailabilityListener	listener;
	protected	int		maxFailures;
	protected	int		waitTimeMillis;
	
	protected	int		numFailures = 0;
	protected	Timer	timer;
	protected	ServerCheckerResponseTextHandler	handler;
	
	private class ServerCheckerResponseTextHandler implements ResponseTextHandler
	{
		ServerAvailabilityChecker checker;
		
		public	ServerCheckerResponseTextHandler(ServerAvailabilityChecker checker)
		{
			this.checker = checker;
		}
		public void onCompletion(String responseText)
		{
//			Window.alert("Server Ping: response text: -"+responseText+"-");
			if (responseText != null && responseText.length() > 0)
			{
				//	Server is available. Call the appropriate method on the
				//	listener.
				checker.checkResult(true);
			}
			else
			{
				checker.checkResult(false);
			}
		}
	}
	public	ServerAvailabilityChecker(String url,int maxFailures,
				int waitTimeMillis, ServerAvailabilityListener listener)
	{
		this.url = url;
		this.maxFailures = maxFailures;
		this.listener = listener;
		this.waitTimeMillis = waitTimeMillis;
		this.handler = new  ServerCheckerResponseTextHandler(this);
		
		timer = new Timer()
		{
			public	void	run()
			{
				runCheck();
			}
		};
	}
	public	void	start()
	{
		this.timer.schedule(this.waitTimeMillis);
	}
	private	void	runCheck()
	{
		if (handler != null)
		{
			try
			{
				if (!HTTPRequest.asyncPost(url+"&cflag="+(getClientGUID()),"a=b",handler))
				{
					//	Failure
					checkResult(false);
				}
			}
			catch(Exception e)
			{
				//	Failure to contact the server. Increate
				checkResult(false);
			}
		}
	}
	private	void	checkResult(boolean success)
	{
		if (success)
		{
			if (this.listener != null)
			{
				this.listener.serverAvailable();
			}
			else
			{
				//	Nothing. No one to report to.
//				Window.alert("No listener to report success to");
			}
		}
		else
		{
			this.numFailures++;
			if (this.numFailures >= this.maxFailures)
			{
				if (this.listener != null)
				{
					this.listener.serverUnavailable();
				}
				else
				{
					//	Nothing. No one to report to.
//					Window.alert("No listener to report failure to");
				}
			}
			else
			{
				//	Schedule the check again.
				this.timer.schedule(this.waitTimeMillis);
			}
		}
	}
	private native String getClientGUID() /*-{
		return ($wnd.getAGuid());
	}-*/;
}
