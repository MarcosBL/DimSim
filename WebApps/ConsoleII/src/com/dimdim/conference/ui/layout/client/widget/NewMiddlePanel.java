package com.dimdim.conference.ui.layout.client.widget;

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.dialogues.client.SettingsDialog;
import com.dimdim.conference.ui.dialogues.client.common.ConferenceClosedHtml;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.layout.client.main.NewLayout;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ClientStateModelListener;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModelAdapter;
import com.dimdim.conference.ui.model.client.UIResources;
import com.dimdim.conference.ui.publisher.client.PublisherInterfaceManager;
import com.dimdim.conference.ui.sharing.client.CollaborationAreaManager;
import com.dimdim.conference.ui.sharing.client.ResourceSharingPanel;
import com.dimdim.conference.ui.user.client.ActiveAudiosManager;
import com.dimdim.conference.ui.user.client.ActivePresenterAVListener;
import com.dimdim.conference.ui.user.client.ActivePresenterAVManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.dimdim.conference.ui.panels.client.WorkspacePanel;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;

public class NewMiddlePanel implements WindowCloseListener, 
		ClientStateModelListener, ActivePresenterAVListener, PopupListener, UserCallbacks{

	//	protected HorizontalPanel	basePanel = new HorizontalPanel();
	UIRosterEntry currentUser = null;
	
	protected	CollaborationAreaManager	collaborationAreaManager;
	
	WorkspacePanel workSpace = null;
	protected	UserRoster			userRoster;
	private	boolean	sessionClosed = false;
	private	String	trackbackUrl = "/";
	RosterModelAdapter rosterModelListener = null;
	NewLayout consoleLayout = null;
	ConsoleMiddleLeftPanel leftPanel = null;
	private	boolean	serverConnectionLost = false;
	
	public NewMiddlePanel(NewLayout consoleLayout, UIRosterEntry currentUser)
	{
//		this.initWidget(basePanel);
		this.currentUser = currentUser;
		this.consoleLayout = consoleLayout;
		//Window.alert("inside middle panel..");
		
		leftPanel = new ConsoleMiddleLeftPanel(consoleLayout, currentUser, this);
//		basePanel.add(leftPanel);
//		basePanel.setWidth("100%");
//		basePanel.setHeight("100%");
//
//		basePanel.add(leftPanel);
		
		CollaborationAreaManager.initManager(currentUser, 600, 400, "res_showing");
		this.collaborationAreaManager = CollaborationAreaManager.getManager();
		ResourceSharingPanel rsp = this.collaborationAreaManager.getResourceSharingPanel();
		//basePanel.add(rsp);
		userRoster = leftPanel.getUserRoster();
		
		ResourceSharingController rsc = leftPanel.getResourceRoster().getResourceManager().getSharingController();
		workSpace = new WorkspacePanel(currentUser, rsp, consoleLayout, rsc, this.collaborationAreaManager);
		this.collaborationAreaManager.addResourceSharingCallbacksListener(rsc);
		//workSpace.setStyleName("console-collab-area");
		//workSpace.setWidth("100%");
		//basePanel.add(workSpace);
		//workSpace.setBorder();
		//consoleLayout.addWidgetToID("collab_area", workSpace);
		//workSpace.panelResized(740, 700);
		
		//leftPanel.setStyleName("console-left-area");
		
//		basePanel.setCellHorizontalAlignment(leftPanel,HorizontalPanel.ALIGN_LEFT);
//		basePanel.setCellVerticalAlignment(leftPanel,VerticalPanel.ALIGN_TOP);
//		basePanel.setCellWidth(leftPanel, "20%");
		//basePanel.setCellHeight(leftPanel, "100%");
//		basePanel.setCellHorizontalAlignment(leftPanel,HorizontalPanel.ALIGN_LEFT);
//		basePanel.setCellVerticalAlignment(leftPanel,VerticalPanel.ALIGN_TOP);
//		Label tempLabel = new Label("place holder for workspace...");
//		basePanel.add(tempLabel);
//		basePanel.setCellHorizontalAlignment(workSpace,HorizontalPanel.ALIGN_LEFT);
//		basePanel.setCellVerticalAlignment(workSpace,VerticalPanel.ALIGN_TOP);
		//basePanel.setCellWidth(workSpace, "80%");
		//basePanel.setCellHeight(workSpace, "100%");
		
		
		leftPanel.getResourceRoster().getResourceManager().
			getSharingController().setResourceSharingDisplay(workSpace);
		
		//basePanel.setCellHorizontalAlignment(rsp,HorizontalPanel.ALIGN_LEFT);
		//basePanel.setCellVerticalAlignment(rsp,VerticalPanel.ALIGN_TOP);
//		basePanel.setWidth("100%");
//		basePanel.setHeight("100%");
		
		this.rosterModelListener = new LayoutRosterListener(this);
		
		trackbackUrl = this.getReturnUrl();
		Window.addWindowCloseListener(this);
		
	}
	public	void	divShown(String divId)
	{
		if (this.workSpace != null)
		{
			this.workSpace.divShown(divId);
		}
	}
	public	void	divHidden(String divId)
	{
		if (this.workSpace != null)
		{
			this.workSpace.divHidden(divId);
		}
	}
	public	WorkspacePanel	getWorkspacePanel()
	{
		return	this.workSpace;
	}
	public void resizePanel(int panelWidth, int panelHeight)
	{
//		this.setWidth(panelWidth+"px");
		workSpace.containerResized(panelWidth, panelHeight);
		
//		if(workSpace.isChatDisplayed)
//		{
//			panelWidth = panelWidth - 500;
//		}
//		else
//		{
//			panelWidth = panelWidth - (500-workSpace.chatWidgetWidth);
//		}
//		
//		DebugPanel.getDebugPanel().addDebugMessage("new middle panel window width:"+panelWidth+", height:"+panelHeight +", workSpace.isChatDisplayed ="+workSpace.isChatDisplayed);
//		workSpace.panelResized(panelWidth, panelHeight - 80);
	}
	
	protected	void	entryDeniedTo(UIRosterEntry user)
	{
		if (user.getUserId().equals(currentUser.getUserId()))
		{
			onRemovedFromConference();
		}
	}
	
	
	public	void	checkOldHostConsole(UIRosterEntry user)
	{
		if (currentUser.getStatus().equals(""))
		{
			currentUser.setUserId(ConferenceGlobals.getUserId());
			currentUser.setStatus(ConferenceGlobals.getUserStatus());
		}
//		Window.alert(currentUser.toJson());
//		Window.alert(user.toJson());
		
		int	myIdInt = (new Integer(currentUser.getUserId())).intValue();
		int	newHostIdInt = (new Integer(user.getUserId())).intValue();
//		Window.alert("Old Host ID:"+myIdInt);
//		Window.alert("New Host ID:"+newHostIdInt);
		
		if (currentUser.isPreviousHost() || (currentUser.isHost() && (myIdInt < newHostIdInt)))
		{
				if (!sessionClosed)
				{
					sessionClosed = true;
					this.closePopoutAndAudioPlayers();
					this.closeMyAudio();
					//workspace.closeAllShares();
					ClientModel.getClientModel().getClientStateModel().setConferenceInactive();
					consoleLayout.stopTimer();
					ConferenceClosedHtml cch = new ConferenceClosedHtml(this);
					DefaultCommonDialog.showMessage(UserGlobals.getUserGlobals().
							getYouAreRemovedHeading(),UserGlobals.getUserGlobals().getYouAreRemovedMessage(), cch);
				}
		}
//		UIGlobals.theDialogBox.addPopupListener(cch);
	}
	
	public	void setActivePresenterWidgetsVisibility(UIRosterEntry user)
	{
			//hide/show the div of show items
		boolean activePresenter = false;
		if (user.getUserId().equals(currentUser.getUserId()))
		{
			activePresenter = UIGlobals.isActivePresenter(user);
			leftPanel.setCurrentUser(user);
			consoleLayout.setIDVisibility("main_showitems", activePresenter);
		}
		if (this.workSpace != null)
		{
			this.workSpace.roleChanged(activePresenter);
		}
	}
	
	public	void	onRemovedFromConference()
	{
		sessionClosed = true;
		this.closePopoutAndAudioPlayers();
		this.closeMyAudio();
		//workspace.closeAllShares();
		ClientModel.getClientModel().getClientStateModel().setConferenceInactive();
		consoleLayout.stopTimer();
		ConferenceClosedHtml cch = new ConferenceClosedHtml(this);
		DefaultCommonDialog.showMessage(UserGlobals.getUserGlobals().
				getYouAreRemovedHeading(),UserGlobals.getUserGlobals().getYouAreRemovedMessage(), cch);
//		UIGlobals.theDialogBox.addPopupListener(cch);
	}
	
	private	void	closePublisher()
	{
		if (UIGlobals.isActivePresenter(currentUser) && ConferenceGlobals.publisherEnabled
				&& ("true".equalsIgnoreCase(ConferenceGlobals.getPubAvailable())) )
		{
			PublisherInterfaceManager.getManager().stopDTPAndAppShare();
//			PublisherInterfaceManager.getManager().stopUploadPPT();
//			PublisherInterfaceManager.getManager().stopAppSelector();
			PublisherInterfaceManager.getManager().closePublisher();
		}
	}
	
	private void	closePopoutAndAudioPlayers()
	{
		try
		{
			this.closePopouts();
		}
		catch(Exception e)
		{
			
		}
		if (this.userRoster != null)
		{
			try
			{
				this.userRoster.getUserList().consoleClosing();
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	public void onTrackbackUrlChanged(String url)
	{
	    //when url is changed it is coming with "" so chopping them
	    	if(url.startsWith("\"") && url.length() > 1)
	    	{	
	    	    url = url.substring(1, url.length()-1);
	    	}
		this.trackbackUrl = url;

		ConferenceGlobals.userInfoDictionary.setStringValue("return_url",url);
		ConferenceGlobals.userInfoDictionary.setStringValue("return_url_in_setings", url);	
	}
	
	public void onTimeChanged(String time)
	{
	    //Window.alert("time changed ... time = "+time);
	    String temp = time.substring(1, time.length()-1);
	    //Window.alert("time changed ... temp = "+temp);
	    //Window.alert("time changed ... timeInteger = "+Integer.parseInt(temp));
	    
	    int timeInt = Integer.parseInt(temp);
	    ConferenceGlobals.setCurrentMaxMeetingTime(timeInt);
	}
	
	
	public	void	closeConsole()
	{
		sessionClosed = true;
		this.closePopoutAndAudioPlayers();
		this.closeMyAudio();
		this.onSignout();
		
//		if (this.wbMovie != null)
//		{
//			this.wbMovie.stop("WB2N");
//		}
	}
	public	void	closeMyAudio()
	{
		if (ActiveAudiosManager.getManager(currentUser) != null)
		{
			ActiveAudiosManager.getManager(currentUser).consoleClosing();
//			this.myAudioEventsListener = null;
		}
//		if (this.presenterAVManager != null)
//		{
			ActivePresenterAVManager.getPresenterAVManager(currentUser).consoleClosing();
//			this.presenterAVManager = null;
//		}
//		else
//		{
//			Window.alert("Presenter AV Manager not accessible");
//		}
	}

	protected	void	onSignout()
	{
//		if (this.wbMovie != null)
//		{
//			this.wbMovie.stop("WB2N");
//		}
		sessionClosed = true;
		this.closePublisher();
		closePopoutAndAudioPlayers();
		closeMyAudio();
		ClientModel.getClientModel().leaveConference();
		//Window.alert("clicked on signout....after publisher closed isInPopup = "+ConferenceGlobals.isInPopup);
		if (ConferenceGlobals.isInPopup)
		{
			closeWindow();
		}
		else
		{
			try
			{
				String returnUrl = this.trackbackUrl;
				if (returnUrl == null || returnUrl.equals("undefined") || returnUrl.length() == 0)
				{
				    //returnUrl = ConferenceGlobals.currentBaseWebappURL;
				    returnUrl = getDefaultTrackbackURL();
				}
				setLocation(returnUrl);
			}
			catch(Exception e)
			{
				setLocation(ConferenceGlobals.currentBaseWebappURL);
			}
		}
	}
	/**************************** Window Close Interface *****************/
	private	void	closeAllMeetingActivity()
	{
		closePublisher();
		closePopoutAndAudioPlayers();
		closeMyAudio();
	}
	public String onWindowClosing()
	{
	    //Window.alert("In onWindowClosing");
		String retval = "";
			if (!ClientModel.getClientModel().getClientStateModel().isConferenceActive() ||
					UIGlobals.isInLobby(this.currentUser))
			{
				//	If the conference is not active return null, irrespective of the role.
				//Window.alert("null");
				return null;
			}
			else
			{
				//Window.alert("after closeMyAudio");
				if  (UIGlobals.isPresenter(this.currentUser))
				{
					closePublisher();
					UIResourceObject resource = collaborationAreaManager.getResourceSharingPanel().getActiveResource();
					if(null != resource && UIResourceObject.RESOURCE_TYPE_COBROWSE.equals(resource.getResourceType()))
					{
						//Window.alert("If you are sharing a website and do not want to close a meeting, Please click 'Cancel'");
						retval = "If you are sharing a website and do not want to close a meeting, Please click 'Cancel'";
					}else{
						//Window.alert(UIConstants.getConstants().presenterDepartureWarning());
						retval = UIConstants.getConstants().presenterDepartureWarning();
					}
				}
				else
				{
					//Window.alert(UIConstants.getConstants().attendeeDepartureWarning());
					retval = UIConstants.getConstants().attendeeDepartureWarning();
				}
			}
		
		//Window.alert("retval = "+retval);
			retval = retval.trim();
		return retval;
	}
	
	public void onWindowClosed()
	{
		sessionClosed = true;
//		if (ClientModel.getClientModel().getClientStateModel().isConferenceActive())
//		{
			try
			{
				closeAllMeetingActivity();
			}
			catch(Exception e)
			{
				//Window.alert("Exception while closing popout.");
			}
			ClientModel.getClientModel().leaveConference();
//		}
//		else
//		{
//		}
	}
	
	
	public	void	onConferenceClosed()
	{
		this.closePopoutAndAudioPlayers();
//		Window.alert("Closing Console - 1");
		this.closeMyAudio();
//		Window.alert("Closing Console - 2");
		consoleLayout.stopTimer();
//		Window.alert("Closing Console - 3");
		ConferenceClosedHtml cch = new ConferenceClosedHtml(this);
//		Window.alert("Closing Console - 4");
		DefaultCommonDialog dlg = new DefaultCommonDialog(UIStrings.getThankyouLabel(), cch, "meeting-closed");
		dlg.addPopupListener(cch);
//		Window.alert("Closing Console - 5");
		dlg.drawDialog();
//		Window.alert("Closing Console - 6");
		/*
		DefaultCommonDialog dlg = new DefaultCommonDialog(this.centerHeading, cch, "meeting-closed");
		dlg.addPopupListener(cch);
		UIGlobals.theDialogBox = dlg;
		int left = UIGlobals.getCommonDialogBoxPopupPositionX();
		int top = UIGlobals.getCommonDialogBoxPopupPositionY();
		UIGlobals.theDialogBox.setPopupPosition(left, top);
		UIGlobals.theDialogBox.show();
		*/
	}
	public void onConsoleDataSent()
	{
		//this is one call that is made on publisher whose return value is not required
		//but on ie first time call on pub is not working properly later calls work properly
		//so just making this redundant but necessary call
		if(ConferenceGlobals.publisherEnabled )
		{
			int temp = PublisherInterfaceManager.getManager().isDriverPresent();
		}
		
		if (UIGlobals.isPresenter(this.currentUser))
		{
			//Window.alert("in onConsoleDataSent calling ...  isDriverPresent ConferenceGlobals.publisherEnabled = "+ConferenceGlobals.publisherEnabled);
		    if(ConferenceGlobals.publisherEnabled )
		    {
				int returnVal = PublisherInterfaceManager.getManager().isDriverPresent();
				int erroMsgCode = -1;
				//if dirver is not installer show a message
				if(returnVal != 1)
				{
				    if (returnVal == 0)
				    {
						erroMsgCode = 24;
				    }
				    else if (returnVal == 2)
				    {
						erroMsgCode = 25;
				    }
				    DefaultCommonDialog.showMessage(
				    		ConferenceGlobals.getDisplayString("publisher_error.header","Warning"), 
				    		ConferenceGlobals.getDisplayString("publisher_error."+erroMsgCode,"Unknown Error"), this);
				}
			    else if (getScreenWidth() < 1024 && getScreenHeight() < 768)
			    {
			    	String header = ConferenceGlobals.getDisplayString("low_resolution.header","Warning");
			    	String message1 = ConferenceGlobals.getDisplayString("low_resolution.message1","low resolution");
			    	String message2 = ConferenceGlobals.getDisplayString("low_resolution.message2","low resolution");
				    DefaultCommonDialog.showMessage(header,message1+" "+getScreenWidth()+" x "+
				    		getScreenHeight()+" "+message2, this);
			    }
				else
				{
				  //follow normal flow
				    onPopupClosed(null, false);
				}
		    }
		    else if (getScreenWidth() < 1024 && getScreenHeight() < 768)
		    {
		    	String header = ConferenceGlobals.getDisplayString("low_resolution.header","Warning");
		    	String message1 = ConferenceGlobals.getDisplayString("low_resolution.message1","low resolution");
		    	String message2 = ConferenceGlobals.getDisplayString("low_resolution.message2","low resolution");
			    DefaultCommonDialog.showMessage(header,message1+" "+getScreenWidth()+" x "+
			    		getScreenHeight()+" "+message2, this);
		    }
		    else
		    {
		    	onPopupClosed(null,false);
		    }
		}
	}
	
	public void onPopupClosed(PopupPanel popup, boolean autoClosed)
	{
	    if(!ConferenceGlobals.isPresenterAVAudioDisabled())
	    {
	    	startPresenterAV();
	    }
	}
	
	private void startPresenterAV(){
		ActivePresenterAVManager.getPresenterAVManager(currentUser).reloadPresenterAV();
	}
	
	public	void	onTimeWarning1()
	{
		DefaultCommonDialog.showMessage(UserGlobals.getUserGlobals().getTimeWarningMessageHeading(),
				getTimeWarningMessage(), new PopupListener()
		{
			public	void	onPopupClosed(PopupPanel pp, boolean autoClosed)
			{
				SettingsDialog dlg = new SettingsDialog();
				dlg.drawDialog();
			}
		});
		
	}
	public	void	onTimeWarning2()
	{
		DefaultCommonDialog.showMessage(UserGlobals.getUserGlobals().getTimeWarningMessageHeading(),
				getTimeWarningMessage());
	}
	public	void	onTimeWarning3()
	{
		DefaultCommonDialog.showMessage(UserGlobals.getUserGlobals().getTimeWarningMessageHeading(),
				getTimeWarningMessage());
	}
	protected	String	getTimeWarningMessage()
	{
		String s1 = UserGlobals.getUserGlobals().getTimeWarning1Message();
		String s2 = UserGlobals.getUserGlobals().getTimeWarning2Message();
		String s3 = UserGlobals.getUserGlobals().getTimeWarning3Message();
		String startDateStr = UIGlobals.getConsoleStartDate_str();
		String endDateStr = UIGlobals.getConsoleEndDate_str();
		
		return	s1+startDateStr+" "+s2+" "+endDateStr+" "+s3;
	}
	protected	String	getTimeExpiredMessage()
	{
		String s1 = UserGlobals.getUserGlobals().getTimeWarning1Message();
		String s2 = UserGlobals.getUserGlobals().getTimeWarning4Message();
		String s3 = UserGlobals.getUserGlobals().getTimeWarning5Message();
		String startDateStr = UIGlobals.getConsoleStartDate_str();
		String endDateStr = UIGlobals.getConsoleEndDate_str();
		
		return	s1+startDateStr+" "+s2+" "+endDateStr+" "+s3;
	}
	public	void	onTimeExpired()
	{
		this.closePublisher();
		this.closePopoutAndAudioPlayers();
		this.closeMyAudio();
		ClientModel.getClientModel().getClientStateModel().setConferenceInactive();
		consoleLayout.stopTimer();
		DefaultCommonDialog.showMessage(UserGlobals.getUserGlobals().getTimeWarningMessageHeading(),
				getTimeExpiredMessage(), new PopupListener()
			{
				public	void	onPopupClosed(PopupPanel pp, boolean autoClosed)
				{
					closeConsole();
				}
			});
	}
	
	
	private native void closePopouts() /*-{
		$wnd.handleConsoleClosedForPopouts();
	}-*/;
	private native void closeWindow() /*-{
 		$wnd.close();
	}-*/;
	private native void setLocation(String url) /*-{
  		$wnd.setUrlToTopWindow(url);
	}-*/;
	private String getReturnUrl()
	{
		if(currentUser.isHost())
		{
			return ConferenceGlobals.userInfoDictionary.getStringValue("return_url");
		}else{
			return ConferenceGlobals.userInfoDictionary.getStringValue("return_url_in_setings");
		}
	}
	private native String getDefaultTrackbackURL() /*-{
		return ($wnd.trackback_url);
	}-*/;

	public void onServerConnectionLost(String message) {
		if (sessionClosed)
		{
			return;
		}
		consoleLayout.stopTimer();
//		closeBeforeReconnectAttempt();
		this.showReconnectDialog(message);
	}

	public void onServerSessionLost(String message) {
		if (sessionClosed)
		{
			return;
		}
//		closeBeforeReconnectAttempt();
		consoleLayout.stopTimer();
		this.closePublisher();
		this.closePopoutAndAudioPlayers();
		this.closeMyAudio();
		ClientModel.getClientModel().getClientStateModel().setConferenceInactive();
		this.showRejoinDialog();
		/*
		DefaultCommonDialog.showMessage("Warning", message, new PopupListener()
		{
			public	void	onPopupClosed(PopupPanel pp, boolean autoClosed)
			{
				closeConsole();
			}
		});
		*/
	}
	
	protected	boolean	assistentShown = false;

	public void activePresenterAVResponseReceived() {
		if (!assistentShown && UIGlobals.isActivePresenter(this.currentUser))
		{
			assistentShown = true;
			if (consoleLayout != null )
			{
				if(ConferenceGlobals.isAssistantEnabled()){
					if(null != consoleLayout.getMeetingAssistent())
					{
						consoleLayout.getMeetingAssistent().showMeetingAssistent();
					}
				}
			}
		}
		
	}

	public RosterModelAdapter getRosterModelListener() {
		return rosterModelListener;
	}

	public void setRosterModelListener(RosterModelAdapter rosterModelListener) {
		this.rosterModelListener = rosterModelListener;
	}

	public void userClickedOnSignout() {
		this.consoleLayout.getFullPanel().getTopPanel().topPanel.signout();
		
	}

	public void removeWindowListener() {
		Window.removeWindowCloseListener(this);
	}
	
	public ClickListener getRecordListener()
	{
		return workSpace.getRecordControlClickListener();
	}
	
	public ConsoleMiddleLeftPanel getLeftPanel() {
		return leftPanel;
	}
	public void onDisplayNameChanged() {
		//Window.alert("insdie display name changed..."+user.getDisplayName());
		consoleLayout.getFullPanel().getTopPanel().topPanel.onDisplayNameChanged();
	}
	
	public void onPartCountChanged(String count) {
			String temp = count.substring(1, count.length()-1);
		    //Window.alert("max part changed ... temp = "+temp);
		    //Window.alert("time changed ... timeInteger = "+Integer.parseInt(temp));
		    
		    int countInt = Integer.parseInt(temp);
		    ConferenceGlobals.setCurrentMaxParticipants(countInt);
		
	}
	
	public boolean isRecordingInProgress()
	{
		return this.workSpace.isRecordingInProgress();
	}

	public void stopRecording()
	{
//		this.workSpace.stopRecording();
	}

	private native int getScreenWidth() /*-{
		return screen.width;
	}-*/;
	private native int getScreenHeight() /*-{
		return screen.height;
	}-*/;
	
	private	void	closeBeforeReconnectAttempt()
	{
		if (!serverConnectionLost)
		{
			serverConnectionLost = true;
			closeConsole();
		}
	}
	public void onConferenceId(String conferenceId)
	{
		onServerSessionLost(conferenceId);
	}
	public void onMissingConferenceKey()
	{
		DebugPanel.getDebugPanel().addDebugMessage("Command with missing confKey");
	}
	public void onNoConferenceForKey()
	{
		this.onConferenceClosed();
	}
	private	void	showReconnectDialog(String message)
	{
//		final WindowCloseListener wcl = this;
		ConfirmationListener listener = new ConfirmationListener()
		{
			public void onCancel()
			{
				if (!serverConnectionLost)
				{
					serverConnectionLost = true;
//					closeMyAudio();
					closePublisher();
//					closePopoutAndAudioPlayers();
					ClientModel.getClientModel().getClientStateModel().setConferenceInactive();
					closeConsole();
				}
			}
			public void onOK()
			{
				consoleLayout.restartTimer();
			}
		};
		
		String message2 = ConferenceGlobals.getDisplayString("server_error.comment_4","Please click on Reconnect to enter the meeting room again.");
		ConfirmationDialog dlg = new ConfirmationDialog(
				"Warning",message+" "+message2,
				"default-message",listener);
		dlg.drawDialog();
		dlg.setButtonLabels(ConferenceGlobals.getDisplayString("reconnect.button.label","Reconnect"),
				ConferenceGlobals.getDisplayString("quit.button.label","Quit") );
	}
	private	void	showRejoinDialog()
	{
		final WindowCloseListener wcl = this;
		ConfirmationListener listener = new ConfirmationListener()
		{
			public void onCancel()
			{
				closeBeforeReconnectAttempt();
			}
			public void onOK()
			{
				Window.removeWindowCloseListener(wcl);
				if (currentUser.isHost())
				{
//					Window.alert("Attempting reconnect host. Essentially same as reload sequence for pub installer. Success or failure depends on the server availability.");
					String reloadConsoleUrl = "/"+ConferenceGlobals.webappName+
							"/ReloadConsole.action"+"?sessionKey="+ConferenceGlobals.sessionKey;
					setLocation(reloadConsoleUrl);
				}
				else
				{
//					Window.alert("Attempting reconnect. Essentially rejoin. Success or failure depends on the server availability.");
					String	rejoinURL = UIResources.getUIResources().getConferenceInfo("rejoinURL");
					setLocation(rejoinURL+"&displayName="+currentUser.getDisplayName());
				}
				//consoleLayout.restartTimer();
			}
		};
		
		String message2 = ConferenceGlobals.getDisplayString("server_error.comment_4","Please click on Reconnect to enter the meeting room again.");
		ConfirmationDialog dlg = new ConfirmationDialog(
				"Warning",message2,
				"default-message",listener);
		dlg.drawDialog();
		dlg.setButtonLabels(ConferenceGlobals.getDisplayString("reconnect.button.label","Reconnect"),
				ConferenceGlobals.getDisplayString("quit.button.label","Quit") );
	}
}
