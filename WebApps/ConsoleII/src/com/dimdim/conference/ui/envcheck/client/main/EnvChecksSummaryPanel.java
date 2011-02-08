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

package com.dimdim.conference.ui.envcheck.client.main;

import org.gwtwidgets.client.ui.PNGImage;

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EnvChecksSummaryPanel extends Composite
{
	protected	VerticalPanel	basePanel	=	new VerticalPanel();
	
	protected	PNGImage		osCheckSuccess = new PNGImage("images/checkbox-on.png",16,16);
	protected	PNGImage		osCheckFailure = new PNGImage("images/checkbox-off.png",16,16);
	protected	Label			osCheckComment = new Label();
	protected	HorizontalPanel	osCheckPanel = new HorizontalPanel();
	
	protected	PNGImage		browserTypeCheckSuccess = new PNGImage("images/checkbox-on.png",16,16);
	protected	PNGImage		browserTypeCheckFailure = new PNGImage("images/checkbox-off.png",16,16);
	protected	Label			browserTypeCheckComment = new Label();
	protected	HorizontalPanel	browserTypeCheckPanel = new HorizontalPanel();
	
	protected	PNGImage		flashCheckSuccess = new PNGImage("images/checkbox-on.png",16,16);
	protected	PNGImage		flashCheckFailure = new PNGImage("images/checkbox-off.png",16,16);
	protected	Label			flashCheckComment = new Label();
	protected	HorizontalPanel	flashCheckPanel = new HorizontalPanel();
	
	protected	PNGImage		bandwidthCheckSuccess = new PNGImage("images/checkbox-on.png",16,16);
	protected	PNGImage		bandwidthCheckFailure = new PNGImage("images/checkbox-off.png",16,16);
	protected	Label			bandwidthCheckComment = new Label();
	protected	HorizontalPanel	bandwidthCheckPanel = new HorizontalPanel();
	
	protected	PNGImage		publisherCheckSuccess = new PNGImage("images/checkbox-on.png",16,16);
	protected	PNGImage		publisherCheckFailure = new PNGImage("images/checkbox-off.png",16,16);
	protected	Label			publisherCheckComment = new Label();
	protected	HorizontalPanel	publisherCheckPanel = new HorizontalPanel();
	
	public	EnvChecksSummaryPanel()
	{
		initWidget(this.basePanel);
		
		String osCheckCommentText = EnvGlobals.getDisplayString("oscheck.label","OS Check");
		this.prepareCheckResultPanel(this.osCheckPanel,this.osCheckSuccess,
					this.osCheckFailure,this.osCheckComment,osCheckCommentText);
		
		String browserTypeCheckCommentText = EnvGlobals.getDisplayString("browsercheck.label","Browser Type Check");
		this.prepareCheckResultPanel(this.browserTypeCheckPanel,this.browserTypeCheckSuccess,
					this.browserTypeCheckFailure,this.browserTypeCheckComment,browserTypeCheckCommentText);
		
		String flashCheckCommentText = EnvGlobals.getDisplayString("flashcheck.label","Flash Check");
		this.prepareCheckResultPanel(this.flashCheckPanel,this.flashCheckSuccess,
					this.flashCheckFailure,this.flashCheckComment,flashCheckCommentText);
		
		String bandwidthCheckCommentText = EnvGlobals.getDisplayString("bwcheck.label","Bandwidth Check");
		this.prepareCheckResultPanel(this.bandwidthCheckPanel,this.bandwidthCheckSuccess,
					this.bandwidthCheckFailure,this.bandwidthCheckComment,bandwidthCheckCommentText);
		
		String publisherCheckCommentText = EnvGlobals.getDisplayString("pubcheck.label","Publisher Check");
		this.prepareCheckResultPanel(this.publisherCheckPanel,this.publisherCheckSuccess,
					this.publisherCheckFailure,this.publisherCheckComment,publisherCheckCommentText);
	}
	private	void	prepareCheckResultPanel(HorizontalPanel row,
			PNGImage okImage, PNGImage errorImage, Label commentLabel, String comment)
	{
		row.add(okImage);
		row.setCellHorizontalAlignment(okImage,HorizontalPanel.ALIGN_LEFT);
		row.setCellVerticalAlignment(okImage,VerticalPanel.ALIGN_MIDDLE);
		
		row.add(errorImage);
		row.setCellHorizontalAlignment(errorImage,HorizontalPanel.ALIGN_LEFT);
		row.setCellVerticalAlignment(errorImage,VerticalPanel.ALIGN_MIDDLE);
		
		commentLabel.setText(comment);
		commentLabel.setStyleName("common-text");
		commentLabel.addStyleName("env-check-result-comment");
		row.add(commentLabel);
		row.setCellWidth(commentLabel,"100%");
		row.setCellHorizontalAlignment(commentLabel,HorizontalPanel.ALIGN_LEFT);
		row.setCellVerticalAlignment(commentLabel,VerticalPanel.ALIGN_MIDDLE);
		Window.alert("commentLabel = "+commentLabel);
		this.basePanel.add(row);
		row.setStyleName("env-check-result-row");
		row.setVisible(false);
	}
	public	void	setOSCheckResult(boolean result)
	{
		this.setCheckResultPanel(this.osCheckPanel,this.osCheckSuccess,this.osCheckFailure,result);
	}
	public	void	setBrowserTypeCheckResult(boolean result)
	{
		this.setCheckResultPanel(this.browserTypeCheckPanel,this.browserTypeCheckSuccess,this.browserTypeCheckFailure,result);
	}
	public	void	setFlashCheckResult(boolean result)
	{
		this.setCheckResultPanel(this.flashCheckPanel,this.flashCheckSuccess,this.flashCheckFailure,result);
	}
	public	void	setBandwidthCheckResult(boolean result)
	{
		this.setCheckResultPanel(this.bandwidthCheckPanel,this.bandwidthCheckSuccess,this.bandwidthCheckFailure,result);
	}
	public	void	setPublisherCheckResult(boolean result)
	{
		this.setCheckResultPanel(this.publisherCheckPanel,this.publisherCheckSuccess,this.publisherCheckFailure,result);
	}
	private	void	setCheckResultPanel(HorizontalPanel row,
			PNGImage okImage, PNGImage errorImage, boolean result)
	{
		row.setVisible(true);
		if (result)
		{
			okImage.setVisible(true);
			errorImage.setVisible(false);
		}
		else
		{
			okImage.setVisible(false);
			errorImage.setVisible(true);
		}
	}
}
