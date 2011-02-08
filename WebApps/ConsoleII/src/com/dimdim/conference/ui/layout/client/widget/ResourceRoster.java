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

package com.dimdim.conference.ui.layout.client.widget;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.common.client.list.ListPanelsContainer;
import com.dimdim.conference.ui.common.client.list.SortedListPanel;
import com.dimdim.conference.ui.common.client.resource.ResourceList;
import com.dimdim.conference.ui.common.client.resource.ResourceListPropertiesProvider;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.managers.client.resource.PresenterResourceList;
import com.dimdim.conference.ui.managers.client.resource.ResourceListControlsProvider;
import com.dimdim.conference.ui.managers.client.resource.ResourceManager;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.resources.client.ResourceControlDialog;
import com.dimdim.conference.ui.resources.client.SelectFileDialogue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceRoster extends CommonRoster
{
	protected	ResourceManager			resourceManager;
	protected	ResourceList			resourceList;
	protected	ResourceListControlsProvider	resourceListControlsProvider;
	protected	ResourceListPropertiesProvider	resourceListPropertiesProvider;
	protected	ClickListener shareLinkClickListener = null;
	protected	ClickListener allLinkClickListener;
//	protected	ListPanelsContainer	listContainer;
	UserCallbacks ucb = null;
	
	public	ResourceRoster(UIRosterEntry currentUser, UserCallbacks ucb)
	{
		super(currentUser);
		this.ucb = ucb;
		this.resourceManager = new ResourceManager(currentUser, ucb);
		this.resourceListControlsProvider = new ResourceListControlsProvider(currentUser,resourceManager);
		this.resourceListPropertiesProvider = new ResourceListPropertiesProvider();
		this.resourceList = new PresenterResourceList(this.resourceListControlsProvider,
				this.resourceListPropertiesProvider,this.resourceManager);
		this.resourceManager.getSharingController().setResourceList(this.resourceList);
		
		this.listPanel = new SortedListPanel(this.resourceList);
		
		String allLink = UIGlobals.getListPanelAllLinkLabel(ClientModel.
				getClientModel().getRosterModel().getCurrentUser());
		allLinkClickListener = this.getListPanelRhsLinkClickListener();
		
		String shareLink = UIGlobals.getListPanelShareLinkLabel(ClientModel.
				getClientModel().getRosterModel().getCurrentUser());
		shareLinkClickListener = this.getListPanelLhsLinkClickListener();
	
		super.createLinks(shareLink,ConferenceGlobals.getTooltip("share_resource_link"),shareLinkClickListener,
				allLink,ConferenceGlobals.getTooltip("manage_resource_link"),allLinkClickListener);
	
//		listContainer = new ListPanelsContainer(shareLink,ConferenceGlobals.getTooltip("share_resource_link"),shareLinkClickListener,
//			allLink,ConferenceGlobals.getTooltip("manage_resource_link"),allLinkClickListener);
//		listContainer.addListPanel1(listPanel);
		
//		initWidget(listContainer);
//		initWidget(listPanel);
	}
	public	void setCurrentUser(UIRosterEntry currentUser)
	{
		this.me = currentUser;
	}
	public	ClickListener	getAllLinkClickListener()
	{
		return	this.allLinkClickListener;
	}
	protected	ClickListener	getListPanelRhsLinkClickListener()
	{
		return new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
		    	//Window.alert("List Panel RHS Click Listener");
				if (resourceList.getListSize() == 0)
				{
					DefaultCommonDialog dlg = DefaultCommonDialog.createDialog("Show Items",
								"There are no items");
					dlg.drawDialog();
				}
				else
				{
					ResourceControlDialog rcd = new ResourceControlDialog(resourceManager,resourceList,me.isHost());
					rcd.drawDialog();
				}
			}
		};
	}
	
	protected	ClickListener	getListPanelLhsLinkClickListener()
	{
		return new ClickListener()
		{
		   // look here
			public	void	onClick(Widget sender)
			{
		    	//Window.alert("List Panel LHS Click Listener");
			    if (UIGlobals.isActivePresenter(me))
				{	   
			    	SelectFileDialogue safd = new SelectFileDialogue(resourceManager.getSharingController());
					
					resourceManager.getSharingController().showFileSelector(safd);
					safd.addPopupListener(new PopupListener()
							{
					    		public void onPopupClosed(PopupPanel sender, boolean autoClosed)
					    		{
					    		    ClientModel.getClientModel().getRosterModel().resetSessionTimeout();
									resourceManager.getSharingController().showFileSelector(null);
					    		}
							});
				}//end of if active presenter
			    else
			    {
			    	Window.alert("sorry nothing to do here");
			    }
			}
		};
			
	}
	public	ResourceManager	getResourceManager()
	{
		return	this.resourceManager;
	}
	public	ResourceList	getResourceList()
	{
		return	this.resourceList;
	}
	public	ListPanel	getResourceListPanel()
	{
		return	this.listPanel;
	}
	public ClickListener getShareLinkClickListener() {
		return shareLinkClickListener;
	}
	
//	public Label getLhsLink()
//	{
//		 return listContainer.getLhsLink();
//	}
//	public Label getRhsLink()
//	{
//		return listContainer.getRhsLink();
//	}
}
