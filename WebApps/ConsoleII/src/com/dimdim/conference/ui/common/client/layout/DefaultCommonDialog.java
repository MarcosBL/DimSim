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

package com.dimdim.conference.ui.common.client.layout;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupListener;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DefaultCommonDialog	extends	CommonModalDialog
{
	private	static	DefaultCommonDialog		dcd;
	
	protected	Widget	content;
	protected	Vector	footerButtons;
	protected	HTML	html;
	
	public	DefaultCommonDialog(String title, Widget content, String dialogName)
	{
		this(title, content, null, dialogName);
	}
	public	DefaultCommonDialog(String title, Widget content,
				Vector footerButtons, String dialogName)
	{
		super(title);
		this.content = content;
		this.footerButtons = footerButtons;
		this.dialogName = dialogName;
		this.closeButtonText = UIStrings.getOKLabel();
	}
	public	void	setMessageText(String messageText)
	{
		if (this.html != null)
		{
			this.html.setText(messageText);
		}
	}
	public	void	hideCloseButton()
	{
		this.closeButton.setVisible(false);
	}
	protected	Vector	getFooterButtons()
	{
		return	this.footerButtons;
	}
	protected	Widget	getContent()
	{
		return	this.content;
	}
	public	static	DefaultCommonDialog	createDialog(String title, String message)
	{
		HTML html = new HTML(message);
		html.setStyleName("common-text");
		html.setWordWrap(true);
		
		DefaultCommonDialog dlg = new DefaultCommonDialog(title,html,"default-message");
		dlg.html = html;
		return	dlg;
	}
	public	static	void	showMessage(String title, String message,PopupListener popupListener)
	{
		DefaultCommonDialog.showMessage(title,message,"default-message",popupListener);
	}
	public	static	void	showMessage(String title, String message)
	{
		DefaultCommonDialog.showMessage(title,message,"default-message",null);
	}
	public	static	void	showMessage(String title, String message, String dialogName)
	{
		DefaultCommonDialog.showMessage(title,message,dialogName,null);
	}
	public	static	void	showMessage(String title, String message, String dialogName, PopupListener popupListener)
	{
		hideMessageBox();
		HTML html = new HTML(message);
		html.setStyleName("common-text");
		html.setWordWrap(true);
		dcd = new DefaultCommonDialog(title,html,dialogName);
		if (popupListener != null)
		{
			dcd.addPopupListener(popupListener);
		}
		dcd.drawDialog();
		dcd.addPopupListener(new PopupListener()
		{
			public void onPopupClosed(PopupPanel dialog, boolean autoClosed)
			{
				dcd = null;
			}
		});
//		dcd = null;		
	}
	public	static	void	hideMessageBox()
	{
		if (dcd != null)
		{
			dcd.hide();
			dcd = null;
		}
	}
	public	static	DefaultCommonDialog	getDialog()
	{
		return	DefaultCommonDialog.dcd;
	}
}
