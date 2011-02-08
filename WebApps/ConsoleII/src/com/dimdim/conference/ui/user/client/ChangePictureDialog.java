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

package com.dimdim.conference.ui.user.client;

import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FileUpload;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.CommandURLFactory;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ChangePictureDialog	extends	CommonModalDialog	implements	ClickListener
{
	protected	Button	applyButton;
	protected	Button	saveButton;
	protected	String	photoUrl;
	protected	boolean	defaultPhoto;
	protected	UIRosterEntry	me;
	
	protected	FormPanel	photoUploadForm;
	protected	FileUpload	photoUploadField;
	
	protected	RadioButton		defaultPhotoButton;
	protected	RadioButton		customPhotoButton;
//	protected	Label			uploadLink;
	
	protected	UserRosterManager	userManager;
	protected	CommandURLFactory	commandURLFactory	=	new	CommandURLFactory();
	
	public	ChangePictureDialog(UserRosterManager userManager, UIRosterEntry me)
	{
		super(UIStrings.getChangePictureDialogHeader());
		
		this.me = me;
		this.photoUrl = "";
		this.defaultPhoto = true;
		this.dialogName = "large";
		this.userManager = userManager;
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
		contentPanel.setStyleName("change-photo-content-panel");
		String defaultPhotoUrl = UserGlobals.getUserGlobals().getDefaultPhotoUrl();
		String currentPhotoUrl = this.me.getPhotoUrl();
		
		//Window.alert("inside chage picture");
		this.defaultPhotoButton = new RadioButton("PhotoSelection");
		this.defaultPhotoButton.addClickListener(this);
		this.defaultPhotoButton.setStyleName("change-photo-radio-button");
		this.customPhotoButton = new RadioButton("PhotoSelection");
		this.customPhotoButton.addClickListener(this);
		this.customPhotoButton.setStyleName("change-photo-radio-button");
		
		//	Custom Photo caption
		
		Label comment1 = new Label(UserGlobals.getUserGlobals().getChangePictureComment1());
		comment1.setStyleName("change-photo-picture-caption");
		comment1.addStyleName("common-text");
		contentPanel.add(comment1);
		
		Label comment2 = new Label(UserGlobals.getUserGlobals().getChangePictureComment2());
		comment2.setStyleName("change-photo-picture-caption");
		comment2.addStyleName("common-text");
		contentPanel.add(comment2);
		
		HorizontalPanel customPhotoCaption = new HorizontalPanel();
		customPhotoCaption.setStyleName("change-photo-picture-caption");
		customPhotoCaption.add(this.customPhotoButton);
		Label label2 = new Label(UIStrings.getChangePhotoLabel());
		label2.setStyleName("common-text");
		label2.addStyleName("change-photo-label");
		customPhotoCaption.add(label2);
		
//		HorizontalPanel uploadCaption = new HorizontalPanel();
//		uploadCaption.setStyleName("change-photo-picture-caption");
//		this.uploadLink = new Label("Upload");
//		this.uploadLink.addClickListener(this);
//		this.uploadLink.setStyleName("common-text");
//		this.uploadLink.addStyleName("change-photo-upload-label");
//		this.uploadLink.addStyleName("common-anchor");
//		uploadCaption.add(this.uploadLink);
		
		//	Create a form panel for the photo file upload.
		
		this.photoUploadForm = new FormPanel();
		this.photoUploadForm.setAction(this.commandURLFactory.getPhotoUploadURL());
		this.photoUploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		this.photoUploadForm.setMethod(FormPanel.METHOD_POST);
		
		this.photoUploadField = new FileUpload();
		this.photoUploadField.setName("photo");
		this.photoUploadForm.add(this.photoUploadField);
		this.photoUploadField.setStyleName("photo-file-upload-field");
		
		contentPanel.add(customPhotoCaption);
		contentPanel.add(this.photoUploadForm);
//		contentPanel.add(uploadCaption);
		
		//	Default Photo Caption
		
		HorizontalPanel defaultPhotoCaption = new HorizontalPanel();
		defaultPhotoCaption.setStyleName("change-photo-picture-caption");
		defaultPhotoCaption.add(this.defaultPhotoButton);
		Label label1 = new Label(UIStrings.getUseDefaultPhotoLabel());
		label1.setStyleName("common-text");
		label1.addStyleName("change-photo-label");
		defaultPhotoCaption.add(label1);
		if (UserGlobals.getUserGlobals().isPhotoUrlDefault(currentPhotoUrl))
		{
			this.defaultPhotoButton.setChecked(true);
			this.customPhotoButton.setChecked(false);
		}
		else
		{
			this.defaultPhotoButton.setChecked(false);
			this.customPhotoButton.setChecked(true);
		}
		contentPanel.add(defaultPhotoCaption);
		
		//	Photo Panel
		
		if (currentPhotoUrl == null || currentPhotoUrl.length() == 0)
		{
			currentPhotoUrl = defaultPhotoUrl;
		}
		Image photoImage = new Image(currentPhotoUrl);
		photoImage.setStyleName("change-photo-picture");
		contentPanel.add(photoImage);
		contentPanel.setCellVerticalAlignment(photoImage,VerticalPanel.ALIGN_MIDDLE);
		contentPanel.setCellHorizontalAlignment(photoImage,HorizontalPanel.ALIGN_LEFT);
		
//		this.photoUploadField.setVisible(false);
//		this.uploadLink.setVisible(false);
		
		return	contentPanel;
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		applyButton = new Button();
		applyButton.setText(UIStrings.getOKLabel());
		applyButton.setStyleName("dm-popup-close-button");
		applyButton.addClickListener(this);
		v.addElement(applyButton);
		
//		saveButton = new Button();
//		saveButton.setText("Save");
//		saveButton.setStyleName("dm-popup-close-button");
//		saveButton.addClickListener(this);
//		v.addElement(saveButton);
		
		return	v;
	}
	public	void	onClick(Widget w)
	{
		if (w == this.applyButton)
		{
			if (this.customPhotoButton.isChecked())
			{
				if (this.photoUploadField.getFilename() != null &&
						this.photoUploadField.getFilename().length() > 0)
				{
					this.photoUploadForm.submit();
				}
			}
			else
			{
				this.userManager.setPhotoToDefault();
			}
			hide();
		}
		else if (w == this.defaultPhotoButton)
		{
			this.photoUploadField.setVisible(true);
		}
		else if (w == this.customPhotoButton)
		{
			this.photoUploadField.setVisible(true);
		}
	}
}
