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

package com.dimdim.conference.ui.workspacepopout.client;

import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.ResourceModel;
import com.dimdim.conference.ui.model.client.WhiteboardModel;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.PopoutModel;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;

import com.dimdim.conference.ui.panels.client.ChatWidget;
import com.dimdim.conference.ui.panels.client.LayoutController;
import com.dimdim.conference.ui.panels.client.ResourcePlayerAndPublicChatPanelSet;

import com.dimdim.conference.ui.popout.client.PopoutWindow;
import com.dimdim.conference.ui.popout.client.PopoutWindowContent;
import com.dimdim.conference.ui.sharing.client.CollaborationAreaManager;
import com.dimdim.conference.ui.sharing.client.ResourceSharingPanel;

import com.dimdim.ui.common.client.data.UIDataDictionaryManager;
import com.dimdim.ui.common.client.data.UIDataReadingProgressListener;
import com.dimdim.ui.common.client.json.ServerResponse;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WorkspacePopout implements EntryPoint, UIDataReadingProgressListener, LayoutController
{
	
	public void addPanelToLayout(String divId, Widget panel)
	{
//		Window.alert("WorkspacePopout:addPanelToLayout::"+divId+", panel:"+panel);
		try
		{
			if (RootPanel.get(divId) != null)
			{
			RootPanel.get(divId).clear();
			RootPanel.get(divId).add(panel);
			}
		}
		catch(Exception e)
		{
//			Window.alert("error while adding widget "+e.getMessage());
		}
	}
	public void hidePanel(String divId)
	{
//		Window.alert("WorkspacePopout:hidePanel::"+divId);
		try
		{
			//RootPanel.get(id).setVisible(visibility);
			if (RootPanel.get(divId) != null)
			{
			RootPanel.get(divId).setStyleName("Hide");
			if (this.popoutContent != null)
			{
				this.popoutContent.divHidden(divId);
			}
			}
		}
		catch(Exception e)
		{
//			Window.alert("error while adding widget "+e.getMessage());
		}
	}
	public void removePanelFromLayout(String divId, Widget panel)
	{
	}
	public void showPanel(String divId)
	{
//		Window.alert("WorkspacePopout:showPanel::"+divId);
		try
		{
			//RootPanel.get(id).setVisible(visibility);
			if (RootPanel.get(divId) != null)
			{
			RootPanel.get(divId).setStyleName("Show");
			if (this.popoutContent != null)
			{
				this.popoutContent.divShown(divId);
			}
			}
		}
		catch(Exception e)
		{
//			Window.alert("error while adding widget "+e.getMessage());
		}
	}
	protected	PopoutWindow				popoutWindow;
	protected	WorkspacePopoutContentPage	popoutContent;
//	protected	WorkspaceTabPage			workspaceTabPage;
	
//	protected	ChatWidget chatWidget = null;
//	protected	CollaborationAreaManager	collaborationAreaManager;
//	protected	ResourceSharingPanel		resourceSharingPanel;
//	protected	ResourcePlayerAndPublicChatPanelSet	panelSet1;
	
	public void onModuleLoad()
	{
		ConferenceGlobals.setContentWidth( Window.getClientWidth() );
		ConferenceGlobals.setContentHeight( Window.getClientHeight() );
		ConferenceGlobals.conferenceKey = getConfKey();
		
		UIDataDictionaryManager.initManager(getWebAppName(),getConfKey(),getUserType(),
//				new String[]{"dictionary","dictionary","dictionary","dictionary","dictionary"},
//				new String[]{"console","console","console","global_string","session_string"},
//				new String[]{"ui_strings","tooltips","default_layout","emoticons","user_info"+getDataCacheId()});
				new String[]{"combined"},
				new String[]{"session_string"},
				new String[]{"user_info"+getDataCacheId()});
		UIDataDictionaryManager.getManager().readDataDictionaries(this);
	}
	public void dataReadingComplete(ServerResponse data)
	{
//		RootPanel.get().remove(lmm);
//		hidePanel("pre-loader");
//		showPanel("content_body");
		
//		Window.alert("WorkspacePopout:dataReadingComplete:1");
		ConferenceGlobals.readDictionaries();
//		Window.alert("WorkspacePopout:dataReadingComplete:2");
		ConferenceGlobals.init();
//		Window.alert("WorkspacePopout:dataReadingComplete:3");
		popoutContent = new WorkspacePopoutContentPage(this);
		
		this.popoutWindow = new PopoutWindow(PopoutModel.WORKSPACE,PopoutModel.WORKSPACE,popoutContent);
//		Window.alert("WorkspacePopout:dataReadingComplete:4");
		AnalyticsConstants.reportFullScreenStarted();
//		this.showPage();
//		Window.alert("WorkspacePopout:dataReadingComplete:5");
	}
	public	void	showPage()
	{
		hidePanel("pre-loader");
		showPanel("content_body");
	}
//	public void preparePopoutWindowContent()
//	{
//		Window.alert("WorkspacePopout:1");
//		UIRosterEntry currentUser = ClientModel.getClientModel().getRosterModel().getCurrentUser();
//		CollaborationAreaManager.initManager(currentUser, 600, 400, "res_showing");
//		this.collaborationAreaManager = CollaborationAreaManager.getManager();
//		this.resourceSharingPanel = this.collaborationAreaManager.getResourceSharingPanel();
//		
//		chatWidget = new ChatWidget(this);
//		this.addPanelToLayout("collab_area", this.resourceSharingPanel);
//		Window.alert("WorkspacePopout:2");
//		panelSet1 = new ResourcePlayerAndPublicChatPanelSet(chatWidget,this.resourceSharingPanel);
//		panelSet1.setWindowId(PopoutModel.WORKSPACE);
//	}
//	public void resizePopoutWindowContent(int width, int height)
//	{
//		Window.alert("Resizig popout window: width:"+width+", height:"+height);
//		this.workspaceTabPage.resizePopoutWindowContent(width,height);
//	}
//	public Vector getRequiredFeatureIds()
//	{
//		Vector	v = new Vector();
//		v.addElement(RosterModel.ModelFeatureId);
//		v.addElement(ResourceModel.ModelFeatureId);
//		v.addElement(WhiteboardModel.ModelFeatureId);
//		return v;
//	}
//	public	void	initializePanelData(String panelId, String panelData)
//	{
//		Window.alert("WorkspacePopout:initializePanelData::"+panelId+":"+panelData);
//		if (panelId.equals(this.chatWidget.getPopoutSupportingChatPanel().getPanelId()))
//		{
//			PopoutSupportingPanel psp = this.chatWidget.getPopoutSupportingChatPanel();
//			if (psp != null)
//			{
//				psp.readPanelData(panelData);
//			}
//			else
//			{
//				Window.alert("No panel by id:"+panelId);
//			}
//		}
//	}
	private native String getWebAppName() /*-{
	 return ($wnd.web_app_name);
	}-*/;
	private native String getConfKey() /*-{
	 return ($wnd.conf_key);
	}-*/;
	private native String getUserType() /*-{
	 return ($wnd.userType);
	}-*/;
	private native String getDataCacheId() /*-{
		return $wnd.data_cache_id;
	}-*/;
}

