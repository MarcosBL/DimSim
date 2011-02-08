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

package com.dimdim.conference.ui.common.client.util;

import java.util.Vector;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.Window;

/**
 * This dialog is used as a singleton by the rest of the console. This is to
 * ensure that there is only 1 dialog present and visible at a time. If the
 * console code needs to change messages to display, they need to send a
 * message to this class. If the dialog is on display the message will
 * simply be replaced in an existing dialog on display or a new dialog will
 * be created.
 * 
 * This dialog always exists, effectively, only for the duration of messages.
 * Which means that a new object is always created when required. All the
 * previous close and other listeners are obsolete as the dialog is closed.
 * This ensure that the console objects which use this dialog do not have
 * to manage state over a long period.
 */

public class CommonUserInformationDialog extends CommonModalDialog implements PopupListener {
	
	protected	static	CommonUserInformationDialog		theDialog;
	
	public	static	CommonUserInformationDialog		getCommonUserInformationDialog()
	{
		return	CommonUserInformationDialog.theDialog;
	}
	public	static	CommonUserInformationDialog		getCommonUserInformationDialog(String title, String message)
	{
		if (CommonUserInformationDialog.theDialog == null)
		{
			Label label =new Label(message);
			label.setStyleName("common-text");
			CommonUserInformationDialog.theDialog = new CommonUserInformationDialog(title, label);
		}
		return	CommonUserInformationDialog.theDialog;
	}
	public	static	CommonUserInformationDialog		getCommonUserInformationDialog(String title, Widget messageWidget)
	{
		if (CommonUserInformationDialog.theDialog == null)
		{
			CommonUserInformationDialog.theDialog = new CommonUserInformationDialog(title, messageWidget);
		}
		return	CommonUserInformationDialog.theDialog;
	}
	public	static	void	hideCommonUserInformationDialog()
	{
		if (CommonUserInformationDialog.theDialog != null)
		{
			CommonUserInformationDialog.theDialog.hide();
			CommonUserInformationDialog.theDialog = null;
		}
	}
	
//	protected	String			comment;
	protected	Widget			contentWidget;
	protected	Button			okButton;
//	protected	Label			messageText;
	protected	VerticalPanel	contentPanel;
	
//	private CommonUserInformationDialog(String title, String comment)
//	{
//		super(title);
//		this.comment = comment;
//		this.dialogName = "default-message";
//		this.closeButtonText = UIGlobals.getNoLabelText();
//		this.addPopupListener(this);
//	}
	private CommonUserInformationDialog(String title, Widget contentWidget)
	{
		super(title);
		this.contentWidget = contentWidget;
		this.dialogName = "small";
		this.closeButtonText = UIGlobals.getNoLabelText();
		this.addPopupListener(this);
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		okButton = new Button();
		okButton.setText(UIGlobals.getYesLabelText());
		okButton.setStyleName("dm-popup-close-button");
		v.addElement(okButton);
		
		return	v;
	}
	public	void	addOKClickListener(ClickListener listener)
	{
		this.okButton.addClickListener(listener);
	}
	public	void	removeOKClickListener(ClickListener listener)
	{
		this.okButton.removeClickListener(listener);
	}
	public	void	addCancelClickListener(ClickListener listener)
	{
		this.closeButton.addClickListener(listener);
	}
	public	void	removeCancelClickListener(ClickListener listener)
	{
		this.closeButton.removeClickListener(listener);
	}
	public	void	showOKButton()
	{
		this.okButton.setVisible(true);
	}
	public	void	hideOKButton()
	{
		this.okButton.setVisible(false);
	}
	public	void	showCancelButton()
	{
		this.closeButton.setVisible(true);
	}
	public	void	hideCancelButton()
	{
		this.closeButton.setVisible(false);
	}
	public	void	setButtonLabels(String okLabel, String cancelLabel)
	{
		this.okButton.setText(okLabel);
		this.closeButton.setText(cancelLabel);
	}
	protected	Widget	getContent()
	{
//		Window.alert("CUID - 1");
		contentPanel = new VerticalPanel();
//		if (this.contentWidget != null)
//		{
//			Window.alert("CUID - 2");
			contentPanel.add(this.contentWidget);
			contentPanel.setCellHorizontalAlignment(this.contentWidget, HorizontalPanel.ALIGN_CENTER);
			contentPanel.setCellVerticalAlignment(this.contentWidget, VerticalPanel.ALIGN_MIDDLE);
//		}
//		else
//		{
//			Window.alert("CUID - 3");
//			messageText = new Label(this.comment);
//			messageText.setWordWrap(true);
//			messageText.setStyleName("common-text");
//			this.contentWidget = messageText;
//			vp.add(messageText);
//			vp.setCellHorizontalAlignment(this.messageText, HorizontalPanel.ALIGN_CENTER);
//			vp.setCellVerticalAlignment(this.messageText, VerticalPanel.ALIGN_MIDDLE);
//		}
//		Window.alert("CUID - 4");
		return	contentPanel;
	}
	public	void	setMessage(String title, String message)
	{
		Label label =new Label(message);
		label.setStyleName("common-text");
		this.setContentWidget(title, label);
//		if (title != null)
//		{
//			this.captionText.setText(title);
//		}
//		if (comment == null)
//		{
//			//	This means that the dialog was added a complete widget.
//			this.comment = message;
//			messageText = new Label(this.comment);
//			messageText.setWordWrap(true);
//			messageText.setStyleName("common-text");
//			this.contentWidget = messageText;
//			vp.add(messageText);
//			vp.setCellHorizontalAlignment(this.messageText, HorizontalPanel.ALIGN_CENTER);
//			vp.setCellVerticalAlignment(this.messageText, VerticalPanel.ALIGN_MIDDLE);
//		}
//		else if (this.messageText != null)
//		{
//			this.messageText.setText(message);
//		}
	}
	public	void	setContentWidget(String title, Widget widget)
	{
		if (title != null)
		{
			this.captionText.setText(title);
		}
//		if (this.contentWidget != null)
//		{
//			Window.alert("setContentWidget:1");
//			this.contentPanel.remove(this.contentWidget);
			this.contentPanel.clear();
//			Window.alert("setContentWidget:2");
//			this.contentWidget = widget;
//			Window.alert("setContentWidget:3");
			this.contentPanel.add(widget);
//			Window.alert("setContentWidget:4");
			contentPanel.setCellHorizontalAlignment(widget, HorizontalPanel.ALIGN_CENTER);
//			Window.alert("setContentWidget:5");
			contentPanel.setCellVerticalAlignment(widget, VerticalPanel.ALIGN_MIDDLE);
//			Window.alert("setContentWidget:6");
//		}
	}
	public void onPopupClosed(PopupPanel sender, boolean autoClosed)
	{
		CommonUserInformationDialog.theDialog = null;
	}
}
