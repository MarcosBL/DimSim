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

package com.dimdim.conference.ui.panels.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.ImageNTextWidget;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.CoBrowseModel;
import com.dimdim.conference.ui.model.client.EventsJsonHandler;
import com.dimdim.conference.ui.model.client.EventsJsonProxy;
import com.dimdim.conference.ui.model.client.PPTSharingModel;
import com.dimdim.conference.ui.model.client.PopoutModel;
import com.dimdim.conference.ui.model.client.PopoutPanelProxy;
import com.dimdim.conference.ui.model.client.PopoutPanelSet;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.model.client.PopoutWindowProxy;
import com.dimdim.conference.ui.model.client.ResourceModel;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.SharingModel;
import com.dimdim.conference.ui.model.client.WhiteboardModel;
import com.dimdim.conference.ui.model.client.RecordingModel;
import com.dimdim.conference.ui.sharing.client.ResourceSharingPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * As the name suggests this object represents the combination of resource player
 * and chat widget. Main functional support from this object is the popout window
 * communication. This object exists simply because the two panels are moved into
 * popout together always. At present at least.
 */

public class ResourcePlayerAndPublicChatPanelSet implements PopoutPanelSet
{
	
	protected	PopoutModel			popoutModel = ClientModel.getClientModel().getPopoutModel();
//	protected	WorkspacePopoutPanelSet	workspacePopoutPanelSet;
	protected	PopoutWindowProxy	workspacePopoutProxy;
	protected	EventsJsonProxy		workspaceEventsProxy;
//	protected	WorkspacePopoutClickListener	workspacePopoutClickListener;
	protected	boolean				workspacePopedOut = false;
	
	protected	ChatWidget				chatWidget;
	protected	ResourceSharingPanel	resourcePlayer;
	
	protected	Vector	featureModelIds;
	protected	Vector	panelIds;
	
	protected	ImageNTextWidget		label;
	protected	Image		image;
	
	protected	boolean		inConsole = true;
	protected	boolean		inPopout = false;
	protected	boolean		consolePanelPoppedOut = false;
	
