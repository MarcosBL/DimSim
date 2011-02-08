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

package com.dimdim.conference.ui.envcheck.client.layout;

import asquare.gwt.tk.client.ui.ModalDialog;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class IFramePopup	extends	ModalDialog
{
	protected	Frame	frame	=	new	Frame();
	
	public	IFramePopup(String url, int contenWidth, int contentHeight)
	{
		this.setContentWidth(contenWidth+"px");
		this.setContentHeight(contentHeight+"px");
		this.frame = new Frame(url);
		this.frame.setStyleName("env-checks-helptext-frame");
		Element elem = this.frame.getElement();
		DOM.setIntAttribute(elem,"frameBorder",0);
		DOM.setAttribute(elem,"scrolling","no");
		this.add(this.frame);
		this.frame.setSize("100%","100%");
	}
	public	void	setUrl(String url)
	{
		this.frame.setUrl(url);
	}
}
