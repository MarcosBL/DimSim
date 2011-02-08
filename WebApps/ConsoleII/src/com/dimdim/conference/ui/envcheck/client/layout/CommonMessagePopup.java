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

package com.dimdim.conference.ui.envcheck.client.layout;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.History;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import asquare.gwt.tk.client.ui.ModalDialog;

/**
 *	A simple message popup, that just shows a simple message. Only significant
 *	distinction is that the box can be a little stylized by putting the margin
 *	bars.
 */
public class CommonMessagePopup extends ModalDialog  implements PopupListener {

	protected	String		message;
	protected	Button			closeButton;
	protected	String			dialogName = null;
	protected	VerticalPanel		vp;
	protected	String				closeButtonText = "Close";
	protected	Widget		content;
	protected	boolean		showCloseButton = true;
	
	public	CommonMessagePopup(String dialogName, String title, Widget content)
	{
		this.dialogName = dialogName;
		this.content = content;
		setCaption(title,false);
	}
	public CommonMessagePopup(String title, String message)
	{
		this("common-message",title,message);
	}
	public CommonMessagePopup(String dialogName, String title, String message)
	{
		this.message = message;
		this.dialogName = dialogName;
		setCaption(title,false);
		Label c = new Label(this.message);
		c.setStyleName("common-text");
		c.setWordWrap(true);
		this.content = c;
	}
	public	void	setShowCloseButton(boolean b)
	{
		this.showCloseButton = b;
	}
	protected	void	drawDialog()
	{
		vp = new VerticalPanel();
		RoundedPanel rp = new RoundedPanel(vp);
		
		vp.setStyleName("common-dialog-outer-panel");
		rp.setStyleName("common-dialog-rounded-corner-panel");
	    
		DockPanel buttonPanel = new DockPanel();
		if (this.showCloseButton)
		{
			closeButton = new Button(closeButtonText, new ClickListener()
			{
				public void onClick(Widget sender)
				{
					hide();
					History.back();	//	TODO move out into a listener.
				}
			});
			closeButton.setStyleName("dm-popup-close-button");
			buttonPanel.add(closeButton,DockPanel.EAST);
			buttonPanel.setSpacing(0);
			HTML filler1 = new HTML("&nbsp;");
			buttonPanel.add(filler1,DockPanel.EAST);
		}
		if (this.dialogName != null)
		{
			//	Create a width adjustment panel.
			String widthStyle = this.dialogName+"-dialog-width";
			String heightStyle1 = this.dialogName+"-dialog-height-one";
			String heightStyle2 = this.dialogName+"-dialog-height-two";
			String contentWidthStyle = this.dialogName+"-dialog-content";
			
			content.addStyleName(contentWidthStyle);
			HorizontalPanel upperPanel = new HorizontalPanel();
			
			HTML upperLeftBar = new HTML("&nbsp;");
			upperLeftBar.setStyleName(heightStyle1);
			upperPanel.add(upperLeftBar);
			upperPanel.add(content);
			upperPanel.setCellWidth(content,"100%");
			upperPanel.setCellVerticalAlignment(content,VerticalPanel.ALIGN_MIDDLE);
			
			HorizontalPanel lowerPanel = new HorizontalPanel();
			lowerPanel.setStyleName(widthStyle);
			
			HTML lowerLeftBar = new HTML("&nbsp;");
			lowerLeftBar.setStyleName(heightStyle2);
			lowerPanel.add(lowerLeftBar);
			lowerPanel.add(buttonPanel);
			lowerPanel.setCellWidth(buttonPanel,"100%");
			lowerPanel.setCellHorizontalAlignment(buttonPanel,HorizontalPanel.ALIGN_RIGHT);
			lowerPanel.setCellVerticalAlignment(buttonPanel,VerticalPanel.ALIGN_MIDDLE);
			
			vp.add(upperPanel);
			vp.add(lowerPanel);
			this.addStyleName(this.dialogName+"-dialog-size");
		}
		else
		{
			vp.add(content);
			vp.setCellWidth(content,"100%");
			
			vp.add(buttonPanel);
			vp.setCellWidth(buttonPanel,"100%");
			vp.setCellHorizontalAlignment(buttonPanel,HorizontalPanel.ALIGN_RIGHT);
		}
		this.addPopupListener(this);
		
		this.add(vp);
	}
	public	void	show()
	{
		this.drawDialog();
		super.show();
		this.showDialogComplete();
	}
	public boolean onKeyDownPreview(char key, int modifiers)
	{
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key)
		{
			case KeyboardListener.KEY_ENTER:
			case KeyboardListener.KEY_ESCAPE:
				this.closeButton.click();
				break;
		}
		return true;
	}
	public	void	onPopupClosed(PopupPanel popup, boolean autoClosed)
	{
	}
	
	protected	void	showDialogComplete()
	{
		
	}
}

