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

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIServerResponse;
import com.dimdim.conference.ui.managers.client.common.FeatureManager;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.CommandExecListener;
import com.dimdim.conference.ui.model.client.ResourceModel;
import com.google.gwt.user.client.Window;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The resource manager does all the work involved in resource control, which
 * is creating, sharing, renaming, importing and deleting resources. This class
 * receives control upon all the button and short cut links clicks and does
 * the work there after.
 * 
 * The resource manager contains the workspace manager and 
 */

public class ResourceManager	extends	FeatureManager	implements	CommandExecListener
{
	protected	UIRosterEntry	me;
	protected	ResourceModel	resourceModel;
	protected	int		counter = 0;
	protected	String	successMessage = "";
	
	protected	ResourceSharingController	sharingController;
	protected	boolean		shareNextAddedResource = false;
	UserCallbacks ucb = null;
	
	public	ResourceManager(UIRosterEntry me, UserCallbacks ucb )
	{
		this.me = me;
		this.ucb = ucb;
		this.resourceModel = ClientModel.getClientModel().getResourceModel();
		this.resourceModel.setCommandExecListener(this);
		
		this.sharingController = new ResourceSharingController(me,this);
	}
	public	ResourceSharingController	getSharingController()
	{
		return	this.sharingController;
	}
	public	boolean	allowResourceControl()
	{
		return	UIGlobals.isOrganizer(this.me) || UIGlobals.isActivePresenter(this.me);
	}
	public	void	createNewResource()
	{
		this.resourceModel.createResource("Item "+(counter++),UIConstants.RESOURCE_TYPE_DEFAULT,null,null);
	}
	
	public	void	createNewResource(String name, String type, String mediaId, String appHandle)
	{
		this.shareNextAddedResource = true;
		this.resourceModel.createResource(name,type,mediaId,appHandle);
	}
	
	public	void	createNewCobResource(String url, String confAddress)
	{
		//Window.alert("addding resource...");
		this.setProgressMessage("Adding "+url);
		this.shareNextAddedResource = true;
		this.resourceModel.createCobResource(url, confAddress);
	}
	
	public	void	resourceAdded(UIResourceObject res)
	{
		if (this.shareNextAddedResource)
		{
			this.sharingController.startSharingIfNotActive(res);
			this.shareNextAddedResource = false;
		}
	}
	public	void	renameResource(UIResourceObject res, String newName)
	{
		if (res.getOwnerId().equalsIgnoreCase("SYSTEM"))
		{
//			Window.alert("System resource cannot be renamed. "+
//					"Once the resource creation is complete, this link will be hidden,");
		}
		else
		{
			this.successMessage = "Item renamed";
//			CommandExecWaiter.getWaiter().showWaitPopup("Renaming "+res.getResourceName());
			this.setProgressMessage("Renaming "+res.getResourceName());
			this.resourceModel.renameResource(res,newName);
		}
	}
	public	void	deleteResource(UIResourceObject res)
	{
		if (res.getOwnerId().equalsIgnoreCase("SYSTEM"))
		{
			Window.alert("System resource cannot be deleted. "+
					"Once the resource creation is complete, this link will be hidden,");
		}
		else
		{
			this.successMessage = "Item deleted";
//			CommandExecWaiter.getWaiter().showWaitPopup("Deleting "+res.getResourceName());
			this.setProgressMessage("Deleting "+res.getResourceName());
			this.resourceModel.deleteResource(res);
		}
	}
	public void onExecComplete(UIServerResponse serverResponse)
	{
		if (serverResponse.isSuccess())
		{
//			CommandExecWaiter.getWaiter().showMessageAndClose(this.successMessage);
			this.commandExecSuccess(this.successMessage);
			this.setProgressListener(null);
		}
		else
		{
//			CommandExecWaiter.getWaiter().showMessageAndClose(serverResponse.getMessageText());
			this.commandExecError(serverResponse.getMessageText());
		}
	}
	public UserCallbacks getUcb() {
		return ucb;
	}
}
