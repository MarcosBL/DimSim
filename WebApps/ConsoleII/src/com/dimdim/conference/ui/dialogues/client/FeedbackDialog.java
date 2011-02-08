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
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.dialogues.client.common.CommonConsoleHelpers;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.user.client.IInvitationManager;
import com.dimdim.conference.ui.user.client.InvitationsManager;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class FeedbackDialog extends CommonModalDialog implements ClickListener
{
	protected	VerticalPanel	basePanel	=	null;
	protected	Button		sendButton = null;
	protected	TextArea	message = null;
	
	protected	HTML			rateExperienceComment = new HTML(UIGlobals.getRateYourExperienceComment()+ConferenceGlobals.feedbackEmail+" ):");
	protected	RadioButton		excellent = new RadioButton("Experience");
	protected	Label			excellentButtonTag = new Label(UIGlobals.getExperienceRateLabelText(4));
	protected	RadioButton		good = new RadioButton("Experience");
	protected	Label			goodButtonTag = new Label(UIGlobals.getExperienceRateLabelText(3));
	protected	RadioButton		neutral = new RadioButton("Experience");
	protected	Label			neutralButtonTag = new Label(UIGlobals.getExperienceRateLabelText(2));
	protected	RadioButton		fair = new RadioButton("Experience");
	protected	Label			fairButtonTag = new Label(UIGlobals.getExperienceRateLabelText(1));
	protected	RadioButton		poor = new RadioButton("Experience");
	protected	Label			poorButtonTag = new Label(UIGlobals.getExperienceRateLabelText(0));
	protected 	InvitationsManager invitationsManager;
	
	protected	IInvitationManager	invitationManager;
	
	public	FeedbackDialog(UserRosterManager userManager)
	{
		super(UIStrings.getFeedbackDialogHeader());
//		super("Dimdim Meeting Invitations","400px");
		
		this.invitationsManager = new InvitationsManager(userManager);
		CommonConsoleHelpers.setInvitationsManager(this.invitationsManager);
		
		this.invitationManager = CommonConsoleHelpers.getInvitationsManager();
		this.addStyleName("invitations-dialog-box");
//		super.addLogoImage = false;
	}
	
	protected	Widget	getContent()
	{
		basePanel	=	new VerticalPanel();
		message = new TextArea();
		
		
		basePanel.add(this.rateExperienceComment);
		basePanel.setCellWidth(this.rateExperienceComment,"100%");
		basePanel.setCellVerticalAlignment(this.rateExperienceComment, VerticalPanel.ALIGN_MIDDLE);
		this.rateExperienceComment.setStyleName("invitations-preview-comment");
		
		HTML line1 = new HTML("&nbsp;");
		line1.setStyleName("line-break");
		basePanel.add(line1);
		
		HorizontalPanel excellentPanel = new HorizontalPanel();
		excellentPanel.add(this.excellent);
		this.excellent.addStyleName("feedback-button");
		excellentPanel.setCellVerticalAlignment(this.excellent, VerticalPanel.ALIGN_MIDDLE);
		excellentPanel.add(this.excellentButtonTag);
		excellentPanel.setCellVerticalAlignment(this.excellentButtonTag, VerticalPanel.ALIGN_MIDDLE);
		this.excellentButtonTag.setWordWrap(false);
		this.excellentButtonTag.setStyleName("invitations-preview-comment");
		this.excellentButtonTag.addStyleName("feedback-button-tag-text");
		basePanel.add(excellentPanel);
		
		HorizontalPanel goodPanel = new HorizontalPanel();
		goodPanel.add(this.good);
		this.good.addStyleName("feedback-button");
		goodPanel.setCellVerticalAlignment(this.good, VerticalPanel.ALIGN_MIDDLE);
		goodPanel.add(this.goodButtonTag);
		goodPanel.setCellVerticalAlignment(this.goodButtonTag, VerticalPanel.ALIGN_MIDDLE);
		this.goodButtonTag.setWordWrap(false);
		this.goodButtonTag.setStyleName("invitations-preview-comment");
		this.goodButtonTag.addStyleName("feedback-button-tag-text");
		basePanel.add(goodPanel);
		
		HorizontalPanel neutralPanel = new HorizontalPanel();
		neutralPanel.add(this.neutral);
		this.neutral.addStyleName("feedback-button");
		neutralPanel.setCellVerticalAlignment(this.neutral, VerticalPanel.ALIGN_MIDDLE);
		neutralPanel.add(this.neutralButtonTag);
		neutralPanel.setCellVerticalAlignment(this.neutralButtonTag, VerticalPanel.ALIGN_MIDDLE);
		this.neutralButtonTag.setWordWrap(false);
		this.neutralButtonTag.setStyleName("invitations-preview-comment");
		this.neutralButtonTag.addStyleName("feedback-button-tag-text");
		basePanel.add(neutralPanel);
		
		HorizontalPanel fairPanel = new HorizontalPanel();
		fairPanel.add(this.fair);
		this.fair.addStyleName("feedback-button");
		fairPanel.setCellVerticalAlignment(this.fair, VerticalPanel.ALIGN_MIDDLE);
		fairPanel.add(this.fairButtonTag);
		fairPanel.setCellVerticalAlignment(this.fairButtonTag, VerticalPanel.ALIGN_MIDDLE);
		this.fairButtonTag.setWordWrap(false);
		this.fairButtonTag.setStyleName("invitations-preview-comment");
		this.fairButtonTag.addStyleName("feedback-button-tag-text");
		basePanel.add(fairPanel);
		
		HorizontalPanel poorPanel = new HorizontalPanel();
		poorPanel.add(this.poor);
		this.poor.addStyleName("feedback-button");
		poorPanel.setCellVerticalAlignment(this.poor, VerticalPanel.ALIGN_MIDDLE);
		poorPanel.add(this.poorButtonTag);
		poorPanel.setCellVerticalAlignment(this.poorButtonTag, VerticalPanel.ALIGN_MIDDLE);
		this.poorButtonTag.setWordWrap(false);
		this.poorButtonTag.setStyleName("invitations-preview-comment");
		this.poorButtonTag.addStyleName("feedback-button-tag-text");
		basePanel.add(poorPanel);
		
		
		HTML line2 = new HTML("&nbsp;");
		line2.setStyleName("line-break");
		basePanel.add(line2);
		
//		Window.alert("5");
		HTML messageComment = new HTML(UIGlobals.getFeedbackMessageComment());
		basePanel.add(messageComment);
		basePanel.setCellWidth(messageComment,"100%");
		messageComment.setStyleName("invitations-preview-comment");
		basePanel.add(message);
		message.setVisibleLines(4);
		basePanel.setCellWidth(message,"100%");
		message.setStyleName("invitations-preview-textarea");
		
		HTML line3 = new HTML("&nbsp;");
		line3.setStyleName("line-break");
		basePanel.add(line3);
		
		return	basePanel;
	}
	public	void	onClick(Widget sender)
	{
		if (this.invitationManager == null)
		{
			this.invitationManager = CommonConsoleHelpers.getInvitationsManager();
		}
		if (sender == this.sendButton && this.invitationManager != null)
		{
			int	rating = 999;
			if (this.excellent.isChecked())	rating = 0;
			else if (this.good.isChecked()) rating = 1;
			else if (this.neutral.isChecked())	rating = 2;
			else if (this.fair.isChecked())	rating = 3;
			else if (this.poor.isChecked())	rating = 4;
			
			String comment = this.message.getText();
			this.invitationManager.sendFeedback(rating, comment, ConferenceGlobals.feedbackEmail);
			
		}
		hide();
		//Window.alert("the mail will be sent to .."+ConferenceGlobals.organizerEmail);
		//Window.alert(UIGlobals.getFeedbackConfirmation());
		
		DefaultCommonDialog.showMessage(ConferenceGlobals.getDisplayString("console.feedback.header",
				"Feedback Delivered"), UIGlobals.getFeedbackConfirmation());
		
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		sendButton = new Button();
		sendButton.setText(UIStrings.getSendLabel());
		sendButton.setStyleName("dm-popup-close-button");
		sendButton.addClickListener(this);
		v.addElement(sendButton);
		
		return	v;
	}
	protected	void	showDialogComplete()
	{
		this.message.setFocus(true);
	}
}
