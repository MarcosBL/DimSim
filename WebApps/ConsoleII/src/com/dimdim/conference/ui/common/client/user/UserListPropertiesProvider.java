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

package com.dimdim.conference.ui.common.client.user;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryMovieModel;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserListPropertiesProvider implements ListPropertiesProvider
{
	protected	UIRosterEntry	me;
	
	public UserListPropertiesProvider(UIRosterEntry me)
	{
		this.me = me;
	}
	public String getFooterLink1Text()
	{
		return null;
	}
	public String getFooterLink2Text()
	{
		return "Manage";
	}
	public ListEntryPropertiesProvider getListEntryPropertiesProvider(UIObject object)
	{
		UIRosterEntry user = (UIRosterEntry)object;
		if (!me.isHost())
		{
			return	new AttendeeConsoleUserListEntryPropertiesProvider(user,me);
		}
		return	new PresenterConsoleUserListEntryPropertiesProvider(user,me);
	}
	public String listPanelStyleName()
	{
		return null;
	}

}
