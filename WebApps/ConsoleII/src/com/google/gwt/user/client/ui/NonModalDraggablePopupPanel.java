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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

/**
 * A panel that can "pop up" over other widgets. It overlays the browser's
 * client area (and any previously-created popups).
 * 
 * <p>
 * <img class='gallery' src='DmPopupPanel.png'/>
 * </p>
 * 
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.DmPopupPanelExample}
 * </p>
 */
public class NonModalDraggablePopupPanel extends NonModalPopupPanel implements MouseListener
    {

	  protected HTML caption = new HTML();
	  protected	int panelHeight = 0;
	  protected int panelWidth = 0;
	  protected boolean dragging;
	  protected int dragStartX, dragStartY;
	  
  /**
   * Creates an empty popup panel. A child widget must be added to it before it
   * is shown.
   */
  public NonModalDraggablePopupPanel() {
	    caption.addMouseListener(this);
  }

  /**
   * Creates an empty popup panel, specifying its "auto-hide" property.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   */
  public NonModalDraggablePopupPanel(boolean autoHide) {
    super(autoHide);

    caption.addMouseListener(this);
  }
  public	int	getPanelHeight()
  {
	  return	this.panelHeight;
  }
  public	void	setPanelHeight(int panelHeight)
  {
	  this.panelHeight = panelHeight;
  }

  public void onMouseDown(Widget sender, int x, int y) {
	   
	  	dragging = true;
	    DOM.setCapture(caption.getElement());
	    dragStartX = x;
	    dragStartY = y;
	  }

  public void onMouseEnter(Widget sender) {
  }

  public void onMouseLeave(Widget sender) {
  }

  public void onMouseMove(Widget sender, int x, int y) {
    if (dragging) {
      int absX = x + getAbsoluteLeft();
      int absY = y + getAbsoluteTop();
      
      int posX = absX - dragStartX;
      int posY = absY - dragStartY;
      
      if (posX+this.panelWidth > Window.getClientWidth())
      {
    	  posX = Window.getClientWidth() - this.panelWidth;
      }
      if (posY+this.panelHeight > Window.getClientHeight())
      {
    	  posY = Window.getClientHeight() - this.panelHeight;
      }
      setPopupPosition(posX, posY);
    }
  }

  public void onMouseUp(Widget sender, int x, int y) {
    dragging = false;
    DOM.releaseCapture(caption.getElement());
  }

}
