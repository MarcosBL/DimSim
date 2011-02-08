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
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.RosterModelAdapter;
import com.dimdim.conference.ui.model.client.RosterModel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DiscussionRosterListener	extends	RosterModelAdapter
{
	protected	UIRosterEntry	me;
	protected	DiscussionWidget	discussionWidget;
	
	public	DiscussionRosterListener(UIRosterEntry me, DiscussionWidget discussionWidget)
	{
		this.me = me;
		this.discussionWidget = discussionWidget;
		
		ClientModel.getClientModel().getRosterModel().addListener(this);
	}
	public	void	onUserRoleChanged(UIRosterEntry user)
	{
		if (user.getUserId().equals(me.getUserId()))
		{
			if (UIGlobals.isActivePresenter(user))
			{
				this.discussionWidget.hideAVPanel();
				this.discussionWidget.becameActivePresenter();
				this.discussionWidget.showAVPanel();
//				this.discussionWidget.getWorkspaceTabs().getLeftTabGroup().showDefaultTab();
			}
			else if(UIGlobals.isOnlyPresenter(user))
			{
				this.discussionWidget.hideAVPanel();
//				this.discussionWidget.getWorkspaceTabs().getLeftTabGroup().showDefaultTab();
			}
		}
	}
	public	void	onUserRemoved(UIRosterEntry user)
	{
		if (user.getUserId().equals(me.getUserId()))
		{
			this.discussionWidget.hideAVPanel();
		}
	}
}
