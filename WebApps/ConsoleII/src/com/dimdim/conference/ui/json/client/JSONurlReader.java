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
import com.google.gwt.user.client.Window;

//import	com.google.gwt.json.client.JSONParser;
//import	com.google.gwt.json.client.JSONValue;
//import	com.google.gwt.json.client.JSONException;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class JSONurlReader
{
//	public	static	int	callCount = 0;
	public	static	int	errorCount = 0;
	
	protected	static	boolean	busy = false;
	protected	static	Object	syncReturn;
//	protected	boolean		eventsReader = false;
	
	protected	String	url;
	protected	String	confKey = "null";
//	protected	JSONurlReaderCallback	callback;
//	protected	ResponseAndEventReader	jsonReader = new ResponseAndEventReader();
	protected	ResponseTextHandler handler;
	/*
	private	class	SyncReadingSupportCallback	implements	JSONurlReaderCallback
	{
		public	void	urlReadingComplete(UIServerResponse response)
		{
			if (response != null && response.isSuccess() && response.hasData())
			{
				JSONurlReader.syncReturn = response.getAvailableData();
//				Window.alert("SyncReadingSupportCallback:urlReadingComplete::"+syncReturn.toString());
			}
			else
			{
//				Window.alert("Received no data");
			}
		}
		public	void	serverConnectionLost(String message)
		{
			
		}
	}
	*/
	/**
	 * A simple fire and forget message dispatch.
	 */
	public	JSONurlReader(String url)
	{
		this.url = url;
		this.handler = new NoOpResponseTextHandler();
	}
	public	JSONurlReader(String url, ResponseTextHandler handler)
	{
		this.url = url;
		this.handler = handler;
//		this.callback = callback;
	}
	public	JSONurlReader(String url, String confKey, ResponseTextHandler handler)
	{
		this.url = url;
		this.confKey = confKey;
		this.handler = handler;
//		this.callback = callback;
	}
	
	protected class NoOpResponseTextHandler implements ResponseTextHandler
	{
		public	UIServerResponse	ret = null;
		
		public	NoOpResponseTextHandler()
		{
		}
		public void onCompletion(String responseText)
		{
		}
	}
	
	public	Object	doReadURLSync(long timeout)
	{
		JSONurlReader.syncReturn = null;
//		callback = new SyncReadingSupportCallback();
//		long tm = System.currentTimeMillis();
		
		doReadURL();
		
		/*
		while ((System.currentTimeMillis()-tm)<timeout)
		{
			//Window.alert("Waiting");
			if (JSONurlReader.syncReturn != null)
			{
				//Window.alert("url read returned");
				break;
			}
		}
		if (JSONurlReader.syncReturn != null)
		{
			//Window.alert("Returning:"+JSONurlReader.syncReturn.toString());
		}
		else
		{
			//Window.alert("No reply received within the wait time");
		}
		*/
		
		return	JSONurlReader.syncReturn;
	}
	public	void	doReadURL()
	{
		/*
		*/
//		//Window.alert("Trying to read::"+url);
//		if (handler == null)
//		{
//			handler = new JSONurlReaderResponseTextHandler();
//		}
		if (handler != null)
		{
			JSONurlReader.busy = true;
			try
			{
				if (!HTTPRequest.asyncGet(url+"&confKey="+confKey+"&cflag="+(getClientGUID()),handler))
				{
					//Window.alert("HTTPRequest.asyncGet failed");
					//	The call failed to issue. Trigger the callback immediately.
					JSONurlReader.busy = false;
//					UIServerResponse ret = new UIServerResponse();
//					ret.setSuccess(false);
//					if (callback != null)
//					{
//						callback.urlReadingComplete(ret);
//					}
				}
			}
			catch(Exception e)
			{
//				Window.alert(e.getMessage());
				JSONurlReader.busy = false;
				JSONurlReader.errorCount++;
			}
		}
	}
//	public	void	doPostURL()
//	{
//		this.doPostURL("");
//	}
//	public	void	doPostURL(String data)
//	{
//		if (handler != null)
//		{
//			JSONurlReader.busy = true;
//			try
//			{
//				if (!HTTPRequest.asyncPost(url+"&confKey="+confKey, data, handler))
//				{
//					JSONurlReader.busy = false;
//				}
//			}
//			catch(Exception e)
//			{
//				JSONurlReader.busy = false;
//			}
//		}
//	}
	public boolean isBusy()
	{
		return JSONurlReader.busy;
	}
	private native String getClientGUID() /*-{
	 return ($wnd.getAGuid());
	}-*/;
//	public static void setBusy(boolean busy)
//	{
//		JSONurlReader.busy = busy;
//	}
}