	public	ResourcePlayerAndPublicChatPanelSet(ChatWidget chatWidget,
				ResourceSharingPanel resourcePlayer, ImageNTextWidget fullScreenLink)
	{
		this.label = fullScreenLink;
		this.chatWidget = chatWidget;
		this.resourcePlayer = resourcePlayer;
		
//		this.workspacePopoutPanelSet = new WorkspacePopoutPanelSet(this);
//		this.workspacePopoutClickListener = new WorkspacePopoutClickListener(this,null);
		
		this.workspacePopoutProxy = new PopoutWindowProxy(this);
		this.workspaceEventsProxy = new EventsJsonProxy(null,this.workspacePopoutProxy);
	}
	public ImageNTextWidget getLabel()
	{
		return label;
	}
	public void setLabel(ImageNTextWidget label)
	{
		this.label = label;
	}
	public Image getImage()
	{
		return image;
	}
	public void setImage(Image image)
	{
		this.image = image;
	}
	public Vector getFeatureModelIds()
	{
		this.featureModelIds = new Vector();
		this.featureModelIds.addElement(RosterModel.ModelFeatureId);
		this.featureModelIds.addElement(ResourceModel.ModelFeatureId);
		
		if (ClientModel.getClientModel().getWhiteboardModel().getNumberOfObjects() > 0)
		{
			this.featureModelIds.addElement(WhiteboardModel.ModelFeatureId);
		}
		else if (ClientModel.getClientModel().getSharingModel().getNumberOfObjects() > 0)
		{
			this.featureModelIds.addElement(SharingModel.ModelFeatureId);
		}
		else if (ClientModel.getClientModel().getPPTSharingModel().getNumberOfObjects() > 0)
		{
			this.featureModelIds.addElement(PPTSharingModel.ModelFeatureId);
		}
		else if (ClientModel.getClientModel().getCobrowseModel().getNumberOfObjects() > 0)
		{
			this.featureModelIds.addElement(CoBrowseModel.ModelFeatureId);
		}
		
		if (ClientModel.getClientModel().getRecordingModel().getNumberOfObjects() > 0)
		{
			this.featureModelIds.addElement(RecordingModel.ModelFeatureId);
		}
		return	this.featureModelIds;
	}
	public PopoutSupportingPanel getPanel(String panelId)
	{
		if (panelId.equals(this.chatWidget.getPopoutSupportingChatPanel().getPanelId()))
		{
			return	this.chatWidget.getPopoutSupportingChatPanel();
		}
		else if (panelId.equals(this.resourcePlayer.getPanelId()))
		{
			return	this.resourcePlayer;
		}
		return	null;
	}
	public String getPanelSetId()
	{
		return PopoutModel.WORKSPACE;
	}
	public Vector getPanelIds()
	{
		if (this.panelIds == null)
		{
			this.panelIds = new Vector();
			this.panelIds.addElement(this.chatWidget.getPopoutSupportingChatPanel().getPanelId());
			this.panelIds.addElement(this.resourcePlayer.getPanelId());
		}
		return this.panelIds;
	}
	public	void	workspaceContentPoppedOut()
	{
		if (!this.workspacePopedOut)
		{
			this.chatWidget.setChatVisible(false);
			this.resourcePlayer.setVisible(false);
			this.workspacePopedOut = true;
		}
	}
	public	void	workspaceContentPoppedIn()
	{
		if (this.workspacePopedOut)
		{
			this.chatWidget.setChatVisible(true);
			this.resourcePlayer.setVisible(true);
			this.resourcePlayer.resizePanel();
			this.workspacePopedOut = false;
			if (this.label != null)
			{
				this.label.setText(UIStrings.getPopoutWorkspaceLinkText());
			}
			if (image != null)
			{
				image.setUrl("images/popout.gif");
			}
		}
	}
	public void panelSetPopedIn()
	{
//		Window.alert("Workspace popping in");
		this.workspaceContentPoppedIn();
		this.workspacePopoutClosed();
		EventsJsonHandler.getHandler().removeEventProxy(this.getWorkspaceEventsProxy());
		
		this.setWorkspacePopedOut(false);
		if (this.label != null)
		{
			this.label.setText(UIStrings.getPopoutWorkspaceLinkText());
		}
		if (image != null)
		{
			image.setUrl("images/popout.gif");
		}
//		Window.alert("Workspace popin complete");
	}
	public void panelSetPopedOut()
	{
		this.workspaceContentPoppedOut();
	}
	public boolean isInConsole()
	{
		return this.inConsole;
	}
	public boolean isInPopout()
	{
		return this.inPopout;
	}
	public void panelSetInConsole()
	{
		this.inConsole = true;
		this.inPopout = false;
	}
	public void panelSetInPopout()
	{
		this.inConsole = false;
		this.inPopout = true;
	}
	public void setWindowId(String windowId)
	{
		this.panelSetInPopout();
		
		this.resourcePlayer.panelInPopout();
		this.resourcePlayer.setPopoutPanelProxy(
				new PopoutPanelProxy(windowId,this.resourcePlayer,true));
		
		this.chatWidget.getPopoutSupportingChatPanel().panelInPopout();
		this.chatWidget.getPopoutSupportingChatPanel().setPopoutPanelProxy(
				new PopoutPanelProxy(windowId,this.chatWidget.getPopoutSupportingChatPanel(),true));
	}
	private	native	void	workspacePopoutClosed() /*-{
		$wnd.popoutClosed();
	}-*/;
	public void popoutWindowClosed()
	{
		this.panelSetPopedIn();
	}
	public void popoutWindowLoaded()
	{
		getWorkspacePopoutProxy().panelPoppedOut();
//		Window.alert("4");
		EventsJsonHandler.getHandler().addEventProxy(getWorkspaceEventsProxy());
//		Window.alert("Workspace popout complete");
		if (label != null)
		{
			label.setText(UIStrings.getPopinWorkspaceLinkText());
		}
		if (image != null)
		{
			image.setUrl("images/popin.gif");
		}
		workspaceContentPoppedOut();
		setWorkspacePopedOut(true);
	}
	public EventsJsonProxy getWorkspaceEventsProxy()
	{
		return workspaceEventsProxy;
	}
	public void setWorkspaceEventsProxy(EventsJsonProxy workspaceEventsProxy)
	{
		this.workspaceEventsProxy = workspaceEventsProxy;
	}
	public boolean isWorkspacePopedOut()
	{
		return workspacePopedOut;
	}
	public void setWorkspacePopedOut(boolean workspacePopedOut)
	{
		this.workspacePopedOut = workspacePopedOut;
	}
	public PopoutWindowProxy getWorkspacePopoutProxy()
	{
		return workspacePopoutProxy;
	}
	public void setWorkspacePopoutProxy(PopoutWindowProxy workspacePopoutProxy)
	{
		this.workspacePopoutProxy = workspacePopoutProxy;
	}
}
