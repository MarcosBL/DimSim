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

import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ClickListener;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.model.client.UIListModel;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.UIListEntry;
import com.dimdim.conference.ui.model.client.UIListAndEntryProperties;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ListPanelsContainer extends Composite
{
	protected	ListPanel			listPanel1;
	protected	ListPanel			listPanel2;
	protected	ListPanel			listPanel3;
	
	protected	String			lhsLinkText;
	protected	ClickListener	lhsLinkClickLstener;
	protected	Label			lhsLink;
	
	protected	String			rhsLinkText;
	protected	ClickListener	rhsLinkClickLstener;
	protected	Label			rhsLink;
	
	protected	VerticalPanel	basePanel = new VerticalPanel();
	//protected	DockPanel		footerPanel = new DockPanel();
	
	public	ListPanelsContainer(String lhsLinkText,String lhsLinkTooltip, ClickListener lhsLinkClickListener,
			String rhsLinkText,String rhsLinkTooltip, ClickListener rhsLinkClickListener)
	{
		initWidget(this.basePanel);
		
		this.lhsLinkText = lhsLinkText;
		this.lhsLinkClickLstener = lhsLinkClickListener;
		this.rhsLinkText = rhsLinkText;
		this.rhsLinkClickLstener = rhsLinkClickListener;
		
		//footerPanel  = new DockPanel();
		//footerPanel.setStyleName("list-container-footer");
		if (lhsLinkText != null || rhsLinkText != null)
		{
			if (lhsLinkText != null)
			{
				lhsLink = new Label(lhsLinkText);
				lhsLink.setWordWrap(false);
				//lhsLink.setStyleName("common-text");
				//lhsLink.addStyleName("list-container-footer-link");
				//lhsLink.addStyleName("common-anchor");
				lhsLink.setTitle(lhsLinkTooltip);
				if (lhsLinkClickListener != null)
				{
					lhsLink.addClickListener(lhsLinkClickListener);
				}
				//footerPanel.add(lhsLink,DockPanel.WEST);
			}
			if (rhsLinkText != null)
			{
				rhsLink = new Label(rhsLinkText);
				rhsLink.setWordWrap(false);
				//rhsLink.setStyleName("common-text");
				//rhsLink.addStyleName("list-container-footer-link");
				//rhsLink.addStyleName("common-anchor");
				rhsLink.setTitle(rhsLinkTooltip);
				if (rhsLinkClickListener != null)
				{
					rhsLink.addClickListener(rhsLinkClickListener);
				}
				//footerPanel.add(rhsLink,DockPanel.EAST);
			}
//			HTML seperator = new HTML("&nbsp;");
			//footerPanel.add(seperator,DockPanel.CENTER);
			//footerPanel.setCellWidth(seperator,"100%");
		}
		else
		{
//			HTML seperator = new HTML("&nbsp;");
			//footerPanel.add(seperator,DockPanel.CENTER);
			//footerPanel.setCellWidth(seperator,"100%");
		}
		
		//this.basePanel.add(this.footerPanel);
	}
//	public	void	addListAt(ListPanel listPanel, int index)
//	{
//		this.basePanel.insert(listPanel,index);
//	}
	public	void	addListPanel1(ListPanel listPanel)
	{
		this.listPanel1 = listPanel;
		
		this.basePanel.insert(this.listPanel1,0);
	}
	public	void	addListPanel2(ListPanel listPanel)
	{
		this.listPanel2 = listPanel;
		
		this.basePanel.insert(this.listPanel2,1);
	}
	public	void	addListPanel3(ListPanel listPanel)
	{
		this.listPanel3 = listPanel;
		
		this.basePanel.insert(this.listPanel3,2);
	}
	public Label getLhsLink()
	{
		return lhsLink;
	}
	public Label getRhsLink()
	{
		return rhsLink;
	}
}
