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

public class SignInBodyPanel extends Composite
{
	protected	HorizontalPanel	basePanel = new HorizontalPanel();
	
	protected	SignInBodyLhp	lhpPanel;
	protected	SignInControls	formPanel;
	protected	SignInBodyRhp	rhpPanel;
	
	public	SignInBodyPanel()
	{
		initWidget(basePanel);
		setStyleName("signin-body-panel");
		
		lhpPanel = new SignInBodyLhp();
		formPanel = new SignInControls();
		rhpPanel = new SignInBodyRhp();
		
		basePanel.add(lhpPanel);
		basePanel.setCellHorizontalAlignment(lhpPanel,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(lhpPanel,VerticalPanel.ALIGN_TOP);
		
		basePanel.add(formPanel);
		basePanel.setCellHorizontalAlignment(formPanel,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(formPanel,VerticalPanel.ALIGN_TOP);
		
		basePanel.add(rhpPanel);
		basePanel.setCellHorizontalAlignment(rhpPanel,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(rhpPanel,VerticalPanel.ALIGN_TOP);
	}
	public	VerticalPanel	getFormContainerPanel()
	{
		return	formPanel.getFormContainerPanel();
	}
}
