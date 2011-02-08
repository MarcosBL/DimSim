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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.layout2.client;

import com.dimdim.conference.ui.common.client.list.ListPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CommonRoster
{
	protected	UIRosterEntry		me;
	
	protected	ListPanel		listPanel;
	
	protected	ClickListener	lhsLinkClickListener;
	protected	Label			lhsLink;
	
	protected	ClickListener	rhsLinkClickListener;
	protected	Label			rhsLink;
	
	public	CommonRoster(UIRosterEntry me)
	{
		this.me = me;
	}
	protected	void	createLinks(String lhsLinkText,
			String lhsLinkTooltip, ClickListener lhsLinkClickListener,
			String rhsLinkText,
			String rhsLinkTooltip, ClickListener rhsLinkClickListener)
	{
		this.lhsLinkClickListener = lhsLinkClickListener;
		this.rhsLinkClickListener = rhsLinkClickListener;
		if (lhsLinkText != null)
		{
			lhsLink = new Label(lhsLinkText);
			lhsLink.setWordWrap(false);
			lhsLink.setTitle(lhsLinkTooltip);
		}
		if (rhsLinkText != null)
		{
			rhsLink = new Label(rhsLinkText);
			rhsLink.setWordWrap(false);
			rhsLink.setTitle(rhsLinkTooltip);
		}
	}
	public ListPanel getListPanel()
	{
		return listPanel;
	}
	public Label getLhsLink()
	{
		return lhsLink;
	}
	public Label getRhsLink()
	{
		return rhsLink;
	}
	public ClickListener getLhsLinkClickListener()
	{
		return lhsLinkClickListener;
	}
	public ClickListener getRhsLinkClickListener()
	{
		return rhsLinkClickListener;
	}
}
