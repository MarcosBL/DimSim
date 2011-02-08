/*
 * Copyright 2006 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.impl.PopupImpl;

/**
 * A panel that can "pop up" over other widgets. It overlays the browser's
 * client area (and any previously-created popups).
 * 
 * <p>
 * <img class='gallery' src='PopupPanel.png'/>
 * </p>
 * 
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.PopupPanelExample}
 * </p>
 */
public class HoverPopupPanel extends PopupPanel
{
	protected	boolean		showDone = false;
	/**
	 * Creates an empty popup panel. A child widget must be added to it before it
	 * is shown.
	 */
	public HoverPopupPanel()
	{
		super();
	}
	
	public	static	Element	flashCallbackElement	=	null;
	
	/**
	 *	These two methods do the essential work that is done by the popup panel show
	 *	and hide without actually destroying the panel. Destruction of the panel
	 *	interfers with the flash event flow.
	 */
	public	void	showHoverPopup()
	{
		if (!showDone)
		{
			super.show();
			showDone = true;
			DOM.removeEventPreview(this);
		}
		else
		{
			this.setVisible(true);
//			DOM.addEventPreview(this);
		}
	}
	public	void	hideHoverPopup()
	{
		if (this.isVisible())
		{
			this.setVisible(false);
//			DOM.removeEventPreview(this);
		}
	}
	/**
	 * Creates an empty popup panel, specifying its "auto-hide" property.
	 * 
	 * @param autoHide <code>true</code> if the popup should be automatically
	 *          hidden when the user clicks outside of it
	 */
	public HoverPopupPanel(boolean autoHide)
	{
		super(autoHide);
	}
	
	public boolean onEventPreview(Event event)
	{
		Element target = DOM.eventGetTarget(event);
		if (HoverPopupPanel.flashCallbackElement != null && target != null &&
			  HoverPopupPanel.flashCallbackElement == target)
		{
			return	false;
		}
		return	super.onEventPreview(event);
	}
}
