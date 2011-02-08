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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class HeaderLabel extends Composite
{
	protected	HorizontalPanel		basePanel = new HorizontalPanel();
	
	public	HeaderLabel(String text1, String text2)
	{
		initWidget(basePanel);
		
		Label label1 = new Label(text1);
		label1.setWordWrap(false);
		label1.setStyleName("signin-form-header-1");
		basePanel.add(label1);
		basePanel.setCellHorizontalAlignment(label1,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(label1,VerticalPanel.ALIGN_MIDDLE);
		
		Label label2 = new Label(text2);
		label2.setWordWrap(false);
		label2.setStyleName("signin-form-header-2");
		basePanel.add(label2);
		basePanel.setCellHorizontalAlignment(label2,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(label2,VerticalPanel.ALIGN_MIDDLE);
	}
}
