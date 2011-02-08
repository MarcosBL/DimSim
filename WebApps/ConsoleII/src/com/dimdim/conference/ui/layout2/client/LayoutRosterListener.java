package com.dimdim.conference.ui.layout2.client;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.RosterModelAdapter;

public class LayoutRosterListener extends RosterModelAdapter{

	NewMiddlePanel panel = null;
	
	public LayoutRosterListener(NewMiddlePanel panel)
	{
		this.panel = panel; 
	}
	public	void	onUserRemoved(UIRosterEntry user)
	{
		panel.onRemovedFromConference();
	}
	public	void	onEntryDenied(UIRosterEntry user)
	{
		panel.entryDeniedTo(user);
	}
	public	void	onUserRejoined(UIRosterEntry user)
	{
		/**
		 * This listener is responsible for closing the previous
		 * console of the host.
		 */
		if (user.isHost())
		{
			panel.checkOldHostConsole(user);
		}
	}
	
	public	void	onUserRoleChanged(UIRosterEntry user)
	{
		panel.setActivePresenterWidgetsVisibility(user);

	}
	
	
}
