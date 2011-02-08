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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ClickListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CommonSubTab	extends	Composite	implements	ClickListener
{
	protected	String	name;
	protected	String	unselectedText;
	protected	String	selectedText;
	protected	boolean	typeComment = false;
	protected	CommonTab	parentTab;
	protected	Label	label = new Label();
	protected	Image	image = null;
	
	public	CommonSubTab(String name, String unselectedText,
				String selectedText, String tooltip, CommonTab parentTab)
	{
		label.setWordWrap(false);
		initWidget(label);
		
		this.name = name;
		this.unselectedText = unselectedText;
		this.selectedText = selectedText;
		this.parentTab = parentTab;
		
		this.label.setText(unselectedText);
		this.label.setTitle(tooltip);
		this.label.addClickListener(this);
	}
	public	Label	getLabel()
	{
		return	label;
	}
	public	Image	getImage()
	{
		return	image;
	}
	public	void	setImageUrl(String url)
	{
		if (image == null)
		{
			image = new Image();
		}
		image.setUrl(url);
	}
	
	public	void	setImage(Image image)
	{
		this.image = image;
	}
	
	public boolean isTypeComment()
	{
		return typeComment;
	}
	public void setTypeComment(boolean typeComment)
	{
		this.typeComment = typeComment;
	}
	public	void	onClick(Widget w)
	{
		//	This is the only way of setting the subtab selected. Preferably
		//	it will be the only public method. Pass on the event to the tab,
		//	which is responsible for managing the tab content panel. The tab
		//	will set this subtab selected and the previous selected sub tab as
		//	unselected.
		if (this.label == w)
		{
			this.parentTab.setSubTabSelected(this);
		}
	}
	public	Widget	getTabContent()
	{
		Widget content = this.parentTab.getTabContentProvider().
			getTabContent(this.parentTab.getName(),this.name,
				this.parentTab.getContentWidth(),this.parentTab.getContentHeight());
		
		return  content;
	}
	protected	void	setSelected(boolean selected)
	{
		if (selected)
		{
			//	Set the styles for the label to selected, get the content from
			//	the content provider and set it in the content panel.
			
			Widget content = this.parentTab.getTabContentProvider().
				getTabContent(this.parentTab.getName(),this.name,
					this.parentTab.getContentWidth(),this.parentTab.getContentHeight());
			this.parentTab.getTabContentPanel().setTabContent(content);
		}
		else
		{
			//	The content panel management is done by the selected sub tab. Here
			//	simply reset the label styles.
		}
	}
}
