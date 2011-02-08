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

package com.dimdim.conference.ui.common.client.util;

import java.util.HashMap;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ClickListener;

import com.dimdim.conference.ui.model.client.JSInterface;
import com.dimdim.conference.ui.model.client.JSCallbackListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class FlashCallbackHandler	implements	ClickListener, JSCallbackListener
{
	public String getListenerName()
	{
		return "PPT_EVENT";
	}
	public void handleCallFromJS(String data)
	{
	}
	public void handleCallFromJS2(String data1, String data2)
	{
		final String streamName = data1;
		final String eventName = data2;
//    	DebugPanel.getDebugPanel().addDebugMessage("Flash callback event:"+streamName+":"+eventName);
//		Window.alert("Call from flash- "+streamName+":"+eventName);
		final FlashStreamHandler streamHandler = (FlashStreamHandler)this.streamHandlers.get(streamName);
		if (streamHandler != null)
		{
//			Window.alert("Have a stream handler");
		    DeferredCommand.add(new Command()
		    {
		        public void execute()
		        {
		        	streamHandler.handleEvent(eventName);
		        }
		    });
//			streamHandler.handleEvent(eventName);
//			Window.alert("Flash callback handler complete");
		}
		else
		{
        	DebugPanel.getDebugPanel().addDebugMessage("Flash callback event: no handler for the stream");
		}
	}
	public void handleCallFromJS3(String data1, String data2, String data3)
	{
	}

	private	static	FlashCallbackHandler	handler;
	
	public	static	FlashCallbackHandler	getHandler()
	{
		if (FlashCallbackHandler.handler == null)
		{
			FlashCallbackHandler.handler = new FlashCallbackHandler();
			JSInterface.addCallbackListener(FlashCallbackHandler.handler);
		}
		return	FlashCallbackHandler.handler;
	}
	
	private	HashMap	streamHandlers = new HashMap();
	
	private	FlashCallbackHandler()
	{
		
	}
	/**
	 * At present only 1 handler per stream is allowed at present.
	 * 
	 * @param streamHandler
	 */
	public	void	addStreamHandler(FlashStreamHandler streamHandler)
	{
		this.streamHandlers.put(streamHandler.getStreamName(),streamHandler);
	}
	public	void	removeStreamHandler(FlashStreamHandler streamHandler)
	{
		this.streamHandlers.remove(streamHandler.getStreamName());
	}
	public	void	removeStreamHandler(String streamName)
	{
		this.streamHandlers.remove(streamName);
	}
	public	void	onClick(Widget w)
	{
		String streamName = getStreamNameFromFlash();
		final String eventName = getEventNameFromFlash();
//		Window.alert("Call from flash- "+streamName+":"+eventName);
		final FlashStreamHandler streamHandler = (FlashStreamHandler)this.streamHandlers.get(streamName);
		if (streamHandler != null)
		{
//			Window.alert("Have a stream handler");
		    DeferredCommand.add(new Command() {
		        public void execute() {
		        	streamHandler.handleEvent(eventName);
		        }
		      });
//			streamHandler.handleEvent(eventName);
//			Window.alert("Flash callback handler complete");
		}
	}
	private native String getStreamNameFromFlash() /*-{
		return ($wnd.streamName_from_flash);
	}-*/;
	private native String getEventNameFromFlash() /*-{
		return ($wnd.eventName_from_flash);
	}-*/;
}
