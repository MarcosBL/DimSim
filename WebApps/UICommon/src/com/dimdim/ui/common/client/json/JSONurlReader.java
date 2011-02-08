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

package com.dimdim.ui.common.client.json;

import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class JSONurlReader
{
	public		static	int		errorCount = 0;
	protected	static	boolean	busy = false;
	
	protected	String	url;
	protected	ResponseTextHandler handler;
	
	/**
	 * A simple fire and forget message dispatch.
	 */
	private	JSONurlReader(String url)
	{
		this.url = url;
		this.handler = new NoOpResponseTextHandler();
	}
	private	JSONurlReader(String url, ResponseTextHandler handler)
	{
		this.url = url;
		this.handler = handler;
	}
	protected class NoOpResponseTextHandler implements ResponseTextHandler
	{
		public void onCompletion(String responseText)
		{
		}
	}
	public	boolean	doReadURL()
	{
		boolean	b = true;
		if (handler != null)
		{
			JSONurlReader.busy = true;
			try
			{
				if (!HTTPRequest.asyncGet(url+"&cflag="+(getClientGUID()),handler))
				{
					b = false;
				}
			}
			catch(Exception e)
			{
				JSONurlReader.errorCount++;
				b = false;
			}
			JSONurlReader.busy = false;
		}
		return	b;
	}
	public	boolean	doPostURL(String data)
	{
		boolean	b = true;
		if (handler != null)
		{
			JSONurlReader.busy = true;
			try
			{
				if (!HTTPRequest.asyncPost(url+"&cflag="+(getClientGUID()),data,handler))
				{
					b = false;
				}
			}
			catch(Exception e)
			{
				JSONurlReader.errorCount++;
				b = false;
			}
			JSONurlReader.busy = false;
		}
		return	b;
	}
	public boolean isBusy()
	{
		return JSONurlReader.busy;
	}
	private native String getClientGUID() /*-{
		return "1";
	}-*/;
}
