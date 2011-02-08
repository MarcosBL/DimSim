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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ClickListener;
import org.gwtwidgets.client.ui.PNGImage;

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SignInControls extends Composite implements ClickListener
{
	protected	VerticalPanel	basePanel	=	new	VerticalPanel();
	
	protected	HorizontalPanel	topBar;
	protected	HorizontalPanel	formBody;
	protected	HorizontalPanel	bottomBar;
	
	protected	VerticalPanel	buttonsContainer;
	protected	VerticalPanel	formContainer;
	protected	VerticalPanel	buttonsPanel;
	
	protected	Label		privacyPolicyLink;
	protected	Label		copyrightPolicyLink;
	
	public	SignInControls( )
	{
		initWidget(basePanel);
		
		/**
		 * Assemble the form body top bar
		 */
		topBar = new HorizontalPanel();
		
		PNGImage topLeftCorner = new PNGImage("images/form-area-leftcorner.png",47,47);
		topBar.add(topLeftCorner);
		topBar.setCellHorizontalAlignment(topLeftCorner,HorizontalPanel.ALIGN_LEFT);
		topBar.setCellVerticalAlignment(topLeftCorner,VerticalPanel.ALIGN_TOP);
		
		PNGImage topCenter = new PNGImage("images/formarea-top-back.png",305,47);
		topBar.add(topCenter);
		topBar.setCellHorizontalAlignment(topCenter,HorizontalPanel.ALIGN_LEFT);
		topBar.setCellVerticalAlignment(topCenter,VerticalPanel.ALIGN_TOP);
		
		PNGImage topRightCorner = new PNGImage("images/formarea-right-corner.png",47,47);
		topBar.add(topRightCorner);
		topBar.setCellHorizontalAlignment(topRightCorner,HorizontalPanel.ALIGN_LEFT);
		topBar.setCellVerticalAlignment(topRightCorner,VerticalPanel.ALIGN_TOP);
		
		basePanel.add(topBar);
		basePanel.setCellHorizontalAlignment(topBar,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(topBar,VerticalPanel.ALIGN_TOP);
		
		/**
		 * Assemble the form body
		 */
		formBody = new HorizontalPanel();
		
		PNGImage bodyLeft = new PNGImage("images/formarea-left-tile.png",47,431);
		formBody.add(bodyLeft);
		formBody.setCellHorizontalAlignment(bodyLeft,HorizontalPanel.ALIGN_LEFT);
		formBody.setCellVerticalAlignment(bodyLeft,VerticalPanel.ALIGN_TOP);
		
		formContainer = new VerticalPanel();
		formContainer.setWidth("305px");
		formContainer.setHeight("431px");
		
		buttonsContainer = new VerticalPanel();
		buttonsContainer.setWidth("305px");
		buttonsContainer.setHeight("431px");
		
		buttonsPanel = new VerticalPanel();
		buttonsPanel.setWidth("305px");
		buttonsPanel.setHeight("431px");
		
		formContainer.add(buttonsContainer);
		formContainer.setCellHorizontalAlignment(buttonsContainer,HorizontalPanel.ALIGN_CENTER);
		formContainer.setCellVerticalAlignment(buttonsContainer,VerticalPanel.ALIGN_TOP);
		formContainer.setStyleName("signin-form-body-center");
		
		buttonsContainer.add(buttonsPanel);
		buttonsContainer.setCellHorizontalAlignment(buttonsPanel,HorizontalPanel.ALIGN_CENTER);
		buttonsContainer.setCellVerticalAlignment(buttonsPanel,VerticalPanel.ALIGN_MIDDLE);
		
		formBody.add(formContainer);
		formBody.setCellHorizontalAlignment(formContainer,HorizontalPanel.ALIGN_CENTER);
		formBody.setCellVerticalAlignment(formContainer,VerticalPanel.ALIGN_MIDDLE);
		
		PNGImage bodyRight = new PNGImage("images/formarea-right-tile.png",47,431);
		formBody.add(bodyRight);
		formBody.setCellHorizontalAlignment(bodyRight,HorizontalPanel.ALIGN_LEFT);
		formBody.setCellVerticalAlignment(bodyRight,VerticalPanel.ALIGN_TOP);
		
		basePanel.add(formBody);
		basePanel.setCellHorizontalAlignment(formBody,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(formBody,VerticalPanel.ALIGN_TOP);
		
		/**
		 * Assemble the form bottom bar
		 */
		bottomBar = new HorizontalPanel();
		
		PNGImage bottomLeftCorner = new PNGImage("images/formarea-bot-left-corner.png",47,46);
		bottomBar.add(bottomLeftCorner);
		bottomBar.setCellHorizontalAlignment(bottomLeftCorner,HorizontalPanel.ALIGN_LEFT);
		bottomBar.setCellVerticalAlignment(bottomLeftCorner,VerticalPanel.ALIGN_TOP);
		
		PNGImage bottomCenter = new PNGImage("images/formarea-bottom-tile.png",305,46);
		bottomBar.add(bottomCenter);
		bottomBar.setCellHorizontalAlignment(bottomCenter,HorizontalPanel.ALIGN_LEFT);
		bottomBar.setCellVerticalAlignment(bottomCenter,VerticalPanel.ALIGN_TOP);
		
		PNGImage bottomRightCorner = new PNGImage("images/formarea-bot-right-corner.png",47,46);
		bottomBar.add(bottomRightCorner);
		bottomBar.setCellHorizontalAlignment(bottomRightCorner,HorizontalPanel.ALIGN_LEFT);
		bottomBar.setCellVerticalAlignment(bottomRightCorner,VerticalPanel.ALIGN_TOP);
		
		basePanel.add(bottomBar);
		basePanel.setCellHorizontalAlignment(bottomBar,HorizontalPanel.ALIGN_LEFT);
		basePanel.setCellVerticalAlignment(bottomBar,VerticalPanel.ALIGN_TOP);
		
		/**
		 * Privacy and copyright policy links
		 */
		HorizontalPanel linksPanel = new HorizontalPanel();
		linksPanel.setStyleName("bottom-panel-links");
		
		privacyPolicyLink = new Label(EnvGlobals.getDisplayString("dimdim_privacy_policy_link","dimdim privacy policy"));
		privacyPolicyLink.setStyleName("common-text");
		privacyPolicyLink.addStyleName("common-anchor");
		privacyPolicyLink.addStyleName("bottom-panel-link");
		privacyPolicyLink.addClickListener(this);
		
		copyrightPolicyLink = new Label(EnvGlobals.getDisplayString("dimdim_copyright_policy_link","dimdim trademark and copyright policy"));
		copyrightPolicyLink.setStyleName("common-text");
		copyrightPolicyLink.addStyleName("common-anchor");
		copyrightPolicyLink.addStyleName("bottom-panel-link");
		copyrightPolicyLink.addClickListener(this);
		
		linksPanel.add(privacyPolicyLink);
		linksPanel.setCellHorizontalAlignment(privacyPolicyLink,HorizontalPanel.ALIGN_LEFT);
		linksPanel.setCellVerticalAlignment(privacyPolicyLink,VerticalPanel.ALIGN_MIDDLE);
		
		HTML seperator1 = new HTML("|");
		seperator1.setStyleName("bottom-panel-links-seperator");
		linksPanel.add(seperator1);
		linksPanel.setCellHorizontalAlignment(seperator1,HorizontalPanel.ALIGN_LEFT);
		linksPanel.setCellVerticalAlignment(seperator1,VerticalPanel.ALIGN_MIDDLE);
		
		linksPanel.add(copyrightPolicyLink);
		linksPanel.setCellHorizontalAlignment(copyrightPolicyLink,HorizontalPanel.ALIGN_LEFT);
		linksPanel.setCellVerticalAlignment(copyrightPolicyLink,VerticalPanel.ALIGN_MIDDLE);
		
		basePanel.add(linksPanel);
		basePanel.setCellHorizontalAlignment(linksPanel,HorizontalPanel.ALIGN_CENTER);
		basePanel.setCellVerticalAlignment(linksPanel,VerticalPanel.ALIGN_MIDDLE);
	}
	public	VerticalPanel	getFormContainerPanel()
	{
		return	buttonsContainer;
	}
	public void onClick(Widget sender)
	{
		if (sender == this.privacyPolicyLink)
		{
			this.gotoURL("http://www.dimdim.com/index.php?option=com_wrapper&Itemid=78");
		}
		else if (sender == this.copyrightPolicyLink)
		{
			this.gotoURL("http://www.dimdim.com/index.php?option=com_wrapper&Itemid=77");
		}
	}
	private native void gotoURL(String url) /*-{
		$wnd.loadURL(url);
	}-*/;
}
