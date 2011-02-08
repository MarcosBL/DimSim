package com.dimdim.conference.ui.resources.client;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.json.client.JSONurlReader;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.CommandURLFactory;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SelectFileDialogue extends SelectOnlyFileDialog{
	protected	CommandURLFactory		commandsFactory = new CommandURLFactory();
	protected	DocUploadPanel 	newPPTUploadPanel;
	
	public SelectFileDialogue(ResourceSharingController sharingController) {
		super(sharingController);
		//Window.alert("created ... upload panel...");
		this.dialogName = "large";
	}
	
	protected	Widget	getContent()
	{
		VerticalPanel contentPanel = (VerticalPanel)super.getContent();
		//adding different submit action to form
		
		String action = "http://"+ConferenceGlobals.dmsServerAddress+"/uploadPresentation";
		//Window.alert("selectFileForm = "+selectFileForm);
		selectFileForm.setAction(action);
		
		//button text is made as close
		changeButtonToClose();
		
//		VerticalPanel panel = new VerticalPanel();
//		selectFileForm.setWidget(panel);
		//Window.alert("selectFileForm target= "+selectFileForm.getTarget());
		//Window.alert("selectFileForm action= "+selectFileForm.getAction());
		//setting names of attributes so that the form is submitted to DMS properly
		//existingFileField.setName("presentationFile");
		//Window.alert("selectFileForm contentPanel= "+contentPanel);
		//selectFileForm.setWidget(contentPanel);
		//Window.alert("name set to existingFileField...");
		//addng file upload widget
		//panel.add(existingFileField);
		
//		TextBox	meetingID = new TextBox();
//		meetingID.setVisible(false);
//		meetingID.setName("meetingID");
//		meetingID.setText(ConferenceGlobals.getConferenceKey());
		//Window.alert("meeting id text = "+meetingID.getText());
		//Window.alert("selectFileForm = "+selectFileForm);
//		selectFileForm.add(meetingID);
		//Window.alert("meeting id added");
		
//		TextBox type = new TextBox();
//		type.setVisible(false);
//		type.setName("docType");
//		type.setText(docType);
//		selectFileForm.add(type);
		//Window.alert("type added");
		
		this.existingFileField.setName("docFile");
		newPPTUploadPanel = new DocUploadPanel(
				"http://"+ConferenceGlobals.dmsServerAddress,
				ConferenceGlobals.conferenceKeyQualified,
				this, sharingController);
		contentPanel.add(newPPTUploadPanel);
		//newPPTUploadPanel.setDocType(docType);
		
		//Window.alert("added custom fields...");
		this.applyButton.removeClickListener(this);
		this.applyButton.addClickListener(newPPTUploadPanel);
		
		 // Add an event handler to the form.
		selectFileForm.addFormHandler(newPPTUploadPanel);
		
		FormHandler fh = new FormHandler()
		{
	      public void onSubmitComplete(FormSubmitCompleteEvent event) {
//	        // When the form submission is successfully completed, this event is
//	        // fired. Assuming the service returned a response of type text/plain,
//	        // we can get the result text here (see the FormPanel documentation for
//	        // further explanation).
//	    	Window.alert("dilip event get..");
//	    	Window.alert(event.toString());
//	    	String result = event.getResults();
//			Window.alert(result);
//			DMSServerResponse dmsResponse = new DMSServerResponse(result);
//			String url = commandURLFactory.createSealDMSPresentaion(dmsResponse.getPptName(), 
//					dmsResponse.getPptID(), dmsResponse.getNoOfSlides(), ConferenceGlobals.getConferenceKey());
//			Window.alert("url = "+url);
//			executeCommand(url);
//			//Window.alert("after executing action....");
//			sharingController.continueWorkAfterDMS(dmsResponse.getPptID());
//			//Window.alert("done with submitting to server...");
//			hide();
	      }
		
	      public void onSubmit(FormSubmitEvent event) {
	        // This event is fired just before the form is submitted. We can take
	        // this opportunity to perform validation.
	    	  
	    	//this form is not for submission hence explicitly avopiding it, in mac safari this was getting submitted   
	        /*if (tb.getText().length() == 0) {
	          Window.alert("The text box must not be empty");
	          event.setCancelled(true);
	        }*/
	    	  event.setCancelled(true);
	    	  //Window.alert("before submitting...");
	      }
	    };
		
		//Window.alert("added listeners..");
		
		return	contentPanel;
	}
	public	void	onClick(Widget w)
	{
		//Window.alert("dilip on click...");
		this.errorMessage.setText(" ");
		if (w == this.applyButton)
		{
			selectFileForm.submit();
		}
		else if (w == this.closeButton)
		{
			cancelUpload();
		}
	}

	public void cancelUpload() {
		this.newPPTUploadPanel.uploadCancelled();
		try
		{
			hide();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void changeButtonToClose(){
	    this.resetCloseButtonText(UIStrings.getCloseLabel());
	}
	
	public void changeButtonToCancel(){
	    this.resetCloseButtonText(UIStrings.getCancelLabel());
	}
	
	protected	void	executeCommand(String url)
	{
		JSONurlReader reader = new JSONurlReader(url);
		reader.doReadURL();
	}
	
	public Button getShareButton()
	{
		return this.applyButton;
	}

}
