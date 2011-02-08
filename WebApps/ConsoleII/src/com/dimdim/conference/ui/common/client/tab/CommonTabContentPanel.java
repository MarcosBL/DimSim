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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CommonTabContentPanel extends	Composite
{
	protected	String	tabGroup;
	protected	Widget	currentContent;
	protected	VerticalPanel	basePanel = new VerticalPanel();
	
	public	CommonTabContentPanel(String tabGroup)
	{
		initWidget(basePanel);
		this.tabGroup = tabGroup;
	}
	
	public	void	setTabContent(Widget w)
	{
		if (currentContent == null || currentContent != w)
		{
			if (currentContent != null)
			{
				this.basePanel.remove(this.currentContent);
			}
			this.currentContent = w;
			this.basePanel.add(this.currentContent);
			this.basePanel.setCellVerticalAlignment(this.currentContent,VerticalPanel.ALIGN_TOP);
			this.basePanel.setCellHorizontalAlignment(this.currentContent,HorizontalPanel.ALIGN_CENTER);
		}
	}
}
