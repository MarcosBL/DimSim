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

package com.dimdim.conference.ui.dialogues.client.common;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class AboutCommonDialog	extends	CommonModalDialog
{
	private	static	AboutCommonDialog		dcd;
	
	protected	Widget	content;
	protected	Vector	footerButtons;
	protected	HTML	html;
	
	public	AboutCommonDialog(String title, Widget content, String dialogName)
	{
		this(title, content, null, dialogName);
	}
	private	AboutCommonDialog(String title, Widget content,
				Vector footerButtons, String dialogName)
	{
		super(title);
		this.content = content;
		this.footerButtons = footerButtons;
		this.dialogName = dialogName;
		this.closeButtonText = UIStrings.getOKLabel();		
		UIResources  uiResources = UIResources.getUIResources();
		FlexTable productInfo = new FlexTable();
		String	productVersion = getProductVersion();
		String	premiumCatalogue = getPremiumCatalogue();
		String	premiumCatalogueLogo = UIStrings.getAboutPoweredLogo();
//		Window.alert(premiumCatalogueLogo);
		productInfo.setWidget(0, 0, createTextHTML(productVersion));
		productInfo.getFlexCellFormatter().setColSpan(0, 0, 3);
		//Window.alert(getProductVersion());
//		Window.alert(premiumCatalogue);
		//Window.alert(uiResources.getConferenceInfo("dimdim.premiumCatalogue"));
		if(premiumCatalogue.equalsIgnoreCase("1"))
		{
//			Window.alert("comes here 1");
			Image logo = new Image();
			logo.setUrl(premiumCatalogueLogo);
			productInfo.setWidget(0, 3, logo);
//			Window.alert("comes here 2");
			productInfo.setWidget(0, 4, createTextHTML("<div style=\"color: #1b61da;\">Powered By <a href=\"http://www.dimdim.com\" target=\"_blank\"><u>Dimdim"));	
			productInfo.getFlexCellFormatter().setColSpan(0, 4, 9);
//			Window.alert("comes here 3");
			//productInfo.setWidth("2px");				
		}
		productInfo.setStyleName("common-text");
//		Window.alert("comes here 4");
		buttonPanel.add(productInfo,DockPanel.WEST);
//		Window.alert("comes here 5");
	}

	protected	HTML	createTextHTML(String text)
	{
		HTML html = new HTML();
		html.setHTML("<span>"+text+"</span>");
		html.setStyleName("common-text");
		html.addStyleName("about-info-text");
		return	html;
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
//	private	static	AboutCommonDialog	createDialog(String title, String message)
//	{
//		HTML html = new HTML(message);
//		html.setStyleName("common-text");
//		html.setWordWrap(true);
//		
//		AboutCommonDialog dlg = new AboutCommonDialog(title,html,"small");
//		dlg.html = html;
//		return	dlg;
//	}
//	private	static	void	showMessage(String title, String message,PopupListener popupListener)
//	{
//		AboutCommonDialog.showMessage(title,message,"small",popupListener);
//	}
//	private	static	void	showMessage(String title, String message)
//	{
//		AboutCommonDialog.showMessage(title,message,"small",null);
//	}
//	private	static	void	showMessage(String title, String message, String dialogName)
//	{
//		AboutCommonDialog.showMessage(title,message,dialogName,null);
//	}
//	private	static	void	showMessage(String title, String message, String dialogName, PopupListener popupListener)
//	{
//		hideMessageBox();
//		HTML html = new HTML(message);
//		html.setStyleName("common-text");
//		html.setWordWrap(true);
//		dcd = new AboutCommonDialog(title,html,dialogName);
//		if (popupListener != null)
//		{
//			dcd.addPopupListener(popupListener);
//		}
//		dcd.drawDialog();
//		dcd.addPopupListener(new PopupListener()
//		{
//			public void onPopupClosed(PopupPanel dialog, boolean autoClosed)
//			{
//				dcd = null;
//			}
//		});
////		dcd = null;		
//	}
//	public	static	void	hideMessageBox()
//	{
//		if (dcd != null)
//		{
//			dcd.hide();
//			dcd = null;
//		}
//	}
//	public	static	AboutCommonDialog	getDialog()
//	{
//		return	AboutCommonDialog.dcd;
//	}
	
	public static native String getPremiumCatalogue() /*-{
	return $wnd.premiumCatalogue;
	}-*/;

	public static native String getProductVersion() /*-{
	return $wnd.productVersion;
	}-*/;

	
}
