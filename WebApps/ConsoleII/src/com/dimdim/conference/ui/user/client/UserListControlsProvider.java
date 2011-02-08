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

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.json.client.UIObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserListControlsProvider extends DefaultListControlsProvider
{
	protected	UIRosterEntry		me;
	protected	UserRosterManager	rosterManager;
	private UserCallbacks userCallbacks;
	
	public	UserListControlsProvider(UIRosterEntry me,UserRosterManager rosterManager, UserCallbacks userCallbacks)
	{
		this.me = me;
		this.rosterManager = rosterManager;
		this.userCallbacks = userCallbacks;
	}
	/**
	 * Footer link 2 is manage link. If I am presenter then throw the manage box
	 * otherwise the simple display scroll box.
	 */
	public ListEntryControlsProvider getListEntryControlsProvider(UIObject object)
	{
		UIRosterEntry user = (UIRosterEntry)object;
		ListEntryControlsProvider returnValue = null;
		
		//Window.alert("insideUserListControlsProvider  userCallbacks = "+userCallbacks);
		if (UIGlobals.isAttendee(me))
		{
			returnValue = new AttendeeConsoleUserListEntryControlsProvider(user,me,
					user.getUserId().equals(this.me.getUserId()),this.rosterManager);
			returnValue.setUserSignoutListener(this.userCallbacks);
			
			return	returnValue;
		}
		
		returnValue = new PresenterConsoleUserListEntryControlsProvider(user,me,
				user.getUserId().equals(this.me.getUserId()),this.rosterManager); 
		returnValue.setUserSignoutListener(this.userCallbacks);
		
		return	returnValue;
	
	}
}

