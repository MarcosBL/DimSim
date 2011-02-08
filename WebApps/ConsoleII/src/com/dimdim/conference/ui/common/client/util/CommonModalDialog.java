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

package com.dimdim.conference.ui.common.client.util;

import java.util.Vector;

import asquare.gwt.tk.client.ui.ModalDialog;
import asquare.gwt.tk.client.ui.behavior.FocusModel;

//import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.model.client.CommandExecProgressListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public abstract class CommonModalDialog extends ModalDialog implements CommandExecProgressListener, HasFocus
{
	protected	String		title;
	protected	Button			closeButton;
	protected	ClickListener	defaultCloseListener;
	protected	ClickListener	closeListener;
	protected	String			dialogName = null;
	protected	Vector			footerButtons = null;
	protected	VerticalPanel		vp;
	protected	String				closeButtonText = UIStrings.getCancelLabel();
	protected	FixedLengthLabel		messageLabel = new FixedLengthLabel("",15);
	protected	Image			close;
	
	protected	ScrollPanel		scrollPanel_H = new ScrollPanel();
	protected	ScrollPanel		scrollPanel = new ScrollPanel();
	protected	DockPanel buttonPanel = new DockPanel();
	
	protected	Label		captionText;
	protected	HorizontalPanel upperPanel;
	
	public	CommonModalDialog(String title)
	{
		this.title = title;
		
		HorizontalPanel caption = new HorizontalPanel();
		caption.setWidth("100%");
		captionText = new Label(title);
//		captionText.setStyleName("common-table-header");
		caption.add(captionText);
		caption.setCellWidth(captionText, "100%");
		
		close = UIImages.getImageBundle(UIImages.defaultSkin).getCloseDialog();
		close.addClickListener(new ClickListener()
		{
			public void onClick(Widget sender)
			{
				hide();
			}
		});
		caption.add(close);
		
		scrollPanel_H.add(caption);
//		scrollPanel_H.setCellWidth(caption, "100%");
//		scrollPanel_H.add(close);
		
		setCaption(scrollPanel_H);
		caption.addStyleName("draggable-panel-header");
		close.addStyleName("anchor-cursor");
	}
	public boolean onEventPreview(Event event)
	{
		if (DOM.eventGetType(event) == Event.ONKEYPRESS)
		{
			if (event.getKeyCode() == KeyboardListener.KEY_ESCAPE)
			{
				onEscapeKey();
			}
		}
		return	super.onEventPreview(event);
	}
	protected	void	onEscapeKey()
	{
		hide();
	}
	public	void	setCloseListener(ClickListener l)
	{
		closeListener = l;
	}
	public	void	addCloseListener(ClickListener cl)
	{
		closeButton.addClickListener(cl);
	}
	public	void	removeDefaultCloseListener()
	{
		closeButton.removeClickListener(this.defaultCloseListener);
	}
	public	void	drawDialog()
	{
		this.defaultCloseListener = new ClickListener()
		{
			public void onClick(Widget sender)
			{
				hide();
			}
		};
		closeButton = new Button(closeButtonText, this.defaultCloseListener);
		
//		DomUtil.setId(this, "dialog-styled");
		vp = new VerticalPanel();
//		setWidget(rp);
		
		vp.setStyleName("common-dialog-outer-panel");
		
//	    Window.alert("1");
	    
		
		if (this.closeListener != null)
		{
			this.closeButton.addClickListener(this.closeListener);
		}
		closeButton.setStyleName("dm-popup-close-button");
		buttonPanel.add(closeButton,DockPanel.EAST);
		buttonPanel.setSpacing(0);
		HTML filler1 = new HTML("&nbsp;");
		buttonPanel.add(filler1,DockPanel.EAST);
		
//	    Window.alert("2");
	    
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
		
//	    Window.alert("3");
	    
		this.messageLabel.setStyleName("common-text");
		this.messageLabel.addStyleName("dialog-message-label");
		buttonPanel.add(this.messageLabel,DockPanel.WEST);
		buttonPanel.setCellVerticalAlignment(this.messageLabel,VerticalPanel.ALIGN_MIDDLE);
		buttonPanel.setCellWidth(this.messageLabel,"100%");
		
		Widget c = getContent();
		
		if (this.dialogName != null)
		{
//		    Window.alert("4");
		    
			//	Create a width adjustment panel.
			String widthStyle = this.dialogName+"-dialog-width";
			String heightStyle1 = this.dialogName+"-dialog-height-one";
			String heightStyle2 = this.dialogName+"-dialog-height-two";
			String contentWidthStyle = this.dialogName+"-dialog-content";
			
			c.addStyleName(contentWidthStyle);
			upperPanel = new HorizontalPanel();
			
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
			
//		    Window.alert("5");
			vp.add(upperPanel);
			vp.add(lowerPanel);
		}
		else
		{
//		    Window.alert("6");
		    
			vp.add(c);
			vp.setCellWidth(c,"100%");
			
			vp.add(buttonPanel);
			vp.setCellWidth(buttonPanel,"100%");
			vp.setCellHorizontalAlignment(buttonPanel,HorizontalPanel.ALIGN_RIGHT);
//		    Window.alert("7");
		    
		}
//		RoundedPanel rp = new RoundedPanel(vp);
//		rp.setStyleName("common-dialog-rounded-corner-panel");
		
//		Window.alert("8");
		
		scrollPanel.add(vp);
		
		add(scrollPanel);
		
		/*
		FocusWidget fw = getFocusWidget();
		if (fw != null)
		{
			this.getFocusModel().add(fw);
		}
		else
		{
			Window.alert("No focus widget provided");
		}
		*/
		this.setFocusModel(new FocusModel()
			{
					public	HasFocus	getFocusWidget()
					{
						return	getDmFocusModel();
					}
			});
		
		DmGlassPanel dgp = new DmGlassPanel(this);
		dgp.show();
//		show();
	}
	public	void	changeDialogName(String newName)
	{
		//	This method essentially resizes the dialog, by resizing the background
		//	panels. This style needs to be tested in all browsers.
	}
	public	void	setDialogPosition(int left, int top)
	{
		//	Move the popup from its default center of the page position if required.
		//	
	}
	protected	HasFocus	getDmFocusModel()
	{
		return	this;
	}
	protected	abstract Widget	getContent();
	
	protected	abstract Vector getFooterButtons();
	
	protected	FocusWidget	getFocusWidget()
	{
		return	null;
	}
	protected	void	panelOnDisplay()
	{
	}
	public void commandExecError(String message)
	{
	}
	public void commandExecSuccess(String message)
	{
	}
	public void setProgressMessage(String message)
	{
	}
	public int getTabIndex()
	{
		return 0;
	}
	public void setAccessKey(char c)
	{
	}
	public void setFocus(boolean b)
	{
		if (getFocusWidget() != null)
		{
			getFocusWidget().setFocus(b);
		}
	}
	public void setTabIndex(int arg0)
	{
	}
	public void addKeyboardListener(KeyboardListener arg0)
	{
	}
	public void removeKeyboardListener(KeyboardListener arg0)
	{
	}
	public void addFocusListener(FocusListener arg0)
	{
	}
	public void removeFocusListener(FocusListener arg0)
	{
	}
	/**
	 * This method resets the close button text and adds a new listener to the
	 * close button. This is required for speacial cases dialogs which might want
	 * to trigger a specific action on close.
	 */
	public	void	resetCloseButtonText(String text)
	{
		this.closeButton.setText(text);
	}
	public	void	addCloseClickListener(ClickListener clickListener)
	{
		this.closeButton.addClickListener(clickListener);
		this.close.addClickListener(clickListener);
	}
}
