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

import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryPropertiesProvider;
import com.dimdim.conference.ui.common.client.UIGlobals;

/**
 * This list entry is used to show users in the roster where click on the icons is disabled while adding to panel
 * One should use UserListEntryWithClicks if the panel should have cliks enabled, like meeting lobby task lists
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserListEntry	extends	ListEntry
{
	protected	UIRosterEntry	user;
	
	public	UserListEntry(UIRosterEntry user,
		ListEntryControlsProvider	listEntryControlsProvider,
		ListEntryPropertiesProvider	listEntryPropertiesProvider)
	{
		super(user.getUserId(),user.getDisplayName(),
				listEntryControlsProvider,listEntryPropertiesProvider);
		this.user = user;
	}
	public	UIRosterEntry	getUser()
	{
		return	this.user;
	}
}
