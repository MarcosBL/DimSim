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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.ui.common.client.flash;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.Element;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This widget encapsulates a flash movie, with given parameters. Objective
 * of this widget is to provide the following:
 * 
 * 1. Easier and friendlier flash player installation detection and upgrade.
 * 2. Communication between the flash movie and the rest of the browser.
 * 3. Minimize browser level page, frame or other component reloads.
 * 
 * At present this widget depends on the html page including the swfobject_source.js
 * file. In time sections of this script will be absorbed into this widget.
 */

public class SWFMovie	extends	ComplexPanel
{
	private Element m_contentDiv;
	
	protected	String	id;
	protected	String	name;
	protected	String	height;
	protected	String	width;
	protected	String	swfUrl;
	protected	String	color;
	
	protected	boolean	ie;
	
	public SWFMovie(String id, String name,
			String height, String width, String swfUrl, String color)
	{
		this.id = id;
		this.name = name;
		this.height = height;
		this.width = width;
		this.swfUrl = swfUrl/*+"$"+UIGlobals.getSWFVersionNumber()*/;
		this.color = color;
		
//		Window.alert("11");
		Element outer = DOM.createDiv();
//		Window.alert("12");
		setElement(outer);
		setStyleName("flash-container");
		
//		Window.alert("13");
		m_contentDiv = DOM.createDiv();
		setStyleName(m_contentDiv, "flash-content", true);
		DOM.appendChild(outer, m_contentDiv);
		DOM.setInnerHTML(m_contentDiv, "Loading..");
//		Window.alert("14");
	}
	
	private native boolean isIE() /*-{
		if (navigator.plugins && navigator.mimeTypes && navigator.mimeTypes.length) {
			return	false;
		}
		return	true;
	}-*/;
	
	private native boolean startMovie(Element elem, String movieUrl, String id2,
			String height, String width, String version, String color) /*-{
		
		$wnd.createAndWriteMovieElement(elem,movieUrl,id2,height,width,version,color);
		
		return true;
	}-*/;
	
	private	native void changeSlide(String nodeId, int slideIndex)/*-{
	
		$wnd.setSlideIndexInFlash(nodeId,slideIndex);
		
	}-*/;
	
	private	native void startPlaying(String nodeId)/*-{
		
		$wnd.startBroadcastingOrPlaying(nodeId);
		
	}-*/;

	private	native void stopPlaying(String nodeId)/*-{
		
		$wnd.stopBroadcastingOrPlaying(nodeId);
		
	}-*/;

	private	native void startPlayingElement(Element elem)/*-{
	
		$wnd.startBroadcastingOrPlaying2(elem);
		
	}-*/;
	
	private	native void stopPlayingElement(Element elem)/*-{
		
		$wnd.stopBroadcastingOrPlaying2(elem);
		
	}-*/;

	/**
	 * 
		// <![CDATA[
		var so = new SWFObject(movieUrl, id2, height, width, version, color);
		so.write(elementId);
		// ]]>
	 *
	 */
	public	void	show()
	{
		try
		{
			this.startMovie(m_contentDiv, swfUrl, name, height, width, "8", color);
		}
		catch(Exception e)
		{
		}
	}
	public	void	setSlide(String nodeId, int slideIndex)
	{
		try
		{
			this.changeSlide(nodeId,slideIndex);
		}
		catch(Exception e)
		{
		}
	}
	public	boolean	start(String nodeId)
	{
		try
		{
			this.startPlaying(nodeId);
			return	true;
		}
		catch(Exception e)
		{
			return	false;
		}
	}
	public	boolean	stop(String nodeId)
	{
		try
		{
			this.stopPlaying(nodeId);
			return	true;
		}
		catch(Exception e)
		{
			return	false;
		}
	}
	public	Element	getMovieElement()
	{
		return	this.m_contentDiv;
	}
	public String getHeight()
	{
		return height;
	}
	public void setHeight(String height)
	{
		this.height = height;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getSwfUrl()
	{
		return swfUrl;
	}
	public void setSwfUrl(String swfUrl)
	{
		this.swfUrl = swfUrl;
	}
	public String getWidth()
	{
		return width;
	}
	public void setWidth(String width)
	{
		this.width = width;
	}
	/**
	 * Whiteboard methods.
	 */
	public	boolean	lockWhiteboard(String nodeId)
	{
		try
		{
			this.lockWhiteboardJS(nodeId);
			return	true;
		}
		catch(Exception e)
		{
			return	false;
		}
	}
	private	native	void	lockWhiteboardJS(String nodeId)/*-{
		$wnd.lockWhiteboardInFlash(nodeId);
	}-*/;
	public	boolean	unlockWhiteboard(String nodeId)
	{
		try
		{
			this.unlockWhiteboardJS(nodeId);
			return	true;
		}
		catch(Exception e)
		{
			return	false;
		}
	}
	private	native	void	unlockWhiteboardJS(String nodeId)/*-{
		$wnd.unlockWhiteboardInFlash(nodeId);
	}-*/;
	public	void	resizeWhiteboard(String nodeId,int width, int height)
	{
		try
		{
			this.resizeWhiteboardJS(nodeId,width,height);
		}
		catch(Exception e)
		{
		}
	}
	private	native	void	resizeWhiteboardJS(String nodeId,int width,int height)/*-{
		$wnd.resizeWhiteboardInFlash(nodeId,width,height);
	}-*/;
}
