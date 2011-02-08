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

package com.dimdim.conference.ui.envcheck.client.command;

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;
import com.dimdim.conference.ui.envcheck.client.layout.CheckPanel;
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

public class DmFlashMovie	extends	ComplexPanel
{
//	protected	DockPanel	outer = new DockPanel();
	private Element m_contentDiv;
	
	protected	String	id;
	protected	String	name;
	protected	String	height;
	protected	String	width;
	protected	String	swfUrl;
	protected	String	color;
	
	protected	boolean	ie;
	
	public DmFlashMovie(String id, String name,
			String height, String width, String swfUrl, String color, CheckPanel checkPanel)
	{
		this.id = id;
		this.name = name;
		this.height = height;
		this.width = width;
		this.swfUrl = swfUrl/*+"$"+UIGlobals.getSWFVersionNumber()*/;
		this.color = color;
		
		Element outer = DOM.createDiv();
		setElement(outer);
		setStyleName("flash-container");
		m_contentDiv = DOM.createDiv();
		setStyleName(m_contentDiv, "flash-content", true);
		DOM.appendChild(outer, m_contentDiv);
		
		//DOM.setInnerHTML(m_contentDiv,
		//		"<p class=\"common-text\">"+EnvGlobals.getDisplayString("flashcheckfail.message","Adobe Flash Player (version 9 or higher) is needed to participate in a meeting. <p class=\"common-text\">Click on <a href=http://www.adobe.com/go/getflash/>this link</a> to download and install.")
				//"<p class=\"common-text\"><strong>Dimdim Web Meeting requires Adobe Flash Player version 9 or later.</strong>"
				//+"<p class=\"common-text\">Click on <a href=http://www.adobe.com/go/getflash/>this link</a> to download and install "+
				//"Adobe Flash player on this computer."+
				//"<p class=\"common-text\">After the Adobe Flash player installation please refresh this page in order to proceed."
		//	);
		checkPanel.setCheckFailed(EnvGlobals.getDisplayString("flashcheckfail.message","Adobe Flash Player (version 9 or higher) is needed. Please Click on <a href=http://www.adobe.com/go/getflash/>this link</a> to download and install."));
		
	}
	private native boolean startMovie(Element elem, String movieUrl, String id2,
			String height, String width, String version, String color) /*-{
		
		$wnd.createAndWriteMovieElement(elem,movieUrl,id2,height,width,version,color);
		
		return true;
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
//		Element elem = DOM.getElementById(id);
		try
		{
			// modifing to support flash 9 and above...
			this.startMovie(m_contentDiv, swfUrl, name, height, width, "6", color);
		}
		catch(Exception e)
		{
			Window.alert(e.getMessage());
		}
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
}
