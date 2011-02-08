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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

import com.google.gwt.user.client.ui.ClickListener;
//import com.google.gwt.user.client.ui.FocusListener;
//import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HoverPopupPanel;
import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.NonModalPopupPanel;
//import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Timer;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.data.UIParams;
//import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
//import com.dimdim.conference.ui.common.client.util.CopyToClipBoard;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.dimdim.conference.ui.common.client.util.LabelHoverStyler;
//import com.dimdim.conference.ui.dialogues.client.FeedbackDialog;
//import com.dimdim.conference.ui.dialogues.client.SettingsDialog;
//import com.dimdim.conference.ui.dialogues.client.common.AboutCommonDialog;
//import com.dimdim.conference.ui.dialogues.client.common.AboutConferenceHtml;
//import com.dimdim.conference.ui.dialogues.client.common.MeetingInfoHtml;
//import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
//import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
//import com.dimdim.conference.ui.model.client.UIResources;
import com.dimdim.conference.ui.user.client.UserRosterManager;

public class ToolsPopupPanel extends NonModalPopupPanel
			implements PopupListener
{
//	protected	FocusPanel	pane = new FocusPanel();
	protected	int			showTime = UIGlobals.getHoverInitialShowTime();
	protected	Timer		timer;
	protected	boolean		hasFocus	=	false;
    
	protected	String	aboutHeading = UIStrings.getAboutDialogHeader();
	protected	Label	nameLabel = null;
	protected	Label	settingsLabel = null;
	protected	Label	assistantLabel = null;
	protected	Label	meetingLabel = null;
	protected	Label	aboutLabel = null;
	protected	Label	feedbackLabel = null;
	
//	protected	HorizontalPanel	headerPanel	=	new	HorizontalPanel();
	protected	HorizontalPanel	contentPanel	=	new	HorizontalPanel();
	protected	VerticalPanel	toolsPanel	=	new VerticalPanel();
//	protected	HorizontalPanel	linksPanel	=	new	HorizontalPanel();
	
	protected 	boolean 		subMenuOpen = false;
	protected	UIRosterEntry	currentUser;
	protected	UserRosterManager userManager;
	
	protected	ResourceRoster resRoster = null;
	protected	ClickListener	shareClickListener = null;
	protected	LabelHoverStyler	labelHoverStyler = new LabelHoverStyler();
	
	protected	int			toolsCount=0;
	
	public ToolsPopupPanel(ResourceRoster resRoster,
				ClickListener shareClickListener, UIRosterEntry currentUser)
	{
		super(false);
		this.setStyleName("dm-hover-popup");
		this.addStyleName("tool-popup-panel");
		this.resRoster = resRoster;
		this.shareClickListener = shareClickListener;
		this.currentUser = currentUser;
		userManager = new UserRosterManager(currentUser);
		
//		pane.addMouseListener(this);
//		pane.addFocusListener(this);
		
		DockPanel outer = new DockPanel();
		outer.setStyleName("dm-hover-popup-body");
//		pane.add(outer);
		
//		headerPanel.setStyleName("dm-hover-popup-header");
//		outer.add(headerPanel,DockPanel.NORTH);
//		outer.setCellWidth(headerPanel,"100%");
		
		this.contentPanel.setStyleName("dm-hover-popup-content");
		outer.add(contentPanel,DockPanel.NORTH);
//		outer.setCellHeight(contentPanel,"100%");
		
		this.contentPanel.add(this.toolsPanel);
//		this.contentPanel.setCellHeight(this.toolsPanel, "100%");
//		this.contentPanel.setCellWidth(this.toolsPanel, "100%");
		
		this.writeToolsPanel();
//		this.linksPanel.setStyleName("dm-hover-popup-links-panel");
//		outer.add(linksPanel,DockPanel.SOUTH);
//		outer.setCellHeight(linksPanel,"100%");
		
//		this.add(pane);
		this.add(outer);
		this.addPopupListener(this);
	}
	public	void	popupVisible()
	{
		this.timer = new Timer()
		{
			public void run()
			{
				timer = null;
				hideHoverPopup();
			}
		};
		this.timer.schedule(this.showTime);
	}
//	public void onFocus(Widget sender)
//	{
//		onMouseEnter(sender);
//	}
//	public void onLostFocus(Widget sender)
//	{
//		onMouseLeave(sender);
//	}
//	public void onMouseDown(Widget sender, int x, int y)
//	{
//	}
//	public void onMouseEnter(Widget sender)
//	{
//		if (this.timer != null)
//		{
//			this.timer.cancel();
//			this.timer = null;
//		}
//		this.hasFocus = true;
//		this.showTime = UIGlobals.getHoverPostMouseOutShowTime();
//	}
	public void closePopup()
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
		if (!this.hasFocus)
		{
			timer = new Timer()
			{
				public void run()
				{
					timer = null;
					hideHoverPopup();
				}
			};
			timer.schedule(this.showTime);
		}
	}
