package com.dimdim.conference.ui.layout2.client;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.list.DefaultListModelListener;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.util.CopyToClipBoard;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.common.client.util.DmGlassPanel2;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.dimdim.conference.ui.common.client.util.RunningClockWidget;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewTopPanel  implements ClickListener
{
	protected	UIRosterEntry currentUser = null;
	final String defaultImage = "images/dimdim-logo.png";
	protected	MainLayout 			consoleLayout = null;
	protected	RunningClockWidget	meetingClock = null;
//	protected	ConsoleTopPanel		topPanel = null;
	
	protected	Label				logoTextLabel;
//	protected	ConsoleToolsPanel	toolsPanel;
	protected	ConsoleSignOutPanel	signOutPanel;
	
	protected	FocusPanel		fp = null;
	protected	Label			notifications = null;
	protected	Label			tools = null;
	protected	Image			downArrow;
	
	protected	TasksManagementPanel	tmp = null;
	protected	RoundedPanel			pendingTasksDropDownRounded = null;
	protected	NotificationsDialog		nfDlg = null;
	
	protected	ResourceRoster	resRoster = null;
	protected	ClickListener	shareClickListener = null;
	
	protected   Timer toggle = null;
	
	public NewTopPanel(MainLayout consoleLayout, UIRosterEntry currentUser)
	{
		this.currentUser = currentUser;
		this.consoleLayout = consoleLayout;
		String logo = ConferenceGlobals.userInfoDictionary.getStringValue("default_logo");
		String logoText = ConferenceGlobals.userInfoDictionary.getStringValue("default_logo_text");
		
		if(null == logo || logo.length() == 0)
		{
			logo = defaultImage;
		}
		Image logoImage = new Image(logo+"?cflag="+Random.nextInt());
		logoImage.setStyleName("console-logo-image");
		logoImage.addClickListener(DebugPanel.getDebugPanel());
		consoleLayout.addWidgetToID("logo-image", logoImage);
		
		logoTextLabel = new Label(UIRosterEntry.decodeBase64(logoText));
		logoTextLabel.addClickListener(DebugPanel.getDebugPanel());
		consoleLayout.addPanelToLayout("logo_text", logoTextLabel);
		
		tools = new Label("Tools");
		tools.addStyleName("tools-button-label");
		tools.addClickListener(this);
		downArrow = this.getDownArrowImage();
		downArrow.addClickListener(this);
		HorizontalPanel h1 = new HorizontalPanel();
		h1.add(tools);
		h1.setCellVerticalAlignment(tools, VerticalPanel.ALIGN_MIDDLE);
		h1.add(downArrow);
		h1.setCellVerticalAlignment(downArrow, VerticalPanel.ALIGN_MIDDLE);
		h1.addStyleName("tools-button");
		consoleLayout.addWidgetToID("tools_button", h1);
//		toolsPanel = new ConsoleToolsPanel(consoleLayout,currentUser);
//		consoleLayout.addWidgetToID("tools_button", toolsPanel);
		
		meetingClock = new RunningClockWidget();
		consoleLayout.addWidgetToID("time_meeting", meetingClock);
		
		fp = new FocusPanel();
		notifications = new Label(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications"));
		fp.add(notifications);
		
		createTaskPanel(currentUser);

		consoleLayout.setIDVisibility("notifications_image", false);
		consoleLayout.setIDVisibility("notifications", false);
		if(currentUser.isHost())
		{
			//add only if he is a presenter
			consoleLayout.addWidgetToID("notifications", fp);
			fp.addClickListener(this);
		}
		
		signOutPanel = new ConsoleSignOutPanel(consoleLayout,currentUser);
		consoleLayout.addWidgetToID("signout_link", signOutPanel);
		
//		topPanel.setWidth("100%");
//		topPanel.setHeight("100%");
		
		UIParams uiParams = UIParams.getUIParams();
		
		Label meetingIdLabel = new Label(UIStrings.getMeetingIdLabel());
		consoleLayout.addWidgetToID("meetingid_label", meetingIdLabel);
//		meetingIdLabel.addStyleName("meetingid-label");
		Label meetingLabel = new FixedLengthLabel(ConferenceGlobals.conferenceKey,20);
		meetingLabel.addStyleName("common-anchor");
		meetingLabel.addStyleName("console-top-panel-link");
		String	joinURL = UIResources.getUIResources().getConferenceInfo("joinURL");
		CopyToClipBoard copyListener = new CopyToClipBoard(joinURL);
		meetingLabel.setTitle(ConferenceGlobals.getDisplayString("console.copy.url.tooltip","Click to copy Meeting Url"));
		meetingLabel.addClickListener(copyListener);
		consoleLayout.addWidgetToID("meetingid_text", meetingLabel);
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_dialinfo", "true")))
		{
			addDialInInfo();
		}
		else
		{
			hideDialInInfo();
		}
	}

	private void createTaskPanel(final UIRosterEntry currentUser)
	{
		tmp = new TasksManagementPanel(currentUser);
		pendingTasksDropDownRounded = new RoundedPanel(tmp);
		tmp.addListModelListenerToAllLists(new DefaultListModelListener()
			{
				public void listEntryAdded(ListEntry newEntry)
				{
					if (tmp.getNumberOfTasks() > 0)
					{
						if(currentUser.isHost())
						{
							consoleLayout.setIDVisibility("notifications", true);
							consoleLayout.setIDVisibility("notifications_image", true);
						}
						notifications.setText(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications")+" ("+tmp.getNumberOfTasks()+")");
						startBlinker();
					}
				}
				public void listEntryRemoved(ListEntry removedEntry)
				{
					if (tmp.getNumberOfTasks() == 0)
					{
						if(null != nfDlg)
						{
							nfDlg.hide();
						}
						consoleLayout.setIDVisibility("notifications", false);
						consoleLayout.setIDVisibility("notifications_image", false);
						notifications.setText(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications")+" ");
						stopBlinker();
					}
					else if (tmp.getNumberOfTasks() > 0)
					{
						notifications.setText(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications")+" ("+tmp.getNumberOfTasks()+")");
					}
				}
			});
	}
	private void addDialInInfo()
	{
		if(ConferenceGlobals.showPhoneInfo )
		{
			if(ConferenceGlobals.internToll.length() > 0 && ConferenceGlobals.attendeePasscode.length() > 0)
			{
				consoleLayout.addWidgetToID("phone_number_label", new Label(UIStrings.getTollInfoLabel()));
				consoleLayout.addWidgetToID("passcode_label", new Label(UIStrings.getAttendePasscodeLabel()));		
				consoleLayout.addWidgetToID("phone_number_text", new Label(ConferenceGlobals.internToll));
				consoleLayout.addWidgetToID("passcode_text", new Label(ConferenceGlobals.attendeePasscode) );
			}
			else
			{
				hideDialInInfo();
			}
		}
	}
	private	void hideDialInInfo()
	{
		consoleLayout.setIDVisibility("phone_number_block", false);
		
		//	In some of the browsers, if the container visibility is set to false
		//	some of the inside elements still show up. Not sure why. This is
		//	simply to account for that possibility.
		
		consoleLayout.setIDVisibility("phone_number_seperator", false);
		consoleLayout.setIDVisibility("phone_number_seperator", false);
		consoleLayout.setIDVisibility("phone_number_text", false);
		consoleLayout.setIDVisibility("passcode_label", false);
		consoleLayout.setIDVisibility("passcode_seperator", false);
		consoleLayout.setIDVisibility("passcode_text", false);
	}
//	private Widget createToolsPanel()
//	{
//		VerticalPanel horPanel = new VerticalPanel();
//		
//		toolsPanel = new ConsoleToolsPanel(consoleLayout, currentUser);
//		horPanel.add(toolsPanel);
//		horPanel.setCellHorizontalAlignment(toolsPanel, HorizontalPanel.ALIGN_RIGHT);
//		horPanel.setCellVerticalAlignment(toolsPanel, VerticalPanel.ALIGN_TOP);
//		horPanel.setCellHeight(toolsPanel, "100%");
//		
//		return horPanel;
//	}
	public void resizePanel(int panelWidth, int panelHeight)
	{
//		this.setWidth(panelWidth+"px");
	}

	public void onClick(Widget sender)
	{
		if(sender == fp)
		{
			nfDlg = new NotificationsDialog(pendingTasksDropDownRounded);
			nfDlg.drawDialog();
		}
		else if (sender == tools || sender == downArrow)
		{
			//	Create the tools drop down panel and show.
			int left = downArrow.getAbsoluteLeft()+5;
			int top = downArrow.getAbsoluteTop()+10;
			ToolsPopupPanel tpp = new ToolsPopupPanel(this.resRoster,this.shareClickListener,this.currentUser);
			DmGlassPanel2 dgp = new DmGlassPanel2(tpp);
			dgp.show(left, top);
			tpp.popupVisible();
		}
	}
	private	void	startBlinker()
	{	
		if (this.toggle == null )
		{	
			this.toggle = new Timer() 
			{
				boolean unread = true;				
				
				public void run() 
				{					
					if(unread)
					{
						notifications.addStyleName("host-chat-message");
						unread = false;
					}
					else
					{
						notifications.removeStyleName("host-chat-message");
						unread = true;
					}										
				}				
			};			
		    this.toggle.scheduleRepeating(1000);	  		    
		}
	}
	public Image getDownArrowImage()
	{
		return UIImages.getImageBundle(UIImages.defaultSkin).getHideChat();
	}
	private	void	stopBlinker()
	{
		notifications.removeStyleName("host-chat-message");	
		if (this.toggle != null)
		{
			this.toggle.cancel();
			this.toggle = null;
		}
	}
	public ConsoleSignOutPanel getSignOutPanel()
	{
		return signOutPanel;
	}
	public void setResRoster(ResourceRoster resRoster)
	{
		this.resRoster = resRoster;
	}
	public void setShareClickListener(ClickListener shareClickListener)
	{
		this.shareClickListener = shareClickListener;
	}
}
