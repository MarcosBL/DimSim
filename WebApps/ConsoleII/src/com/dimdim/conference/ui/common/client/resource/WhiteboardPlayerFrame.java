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

package com.dimdim.conference.ui.common.client.resource;

import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIWhiteboardControlEvent;

import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.WhiteboardModelListener;
import com.dimdim.conference.ui.common.client.LayoutGlobals;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The width and height passed to the constructor and the 
 */

public class WhiteboardPlayerFrame //extends Composite	//implements	WhiteboardModelListener
{
	/*
	//	These methods are really implemented by the resource player, which is responsible for
	//	showing and hiding the whiteboard similar to any and all other resources.
	
//	public void onWhiteboardStarted(UIWhiteboardControlEvent event)
//	{
//	}
//	public void onWhiteboardStopped(UIWhiteboardControlEvent event)
//	{
//	}
	
	protected	Frame	wbBroadcasterFrame	= null;
	protected	int		wbWidth = 640;
	protected	int		wbHeight = 480;
	protected	boolean	frameActive = false;
	protected	boolean	frameLoaded = false;
	
	protected	int		frameWidth;
	protected	int		frameHeight;
	protected	String	frameUrl;
	
	protected	UIRosterEntry	me;
	protected	ScrollPanel	scroller;
//	protected	ResponseAndEventReader	jsonReader = new ResponseAndEventReader();
	
	protected	int		lastKnownWidth;
	protected	int		lastKnownHeight;
	
	public	WhiteboardPlayerFrame(UIRosterEntry me, int width, int height)
	{
		this.me = me;
		scroller = new ScrollPanel();
		scroller.setHeight(height+"px");
		scroller.setWidth(width+"px");
		initWidget(scroller);
		
		wbBroadcasterFrame = new Frame();
		wbBroadcasterFrame.setStyleName("ppt-broadcaster-frame");
		scroller.add(wbBroadcasterFrame);
		
		setPanelSize(width,height);
//		ClientModel.getClientModel().getWhiteboardModel().addListener(this);
	}
	protected	void	setPanelSize(int containerWidth, int containerHeight)
	{
		this.lastKnownWidth = containerWidth;
		this.lastKnownHeight = containerHeight;
		this.frameWidth = containerWidth;
		this.frameHeight = containerHeight;
		if (this.frameWidth < this.wbWidth)
		{
			this.frameWidth = this.wbWidth;
		}
		if (this.frameHeight < this.wbHeight)
		{
			this.frameHeight = this.wbHeight;
		}
		
		this.scroller.setSize(containerWidth+"px",containerHeight+"px");
		this.wbBroadcasterFrame.setSize(frameWidth+"px",frameHeight+"px");
	}
	/**
	 * The whiteboard itself cannot be resized once created.
	 * 
	 * @param width
	 * @param height
	 *--/
	public	void	resizeWidget(int width, int height)
	{
		setPanelSize(width,height);
		if (this.frameActive)
		{
			closeWhiteboard();
			this.showWhiteboard();
		}
	}
	private	void	closeWhiteboard()
	{
		if (this.frameLoaded)
		{
//			this.closeWhiteboardJS("wbFrame");
			this.frameLoaded = false;
		}
	}
	public	void	onTabChange(boolean active)
	{
		this.frameActive = active;
		if (active)
		{
			this.showWhiteboard();
		}
		//else
		//{
		//	closeWhiteboard();
		//}
	}
	private	void	showWhiteboard()
	{
		try
		{
			DOM.setAttribute(wbBroadcasterFrame.getElement(),"id","tabContentFrame");
			DOM.setAttribute(wbBroadcasterFrame.getElement(),"name","tabContentFrame");
			
			String rtmpUrl = UIGlobals.getStreamingUrlsTable().getWhiteboardRtmpUrl();
			String rtmptUrl = UIGlobals.getStreamingUrlsTable().getWhiteboardRtmptUrl();
			
			String frameUrl = "/"+ConferenceGlobals.getWebappName()+
				"/html/whiteboard/GetWhiteboardModulePage.action?"+
				"name="+ConferenceGlobals.getConferenceKey()+
				"&rtmpUrl="+rtmpUrl+
				"&rtmptUrl="+rtmptUrl+
				"&width="+frameWidth+
				"&height="+frameHeight+
				"&t="+System.currentTimeMillis();
			this.wbBroadcasterFrame.setUrl(frameUrl);
			this.frameLoaded = true;
			
			Label headerLabel = LayoutGlobals.getWorkspaceHeaderLabel();
			if (headerLabel != null)
			{
				headerLabel.setText("Sharing Collaborative Whiteboard");
			}
		}
		catch(Exception e)
		{
//			Window.alert("Error while loading whiteboard frame:"+e.toString());
		}
	}
	public boolean isFrameActive()
	{
		return frameActive;
	}
	public void setFrameActive(boolean frameActive)
	{
		this.frameActive = frameActive;
	}
	/**
	 * This method will explicitly add the whiteboard movie to the panel. If the
	 * movie had already been created this method will forcefully remove it. In this
	 * case, the whiteboard movie does not need to be created more than once during
	 * the lifetime of the client console. This method is not expected to be used
	 * more than once.
	 * 
	 * @param width
	 * @param height
	 *--/
	public void onLockWhiteboard(UIWhiteboardControlEvent event)
	{
		this.lockWhiteboardJS("wbFrame");
	}
	public void onUnlockWhiteboard(UIWhiteboardControlEvent event)
	{
		this.unlockWhiteboardJS("wbFrame");
	}
//	private native void closeWhiteboardJS(String frameId) /*-{
//		$wnd.sendCloseWhiteboardToFrame(frameId);
//	}-*--/;
	private native void lockWhiteboardJS(String frameId) /*-{
		$wnd.sendLockWhiteboardToFrame(frameId);
	}-*--/;
	private native void unlockWhiteboardJS(String frameId) /*-{
		$wnd.sendUnlockWhiteboardToFrame(frameId);
	}-*--/;
	*/
}

