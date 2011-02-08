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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.panels.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class StopRecordingQuestion1Widget extends VerticalPanel
{
	public	StopRecordingQuestion1Widget()
	{
		Label line1 = new Label("Each meeting can have a single recording.");
		line1.setWordWrap(true);
		line1.setStyleName("common-text");
		this.add(line1);
		this.setCellHorizontalAlignment(line1, HorizontalPanel.ALIGN_LEFT);
		this.setCellVerticalAlignment(line1, VerticalPanel.ALIGN_MIDDLE);
		
		Label line2 = new Label("If you restart recording later, you will overwrite this recording.");
		line2.setWordWrap(true);
		line2.setStyleName("common-text");
		this.add(line2);
		this.setCellHorizontalAlignment(line2, HorizontalPanel.ALIGN_LEFT);
		this.setCellVerticalAlignment(line2, VerticalPanel.ALIGN_MIDDLE);
		
		Label line3 = new Label("Do you want to stop recording now?");
		line3.setWordWrap(true);
		line3.setStyleName("common-text");
		this.add(line3);
		this.setCellHorizontalAlignment(line3, HorizontalPanel.ALIGN_LEFT);
		this.setCellVerticalAlignment(line3, VerticalPanel.ALIGN_MIDDLE);
	}
}
