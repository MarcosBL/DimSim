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

package com.dimdim.conference.ui.user.client.email;

import com.google.gwt.user.client.Window;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.common.client.list.ListControlPanel;
import com.dimdim.conference.ui.common.client.UserGlobals;

import com.dimdim.conference.ui.json.client.UIEmailAttemptResult;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.RosterModelListener;
import com.dimdim.conference.ui.common.client.user.UserListEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EmailTaskList extends DefaultList implements RosterModelListener
{
	protected	int	counter = 0;
	
	public EmailTaskList(ListControlsProvider listControlsProvider,
			ListPropertiesProvider listPropertiesProvider)
	{
		super(-1,UserGlobals.getUserGlobals().getMaxVisibleParticipants(),5,
				listControlsProvider,listPropertiesProvider);
		
		ClientModel.getClientModel().getRosterModel().addListener(this);
	}
	public void onEmailError(UIEmailAttemptResult emailResult)
	{
//		Window.alert(emailResult.toString());
		String id = ""+(counter++);
		emailResult.setId(id);
		ListEntry listEntry = new EmailTaskListEntry(emailResult,
				this.listControlsProvider.getListEntryControlsProvider(emailResult),
				this.listPropertiesProvider.getListEntryPropertiesProvider(emailResult));
		
		this.addEntry(listEntry);
	}
	public void onEmailOK(UIEmailAttemptResult emailResult)
	{
//		Window.alert(emailResult.toString());
	}
	public void onUserChanged(UIRosterEntry user)
	{
	}
	public void onUserLeft(UIRosterEntry user)
	{
	}
	public void onUserRemoved(UIRosterEntry user)
	{
	}
	public ListControlPanel getListControlPanel()
	{
		return	new EmailControlPanel(this);
	}
	/**
	 * No op implementations. No actions are required for this listener.
	 */
	public void onUserRoleChanged(UIRosterEntry user)
	{
	}
	public void onUserJoined(UIRosterEntry newUser)
	{
	}
	public void onUserRejoined(UIRosterEntry newUser)
	{
	}
	public void onPermissionsChanged(UIRosterEntry user)
	{
	}
	public	void	onChatPermissionsChanged(UIRosterEntry user)
	{
		
	}
	public	void	onAudioVidoPermissionsChanged(UIRosterEntry user)
	{
		
	}
	public void onEntryDenied(UIRosterEntry newUser)
	{
	}
	public void onEntryGranted(UIRosterEntry newUser)
	{
	}
	public void onUserArrived(UIRosterEntry newUser)
	{
	}
}
