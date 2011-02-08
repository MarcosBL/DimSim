package com.dimdim.conference.ui.layout.client.widget;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.list.DefaultListModelListener;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.util.CopyToClipBoard;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.common.client.util.RunningClockWidget;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.layout.client.main.NewLayout;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewTopPanel  implements ClickListener{
//	protected HorizontalPanel	basePanel = new HorizontalPanel();
	UIRosterEntry currentUser = null;
	final String defaultImage = "images/dimdim-logo.png";
	NewLayout consoleLayout = null;
	RunningClockWidget meetingClock = null;
	ConsoleTopPanel topPanel = null;
	FocusPanel fp = null;
	Label notifications = null;
	
	protected	TasksManagementPanel	tmp = null;
	//protected	DisclosurePanel	pendingTasksDropDown = null;
	protected	RoundedPanel	pendingTasksDropDownRounded = null;
	NotificationsDialog nfDlg = null;
	protected   Timer toggle = null;
	
	public NewTopPanel(NewLayout consoleLayout, UIRosterEntry currentUser)
	{
//		this.initWidget(basePanel);
		this.currentUser = currentUser;
		this.consoleLayout = consoleLayout;
		String logo = ConferenceGlobals.userInfoDictionary.getStringValue("default_logo");
		//Window.alert("inside NewTopPanel logo url = "+logo);
		
		if(null == logo || logo.length() == 0)
		{
			logo = defaultImage;
		}
		Image logoImage = new Image(logo+"?cflag="+Random.nextInt());
		logoImage.setStyleName("console-logo-image");
		logoImage.addClickListener(DebugPanel.getDebugPanel());
		
		//basePanel.add(logoImage);
		consoleLayout.addWidgetToID("image", logoImage);
		
		Widget  topPanel = createLinksPanel();
		//basePanel.add(topPanel);
		consoleLayout.addWidgetToID("top_links", topPanel);
		//logoImage.setWidth("100%");
		//logoImage.setHeight("100%");
		
		meetingClock = new RunningClockWidget();
		consoleLayout.addWidgetToID("time_meeting", meetingClock);
		
		fp = new FocusPanel();
		notifications = new Label(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications"));
		fp.add(notifications);
		
		createTaskPanel(currentUser);

		consoleLayout.setIDVisibility("notifications_image", false);
		consoleLayout.setIDVisibility("notifications", false);
		if(UIGlobals.isPresenter(currentUser))
		{
			//add only if he is a presenter
			consoleLayout.addWidgetToID("notifications", fp);
			fp.addClickListener(this);
		}
		
		
		topPanel.setWidth("100%");
		topPanel.setHeight("100%");
		
		//basePanel.setCellHorizontalAlignment(logoImage, HorizontalPanel.ALIGN_LEFT);
		//basePanel.setCellVerticalAlignment(logoImage, VerticalPanel.ALIGN_TOP);
		
		//basePanel.setCellHorizontalAlignment(topPanel, HorizontalPanel.ALIGN_RIGHT);
		//basePanel.setCellVerticalAlignment(topPanel, VerticalPanel.ALIGN_TOP);
		
		
		
		//basePanel.setCellHeight(logoImage, "100%");
		//basePanel.setCellHeight(topPanel, "100%");
		//basePanel.setWidth("100%");
		//basePanel.setHeight("100%");
		//basePanel.setBorderWidth(1);
		//basePanel.setCellHorizontalAlignment(meetingClock, HorizontalPanel.ALIGN_RIGHT);
		//basePanel.setCellVerticalAlignment(meetingClock, VerticalPanel.ALIGN_BOTTOM);
		UIParams uiParams = UIParams.getUIParams();
		
		consoleLayout.addWidgetToID("meetingid", new Label(UIStrings.getMeetingIdLabel()));
		Label meetingLabel = new Label(ConferenceGlobals.conferenceKey);
		meetingLabel.setStyleName("common-anchor");
		String	joinURL = UIResources.getUIResources().getConferenceInfo("joinURL");
		CopyToClipBoard copyListener = new CopyToClipBoard(joinURL);
		meetingLabel.setTitle(ConferenceGlobals.getDisplayString("console.copy.url.tooltip","Click to copy Meeting Url"));
		meetingLabel.addClickListener(copyListener);
		consoleLayout.addWidgetToID("meetingid_txt", meetingLabel);
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_dialinfo", "true")))
		{
			addDialInInfo();
		}
	}

	private void createTaskPanel(final UIRosterEntry currentUser) {
		tmp = new TasksManagementPanel(currentUser);
		//pendingTasksDropDown = new DisclosurePanel();
		//pendingTasksDropDownRounded = new RoundedPanel(pendingTasksDropDown);
		
		//final DropDownHeaderPanel taskDropDownHeaderPanel = new DropDownHeaderPanel(UIStrings.getParticipantsLabel()+" (1)", 
		//		tmp.getListsBrowseControl(), pendingTasksDropDown);
		
		//pendingTasksDropDown.setHeader(taskDropDownHeaderPanel);
		//pendingTasksDropDown.setStyleName("tk-DropDownPanel");
		
		//taskDropDownHeaderPanel.setText(UIStrings.getPendingTasksLabel());
		
		//pendingTasksDropDown.addHeaderControl(tmp.getListsBrowseControl());
		
		//tmp.getListsBrowseControl().setContainerPanel(pendingTasksDropDown);
		//pendingTasksDropDown.add(tmp);
		//pendingTasksDropDown.setOpen(true);
		//pendingTasksDropDown.setOpen(false);
		//this.pendingTasksDropDownRounded.setVisible(false);
		pendingTasksDropDownRounded = new RoundedPanel(tmp);
		tmp.addListModelListenerToAllLists(new DefaultListModelListener()
			{
				public void listEntryAdded(ListEntry newEntry)
				{
					if (tmp.getNumberOfTasks() > 0)
					{
						//pendingTasksDropDownRounded.setVisible(true);
						//pendingTasksDropDown.setOpen(true);
						if(UIGlobals.isPresenter(currentUser))
						{
							consoleLayout.setIDVisibility("notifications", true);
							consoleLayout.setIDVisibility("notifications_image", true);
						}
						notifications.setText(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications")+" ("+tmp.getNumberOfTasks()+")");
						//notifications.addStyleName("blinking_link");
						startBlinker();
					}
				}
				public void listEntryRemoved(ListEntry removedEntry)
				{
					if (tmp.getNumberOfTasks() == 0)
					{
						//pendingTasksDropDown.setOpen(false);
						//pendingTasksDropDownRounded.setVisible(false);
						if(null != nfDlg)
						{
							nfDlg.hide();
						}
						consoleLayout.setIDVisibility("notifications", false);
						consoleLayout.setIDVisibility("notifications_image", false);
						notifications.setText(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications")+" ");
						//notifications.removeStyleName("blinking_link");
						stopBlinker();
					}
				}
			});
	}
	
	private String getSubString(String inputString, int numberOfChar)
	{
		String returnVal = "";
		if(inputString!= null && inputString.length() > numberOfChar)
		{
			returnVal = ConferenceGlobals.internToll.substring(0, numberOfChar-1);
		}else{
			returnVal = inputString;
		}
		return returnVal;
	}
	
	private void addDialInInfo() {
		
		//Window.alert("inside addDialInInfo ConferenceGlobals.dialInInfo = "+ConferenceGlobals.showPhoneInfo);
		if(ConferenceGlobals.showPhoneInfo )
		{
				consoleLayout.addWidgetToID("dailinfo", new Label(UIStrings.getTollInfoLabel()));
				consoleLayout.addWidgetToID("confid", new Label(UIStrings.getAttendePasscodeLabel()));		
			
				Label internToll = new Label(getSubString(ConferenceGlobals.internToll, 20));
				Label attPassCode = new Label(getSubString(ConferenceGlobals.attendeePasscode, 20));
				
				internToll.setTitle(ConferenceGlobals.internToll);
				attPassCode.setTitle(ConferenceGlobals.attendeePasscode);
				consoleLayout.addWidgetToID("dailinfo_txt", internToll);
				consoleLayout.addWidgetToID("confid_txt", attPassCode);
			
				if(ConferenceGlobals.internToll.length() > 0)
					consoleLayout.setIDVisibility("dailinfo_panel", true);
				else
					consoleLayout.setIDVisibility("dailinfo_panel", false);

				if(ConferenceGlobals.attendeePasscode.length() > 0)
					consoleLayout.setIDVisibility("passcode_panel", true);
				else
					consoleLayout.setIDVisibility("passcode_panel", false);
		}
		
	}

	private Widget createLinksPanel()
	{
		VerticalPanel horPanel = new VerticalPanel();
		
		topPanel = new ConsoleTopPanel(consoleLayout, currentUser);
		
		horPanel.add(topPanel);
		//horPanel.add(meetingClock);
		
		horPanel.setCellHorizontalAlignment(topPanel, HorizontalPanel.ALIGN_RIGHT);
		horPanel.setCellVerticalAlignment(topPanel, VerticalPanel.ALIGN_TOP);
		
		
		horPanel.setCellHeight(topPanel, "100%");
		//horPanel.setCellHeight(meetingClock, "100%");
		//horPanel.setBorderWidth(1);
		return horPanel;
	}
	public void resizePanel(int panelWidth, int panelHeight)
	{
//		this.setWidth(panelWidth+"px");
	}

	public void onClick(Widget sender) {
		if(sender == fp)
		{
			//Window.alert("Clicked on notifications..");
			//RoundedPanel rp = consoleLayout.getFullPanel().getMiddlePanel().getLeftPanel().getPendingTasksDropDownRounded();
			nfDlg = new NotificationsDialog(pendingTasksDropDownRounded);
			nfDlg.drawDialog();
			
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
	
	private	void	stopBlinker()
	{
		notifications.removeStyleName("host-chat-message");	
		if (this.toggle != null)
		{
			this.toggle.cancel();
			this.toggle = null;
		}
	}
	
	public ConsoleTopPanel getLinksPanel() {
		return topPanel;
	}
	
}
