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

import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.JSONurlReader;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.CommandURLFactory;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.dms.ConversionProgressCheckResponse;
import com.dimdim.conference.ui.model.client.dms.DMSjsonResponseHandler;
import com.dimdim.conference.ui.model.client.dms.DmFlashFileUploadListener;
import com.dimdim.conference.ui.model.client.dms.DmFlashFileUploader;
import com.dimdim.conference.ui.model.client.dms.PPTConversionCanceller;
import com.dimdim.conference.ui.model.client.dms.PPTConversionProgressChecker;
import com.dimdim.conference.ui.model.client.dms.PPTConversionProgressListener;
import com.dimdim.conference.ui.model.client.dms.PPTConversionStartListener;
import com.dimdim.conference.ui.model.client.dms.PPTConversionStarter;
import com.dimdim.conference.ui.model.client.dms.PPTIDGenerationListener;
import com.dimdim.conference.ui.model.client.dms.PPTIDGenerator;
import com.dimdim.conference.ui.model.client.dms.StartConversionResponse;
import com.dimdim.conference.ui.model.client.helper.FlashXmlCallInterface;
import com.dimdim.conference.ui.model.client.helper.ProgressCheckResponse;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This overall full page panel will simply provide a common banner, lhs
 * and rhs sections page. The lhs will contain all the available tests and
 * results, progress or any associated components for the tests will be
 * displayed in the rhs.
 */

