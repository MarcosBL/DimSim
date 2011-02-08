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

package com.dimdim.conference.ui.common.client.list;

import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DefaultListEntryControlsProvider implements ListEntryControlsProvider
{
	protected	ListEntryPanel	listEntryPanel;
	
	UserCallbacks userCallBack = null;
	
	public ClickListener getNameLabelClickListener()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public ListEntryPanel getListEntryPanel()
	{
		return listEntryPanel;
	}
	public void setListEntryPanel(ListEntryPanel listEntryPanel)
	{
		this.listEntryPanel = listEntryPanel;
	}
	public DefaultListEntryControlsProvider()
	{
		
	}
	public ClickListener getImage1ClickListener()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public ClickListener getImage2ClickListener()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public ClickListener getImage3ClickListener()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public ClickListener getImage4ClickListener()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public ClickListener getImage5ClickListener()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public ClickListener getNameLabelMouseListener()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public	ClickListener getStartChatClickListener()
	{
		return null;
	}
	
	public UserCallbacks getUserSignoutListener() {
		return userCallBack;
	}
	public void setUserSignoutListener(UserCallbacks userCallBack) {
		this.userCallBack = userCallBack;
	}
	
}
