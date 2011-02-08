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

package com.dimdim.conference.ui.user.client;

import java.util.Vector;

//import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SetDisplayNamePopup extends CommonModalDialog implements ClickListener
{
	protected	VerticalPanel		basePanel = new VerticalPanel();
	protected	UIRosterEntry		currentUser;
	protected	Button			sendButton = null;
	//protected	HTML			settingsMainComment = new HTML(UIGlobals.getSettingsMainComment());
	protected	TextBox			displayName = null;
	protected	UserRosterManager	userManager;
	protected	ListEntry		userListEntry;
	Label errorLabel = new Label();
	
	public	SetDisplayNamePopup(UserRosterManager userManager,UIRosterEntry currentUser, ListEntry userListEntry)
	{
		super(ConferenceGlobals.getDisplayString("setdisplay.header","Set Display Name"));
		this.currentUser = currentUser;
		this.userManager = userManager;
		this.userListEntry = userListEntry;
		this.dialogName = "small";
	}
	
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		sendButton = new Button();
		sendButton.setText(UIStrings.getOKLabel());
		sendButton.setStyleName("dm-popup-close-button");
		sendButton.addClickListener(this);
		v.addElement(sendButton);
		
		return	v;
	}

	protected Widget getContent() {
		//basePanel.add(this.settingsMainComment);
		//basePanel.setCellWidth(this.settingsMainComment,"100%");
		//this.settingsMainComment.setStyleName("invitations-preview-comment");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.addStyleName("chat-text-area");
		
		Label label = new Label("New Name:");
		label.setStyleName("common-text");
		label.addStyleName("meeting-info-label");
		hPanel.add(label);
		
		displayName = new TextBox();
		displayName.setText(currentUser.getDisplayName());
		displayName.setStyleName("common-text");
		displayName.addStyleName("chat-text-area");
		displayName.setMaxLength(12);
		hPanel.add(displayName);
		
		basePanel.add(hPanel);
		basePanel.setCellVerticalAlignment(hPanel, VerticalPanel.ALIGN_MIDDLE);
		
		basePanel.add(new Label(" "));
		
		displayName.addClickListener(new ClickListener()
		{

			public void onClick(Widget sender) {
				errorLabel.setText(" ");
			}
			
		});
		errorLabel.setStyleName("common-text");
		errorLabel.addStyleName("common-error-message");
		basePanel.add(errorLabel);
		
		return	basePanel;
	}

	public void onClick(Widget sender) {
		if (sender == this.sendButton)
		{
			String name = displayName.getText();
			String base64Name = UIRosterEntry.encodeBase64(displayName.getText());

			if(name != null && name.length() > 0)
			{
				if(name.length() < 13)
				{
					this.userManager.setDisplayName(base64Name);
					this.currentUser.setDisplayName(name);
					this.userListEntry.setName(name);
					hide();
				}else{
					errorLabel.setText(ConferenceGlobals.getDisplayString("setdisplay.error1","Display name can not be more then 12 Char"));
				}
			}else{
				errorLabel.setText(ConferenceGlobals.getDisplayString("setdisplay.error","Display name can not be empty"));
			}
		}
		
	}
	
}
