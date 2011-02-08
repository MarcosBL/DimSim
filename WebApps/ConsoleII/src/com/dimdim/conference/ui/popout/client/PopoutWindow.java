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

import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.EventsJsonHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.WindowResizeListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Objects of this class represent a poped out window from dimdim console.
 * Each such window may contain one or more widgets from the console. Each
 * of these widgets may consume events of one or more features. Each popout
 * window is a final popout. It is not allowed to popout any further windows.
 * 
 * This object does part of the work of NewConsole object for the main console.
 * The top level entry point work. It does not define the entry point itself
 * because it is a common object that will be used by each specific page
 * which will provide the specific panel to display in the poped out window.
 * 
 * Specifically:
 * 
 * 1.	Read the page parameters into globals as applicable.
 * 2.	Create the client model.
 */

public class PopoutWindow  implements WindowCloseListener
{
	
	protected	final String	windowId;
	protected	String	panelId;
	
	protected	PopoutWindowContent		popoutWindowContent;
	protected	PopoutPageEventsReader		eventsReader;
	protected	PopoutPageEventTextHandler	eventsTextHandler;
	
	protected	PopoutPageDataTextHandler	dataTextHandler;
	protected	PopoutPageDataReader		dataReader;
	
	private		PopoutResizeListener	resizeListener;
	/**
	 * 
	 * 
	 */
	public	PopoutWindow(final String windowId, String panelId, PopoutWindowContent popoutWindowContent)
	{
		this.windowId = windowId;
		this.panelId = panelId;
		this.popoutWindowContent = popoutWindowContent;
		
		ClientModel.createClientModel();
//		Window.alert("1");
		Window.addWindowCloseListener(this);
		resizeListener = new PopoutResizeListener();
		Window.addWindowResizeListener(resizeListener);
		
		popoutWindowContent.preparePopoutWindowContent();
		
//		Window.alert("2");
		this.eventsTextHandler = new PopoutPageEventTextHandler(EventsJsonHandler.getHandler());
//		Window.alert("3");
		this.dataTextHandler = new PopoutPageDataTextHandler(windowId,
				popoutWindowContent,this.eventsTextHandler);
		
//		Window.alert("4");
		this.dataReader = new PopoutPageDataReader(100,this.dataTextHandler);
//		Window.alert("5");
//		this.dataReader.start();
	    
//		Window.alert("6");
		this.eventsReader = new PopoutPageEventsReader(1000,this.eventsTextHandler);
//		Window.alert("7");
//		this.eventsReader.start();
	    
//		Window.alert("8");
//		Window.alert("9");
	    DeferredCommand.add(new Command() {
	        public void execute() {
	        	resizeListener.onWindowResized(Window.getClientWidth(), Window.getClientHeight());
	          
				String popoutLoadedMessage = "{objClass:\"PopoutPanelData\",windowId:\""+
					windowId+"\",panelId:\""+windowId+"\",dataText:\"POPOUT_LOADED\"}";
				reportPopoutLoadedToConsole(popoutLoadedMessage);
	        }
	      });
//		Window.alert("10");
	}
	public void onWindowClosed()
	{
		/*
		if (isCloseCallbackRequired())
		{
			String popoutClosedMessage = "{objClass:\"PopoutPanelData\",windowId:\""+
				windowId+"\",panelId:\""+windowId+"\",dataText:\"POPOUT_CLOSED\"}";
			reportPopoutClosedToConsole(popoutClosedMessage);
		}
		else
		{
			Window.alert("Popout closed by console");
		}
		*/
	}
	public String onWindowClosing()
	{
		if (isCloseCallbackRequired())
		{
			String popoutClosedMessage = "{objClass:\"PopoutPanelData\",windowId:\""+
				windowId+"\",panelId:\""+windowId+"\",dataText:\"POPOUT_CLOSED\"}";
			reportPopoutClosedToConsole(popoutClosedMessage);
		}
		else
		{
//			Window.alert("Popout closed by console");
		}
		return null;
	}
	public void doResize(int width, int height)
	{
		ConferenceGlobals.setContentWidth( width );
		ConferenceGlobals.setContentHeight( height );
		
		this.popoutWindowContent.resizePopoutWindowContent(width,height);
	}
	private native boolean isCloseCallbackRequired() /*-{
		return	$wnd.close_callback_required;
	}-*/;
	private native void reportPopoutLoadedToConsole(String msg) /*-{
		$wnd.sendMessageFromPopoutToConsole(msg);
	}-*/;
	private native void reportPopoutClosedToConsole(String msg) /*-{
		$wnd.sendMessageFromPopoutToConsole(msg);
	}-*/;
	class	PopoutResizeListener	implements	WindowResizeListener
	{
		PopoutResizeListener()
		{
		}
		public	void	onWindowResized(int w, int h)
		{
			PopoutResizeCommand command = new PopoutResizeCommand(w,h);
			DeferredCommand.addCommand(command);
		}
	}
	
	class	PopoutResizeCommand	implements	Command
	{
		int	width;
		int	height;
		public	PopoutResizeCommand(int w, int h)
		{
			this.width = w;
			this.height = h;
		}
		public	void	execute()
		{
			doResize(width,height);
		}
	}
}
