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

package com.dimdim.conference.ui.common.client.tab;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The content providers are expected to hold on to the content widget.
 * Resizing of a widget may involve other specific actions and are better
 * taken care of by the widget generator.
 */

public interface CommonTabContentProvider
{
	
	public	Widget	getTabContent(String tabName, String subTabName, int width, int height);
	
	public	void	resizeTabContent(String tabName, String subTabName, int width, int height);
	
//	public	int		getMinimumWidth(String tabName, String subTabName);
	
//	public	int		getMinimumHeight(String tabName, String subTabName);
	
	/**
	 * Following two methods could be used by the content provider for
	 * performance and optimization.
	 */
	
	public	boolean	tabSelected(String tabName, String subTabName);
	
	public	boolean	tabUnselected(String tabName, String subTabName);
	
}

