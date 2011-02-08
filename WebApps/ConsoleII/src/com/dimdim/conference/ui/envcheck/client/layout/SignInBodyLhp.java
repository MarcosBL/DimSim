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
import org.gwtwidgets.client.ui.PNGImage;

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SignInBodyLhp extends Composite
{
	protected	VerticalPanel	basePanel	=	new	VerticalPanel();
	
	protected	PNGImage	peopleTop;
	protected	PNGImage	peopleMiddle;
	protected	PNGImage	peopleBottom;
	
	
	public	SignInBodyLhp()
	{
		initWidget(basePanel);
		setStyleName("signin-body-lhp-panel");
		setWidth("523px");
		
		peopleTop = new PNGImage("images/people-top.png",523,75);
		basePanel.add(peopleTop);
		basePanel.setCellHorizontalAlignment(peopleTop,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(peopleTop,VerticalPanel.ALIGN_TOP);
		
		peopleMiddle = new PNGImage("images/people-middle.png",523,161);
		basePanel.add(peopleMiddle);
		basePanel.setCellHorizontalAlignment(peopleMiddle,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(peopleMiddle,VerticalPanel.ALIGN_TOP);
		
		peopleBottom = new PNGImage("images/people-bottom.png",523,33);
		peopleBottom.setStyleName("signin-page-image");
		basePanel.add(peopleBottom);
		basePanel.setCellHorizontalAlignment(peopleBottom,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(peopleBottom,VerticalPanel.ALIGN_TOP);
		
		String line1 = EnvGlobals.getDisplayString("start_page_lhp_line_1","");
		Label label1 = new Label(line1);
		basePanel.add(label1);
		label1.setStyleName("signin-body-lhp-line1");
		label1.addStyleName("common-text");
		basePanel.setCellHorizontalAlignment(label1,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(label1,VerticalPanel.ALIGN_TOP);
		
		String line2 = EnvGlobals.getDisplayString("start_page_lhp_line_2","");
		Label label2 = new Label(line2);
		basePanel.add(label2);
		label2.setStyleName("signin-body-lhp-line2");
		label2.addStyleName("common-text");
		basePanel.setCellHorizontalAlignment(label2,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(label2,VerticalPanel.ALIGN_TOP);
		
		String line3 = EnvGlobals.getDisplayString("start_page_lhp_line_3","");
		Label label3 = new Label(line3);
		basePanel.add(label3);
		label3.setStyleName("signin-body-lhp-line3");
		label3.addStyleName("common-text");
		basePanel.setCellHorizontalAlignment(label3,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(label3,VerticalPanel.ALIGN_TOP);
		
		String line4 = EnvGlobals.getDisplayString("start_page_lhp_line_4","");
		Label label4 = new Label(line4);
		basePanel.add(label4);
		label4.setStyleName("signin-body-lhp-line4");
		label4.addStyleName("common-text");
		basePanel.setCellHorizontalAlignment(label4,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(label4,VerticalPanel.ALIGN_TOP);
	}
}
