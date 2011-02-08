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

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.list.DefaultListEntryPropertiesProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryPropertiesProvider;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIEmailAttemptResult;
import com.dimdim.conference.ui.json.client.UIObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EmailTaskPropertiesProvider extends DefaultListEntryPropertiesProvider
	implements ListPropertiesProvider
{
	protected	ListPanel	listPanel;
	protected	UIEmailAttemptResult	emailResult;
	protected	UIRosterEntry	me;
	protected	UserRosterManager	userRosterManager;
	
	public	EmailTaskPropertiesProvider(UIRosterEntry me, UserRosterManager userRosterManager)
	{
		this.me = me;
		this.userRosterManager = userRosterManager;
	}
	public	EmailTaskPropertiesProvider(UIEmailAttemptResult emailResult,
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
	public ListEntryPropertiesProvider getListEntryPropertiesProvider(UIObject object)
	{
		UIEmailAttemptResult emailResult = (UIEmailAttemptResult)object;
		return new EmailTaskPropertiesProvider(emailResult,me,userRosterManager,listPanel);
	}
	public String getNameLabelStyle()
	{
		return "list-entry-wide-label";
	}
	public Image getImage1Url()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getEmailTaskImage1Url();
	}
	public Image getImage2Url()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getCancelEmailTaskImageUrl();
	}
	public int getNameLabelWidth()
	{
		return 40;
	}
}
