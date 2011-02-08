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

import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class StartRecordingQuestion1Widget extends VerticalPanel
{
	public	StartRecordingQuestion1Widget()
	{
		String message1 = ConferenceGlobals.getDisplayString("workspace.recording.start.desc","");
		Label line1 = new Label(message1);
		line1.setStyleName("common-text");
		line1.setWordWrap(true);
		this.add(line1);
		this.setCellHorizontalAlignment(line1, HorizontalPanel.ALIGN_LEFT);
		this.setCellVerticalAlignment(line1, VerticalPanel.ALIGN_MIDDLE);
		
		String	productVersion = getProductVersion();
		
		Label line2 = new Label("Note: Whiteboard and Co-browsing are not recorded in this release ("+productVersion+")");
		line2.setStyleName("common-text");
		line2.setWordWrap(true);
		this.add(line2);
		this.setCellHorizontalAlignment(line2, HorizontalPanel.ALIGN_LEFT);
		this.setCellVerticalAlignment(line2, VerticalPanel.ALIGN_MIDDLE);
	}
	
	public static native String getProductVersion() /*-{
		return $wnd.productVersion;
	}-*/;
}
