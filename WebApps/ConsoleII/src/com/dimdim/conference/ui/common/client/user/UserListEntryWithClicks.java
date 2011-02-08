package com.dimdim.conference.ui.common.client.user;

import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

public class UserListEntryWithClicks extends UserListEntry{

	public UserListEntryWithClicks(UIRosterEntry user, ListEntryControlsProvider listEntryControlsProvider, ListEntryPropertiesProvider listEntryPropertiesProvider) {
		super(user, listEntryControlsProvider, listEntryPropertiesProvider);
	}

}
