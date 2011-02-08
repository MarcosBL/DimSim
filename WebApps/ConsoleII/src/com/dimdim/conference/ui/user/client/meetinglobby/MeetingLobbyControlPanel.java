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

package com.dimdim.conference.ui.user.client.meetinglobby;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListControlPanel;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.util.RadioButtonList;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MeetingLobbyControlPanel	extends ListControlPanel
{
	protected	VerticalPanel	basePanel = new VerticalPanel();
	protected	Button		applyButton;
	protected	DefaultList	listModel;
	protected	UserRosterManager	userRosterManager;
	
	protected	VerticalPanel	scrolledTable = new VerticalPanel();
	protected	Vector		rows = new Vector();
	protected	Vector		acceptBoxes = new Vector();
	protected	Vector		denyBoxes = new Vector();
	protected	Vector		users = new Vector();
	
	protected	RadioButtonList	acceptList	=	new	RadioButtonList();
	protected	RadioButtonList	denyList	=	new	RadioButtonList();
	
	public	MeetingLobbyControlPanel(UserRosterManager userRosterManager,
			DefaultList listModel)
	{
		initWidget(this.basePanel);
		this.userRosterManager = userRosterManager;
		this.listModel = listModel;
		
		this.basePanel.add(getContent());
	}
	protected	Widget	getContent()
	{
		ScrollPanel scroller = new ScrollPanel();
		scroller.setStyleName("list-popup-scroll-panel");
		VerticalPanel table = new VerticalPanel();
		scrolledTable = new VerticalPanel();
		scroller.add(scrolledTable);
//		table.setStyleName("list-control-table");
		
		HorizontalPanel header = new HorizontalPanel();
		header.add(createLabel(".","user-image-header"));
		header.add(createLabel(UIStrings.getNameLabel(),"lobby-name"));
		header.add(createLabel(UIStrings.getEmailLabel(),"lobby-email"));
		
		Label accept = createLabel(UIStrings.getAcceptLabel(),"user-chat-button-header");
		accept.addStyleName("common-anchor");
		accept.addClickListener(this.acceptList);
		header.add(accept);
		Label deny = createLabel(UIStrings.getDenyLabel(),"user-chat-button-header");
		deny.addStyleName("common-anchor");
		deny.addClickListener(this.denyList);
		header.add(deny);
		
		header.addStyleName("common-dialog-row");
		table.add(header);
		table.add(scroller);
		table.setCellWidth(scroller,"100%");
		
		int	size = this.listModel.getListSize();
		int i=0;
		for (i=0; i<size; i++)
		{
			UserListEntry ule = (UserListEntry)this.listModel.getListEntryAt(i);
			UIRosterEntry user = ule.getUser();
			this.users.addElement(user);
			
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
			
//			table.setWidget((i+1), 0, image);
			Label nameLabel = createTextHTML(user.getDisplayName(),"lobby-name");
			nameLabel.addStyleName("common-anchor");
			row.add(nameLabel);
			Label emailLabel = createTextHTML(user.getUserId(),"lobby-email");
			emailLabel.addStyleName("common-anchor");
			row.add(emailLabel);
			
			HorizontalPanel hp3 = new HorizontalPanel();
//			CheckBox cb2 = new CheckBox();
			RadioButton cb2 = new RadioButton(user.getUserId());
			hp3.add(cb2);
			hp3.setStyleName("user-chat-button");
			hp3.setCellHorizontalAlignment(cb2,HorizontalPanel.ALIGN_LEFT);
			hp3.setCellVerticalAlignment(cb2,VerticalPanel.ALIGN_MIDDLE);
			row.add(hp3);
			row.setCellHorizontalAlignment(hp3,HorizontalPanel.ALIGN_LEFT);
			row.setCellVerticalAlignment(hp3,VerticalPanel.ALIGN_MIDDLE);
			this.acceptBoxes.addElement(cb2);
			this.acceptList.addRadioButton(cb2);
			
			HorizontalPanel hp4 = new HorizontalPanel();
//			CheckBox cb3 = new CheckBox();
			RadioButton cb3 = new RadioButton(user.getUserId());
			hp4.add(cb3);
			hp4.setStyleName("user-chat-button");
			hp4.setCellHorizontalAlignment(cb3,HorizontalPanel.ALIGN_LEFT);
			hp4.setCellVerticalAlignment(cb3,VerticalPanel.ALIGN_MIDDLE);
			row.add(hp4);
			row.setCellHorizontalAlignment(hp4,HorizontalPanel.ALIGN_LEFT);
			row.setCellVerticalAlignment(hp4,VerticalPanel.ALIGN_MIDDLE);
			this.denyBoxes.addElement(cb3);
			this.denyList.addRadioButton(cb3);
			
			row.addStyleName("common-dialog-row");
			scrolledTable.add(row);
			this.rows.addElement(row);
		}
		
		return	table;
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
	public	void	applyChanges()
	{
		int	size = this.users.size();
		for (int i=size-1; i>=0; i--)
		{
			RadioButton accept = (RadioButton)this.acceptBoxes.elementAt(i);
			RadioButton deny = (RadioButton)this.denyBoxes.elementAt(i);
			if (accept.isChecked() || deny.isChecked())
			{
				UIRosterEntry user = (UIRosterEntry)this.users.elementAt(i);
				Widget row = (Widget)this.rows.elementAt(i);
				
				if (accept.isChecked())
				{
					this.userRosterManager.grantEntryToUser(user);
				}
				else
				{
					this.userRosterManager.denyEntryToUser(user);
				}
				this.scrolledTable.remove(row);
				
				this.acceptBoxes.remove(i);
				this.denyBoxes.remove(i);
				this.users.remove(i);
				this.rows.remove(i);
			}
		}
	}
	public String getPanelLabel()
	{
		return "Waiting Area";
	}
}
