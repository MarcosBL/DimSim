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

package com.dimdim.conference.ui.panels.client;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.util.ImageNTextWidget;
import com.dimdim.conference.ui.json.client.UIRecordingControlEvent;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ClientLayout;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RecordingModelListener;
import com.dimdim.conference.ui.model.client.ResourceSharingDisplay;
import com.dimdim.conference.ui.sharing.client.CollaborationAreaManager;
import com.dimdim.conference.ui.sharing.client.ResourceSharingPanel;
import com.dimdim.conference.ui.user.client.ActiveAudiosManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.dimdim.conference.ui.sharing.client.SharingStatusListener;
import com.dimdim.conference.ui.sharing.client.CollaborationWidgetDisplayPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This panel represents combination of the resource player, chat widget and related
 * ui controls. Functional support provided by this object is post and pre popout 
 * panels control, resize management on window resize and chat hide and show and control
 * links state management.
 */

public class WorkspacePanel implements ClickListener, ResourceSharingDisplay, ClientLayout, SharingStatusListener
{
	protected	UIRosterEntry		currentUser;
	
	protected	ImageNTextWidget showChatPanel = null;
	protected	ImageNTextWidget fullScreenLinkPanel = null;
	protected	ImageNTextWidget recordLinkPanel = null;
	
	protected	Label chatLink = null;
	
	protected	ChatWidget chatWidget = null;
	
	protected	boolean isChatDisplayed = true;
	
	protected	int	prevContainerWidth = -1;
	protected	int	prevContainerHeight = -1;
	
	private  int chatWidgetWidth = 0;

	private	ResourceSharingPanel resourcePlayer = null;
	private CollaborationAreaManager collaborationAreaManager = null;

	private LayoutController	layout;
	private	ResourcePlayerAndPublicChatPanelSet	panelSet1;
	private	WorkspacePopoutClickListener	fullScreenClickListener;
	private	RecordControlClickListener		recordControlClickListener;
	private	ResourceSharingController		rsc;
	private Label recordLink = null; 
	protected	Label	fullScreenLink = null;
	private CobrowseWidget cobWidget = null;
	private	boolean		inPopout = false;
	protected   Timer toggle = null;
	UIImages uiImages = null;
	
