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

package com.dimdim.conference.ui.layout2.client;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.LayoutGlobals;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.user.NewChatController;
import com.dimdim.conference.ui.common.client.util.CopyFlashListener;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.GetEventResponseTextHandler;
import com.dimdim.conference.ui.json.client.JSONurlReadingTimer;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.panels.client.LayoutController;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.EventsJsonHandler;
import com.dimdim.conference.ui.model.client.JSInterface;
import com.dimdim.ui.common.client.data.UIDataDictionaryManager;
import com.dimdim.ui.common.client.data.UIDataReadingProgressListener;
import com.dimdim.ui.common.client.json.ServerResponse;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MainLayout implements EntryPoint, UIDataReadingProgressListener, LayoutController
{
	
	private	JSONurlReadingTimer	eventsReadingTimer;
	private	ConsoleFullPanel fullPanel;
	private	GetEventResponseTextHandler gerth;
//	private String departureWarning;
	private UIRosterEntry	me;
	private	ResizeListener	resizeListener;
//	private	LoadingModuleMessage	lmm;
	
//	private	int	originalWidth = -1;
//	private	int	originalHeight = -1;
//	private	int	minimumWidth = -1;
//	private	int	minimumHeight = -1;

	MeetingAssistentDialog meetingAssistent = null;
	private	Label	backgroundMiddleFiller1 = new Label(" ");
	private	Label	backgroundMiddleFiller2 = new Label(" ");
	
	public void onModuleLoad()
	{
//		this.originalWidth = Window.getClientWidth();
//		this.minimumWidth = this.originalWidth;
//		if (this.minimumWidth > 1024)
//		{
//			this.minimumWidth = 1024;
//		}
//		this.originalHeight = Window.getClientHeight();
//		this.minimumHeight = this.originalHeight;
//		if (this.minimumHeight > 768)
//		{
//			this.minimumHeight = 768;
//		}
		ConferenceGlobals.setContentWidth( Window.getClientWidth() );
		ConferenceGlobals.setContentHeight( Window.getClientHeight() );
		
		ConferenceGlobals.conferenceKey = getConfKey();
//		lmm = new LoadingModuleMessage("Loading.....");
		//RootPanel.get("MainConsole").add(lmm);
//		lmm.setSize(Window.getClientWidth()+"px",Window.getClientHeight()+"px");
		UIDataDictionaryManager.initManager(getWebAppName(),getConfKey(),getUserType(),
//				new String[]{"dictionary","dictionary","dictionary","dictionary","dictionary"},
//				new String[]{"console","console","console","global_string","session_string"},
//				new String[]{"ui_strings","tooltips","default_layout","emoticons","user_info"+getDataCacheId()});
				new String[]{"combined"},
				new String[]{"session_string"},
				new String[]{"user_info"+getDataCacheId()});
		UIDataDictionaryManager.getManager().readDataDictionaries(this);
	}
	public void dataReadingComplete(ServerResponse data)
	{
//		Window.alert("dataReadingComplete");
		ConferenceGlobals.readDictionaries();
		ConferenceGlobals.init();
		loadConsole();
//	    DeferredCommand.add(new Command() {
//	        public void execute() {
//	        	loadConsole();
//	        }
//	      });
	}
	
	protected void loadConsole()
	{
		DebugPanel.getDebugPanel().addDebugMessage("Initializing Console");
		//	Now once the environment i initialized the client model can be
		//	created which depends on it.
		
//		String getEventsURL = ConferenceGlobals.webappRoot+
//			"GetEvents.action?sessionKey="+ConferenceGlobals.sessionKey;
		String getEventsURL = ConferenceGlobals.webappRoot+
			"GetEvents.action?sessionKey="+ConferenceGlobals.sessionKey;
		String serverPingURL = ConferenceGlobals.webappRoot+"PingServer.action";
		ClientModel.createClientModel();
		DebugPanel.getDebugPanel().addDebugMessage("Client model creation complete");
		
	    Window.setMargin("0px");

		try
		{
			UIRosterEntry currentUser = ClientModel.getClientModel().getRosterModel().getCurrentUser();
			fullPanel = new ConsoleFullPanel(this, currentUser);
			//RootPanel.get("MainConsole").remove(this.lmm);
			//RootPanel.get("MainConsole").add(fullPanel);
			
			//RootPanel.get("MainConsole").add(fullPanel.getTopPanel())
			//this.meetingAssistent = new MeetingAssistentDialog(this.workspaceClickListener);
		}
		catch(Exception e)
		{
			Window.alert(e.getMessage());
		}
		DebugPanel.getDebugPanel().addDebugMessage("Fetching initial data");
		ClientModel.getClientModel().getClientStateModel().addListener(this.fullPanel.getMiddlePanel());
		ClientModel.getClientModel().getRosterModel().addListener(this.fullPanel.getMiddlePanel().getRosterModelListener());
		
		UIParams uiParams = UIParams.getUIParams();
		EventsJsonHandler eventsHandler = EventsJsonHandler.getHandler();
		eventsHandler.setEventsTracker(DebugPanel.getDebugPanel());
		gerth = new GetEventResponseTextHandler(eventsHandler,
				uiParams.getRegularEventPollIntervalMillis(),uiParams.getMaxEventPollErrors());
		gerth.setServerPingURL(serverPingURL);
		eventsReadingTimer = new JSONurlReadingTimer(getEventsURL,getConfKey(),uiParams.getInitialEventPollDelayMillis(), gerth);
		gerth.setServerStatusListener(this.eventsReadingTimer);
		eventsReadingTimer.start();
		
		resizeListener = new ResizeListener();
		Window.addWindowResizeListener(resizeListener);
	    DeferredCommand.addCommand(new Command()
	      {
	        public void execute()
	        {
	          resizeListener.onWindowResized(Window.getClientWidth(), Window.getClientHeight());
	          continueLoading();
	        }
	      });
	    
		ClientModel.getClientModel().getRosterModel().reportConsoleLoaded();
		
//		ConsoleDataReader dataReader = new ConsoleDataReader(this);
//		dataReader.readConsoleData();
		DebugPanel.getDebugPanel().addDebugMessage("Console initialization complete.");
		
		JSInterface.addCallbackListener(CopyFlashListener.getListener());
//		showConsole();
		
//		linking meeting assistant dialog listener
//		meetingAssistent = new MeetingAssistentDialog(fullPanel.getMiddlePanel().getLeftPanel());
//		fullPanel.getTopPanel().getToolsPanel().getAssistantLabel().addClickListener(this);
	}
	
//	public void onClick(Widget sender)
//	{
//			AnalyticsConstants.reportAssistantLaunched();
//			//Window.alert("clicked on assistant..");
//			this.meetingAssistent.showMeetingAssistent();
//	}
	
	public void showConsole()
	{
		setIDVisibility("pre-loader", false);
		setIDVisibility("content_body", true);
	}
	
	//public	MeetingAssistentDialog	getMeetingAssistent()
	//{
	//	return	this.meetingAssistent;
	//}
	/**
	 * This would remove all child widgets and add the input widget
	 * @param id
	 * @param widget
	 */
	public void addWidgetToID(String id, Widget widget)
	{
		try
		{
			RootPanel.get(id).clear();
			RootPanel.get(id).add(widget);
		}
		catch(Exception e)
		{
//			Window.alert("error while adding widget "+e.getMessage());
		}
	}
	
	public void setIDVisibility(String id, boolean visibility)
	{
		try
		{
			if (RootPanel.get(id) != null)
			{
				if(visibility)
				{
					RootPanel.get(id).setStyleName("Show");
				}
				else
				{
					RootPanel.get(id).setStyleName("Hide");
				}
			}
		}
		catch(Exception e)
		{
//			Window.alert("error while adding widget "+e.getMessage());
		}
	}
	
	public	void	continueLoading()
	{
		RootPanel.get("background_middle_1").add(backgroundMiddleFiller1);
		RootPanel.get("background_middle_2").add(backgroundMiddleFiller2);
		
		gerth.setMaxConnectFailuresComment(ConferenceGlobals.getDisplayString("server_error.comment_1",""));
		gerth.setServerUnreachableComment(ConferenceGlobals.getDisplayString("server_error.comment_2",""));
		gerth.setWebappUnreachableComment(ConferenceGlobals.getDisplayString("server_error.comment_3",""));
		me = ClientModel.getClientModel().getRosterModel().getCurrentUser();
		
		if (UIGlobals.isInLobby(me))
		{
			DefaultCommonDialog.showMessage(
					ConferenceGlobals.getDisplayString("console.meetinglobbylabel","Waiting Area"),
					ConferenceGlobals.getDisplayString("lobby.waiting_comment","Waiting"));
			DefaultCommonDialog dcd = DefaultCommonDialog.getDialog();
			if (dcd != null)
			{
				dcd.resetCloseButtonText(ConferenceGlobals.getDisplayString("top_bar.signout.label","sign out"));
				dcd.addCloseClickListener(new ClickListener()
				{
					public void onClick(Widget sender)
					{
						signOutIfInLobby();
					}
				});
			}
		}
		EventsJsonHandler eventsHandler = EventsJsonHandler.getHandler();
		eventsHandler.setEventsTracker(DebugPanel.getDebugPanel());
//		showConsole();
	}
	
	
	private	void	signOutIfInLobby()
	{
		if (me != null && UIGlobals.isInLobby(me))
		{
			fullPanel.getMiddlePanel().closeConsole();
		}
	}
	private	void	doResize(int width, int height)
	{
		UIParams uiParams = UIParams.getUIParams();
		int windowMinimumWidth = uiParams.getBrowserParamIntValue("window_minimum_height", 0);
		int windowMinimumHeight = uiParams.getBrowserParamIntValue("window_minimum_height", 0);
		//Window.alert("onWindowResized window width:"+width+", height:"+height);
		if (width < windowMinimumWidth)
		{
			width = windowMinimumWidth;
		}
		if (height < windowMinimumHeight)
		{
			height = windowMinimumHeight;
		}
		int backgroundHeaderHeight = uiParams.getBrowserParamIntValue("background_header_height", 115);
		int backgroundFooterHeight = uiParams.getBrowserParamIntValue("background_footer_height", 115);
		int backgroundMiddleAllowance = uiParams.getBrowserParamIntValue("background_middle_allowance", 0);
//		int avBoxesVerticalAlignmentLimit = uiParams.getBrowserParamIntValue("av_boxes_vertical_alignment", 768);
//		boolean vertical = height>avBoxesVerticalAlignmentLimit;
//		LayoutGlobals.setAVFloatsLayout(vertical);
//		if (vertical)
//		{
//			NewChatController.getController().setMaxPrivateChats(3);
//		}
//		else
//		{
//			NewChatController.getController().setMaxPrivateChats(2);
//		}
		
		int fillerHeight = height-(backgroundHeaderHeight+backgroundFooterHeight+backgroundMiddleAllowance);
		int logoImageHeight = uiParams.getBrowserParamIntValue("logo_image_height", 0);
		int logoImagePosition = uiParams.getBrowserParamIntValue("logo_image_position", 0);
		int filler1Height = fillerHeight - (logoImageHeight+logoImagePosition);
		int filler2Height = logoImagePosition;
		backgroundMiddleFiller1.setHeight(filler1Height+"px");
		backgroundMiddleFiller2.setHeight(filler2Height+"px");
		
		DebugPanel.getDebugPanel().addDebugMessage("onWindowResized window width:"+width+", height:"+height);
		width = width - uiParams.getWindowWidthAllowance();
		height = height - uiParams.getWindowHeightAllowance();
		ConferenceGlobals.setContentWidth(width);
		ConferenceGlobals.setContentHeight(height);
		fullPanel.resizePanel(width, height);
	}
	
	public	void	stopTimer()
	{
		this.eventsReadingTimer.stop();
	}
	public	void	restartTimer()
	{
		this.eventsReadingTimer.reStart();
	}
	
	public void addPanelToLayout(String divId, Widget panel)
	{
		this.addWidgetToID(divId, panel);
	}
	public void removePanelFromLayout(String divId, Widget panel)
	{
		//	NOT SUPPORTED YET
	}
	public void showPanel(String divId)
	{
		this.setIDVisibility(divId, true);
		if (this.fullPanel != null)
		{
			this.fullPanel.divShown(divId);
		}
	}
	public void hidePanel(String divId)
	{
		this.setIDVisibility(divId, false);
		if (this.fullPanel != null)
		{
			this.fullPanel.divHidden(divId);
		}
	}
	
	private native String getWebAppName() /*-{
	 return ($wnd.web_app_name);
	}-*/;

	public	native boolean hasActiveXControl() /*-{
		return	($wnd.dimdimPublisherControl1 != null &&
				 $wnd.dimdimPublisherControl1.ConferenceKey != null);
	}-*/;
	private native String getConfKey() /*-{
	 return ($wnd.conf_key);
	}-*/;
	private native String getUserType() /*-{
	 return ($wnd.userType);
	}-*/;
	private native String getDataCacheId() /*-{
		return $wnd.data_cache_id;
	}-*/;
	public ConsoleFullPanel getFullPanel() {
		return fullPanel;
	}
	public MeetingAssistentDialog getMeetingAssistent()
	{
		return meetingAssistent;
	}
	
	class	ResizeListener	implements	WindowResizeListener
	{
		ResizeListener()
		{
		}
		public	void	onWindowResized(int w, int h)
		{
			ResizeCommand command = new ResizeCommand(w,h);
			DeferredCommand.addCommand(command);
		}
	}
	
	class	ResizeCommand	implements	Command
	{
		int	width;
		int	height;
		public	ResizeCommand(int w, int h)
		{
			this.width = w;
			this.height = h;
		}
		public	void	execute()
		{
			doResize(width,height);
		}
	}
}


