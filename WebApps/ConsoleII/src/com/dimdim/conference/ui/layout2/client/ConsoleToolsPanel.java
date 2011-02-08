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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.ui.layout2.client;

/*
import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.common.client.util.CopyToClipBoard;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.dimdim.conference.ui.dialogues.client.FeedbackDialog;
import com.dimdim.conference.ui.dialogues.client.SettingsDialog;
import com.dimdim.conference.ui.dialogues.client.common.AboutCommonDialog;
import com.dimdim.conference.ui.dialogues.client.common.AboutConferenceHtml;
import com.dimdim.conference.ui.dialogues.client.common.MeetingInfoHtml;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModelAdapter;
import com.dimdim.conference.ui.model.client.UIResources;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HoverPopupPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
*/

public class ConsoleToolsPanel //extends Composite	implements ClickListener
{
/*
	protected final DockPanel topPanel = new DockPanel();

	protected final VerticalPanel links = new VerticalPanel();
	protected final HorizontalPanel linksLine1 = new HorizontalPanel();
	protected final HorizontalPanel linksLine2 = new HorizontalPanel();
	protected final HTML heading = new HTML("");
	
	private	String	aboutHeading = UIStrings.getAboutDialogHeader();
	protected	Label	nameLabel = null;
	protected	Label	settingsLabel = null;
	protected	Label	assistantLabel = null;
	protected	Label	meetingLabel = null;
	protected	Label	logoutLabel = null;
	protected	Label	aboutLabel = null;
	protected	Label	feedbackLabel = null;
	
	protected	MainLayout		console;
	protected	UIRosterEntry	currentUser;
	protected	RosterModelAdapter	rosterModelListener;
	protected	UserRosterManager userManager;
	
	private	Label	tools = new Label("Tools");
	
	private ConsoleToolsPanel(MainLayout console, UIRosterEntry currentUser)
	{
		this.console = console;
		this.currentUser = currentUser;
		userManager = new UserRosterManager(currentUser);
		this.initWidget(tools);
//		DOM.setAttribute(topPanel.getElement(), "id", "console-topPanel");
		topPanel.setStyleName("console-top-panel");
		
		topPanel.add(links, DockPanel.EAST);
		topPanel.setCellHorizontalAlignment(links, DockPanel.ALIGN_RIGHT);
		topPanel.setCellVerticalAlignment(links, HasVerticalAlignment.ALIGN_TOP);
		
		UIParams uiParams = UIParams.getUIParams();
		//String top_links_sign_out = uiParams.getStringParamValue("top_links_sign_out", "true");
		//Window.alert("top_links_sign_out = "+top_links_sign_out);
		//Window.alert("top_links_about = "+uiParams.getStringParamValue("top_links_about", "true"));
		
		HTML seperator1 = new HTML("|");
		seperator1.setStyleName("console-top-panel-seperator");
		HTML seperator2 = new HTML("|");
		seperator2.setStyleName("console-top-panel-seperator");
		HTML seperator3 = new HTML("|");
		seperator3.setStyleName("console-top-panel-seperator");
		HTML seperator4 = new HTML("|");
		seperator4.setStyleName("console-top-panel-seperator");
		HTML seperator5 = new HTML("|");
		seperator5.setStyleName("console-top-panel-seperator");
		HTML seperator6 = new HTML("|");
		seperator6.setStyleName("console-top-panel-seperator");
		
		//	Top links line1.
		String 	nm = this.currentUser.getDisplayName();
		nameLabel = new FixedLengthLabel(nm,25);
		if (nm.length() > 25)
		{
			nameLabel.setTitle(nm);
		}
		nameLabel.setWordWrap(false);
		nameLabel.setStyleName("common-text");
		nameLabel.addStyleName("common-label");
//		nameLabel.addClickListener(FlashCallbackHandler.getHandler());
//		nameLabel.addClickListener(new ClickListener()
//		{
//			public void onClick(Widget sender)
//			{
//				String reloadUrl = (new CommandURLFactory()).getReloadConsoleURL();
//				setLocation(reloadUrl);
//			}
//		});
//		DOM.setAttribute(nameLabel.getElement(),"id","flash_bridge");
		HoverPopupPanel.flashCallbackElement = nameLabel.getElement();
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_settings", "true")))
		{
			settingsLabel = new Label(UIStrings.getSettingsLabel());
			settingsLabel.setWordWrap(false);
			settingsLabel.setTitle(ConferenceGlobals.getTooltip("settings_link"));
			settingsLabel.addClickListener(this);
			settingsLabel.setStyleName("console-top-panel-link");
		}
		
		assistantLabel = new Label(UIGlobals.getAssistantSubTabLabel());
		assistantLabel.setWordWrap(false);
		assistantLabel.setTitle(ConferenceGlobals.getTooltip("assistant_link"));
		assistantLabel.addClickListener(this);
		assistantLabel.setStyleName("console-top-panel-link");
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_info", "true")))
		{
			meetingLabel = new Label(UIStrings.getMeetingInfoLabel());
			meetingLabel.setWordWrap(false);
			meetingLabel.setTitle(ConferenceGlobals.getTooltip("meetinginfo_link"));
			meetingLabel.addClickListener(this);
			meetingLabel.setStyleName("console-top-panel-link");
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_sign_out", "true")))
		{
			if (UIGlobals.isPresenter(this.currentUser))
			{
				logoutLabel = new Label(UIStrings.getPresenterSignOutLabel());
			}
			else
			{
				logoutLabel = new Label(UIStrings.getAttendeeSignOutLabel());
			}
		
			logoutLabel.setTitle(ConferenceGlobals.getTooltip("signout_link"));
			logoutLabel.setWordWrap(false);
			logoutLabel.addClickListener(this);
			logoutLabel.setStyleName("console-top-panel-link");
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_feedback", "true")))
		{
			feedbackLabel = new Label(UIStrings.getFeedbackLabel());
			feedbackLabel.setTitle(ConferenceGlobals.getTooltip("feedback_link"));
			feedbackLabel.addClickListener(this);
			feedbackLabel.setStyleName("console-top-panel-link");
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_about", "true")))
		{
			aboutLabel = new Label(UIStrings.getAboutLabel());
			aboutLabel.setTitle(ConferenceGlobals.getTooltip("about_link"));		
			aboutLabel.addClickListener(this);
			aboutLabel.setStyleName("console-top-panel-link");
		}
		
		linksLine1.setStyleName("console-top-links-line");
		linksLine1.add(nameLabel);
		linksLine1.setCellVerticalAlignment(nameLabel,VerticalPanel.ALIGN_MIDDLE);
		
		if (UIGlobals.isPresenter(this.currentUser))
		{
			if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_settings", "true")))
			{
				linksLine1.add(seperator1);
				linksLine1.setCellVerticalAlignment(seperator1,VerticalPanel.ALIGN_MIDDLE);
				linksLine1.add(settingsLabel);
				linksLine1.setCellVerticalAlignment(settingsLabel,VerticalPanel.ALIGN_MIDDLE);
			}
		}
		
		if (UIGlobals.isPresenter(this.currentUser) && ConferenceGlobals.isAssistantEnabled())
		{
			linksLine1.add(seperator6);
			linksLine1.setCellVerticalAlignment(seperator6,VerticalPanel.ALIGN_MIDDLE);
			linksLine1.add(assistantLabel);
			linksLine1.setCellVerticalAlignment(assistantLabel,VerticalPanel.ALIGN_MIDDLE);
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_info", "true")))
		{
			linksLine1.add(seperator2);
			linksLine1.setCellVerticalAlignment(seperator2,VerticalPanel.ALIGN_MIDDLE);
			linksLine1.add(meetingLabel);
			linksLine1.setCellVerticalAlignment(meetingLabel,VerticalPanel.ALIGN_MIDDLE);
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_feedback", "true")))
		{
			linksLine1.add(seperator3);
			linksLine1.setCellVerticalAlignment(seperator3,VerticalPanel.ALIGN_MIDDLE);
			linksLine1.add(feedbackLabel);
			linksLine1.setCellVerticalAlignment(feedbackLabel,VerticalPanel.ALIGN_MIDDLE);
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_about", "true")))
		{
			linksLine1.add(seperator4);
			linksLine1.setCellVerticalAlignment(seperator4,VerticalPanel.ALIGN_MIDDLE);
			linksLine1.add(aboutLabel);
			linksLine1.setCellVerticalAlignment(aboutLabel,VerticalPanel.ALIGN_MIDDLE);
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_sign_out", "true")))
		{
			linksLine1.add(seperator5);
			linksLine1.setCellVerticalAlignment(seperator4,VerticalPanel.ALIGN_MIDDLE);
			linksLine1.add(logoutLabel);
			linksLine1.setCellVerticalAlignment(logoutLabel,VerticalPanel.ALIGN_MIDDLE);
		}
		

//		if (UIGlobals.isOrganizer(this.currentUser))
//		{
//			if (LayoutGlobals.isWhiteboardSupported() && ConferenceGlobals.whiteboardEnabled)
//			{
//				String rtmpUrl = UIGlobals.getStreamingUrlsTable().getWhiteboardRtmpUrl();
//				String rtmptUrl = UIGlobals.getStreamingUrlsTable().getWhiteboardRtmptUrl();
//				
//				String fullMovieUrl = "swf/wb_2.swf"+"?"+ConferenceGlobals.getConferenceKey()+
//						"$"+rtmpUrl+"$"+rtmptUrl+"$2$2";
//				this.wbMovie = new DmFlashWidget2("WB2","WB2N",
//							"2px","2px",fullMovieUrl,"black");
//				linksLine1.add(wbMovie);
//				wbMovie.show();
//			}
//		}
		//	Top links line 2.
		//	linksLine1.setStyleName("console-top-links-line");

		
		links.setStyleName("console-top-links-panel");
		links.add(linksLine1);
		links.setCellHorizontalAlignment(linksLine1,HorizontalPanel.ALIGN_RIGHT);
		//links.add(linksLine2);
		//links.setCellHorizontalAlignment(linksLine2,HorizontalPanel.ALIGN_RIGHT);
		
		topPanel.add(heading, DockPanel.CENTER);
		topPanel.setVerticalAlignment(DockPanel.ALIGN_TOP);
		topPanel.setCellWidth(heading, "100%");
		
	}
	private native void setLocation(String url) /*-{
		$wnd.location = url;
	}-*--/;

	public void onClick(Widget sender)
	{
	 
		if (sender == this.aboutLabel)
		{
			AboutConferenceHtml ach = new AboutConferenceHtml();
			AboutCommonDialog dlg = new AboutCommonDialog(aboutHeading, ach, "about-meeting");
//			Window.alert("comes here 6");
			dlg.drawDialog();
//			Window.alert("comes here end");
		}
		else if (sender == this.meetingLabel)
		{
			AnalyticsConstants.reportMeetingInfoLaunched();
			MeetingInfoHtml mih = new MeetingInfoHtml();
			//Button copyButton = new Button("Copy URL");
			//copyButton.setTitle("Copy to ClipBoard");
			//copyButton.setStyleName("invitations-preview-send-button");
			//Vector vect = new Vector();
			//vect.add(copyButton);
			DefaultCommonDialog dlg = new DefaultCommonDialog(UIStrings.getMeetingInfoDialogHeader(),
					mih, "meeting-info");
			dlg.drawDialog();
			String	joinURL = UIResources.getUIResources().getConferenceInfo("joinURL");
			CopyToClipBoard copyListener = new CopyToClipBoard(joinURL);
		}
		else if (sender == this.feedbackLabel)
		{
			FeedbackDialog dlg = new FeedbackDialog(userManager);
			dlg.drawDialog();
		}
		else if (sender == this.settingsLabel)
		{
			SettingsDialog dlg = new SettingsDialog();
			dlg.drawDialog();
		}
		else if (sender == this.logoutLabel)
		{
			signout();
		}
	}

	public void signout() {
		final WindowCloseListener wcl = console.getFullPanel().getMiddlePanel();
		ConfirmationListener listener = new ConfirmationListener()
		{
			public void onCancel()
			{
			}
			public void onOK()
			{
				Window.removeWindowCloseListener(wcl);
				console.getFullPanel().getMiddlePanel().onSignout();
			}
		};
		String s = UIConstants.getConstants().attendeeDepartureWarning();
		if  (UIGlobals.isPresenter(this.currentUser))
		{
			s = UIConstants.getConstants().presenterDepartureWarning();
		}
		ConfirmationDialog dlg = new ConfirmationDialog(
				"Warning",s+" "+ConferenceGlobals.getDisplayString("top_bar.signout.press.yes", "Please press Yes to confirm."),
				"default-message",listener);
		dlg.drawDialog();
	}

	public void onWindowClosed() {
		//sessionClosed = true;
//		if (ClientModel.getClientModel().getClientStateModel().isConferenceActive())
//		{
			//try
			//{
				//closeAllMeetingActivity();
			//}
			//catch(Exception e)
			//{
				//Window.alert("Exception while closing popout.");
			//}
			ClientModel.getClientModel().leaveConference();
//		}
//		else
//		{
//		}
		
	}
	public Label getAssistantLabel() {
		return assistantLabel;
	}

	public void onDisplayNameChanged()
	{
		UIRosterEntry user = ClientModel.getClientModel().getRosterModel().getCurrentUser();
		//Window.alert("the name that is changed is "+user.getDisplayName());
		this.currentUser = user;
		String 	nm = this.currentUser.getDisplayName();
		Label temp = new FixedLengthLabel(nm,25);
		nameLabel.setText(temp.getText());
	}
	
*/
}
