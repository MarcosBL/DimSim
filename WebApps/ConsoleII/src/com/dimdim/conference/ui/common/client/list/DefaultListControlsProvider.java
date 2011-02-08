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

import com.dimdim.conference.ui.json.client.UIObject;
import com.google.gwt.user.client.ui.ClickListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DefaultListControlsProvider implements ListControlsProvider
{
	protected	ListPanel	listPanel;
	
	public DefaultListControlsProvider()
	{
		
	}
	public ListPanel getListPanel()
	{
		return listPanel;
	}
	public void setListPanel(ListPanel listPanel)
	{
		this.listPanel = listPanel;
	}
	public ClickListener getFooterLink1ClickListener()
	{
		return null;
	}
	public ClickListener getFooterLink2ClickListener()
	{
		return null;
	}
	public ListEntryControlsProvider getListEntryControlsProvider(UIObject object)
	{
		return	new	DefaultListEntryControlsProvider();
	}
}
