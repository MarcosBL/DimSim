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

import asquare.gwt.tk.client.ui.DropDownListener;
import asquare.gwt.tk.client.ui.DropDownPanel;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.DefaultListModelListener;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class ConsoleMiddleLeftPanel implements DropDownListener
{
	protected	UIRosterEntry	currentUser;
	
	protected	ResourceRoster	rl = null;
	protected	UserRoster		ul = null;
	
	private MainLayout consoleLayout = null;
	
	public ConsoleMiddleLeftPanel(MainLayout consoleLayout, UIRosterEntry currentUser, UserCallbacks ucb)
	{
		this.currentUser = currentUser;
		this.consoleLayout  = consoleLayout;
		
		ul = new UserRoster(currentUser, ucb);
		rl = new ResourceRoster(currentUser, ucb);
		//some resource call backs are required by the user
		ul.getUserList().setResCallBacks(rl.getResourceManager().getSharingController());
		
		consoleLayout.addWidgetToID("participants_navigation", ul.getUsersListPanel().getListBrowseControl());
		
		ul.getUserList().addListModelListener(new DefaultListModelListener()
		{
			public void listEntryAdded(ListEntry newEntry)
			{
				addParticpantsHeader(UIStrings.getParticipantsLabel()+" ("+(ul.getUserList().getListSize())+")");
			}
			public void listEntryRemoved(ListEntry removedEntry)
			{
				addParticpantsHeader(UIStrings.getParticipantsLabel()+" ("+(ul.getUserList().getListSize())+")");
			}
		});
		
		consoleLayout.addWidgetToID("leftpod_middle_users", ul.getListPanel());
		if(null != ul.getLhsLink() && ("true".equalsIgnoreCase(ConferenceGlobals.getShowInviteLinks())) )
		{
			FocusPanel fp = new FocusPanel();
			fp.add(ul.getLhsLink());
			fp.setWidth("100%");
			fp.addClickListener(ul.getListPanelLhsLinkClickListener());
			consoleLayout.addWidgetToID("users_invite", fp);
		}
		else
		{
			consoleLayout.setIDVisibility("users_invite_btn", false);
		}
		
		FocusPanel fp1 = new FocusPanel();
		fp1.add(ul.getRhsLink());
		fp1.setWidth("100%");
		fp1.addClickListener(ul.getListPanelRhsLinkClickListener());
		consoleLayout.addWidgetToID("participants_manage", fp1);
		
		if(!UIGlobals.isPresenter(currentUser))
		{
			//hide the div of show items
			consoleLayout.setIDVisibility("main_showitems", false);
		}
		
		addResourcesHeader(UIStrings.getShowItemsLabel()+" (0)");
		rl.getResourceList().addListModelListener(new DefaultListModelListener()
		{
			public void listEntryAdded(ListEntry newEntry)
			{
				addResourcesHeader(UIStrings.getShowItemsLabel()+
								" ("+(rl.getResourceList().getListSize())+")");
			}
			public void listEntryRemoved(ListEntry removedEntry)
			{
				addResourcesHeader(UIStrings.getShowItemsLabel()+
								" ("+(rl.getResourceList().getListSize())+")");
			}
		});
		setCurrentUser(currentUser);
		
		if (!UIGlobals.isActivePresenter(currentUser))
		{
			consoleLayout.setIDVisibility("main_showitems", false);
		}
		else
		{
			consoleLayout.setIDVisibility("main_showitems", true);
		}
	}
	public void setCurrentUser(UIRosterEntry currentUser)
	{
		this.currentUser = currentUser;
		rl.setCurrentUser(currentUser);
		
//		consoleLayout.addWidgetToID("show_items_navigation", rl.getResourceListPanel().getListBrowseControl());
		consoleLayout.addWidgetToID("leftpod_middle_items", rl.getResourceTypeListPanel());
		
		FocusPanel fp = new FocusPanel();
		fp.add(rl.getLhsLink());
		fp.setWidth("100%");
		fp.addClickListener(rl.getListPanelLhsLinkClickListener());
		consoleLayout.addWidgetToID("share_button_wrapper", fp);
		
		FocusPanel fp1 = new FocusPanel();
		fp1.add(rl.getRhsLink());
		fp1.setWidth("100%");
		fp1.addClickListener(rl.getListPanelRhsLinkClickListener());
		consoleLayout.addWidgetToID("items_manage", fp1);
	}
	private void addParticpantsHeader(String string)
	{
		Label lbl = new Label(string);
		lbl.setStyleName("common-text");
		lbl.addStyleName("common-bold-text");
		consoleLayout.addWidgetToID("participants", lbl);
	}
	private void addResourcesHeader(String string)
	{
		Label lbl = new Label(string);
		lbl.setStyleName("common-text");
		lbl.addStyleName("common-bold-text");
		consoleLayout.addWidgetToID("show_items", lbl);
	}
	public	ResourceRoster	getResourceRoster()
	{
		return	this.rl;
	}
	public	UserRoster	getUserRoster()
	{
		return	this.ul;
	}
	public void dropDownClosed(DropDownPanel sender)
	{
	}
	public void dropDownOpened(DropDownPanel sender)
	{
	}
	public ClickListener getShareButtonListener()
	{
		return rl.getListPanelLhsLinkClickListener();
	}
}