	public WorkspacePanel(UIRosterEntry currentUser,
			ResourceSharingPanel resourcePlayer, LayoutController layout,
			ResourceSharingController rsc, CollaborationAreaManager collaborationAreaManager)
	{
		this.currentUser = currentUser;
		this.resourcePlayer  = resourcePlayer;
		this.layout = layout;
		this.rsc = rsc;
		this.collaborationAreaManager = collaborationAreaManager;
		uiImages = UIImages.getImageBundle(UIImages.defaultSkin);
		chatWidgetWidth = UIParams.getUIParams().getBrowserParamIntValue("public_chat_panel_width", 0);
		fillContent();
		ConferenceGlobals.setClientLayout(this);
		resourcePlayer.setSharingStatusListener(this);
	}
	public	ResourceSharingController	getResourceSharingController()
	{
		return	this.rsc;
	}
	public ResourcePlayerAndPublicChatPanelSet getPanelSet1()
	{
		return panelSet1;
	}
	public	ChatWidget	getChatWidget()
	{
		return	this.chatWidget;
	}
	private void fillContent()
	{
		buildWorspacePanel();
		
		chatWidget = new ChatWidget(layout, this);
		panelSet1 = new ResourcePlayerAndPublicChatPanelSet(chatWidget, this.resourcePlayer, fullScreenLinkPanel);
		fullScreenClickListener = new WorkspacePopoutClickListener(panelSet1);
		//fullScreenClickListener.setLabel(fullScreenLink);
		//fullScreenClickListener.setImage(fullScreenLinkPanel.getImage());
		
		this.fullScreenLinkPanel.addClickListener(fullScreenClickListener);
		
		if(!ConferenceGlobals.publicChatEnabled)
		{
//			Window.alert("public chat disabled so removing the widget");
			chatWidget.removeChatWidget();
			hideChatLink();
		}
	}
	private void buildWorspacePanel()
	{
		recordLink = new Label(ConferenceGlobals.getDisplayString("workspace.recording.host.label","Record"));
		recordLink.setWordWrap(false);
		this.currentUser = ClientModel.getClientModel().getRosterModel().getCurrentUser();
		//Window.alert("current user = "+currentUser +" currentUser.isHost() = "+currentUser.isHost());

		recordControlClickListener = new RecordControlClickListener(this);
		recordLinkPanel = new ImageNTextWidget(recordLink, uiImages.getRecordOff(), recordControlClickListener, true);
		recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.recording.host.title","This meeting is at present not being recording"));
		recordLinkPanel.getImage().setTitle(ConferenceGlobals.getDisplayString("workspace.recording.host.icon.title","This meeting is at present not being recorded"));
		
		if (!this.currentUser.isHost())
		{
			recordLinkPanel.setText("");
			recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.att.title","This meeting is at present not being recording"));
			recordLinkPanel.getImage().setTitle("");
		}
		fullScreenLink = new Label(ConferenceGlobals.getDisplayString("workspace.fullscreen.label","Full Screen"));
		fullScreenLinkPanel = new ImageNTextWidget(fullScreenLink, uiImages.getFullScreen(), this, true);
		//fullScreenLinkPanel.add(fullScreenLink);
		fullScreenLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.fullscreen.title","Click to make it Full Screen"));
		//fullScreenLinkPanel.setWidth("100%");
		//fullScreenLinkPanel.addClickListener(this);
		
		chatLink = new Label(ConferenceGlobals.getDisplayString("workspace.show.chat.label","Show Chat"));
		showChatPanel = new ImageNTextWidget(chatLink, uiImages.getShowChat(), this, true);
		//showChatPanel.add(chatLink);
		showChatPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.show.chat.title","Click to show Chat"));
		//showChatPanel.setWidth("100%");
		//showChatPanel.addClickListener(this);
		
		//chatLink.addStyleName("inactive_link");
		
		Label nowSharing = new Label(UIGlobals.getNowSharingText());
		nowSharing.setStyleName("common-text");
		nowSharing.addStyleName("common-bold-text");
		
		layout.addPanelToLayout("now_sharing", nowSharing);
		
		/*if (ConferenceGlobals.cobEnabled)
		{
			cobWidget = new CobrowseWidget(rsc, currentUser );
			resourcePlayer.setCobWidget(cobWidget);
			layout.addPanelToLayout("co_browse", cobWidget);
		}*/

//		if(UIGlobals.isPresenter(currentUser))
//		{
			//layout.showPanel("record");
			//layout.showPanel("record_wrapper");
		if (ConferenceGlobals.recordingEnabled)
		{
			layout.addPanelToLayout("record_wrapper", recordLinkPanel);
		}
//		}
		
		if (ConferenceGlobals.fullscreenEnabled)
		{
			layout.addPanelToLayout("full_screen_wrapper", fullScreenLinkPanel);
		}
		
		layout.addPanelToLayout("show_chat_wrapper", showChatPanel);
		layout.addPanelToLayout("collab_area", resourcePlayer);
		
		RecordingModelListener rml = new RecordingModelListener()
		{
			public void onRecordingdStarted(UIRecordingControlEvent event)
			{
				//Window.alert("inside onRecordingdStarted");
				recordLinkPanel.setImage(uiImages.getRecord());
				if (currentUser.isHost())
				{
					//	On the host console, all the work is already done by the click listener
					//	the event is only for the purpose of the popout. In popout set the link
					//	to look like on the attendee side, because the controls cannot be handled
					//	on the popout window.
					if (inPopout)
					{
						recordLink.setText(ConferenceGlobals.getDisplayString("workspace.recording.att.label","Stopped"));
						recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.recording.att.title","This meeting is now being recorded"));
					}
					else
					{
						recordLink.setText(ConferenceGlobals.getDisplayString("workspace.norecording.host.label","Stop"));
						recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.host.title","Click here to stop recording."));
						recordLinkPanel.getImage().setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.host.icon.title","This meeting is now being recorded"));
					}
					return;
				}
				else
				{
					checkDTPShare(true);
					recordLink.setText(ConferenceGlobals.getDisplayString("workspace.recording.att.label","Stopped"));
					recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.recording.att.title","This meeting is now being recorded"));
				}
				if (!inPopout)
				{
					ActiveAudiosManager.getManager(currentUser).restartAsRecord();
				}
				recordLinkPanel.removeStyleName("inactive_link");
				recordLinkPanel.addStyleName("active_link");
			}
			public void onRecordingStopped(UIRecordingControlEvent event)
			{
				//Window.alert("inside onRecordingStopped");
				recordLinkPanel.setImage(uiImages.getRecordOff());
				if (currentUser.isHost())
				{
					if (inPopout)
					{
						recordLink.setText("");
						recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.att.title","This meeting is at present not being recording"));
					}
					else
					{
						recordLink.setText(ConferenceGlobals.getDisplayString("workspace.recording.host.label","Record"));
						recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.recording.host.title","Click here to start recording."));
						recordLinkPanel.getImage().setTitle(ConferenceGlobals.getDisplayString("workspace.recording.host.icon.title","This meeting is at present not being recorded"));
					}
					return;
				}
				else
				{
					checkDTPShare(false);
//					recordLink.setText(ConferenceGlobals.getDisplayString("workspace.norecording.att.label","Stop"));
					recordLink.setText("");
					recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.att.title","This meeting is at present not being recording"));
				}
				if (!inPopout)
				{
					ActiveAudiosManager.getManager(currentUser).restartAsLive();
				}
				recordLinkPanel.addStyleName("inactive_link");
				recordLinkPanel.removeStyleName("active_link");
			}
		};
		ClientModel.getClientModel().getRecordingModel().addListener(rml);
		
	}
	private	void	checkDTPShare(boolean recordingOn)
	{
		if (UIGlobals.isActivePresenter(this.currentUser))
		{
			ResourceSharingController rsc = this.getResourceSharingController();
			if (recordingOn)
			{
				if (rsc.isDTPSharingActive())
				{
					rsc.restartDTPwithRecord(null);
				}
			}
			else
			{
				if (rsc.isDTPSharingActive())
				{
					rsc.restartDTPwithoutRecord(null);
				}
			}
		}
	}
	public void onSharingStarted(UIResourceObject resource)
	{
		this.fullScreenClickListener.closePopout();
		this.collaborationAreaManager.onSharingStarted(resource);
	}
	public void onSharingStopped(UIResourceObject resource)
	{
		this.collaborationAreaManager.onSharingStopped(resource);
		if(null != cobWidget)
		{
			cobWidget.setLocked(false);
		}
			
	}
	public	void	closePopout()
	{
		this.fullScreenClickListener.closePopout();
	}
	public void changeRecordingStatus(boolean isRecording )
	{
		if(isRecording)
		{
			//Window.alert("inside change recording status isRecording = "+isRecording);
			if (this.currentUser.isHost())
			{
				recordLink.setText(ConferenceGlobals.getDisplayString("workspace.norecording.host.label","Stop"));
				recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.host.title","Click here to stop recording."));
				recordLinkPanel.getImage().setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.host.icon.title","This meeting is now being recorded"));
			}else{
				recordLink.setText(ConferenceGlobals.getDisplayString("workspace.recording.att.label","Stopped"));
				recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.recording.att.title","This meeting is now being recorded"));
			}
			recordLinkPanel.setImage(uiImages.getRecord());
			recordLinkPanel.removeStyleName("inactive_link");
			recordLinkPanel.addStyleName("active_link");
		}
		else
		{
			//Window.alert("inside change recording status isRecording = "+isRecording);
			if (this.currentUser.isHost())
			{
				recordLink.setText(ConferenceGlobals.getDisplayString("workspace.recording.host.label","Record"));
				recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.recording.host.title","Click here to start recording."));
				recordLinkPanel.getImage().setTitle(ConferenceGlobals.getDisplayString("workspace.recording.host.icon.title","This meeting is at present not being recorded"));
			}else{
				recordLink.setText(ConferenceGlobals.getDisplayString("workspace.norecording.att.label","Stop"));
				recordLinkPanel.setTitle(ConferenceGlobals.getDisplayString("workspace.norecording.att.title","This meeting is at present not being recording"));
			}
			recordLinkPanel.setImage(uiImages.getRecordOff());
			recordLinkPanel.removeStyleName("active_link");
			recordLinkPanel.addStyleName("inactive_link");
		}
		//Window.alert("inside change recording status");
	}
	
