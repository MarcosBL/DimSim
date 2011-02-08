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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SignInFullPage extends Composite
{
	protected	VerticalPanel	basePanel = new VerticalPanel();
	
	protected	SignInTopPanel	topPanel;
	protected	SignInBodyPanel	bodyPanel;
	
	public	SignInFullPage()
	{
		initWidget(basePanel);
		
		basePanel.setWidth("100%");
		setStyleName("signin-full-page");
		
		topPanel = new SignInTopPanel();
		basePanel.add(topPanel);
		basePanel.setCellHorizontalAlignment(topPanel,HorizontalPanel.ALIGN_CENTER);
		basePanel.setCellVerticalAlignment(topPanel,VerticalPanel.ALIGN_TOP);
		basePanel.setCellWidth(topPanel,"100%");
		
		bodyPanel = new SignInBodyPanel();
		basePanel.add(bodyPanel);
		basePanel.setCellHorizontalAlignment(bodyPanel,HorizontalPanel.ALIGN_CENTER);
		basePanel.setCellVerticalAlignment(bodyPanel,VerticalPanel.ALIGN_TOP);
		basePanel.setCellWidth(bodyPanel,"100%");
	}
	public	VerticalPanel	getFormContainerPanel()
	{
		return	bodyPanel.getFormContainerPanel();
	}
}