//	public void onMouseLeave(Widget sender)
//	{
//		if(!subMenuOpen)
//		{
//			this.hasFocus = false;
//			closePopup();
//		}
//	}
//	public void onMouseMove(Widget sender, int x, int y)
//	{
//	}
//	public void onMouseUp(Widget sender, int x, int y)
//	{
//	}
//	public void onClick(Widget sender)
//	{
//		this.hide();
//		if (sender == this.aboutLabel)
//		{
//			AboutConferenceHtml ach = new AboutConferenceHtml();
//			AboutCommonDialog dlg = new AboutCommonDialog(aboutHeading, ach, "about-meeting");
//			dlg.drawDialog();
//		}
//		else if (sender == this.meetingLabel)
//		{
//			AnalyticsConstants.reportMeetingInfoLaunched();
//			MeetingInfoHtml mih = new MeetingInfoHtml();
//			DefaultCommonDialog dlg = new DefaultCommonDialog(UIStrings.getMeetingInfoDialogHeader(),
//					mih, "meeting-info");
//			dlg.drawDialog();
//			String	joinURL = UIResources.getUIResources().getConferenceInfo("joinURL");
//			CopyToClipBoard copyListener = new CopyToClipBoard(joinURL);
//		}
//		else if (sender == this.feedbackLabel)
//		{
//			FeedbackDialog dlg = new FeedbackDialog(userManager);
//			dlg.drawDialog();
//		}
//		else if (sender == this.settingsLabel)
//		{
//			SettingsDialog dlg = new SettingsDialog();
//			dlg.drawDialog();
//		}
//		else if (sender == this.assistantLabel)
//		{
//			MeetingAssistentDialog meetingAssistent =
//				new MeetingAssistentDialog(this.resRoster,this.shareClickListener);
//			AnalyticsConstants.reportAssistantLaunched();
//			meetingAssistent.showMeetingAssistent();
//		}
//	}
	public	void	onPopupClosed(PopupPanel popup, boolean autoClosed)
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
	}
	public int getShowTime()
	{
		return showTime;
	}
	public void setShowTime(int showTime)
	{
		this.showTime = showTime;
	}
	public	boolean	supportsRefresh()
	{
		return	false;
	}
	public	void	hideHoverPopup()
	{
		this.showTime = UIGlobals.getHoverInitialShowTime();
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
	}
	public	int		getToolsCount()
	{
		return	this.toolsCount;
	}
	private	void	writeToolsPanel()
	{
		ToolsClickListener tcl = new ToolsClickListener(this);
		toolsCount = 0;
		Label lastLabel = null;
		//	Top links line1.
		UIParams uiParams = UIParams.getUIParams();
		String 	nm = this.currentUser.getDisplayName();
		nameLabel = new FixedLengthLabel(nm,25);
		if (nm.length() > 25)
		{
			nameLabel.setTitle(nm);
		}
		nameLabel.setWordWrap(false);
		nameLabel.setStyleName("common-text");
		nameLabel.addStyleName("common-label");
		HoverPopupPanel.flashCallbackElement = nameLabel.getElement();
		if (this.currentUser.isHost())
		{
			if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_settings", "true")))
			{
				settingsLabel = new Label(UIStrings.getSettingsLabel());
				settingsLabel.setWordWrap(false);
				settingsLabel.setTitle(ConferenceGlobals.getTooltip("settings_link"));
				settingsLabel.addClickListener(tcl);
				settingsLabel.setStyleName("tool-entry");
				settingsLabel.addStyleName("settings-tool-entry");
				settingsLabel.addMouseListener(labelHoverStyler);
				
				toolsPanel.add(settingsLabel);
				toolsCount++;
				lastLabel = settingsLabel;
			}
		}
		
		if (this.currentUser.isActivePresenter() && ConferenceGlobals.isAssistantEnabled())
		{
			assistantLabel = new Label(UIGlobals.getAssistantSubTabLabel());
			assistantLabel.setWordWrap(false);
			assistantLabel.setTitle(ConferenceGlobals.getTooltip("assistant_link"));
			assistantLabel.addClickListener(tcl);
			assistantLabel.setStyleName("tool-entry");
			assistantLabel.addStyleName("assistant-tool-entry");
			assistantLabel.addMouseListener(labelHoverStyler);
			
			toolsPanel.add(assistantLabel);
			toolsCount++;
			lastLabel = assistantLabel;
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_info", "true")))
		{
			meetingLabel = new Label(UIStrings.getMeetingInfoLabel());
			meetingLabel.setWordWrap(false);
			meetingLabel.setTitle(ConferenceGlobals.getTooltip("meetinginfo_link"));
			meetingLabel.addClickListener(tcl);
			meetingLabel.setStyleName("tool-entry");
			meetingLabel.addStyleName("meeting-info-tool-entry");
			meetingLabel.addMouseListener(labelHoverStyler);
			
			toolsPanel.add(meetingLabel);
			toolsCount++;
			lastLabel = meetingLabel;
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_feedback", "true")))
		{
			feedbackLabel = new Label(UIStrings.getFeedbackLabel());
			feedbackLabel.setTitle(ConferenceGlobals.getTooltip("feedback_link"));
			feedbackLabel.addClickListener(tcl);
			feedbackLabel.setStyleName("tool-entry");
			feedbackLabel.addStyleName("feedback-tool-entry");
			feedbackLabel.addMouseListener(labelHoverStyler);
			
			toolsPanel.add(feedbackLabel);
			toolsCount++;
			lastLabel = feedbackLabel;
		}
		
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_about", "true")))
		{
			aboutLabel = new Label(UIStrings.getAboutLabel());
			aboutLabel.setTitle(ConferenceGlobals.getTooltip("about_link"));		
			aboutLabel.addClickListener(tcl);
			aboutLabel.setStyleName("tool-entry");
			aboutLabel.addStyleName("about-tool-entry");
			aboutLabel.addMouseListener(labelHoverStyler);
			
			toolsPanel.add(aboutLabel);
			toolsCount++;
			lastLabel = aboutLabel;
		}
		
		if (lastLabel != null)
		{
			lastLabel.addStyleName("last-tool-entry");
		}
	}
}
