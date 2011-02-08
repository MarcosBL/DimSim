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

package com.dimdim.conference.ui.user.client.questiontask;

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultListEntryPropertiesProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryPropertiesProvider;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.json.client.UIObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class QuestionTaskPropertiesProvider extends DefaultListEntryPropertiesProvider
	implements ListPropertiesProvider
{
	protected	ListPanel	listPanel;
	protected	UIRosterEntry	user;
	protected	UIRosterEntry	me;
	protected	UserRosterManager	userRosterManager;
	
	public	QuestionTaskPropertiesProvider(UIRosterEntry me, UserRosterManager userRosterManager)
	{
		this.me = me;
		this.userRosterManager = userRosterManager;
	}
	public	QuestionTaskPropertiesProvider(UIRosterEntry user,
			UIRosterEntry me, UserRosterManager userRosterManager,ListPanel listPanel)
	{
		this.user = user;
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
		UIRosterEntry user = (UIRosterEntry)object;
		return new QuestionTaskPropertiesProvider(user,me,userRosterManager,listPanel);
	}
	public Image getImage1Url()
	{
		return UserGlobals.getUserGlobals().getMoodImageUrl(user.getMood());
	}
	public Image getImage2Url()
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getCancelQuestionTaskImageUrl();
	}
	
	public String getImage1Tooltip()
	{
	    	//Window.alert("overriding the super's method");
	    	return UIStrings.getMoodToolTip(user.getMood());
		//return "getImage1Tooltip";
	}
	public String getImage2Tooltip()
	{
	    	//Window.alert("overriding the super's method");
		return UIStrings.getResolveLabel();
	}
}
