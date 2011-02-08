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

package com.dimdim.conference.ui.common.client.layout;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.resource.PresenterResourceSharingPlayer;
import com.dimdim.conference.ui.common.client.resource.ResourceSharingPlayer;
import com.dimdim.conference.ui.common.client.resource.WhiteboardPlayerFrame;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIWhiteboardControlEvent;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.WhiteboardModel;
import com.dimdim.conference.ui.model.client.WhiteboardModelListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Timer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class is modeled after the gwt tab content. It is used as the tab
 * content panel by the workspace left tab area, which displays the content
 * for the selected tab. This panel knows the supported tabs. Only the tab
 * name provided by this class should be used. Use of unsupported tab names
 * will result in a neutral waiting movie panel.
 * 
 * This panel maintains a scroller which may be used or not depending on the
 * sizes of the containning movie panels.
 */

public class WorkspaceTabPanelContent	//extends	Composite implements WorkspaceTabListener, WhiteboardModelListener
{
	/*
	public	static	final	String	ApplicationTab	=	"application";
	public	static	final	String	UploadTab	=	"upload";
	public	static	final	String	WhiteboardTab	=	"whiteboard";
	public	static	final	String	CollaborationTab	=	"collaboration";
	public	static	final	String	DiscussionTab	=	"discussion";
	public	static	final	String	WorkspaceTab	=	"workspace";
	
	protected	HorizontalPanel	basePanel	=	new	HorizontalPanel();
	protected	UIRosterEntry	me;
	
	protected	String		currentTab = null;
	
	protected	int		width;
	protected	int		height;
	protected	ResourceSharingPlayer	resourceSharingPlayer;
//	protected	WhiteboardPlayerPanel	whiteboardPlayerPanel;
	protected	HorizontalPanel			resourceDisplayPanel;
	protected	WhiteboardPlayerFrame	whiteboardPlayerFrame;
	
	private	WorkspaceTabPanelContent(UIRosterEntry me, int width, int height)
	{
		initWidget(basePanel);
		
		this.me = me;
		this.width = width;
		this.height = height;
		if (UIGlobals.isPresenter(me))
		{
			this.resourceSharingPlayer = new PresenterResourceSharingPlayer(me,width,height);
		}
		else
		{
			this.resourceSharingPlayer = new ResourceSharingPlayer(me,width,height);
		}
		this.whiteboardPlayerFrame = new WhiteboardPlayerFrame(me,width,height);
		
		ClientModel.getClientModel().getWhiteboardModel().addListener(this);
	}
	public HorizontalPanel getResourceDisplayPanel()
	{
		return resourceDisplayPanel;
	}
	public void setResourceDisplayPanel(HorizontalPanel resourceDisplayPanel)
	{
		this.resourceDisplayPanel = resourceDisplayPanel;
	}
	public boolean onBeforeTabSelected(String tabName)
	{
		//	At present there is no specific reason for disallowing tab
		//	change. The presenter and attendee views of the tabs are
		//	controlled so that the all the tabs visible to a user are
		//	all accessible at all times.
		return	true;
	}
	public void onTabSelected(String tabName)
	{
		if (onBeforeTabSelected(tabName) && tabName != null)
		{
			if (currentTab == null || contentChangeRequired(tabName))
			{
				if (currentTab != null)
				{
					//	Give the current tab to do work on close.
					if (currentTab == WorkspaceTabPanelContent.WhiteboardTab)
					{
						this.whiteboardPlayerFrame.onTabChange(false);
					}
					else if (currentTab == WorkspaceTabPanelContent.ApplicationTab ||
							currentTab == WorkspaceTabPanelContent.UploadTab ||
							currentTab == WorkspaceTabPanelContent.CollaborationTab ||
							currentTab == WorkspaceTabPanelContent.WorkspaceTab )
					{
//						this.resourceSharingPlayer.onTabChange(false);
					}
				}
				
				if (currentTab != null)
				{
					basePanel.remove(0);
				}
				currentTab = tabName;
				
				Widget w = getTabContent(currentTab);
				basePanel.add(w);
				if (currentTab == WorkspaceTabPanelContent.WhiteboardTab)
				{
					this.whiteboardPlayerFrame.onTabChange(true);
				}
				else if (currentTab == WorkspaceTabPanelContent.ApplicationTab ||
						currentTab == WorkspaceTabPanelContent.UploadTab ||
						currentTab == WorkspaceTabPanelContent.CollaborationTab ||
						currentTab == WorkspaceTabPanelContent.WorkspaceTab )
				{
//					this.resourceSharingPlayer.onTabChange(true);
				}
			}
		}
	}
	private	boolean	contentChangeRequired(String newTab)
	{
		if ((currentTab == WorkspaceTabPanelContent.ApplicationTab ||
				currentTab == WorkspaceTabPanelContent.UploadTab ||
				currentTab == WorkspaceTabPanelContent.CollaborationTab ||
				currentTab == WorkspaceTabPanelContent.WorkspaceTab) &&
			newTab == WorkspaceTabPanelContent.WhiteboardTab)
		{
			return	true;
		}
		if ((newTab == WorkspaceTabPanelContent.ApplicationTab ||
				newTab == WorkspaceTabPanelContent.UploadTab ||
				newTab == WorkspaceTabPanelContent.CollaborationTab ||
				newTab == WorkspaceTabPanelContent.WorkspaceTab) &&
			currentTab == WorkspaceTabPanelContent.WhiteboardTab)
		{
			return	true;
		}
		return	false;
	}
	public void workspaceResized(int newWorkspaceWidth, int newWorkspaceHeight)
	{
		if (this.width == newWorkspaceWidth && this.height == newWorkspaceHeight)
		{
			return;
		}
		this.width = newWorkspaceWidth;
		this.height = newWorkspaceHeight;
//		Window.alert("Resizing workspace tab content:"+width+"--"+height);
		if (this.whiteboardPlayerFrame != null)
		{
			this.whiteboardPlayerFrame.resizeWidget(newWorkspaceWidth,newWorkspaceHeight);
		}
		
//		if (this.resourceSharingPlayer != null)
//		{
//			this.resourceSharingPlayer.resizeWidget(newWorkspaceWidth,newWorkspaceHeight);
//		}
	}
	protected	Widget	getTabContent(String tabName)
	{
		return	resourceDisplayPanel;
//		if (tabName.equals(WorkspaceTabPanelContent.WhiteboardTab))
//		{
//			return	this.whiteboardPlayerFrame;
//		}
//		else
//		{
//			return	this.resourceSharingPlayer;
//		}
	}
	public ResourceSharingPlayer getResourceSharingPlayer()
	{
		return resourceSharingPlayer;
	}
	public void setResourceSharingPlayer(ResourceSharingPlayer resourceSharingPlayer)
	{
		this.resourceSharingPlayer = resourceSharingPlayer;
	}
	public void onLockWhiteboard(final UIWhiteboardControlEvent event)
	{
		DebugPanel.getDebugPanel().addDebugMessage("Whiteboard locked");
		if (currentTab == WorkspaceTabPanelContent.WhiteboardTab)
		{
			this.whiteboardPlayerFrame.onLockWhiteboard(event);
		}
		else
		{
			this.onTabSelected( WorkspaceTabPanelContent.WhiteboardTab );
			Timer t = new Timer()
			{
				public void run()
				{
					whiteboardPlayerFrame.onLockWhiteboard(event);
				}
			};
			t.schedule(5000);
		}
	}
	public void onUnlockWhiteboard(UIWhiteboardControlEvent event)
	{
		DebugPanel.getDebugPanel().addDebugMessage("Whiteboard unlocked");
		if (currentTab == WorkspaceTabPanelContent.WhiteboardTab)
		{
			this.whiteboardPlayerFrame.onUnlockWhiteboard(event);
		}
		else
		{
			this.onTabSelected( WorkspaceTabPanelContent.WhiteboardTab );
		}
	}
	public void onWhiteboardStarted(UIWhiteboardControlEvent event)
	{
		DebugPanel.getDebugPanel().addDebugMessage("Whiteboard started");
		this.onTabSelected( WorkspaceTabPanelContent.WhiteboardTab );
	}
	public void onWhiteboardStopped(UIWhiteboardControlEvent event)
	{
		DebugPanel.getDebugPanel().addDebugMessage("Whiteboard stopped");
		this.onTabSelected( WorkspaceTabPanelContent.CollaborationTab );
	}
	*/
}

