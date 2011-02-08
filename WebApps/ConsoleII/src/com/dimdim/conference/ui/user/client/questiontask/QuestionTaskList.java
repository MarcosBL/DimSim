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

package com.dimdim.conference.ui.user.client.questiontask;

import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListControlPanel;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIEmailAttemptResult;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.RosterModelListener;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class QuestionTaskList extends DefaultList implements RosterModelListener
{
	public QuestionTaskList(ListControlsProvider listControlsProvider,
			ListPropertiesProvider listPropertiesProvider)
	{
		super(-1,UserGlobals.getUserGlobals().getMaxVisibleParticipants(),5,
				listControlsProvider,listPropertiesProvider);
		
		ClientModel.getClientModel().getRosterModel().addListener(this);
	}
	public void onUserChanged(UIRosterEntry user)
	{
		//String mood = user.getMood();
		/*if (mood.equals(UserGlobals.Question))
		{
			ListEntry listEntry = new UserListEntryWithClicks(user,
					this.listControlsProvider.getListEntryControlsProvider(user),
					this.listPropertiesProvider.getListEntryPropertiesProvider(user));
			
			this.addEntry(listEntry);
		}
		else
		{*/
			this.removeEntry(user.getUserId());
		//}
	}
	public void onUserLeft(UIRosterEntry user)
	{
		this.removeEntry(user.getUserId());
	}
	public void onUserRemoved(UIRosterEntry user)
	{
		this.removeEntry(user.getUserId());
	}
	public ListControlPanel getListControlPanel()
	{
		return	new QuestionsControlPanel(this);
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
	public void onEmailError(UIEmailAttemptResult emailResult)
	{
	}
	public void onEmailOK(UIEmailAttemptResult emailResult)
	{
	}
}
