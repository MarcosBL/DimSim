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
import com.google.gwt.user.client.ui.Widget;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.util.CopyToClipBoard;
import com.dimdim.conference.ui.dialogues.client.FeedbackDialog;
import com.dimdim.conference.ui.dialogues.client.SettingsDialog;
import com.dimdim.conference.ui.dialogues.client.common.AboutCommonDialog;
import com.dimdim.conference.ui.dialogues.client.common.AboutConferenceHtml;
import com.dimdim.conference.ui.dialogues.client.common.MeetingInfoHtml;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.UIResources;

public class ToolsClickListener implements ClickListener
{
	protected	ToolsPopupPanel	toolsPanel;
	
	public	ToolsClickListener(ToolsPopupPanel	toolsPanel)
	{
		this.toolsPanel = toolsPanel;
	}
	public void onClick(Widget sender)
	{
		toolsPanel.hide();
		if (sender == toolsPanel.aboutLabel)
		{
			AboutConferenceHtml ach = new AboutConferenceHtml();
			AboutCommonDialog dlg = new AboutCommonDialog(toolsPanel.aboutHeading, ach, "about-meeting");
			dlg.drawDialog();
		}
		else if (sender == toolsPanel.meetingLabel)
		{
			AnalyticsConstants.reportMeetingInfoLaunched();
			MeetingInfoHtml mih = new MeetingInfoHtml();
			DefaultCommonDialog dlg = new DefaultCommonDialog(UIStrings.getMeetingInfoDialogHeader(),
					mih, "meeting-info");
			dlg.drawDialog();
			String	joinURL = UIResources.getUIResources().getConferenceInfo("joinURL");
			CopyToClipBoard copyListener = new CopyToClipBoard(joinURL);
		}
		else if (sender == toolsPanel.feedbackLabel)
		{
			FeedbackDialog dlg = new FeedbackDialog(toolsPanel.userManager);
			dlg.drawDialog();
		}
		else if (sender == toolsPanel.settingsLabel)
		{
			SettingsDialog dlg = new SettingsDialog();
			dlg.drawDialog();
		}
		else if (sender == toolsPanel.assistantLabel)
		{
			MeetingAssistentDialog meetingAssistent =
				new MeetingAssistentDialog(toolsPanel.resRoster,toolsPanel.shareClickListener);
			AnalyticsConstants.reportAssistantLaunched();
			meetingAssistent.showMeetingAssistent();
		}
	}
}
