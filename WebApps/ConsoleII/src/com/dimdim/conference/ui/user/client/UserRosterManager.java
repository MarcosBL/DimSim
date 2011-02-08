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

package com.dimdim.conference.ui.user.client;

import com.google.gwt.user.client.Window;

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.resource.ResourceCallbacks;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class is responsible for top level user management. It receives all
 * the events from server as well as does the work of dispatching the commands.
 * Applicable commands for the user roster manager right now re mood changes
 * accept entry into meeting and deny entry.
 */

public class UserRosterManager
{
	protected	UIRosterEntry	me;
	protected	RosterModel		rosterModel;
	protected	ActivePresenterAVManager	presenterAVManager;
	public	UserRosterManager(UIRosterEntry me)
	{
		this.me = me;
		this.rosterModel = ClientModel.getClientModel().getRosterModel();
		this.presenterAVManager = ActivePresenterAVManager.getPresenterAVManager(me);
	}
	public ActivePresenterAVManager getPresenterAVManager()
	{
		return presenterAVManager;
	}
	public	boolean	allowUserControl()
	{
		return	UIGlobals.isOrganizer(this.me) || UIGlobals.isActivePresenter(this.me);
	}
	public	void	changeMood(String mood, boolean custom)
	{
		this.rosterModel.setMood(mood);
//		Window.alert("Changing mood for: "+me.getUserId()+":"+mood+":"+custom);
	}
	
	public	void	setDisplayName(String name)
	{
		this.rosterModel.setDisplayName(name);
	}
	
	public	void	grantEntryToUser(UIRosterEntry user)
	{
		this.rosterModel.grantAttendeeEntry(user);
	}
	public	void	denyEntryToUser(UIRosterEntry user)
	{
		this.rosterModel.denyAttendeeEntry(user);
	}
	public	void	setPhotoToDefault()
	{
		this.rosterModel.setPhotoToDefault();
	}
	public	void	enableChatForAll()
	{
		this.rosterModel.enableChatForAll();
	}
	public	void	enableChatFor(String userId)
	{
		this.rosterModel.enableChatFor(userId);
	}
	public	void	disableChatForAll()
	{
		this.rosterModel.disableChatForAll();
	}
	public	void	disableChatFor(String userId)
	{
		this.rosterModel.disableChatFor(userId);
	}
	public	void	removeUser(final UIRosterEntry user)
	{
		ConfirmationListener listener = new ConfirmationListener()
		{
			public void onCancel()
			{
			}
			public void onOK()
			{
				rosterModel.removeAttendee(user);		
			}
		};
		
		String s = UIConstants.getConstants().attendeeRemoveWarning();
		s = s.replaceAll("USER_REPLACE", user.getDisplayName());
		
		ConfirmationDialog dlg = new ConfirmationDialog(
				"Warning",s+" "+ConferenceGlobals.getDisplayString("top_bar.signout.press.yes", "Please press Yes to confirm."),
				"default-message",listener);
		dlg.drawDialog();
	}
	
	public	void	disableAudioForAll()
	{
		this.rosterModel.disableAudioForAll();
	}
	public	void	disableAudioFor(String userId)
	{
		this.rosterModel.disableAudioFor(userId);
	}
	public	int		getMaximumAttendeeAudios()
	{
		return	this.rosterModel.getMaximumAttendeeAudios();
	}
	public	int		getMaximumAttendeeVideos()
	{
		return	this.rosterModel.getMaximumAttendeeVideos();
	}
	public	int		getAvailableAttendeeAudios()
	{
		return	this.rosterModel.getAvailableAttendeeAudios();
	}
	public	int		getAvailableAttendeeVideos()
	{
		return	this.rosterModel.getAvailableAttendeeVideos();
	}
	public	boolean	canEnableAudioFor(String userId)
	{
		return	this.rosterModel.canEnableAudioFor(userId);
	}
	public	boolean	canEnableVideoFor(String userId)
	{
		return	this.rosterModel.canEnableVideoFor(userId);
	}
	public	void	enableAudioFor(String userId)
	{
		this.rosterModel.enableAudioFor(userId);
	}
	public	void	disableVideoForAll()
	{
		this.rosterModel.disableVideoForAll();
	}
	public	void	disableVideoFor(String userId)
	{
		this.rosterModel.disableVideoFor(userId);
	}
	public	void	enableVideoFor(String userId)
	{
		this.rosterModel.enableVideoFor(userId);
	}
	public	void	sendJoinInvitations(String attendees, String presenters, String message)
	{
		this.rosterModel.sendJoinInvitations(attendees, presenters, message);
	}
	public	void	sendFeedback(int rating, String comment, String toEmail)
	{
		this.rosterModel.sendFeedback(rating,comment, toEmail);
	}
	public	void	makeActivePresenter(UIRosterEntry user)
	{
		this.rosterModel.makeActivePresenter(user);
	}
	public	void	makePresenter(UIRosterEntry user)
	{
		this.rosterModel.makePresender(user);
	}
	
}

