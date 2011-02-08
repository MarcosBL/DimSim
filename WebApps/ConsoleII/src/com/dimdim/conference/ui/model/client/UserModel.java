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

package com.dimdim.conference.ui.model.client;

import	java.util.HashMap;
import java.util.Iterator;
import	java.util.Vector;

import	com.dimdim.conference.ui.json.client.UIAttendeePermissions;
import	com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * User model refers to the current user. This object keeps track of the login
 * id used and also watches for the logged in event. This model is used by the
 * UI widgets to appropriately enable and disable available functionality.
 */
public class UserModel	extends	UISubModel
{
	protected	String		loginId;
	protected	Vector		roleChangeListeners;
	
	protected	UIAttendeePermissions	localUserPermissions;
	protected	UIRosterEntry			currentUser;
	
//	public	UserModel()
//	{
//		this.currentUser = new UIRosterEntry();
//		
//		this.currentUser.setUserId(getUserId());
//		this.currentUser.setRole(getUserRole());
//		this.currentUser.setDisplayName(getUserName());
//		
//		String mood = getUserMood();
//		this.currentUser.setMood(mood);
//		this.currentUser.setPresence("on-line");
//		
//		this.localUserPermissions = new UIAttendeePermissions();
//		this.roleChangeListeners = new Vector();
//		this.setLoginId(getUserId());
//	}
//	public	void	leaveConference()
//	{
//		String url = this.commandsFactory.getLeaveConferenceURL();
//		super.executeCommand(url);
//	}
	/*
	public String getLoginId()
	{
		return this.loginId;
	}
	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}
	
	public UIAttendeePermissions getLocalUserPermissions()
	{
		return this.localUserPermissions;
	}
	public void setLocalUserPermissions(UIAttendeePermissions localUserPermissions)
	{
		this.localUserPermissions = localUserPermissions;
	}
	public	void	setPermissions(UIAttendeePermissions perms)
	{
		this.localUserPermissions = perms;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
//			((RosterModelListener)iter.next()).onPermissionsChanged(this.localUserPermissions);
		}
		Iterator iter2 = this.roleChangeListeners.iterator();
		while (iter2.hasNext())
		{
		}
	}
	
	public	UIRosterEntry	getCurrentUser()
	{
		return	this.currentUser;
	}
	public	void	setCurrentUser(UIRosterEntry user)
	{
		this.currentUser.refreshWithChanges(user);
		String role = this.currentUser.getRole();
		if (role.equals(UIRosterEntry.ROLE_ACTIVE_PRESENTER))
		{
			this.setPermissions(UIAttendeePermissions.getActivePresenterPermissions());
		}
		else if (role.equals(UIRosterEntry.ROLE_PRESENTER))
		{
			this.setPermissions(UIAttendeePermissions.getPresenterPermissions());
		}
		else
		{
			this.setPermissions(new UIAttendeePermissions());
		}
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((UserModelListener)iter.next()).localUserChanged(this.currentUser);
		}
	}
    private String getUserId()
    { 
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_id");
	}
    private String getUserName()
    { 
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_name");
	}
    private String getUserMood()
    { 
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_mood");
	}
    private String getUserRole()
    { 
		return ConferenceGlobals.userInfoDictionary.getStringValue("user_role");
	}
    */
}
