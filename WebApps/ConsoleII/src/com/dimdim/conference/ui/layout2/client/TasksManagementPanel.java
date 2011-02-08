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

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.layout.MultipleListsControlDialog;
import com.dimdim.conference.ui.common.client.list.DefaultMultipleListsBrowseControl;
import com.dimdim.conference.ui.common.client.list.ListModelListener;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.common.client.list.ListPanelsContainer;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.dimdim.conference.ui.user.client.email.EmailTaskControlsProvider;
import com.dimdim.conference.ui.user.client.email.EmailTaskList;
import com.dimdim.conference.ui.user.client.email.EmailTaskPropertiesProvider;
import com.dimdim.conference.ui.user.client.meetinglobby.MeetingLobbyTaskControlsProvider;
import com.dimdim.conference.ui.user.client.meetinglobby.MeetingLobbyTaskList;
import com.dimdim.conference.ui.user.client.meetinglobby.MeetingLobbyTaskPropertiesProvider;
import com.dimdim.conference.ui.user.client.questiontask.QuestionTaskControlsProvider;
import com.dimdim.conference.ui.user.client.questiontask.QuestionTaskList;
import com.dimdim.conference.ui.user.client.questiontask.QuestionTaskPropertiesProvider;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class TasksManagementPanel extends Composite
{
	protected	UIRosterEntry		me;
	
	protected	UserRosterManager		userManager;
	
	protected	QuestionTaskList				questionTaskList;
	protected	QuestionTaskControlsProvider		questionTaskControlsProvider;
	protected	QuestionTaskPropertiesProvider		questionTaskPropertiesProvider;
	protected	ListPanel		questionTaskListPanel;
	
	protected	EmailTaskList				emailTaskList;
	protected	EmailTaskControlsProvider		emailTaskControlsProvider;
	protected	EmailTaskPropertiesProvider		emailTaskPropertiesProvider;
	protected	ListPanel		emailTaskListPanel;
	
	protected	MeetingLobbyTaskList				meetingLobbyTaskList;
	protected	MeetingLobbyTaskControlsProvider	meetingLobbyTaskControlsProvider;
	protected	MeetingLobbyTaskPropertiesProvider	meetingLobbyTaskPropertiesProvider;
	protected	ListPanel		meetingLobbyListPanel;
	
	protected	ListPanelsContainer	listContainer;
	protected	DefaultMultipleListsBrowseControl	listBrowseControl;
	
	public	TasksManagementPanel(UIRosterEntry currentUser)
	{
		this.me = currentUser;
		this.userManager = new UserRosterManager(currentUser);
		
		this.questionTaskControlsProvider = new QuestionTaskControlsProvider(currentUser,userManager);
		this.questionTaskPropertiesProvider = new QuestionTaskPropertiesProvider(currentUser,userManager);
		this.questionTaskList = new QuestionTaskList(this.questionTaskControlsProvider,
				this.questionTaskPropertiesProvider);
		this.questionTaskListPanel = new ListPanel(this.questionTaskList);
		
		this.emailTaskControlsProvider = new EmailTaskControlsProvider(currentUser,userManager);
		this.emailTaskPropertiesProvider = new EmailTaskPropertiesProvider(currentUser,userManager);
		this.emailTaskList = new EmailTaskList(this.emailTaskControlsProvider,
				this.emailTaskPropertiesProvider);
		this.emailTaskListPanel = new ListPanel(this.emailTaskList);
		
		this.meetingLobbyTaskControlsProvider = new MeetingLobbyTaskControlsProvider(currentUser,userManager);
		this.meetingLobbyTaskPropertiesProvider = new MeetingLobbyTaskPropertiesProvider(currentUser,userManager);
		this.meetingLobbyTaskList = new MeetingLobbyTaskList(userManager,
				this.meetingLobbyTaskControlsProvider,
				this.meetingLobbyTaskPropertiesProvider);
		this.meetingLobbyListPanel = new ListPanel(this.meetingLobbyTaskList);
		
		String allLink = UIGlobals.getListPanelAllLinkLabel(ClientModel.
				getClientModel().getRosterModel().getCurrentUser());
		String lhsLink = null;
		ClickListener lhsListener = null;
		
		listContainer = new ListPanelsContainer(lhsLink,null,lhsListener,
				allLink,ConferenceGlobals.getTooltip("manage_task_link"),getListPanelRhsLinkClickListener());
		
		listContainer.addListPanel1(questionTaskListPanel);
		listContainer.addListPanel2(meetingLobbyListPanel);
		listContainer.addListPanel3(emailTaskListPanel);
		
		listBrowseControl = new DefaultMultipleListsBrowseControl(questionTaskListPanel.getListBrowseControl());
		listBrowseControl.setList2BrowseControl(meetingLobbyListPanel.getListBrowseControl());
		
		initWidget(listContainer);
	}
	public	void	addListModelListenerToAllLists(ListModelListener listModelListener)
	{
		this.questionTaskList.addListModelListener(listModelListener);
		this.meetingLobbyTaskList.addListModelListener(listModelListener);
		this.emailTaskList.addListModelListener(listModelListener);
	}
	public	int	getNumberOfTasks()
	{
		return	this.questionTaskList.getListSize() + this.meetingLobbyTaskList.getListSize() +
			this.emailTaskList.getListSize();
	}
	public	DefaultMultipleListsBrowseControl	getListsBrowseControl()
	{
		return	this.listBrowseControl;
	}

	protected	ClickListener	getListPanelRhsLinkClickListener()
	{
		return new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				if (getNumberOfTasks() > 0)
				{
//					DialogsTracker.showDialog(DefaultCommonDialog.createDialog("Participants",
//							"There are no participants"));
					MultipleListsControlDialog mldc = new MultipleListsControlDialog(ConferenceGlobals.getDisplayString("workspace.notifications","Notifications"));
					if (questionTaskList.getListSize() > 0)
					{
						mldc.addList(questionTaskList);
					}
					if (meetingLobbyTaskList.getListSize() > 0)
					{
						mldc.addList(meetingLobbyTaskList);
					}
					if (emailTaskList.getListSize() > 0)
					{
						mldc.addList(emailTaskList);
					}
					mldc.drawDialog();
				}
				else
				{
					DefaultCommonDialog dlg = DefaultCommonDialog.createDialog("Tasks",
								"There are no pending tasks");
					dlg.drawDialog();
				}
			}
		};
	}
}
