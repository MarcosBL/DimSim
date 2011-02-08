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

package com.dimdim.conference.ui.resources.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.resource.ResourceListEntry;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.managers.client.resource.ResourceManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceDeleteDialog	extends	CommonModalDialog implements ClickListener
{
	protected	Button			deleteButton;
	protected	ListEntry		listEntry;
//	protected	ResourceListEntryManager	listEntryManager;
	protected	ResourceManager		resourceManager;
	protected	UIResourceObject	resource;
	
	public	ResourceDeleteDialog(ResourceManager resourceManager,
				ListEntry listEntry)
	{
		super(UIStrings.getDeleteResourceDialogHeader());
		this.closeButtonText = UIStrings.getCancelLabel();
//		this.listEntryManager = listEntryManager;
		this.listEntry = listEntry;
		this.resource = ((ResourceListEntry)listEntry).getResource();
		this.resourceManager = resourceManager;
		super.dialogName = "large";
	}
	protected	Widget	getContent()
	{
		VerticalPanel table = new VerticalPanel();
		
		Label comment = new Label(ResourceGlobals.getResourceGlobals().getDeleteComment1(resource));
		comment.setStyleName("common-text");
		comment.addStyleName("common-table-header");
		table.add(comment);
		
		HorizontalPanel namePanel = new HorizontalPanel();
		
		Image image = listEntry.getImage1Url();
		namePanel.add(image);
		
		Label nameLabel = new Label(resource.getResourceName());
		nameLabel.setStyleName("common-text");
		nameLabel.addStyleName("common-table-text");
		namePanel.add(nameLabel);
		
//		Label qm = new Label("?");
//		qm.setStyleName("common-text");
//		qm.addStyleName("common-table-header");
//		namePanel.add(qm);
		
		table.add(namePanel);
		
//		String s = ResourceGlobals.getResourceGlobals().getDeleteComment2(resource);
//		if (s != null && s.length() > 0)
//		{
//			Label comment2 = new Label(s);
//			comment2.setStyleName("common-text");
//			comment2.addStyleName("common-table-header");
//			table.add(comment2);
//		}
		
//		HTML line1 = new HTML("&nbsp;");
//		line1.setStyleName("resource-delete-dialog-line-break");
//		table.add(line1);
		
		return	table;
	}
	protected	Label	createLabel(String labelText, String styleName)
	{
		Label html = new Label(labelText);
		html.setStyleName("common-table-header");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Label	createTextHTML(String text, String styleName)
	{
		Label html = new Label(text);
		html.setStyleName("common-table-text");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		deleteButton = new Button();
		deleteButton.setText(UIStrings.getDeleteLabel());
		deleteButton.setStyleName("dm-popup-close-button");
		deleteButton.addClickListener(this);
		v.addElement(deleteButton);
		
		return	v;
	}
	public	void	onClick(Widget w)
	{
		if (w == deleteButton)
		{
//			this.disableAllButtons();
			this.resourceManager.setProgressListener(this);
			this.resourceManager.deleteResource(this.resource);
			hide();
		}
	}
}
