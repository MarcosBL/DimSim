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

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.user.NewChatController;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
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

public class UserControlDialog	extends	CommonModalDialog
{
	protected	Button	applyButton;
	protected	DefaultList	listModel;
	protected	UIRosterEntry	me;
//	protected	UIListEntryManager		userListEntryManager;
//	protected	UserListAndEntryProperties	userListAndEntryProperties;
	protected	boolean		allowUserControl;
	
	public	UserControlDialog(UIRosterEntry me, DefaultList listModel, boolean allowUserControl)
	{
		super(UIStrings.getParticipantsLabel());
		this.me = me;
		this.listModel = listModel;
//		this.userListEntryManager = userListEntryManager;
//		this.userListAndEntryProperties = userListAndEntryProperties;
		this.allowUserControl = allowUserControl;
		this.addStyleName("user-control-dialog-box");
		this.closeButtonText = UIStrings.getOKLabel();
	}
	protected	Widget	getContent()
	{
		ScrollPanel scroller = new ScrollPanel();
		scroller.setStyleName("list-popup-scroll-panel");
		VerticalPanel table = new VerticalPanel();
		VerticalPanel scrolledTable = new VerticalPanel();
		scroller.add(scrolledTable);
//		table.setStyleName("list-control-table");
		
		//UIResources  uiResources = UIResources.getUIResources();
		//String	subject = uiResources.getConferenceInfo("subject");
		int	size = this.listModel.getListSize();
		int i=0;
		if (size >0)
		{
			HorizontalPanel header = new HorizontalPanel();
			header.add(createLabel(ConferenceGlobals.getDisplayString("console.moodlabel","Mood"),"user-image-header"));
			header.add(createLabel(UIStrings.getNameLabel(),"user-name-header"));
			//header.add(createLabel(UIStrings.getEmailLabel(),"user-email-header"));
			
			if (this.allowUserControl)
			{
				Label chat = createLabel(UIStrings.getChatLabel(),"user-chat-button-header");
				chat.addStyleName("common-anchor-default-color");
				header.add(chat);
				String label = UIStrings.getAudioLabel();
				if(ConferenceGlobals.isPresenterAVVideoOnly())
				{
					label = UIStrings.getVideoLabel();
				}
				Label audio = createLabel(label,"user-chat-button-header");
				audio.addStyleName("common-anchor-default-color");
				header.add(audio);

			}
			header.addStyleName("common-dialog-row");
			table.add(header);
			table.add(scroller);
			table.setCellWidth(scroller,"100%");
			
			for (i=0; i<size; i++)
			{
				UserListEntry ule = (UserListEntry)this.listModel.getListEntryAt(i);
				final UIRosterEntry user = ule.getUser();
//				UIListEntryPanelMouseAndClickListener mcl = new UIListEntryPanelMouseAndClickListener(
//						ule,this.userListAndEntryProperties,this.userListEntryManager);
//				mcl.setSecondLevelPopup(true);
				
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
				
	//			table.setWidget((i+1), 0, image);
				Label nameLabel = createTextLabel(user.getDisplayName(),"user-name");
				if (!me.getUserId().equals(user.getUserId()))
				{
					if(ConferenceGlobals.privateChatEnabled)
					{
						nameLabel.addStyleName("common-anchor-default-color");
						nameLabel.addClickListener(new ClickListener()
						{
							public	void	onClick(Widget sender)
							{
								NewChatController.getController().showChatPopupIfPossible(user);
								hide();
							}
						});
					}
				}
//				nameLabel.addClickListener(mcl);
//				nameLabel.addMouseListener(mcl);
				row.add(nameLabel);
				
				if (this.allowUserControl)
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
					
					HorizontalPanel hp3 = new HorizontalPanel();
					CheckBox cb2 = new CheckBox();
					hp3.add(cb2);
					hp3.setStyleName("user-chat-button");
					hp3.setCellHorizontalAlignment(cb2,HorizontalPanel.ALIGN_LEFT);
					hp3.setCellVerticalAlignment(cb2,VerticalPanel.ALIGN_MIDDLE);
					row.add(hp3);
					row.setCellHorizontalAlignment(hp3,HorizontalPanel.ALIGN_LEFT);
					row.setCellVerticalAlignment(hp3,VerticalPanel.ALIGN_MIDDLE);
					
//					HorizontalPanel hp4 = new HorizontalPanel();
//					CheckBox cb3 = new CheckBox();
//					hp4.add(cb3);
//					hp4.setStyleName("user-chat-button");
//					hp4.setCellHorizontalAlignment(cb3,HorizontalPanel.ALIGN_LEFT);
//					hp4.setCellVerticalAlignment(cb3,VerticalPanel.ALIGN_MIDDLE);
//					row.add(hp4);
//					row.setCellHorizontalAlignment(hp4,HorizontalPanel.ALIGN_LEFT);
//					row.setCellVerticalAlignment(hp4,VerticalPanel.ALIGN_MIDDLE);
				}
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
	protected	Label	createTextLabel(String text, String styleName)
	{
		Label html = new Label(text);
		html.setStyleName("common-table-text");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	HTML	createTextHTML(String text, String styleName)
	{
		HTML html = new HTML(text);
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
		if (this.allowUserControl)
		{
			applyButton = new Button();
			applyButton.setText(UIStrings.getOKLabel());
			applyButton.setStyleName("dm-popup-close-button");
	//		sendButton.addClickListener(this);
			v.addElement(applyButton);
		}
		return	v;
	}
}
