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

package com.dimdim.conference.ui.popout.client;

import com.google.gwt.user.client.ResponseTextHandler;

import	com.google.gwt.json.client.JSONParser;
import	com.google.gwt.json.client.JSONValue;
import com.dimdim.conference.ui.json.client.ResponseAndEventReader;
import com.dimdim.conference.ui.json.client.JSONurlReaderCallback;
//import com.dimdim.conference.ui.json.client.JSONValue;
//import com.dimdim.conference.ui.json.client.JSONParser;
import com.dimdim.conference.ui.json.client.UIServerResponse;

public	class	PopoutPageEventTextHandler	implements	ResponseTextHandler
{
	protected	JSONurlReaderCallback	callback;
	protected	ResponseAndEventReader	jsonReader;
	
	public	PopoutPageEventTextHandler(JSONurlReaderCallback callback)
	{
		this.callback = callback;
		this.jsonReader = new ResponseAndEventReader();
	}
	public	void	onCompletion(String responseText)
	{
		try
		{
			if (responseText == null || responseText.length() == 0)
			{
			}
			else
			{
				if (responseText.startsWith("[") && responseText.endsWith("]"))
				{
					//	Then we have our buffer. This nominal check need to expand to
					//	a full crc check.
					if (responseText.length() > 10)
					{
						//Window.alert("-"+responseText+"-");
						JSONValue jsonObject = JSONParser.parse(responseText);
						if (jsonObject != null)
						{
							//Window.alert("-"+jsonObject+"-");
							UIServerResponse ret = jsonReader.readGetEventsResponse(jsonObject);
							//Window.alert("-"+ret+"-");
							if (callback != null && ret != null)
							{
								ret.setEventText(responseText);
								callback.urlReadingComplete(ret);
							}
							else
							{
								//Window.alert("No callback available to pass on the data:"+ret.toString());
							}
						}
						else
						{
							//Window.alert("*** JSONParser.parse returned null");
						}
					}
				}
				else
				{
					//	We have an unexpected bad return. Like a bad proxy in between.
					//	However this is a secondary listener, in a popout page. All real
					//	errors are handled by the console listener in the main page.
				}
			}
		}
		catch (Exception e)
		{
		}
	}
}
