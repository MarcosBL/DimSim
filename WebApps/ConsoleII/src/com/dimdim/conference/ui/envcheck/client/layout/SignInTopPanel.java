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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import org.gwtwidgets.client.ui.PNGImage;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SignInTopPanel extends Composite 
{
	protected	HorizontalPanel	basePanel	=	new HorizontalPanel();
	
	protected	PNGImage	logo;
	protected	Label		localeChangeLink;
	protected	Image		arrow;
	
	public	SignInTopPanel()
	{
		initWidget(basePanel);
		setWidth("946px");
		setStyleName("signin-top-panel");
		
		String url = this.getLogoImageUrl();
		String width = this.getLogoImageWidth();
		String height = this.getLogoImageHeight();
		logo = new PNGImage(url,Integer.parseInt(width),Integer.parseInt(height));
		basePanel.add(logo);
		basePanel.setCellHorizontalAlignment(logo,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(logo,VerticalPanel.ALIGN_TOP);
		
		Label filler = new Label(" ");
		basePanel.add(filler);
		basePanel.setCellWidth(filler,"100%");
		basePanel.setCellHorizontalAlignment(filler,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(filler,VerticalPanel.ALIGN_TOP);
	}
	private native String getLogoImageUrl() /*-{
		return ($wnd.logo_image_url);
	}-*/;
	private native String getLogoImageWidth() /*-{
		return ($wnd.logo_image_width);
	}-*/;
	private native String getLogoImageHeight() /*-{
		return ($wnd.logo_image_height);
	}-*/;
}
