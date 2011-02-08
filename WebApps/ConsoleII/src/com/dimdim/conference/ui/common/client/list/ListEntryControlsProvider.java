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
 * 
 */

public interface ListEntryControlsProvider
{
	
	public	void	setListEntryPanel(ListEntryPanel listEntryPanel);
	
	public	ClickListener	getImage1ClickListener();
	
	public	ClickListener	getNameLabelMouseListener();
	
	public	ClickListener	getNameLabelClickListener();
	
	public	ClickListener	getImage2ClickListener();
	
	public	ClickListener	getImage3ClickListener();
	
	public	ClickListener	getImage4ClickListener();
	
	public	ClickListener	getImage5ClickListener();
	
	public	ClickListener getStartChatClickListener();
	
	public	UserCallbacks getUserSignoutListener();
	
	public	void setUserSignoutListener(UserCallbacks setUserSignoutListener);
	
}
