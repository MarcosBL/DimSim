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
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple example of an 'about' dialog box.
 */
public class ConfirmationDialog extends CommonModalDialog implements ClickListener {
	
	protected	String	comment;
	protected	Widget	contentWidget;
	protected	Button	okButton;
	protected	ConfirmationListener	confirmationListener;
	protected	Label		messageText;
	protected	boolean		hideOnOK = true;
	
	public ConfirmationDialog(String title, String comment,
			String dialogName, ConfirmationListener confirmationListener)
	{
		super(title);
		this.comment = comment;
		this.dialogName = dialogName;
		this.confirmationListener = confirmationListener;
		this.closeListener = this;
		this.closeButtonText = UIGlobals.getNoLabelText();
	}
	protected	void	onEscapeKey()
	{
		if (this.confirmationListener != null)
		{
			this.confirmationListener.onCancel();
		}
		hide();
	}
//	private ConfirmationDialog(String title, Widget contentWidget,
//			String dialogName, ConfirmationListener confirmationListener)
//	{
//		super(title);
//		this.contentWidget = contentWidget;
//		this.dialogName = dialogName;
//		this.confirmationListener = confirmationListener;
//		this.closeListener = this;
//		this.closeButtonText = UIGlobals.getNoLabelText();
//	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		okButton = new Button();
		okButton.setText(UIGlobals.getYesLabelText());
		okButton.setStyleName("dm-popup-close-button");
		okButton.addClickListener(this);
		v.addElement(okButton);
		
		return	v;
	}
	public	void	setHideOnOK(boolean b)
	{
		this.hideOnOK = b;
	}
	public	void	hideOKButton()
	{
		this.okButton.setVisible(false);
	}
	public	void	hideCancelButton()
	{
		this.closeButton.setVisible(false);
	}
	public	void	showCancelButton()
	{
		this.closeButton.setVisible(true);
	}
	public	void	setButtonLabels(String okLabel, String cancelLabel)
	{
		this.okButton.setText(okLabel);
		this.closeButton.setText(cancelLabel);
	}
	protected	Widget	getContent()
	{
		if (this.contentWidget != null)
		{
			return	this.contentWidget;
		}
		else
		{
			VerticalPanel  vp = new VerticalPanel();
			
			messageText = new Label(this.comment);
			messageText.setWordWrap(true);
			messageText.setStyleName("common-text");
			vp.add(messageText);
			
			return	vp;
		}
	}
	public	void	setMessage(String message)
	{
		if (this.messageText != null)
		{
			this.messageText.setText(message);
		}
	}
	public	void	onClick(Widget w)
	{
		if (w == okButton)
		{
			if (this.confirmationListener != null)
			{
				this.confirmationListener.onOK();
			}
			if (this.hideOnOK)
			{
				hide();
			}
		}
		else if (w == this.closeButton)
		{
			if (this.confirmationListener != null)
			{
				this.confirmationListener.onCancel();
			}
		}
	}
}
