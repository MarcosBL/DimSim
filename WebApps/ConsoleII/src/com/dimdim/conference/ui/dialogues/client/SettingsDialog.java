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

package com.dimdim.conference.ui.dialogues.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.SettingsModel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SettingsDialog extends CommonModalDialog implements ClickListener
{
	protected	VerticalPanel	basePanel	=	null;
	protected	Button			sendButton = null;
	protected	HTML			settingsMainComment = new HTML(UIGlobals.getSettingsMainComment());
	
	protected	HTML			meetingLobbyComment = new HTML(UIGlobals.getMeetingLobbySettingComment());
	protected	RadioButton		enableLobby = new RadioButton("Lobby");
	protected	Label			enableLobbyButtonTag = new Label(UIGlobals.getEnableLobbyButtonTagText());
	protected	RadioButton		disableLobby = new RadioButton("Lobby");
	protected	Label			disableLobbyButtonTag = new Label(UIGlobals.getDisableLobbyButtonTagText());
	
	protected	HTML			networkProfileComment = new HTML(UIGlobals.getNetworkProfileSettingComment());

	protected	RadioButton		networkProfile1 = new RadioButton("NetworkProfile");
	protected	Label			networkProfile1ButtonTag = new Label(UIGlobals.getNetworkProfile1ButtonTagText());
	protected	RadioButton		networkProfile2 = new RadioButton("NetworkProfile");
	protected	Label			networkProfile2ButtonTag = new Label(UIGlobals.getNetworkProfile2ButtonTagText());
	protected	RadioButton		networkProfile3 = new RadioButton("NetworkProfile");
	protected	Label			networkProfile3ButtonTag = new Label(UIGlobals.getNetworkProfile3ButtonTagText());
	
	protected	HTML			imageQualityComment = new HTML(UIGlobals.getImageQualitySettingComment());
	
	protected	RadioButton		imageQualityLow = new RadioButton("ImageQuality");
	protected	Label			imageQualityLowButtonTag = new Label(UIGlobals.getImageQualityLowButtonTagText());
	protected	RadioButton		imageQualityMedium = new RadioButton("ImageQuality");
	protected	Label			imageQualityMediumButtonTag = new Label(UIGlobals.getImageQualityMediumButtonTagText());
	protected	RadioButton		imageQualityHigh = new RadioButton("ImageQuality");
	protected	Label			imageQualityHighButtonTag = new Label(UIGlobals.getImageQualityHighButtonTagText());

	protected	HTML			mouseTrackComment = new HTML(UIGlobals.getMouseEnableSettingComment());
	protected	RadioButton		enableMouseTrack = new RadioButton("Mouse");
	protected	Label			enableMouseTrackButtonTag = new Label(UIGlobals.getMouseEnableButtonTagText());
	protected	RadioButton		disableMouseTrack = new RadioButton("Mouse");
	protected	Label			disableMouseTrackButtonTag = new Label(UIGlobals.getMouseDisableButtonTagText());

	
	protected	HTML		maxParticipantsComment = new HTML(UIGlobals.getMaxAttendeesSettingComment());
	protected	HTML		maxParticipantsReachedComment = new HTML(UIGlobals.getMaxAttendeesSettingComment());
	protected	ListBox		maxParticipantsOptions;
	
	protected	HTML		currentMeetingLengthComment = new HTML(UIGlobals.getCurrentMeetingLengthComment());
	protected	HTML		currentMeetingLengthHoursSuffix = new HTML(UIGlobals.getMeetingHoursSuffix());
	protected	HTML		currentMeetingLengthMinutesSuffix = new HTML(UIGlobals.getMeetingMinutesSuffix());
	
	protected	HTML		maxMeetingTimeComment = new HTML(UIGlobals.getMaxTimeSettingComment());
	protected	HTML		maxMeetingTimeReachedComment = new HTML(UIGlobals.getMaxTimeSettingComment());
	protected	HTML		maxMeetingHoursSuffix = new HTML(UIGlobals.getMeetingHoursSuffix());
	protected	HTML		maxMeetingMinutesSuffix = new HTML(UIGlobals.getMeetingMinutesSuffix());
//	protected	ListBox		maxMeetingTimeOptions;
	protected	ListBox		maxMeetingHoursOptions;
	protected	ListBox		maxMeetingMinutesOptions;
	
	protected	HTML		maxAttendeeMikesComment = new HTML(ConferenceGlobals.getDisplayString("meetingsetting.max_mikes","Maximum Attendee Mics:"));
	protected	ListBox		maxAttendeeMikes;
	
	//protected	HTML		returnURLComment = new HTML("Track Back URL:");
	protected	HTML		returnURLComment = new HTML(UIGlobals.getReturnURL());
	
	protected	TextBox		returnURL;
	
	public	SettingsDialog()
	{
		super(UIStrings.getMeetingSettingsDialogHeader());
//		super("Dimdim Meeting Invitations","400px");
		
		this.addStyleName("invitations-dialog-box");
//		super.addLogoImage = false;
	}
	
	protected	Widget	getContent()
	{
		SettingsModel currentSettings = ClientModel.getClientModel().getSettingsModel();
		RosterModel rosterModel = ClientModel.getClientModel().getRosterModel();
		String  currentNetworkProfile = rosterModel.getCurrentUser().getNetProfile();
		String  currentImageQuality = rosterModel.getCurrentUser().getImgQuality();
		int	currentMaxAudios = rosterModel.getMaximumAttendeeAudios();
		UIParams uiParams = UIParams.getUIParams();
		basePanel	=	new VerticalPanel();
		
//		basePanel.setStyleName("send-invitation-preview-box");
		basePanel.add(this.settingsMainComment);
		basePanel.setCellWidth(this.settingsMainComment,"100%");
		this.settingsMainComment.setStyleName("invitations-preview-comment");
		
		HTML line0 = new HTML("&nbsp;");
		line0.setStyleName("line-break");
		basePanel.add(line0);
		
		//	Meeting Lobby
		
		HorizontalPanel lobbyControlButtons = new HorizontalPanel();
		lobbyControlButtons.add(this.meetingLobbyComment);
		lobbyControlButtons.setCellVerticalAlignment(this.meetingLobbyComment, VerticalPanel.ALIGN_MIDDLE);
		this.meetingLobbyComment.setStyleName("invitations-preview-comment");
		this.meetingLobbyComment.addStyleName("settings-item-heading");
		
		this.enableLobby.setChecked(currentSettings.isLobbyEnabled());
		lobbyControlButtons.add(this.enableLobby);
		lobbyControlButtons.setCellVerticalAlignment(this.enableLobby, VerticalPanel.ALIGN_MIDDLE);
		lobbyControlButtons.add(this.enableLobbyButtonTag);
		lobbyControlButtons.setCellVerticalAlignment(this.enableLobbyButtonTag, VerticalPanel.ALIGN_MIDDLE);
		this.enableLobbyButtonTag.setWordWrap(false);
		this.enableLobbyButtonTag.setStyleName("invitations-preview-comment");
		this.enableLobbyButtonTag.addStyleName("settings-button-tag-text");
		
		this.disableLobby.setChecked(!currentSettings.isLobbyEnabled());
		lobbyControlButtons.add(this.disableLobby);
		lobbyControlButtons.setCellVerticalAlignment(this.disableLobby, VerticalPanel.ALIGN_MIDDLE);
		lobbyControlButtons.add(this.disableLobbyButtonTag);
		lobbyControlButtons.setCellVerticalAlignment(this.disableLobbyButtonTag, VerticalPanel.ALIGN_MIDDLE);
		this.disableLobbyButtonTag.setWordWrap(false);
		this.disableLobbyButtonTag.setStyleName("invitations-preview-comment");
		this.disableLobbyButtonTag.addStyleName("settings-button-tag-text");
		
		basePanel.add(lobbyControlButtons);
		
	
		
		//	Network Profile
		
		if(ConferenceGlobals.publisherEnabled)
		{
			HTML line1 = new HTML("&nbsp;");
			line1.setStyleName("line-break");
			basePanel.add(line1);
			
			HorizontalPanel networkProfileButtons = new HorizontalPanel();
			networkProfileButtons.add(this.networkProfileComment);
			networkProfileButtons.setCellVerticalAlignment(this.networkProfileComment, VerticalPanel.ALIGN_MIDDLE);
			this.networkProfileComment.setStyleName("invitations-preview-comment");
			this.networkProfileComment.addStyleName("settings-item-heading");
			
			this.networkProfile1.setChecked(currentNetworkProfile.equals("1"));
			networkProfileButtons.add(this.networkProfile1);
			networkProfileButtons.setCellVerticalAlignment(this.networkProfile1, VerticalPanel.ALIGN_MIDDLE);
			networkProfileButtons.add(this.networkProfile1ButtonTag);
			networkProfileButtons.setCellVerticalAlignment(this.networkProfile1ButtonTag, VerticalPanel.ALIGN_MIDDLE);
			this.networkProfile1ButtonTag.setWordWrap(false);
			this.networkProfile1ButtonTag.setStyleName("invitations-preview-comment");
			this.networkProfile1ButtonTag.addStyleName("settings-button-tag-text");
			
			this.networkProfile2.setChecked(currentNetworkProfile.equals("2"));
			networkProfileButtons.add(this.networkProfile2);
			networkProfileButtons.setCellVerticalAlignment(this.networkProfile2, VerticalPanel.ALIGN_MIDDLE);
			networkProfileButtons.add(this.networkProfile2ButtonTag);
			networkProfileButtons.setCellVerticalAlignment(this.networkProfile2ButtonTag, VerticalPanel.ALIGN_MIDDLE);
			this.networkProfile2ButtonTag.setWordWrap(false);
			this.networkProfile2ButtonTag.setStyleName("invitations-preview-comment");
			this.networkProfile2ButtonTag.addStyleName("settings-button-tag-text");
			
			this.networkProfile3.setChecked(currentNetworkProfile.equals("3"));
			networkProfileButtons.add(this.networkProfile3);
			networkProfileButtons.setCellVerticalAlignment(this.networkProfile3, VerticalPanel.ALIGN_MIDDLE);
			networkProfileButtons.add(this.networkProfile3ButtonTag);
			networkProfileButtons.setCellVerticalAlignment(this.networkProfile3ButtonTag, VerticalPanel.ALIGN_MIDDLE);
			this.networkProfile3ButtonTag.setWordWrap(false);
			this.networkProfile3ButtonTag.setStyleName("invitations-preview-comment");
			this.networkProfile3ButtonTag.addStyleName("settings-button-tag-text");
			
			basePanel.add(networkProfileButtons);
			
//			HTML line2 = new HTML("&nbsp;");
//			line2.setStyleName("line-break");
//			basePanel.add(line2);
			
			//	Image Quality
			
//			HorizontalPanel imageQualityButtons = new HorizontalPanel();
//			imageQualityButtons.add(this.imageQualityComment);
//			imageQualityButtons.setCellVerticalAlignment(this.imageQualityComment, VerticalPanel.ALIGN_MIDDLE);
//			this.imageQualityComment.setStyleName("invitations-preview-comment");
//			this.imageQualityComment.addStyleName("settings-item-heading");
//			
//			this.imageQualityLow.setChecked(currentImageQuality.equalsIgnoreCase("low"));
//			imageQualityButtons.add(this.imageQualityLow);
//			imageQualityButtons.setCellVerticalAlignment(this.imageQualityLow, VerticalPanel.ALIGN_MIDDLE);
//			imageQualityButtons.add(this.imageQualityLowButtonTag);
//			imageQualityButtons.setCellVerticalAlignment(this.imageQualityLowButtonTag, VerticalPanel.ALIGN_MIDDLE);
//			this.imageQualityLowButtonTag.setWordWrap(false);
//			this.imageQualityLowButtonTag.setStyleName("invitations-preview-comment");
//			this.imageQualityLowButtonTag.addStyleName("settings-button-tag-text");
//			
//			this.imageQualityMedium.setChecked(currentImageQuality.equalsIgnoreCase("medium"));
//			imageQualityButtons.add(this.imageQualityMedium);
//			imageQualityButtons.setCellVerticalAlignment(this.imageQualityMedium, VerticalPanel.ALIGN_MIDDLE);
//			imageQualityButtons.add(this.imageQualityMediumButtonTag);
//			imageQualityButtons.setCellVerticalAlignment(this.imageQualityMediumButtonTag, VerticalPanel.ALIGN_MIDDLE);
//			this.imageQualityMediumButtonTag.setWordWrap(false);
//			this.imageQualityMediumButtonTag.setStyleName("invitations-preview-comment");
//			this.imageQualityMediumButtonTag.addStyleName("settings-button-tag-text");
//			
//			this.imageQualityHigh.setChecked(currentImageQuality.equalsIgnoreCase("high"));
//			imageQualityButtons.add(this.imageQualityHigh);
//			imageQualityButtons.setCellVerticalAlignment(this.imageQualityHigh, VerticalPanel.ALIGN_MIDDLE);
//			imageQualityButtons.add(this.imageQualityHighButtonTag);
//			imageQualityButtons.setCellVerticalAlignment(this.imageQualityHighButtonTag, VerticalPanel.ALIGN_MIDDLE);
//			this.imageQualityHighButtonTag.setWordWrap(false);
//			this.imageQualityHighButtonTag.setStyleName("invitations-preview-comment");
//			this.imageQualityHighButtonTag.addStyleName("settings-button-tag-text");
//			
//			basePanel.add(imageQualityButtons);
//			
//			HTML line3 = new HTML("&nbsp;");
//			line3.setStyleName("line-break");
//			basePanel.add(line3);
			
			// Mouse Track
			
//			HorizontalPanel mouseControlButtons = new HorizontalPanel();
//			mouseControlButtons.add(this.mouseTrackComment);
//			mouseControlButtons.setCellVerticalAlignment(this.mouseTrackComment, VerticalPanel.ALIGN_MIDDLE);
//			this.mouseTrackComment.setStyleName("invitations-preview-comment");
//			this.mouseTrackComment.addStyleName("settings-item-heading");
//			
//			this.enableMouseTrack.setChecked(currentSettings.isMouseTrackEnabled());
//			mouseControlButtons.add(this.enableMouseTrack);
//			mouseControlButtons.setCellVerticalAlignment(this.enableMouseTrack, VerticalPanel.ALIGN_MIDDLE);
//			mouseControlButtons.add(this.enableMouseTrackButtonTag);
//			mouseControlButtons.setCellVerticalAlignment(this.enableMouseTrackButtonTag, VerticalPanel.ALIGN_MIDDLE);
//			this.enableMouseTrackButtonTag.setWordWrap(false);
//			this.enableMouseTrackButtonTag.setStyleName("invitations-preview-comment");
//			this.enableMouseTrackButtonTag.addStyleName("settings-button-tag-text");
//			
//			this.disableMouseTrack.setChecked(!currentSettings.isMouseTrackEnabled());
//			mouseControlButtons.add(this.disableMouseTrack);
//			mouseControlButtons.setCellVerticalAlignment(this.disableMouseTrack, VerticalPanel.ALIGN_MIDDLE);
//			mouseControlButtons.add(this.disableMouseTrackButtonTag);
//			mouseControlButtons.setCellVerticalAlignment(this.disableMouseTrackButtonTag, VerticalPanel.ALIGN_MIDDLE);
//			this.disableMouseTrackButtonTag.setWordWrap(false);
//			this.disableMouseTrackButtonTag.setStyleName("invitations-preview-comment");
//			this.disableMouseTrackButtonTag.addStyleName("settings-button-tag-text");
//			
//			basePanel.add(mouseControlButtons);
		}//these settings make sense only whe publisher is enabled
		
		HTML line5 = new HTML("&nbsp;");
		line5.setStyleName("line-break");
		basePanel.add(line5);
		
		//	Maximum participants
		HorizontalPanel  maxParticipantsPanel = new HorizontalPanel();
		maxParticipantsPanel.add(this.maxParticipantsComment);
		maxParticipantsPanel.setCellVerticalAlignment(this.maxParticipantsComment, VerticalPanel.ALIGN_MIDDLE);
		maxParticipantsComment.setStyleName("invitations-preview-comment");
		maxParticipantsComment.addStyleName("settings-item-heading");
		
		maxParticipantsOptions = prepareListBox(ConferenceGlobals.currentMaxParticipants,
				uiParams.getMaxParticipantsForAnyMeeting(), 5);
		maxParticipantsPanel.add(maxParticipantsOptions);
		maxParticipantsPanel.setCellVerticalAlignment(maxParticipantsOptions,VerticalPanel.ALIGN_MIDDLE);
		
		basePanel.add(maxParticipantsPanel);
		basePanel.setCellVerticalAlignment(maxParticipantsPanel,VerticalPanel.ALIGN_MIDDLE);
		
		HTML line4 = new HTML("&nbsp;");
		line4.setStyleName("line-break");
		basePanel.add(line4);
		
		String s = ConferenceGlobals.getOverrideMaxParticipants();
		if (s != null && s.equalsIgnoreCase("false"))
		{
			maxParticipantsOptions.setEnabled(false);
		}
		
		//	Maximum meeting time
		
		int hours = ConferenceGlobals.currentMaxMeetingTime / 60;
		int minutes = ConferenceGlobals.currentMaxMeetingTime % 60;
		
		
		HorizontalPanel  currentMeetingLengthPanel = new HorizontalPanel();
		currentMeetingLengthComment.setStyleName("invitations-preview-comment");
		currentMeetingLengthComment.addStyleName("settings-item-heading");
		currentMeetingLengthPanel.add(currentMeetingLengthComment);
		currentMeetingLengthPanel.setCellVerticalAlignment(currentMeetingLengthComment,VerticalPanel.ALIGN_MIDDLE);
		
		currentMeetingLengthHoursSuffix.setStyleName("common-text");
		currentMeetingLengthHoursSuffix.addStyleName("common-label");
		currentMeetingLengthMinutesSuffix.setStyleName("common-text");
		currentMeetingLengthMinutesSuffix.addStyleName("common-label");
		
		Label h = new Label(""+hours);
		h.setStyleName("common-text");
		h.addStyleName("common-label");
		currentMeetingLengthPanel.add(h);
		currentMeetingLengthPanel.setCellVerticalAlignment(h,VerticalPanel.ALIGN_MIDDLE);
		currentMeetingLengthPanel.add(currentMeetingLengthHoursSuffix);
		currentMeetingLengthPanel.setCellVerticalAlignment(currentMeetingLengthHoursSuffix,VerticalPanel.ALIGN_MIDDLE);
		
		Label m = new Label(""+minutes);
		m.setStyleName("common-text");
		m.addStyleName("common-label");
		currentMeetingLengthPanel.add(m);
		currentMeetingLengthPanel.setCellVerticalAlignment(m,VerticalPanel.ALIGN_MIDDLE);
		currentMeetingLengthPanel.add(currentMeetingLengthMinutesSuffix);
		currentMeetingLengthPanel.setCellVerticalAlignment(currentMeetingLengthMinutesSuffix,VerticalPanel.ALIGN_MIDDLE);
		
		basePanel.add(currentMeetingLengthPanel);
		basePanel.setCellVerticalAlignment(currentMeetingLengthPanel,VerticalPanel.ALIGN_MIDDLE);
		
		
		
		HorizontalPanel  maxMeetingTimePanel = new HorizontalPanel();
		maxMeetingTimePanel.add(this.maxMeetingTimeComment);
		maxMeetingTimePanel.setCellVerticalAlignment(this.maxMeetingTimeComment, VerticalPanel.ALIGN_MIDDLE);
		maxMeetingTimeComment.setStyleName("invitations-preview-comment");
		maxMeetingTimeComment.addStyleName("settings-item-heading");
		
		maxMeetingHoursSuffix.setStyleName("invitations-preview-comment");
		maxMeetingMinutesSuffix.setStyleName("invitations-preview-comment");
		
		prepateMeetingTimeListBoxes(ConferenceGlobals.currentMaxMeetingTime,
				uiParams.getMaxMeetingTimeForAnyMeeting());
		
//		maxMeetingTimeOptions = prepareListBox(ConferenceGlobals.currentMaxMeetingTime,
//				uiParams.getMaxMeetingTimeForAnyMeeting(), 20);
		
		maxMeetingTimePanel.add(this.maxMeetingHoursOptions);
		maxMeetingTimePanel.setCellVerticalAlignment(maxMeetingHoursOptions,VerticalPanel.ALIGN_MIDDLE);
		maxMeetingTimePanel.add(this.maxMeetingHoursSuffix);
		maxMeetingTimePanel.setCellVerticalAlignment(maxMeetingHoursSuffix,VerticalPanel.ALIGN_MIDDLE);
		
		maxMeetingTimePanel.add(this.maxMeetingMinutesOptions);
		maxMeetingTimePanel.setCellVerticalAlignment(maxMeetingMinutesOptions,VerticalPanel.ALIGN_MIDDLE);
		maxMeetingTimePanel.add(this.maxMeetingMinutesSuffix);
		maxMeetingTimePanel.setCellVerticalAlignment(maxMeetingMinutesSuffix,VerticalPanel.ALIGN_MIDDLE);
		
		basePanel.add(maxMeetingTimePanel);
		basePanel.setCellVerticalAlignment(maxMeetingTimePanel,VerticalPanel.ALIGN_MIDDLE);
		
		HTML line6 = new HTML("&nbsp;");
		line6.setStyleName("line-break");
		basePanel.add(line6);
		
		//	Maximum attendee mikes
		if(!ConferenceGlobals.isPresenterAVAudioDisabled() && !ConferenceGlobals.isMeetingVideoChat())
		{
			HorizontalPanel  maxAttendeeMikesPanel = new HorizontalPanel();
			maxAttendeeMikesPanel.add(this.maxAttendeeMikesComment);
			maxAttendeeMikesPanel.setCellVerticalAlignment(this.maxAttendeeMikesComment, VerticalPanel.ALIGN_MIDDLE);
			maxAttendeeMikesComment.setStyleName("invitations-preview-comment");
			maxAttendeeMikesComment.addStyleName("settings-item-heading");
			
			maxAttendeeMikes = new ListBox();
			String sm = getServerMaxAttendeeAudios();
			int smi = (new Integer(sm)).intValue();
			for (int i=currentMaxAudios; i<=smi; i++)
			{
				maxAttendeeMikes.addItem(""+i);
			}
			maxAttendeeMikesPanel.add(maxAttendeeMikes);
			maxAttendeeMikesPanel.setCellVerticalAlignment(maxAttendeeMikes,VerticalPanel.ALIGN_MIDDLE);
			maxAttendeeMikesPanel.setVisible(false);
			
			basePanel.add(maxAttendeeMikesPanel);
			basePanel.setCellVerticalAlignment(maxAttendeeMikesPanel,VerticalPanel.ALIGN_MIDDLE);
			
			HTML line7 = new HTML("&nbsp;");
			line7.setStyleName("line-break");
			line7.setVisible(false);
			basePanel.add(line7);
		}
		//	Track back url
		
		HorizontalPanel  returnURLPanel = new HorizontalPanel();
		returnURLPanel.add(this.returnURLComment);
		returnURLPanel.setCellVerticalAlignment(this.returnURLComment, VerticalPanel.ALIGN_MIDDLE);
		returnURLComment.setStyleName("invitations-preview-comment");
		returnURLComment.addStyleName("settings-item-heading");
		
		returnURL = new TextBox();
		returnURL.setText(this.getReturnUrlForSettings());
		returnURL.setStyleName("common-text");
		returnURLPanel.add(returnURL);
		returnURLPanel.setCellVerticalAlignment(returnURL,VerticalPanel.ALIGN_MIDDLE);
		
		basePanel.add(returnURLPanel);
		basePanel.setCellVerticalAlignment(returnURLPanel,VerticalPanel.ALIGN_MIDDLE);
		
		HTML line8 = new HTML("&nbsp;");
		line8.setStyleName("line-break");
		basePanel.add(line8);
		
		
		
//		Window.alert("7");
		return	basePanel;
	}
	protected	void	prepateMeetingTimeListBoxes(int currentValue, int maxValue)
	{
		int	availableTime = maxValue - currentValue;
		this.maxMeetingHoursOptions = new ListBox();
		this.maxMeetingHoursOptions.addItem("0");
		this.maxMeetingMinutesOptions = new ListBox();
		this.maxMeetingMinutesOptions.addItem("0");
		if (availableTime > 0)
		{
			int hours = availableTime / 60;
			int minutes = availableTime % 60;
			for (int i=0; i<hours-1; i++)
			{
				this.maxMeetingHoursOptions.addItem((i+1)+"");
			}
			/*if (hours > 0)
			{
				this.maxMeetingMinutesOptions.addItem("15");
				this.maxMeetingMinutesOptions.addItem("30");
				this.maxMeetingMinutesOptions.addItem("45");
			}
			else
			{
				if (minutes > 15)
				{
					this.maxMeetingMinutesOptions.addItem("15");
				}
				if (minutes > 30)
				{
					this.maxMeetingMinutesOptions.addItem("30");
				}
				if (minutes > 45)
				{
					this.maxMeetingMinutesOptions.addItem("45");
				}
			}*/

				if (minutes == 15)
				{
					this.maxMeetingMinutesOptions.addItem("15");
				}
				if (minutes == 30)
				{
					this.maxMeetingMinutesOptions.addItem("15");
					this.maxMeetingMinutesOptions.addItem("30");
				} else
				{
					this.maxMeetingMinutesOptions.addItem("15");
					this.maxMeetingMinutesOptions.addItem("30");
					this.maxMeetingMinutesOptions.addItem("45");
					
				}	
				/*if (minutes == 45)
				{
					this.maxMeetingMinutesOptions.addItem("15");
					this.maxMeetingMinutesOptions.addItem("30");
					this.maxMeetingMinutesOptions.addItem("45");
				}*/
				
		}
	}
	protected	ListBox		prepareListBox(int currentValue, int maxValue, int interval)
	{
		if (currentValue >= maxValue)
		{
			ListBox lb = new ListBox();
			lb.addItem(""+maxValue);
			return	lb;
		}
		ListBox list = new ListBox();
		int i = currentValue;
		while (i%interval > 0)
		{
			i++;
		}
		for (; i<=maxValue; i+=interval)
		{
			if (i>maxValue)
			{
				break;
			}
			list.addItem(""+i);
		}
		return	list;
	}
	public	void	onClick(Widget sender)
	{
		//Window.alert("arg0");
		boolean settingsChange = false;
		boolean pubSettingsChange = false;	
		boolean hideWindow = true;
		
		if (sender == this.sendButton)
		{
			SettingsModel currentSettings = ClientModel.getClientModel().getSettingsModel();
			RosterModel rosterModel = ClientModel.getClientModel().getRosterModel();
			String networkProfile = "1";
			String imageQuality = "low";
			int	currentMaxAudios = rosterModel.getMaximumAttendeeAudios();
			
			//	Enable / disable lobby
			
			if (currentSettings.isLobbyEnabled() && this.disableLobby.isChecked())
			{
				settingsChange = true;
				rosterModel.disableLobby();
			}
			else if (!currentSettings.isLobbyEnabled() && this.enableLobby.isChecked())
			{
				settingsChange = true;			
				rosterModel.enableLobby();
			}

			//	END Enable / disable lobby

			
			if(ConferenceGlobals.publisherEnabled)
			{
				//PublisherInterfaceManager pubInterface = PublisherInterfaceManager.getManager();
				//	Enable / disable Mouse
				
				
				boolean changeMouseSetting = false;
				//if (currentSettings.isMouseTrackEnabled() && this.disableMouseTrack.isChecked())
				//{
				//	changeMouseSetting = true;
				//	pubSettingsChange = true;
				//}
				//else if (!currentSettings.isMouseTrackEnabled() && this.enableMouseTrack.isChecked())
				//{
				//	changeMouseSetting = true;
				//	pubSettingsChange = true;
				//}
				//if (changeMouseSetting)
				//{
				//	if(this.enableMouseTrack.isChecked())
				//	{
				//		pubInterface.setMouseVisible("1");
				//		currentSettings.setMouseTrackEnabled(true);
				//	} else 
				//	{
				//		pubInterface.setMouseVisible("0");
				//		currentSettings.setMouseTrackEnabled(false);
				//	}
				//}
			
				//	END Enable / disable Mouse

			
				// Network Profile
				
				//	The network profile and image qualty settings are purely local, as they
				//	used by the av and audio broadcasters and communicated to the other
				//	attendees through streaming events.
				
				
				if (this.networkProfile2.isChecked())
				{
					networkProfile = "2";
				}
				else if (this.networkProfile3.isChecked())
				{
					networkProfile = "3";
				}
		
				if(rosterModel.getCurrentUser().getNetProfile().compareTo(networkProfile) != 0)
				{	
					pubSettingsChange = true;		
					//Window.alert("Network Profile Changed");
					
					rosterModel.getCurrentUser().setNetProfile(networkProfile);
					if (rosterModel.getCurrentActivePresenter().getUserId().
							equals(rosterModel.getCurrentUser().getUserId()))
					{
						rosterModel.getCurrentActivePresenter().setNetProfile(networkProfile);
					}
				
					// Set the Network Profile on the Publisher Configuration.
					//now publisher related setttings will not be updated from console
					//pubInterface.setNetworkProfileValue(networkProfile);
				}
				
				// End Set the Network Profile.
			
				// Image Profile
				
				//if (this.imageQualityMedium.isChecked())
				//{
				//	imageQuality = "medium";
				//}
				//else if (this.imageQualityHigh.isChecked())
				//{
				//	imageQuality = "high";
				//}
			
				//Window.alert("Image Quality:"+imageQuality);
				
				//if(rosterModel.getCurrentUser().getImgQuality().compareTo(imageQuality) != 0)
				//{	
				//	pubSettingsChange = true;		
				//	rosterModel.getCurrentUser().setImgQuality(imageQuality);
				//	if (rosterModel.getCurrentActivePresenter().getUserId().
				//			equals(rosterModel.getCurrentUser().getUserId()))
				//	{
				//		rosterModel.getCurrentActivePresenter().setImgQuality(imageQuality);
				//	}
				//	
				//	pubInterface.setImageProfileValue(imageQuality);
				//}
	
				// End of Image Profile
				
				//	Save all settings on server for this user and for conference if
				//	the user is the active presenter or organizer.
			}//these settings are hidden when publisher is disabled
			
			int mp = this.maxParticipantsOptions.getSelectedIndex();
			//Window.alert("checking max participants");
			String maxParticipants = this.maxParticipantsOptions.getItemText(mp);
			
			if(ConferenceGlobals.currentMaxParticipants != (new Integer(maxParticipants)).intValue())
			{
				settingsChange = true;
			}
			
			int mh = this.maxMeetingHoursOptions.getSelectedIndex();
			String extendHours = this.maxMeetingHoursOptions.getItemText(mh);
			//Window.alert("checking max hrs");
			
			int mm = this.maxMeetingMinutesOptions.getSelectedIndex();
			String extendMinutes = this.maxMeetingMinutesOptions.getItemText(mm);
			//Window.alert("checking max min");
			
			int currentExtention = (new Integer(extendHours)).intValue()*60+
					(new Integer(extendMinutes)).intValue();
			int newMaxTime = ConferenceGlobals.currentMaxMeetingTime + currentExtention;
			//Window.alert("checking newMaxTime = "+newMaxTime);
			
			String returnUrl = this.returnURL.getText();
			//Window.alert("checking returnUrl = "+returnUrl);
			if (returnUrl.length() == 0)
			{
				returnUrl = this.getReturnUrl();
			}
			else
			{
				this.setReturnUrl(returnUrl);
			}

			rosterModel.setProfileOptions(networkProfile, imageQuality,
					maxParticipants, newMaxTime+"",returnUrl);

			if(ConferenceGlobals.currentMaxMeetingTime != (new Integer(newMaxTime)).intValue())
			{	
				settingsChange = true;
				ConferenceGlobals.setCurrentMaxMeetingTime(newMaxTime+"");
			}
			//Window.alert("after setting into roster model ");
			if(!ConferenceGlobals.isPresenterAVAudioDisabled() && !ConferenceGlobals.isMeetingVideoChat())
			{
				int ma = this.maxAttendeeMikes.getSelectedIndex()+currentMaxAudios;
				if (ma != currentMaxAudios)
				{
					settingsChange = true;
					rosterModel.setMaximumAttendeeAudios(ma);
				}
			}
			
			ConferenceGlobals.setCurrentMaxParticipants(maxParticipants);
		}
		
		hide();
		
		if(settingsChange)
			DefaultCommonDialog.showMessage(ConferenceGlobals.getDisplayString("console.settings.saved.header","Settings Saved"), UIGlobals.getChangeConfirmForCurrMeeting());
		
		if(pubSettingsChange)
			DefaultCommonDialog.showMessage(ConferenceGlobals.getDisplayString("console.settings.saved.header","Settings Saved"), UIGlobals.getSettingChangeConfirmation());
	
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		sendButton = new Button();
		sendButton.setText(UIStrings.getOKLabel());
		sendButton.setStyleName("dm-popup-close-button");
		sendButton.addClickListener(this);
		v.addElement(sendButton);
		
		return	v;
	}
	protected	void	showDialogComplete()
	{
	}
	private String getServerMaxAttendeeAudios()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("server_max_attendee_audios");
	}
	
	//this will be set to a different url if the call is from protal and me is host
	private String getReturnUrlForSettings()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("return_url_in_setings");
	}
	
	private String getReturnUrl()
	{
		return ConferenceGlobals.userInfoDictionary.getStringValue("return_url");
	}
	private void setReturnUrl(String url)
	{
		ConferenceGlobals.userInfoDictionary.setStringValue("return_url",url);
	}
}
