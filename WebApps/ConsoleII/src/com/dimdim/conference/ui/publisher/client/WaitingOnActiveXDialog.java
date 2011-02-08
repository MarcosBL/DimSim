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

package com.dimdim.conference.ui.publisher.client;

import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ResourceModel;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 */
public class WaitingOnActiveXDialog //extends PopupPanel
{
/*
	  private static final String LOGO_IMAGE = "images/dimdim_vertical2.png";
	  private static final String IN_PROGRESS_MESSAGE_1 = "";
	  private static final String IN_PROGRESS_MESSAGE_2 = " in progress";
	  private static final String WAITING_MESSAGE_1 = "Waiting for ";
	  private static final String WAITING_MESSAGE_2 = " to complete.";
	  
	private WaitingOnActiveXDialog(String message)
	{
		// Use this opportunity to set the dialog's caption.

		// Create a DockPanel to contain the 'about' label and the 'OK' button.
		DockPanel outer = new DockPanel();
//    outer.setSpacing(4);, WaitAndContinueDialogUserListener listener

		String captionText = IN_PROGRESS_MESSAGE_1+message+IN_PROGRESS_MESSAGE_2;
		HorizontalPanel captionPanel = new HorizontalPanel();
		captionPanel.setStyleName("dimdimPopupBoxCaption");
    
		Label captionLabel = new Label(captionText);
		captionLabel.setStyleName("dimdimPopupBoxCaptionLabel");
		captionPanel.add(captionLabel);
		outer.add(captionPanel,DockPanel.NORTH);
	
		// Create the 'about' label. Placing it in the 'rest' position within the
		// dock causes it to take up any remaining space after the 'OK' button
		// has been laid out.
	
		HTML text = new HTML(WAITING_MESSAGE_1+message+WAITING_MESSAGE_2);
		text.setStyleName("dimdimPopupBoxTableText");
		HorizontalPanel tmpPanel = new HorizontalPanel();
	
		tmpPanel.setStyleName("dimdimPopupBoxContainerPanel");
		tmpPanel.add(text);
	
		outer.add(tmpPanel, DockPanel.NORTH);
//    outer.setStyleName("dimdimPopupBoxContainerPanel");

    // Add a bit of spacing and margin to the dock to keep the components from
    // being placed too closely together.
//    outer.setSpacing(8);
		setStyleName("dimdimPopupBox");

		add(outer);
	}
  /*
	public  void	waitAndCotinueCreateAppShare(final WaitAndContinueUserListener listener,
			final Element elem, final WaitAndContinueData listenerData)
	{
		Timer t = new Timer()
		{
			public	void	run()
			{
				int popupState = 0;
				if (elem == null)
				{
					popupState = getPopupFinishState();
				}
				else
				{
					popupState = getPopupFinishStateFirefox(elem);
				}
				if (popupState != 0)
				{
					//	Popup has finished.
					this.cancel();
//					Window.alert("Popup finished with status:"+popupState);
					if (popupState == 1)
					{
						int appHandle = 0;
						if (elem == null)
						{
							appHandle = getAppHandleToShare();
						}
						else
						{
							appHandle = getAppHandleToShareFirefox(elem);
						}
						if (appHandle > 0 && listener != null)
						{
							listenerData.setInt1(appHandle);
							listener.continueWork(listenerData);
//							rdh.continueAppShareCreation(appHandle,streamId,resourceId,resourceName,isImport);
						}
					}
					hide();
				}
			}
		};
		t.scheduleRepeating(500);
	}
  
	public  void	waitAndCotinuePPTUpload(final WaitAndContinueUserListener listener,
			final Element elem, final WaitAndContinueData listenerData)
	{
		Timer t = new Timer()
		{
			public	void	run()
			{
				int popupState = 0;
				if (elem == null)
				{
					popupState = getPopupFinishState();
				}
				else
				{
					popupState = getPopupFinishStateFirefox(elem);
				}
				if (popupState != 0)
				{
					//	Popup has finished.
					this.cancel();
//					Window.alert("Popup finished with status:"+popupState);
					if (popupState == 1)
					{
						listener.continueWork(listenerData);
//						rdh.continueCreateNewPresentation(presentationId,resourceId,resourceName,isImport);
					}
					hide();
				}
			}
		};
		t.scheduleRepeating(500);
	}
	*--/
	public  void	waitForScreenShareCompletion(final WaitAndContinueUserListener listener,
			final Element elem, final WaitAndContinueData listenerData)
	{
		Timer t = new Timer()
		{
			public	void	run()
			{
				int popupState = 0;
				if (elem == null)
				{
					popupState = getPopupFinishState();
				}
				else
				{
					popupState = getPopupFinishStateFirefox(elem);
				}
				if (popupState != 0)
				{
					//	Popup has finished.
					this.cancel();
					ResourceModel rm = ClientModel.getClientModel().getResourceModel();
					rm.setCurrentResourceUnselected();
					hide();
				}
			}
		};
		t.scheduleRepeating(500);
	}
  
	private native int getPopupFinishState()/*-{
		return $wnd.dimdimPublisherControl1.DimdimHasFinished();
	}-*--/;
	private native int getAppHandleToShare()/*-{
		return $wnd.dimdimPublisherControl1.DimdimGetResult;
	}-*--/;
	private native int getPopupFinishStateFirefox(Element elem) /*-{
		return elem.DimdimHasFinished();
     }-*--/;
	private native int getAppHandleToShareFirefox(Element elem)/*-{
		return elem.DimdimGetResult();
	}-*--/;
	*/
}
