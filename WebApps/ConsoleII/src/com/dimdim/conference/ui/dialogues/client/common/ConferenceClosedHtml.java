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
/*
 **************************************************************************
 *	File Name  : IConstants.java
 *  Created On : Jun 13, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.ui.dialogues.client.common;

import com.dimdim.conference.ui.model.client.ClientStateModelListener;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Saurav Mohapatra
 *
 */
public class ConferenceClosedHtml	extends	Composite	implements	PopupListener
{
	private ClientStateModelListener csml;
	private FlexTable table = new FlexTable();
	
	public	ConferenceClosedHtml(ClientStateModelListener csml)
	{
		initWidget(table);
		
		String line1 = ConferenceGlobals.getDisplayString("conference_closed_message.line1",
				"The Presenter (who invited you for this web");
		String line2 = ConferenceGlobals.getDisplayString("conference_closed_message.line2",
				"conference) has left the web conference. Thanks");
		String line3 = ConferenceGlobals.getDisplayString("conference_closed_message.line3",
				"for participating in a Dimdim web conference.");
		String line4 = ConferenceGlobals.getDisplayString("conference_closed_message.line4",
				"You will be leaving this web conference on");
		String line5 = ConferenceGlobals.getDisplayString("conference_closed_message.line5",
				"clicking the OK button.");
		
		table.setWidget(0,0,createText(line1));
		table.setWidget(1,0,createText(line2));
		table.setWidget(2,0,createText(line3));
		table.setWidget(3,0,createText(line4));
		table.setWidget(4,0,createText(line5));
		
		this.csml = csml;
	}
	protected	Label	createText(String text)
	{
		Label html = new Label(text);
		html.setStyleName("common-table-text");
		return	html;
	}
	public	void	onPopupClosed(PopupPanel pp, boolean autoClosed)
	{
		this.csml.closeConsole();
	}
}
