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

package com.dimdim.conference.ui.managers.client.resource;

import com.dimdim.conference.ui.common.client.list.DefaultListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.json.client.UIObject;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceListControlsProvider extends DefaultListControlsProvider
{
	protected	UIRosterEntry	me;
	protected	ResourceManager	resourceManager;
	
	public	ResourceListControlsProvider(UIRosterEntry me, ResourceManager resourceManager)
	{
		this.me = me;
		this.resourceManager = resourceManager;
	}
	/**
	 * Footer link 2 is manage link. If I am presenter then throw the manage box
	 * otherwise the simple display scroll box.
	 */
	public ListEntryControlsProvider getListEntryControlsProvider(UIObject object)
	{
		UIResourceObject resource = (UIResourceObject)object;
		return new ResourceListEntryControlsProvider(me,resourceManager,resource);
	}
}

