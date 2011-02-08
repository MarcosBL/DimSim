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

package com.dimdim.conference.ui.dialogues.client.common;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple example of an 'about' dialog box.
 */
public class MeetingInfoDialog //extends CommonModalDialog 
{
	/*
	public MeetingInfoDialog()
	{
		super(UIStrings.getMeetingInfoDialogHeader());
		this.addStyleName("meeting-info-dialog-box");
		super.closeButtonText = UIStrings.getOKLabel();
	}
	protected	Vector	getFooterButtons()
	{
		return	null;
	}
	protected	Widget	getContent()
	{
		UIResources  uiResources = UIResources.getUIResources();
		FlexTable	table = new FlexTable();
		
		String	subject = uiResources.getConferenceInfo("subject");
		String	key = uiResources.getConferenceInfo("key");
		String	organizerName = uiResources.getConferenceInfo("organizerName");
		organizerName = UIRosterEntry.decodeBase64(organizerName);
		if (organizerName.length() > 50)
		{
			organizerName = organizerName.substring(0, 50);
		}
		String	organizerEmail = uiResources.getConferenceInfo("organizerEmail");
		String	startDate = uiResources.getConferenceInfo("startDate");
		String	joinURL = uiResources.getConferenceInfo("joinURL");
		String	mailToTag = "<a href=\"mailto:"+organizerEmail+
			"?subject="+subject+"\">"+organizerName+"</a>";
		
		table.setWidget(0, 0, createLabel(UIStrings.getMeetingInfoSubjectLabel()));
		table.setWidget(0, 1, createTextHTML(subject));
		table.setWidget(1, 0, createLabel(UIStrings.getMeetingInfoKeyLabel()));
		table.setWidget(1, 1, createTextHTML(key));
		table.setWidget(2, 0, createLabel(UIStrings.getMeetingInfoOrganizerLabel()));
		table.setWidget(2, 1, createTextHTML(mailToTag));
		table.setWidget(3, 0, createLabel(UIStrings.getMeetingInfoStartTimeLabel()));
		table.setWidget(3, 1, createTextHTML(startDate));
		table.setWidget(4, 0, createLabel(UIStrings.getMeetingInfoJoinURLLabel()));
		table.setWidget(4, 1, createTextHTML(joinURL));
		
		return	table;
	}
	protected	HTML	createLabel(String labelText)
	{
		HTML html = new HTML();
		html.setHTML("<span>"+labelText+": </span>");
		html.setStyleName("common-text");
		html.addStyleName("meeting-info-label");
		return	html;
	}
	protected	HTML	createTextHTML(String text)
	{
		HTML html = new HTML();
		html.setHTML("<span>"+text+"</span>");
		html.setStyleName("common-text");
		html.addStyleName("meeting-info-text");
		return	html;
	}
	*/
}
