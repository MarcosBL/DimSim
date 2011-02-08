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
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceRenameDialog	extends	CommonModalDialog implements ClickListener, KeyboardListener
{
	protected	Button	renameButton;
	protected	ListEntry		listEntry;
//	protected	ResourceListEntryManager	listEntryManager;
	protected	ResourceManager		resourceManager;
	protected	UIResourceObject	resource;
	protected	TextBox		newName = new TextBox();
	
	public	ResourceRenameDialog(ResourceManager resourceManager,
			ListEntry listEntry)
	{
		super(UIStrings.getRenameLabel());
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
		
//		Label comment = new Label("Rename");
//		comment.setStyleName("common-text");
//		comment.addStyleName("common-table-header");
//		table.add(comment);
		
		HorizontalPanel namePanel = new HorizontalPanel();
		
		Image image = listEntry.getImage1Url();
		namePanel.add(image);
		
		Label nameLabel = new Label(resource.getResourceName());
		nameLabel.setStyleName("common-text");
		nameLabel.addStyleName("common-table-text");
		namePanel.add(nameLabel);
		
		table.add(namePanel);
		
		Label comment2 = new Label(ResourceGlobals.getResourceGlobals().getRenameComment1(resource));
		comment2.setStyleName("common-text");
		comment2.addStyleName("common-table-header");
		table.add(comment2);
		
		this.newName = new TextBox();
		this.newName.setText("");
		this.newName.setStyleName("common-text");
		this.newName.addStyleName("common-table-text");
		this.newName.setStyleName("resource-rename-new-name");
		this.newName.setMaxLength(40);
		table.add(this.newName);
		this.newName.addKeyboardListener(this);
		
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
		
		renameButton = new Button();
		renameButton.setText(UIStrings.getRenameLabel());
		renameButton.setStyleName("dm-popup-close-button");
		renameButton.addClickListener(this);
		v.addElement(renameButton);
		renameButton.setEnabled(false);
		
		return	v;
	}
	public	void	onClick(Widget w)
	{
		if (w == renameButton)
		{
			String s = this.newName.getText();
			if (s != null && (s=s.trim()).length() >0)
			{
//				this.disableAllButtons();
				this.resourceManager.setProgressListener(this);
				this.resourceManager.renameResource(this.resource,s);
				this.listEntry.setName(s);
			}
			hide();
		}
	}
	public	void	commandExecSuccess(String message)
	{
		super.commandExecSuccess(message);
		this.newName.setEnabled(false);
	}
	protected	void	panelOnDisplay()
	{
		this.newName.setFocus(true);
	}
	protected	FocusWidget	getFocusWidget()
	{
		return	this.newName;
	}
	public void onKeyDown(Widget arg0, char arg1, int arg2)
	{
		this.checkText(arg0);
	}
	public void onKeyPress(Widget arg0, char arg1, int arg2)
	{
		this.checkText(arg0);
	}
	public void onKeyUp(Widget arg0, char arg1, int arg2)
	{
		this.checkText(arg0);
	}
	public void checkText(Widget arg0)
	{
		if (arg0 == this.newName)
		{
			String s = this.newName.getText();
			if (s != null && s.length() >0)
			{
				this.renameButton.setEnabled(true);
			}
			else
			{
				this.renameButton.setEnabled(false);
			}
		}
	}
}
