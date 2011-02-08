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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.panels.client;

//import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
//import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
//import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.common.client.util.CommonUserInformationDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.managers.client.resource.DTPRestartListener;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.user.client.ActivePresenterAVManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class RecordControlClickListener implements ClickListener, DTPRestartListener /*ConfirmationListener, PopupListener */
{
	protected	WorkspacePanel	workspacePanel;
	protected	boolean			previousRecordingExists;
	protected	boolean			recordingInProgress;
//	protected	ConfirmationDialog	confirmationDialog;
	protected	StopRecordingFormWidget	stopRecordingFormWidget;
	protected	boolean					recordingStart = false;
	
	private		ClickListener	cancelClickListener;
	private		ClickListener	okClickListener;
	private		boolean			saveRecording = false;
	private		boolean			inPopout = false;
	
	public	RecordControlClickListener(WorkspacePanel workspacePanel)
	{
		this.workspacePanel = workspacePanel;
		this.previousRecordingExists = false;
		this.recordingInProgress = false;
	}
	public boolean isRecordingInProgress()
	{
		return recordingInProgress;
	}
	public	void	setInPopout(boolean inPopout)
	{
		this.inPopout = inPopout;
	}
	public void onClick(Widget sender)
	{
		//	No controls are allowed in popout.
		if (this.inPopout)
		{
			return;
		}
		// No need to check the sener because this click listener has only 1 purpose.
		UIRosterEntry currentUser = this.workspacePanel.getCurrentUser();
		if (currentUser.isHost())
		{
//			Window.alert("1");
			cancelClickListener = new ClickListener()
			{
				public void onClick(Widget sender)
				{
					CommonUserInformationDialog.hideCommonUserInformationDialog();
				}
			};
//			Window.alert("2");
			if (!ClientModel.getClientModel().getRecordingModel().isRecordingActive())
			{
				//	Recording is being started.
				Widget confirmationMessage = new StartRecordingQuestion1Widget();
//				Window.alert("3");
//				String confirmationMessage = ConferenceGlobals.getDisplayString("workspace.recording.start.desc","Do you want to start recording?");
				if (this.previousRecordingExists)
				{
					confirmationMessage = new StartRecordingQuestion2Widget();
//					Window.alert("4");
//					confirmationMessage = ConferenceGlobals.getDisplayString("workspace.recording.start.desc1","You have a previous recording. "+
//						"A single meeting can have only a single recording session. "+
//						"If you start recording again, previous recording will be erased. "+
//						"Do you wish to start recording?");
				}
//				Window.alert("5");
				okClickListener = new ClickListener()
				{
					public void onClick(Widget sender)
					{
						onOK();
					}
				};
//				Window.alert("6");
				String title = ConferenceGlobals.getDisplayString("workspace.recording.start.header","Do you want to start recording?");
//				Window.alert("7");
				CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog(
						title, confirmationMessage);
//				Window.alert("8");
				try
				{
					cuid.drawDialog();
				}
				catch(Exception e)
				{
					Window.alert(e.getMessage());
				}
//				Window.alert("9");
		    	cuid.addOKClickListener(okClickListener);
//				Window.alert("10");
		    	cuid.addCancelClickListener(cancelClickListener);
//				Window.alert("11");
				
//				confirmationDialog = new ConfirmationDialog(ConferenceGlobals.getDisplayString("workspace.recording.start.header","Do you want to start recording?"),
//					confirmationMessage,
//					"default-message",this);
//				confirmationDialog.setHideOnOK(false);
//				confirmationDialog.drawDialog();
			}
			else
			{
				//	For stop there are a few cases.
//				stopRecordingFormWidget = new StopRecordingFormWidget(ConferenceGlobals.getDisplayString("record.title.value","title")
//						,ConferenceGlobals.getDisplayString("record.description.value","description"),
//						ConferenceGlobals.getDisplayString("record.category.value","category"),
//						ConferenceGlobals.getDisplayString("record.keywords.value","keywords"));
				Widget confirmationMessage = new StopRecordingQuestion1Widget();
				String header = "Stop Recording";
				CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog(
						header, confirmationMessage);
				
				okClickListener = new ClickListener()
				{
					public void onClick(Widget sender)
					{
						onOK();
					}
				};
		    	cuid.drawDialog();
		    	cuid.addOKClickListener(okClickListener);
		    	cuid.addCancelClickListener(cancelClickListener);
				
//				confirmationDialog = new ConfirmationDialog("Stop Recording",stopRecordingFormWidget,
//					"default-message",this);
//				confirmationDialog.setHideOnOK(false);
//				confirmationDialog.drawDialog();
			}
		}
	}
	private	void	showMessage(String title, String message)
	{
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
		if (cuid == null)
		{
			cuid = CommonUserInformationDialog.getCommonUserInformationDialog(title,message);
	    	cuid.drawDialog();
		}
		else
		{
			cuid.setMessage(title,message);
		}
//		if (this.confirmationDialog != null)
//		{
//			this.confirmationDialog.setMessage(message);
//		}
//		else
//		{
//			DefaultCommonDialog.showMessage(title, message);
//			DefaultCommonDialog.getDialog().hideCloseButton();
//		}
	}
//	private	void	hideMessageBoxActual()
//	{
//		if (confirmationDialog != null)
//		{
//			confirmationDialog.hide();
//		}
//		else
//		{
//			DefaultCommonDialog.hideMessageBox();
//		}
//	}
//	private	void	hideMessageBox(boolean withDelay)
//	{
//		if (withDelay)
//		{
//			Timer closeDialog = new Timer()
//			{
//				public void run()
//				{
//					CommonUserInformationDialog.hideCommonUserInformationDialog();
//				}
//			};
//			closeDialog.schedule(2000);
//		}
//		else
//		{
//			CommonUserInformationDialog.hideCommonUserInformationDialog();
//		}
//	}
	public void onCancel()
	{
	}
	public void onOK()
	{
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
		cuid.removeDefaultCloseListener();
		cuid.hideCancelButton();
		cuid.hideOKButton();
		ResourceSharingController rsc = this.workspacePanel.getResourceSharingController();
		if (!ClientModel.getClientModel().getRecordingModel().isRecordingActive())
		{
			recordingStart = true;
			rsc.setRecordingOn(true);
			this.startRecording();
		}
		else
		{
			recordingStart = false;
			rsc.setRecordingOn(false);
			this.stopRecording();
		}
	}
//	public void onPopupClosed(PopupPanel sender, boolean autoClosed)
//	{
//		ResourceSharingController rsc = this.workspacePanel.getResourceSharingController();
//		if (this.recordingStart)
//		{
//			if (rsc.isDTPSharingActive())
//			{
//				rsc.restartDTPwithRecord();
//			}
//		}
//		else
//		{
//			if (rsc.isDTPSharingActive())
//			{
//				rsc.restartDTPwithoutRecord();
//			}
//		}
//	}
	public	void	startRecording()
	{
		UIRosterEntry currentUser = this.workspacePanel.getCurrentUser();
		ClientModel.getClientModel().getRecordingModel().toggleRecording();
		if(ClientModel.getClientModel().getRecordingModel().isRecordingActive())
		{
			this.recordingInProgress = true;
			workspacePanel.changeRecordingStatus(true);
			if (ActivePresenterAVManager.getPresenterAVManager(currentUser).isBroadcasterActive())
			{
				this.showMessage(ConferenceGlobals.getDisplayString("workspace.recording.starting.header","Starting Recording")
						, ConferenceGlobals.getDisplayString("workspace.recording.starting.desc","Recording is being started. The audio video broadcaster and desktop sharing will be restarted in record mode.")
						);
				ActivePresenterAVManager.getPresenterAVManager(currentUser).restartAsRecord();
				restartDTPwithRecording(true);
			}
			else
			{
				restartDTPwithRecording(false);
			}
		}
	}
	public	void	stopRecording()
	{
//		Window.alert("stopRecording:1");
		UIRosterEntry currentUser = this.workspacePanel.getCurrentUser();
		ClientModel.getClientModel().getRecordingModel().toggleRecording();
		if(!ClientModel.getClientModel().getRecordingModel().isRecordingActive())
		{
//			Window.alert("stopRecording:2");
			this.recordingInProgress = false;
			workspacePanel.changeRecordingStatus(false);
			if (ActivePresenterAVManager.getPresenterAVManager(currentUser).isBroadcasterActive())
			{
				this.showMessage(ConferenceGlobals.getDisplayString("workspace.recording.stopping.header","Starting Recording")
						, ConferenceGlobals.getDisplayString("workspace.recording.stopping.desc","Recording is being started. The audio video broadcaster and desktop sharing will be restarted in record mode.")
						);
				ActivePresenterAVManager.getPresenterAVManager(currentUser).restartAsLive();
//				Window.alert("stopRecording:3");
				restartDTPwithoutRecording(true);
//				Window.alert("stopRecording:4");
			}
			else
			{
//				Window.alert("stopRecording:5");
				restartDTPwithoutRecording(false);
//				Window.alert("stopRecording:6");
			}
		}
	}
	private	void	restartDTPwithRecording(boolean avRestarted)
	{
		ResourceSharingController rsc = this.workspacePanel.getResourceSharingController();
//		this.hideMessageBox(avRestarted || rsc.isDTPSharingActive());
		if (rsc.isDTPSharingActive())
		{
			rsc.restartDTPwithRecord(this);
		}
		else
		{
			CommonUserInformationDialog.hideCommonUserInformationDialog();
		}
	}
	private	void	restartDTPwithoutRecording(boolean avRestarted)
	{
//		Window.alert("restartDTPwithoutRecording:1");
		ResourceSharingController rsc = this.workspacePanel.getResourceSharingController();
//		this.hideMessageBox(avRestarted || rsc.isDTPSharingActive());
		if (rsc.isDTPSharingActive())
		{
//			Window.alert("restartDTPwithoutRecording:2");
			rsc.restartDTPwithoutRecord(this);
//			Window.alert("restartDTPwithoutRecording:3");
		}
		else
		{
//			Window.alert("restartDTPwithoutRecording:4");
			this.dtpRestarted(false, true);
//			Window.alert("restartDTPwithoutRecording:5");
//			CommonUserInformationDialog.hideCommonUserInformationDialog();
		}
	}
	public void dtpRestarted(boolean recordingOn, boolean restartSuccessful)
	{
//		Window.alert("dtpRestarted:1");
		if (restartSuccessful)
		{
			if (!recordingOn)
			{
//				Window.alert("dtpRestarted:2");
				this.promptForSave();
//				Window.alert("dtpRestarted:3");
			}
			else
			{
				//	Recording is being stopped. We need to prompt the user for more information
				//	Once the recording is stopped.
				CommonUserInformationDialog.hideCommonUserInformationDialog();
			}
		}
		else
		{
//			Window.alert("dtpRestarted:4");
			this.showMessage("Desktop Sharing Restart Error", "DTP Restart failed. Please restart desktop sharing again.");
//			Window.alert("dtpRestarted:5");
		}
		ResourceSharingController rsc = this.workspacePanel.getResourceSharingController();
		rsc.removeDTPRestartListener();
	}
	private	void	promptForSave()
	{
		Widget confirmationMessage = new StopRecordingQuestion2Widget();
		String header = "Save Recording ?";
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
//		Window.alert("promptForSaveAndUpload:3");
		if (cuid == null)
		{
//			Window.alert("promptForSaveAndUpload:4");
			cuid = CommonUserInformationDialog.getCommonUserInformationDialog(header,confirmationMessage);
//			Window.alert("promptForSaveAndUpload:5");
	    	cuid.drawDialog();
//			Window.alert("promptForSaveAndUpload:6");
		}
		else
		{
//			Window.alert("promptForSaveAndUpload:7");
			if (this.okClickListener != null)
			{
				cuid.removeOKClickListener(this.okClickListener);
			}
			if (this.cancelClickListener != null)
			{
				cuid.removeCancelClickListener(this.cancelClickListener);
			}
//			Window.alert("promptForSaveAndUpload:8");
			cuid.setContentWidget(header,confirmationMessage);
//			Window.alert("promptForSaveAndUpload:9");
		}
		
		okClickListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				saveRecording = true;
				previousRecordingExists = true;
				if (ConferenceGlobals.blipTVEnabled)
				{
					promptForUpload();
				}
				else
				{
					ClientModel.getClientModel().getRecordingModel().SetRecordingStopOptions(
							saveRecording,false,"","","","");
					
					showSaveExitMessage();
				}
			}
		};
		cancelClickListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				saveRecording = false;
				previousRecordingExists = false;
				showNoSaveExitMessage();
				
				ClientModel.getClientModel().getRecordingModel().SetRecordingStopOptions(
						saveRecording, false,"", "", "", "");
			}
		};
		cuid.addOKClickListener(this.okClickListener);
		cuid.addCancelClickListener(this.cancelClickListener);
		cuid.showOKButton();
		cuid.showCancelButton();
	}
	private	void	showSaveExitMessage()
	{
		Widget confirmationMessage = new StopRecordingMessage1Widget();
		String header = "Recording Pending";
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
//		Window.alert("promptForSaveAndUpload:3");
		cuid.removeOKClickListener(this.okClickListener);
		cuid.removeCancelClickListener(this.cancelClickListener);
//		Window.alert("promptForSaveAndUpload:8");
		cuid.setContentWidget(header,confirmationMessage);
//		Window.alert("promptForSaveAndUpload:9");
		
		okClickListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				CommonUserInformationDialog.hideCommonUserInformationDialog();
			}
		};
		cuid.addOKClickListener(this.okClickListener);
		cuid.showOKButton();
		cuid.hideCancelButton();
	}
	private	void	showNoSaveExitMessage()
	{
		Widget confirmationMessage = new StopRecordingQuestion3Widget();
		String header = "Confirm";
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
//		Window.alert("promptForSaveAndUpload:3");
//			Window.alert("promptForSaveAndUpload:7");
			if (this.okClickListener != null)
			{
				cuid.removeOKClickListener(this.okClickListener);
			}
			if (this.cancelClickListener != null)
			{
				cuid.removeCancelClickListener(this.cancelClickListener);
			}
//			Window.alert("promptForSaveAndUpload:8");
			cuid.setContentWidget(header,confirmationMessage);
//			Window.alert("promptForSaveAndUpload:9");
		
		okClickListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				CommonUserInformationDialog.hideCommonUserInformationDialog();
			}
		};
		cancelClickListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				promptForSave();
			}
		};
		cuid.addOKClickListener(this.okClickListener);
		cuid.addCancelClickListener(this.cancelClickListener);
		cuid.showOKButton();
		cuid.showCancelButton();
	}
	private	void	promptForUpload()
	{
//		Window.alert("promptForSaveAndUpload:1");
		stopRecordingFormWidget = new StopRecordingFormWidget(ConferenceGlobals.getDisplayString("record.title.value","title")
			,ConferenceGlobals.getDisplayString("record.description.value","description"),
			ConferenceGlobals.getDisplayString("record.category.value","category"),
			ConferenceGlobals.getDisplayString("record.keywords.value","keywords"));
		
//		Window.alert("promptForSaveAndUpload:2");
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
//		Window.alert("promptForSaveAndUpload:3");
//			Window.alert("promptForSaveAndUpload:7");
			if (this.okClickListener != null)
			{
				cuid.removeOKClickListener(this.okClickListener);
			}
			if (this.cancelClickListener != null)
			{
				cuid.removeCancelClickListener(this.cancelClickListener);
			}
//			Window.alert("promptForSaveAndUpload:8");
			cuid.setContentWidget("Upload Recording to blip.tv?",stopRecordingFormWidget);
//			Window.alert("promptForSaveAndUpload:9");
		
		okClickListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				ClientModel.getClientModel().getRecordingModel().SetRecordingStopOptions(
						saveRecording, true,
						stopRecordingFormWidget.getTitleText(),
						stopRecordingFormWidget.getDescriptionText(),
						stopRecordingFormWidget.getCategoryText(),
						stopRecordingFormWidget.getKeywordsText());
					
				CommonUserInformationDialog.hideCommonUserInformationDialog();
			}
		};
		cancelClickListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				ClientModel.getClientModel().getRecordingModel().SetRecordingStopOptions(
						saveRecording, false,"", "", "", "");
					
				CommonUserInformationDialog.hideCommonUserInformationDialog();
			}
		};
		cuid.addOKClickListener(this.okClickListener);
		cuid.addCancelClickListener(this.cancelClickListener);
		cuid.showOKButton();
		cuid.showCancelButton();
	}
}
