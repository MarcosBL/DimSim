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

package com.dimdim.conference.ui.user.client.email;

import com.dimdim.conference.ui.common.client.list.DefaultListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.json.client.UIEmailAttemptResult;
import com.dimdim.conference.ui.json.client.UIObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EmailTaskControlsProvider extends DefaultListEntryControlsProvider
	implements ListControlsProvider
{
	protected	ListPanel	listPanel;
	protected	UIEmailAttemptResult	emailResult;
	protected	UIRosterEntry	me;
	protected	UserRosterManager	userRosterManager;
	
	public	EmailTaskControlsProvider(UIRosterEntry me, UserRosterManager userRosterManager)
	{
		this.me = me;
		this.userRosterManager = userRosterManager;
	}
	public	EmailTaskControlsProvider(UIEmailAttemptResult emailResult,
			UIRosterEntry me, UserRosterManager userRosterManager,ListPanel listPanel)
	{
		this.emailResult = emailResult;
		this.me = me;
		this.userRosterManager = userRosterManager;
		this.listPanel = listPanel;
	}
	public void setListPanel(ListPanel listPanel)
	{
		this.listPanel = listPanel;
	}
	public ListEntryControlsProvider getListEntryControlsProvider(UIObject object)
	{
		UIEmailAttemptResult emailResult = (UIEmailAttemptResult)object;
		return new EmailTaskControlsProvider(emailResult,me,userRosterManager,listPanel);
	}
	public ClickListener getImage2ClickListener()
	{
		return	new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				listPanel.getList().removeEntry(emailResult.getId());
			}
		};
	}
}
