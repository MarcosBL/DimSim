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

package com.dimdim.conference.ui.layout.client.widget;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.common.client.list.ListPanelsContainer;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.common.client.user.UserListPropertiesProvider;
import com.dimdim.conference.ui.dialogues.client.InvitationPreviewDialog;
import com.dimdim.conference.ui.dialogues.client.common.CommonConsoleHelpers;
import com.dimdim.conference.ui.dialogues.client.common.PermissionsControlDialog;
import com.dimdim.conference.ui.dialogues.client.common.UserControlDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.user.client.ActiveAudiosManager;
import com.dimdim.conference.ui.user.client.InvitationsManager;
import com.dimdim.conference.ui.user.client.UserList;
import com.dimdim.conference.ui.user.client.UserListControlsProvider;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserRoster extends CommonRoster
{
	protected	ActiveAudiosManager	activeAudiosManager;
	
	protected	UserRosterManager		userManager;
	protected	UserList				userList;
	protected	ListControlsProvider	userListControlsProvider;
	protected	ListPropertiesProvider	userListPropertiesProvider;
	
//	protected	ListPanelsContainer	listContainer;
	
	public	UserRoster(UIRosterEntry currentUser, UserCallbacks userCallBacks)
	{
		super(currentUser);
		this.userManager = new UserRosterManager(currentUser);
		this.userListControlsProvider = new UserListControlsProvider(currentUser,userManager, userCallBacks);
		this.userListPropertiesProvider = new UserListPropertiesProvider(currentUser);
		this.userList = new UserList(me,this.userListControlsProvider,
				this.userListPropertiesProvider, userCallBacks);
		
		this.listPanel = new ListPanel(this.userList);
		
		String allLink = UIGlobals.getListPanelAllLinkLabel(ClientModel.
				getClientModel().getRosterModel().getCurrentUser());
		String inviteLink = null;
		ClickListener inviteListener = null;
		String rightLinkToolTip = ConferenceGlobals.getTooltip("show_all_link");
		if (UIGlobals.isPresenter(me))
		{
			inviteLink = UIStrings.getInviteLinkLabel();
			inviteListener = getListPanelLhsLinkClickListener();
			rightLinkToolTip = ConferenceGlobals.getTooltip("manage_participant_link");
		}
		
		super.createLinks(inviteLink,ConferenceGlobals.getTooltip("invite_link"),inviteListener,
				allLink, rightLinkToolTip,getListPanelRhsLinkClickListener());
		
//		listContainer = new ListPanelsContainer(inviteLink,ConferenceGlobals.getTooltip("invite_link"),inviteListener,
//				allLink,ConferenceGlobals.getTooltip("manage_participant_link"),getListPanelRhsLinkClickListener());
//		listContainer.addListPanel1(listPanel);
		
		this.activeAudiosManager = ActiveAudiosManager.getManager(currentUser);
		
//		initWidget(listContainer);
	}
	
//	public Label getLhsLink() {
//		return listContainer.getLhsLink();
//	}
//	public Label getRhsLink() {
//		return listContainer.getRhsLink();
//	}
	
	public	UserList	getUserList()
	{
		return	this.userList;
	}
	public	ListPanel	getUsersListPanel()
	{
		return	this.listPanel;
	}
	protected	ClickListener	getListPanelRhsLinkClickListener()
	{
		return new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				if (userList.getListSize() == 1)
				{
//					DialogsTracker.showDialog(DefaultCommonDialog.createDialog("Participants",
//							"There are no participants"));
					DefaultCommonDialog dlg = DefaultCommonDialog.createDialog(
							UIStrings.getPermissionsLabel(),
							UIStrings.getNoParticipantsMessage());
					dlg.drawDialog();
				}
				else
				{
					//only organizer will be given the right to manage users
					if (UIGlobals.isOrganizer(me))
					{
//						DialogsTracker.showDialog(new PermissionsControlDialog(
//								userManager, userList));
						PermissionsControlDialog dlg = new PermissionsControlDialog(
								userManager, userList);
						dlg.drawDialog();
					}
					else
					{
//						DialogsTracker.showDialog(new UserControlDialog(userList, false));
						UserControlDialog dlg = new UserControlDialog(me,userList, false);
						dlg.drawDialog();
					}
				}
			}
		};
	}
	protected	ClickListener	getListPanelLhsLinkClickListener()
	{
		return new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
			    	InvitationsManager invitationsManager;
			    	invitationsManager = new InvitationsManager(userManager);
			    	CommonConsoleHelpers.setInvitationsManager(invitationsManager);
			    	
			    	InvitationPreviewDialog dlg = new InvitationPreviewDialog(invitationsManager,"", "");
				dlg.drawDialog();
				//SendInvitationDialog dlg = new SendInvitationDialog(userManager,me);
				//dlg.drawDialog();
//				dlg.show();
			}
		};
	}
	public	ActiveAudiosManager	getActiveAudiosManager()
	{
		return	this.activeAudiosManager;
	}

	public UserRosterManager getUserManager() {
		return userManager;
	}
	
}
