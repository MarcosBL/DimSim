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

package com.dimdim.conference.ui.common.client.util;

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The buttons are being created in the specific class because the 
 */

public class LabelHoverStyler implements MouseListener
{
	protected	String	hoverStyle = "common-list-entry-label-hover";
	protected	String	normalStyle = "common-list-entry-label";
	
	public LabelHoverStyler()
	{
	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		if(sender != null)
		{
			sender.removeStyleName(normalStyle);
			sender.addStyleName(hoverStyle);
		}
	}
	public void onMouseLeave(Widget sender)
	{
		if(sender != null)
		{
			sender.removeStyleName(hoverStyle);
			sender.addStyleName(normalStyle);
		}
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
}
