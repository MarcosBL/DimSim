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

import	java.util.Vector;
import com.dimdim.conference.ui.model.client.LayoutProperties;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.layout.client.main.NewLayout;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class FullScreenClickListener //implements ClickListener
{
	/*
//	protected	NewLayout	layout;
	protected	WorkspacePanel	workspace;
	protected	Label		label;
	protected	Image		image;
	
	protected	boolean		modeFullScreen;
	
	protected	LayoutProperties	layoutProperties;
	
	private	FullScreenClickListener(WorkspacePanel workspace)
	{
//		this.layout = layout;
		this.workspace = workspace;
		this.modeFullScreen = false;
		this.layoutProperties = LayoutProperties.getLayout(LayoutProperties.CONSOLE_4_LAYOUT);
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
	public boolean isModeFullScreen()
	{
		return modeFullScreen;
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
		//	Set styles.
		Vector divIds = this.layoutProperties.getDivIds();
		int	num = divIds.size();
		for (int i=0; i<num; i++)
		{
			String	divId = (String)divIds.elementAt(i);
			if (this.modeFullScreen)
			{
				//	The ui is currently in full screen view, go to normal view.
				String style = this.layoutProperties.getNormalViewStyle(divId);
				setVisibilityStyle(divId,style);
			}
			else
			{
				//	The ui is currently in normal view, go to full screen view.
				String style = this.layoutProperties.getFullScreenViewStyle(divId);
				setVisibilityStyle(divId,style);
			}
		}
		//	Remember state
		if (this.modeFullScreen)
		{
			//	The ui is currently in full screen view, go to normal view.
			this.modeFullScreen = false;
		}
		else
		{
			//	The ui is currently in normal view, go to full screen view.
			this.modeFullScreen = true;
		}
		this.workspace.containerResized();
	}
	public void setVisibilityStyle(String id, String style)
	{
		try
		{
			RootPanel.get(id).setStyleName(style);
		}
		catch(Exception e)
		{
//			Window.alert("error while adding widget "+e.getMessage());
		}
	}
	*/
}
