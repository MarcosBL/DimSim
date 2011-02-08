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

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public	class	GetEventResponseJsonHandler	implements	JSONurlReaderCallback
{
	protected	HashMap		listeners;
	
	private	GetEventResponseJsonHandler(HashMap map)
	{
		this.listeners = map;
	}
	public	void	urlReadingComplete(UIServerResponse response)
	{
		if (response != null && response.isSuccess() && response.hasData())
		{
			ArrayList ary = response.getArrayList();
//			Object data = response.getData();
//			Window.alert("GetEventResponseJsonHandler::urlReadingComplete:"+ary);
			if (ary != null)
			{
				int size = ary.size();
//				Window.alert("Number of events:"+size);
				for (int i=0; i<size; i++)
				{
					UIServerResponse event = (UIServerResponse)ary.get(i);
//					Window.alert("GetEventResponseJsonHandler::urlReadingComplete:"+event);
					if (event != null && event.isSuccess() && event.hasData())
					{
						UIEventListener listener = (UIEventListener)this.listeners.get(event.getFeatureId());
						if (listener != null)
						{
							listener.onEvent(event.getEventId(),event.getAvailableData());
						}
						else
						{
							//Window.alert("No handler available for feature:"+event.getFeatureId());
						}
					}
				}
			}
			/*
			else if (data != null)
			{
				UIServerResponse event = (UIServerResponse)data;
				if (event != null && event.isSuccess() && event.hasData())
				{
					UIEventListener listener = (UIEventListener)this.listeners.get(event.getFeatureId());
					if (listener != null)
					{
						listener.onEvent(event.getEventId(),event.getAvailableData());
					}
				}
			}
			*/
			/**
			 * Single event block
//			Window.alert("Received event:"+response.getAvailableData().toString());
			UIEventListener listener = (UIEventListener)this.listeners.get(response.getFeatureId());
			if (listener != null)
			{
				listener.onEvent(response.getEventId(),response.getAvailableData());
			}
			else
			{
//				Window.alert("No listener for event:"+response.getAvailableData().toString());
			}
			*/
		}
//		else
//		{
//			this.listener.onEvent(null,null);
//		}
	}
	public	void	serverConnectionLost(String message)
	{
		
	}
	public	void	eventTimerStopped()
	{
		
	}
	public	void	eventTimerRestarted()
	{
		
	}
}
