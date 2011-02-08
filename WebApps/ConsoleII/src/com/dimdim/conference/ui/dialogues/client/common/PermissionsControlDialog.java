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

package com.dimdim.conference.ui.dialogues.client.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.user.NewChatController;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.dialogues.client.common.helper.CheckBoxSet;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.UIResources;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PermissionsControlDialog	extends	CommonModalDialog implements ClickListener
{
	protected	Button	applyButton;
	protected	DefaultList	listModel;
//	protected	UserListAndEntryProperties	userListAndEntryProperties;
	protected	UserRosterManager		userManager;
	protected	boolean		allowUserControl;
	protected	Label		chatHeaderLabel;
	protected	boolean		allChatEnabled = true;
	protected	CheckBoxSet	audioCheckBoxSet;
	
	protected	HashMap	chatCheckBoxes = new HashMap();
	protected	HashMap	audioCheckBoxes = new HashMap();
//	protected	HashMap	videoCheckBoxes = new HashMap();
	
	public	PermissionsControlDialog(UserRosterManager userManager,
			DefaultList listModel)
	{
		super(UIStrings.getPermissionsControlDialogHeader());
		this.userManager = userManager;
		this.listModel = listModel;
		this.audioCheckBoxSet = new CheckBoxSet(userManager.getMaximumAttendeeAudios(),null);
//		this.userListAndEntryProperties = userListAndEntryProperties;
//		this.addStyleName("user-control-dialog-box");
	}
	protected	Widget	getContent()
	{
		ScrollPanel scroller = new ScrollPanel();
		scroller.setStyleName("list-popup-scroll-panel");
		VerticalPanel table = new VerticalPanel();
		VerticalPanel scrolledTable = new VerticalPanel();
		scroller.add(scrolledTable);
//		table.setStyleName("list-control-table");
		
		/*
		HorizontalPanel header1 = new HorizontalPanel();
		header1.add(createLabel("Conference Permissions","conference-permissions-header"));
		
		Label chat1 = createLabel("Chat","conference-chat-button-header");
		header1.add(chat1);
		Label audio1 = createLabel("Audio","conference-chat-button-header");
		header1.add(audio1);
		Label video1 = createLabel("Video","conference-chat-button-header");
		header1.add(video1);
		
		header1.addStyleName("common-dialog-row");
		table.add(header1);
		
		HorizontalPanel defaultsRow = new HorizontalPanel();
		defaultsRow.addStyleName("common-dialog-row");
		Label text1 = createTextHTML("Let all attendees to:",
					"conference-permissions-comment-1");
		defaultsRow.add(text1);
		
		HorizontalPanel hp21 = new HorizontalPanel();
		CheckBox cb11 = new CheckBox();
		hp21.add(cb11);
		hp21.setStyleName("user-chat-button");
		hp21.setCellHorizontalAlignment(cb11,HorizontalPanel.ALIGN_LEFT);
		hp21.setCellVerticalAlignment(cb11,VerticalPanel.ALIGN_MIDDLE);
		defaultsRow.add(hp21);
		defaultsRow.setCellHorizontalAlignment(hp21,HorizontalPanel.ALIGN_LEFT);
		defaultsRow.setCellVerticalAlignment(hp21,VerticalPanel.ALIGN_MIDDLE);
		
		HorizontalPanel hp31 = new HorizontalPanel();
		CheckBox cb21 = new CheckBox();
		hp31.add(cb21);
		hp31.setStyleName("user-chat-button");
		hp31.setCellHorizontalAlignment(cb21,HorizontalPanel.ALIGN_LEFT);
		hp31.setCellVerticalAlignment(cb21,VerticalPanel.ALIGN_MIDDLE);
		defaultsRow.add(hp31);
		defaultsRow.setCellHorizontalAlignment(hp31,HorizontalPanel.ALIGN_LEFT);
		defaultsRow.setCellVerticalAlignment(hp31,VerticalPanel.ALIGN_MIDDLE);
		
		HorizontalPanel hp41 = new HorizontalPanel();
		CheckBox cb31 = new CheckBox();
		hp41.add(cb31);
		hp41.setStyleName("user-chat-button");
		hp41.setCellHorizontalAlignment(cb31,HorizontalPanel.ALIGN_LEFT);
		hp41.setCellVerticalAlignment(cb31,VerticalPanel.ALIGN_MIDDLE);
		defaultsRow.add(hp41);
		defaultsRow.setCellHorizontalAlignment(hp41,HorizontalPanel.ALIGN_LEFT);
		defaultsRow.setCellVerticalAlignment(hp41,VerticalPanel.ALIGN_MIDDLE);
		
		table.add(defaultsRow);
		
		HTML line1 = new HTML("&nbsp;");
		line1.setStyleName("section-break");
		table.add(line1);
		
		HorizontalPanel header2 = new HorizontalPanel();
		header2.add(createLabel("Enable Meeting Lobby","conference-permissions-header"));
		
		HorizontalPanel hp22 = new HorizontalPanel();
		CheckBox cb12 = new CheckBox();
		hp22.add(cb12);
		hp22.setStyleName("user-chat-button");
		hp22.setCellHorizontalAlignment(cb12,HorizontalPanel.ALIGN_LEFT);
		hp22.setCellVerticalAlignment(cb12,VerticalPanel.ALIGN_MIDDLE);
		header2.add(hp22);
		header2.setCellHorizontalAlignment(hp22,HorizontalPanel.ALIGN_LEFT);
		header2.setCellVerticalAlignment(hp22,VerticalPanel.ALIGN_MIDDLE);
		
		HTML filler1 = new HTML(".");
		filler1.setStyleName("conference-button-filler");
		header2.setCellHorizontalAlignment(filler1,HorizontalPanel.ALIGN_LEFT);
		header2.setCellVerticalAlignment(filler1,VerticalPanel.ALIGN_MIDDLE);
		header2.add(filler1);
		
		HTML filler2 = new HTML(".");
		filler2.setStyleName("conference-button-filler");
		header2.setCellHorizontalAlignment(filler2,HorizontalPanel.ALIGN_LEFT);
		header2.setCellVerticalAlignment(filler2,VerticalPanel.ALIGN_MIDDLE);
		header2.add(filler2);
		
		header2.addStyleName("common-dialog-row");
		table.add(header2);
		
		HTML line2 = new HTML("&nbsp;");
		line2.setStyleName("section-break");
		table.add(line2);
		
		*/
		
		int	size = this.listModel.getListSize();
		//	Now all the users if any are available.
		if (size > 0)
		{
			UIResources  uiResources = UIResources.getUIResources();
			HorizontalPanel header = new HorizontalPanel();
			header.add(createLabel(ConferenceGlobals.getDisplayString("console.moodlabel","Mood"),"user-image-header"));
			header.add(createLabel(UIStrings.getNameLabel(),"user-name-header"));
			//header.add(createLabel(UIStrings.getEmailLabel(),"user-email-header"));
			
			//if private chat is disbaled as feature 
			if(ConferenceGlobals.privateChatEnabled)
			{
				chatHeaderLabel = createLabel(UIStrings.getChatLabel(),"user-chat-button-header");
				chatHeaderLabel.addStyleName("common-anchor-default-color");
				header.add(chatHeaderLabel);
				chatHeaderLabel.addClickListener(this);
			}
			if(!ConferenceGlobals.isPresenterAVAudioDisabled() && ConferenceGlobals.partListEnabled )
			{
				String label = UIStrings.getAudioLabel();
				if(ConferenceGlobals.isPresenterAVVideoOnly())
				{
					label = UIStrings.getVideoLabel();
					audioCheckBoxSet.setMaxChecked(userManager.getMaximumAttendeeVideos());
				}
				Label audio = createLabel(label, "user-chat-button-header");
				header.add(audio);
			}
//			Label video = createLabel("Video","user-chat-button-header");
//			header.add(video);
			
			header.addStyleName("common-dialog-row");
			table.add(header);
			table.add(scroller);
			table.setCellWidth(scroller,"100%");
			int i=0;
			for (i=0; i<size; i++)
			{
				UserListEntry ule = (UserListEntry)this.listModel.getListEntryAt(i);
				final UIRosterEntry user = ule.getUser();
				if (user.isHost())
				{
					continue;
				}
				HorizontalPanel row = new HorizontalPanel();
				
				HorizontalPanel hp1 = new HorizontalPanel();
				Image image = UserGlobals.getUserGlobals().getMoodImageUrl(user.getMood());
				hp1.add(image);
				hp1.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_CENTER);
				hp1.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);
				hp1.setStyleName("user-image");
				
				row.add(hp1);
				row.setCellHorizontalAlignment(hp1,HorizontalPanel.ALIGN_CENTER);
				row.setCellVerticalAlignment(hp1,VerticalPanel.ALIGN_MIDDLE);
				
				Label nameLabel = createTextHTML(user.getDisplayName(),"user-name");
				if(ConferenceGlobals.privateChatEnabled)
				{
					nameLabel.addClickListener(new ClickListener()
							{
								public	void	onClick(Widget sender)
								{
									NewChatController.getController().showChatPopupIfPossible(user);
									hide();
								}
							});
					nameLabel.addStyleName("common-anchor-default-color");
				}
				row.add(nameLabel);
				
				if(ConferenceGlobals.privateChatEnabled)
				{
					HorizontalPanel hp2 = new HorizontalPanel();
					CheckBox cb1 = new CheckBox();
					hp2.add(cb1);
					hp2.setStyleName("user-chat-button");
					hp2.setCellHorizontalAlignment(cb1,HorizontalPanel.ALIGN_LEFT);
					hp2.setCellVerticalAlignment(cb1,VerticalPanel.ALIGN_MIDDLE);
					row.add(hp2);
					row.setCellHorizontalAlignment(hp2,HorizontalPanel.ALIGN_LEFT);
					row.setCellVerticalAlignment(hp2,VerticalPanel.ALIGN_MIDDLE);
					cb1.setChecked(user.isChatOn());
					this.chatCheckBoxes.put(user.getUserId(),cb1);
				}
				
				if(!ConferenceGlobals.isPresenterAVAudioDisabled() && ConferenceGlobals.partListEnabled )
				{
					HorizontalPanel hp3 = new HorizontalPanel();
					CheckBox cb2 = new CheckBox();
					hp3.add(cb2);
					hp3.setStyleName("user-chat-button");
					hp3.setCellHorizontalAlignment(cb2,HorizontalPanel.ALIGN_LEFT);
					hp3.setCellVerticalAlignment(cb2,VerticalPanel.ALIGN_MIDDLE);
					row.add(hp3);
					row.setCellHorizontalAlignment(hp3,HorizontalPanel.ALIGN_LEFT);
					row.setCellVerticalAlignment(hp3,VerticalPanel.ALIGN_MIDDLE);
					if(user.isVideoOn() && !ConferenceGlobals.isPresenterAVVideoOnly())
					{
						cb2.setChecked(user.isVideoOn());
						cb2.setEnabled(false);
					}
					else
					{
						cb2.setChecked(user.isAudioOn());
						if(ConferenceGlobals.isPresenterAVVideoOnly())
						{
							cb2.setChecked(user.isVideoOn());
						}
						this.audioCheckBoxes.put(user.getUserId(),cb2);
						this.audioCheckBoxSet.addCheckBox(cb2);
					}
				}
				/*
				HorizontalPanel hp4 = new HorizontalPanel();
				CheckBox cb3 = new CheckBox();
				hp4.add(cb3);
				hp4.setStyleName("user-chat-button");
				hp4.setCellHorizontalAlignment(cb3,HorizontalPanel.ALIGN_LEFT);
				hp4.setCellVerticalAlignment(cb3,VerticalPanel.ALIGN_MIDDLE);
				row.add(hp4);
				row.setCellHorizontalAlignment(hp4,HorizontalPanel.ALIGN_LEFT);
				row.setCellVerticalAlignment(hp4,VerticalPanel.ALIGN_MIDDLE);
				cb3.setChecked(user.isVideoOn());
				this.videoCheckBoxes.put(user.getUserId(),cb3);
				*/
				
				row.addStyleName("common-dialog-row");
				scrolledTable.add(row);
			}
		}
		else
		{
			HorizontalPanel header2 = new HorizontalPanel();
			header2.add(createLabel(UIStrings.getNoParticipantsMessage(),"conference-permissions-header"));
			
			table.add(header2);
		}
		return	table;
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
		
		int	size = this.listModel.getListSize();
		//	Now all the users if any are available.
		if (size > 0)
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
			if (this.chatCheckBoxes != null)
			{
				RosterModel rosterModel = ClientModel.getClientModel().getRosterModel();
				ArrayList users = rosterModel.getRoster();
				int size = users.size();
				for (int i=0; i<size; i++)
				{
					UIRosterEntry user = (UIRosterEntry)users.get(i);
					CheckBox cb1 = (CheckBox)this.chatCheckBoxes.get(user.getUserId());
					if (cb1 != null)
					{
						if (cb1.isChecked() && !user.isChatOn())
						{
							this.userManager.enableChatFor(user.getUserId());
						}
						else if (!cb1.isChecked() && user.isChatOn())
						{
							this.userManager.disableChatFor(user.getUserId());
						}
					}
					if(!ConferenceGlobals.isPresenterAVAudioDisabled() && ConferenceGlobals.partListEnabled )
					{
					CheckBox cb2 = (CheckBox)this.audioCheckBoxes.get(user.getUserId());
					if (cb2 != null)
					{
						if(ConferenceGlobals.isPresenterAVVideoOnly())
						{
							if (cb2.isChecked() && !user.isVideoOn())
							{
								this.userManager.enableVideoFor(user.getUserId());
							}else if (!cb2.isChecked() && user.isVideoOn())
							{
								this.userManager.disableVideoFor(user.getUserId());
							}	
						}else
						{
							if (cb2.isChecked() && !user.isAudioOn())
							{
								this.userManager.enableAudioFor(user.getUserId());
							}
							else if (!cb2.isChecked() && user.isAudioOn())
							{
								this.userManager.disableAudioFor(user.getUserId());
							}
						}
					}
					}
					/*
					CheckBox cb3 = (CheckBox)this.videoCheckBoxes.get(user.getUserId());
					if (cb3 != null)
					{
						if (cb3.isChecked() && !user.isVideoOn())
						{
							this.userManager.enableVideoFor(user.getUserId());
						}
						else if (!cb3.isChecked() && user.isVideoOn())
						{
							this.userManager.disableVideoFor(user.getUserId());
						}
					}
					*/
				}
				hide();
			}
		}
		else if (w == this.chatHeaderLabel)
		{
			allChatEnabled = !allChatEnabled;
//			Window.alert("Chat Permission:"+allChatEnabled);
			Iterator iter = this.chatCheckBoxes.values().iterator();
			while (iter.hasNext())
			{
				CheckBox cb = (CheckBox)iter.next();
				cb.setChecked(allChatEnabled);
				if (cb.isEnabled())
				{
					/*
					if (allDeleteChecked)
					{
						this.checkedCount++;
					}
					else
					{
						this.checkedCount--;
					}
					*/
				}
			}
		}
	}
}