	public boolean isInPopout()
	{
		return inPopout;
	}
	public void setInPopout(boolean inPopout)
	{
		this.inPopout = inPopout;
		if (this.inPopout)
		{
			this.fullScreenLinkPanel.removeClickListener(this.fullScreenClickListener);
			this.fullScreenLinkPanel.addClickListener(this);
		}
		if (this.recordControlClickListener != null)
		{
			this.recordControlClickListener.setInPopout(inPopout);
		}
	}
	public void onClick(Widget sender)
	{
		if(showChatPanel.getLabel() == sender || showChatPanel.getImage() == sender)
		{
			if(null != chatWidget)
			{
				chatWidget.addChatWidget();
			}
		}
		else if (this.fullScreenLinkPanel.getLabel() == sender || this.fullScreenLinkPanel.getImage() == sender)
		{
			if (this.inPopout)
			{
				AnalyticsConstants.reportFullScreenStopped();
				closeWindow();
			}
		}
	}
	private native void closeWindow() /*-{
		$wnd.close();
	}-*/;
	public	void	divShown(String divId)
	{
		if (divId.equals("show_chat_wrapper"))
		{
			if (this.isChatDisplayed)
			{
				showChatLink();
			}
		}
	}
	private void showChatLink()
	{
		if(null != chatLink)
		{
			isChatDisplayed = false;
			showChatPanel.setVisible(true);
			layout.showPanel("show_chat_wrapper");
			this.containerResized();
		}
	}
	public	void	divHidden(String divId)
	{
		if (divId.equals("show_chat_wrapper"))
		{
			if (!this.isChatDisplayed)
			{
				hideChatLink();
			}
		}
	}
	private void hideChatLink()
	{
		if (null != chatLink)
		{
			isChatDisplayed = true;
			showChatPanel.setVisible(false);
			layout.hidePanel("show_chat_wrapper");
			this.containerResized();
		}
	}
	public UIRosterEntry getCurrentUser()
	{
		return currentUser;
	}
	public	void	containerResized()
	{
		this.containerResized(this.prevContainerWidth, this.prevContainerHeight, false);
	}
	public void containerResized(int containerWidth, int containerHeight)
	{
		this.containerResized(containerWidth, containerHeight, false);
	}
	public void containerResized(int containerWidth, int containerHeight, boolean collapseChat)
	{
		UIParams uiParams = UIParams.getUIParams();
		this.prevContainerWidth = containerWidth;
		this.prevContainerHeight = containerHeight;
		
		int	panelWidth = containerWidth;
		int	panelHeight = containerHeight;
		
		//	Width correction for the left column and height correction for the styles.
		if (this.inPopout)
		{
			int popoutHeightAllowance = uiParams.getBrowserParamIntValue("popout_height_allowance", 0);
			panelHeight = panelHeight - popoutHeightAllowance;
			
			int marginWidthAllowance = uiParams.getBrowserParamIntValue("popout_width_allowance", 0);
			panelWidth = panelWidth - (marginWidthAllowance);
		}
		else
		{
			//	In console.
			int leftPanelWidth = uiParams.getBrowserParamIntValue("left_panel_width", 200);
			int marginWidthAllowance = uiParams.getBrowserParamIntValue("margin_width_allowance", 0);
			panelWidth = panelWidth - (leftPanelWidth+marginWidthAllowance);
			
			int topPanelHeight = uiParams.getBrowserParamIntValue("console_top_panel_height", 0);
			int marginHeightAllowance = uiParams.getBrowserParamIntValue("margin_height_allowance", 0);
			panelHeight = panelHeight - (topPanelHeight+marginHeightAllowance);
		}
		
		if(isChatDisplayed && ConferenceGlobals.publicChatEnabled)
		{
			panelWidth = panelWidth - chatWidgetWidth;
		}
		
//		DebugPanel.getDebugPanel().addDebugMessage("new middle panel window width:"+panelWidth+", height:"+panelHeight +", workSpace.isChatDisplayed ="+workSpace.isChatDisplayed);
		panelResized(panelWidth, panelHeight);
		if (collapseChat)
		{
			this.collapseChatIfRequired(this.resourcePlayer.getActiveResource());
		}
	}
	private void panelResized(int width, int height)
	{
		if(width < 0 || height < 0)
		{
			return;
		}
//		int overAllCollabWidthDeduction = 50;
//		if((width-(overAllCollabWidthDeduction)) > 0 && (height-60) > 0)
//		{
//		resourcePlayer.resizePanel((width - overAllCollabWidthDeduction), (height-60));
		resourcePlayer.resizePanel(width, height);
//		}
		if(null != chatWidget)
		{
//			if((height-40) > 0)
//			{
				chatWidget.panelResized(chatWidgetWidth, height);
//			}
		}
	}
	public RecordControlClickListener getRecordControlClickListener()
	{
		return recordControlClickListener;
	}
	
