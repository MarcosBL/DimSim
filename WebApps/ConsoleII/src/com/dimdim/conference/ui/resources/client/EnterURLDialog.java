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

package com.dimdim.conference.ui.resources.client;

import java.util.Vector;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.common.client.util.CommonUserInformationDialog;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.CommandExecProgressListener;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EnterURLDialog	extends	CommonModalDialog	implements	ClickListener
{
	
	protected	Button		applyButton;
	protected	TextBox		url = new TextBox();
	

	protected	ResourceSharingController	sharingController;
	
	public	EnterURLDialog(ResourceSharingController sharingController)
	{
		super(ConferenceGlobals.getDisplayString("resource.cobrowse.select.label","Upload URL"));
		this.sharingController = sharingController;
		this.dialogName = "enter-url";
		this.closeButtonText = UIStrings.getCancelLabel();
		super.closeListener = this;
	}
//	public	ChangePictureDialog(UIRosterEntry me, String currentPhotoUrl)
//	{
//		super("Change Picture");
//		
//		this.me = me;
//		this.photoUrl = currentPhotoUrl;
//		this.defaultPhoto = false;
//	}
	protected	Widget	getContent()
	{
		VerticalPanel contentPanel = new VerticalPanel();
//		contentPanel.setStyleName("enter-url-content-panel");
		
		Label comment1 = new Label(UIStrings.getEnterURLLabel());
		comment1.addStyleName("common-text");
		contentPanel.add(comment1);
		
		this.url.setStyleName("common-text");
		this.url.addStyleName("enter-url-field");
		contentPanel.add(this.url);
		url.setFocus(true);
		KeyboardListenerAdapter keyboardListener = new KeyboardListenerAdapter()
		{
			public void onKeyDown(Widget sender, char keyCode, int modifiers)
			{
			}
			public void onKeyUp(Widget sender, char keyCode, int modifiers)
			{
				if( keyCode == KeyboardListener.KEY_ENTER )
				{
					onClick(applyButton);
				}
			}
		};
		url.addKeyboardListener(keyboardListener);
		return	contentPanel;
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		applyButton = new Button();
		
		applyButton.setText(UIStrings.getShareLabel());
		applyButton.setStyleName("dm-popup-close-button");
		applyButton.addClickListener(this);
		v.addElement(applyButton);
		
		return	v;
	}
	public	void	onClick(Widget w)
	{
		if (w == this.applyButton)
		{
			String urlString = this.url.getText();
			if(null != urlString && urlString.length() > 0)
			{
				if(urlString.startsWith("http://"))
				{
					
				}else{
					urlString = "http://"+urlString;
				}
				
				sharingController.addCobRes(urlString, new CobCommandExecListener());
				hide();
			}else{
				Window.alert("Please give a url");
			}
		}
		
	}
	class CobCommandExecListener implements CommandExecProgressListener
	{
		String waitAMoment = ConferenceGlobals.getDisplayString("stopping.desktop.desc","This may take a moment. Please wait.");
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
		
		String errorMessage = ConferenceGlobals.getDisplayString("cob.res.add.error","Synchrolive could not add the url for sharing. This is a temporary problem. Can you please retry again.");
		String errorTitle = ConferenceGlobals.getDisplayString("error.label","Error");
		String progressTitle = ConferenceGlobals.getDisplayString("info.label","Info");
		public void commandExecError(String message) {
			//Window.alert("commandExecError message = "+message);
			if (cuid == null)
			{
				cuid = CommonUserInformationDialog.getCommonUserInformationDialog(errorTitle, errorMessage);
		    	cuid.drawDialog();
			}
			else
			{
				cuid.setMessage(errorTitle, errorMessage);
			}
	    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
			cuid.hideOKButton();
			cuid.hideCancelButton();			
			
		}
		public void commandExecSuccess(String message) {
			//Window.alert("commandExecSuccess message = "+message);
			cuid.hide();
			
		}
		public void setProgressMessage(String message) {
			//Window.alert("setProgressMessage message = "+message);
			if (cuid == null)
			{
				cuid = CommonUserInformationDialog.getCommonUserInformationDialog(progressTitle, message+" "+waitAMoment);
		    	cuid.drawDialog();
			}
			else
			{
				cuid.setMessage(progressTitle, message+" "+waitAMoment);
			}
	    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
			cuid.hideOKButton();
			cuid.hideCancelButton();
			
		}
	}
}
