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

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.NonModalPopupPanel;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;

import org.gwtwidgets.client.ui.PNGImage;

/**
 * Presence of the flash movie in the hover popup enforces a certain pattern.
 * Basically the movie tag must not be rewritten during the lifetime of the
 * movie, which is determined by the speaker. Hence this listener constructs
 * the popup panel the first time it is required and thereafter refreshes it
 * from the controls and content produced by the list entry manager. This should
 * be simple add and remove from a set of container panels for each control or
 * set of controls such as header buttons.
 * 
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */
public abstract class ListEntryPanelMouseListener implements	MouseListener
{
//	protected	static	PopupPanel	listEntryFocusPopup = null;
	
	//	This panel is required simply to get its current width and height so that
	//	the hover popup can be displayed at proper position.
	
    protected	ListEntry					listEntry;
//	protected	ListEntryPropertiesProvider	listEntryPropertiesProvider;
//	protected	ListEntryControlsProvider	listEntryControlsProvider;
	
	protected	NonModalPopupPanel		theHoverPopup = null;
//	protected	boolean		secondLevelPopup = false;
	
	public ListEntryPanelMouseListener(ListEntry listEntry)
//			ListEntryPropertiesProvider listEntryPropertiesProvider,
//			ListEntryControlsProvider listEntryControlsProvider)
	{
		this.listEntry = listEntry;
//		this.listEntryPropertiesProvider = listEntryPropertiesProvider;
//		this.listEntryControlsProvider = listEntryControlsProvider;
	}
	public	void	setSecondLevelPopup(boolean b)
	{
//		this.secondLevelPopup = b;
	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		try
		{
			UIGlobals.closePreviousHoverPopup();
				this.createPopupPanel();
			int left = sender.getAbsoluteLeft()+UIGlobals.getHoverPopupWidthClearance();
			int top = sender.getAbsoluteTop()-UIGlobals.getHoverPopupHeightClearance();
			
			UIGlobals.setLastPopupPosition(left,top);
			this.theHoverPopup.setPopupPosition(left, top);
//			UIGlobals.showHoverPopup(this.theHoverPopup);
			
			((ListEntryHoverPopupPanel)theHoverPopup).popupVisible();
			
			
//			this.theHoverPopup.popupVisible();
//			Window.alert("8");
//			UIGlobals.consoleMiddleLeftHoverPopup = this.theHoverPopup;
//			this.theHoverPopup.showHoverPopup();
//			Window.alert("9");
//			this.theHoverPopup.setVisible(true);
			
		}
		catch(Exception e)
		{
//			Window.alert(e.getMessage());
		}
	}
	public void onMouseLeave(Widget sender)
	{
//		UIGlobals.closePreviousHoverPopup();
		if (UIGlobals.theDialogBox != null)
		{
//			if (!this.secondLevelPopup)
//				return;
		}
		if (this.theHoverPopup != null)
		{
//			((UIListEntryFocusPopupPanel)this.theHoverPopup).closePopup();
		}
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
	protected	abstract	void	createPopupPanel();
}
