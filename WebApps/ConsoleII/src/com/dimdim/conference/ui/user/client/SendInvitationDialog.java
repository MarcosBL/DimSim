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

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.dialogues.client.SendInvitationPanel;
import com.dimdim.conference.ui.dialogues.client.common.CommonConsoleHelpers;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SendInvitationDialog	extends	CommonModalDialog
{
	protected	VerticalPanel	basePanel = new VerticalPanel();
	protected	InvitationsManager invitationsManager;
	protected	SendInvitationPanel	sendInvitationPanel;
	
	public	SendInvitationDialog(UserRosterManager userManager, UIRosterEntry currentUser)
	{
		super(UIStrings.getSendInvitationsLabel());
		
		this.invitationsManager = new InvitationsManager(userManager);
		CommonConsoleHelpers.setInvitationsManager(this.invitationsManager);
		sendInvitationPanel = new SendInvitationPanel(invitationsManager);
		sendInvitationPanel.setContainer(this);
		//InvitationPreviewDialog dlg = new InvitationPreviewDialog(invitationsManager,"", "");
		//dlg.drawDialog();
		
		basePanel.add(sendInvitationPanel);
		basePanel.setCellWidth(sendInvitationPanel,"100%");
		basePanel.setCellHorizontalAlignment(sendInvitationPanel,HorizontalPanel.ALIGN_LEFT);
		basePanel.setStyleName("send-invitation-small-form-box");
		
		super.dialogName = "small";
	}
	protected	void	panelOnDisplay()
	{
		this.sendInvitationPanel.panelOpened();
	}
	protected	Widget	getContent()
	{
		return	this.basePanel;
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		v.addElement(this.sendInvitationPanel.getSendInviteButton());
		v.addElement(this.sendInvitationPanel.getSendInviteLocalButton());
		
		return	v;
	}
	protected	FocusWidget	getFocusWidget()
	{
		return	this.sendInvitationPanel.getEmailTextBox();
	}
}
