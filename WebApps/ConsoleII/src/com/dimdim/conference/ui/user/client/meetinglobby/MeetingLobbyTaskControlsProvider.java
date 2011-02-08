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

import com.dimdim.conference.ui.common.client.list.DefaultListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.json.client.UIObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MeetingLobbyTaskControlsProvider extends DefaultListEntryControlsProvider
	implements ListControlsProvider
{
	protected	ListPanel	listPanel;
	protected	UIRosterEntry	user;
	protected	UIRosterEntry	me;
	protected	UserRosterManager	userRosterManager;
	
	public	MeetingLobbyTaskControlsProvider(UIRosterEntry me, UserRosterManager userRosterManager)
	{
		this.me = me;
		this.userRosterManager = userRosterManager;
	}
	public	MeetingLobbyTaskControlsProvider(UIRosterEntry user,
			UIRosterEntry me, UserRosterManager userRosterManager,ListPanel listPanel)
	{
		this.user = user;
		this.me = me;
		this.userRosterManager = userRosterManager;
		this.listPanel = listPanel;
	}
	public void setListPanel(ListPanel listPanel)
	{
		this.listPanel = listPanel;
	}
	public ListEntryControlsProvider getListEntryControlsProvider(UIObject object)
	{
		UIRosterEntry user = (UIRosterEntry)object;
		return new MeetingLobbyTaskControlsProvider(user,me,userRosterManager,listPanel);
	}
	/**
	 * Image 2 is grant entry.
	 */
	public ClickListener getImage2ClickListener()
	{
		return	new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				userRosterManager.grantEntryToUser(user);
				listPanel.getList().removeEntry(user.getUserId());
			}
		};
	}
	/**
	 * Image 3 is deny entry.
	 */
	public ClickListener getImage3ClickListener()
	{
		return	new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				userRosterManager.denyEntryToUser(user);
				listPanel.getList().removeEntry(user.getUserId());
			}
		};
	}
}