	public	void	startBlinker(final String styleName)
	{	
		
		
//		Window.alert("Check Image first time : " + checkImageFirstTime + " checkMinimized : " + checkMinimizedMessageAgain);
	
		if (this.toggle == null && !isChatDisplayed)
		{	
			this.toggle = new Timer() 
			{
				boolean unread = true;				
				
				public void run() 
				{					
					if(unread)
					{
						chatLink.addStyleName("host-chat-message");
						unread = false;
					}
					else
					{
						chatLink.removeStyleName("host-chat-message");
						unread = true;
					}										
				}				
			};			
		    this.toggle.scheduleRepeating(1000);	  		    
		}		
	}
	
	public	void	stopBlinker()
	{
		chatLink.removeStyleName("host-chat-message");	
		if (this.toggle != null)
		{
			this.toggle.cancel();
			this.toggle = null;
		}
	}
	public	boolean	isRecordingInProgress()
	{
		return	this.recordControlClickListener.isRecordingInProgress();
	}
//	public void stopRecording()
//	{
//		this.recordControlClickListener.stopRecording();
//	}
	public ImageNTextWidget getFullScreenLink() {
		return fullScreenLinkPanel;
	}
	
	public void setFullScreenLink(Label fullScreenLink) {
		this.fullScreenLink = fullScreenLink;
	}
	public	void	roleChanged(boolean activePresenter)
	{
		this.collaborationAreaManager.roleChanged(activePresenter);
		if(null != cobWidget)
		{
			this.cobWidget.roleChanged(activePresenter);
		}
	}
	public void syncCobUrl() {
		//Window.alert("inside workspace sync cob url");
		this.resourcePlayer.syncCobUrl();
		
	}
	
