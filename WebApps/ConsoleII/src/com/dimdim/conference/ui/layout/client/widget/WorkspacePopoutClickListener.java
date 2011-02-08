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

import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.EventsJsonHandler;
import com.dimdim.conference.ui.model.client.PopoutModel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.panels.client.ResourcePlayerAndPublicChatPanelSet;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class WorkspacePopoutClickListener //implements ClickListener
{
	/*
	protected	ResourcePlayerAndPublicChatPanelSet	workspacePage;
	protected	boolean		fullScreenMode;
	protected	Label		label;
	protected	Image		image;
	protected	PopoutModel			popoutModel = ClientModel.getClientModel().getPopoutModel();
	
	private	WorkspacePopoutClickListener(ResourcePlayerAndPublicChatPanelSet workspacePage)
	{
		this.workspacePage = workspacePage;
		this.fullScreenMode = false;
	}
	public Label getLabel()
	{
		return label;
	}
	public void setLabel(Label label)
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
	public void onClick(Widget sender)
	{
		if (UIGlobals.amInLobby())
		{
			//	do nothing. This should never happen as the UI should be covered
			//	with glass panel for when the user is in lobby. However this is
			//	an extra protection.
			return;
		}
		if (this.workspacePage.isWorkspacePopedOut())
		{
			reportCloseCallbackNotRequired();
			this.workspacePage.workspaceContentPoppedIn();
//			Window.alert("Popping in workspace");
			this.popinWorkspace();
			EventsJsonHandler.getHandler().removeEventProxy(this.workspacePage.getWorkspaceEventsProxy());
			this.workspacePage.setWorkspacePopedOut(false);
			if (this.label != null)
			{
				this.label.setText(UIStrings.getPopoutWorkspaceLinkText());
				this.label.setTitle(ConferenceGlobals.getTooltip("popout_link"));
			}
			if (this.image != null)
			{
				this.image.setUrl("images/popout.gif");
			}
		}
		else
		{
//			this.fullScreenMode = 
//			Window.alert("Popping out workspace");
			String url = this.popoutModel.getPopoutWindowUrl(PopoutModel.WORKSPACE,null);
//			Window.alert("1");
			this.popoutWorkspace(url);
			if (label != null)
			{
				label.setText(UIStrings.getPopinWorkspaceLinkText());
				label.setTitle(ConferenceGlobals.getTooltip("popin_link"));
			}
			if (image != null)
			{
				image.setUrl("images/popin.gif");
			}
//			Window.alert("2");
//			Window.alert("3");
		}
	}
	*/
	private	native	void	popoutWorkspace(String url) /*-{
		$wnd.popoutWorkspace(url);
	}-*/;
	private	native	void	reportCloseCallbackNotRequired() /*-{
		$wnd.reportCloseCallbackNotRequiredToPopout();
	}-*/;
	private	native	void	popinWorkspace() /*-{
		$wnd.closeWorkspacePopout();
	}-*/;
}
