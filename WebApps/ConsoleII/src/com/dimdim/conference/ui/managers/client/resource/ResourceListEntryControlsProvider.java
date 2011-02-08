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
import com.dimdim.conference.ui.common.client.list.DefaultListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.user.client.ActivePresenterAVManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceListEntryControlsProvider extends DefaultListEntryControlsProvider
{
	protected	UIRosterEntry	me;
	protected	UIResourceObject	resource;
	protected	ResourceManager		resourceManager;
		
	protected	ResourceListEntryControlsProvider(UIRosterEntry me,
			ResourceManager resourceManager, UIResourceObject resource)
	{
		this.me = me;
		this.resourceManager = resourceManager;
		this.resource = resource;
		
	}
	
	
	/**
	 * Image 2 for resource is share
	 */
	public ClickListener getImage2ClickListener()
	{
		if (UIGlobals.isActivePresenter(this.me))
		{
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
					resourceManager.getSharingController().toggleSharing(resource);
				}
			};
		}
		return super.getImage2ClickListener();
	}
	/**
	 * Image 3 for resource is delete
	 */
	/*
	public ClickListener getImage3ClickListener()
	{
		if (UIGlobals.isActivePresenter(this.me))
		{
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
//					DialogsTracker.showDialog(new ResourceDeleteDialog(
//							resourceManager,listEntryPanel.getListEntry()));
					UIResourceObject currentActiveResource = ConferenceGlobals.getCurrentSharedResource();
					if (currentActiveResource == null ||
							!currentActiveResource.getResourceId().equals(resource.getResourceId()))
					{
						ResourceDeleteDialog dlg = new ResourceDeleteDialog(
								resourceManager,listEntryPanel.getListEntry());
						dlg.drawDialog();
					}
					else
					{
						String messageHeader = ConferenceGlobals.getDisplayString("delete_resource.heading","Delete Resource");
						String message = ConferenceGlobals.getDisplayString("delete_resource.sharing_in_progress.message","Sharing in progress");
						DefaultCommonDialog.showMessage(messageHeader,message);
					}
				}
			};
		}
		return super.getImage3ClickListener();
	}
	*/
	/**
	 * Image 4 for resource is rename
	 */
	/*
	public ClickListener getImage4ClickListener()
	{
		if (UIGlobals.isActivePresenter(this.me))
		{
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
//					DialogsTracker.showDialog(new ResourceRenameDialog(
//							resourceManager,listEntryPanel.getListEntry()));
					ResourceRenameDialog dlg = new ResourceRenameDialog(
								resourceManager,listEntryPanel.getListEntry());
					dlg.drawDialog();
				}
			};
		}
		return super.getImage4ClickListener();
	}
	*/
	/**
	 * Name label click is 
	 */
	public ClickListener getNameLabelClickListener()
	{
		//Window.alert("insdie getNameLabelClickListener me = "+me.getDisplayName());
		//if (UIGlobals.isActivePresenter(this.me))
		//{
			//Window.alert("insdie getNameLabelClickListener UIGlobals.isActivePresenter(this.me) = "+UIGlobals.isActivePresenter(this.me));
		return	new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				//Window.alert("clicked to toggle share resource = "+resource);
				
				resourceManager.getSharingController().toggleSharing(resource);
				
			}
		};
		//}
		//return super.getNameLabelClickListener();
	}
	
	private native void setLocation(String url) /*-{
		$wnd.location = url;
	}-*/;
	
}
