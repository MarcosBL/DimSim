package com.dimdim.conference.ui.layout.client.widget;

import java.util.Vector;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.google.gwt.user.client.ui.Widget;

public class NotificationsDialog extends CommonModalDialog	{

	
	private RoundedPanel pendingTasks;


	public	NotificationsDialog(RoundedPanel pendingTasks)
	{
		super(UIStrings.getPendingTasksLabel());
		this.pendingTasks = pendingTasks;
	}
	
	protected Widget getContent() {
		pendingTasks.setVisible(true);
		return pendingTasks;
	}

	
	protected Vector getFooterButtons() {
		// TODO Auto-generated method stub
		return null;
	}

}
