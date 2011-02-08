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

package com.dimdim.conference.ui.dialogues.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.user.client.IInvitationManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class InvitationPreviewDialog extends CommonModalDialog implements ClickListener
{
	protected	VerticalPanel	basePanel	=	null;
	protected	Button		sendButton = null;
	protected	Button		sendInviteLocal = new Button();
	protected	TextArea	presenters = null;
	protected	TextArea	attendees = null;
	protected	TextArea	message = null;
	protected   Label errorMessageLabel;
	
	protected	IInvitationManager	invitationManager;
	
	protected	String	currentPresenters;
	protected	String	currentAttendees;
	
	FormPanel fPanel = null; 
	
	public	InvitationPreviewDialog(IInvitationManager invitationManager,
				String currentPresenters, String currentAttendees)
	{
		super(UIStrings.getInvitationsPreviewDialogHeader());
		
		this.currentAttendees = currentAttendees;
		this.currentPresenters = currentPresenters;
		this.invitationManager = invitationManager;
		this.addStyleName("invitations-dialog-box");
	}
	
	protected	Widget	getContent()
	{
		basePanel	=	new VerticalPanel();
		presenters = new TextArea();
		attendees = new TextArea();
		message = new TextArea();
		
//		basePanel.setStyleName("send-invitation-preview-box");
//		Window.alert("3");
		
//		HTML presentersComment = new HTML(UIGlobals.getInvitePresentersComment());
//		basePanel.add(presentersComment);
//		basePanel.setCellWidth(presentersComment,"100%");
//		presentersComment.setStyleName("invitations-preview-comment");
//		basePanel.add(presenters);
//		basePanel.setCellWidth(presenters,"100%");
//		presenters.setStyleName("invitations-preview-textarea");
		
		HTML line1 = new HTML("&nbsp;");
		line1.setStyleName("line-break");
		basePanel.add(line1);
		
		
//		Window.alert("4");
		HTML attendeesComment = new HTML(UIGlobals.getInviteAttendeesComment());
		basePanel.add(attendeesComment);
		basePanel.setCellWidth(attendeesComment,"100%");
		attendeesComment.setStyleName("invitations-preview-comment");
		basePanel.add(attendees);
		basePanel.setCellWidth(attendees,"100%");
		attendees.setStyleName("invitations-preview-textarea");
		
		
		HTML line2 = new HTML("&nbsp;");
		line2.setStyleName("line-break");
		basePanel.add(line2);
		
//		Window.alert("5");
		HTML messageComment = new HTML(UIGlobals.getAddPersonalMessageComment());
		basePanel.add(messageComment);
		basePanel.setCellWidth(messageComment,"100%");
		messageComment.setStyleName("invitations-preview-comment");
		basePanel.add(message);
		message.setVisibleLines(4);
		message.setText(UIGlobals.getDefaultInvitationPersonalMessage());
		basePanel.setCellWidth(message,"100%");
		message.setStyleName("invitations-preview-textarea");
		
		HTML line3 = new HTML("&nbsp;");
		line3.setStyleName("line-break");
		basePanel.add(line3);
		
		attendees.setTabIndex(1);
		message.setTabIndex(2);
//		Window.alert("6");
		if (this.currentAttendees != null)
		{
			this.attendees.setText(this.currentAttendees);
		}
		if (this.currentPresenters != null)
		{
			this.presenters.setText(this.currentPresenters);
		}
		
		errorMessageLabel = new Label(UIStrings.getValidEmailRquired());
		errorMessageLabel.setStyleName("email-error-message");
		errorMessageLabel.setWordWrap(true);
		errorMessageLabel.setVisible(false);
		this.basePanel.add(errorMessageLabel);
		this.basePanel.setCellVerticalAlignment(errorMessageLabel, VerticalPanel.ALIGN_BOTTOM);
		
//		RoundedPanel rp = new RoundedPanel(basePanel);
//		rp.setStyleName("send-invitation-preview-rounded-corner-box");
		
//		Window.alert("7");
		
		attendees.addKeyboardListener(new KeyboardListenerAdapter(){
					public void onKeyDown(Widget arg0, char arg1, int arg2)
					{
						errorMessageLabel.setVisible(false);
					}
				}
			);
		
		return	basePanel;
	}
	public	void	onClick(Widget sender)
	{
		String token = null;
		String[] tokens = null;
		int checkResult = 0;
		
		if (sender == this.sendButton)
		{
			String p = getPresenters();
			String a = getAttendees();
			if (p.length() > 0 || a.length() > 0)
			{
				//Window.alert("DILIP inside else...a = "+a);
				a = a.replace(',', ';');
				a = a.replace('\n', ';');
				tokens = a.split(";");
				//Window.alert("DILIP after replacing = "+a);
				
				for (int i = 0; i < tokens.length; i++) {
					token = tokens[i].trim();
					//Window.alert("token ="+token);
					if(token.length() > 0)
					{
					    checkResult = checkEmail(token);
					    //Window.alert("checkResult ="+checkResult);
					    if (checkResult == 1)
					    {
						errorMessageLabel.setVisible(true);
						this.attendees.setFocus(true);
						break;
					    }
					}
				}//end of for
				if(checkResult != 1){
					String m = this.message.getText();
					this.invitationManager.sendInvitations(p,a,m);
					//DefaultCommonDialog.showMessage("Email Confirmation", UIStrings.getEmailConfirm());
					hide();
				}
			}
		}else if (sender== this.sendInviteLocal)
		{
		    String p = getPresenters();
		    String a = getAttendees();
		    if (a.length() > 0 || p.length() > 0)
			{
			a = a.replace(',', ';');
			a = a.replace('\n', ';');
			tokens = a.split(";");

			for (int i = 0; i < tokens.length; i++) {
				token = tokens[i].trim();
				if(token.length() > 0)
				{
				    checkResult = checkEmail(token);
				    if (checkResult == 1)
				    {
					messageLabel.setVisible(true);
					this.attendees.setFocus(true);
					break;
				    }
				}
			}//end of for
			if(checkResult != 1){
				this.invitationManager.sendInvitationsLocal(p,a, this.message.getText());
				//this.email.setText("");
				messageLabel.setVisible(false);
				hide();
			}

//				Window.alert("8");
			}
		}else{
			hide();
		}
	}
	
	private String getPresenters()
	{
		String s = this.presenters.getText();
		if (s != null && (s=s.trim()).length() > 0)
		{
			return	s;
		}
		return	"";
	}
	private String getAttendees()
	{
		String s = this.attendees.getText();
		if (s != null && (s=s.trim()).length() > 0)
		{
			return	s;
		}
		return	"";
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		sendButton = new Button();
		sendButton.setText(UIStrings.getSendInviteLabel());
		sendButton.setStyleName("invitations-preview-send-button");
		sendButton.setTitle("Click here to send mail using Dimdim");
		sendButton.addClickListener(this);
		
		sendInviteLocal = new Button();
		sendInviteLocal.setText(UIStrings.getSendInviteLocalLabel());
		sendInviteLocal.setStyleName("invitations-preview-send-button");
		sendInviteLocal.setTitle("Click here to send mail using Local Client");
		sendInviteLocal.addClickListener(this);
		
		sendButton.setTabIndex(3);
		sendInviteLocal.setTabIndex(4);
		
		v.addElement(sendButton);
		v.addElement(sendInviteLocal);
		
		return	v;
	}
	protected	void	showDialogComplete()
	{
		this.presenters.setFocus(true);
	}
	protected	FocusWidget	getFocusWidget()
	{
		return	this.attendees;
	}
	
	protected native int checkEmail(String email) /*-{
		return	$wnd.validateEmail(email);
	}-*/;
}
