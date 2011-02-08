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

import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.common.client.resource.ResourceList;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ClientModel;
//import com.dimdim.conference.ui.common.client.ResourceGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PresenterResourceList extends ResourceList
{
	protected	ResourceManager	resourceManager;
	
	public PresenterResourceList(ListControlsProvider listControlsProvider,
			ListPropertiesProvider listPropertiesProvider, ResourceManager resourceManager)
	{
		super(listControlsProvider,listPropertiesProvider,false);
		this.resourceManager = resourceManager;
		
		ClientModel.getClientModel().getResourceModel().addListener(this);
	}
	public void onResourceAdded(UIResourceObject res)
	{
		super.onResourceAdded(res);
//		ResourceGlobals.getResourceGlobals().setDesktopResourceId(res);
		this.resourceManager.resourceAdded(res);
	}
}
