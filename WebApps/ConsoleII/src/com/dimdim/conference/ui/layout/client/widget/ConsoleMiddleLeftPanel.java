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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.ui.layout.client.widget;

import asquare.gwt.tk.client.ui.DropDownListener;
import asquare.gwt.tk.client.ui.DropDownPanel;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.DefaultListModelListener;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.layout.client.main.NewLayout;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class ConsoleMiddleLeftPanel implements DropDownListener
{
//	protected final DockPanel basePanel = new DockPanel();
	protected TextBox	search = new TextBox();
	
	protected	UIRosterEntry	currentUser;
	
	protected	ResourceRoster		rl = null;
	protected	UserRoster		ul = null;
	protected	TasksManagementPanel	tmp = null;
	
	///protected	DisclosurePanel	usersDropDown = null;
	//protected	RoundedPanel	usersDropDownRounded = null;
	
	//protected	DisclosurePanel	resourcesDropDown = null;
	//protected	RoundedPanel	resourcesDropDownRounded = null;
	
	//protected	DisclosurePanel	pendingTasksDropDown = null;
	//protected	RoundedPanel	pendingTasksDropDownRounded = null;
	private NewLayout consoleLayout = null;

	
	/**
	 *	The resource and invitation widgets are constructed and added to this
	 *	panel.
	 *	
	 *	@param currentUser
	 */
	
	public ConsoleMiddleLeftPanel(NewLayout consoleLayout, UIRosterEntry currentUser, UserCallbacks ucb)
	{
//		this.initWidget(basePanel);
		//basePanel.setSize("100%", "100%");
		//basePanel.setBorderWidth(2);
		
		//this.setStyleName("console-middle-left-panel");
		//this.setStyleName("console-applist-header-row");
		
		this.currentUser = currentUser;
		this.consoleLayout  = consoleLayout;
		
		ul = new UserRoster(currentUser, ucb);
		rl = new ResourceRoster(currentUser, ucb);
		//some resource call backs are required by the user
		ul.getUserList().setResCallBacks(rl.getResourceManager().getSharingController());
		
		
		//usersDropDown = new DisclosurePanel();
		//final DropDownHeaderPanel dropDownHeaderPanel = new DropDownHeaderPanel(UIStrings.getParticipantsLabel()+" (1)", 
		//		ul.getUsersListPanel().getListBrowseControl(), usersDropDown);
		
		
		//usersDropDown.setHeader(dropDownHeaderPanel);
		//usersDropDown.setStyleName("tk-DropDownPanel");
		
		//usersDropDown.setHeaderText(UIStrings.getParticipantsLabel()+" (1)", false);
		
	
		//usersDropDown.addHeaderControl(ul.getUsersListPanel().getListBrowseControl());
		//ul.getUsersListPanel().getListBrowseControl().setContainerPanel(usersDropDown);
		
		consoleLayout.addWidgetToID("participants_navigation", ul.getUsersListPanel().getListBrowseControl());
		
		ul.getUserList().addListModelListener(new DefaultListModelListener()
		{
			public void listEntryAdded(ListEntry newEntry)
			{
				//usersDropDown.setHeaderText(UIStrings.getParticipantsLabel()+
				//		" ("+(ul.getUserList().getListSize())+")", false);
				//dropDownHeaderPanel.setText(UIStrings.getParticipantsLabel()+" ("+(ul.getUserList().getListSize())+")");
				//DropDownHeaderPanel dropDownHeaderPanel = new DropDownHeaderPanel(UIStrings.getParticipantsLabel()+
				//											" ("+(ul.getUserList().getListSize())+")");
				//usersDropDown.setHeader(dropDownHeaderPanel);
				addParticpantsHeader(UIStrings.getParticipantsLabel()+" ("+(ul.getUserList().getListSize())+")");
			}
			public void listEntryRemoved(ListEntry removedEntry)
			{
				//usersDropDown.setHeaderText(UIStrings.getParticipantsLabel()+
				//		" ("+(ul.getUserList().getListSize())+")", false);
				//dropDownHeaderPanel.setText(UIStrings.getParticipantsLabel()+
				//		" ("+(ul.getUserList().getListSize())+")");
				//usersDropDown.setHeader(dropDownHeaderPanel);
				addParticpantsHeader(UIStrings.getParticipantsLabel()+" ("+(ul.getUserList().getListSize())+")");
			}
		});
		
		consoleLayout.addWidgetToID("leftpod_middle_users", ul.getListPanel());
		//usersDropDown.add(ul);
		//usersDropDown.setOpen(true);
		
		//usersDropDownRounded = new RoundedPanel(usersDropDown);
		//basePanel.add(usersDropDownRounded,DockPanel.NORTH);
		
		//usersDropDownRounded.setSize("100%", "100%");
		//basePanel.setCellHeight(usersDropDownRounded, "100%");
		//basePanel.setCellWidth(usersDropDownRounded, "100%");
		if(null != ul.getLhsLink() && ("true".equalsIgnoreCase(ConferenceGlobals.getShowInviteLinks())) )
		{
			//consoleLayout.addWidgetToID("users_invite", ul.getLhsLink());
			
			FocusPanel fp = new FocusPanel();
			fp.add(ul.getLhsLink());
			fp.setWidth("100%");
			//fp.setStyleName("user-list-hover");
			fp.addClickListener(ul.getListPanelLhsLinkClickListener());
			consoleLayout.addWidgetToID("users_invite", fp);
		}
		else
		{
			consoleLayout.setIDVisibility("users_invite_btn", false);
		}
//		consoleLayout.addWidgetToID("participants_manage",  ul.getRhsLink());
		
		FocusPanel fp1 = new FocusPanel();
		fp1.add(ul.getRhsLink());
		fp1.setWidth("100%");
		//fp.setStyleName("user-list-hover");
		fp1.addClickListener(ul.getListPanelRhsLinkClickListener());
		consoleLayout.addWidgetToID("participants_manage", fp1);
		
//		ul.attachClickListeners();
		
		/*HorizontalPanel h1 = new HorizontalPanel();
		h1.setStyleName("console-middle-left-panel-float");
		h1.add(new HTML("&nbsp;"));
		basePanel.add(h1,DockPanel.NORTH);*/
		
		if(!UIGlobals.isPresenter(currentUser))
		{
			//hide the div of show items
			consoleLayout.setIDVisibility("main_showitems", false);
		}
		
			
			//resourcesDropDown = new DisclosurePanel();
			//resourcesDropDownRounded = new RoundedPanel(resourcesDropDown);
			//basePanel.add(resourcesDropDownRounded,DockPanel.NORTH);
			
			//final DropDownHeaderPanel resDropDownHeaderPanel = new DropDownHeaderPanel(UIStrings.getParticipantsLabel()+" (1)", 
			//		rl.getResourceListPanel().getListBrowseControl(), resourcesDropDown);
			
			//resourcesDropDown.setHeader(resDropDownHeaderPanel);
			//resourcesDropDown.setStyleName("tk-DropDownPanel");
			
			//resourcesDropDown.setHeaderText(UIStrings.getShowItemsLabel(), false);
			//resourcesDropDown.addHeaderControl(rl.getResourceListPanel().getListBrowseControl());
			//rl.getResourceListPanel().getListBrowseControl().setContainerPanel(resourcesDropDown);
			addResourcesHeader(UIStrings.getShowItemsLabel()+" (0)");
			rl.getResourceList().addListModelListener(new DefaultListModelListener()
					{
						public void listEntryAdded(ListEntry newEntry)
						{
							//resDropDownHeaderPanel.setText(UIStrings.getShowItemsLabel()+
							//		" ("+(rl.getResourceList().getListSize())+")");
							addResourcesHeader(UIStrings.getShowItemsLabel()+
									" ("+(rl.getResourceList().getListSize())+")");
						}
						public void listEntryRemoved(ListEntry removedEntry)
						{
							//resDropDownHeaderPanel.setText(UIStrings.getShowItemsLabel()+
							//		" ("+(rl.getResourceList().getListSize())+")");
							addResourcesHeader(UIStrings.getShowItemsLabel()+
									" ("+(rl.getResourceList().getListSize())+")");
						}
					});
			//resourcesDropDown.add(rl);
			//resourcesDropDown.setOpen(true);
			//rl.attachClickListeners();
			
			setCurrentUser(currentUser);
			
			/*HorizontalPanel h2 = new HorizontalPanel();
			h2.setStyleName("console-middle-left-panel-float");
			h2.add(new HTML("&nbsp;"));
			basePanel.add(h2,DockPanel.NORTH);*/
			
			/*tmp = new TasksManagementPanel(currentUser);
			pendingTasksDropDown = new DisclosurePanel();
			pendingTasksDropDownRounded = new RoundedPanel(pendingTasksDropDown);
			basePanel.add(pendingTasksDropDownRounded,DockPanel.NORTH);
			
			final DropDownHeaderPanel taskDropDownHeaderPanel = new DropDownHeaderPanel(UIStrings.getParticipantsLabel()+" (1)", 
					tmp.getListsBrowseControl(), pendingTasksDropDown);
			
			pendingTasksDropDown.setHeader(taskDropDownHeaderPanel);
			pendingTasksDropDown.setStyleName("tk-DropDownPanel");
			
			taskDropDownHeaderPanel.setText(UIStrings.getPendingTasksLabel());
			
			//pendingTasksDropDown.addHeaderControl(tmp.getListsBrowseControl());
			
			tmp.getListsBrowseControl().setContainerPanel(pendingTasksDropDown);
			pendingTasksDropDown.add(tmp);
			pendingTasksDropDown.setOpen(true);
			pendingTasksDropDown.setOpen(false);
			this.pendingTasksDropDownRounded.setVisible(false);*/
			
			if (!UIGlobals.isActivePresenter(currentUser))
			{
				//this.resourcesDropDownRounded.setVisible(false);
				//this.pendingTasksDropDownRounded.setVisible(false);
				consoleLayout.setIDVisibility("main_showitems", false);
			}
			else
			{
				consoleLayout.setIDVisibility("main_showitems", true);
			}
			
			/*tmp.addListModelListenerToAllLists(new DefaultListModelListener()
				{
					public void listEntryAdded(ListEntry newEntry)
					{
						if (tmp.getNumberOfTasks() > 0)
						{
							pendingTasksDropDownRounded.setVisible(true);
							pendingTasksDropDown.setOpen(true);
						}
					}
					public void listEntryRemoved(ListEntry removedEntry)
					{
						if (tmp.getNumberOfTasks() == 0)
						{
							pendingTasksDropDown.setOpen(false);
							pendingTasksDropDownRounded.setVisible(false);
						}
					}
				});
*/		
		
	}

	public void setCurrentUser(UIRosterEntry currentUser) {
		this.currentUser = currentUser;
		rl.setCurrentUser(currentUser);
		
		consoleLayout.addWidgetToID("show_items_navigation", rl.getResourceListPanel().getListBrowseControl());
		
		consoleLayout.addWidgetToID("leftpod_middle_items", rl.getListPanel());
		FocusPanel fp = new FocusPanel();
		fp.add(rl.getLhsLink());
		fp.setWidth("100%");
		//fp.setStyleName("user-list-hover");
		fp.addClickListener(rl.getListPanelLhsLinkClickListener());
		consoleLayout.addWidgetToID("share_button_wrapper", fp);
		
		FocusPanel fp1 = new FocusPanel();
		fp1.add(rl.getRhsLink());
		fp1.setWidth("100%");
		fp1.addClickListener(rl.getListPanelRhsLinkClickListener());
		consoleLayout.addWidgetToID("items_manage", fp1);
		//RootPanel.get("share_wrapper").
		//Element elem = RootPanel.get("share_wrapper").getElement();
		//DOM.setElementProperty(elem, "onClick");
	}
	
	private void addParticpantsHeader(String string) {
		Label lbl = new Label(string);
		lbl.setStyleName("common-text");
		lbl.addStyleName("common-bold-text");
		consoleLayout.addWidgetToID("participants", lbl);
	}
	
	private void addResourcesHeader(String string) {
		Label lbl = new Label(string);
		lbl.setStyleName("common-text");
		lbl.addStyleName("common-bold-text");
		consoleLayout.addWidgetToID("show_items", lbl);
	}
	
	public	ResourceRoster	getResourceRoster()
	{
		return	this.rl;
	}
	public	UserRoster	getUserRoster()
	{
		return	this.ul;
	}
//	private	void	hasLobbyTasks(int numberOfTasks)
//	{
//		this.numLobbyTasks = numberOfTasks;
//		this.hasTasks();
//	}
//	private	void	hasQuestionTasks(int numberOfTasks)
//	{
//		this.numQuestionTasks = numberOfTasks;
//		this.hasTasks();
//	}
//	private	void	hasTasks()
//	{
//		if (UIGlobals.isActivePresenter(this.currentUser))
//		{
//			int	numberOfTasks = this.numLobbyTasks + this.numQuestionTasks;
//			if (numberOfTasks > 0)
//			{
//				this.pendingTasksDropDownRounded.setVisible(true);
//				this.pendingTasksDropDown.setOpen(true);
//				this.pendingTasksDropDown.setHeaderText(UIStrings.getPendingTasksLabel()+" ("+numberOfTasks+")", false);
//			}
//			else
//			{
//				this.pendingTasksDropDownRounded.setVisible(false);
//				this.pendingTasksDropDown.setOpen(false);
//				this.pendingTasksDropDown.setHeaderText(UIStrings.getPendingTasksLabel(), false);
//			}
//		}
//	}
//	public void numberOfResourcesChanged(int numberOfResources)
//	{
//		resourcesDropDown.setHeaderText(UIStrings.getShowItemsLabel()+" ("+numberOfResources+")", false);
//	}
//	public void numberOfUsersChanged(int numberOfUsers)
//	{
//		usersDropDown.setHeaderText(UIStrings.getParticipantsLabel()+" ("+(numberOfUsers+1)+")", false);
//	}
	public void dropDownClosed(DropDownPanel sender)
	{
	}
	public void dropDownOpened(DropDownPanel sender)
	{
//		if (sender == this.sendInvitationDropDown)
//		{
//			this.si.invitationWidgetOpened();
//		}
	}
	
	public ClickListener getShareButtonListener(){
		
		return rl.getListPanelLhsLinkClickListener();
	}
}
