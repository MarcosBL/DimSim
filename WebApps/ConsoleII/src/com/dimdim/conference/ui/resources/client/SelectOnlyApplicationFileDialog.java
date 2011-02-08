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
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.CommandURLFactory;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SelectOnlyApplicationFileDialog	//extends	CommonModalDialog	implements	ClickListener
{
/*
	protected	Button		applyButton;
	protected	String		fileName = "";
	
	protected	FormPanel		selectFileForm;
	protected	FileUpload		existingFileField;
	
	protected	HTML			errorMessage = new HTML(" ");
	
	protected	CommandURLFactory	commandURLFactory	=	new	CommandURLFactory();
	
	protected	String	applicationName;
	protected	String	applicationType;
	protected	String	DOCType;
	protected	String	docType;
	
	protected	ResourceSharingController	sharingController;
	
	public	SelectOnlyApplicationFileDialog(String applicationName, String applicationType,
				ResourceSharingController sharingController, String DOCType, String docType)
	{
		super(UIStrings.getSelectLabel()+ConferenceGlobals.getDisplayString("console.doctype.label."+docType,DOCType));
		
		this.applicationName = applicationName;
		this.applicationType = applicationType;
		this.sharingController = sharingController;
		this.DOCType = DOCType;
		this.docType = docType;
		this.dialogName = "large";
		this.closeButtonText = UIStrings.getCancelLabel();
		super.closeListener = this;
	}
	protected	Widget	getContent()
	{
		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.setStyleName("powerpoint-info-text");
		
		Label comment1 = new Label(ResourceGlobals.getResourceGlobals().
				getSelectFileComment1().replaceAll("file",this.docType));
		comment1.setStyleName("change-photo-picture-caption");
		comment1.addStyleName("common-text");
		comment1.addStyleName("common-4px-top-bottom-spacing");
		comment1.setWordWrap(true);
//		comment1.setWidth("200px");
//		comment1.setWidth("250px");
		
		contentPanel.add(comment1);
		contentPanel.add(new HTML(" "));
		
		this.selectFileForm = new FormPanel();
		this.selectFileForm.setAction(this.commandURLFactory.getPhotoUploadURL());
		this.selectFileForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		this.selectFileForm.setMethod(FormPanel.METHOD_POST);
		
		this.existingFileField = new FileUpload();
		this.existingFileField.setName("photo");
		this.selectFileForm.add(this.existingFileField);
		this.existingFileField.setStyleName("photo-file-upload-field");
		
		contentPanel.add(this.selectFileForm);
		
		contentPanel.add(new HTML(" "));
		this.errorMessage.setStyleName("common-text");
		contentPanel.add(this.errorMessage);
		
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
		this.errorMessage.setText(" ");
		if (w == this.applyButton)
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
							String message = UIStrings.getSupportedFileTypesComment()+this.applicationType;
							String messageHeader = message;
							if (messageHeader.length() > 30)
							{
								messageHeader = messageHeader.substring(0,30);
							}
							DefaultCommonDialog.showMessage(messageHeader,message);
//							this.errorMessage.setText(UIStrings.getSupportedFileTypesComment()+this.applicationType);
						}
					}
					if (startShare)
					{
						this.sharingController.continueAppControlShare(this.applicationName,"\""+this.fileName+"\"");
						hide();
					}
				}
				else
				{
					this.errorMessage.setText(UIStrings.getFileNameRequiredMessage());
				}
		}
		else if (w == this.closeButton)
		{
			//	Tell the share controller that the selection was cancelled.
			this.sharingController.cancelAppControlShare();
		}
	}
*/
}
