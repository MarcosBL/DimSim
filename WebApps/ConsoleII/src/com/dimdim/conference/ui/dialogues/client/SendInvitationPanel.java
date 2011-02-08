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
 *	File Name  : RosterWidget.java
 *  Created On : Jul 2, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.ui.dialogues.client;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.user.client.IInvitationManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Jayant Pandit
 *
 */

public class SendInvitationPanel extends Composite implements ClickListener, KeyboardListener
{
	//protected	DockPanel panel = new DockPanel();
	
//	protected	DockPanel  tmp;
//	protected	Label  presenterLabel = new Label();
//	protected	CheckBox  asPresenter = new CheckBox();
	
	protected	Label  toLabel = new Label();
	protected	TextBox	email = new TextBox();
	
	protected	Button	sendInvite = new Button();
	protected	Button	sendInviteLocal = new Button();
	protected	Label	preview;
	protected   Label messageLabel;
	protected   boolean cleanEmailBox = true ;
	protected	IInvitationManager	invitationManager;
	
	//	The container dialog which must be closed before displaying the
	//	preview box.
	
	protected	CommonModalDialog	container;
	
	
	/**
	 * @param navigator
	 * @param panelId
	 */
	public SendInvitationPanel(IInvitationManager invitationManager)
	{
		//initWidget(panel);
		//panel.setStyleName("dm-send-invitation-panel");
		this.invitationManager = invitationManager;
		
//		panel.setWidth("85%");
		
		InvitationPreviewDialog dlg = new InvitationPreviewDialog(invitationManager,"", "");
		dlg.drawDialog();
		
		/*this.toLabel.setText(UIStrings.getInviteLabel());
		this.toLabel.setStyleName("console-label");
		this.panel.add(this.toLabel, DockPanel.NORTH);
		this.panel.setCellHorizontalAlignment(this.toLabel, HasAlignment.ALIGN_LEFT);
		this.panel.setCellVerticalAlignment(this.toLabel, HasAlignment.ALIGN_MIDDLE);
		this.panel.setCellWidth(this.toLabel, "100%");*/
		
//		this.presenterLabel.setText("As presenter: ");
//		this.presenterLabel.setStyleName("console-label");
//		tmp = new DockPanel();
//		tmp.add(this.presenterLabel,DockPanel.WEST);
//		tmp.add(this.asPresenter,DockPanel.WEST);
		
//		this.panel.add(this.tmp, DockPanel.NORTH);
//		this.panel.setCellHorizontalAlignment(this.tmp, HasAlignment.ALIGN_LEFT);
//		this.panel.setCellVerticalAlignment(this.tmp, HasAlignment.ALIGN_MIDDLE);
//		this.panel.setCellWidth(this.tmp, "100%");
		
//		this.email.setSize("120px","30px");
//		this.email.setWidth("94%");
		/*this.email.setStyleName("send-invitation-small-form-email");
		this.email.setMaxLength(80);
		this.email.addKeyboardListener(this);
		this.email.setText("Enter email id here");
		this.email.addClickListener(this);*/		
		
		/*this.panel.add(this.email, DockPanel.NORTH);
		this.panel.setCellHorizontalAlignment(this.email, HasAlignment.ALIGN_LEFT);
		this.panel.setCellVerticalAlignment(this.email, HasAlignment.ALIGN_MIDDLE);*/
//		this.panel.setCellWidth(this.email, "100%");
		
		/*this.sendInvite.setText(UIStrings.getSendInviteLabel());
		this.sendInvite.addClickListener(this);
		this.sendInvite.setStyleName("console-button");
		this.sendInvite.addStyleName("send-invite-button");*/
//		this.panel.add(this.sendInvite, DockPanel.NORTH);
//		this.panel.setCellHorizontalAlignment(this.sendInvite, HasAlignment.ALIGN_LEFT);
//		this.panel.setCellVerticalAlignment(this.sendInvite, HasAlignment.ALIGN_MIDDLE);
		/*this.sendInvite.setEnabled(false);
		
		this.sendInviteLocal.setText(UIStrings.getSendInviteLocalLabel());
		this.sendInviteLocal.addClickListener(this);
		this.sendInviteLocal.setStyleName("console-button");
		this.sendInviteLocal.addStyleName("send-invite-button");*/
//		this.panel.add(this.sendInvite, DockPanel.NORTH);
//		this.panel.setCellHorizontalAlignment(this.sendInvite, HasAlignment.ALIGN_LEFT);
//		this.panel.setCellVerticalAlignment(this.sendInvite, HasAlignment.ALIGN_MIDDLE);
		//this.sendInviteLocal.setEnabled(false);
		
		/*this.preview = new Label(UIStrings.getPreviewLabel());
		this.preview.addClickListener(this);
		this.preview.setStyleName("dm-send-invitation-panel-link");
		this.panel.add(this.preview, DockPanel.NORTH);
		this.panel.setCellHorizontalAlignment(this.preview, HasAlignment.ALIGN_LEFT);
		this.panel.setCellVerticalAlignment(this.preview, HasAlignment.ALIGN_MIDDLE);*/
		
		/*this.messageLabel = new Label(UIStrings.getValidEmailRquired());
		this.messageLabel.setStyleName("email-error-message");
		this.messageLabel.setWordWrap(true);
		this.messageLabel.setVisible(false);
		this.panel.add(messageLabel, DockPanel.SOUTH);*/
	}
	public CommonModalDialog getContainer()
	{
		return container;
	}
	public void setContainer(CommonModalDialog container)
	{
		this.container = container;
	}
	public	Button	getSendInviteButton()
	{
		return	this.sendInvite;
	}
	public	Button	getSendInviteLocalButton()
	{
		return	this.sendInviteLocal;
	}
	public	void	onClick(Widget w)
	{
//		Window.alert("1");
		String presenters = getPresenters();
//		Window.alert("2");
		String attendees = getAttendees();
		String token = null;
		String[] tokens = null;
		int checkResult = 0;
		
		if (w == this.preview)
		{
			if (this.container != null)
			{
				this.container.hide();
			}
//			Window.alert("4");
			this.invitationManager.displayPreviewPanel(presenters,attendees);
//			Window.alert("5");
			this.email.setText("");
//			this.asPresenter.setChecked(false);
		}
		else if (w == this.sendInvite)
		{
//			Window.alert("6");
			if (attendees.length() > 0 || presenters.length() > 0)
			{
				attendees = attendees.replace(',', ';');
				attendees = attendees.replace('\n', ';');
				tokens = attendees.split(";");

				for (int i = 0; i < tokens.length; i++) {
					token = tokens[i].trim();
					checkResult = checkEmail(token);
					if (checkResult == 1)
					{
						messageLabel.setVisible(true);
						this.email.setFocus(true);
						break;
					}
				}//end of for
				if(checkResult != 1){
					//Window.alert("DILIP inside else");
					this.invitationManager.sendInvitations(presenters,attendees,"");
					this.email.setText("");
					messageLabel.setVisible(false);
					//ConfirmationDialog dialogue = new ConfirmationDialog("test", "testComment", "testDlgName", );
					/*ConfirmationDialog cd = new ConfirmationDialog("test", "testComment", "testDlgName",
							new InviteConfirmationListener(this.invitationManager, presenters, attendees)
							);
					cd.drawDialog();*/
					DefaultCommonDialog.showMessage("Email Confirmation", UIStrings.getEmailConfirm());
				}

//				Window.alert("8");
			}
			
//			this.asPresenter.setChecked(false);
		} else if ((w == this.email) && cleanEmailBox)
		{	
			this.email.setText("");
			cleanEmailBox = false;
		}else if (w == this.sendInviteLocal)
		{
		    if (attendees.length() > 0 || presenters.length() > 0)
			{
			attendees = attendees.replace(',', ';');
			attendees = attendees.replace('\n', ';');
			tokens = attendees.split(";");

			for (int i = 0; i < tokens.length; i++) {
				token = tokens[i].trim();
				checkResult = checkEmail(token);
				if (checkResult == 1)
				{
					messageLabel.setVisible(true);
					this.email.setFocus(true);
					break;
				}
			}//end of for
			if(checkResult != 1){
				this.invitationManager.sendInvitationsLocal(presenters,attendees,"");
				this.email.setText("");
				messageLabel.setVisible(false);
			}

//				Window.alert("8");
			}
		}
			
			
//		Window.alert("9");
	}
	public String getPresenters()
	{
		/*
		if (asPresenter.isChecked())
		{
			String s = this.email.getText();
			if (s != null && (s=s.trim()).length() > 0)
			{
				return	s;
			}
		}
		*/
		return	"";
	}
	public String getAttendees()
	{
//		if (!asPresenter.isChecked())
//		{
			String s = this.email.getText();
			if (s != null && (s=s.trim()).length() > 0)
			{
				return	s;
			}
//		}
		return	"";
	}
	public	void	panelOpened()
	{
		this.email.setFocus(true);
	}
	public void onKeyDown(Widget arg0, char arg1, int arg2)
	{
		checkText(arg0);
	}
	public void onKeyPress(Widget arg0, char arg1, int arg2)
	{
		checkText(arg0);
	}
	public void onKeyUp(Widget arg0, char arg1, int arg2)
	{
		checkText(arg0);
	}
	public void checkText(Widget arg0)
	{
		messageLabel.setVisible(false);
		if (arg0 == this.email)
		{
			String s = this.email.getText();
			if (s != null && s.length() >0)
			{
				this.sendInvite.setEnabled(true);
				this.sendInviteLocal.setEnabled(true);
			}
			else
			{
				this.sendInvite.setEnabled(false);
				this.sendInviteLocal.setEnabled(false);
			}
		}
	}
	public	TextBox	getEmailTextBox()
	{
		return	this.email;
	}
	
	/*class InviteConfirmationListener implements ConfirmationListener{
		IInvitationManager invManager = null;
		 String presenters = null;
		 String attendees = null;
		
		public InviteConfirmationListener(IInvitationManager invManager, String presenters, String attendees){
			this.invManager = invManager;
			this.presenters = presenters;
			this.attendees = attendees;
		}
		public void onCancel() {
			// TODO Auto-generated method stub
			Window.alert("DILIP cancle");
			
		}

		public void onOK() {
			invitationManager.sendInvitations(presenters,attendees,"");
			
		}
		
	}*/
	
	protected native int checkEmail(String email) /*-{
  		return	$wnd.validateEmail(email);
	}-*/;
}
