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

package com.dimdim.conference.ui.user.client;

import java.util.Vector;

import asquare.gwt.tk.client.ui.ModalDialog;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SingleAttendeePermissionsControlDialog	extends	CommonModalDialog implements ClickListener
{
	protected	Button		applyButton;
	protected	ListEntry	listEntry;
	protected	UserRosterManager		userManager;
	
	protected	CheckBox	chatControl;
	protected	CheckBox	audioControl;
	protected	CheckBox	removeControl;
	
	public	SingleAttendeePermissionsControlDialog(UserRosterManager userManager,
			ListEntry listEntry)
	{
		super(UIStrings.getPermissionsControlDialogHeader()+
				" - "+((UserListEntry)listEntry).getUser().getDisplayName());
		this.userManager = userManager;
		this.listEntry = listEntry;
	}
	protected	Widget	getContent()
	{
		VerticalPanel table = new VerticalPanel();
		
			UIResources  uiResources = UIResources.getUIResources();
			HorizontalPanel header = new HorizontalPanel();
			header.add(createLabel(".","user-image-header"));
			header.add(createLabel(UIStrings.getNameLabel(),"user-name-header"));
			header.add(createLabel(UIStrings.getEmailLabel(),"user-email-header"));
			
			if(ConferenceGlobals.privateChatEnabled)
			{
				Label chat = createLabel(UIStrings.getChatLabel(),"user-chat-button-header");
	//			chat.addStyleName("common-anchor");
				header.add(chat);
			}
			if(!ConferenceGlobals.isPresenterAVAudioDisabled())
			{
				String label = UIStrings.getAudioLabel();
				if(ConferenceGlobals.isPresenterAVVideoOnly())
				{
					label = UIStrings.getVideoLabel();
				}
				Label audio = createLabel(label,"user-chat-button-header");
				header.add(audio);
			}
//			Label video = createLabel("Video","user-chat-button-header");
//			header.add(video);
			
			header.addStyleName("common-dialog-row");
			table.add(header);
			table.setCellWidth(header,"100%");
			{
				UserListEntry ule = (UserListEntry)listEntry;
				UIRosterEntry user = ule.getUser();
				
				HorizontalPanel row = new HorizontalPanel();
				
				HorizontalPanel hp1 = new HorizontalPanel();
				Image image = ule.getImage1Url();
				hp1.add(image);
				hp1.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_CENTER);
				hp1.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);
				hp1.setStyleName("user-image");
				row.add(hp1);
				row.setCellHorizontalAlignment(hp1,HorizontalPanel.ALIGN_CENTER);
				row.setCellVerticalAlignment(hp1,VerticalPanel.ALIGN_MIDDLE);
				
				Label nameLabel = createTextHTML(user.getDisplayName(),"user-name");
//				nameLabel.addStyleName("common-anchor");
				row.add(nameLabel);
				
				String	subject = uiResources.getConferenceInfoAndDecode64("subject");
				String	mailToTag = "<a href=\"mailto:"+user.getUserId()+
					"?subject="+subject+"\">"+user.getUserId()+"</a>";
				
				HTML emailLabel = new HTML("<span>"+mailToTag+"</span>");
				emailLabel.setStyleName("common-table-text");
				emailLabel.addStyleName("user-email");
				emailLabel.addStyleName("common-anchor");
				row.add(emailLabel);
				
				if(ConferenceGlobals.privateChatEnabled)
				{
					HorizontalPanel hp2 = new HorizontalPanel();
					this.chatControl = new CheckBox();
					hp2.add(chatControl);
					hp2.setStyleName("user-chat-button");
					hp2.setCellHorizontalAlignment(chatControl,HorizontalPanel.ALIGN_LEFT);
					hp2.setCellVerticalAlignment(chatControl,VerticalPanel.ALIGN_MIDDLE);
					row.add(hp2);
					row.setCellHorizontalAlignment(hp2,HorizontalPanel.ALIGN_LEFT);
					row.setCellVerticalAlignment(hp2,VerticalPanel.ALIGN_MIDDLE);
					chatControl.setChecked(user.isChatOn());
				}
				
				
				if (!ConferenceGlobals.isPresenterAVAudioDisabled())
				{					
					HorizontalPanel hp3 = new HorizontalPanel();
					this.audioControl = new CheckBox();
					hp3.add(audioControl);
					hp3.setStyleName("user-chat-button");
					hp3.setCellHorizontalAlignment(audioControl,HorizontalPanel.ALIGN_LEFT);
					hp3.setCellVerticalAlignment(audioControl,VerticalPanel.ALIGN_MIDDLE);
					row.add(hp3);					
					row.setCellHorizontalAlignment(hp3,HorizontalPanel.ALIGN_LEFT);
					row.setCellVerticalAlignment(hp3,VerticalPanel.ALIGN_MIDDLE);
					audioControl.setChecked(user.isAudioOn());
					if (!user.isAudioOn())
					{
						audioControl.setEnabled(userManager.canEnableAudioFor(user.getUserId()));
					}					
				}	
				row.addStyleName("common-dialog-row");
				table.add(row);
			}
			
		return	table;
	}
	public	void	setDialogPosition(int left, int top)
	{
		//	Move the popup from its default center of the page position if required.
		removeController(getController(ModalDialog.PositionDialogController.class));
		setPopupPosition(left, top);
	}
	protected	Widget	createHeaderWidget(String text, String style)
	{
		HorizontalPanel hp = new HorizontalPanel();
		CheckBox cb = new CheckBox();
		cb.setStyleName("control-table-header-checkbox");
		hp.setCellVerticalAlignment(cb,VerticalPanel.ALIGN_MIDDLE);
		
		hp.add(createLabel(text,style));
		hp.add(cb);
		hp.setStyleName("control-table-header-checkbox-combo");
		
		return	hp;
	}
	protected	Label	createLabel(String labelText, String styleName)
	{
		Label html = new Label(labelText);
		html.setStyleName("common-table-header");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Label	createTextHTML(String text, String styleName)
	{
		Label html = new Label(text);
		html.setStyleName("common-table-text");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
		{
			applyButton = new Button();
			applyButton.setText(UIStrings.getOKLabel());
			applyButton.setStyleName("dm-popup-close-button");
			applyButton.addClickListener(this);
			v.addElement(applyButton);
		}
		
		return	v;
	}
	public	void	onClick(Widget w)
	{
		if (w == applyButton)
		{
				RosterModel rosterModel = ClientModel.getClientModel().getRosterModel();
				{
					UIRosterEntry user = ((UserListEntry)this.listEntry).getUser();
					{
						if(ConferenceGlobals.privateChatEnabled)
						{
							if (chatControl.isChecked() && !user.isChatOn())
							{
								this.userManager.enableChatFor(user.getUserId());
							}
							else if (!chatControl.isChecked() && user.isChatOn())
							{
								this.userManager.disableChatFor(user.getUserId());
							}
						}
					}
					if(!ConferenceGlobals.isPresenterAVAudioDisabled())
					{
						if (audioControl.isChecked() && !user.isAudioOn())
						{
							this.userManager.enableAudioFor(user.getUserId());
						}
						else if (!audioControl.isChecked() && user.isAudioOn())
						{
							this.userManager.disableAudioFor(user.getUserId());
						}
					}
				}
			
			hide();
		}
	}
}
