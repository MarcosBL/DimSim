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

package com.dimdim.conference.ui.user.client.meetinglobby;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListControlPanel;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.user.UserListEntryWithClicks;
import com.dimdim.conference.ui.json.client.UIEmailAttemptResult;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.RosterModelListener;
import com.dimdim.conference.ui.user.client.UserRosterManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MeetingLobbyTaskList extends DefaultList implements RosterModelListener
{
	protected	UserRosterManager	userRosterManager;
	
	public MeetingLobbyTaskList(UserRosterManager userRosterManager,
			ListControlsProvider listControlsProvider,
			ListPropertiesProvider listPropertiesProvider)
	{
		super(-1,UserGlobals.getUserGlobals().getMaxVisibleParticipants(),5,
				listControlsProvider,listPropertiesProvider);
		
		this.userRosterManager = userRosterManager;
		ClientModel.getClientModel().getRosterModel().addListener(this);
	}
	public void onUserLeft(UIRosterEntry user)
	{
		this.removeEntry(user.getUserId());
	}
	public void onUserRemoved(UIRosterEntry user)
	{
		this.removeEntry(user.getUserId());
	}
	public void onEntryDenied(UIRosterEntry newUser)
	{
		this.removeEntry(newUser.getUserId());
	}
	public void onEntryGranted(UIRosterEntry newUser)
	{
		this.removeEntry(newUser.getUserId());
	}
	public void onUserArrived(UIRosterEntry newUser)
	{
		ListEntry listEntry = new UserListEntryWithClicks(newUser,
				this.listControlsProvider.getListEntryControlsProvider(newUser),
				this.listPropertiesProvider.getListEntryPropertiesProvider(newUser));
		
		this.addEntry(listEntry);
	}
	public ListControlPanel getListControlPanel()
	{
		return	new MeetingLobbyControlPanel(userRosterManager,this);
	}
	/**
	 * No op implementations. No actions are required for this listener.
	 */
	public void onUserRoleChanged(UIRosterEntry user)
	{
	}
	public void onUserJoined(UIRosterEntry newUser)
	{
		if (UIGlobals.isInLobby(newUser))
		{
			this.onUserArrived(newUser);
		}
	}
	public void onUserRejoined(UIRosterEntry newUser)
	{
	}
	public void onPermissionsChanged(UIRosterEntry user)
	{
	}
	public void onUserChanged(UIRosterEntry user)
	{
	}
	public void onEmailError(UIEmailAttemptResult emailResult)
	{
	}
	public void onEmailOK(UIEmailAttemptResult emailResult)
	{
	}
	public	void	onChatPermissionsChanged(UIRosterEntry user)
	{
	}
	public	void	onAudioVidoPermissionsChanged(UIRosterEntry user)
	{
	}
}