public class DocUploadPanel	extends	Composite implements PPTConversionStartListener,
	ClickListener, PPTConversionProgressListener, PPTIDGenerationListener,
	DmFlashFileUploadListener, FormHandler
{
	private	static	final	int	WAITING_FOR_FILE_SELECT	=	0;
	private	static	final	int	WAITING_FOR_PPT_ID_GENERATION	=	1;
	private	static	final	int	WAITING_FOR_FILE_UPLOAD	=	2;
	private	static	final	int	WAITING_FOR_CONVERSION	=	3;
//	private	static	final	int	WAITING_FOR_PREPARATION	=	4;
	
	protected	int	state = WAITING_FOR_FILE_SELECT;
	
	protected	String	fileType = "ppt";
	protected	String	fileTypeDescription = "Supported Files are .ppt, .pdf, .pptx";
	
	protected	VerticalPanel	basePanel	=	new	VerticalPanel();
	
	protected	String	dmsUrl;
	protected	String	meetingId = "global-meeting";
	protected	String	pptType = "regular";
	protected	String	pptId = "";
	
	protected	DmFlashFileUploader			dmFlashFileUploader;
	protected	PPTConversionProgressChecker	progressChecker;
	protected	PPTConversionStarter		conversionStarter;
	protected	PPTIDGenerator			pptIDGenerator;
	protected	DMSjsonResponseHandler	dmsJsonResponseHandler = new DMSjsonResponseHandler();
	protected	ResourceSharingController	sharingController;
	protected	SelectFileDialogue	dialog;
	
//	protected	TextBox		selectedFile;
//	protected	Button		browseButton;
//	protected	Label		uploadButton;
	
	protected	Label		placeholderLabel1 = new Label(" ");
	protected	Label		placeholderLabel2 = new Label(" ");
	
	protected	Image		waiting;
	protected	HorizontalPanel		progressMessagePanel = new HorizontalPanel();
//	protected	HorizontalPanel		progressBarPanel = new HorizontalPanel();
	protected	HorizontalPanel		progressCommonWaitMessagePanel = new HorizontalPanel();
	protected	HorizontalPanel		progressCommonWaitMessagePanel2 = new HorizontalPanel();
	protected	Label		progressMessageLabel;
	protected	Label		progressMessage;
	protected	Label		progressCommonWaitMessage;
	protected	Label		progressCommonWaitMessage2;
//	protected	ProgressBar	progressBar;
	protected	Timer		activityTimer;
	
//	protected	HorizontalPanel	uploadProgressMessagePanel = new HorizontalPanel();
//	protected	Label		uploadProgressMessageLabel;
//	protected	Label		uploadProgressMessage;
//	protected	ProgressBar	uploadProgressBar;
	
//	protected	HorizontalPanel	conversionProgressMessagePanel = new HorizontalPanel();
//	protected	Label		conversionProgressMessageLabel;
//	protected	Label		conversionProgressMessage;
//	protected	ProgressBar	conversionProgressBar;
	
	protected	String		selectedFileName = "";
	protected	int			selectedFileSize = -1;
	protected	int			totalSlides = -1;
	protected	int			slidesConverted = 0;
	protected	int			slideWidth = -1;
	protected	int			slideHeight = -1;
	protected	String		docType = "ppt";
	protected	CommandURLFactory	commandURLFactory	=	new	CommandURLFactory();
	private boolean noErrorOccured = true;
	
	String []allowedFileTypes = {".ppt", ".pdf", ".pptx"};
	String []allowedFileTypesExtentions = {"ppt", "pdf", "pptx"};
	
	public DocUploadPanel(String dmsUrl, String meetingId,
			SelectFileDialogue dialogue, ResourceSharingController sharingController
			)
	{
		initWidget(this.basePanel);
		
		this.dmsUrl = dmsUrl;
		this.meetingId = meetingId;
		this.sharingController = sharingController;
		this.dialog = dialogue;
		//this.docType = docType;
		//this.fileType = fileType;
		//this.fileTypeDescription = fileTypeDescription;
		dmFlashFileUploader = new DmFlashFileUploader(this);
		
//		selectedFile = this.addFileBrowseField("", "selectedFile", "", false);		
		
		//uploadButton = new Label("Start Upload");
		//this.basePanel.add(uploadButton);
		//uploadButton.addClickListener(this);
		
//		this.basePanel.add(this.placeholderLabel1);
		
		this.basePanel.add(new HTML(" "));
		this.basePanel.add(new HTML(" "));		
		
		progressCommonWaitMessage = new Label("");
		progressCommonWaitMessage.setStyleName("console-label");
		progressCommonWaitMessage.addStyleName("common-text");
		progressCommonWaitMessage.setWordWrap(true);
//		progressCommonWaitMessage.setWidth("250px");
		
		progressCommonWaitMessagePanel.add(progressCommonWaitMessage);
		progressCommonWaitMessagePanel.setCellHeight(progressCommonWaitMessage, "40%");		
		basePanel.add(progressCommonWaitMessagePanel);
		basePanel.setCellWidth(progressCommonWaitMessagePanel, "50%");
		
		progressCommonWaitMessage2 = new Label("");
		progressCommonWaitMessage2.setStyleName("console-label");
		progressCommonWaitMessage2.addStyleName("common-text");
		progressCommonWaitMessage2.setWordWrap(true);
//		progressCommonWaitMessage2.setWidth("250px");
		progressCommonWaitMessagePanel2.add(progressCommonWaitMessage2);
		progressCommonWaitMessagePanel2.setCellHeight(progressCommonWaitMessage2, "40%");		
		basePanel.add(progressCommonWaitMessagePanel2);
//		basePanel.setCellWidth(progressCommonWaitMessagePanel2, "50%");
		
		progressMessageLabel = new Label("   ");
		progressMessageLabel.setWordWrap(false);
		progressMessageLabel.setStyleName("console-label");
		progressMessageLabel.addStyleName("common-text");
		progressMessageLabel.addStyleName("common-4px-spacing");
		progressMessage = new Label("");
		progressMessage.setStyleName("console-label");	
		progressMessage.addStyleName("common-text");
		progressMessage.addStyleName("common-4px-spacing");
		
		this.basePanel.add(new HTML(" "));
		
		waiting = new Image("images/waiting.gif");
		progressMessagePanel.add(waiting);
		progressMessagePanel.setCellHorizontalAlignment(waiting, HorizontalPanel.ALIGN_LEFT);
		progressMessagePanel.setCellVerticalAlignment(waiting, VerticalPanel.ALIGN_MIDDLE);
		waiting.setVisible(false);
		
		progressMessagePanel.add(progressMessageLabel);
		progressMessagePanel.setCellWidth(progressMessageLabel, "60%");
		progressMessagePanel.setCellHorizontalAlignment(progressMessageLabel, HorizontalPanel.ALIGN_LEFT);
		progressMessagePanel.setCellVerticalAlignment(progressMessageLabel, VerticalPanel.ALIGN_MIDDLE);
		
		Label ll = new Label(" ");
		progressMessagePanel.add(ll);
		progressMessagePanel.setCellHorizontalAlignment(ll, HorizontalPanel.ALIGN_LEFT);
		
		progressMessagePanel.add(progressMessage);
		progressMessagePanel.setCellHorizontalAlignment(progressMessage, HorizontalPanel.ALIGN_LEFT);
		progressMessagePanel.setCellVerticalAlignment(progressMessage, VerticalPanel.ALIGN_MIDDLE);
		progressMessagePanel.setCellWidth(progressMessage, "30%");
		basePanel.add(progressMessagePanel);
		basePanel.setCellWidth(progressMessagePanel, "100%");
		
		
//		progressBar = new ProgressBar(20);
//		progressBar.addStyleName("progressbar-noborder");
//		progressBar.setWidth("160px");
//		progressBar.setHeight("16px");
//		progressBarPanel.add(progressBar);
//		progressBarPanel.setCellHorizontalAlignment(progressBar, HorizontalPanel.ALIGN_LEFT);
//		progressBarPanel.add(this.placeholderLabel2);
//		basePanel.add(progressBarPanel);
//		basePanel.setCellWidth(progressBarPanel, "40%");
		
		this.pptIDGenerator = new PPTIDGenerator(this.dmsUrl+
				"/generateDocID2?meetingID="+this.meetingId,this);
				//"/generateDocID",this);
		
		ClientModel.getClientModel().getRosterModel().extendSessionTimeout();
//		dialog.applyButton.setVisible(false);
	}
	/**
	 * These events will be fired by the upload form. 
	 */
	public void onSubmit(FormSubmitEvent event)
	{
//		Window.alert("Submitting upload form: "+event);
		DebugPanel.getDebugPanel().addDebugMessage("Posting file upload form:"+event.toString());
		state = WAITING_FOR_FILE_UPLOAD;
	}
	public void onSubmitComplete(FormSubmitCompleteEvent event)
	{
		//	File submition is complete. Start the conversion process.
//		Window.alert("Upload form submit return: "+event);
		String s = "null event";
		if (event != null)
		{
			s = event.toString()+", response text:"+event.getResults();
		}
		DebugPanel.getDebugPanel().addDebugMessage("Upload form post return: "+s);
		if (this.activityTimer != null)
		{
			this.activityTimer.cancel();
			this.activityTimer = null;
		}
		DebugPanel.getDebugPanel().addDebugMessage("Kicking off conversion");
		fileUploadCompleted(this.selectedFileName);
	}
	/**
	 * Following methods are used to analyze the callbacks from the flash
	 * movie and give appropriate information to the user.
	 */
	public	void	fileSelected(String fileName, int fileSize)
	{
	    	//Window.alert("inside fileSelected just before calling generateNewPPTID");
		this.selectedFileName = fileName;
//		this.selectedFile.setText(fileName);
		this.selectedFileSize = fileSize;
		
		this.pptIDGenerator.generateNewPPTID();
	}
	public	void	uploadProgress(String fileName, int bytesLoaded)
	{
		progressCommonWaitMessage.setText(ConferenceGlobals.getDisplayString("console.ppt.upload.wait", "Depending on size of document," +
		" it might take a few minutes..."));
//		progressCommonWaitMessage.setWordWrap(true);
		
		progressMessageLabel.setText(ConferenceGlobals.getDisplayString("console.ppt.upload.progress","Uploading presentation:"));
		int uploadProgress = (100*bytesLoaded)/this.selectedFileSize ;
		this.progressMessage.setText(uploadProgress+" %");
//		this.setProgress(uploadProgress);
	}
	private	void	showUploadProgress(String fileName, int seconds)
	{
		progressCommonWaitMessage.setText(ConferenceGlobals.getDisplayString("console.ppt.upload.wait", "Depending on size of document," +
		" it might take a few minutes..."));
//		progressCommonWaitMessage.setWordWrap(true);
		int min = seconds / 60;
		int sec = seconds % 60;
		progressMessageLabel.setText(ConferenceGlobals.getDisplayString("console.ppt.upload.progress","Uploading presentation:"));
		this.progressMessage.setText(min+":"+sec);
//		this.setProgress(uploadProgress);
	}
	public	void	fileUploadCompleted(String fileName)
	{
		if (this.fileType.equalsIgnoreCase("pdf"))
		{
			AnalyticsConstants.reportPDFUpload();
		}
		else
		{
			AnalyticsConstants.reportPPTUpload();
		}
//		this.uploadProgressMessage.setText("Upload Progress: complete");
		//Window.alert("file upload completed...fileName = "+fileName);
		//	Start the slide conversion process.
		DebugPanel.getDebugPanel().addDebugMessage("File upload completed, starting conversion.");
		String conversionUrl = dmsUrl+"/startDocumentConversion2?docID="+this.pptId+
			"&docName="+getEncodedFileName()+"&meetingID="+this.meetingId+
			"&roomID=_default&sessionID="+ConferenceGlobals.conferenceId+
			"&docType="+docType+"&uploadType=regular";
		conversionStarter = new PPTConversionStarter(conversionUrl,this);
		state = WAITING_FOR_CONVERSION;
		//Window.alert("upload completed, calling start conversion fileName = "+fileName);
		conversionStarter.startConversion();
		
		progressCommonWaitMessage.setText(ConferenceGlobals.getDisplayString("console.ppt.upload.wait", "Depending on size of document," +
				" it might take a few minutes..."));
//		progressCommonWaitMessage.setWordWrap(true);
//		this.conversionProgressMessage.setText("Conversion Progress: Started");
		this.progressMessageLabel.setText(ConferenceGlobals.getDisplayString("console.ppt.conversion.progress", "Converting presentation")+":");
//		this.progressMessage.setText("");
		this.progressMessage.setText(ConferenceGlobals.getDisplayString("console.ppt.conversion.wait","Please Wait..."));
//		this.setProgress(0);
	}
	public	void	uploadCancelled()
	{
//		this.callClearQueue();
		state = WAITING_FOR_FILE_SELECT;
		dialog.getShareButton().setEnabled(true);
		if (this.activityTimer != null)
		{
			this.activityTimer.cancel();
			this.activityTimer = null;
		}
		FlashXmlCallInterface.getInterface().stopResponseChecker();
		if (this.progressChecker != null)
		{
			this.progressChecker.stopCheck();
		}
		if (this.pptId != null && this.pptId.length() > 0 && noErrorOccured )
		{
			PPTConversionCanceller canceller = new PPTConversionCanceller(dmsUrl,this.pptId,this.meetingId,this.docType);
			canceller.cancelConversion();
			this.pptId = null;
		}
//		this.browseButton.setEnabled(true);
	}
	/*
	private	TextBox	addFileBrowseField(String label, String name, String value, boolean visible)
	{
		HorizontalPanel fieldPanel = new HorizontalPanel();
		
		TextBox field = new TextBox();
		field.setName(name);
		field.setText(value);
		field.addStyleName("ppt-upload-filename-textbox");
		
//		Label fieldLabel = new Label(label);
//		fieldLabel.setStyleName("form-field-label");
//		fieldPanel.add(fieldLabel);
//		fieldPanel.setCellHorizontalAlignment(fieldLabel, HorizontalPanel.ALIGN_RIGHT);
//		fieldPanel.setCellVerticalAlignment(fieldLabel, VerticalPanel.ALIGN_MIDDLE);
		fieldPanel.add(field);
		fieldPanel.setCellHorizontalAlignment(field, HorizontalPanel.ALIGN_LEFT);
		fieldPanel.setCellVerticalAlignment(field, VerticalPanel.ALIGN_MIDDLE);
		//Window.alert("adding a browse button...");
		
		fieldPanel.add(new Label(" "));
		
//		browseButton = new Button(ConferenceGlobals.getDisplayString("console.browse.button","Browse"));
//		fieldPanel.add(browseButton);
//		fieldPanel.setCellHorizontalAlignment(browseButton, HorizontalPanel.ALIGN_RIGHT);
//		fieldPanel.setCellVerticalAlignment(browseButton, VerticalPanel.ALIGN_MIDDLE);
//		browseButton.addClickListener(this);
		
		fieldPanel.setStyleName("form-field");
		basePanel.add(fieldPanel);
		basePanel.setCellWidth(fieldPanel, "40%");
		basePanel.setCellVerticalAlignment(fieldPanel, VerticalPanel.ALIGN_MIDDLE);
//		fieldPanel.setVisible(visible);
//		fieldPanel.setWidth("100%");
		//fieldPanel.setBorderWidth(1);
		
		
		return	field;
	}
	*/
	public void onClick(Widget sender)
	{
//		if (sender == this.browseButton)
//		{
//			clearMessage();
//			if (this.isSWFUploadReady())
//			{
//				this.browseButton.setEnabled(false);
//				callResetFileTypesAndDescription("*."+dialog.applicationType,"(..*."+dialog.applicationType+")");
//				callClearQueue();
//				callBrowse();
//			}
//			else
//			{
//				this.showErrorMessage(ConferenceGlobals.getDisplayString("console.ppt.error1","File upload component is not available"));
//			}
//		}
		if (sender == dialog.getShareButton() && state == WAITING_FOR_FILE_SELECT)
		{
			clearMessage();
			noErrorOccured = true;
			String s = this.dialog.existingFileField.getFilename();
//			Window.alert("insdie click of share button... Selected file:"+s);
			if (s != null && s.trim().length() > 0)
			{
				selectedFileName = s;
				int i = selectedFileName.lastIndexOf("\\");
				if (i > 0)
				{
					selectedFileName = selectedFileName.substring(i+1);
				}
//				if (selectedFileName.toLowerCase().endsWith(this.fileType))
				if (this.isFileRightType(this.selectedFileName))
				{
					dialog.getShareButton().setEnabled(false);
					//setting the text of button to cancel
					dialog.changeButtonToCancel();
					i = selectedFileName.lastIndexOf("/");
					if (i > 0)
					{
						selectedFileName = selectedFileName.substring(i+1);
					}
					//	In javascript we dont have the file size. This is to
					//	fake the upload progress.
					this.selectedFileSize = 60;
					DebugPanel.getDebugPanel().addDebugMessage("Calling ppt id generation");
					state = WAITING_FOR_PPT_ID_GENERATION;
					waiting.setVisible(true);
					this.pptIDGenerator.generateNewPPTID();
				}
				else
				{					
					this.showErrorMessage(this.fileTypeDescription+".");				
				}
			}
			else
			{				
				this.showErrorMessage(this.fileTypeDescription+".");
			}
		}
	}
//	private	native	void	callBrowse()/*-{
//		$wnd.selectFile();
//	}-*/;
//	private	native	void	callUpload()/*-{
//		$wnd.startUpload();
//	}-*/;
//	private	native	void	callClearQueue()/*-{
//		$wnd.clearQueue();
//	}-*/;
//	private	native	void	callResetUrl(String newUrl)/*-{
//		$wnd.changeUrl(newUrl);
//	}-*/;
//	private	native	boolean	isSWFUploadReady()/*-{
//		return	$wnd.isSWFUploadReady();
//	}-*/;
//	private	native	void	callResetFileTypesAndDescription(String fileTypes, String fileDescription)/*-{
//		$wnd.changeFileTypesAndDescription(fileTypes,fileDescription);
//	}-*/;
	
	/**
	 * ppt conversion start listener.
	 */
	public void conversionStarted(String responseText)
	{
		//	Kick off the pogress tracker.
//	    	Window.alert("inside conversionStarted just before calling doc status responseText = "+responseText);
		StartConversionResponse scr = dmsJsonResponseHandler.readStartConversionResponse(responseText);
		if (scr != null && scr.getResult())
		{
			this.progressChecker = new PPTConversionProgressChecker(dmsUrl,meetingId,pptId,1000,this);
			this.progressChecker.startCheck();
		}
		else
		{
			showConversionError(responseText);
			//	Show message for start error
		}
	}
	public void conversionStartError(String message)
	{
		showConversionError(message);
		dialog.getShareButton().setEnabled(true);
		//	Show message for start error.
//		this.browseButton.setEnabled(true);
	}
	
	/**
	 * Conversion progress listener callbacks
	 */
	public void conversionCancelled()
	{
//		this.browseButton.setEnabled(true);
		DebugPanel.getDebugPanel().addDebugMessage("Conversion cancelled");
		waiting.setVisible(false);
		dialog.getShareButton().setEnabled(true);
	}
	public ProgressCheckResponse conversionCheckReturn(String responseText)
	{
		//Window.alert("in conversion check response text = "+responseText);
		int error = 7500;
		ConversionProgressCheckResponse cpcr = this.dmsJsonResponseHandler.readConversionProgressCheckResponse(responseText);
		if (cpcr != null)
		{
			error = cpcr.getError();
			if (this.totalSlides <= 0)
			{
				this.totalSlides = cpcr.getTotalSlides();
			}
			this.slidesConverted = cpcr.getSlidesConverted();
		}
		if (error == 7200)
		{
			//	This means success.
//			int conversionProgress = (100*slidesConverted)/this.totalSlides ;
			int temp;
			if(this.slidesConverted == 0 && this.totalSlides == 0)
			{			
				this.progressMessageLabel.setText(ConferenceGlobals.getDisplayString("console.ppt.conversion.progress","Converting Presentation"));
				this.progressMessage.setText(ConferenceGlobals.getDisplayString("console.ppt.conversion.wait","Please Wait..."));
				for (temp = 0; temp<=100; temp+=10)
					{	
//						this.setProgress(temp);			
					}												
			}
			else
			{
				this.slideHeight = cpcr.getHeight();
				this.slideWidth = cpcr.getWidth();
				this.progressMessageLabel.setText(ConferenceGlobals.getDisplayString("console.ppt.preparing.progress","Preparing Presentation"));
				String s = this.slidesConverted+"";
				if (this.totalSlides > 0)
				{
					s += " of "+this.totalSlides;
				}
				this.progressMessage.setText(s+" pages");
//				this.setProgress(conversionProgress);
			}		
		}
		else
		{
			String errorMessage = ConferenceGlobals.getDisplayString("dms_error."+error, ""+error);
			this.showConversionError(errorMessage);
			this.progressChecker.stopCheck();
			dialog.getShareButton().setEnabled(true);
			//chaning the text to close
			dialog.changeButtonToClose();
			noErrorOccured = false;
//			this.browseButton.setEnabled(true);
		}
		return	cpcr;
	}
	public void conversionComplete(ProgressCheckResponse response)
	{
		this.progressMessage.setText(ConferenceGlobals.getDisplayString("console.ppt.complete","Complete "));
		//Window.alert("inside conversion complete responseText = "+response);
		//Window.alert("now calling the stop presentation...");
		//explicitly calling the stop presentation coz else the flash movie was not getting
		//enough time to uinload
		//sharingController.stopPPTPresentation();
		//DMSServerResponse dmsResponse = new DMSServerResponse(responseText);
		String url = commandURLFactory.createSealDMSPresentaion(getEncodedFileName(), 
				this.pptId, this.totalSlides, ConferenceGlobals.getConferenceKey(),
				this.slideWidth, this.slideHeight);
		//Window.alert("url = "+url);
		executeCommand(url);
		//sharingController.continueWorkAfterDMS(this.pptId);
		//dialog.hide();
	}
	
	protected	void	executeCommand(String url)
	{
		//this handler is used so that the console side change are made only after server side is done
		ResponseTextHandler respHandler = new ResponseTextHandler(){

			public void onCompletion(String responseText) {
				//Window.alert("on completion of ResponseTextHandler.. pptID="+pptId);
				sharingController.continueWorkAfterDMS(pptId);
				dialog.hide();
				
			}
			
		};
		
		JSONurlReader reader = new JSONurlReader(url, ConferenceGlobals.getConferenceKey(), respHandler);
		
		reader.doReadURL();
	}
	
	public void conversionError(String error)
	{
		this.showConversionError(error);
		dialog.getShareButton().setEnabled(true);
//		this.browseButton.setEnabled(true);
	}
	
	/**
	 * New pptid generation callbacks.
	 */
	public void pptIDGenerated(String newPPTID)
	{
		if (state == WAITING_FOR_PPT_ID_GENERATION)
		{
			DebugPanel.getDebugPanel().addDebugMessage("ppt id generated:"+newPPTID);
	//		Window.alert("pptIDGenerated = "+newPPTID);
			this.pptId = newPPTID;
			//this.pptId = selectedFileName;
			this.totalSlides = -1;
			this.slidesConverted = 0;
	//		String newUrl = this.dmsUrl+"/test1/file_upload.php?docID="+this.pptId+"&docType="+docType+"&meetingID="+this.meetingId;
			String newUrl = this.dmsUrl+"/uploadOnlyDocument"
				+"?meetingID="+this.meetingId+"&docID="+this.pptId+"&docType="+docType;
			this.dialog.selectFileForm.setAction(newUrl);
			DebugPanel.getDebugPanel().addDebugMessage("Reset form url, submitting upload form. Next comment should be 'posting upload form'");
			this.activityTimer = new Timer()
			{
				protected	int	count = 0;
				public void run()
				{
					showUploadProgress(selectedFileName,count++);
				}
			};
			this.activityTimer.scheduleRepeating(1000);
			this.dialog.selectFileForm.submit();
			state = WAITING_FOR_FILE_UPLOAD;
		}
		else
		{
			DebugPanel.getDebugPanel().addDebugMessage("Unexpected ppt id generation event");
		}
//		callResetUrl(newUrl);
//		callUpload();
	}
	public void pptIDGenerationFailed(String message)
	{
		if (state == WAITING_FOR_PPT_ID_GENERATION)
		{
			state = WAITING_FOR_FILE_SELECT;
			this.showConversionError(ConferenceGlobals.getDisplayString("console.ppt.conversion.fail","document id generation failed"));
			dialog.getShareButton().setEnabled(true);
			DebugPanel.getDebugPanel().addDebugMessage("ppt id generation failed:"+message);
		}
		else
		{
			DebugPanel.getDebugPanel().addDebugMessage("Unexpected ppt id generation event");
		}
//		this.browseButton.setEnabled(true);
	}
	public void onInterfaceResponse(String message)
	{
		DebugPanel.getDebugPanel().addDebugMessage("ppt id generation response:"+message);
	}
	
	//public void setDocType(String docType)
	//{
	//	this.docType = docType;
	//}
//	private	void	setProgress(int p)
//	{
//		if (this.progressBar == null)
//		{
//			progressBar = new ProgressBar(20);
//			progressBar.addStyleName("progressbar-noborder");
////			progressBar.setWidth("200px");
//			progressBar.setWidth("300px");
////			progressBar.setHeight("20px");
//			progressBarPanel.remove(this.placeholderLabel2);
//			progressBarPanel.add(progressBar);
//			progressBarPanel.setCellHorizontalAlignment(progressBar, HorizontalPanel.ALIGN_LEFT);
//		}
//		this.progressBar.setProgress(p);
//	}
	private	void	showUploadError(String error)
	{
		String msg = ConferenceGlobals.getDisplayString("dms_error_upload_1", "The document upload failed with error:");
		String s2 = ConferenceGlobals.getDisplayString("dms_error_2", "Please retry. If it continues to fail, please send a detailed email to support@dimdim.com");
		if (error != null)
		{
			msg = msg+" "+error+". ";
		}
		msg += s2;
		showErrorMessage(msg);
	}
	private	void	showConversionError(String error)
	{
		String msg = ConferenceGlobals.getDisplayString("dms_error_conversion_1", "The document conversion failed with error:");
		//String s2 = ConferenceGlobals.getDisplayString("dms_error_2", "Please retry. If it continues to fail, please send a detailed email to support@dimdim.com");
		if (error != null)
		{
			msg = msg+" "+error+". ";
		}
		waiting.setVisible(false);
		showErrorMessage(msg);
		//progressCommonWaitMessage2.setText(s2);
		//progressCommonWaitMessage2.addStyleName("common-error-message");
	}
	private	void	showErrorMessage(String message)
	{
		waiting.setVisible(false);
		progressCommonWaitMessage.setText(message);
		progressCommonWaitMessage.addStyleName("upload-error");
		progressMessage.setText("");
		progressMessageLabel.setText("");
		progressCommonWaitMessage.addStyleName("common-error-message");
	}
	private	void	clearMessage()
	{
		waiting.setVisible(false);
		progressCommonWaitMessage.setText("");
		progressCommonWaitMessage.removeStyleName("common-error-message");
		progressCommonWaitMessage2.setText("");
		progressCommonWaitMessage2.removeStyleName("common-error-message");
	}
	
	/*private	boolean	isFileRightType(String fileName)
	{
		if (this.docType.equalsIgnoreCase("ppt"))
		{
			if (selectedFileName.toLowerCase().endsWith("ppt") ||
					selectedFileName.toLowerCase().endsWith("pptx"))
			{
				return	true;
			}
			else
			{
				return	false;
			}
		}
		else
		{
			if (selectedFileName.toLowerCase().endsWith("pdf"))
			{
				return	true;
			}
			else
			{
				return	false;
			}
		}
	}*/
	
	private	boolean	isFileRightType(String fileName)
	{
	    String allowedFileType = "";
	   for (int i = 0; i < allowedFileTypes.length; i++)
	   {
	       allowedFileType = allowedFileTypes[i];
	       if (selectedFileName.toLowerCase().endsWith(allowedFileType))
		   {
		   	this.fileType = allowedFileTypesExtentions[i];
		   	this.docType = allowedFileTypesExtentions[i];
		   	return true;
		   }
	   }
	   return false;
	}
	
	private String getEncodedFileName(){
	    return encodeBase64(this.selectedFileName);   
	}
	
	private	native	String	encodeBase64(String s) /*-{
		return $wnd.Base64.encode(s);
	}-*/;
	
	
}
