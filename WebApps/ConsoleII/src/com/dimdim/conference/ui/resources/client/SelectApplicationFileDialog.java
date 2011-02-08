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

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.CommandURLFactory;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SelectApplicationFileDialog	//extends	CommonModalDialog	implements	ClickListener
{
	/*
	protected	Button		applyButton;
	protected	String		fileName = "";
	
	protected	FormPanel		selectFileForm;
	protected	FileUpload		existingFileField;
	
	protected	RadioButton		newFileButton;
	protected	RadioButton		existingFileButton;
	protected	HTML			errorMessage = new HTML(" ");
	
	protected	CommandURLFactory	commandURLFactory	=	new	CommandURLFactory();
	
	protected	String	applicationName;
	protected	String	applicationType;
	protected	String	DOCType;
	protected	String	docType;
	protected	boolean	defaultNew;
	
	protected	ResourceSharingController	sharingController;
	
	public	SelectApplicationFileDialog(String applicationName, String applicationType,
				ResourceSharingController sharingController, String DOCType, String docType,
				boolean defaultNew)
	{
		super(UIStrings.getSelectLabel()+DOCType);
		
		this.defaultNew = defaultNew;
		this.applicationName = applicationName;
		this.applicationType = applicationType;
		this.sharingController = sharingController;
		this.DOCType = DOCType;
		this.docType = docType;
		this.dialogName = "change-picture";
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
		contentPanel.setStyleName("change-photo-content-panel");
		
		this.newFileButton = new RadioButton("FileSelection");
		this.newFileButton.addClickListener(this);
		this.newFileButton.setStyleName("change-photo-radio-button");
//		this.newFileButton.setChecked(true);
		this.existingFileButton = new RadioButton("FileSelection");
		this.existingFileButton.addClickListener(this);
		this.existingFileButton.setStyleName("change-photo-radio-button");
		
		Label comment1 = new Label(ResourceGlobals.getResourceGlobals().
				getSelectFileComment1().replaceAll("file",this.docType));
		comment1.setStyleName("change-photo-picture-caption");
		comment1.addStyleName("common-text");
		contentPanel.add(comment1);
		
		Label comment2 = new Label(ResourceGlobals.getResourceGlobals().
				getSelectFileComment2().replaceAll("file",this.docType));
		comment2.setStyleName("change-photo-picture-caption");
		comment2.addStyleName("common-text");
		contentPanel.add(comment2);
		
		HorizontalPanel customPhotoCaption = new HorizontalPanel();
		customPhotoCaption.setStyleName("change-photo-picture-caption");
		customPhotoCaption.add(this.existingFileButton);
		Label label2 = new Label(UIStrings.getUseExistingLabel()+this.DOCType);
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
		
		this.selectFileForm = new FormPanel();
		this.selectFileForm.setAction(this.commandURLFactory.getPhotoUploadURL());
		this.selectFileForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		this.selectFileForm.setMethod(FormPanel.METHOD_POST);
		
		this.existingFileField = new FileUpload();
		this.existingFileField.setName("photo");
		this.selectFileForm.add(this.existingFileField);
		this.existingFileField.setStyleName("photo-file-upload-field");
		
		contentPanel.add(customPhotoCaption);
		contentPanel.add(this.selectFileForm);
//		contentPanel.add(uploadCaption);
		
		//	Default Photo Caption
		
		HorizontalPanel defaultPhotoCaption = new HorizontalPanel();
		defaultPhotoCaption.setStyleName("change-photo-picture-caption");
		defaultPhotoCaption.add(this.newFileButton);
		Label label1 = new Label(UIStrings.getCreateNewLabel()+this.DOCType);
		label1.setStyleName("common-text");
		label1.addStyleName("change-photo-label");
		defaultPhotoCaption.add(label1);
		contentPanel.add(defaultPhotoCaption);
		
		contentPanel.add(new HTML(" "));
		this.errorMessage.setStyleName("common-error-message");
		contentPanel.add(this.errorMessage);
		
		if (this.defaultNew)
		{
			this.newFileButton.setChecked(true);
			this.applyButton.setEnabled(true);
		}
		else
		{
			this.existingFileButton.setChecked(true);
			this.applyButton.setEnabled(true);
		}
		
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
		
//		saveButton = new Button();
//		saveButton.setText("Save");
//		saveButton.setStyleName("dm-popup-close-button");
//		saveButton.addClickListener(this);
//		v.addElement(saveButton);
		
		return	v;
	}
	public	void	onClick(Widget w)
	{
		this.errorMessage.setText(" ");
		if (w == this.applyButton)
		{
			if (this.existingFileButton.isChecked())
			{
				this.fileName = this.existingFileField.getFilename();
				
				this.fileName = this.fileName.trim();
				
				if (this.fileName != null && this.fileName.length() > 0)
				{
					//	Continue with the application share with existing file
					boolean	startShare = true;
					if (this.applicationType.length() > 0)
					{
						startShare = false;
						int dot = fileName.lastIndexOf(".");
						if (dot > 0)
						{
							String type = fileName.substring(dot+1);
							if (this.applicationType.indexOf(type) != -1)
							{
								startShare = true;
							}
						}
						if (!startShare)
						{
							this.errorMessage.setText(UIStrings.getSupportedFileTypesComment()+this.applicationType);
						}
					}
					if (startShare)
					{
						hide();
					    DeferredCommand.add(new Command() {
					        public void execute() {
								sharingController.continueAppControlShare(applicationName,"\""+fileName+"\"");
					        }
					      });
					}
				}
				else
				{
					this.errorMessage.setText(UIStrings.getFileNameRequiredMessage());
				}
			}
			else
			{
				//	Continue with the application share with a new file
				hide();
			    DeferredCommand.add(new Command() {
			        public void execute() {
						sharingController.continueAppControlShare(applicationName,"");
			        }
			      });
			}
		}
		else if (w == this.closeButton)
		{
			//	Tell the share controller that the selection was cancelled.
			this.sharingController.cancelAppControlShare();
		}
		else if (w == this.newFileButton)
		{
			if (this.newFileButton.isChecked())
			{
				this.applyButton.setEnabled(true);
			}
		}
		else if (w == this.existingFileButton)
		{
			this.checkText(this.existingFileField);
		}
	}
	public void onKeyDown(Widget arg0, char arg1, int arg2)
	{
		this.checkText(arg0);
	}
	public void onKeyPress(Widget arg0, char arg1, int arg2)
	{
		this.checkText(arg0);
	}
	public void onKeyUp(Widget arg0, char arg1, int arg2)
	{
		this.checkText(arg0);
	}
	public void checkText(Widget arg0)
	{
		if (arg0 == this.existingFileField)
		{
			if (this.existingFileButton.isChecked())
			{
				this.applyButton.setEnabled(true);
				this.fileName = this.existingFileField.getFilename();
				if (fileName != null && fileName.length() >0)
				{
					if (this.applicationType.length() > 0)
					{
						//	Supported types are specified, check the file type against them.
						int dot = fileName.lastIndexOf(".");
						if (dot > 0)
						{
							String type = fileName.substring(dot+1);
							if (this.applicationType.indexOf(type) != -1)
							{
								this.applyButton.setEnabled(true);
							}
							else
							{
								this.errorMessage.setText(UIStrings.getSupportedFileTypesComment()+this.applicationType);
							}
						}
					}
					else
					{
						this.applyButton.setEnabled(true);
					}
				}
				else
				{
					//	Existing file is checked but not specified.
					this.applyButton.setEnabled(true);
				}
			}
		}
	}
	*/
}
