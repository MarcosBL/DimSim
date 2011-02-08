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

package com.dimdim.conference.ui.envcheck.client.layout;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Composite;
import org.gwtwidgets.client.ui.PNGImage;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SignInBodyRhp extends Composite
{
	protected	VerticalPanel	basePanel	=	new	VerticalPanel();
	
	protected	Label		topFiller;
	protected	PNGImage	centerBand;
	
	public	SignInBodyRhp()
	{
		initWidget(basePanel);
		
		topFiller = new Label(" ");
		topFiller.setHeight("75px");
		basePanel.add(topFiller);
		basePanel.setCellHorizontalAlignment(topFiller,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(topFiller,VerticalPanel.ALIGN_TOP);
		
		centerBand = new PNGImage("images/blue-tile.png",24,161);
		basePanel.add(centerBand);
		basePanel.setCellHorizontalAlignment(centerBand,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(centerBand,VerticalPanel.ALIGN_TOP);
	}
}
