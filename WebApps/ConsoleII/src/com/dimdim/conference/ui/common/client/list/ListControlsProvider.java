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

import com.google.gwt.user.client.ui.ClickListener;
import com.dimdim.conference.ui.json.client.UIObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Each list has at least 1 control that refers to the entire list. This
 * does not include the page navigation or scrolling, but any action that
 * specific list emplementers may want to take.
 */

public interface ListControlsProvider
{
	/**
	 * This click listener is attached to the link at the bottom left
	 * corner of the list panel footer, if used.
	 * 
	 * @return
	 */
//	public	ClickListener	getFooterLink1ClickListener();
	
	/**
	 * This click listener is attached to the link at the bottom right
	 * corner of the list panel footer, if used.
	 * 
	 * @return
	 */
//	public	ClickListener	getFooterLink2ClickListener();
	
	public void setListPanel(ListPanel listPanel);
	
	public	ListEntryControlsProvider	getListEntryControlsProvider(UIObject object);
	
}
