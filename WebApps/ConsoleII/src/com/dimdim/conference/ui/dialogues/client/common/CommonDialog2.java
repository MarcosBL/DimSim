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

package com.dimdim.conference.ui.dialogues.client.common;

import java.util.Vector;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.dialogues.client.common.helper.FixedLengthLabel;
import com.dimdim.conference.ui.model.client.CommandExecProgressListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A simple example of an 'about' dialog box.
 */
public abstract class CommonDialog2 //extends DialogBox  implements PopupListener, CommandExecProgressListener
{
	/*
	protected	Button			closeButton;
	protected	ClickListener	closeListener;
	protected	String			dialogName = null;
	protected	Vector			footerButtons = null;
//	protected	DockPanel		outer;
	protected	VerticalPanel		vp;
	protected	String				closeButtonText = UIStrings.getCloseLabel();
	protected	FixedLengthLabel		messageLabel = new FixedLengthLabel("",15);
//	protected	boolean			addLogoImage = true;
	
	public CommonDialog2(String title)
	{
		// Use this opportunity to set the dialog's caption.
		setText(title);
		addStyleName("dialog-z-index");
//		setSize("100%","100%");	Does not work. The dialog goes full browser size.
	}
	public	void	setCloseListener(ClickListener l)
	{
		closeListener = l;
	}
	public	void	addCloseListener(ClickListener cl)
	{
		closeButton.addClickListener(cl);
	}
	public	void	drawDialog()
	{
		vp = new VerticalPanel();
		RoundedPanel rp = new RoundedPanel(vp);
		setWidget(rp);
//		rp.setSize("100%","100%"); does not work. the panel goes full browser size.
		
		vp.setStyleName("common-dialog-outer-panel");
		rp.setStyleName("common-dialog-rounded-corner-panel");
	    
//		outer = new DockPanel();
//		outer.setStyleName("common-dialog-outer-panel");
//		if (this.addLogoImage)
//		{
//			outer.add(new Image(LOGO_IMAGE), DockPanel.WEST);
//		}
		DockPanel buttonPanel = new DockPanel();
		closeButton = new Button(closeButtonText, new ClickListener()
		{
			public void onClick(Widget sender)
			{
				hide();
			}
		});
		if (this.closeListener != null)
		{
			this.closeButton.addClickListener(this.closeListener);
		}
		closeButton.setStyleName("dm-popup-close-button");
		buttonPanel.add(closeButton,DockPanel.EAST);
		buttonPanel.setSpacing(0);
		HTML filler1 = new HTML("&nbsp;");
		buttonPanel.add(filler1,DockPanel.EAST);
		
		footerButtons = this.getFooterButtons();
		if (footerButtons != null)
		{
			int size = footerButtons.size();
			for (int i=0; i<size; i++)
			{
				Button button = (Button)footerButtons.elementAt(i);
				buttonPanel.add(button,DockPanel.EAST);
				HTML filler2 = new HTML("&nbsp;");
				buttonPanel.add(filler2,DockPanel.EAST);
			}
		}
		
		this.messageLabel.setStyleName("common-text");
		this.messageLabel.addStyleName("dialog-message-label");
		buttonPanel.add(this.messageLabel,DockPanel.WEST);
		buttonPanel.setCellVerticalAlignment(this.messageLabel,VerticalPanel.ALIGN_MIDDLE);
		buttonPanel.setCellWidth(this.messageLabel,"100%");
		
		Widget c = getContent();
		
		if (this.dialogName != null)
		{
			//	Create a width adjustment panel.
			String widthStyle = this.dialogName+"-dialog-width";
			String heightStyle1 = this.dialogName+"-dialog-height-one";
			String heightStyle2 = this.dialogName+"-dialog-height-two";
			String contentWidthStyle = this.dialogName+"-dialog-content";
			
			c.addStyleName(contentWidthStyle);
			HorizontalPanel upperPanel = new HorizontalPanel();
			
			HTML upperLeftBar = new HTML("&nbsp;");
			upperLeftBar.setStyleName(heightStyle1);
			upperPanel.add(upperLeftBar);
			upperPanel.add(c);
			upperPanel.setCellWidth(c,"100%");
			upperPanel.setCellVerticalAlignment(c,VerticalPanel.ALIGN_MIDDLE);
			
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
		}
		else
		{
			vp.add(c);
			vp.setCellWidth(c,"100%");
			
			vp.add(buttonPanel);
			vp.setCellWidth(buttonPanel,"100%");
			vp.setCellHorizontalAlignment(buttonPanel,HorizontalPanel.ALIGN_RIGHT);
		}
		this.addPopupListener(this);
		UIGlobals.closePreviousHoverPopup();
//		if (UIGlobals.consoleMiddleLeftPopup != null)
//		{
//			UIGlobals.consoleMiddleLeftPopup.hide();
//			UIGlobals.consoleMiddleLeftPopup = null;
//		}
	}
	public	void	setProgressMessage(String message)
	{
		this.messageLabel.setText(message);
	}
	public	void	commandExecSuccess(String message)
	{
		this.setProgressMessage(message);
		this.disableAllButtons(true);
		this.closeButton.setText(UIStrings.getCloseLabel());
		this.removeStyleName("cursor-wait");
//		this.hide();
	}
	public	void	commandExecError(String message)
	{
		this.setProgressMessage(message);
		this.enableAllButtons();
	}
	protected	void	disableAllButtons()
	{
		disableAllButtons(false);
	}
	protected	void	disableAllButtons(boolean closeButtonEnabled)
	{
		this.setAllButtonsEnabled(false,closeButtonEnabled);
		this.addStyleName("cursor-wait");
	}
	protected	void	enableAllButtons()
	{
		this.enableAllButtons(true);
	}
	protected	void	enableAllButtons(boolean closeButtonEnabled)
	{
		this.setAllButtonsEnabled(true,closeButtonEnabled);
		this.removeStyleName("cursor-wait");
	}
	protected	void	setAllButtonsEnabled(boolean enabled, boolean closeButtonEnabled)
	{
		this.closeButton.setEnabled(closeButtonEnabled);
		if (footerButtons != null)
		{
			int size = footerButtons.size();
			for (int i=0; i<size; i++)
			{
				Button button = (Button)footerButtons.elementAt(i);
				button.setEnabled(enabled);
			}
		}
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
//			case KeyboardListener.KEY_ENTER:
			case KeyboardListener.KEY_ESCAPE:
				this.closeButton.click();
//				hide();
				break;
		}
		return true;
	}
	public	void	onPopupClosed(PopupPanel popup, boolean autoClosed)
	{
		UIGlobals.theDialogBox = null;
	}
	
	protected	abstract Widget	getContent();
	
	protected	abstract Vector getFooterButtons();
	
	protected	void	showDialogComplete()
	{
		
	}
	*/
}

