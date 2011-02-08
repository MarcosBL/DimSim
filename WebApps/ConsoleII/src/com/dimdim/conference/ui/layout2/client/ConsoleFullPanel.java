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

package com.dimdim.conference.ui.layout2.client;

import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This is a logical panel that simply represents all the gwt elements in the
 * console together. This is not a single panel. This constructor creates all
 * the individual top level gwt panels and adds them to appropriate divisions
 * in the jsp layout.
 * 
 * Major panels in this layout are:
 * 
 * 1. Middle panel which contains the participants, resources, workspace and
 * 	  chat panels.
 * 2. Tools panel, which on click will show various options such as settings,
 * 	  about, info etc.
 * 3. Signout panel - this is simply the signout link.
 * 4. Top panel - this is another logical panel, which will add the name, dial-
 * 	  in information and notifications link.
 */

public class ConsoleFullPanel
{
//	private MainLayout consoleLayout;
//	private UIRosterEntry currentUser;
	
	protected	NewMiddlePanel	nmp = null;
	protected	NewTopPanel		ntp = null;
	
	public ConsoleFullPanel(MainLayout consoleLayout, UIRosterEntry currentUser)
	{
//		this.consoleLayout = consoleLayout;
//		this.currentUser = currentUser;
		
		ntp = new NewTopPanel(consoleLayout, currentUser);
		nmp = new NewMiddlePanel(consoleLayout, currentUser);
		ntp.setResRoster(nmp.getLeftPanel().getResourceRoster());
		ntp.setShareClickListener(nmp.getLeftPanel().getShareButtonListener());
	}
	public void resizePanel(int width, int height)
	{
		nmp.resizePanel(width, height);
	}
	public	void	divShown(String divId)
	{
		if (this.nmp != null)
		{
			this.nmp.divShown(divId);
		}
	}
	public	void	divHidden(String divId)
	{
		if (this.nmp != null)
		{
			this.nmp.divHidden(divId);
		}
	}
	public NewMiddlePanel getMiddlePanel()
	{
		return nmp;
	}
	public NewTopPanel getTopPanel()
	{
		return ntp;
	}
}
