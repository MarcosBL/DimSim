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

package com.dimdim.conference.ui.layout2.client;

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.common.client.list.SortedListPanel;
import com.dimdim.conference.ui.common.client.resource.ResourceList;
import com.dimdim.conference.ui.common.client.resource.ResourceListPropertiesProvider;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.managers.client.resource.PresenterResourceList;
import com.dimdim.conference.ui.managers.client.resource.ResourceListControlsProvider;
import com.dimdim.conference.ui.managers.client.resource.ResourceManager;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.resources.client.EnterURLDialog;
import com.dimdim.conference.ui.resources.client.ResourceControlDialog;
import com.dimdim.conference.ui.resources.client.ResourceTypeList;
import com.dimdim.conference.ui.resources.client.ResourceTypeListPanel;
import com.dimdim.conference.ui.resources.client.ResourceTypePanelsControlsProvider;
import com.dimdim.conference.ui.resources.client.SelectFileDialogue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceRoster extends CommonRoster implements ResourceTypePanelsControlsProvider
{
	protected	ResourceManager			resourceManager;
	protected	ResourceList			resourceList;
	protected	ResourceTypeList		resourceTypeList;
	protected	ResourceTypeListPanel	resourceTypeListPanel;
	
	protected	ResourceListControlsProvider	resourceListControlsProvider;
	protected	ResourceListPropertiesProvider	resourceListPropertiesProvider;
	protected	ClickListener shareLinkClickListener = null;
	protected	ClickListener allLinkClickListener = null;
	
	protected	UserCallbacks ucb = null;
	
	public	ResourceRoster(UIRosterEntry currentUser, UserCallbacks ucb)
	{
		super(currentUser);
		this.ucb = ucb;
		this.resourceManager = new ResourceManager(currentUser, ucb);
		this.resourceListControlsProvider = new ResourceListControlsProvider(currentUser,resourceManager);
		this.resourceListPropertiesProvider = new ResourceListPropertiesProvider();
		this.resourceList = new PresenterResourceList(this.resourceListControlsProvider,
				this.resourceListPropertiesProvider,this.resourceManager);
		this.resourceTypeList = new ResourceTypeList(this.resourceList);
		
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
		
		this.resourceTypeListPanel = new ResourceTypeListPanel(this.resourceTypeList,this);
		
	}
	public	void setCurrentUser(UIRosterEntry currentUser)
	{
		this.me = currentUser;
	}
	public	ClickListener	getAllLinkClickListener()
	{
		return	this.allLinkClickListener;
	}
	public	ClickListener	getListPanelRhsLinkClickListener()
	{
		return new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
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
	public	ClickListener	getListPanelLhsLinkClickListener()
	{
		return new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
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
				}
			    else
			    {
//			    	Window.alert("sorry nothing to do here");
			    }
			}
		};
	}
	public	ClickListener	getShareCobClickListener()
	{
		return new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
			    if (UIGlobals.isActivePresenter(me))
				{
			    	EnterURLDialog urlDialog = new EnterURLDialog(resourceManager.getSharingController());
					urlDialog.drawDialog();
				}
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
	public ClickListener getShareLinkClickListener()
	{
		return shareLinkClickListener;
	}
	public ResourceTypeListPanel getResourceTypeListPanel()
	{
		return resourceTypeListPanel;
	}
	public	ResourceTypePanelsControlsProvider	getResourceTypePanelsControlsProvider()
	{
		return	this;
	}
//	public ClickListener getImage5ClickListner(String typeName)
//	{
//		Window.alert("Creating popup panel for: "+typeName);
//		ClickListener listener = null;
//		ResourceTypeListEntry typeEntry = this.resourceTypeList.findResourceTypeListEntry(typeName);
//		if (typeEntry != null && typeEntry.getSlot5Image() != null)
//		{
//			listener =	new ClickListener()
//			{
//				public	void	onClick(Widget sender)
//				{
//					int left = sender.getAbsoluteLeft()+20;
//					int top = sender.getAbsoluteTop()+20;
//					ResourceTypeListEntryPopupPanel rtlepp = new
//						ResourceTypeListEntryPopupPanel(resourceList,
//								getResourceTypePanelsControlsProvider());
//					DmGlassPanel2 dgp = new DmGlassPanel2(rtlepp);
//					dgp.show(left, top);
//					rtlepp.popupVisible();
//				}
//			};
//		}
//		return listener;
//	}
	public ClickListener getNameLabelClickListener(final UIResourceObject resource)
	{
		if (resource != null)
		{
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
					resourceManager.getSharingController().toggleSharing(resource);
				}
			};
		}
		else
		{
			return	null;
		}
	}
	public	ClickListener	getTypePopupHeaderClickListener(String typeName)
	{
//		Window.alert("Getting popup header click listener: "+typeName);
		if (typeName.equals(UIConstants.RESOURCE_TYPE_PRESENTATION) ||
				typeName.equals(UIConstants.RESOURCE_TYPE_PDF))
		{
			return	this.shareLinkClickListener;
		}else if (typeName.equals(UIConstants.RESOURCE_TYPE_COBROWSE))
				{
					return	this.getShareCobClickListener();
				}
		else
		{
			return	null;
		}
	}
	public	ClickListener	getTypePopupFooterClickListener(final String  typeName)
	{
//		Window.alert("Getting popup footer click listener: "+typeName);
		if (typeName.equals(UIConstants.RESOURCE_TYPE_PRESENTATION) ||
				typeName.equals(UIConstants.RESOURCE_TYPE_PDF) ||
				typeName.equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			//return	this.allLinkClickListener;
				return new ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						if (resourceList.getListSize() == 0)
						{
							DefaultCommonDialog dlg = DefaultCommonDialog.createDialog("Show Items",
										"There are no items");
							dlg.drawDialog();
						}
						else
						{
							ResourceControlDialog rcd = new ResourceControlDialog(resourceManager,resourceList,me.isHost(), typeName);
							rcd.drawDialog();
						}
					}
				};
			}
		else{
			return null;
		}
	}
}
