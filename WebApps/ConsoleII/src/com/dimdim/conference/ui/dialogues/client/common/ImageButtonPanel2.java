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

package com.dimdim.conference.ui.dialogues.client.common;

import org.gwtwidgets.client.ui.PNGImage;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ImageButtonPanel2 extends Composite
{
	protected	HorizontalPanel	basePanel = new HorizontalPanel();
	protected	HorizontalPanel	labelPanel = new HorizontalPanel();
	
	protected	PNGImage	baseLeftEdge;
	protected	PNGImage	baseRightEdge;
	
	protected	Label	line1;
	protected	Label	line2;
	
	public	ImageButtonPanel2(String label1, String label2, String name)
	{
		initWidget(basePanel);
//        sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.ONLOAD | Event.ONERROR);
		
		this.line1 = new Label(label1);
//		this.line1.addMouseListener(this);
//		this.line1.setStyleName("common-text");
		if (label2 != null)
		{
			this.line2 = new Label(label2);
//			this.line2.addMouseListener(this);
//			this.line2.setStyleName("common-text");
			this.line2.setStyleName("anchor-cursor");
		}
		this.basePanel.setStyleName("wizard-label-base");
		
		baseLeftEdge = new PNGImage("images/assistent/"+name+"-left.png",12,63);
		this.basePanel.add(baseLeftEdge);
		this.basePanel.setCellHorizontalAlignment(baseLeftEdge,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(baseLeftEdge,VerticalPanel.ALIGN_MIDDLE);
		
		this.labelPanel.add(this.line1);
		this.line1.setStyleName("wizard-label");
		this.line1.addStyleName("anchor-cursor");
		this.labelPanel.setCellHorizontalAlignment(this.line1,HorizontalPanel.ALIGN_CENTER);
		this.labelPanel.setCellVerticalAlignment(this.line1,VerticalPanel.ALIGN_MIDDLE);
		
		this.basePanel.add(this.labelPanel);
		//basePanel.setBorderWidth(1);
		this.labelPanel.setStyleName("wizard-label-lanel");
		this.labelPanel.addStyleName(name+"-label-panel");
		//this.labelPanel.setBorderWidth(1);
		//this.labelPanel.setStyleName("red-label-panel");
		this.basePanel.setCellHeight(this.labelPanel,"100%");
		this.basePanel.setCellHorizontalAlignment(this.labelPanel,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(this.labelPanel,VerticalPanel.ALIGN_MIDDLE);
		
		baseRightEdge = new PNGImage("images/assistent/"+name+"-right.png",12,63);
		this.basePanel.add(baseRightEdge);
		this.basePanel.setCellHorizontalAlignment(baseRightEdge,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(baseRightEdge,VerticalPanel.ALIGN_MIDDLE);
	}
	public Label getLine1()
	{
		return	this.line1;
	}
	public void addClickListener(ClickListener clickListener)
	{
		this.line1.addClickListener(clickListener);
		if (this.line2 != null)
		{
			this.line2.addClickListener(clickListener);
		}
	}
	public void onMouseDown(Widget arg0, int arg1, int arg2)
	{
	}
	public void onMouseEnter(Widget label)
	{
	}
	public void onMouseLeave(Widget label)
	{
	}
	public void onMouseMove(Widget arg0, int arg1, int arg2)
	{
	}
	public void onMouseUp(Widget arg0, int arg1, int arg2)
	{
	}
}

