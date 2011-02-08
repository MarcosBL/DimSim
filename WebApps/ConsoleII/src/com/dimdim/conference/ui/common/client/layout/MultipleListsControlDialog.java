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

package com.dimdim.conference.ui.common.client.layout;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListControlPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MultipleListsControlDialog	extends	CommonModalDialog implements ClickListener
{
	protected	Vector	lists;
	
	protected	Button		applyButton;
	protected	Button		okButton;
	protected	Vector		memberControlPanels;
	
	public	MultipleListsControlDialog(String title)
	{
		super(title);
		this.lists = new Vector();
		this.memberControlPanels = new Vector();
	}
	public	void	addList(DefaultList list)
	{
		this.lists.addElement(list);
	}
	protected	Widget	getContent()
	{
		int size = lists.size();
		TabPanel tabPanel = new TabPanel();
		for (int i=0; i<size; i++)
		{
			DefaultList list = (DefaultList)lists.elementAt(i);
			ListControlPanel w = list.getListControlPanel();
			if (w != null)
			{
				this.memberControlPanels.addElement(w);
				tabPanel.add(w,w.getPanelLabel());
			}
		}
		tabPanel.selectTab(0);
		
		return	tabPanel;
	}
	protected	Label	createLabel(String labelText, String styleName)
	{
		Label html = new Label(labelText);
		html.setStyleName("common-table-header");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Label	createTextHTML(String text, String styleName)
	{
		Label html = new Label(text);
		html.setStyleName("common-table-text");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		
//		applyButton = new Button();
//		applyButton.setText(UIStrings.getApplyLabel());
//		applyButton.setStyleName("dm-popup-close-button");
//		applyButton.addClickListener(this);
//		v.addElement(applyButton);
		
		okButton = new Button();
		okButton.setText(UIStrings.getOKLabel());
		okButton.setStyleName("dm-popup-close-button");
		okButton.addClickListener(this);
		v.addElement(okButton);
		
		return	v;
	}
	public void onClick(Widget sender)
	{
		if (sender == this.applyButton || sender == this.okButton)
		{
			int	size = this.memberControlPanels.size();
			for (int i=0; i<size; i++)
			{
				((ListControlPanel)this.memberControlPanels.elementAt(i)).applyChanges();
			}
			if (sender == this.okButton)
			{
				hide();
			}
		}
	}
}