	public void navigateTo(String state) {
		//Window.alert("inside workspace sync cob url");
		this.resourcePlayer.navigateTo(state);
		
	}
	
	public void lock(boolean lock) {
		//Window.alert("inside workspace sync cob url");
		this.resourcePlayer.lock(lock);
		
	}
	
	public void writeCobResName(String url) {
		this.resourcePlayer.writeCobResName(url);
		
	}
	public void resourceSharingStarted(UIResourceObject res)
	{
		collapseChatIfRequired(res);
	}
	private void collapseChatIfRequired(UIResourceObject res)
	{
		if (res != null)
		{
			if (ConferenceGlobals.publicChatEnabled && this.isChatDisplayed)
			{
				boolean	collapseChat = true;
				String resType = res.getResourceType();
				if (resType.equals(UIResourceObject.RESOURCE_TYPE_PRESENTATION) ||
						resType.equals(UIResourceObject.RESOURCE_TYPE_WHITEBOARD))
				{
					//	If its the ppt or wb movie, then the aspect ratio must be considered.
					//	Collapse the chat panel only if that is going to give additional height.
					//	If the movie height is not significantly shorter than the panel height
					//	then there is height to spare, collapse the chat panel.
					CollaborationWidgetDisplayPanel panel = this.resourcePlayer.getMoviePanel();
					if (panel != null)
					{
						if ((panel.getLastKnownHeight() - panel.getMovieHeight()) > 10)
						{
							collapseChat = true;
						}
						else
						{
							collapseChat = false;
						}
					}
				}
				if (collapseChat)
				{
					this.chatWidget.removeChatWidget();
				}
			}
		}
	}
	public void resourceSharingStopped()
	{
		if (ConferenceGlobals.publicChatEnabled && !this.isChatDisplayed)
		{
			this.chatWidget.addChatWidget();
		}
	}

}

